package gregtech.api.items.armor.behaviors;

import gregtech.api.items.armor.ArmorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.mana.ManaItemHandler;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

public class WaterBreathingBehavior implements IArmorBehavior {

    public static final WaterBreathingBehavior INSTANCE = new WaterBreathingBehavior();

    protected WaterBreathingBehavior() {}

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.WATER_BREATHING_KEY;
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (player.isInWater() && stack.hasTagCompound() && stack.getTagCompound().getBoolean(ArmorHelper.WATER_BREATHING_KEY)) {
            if(player.getAir() <= 1 && ArmorHelper.drainArmor(stack, 5000)) {
                player.setAir(300);
            }
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.WATER_BREATHING_KEY, true);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.waterbreathing");
    }
}
