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

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.lang.reflect.Field;
import java.util.Arrays;

public class GT_TileEntity_DEHP extends GT_MetaTileEntity_DrillerBase {
    private static float nulearHeatMod = 2f;
    private byte mMode;
    private byte mTier;

    public GT_TileEntity_DEHP(int aID, int tier, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mTier = (byte) tier;
    }

    public GT_TileEntity_DEHP(String aName, byte mTier) {
        super(aName);
        this.mTier = (byte) mTier;
    }

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        try {
            Class c = TileEntityNuclearReactorElectric.class;
            Field f = c.getDeclaredField("huOutputModifier");
            f.setAccessible(true);
            nulearHeatMod = f.getFloat(f);
        } catch (SecurityException | IllegalArgumentException | ExceptionInInitializerError | NullPointerException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        super.onConfigLoad(aConfig);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mTier", mTier);
        aNBT.setByte("mMode", mMode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mTier = aNBT.getByte("mTier");
        mMode = aNBT.getByte("mMode");
        super.loadNBTData(aNBT);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_DEHP(this.mName, mTier);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "DrillingRig.png");
    }

    @Override
    public String[] getDescription() {
        String[] dscSteam = {"Controller Block for the Deep Earth Heat Pump " + (mTier > 1 ? mTier : ""), "Size(WxHxD): 3x7x3", "Controller (Front middle at bottom)", "3x1x3 Base of " + getCasingBlockItem().name(), "1x3x1 " + getCasingBlockItem().name() + " pillar (Center of base)", "1x3x1 " + this.getFrameMaterial().mName + " Frame Boxes (Each pillar side and on top)", "1x Input Hatch (One of base casings)", "1x Output Hatch (One of base casings)", "1x Maintenance Hatch (One of base casings)", "1x " + GT_Values.VN[this.getMinTier()] + "+ Energy Hatch (Any bottom layer casing)", "Consumes " + GT_Values.V[mTier + 2] + "EU/t", "Has 4 Modes, use the Screwdriver to change them:", "0 Idle, 1 Steam, 2 Superheated Steam (requires Distilled Water), 3 Retract", "Explodes when it runs out of Water/Distilled Water", "Converts " + (long) (mTier * 1200 * 20) + "L/s Water(minus 10% per Maintenance Problem) to Steam", "Converts " + (long) (mTier * 600 * 20) + "L/s Distilled Water(minus 10% per Maintenance Problem) to SuperheatedSteam"};
        String[] dscCooleant = {"Controller Block for the Deep Earth Heat Pump " + (mTier > 1 ? mTier : ""), "Size(WxHxD): 3x7x3", "Controller (Front middle at bottom)", "3x1x3 Base of " + getCasingBlockItem().name(), "1x3x1 " + getCasingBlockItem().name() + " pillar (Center of base)", "1x3x1 " + this.getFrameMaterial().mName + " Frame Boxes (Each pillar side and on top)", "1x Input Hatch (One of base casings)", "1x Output Hatch (One of base casings)", "1x Maintenance Hatch (One of base casings)", "1x " + GT_Values.VN[this.getMinTier()] + "+ Energy Hatch (Any bottom layer casing)", "Consumes " + GT_Values.V[mTier + 2] + "EU/t", "Has 4 Modes, use the Screwdriver to change them:", "0 Idle, 1 & 2 Coolant Heating Mode (no Difference between them), 3 Retract", "Explodes when it runs out of Coolant", "Heats up " + (long) (mTier * 24 * ((double) nulearHeatMod)) * 20 + "L/s Coolant(minus 10% per Maintenance Problem)"};
        return ConfigHandler.DEHPDirectSteam ? dscSteam : dscCooleant;
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_HeatProof;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Tungsten;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 11;
    }

    @Override
    protected int getMinTier() {
        return 2 + mTier;
    }

    @Override
    protected boolean checkHatches() {
        return !this.mMaintenanceHatches.isEmpty() && !this.mOutputHatches.isEmpty() && !this.mInputHatches.isEmpty();
    }

    private long getFluidFromHatches(Fluid f) {
        long ret = 0;
        for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
            if (ih.getFluid().getFluid().equals(f))
                ret += ih.getFluidAmount();
        }
        return ret;
    }

    private long getWaterFromHatches(boolean onlyDistilled) {
        Fluid toConsume1 = FluidRegistry.WATER;
        Fluid toConsume2 = GT_ModHandler.getDistilledWater(1L).getFluid();
        if (onlyDistilled)
            toConsume1 = toConsume2;
        long ret = 0;
        for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
            if (ih.getFluid().getFluid().equals(toConsume1) || ih.getFluid().getFluid().equals(toConsume2))
                ret += ih.getFluidAmount();
        }
        return ret;
    }

    @Override
    protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (mMode != 3) {
            this.isPickingPipes = false;
            return true;
        }
        return super.workingUpward(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ++mMode;
        if (mMode >= 4)
            mMode = 0;
        GT_Utility.sendChatToPlayer(aPlayer, "Mode: " + mMode);
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
    }

    protected boolean workingDownward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (mMode == 3) {
            this.isPickingPipes = true;
            return true;
        }

        if (!this.tryLowerPipe()) {
            if (this.waitForPipes()) {
                return false;
            } else {
                if (ConfigHandler.DEHPDirectSteam) {
                    if (mMode == 1) {
                        long steamProduced = (mTier * 600 * 2L * this.mEfficiency / 10000L);
                        long waterConsume = ((steamProduced + 160) / 160);

                        if (getWaterFromHatches(false) - waterConsume > 0) {
                            consumeFluid(FluidRegistry.WATER, waterConsume);
                            addOutput(GT_ModHandler.getSteam(steamProduced));
                        } else {
                            explodeMultiblock();
                            return false;
                        }
                    } else if (mMode == 2) {
                        long steamProduced = (mTier * 300 * 2L * this.mEfficiency / 10000L);
                        long waterConsume = ((steamProduced + 160) / 160);

                        if (getWaterFromHatches(true) - waterConsume > 0) {
                            consumeFluid(GT_ModHandler.getDistilledWater(1).getFluid(), waterConsume);
                            addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", (int) steamProduced));
                        } else {
                            explodeMultiblock();
                            return false;
                        }
                    }
                } else {
                    if (mMode == 1 || mMode == 2) {
                        long coolantConverted = (long) (mTier * 24 * ((double) nulearHeatMod) * this.mEfficiency / 10000L);
                        if (getFluidFromHatches(FluidRegistry.getFluid("ic2coolant")) - coolantConverted > 0) {
                            consumeFluid(FluidRegistry.getFluid("ic2coolant"), coolantConverted);
                            addOutput(FluidRegistry.getFluidStack("ic2hotcoolant", (int) coolantConverted));
                        } else {
                            explodeMultiblock();
                            return false;
                        }
                    }
                }
            }
        } else {
            return true;
        }
        return true;
    }

    private boolean consumeFluid(Fluid fluid, long ammount) {


        if (ammount > Integer.MAX_VALUE) {
            int[] tmp = new int[(int) (ammount / Integer.MAX_VALUE)];
            Arrays.fill(tmp, (int) (ammount / Integer.MAX_VALUE));
            for (int i = 0; i < tmp.length; i++) {
                for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
                    if (fluid.equals(FluidRegistry.WATER) ? ih.getFluid().getFluid().equals(fluid) || ih.getFluid().getFluid().equals(GT_ModHandler.getDistilledWater(1).getFluid()) : ih.getFluid().getFluid().equals(fluid))
                        tmp[i] -= ih.drain((int) ammount, true).amount;
                    if (tmp[i] <= 0)
                        break;
                }
            }

            if (tmp[tmp.length - 1] <= 0) {
                return true;
            }

            return false;
        }

        long tmp = ammount;
        for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
            if (fluid.equals(FluidRegistry.WATER) ? ih.getFluid().getFluid().equals(fluid) || ih.getFluid().getFluid().equals(GT_ModHandler.getDistilledWater(1).getFluid()) : ih.getFluid().getFluid().equals(fluid))
                tmp -= ih.drain((int) ammount, true).amount;
            if (tmp <= 0)
                return true;
        }
        return false;
    }

    @Override
    protected void setElectricityStats() {
        try {
            this.mEUt = isPickingPipes ? 60 : Math.toIntExact(GT_Values.V[getMinTier()]);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            this.mEUt = Integer.MAX_VALUE - 7;
        }
        this.mProgresstime = 0;
        this.mMaxProgresstime = 1;
        this.mEfficiency = this.getCurrentEfficiency((ItemStack) null);
        this.mEfficiencyIncrease = 10000;
    }
}
