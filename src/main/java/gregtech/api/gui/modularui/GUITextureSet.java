package gregtech.api.gui.modularui;

import java.util.function.Function;

import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.SteamVariant;

/**
 * Set of textures that is commonly used for GUI but can vary depending on "style" of machines, e.g. bronze steam or
 * steel steam. <br>
 * This has builder pattern; Textures you didn't specify will fall back to default ones.
 */
public class GUITextureSet {

    private UITexture mainBackground;
    private UITexture itemSlot;
    private UITexture fluidSlot;
    private UITexture coverTabNormal;
    private UITexture coverTabHighlight;
    private UITexture coverTabDisabled;
    private UITexture coverTabNormalFlipped;
    private UITexture coverTabHighlightFlipped;
    private UITexture coverTabDisabledFlipped;
    private UITexture titleTabNormal;
    private UITexture titleTabDark;
    private UITexture titleTabAngular;
    private UITexture gregtechLogo;

    public static final GUITextureSet DEFAULT = new GUITextureSet()
        .setMainBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT)
        .setItemSlot(GuiTextures.SLOT)
        .setFluidSlot(GuiTextures.SLOT_DARK)
        .setCoverTab(
            GT_UITextures.TAB_COVER_NORMAL,
            GT_UITextures.TAB_COVER_HIGHLIGHT,
            GT_UITextures.TAB_COVER_DISABLED)
        .setTitleTab(GT_UITextures.TAB_TITLE, GT_UITextures.TAB_TITLE_DARK, GT_UITextures.TAB_TITLE_ANGULAR)
        .setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT);

    public static final Function<SteamVariant, GUITextureSet> STEAM = steamVariant -> new GUITextureSet()
        .setMainBackground(GT_UITextures.BACKGROUND_STEAM.get(steamVariant))
        .setItemSlot(GT_UITextures.SLOT_ITEM_STEAM.get(steamVariant))
        .setFluidSlot(GT_UITextures.SLOT_FLUID_STEAM.get(steamVariant))
        .setCoverTab(
            GT_UITextures.TAB_COVER_STEAM_NORMAL.get(steamVariant),
            GT_UITextures.TAB_COVER_STEAM_HIGHLIGHT.get(steamVariant),
            GT_UITextures.TAB_COVER_STEAM_DISABLED.get(steamVariant))
        .setTitleTab(
            GT_UITextures.TAB_TITLE_STEAM.getAdaptable(steamVariant),
            GT_UITextures.TAB_TITLE_DARK_STEAM.getAdaptable(steamVariant),
            GT_UITextures.TAB_TITLE_ANGULAR_STEAM.getAdaptable(steamVariant))
        .setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_STEAM.get(steamVariant));

    public GUITextureSet() {}

    // region setters

    public GUITextureSet setMainBackground(UITexture mainBackground) {
        this.mainBackground = mainBackground;
        return this;
    }

    public GUITextureSet setItemSlot(UITexture itemSlot) {
        this.itemSlot = itemSlot;
        return this;
    }

    public GUITextureSet setFluidSlot(UITexture fluidSlot) {
        this.fluidSlot = fluidSlot;
        return this;
    }

    public GUITextureSet setCoverTab(UITexture coverNormal, UITexture coverHighlight, UITexture coverDisabled) {
        this.coverTabNormal = coverNormal;
        this.coverTabHighlight = coverHighlight;
        this.coverTabDisabled = coverDisabled;
        this.coverTabNormalFlipped = coverNormal.getSubArea(1, 0, 0, 1);
        this.coverTabHighlightFlipped = coverHighlight.getSubArea(1, 0, 0, 1);
        this.coverTabDisabledFlipped = coverDisabled.getSubArea(1, 0, 0, 1);
        return this;
    }

    public GUITextureSet setTitleTab(UITexture titleNormal, UITexture titleDark, UITexture titleTabAngular) {
        this.titleTabNormal = titleNormal;
        this.titleTabDark = titleDark;
        this.titleTabAngular = titleTabAngular;
        return this;
    }

    public GUITextureSet setGregTechLogo(UITexture gregtechLogo) {
        this.gregtechLogo = gregtechLogo;
        return this;
    }

    // endregion

    // region getters

    public UITexture getMainBackground() {
        return mainBackground != null ? mainBackground : DEFAULT.mainBackground;
    }

    public UITexture getItemSlot() {
        return itemSlot != null ? itemSlot : DEFAULT.itemSlot;
    }

    public UITexture getFluidSlot() {
        return fluidSlot != null ? fluidSlot : DEFAULT.fluidSlot;
    }

    public UITexture getCoverTabNormal() {
        return coverTabNormal != null ? coverTabNormal : DEFAULT.coverTabNormal;
    }

    public UITexture getCoverTabHighlight() {
        return coverTabHighlight != null ? coverTabHighlight : DEFAULT.coverTabHighlight;
    }

    public UITexture getCoverTabDisabled() {
        return coverTabDisabled != null ? coverTabDisabled : DEFAULT.coverTabDisabled;
    }

    public UITexture getCoverTabNormalFlipped() {
        return coverTabNormalFlipped != null ? coverTabNormalFlipped : DEFAULT.coverTabNormalFlipped;
    }

    public UITexture getCoverTabHighlightFlipped() {
        return coverTabHighlightFlipped != null ? coverTabHighlightFlipped : DEFAULT.coverTabHighlightFlipped;
    }

    public UITexture getCoverTabDisabledFlipped() {
        return coverTabDisabledFlipped != null ? coverTabDisabledFlipped : DEFAULT.coverTabDisabledFlipped;
    }

    public UITexture getTitleTabNormal() {
        return titleTabNormal != null ? titleTabNormal : DEFAULT.titleTabNormal;
    }

    public UITexture getTitleTabDark() {
        return titleTabDark != null ? titleTabDark : DEFAULT.titleTabDark;
    }

    public UITexture getTitleTabAngular() {
        return titleTabAngular != null ? titleTabAngular : DEFAULT.titleTabAngular;
    }

    public UITexture getGregTechLogo() {
        return gregtechLogo != null ? gregtechLogo : DEFAULT.gregtechLogo;
    }

    // endregion
}
