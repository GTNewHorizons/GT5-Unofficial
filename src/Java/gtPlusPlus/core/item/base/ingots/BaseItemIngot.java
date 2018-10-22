package gtPlusPlus.core.item.base.ingots;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_ModHandler;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemIngot extends BaseItemComponent{

	protected final String materialName;
	protected final String unlocalName;

	public BaseItemIngot(final Material material) {
		this(material, ComponentTypes.INGOT);
	}

	public BaseItemIngot(final Material material, final ComponentTypes type) {
		super(material, type);
		this.materialName = material.getLocalizedName();
		this.unlocalName = material.getUnlocalizedName();
		if (type != ComponentTypes.HOTINGOT) {
			this.generateCompressorRecipe();
		}
	}

	private void generateCompressorRecipe(){
		final ItemStack tempStack = componentMaterial.getIngot(9);
		final ItemStack tempOutput = componentMaterial.getBlock(1);
		if (tempStack != null && tempOutput != null){
			GT_ModHandler.addCompressionRecipe(tempStack, tempOutput);
		}
	}


}
