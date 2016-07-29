package miscutil.core.item.base.gears;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemGear extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemGear(String unlocalizedName, String materialName, int colour, int tier) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemGear");
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemG", "g"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Gear");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A large Gear, constructed from " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return Utils.generateSingularRandomHexValue();
		}
		return colour;

	}

	private void addExtruderRecipe(){
		Utils.LOG_INFO("Adding recipe for "+materialName+" Gears");
		String tempIngot = unlocalName.replace("itemGear", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 8);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack,
					ItemList.Shape_Extruder_Gear.get(1),
					UtilsItems.getSimpleStack(this),
					40*mTier*20,
					24*mTier);	
		}				
	}

}
