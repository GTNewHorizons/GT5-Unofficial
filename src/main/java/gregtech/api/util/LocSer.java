package gregtech.api.util;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Base64;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.GTMod;
import io.netty.buffer.Unpooled;

/**
 * A data structure that represents an unlocalized message.
 * Can be encoded to / decoded from bytes or base64 for easy network transmission.
 * Use `display` function to get the localized and formatted string.
 * Use `PacketBuffer` rather than `ByteBuf` and `ByteArrayDataInput` which is used by the `Localized`. So we can reuse
 * the serialization utilities in `NetworkUtils`.
 *
 * @see Localized
 * @see ChatComponentLocalized
 */
public class LocSer {

    protected enum LSType {

        FormatString((byte) 0),
        ItemStackName((byte) 1),
        FluidStackName((byte) 2),
        Invalid((byte) 7);

        public final byte value;

        LSType(byte aByte) {
            this.value = aByte;
        }
    }

    public Object key;
    public Object[] args;
    public LSType type;

    public LocSer() {}

    protected LocSer(Object key, LSType type) {
        this.key = key;
        this.args = new Object[0];
        this.type = type;
    }

    /** Localizes a lang key directly */
    public LocSer(String key, Object... args) {
        this.key = key;
        this.args = args;
        this.type = LSType.FormatString;
    }

    public static LocSer itemStackName(@NotNull ItemStack stack) {
        return new LocSer(stack, LSType.ItemStackName);
    }

    public static LocSer fluidStackName(@NotNull FluidStack stack) {
        return new LocSer(stack, LSType.FluidStackName);
    }

    public byte[] encodeToBytes() {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        encode(buffer);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        return data;
    }

    public String encodeToBase64() {
        byte[] data = encodeToBytes();
        return Base64.getEncoder()
            .encodeToString(data);
    }

    public static LocSer decodeFromBytes(byte[] data) {
        LocSer localized = new LocSer();
        localized.decode(new PacketBuffer(Unpooled.wrappedBuffer(data)));
        return localized;
    }

    public static LocSer decodeFromBase64(String base64) {
        byte[] data = Base64.getDecoder()
            .decode(base64);
        return decodeFromBytes(data);
    }

    public void encode(PacketBuffer buffer) {
        if (type == LSType.ItemStackName && key instanceof ItemStack stack) {
            buffer.writeByte(type.value);
            NetworkUtils.writeItemStack(buffer, stack);
        } else if (type == LSType.FluidStackName && key instanceof FluidStack stack) {
            buffer.writeByte(type.value);
            NetworkUtils.writeFluidStack(buffer, stack);
        } else if (type == LSType.FormatString && key instanceof String k) {
            buffer.writeByte(type.value);
            NetworkUtils.writeStringSafe(buffer, k);
            buffer.writeInt(args.length);
            for (Object arg : args) {
                encodeArg(buffer, arg);
            }
        } else {
            buffer.writeByte(LSType.Invalid.value);
            GTMod.GT_FML_LOGGER.error("Attempted to encode invalid key");
        }
    }

    public void decode(PacketBuffer buffer) {
        this.type = LSType.values()[buffer.readByte()];
        if (this.type == LSType.Invalid) {
            return;
        } else if (this.type == LSType.FormatString) {
            this.key = NetworkUtils.readStringSafe(buffer);
        } else if (this.type == LSType.ItemStackName) {
            this.key = NetworkUtils.readItemStack(buffer);
        } else if (this.type == LSType.FluidStackName) {
            this.key = NetworkUtils.readFluidStack(buffer);
        } else {
            throw new IllegalStateException("Unexpected value: " + this.type);
        }

        if (this.type == LSType.FormatString) {
            this.args = new Object[buffer.readInt()];
            for (int i = 0; i < args.length; i++) {
                args[i] = decodeArg(buffer);
            }
        } else {
            this.args = new Object[0];
        }
    }

    /**
     * Something that converts a list of arguments into a list of strings. 99% of the time you'll just want to use
     * {@link #processArgs(Object[])}. The return type is Object[] because
     * {@link StatCollector#translateToLocalFormatted(String, Object...)} has an Object vararg param.
     */
    public interface ArgProcessor {

        Object[] process(Object[] args);
    }

    /**
     * Localizes this object into a string. Most of the time you'll just want to call {@link #toString()}.
     *
     * @see GTUtility#processFormatStacks(String)
     */
    public String localize(ArgProcessor argProcessor) {
        // §s and §t are format stack codes, see processFormatStacks for more info
        if (type == LSType.ItemStackName && key instanceof ItemStack stack) {
            return "§s" + stack.getDisplayName() + "§t";
        } else if (type == LSType.FluidStackName && key instanceof FluidStack stack) {
            return "§s" + stack.getLocalizedName() + "§t";
        } else if (type == LSType.FormatString && key instanceof String k) {
            return "§s" + GTUtility.translate(k, argProcessor.process(args)) + "§t";
        } else {
            return "<localize error>";
        }
    }

    @Override
    public String toString() {
        return localize(LocSer::processArgs);
    }

    public String display() {
        return GTUtility.processFormatStacks(this.toString());
    }

    private static final byte TYPE_INVALID = 0;
    private static final byte TYPE_INT = 1;
    private static final byte TYPE_LONG = 2;
    private static final byte TYPE_FLOAT = 3;
    private static final byte TYPE_DOUBLE = 4;
    private static final byte TYPE_STRING = 5;
    private static final byte TYPE_LOCALIZED = 6;

    private static void encodeArg(PacketBuffer buffer, Object arg) {
        if (arg instanceof Integer i) {
            buffer.writeByte(TYPE_INT);
            buffer.writeInt(i);
            return;
        }

        if (arg instanceof Long l) {
            buffer.writeByte(TYPE_LONG);
            buffer.writeLong(l);
            return;
        }

        if (arg instanceof Float f) {
            buffer.writeByte(TYPE_FLOAT);
            buffer.writeFloat(f);
            return;
        }

        if (arg instanceof Double d) {
            buffer.writeByte(TYPE_DOUBLE);
            buffer.writeDouble(d);
            return;
        }

        if (arg instanceof String s) {
            buffer.writeByte(TYPE_STRING);
            NetworkUtils.writeStringSafe(buffer, s);
            return;
        }

        if (arg instanceof LocSer l) {
            buffer.writeByte(TYPE_LOCALIZED);

            l.encode(buffer);
            return;
        }

        buffer.writeByte(TYPE_INVALID);

        GTMod.GT_FML_LOGGER.error("Attempted to send illegal Localized argument over the network: {}", arg);
        GTMod.GT_FML_LOGGER.error(new Exception());
    }

    private static Object decodeArg(PacketBuffer buffer) {
        switch (buffer.readByte()) {
            case TYPE_INVALID -> {
                return "<invalid value>";
            }
            case TYPE_INT -> {
                return buffer.readInt();
            }
            case TYPE_LONG -> {
                return buffer.readLong();
            }
            case TYPE_FLOAT -> {
                return buffer.readFloat();
            }
            case TYPE_DOUBLE -> {
                return buffer.readDouble();
            }
            case TYPE_STRING -> {
                return NetworkUtils.readStringSafe(buffer);
            }
            case TYPE_LOCALIZED -> {
                LocSer l = new LocSer();
                l.decode(buffer);
                return l;
            }
        }

        return "<error>";
    }

    public static Object[] processArgs(Object[] args) {
        String[] out = new String[args.length];

        for (int idx = 0; idx < args.length; idx++) {
            Object arg = args[idx];

            if (arg instanceof LocSer l) {
                out[idx] = l.localize(LocSer::processArgs);
                continue;
            }

            if (arg instanceof Integer i) {
                out[idx] = formatNumber(i);
                continue;
            }

            if (arg instanceof Long l) {
                out[idx] = formatNumber(l);
                continue;
            }

            if (arg instanceof Float f) {
                out[idx] = formatNumber(f);
                continue;
            }

            if (arg instanceof Double d) {
                out[idx] = formatNumber(d);
                continue;
            }

            out[idx] = arg.toString();
        }

        return out;
    }
}
