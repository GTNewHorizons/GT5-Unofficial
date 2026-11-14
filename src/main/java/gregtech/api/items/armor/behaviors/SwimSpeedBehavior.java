package gregtech.api.items.armor.behaviors;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

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
        if (!(other instanceof SwimSpeedBehavior o)) return this;
        return new SwimSpeedBehavior(swimSpeed + o.swimSpeed);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        EntityPlayer player = context.getPlayer();

        if (!player.isInWater() || player.capabilities.isFlying) return;

        if (player.moveForward == 0 && player.moveStrafing == 0) return;

        if (!context.drainEnergy(2)) return;

        if (!context.isRemote()) return;

        EntityPlayerSP sp = (EntityPlayerSP) player;

        if (sp.moveForward > 0F) {
            sp.moveFlying(0F, 1F, swimSpeed);
        }

        if (context.isBehaviorActive(BehaviorName.OmniMovement)) {
            if (sp.moveForward < 0F) {
                sp.moveFlying(0F, -1F, swimSpeed);
            }

            if (sp.moveStrafing > 0F) {
                sp.moveFlying(1F, 0F, swimSpeed);
            }

            if (sp.moveStrafing < 0F) {
                sp.moveFlying(-1F, 0F, swimSpeed);
            }

            if (sp.movementInput.sneak) {
                sp.moveEntity(0, -swimSpeed * VERTICAL_SPEED_MULT, 0);
            }

            if (sp.movementInput.jump) {
                sp.moveEntity(0, swimSpeed * VERTICAL_SPEED_MULT, 0);
            }
        }
    }
}
