package gtPlusPlus.core.item.base.ingots;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class BaseItemIngot extends BaseItemComponent{

	protected final String materialName;
	protected final String unlocalName;

	public BaseItemIngot(Material material) {
		this(material, ComponentTypes.INGOT);
	}
	
	public BaseItemIngot(Material material, ComponentTypes type) {
		super(material, type);
		this.materialName = material.getLocalizedName();
		this.unlocalName = material.getUnlocalizedName();
		generateCompressorRecipe();
	}

	private void generateCompressorRecipe(){
		if (unlocalName.contains("itemIngot")){
			ItemStack tempStack = ItemUtils.getSimpleStack(this, 9);
			ItemStack tempOutput = null;
			String temp = getUnlocalizedName().replace("item.itemIngot", "block");
			Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: "+getUnlocalizedName());
			if (getUnlocalizedName().contains("item.")){
				temp = getUnlocalizedName().replace("item.", "");
				Utils.LOG_WARNING("Generating OreDict Name: "+temp);
			}
			temp = temp.replace("itemIngot", "block");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
			if (temp != null && temp != ""){
				tempOutput = ItemUtils.getItemStackOfAmountFromOreDict(temp, 1);
				if (tempOutput != null){
					GT_ModHandler.addCompressionRecipe(tempStack, tempOutput);
				}
				
			}
		}
		else if (unlocalName.contains("itemHotIngot")){
			return;
		}
		

	}
	
}
