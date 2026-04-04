package gregtech.api.items.armor.behaviors;

import net.minecraft.client.entity.EntityPlayerSP;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

public class SwimSpeedBehavior implements IArmorBehavior {

    public static final SwimSpeedBehavior INSTANCE = new SwimSpeedBehavior(0.025f);
    /// Somewhat arbitrary multiplier to make vertical flight speed comparable to horizontal flight speed
    private static final double VERTICAL_SPEED_MULT = 3;

    private final float swimSpeed;

    protected SwimSpeedBehavior(float swimSpeed) {
        this.swimSpeed = swimSpeed;
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.SwimSpeed;
    }

    @Override
    public @NotNull IArmorBehavior merge(@NotNull IArmorBehavior other) {
        return new SwimSpeedBehavior(swimSpeed + ((SwimSpeedBehavior) other).swimSpeed);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (!context.isRemote()) return;

        EntityPlayerSP player = (EntityPlayerSP) context.getPlayer();

        if (!player.isInWater() || player.capabilities.isFlying) return;

        if (player.moveForward != 0 || player.moveStrafing != 0) {
            if (context.drainEnergy(2)) {
                if (player.moveForward > 0F) {
                    player.moveFlying(0F, 1F, swimSpeed);
                }

                if (context.isBehaviorActive(BehaviorName.OmniMovement)) {
                    if (player.moveForward < 0F) {
                        player.moveFlying(0F, -1F, swimSpeed);
                    }

                    if (player.moveStrafing > 0F) {
                        player.moveFlying(1F, 0F, swimSpeed);
                    }

                    if (player.moveStrafing < 0F) {
                        player.moveFlying(-1F, 0F, swimSpeed);
                    }

                    if (player.movementInput.sneak) {
                        player.moveEntity(0, -swimSpeed * VERTICAL_SPEED_MULT, 0);
                    }

                    if (player.movementInput.jump) {
                        player.moveEntity(0, swimSpeed * VERTICAL_SPEED_MULT, 0);
                    }
                }
            }
        }
    }
}
