package gtnhintergalactic.client;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.render.TextureFactory;

/**
 * Textures used for MTEs are defined here
 *
 * @author minecraft7771
 */
public class IGTextures implements Runnable {

    public static IIconContainer SIPHON_OVERLAY_FRONT_GLOW;
    public static IIconContainer SIPHON_OVERLAY_FRONT_ACTIVE_GLOW;
    public static IIconContainer SIPHON_OVERLAY_FRONT;

    public static IIconContainer DYSON_OVERLAY_FRONT_GLOW;
    public static IIconContainer DYSON_OVERLAY_FRONT_ACTIVE_GLOW;
    public static IIconContainer DYSON_OVERLAY_FRONT;
    public static IIconContainer DYSON_OVERLAY_FRONT_ACTIVE;

    public static final int CASING_INDEX_RECEIVER = 150;
    public static final int CASING_INDEX_COMMAND = 151;
    public static final int CASING_INDEX_LAUNCH = 152;
    public static final int CASING_INDEX_FLOOR = 153;
    public static final int CASING_INDEX_SIPHON = 154;

    /**
     * Register all used textures
     */
    @Override
    public void run() {
        SIPHON_OVERLAY_FRONT = CustomIcon.create("iconsets/OVERLAY_FRONT_PLANETARYSIPHON");
        SIPHON_OVERLAY_FRONT_GLOW = CustomIcon.create("iconsets/OVERLAY_FRONT_PLANETARYSIPHON_GLOW");
        SIPHON_OVERLAY_FRONT_ACTIVE_GLOW = CustomIcon.create("iconsets/OVERLAY_FRONT_PLANETARYSIPHON_ACTIVE_GLOW");

        DYSON_OVERLAY_FRONT = CustomIcon.create("iconsets/OVERLAY_FRONT_DYSONSPHERE");
        DYSON_OVERLAY_FRONT_ACTIVE = CustomIcon.create("iconsets/OVERLAY_FRONT_DYSONSPHERE_ACTIVE");
        DYSON_OVERLAY_FRONT_GLOW = CustomIcon.create("iconsets/OVERLAY_FRONT_DYSONSPHERE_GLOW");
        DYSON_OVERLAY_FRONT_ACTIVE_GLOW = CustomIcon.create("iconsets/OVERLAY_FRONT_DYSONSPHERE_ACTIVE_GLOW");

        BlockIcons.setCasingTextureForId(CASING_INDEX_RECEIVER, TextureFactory.of(GregTechAPI.sBlockCasingsDyson, 0));
        BlockIcons.setCasingTextureForId(CASING_INDEX_LAUNCH, TextureFactory.of(GregTechAPI.sBlockCasingsDyson, 2));
        BlockIcons.setCasingTextureForId(CASING_INDEX_COMMAND, TextureFactory.of(GregTechAPI.sBlockCasingsDyson, 5));
        BlockIcons.setCasingTextureForId(CASING_INDEX_FLOOR, TextureFactory.of(GregTechAPI.sBlockCasingsDyson, 9));
        BlockIcons.setCasingTextureForId(CASING_INDEX_SIPHON, TextureFactory.of(GregTechAPI.sBlockCasingsSiphon));
    }
}
