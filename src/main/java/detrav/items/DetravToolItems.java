package detrav.items;

import detrav.enums.IDDetraveMetaGeneratedTool01;
import detrav.items.tools.DetravElectricProspectorItem;
import detrav.items.tools.DetravProspector;
import detrav.items.tools.DetravProspectorItem;
import detrav.items.tools.DetravToolElectricProspector;
import gregtech.api.enums.GTValues;

/**
 * The standalone Prospector's Scanner items: one registered Forge item per tier, with the crafting material encoded
 * in the metadata, so that NEI can tell one material's scanner from another's.
 * <p/>
 * These replace {@code DetravMetaGeneratedTool01}, which held every tier of both families as metadata on one item and
 * kept the material in NBT. The unlocalized names still begin with {@code gt.detrav.metatool.01} because
 * VisualProspecting recognises a scanner in the player's hand by that prefix.
 */
public final class DetravToolItems {

    public static DetravProspectorItem PROSPECTOR_LV;
    public static DetravProspectorItem PROSPECTOR_MV;
    public static DetravProspectorItem PROSPECTOR_HV;
    public static DetravProspectorItem PROSPECTOR_EV;
    public static DetravProspectorItem PROSPECTOR_IV;
    public static DetravElectricProspectorItem ELECTRIC_PROSPECTOR_LUV;
    public static DetravElectricProspectorItem ELECTRIC_PROSPECTOR_ZPM;
    public static DetravElectricProspectorItem ELECTRIC_PROSPECTOR_UV;
    public static DetravElectricProspectorItem ELECTRIC_PROSPECTOR_UHV;

    private DetravToolItems() {}

    /**
     * Must run during pre-init, before the ore dictionary handler declares the materials each scanner can be made
     * from.
     */
    public static void register() {
        PROSPECTOR_LV = prospector(
            "detrav.metatool.01.prospector_lv",
            1,
            IDDetraveMetaGeneratedTool01.ProspectorScannerLV.ID);
        PROSPECTOR_MV = prospector(
            "detrav.metatool.01.prospector_mv",
            2,
            IDDetraveMetaGeneratedTool01.ProspectorScannerMV.ID);
        PROSPECTOR_HV = prospector(
            "detrav.metatool.01.prospector_hv",
            3,
            IDDetraveMetaGeneratedTool01.ProspectorScannerHV.ID);
        PROSPECTOR_EV = prospector(
            "detrav.metatool.01.prospector_ev",
            4,
            IDDetraveMetaGeneratedTool01.ProspectorScannerEV.ID);
        PROSPECTOR_IV = prospector(
            "detrav.metatool.01.prospector_iv",
            5,
            IDDetraveMetaGeneratedTool01.ProspectorScannerIV.ID);

        // The hand scanner stops at IV. Past that the electric ones take over, and a hand scanner at those tiers was
        // strictly worse than the electric one of the same tier.

        ELECTRIC_PROSPECTOR_LUV = electricProspector(
            "detrav.metatool.01.electric_prospector_luv",
            6,
            IDDetraveMetaGeneratedTool01.ElectricProspectorScannerLuV.ID,
            102_400_000L);
        ELECTRIC_PROSPECTOR_ZPM = electricProspector(
            "detrav.metatool.01.electric_prospector_zpm",
            7,
            IDDetraveMetaGeneratedTool01.ElectricProspectorScannerZPM.ID,
            409_600_000L);
        ELECTRIC_PROSPECTOR_UV = electricProspector(
            "detrav.metatool.01.electric_prospector_uv",
            8,
            IDDetraveMetaGeneratedTool01.ElectricProspectorScannerUV.ID,
            1_638_400_000L);
        ELECTRIC_PROSPECTOR_UHV = electricProspector(
            "detrav.metatool.01.electric_prospector_uhv",
            9,
            IDDetraveMetaGeneratedTool01.ElectricProspectorScannerUHV.ID,
            6_553_600_000L);
    }

    private static DetravProspectorItem prospector(String unlocalisedName, int tier, int legacyMeta) {
        return new DetravProspectorItem(
            unlocalisedName,
            new DetravProspector(tier),
            "%material Prospector's Scanner (" + GTValues.VN[tier] + ")",
            legacyMeta);
    }

    private static DetravElectricProspectorItem electricProspector(String unlocalisedName, int tier, int legacyMeta,
        long maxCharge) {
        return new DetravElectricProspectorItem(
            unlocalisedName,
            new DetravToolElectricProspector(tier),
            "%material Electric Prospector's Scanner (" + GTValues.VN[tier] + ")",
            legacyMeta,
            maxCharge,
            GTValues.V[tier],
            tier);
    }
}
