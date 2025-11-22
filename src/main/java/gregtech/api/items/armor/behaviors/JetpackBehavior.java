package gregtech.api.items.armor.behaviors;

import static gregtech.loaders.ExtraIcons.jetpackAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorKeybinds;
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
    public IIcon getModularArmorTexture() {
        return jetpackAugment;
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
        return Collections.singleton(ArmorKeybinds.JETPACK_KEYBIND);
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
        boolean ascend = ArmorKeybinds.VANILLA_JUMP.isKeyDown(player);
        boolean descend = ArmorKeybinds.VANILLA_SNEAK.isKeyDown(player);

        if (ascend || context.isBehaviorActive(BehaviorName.JetpackHover) && !player.onGround) {
            if (!player.isInWater() && context.drainEnergy(20)) {
                if (ascend) {
                    if (!context.isBehaviorActive(BehaviorName.JetpackHover)) {
                        player.motionY = Math.min(player.motionY + currentAccel, currentSpeedVertical);
                    } else {
                        if (descend) player.motionY = Math
                            .min(player.motionY + currentAccel, jetpackStats.getVerticalHoverSlowSpeed());
                        else player.motionY = Math
                            .min(player.motionY + currentAccel, jetpackStats.getVerticalHoverSpeed());
                    }
                } else if (descend) {
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

                if (ArmorKeybinds.VANILLA_FORWARD.isKeyDown(player)) player.moveFlying(0, speedForward, speedForward);
                if (ArmorKeybinds.VANILLA_BACK.isKeyDown(player))
                    player.moveFlying(0, -speedSideways, speedSideways * 0.8f);
                if (ArmorKeybinds.VANILLA_LEFT.isKeyDown(player)) player.moveFlying(speedSideways, 0, speedSideways);
                if (ArmorKeybinds.VANILLA_RIGHT.isKeyDown(player)) player.moveFlying(-speedSideways, 0, speedSideways);
                if (!player.getEntityWorld().isRemote) {
                    player.fallDistance = 0;
                }
                // spawnParticle(player.getEntityWorld(), player, jetpackStats.getParticle());
            }
        }
    }
}
