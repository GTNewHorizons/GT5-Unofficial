package gtPlusPlus.core.item.base.dusts;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemDust extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;
	String name = "";
	private final int mTier;
	private final Material dustInfo;

	public BaseItemDust(final String unlocalizedName, final String materialName, final Material matInfo, final int colour, final String pileSize, final int tier, final int sRadioactivity){
		this(unlocalizedName, materialName, matInfo, colour, pileSize, tier, sRadioactivity, true);
	}

	public BaseItemDust(String unlocalizedName, String materialName, Material matInfo, int colour, String pileSize, int tier, int sRadioactivity, boolean addRecipes) {
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
		Logger.WARNING("Unlocalized name for OreDict nameGen: "+this.getUnlocalizedName());
		if (this.getUnlocalizedName().contains("item.")){
			temp = this.getUnlocalizedName().replace("item.", "");
			Logger.WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = this.getUnlocalizedName();
		}
		if (temp.contains("DustTiny")){
			temp = temp.replace("itemD", "d");
			Logger.WARNING("Generating OreDict Name: "+temp);
		}
		else if (temp.contains("DustSmall")){
			temp = temp.replace("itemD", "d");
			Logger.WARNING("Generating OreDict Name: "+temp);
		}
		else {
			temp = temp.replace("itemD", "d");
			Logger.WARNING("Generating OreDict Name: "+temp);
		}
		if ((temp != null) && !temp.equals("")){
			GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
		}
		if (addRecipes){
			this.addFurnaceRecipe();
			this.addMacerationRecipe();
		}
	}

	private String getCorrectTexture(final String pileSize){
		if (!CORE.ConfigSwitches.useGregtechTextures){
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

		String unlocal = super.getItemStackDisplayName(iStack);
		if (!unlocal.toLowerCase().contains(".name")) {
			return unlocal;
		}
		else {
			return unlocal;
		}

		/*if (this.getUnlocalizedName().contains("DustTiny")){
			this.name = "Tiny Pile of "+this.materialName + " Dust";
		}
		else if (this.getUnlocalizedName().contains("DustSmall")){
			this.name = "Small Pile of "+this.materialName + " Dust";
		}
		else {
			this.name = this.materialName + " Dust";
		}
		return this.name;*/
	}

	protected final int sRadiation;
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		
		if (this.dustInfo != null){
			if (entityHolding instanceof EntityPlayer){
				if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode){
					EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.dustInfo.vRadiationLevel, world, entityHolding);	
				}
			}
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		if (stack.getDisplayName().equalsIgnoreCase("fluorite")){
			list.add("Mined from Sandstone and Limestone.");
		}
		if (this.dustInfo != null){
			list.add(this.dustInfo.vChemicalFormula);
		}
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
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

		Logger.MATERIALS("Adding Maceration recipe for "+this.materialName+" Ingot -> Dusts");
		final int chance = (this.mTier*10)/MathUtils.randInt(10, 20);
		GT_ModHandler.addPulverisationRecipe(dustInfo.getIngot(1), dustInfo.getDust(1), null, chance);
		
	}

	private void addFurnaceRecipe(){

		ItemStack aDust = dustInfo.getDust(1);
		ItemStack aOutput;
		
		if (this.dustInfo.requiresBlastFurnace()) {
			aOutput = dustInfo.getHotIngot(1);			
			if (addBlastFurnaceRecipe(aDust, null, aOutput, null, dustInfo.getMeltingPointK())){
				Logger.MATERIALS("Successfully added a blast furnace recipe for "+this.materialName);
			}
			else {
				Logger.MATERIALS("Failed to add a blast furnace recipe for "+this.materialName);
			}
		}
		else {
			aOutput = dustInfo.getIngot(1);
			if (CORE.GT_Recipe.addSmeltingAndAlloySmeltingRecipe(aDust, aOutput)){
				Logger.MATERIALS("Successfully added a furnace recipe for "+this.materialName);
			}
			else {
				Logger.MATERIALS("Failed to add a furnace recipe for "+this.materialName);
			}
		}
		
	}

	private boolean addBlastFurnaceRecipe(final ItemStack input1, final ItemStack input2, final ItemStack output1, final ItemStack output2, final int tempRequired){

		int timeTaken = 125*this.mTier*20;

		if (this.mTier <= 4){
			timeTaken = 25*this.mTier*20;
		}
		int aSlot = mTier - 2;
		if (aSlot < 2) {
			aSlot = 2;
		}
		long aVoltage = GT_Values.V[aSlot >= 2 ? aSlot : 2];

		return GT_Values.RA.addBlastRecipe(
				input1,
				input2,
				GT_Values.NF, GT_Values.NF,
				output1,
				output2,
				timeTaken,
				(int) aVoltage,
				tempRequired);



	}
}
