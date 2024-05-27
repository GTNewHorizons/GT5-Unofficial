package gtPlusPlus.plugin.agrichem;

import gtPlusPlus.plugin.agrichem.logic.AlgaeGeneticData;
import net.minecraft.item.ItemStack;

public interface IAlgalItem {

    AlgaeDefinition getAlgaeType(ItemStack aStack);

    AlgaeGeneticData getSpeciesData(ItemStack aStack);
}
