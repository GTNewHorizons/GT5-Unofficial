package gtPlusPlus.core.item.base.dusts;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItemDustUnique extends Item{

	protected final int colour;
	protected final int sRadiation;
	protected final String materialName;
	protected final String name;
	protected final String chemicalNotation;
	
	public BaseItemDustUnique(final String unlocalizedName, final String materialName, final int colour, final String pileSize) {
		this(unlocalizedName, materialName, "NullFormula", colour, pileSize);
	}

	public BaseItemDustUnique(final String unlocalizedName, final String materialName, final String mChemicalFormula, final int colour, final String pileSize) {
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);
		this.setTextureName(this.getCorrectTexture(pileSize));
		this.setCreativeTab(tabMisc);
		this.colour = colour;
		this.materialName = materialName;
		if (mChemicalFormula.equals("") || mChemicalFormula.equals("NullFormula")){
			this.chemicalNotation = StringUtils.subscript(materialName);			
		}
		else {
			this.chemicalNotation = StringUtils.subscript(mChemicalFormula);			
		}
		this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
		GameRegistry.registerItem(this, unlocalizedName);

		if (this.getUnlocalizedName().contains("DustTiny")){
			this.name = "Tiny Pile of "+this.materialName+ " Dust";
		}
		else if (this.getUnlocalizedName().contains("DustSmall")){
			this.name = "Small Pile of "+this.materialName+ " Dust";
		}
		else {
			this.name = this.materialName+ " Dust";
		}

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
	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {
		return this.name;
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
			return "gregtech" + ":" + "materialicons/SHINY/dustSmall";
		}
		else if (pileSize.toLowerCase().contains("tiny")){
			return "gregtech" + ":" + "materialicons/SHINY/dustTiny";
		}
		return "gregtech" + ":" + "materialicons/SHINY/dust";
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (this.chemicalNotation.length() > 0 && !chemicalNotation.equals("") && !chemicalNotation.equals("NullFormula")){
			list.add(this.chemicalNotation);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}	

	public final String getMaterialName() {
		return StringUtils.subscript(this.materialName);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}

}
