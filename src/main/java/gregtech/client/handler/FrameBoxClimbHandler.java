package gregtech.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Lets the local player climb frame box sides as on a ladder.
 */
@SideOnly(Side.CLIENT)
public class FrameBoxClimbHandler {

    // Same limits as the vanilla ladder in EntityLivingBase#moveEntityWithHeading.
    private static final double LADDER_CLIMB_SPEED = 0.2D;
    private static final double LADDER_MAX_HORIZONTAL_SPEED = 0.15D;
    private static final double LADDER_MAX_FALL_SPEED = 0.15D;

    private boolean isWithin = false;

    public void onSideTouched(EntityPlayer player) {
        isWithin = true;
        if (player.isCollidedHorizontally) {
            player.motionY = LADDER_CLIMB_SPEED;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !isWithin) {
            return;
        }
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.isGamePaused()) {
            return;
        }
        isWithin = false;
        final EntityPlayer player = minecraft.thePlayer;
        if (player == null) {
            return;
        }
        player.motionX = MathHelper
            .clamp_double(player.motionX, -LADDER_MAX_HORIZONTAL_SPEED, LADDER_MAX_HORIZONTAL_SPEED);
        player.motionZ = MathHelper
            .clamp_double(player.motionZ, -LADDER_MAX_HORIZONTAL_SPEED, LADDER_MAX_HORIZONTAL_SPEED);
        player.motionY = Math.max(player.motionY, -LADDER_MAX_FALL_SPEED);
        if (player.isSneaking() && player.motionY < 0.0D) {
            player.motionY = 0.0D;
        }
    }
}
