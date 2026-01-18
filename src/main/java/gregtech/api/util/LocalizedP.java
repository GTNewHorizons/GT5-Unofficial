package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.GTMod;
import gregtech.api.enums.ChatMessage;
import gregtech.api.net.GTPacketChat;
import io.netty.buffer.Unpooled;

/**
 * A data structure that represents an unlocalized message.
 * Use `PacketBuffer` rather than `ByteBuf` which is used by the `Localized`
 *
 * @see Localized
 * @see GTPacketChat
 * @see ChatMessage
 * @see GTUtility#processFormatStacks(String)
 * @see GTTextBuilder
 */
public class LocalizedP {

    public enum LPType {

        Key((byte) 0),
        ItemStackName((byte) 1),
        FluidStackName((byte) 2),
        Invalid((byte) 7);

        public final byte value;

        LPType(byte aByte) {
            this.value = aByte;
        }
    }

    public Object key;
    public Object[] args;
    public LPType type;

    private LocalizedP() {}

    private LocalizedP(Object key, LPType type) {
        this.key = key;
        this.args = new Object[0];
        this.type = type;
    }

    /** Localizes a lang key directly */
    public LocalizedP(String key, Object... args) {
        this.key = key;
        this.args = args;
        this.type = LPType.Key;
    }

    public static LocalizedP itemStackName(ItemStack stack) {
        return new LocalizedP(stack, LPType.ItemStackName);
    }

    public static LocalizedP fluidStackName(FluidStack stack) {
        return new LocalizedP(stack, LPType.FluidStackName);
    }

    public void encode(PacketBuffer buffer) {
        if (type == LPType.ItemStackName && key instanceof ItemStack stack) {
            buffer.writeByte(type.value);
            NetworkUtils.writeItemStack(buffer, stack);
        } else if (type == LPType.FluidStackName && key instanceof FluidStack stack) {
            buffer.writeByte(type.value);
            NetworkUtils.writeFluidStack(buffer, stack);
        } else if (type == LPType.Key && key instanceof String k) {
            buffer.writeByte(type.value);
            NetworkUtils.writeStringSafe(buffer, k);
            buffer.writeInt(args.length);
            for (Object arg : args) {
                encodeArg(buffer, arg);
            }
        } else {
            buffer.writeByte(LPType.Invalid.value);
            GTMod.GT_FML_LOGGER.error("Attempted to encode invalid key");
        }
    }

    public byte[] encodeToBytes() {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        encode(buffer);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        return data;
    }

    public void decode(PacketBuffer buffer) {
        this.type = LPType.values()[buffer.readByte()];
        if (this.type == LPType.Invalid) {
            return;
        } else if (this.type == LPType.Key) {
            this.key = NetworkUtils.readStringSafe(buffer);
        } else if (this.type == LPType.ItemStackName) {
            this.key = NetworkUtils.readItemStack(buffer);
        } else if (this.type == LPType.FluidStackName) {
            this.key = NetworkUtils.readFluidStack(buffer);
        } else {
            throw new IllegalStateException("Unexpected value: " + this.type);
        }

        if (this.type == LPType.Key) {
            this.args = new Object[buffer.readInt()];
            for (int i = 0; i < args.length; i++) {
                args[i] = decodeArg(buffer);
            }
        } else {
            this.args = new Object[0];
        }
    }

    public static LocalizedP decodeFromBytes(byte[] data) {
        LocalizedP localized = new LocalizedP();
        localized.decode(new PacketBuffer(Unpooled.wrappedBuffer(data)));
        return localized;
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
        if (type == LPType.ItemStackName && key instanceof ItemStack stack) {
            return "§s" + stack.getDisplayName() + "§t";
        } else if (type == LPType.FluidStackName && key instanceof FluidStack stack) {
            return "§s" + stack.getLocalizedName() + "§t";
        } else if (type == LPType.Key && key instanceof String k) {
            return "§s" + GTUtility.translate(k, argProcessor.process(args)) + "§t";
        } else {
            return "<localize error>";
        }
    }

    @Override
    public String toString() {
        return localize(LocalizedP::processArgs);
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

        if (arg instanceof LocalizedP l) {
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
                LocalizedP l = new LocalizedP();
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

            if (arg instanceof LocalizedP l) {
                out[idx] = l.localize(LocalizedP::processArgs);
                continue;
            }

            if (arg instanceof Integer i) {
                out[idx] = GTUtility.formatNumbers(i);
                continue;
            }

            if (arg instanceof Long l) {
                out[idx] = GTUtility.formatNumbers(l);
                continue;
            }

            if (arg instanceof Float f) {
                out[idx] = GTUtility.formatNumbers(f);
                continue;
            }

            if (arg instanceof Double d) {
                out[idx] = GTUtility.formatNumbers(d);
                continue;
            }

            out[idx] = arg.toString();
        }

        return out;
    }
}
