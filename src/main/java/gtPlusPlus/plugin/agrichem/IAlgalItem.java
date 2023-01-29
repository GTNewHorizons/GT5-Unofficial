package gtPlusPlus.plugin.agrichem;

import net.minecraft.item.ItemStack;

import gtPlusPlus.plugin.agrichem.logic.AlgaeGeneticData;

public interface IAlgalItem {

    public abstract AlgaeDefinition getAlgaeType(ItemStack aStack);

    public abstract AlgaeGeneticData getSpeciesData(ItemStack aStack);
}
