package gregtech.api.items;

import static gregtech.api.enums.GTValues.M;

import java.util.Arrays;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.render.items.GeneratedMaterialRenderer;

/**
 * @author Gregorius Techneticies
 *         <p/>
 *         One Item for everything!
 *         <p/>
 *         This brilliant Item Class is used for automatically generating all possible variations of Material Items,
 *         like Dusts, Ingots, Gems, Plates and similar. It saves me a ton of work, when adding Items, because I always
 *         have to make a new Item SubType for each OreDict Prefix, when adding a new Material.
 *         <p/>
 *         As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be custom
 *         defined, just to save space and MetaData.
 *         <p/>
 *         These Items can also have special RightClick abilities, electric Charge or even be set to become a Food alike
 *         Item.
 */
public abstract class MetaGeneratedItemX32 extends MetaGeneratedItem {

    protected final OrePrefixes[] mGeneratedPrefixList;

    /**
     * Creates the Item using these Parameters.
     *
     * @param aUnlocalized         The Unlocalized Name of this Item.
     * @param aGeneratedPrefixList The OreDict Prefixes you want to have generated.
     */
    public MetaGeneratedItemX32(String aUnlocalized, OrePrefixes... aGeneratedPrefixList) {
        super(aUnlocalized, (short) 32000, (short) 766);
        mGeneratedPrefixList = Arrays.copyOf(aGeneratedPrefixList, 32);

        for (int i = 0; i < 32000; i++) {
            OrePrefixes tPrefix = mGeneratedPrefixList[i / 1000];
            if (tPrefix == null) continue;
            if (tPrefix == OrePrefixes.___placeholder___) continue;
            Materials tMaterial = GregTechAPI.sGeneratedMaterials[i % 1000];
            if (tMaterial == null) continue;
            if (doesMaterialAllowGeneration(tPrefix, tMaterial)) {
                ItemStack tStack = new ItemStack(this, 1, i);
                if (tPrefix.isUnifiable()) {
                    GTOreDictUnificator.set(tPrefix, tMaterial, tStack);
                } else {
                    GTOreDictUnificator.registerOre(tPrefix.get(tMaterial), tStack);
                }
                if ((tPrefix == OrePrefixes.stick || tPrefix == OrePrefixes.wireFine || tPrefix == OrePrefixes.ingot)
                    && (tMaterial == Materials.Lead || tMaterial == Materials.Tin
                        || tMaterial == Materials.SolderingAlloy)) {
                    GregTechAPI.sSolderingMetalList.add(tStack);
                    GTModHandler.registerBoxableItemToToolBox(tStack);
                }
            }
        }
    }

    /* ---------- OVERRIDEABLE FUNCTIONS ---------- */

    /**
     * @return the Color Modulation the Material is going to be rendered with.
     */
    @Override
    public short[] getRGBa(ItemStack aStack) {
        if (!Materials.isMaterialItem(getDamage(aStack))) {
            return Materials._NULL.mRGBa;
        }
        Materials tMaterial = GregTechAPI.sGeneratedMaterials[getDamage(aStack) % 1000];
        if (tMaterial == null) {
            return Materials._NULL.mRGBa;
        }
        return tMaterial.mRGBa;
    }

    /**
     * @param aPrefix   this can be null, you have to return false in that case
     * @param aMaterial this can be null, you have to return false in that case
     * @return if this Item should be generated and visible.
     */
    public boolean doesMaterialAllowGeneration(OrePrefixes aPrefix, Materials aMaterial) {
        // You have to check for at least these Conditions in every Case! So add a super Call like the following for
        // this before executing your Code:
        // if (!super.doesMaterialAllowGeneration(aPrefix, aMaterial)) return false;
        return aPrefix != null && aPrefix.doGenerateItem(aMaterial);
    }

    /* ---------- OVERRIDEABLE FUNCTIONS ---------- */
    /**
     * @param aMetaData a Index from [0 - 31999]
     * @param aMaterial the Material
     * @return an Icon Container for the Item Display.
     */
    public final IIconContainer getIconContainer(int aMetaData, Materials aMaterial) {
        final OrePrefixes prefixes = getOrePrefix(aMetaData);
        return prefixes != null && prefixes.getTextureIndex() >= 0
            ? aMaterial.mIconSet.mTextures[prefixes.getTextureIndex()]
            : null;
    }

    /**
     * @param aPrefix         always != null
     * @param aMaterial       always != null
     * @param aDoShowAllItems this is the Configuration Setting of the User, if he wants to see all the Stuff like Tiny
     *                        Dusts or Crushed Ores as well.
     * @return if this Item should be visible in NEI or Creative
     */
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return true;
    }

    /* ---------- INTERNAL OVERRIDES ---------- */

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        final int damage = aStack.getItemDamage();
        final OrePrefixes prefix = getOrePrefix(damage);
        final Materials material = getMaterial(damage);
        if (prefix != null && material != null) return prefix.getLocalizedNameForItem(material);
        return super.getItemStackDisplayName(aStack);
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        if (Materials.isMaterialItem(aDamage)) {
            Materials aMaterial = getMaterial(aDamage);
            if (aMaterial != null && aMaterial != Materials.Empty && aMaterial != Materials._NULL) {
                OrePrefixes aPrefix = getOrePrefix(aDamage);
                if (aPrefix != null) return GTUtility.copyAmount(1, aPrefix.mContainerItem);
            }
        }
        return null;
    }

    @Override
    public final IIconContainer getIconContainer(int aMetaData) {
        if (!Materials.isMaterialItem(aMetaData)) return null;
        final Materials materials = getMaterial(aMetaData);
        return materials == null ? null : getIconContainer(aMetaData, materials);
    }

    @Override
    public GeneratedMaterialRenderer getMaterialRenderer(int aMetaData) {
        if (!Materials.isMaterialItem(aMetaData)) return null;
        final Materials materials = getMaterial(aMetaData);
        return materials == null ? null : materials.renderer;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        for (int i = 0; i < 32000; i++) {
            OrePrefixes aPrefix = getOrePrefix(i);
            Materials aMaterial = getMaterial(i);
            if (aPrefix != null && aMaterial != null) {
                if (doesMaterialAllowGeneration(aPrefix, aMaterial)
                    && doesShowInCreative(aPrefix, aMaterial, GregTechAPI.sDoShowAllItemsInCreative)) {
                    ItemStack tStack = new ItemStack(this, 1, i);
                    isItemStackUsable(tStack);
                    aList.add(tStack);
                }
            }
        }
        super.getSubItems(aItem, aCreativeTab, aList);
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 0) return null;
        if (aMetaData < 32000) {
            Materials tMaterial = getMaterial(aMetaData);
            if (tMaterial == null) return null;
            IIconContainer tIcon = getIconContainer(aMetaData, tMaterial);
            if (tIcon != null) return tIcon.getIcon();
            return null;
        }
        return aMetaData - 32000 < mIconList.length ? mIconList[aMetaData - 32000][0] : null;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        final int damage = getDamage(stack);
        final int stackSize = super.getItemStackLimit(stack);
        if (damage >= 32_000) return stackSize;

        final OrePrefixes prefix = getOrePrefix(damage);
        if (prefix == null) return stackSize;

        return Math.min(stackSize, prefix.getDefaultStackSize());
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        if (!Materials.isMaterialItem(getDamage(aStack))) return;
        final int damage = aStack.getItemDamage();
        final Materials material = getMaterial(damage);
        final OrePrefixes prefix = getOrePrefix(damage);
        if (material == null || prefix == null) return;
        material.addTooltips(aList, prefix.getMaterialAmount() / M);
    }

    public @Nullable Materials getMaterial(int damage) {
        if (!Materials.isMaterialItem(damage)) return null;
        return GregTechAPI.sGeneratedMaterials[damage % 1000];
    }

    private @Nullable OrePrefixes getOrePrefix(int damage) {
        if (!Materials.isMaterialItem(damage)) return null;
        final int i = damage / 1000;
        if (i >= mGeneratedPrefixList.length) return null;
        return mGeneratedPrefixList[i];
    }
}
