package gregtech.api.multitileentity;

public enum StatusTextures {
    Active("active", false),
    ActiveWithGlow("active_glow", true),
    Inactive("inactive", false),
    InactiveWithGlow("inactive_glow", true);

    private final String name;
    private final boolean hasGlow;
    public static final StatusTextures[] TEXTURES = { Active, ActiveWithGlow, Inactive, InactiveWithGlow };

    StatusTextures(String name, boolean hasGlow) {
        this.name = name;
        this.hasGlow = hasGlow;
    }

    public String getName() {
        return name;
    }

    public boolean hasGlow() {
        return hasGlow;
    }
}
