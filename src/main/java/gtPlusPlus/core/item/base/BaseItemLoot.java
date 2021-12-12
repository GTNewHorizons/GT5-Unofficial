package gtPlusPlus.core.item.base;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.Materials;

import gtPlusPlus.api.enums.Quality;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemLoot extends Item{

	private final String materialName;
	private final String unlocalName;
	private final LootTypes lootTypes;
	private Quality lootQuality;
	private final Materials lootMaterial;

	public BaseItemLoot(final LootTypes lootType, final Materials material) {
		this.lootTypes = lootType;
		this.lootMaterial = material;
		this.materialName = material.mDefaultLocalName;
		this.unlocalName = "item"+lootType.LOOT_TYPE+this.materialName;
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "item"+lootType.LOOT_TYPE);
	}

	public ItemStack generateLootStack(){
		this.lootQuality = Quality.getRandomQuality();
		return ItemUtils.getSimpleStack(this, 1);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return (this.materialName+this.lootTypes.DISPLAY_SUFFIX);
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(this.lootQuality.getQuality());

		/*if (componentMaterial.isRadioactive){
				list.add(CORE.GT_Tooltip_Radioactive);
			}*/

		super.addInformation(stack, aPlayer, list, bool);
	}


	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		final short[] temp = this.lootMaterial.mRGBa;
		return Utils.rgbtoHexValue(temp[0], temp[1], temp[2]);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		//EntityUtils.applyRadiationDamageToEntity(lootQuality.vRadioationLevel, world, entityHolding);
	}








	public static enum LootTypes {
		Sword("Sword", " Longsword", "sword"),
		Shortsword("Sword", " Short Blade", "blade"),
		Helmet("Helmet", " Medium Helm", "helmet"),
		Chestplate("Platebody", " Chestplate", "platebody"),
		Leggings("Platelegs", " Platelegs", "platelegs"),
		Boots("Boots", " Boots", "boots");
		private String LOOT_TYPE;
		private String DISPLAY_SUFFIX;
		private String OREDICT_NAME;
		private LootTypes (final String LocalName, final String DisplayName, final String OreDictName){
			this.LOOT_TYPE = LocalName;
			this.DISPLAY_SUFFIX = DisplayName;
			this.OREDICT_NAME = OreDictName;
		}
		public String getLootType(){
			return this.LOOT_TYPE;
		}
		public String getName(){
			return this.DISPLAY_SUFFIX;
		}
		public String getOreDictName(){
			return this.OREDICT_NAME;
		}
	}

}


