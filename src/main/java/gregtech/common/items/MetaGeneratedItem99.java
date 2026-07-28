package gregtech.common.items;

import static gregtech.api.enums.OrePrefixes.cellMolten;
import static gregtech.api.enums.OrePrefixes.material;

import java.util.BitSet;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.google.common.collect.ImmutableList;
import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;

public class MetaGeneratedItem99 extends MetaGeneratedItem {

    public static MetaGeneratedItem99 INSTANCE;

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
    private final BitSet enabled = new BitSet();

    public MetaGeneratedItem99() {
        super("metaitem.99", (short) (10_000 + NUM_CRACKED_CELL_TYPES * 1_000), (short) 0);

        INSTANCE = this;

        for (int i = 0; i < 1000; i++) {
            Material material = generatedMaterial(i);
            int subId = MU.oldSubId(material);
            if (material == null || subId < 0 || subId >= 1_000) {
                continue;
            }

            if (MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_FLUID)
                && !MU.hasFlag(material, GTMaterialFlag.NO_SMELTING)
                && !MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)) {
                if (!cellMolten.mNotGeneratedItems.contains(material)) {
                    registerMolten(material, subId);
                }
                Material smeltInto = MU.smeltInto(material);
                int smeltSubId = MU.oldSubId(smeltInto);
                if (smeltInto != material && smeltSubId >= 0 && smeltSubId < 1_000) {
                    registerMolten(smeltInto, smeltSubId);
                }
            }

            if (MU.canBeCracked(material)) {
                registerCracked(material, subId);
            }
        }

        // We're not going to use these BitSets, so clear them to save memory.
        mEnabledItems.clear();
        mVisibleItems.clear();
    }

    private void registerMolten(Material material, int i) {
        if (MetaGeneratedItemX32.DUMP_MODE) {
            MetaGeneratedItemX32.DUMP_VARIANTS.add(
                new MetaGeneratedItemX32.LegacyVariant(
                    "metaitem.99",
                    cellMolten.getName(),
                    MU.internalName(material),
                    i));
        }
        if (!MetaGeneratedItemX32.DUMP_MODE && MU.isCutOver(cellMolten, material)) return;

        ItemStack tStack = new ItemStack(this, 1, i);
        enabled.set(i);

        if (cellMolten.isUnifiable()) {
            GTOreDictUnificator.set(cellMolten, material, tStack);
        } else {
            GTOreDictUnificator.registerOre(cellMolten.oreDictName(material), tStack);
        }
    }

    private void registerCracked(Material material, int i) {
        int offset = 10_000;
        for (OrePrefixes prefix : CRACKED_CELL_TYPES) {
            if (MetaGeneratedItemX32.DUMP_MODE) {
                MetaGeneratedItemX32.DUMP_VARIANTS.add(
                    new MetaGeneratedItemX32.LegacyVariant(
                        "metaitem.99",
                        prefix.getName(),
                        MU.internalName(material),
                        offset + i));
            }
            if (MetaGeneratedItemX32.DUMP_MODE || !MU.isCutOver(prefix, material)) {
                ItemStack tStack = new ItemStack(this, 1, offset + i);
                enabled.set(offset + i);

                if (prefix.isUnifiable()) {
                    GTOreDictUnificator.set(prefix, material, tStack);
                } else {
                    GTOreDictUnificator.registerOre(prefix.oreDictName(material), tStack);
                }
            }

            offset += 1_000;
        }
    }

    /** Returns null for item damage out of bounds. */
    public OrePrefixes getOrePrefix(int damage) {
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
        Material material = getMaterial(aStack.getItemDamage());
        if (prefix == cellMolten) {
            return MU.moltenRgba(material);
        }
        short[] rgba = MU.rgba(material);
        return rgba != null ? rgba : MU.rgba(Materials2Materials.NULL);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        final int damage = aStack.getItemDamage();
        final OrePrefixes prefix = getOrePrefix(damage);
        final Material material = getMaterial(damage);
        if (prefix != null && material != null) return prefix.getLocalizedNameForItem(MU.internalName(material));
        return super.getItemStackDisplayName(aStack);
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
        Material material = getMaterial(aMetaData);
        OrePrefixes prefix = getOrePrefix(aMetaData);
        if (material != null && prefix != null) {
            return MU.iconSet(material).mTextures[prefix.getTextureIndex()];
        }
        return null;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        OrePrefixes prefix = getOrePrefix(aStack.getItemDamage());
        if (prefix != null) {
            return prefix.getDefaultStackSize();
        } else {
            return 64;
        }
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        if (!isMaterialItem(aStack)) return;
        final int damage = aStack.getItemDamage();
        final Material material = getMaterial(damage);
        final OrePrefixes prefix = getOrePrefix(damage);
        if (material == null || prefix == null) return;
        MU.addTooltipsOf(material, aList);
    }
}
