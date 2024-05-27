package gtPlusPlus.plugin.agrichem;

import net.minecraft.item.ItemStack;

import gtPlusPlus.plugin.agrichem.logic.AlgaeGeneticData;

public interface IAlgalItem {

    AlgaeDefinition getAlgaeType(ItemStack aStack);

    AlgaeGeneticData getSpeciesData(ItemStack aStack);
}
