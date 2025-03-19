package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class SwimSpeedBehavior implements IArmorBehavior {

    public static final SwimSpeedBehavior INSTANCE = new SwimSpeedBehavior();

    protected SwimSpeedBehavior() {}

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.SWIM_SPEED_KEY;
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (!world.isRemote || !player.isInWater() || player.capabilities.isFlying) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.SWIM_SPEED_KEY) && ArmorHelper.drainArmor(stack, 2)) {
            player.motionX *= 1.2;
            player.motionZ *= 1.2;
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.SWIM_SPEED_KEY, true);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.swimspeed");
    }
}
