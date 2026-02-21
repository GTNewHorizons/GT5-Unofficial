package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import bartworks.util.MathUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorKeybinds;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.util.GTUtility;

public class SpeedBoostBehavior implements IArmorBehavior {

    public static final SpeedBoostBehavior MECH_ARMOR_INSTANCE = new SpeedBoostBehavior(0.05F);
    /// Somewhat arbitrary multiplier to make vertical flight speed comparable to horizontal flight speed
    private static final double VERTICAL_SPEED_MULT = 8;

    public static final float SPEED_INCREMENT = 0.25F;

    private final float speedup;

    protected SpeedBoostBehavior(float speedUp) {
        this.speedup = speedUp;
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.SpeedBoost;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        ArmorState state = context.getArmorState();

        if (keyPressed == ArmorKeybinds.SPEED_INCREASE_KEYBIND) {
            state.speedBoost += SPEED_INCREMENT;
        } else if (keyPressed == ArmorKeybinds.SPEED_DECREASE_KEYBIND) {
            state.speedBoost -= SPEED_INCREMENT;
        }

        state.speedBoost = MathUtils.clamp(state.speedBoost, 0, 1);

        GTUtility.sendChatToPlayer(context.getPlayer(), "New speed: " + Math.round(state.speedBoost * 100F) + "%");
    }

    @Override
    public void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        context.getArmorState().speedBoost = stackTag.getFloat("speedBoost");
    }

    @Override
    public void saveArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        stackTag.setFloat("speedBoost", context.getArmorState().speedBoost);
    }

    @Override
    public @NotNull IArmorBehavior merge(@NotNull IArmorBehavior other) {
        return new SpeedBoostBehavior(this.speedup + ((SpeedBoostBehavior) other).speedup);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ImmutableSet.of(ArmorKeybinds.SPEED_INCREASE_KEYBIND, ArmorKeybinds.SPEED_DECREASE_KEYBIND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onArmorTick(@NotNull ArmorContext context) {
        if (!context.isRemote()) return;

        EntityPlayerSP player = (EntityPlayerSP) context.getPlayer();

        float speed = context.getArmorState().speedBoost * this.speedup;

        if (speed <= 0) return;

        if ((player.onGround || player.capabilities.isFlying) && !player.isInWater()
            && (player.moveForward != 0 || player.moveStrafing != 0
                || (player.capabilities.isFlying && (player.movementInput.sneak || player.movementInput.jump)))) {
            if (context.drainEnergy(1)) {
                if (player.moveForward > 0F) {
                    player.moveFlying(0F, 1F, speed);
                }

                if (context.isBehaviorActive(BehaviorName.OmniMovement)) {
                    if (player.moveForward < 0F) {
                        player.moveFlying(0F, -1F, speed);
                    }

                    if (player.moveStrafing > 0F) {
                        player.moveFlying(1F, 0F, speed);
                    }

                    if (player.moveStrafing < 0F) {
                        player.moveFlying(-1F, 0F, speed);
                    }

                    if (player.capabilities.isFlying) {
                        if (player.movementInput.sneak) {
                            player.moveEntity(0, -speed * VERTICAL_SPEED_MULT, 0);
                        }

                        if (player.movementInput.jump) {
                            player.moveEntity(0, speed * VERTICAL_SPEED_MULT, 0);
                        }
                    }
                }
            }
        }
    }
}
