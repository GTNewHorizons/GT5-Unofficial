package gregtech.api.items.armor.behaviors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class JetpackPerfectHoverBehavior implements IArmorBehavior {

    public static JetpackPerfectHoverBehavior INSTANCE = new JetpackPerfectHoverBehavior();

    @Override
    public void addBehaviorNBT(@NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.JETPACK_PERFECT_HOVER_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.JETPACK_PERFECT_HOVER_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.jetpackperfecthover");
    }
}
