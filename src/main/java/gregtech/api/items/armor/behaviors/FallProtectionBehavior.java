package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class FallProtectionBehavior implements IArmorBehavior {

    public static final FallProtectionBehavior INSTANCE = new FallProtectionBehavior();

    protected FallProtectionBehavior() {}

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.FALL_PROTECTION_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.FALL_PROTECTION_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.fallprotection");
    }
}
