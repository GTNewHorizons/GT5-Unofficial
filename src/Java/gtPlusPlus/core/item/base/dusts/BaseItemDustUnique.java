package gtPlusPlus.core.item.base.dusts;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseItemDustUnique extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;
	String name = "";
	String chemicalNotation = "";

	public BaseItemDustUnique(final String unlocalizedName, final String materialName, final int colour, final String pileSize) {
		this.setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);
		this.setTextureName(this.getCorrectTexture(pileSize));
		this.setCreativeTab(tabMisc);
		this.colour = colour;
		this.materialName = materialName;
		this.chemicalNotation = MaterialUtils.subscript(materialName);
		this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
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
	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {

		if (this.getUnlocalizedName().contains("DustTiny")){
			this.name = "Tiny Pile of "+this.materialName+ " Dust";
		}
		else if (this.getUnlocalizedName().contains("DustSmall")){
			this.name = "Small Pile of "+this.materialName+ " Dust";
		}
		else {
			this.name = this.materialName+ " Dust";
		}
		return this.name;
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
			return "gregtech" + ":" + "materialicons/SHINY/dustSmall";
		}
		else if (pileSize.toLowerCase().contains("tiny")){
			return "gregtech" + ":" + "materialicons/SHINY/dustTiny";
		}
		return "gregtech" + ":" + "materialicons/SHINY/dust";
	}

	protected final int sRadiation;
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(this.sRadiation, world, entityHolding);
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (containsSubScript(this.chemicalNotation)){
			list.add(this.chemicalNotation);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	static boolean containsSubScript(final String s){
		if (s.contains(MaterialUtils.superscript("1"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("2"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("3"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("4"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("5"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("6"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("7"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("8"))) {
			return true;
		}
		if (s.contains(MaterialUtils.superscript("9"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("1"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("2"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("3"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("4"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("5"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("6"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("7"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("8"))) {
			return true;
		}
		if (s.contains(MaterialUtils.subscript("9"))) {
			return true;
		}
		final String r = MaterialUtils.subscript(s);
		if (r.contains(("1"))) {
			return false;
		}
		if (r.contains(("2"))) {
			return false;
		}
		if (r.contains(("3"))) {
			return false;
		}
		if (r.contains(("4"))) {
			return false;
		}
		if (r.contains(("5"))) {
			return false;
		}
		if (r.contains(("6"))) {
			return false;
		}
		if (r.contains(("7"))) {
			return false;
		}
		if (r.contains(("8"))) {
			return false;
		}
		if (r.contains(("9"))) {
			return false;
		}
		return false;
	}

	public final String getMaterialName() {
		return MaterialUtils.subscript(this.materialName);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}

}
