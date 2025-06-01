package gregtech.common.handlers;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WrenchPrecisionModeClientState {

    private static boolean lastClickState = false;
    private static boolean hasBroken = false;

    /**
     * Returns whether the player is allowed to break a block this click.
     * Only true until the first block is broken.
     */
    public static boolean canBreak() {
        return !hasBroken;
    }

    /**
     * Marks that a block has been broken during the current mouse hold.
     */
    public static void markBroken() {
        hasBroken = true;
    }

    /**
     * Resets block-break tracking, called on mouse release.
     */
    public static void reset() {
        hasBroken = false;
    }

    /**
     * Client tick handler: checks for mouse release and resets tracking.
     */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        // Only reset when left-click was released
        boolean currentClick = Mouse.isButtonDown(0); // 0 = left click
        if (!currentClick && lastClickState) {
            reset();
        }

        lastClickState = currentClick;
    }
}
