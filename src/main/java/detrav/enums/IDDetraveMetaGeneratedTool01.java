package detrav.enums;

public enum IDDetraveMetaGeneratedTool01 {
    ProspectorScannerULV(0),
    ProspectorScannerLV(2),
    ProspectorScannerMV(4),
    ProspectorScannerHV(6),
    ProspectorScannerEV(8),
    ProspectorScannerIV(10),
    ProspectorScannerLuV(12),
    ProspectorScannerZPM(14),
    ProspectorScannerUV(16),
    ProspectorScannerUHV(18),
    ElectricProspectorScannerLuV(100),
    ElectricProspectorScannerZPM(102),
    ElectricProspectorScannerUV(104),
    ElectricProspectorScannerUHV(106),
    ;

    public final int ID;
    private IDDetraveMetaGeneratedTool01(int ID){
        this.ID = ID;
    }
}
