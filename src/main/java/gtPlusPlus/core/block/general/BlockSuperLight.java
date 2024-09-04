package gtPlusPlus.core.block.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;

public class BlockSuperLight extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    public BlockSuperLight() {
        super(Material.circuits);
        this.setBlockName("blockSuperLight");
        this.setCreativeTab(CreativeTabs.tabRedstone);
        GameRegistry.registerBlock(this, "blockSuperLight");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int ordinalSide, final int meta) {
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "SwirlBigBlue");
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World aWorld, int p_149915_2_) {
        return new TileEntitySuperLight();
    }

    public static class TileEntitySuperLight extends TileEntity {

        private long mCreated;

        private long mLastUpdateTick = 0;

        private int[][][][] aLitBlocks = new int[50][10][50][1];

        private boolean mPowered = false;

        public TileEntitySuperLight() {
            mCreated = System.currentTimeMillis();
            Logger.INFO("Created Super-Lamp");
        }

        @Override
        public void readFromNBT(NBTTagCompound aNBT) {
            super.readFromNBT(aNBT);
            mCreated = aNBT.getLong("mCreated");
            mPowered = aNBT.getBoolean("mPowered");
            NBTTagCompound aLightingData = aNBT.getCompoundTag("lighting");
            for (int x = 0; x < 50; x++) {
                for (int y = 0; y < 10; y++) {
                    for (int z = 0; z < 50; z++) {
                        int aData = aLightingData.getInteger("[" + x + "][" + y + "][" + z + "]");
                        aLitBlocks[x][y][z][0] = aData;
                    }
                }
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound aNBT) {
            super.writeToNBT(aNBT);
            aNBT.setLong("mCreated", mCreated);
            aNBT.setBoolean("mPowered", mPowered);
            NBTTagCompound aLightingData = new NBTTagCompound();
            for (int x = 0; x < 50; x++) {
                for (int y = 0; y < 10; y++) {
                    for (int z = 0; z < 50; z++) {
                        int aFlag = aLitBlocks[x][y][z][0];
                        aLightingData.setInteger("[" + x + "][" + y + "][" + z + "]", aFlag);
                    }
                }
            }
            aNBT.setTag("lighting", aLightingData);
        }

        @Override
        public void updateEntity() {
            super.updateEntity();

            if (this.worldObj.isRemote) {
                return;
            }

            try {
                if (mLastUpdateTick == 0 || (System.currentTimeMillis() - mLastUpdateTick) >= 30000) {
                    boolean powered = (this.worldObj
                        .isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord));
                    boolean aLastState = mPowered;
                    // Logger.INFO("Powered: "+powered);
                    mPowered = powered;
                    if (mPowered != aLastState) {
                        updateLighting(powered);
                    }
                }
            } catch (Throwable ignored) {}
        }

        @Override
        public void markDirty() {
            super.markDirty();
        }

        @Override
        public boolean canUpdate() {
            return super.canUpdate();
        }

        public void updateLighting(boolean enable) {

            mLastUpdateTick = System.currentTimeMillis();

            aLitBlocks = new int[50][10][50][1];
            int aLitCounter = 0;
            AutoMap<BlockPos> aBlocksToUpdate = new AutoMap<>();
            Logger.INFO("Trying to relight area.");

            BlockPos aStartIterationPoint = new BlockPos(
                this.xCoord - 24,
                this.yCoord - 4,
                this.zCoord - 24,
                this.worldObj);
            for (int x = 0; x < 50; x++) {
                for (int y = 0; y < 10; y++) {
                    for (int z = 0; z < 50; z++) {
                        int xOff = aStartIterationPoint.xPos + x;
                        int yOff = aStartIterationPoint.yPos + y;
                        int zOff = aStartIterationPoint.zPos + z;
                        Block aBlockGet = this.worldObj.getBlock(xOff, yOff, zOff);
                        if (aBlockGet != null) {
                            if (aBlockGet instanceof BlockAir) {

                                int aLight = aBlockGet.getLightValue();

                                // Don't Need to relight anything.
                                if ((enable && aLight > 0) || (!enable && aLight == 0)) {
                                    continue;
                                }
                                // Turning Lights on
                                else if (enable && aLight == 0) {
                                    aBlocksToUpdate.put(new BlockPos(xOff, yOff, zOff, this.worldObj));
                                    this.worldObj
                                        .setBlock(xOff, yOff, zOff, ModBlocks.MatterFabricatorEffectBlock, 0, 3);
                                    aLitCounter++;
                                }
                                // Turning Lights off
                                else if (!enable && aLight > 0) {
                                    aBlocksToUpdate.put(new BlockPos(xOff, yOff, zOff, this.worldObj));
                                    if (aBlockGet instanceof BlockLightGlass) {
                                        Logger.INFO("Dimmed air.");
                                        this.worldObj.setBlock(xOff, yOff, zOff, Blocks.air, 0, 3);
                                    }
                                }
                                aLitBlocks[x][y][z][0] = enable ? 15 : 0;
                            } else {
                                aLitBlocks[x][y][z][0] = -1;
                            }
                        } else {
                            aLitBlocks[x][y][z][0] = -1;
                        }
                    }
                }
            }
        }
    }
}
