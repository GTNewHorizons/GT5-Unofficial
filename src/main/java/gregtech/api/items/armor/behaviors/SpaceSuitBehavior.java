package gregtech.api.items.armor.behaviors;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import gregtech.api.hazards.Hazard;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorHelper;

public class SpaceSuitBehavior implements IArmorBehavior {

    public static final SpaceSuitBehavior INSTANCE = new SpaceSuitBehavior();

    protected SpaceSuitBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.SpaceSuit;
    }

    @Override
    public void saveArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        stackTag.setBoolean(ArmorHelper.FORCE_SPACE_SUIT_NBT_KEY, true);
    }
}
