package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemComponent extends Item{

	public final Material componentMaterial;
	public final String materialName;
	public final String unlocalName;
	public final ComponentTypes componentType;
	public final int componentColour;
	public Object extraData;

	public BaseItemComponent(final Material material, final ComponentTypes componentType) {
		this.componentMaterial = material;
		this.unlocalName = "item"+componentType.COMPONENT_NAME+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.componentType = componentType;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(this.getCorrectTextures());
		this.componentColour = material.getRgbAsHex();
		GameRegistry.registerItem(this, this.unlocalName);
		GT_OreDictUnificator.registerOre(componentType.getOreDictName()+material.getUnlocalizedName(), ItemUtils.getSimpleStack(this));
	}

	//For Cell Generation
	public BaseItemComponent(final String unlocalName, final String localName, final short[] RGBA) {
		this.componentMaterial = null;
		this.unlocalName = "itemCell"+unlocalName;
		this.materialName = localName;
		this.componentType = ComponentTypes.CELL;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.componentColour = MathUtils.getRgbAsHex(RGBA);
		this.extraData = RGBA;
		this.setTextureName(CORE.MODID + ":" + "item"+ComponentTypes.CELL.COMPONENT_NAME);
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(ComponentTypes.CELL.getOreDictName()+unlocalName, ItemUtils.getSimpleStack(this));
	}

	public String getCorrectTextures(){
		if (!CORE.configSwitches.useGregtechTextures){
			return CORE.MODID + ":" + "item"+this.componentType.COMPONENT_NAME;
		}
		if (this.componentType == ComponentTypes.GEAR){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "gearGt";
		}
		else if (this.componentType == ComponentTypes.SMALLGEAR){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "gearGtSmall";
		}
		else if (this.componentType == ComponentTypes.ROD){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "stick";
		}
		else if (this.componentType == ComponentTypes.RODLONG){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "stickLong";
		}
		else if (this.componentType == ComponentTypes.PLATEDOUBLE){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "plateDouble";
		}
		else if (this.componentType == ComponentTypes.CELL){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "cell";
		}
		else if (this.componentType == ComponentTypes.PLASMACELL){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "cellPlasma";
		}
		else if (this.componentType == ComponentTypes.BOLT){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "bolt";
		}
		else if (this.componentType == ComponentTypes.RING){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "ring";
		}
		else if (this.componentType == ComponentTypes.ROTOR){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "rotor";
		}
		else if (this.componentType == ComponentTypes.SCREW){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "screw";
		}
		else if (this.componentType == ComponentTypes.INGOT){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "ingot";
		}
		else if (this.componentType == ComponentTypes.HOTINGOT){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "ingotHot";
		}
		return "gregtech" + ":" + "materialicons/METALLIC/" + this.componentType.COMPONENT_NAME.toLowerCase();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		if (this.componentType == ComponentTypes.SMALLGEAR){
			return "Small " + this.materialName+" Gear";
		}

		if (this.componentMaterial != null) {
			return (this.componentMaterial.getLocalizedName()+this.componentType.DISPLAY_NAME);
		}
		return this.materialName+" Cell";
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		if ((this.materialName != null) && (this.materialName != "") && !this.materialName.equals("") && (this.componentMaterial != null)){


			if (this.componentType == ComponentTypes.DUST){
				//list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");
			}
			if (this.componentType == ComponentTypes.INGOT){
				//list.add(EnumChatFormatting.GRAY+"A solid ingot of " + materialName + ".");
				if ((this.materialName != null) && (this.materialName != "") && !this.materialName.equals("") && this.unlocalName.toLowerCase().contains("ingothot")){
					list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.RED+"Very hot! "+EnumChatFormatting.GRAY+" Avoid direct handling..");
				}
			}
			if (this.componentType == ComponentTypes.PLATE){
				//list.add(EnumChatFormatting.GRAY+"A flat plate of " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.PLATEDOUBLE){
				//list.add(EnumChatFormatting.GRAY+"A double plate of " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.ROD){
				//list.add(EnumChatFormatting.GRAY+"A 40cm Rod of " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.RODLONG){
				//list.add(EnumChatFormatting.GRAY+"A 80cm Rod of " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.ROTOR){
				//list.add(EnumChatFormatting.GRAY+"A Rotor made out of " + materialName + ". ");
			}
			if (this.componentType == ComponentTypes.BOLT){
				//list.add(EnumChatFormatting.GRAY+"A small Bolt, constructed from " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.SCREW){
				//list.add(EnumChatFormatting.GRAY+"A 8mm Screw, fabricated out of some " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.GEAR){
				//list.add(EnumChatFormatting.GRAY+"A large Gear, constructed from " + materialName + ".");
			}
			if (this.componentType == ComponentTypes.RING){
				//list.add(EnumChatFormatting.GRAY+"A " + materialName + " Ring.");
			}
			if (this.componentMaterial != null){
				if (!this.componentMaterial.vChemicalFormula.equals("??") && !this.componentMaterial.vChemicalFormula.equals("?")) {
					list.add(Utils.sanitizeStringKeepBrackets(this.componentMaterial.vChemicalFormula));
				}

				if (this.componentMaterial.isRadioactive){
					list.add(CORE.GT_Tooltip_Radioactive);
				}
			}

		}

		super.addInformation(stack, aPlayer, list, bool);
	}


	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return this.componentColour;
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (this.componentMaterial != null){
			EntityUtils.applyRadiationDamageToEntity(this.componentMaterial.vRadiationLevel, world, entityHolding);
		}
	}








	public static enum ComponentTypes {
		DUST("Dust", " Dust", "dust"),
		INGOT("Ingot", " Ingot", "ingot"),
		HOTINGOT("HotIngot", " Hot Ingot", "ingotHot"),
		PLATE("Plate", " Plate", "plate"),
		PLATEDOUBLE("PlateDouble", " Double Plate", "plateDouble"),
		ROD("Rod", " Rod", "stick"),
		RODLONG("RodLong", " Long Rod", "stickLong"),
		GEAR("Gear", " Gear", "gear"),
		SMALLGEAR("SmallGear", " Gear", "gearGtSmall"), //TODO
		SCREW("Screw", " Screw", "screw"),
		BOLT("Bolt", " Bolt", "bolt"),
		ROTOR("Rotor", " Rotor", "rotor"),
		RING("Ring", " Ring", "ring"),
		PLASMACELL("CellPlasma", " Plasma Cell", "cellPlasma"),
		CELL("Cell", " Cell", "cell"),
		NUGGET("Nugget", " Nugget", "nugget"),
		PLATEHEAVY("HeavyPlate", " Heavy Plate", "plateHeavy");

		private String COMPONENT_NAME;
		private String DISPLAY_NAME;
		private String OREDICT_NAME;
		private ComponentTypes (final String LocalName, final String DisplayName, final String OreDictName){
			this.COMPONENT_NAME = LocalName;
			this.DISPLAY_NAME = DisplayName;
			this.OREDICT_NAME = OreDictName;
		}

		public String getComponent(){
			return this.COMPONENT_NAME;
		}

		public String getName(){
			return this.DISPLAY_NAME;
		}

		public String getOreDictName(){
			return this.OREDICT_NAME;
		}

	}

}


