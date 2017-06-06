package gtPlusPlus.xmod.gregtech.api.items;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.*;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * @author Gregorius Techneticies
 *         <p/>
 *         One Item for everything!
 *         <p/>
 *         This brilliant Item Class is used for automatically generating all possible variations of Material Items, like Dusts, Ingots, Gems, Plates and similar.
 *         It saves me a ton of work, when adding Items, because I always have to make a new Item SubType for each OreDict Prefix, when adding a new Material.
 *         <p/>
 *         As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be custom defined, just to save space and MetaData.
 *         <p/>
 *         These Items can also have special RightClick abilities, electric Charge or even be set to become a Food alike Item.
 */
public abstract class Gregtech_MetaItem_X32 extends Gregtech_MetaItem {

	protected final OrePrefixes[] mGeneratedPrefixList;

	/**
	 * Creates the Item using these Parameters.
	 *
	 * @param aUnlocalized         The Unlocalized Name of this Item.
	 * @param aGeneratedPrefixList The OreDict Prefixes you want to have generated.
	 */
	public Gregtech_MetaItem_X32(final String aUnlocalized, final OrePrefixes... aGeneratedPrefixList) {
		super(aUnlocalized, (short) 32000, (short) 766);
		this.mGeneratedPrefixList = Arrays.copyOf(aGeneratedPrefixList, 32);

		for (int i = 0; i < 32000; i++) {
			final OrePrefixes tPrefix = this.mGeneratedPrefixList[i / 1000];
			if (tPrefix == null) {
				continue;
			}
			final Materials tMaterial = GregTech_API.sGeneratedMaterials[i % 1000];
			if (tMaterial == null) {
				continue;
			}
			if (this.doesMaterialAllowGeneration(tPrefix, tMaterial)) {
				final ItemStack tStack = new ItemStack(this, 1, i);
				GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".name", this.getDefaultLocalization(tPrefix, tMaterial, i));
				GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".tooltip", tMaterial.getToolTip(tPrefix.mMaterialAmount / GT_Values.M));
				if (tPrefix.mIsUnificatable) {
					GT_OreDictUnificator.set(tPrefix, tMaterial, tStack);
				} else {
					GT_OreDictUnificator.registerOre(tPrefix.get(tMaterial), tStack);
				}
				if (((tPrefix == OrePrefixes.stick) || (tPrefix == OrePrefixes.wireFine)) && ((tMaterial == Materials.Lead) || (tMaterial == Materials.Tin) || (tMaterial == Materials.SolderingAlloy))) {
					GregTech_API.sSolderingMetalList.add(tStack);
				}
			}
		}
	}

	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */

	/**
	 * @return the Color Modulation the Material is going to be rendered with.
	 */
	@Override
	public short[] getRGBa(final ItemStack aStack) {
		final Materials tMaterial = GregTech_API.sGeneratedMaterials[this.getDamage(aStack) % 1000];
		return tMaterial == null ? Materials._NULL.mRGBa : tMaterial.mRGBa;
	}

	/**
	 * @param aPrefix   this can be null, you have to return false in that case
	 * @param aMaterial this can be null, you have to return false in that case
	 * @return if this Item should be generated and visible.
	 */
	public boolean doesMaterialAllowGeneration(final OrePrefixes aPrefix, final Materials aMaterial) {
		// You have to check for at least these Conditions in every Case! So add a super Call like the following for this before executing your Code:
		// if (!super.doesMaterialAllowGeneration(aPrefix, aMaterial)) return false;
		return (aPrefix != null) && (aMaterial != null) && aPrefix.doGenerateItem(aMaterial);
	}

	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */

	/**
	 * @param aPrefix   the OreDict Prefix
	 * @param aMaterial the Material
	 * @param aMetaData a Index from [0 - 31999]
	 * @return the Localized Name when default LangFiles are used.
	 */
	public String getDefaultLocalization(final OrePrefixes aPrefix, final Materials aMaterial, final int aMetaData) {
		return aPrefix.getDefaultLocalNameForItem(aMaterial);
	}

	/**
	 * @param aMetaData a Index from [0 - 31999]
	 * @param aMaterial the Material
	 * @return an Icon Container for the Item Display.
	 */
	public final IIconContainer getIconContainer(final int aMetaData, final Materials aMaterial) {
		return (this.mGeneratedPrefixList[aMetaData / 1000] != null) && (this.mGeneratedPrefixList[aMetaData / 1000].mTextureIndex >= 0) ? aMaterial.mIconSet.mTextures[this.mGeneratedPrefixList[aMetaData / 1000].mTextureIndex] : null;
	}

	/**
	 * @param aPrefix         always != null
	 * @param aMaterial       always != null
	 * @param aDoShowAllItems this is the Configuration Setting of the User, if he wants to see all the Stuff like Tiny Dusts or Crushed Ores as well.
	 * @return if this Item should be visible in NEI or Creative
	 */
	public boolean doesShowInCreative(final OrePrefixes aPrefix, final Materials aMaterial, final boolean aDoShowAllItems) {
		return true;
	}

	/* ---------- INTERNAL OVERRIDES ---------- */

	@Override
	public ItemStack getContainerItem(final ItemStack aStack) {
		final int aDamage = aStack.getItemDamage();
		if ((aDamage < 32000) && (aDamage >= 0)) {
			final Materials aMaterial = GregTech_API.sGeneratedMaterials[aDamage % 1000];
			if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
				final OrePrefixes aPrefix = this.mGeneratedPrefixList[aDamage / 1000];
				if (aPrefix != null) {
					return GT_Utility.copyAmount(1, aPrefix.mContainerItem);
				}
			}
		}
		return null;
	}

	@Override
	public final IIconContainer getIconContainer(final int aMetaData) {
		return GregTech_API.sGeneratedMaterials[aMetaData % 1000] == null ? null : this.getIconContainer(aMetaData, GregTech_API.sGeneratedMaterials[aMetaData % 1000]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final void getSubItems(final Item var1, final CreativeTabs aCreativeTab, final List aList) {
		for (int i = 0; i < 32000; i++) {
			if (this.doesMaterialAllowGeneration(this.mGeneratedPrefixList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000]) && this.doesShowInCreative(this.mGeneratedPrefixList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000], GregTech_API.sDoShowAllItemsInCreative)) {
				final ItemStack tStack = new ItemStack(this, 1, i);
				this.isItemStackUsable(tStack);
				aList.add(tStack);
			}
		}
		super.getSubItems(var1, aCreativeTab, aList);
	}

	@Override
	public final IIcon getIconFromDamage(final int aMetaData) {
		if (aMetaData < 0) {
			return null;
		}
		if (aMetaData < 32000) {
			final Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) {
				return null;
			}
			final IIconContainer tIcon = this.getIconContainer(aMetaData, tMaterial);
			if (tIcon != null) {
				return tIcon.getIcon();
			}
			return null;
		}
		return (aMetaData - 32000) < this.mIconList.length ? this.mIconList[aMetaData - 32000][0] : null;
	}

	@Override
	public int getItemStackLimit(final ItemStack aStack) {
		final int tDamage = this.getDamage(aStack);
		if ((tDamage < 32000) && (this.mGeneratedPrefixList[tDamage / 1000] != null)) {
			return Math.min(super.getItemStackLimit(aStack), this.mGeneratedPrefixList[tDamage / 1000].mDefaultStackSize);
		}
		return super.getItemStackLimit(aStack);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
		if (stack.getDisplayName().contains("ULV")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(200, 180, 180);
		}
		else if (stack.getDisplayName().contains("LuV")){
			HEX_OxFFFFFF = 0xffffcc;
		}
		else if (stack.getDisplayName().contains("ZPM")){
			HEX_OxFFFFFF = 0xffe600;
		}
		else if (stack.getDisplayName().contains("UV")){
			HEX_OxFFFFFF = 0xffb300;
		}
		else if (stack.getDisplayName().contains("MAX")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(MathUtils.randInt(220, 250), MathUtils.randInt(221, 251), MathUtils.randInt(220, 250));
		}
		else if (stack.getDisplayName().contains("Sodium")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(90, 90, 255);
		}
		else if (stack.getDisplayName().contains("Cadmium")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(150, 150, 80);
		}
		else if (stack.getDisplayName().contains("Lithium")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(225, 220, 255);
		}
		else if (stack.getDisplayName().contains("Wrought")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(200, 180, 180);
		}
		else if (stack.getDisplayName().contains("Bronze")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(255, 128, 0);
		}
		else if (stack.getDisplayName().contains("Brass")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(255, 180, 0);
		}
		else if (stack.getDisplayName().contains("Invar")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(180, 180, 120);
		}
		else {
			HEX_OxFFFFFF = 0xffffff;
		}
		return HEX_OxFFFFFF;
	}
}