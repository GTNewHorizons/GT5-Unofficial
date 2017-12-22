package gtPlusPlus.core.item.base.ore;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseOreComponent extends Item{

	public final Material componentMaterial;
	public final String materialName;
	public final String unlocalName;
	public final ComponentTypes componentType;
	public final int componentColour;
	public Object extraData;

	public BaseOreComponent(final Material material, final ComponentTypes componentType) {
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

	public String getCorrectTextures(){
		if (!CORE.ConfigSwitches.useGregtechTextures){
			return CORE.MODID + ":" + "item"+this.componentType.COMPONENT_NAME;
		}
		
		/*if (this.componentType == ComponentTypes.GEAR){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "gearGt";
		}
		else if (this.componentType == ComponentTypes.SMALLGEAR){
			return "gregtech" + ":" + "materialicons/METALLIC/" + "gearGtSmall";
		}*/
		
		return "gregtech" + ":" + "materialicons/METALLIC/" + this.componentType.COMPONENT_NAME.toLowerCase();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
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

			if (this.componentMaterial != null){
				if (!this.componentMaterial.vChemicalFormula.equals("??") && !this.componentMaterial.vChemicalFormula.equals("?") && this.componentMaterial.getState() != MaterialState.PURE_LIQUID) {
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
			if (entityHolding instanceof EntityPlayer){
				if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode){
					EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.componentMaterial.vRadiationLevel, world, entityHolding);	
				}
			}
		}
	}








	
	public static enum ComponentTypes {
		DUST("Dust", " Dust", "dust"),
		DUSTDIRTY("Ingot", " Ingot", "ingot"),
		DUSTIMPURE("Ingot", " Ingot", "ingot"),
		DUSTPURE("Ingot", " Ingot", "ingot"),
		DUSTREFINED("Ingot", " Ingot", "ingot"),
		CRUSHED("Ingot", " Ingot", "ingot"),
		CRUSHEDCENTRIFUGED("Ingot", " Ingot", "ingot"),
		CRUSHEDPURIFIED("Ingot", " Ingot", "ingot");

		private String COMPONENT_NAME;
		private String DISPLAY_NAME;
		private String OREDICT_NAME;
		private ComponentTypes (final String LocalName, final String DisplayName, final String OreDictName){
			this.COMPONENT_NAME = LocalName;
			this.DISPLAY_NAME = DisplayName;
			this.OREDICT_NAME = OreDictName;
			// dust + Dirty, Impure, Pure, Refined
			// crushed + centrifuged, purified
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


