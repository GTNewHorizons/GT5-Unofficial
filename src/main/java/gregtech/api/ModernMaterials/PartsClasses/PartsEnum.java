package gregtech.api.ModernMaterials.PartsClasses;

public enum PartsEnum {
    Ingot("% Ingot"),

    Gear("% Gear"),
    SmallGear("Small % Gear"),

    Plate("% Plate"),
    DoublePlate("Double % Plate"),
    TriplePlate("Triple % Plate"),
    QuadruplePlate("Quadruple % Plate"),
    QuintuplePlate("Quintuple % Plate");

    PartsEnum(final String partName) {
        this.partName = partName;
    }

    public final String partName;
}
