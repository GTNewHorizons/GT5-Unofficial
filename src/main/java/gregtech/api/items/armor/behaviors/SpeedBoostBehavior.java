package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

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

    /*
     * @Override
     * public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
     * NBTTagCompound tag = getOrCreateNbtCompound(stack);
     * if (!tag.hasKey(ArmorHelper.SPEED_BOOST_KEY)) return;
     * boolean wasEnabled = tag.getBoolean(ArmorHelper.SPEED_BOOST_KEY);
     * tag.setBoolean(ArmorHelper.SPEED_BOOST_KEY, !wasEnabled);
     * if (wasEnabled) {
     * player.removePotionEffect(Potion.nightVision.id);
     * PlayerUtils.messagePlayer(
     * player,
     * StatCollector.translateToLocalFormatted("GT5U.armor.message.disabled", getBehaviorName()));
     * } else {
     * PlayerUtils.messagePlayer(
     * player,
     * StatCollector.translateToLocalFormatted("GT5U.armor.message.enabled", getBehaviorName()));
     * }
     * }
     * @Override
     * public Set<SyncedKeybind> getListenedKeys() {
     * return Collections.singleton(NIGHT_VISION_KEYBIND);
     * }
     */

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (!world.isRemote) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        float speed = tag.getFloat(ArmorHelper.SPEED_BOOST_CURRENT_KEY);
        if (speed > 0F) {
            if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInWater()) {
                if (ArmorHelper.drainArmor(stack, 1)) {
                    player.moveFlying(0F, 1F, speed);
                }
            }
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setFloat(ArmorHelper.SPEED_BOOST_MAX_KEY, speedup);
        tag.setFloat(ArmorHelper.SPEED_BOOST_CURRENT_KEY, speedup);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.speedboost");
    }
}
