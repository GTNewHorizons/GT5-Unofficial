/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static gregtech.api.enums.GT_Values.VN;

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
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import java.lang.reflect.Field;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GT_TileEntity_DEHP extends GT_MetaTileEntity_DrillerBase {
    private static float nulearHeatMod = 2f;
    private byte mMode;
    private byte mTier;

    public GT_TileEntity_DEHP(int aID, int tier, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.mTier = (byte) tier;
    }

    public GT_TileEntity_DEHP(String aName, byte mTier) {
        super(aName);
        this.mTier = mTier;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void onConfigLoad(GT_Config aConfig) {
        try {
            Class c = TileEntityNuclearReactorElectric.class;
            Field f = c.getDeclaredField("huOutputModifier");
            f.setAccessible(true);
            GT_TileEntity_DEHP.nulearHeatMod = f.getFloat(f);
        } catch (SecurityException
                | IllegalArgumentException
                | ExceptionInInitializerError
                | NullPointerException
                | IllegalAccessException
                | NoSuchFieldException e) {
            e.printStackTrace();
        }
        super.onConfigLoad(aConfig);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mTier", this.mTier);
        aNBT.setByte("mMode", this.mMode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mTier = aNBT.getByte("mTier");
        this.mMode = aNBT.getByte("mMode");
        super.loadNBTData(aNBT);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_DEHP(this.mName, this.mTier);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(
                aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "DrillingRig.png");
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        String casings = getCasingBlockItem().get(0).getDisplayName();
        tt.addMachineType("Geothermal Heat Pump")
                .addInfo("Consumes " + GT_Values.V[this.mTier + 2] + "EU/t")
                .addInfo("Has 4 Modes, use the Screwdriver to change them:");
        if (ConfigHandler.DEHPDirectSteam) {
            tt.addInfo("0 Idle, 1 Steam, 2 Superheated Steam (requires Distilled Water), 3 Retract")
                    .addInfo("Explodes when it runs out of Water/Distilled Water")
                    .addInfo("Converts " + (long) (this.mTier * 1200 * 20)
                            + "L/s Water(minus 10% per Maintenance Problem) to Steam")
                    .addInfo("Converts " + (long) (this.mTier * 600 * 20)
                            + "L/s Distilled Water(minus 10% per Maintenance Problem) to SuperheatedSteam");

        } else {
            tt.addInfo("0 Idle, 1 & 2 Coolant Heating Mode (no Difference between them), 3 Retract")
                    .addInfo("Explodes when it runs out of Coolant")
                    .addInfo("Heats up " + (long) (this.mTier * 24 * ((double) GT_TileEntity_DEHP.nulearHeatMod)) * 20
                            + "L/s Coolant(minus 10% per Maintenance Problem)");
        }
        tt.addSeparator()
                .beginStructureBlock(3, 7, 3, false)
                .addController("Front bottom")
                .addOtherStructurePart(casings, "form the 3x1x3 Base")
                .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
                .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
                .addEnergyHatch(VN[getMinTier()] + "+, Any base casing")
                .addMaintenanceHatch("Any base casing")
                .addInputBus("Mining Pipes, optional, any base casing")
                .addInputHatch("Any base casing")
                .addOutputHatch("Any base casing")
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
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
        return 2 + this.mTier;
    }

    @Override
    protected boolean checkHatches() {
        return !this.mMaintenanceHatches.isEmpty() && !this.mOutputHatches.isEmpty() && !this.mInputHatches.isEmpty();
    }

    private long getFluidFromHatches(Fluid f) {
        long ret = 0;
        for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
            if (ih.getFluid().getFluid().equals(f)) ret += ih.getFluidAmount();
        }
        return ret;
    }

    private long getWaterFromHatches(boolean onlyDistilled) {
        Fluid toConsume1 = FluidRegistry.WATER;
        Fluid toConsume2 = GT_ModHandler.getDistilledWater(1L).getFluid();
        if (onlyDistilled) toConsume1 = toConsume2;
        long ret = 0;
        for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
            if (ih.getFluid().getFluid().equals(toConsume1)
                    || ih.getFluid().getFluid().equals(toConsume2)) ret += ih.getFluidAmount();
        }
        return ret;
    }

    @Override
    protected boolean workingUpward(
            ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (this.mMode != 3) {
            this.isPickingPipes = false;
            try {
                Field workState = this.getClass().getField("workState");
                workState.setInt(this, 0);
            } catch (NoSuchFieldError | NoSuchFieldException | IllegalAccessException ignored) {
            }
            return true;
        }
        return super.workingUpward(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (this.getBaseMetaTileEntity().getWorld().isRemote) return;
        ++this.mMode;
        if (this.mMode >= 4) this.mMode = 0;
        GT_Utility.sendChatToPlayer(aPlayer, "Mode: " + this.mMode);
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
    }

    protected boolean workingDownward(
            ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (this.mMode == 3) {
            this.isPickingPipes = true;
            try {
                Field workState = this.getClass().getSuperclass().getDeclaredField("workState");
                workState.setInt(this, 2);
            } catch (NoSuchFieldError | NoSuchFieldException | IllegalAccessException ignored) {
            }
            return true;
        }

        if (tryLowerPipeState(false) != 0) {
            if (this.waitForPipes()) {
                return false;
            } else {
                if (this.mMode == 0) this.mMode = 1;
                if (ConfigHandler.DEHPDirectSteam) {
                    if (this.mMode == 1) {
                        long steamProduced = (this.mTier * 600 * 2L * this.mEfficiency / 10000L);
                        long waterConsume = ((steamProduced + 160) / 160);

                        if (this.getWaterFromHatches(false) - waterConsume > 0) {
                            this.consumeFluid(FluidRegistry.WATER, waterConsume);
                            this.addOutput(GT_ModHandler.getSteam(steamProduced));
                        } else {
                            this.explodeMultiblock();
                            return false;
                        }
                    } else if (this.mMode == 2) {
                        long steamProduced = (this.mTier * 300 * 2L * this.mEfficiency / 10000L);
                        long waterConsume = ((steamProduced + 160) / 160);

                        if (this.getWaterFromHatches(true) - waterConsume > 0) {
                            this.consumeFluid(GT_ModHandler.getDistilledWater(1).getFluid(), waterConsume);
                            this.addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", (int) steamProduced));
                        } else {
                            this.explodeMultiblock();
                            return false;
                        }
                    }
                } else {
                    if (this.mMode == 1 || this.mMode == 2) {
                        long coolantConverted = (long) (this.mTier
                                * 24
                                * ((double) GT_TileEntity_DEHP.nulearHeatMod)
                                * this.mEfficiency
                                / 10000L);
                        if (this.getFluidFromHatches(FluidRegistry.getFluid("ic2coolant")) - coolantConverted > 0) {
                            this.consumeFluid(FluidRegistry.getFluid("ic2coolant"), coolantConverted);
                            this.addOutput(FluidRegistry.getFluidStack("ic2hotcoolant", (int) coolantConverted));
                        } else {
                            this.explodeMultiblock();
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
                    if (fluid.equals(FluidRegistry.WATER)
                            ? ih.getFluid().getFluid().equals(fluid)
                                    || ih.getFluid()
                                            .getFluid()
                                            .equals(GT_ModHandler.getDistilledWater(1)
                                                    .getFluid())
                            : ih.getFluid().getFluid().equals(fluid)) tmp[i] -= ih.drain((int) ammount, true).amount;
                    if (tmp[i] <= 0) break;
                }
            }

            return tmp[tmp.length - 1] <= 0;
        }

        long tmp = ammount;
        for (GT_MetaTileEntity_Hatch_Input ih : this.mInputHatches) {
            if (fluid.equals(FluidRegistry.WATER)
                    ? ih.getFluid().getFluid().equals(fluid)
                            || ih.getFluid()
                                    .getFluid()
                                    .equals(GT_ModHandler.getDistilledWater(1).getFluid())
                    : ih.getFluid().getFluid().equals(fluid)) tmp -= ih.drain((int) ammount, true).amount;
            if (tmp <= 0) return true;
        }
        return false;
    }

    @Override
    protected void setElectricityStats() {
        try {
            this.mEUt = this.isPickingPipes ? 60 : Math.toIntExact(GT_Values.V[this.getMinTier()]);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            this.mEUt = Integer.MAX_VALUE - 7;
        }
        this.mProgresstime = 0;
        this.mMaxProgresstime = 1;
        this.mEfficiency = this.getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
    }
}
