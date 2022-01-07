package gtPlusPlus.plugin.agrichem;

import gtPlusPlus.plugin.agrichem.logic.AlgaeGeneticData;
import net.minecraft.item.ItemStack;

public interface IAlgalItem {

	public abstract AlgaeDefinition getAlgaeType(ItemStack aStack);
	
	public abstract AlgaeGeneticData getSpeciesData(ItemStack aStack);	
	
}
