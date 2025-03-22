package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class StepAssistBehavior implements IArmorBehavior {

    public static final StepAssistBehavior INSTANCE = new StepAssistBehavior();

    protected StepAssistBehavior() {/**/}

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.STEP_ASSIST_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.STEP_ASSIST_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.stepassist");
    }
}
