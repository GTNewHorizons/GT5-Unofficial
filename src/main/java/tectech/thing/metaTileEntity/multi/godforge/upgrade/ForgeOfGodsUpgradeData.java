package tectech.thing.metaTileEntity.multi.godforge.upgrade;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Size;

import tectech.thing.gui.TecTechUITextures;

enum BGColor {

    BLUE(TecTechUITextures.BACKGROUND_GLOW_BLUE, TecTechUITextures.PICTURE_OVERLAY_BLUE),
    PURPLE(TecTechUITextures.BACKGROUND_GLOW_PURPLE, TecTechUITextures.PICTURE_OVERLAY_PURPLE),
    ORANGE(TecTechUITextures.BACKGROUND_GLOW_ORANGE, TecTechUITextures.PICTURE_OVERLAY_ORANGE),
    GREEN(TecTechUITextures.BACKGROUND_GLOW_GREEN, TecTechUITextures.PICTURE_OVERLAY_GREEN),
    RED(TecTechUITextures.BACKGROUND_GLOW_RED, TecTechUITextures.PICTURE_OVERLAY_RED),

    ;

    private final UITexture background;
    private final UITexture overlay;

    BGColor(UITexture background, UITexture overlay) {
        this.background = background;
        this.overlay = overlay;
    }

    public UITexture getBackground() {
        return background;
    }

    public UITexture getOverlay() {
        return overlay;
    }
}

enum BGIcon {

    CHARGE(TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CHARGE, 0.8f),
    CONVERSION(TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CONVERSION, 0.72f),
    CATALYST(TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CATALYST, 1.0f),
    COMPOSITION(TecTechUITextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION, 1.0f),

    ;

    private final UITexture symbol;
    private final float widthRatio;

    BGIcon(UITexture symbol, float widthRatio) {
        this.symbol = symbol;
        this.widthRatio = widthRatio;
    }

    public UITexture getSymbol() {
        return symbol;
    }

    public float getWidthRatio() {
        return widthRatio;
    }
}

enum BGWindowSize {

    STANDARD(250, 250, 110),
    LARGE(300, 300, 85),

    ;

    private final Size size;
    private final int loreY;

    BGWindowSize(int width, int height, int loreY) {
        this.size = new Size(width, height);
        this.loreY = loreY;
    }

    public Size getWindowSize() {
        return size;
    }

    public int getLoreY() {
        return loreY;
    }
}
