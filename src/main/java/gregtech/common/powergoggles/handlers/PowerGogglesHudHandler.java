package gregtech.common.powergoggles.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.powergoggles.PowerGogglesUtil;
import gregtech.common.powergoggles.gui.PowerGogglesRenderer;
import gregtech.common.powergoggles.gui.SimplePowerGogglesRenderer;

@SideOnly(Side.CLIENT)
public class PowerGogglesHudHandler {

    private static final PowerGogglesHudHandler INSTANCE = new PowerGogglesHudHandler();

    private final Minecraft mc = Minecraft.getMinecraft();

    private PowerGogglesRenderer renderer = new SimplePowerGogglesRenderer();

    private PowerGogglesHudHandler() {}

    public static PowerGogglesHudHandler getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public void drawHud(RenderGameOverlayEvent.Post event) {
        if (!shouldDrawHud(event)) {
            return;
        }

        renderer.render(event);
    }

    private boolean shouldDrawHud(RenderGameOverlayEvent.Post event) {

        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return false;
        }
        if (mc.gameSettings.showDebugInfo) {
            return false;
        }
        if (PowerGogglesConfigHandler.hideWhenChatOpen && mc.currentScreen instanceof GuiChat) {
            return false;
        }

        return PowerGogglesUtil.isPlayerWearingGoggles(mc.thePlayer);

    }

    public PowerGogglesRenderer getRenderer() {
        return renderer;
    }
}
