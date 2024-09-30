package gregtech.api.gui.modularui2;

import static gregtech.api.enums.Mods.GregTech;

import com.cleanroommc.modularui.drawable.UITexture;

/**
 * Holds all the references to GUI textures used within GregTech.
 */
public final class GTGuiTextures {

    public static final class IDs {

        public static final String BACKGROUND_STANDARD = "gregtech:standard_bg";
        public static final String BACKGROUND_BRONZE = "gregtech:bronze_bg";
        public static final String BACKGROUND_STEEL = "gregtech:steel_bg";
        public static final String BACKGROUND_PRIMITIVE = "gregtech:primitive_bg";

        public static final String SLOT_ITEM_STANDARD = "gregtech:slot_item_standard";
        public static final String SLOT_ITEM_BRONZE = "gregtech:slot_item_bronze";
        public static final String SLOT_ITEM_STEEL = "gregtech:slot_item_steel";
        public static final String SLOT_ITEM_PRIMITIVE = "gregtech:slot_item_primitive";

        public static final String SLOT_FLUID_STANDARD = "gregtech:slot_fluid_standard";
        public static final String SLOT_FLUID_BRONZE = "gregtech:slot_fluid_bronze";
        public static final String SLOT_FLUID_STEEL = "gregtech:slot_fluid_steel";
        public static final String SLOT_FLUID_PRIMITIVE = "gregtech:slot_fluid_primitive";

        public static final String BUTTON_STANDARD = "gregtech:button_standard";
        public static final String BUTTON_STANDARD_PRESSED = "gregtech:button_standard_pressed";
    }

    public static void init() {}

    // region background

    public static final UITexture BACKGROUND_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/background/singleblock_default")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(IDs.BACKGROUND_STANDARD)
        .build();
    public static final UITexture BACKGROUND_BRONZE = UITexture.builder()
        .location(GregTech.ID, "gui/background/bronze")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(IDs.BACKGROUND_BRONZE)
        .build();
    public static final UITexture BACKGROUND_STEEL = UITexture.builder()
        .location(GregTech.ID, "gui/background/steel")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(IDs.BACKGROUND_STEEL)
        .build();
    public static final UITexture BACKGROUND_PRIMITIVE = UITexture.builder()
        .location(GregTech.ID, "gui/background/primitive")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(IDs.BACKGROUND_PRIMITIVE)
        .build();

    // endregion background

    // region slot

    public static final UITexture SLOT_ITEM_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_standard")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_ITEM_STANDARD)
        .build();
    public static final UITexture SLOT_ITEM_BRONZE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_bronze")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_ITEM_BRONZE)
        .build();
    public static final UITexture SLOT_ITEM_STEEL = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_steel")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_ITEM_STEEL)
        .build();
    public static final UITexture SLOT_ITEM_PRIMITIVE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_primitive")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_ITEM_PRIMITIVE)
        .build();
    public static final UITexture SLOT_FLUID_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_standard")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_FLUID_STANDARD)
        .build();
    public static final UITexture SLOT_FLUID_BRONZE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_bronze")
        .imageSize(18, 18)
        .adaptable(1)
        .name(IDs.SLOT_FLUID_BRONZE)
        .build();
    public static final UITexture SLOT_FLUID_STEEL = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_steel")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_FLUID_STEEL)
        .build();
    public static final UITexture SLOT_FLUID_PRIMITIVE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_primitive")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.SLOT_FLUID_PRIMITIVE)
        .build();

    // endregion slot

    // region button

    public static final UITexture BUTTON_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/button/standard")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.BUTTON_STANDARD)
        .build();
    public static final UITexture BUTTON_STANDARD_PRESSED = UITexture.builder()
        .location(GregTech.ID, "gui/button/standard_pressed")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(IDs.BUTTON_STANDARD_PRESSED)
        .build();

    // endregion button

    // region button overlay

    public static final UITexture OVERLAY_BUTTON_ALLOW_INPUT = fullImageGT("overlay_button/allow_input");
    public static final UITexture OVERLAY_BUTTON_ALLOW_OUTPUT = fullImageGT("overlay_button/allow_output");
    public static final UITexture OVERLAY_BUTTON_BLOCK_INPUT = fullImageGT("overlay_button/block_input");
    public static final UITexture OVERLAY_BUTTON_BLOCK_OUTPUT = fullImageGT("overlay_button/block_output");
    public static final UITexture OVERLAY_BUTTON_CHECKMARK = fullImageGT("overlay_button/checkmark");
    public static final UITexture OVERLAY_BUTTON_CROSS = fullImageGT("overlay_button/cross");
    public static final UITexture OVERLAY_BUTTON_EXPORT = fullImageGT("overlay_button/export");
    public static final UITexture OVERLAY_BUTTON_HOURGLASS = fullImageGT("overlay_button/hourglass");
    public static final UITexture OVERLAY_BUTTON_IMPORT = fullImageGT("overlay_button/import");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_OFF = fullImageGT("overlay_button/redstone_off");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_ON = fullImageGT("overlay_button/redstone_on");
    public static final UITexture OVERLAY_BUTTON_USE_PROCESSING_STATE = fullImageGT(
        "overlay_button/use_processing_state.png");
    public static final UITexture OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE = fullImageGT(
        "overlay_button/use_inverted_processing_state.png");

    // endregion button overlay

    /**
     * Creates texture of full image. Theme of GUI does not affect how the texture is drawn.
     *
     * @param path Path to the image, excluding gregtech/textures/gui
     * @return Texture of full image
     */
    private static UITexture fullImageGT(String path) {
        return UITexture.fullImage(GregTech.ID, "gui/" + path);
    }

    /**
     * Creates texture of full image. Theme color gets applied to the texture.
     *
     * @param path Path to the image, excluding gregtech/textures/gui
     * @return Texture of full image
     */
    private static UITexture fullImageColorableGT(String path) {
        return UITexture.fullImage(GregTech.ID, "gui/" + path, true);
    }
}
