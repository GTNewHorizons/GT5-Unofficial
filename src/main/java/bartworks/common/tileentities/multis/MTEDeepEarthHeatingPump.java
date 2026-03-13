/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.multis;

import static gregtech.api.enums.GTValues.VN;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase;

public class MTEDeepEarthHeatingPump extends MTEDrillerBase {

    private byte mMode;

    public MTEDeepEarthHeatingPump(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEDeepEarthHeatingPump(String aName) {
        super(aName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMode", this.mMode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mMode = aNBT.getByte("mMode");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEDeepEarthHeatingPump(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        String casings = this.getCasingBlockItem()
            .get(0)
            .getDisplayName();
        tt.addMachineType("machtype.dehp")
            .addInfo("gt.dehp.tips", (long) (25600 * 20), (long) (192 * 20), TierEU.RECIPE_HV)
            .beginStructureBlock(3, 7, 3, false)
            .addController("front_bottom_middle")
            .addStructurePart(casings, "gt.driller_shaped_mb.info.casing.1")
            .addStructurePart(casings, "gt.driller_shaped_mb.info.casing.2")
            .addStructurePart(
                GTOreDictUnificator.get(OrePrefixes.frameGt, this.getFrameMaterial(), 1)
                    .getDisplayName(),
                "gt.driller_shaped_mb.info.frame")
            .addEnergyHatch(GTUtility.nestParams("gt.dehp.info.energy", VN[this.getMinTier()]))
            .addMaintenanceHatch("gt.driller_shaped_mb.info.replace")
            .addInputBus("gt.dehp.info.i_bus")
            .addInputHatch("gt.driller_shaped_mb.info.replace")
            .addOutputHatch("gt.driller_shaped_mb.info.replace")
            .toolTipFinisher("tooltip.bw.author_bart_via_bw.name");
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
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public Pos2d getMachineModeSwitchButtonPos() {
        return new Pos2d(98, 91);
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }

    @Override
    public String getMachineModeName() {
        return GTUtility.translate("GT5U.DEHP.mode." + machineMode);
    }

    @Override
    protected int getMinTier() {
        return 3;
    }

    @Override
    protected boolean checkHatches() {
        return !this.mMaintenanceHatches.isEmpty() && !this.mOutputHatches.isEmpty() && !this.mInputHatches.isEmpty();
    }

    private long getFluidFromHatches(Fluid f) {
        long ret = 0;
        for (MTEHatchInput ih : this.mInputHatches) {
            if (ih.getFluid()
                .getFluid()
                .equals(f)) ret += ih.getFluidAmount();
        }
        return ret;
    }

    private long getWaterFromHatches(boolean onlyDistilled) {
        Fluid toConsume1 = FluidRegistry.WATER;
        Fluid toConsume2 = GTModHandler.getDistilledWater(1L)
            .getFluid();
        if (onlyDistilled) toConsume1 = toConsume2;
        long ret = 0;
        for (MTEHatchInput ih : this.mInputHatches) {
            if (ih.getFluid()
                .getFluid()
                .equals(toConsume1)
                || ih.getFluid()
                    .getFluid()
                    .equals(toConsume2))
                ret += ih.getFluidAmount();
        }
        return ret;
    }

    @Override
    protected void addOperatingMessages() {
        // Inheritors can overwrite these to add custom operating messages.
        addResultMessage(STATE_DOWNWARD, true, "deploying_pipe");
        addResultMessage(STATE_DOWNWARD, false, "extracting_pipe");
        addResultMessage(STATE_AT_BOTTOM, true, "circulating_fluid");
        addResultMessage(STATE_AT_BOTTOM, false, "no_mining_pipe");
        addResultMessage(STATE_UPWARD, true, "retracting_pipe");
        addResultMessage(STATE_UPWARD, false, "drill_generic_finished");
        addResultMessage(STATE_ABORT, true, "retracting_pipe");
        addResultMessage(STATE_ABORT, false, "drill_retract_pipes_finished");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility.sendChatTrans(aPlayer, "GT5U.MULTI_MACHINE_CHANGE", getMachineModeName());
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        if (tryLowerPipeState(true) == 0) {
            workState = STATE_DOWNWARD;
            return true;
        }
        if (this.machineMode == 0) {
            long steamProduced = 25600L * this.mEfficiency / 10000L;
            long waterConsume = (steamProduced + 160) / 160;

            if (this.getWaterFromHatches(true) - waterConsume > 0) {
                this.consumeFluid(
                    GTModHandler.getDistilledWater(1)
                        .getFluid(),
                    waterConsume);
                this.addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", (int) steamProduced));
            } else {
                this.explodeMultiblock();
                return false;
            }
        } else if (this.machineMode == 1) {
            long coolantConverted = 192L * this.mEfficiency / 10_000L;
            if (this.getFluidFromHatches(
                GTModHandler.getIC2Coolant(0)
                    .getFluid())
                - coolantConverted > 0) {
                this.consumeFluid(
                    GTModHandler.getIC2Coolant(0)
                        .getFluid(),
                    coolantConverted);
                this.addOutput(FluidRegistry.getFluidStack("ic2hotcoolant", (int) coolantConverted));
            } else {
                this.explodeMultiblock();
                return false;
            }
        }
        return true;
    }

    private boolean consumeFluid(Fluid fluid, long ammount) {
        if (ammount > Integer.MAX_VALUE) {
            int[] tmp = new int[(int) (ammount / Integer.MAX_VALUE)];
            Arrays.fill(tmp, (int) (ammount / Integer.MAX_VALUE));
            for (int i = 0; i < tmp.length; i++) {
                for (MTEHatchInput ih : this.mInputHatches) {
                    if (fluid.equals(FluidRegistry.WATER) ? ih.getFluid()
                        .getFluid()
                        .equals(fluid)
                        || ih.getFluid()
                            .getFluid()
                            .equals(
                                GTModHandler.getDistilledWater(1)
                                    .getFluid())
                        : ih.getFluid()
                            .getFluid()
                            .equals(fluid))
                        tmp[i] -= ih.drain((int) ammount, true).amount;
                    if (tmp[i] <= 0) break;
                }
            }

            return tmp[tmp.length - 1] <= 0;
        }

        long tmp = ammount;
        for (MTEHatchInput ih : this.mInputHatches) {
            if (fluid.equals(FluidRegistry.WATER) ? ih.getFluid()
                .getFluid()
                .equals(fluid)
                || ih.getFluid()
                    .getFluid()
                    .equals(
                        GTModHandler.getDistilledWater(1)
                            .getFluid())
                : ih.getFluid()
                    .getFluid()
                    .equals(fluid))
                tmp -= ih.drain((int) ammount, true).amount;
            if (tmp <= 0) return true;
        }
        return false;
    }

    @Override
    protected void setElectricityStats() {
        try {
            this.mEUt = this.isPickingPipes ? -60 : -((int) TierEU.RECIPE_HV);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            this.mEUt = Integer.MAX_VALUE - 7;
        }
        this.mProgresstime = 0;
        this.mMaxProgresstime = calculateMaxProgressTime(0);
        this.mEfficiency = this.getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
    }

    @Override
    public int calculateMaxProgressTime(int tier, boolean simulateWorking) {
        return 1;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEMultiBlockBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_STEAM,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }
}
