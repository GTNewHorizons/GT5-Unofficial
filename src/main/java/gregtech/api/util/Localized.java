package gregtech.api.util;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.nio.charset.StandardCharsets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import gregtech.api.enums.ChatMessage;
import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacketChat;
import io.netty.buffer.ByteBuf;

/**
 * A data structure that represents an unlocalized message. This can be sent over the network easily. The use
 * of {@link gregtech.api.net.GTPacketChat} is recommended.
 * 
 * @see GTPacketChat
 * @see ChatMessage
 * @see GTUtility#processFormatStacks(String)
 * @see gregtech.api.util.GTTextBuilder
 */
public class Localized {

    public Object key;
    public Object[] args;
    public EnumChatFormatting baseColour = null;

    public Localized() {

    }

    Localized(Object key, Object[] args) {
        this.key = key;
        this.args = args;
    }

    /** Localizes a lang key directly */
    public Localized(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    /** Localizes a ChatMessage, which may have additional processing on the client */
    public Localized(ChatMessage key, Object... args) {
        this.key = key;
        this.args = args;
    }

    /**
     * Sets the base colour for this entry. Does not clobber the previous style, if the output of
     * {@link #localize(ArgProcessor)} is ran through {@link GTUtility#processFormatStacks(String)}.
     */
    public Localized setBase(EnumChatFormatting base) {
        this.baseColour = base;
        return this;
    }

    private static final byte KEY_CHAT = 0;
    private static final byte KEY_LANG = 1;

    public void encode(ByteBuf buffer) {
        if (key instanceof ChatMessage message) {
            buffer.writeByte(KEY_CHAT);
            buffer.writeInt(message.ordinal());
        } else {
            buffer.writeByte(KEY_LANG);
            encodeString(buffer, (String) key);
        }

        buffer.writeByte(baseColour == null ? -1 : baseColour.ordinal());

        buffer.writeInt(args.length);

        for (Object arg : args) {
            encodeArg(buffer, arg);
        }
    }

    public void decode(ByteArrayDataInput buffer) {
        this.key = switch (buffer.readByte()) {
            case KEY_CHAT -> GTDataUtils.getIndexSafe(ChatMessage.values(), buffer.readInt());
            case KEY_LANG -> decodeString(buffer);
            default -> null;
        };

        baseColour = GTDataUtils.getIndexSafe(EnumChatFormatting.values(), buffer.readByte());

        this.args = new Object[buffer.readInt()];

        for (int i = 0; i < args.length; i++) {
            args[i] = decodeArg(buffer);
        }
    }

    /**
     * Something that converts a list of arguments into a list of strings. 99% of the time you'll just want to use
     * {@link #processArgs(Object[])}. The return type is Object[] because
     * {@link net.minecraft.util.StatCollector#translateToLocalFormatted(String, Object...)} has an Object vararg param.
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
        String colour = baseColour == null ? "" : baseColour.toString();

        // §s and §t are format stack codes, see processFormatStacks for more info
        if (key instanceof ChatMessage message) {
            return "§s" + colour + message.localize(args) + "§t";
        } else {
            return "§s" + colour + GTUtility.translate((String) key, argProcessor.process(args)) + "§t";
        }
    }

    @Override
    public String toString() {
        return localize(Localized::processArgs);
    }

    public void sendChat(EntityPlayer player) {
        if (player instanceof EntityPlayerMP playerMP) {
            GTValues.NW.sendToPlayer(new GTPacketChat(this), playerMP);
        } else {
            GTUtility.sendChatToPlayer(player, this.toString());
        }
    }

    private static final byte TYPE_INVALID = 0;
    private static final byte TYPE_INT = 1;
    private static final byte TYPE_LONG = 2;
    private static final byte TYPE_FLOAT = 3;
    private static final byte TYPE_DOUBLE = 4;
    private static final byte TYPE_STRING = 5;
    private static final byte TYPE_LOCALIZED = 6;

    private static void encodeArg(ByteBuf buffer, Object arg) {
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

            encodeString(buffer, s);
            return;
        }

        if (arg instanceof Localized l) {
            buffer.writeByte(TYPE_LOCALIZED);

            l.encode(buffer);
            return;
        }

        buffer.writeByte(TYPE_INVALID);

        GTMod.GT_FML_LOGGER.error("Attempted to send illegal Localized argument over the network: {}", arg);
        GTMod.GT_FML_LOGGER.error(new Exception());
    }

    private static Object decodeArg(ByteArrayDataInput buffer) {
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
                return decodeString(buffer);
            }
            case TYPE_LOCALIZED -> {
                Localized l = new Localized();
                l.decode(buffer);
                return l;
            }
        }

        return "<error>";
    }

    /** Custom string serialization because ByteBufUtils doesn't work with ByteArrayDataInputs */
    private static void encodeString(ByteBuf buffer, String s) {
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(data.length);
        buffer.writeBytes(data);
    }

    private static @NotNull String decodeString(ByteArrayDataInput buffer) {
        int len = buffer.readInt();

        byte[] data = new byte[len];
        buffer.readFully(data);

        return new String(data, StandardCharsets.UTF_8);
    }

    public static Object[] processArgs(Object[] args) {
        String[] out = new String[args.length];

        for (int idx = 0; idx < args.length; idx++) {
            Object arg = args[idx];

            if (arg instanceof Localized l) {
                out[idx] = l.localize(Localized::processArgs);
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
