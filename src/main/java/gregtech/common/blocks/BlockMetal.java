package gregtech.common.blocks;

import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.client.DynamicLangManager;

/// One of 13 hand-curated batches of up to 16 [Material]s (metadata = array index). Every batch is instantiated
/// and registers every slot, whether or not its materials cut over to the MaterialLib `block` shape (see
/// [BlockShapes]), because multiblock casing matchers, a machine-block-update listener and a client-side icon
/// fallback hold these instances by `Block` identity. For a cut-over material only the canonical
/// [gregtech.api.util.GTOreDictUnificator] association moves to the MaterialLib stack, and that slot is hidden
/// from NEI; the block and item stay functional at every slot.
///
/// A null batch entry is a retired slot: the material it held is gone, but the index stays so the metadata of
/// every slot after it is unchanged for saved blocks. Such a slot generates nothing and is hidden from NEI.
public class BlockMetal extends BlockStorage {

    public Material[] mMats;
    public OrePrefixes mPrefix;
    public IIconContainer[] mBlockIcons;
    public boolean mHideBlocks;
    public static boolean mNEIisLoaded = NotEnoughItems.isModLoaded();

    public BlockMetal(String aName, Material[] aMats, OrePrefixes aPrefix, IIconContainer[] aBlockIcons) {
        super(ItemStorage.class, aName, net.minecraft.block.material.Material.iron);
        mMats = aMats;
        mPrefix = aPrefix;
        mBlockIcons = aBlockIcons;
        mHideBlocks = mNEIisLoaded;

        for (int i = 0; i < aMats.length; i++) {
            Material material = aMats[i];
            if (material == null) {
                if (mNEIisLoaded) codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i));
                continue;
            }
            if (MaterialUtils.oldSubId(material) > 0) {
                ItemStack cutover = MaterialParts.stack(aPrefix, material, 1);
                ItemStack canonicalStack = cutover != null ? cutover : new ItemStack(this, 1, i);
                DynamicLangManager.addStack(new ItemStack(this, 1, i));
                if (aPrefix.isUnifiable()) {
                    GTOreDictUnificator.set(aPrefix, material, canonicalStack);
                } else {
                    GTOreDictUnificator.registerOre(aPrefix, material, canonicalStack);
                }
                if (cutover != null && mNEIisLoaded) {
                    codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i));
                }
            }
        }
        if (aMats.length < 16 && mNEIisLoaded) {
            for (int i = aMats.length; i < 16; i++) codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getLocalizedName(int meta) {
        Material material = GTDataUtils.getIndexSafe(mMats, meta);

        if (material == null) material = Materials.NULL;

        return OrePrefixes.block.getLocalizedNameForItem(MaterialUtils.internalName(material));
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16) && aMeta < mMats.length) {
            return mBlockIcons[aMeta].getIcon();
        }
        return null;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aMetaData)) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

}
