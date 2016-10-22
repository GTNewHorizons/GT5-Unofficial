package gtPlusPlus.core.item.base;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;

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

	public BaseItemComponent(Material material, ComponentTypes componentType) {
		this.componentMaterial = material;
		this.unlocalName = "item"+componentType.COMPONENT_NAME+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.componentType = componentType;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "item"+componentType.COMPONENT_NAME);
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(componentType.COMPONENT_NAME.toLowerCase()+material.getUnlocalizedName(), UtilsItems.getSimpleStack(this));
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (componentMaterial.getLocalizedName()+componentType.DISPLAY_NAME);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {

		if (materialName != null && materialName != "" && !materialName.equals("")){


			if (componentType == ComponentTypes.DUST){			
				list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");
			}
			if (componentType == ComponentTypes.INGOT){			
				list.add(EnumChatFormatting.GRAY+"A solid ingot of " + materialName + ".");	
				if (materialName != null && materialName != "" && !materialName.equals("") && unlocalName.toLowerCase().contains("ingothot")){
					list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.RED+"Very hot! "+EnumChatFormatting.GRAY+" Avoid direct handling..");		
				}
			}
			if (componentType == ComponentTypes.PLATE){			
				list.add(EnumChatFormatting.GRAY+"A flat plate of " + materialName + ".");	
			}
			if (componentType == ComponentTypes.PLATEDOUBLE){			
				list.add(EnumChatFormatting.GRAY+"A double plate of " + materialName + ".");	
			}
			if (componentType == ComponentTypes.ROD){			
				list.add(EnumChatFormatting.GRAY+"A 40cm Rod of " + materialName + ".");	
			}
			if (componentType == ComponentTypes.RODLONG){			
				list.add(EnumChatFormatting.GRAY+"A 80cm Rod of " + materialName + ".");
			}
			if (componentType == ComponentTypes.ROTOR){			
				list.add(EnumChatFormatting.GRAY+"A Rotor made out of " + materialName + ". ");	
			}
			if (componentType == ComponentTypes.BOLT){			
				list.add(EnumChatFormatting.GRAY+"A small Bolt, constructed from " + materialName + ".");	
			}
			if (componentType == ComponentTypes.SCREW){			
				list.add(EnumChatFormatting.GRAY+"A 8mm Screw, fabricated out of some " + materialName + ".");	
			}
			if (componentType == ComponentTypes.GEAR){			
				list.add(EnumChatFormatting.GRAY+"A large Gear, constructed from " + materialName + ".");
			}
			if (componentType == ComponentTypes.RING){			
				list.add(EnumChatFormatting.GRAY+"A " + materialName + " Ring.");
			}
			if (componentMaterial.isRadioactive){
				list.add(CORE.GT_Tooltip_Radioactive);
			}

		}

		super.addInformation(stack, aPlayer, list, bool);
	}


	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return componentMaterial.getRgbAsHex();
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		Utils.applyRadiationDamageToEntity(componentMaterial.vRadioationLevel, world, entityHolding);
	}








	public static enum ComponentTypes {
		DUST("Dust", " Dust"),
		INGOT("Ingot", " Ingot"),
		PLATE("Plate", " Plate"),
		PLATEDOUBLE("PlateDouble", " Double Plate"),
		ROD("Rod", " Rod"),
		RODLONG("RodLong", " Long Rod"),
		GEAR("Gear", " Gear"),
		SCREW("Screw", " Screw"),
		BOLT("Bolt", " Bolt"),
		ROTOR("Rotor", " Rotor"),
		RING("Ring", " Ring");	    

		private String COMPONENT_NAME;
		private String DISPLAY_NAME;
		private ComponentTypes (final String LocalName, String DisplayName){
			this.COMPONENT_NAME = LocalName;
			this.DISPLAY_NAME = DisplayName;
		}

		public String getComponent(){
			return COMPONENT_NAME;
		}	 

		public String getName(){
			return DISPLAY_NAME;
		}
	}

}


