package gregtech.api.multitileentity;

public enum SidedTextureNames {

    Base("base"),
    Left("left"),
    Right("right"),
    Top("top"),
    Bottom("bottom"),
    Back("back"),
    Front("front");

    private final String name;
    public static final SidedTextureNames[] TEXTURES = { Base, Left, Right, Top, Bottom, Back, Front };

    SidedTextureNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
