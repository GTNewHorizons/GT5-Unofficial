package gregtech.api.util;

import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.GTValues.VOLTAGE_NAMES;
import static gregtech.api.enums.GTValues.VP;

import lombok.Getter;

/**
 * @see gregtech.api.enums.GTValues
 * @see gregtech.api.enums.VoltageIndex
 */
public enum Voltage {

    ULV(0),
    LV(1),
    MV(2),
    HV(3),
    EV(4),
    IV(5),
    LuV(6),
    ZPM(7),
    UV(8),
    UHV(9),
    UEV(10),
    UIV(11),
    UMV(12),
    UXV(13),
    MAX(14),;

    @Getter
    private final int tier;

    Voltage(int tier) {
        this.tier = tier;
    }

    /**
     * @return the voltage of the tier
     */
    public long getVoltage() {
        return V[tier];
    }

    /**
     * @return the practical voltage used in recipes.
     * @see gregtech.api.enums.TierEU#RECIPE_LV
     */
    public long getRecipeVoltage() {
        return VP[tier];
    }

    /**
     * @return the short name of the voltage (e.g., HV, IV, LuV)
     */
    public String getName() {
        return VN[tier];
    }

    /**
     *
     * @return the long name of the voltage (e.g., "Ultra Low Voltage", "ZPM Voltage", "Ultimate Insane Voltage")
     */
    public String getLongName() {
        return VOLTAGE_NAMES[tier];
    }

    /**
     * @return the Minecraft chat format color prefix of the voltage (e.g., §2 (dark green for LV), §2§n (underlined
     *         dark green for UV))
     */
    public String getColorPrefix() {
        return TIER_COLORS[tier];
    }

    private static final String[] STD_MACHINE_NAME_TEMPLATES = { null, // ULV
        "Basic %s", // LV
        "Advanced %s", "Advanced %s II", "Advanced %s III", "Advanced %s IV", "Elite %s", "Elite %s II", "Ultimate %s",
        // UV
        "Epic %s", "Epic %s II", "Epic %s III", "Epic %s IV", // UMV
        null, null, };

    public String getStandardMachineNameInTier(String lowerNameStem, String higherNameStem) {
        return this.compareTo(UV) < 0 ? getStandardMachineNameInTier(lowerNameStem)
            : getStandardMachineNameInTier(higherNameStem);
    }

    /**
     * Get English name of the machine in the tier.
     *
     * @param nameStem the name stem of the machine (e.g., "Assembling Machine")
     * @return the standardized name of the machine in tier (e.g., "Avanced Assembling Machine II", "Epic Assembly
     *         Constructor IV")
     */
    public String getStandardMachineNameInTier(String nameStem) {
        String template = STD_MACHINE_NAME_TEMPLATES[tier];
        return template != null ? String.format(template, nameStem) : getVoltagePrefixedMachineNameInTier(nameStem);
    }

    public String getVoltagePrefixedMachineNameInTier(String nameStem) {
        return getLongName() + " " + nameStem;
    }

}
