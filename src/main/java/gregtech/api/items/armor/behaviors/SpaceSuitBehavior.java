package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class SpaceSuitBehavior implements IArmorBehavior {

    public static final SpaceSuitBehavior INSTANCE = new SpaceSuitBehavior();

    protected SpaceSuitBehavior() {}

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.FORCE_SPACE_SUIT_NBT_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.FORCE_SPACE_SUIT_NBT_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.spacesuit");
    }
}
