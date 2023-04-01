package gregtech.api.multitileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.util.GT_Util.setTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_HasMultiBlockMachineRelevantData;
import gregtech.common.render.GT_Renderer_Block;
import gregtech.common.render.IRenderedBlock;

public class MultiTileEntityBlockInternal extends Block implements IRenderedBlock {

    public MultiTileEntityRegistry mMultiTileEntityRegistry;

    public MultiTileEntityBlockInternal() {
        super(Material.anvil);
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        /* Do Nothing */
    }

    @Override
    public int getRenderType() {
        return GT_Renderer_Block.INSTANCE == null ? super.getRenderType() : GT_Renderer_Block.INSTANCE.mRenderID;
    }

    @Override
    public final String getUnlocalizedName() {
        return mMultiTileEntityRegistry.mNameInternal;
    }

    @Override
    public final String getLocalizedName() {
        return StatCollector.translateToLocal(mMultiTileEntityRegistry.mNameInternal + ".name");
    }

    public boolean placeBlock(World aWorld, int aX, int aY, int aZ, byte aSide, short aMetaData, NBTTagCompound aNBT,
            boolean aCauseBlockUpdates, boolean aForcePlacement) {
        final MultiTileEntityContainer aMTEContainer = mMultiTileEntityRegistry.getNewTileEntityContainer(
                aWorld,
                aX,
                aY,
                aZ,
                aMetaData,
                aNBT);
        if (aMTEContainer == null) return false;

        final Block tReplacedBlock = aWorld.getBlock(aX, aY, aZ);

        // This is some complicated bullshit Greg had to do to make his MTEs work right.
        // Set Block with reverse MetaData first.
        aWorld.setBlock(aX, aY, aZ, aMTEContainer.mBlock, 15 - aMTEContainer.mBlockMetaData, 2);
        // Make sure the Block has been set, yes I know setBlock has a true/false return value, but guess what, it is
        // not reliable in 0.0001% of cases! -Greg
        if (aWorld.getBlock(aX, aY, aZ) != aMTEContainer.mBlock) {
            aWorld.setBlock(aX, aY, aZ, Blocks.air, 0, 0);
            return false;
        }
        // TileEntity should not refresh yet! -Greg
        ((IMultiTileEntity) aMTEContainer.mTileEntity).setShouldRefresh(false);
        // Fake-Set the TileEntity first, bypassing a lot of checks. -Greg
        setTileEntity(aWorld, aX, aY, aZ, aMTEContainer.mTileEntity, false);
        // Now set the Block with the REAL MetaData. -Greg
        setTileEntity(aWorld, aX, aY, aZ, aMTEContainer.mBlock, aMTEContainer.mBlockMetaData, 0, false);
        // When the TileEntity is set now it SHOULD refresh! -Greg
        ((IMultiTileEntity) aMTEContainer.mTileEntity).setShouldRefresh(true);
        // But make sure again that the Block we have set was actually set properly, because 0.0001%! -Greg
        if (aWorld.getBlock(aX, aY, aZ) != aMTEContainer.mBlock) {
            aWorld.setBlock(aX, aY, aZ, Blocks.air, 0, 0);
            return false;
        }
        // And finally properly set the TileEntity for real! -Greg
        setTileEntity(aWorld, aX, aY, aZ, aMTEContainer.mTileEntity, aCauseBlockUpdates);
        // Yep, all this just to set one Block and its TileEntity properly... -Greg

        try {
            if (aMTEContainer.mTileEntity instanceof IMTE_HasMultiBlockMachineRelevantData) {
                if (((IMTE_HasMultiBlockMachineRelevantData) aMTEContainer.mTileEntity).hasMultiBlockMachineRelevantData())
                    GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("causeMachineUpdate", e);
        }

        try {
            if (!aWorld.isRemote && aCauseBlockUpdates) {
                aWorld.notifyBlockChange(aX, aY, aZ, tReplacedBlock);
                aWorld.func_147453_f(aX, aY, aZ, aMTEContainer.mBlock);
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("aCauseBlockUpdates", e);
        }

        try {
            ((IMultiTileEntity) aMTEContainer.mTileEntity).onTileEntityPlaced();
        } catch (Throwable e) {
            GT_FML_LOGGER.error("onTileEntityPlaced", e);
        }

        try {
            aWorld.func_147451_t /* updateAllLightTypes */(aX, aY, aZ);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("updateAllLightTypes", e);
        }
        return true;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, int aRenderPass, boolean[] aShouldSideBeRendered) {
        return null;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        // TODO: MTE(Texture)
        return null;
    }

    @Override
    public int getRenderPasses(Block aBlock) {
        return 0;
    }

    @Override
    public boolean usesRenderPass(int aRenderPass) {
        return true;
    }

    @Override
    public boolean setBlockBounds(Block aBlock, int aRenderPass) {
        return false;
    }

    @Override
    public IRenderedBlock passRenderingToObject(ItemStack aStack) {
        final TileEntity tTileEntity = mMultiTileEntityRegistry.getNewTileEntity(aStack);
        return tTileEntity instanceof IRenderedBlock ? (IRenderedBlock) tTileEntity : null;
    }

    @Override
    public IRenderedBlock passRenderingToObject(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return null;
    }
}
