package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.api.enums.GT_Values.V;

public class GT_TileEntity_ManualTrafo extends GT_MetaTileEntity_MultiBlockBase {

    private byte texid = 2;
    private long mCoilWicks = 0;
    private boolean upstep = true;

    public GT_TileEntity_ManualTrafo(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ManualTrafo(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity,aTick);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack){
        if (this.mEfficiency < this.getMaxEfficiency(null))
            this.mEfficiency += 100;
        else
            this.mEfficiency = this.getMaxEfficiency(null);

        return this.drainEnergyInput((long)this.mEUt * 2 * this.mEnergyHatches.size()) &&  this.addEnergyOutput((long)this.mEUt * 2 * this.mEnergyHatches.size() * (long)this.mEfficiency / 10000L);
    }
    
    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        this.upstep= !(this.mInventory[1] != null && this.mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit"));
        this.mMaxProgresstime=1;
        this.mProgresstime=0;
        this.mEfficiency=1000;
        this.mEUt= (int) V[this.mEnergyHatches.get(0).mTier];
        return this.upstep ? this.getOutputTier() - this.getInputTier() == mCoilWicks : this.getInputTier() - this.getOutputTier() == mCoilWicks;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

        //check height
        int y = 1;
        boolean stillcoil = true;
        while (stillcoil) {
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    if (x != 0 || z != 0) {
                        stillcoil = aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z).equals(ItemRegistry.BW_BLOCKS[2]) && aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z) == 1;
                        if (stillcoil) {
                            if (mCoilWicks != 0 && mCoilWicks % 8 == 0) {
                                ++y;
                                continue;
                            }
                            this.mCoilWicks++;
                        } else
                            break;
                    }
                }
                if (!stillcoil)
                    break;
            }
        }
        if (mCoilWicks % 8 != 0)
            return false;

        this.mCoilWicks = this.mCoilWicks / 8;

        //check interior (NiFeZn02 Core)
        for (int i = 1; i <= this.mCoilWicks; ++i) {
            if (!aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir).equals(ItemRegistry.BW_BLOCKS[2]) && aBaseMetaTileEntity.getMetaIDOffset(xDir, y, zDir) == 0) {
                return false;
            }
        }

        //check Bottom
        for (int x = -1; x <= 1; ++x)
            for (int z = -1; z <= 1; ++z)
                if (xDir + x != 0 || zDir + z != 0) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, 0, zDir + z);
                    if (!this.addMaintenanceToMachineList(tTileEntity, texid) && !this.addEnergyInputToMachineList(tTileEntity, texid)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + x, 0, zDir + z) != GregTech_API.sBlockCasings1) {
                            return false;
                        }
                    /*
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + x, 0, zDir + z) != 11) {
                            return false;
                        }
                    */
                    }
                }

        //check Top
        for (int x = -1; x <= 1; ++x)
            for (int z = -1; z <= 1; ++z) {
                IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, (int) this.mCoilWicks+1, zDir + z);
                if (!this.addMaintenanceToMachineList(tTileEntity, texid) && !this.addDynamoToMachineList(tTileEntity, texid)) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + x, (int) this.mCoilWicks+1, zDir + z) != GregTech_API.sBlockCasings1) {
                        return false;
                    }
                    /*
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + x, 0, zDir + z) != 11) {
                            return false;
                        }
                    */
                }
            }

        if (this.mDynamoHatches.size() <= 0 || this.mEnergyHatches.size() <= 0 || this.mDynamoHatches.size() > this.mEnergyHatches.size() )
            return false;

        // check dynamos and energy hatches for the same tier
        byte outtier = this.mDynamoHatches.get(0).mTier;
        for (GT_MetaTileEntity_Hatch_Dynamo out : this.mDynamoHatches){
            if (out.mTier!=outtier)
                return false;
        }

        byte intier = this.mEnergyHatches.get(0).mTier;
        for (GT_MetaTileEntity_Hatch_Energy in : this.mEnergyHatches){
            if (in.mTier!=intier)
                return false;
        }

        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_ManualTrafo(mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{"blalbabla"};
    }

    @Override
    public void saveNBTData(NBTTagCompound ntag){
        ntag.setLong("mCoilWicks",mCoilWicks);
        super.saveNBTData(ntag);
    }

    @Override
    public void loadNBTData(NBTTagCompound ntag){
        super.loadNBTData(ntag);
        mCoilWicks = ntag.getLong("mCoilWicks");
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == aFacing ? new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[texid], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)} : new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[texid]};
    }
}
