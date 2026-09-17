package detrav.enums;

/**
 * The metadata every Prospector's Scanner tier held on {@code detrav.metatool.01}. The scanners are their own items
 * now (see {@code detrav.items.DetravToolItems}), but these ids stay reserved: the Postea migration matches old saved
 * stacks by them, and the tiers still carry theirs for the range and success-chance sums that were written in terms
 * of the metadata.
 */
public enum IDDetraveMetaGeneratedTool01 {

    ProspectorScannerLV(2),
    ProspectorScannerMV(4),
    ProspectorScannerHV(6),
    ProspectorScannerEV(8),
    ProspectorScannerIV(10),
    // 12 to 18 were the hand scanners above IV, removed outright. The ids stay reserved: saved stacks in old worlds
    // still carry them, and reusing one would turn those stacks into a different tool.
    ElectricProspectorScannerLuV(100),
    ElectricProspectorScannerZPM(102),
    ElectricProspectorScannerUV(104),
    ElectricProspectorScannerUHV(106),;

    public final int ID;

    IDDetraveMetaGeneratedTool01(int ID) {
        this.ID = ID;
    }
}
