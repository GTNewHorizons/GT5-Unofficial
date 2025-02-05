package gregtech.api.interfaces.tileentity;

import java.util.Map;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface IGregTechDeviceInformation {

    /**
     * Is this even a TileEntity which allows GregTech Sensor Kits? I need things like this Function for
     * MetaTileEntities, you MUST check this!!! Do not assume that it's a Information returning Device, when it just
     * implements this Interface.
     */
    boolean isGivingInformation();

    /**
     * Up to 8 Strings can be returned. Note: If you insert "\\\\" in the String it tries to translate separate Parts of
     * the String instead of the String as a whole.
     *
     * @return an Array of Information Strings. Don't return null!
     */
    String[] getInfoData();

    /**
     * Returns a map of key-value pairs containing device information.
     *
     * @return a Map where keys are information categories and values are corresponding details. Don't return null!
     */
    Map<String, String> getInfoMap();
}
