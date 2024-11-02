package tectech.thing.metaTileEntity.multi.godforge.util;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import tectech.thing.gui.TecTechUITextures;

public enum UpgradeColor {

    // spotless:off

    BLUE(
        TecTechUITextures.BACKGROUND_GLOW_BLUE,
        TecTechUITextures.PICTURE_OVERLAY_BLUE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_BLUE_OPAQUE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_BLUE),

    PURPLE(
        TecTechUITextures.BACKGROUND_GLOW_PURPLE,
        TecTechUITextures.PICTURE_OVERLAY_PURPLE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_PURPLE_OPAQUE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_PURPLE),

    ORANGE(
        TecTechUITextures.BACKGROUND_GLOW_ORANGE,
        TecTechUITextures.PICTURE_OVERLAY_ORANGE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_ORANGE_OPAQUE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_ORANGE),

    GREEN(
        TecTechUITextures.BACKGROUND_GLOW_GREEN,
        TecTechUITextures.PICTURE_OVERLAY_GREEN,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_GREEN_OPAQUE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_GREEN),

    RED(
        TecTechUITextures.BACKGROUND_GLOW_RED,
        TecTechUITextures.PICTURE_OVERLAY_RED,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_RED_OPAQUE,
        TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_RED),

    ;

    // spotless:on

    private final UITexture background;
    private final UITexture overlay;
    private final UITexture opaqueConnector;
    private final UITexture connector;

    UpgradeColor(UITexture background, UITexture overlay, UITexture opaqueConnector, UITexture connector) {
        this.background = background;
        this.overlay = overlay;
        this.opaqueConnector = opaqueConnector;
        this.connector = connector;
    }

    public UITexture getBackground() {
        return background;
    }

    public UITexture getOverlay() {
        return overlay;
    }

    public UITexture getOpaqueConnector() {
        return opaqueConnector;
    }

    public UITexture getConnector() {
        return connector;
    }
}
