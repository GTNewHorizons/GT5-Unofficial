package gregtech.api.items.armor.behaviors;

import gregtech.api.items.armor.ArmorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

public class JetpackPerfectHoverBehavior implements IArmorBehavior {

    public static JetpackPerfectHoverBehavior INSTANCE = new JetpackPerfectHoverBehavior();

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.JETPACK_PERFECT_HOVER_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.JETPACK_PERFECT_HOVER_KEY;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @NotNull List<String> tooltip) {
        if (!getOrCreateNbtCompound(stack).hasKey(ArmorHelper.JETPACK_PERFECT_HOVER_KEY)) return;
        tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.jetpackperfecthover"));
    }
}
