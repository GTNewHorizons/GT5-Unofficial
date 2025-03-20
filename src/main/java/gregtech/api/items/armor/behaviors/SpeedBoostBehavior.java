package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import bartworks.util.MathUtils;
import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.items.armor.ArmorKeybinds;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class SpeedBoostBehavior implements IArmorBehavior {

    public static final SpeedBoostBehavior MECH_ARMOR_INSTANCE = new SpeedBoostBehavior(0.05F);

    private final float speedup;

    protected SpeedBoostBehavior(float speedUp) {
        this.speedup = speedUp;
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.SPEED_BOOST_MAX_KEY;
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.SPEED_BOOST_MAX_KEY)) return;

        float current = tag.getFloat(ArmorHelper.SPEED_BOOST_CURRENT_KEY);

        if (keyPressed == ArmorKeybinds.SPEED_INCREASE_KEYBIND) {
            current += 0.05F;
        } else if (keyPressed == ArmorKeybinds.SPEED_DECREASE_KEYBIND) {
            current -= 0.05F;
        }
        current = MathUtils.clamp(current, 0, 1);

        PlayerUtils.messagePlayer(player, "New speed: " + Math.round(current * 100F) + "%");
        tag.setFloat(ArmorHelper.SPEED_BOOST_CURRENT_KEY, current);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return ImmutableSet.of(ArmorKeybinds.SPEED_INCREASE_KEYBIND, ArmorKeybinds.SPEED_DECREASE_KEYBIND);
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (!world.isRemote) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        float speed = tag.getFloat(ArmorHelper.SPEED_BOOST_CURRENT_KEY) * tag.getFloat(ArmorHelper.SPEED_BOOST_MAX_KEY);
        if (speed > 0F) {
            if ((player.onGround || player.capabilities.isFlying) && !player.isInWater()
                && (player.moveForward != 0 || player.moveStrafing != 0)) {
                if (ArmorHelper.drainArmor(stack, 1)) {
                    if (player.moveForward > 0F) {
                        player.moveFlying(0F, 1F, speed);
                    }
                    if (tag.getBoolean(ArmorHelper.OMNI_MOVEMENT_KEY)) {
                        if (player.moveForward < 0F) {
                            player.moveFlying(0F, -1F, speed);
                        }
                        if (player.moveStrafing > 0F) {
                            player.moveFlying(1F, 0F, speed);
                        }
                        if (player.moveStrafing < 0F) {
                            player.moveFlying(-1F, 0F, speed);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        float increaseMax = speedup;
        if (tag.hasKey(ArmorHelper.SPEED_BOOST_MAX_KEY)) {
            increaseMax += tag.getFloat(ArmorHelper.SPEED_BOOST_MAX_KEY);
        }
        tag.setFloat(ArmorHelper.SPEED_BOOST_CURRENT_KEY, 1F);
        tag.setFloat(ArmorHelper.SPEED_BOOST_MAX_KEY, increaseMax);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.speedboost");
    }
}
