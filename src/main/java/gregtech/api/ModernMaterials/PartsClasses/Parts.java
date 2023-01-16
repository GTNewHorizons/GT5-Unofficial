package gregtech.api.ModernMaterials.PartsClasses;

public enum Parts {

    Ingot("% Ingot"),

    Gear("% Gear"),
    SmallGear("Small % Gear"),

    Plate("% Plate"),
    DoublePlate("Double % Plate"),
    TriplePlate("Triple % Plate"),
    QuadruplePlate("Quadruple % Plate"),
    QuintuplePlate("Quintuple % Plate");

    Parts(final String partName) {
        this.partName = partName;
    }

    private final String partName;

    public String getPartName() {
        return partName;
    }
}
