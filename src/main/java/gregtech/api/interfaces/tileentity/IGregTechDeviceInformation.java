package gregtech.api.interfaces.tileentity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface IGregTechDeviceInformation {

    /**
     * Is this even a TileEntity which allows GregTech Sensor Kits? I need things like this Function for
     * MetaTileEntities, you MUST check this!!! Do not assume that it's a Information returning Device, when it just
     * implements this Interface.
     */
    default boolean isGivingInformation() {
        return false;
    }

    /**
     * Up to 8 Strings can be returned.
     * <p>
     * Each element is either a plain translation key (decoded via {@link #decode}), or a string encoded via
     * {@link #encode} containing a key and substitution arguments separated by {@code \\}.
     *
     * @return an Array of Information Strings.
     */
    default String[] getInfoData() {
        return GTValues.emptyStringArray;
    }

    default void getExtraInfoData(List<String> info) {}

    /**
     * Returns a map of key-value pairs containing device information.
     *
     * @return a Map where keys are information categories and values are corresponding details.
     */
    default Map<String, String> getInfoMap() {
        return Collections.emptyMap();
    }

    /**
     * Encodes a translation key and its arguments into the wire format used by {@link #getInfoData()}.
     * <p>
     * The result is a single string of the form {@code "key\\arg1\\arg2\\..."} that can be stored in the info-data
     * array and later decoded on the client by {@link #decode}.
     * <p>
     * If no arguments are provided the key is returned as-is, and {@link #decode} will translate it directly.
     */
    static String encode(String key, Object... args) {
        if (args == null || args.length == 0) return key;
        StringBuilder sb = new StringBuilder(key);
        for (Object arg : args) sb.append("\\\\")
            .append(arg);
        return sb.toString();
    }

    /**
     * Converts a string produced by {@link #encode} (or a bare translation key) into an {@link IChatComponent} so
     * the client resolves the translation key in the player's own language.
     * <p>
     * Prefer this over {@link #decode} when sending chat messages from the server.
     */
    static IChatComponent toComponent(String encoded) {
        if (encoded == null) return new ChatComponentTranslation("");
        String[] parts = encoded.split("\\\\\\\\");
        if (parts.length == 1) return new ChatComponentTranslation(parts[0]);
        return new ChatComponentTranslation(parts[0], (Object[]) Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Decodes and translates a string previously produced by {@link #encode} (or a bare translation key).
     * <p>
     * When called on the client side {@link StatCollector} resolves the key in the player's language. When called
     * server-side (e.g. for item tooltips stored in NBT) the server locale is used, but the result is still
     * human-readable rather than a raw encoded string. For server-side chat messages prefer {@link #toComponent}.
     */
    static String decode(String data) {
        if (data == null) return "";
        String[] parts = data.split("\\\\\\\\");
        String translated = StatCollector.translateToLocal(parts[0]);
        if (parts.length == 1) return translated;
        Object[] args = Arrays.copyOfRange(parts, 1, parts.length);
        try {
            return String.format(translated, (Object[]) args);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder(translated);
            for (int i = 1; i < parts.length; i++) sb.append(parts[i]);
            return sb.toString();
        }
    }
}
