package gregtech.api.items.armor.behaviors;

import static gregtech.loaders.ExtraIcons.rebreatherAugment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class WaterBreathingBehavior implements IArmorBehavior {

    public static final WaterBreathingBehavior INSTANCE = new WaterBreathingBehavior();

    protected WaterBreathingBehavior() {}

    @Override
    public IIcon getModularArmorTexture() {
        return rebreatherAugment;
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.WATER_BREATHING_KEY;
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (player.isInWater() && stack.hasTagCompound()
            && stack.getTagCompound()
                .getBoolean(ArmorHelper.WATER_BREATHING_KEY)) {
            if (player.getAir() <= 1 && ArmorHelper.drainArmor(stack, 5000)) {
                player.setAir(300);
            }
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.WATER_BREATHING_KEY, true);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.waterbreathing");
    }
}
