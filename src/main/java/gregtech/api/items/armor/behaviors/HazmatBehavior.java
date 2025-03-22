package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class HazmatBehavior implements IArmorBehavior {

    public static final HazmatBehavior INSTANCE = new HazmatBehavior();

    protected HazmatBehavior() {/**/}

    // This tag will treat the armor as if it has the Hazmat Protection Enchantment
    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.HAZMAT_PROTECTION_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.HAZMAT_PROTECTION_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.hazmat");
    }
}
