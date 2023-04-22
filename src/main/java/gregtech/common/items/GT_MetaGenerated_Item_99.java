package gregtech.common.items;

import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.OrePrefixes.cellMolten;

import java.util.BitSet;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;

public class GT_MetaGenerated_Item_99 extends GT_MetaGenerated_Item {

    public static GT_MetaGenerated_Item_99 INSTANCE;

    /**
     * Ore prefixes appear in this list in the order in which they will be assigned ID blocks.
     *
     * <p>
     * In order to avoid breaking existing worlds, the entries in this list must not be re-ordered! The only safe
     * modification that can be made to this list is adding new entries to the end.
     */
    private static final ImmutableList<OrePrefixes> CRACKED_CELL_TYPES = ImmutableList.of(
        OrePrefixes.cellHydroCracked1,
        OrePrefixes.cellHydroCracked2,
        OrePrefixes.cellHydroCracked3,
        OrePrefixes.cellSteamCracked1,
        OrePrefixes.cellSteamCracked2,
        OrePrefixes.cellSteamCracked3);

    private static final int NUM_CRACKED_CELL_TYPES = CRACKED_CELL_TYPES.size();

    /**
     * Assignment of metadata IDs: 0 - 999: Molten cells 10_000 - 15_999: Cracked fluid cells (# IDs used is
     * NUM_CRACKED_CELL_TYPES * 1_000; update this if you add any)
     */
    private BitSet enabled = new BitSet();

    public GT_MetaGenerated_Item_99() {
        super("metaitem.99", (short) (10_000 + NUM_CRACKED_CELL_TYPES * 1_000), (short) 0);

        INSTANCE = this;

        for (Materials tMaterial : GregTech_API.sGeneratedMaterials) {
            if (tMaterial == null || tMaterial.mMetaItemSubID < 0 || tMaterial.mMetaItemSubID >= 1_000) {
                continue;
            }

            if ((tMaterial.contains(SubTag.SMELTING_TO_FLUID)) && (!tMaterial.contains(SubTag.NO_SMELTING))
                && !tMaterial.contains(SubTag.SMELTING_TO_GEM)) {
                registerMolten(tMaterial, tMaterial.mMetaItemSubID);
                if (tMaterial.mSmeltInto != tMaterial && tMaterial.mSmeltInto.mMetaItemSubID >= 0
                    && tMaterial.mSmeltInto.mMetaItemSubID < 1_000) {
                    registerMolten(tMaterial.mSmeltInto, tMaterial.mSmeltInto.mMetaItemSubID);
                }
            }

            if (tMaterial.canBeCracked()) {
                registerCracked(tMaterial, tMaterial.mMetaItemSubID);
            }
        }

        // We're not going to use these BitSets, so clear them to save memory.
        mEnabledItems.clear();
        mVisibleItems.clear();
    }

    private void registerMolten(Materials tMaterial, int i) {
        ItemStack tStack = new ItemStack(this, 1, i);
        enabled.set(i);

        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName(tStack) + ".name",
            cellMolten.getDefaultLocalNameFormatForItem(tMaterial));
        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName(tStack) + ".tooltip",
            tMaterial.getToolTip(cellMolten.mMaterialAmount / M));

        if (cellMolten.mIsUnificatable) {
            GT_OreDictUnificator.set(cellMolten, tMaterial, tStack);
        } else {
            GT_OreDictUnificator.registerOre(cellMolten.get(tMaterial), tStack);
        }
    }

    private void registerCracked(Materials tMaterial, int i) {
        int offset = 10_000;
        for (OrePrefixes prefix : CRACKED_CELL_TYPES) {
            ItemStack tStack = new ItemStack(this, 1, offset + i);
            enabled.set(offset + i);

            GT_LanguageManager.addStringLocalization(
                getUnlocalizedName(tStack) + ".name",
                prefix.getDefaultLocalNameFormatForItem(tMaterial));
            GT_LanguageManager.addStringLocalization(
                getUnlocalizedName(tStack) + ".tooltip",
                tMaterial.getToolTip(prefix.mMaterialAmount / M));

            if (prefix.mIsUnificatable) {
                GT_OreDictUnificator.set(prefix, tMaterial, tStack);
            } else {
                GT_OreDictUnificator.registerOre(prefix.get(tMaterial), tStack);
            }

            offset += 1_000;
        }
    }

    /** Returns null for item damage out of bounds. */
    private Materials getMaterial(int damage) {
        if (damage < 0) {
            return null;
        }
        return GregTech_API.sGeneratedMaterials[damage % 1_000];
    }

    /** Returns null for item damage out of bounds. */
    private OrePrefixes getOrePrefix(int damage) {
        if (damage < 0) {
            return null;
        } else if (damage < 1_000) {
            return cellMolten;
        } else if (damage >= 10_000 && damage < 10_000 + (NUM_CRACKED_CELL_TYPES * 1_000)) {
            return CRACKED_CELL_TYPES.get((damage / 1_000) - 10);
        }
        return null;
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        OrePrefixes prefix = getOrePrefix(aStack.getItemDamage());
        Materials material = getMaterial(aStack.getItemDamage());
        if (material == null) {
            material = Materials._NULL;
        }

        if (prefix == cellMolten) {
            return material.mMoltenRGBa;
        } else {
            return material.mRGBa;
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        Materials material = getMaterial(aStack.getItemDamage());
        if (material != null) {
            return material.getLocalizedNameForItem(aName);
        }
        return aName;
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        OrePrefixes prefix = getOrePrefix(aStack.getItemDamage());
        if (prefix != null) {
            return prefix.mContainerItem;
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        enabled.stream()
            .mapToObj(i -> new ItemStack(this, 1, i))
            .forEach(aList::add);
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        IIconContainer iconContainer = getIconContainer(aMetaData);
        if (iconContainer != null) {
            return iconContainer.getIcon();
        }
        return null;
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        Materials material = getMaterial(aMetaData);
        OrePrefixes prefix = getOrePrefix(aMetaData);
        if (material != null && prefix != null) {
            return material.mIconSet.mTextures[prefix.mTextureIndex];
        }
        return null;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        OrePrefixes prefix = getOrePrefix(aStack.getItemDamage());
        if (prefix != null) {
            return prefix.mDefaultStackSize;
        } else {
            return 64;
        }
    }
}
