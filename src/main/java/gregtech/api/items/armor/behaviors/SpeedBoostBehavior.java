package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import bartworks.util.MathUtils;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorKeybinds;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.util.GTUtility;

public class SpeedBoostBehavior implements IArmorBehavior {

    public static final SpeedBoostBehavior MECH_ARMOR_INSTANCE = new SpeedBoostBehavior(2.0F);

    /// Somewhat arbitrary multiplier to make vertical flight speed comparable to horizontal flight speed
    private static final double VERTICAL_SPEED_MULT = 2.0;

    public static final float SPEED_INCREMENT = 0.5F;

    private final float speedMaxMulti;

    private final float BASE_SPEED = 0.127F;

    public SpeedBoostBehavior(float speedUp) {
        this.speedMaxMulti = speedUp;
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
            state.speedBoostMulti += SPEED_INCREMENT;
        } else if (keyPressed == ArmorKeybinds.SPEED_DECREASE_KEYBIND) {
            state.speedBoostMulti -= SPEED_INCREMENT;
        }

        state.speedBoostMulti = MathUtils.clamp(state.speedBoostMulti, 1, speedMaxMulti);

        GTUtility.sendChatToPlayer(
            context.getPlayer(),
            GTUtility.translate("GT5U.armor.message.speed_set", state.speedBoostMulti));
    }

    @Override
    public void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        float savedBoost = stackTag.getFloat("speedBoostMulti");
        context.getArmorState().speedBoostMulti = Math.max(savedBoost, 1.0F);
    }

    @Override
    public void saveArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        stackTag.setFloat("speedBoostMulti", context.getArmorState().speedBoostMulti);
    }

    @Override
    public @NotNull IArmorBehavior merge(@NotNull IArmorBehavior other) {
        if (!(other instanceof SpeedBoostBehavior o)) return this;
        return new SpeedBoostBehavior(this.speedMaxMulti + o.speedMaxMulti);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ImmutableSet.of(ArmorKeybinds.SPEED_INCREASE_KEYBIND, ArmorKeybinds.SPEED_DECREASE_KEYBIND);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        EntityPlayer player = context.getPlayer();

        float speed = (context.getArmorState().speedBoostMulti - 1) * BASE_SPEED;

        if (player.capabilities.isFlying) {
            speed *= 0.5F;
        }

        if (speed <= 0) return;

        boolean isMoving = !player.isInWater() && (player.moveForward != 0 || player.moveStrafing != 0
            || (player.capabilities.isFlying && (player.isSneaking() || ArmorKeybinds.VANILLA_JUMP.isKeyDown(player))));

        if (!isMoving || !context.drainEnergy(1)) return;

        if (!context.isRemote()) return;

        EntityPlayerSP sp = (EntityPlayerSP) player;

        if (!context.isRemote()) return;
        if (player.onGround || player.capabilities.isFlying) {
            if (player.moveForward > 0F) {
                player.moveFlying(0F, 1F, speed);
            }

            if (context.isBehaviorActive(BehaviorName.OmniMovement)) {
                if (player.moveForward < 0F) player.moveFlying(0F, -1F, speed);
                if (player.moveStrafing > 0F) player.moveFlying(1F, 0F, speed);
                if (player.moveStrafing < 0F) player.moveFlying(-1F, 0F, speed);
            }
        } else {
            player.jumpMovementFactor = 0.02F + (speed * 0.2F);
        }

        if (context.isBehaviorActive(BehaviorName.OmniMovement) && player.capabilities.isFlying) {
            if (player.isSneaking()) {
                player.moveEntity(0, -speed * VERTICAL_SPEED_MULT, 0);
            }

            if (ArmorKeybinds.VANILLA_JUMP.isKeyDown(player)) {
                player.moveEntity(0, speed * VERTICAL_SPEED_MULT, 0);
            }
        }
    }
}
