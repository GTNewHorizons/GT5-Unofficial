package gregtech.common.blocks;

import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.material.MU;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTOreDictUnificator;

/// One of 13 hand-curated batches of up to 16 [Materials] (metadata = array index), instantiated unconditionally
/// in `LoaderGTBlockFluid` regardless of whether any of its materials cut over to the MaterialLib `block` shape
/// (see [gregtech.api.enums.materials2.Materials2BlockShapes]) -- unlike the item cutover, which skips
/// constructing a legacy slot entirely, several of these 13 instances are hard-referenced by `Block` identity
/// from multiblock casing matchers, a machine-block-update listener, and a client-side icon fallback (see
/// `scripts/mu/gen_materials.py`'s `BLOCK_CUTOVER_EXCLUDED`), so the instances themselves must keep existing and
/// registering exactly as before for every material, cut over or not. Only the canonical
/// [gregtech.api.util.GTOreDictUnificator] association moves to the MaterialLib stack for a cut-over material's
/// slot (and that slot is hidden from NEI); the legacy item and block remain fully functional at every slot.
public class BlockMetal extends BlockStorage {

    public Materials[] mMats;
    public OrePrefixes mPrefix;
    public IIconContainer[] mBlockIcons;
    public boolean mHideBlocks;
    public static boolean mNEIisLoaded = NotEnoughItems.isModLoaded();

    public BlockMetal(String aName, Materials[] aMats, OrePrefixes aPrefix, IIconContainer[] aBlockIcons) {
        super(ItemStorage.class, aName, Material.iron);
        mMats = aMats;
        mPrefix = aPrefix;
        mBlockIcons = aBlockIcons;
        mHideBlocks = mNEIisLoaded;

        for (int i = 0; i < aMats.length; i++) {
            if (aMats[i].mMetaItemSubID > 0 && aMats[i].mHasParentMod) {
                Materials materials = aMats[i];
                boolean cutOver = MU.isCutOver(aPrefix, materials);
                ItemStack canonicalStack = cutOver ? MU.stack(aPrefix, materials, 1) : new ItemStack(this, 1, i);
                if (aPrefix.isUnifiable()) {
                    GTOreDictUnificator.set(aPrefix, materials, canonicalStack);
                } else {
                    GTOreDictUnificator.registerOre(aPrefix.get(materials), canonicalStack);
                }
                if (cutOver && mNEIisLoaded) {
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
        Materials material = GTDataUtils.getIndexSafe(mMats, meta);

        if (material == null) material = Materials._NULL;

        return OrePrefixes.block.getLocalizedNameForItem(material);
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
