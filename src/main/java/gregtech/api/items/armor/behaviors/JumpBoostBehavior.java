package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class JumpBoostBehavior implements IArmorBehavior {

    public static final JumpBoostBehavior MECH_ARMOR_INSTANCE = new JumpBoostBehavior(0.2F);

    final private float boost;

    protected JumpBoostBehavior(float boost) {
        this.boost = boost;
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setFloat(ArmorHelper.JUMP_BOOST_KEY, boost);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.JUMP_BOOST_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.jumpboost");
    }
}
