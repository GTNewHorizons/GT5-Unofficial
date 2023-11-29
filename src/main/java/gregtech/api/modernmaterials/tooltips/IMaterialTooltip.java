package gregtech.api.modernmaterials.tooltips;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface IMaterialTooltip {
    ArrayList<String> apply(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList, boolean aF3_H);
}
