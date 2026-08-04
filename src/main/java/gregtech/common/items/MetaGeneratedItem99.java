package gregtech.common.items;

import static gregtech.api.enums.OrePrefixes.cellMolten;

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
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
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

    /// The fluid fill and the two untinted containers, the same textures
    /// [gregtech.api.enums.materials.CellShapes] gives the cell shapes. The fill is one shared texture rather than
    /// the material's own: every set that ships bespoke cell art has cut over to those shapes, leaving only
    /// materials whose fill is the plain rectangle this is.
    private static final String CELL_FILL = "gregtech:materials/NONE/cell";
    private static final String CELL_BASE = "gregtech:materials/cell_base";
    private static final String CELL_PLASMA_BASE = "gregtech:materials/cell_plasma_base";

    /**
     * Assignment of metadata IDs: 0 - 999: Molten cells 10_000 - 15_999: Cracked fluid cells (# IDs used is
     * NUM_CRACKED_CELL_TYPES * 1_000; update this if you add any)
     */
    private final BitSet enabled = new BitSet();

    /// The tinted fluid fill, drawn under an untinted container the way the legacy cell art paired them. Built
    /// here rather than per lookup because a [gregtech.client.iconContainers.items.GTCustomItemIconContainer]
    /// registers itself for the texture stitch when it is constructed, and nothing may be added after it.
    private final IIconContainer moltenCellIcon = Textures.ItemIcons.custom(CELL_FILL, CELL_PLASMA_BASE);
    private final IIconContainer crackedCellIcon = Textures.ItemIcons.custom(CELL_FILL, CELL_BASE);

    public MetaGeneratedItem99() {
        super("metaitem.99", (short) (10_000 + NUM_CRACKED_CELL_TYPES * 1_000), (short) 0);

        INSTANCE = this;

        for (int i = 0; i < 1000; i++) {
            Material material = generatedMaterial(i);
            int subId = MaterialUtils.oldSubId(material);
            if (material == null || subId < 0 || subId >= 1_000) {
                continue;
            }

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_FLUID)
                && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING)
                && !MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)) {
                if (!cellMolten.mNotGeneratedItems.contains(material)) {
                    registerMolten(material, subId);
                }
                Material smeltInto = MaterialUtils.smeltInto(material);
                int smeltSubId = MaterialUtils.oldSubId(smeltInto);
                if (smeltInto != material && smeltSubId >= 0 && smeltSubId < 1_000) {
                    registerMolten(smeltInto, smeltSubId);
                }
            }

            if (MaterialUtils.canBeCracked(material)) {
                registerCracked(material, subId);
            }
        }

        // We're not going to use these BitSets, so clear them to save memory.
        mEnabledItems.clear();
        mVisibleItems.clear();
    }

    private void registerMolten(Material material, int i) {
        if (MaterialParts.isCutOver(cellMolten, material)) return;

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
            if (!MaterialParts.isCutOver(prefix, material)) {
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
            return MaterialUtils.moltenRgba(material);
        }
        short[] rgba = MaterialUtils.rgba(material);
        return rgba != null ? rgba : MaterialUtils.rgba(Materials.NULL);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        final int damage = aStack.getItemDamage();
        final OrePrefixes prefix = getOrePrefix(damage);
        final Material material = getMaterial(damage);
        if (prefix != null && material != null)
            return prefix.getLocalizedNameForItem(MaterialUtils.internalName(material));
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
        if (material == null || prefix == null) return null;
        return prefix == cellMolten ? moltenCellIcon : crackedCellIcon;
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
        MaterialUtils.addTooltips(material, aList);
    }
}
