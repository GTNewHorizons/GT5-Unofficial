package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

/**
 * This behavior simply adds an NBT tag associated with forestry apiarist's armor. It is up to individual armor
 * to inherit the IArmorApiarist interface and check for this tag.
 */
public class ApiaristBehavior implements IArmorBehavior {

    public static final ApiaristBehavior INSTANCE = new ApiaristBehavior();

    protected ApiaristBehavior() {/**/}

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.APIARIST_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.APIARIST_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.apiarist");
    }
}
