package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemComponent extends Item {

	public static enum ComponentTypes {
		DUST("Dust", " Dust", "dust"), INGOT("Ingot", " Ingot", "ingot"), PLATE("Plate", " Plate",
				"plate"), PLATEDOUBLE("PlateDouble", " Double Plate", "plateDouble"), ROD("Rod", " Rod",
						"stick"), RODLONG("RodLong", " Long Rod", "stickLong"), GEAR("Gear", " Gear", "gear"), SCREW(
								"Screw", " Screw", "screw"), BOLT("Bolt", " Bolt", "bolt"), ROTOR("Rotor", " Rotor",
										"rotor"), RING("Ring", " Ring", "ring"), CELL("Cell", " Cell",
												"cell"), NUGGET("Nugget", " Nugget", "nugget");

		private String	COMPONENT_NAME;
		private String	DISPLAY_NAME;
		private String	OREDICT_NAME;

		private ComponentTypes(final String LocalName, final String DisplayName, final String OreDictName) {
			this.COMPONENT_NAME = LocalName;
			this.DISPLAY_NAME = DisplayName;
			this.OREDICT_NAME = OreDictName;
		}

		public String getComponent() {
			return this.COMPONENT_NAME;
		}

		public String getName() {
			return this.DISPLAY_NAME;
		}

		public String getOreDictName() {
			return this.OREDICT_NAME;
		}

	}
	public final Material		componentMaterial;
	public final String			materialName;
	public final String			unlocalName;

	public final ComponentTypes	componentType;

	public BaseItemComponent(final Material material, final ComponentTypes componentType) {
		this.componentMaterial = material;
		this.unlocalName = "item" + componentType.COMPONENT_NAME + material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.componentType = componentType;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "item" + componentType.COMPONENT_NAME);
		GameRegistry.registerItem(this, this.unlocalName);
		GT_OreDictUnificator.registerOre(componentType.getOreDictName() + material.getUnlocalizedName(),
				ItemUtils.getSimpleStack(this));
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		if (this.materialName != null && this.materialName != "" && !this.materialName.equals("")) {

			if (this.componentType == ComponentTypes.DUST) {
				list.add(EnumChatFormatting.GRAY + "A pile of " + this.materialName + " dust.");
			}
			if (this.componentType == ComponentTypes.INGOT) {
				list.add(EnumChatFormatting.GRAY + "A solid ingot of " + this.materialName + ".");
				if (this.materialName != null && this.materialName != "" && !this.materialName.equals("")
						&& this.unlocalName.toLowerCase().contains("ingothot")) {
					list.add(EnumChatFormatting.GRAY + "Warning: " + EnumChatFormatting.RED + "Very hot! "
							+ EnumChatFormatting.GRAY + " Avoid direct handling..");
				}
			}
			if (this.componentType == ComponentTypes.PLATE) {
				list.add(EnumChatFormatting.GRAY + "A flat plate of " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.PLATEDOUBLE) {
				list.add(EnumChatFormatting.GRAY + "A double plate of " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.ROD) {
				list.add(EnumChatFormatting.GRAY + "A 40cm Rod of " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.RODLONG) {
				list.add(EnumChatFormatting.GRAY + "A 80cm Rod of " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.ROTOR) {
				list.add(EnumChatFormatting.GRAY + "A Rotor made out of " + this.materialName + ". ");
			}
			if (this.componentType == ComponentTypes.BOLT) {
				list.add(EnumChatFormatting.GRAY + "A small Bolt, constructed from " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.SCREW) {
				list.add(EnumChatFormatting.GRAY + "A 8mm Screw, fabricated out of some " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.GEAR) {
				list.add(EnumChatFormatting.GRAY + "A large Gear, constructed from " + this.materialName + ".");
			}
			if (this.componentType == ComponentTypes.RING) {
				list.add(EnumChatFormatting.GRAY + "A " + this.materialName + " Ring.");
			}
			if (this.componentMaterial.isRadioactive) {
				list.add(CORE.GT_Tooltip_Radioactive);
			}

		}

		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return this.componentMaterial.getRgbAsHex();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return this.componentMaterial.getLocalizedName() + this.componentType.DISPLAY_NAME;
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
			final boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(this.componentMaterial.vRadioationLevel, world, entityHolding);
	}

}
