package miscutil.core.item.base.dusts;

import static miscutil.core.creative.AddToCreativeTab.tabMisc;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemDust extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;
	String name = "";

	public BaseItemDust(String unlocalizedName, String materialName, int colour, String pileSize) {
		setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);	
		if (pileSize == "dust" || pileSize == "Dust"){
			this.setTextureName(CORE.MODID + ":" + "dust");}
		else{
			this.setTextureName(CORE.MODID + ":" + "dust"+pileSize);}
		this.setMaxStackSize(64);
		this.setCreativeTab(tabMisc);
		this.colour = colour;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);

		String temp = "";
		Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: "+getUnlocalizedName());
		if (getUnlocalizedName().contains("item.")){
			temp = getUnlocalizedName().replace("item.", "");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = getUnlocalizedName();
		}
		if (temp.contains("DustTiny")){
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else if (temp.contains("DustSmall")){
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}		
		if (temp != null && temp != ""){
			GT_OreDictUnificator.registerOre(temp, UtilsItems.getSimpleStack(this));
		}
		addMacerationRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack iStack) {

		if (getUnlocalizedName().contains("DustTiny")){
			name = "Tiny Pile of "+materialName + " Dust";
		}
		else if (getUnlocalizedName().contains("DustSmall")){
			name = "Small Pile of "+materialName + " Dust";
		}
		else {
			name = materialName + " Dust";
		}
		return name;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (pileType != null && materialName != null && pileType != "" && materialName != "" && !pileType.equals("") && !materialName.equals("")){
			if (this.pileType == "dust"){
				list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");		
			}
			else{
				list.add(EnumChatFormatting.GRAY+"A "+this.pileType.toLowerCase()+" pile of " + materialName + " dust.");		
			}
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

	private void addMixerRecipe(){
		ItemStack tempStack = UtilsItems.getSimpleStack(this);
		ItemStack tempOutput = null;
		String temp = "";
		Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: "+getUnlocalizedName());
		if (getUnlocalizedName().contains("item.")){
			temp = getUnlocalizedName().replace("item.", "");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = getUnlocalizedName();
		}
		if (temp.contains("DustTiny")){
			temp = temp.replace("itemDustTiny", "dust");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else if (temp.contains("DustSmall")){
			temp = temp.replace("itemDustSmall", "dust");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}		
		if (temp != null && temp != ""){
			tempOutput = UtilsItems.getItemStackOfAmountFromOreDict(temp, 1);
		}

		if (tempOutput != null){
			if (getUnlocalizedName().contains("DustTiny")){
				Utils.LOG_WARNING("Generating a 9 Tiny dust to 1 Dust recipe for "+materialName);
				UtilsRecipe.addShapelessGregtechRecipe(tempOutput,
						tempStack, tempStack, tempStack,
						tempStack, tempStack, tempStack,
						tempStack, tempStack, tempStack);
			}
			else if (getUnlocalizedName().contains("DustSmall")){
				Utils.LOG_WARNING("Generating a 4 Small dust to 1 Dust recipe for "+materialName);
				UtilsRecipe.addShapelessGregtechRecipe(tempOutput,
						tempStack, tempStack, null,
						tempStack, tempStack, null,
						null, null, null);
			}
			else {
				Utils.LOG_WARNING("Generating a shapeless Dust recipe for "+materialName);
				UtilsRecipe.addShapelessGregtechRecipe(tempOutput,
						"dustTungsten", "dustTantalum", "dustTantalum",
						"dustTantalum", "dustTantalum", "dustTantalum",
						"dustTantalum", "dustTantalum", "dustTantalum");
			}
		}

	}

	private void addMacerationRecipe(){
		Utils.LOG_INFO("Adding recipe for "+materialName+" Dusts");

		String tempIngot = getUnlocalizedName().replace("item.itemDust", "ingot");
		String tempDust = getUnlocalizedName().replace("item.itemDust", "dust");
		ItemStack tempInputStack;
		ItemStack tempOutputStack;
		
		if (getUnlocalizedName().contains("DustSmall") || getUnlocalizedName().contains("DustTiny")){
			return;
		}

		Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: "+getUnlocalizedName());
		if (getUnlocalizedName().contains("item.")){
			tempIngot = getUnlocalizedName().replace("item.", "");
			Utils.LOG_WARNING("Generating OreDict Name: "+tempIngot);
		}
		else {
			tempIngot = getUnlocalizedName();
		}

		tempIngot = tempIngot.replace("itemDust", "ingot");
		Utils.LOG_WARNING("Generating OreDict Name: "+tempIngot);

		if (tempIngot != null && tempIngot != ""){
			tempInputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
			tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempDust, 1);
			if (null != tempOutputStack && null != tempInputStack){
				GT_ModHandler.addPulverisationRecipe(tempInputStack, tempOutputStack);
			}
		}						
	}

}
