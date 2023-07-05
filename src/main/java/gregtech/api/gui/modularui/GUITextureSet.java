package gregtech.api.gui.modularui;

/**
 * Set of textures that is commonly used for GUI but can vary depending on "style" of machines, e.g. bronze steam or
 * steel steam. <br>
 * This has builder pattern; Textures you didn't specify will fall back to default ones.
 */
public class GUITextureSet {

    // private UITexture mainBackground;
    // private UITexture itemSlot;
    // private UITexture fluidSlot;
    // private UITexture coverTabNormal;
    // private UITexture coverTabHighlight;
    // private UITexture coverTabDisabled;
    // private UITexture coverTabNormalFlipped;
    // private UITexture coverTabHighlightFlipped;
    // private UITexture coverTabDisabledFlipped;
    // private AdaptableUITexture titleTabNormal;
    // private AdaptableUITexture titleTabDark;
    // private AdaptableUITexture titleTabAngular;
    // private UITexture gregtechLogo;
    //
    // public static final GUITextureSet DEFAULT = new GUITextureSet()
    // .setMainBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT)
    // .setItemSlot(ModularUITextures.ITEM_SLOT)
    // .setFluidSlot(ModularUITextures.FLUID_SLOT)
    // .setCoverTab(
    // GT_UITextures.TAB_COVER_NORMAL,
    // GT_UITextures.TAB_COVER_HIGHLIGHT,
    // GT_UITextures.TAB_COVER_DISABLED)
    // .setTitleTab(GT_UITextures.TAB_TITLE, GT_UITextures.TAB_TITLE_DARK, GT_UITextures.TAB_TITLE_ANGULAR)
    // .setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT);
    //
    // public static final Function<SteamVariant, GUITextureSet> STEAM = steamVariant -> new GUITextureSet()
    // .setMainBackground(GT_UITextures.BACKGROUND_STEAM.get(steamVariant))
    // .setItemSlot(GT_UITextures.SLOT_ITEM_STEAM.get(steamVariant))
    // .setFluidSlot(GT_UITextures.SLOT_FLUID_STEAM.get(steamVariant))
    // .setCoverTab(
    // GT_UITextures.TAB_COVER_STEAM_NORMAL.get(steamVariant),
    // GT_UITextures.TAB_COVER_STEAM_HIGHLIGHT.get(steamVariant),
    // GT_UITextures.TAB_COVER_STEAM_DISABLED.get(steamVariant))
    // .setTitleTab(
    // GT_UITextures.TAB_TITLE_STEAM.getAdaptable(steamVariant),
    // GT_UITextures.TAB_TITLE_DARK_STEAM.getAdaptable(steamVariant),
    // GT_UITextures.TAB_TITLE_ANGULAR_STEAM.getAdaptable(steamVariant))
    // .setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_STEAM.get(steamVariant));
    //
    // public GUITextureSet() {}
    //
    // // region setters
    //
    // public GUITextureSet setMainBackground(UITexture mainBackground) {
    // this.mainBackground = mainBackground;
    // return this;
    // }
    //
    // public GUITextureSet setItemSlot(UITexture itemSlot) {
    // this.itemSlot = itemSlot;
    // return this;
    // }
    //
    // public GUITextureSet setFluidSlot(UITexture fluidSlot) {
    // this.fluidSlot = fluidSlot;
    // return this;
    // }
    //
    // public GUITextureSet setCoverTab(UITexture coverNormal, UITexture coverHighlight, UITexture coverDisabled) {
    // this.coverTabNormal = coverNormal;
    // this.coverTabHighlight = coverHighlight;
    // this.coverTabDisabled = coverDisabled;
    // this.coverTabNormalFlipped = coverNormal.getFlipped(true, false);
    // this.coverTabHighlightFlipped = coverHighlight.getFlipped(true, false);
    // this.coverTabDisabledFlipped = coverDisabled.getFlipped(true, false);
    // return this;
    // }
    //
    // public GUITextureSet setTitleTab(AdaptableUITexture titleNormal, AdaptableUITexture titleDark,
    // AdaptableUITexture titleTabAngular) {
    // this.titleTabNormal = titleNormal;
    // this.titleTabDark = titleDark;
    // this.titleTabAngular = titleTabAngular;
    // return this;
    // }
    //
    // public GUITextureSet setGregTechLogo(UITexture gregtechLogo) {
    // this.gregtechLogo = gregtechLogo;
    // return this;
    // }
    //
    // // endregion
    //
    // // region getters
    //
    // public UITexture getMainBackground() {
    // return mainBackground != null ? mainBackground : DEFAULT.mainBackground;
    // }
    //
    // public UITexture getItemSlot() {
    // return itemSlot != null ? itemSlot : DEFAULT.itemSlot;
    // }
    //
    // public UITexture getFluidSlot() {
    // return fluidSlot != null ? fluidSlot : DEFAULT.fluidSlot;
    // }
    //
    // public UITexture getCoverTabNormal() {
    // return coverTabNormal != null ? coverTabNormal : DEFAULT.coverTabNormal;
    // }
    //
    // public UITexture getCoverTabHighlight() {
    // return coverTabHighlight != null ? coverTabHighlight : DEFAULT.coverTabHighlight;
    // }
    //
    // public UITexture getCoverTabDisabled() {
    // return coverTabDisabled != null ? coverTabDisabled : DEFAULT.coverTabDisabled;
    // }
    //
    // public UITexture getCoverTabNormalFlipped() {
    // return coverTabNormalFlipped != null ? coverTabNormalFlipped : DEFAULT.coverTabNormalFlipped;
    // }
    //
    // public UITexture getCoverTabHighlightFlipped() {
    // return coverTabHighlightFlipped != null ? coverTabHighlightFlipped : DEFAULT.coverTabHighlightFlipped;
    // }
    //
    // public UITexture getCoverTabDisabledFlipped() {
    // return coverTabDisabledFlipped != null ? coverTabDisabledFlipped : DEFAULT.coverTabDisabledFlipped;
    // }
    //
    // public AdaptableUITexture getTitleTabNormal() {
    // return titleTabNormal != null ? titleTabNormal : DEFAULT.titleTabNormal;
    // }
    //
    // public AdaptableUITexture getTitleTabDark() {
    // return titleTabDark != null ? titleTabDark : DEFAULT.titleTabDark;
    // }
    //
    // public AdaptableUITexture getTitleTabAngular() {
    // return titleTabAngular != null ? titleTabAngular : DEFAULT.titleTabAngular;
    // }
    //
    // public UITexture getGregTechLogo() {
    // return gregtechLogo != null ? gregtechLogo : DEFAULT.gregtechLogo;
    // }
    //
    // // endregion
}
