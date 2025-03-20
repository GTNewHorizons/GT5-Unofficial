package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class InfiniteEnergyBehavior implements IArmorBehavior {

    public static final InfiniteEnergyBehavior INSTANCE = new InfiniteEnergyBehavior();

    protected InfiniteEnergyBehavior() {}

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.INFINITE_ENERGY_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.INFINITE_ENERGY_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.infiniteenergy");
    }
}
