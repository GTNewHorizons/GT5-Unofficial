package gtPlusPlus.core.item.base.misc;

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

public class BaseItemMisc extends Item{
	
	public final String displayName;
	public final String unlocalName;
	public final MiscTypes miscType;
	public final Object componentColour;
	public final String[] description;

	public BaseItemMisc(
			final String displayName,
			final short[] RGB,
			final int maxStackSize,
			final MiscTypes miscType,
			String[] description) {
		
		//Set-up the Misc Generic Item
		this.displayName = displayName;
		String unlocalName = Utils.sanitizeString(displayName);
		this.unlocalName = "item"+miscType.TYPE+unlocalName;
		this.miscType = miscType;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(maxStackSize);
		this.setTextureName(this.getCorrectTextures());
		if (RGB != null){
		this.componentColour = Utils.rgbtoHexValue(RGB[0], RGB[1], RGB[2]);
		}
		else {
			this.componentColour = null;
		}
		this.description = description;
		GameRegistry.registerItem(this, this.unlocalName);
		GT_OreDictUnificator.registerOre(miscType.getOreDictPrefix()+unlocalName, ItemUtils.getSimpleStack(this));
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return this.displayName+miscType.DISPLAY_NAME_SUFFIX;
	}
	
	private String getCorrectTextures(){
		return CORE.MODID + ":" + "item"+this.miscType.TYPE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.description != null){ //Incase I don't add one
			if (this.description.length > 0){ //Incase I somehow add a blank one
				for (int x=0;x<this.description.length;x++){
					list.add(EnumChatFormatting.GRAY+description[x]);					
				}
			}
		}		
		super.addInformation(stack, aPlayer, list, bool);
	}


	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		//Returns default colour if a custom one is not set.
		if (this.componentColour == null){
			return 16777215;
		}
		else {
			return (int) this.componentColour;
		}
		
		
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		//Nothing Fancy here yet.
	}








	public static enum MiscTypes {
		POTION("Potion", " Potion", "potion"),
		KEY("Key", " Key", "key"),
		BIGKEY("KeyBig", " Big Key", "bosskey"),
		BOTTLE("Bottle", " Bottle", "bottle"),
		GEM("Gem", " Gemstone", "gem"),
		MUSHROOM("Mushroom", " Mushroom", "mushroom");

		private String TYPE;
		private String DISPLAY_NAME_SUFFIX;
		private String OREDICT_PREFIX;
		
		private MiscTypes (final String LocalName, final String DisplayNameSuffix, final String OreDictPrefix){
			this.TYPE = LocalName;
			this.DISPLAY_NAME_SUFFIX = DisplayNameSuffix;
			this.OREDICT_PREFIX = OreDictPrefix;
		}

		public String getType(){
			return this.TYPE;
		}

		public String getName(){
			return this.DISPLAY_NAME_SUFFIX;
		}

		public String getOreDictPrefix(){
			return this.OREDICT_PREFIX;
		}

	}

}


