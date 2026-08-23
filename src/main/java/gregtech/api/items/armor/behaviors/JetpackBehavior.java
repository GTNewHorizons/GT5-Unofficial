package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.JetpackStats;

public class JetpackBehavior implements IArmorBehavior {

    public static final JetpackBehavior INSTANCE = new JetpackBehavior(JetpackStats.ADVANCED);
    private final JetpackStats jetpackStats;

    protected JetpackBehavior(JetpackStats stats) {
        jetpackStats = stats;
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.Jetpack;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.getArmorState()
            .isActive(BehaviorName.Jetpack)) {
            performFlying(context);
        }
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.Jetpack);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    /*
     * Called every tick, performs flying if the correct keys are pressed.
     * Logic from SimplyJetpacks2:
     * https://github.com/Tomson124/SimplyJetpacks2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/ItemJetpack.java
     */
    private void performFlying(@NotNull ArmorContext context) {
        EntityPlayer player = context.getPlayer();

        double currentAccel = jetpackStats.getVerticalAcceleration() * (player.motionY < 0.3D ? 2.5D : 1.0D);
        double currentSpeedVertical = jetpackStats.getVerticalSpeed() * (player.isInWater() ? 0.4D : 1.0D);
        boolean ascend = ArmorActionManager.getKeybind("VANILLA_JUMP")
            .isKeyDown(player);
        boolean descend = ArmorActionManager.getKeybind("VANILLA_SNEAK")
            .isKeyDown(player);
        boolean isHovering = context.isBehaviorActive(BehaviorName.JetpackHover);
        boolean isGuiOpen = context.getPlayer().worldObj.isRemote
            && (net.minecraft.client.Minecraft.getMinecraft().currentScreen != null);

        if ((ascend && !isGuiOpen) || isHovering && !player.onGround) {
            if (!player.isInWater() && context.drainEnergy(20)) {
                if (ascend && !isGuiOpen) {
                    if (!isHovering) {
                        player.motionY = Math.min(player.motionY + currentAccel, currentSpeedVertical);
                    } else {
                        if (descend) player.motionY = Math
                            .min(player.motionY + currentAccel, -jetpackStats.getVerticalHoverSlowSpeed());
                        else player.motionY = Math
                            .min(player.motionY + currentAccel, jetpackStats.getVerticalHoverSpeed());
                    }
                } else if (descend && !isGuiOpen) {
                    player.motionY = Math.min(player.motionY + currentAccel, -jetpackStats.getVerticalHoverSpeed());
                } else {
                    player.motionY = Math.min(
                        player.motionY + currentAccel,
                        context.hasBehavior(BehaviorName.JetpackPerfectHover) ? 0
                            : -jetpackStats.getVerticalHoverSlowSpeed());
                }
                float speedSideways = (float) (player.isSneaking() ? jetpackStats.getSidewaysSpeed() * 0.5f
                    : jetpackStats.getSidewaysSpeed());
                float speedForward = (float) (player.isSprinting()
                    ? speedSideways * jetpackStats.getSprintSpeedModifier()
                    : speedSideways);

                float speedMulti = Math.max(context.getArmorState().speedBoostMulti, 1.0F);

                float forceMultiplier = 1.0F + (6.0F * (speedMulti - 1.0F) / speedMulti);

                float finalMulti = (float) Math.sqrt(forceMultiplier);

                float currentForward = speedForward * finalMulti;
                float currentSideways = speedSideways * finalMulti;

                if (!isGuiOpen) {
                    if (ArmorActionManager.getKeybind("VANILLA_FORWARD")
                        .isKeyDown(player)) player.moveFlying(0, currentForward, currentForward);
                    if (ArmorActionManager.getKeybind("VANILLA_BACKWARD")
                        .isKeyDown(player)) player.moveFlying(0, -currentSideways, currentSideways * 0.8f);
                    if (ArmorActionManager.getKeybind("VANILLA_LEFT")
                        .isKeyDown(player)) player.moveFlying(currentSideways, 0, currentSideways);
                    if (ArmorActionManager.getKeybind("VANILLA_RIGHT")
                        .isKeyDown(player)) player.moveFlying(-currentSideways, 0, currentSideways);
                }

                if (!player.getEntityWorld().isRemote) {
                    player.fallDistance = 0;
                }
                // spawnParticle(player.getEntityWorld(), player, jetpackStats.getParticle());
            }
        }
    }
}
