package gregtech.api.modernmaterials.tooltips;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IMaterialTooltip {

    ArrayList<String> apply(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList, boolean aF3_H);
}
