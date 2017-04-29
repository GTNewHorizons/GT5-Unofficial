package gtPlusPlus.core.item.base.dusts;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseItemDust extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;
	String name = "";
	private final int mTier;
	private final Material dustInfo;

	public BaseItemDust(final String unlocalizedName, final String materialName, final Material matInfo, final int colour, final String pileSize, final int tier, final int sRadioactivity) {
		this.setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);
		this.setTextureName(this.getCorrectTexture(pileSize));

		this.setCreativeTab(tabMisc);
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		this.dustInfo = matInfo;
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalizedName);

		String temp = "";
		Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: "+this.getUnlocalizedName());
		if (this.getUnlocalizedName().contains("item.")){
			temp = this.getUnlocalizedName().replace("item.", "");
			Utils.LOG_WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = this.getUnlocalizedName();
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
		if ((temp != null) && !temp.equals("")){
			GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
		}
		this.addFurnaceRecipe();
		this.addMacerationRecipe();
	}

	private String getCorrectTexture(final String pileSize){
		if (!CORE.configSwitches.useGregtechTextures){
			if ((pileSize == "dust") || (pileSize == "Dust")){
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
	public String getItemStackDisplayName(final ItemStack iStack) {

		if (this.getUnlocalizedName().contains("DustTiny")){
			this.name = "Tiny Pile of "+this.materialName + " Dust";
		}
		else if (this.getUnlocalizedName().contains("DustSmall")){
			this.name = "Small Pile of "+this.materialName + " Dust";
		}
		else {
			this.name = this.materialName + " Dust";
		}
		return this.name;
	}

	protected final int sRadiation;
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(this.sRadiation, world, entityHolding);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
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
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (this.dustInfo != null){
			list.add(this.dustInfo.vChemicalFormula);
		}


		//}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}

	private void addMacerationRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+this.materialName+" Dusts");

		String tempIngot = this.getUnlocalizedName().replace("item.itemDust", "ingot");
		final String tempDust = this.getUnlocalizedName().replace("item.itemDust", "dust");
		ItemStack tempInputStack;
		ItemStack tempOutputStack;

		if (this.getUnlocalizedName().contains("DustSmall") || this.getUnlocalizedName().contains("DustTiny")){
			return;
		}

		Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: "+this.getUnlocalizedName());
		if (this.getUnlocalizedName().contains("item.")){
			tempIngot = this.getUnlocalizedName().replace("item.", "");
			Utils.LOG_WARNING("Generating OreDict Name: "+tempIngot);
		}
		else {
			tempIngot = this.getUnlocalizedName();
		}

		tempIngot = tempIngot.replace("itemDust", "ingot");
		Utils.LOG_WARNING("Generating OreDict Name: "+tempIngot);
		final ItemStack[] outputStacks = {this.dustInfo.getDust(1)};
		if ((tempIngot != null) && !tempIngot.equals("")){
			tempInputStack = ItemUtils.getItemStackOfAmountFromOreDict(tempIngot, 1);
			tempOutputStack = ItemUtils.getItemStackOfAmountFromOreDict(tempDust, 1);
			ItemStack tempStackOutput2 = null;
			final int chance = (this.mTier*10)/MathUtils.randInt(10, 20);
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
			if ((null != tempOutputStack) && (null != tempInputStack)){
				GT_ModHandler.addPulverisationRecipe(tempInputStack, tempOutputStack.splitStack(1), tempStackOutput2, chance);
			}
		}
	}

	private void addFurnaceRecipe(){

		String temp = "";
		if (this.getUnlocalizedName().contains("item.")){
			temp = this.getUnlocalizedName().replace("item.", "");
		}
		else {
			temp = this.getUnlocalizedName();
		}
		if (temp.contains("DustTiny") || temp.contains("DustSmall")){
			return;
		}
		temp = temp.replace("itemDust", "ingot");
		if ((temp != null) && !temp.equals("")){

			if (this.dustInfo.requiresBlastFurnace()){
				Utils.LOG_WARNING("Adding recipe for Hot "+this.materialName+" Ingots in a Blast furnace.");
				final String tempIngot = temp.replace("ingot", "ingotHot");
				final ItemStack tempOutputStack = ItemUtils.getItemStackOfAmountFromOreDict(tempIngot, 1);
				if (null != tempOutputStack){
					Utils.LOG_WARNING("This will produce "+tempOutputStack.getDisplayName() + " Debug: "+tempIngot);
					this.addBlastFurnaceRecipe(ItemUtils.getSimpleStack(this), null, tempOutputStack, null, 350*this.mTier);
				}
				return;
			}
			Utils.LOG_WARNING("Adding recipe for "+this.materialName+" Ingots in a furnace.");
			final ItemStack tempOutputStack = ItemUtils.getItemStackOfAmountFromOreDict(temp, 1);
			//Utils.LOG_WARNING("This will produce an ingot of "+tempOutputStack.getDisplayName() + " Debug: "+temp);
			if (null != tempOutputStack){
				if ((this.mTier < 5) || !this.dustInfo.requiresBlastFurnace()){
					if (CORE.GT_Recipe.addSmeltingAndAlloySmeltingRecipe(ItemUtils.getSimpleStack(this), tempOutputStack)){
						Utils.LOG_WARNING("Successfully added a furnace recipe for "+this.materialName);
					}
					else {
						Utils.LOG_WARNING("Failed to add a furnace recipe for "+this.materialName);
					}
				}
				else if ((this.mTier >= 5) || this.dustInfo.requiresBlastFurnace()){
					Utils.LOG_WARNING("Adding recipe for "+this.materialName+" Ingots in a Blast furnace.");
					Utils.LOG_WARNING("This will produce "+tempOutputStack.getDisplayName());
					if (null != tempOutputStack){
						this.addBlastFurnaceRecipe(ItemUtils.getSimpleStack(this), null, tempOutputStack, null, 350*this.mTier);
					}
					return;
				}
			}

		}
	}

	private void addBlastFurnaceRecipe(final ItemStack input1, final ItemStack input2, final ItemStack output1, final ItemStack output2, final int tempRequired){
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
		
		int timeTaken = 250*this.mTier*20;
		
		if (this.mTier <= 4){
			timeTaken = 50*this.mTier*20;
		}
		
		GT_Values.RA.addBlastRecipe(
				input1,
				input2,
				GT_Values.NF, GT_Values.NF,
				output1,
				output2,
				timeTaken,
				this.mTier*60,
				tempRequired);



	}
}
