package gregtech.common.powergoggles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.gtnewhorizons.dashboard.api.HudBounds;
import com.gtnewhorizons.dashboard.api.HudElement;
import com.gtnewhorizons.dashboard.api.HudRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;
import gregtech.common.powergoggles.handlers.PowerGogglesHudHandler;
import gregtech.common.powergoggles.handlers.PowerGogglesKeybindHandler;

/**
 * Lets players move, resize and hide the Power Goggles HUD in Dashboard's HUD editor. The only class using Dashboard,
 * so only call it while Dashboard is loaded.
 */
@SideOnly(Side.CLIENT)
public final class DashboardHud {

    /** Like SimplePowerGogglesRenderer. */
    private static final int BORDER = 3;
    private static final int LINE_GAP = 2;
    private static final int CHART_HEIGHT = 100;

    private static final HudElement POWER_GOGGLES = new HudElement("gregtech:power_goggles", DashboardHud::getBounds);

    private DashboardHud() {}

    public static void register() {
        POWER_GOGGLES
            .setOwnSettings(
                "dashboard.element.gregtech.power_goggles.own_settings",
                PowerGogglesKeybindHandler.openConfigGui.getKeyDescription())
            .setPreview((screenWidth, screenHeight) -> HudRegistry.render(POWER_GOGGLES, DashboardHud::drawPreview));
        HudRegistry.register(POWER_GOGGLES);
    }

    /** False means the player hid the HUD, so skip drawing it and do not call {@link #endRender()}. */
    public static boolean beginPowerGoggles(ScaledResolution resolution) {
        return HudRegistry.beginRender(POWER_GOGGLES, resolution);
    }

    public static void endRender() {
        HudRegistry.endRender();
    }

    /**
     * The measurements below the corner set in the Power Goggles config and the chart above it, scaled around that
     * corner by the HUD scale.
     */
    private static HudBounds getBounds(int screenWidth, int screenHeight) {
        int width = PowerGogglesConfigHandler.rectangleWidth;
        int cornerX = PowerGogglesConfigHandler.mainOffsetX - BORDER;
        int cornerY = screenHeight - PowerGogglesConfigHandler.mainOffsetY;
        int top = 0;
        int bottom = 0;
        if (PowerGogglesConfigHandler.showPowerChart) {
            top = -CHART_HEIGHT - BORDER * 3;
            bottom = -BORDER;
        }
        if (PowerGogglesConfigHandler.showMeasurements) {
            int fontHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            int barHeight = PowerGogglesConfigHandler.showPowerBar ? PowerGogglesConfigHandler.rectangleHeight : 0;
            int height = (int) (fontHeight * PowerGogglesConfigHandler.mainTextScaling + barHeight
                + fontHeight * PowerGogglesConfigHandler.subTextScaling * 2
                + LINE_GAP * 4);
            if (!PowerGogglesConfigHandler.showPowerChart) {
                top = -BORDER;
            }
            bottom = BORDER + height;
        }
        double scale = PowerGogglesConfigHandler.hudScale;
        return new HudBounds(
            cornerX,
            cornerY + (int) Math.floor(top * scale),
            (int) Math.ceil((width + BORDER * 2) * scale),
            (int) Math.ceil((bottom - top) * scale));
    }

    /** The real HUD with the measurements received so far, which shows zero without goggles. */
    private static void drawPreview() {
        ScaledResolution resolution = HudRegistry.getResolution();
        RenderGameOverlayEvent parent = new RenderGameOverlayEvent(0, resolution, 0, 0);
        PowerGogglesHudHandler.getInstance()
            .getRenderer()
            .render(new RenderGameOverlayEvent.Post(parent, RenderGameOverlayEvent.ElementType.ALL));
    }
}
