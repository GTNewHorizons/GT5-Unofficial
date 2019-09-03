/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.api.enums.GT_Values.V;

public class GT_TileEntity_ManualTrafo extends GT_MetaTileEntity_MultiBlockBase {

    private static final byte SINGLE_UPSTEP = 0;
    private static final byte SINGLE_DOWNSTEP = 1;
    private static final byte MULTI_UPSTEP = 2;
    private static final byte MULTI_DOWNSTEP = 3;
    private byte mode;
    private final byte texid = 2;
    private long mCoilWicks;
    private boolean upstep = true;

    public GT_TileEntity_ManualTrafo(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ManualTrafo(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!this.getBaseMetaTileEntity().isAllowedToWork())
            this.stopMachine();
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!this.getBaseMetaTileEntity().isAllowedToWork()) {
            this.stopMachine();
            return false;
        }

        this.mProgresstime = 0;
        this.mMaxProgresstime = 1;

        if (this.getBaseMetaTileEntity().getTimer() % 40 == 0)
            if (this.mEfficiency < this.getMaxEfficiency(null))
                this.mEfficiency += 100;
            else
                this.mEfficiency = this.getMaxEfficiency(null);

        if (this.mode > GT_TileEntity_ManualTrafo.SINGLE_DOWNSTEP) {
            return this.onRunningTickTabbedMode();
        }

        boolean ret = this.drainEnergyInput(this.getInputTier() * 2 * this.mEnergyHatches.size()) && this.addEnergyOutput(this.getInputTier() * 2 * this.mEnergyHatches.size() * (long) this.mEfficiency / this.getMaxEfficiency(null));

        return ret;
    }

    public boolean onRunningTickTabbedMode() {
        boolean ret = false;
        for (GT_MetaTileEntity_Hatch_Dynamo E : this.mDynamoHatches) {
            for (GT_MetaTileEntity_Hatch_Energy I : this.mEnergyHatches) {

                long vtt = I.getEUVar() >= (V[E.mTier] / 2) && E.getEUVar() < E.maxEUStore() ? I.getEUVar() : 0;

                if (vtt == 0)
                    continue;

                long vtp = E.getEUVar() + (vtt);
                long avt = vtp < E.maxEUStore() ? vtp : E.maxEUStore();
                E.setEUVar(avt);
                I.setEUVar(I.getEUVar() - vtt);
                ret = true;
            }
        }
        return ret;
    }


    public long getInputTier() {
        if (this.mEnergyHatches.size() > 0)
            return (long) GT_Utility.getTier(this.mEnergyHatches.get(0).getBaseMetaTileEntity().getInputVoltage());
        else return 0;
    }

    public long getOutputTier() {
        if (this.mDynamoHatches.size() > 0)
            return (long) GT_Utility.getTier(this.mDynamoHatches.get(0).getBaseMetaTileEntity().getOutputVoltage());
        else return 0;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        if (!this.getBaseMetaTileEntity().isAllowedToWork()) {
            this.stopMachine();
            return false;
        }
        this.mode = this.mInventory[1] == null ? 0 : this.mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit") ? this.mInventory[1].getItemDamage() > 4 ? (byte) this.mInventory[1].getItemDamage() : 0 : 0;
        this.upstep = (this.mode == 0 || this.mode == 2);
        this.mProgresstime = 0;
        this.mMaxProgresstime = 1;
        this.mEfficiency = this.mEfficiency > 100 ? this.mEfficiency : 100;
        return this.upstep ? this.getOutputTier() - this.getInputTier() == this.mCoilWicks : this.getInputTier() - this.getOutputTier() == this.mCoilWicks;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {

        this.mode = this.mInventory[1] == null ? 0 : this.mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit") ? this.mInventory[1].getItemDamage() > 4 ? (byte) this.mInventory[1].getItemDamage() : 0 : 0;

        if (this.mode <= 1) {

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
                                ++this.mCoilWicks;
                                if (this.mCoilWicks % 8 == 0) {
                                    ++y;
                                    continue;
                                }
                            } else
                                break;
                        }
                    }
                    if (!stillcoil)
                        break;
                }
            }

            if (this.mCoilWicks % 8 != 0)
                return false;

            this.mCoilWicks = this.mCoilWicks / 8;

            //check interior (NiFeZn02 Core)
            for (int i = 1; i <= this.mCoilWicks; ++i) {
                if (!aBaseMetaTileEntity.getBlockOffset(xDir, i, zDir).equals(ItemRegistry.BW_BLOCKS[2]) && aBaseMetaTileEntity.getMetaIDOffset(xDir, i, zDir) == 0) {
                    return false;
                }
            }

            //check Bottom
            for (int x = -1; x <= 1; ++x)
                for (int z = -1; z <= 1; ++z)
                    if (xDir + x != 0 || zDir + z != 0) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, 0, zDir + z);
                        if (!this.addMaintenanceToMachineList(tTileEntity, this.texid) && !this.addEnergyInputToMachineList(tTileEntity, this.texid)) {
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
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, (int) this.mCoilWicks + 1, zDir + z);
                    if (!this.addMaintenanceToMachineList(tTileEntity, this.texid) && !this.addDynamoToMachineList(tTileEntity, this.texid)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + x, (int) this.mCoilWicks + 1, zDir + z) != GregTech_API.sBlockCasings1) {
                            return false;
                        }
                    /*
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + x, 0, zDir + z) != 11) {
                            return false;
                        }
                    */
                    }
                }

            // check dynamos and energy hatches for the same tier
            byte outtier = this.mDynamoHatches.get(0).mTier;
            for (GT_MetaTileEntity_Hatch_Dynamo out : this.mDynamoHatches) {
                if (out.mTier != outtier)
                    return false;
            }

            byte intier = this.mEnergyHatches.get(0).mTier;
            for (GT_MetaTileEntity_Hatch_Energy in : this.mEnergyHatches) {
                if (in.mTier != intier)
                    return false;
            }
        } else {

            int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
            int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY * 2;
            int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
            //check height
            int y = 1;
            boolean stillcoil = true;
            while (stillcoil) {
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || z != 0) {
                            stillcoil = aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z).equals(ItemRegistry.BW_BLOCKS[2]) && aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z) == 1;
                            if (stillcoil) {
                                ++this.mCoilWicks;
                                if (this.mCoilWicks % 8 == 0) {
                                    ++y;
                                    continue;
                                }
                            } else
                                break;
                        }
                    }
                    if (!stillcoil)
                        break;
                }
            }

            if (this.mCoilWicks % 8 != 0)
                return false;

            this.mCoilWicks = this.mCoilWicks / 8;

            //check interior (NiFeZn02 Core)
            for (int i = 1; i <= this.mCoilWicks; ++i) {
                if (!aBaseMetaTileEntity.getBlockOffset(xDir, i, zDir).equals(ItemRegistry.BW_BLOCKS[2]) && aBaseMetaTileEntity.getMetaIDOffset(xDir, i, zDir) == 0) {
                    return false;
                }
            }

            //check Bottom
            for (int x = -2; x <= 2; ++x)
                for (int z = -2; z <= 2; ++z)
                    if (xDir + x != 0 || zDir + z != 0) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, 0, zDir + z);
                        if (!this.addMaintenanceToMachineList(tTileEntity, this.texid) && !this.addEnergyInputToMachineList(tTileEntity, this.texid)) {
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
            for (int x = -2; x <= 2; ++x)
                for (int z = -2; z <= 2; ++z) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, (int) this.mCoilWicks + 1, zDir + z);
                    if (!this.addMaintenanceToMachineList(tTileEntity, this.texid) && !this.addDynamoToMachineList(tTileEntity, this.texid)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + x, (int) this.mCoilWicks + 1, zDir + z) != GregTech_API.sBlockCasings1) {
                            return false;
                        }
                    /*
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + x, 0, zDir + z) != 11) {
                            return false;
                        }
                    */
                    }
                }

            if (this.mDynamoHatches.size() <= 0 || this.mEnergyHatches.size() <= 0)
                return false;

            byte outtier = this.mDynamoHatches.get(0).mTier;
            for (GT_MetaTileEntity_Hatch_Dynamo out : this.mDynamoHatches) {
                if (out.mTier != outtier)
                    return false;
            }

            byte intier = this.mEnergyHatches.get(0).mTier;
            for (GT_MetaTileEntity_Hatch_Energy in : this.mEnergyHatches) {
                if (in.mTier != intier)
                    return false;
            }


            //check tap hull
            for (int ty = 1; ty <= y; ++ty) {

                byte leveltier = 0;
                if (this.mInventory[1].getItemDamage() == 2)
                    leveltier = ((byte) (intier - ty));
                else if (this.mInventory[1].getItemDamage() == 3)
                    leveltier = ((byte) (intier + ty));
                else
                    return false;

                for (int x = -2; x <= 2; ++x)
                    for (int z = -2; z <= 2; ++z)
                        if (x == -2 || z == -2 || x == 2 || z == 2) {
                            IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, ty, zDir + z);
                            if (!this.addMaintenanceToMachineList(tTileEntity, this.texid) && !this.addEnergyInputToMachineList(tTileEntity, this.texid, leveltier) && !this.addDynamoToMachineList(tTileEntity, this.texid, leveltier)) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + x, ty, zDir + z) != GregTech_API.sBlockCasings1) {
                                    return false;
                                }
                            }
                        }
            }
        }
        return !this.mDynamoHatches.isEmpty() && !this.mEnergyHatches.isEmpty();
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
        return new GT_TileEntity_ManualTrafo(this.mName);
    }

    @Override
    public String[] getDescription() {
        String[] dsc = StatCollector.translateToLocal("tooltip.tile.manualtravo.0.name").split(";");
        String[] fdsc = new String[dsc.length + 1];
        for (int i = 0; i < dsc.length; i++) {
            fdsc[i] = dsc[i];
            fdsc[dsc.length] = StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks";
        }
        return fdsc;
    }

    @Override
    public void saveNBTData(NBTTagCompound ntag) {
        ntag.setLong("mCoilWicks", this.mCoilWicks);
        super.saveNBTData(ntag);
    }

    @Override
    public void loadNBTData(NBTTagCompound ntag) {
        super.loadNBTData(ntag);
        this.mCoilWicks = ntag.getLong("mCoilWicks");
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == aFacing ? new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[this.texid], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)} : new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[this.texid]};
    }

    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex, short tier) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                if (tier == ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mTier) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
                }
                return false;
            } else {
                return false;
            }
        }
    }

    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex, short tier) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
                if (tier == ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mTier) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
                }
                return false;
            } else {
                return false;
            }
        }
    }

}
