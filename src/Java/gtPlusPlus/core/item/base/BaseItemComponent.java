package gtPlusPlus.core.item.base;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemComponent extends Item{

	public final Material componentMaterial;
	public final String materialName;
	public final String unlocalName;
	public final ComponentTypes componentType;
	public final int componentColour;
	public Object extraData;

	public BaseItemComponent(Material material, ComponentTypes componentType) {
		this.componentMaterial = material;
		this.unlocalName = "item"+componentType.COMPONENT_NAME+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.componentType = componentType;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(getCorrectTextures());
		this.componentColour = material.getRgbAsHex();
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(componentType.getOreDictName()+material.getUnlocalizedName(), ItemUtils.getSimpleStack(this));
	}

	//For Cell Generation
	public BaseItemComponent(String unlocalName, String localName, short[] RGBA) {
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
			return CORE.MODID + ":" + "item"+componentType.COMPONENT_NAME;
		}
		if (componentType == ComponentTypes.GEAR){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "gearGt";
		}
		else if (componentType == ComponentTypes.ROD){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "stick";
		}
		else if (componentType == ComponentTypes.RODLONG){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "stickLong";
		}
		else if (componentType == ComponentTypes.PLATEDOUBLE){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "plateDouble";
		}
		return "gregtech" + ":" + "materialicons/METALLIC/" + componentType.COMPONENT_NAME.toLowerCase();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		if (componentMaterial != null) {
			return (componentMaterial.getLocalizedName()+componentType.DISPLAY_NAME);
		}
		return materialName+" Cell";
	}

	public final String getMaterialName() {
		return materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {

		if (materialName != null && materialName != "" && !materialName.equals("") && componentMaterial != null){


			if (componentType == ComponentTypes.DUST){			
				//list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");
			}
			if (componentType == ComponentTypes.INGOT){			
				//list.add(EnumChatFormatting.GRAY+"A solid ingot of " + materialName + ".");	
				if (materialName != null && materialName != "" && !materialName.equals("") && unlocalName.toLowerCase().contains("ingothot")){
					list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.RED+"Very hot! "+EnumChatFormatting.GRAY+" Avoid direct handling..");		
				}
			}
			if (componentType == ComponentTypes.PLATE){			
				//list.add(EnumChatFormatting.GRAY+"A flat plate of " + materialName + ".");	
			}
			if (componentType == ComponentTypes.PLATEDOUBLE){			
				//list.add(EnumChatFormatting.GRAY+"A double plate of " + materialName + ".");	
			}
			if (componentType == ComponentTypes.ROD){			
				//list.add(EnumChatFormatting.GRAY+"A 40cm Rod of " + materialName + ".");	
			}
			if (componentType == ComponentTypes.RODLONG){			
				//list.add(EnumChatFormatting.GRAY+"A 80cm Rod of " + materialName + ".");
			}
			if (componentType == ComponentTypes.ROTOR){			
				//list.add(EnumChatFormatting.GRAY+"A Rotor made out of " + materialName + ". ");	
			}
			if (componentType == ComponentTypes.BOLT){			
				//list.add(EnumChatFormatting.GRAY+"A small Bolt, constructed from " + materialName + ".");	
			}
			if (componentType == ComponentTypes.SCREW){			
				//list.add(EnumChatFormatting.GRAY+"A 8mm Screw, fabricated out of some " + materialName + ".");	
			}
			if (componentType == ComponentTypes.GEAR){			
				//list.add(EnumChatFormatting.GRAY+"A large Gear, constructed from " + materialName + ".");
			}
			if (componentType == ComponentTypes.RING){			
				//list.add(EnumChatFormatting.GRAY+"A " + materialName + " Ring.");
			}			
			if (componentMaterial != null){
				if (!componentMaterial.vChemicalFormula.equals("??")) {
					list.add(componentMaterial.vChemicalFormula);
				}

				if (componentMaterial.isRadioactive){
					list.add(CORE.GT_Tooltip_Radioactive);
				}
			}

		}

		super.addInformation(stack, aPlayer, list, bool);
	}


	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return componentColour;
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		if (componentMaterial != null){
			EntityUtils.applyRadiationDamageToEntity(componentMaterial.vRadioationLevel, world, entityHolding);
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
		SCREW("Screw", " Screw", "screw"),
		BOLT("Bolt", " Bolt", "bolt"),
		ROTOR("Rotor", " Rotor", "rotor"),
		RING("Ring", " Ring", "ring"), 
		CELL("Cell", " Cell", "cell"), 
		NUGGET("Nugget", " Nugget", "nugget");	    

		private String COMPONENT_NAME;
		private String DISPLAY_NAME;
		private String OREDICT_NAME;
		private ComponentTypes (final String LocalName, String DisplayName, String OreDictName){
			this.COMPONENT_NAME = LocalName;
			this.DISPLAY_NAME = DisplayName;
			this.OREDICT_NAME = OreDictName;
		}

		public String getComponent(){
			return COMPONENT_NAME;
		}	 

		public String getName(){
			return DISPLAY_NAME;
		}

		public String getOreDictName(){
			return OREDICT_NAME;
		}

	}

}


