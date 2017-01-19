package gtPlusPlus.core.item.base.dusts;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemDustUnique extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;
	String name = "";
	String chemicalNotation = "";

	public BaseItemDustUnique(String unlocalizedName, String materialName, int colour, String pileSize) {
		setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);
		this.setTextureName(getCorrectTexture(pileSize));
		this.setCreativeTab(tabMisc);
		this.colour = colour;
		this.materialName = materialName;
		this.chemicalNotation = MaterialUtils.subscript(materialName);
		this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
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
	}

	@Override
	public String getItemStackDisplayName(ItemStack iStack) {

		if (getUnlocalizedName().contains("DustTiny")){
			name = "Tiny Pile of "+materialName+ " Dust";
		}
		else if (getUnlocalizedName().contains("DustSmall")){
			name = "Small Pile of "+materialName+ " Dust";
		}
		else {
			name = materialName+ " Dust";
		}
		return name;
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
			return "gregtech" + ":" + "materialicons/SHINY/dustSmall";			
		}
		else if (pileSize.toLowerCase().contains("tiny")){
			return "gregtech" + ":" + "materialicons/SHINY/dustTiny";			
		}	
		return "gregtech" + ":" + "materialicons/SHINY/dust";			
	}

	protected final int sRadiation;
	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (containsSubScript(chemicalNotation)){
			list.add(chemicalNotation);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	static boolean containsSubScript(String s){
		if (s.contains(MaterialUtils.superscript("1"))) return true;
		if (s.contains(MaterialUtils.superscript("2"))) return true;
		if (s.contains(MaterialUtils.superscript("3"))) return true;
		if (s.contains(MaterialUtils.superscript("4"))) return true;
		if (s.contains(MaterialUtils.superscript("5"))) return true;
		if (s.contains(MaterialUtils.superscript("6"))) return true;
		if (s.contains(MaterialUtils.superscript("7"))) return true;
		if (s.contains(MaterialUtils.superscript("8"))) return true;
		if (s.contains(MaterialUtils.superscript("9"))) return true;
		if (s.contains(MaterialUtils.subscript("1"))) return true;
		if (s.contains(MaterialUtils.subscript("2"))) return true;
		if (s.contains(MaterialUtils.subscript("3"))) return true;
		if (s.contains(MaterialUtils.subscript("4"))) return true;
		if (s.contains(MaterialUtils.subscript("5"))) return true;
		if (s.contains(MaterialUtils.subscript("6"))) return true;
		if (s.contains(MaterialUtils.subscript("7"))) return true;
		if (s.contains(MaterialUtils.subscript("8"))) return true;
		if (s.contains(MaterialUtils.subscript("9"))) return true;
		String r = MaterialUtils.subscript(s);
		if (r.contains(("1"))) return false;
		if (r.contains(("2"))) return false;
		if (r.contains(("3"))) return false;
		if (r.contains(("4"))) return false;
		if (r.contains(("5"))) return false;
		if (r.contains(("6"))) return false;
		if (r.contains(("7"))) return false;
		if (r.contains(("8"))) return false;
		if (r.contains(("9"))) return false;
		return false;
	}

	public final String getMaterialName() {
		return MaterialUtils.subscript(materialName);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}

}
