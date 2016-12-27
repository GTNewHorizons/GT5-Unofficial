package gtPlusPlus.core.item.base.ingots;

import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemIngot extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;

	public BaseItemIngot(String unlocalizedName, String materialName, int colour, int sRadioactivity) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemIngot");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalizedName);
		String temp = "";
		if (unlocalName.contains("itemIngot")){
			temp = unlocalName.replace("itemI", "i");
		}
		else if (unlocalName.contains("itemHotIngot")){
			temp = unlocalName.replace("itemHotIngot", "ingotHot");
		}
		if (temp != null && temp != ""){
			GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
		}		
		generateCompressorRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Ingot");
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

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
	

	protected final int sRadiation;
	 @Override
		public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		 EntityUtils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
		}
}
