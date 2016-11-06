package gtPlusPlus.core.item.base;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Quality;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseItemLoot extends Item{

	private final String materialName;
	private final String unlocalName;
	private final LootTypes lootTypes;
	private Quality lootQuality;
	private final Materials lootMaterial;

	public BaseItemLoot(LootTypes lootType, Materials material) {
		this.lootTypes = lootType;
		this.lootMaterial = material;
		this.materialName = material.mDefaultLocalName;		
		this.unlocalName = "item"+lootType.LOOT_TYPE+this.materialName;		
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "item"+lootType.LOOT_TYPE);
	}
	
	public ItemStack generateLootStack(){
		lootQuality = Quality.getRandomQuality();
		return ItemUtils.getSimpleStack(this, 1);
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return (materialName+lootTypes.DISPLAY_SUFFIX);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add(lootQuality.getQuality());
			
		/*if (componentMaterial.isRadioactive){
				list.add(CORE.GT_Tooltip_Radioactive);
			}*/		

		super.addInformation(stack, aPlayer, list, bool);
	}


	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		short[] temp = lootMaterial.mRGBa;
		return Utils.rgbtoHexValue(temp[0], temp[1], temp[2]);
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
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
			return LOOT_TYPE;
		}	 
		public String getName(){
			return DISPLAY_SUFFIX;
		}
		public String getOreDictName(){
		return OREDICT_NAME;
		}
	}

}


