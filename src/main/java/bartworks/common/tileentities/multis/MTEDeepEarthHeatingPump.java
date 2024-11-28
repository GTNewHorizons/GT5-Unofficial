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

import static bartworks.util.BWTooltipReference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static gregtech.api.enums.GTValues.VN;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEDeepEarthHeatingPump(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        String casings = this.getCasingBlockItem()
            .get(0)
            .getDisplayName();
        tt.addMachineType("Geothermal Heat Pump")
            .addInfo("Consumes " + TierEU.RECIPE_HV + "EU/t")
            .addInfo("Has 2 Modes, use the Screwdriver to change them:");

        tt.addInfo("Direct Steam and Coolant Heating")
            .addInfo(
                "Direct Steam Mode: Consumes Distilled Water to produce " + (long) (25600 * 20)
                    + "L/s of Superheated Steam")
            .addInfo("Coolant Heating Mode: Converts " + (long) (192 * 20) + "L/s Coolant to Hot Coolant")
            .addInfo("Each maintenance issue lowers output efficiency by 10%")
            .addInfo("Explodes when it runs out of Distilled Water/Coolant");

        tt.beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
            .addOtherStructurePart(
                this.getFrameMaterial().mName + " Frame Boxes",
                "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch(VN[this.getMinTier()] + "+, Any base casing")
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
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public Pos2d getMachineModeSwitchButtonPos() {
        return new Pos2d(98, 91);
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.clear();
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.DEHP.mode." + machineMode);
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        PlayerUtils.messagePlayer(
            aPlayer,
            String.format(StatCollector.translateToLocal("GT5U.MULTI_MACHINE_CHANGE"), getMachineModeName()));
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
            long coolantConverted = (long) (192L * this.mEfficiency / 10000L);
            if (this.getFluidFromHatches(FluidRegistry.getFluid("ic2coolant")) - coolantConverted > 0) {
                this.consumeFluid(FluidRegistry.getFluid("ic2coolant"), coolantConverted);
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
        this.mMaxProgresstime = 1;
        this.mEfficiency = this.getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
    }
}
