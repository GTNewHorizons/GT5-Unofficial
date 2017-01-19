package gtPlusPlus.core.item.base.dusts;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemDust extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;
	String name = "";
	private int mTier;
	private Material dustInfo;

	public BaseItemDust(String unlocalizedName, String materialName, Material matInfo, int colour, String pileSize, int tier, int sRadioactivity) {
		setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);
		this.setTextureName(getCorrectTexture(pileSize));

		this.setCreativeTab(tabMisc);
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		this.dustInfo = matInfo;
		this.sRadiation = sRadioactivity;
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
		if (temp != null && !temp.equals("")){
			GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
		}
		addFurnaceRecipe();
		addMacerationRecipe();
	}

	private String getCorrectTexture(String pileSize){
		if (!CORE.configSwitches.useGregtechTextures){
			if (pileSize == "dust" || pileSize == "Dust"){
				this.setTextureName(CORE.MODID + ":" + "dust");}
			else{
				this.setTextureName(CORE.MODID + ":" + "dust"+pileSize);
			}
		}		
		if (pileSize.toLowerCase().contains("small")){
			return "gregtech" + ":" + "materialicons/METALLIC/dustSmall";			
		}
		else if (pileSize.toLowerCase().contains("tiny")){
			return "gregtech" + ":" + "materialicons/METALLIC/dustTiny";			
		}	
		return "gregtech" + ":" + "materialicons/METALLIC/dust";			
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

	protected final int sRadiation;
	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		//if (pileType != null && materialName != null && pileType != "" && materialName != "" && !pileType.equals("") && !materialName.equals("")){
		/*if (getUnlocalizedName().contains("DustTiny")){
			list.add(EnumChatFormatting.GRAY+"A tiny pile of " + materialName + " dust.");	
		}
		else if (getUnlocalizedName().contains("DustSmall")){
			list.add(EnumChatFormatting.GRAY+"A small pile of " + materialName + " dust.");	
		}
		else {
			list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");
		}*/
		if (stack.getDisplayName().equalsIgnoreCase("fluorite")){
			list.add("Mined from Sandstone and Limestone.");						
		}
		if (sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (dustInfo != null){
			list.add(dustInfo.vChemicalFormula);
		}


		//}
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

	private void addMacerationRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Dusts");

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
		ItemStack[] outputStacks = {dustInfo.getDust(1)};
		if (tempIngot != null && !tempIngot.equals("")){
			tempInputStack = ItemUtils.getItemStackOfAmountFromOreDict(tempIngot, 1);
			tempOutputStack = ItemUtils.getItemStackOfAmountFromOreDict(tempDust, 1);
			ItemStack tempStackOutput2 = null;
			int chance = mTier*10/MathUtils.randInt(10, 20);
			if (outputStacks.length != 0){
				if (outputStacks.length == 1){
					tempStackOutput2 = null;
				}
				else {
					if (!outputStacks[1].getUnlocalizedName().toLowerCase().contains("aaa_broken")){
						tempStackOutput2 = outputStacks[1];
						tempOutputStack = outputStacks[0];
					}
					else {
						tempStackOutput2 = null;
					}
				}								
			}
			else {
				tempStackOutput2 = null;
			}
			if (null != tempOutputStack && null != tempInputStack){
				GT_ModHandler.addPulverisationRecipe(tempInputStack, tempOutputStack.splitStack(1), tempStackOutput2, chance);
			}
		}
	}

	private void addFurnaceRecipe(){		

		String temp = "";
		if (getUnlocalizedName().contains("item.")){
			temp = getUnlocalizedName().replace("item.", "");
		}
		else {
			temp = getUnlocalizedName();
		}
		if (temp.contains("DustTiny") || temp.contains("DustSmall")){
			return;
		}
		temp = temp.replace("itemDust", "ingot");		
		if (temp != null && !temp.equals("")){

			if (dustInfo.requiresBlastFurnace()){
				Utils.LOG_WARNING("Adding recipe for Hot "+materialName+" Ingots in a Blast furnace.");
				String tempIngot = temp.replace("ingot", "ingotHot");
				ItemStack tempOutputStack = ItemUtils.getItemStackOfAmountFromOreDict(tempIngot, 1);
				if (null != tempOutputStack){
					Utils.LOG_WARNING("This will produce "+tempOutputStack.getDisplayName() + " Debug: "+tempIngot);
					addBlastFurnaceRecipe(ItemUtils.getSimpleStack(this), null, tempOutputStack, null, 350*mTier);		
				}				
				return;
			}
			Utils.LOG_WARNING("Adding recipe for "+materialName+" Ingots in a furnace.");
			ItemStack tempOutputStack = ItemUtils.getItemStackOfAmountFromOreDict(temp, 1);
			//Utils.LOG_WARNING("This will produce an ingot of "+tempOutputStack.getDisplayName() + " Debug: "+temp);
			if (null != tempOutputStack){
				if (mTier < 5 || !dustInfo.requiresBlastFurnace()){					
					if (CORE.GT_Recipe.addSmeltingAndAlloySmeltingRecipe(ItemUtils.getSimpleStack(this), tempOutputStack)){
						Utils.LOG_WARNING("Successfully added a furnace recipe for "+materialName);
					}
					else {
						Utils.LOG_WARNING("Failed to add a furnace recipe for "+materialName);
					}
				}				
				else if (mTier >= 5 || dustInfo.requiresBlastFurnace()){
					Utils.LOG_WARNING("Adding recipe for "+materialName+" Ingots in a Blast furnace.");
					Utils.LOG_WARNING("This will produce "+tempOutputStack.getDisplayName());
					if (null != tempOutputStack){
						addBlastFurnaceRecipe(ItemUtils.getSimpleStack(this), null, tempOutputStack, null, 350*mTier);		
					}				
					return;				
				}
			}

		}	
	}

	private void addBlastFurnaceRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, int tempRequired){
		//Special Cases
		/*if (input1.getUnlocalizedName().toLowerCase().contains("tantalloy61")){
			Utils.LOG_INFO("Adding Special handler for Staballoy-61 in the Blast Furnace");
			input2 = UtilsItems.getItemStackOfAmountFromOreDict("dustTantalloy60", 2);
			if (input2 == null){
				Utils.LOG_INFO("invalid itemstack.");
			}
			else {
				Utils.LOG_INFO("Found "+input2.getDisplayName());
			}
		}*/
		GT_Values.RA.addBlastRecipe(
				input1,
				input2,
				GT_Values.NF, GT_Values.NF,
				output1,
				output2,
				250*mTier*20,
				mTier*64, 
				tempRequired);



	}
}
