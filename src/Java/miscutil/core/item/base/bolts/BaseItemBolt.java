package miscutil.core.item.base.bolts;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemBolt extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemBolt(String unlocalizedName, String materialName, int colour, int tier) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemBolt");
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemB", "b"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Bolt");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A small Bolt, constructed from " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
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

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Bolts");
		String tempIngot = unlocalName.replace("itemBolt", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack, 
					ItemList.Shape_Extruder_Bolt.get(1), 
					UtilsItems.getSimpleStack(this, 8),
					30*mTier*20,
					24*mTier);	
		}				
	}

}
