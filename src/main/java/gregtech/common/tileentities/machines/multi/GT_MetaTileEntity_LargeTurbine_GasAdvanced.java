package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.*;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_LargeTurbine_GasAdvanced extends GT_MetaTileEntity_LargeTurbine {

    public GT_MetaTileEntity_LargeTurbine_GasAdvanced(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeTurbine_GasAdvanced(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        return new ITexture[] { MACHINE_CASINGS[1][aColorIndex + 1], aFacing
                == aSide ? (aActive ? TextureFactory.builder()
                                                    .addIcon(LARGETURBINE_NEW_ACTIVE5)
                                                    .build()
                        : hasTurbine() ? TextureFactory.builder()
                                                       .addIcon(LARGETURBINE_NEW5)
                                                       .build()
                                : TextureFactory.builder()
                                                .addIcon(LARGETURBINE_NEW_EMPTY5)
                                                .build())
                        : casingTexturePages[1][57] };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Gas Turbine")
          .addInfo("Warning: This is an experimental multiblock, subject to changes ")
          .addInfo("Controller block for the Large Advanced Gas Turbine")
          .addInfo("Needs a Turbine, place inside controller")
          .addInfo("Only accepts gases above 800k EU/bucket")
          .addInfo("Has no maximum EU/t output, only depends on the Dynamo Hatch")
          .addPollutionAmount(getPollutionPerSecond(null))
          .addSeparator()
          .beginStructureBlock(3, 3, 4, true)
          .addController("Front center")
          .addCasingInfoRange("Advanced Gas Turbine Casing", 8, 30, false)
          .addDynamoHatch("Back center", 1)
          .addMaintenanceHatch("Side centered", 2)
          .addMufflerHatch("Side centered", 2)
          .addInputHatch("Gas Fuel, Side centered", 2)
          .toolTipFinisher("Gregtech");
        return tt;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GT_Recipe tFuel = GT_Recipe_Map.sTurbineFuels.findFuel(aLiquid);
        if (tFuel != null) return tFuel.mSpecialValue;
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeTurbine_GasAdvanced(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings8;
    }

    @Override
    public byte getCasingMeta() {
        return 9;
    }

    @Override
    public int getCasingTextureIndex() {
        return 185;
    }

    @Override
    public boolean isNewStyleRendering() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionLargeGasTurbinePerSecond;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff, int overflowMultiplier,
            float[] flowMultipliers) {
        if (aFluids.size() >= 1) {
            int tEU = 0;
            int actualOptimalFlow = 0;

            FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0); // Identify a SINGLE type of fluid to process.
                                                                          // Doesn't matter which one. Ignore the rest!
            int fuelValue = getFuelValue(firstFuelType);

            if (fuelValue < 100) {
                return 0;
            }

            if (aOptFlow < fuelValue) {
                // turbine too weak and/or fuel too powerful
                // at least consume 1L
                this.realOptFlow = 1;
                // wastes the extra fuel and generate aOptFlow directly
                depleteInput(new FluidStack(firstFuelType, 1));
                this.storedFluid += 1;
                return GT_Utility.safeInt((long) aOptFlow * (long) aBaseEff / 10000L);
            }

            actualOptimalFlow = GT_Utility.safeInt((long) (aOptFlow * flowMultipliers[1] / fuelValue));
            this.realOptFlow = actualOptimalFlow;

            // Allowed to use up to 450% optimal flow rate, depending on the value of overflowMultiplier.
            // This value is chosen because the highest EU/t possible depends on the overflowMultiplier, and the formula
            // used
            // makes it so the flow rate for that max, per value of overflowMultiplier, is (percentage of optimal flow
            // rate):
            // - 150% if it is 1
            // - 300% if it is 2
            // - 450% if it is 3
            // Variable required outside of loop for multi-hatch scenarios.
            int remainingFlow = GT_Utility.safeInt((long) (actualOptimalFlow * (1.5f * overflowMultiplier)));
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow); // try to use up to the max flow defined just above
                    depleteInput(new FluidStack(aFluid, flow)); // deplete that amount
                    this.storedFluid += aFluid.amount;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GT_Utility.safeInt((long) totalFlow * fuelValue);

            if (totalFlow != actualOptimalFlow) {
                float efficiency = getOverflowEfficiency(totalFlow, actualOptimalFlow, overflowMultiplier);
                tEU *= efficiency;
            }
            tEU = GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L);

            // If next output is above the maximum the dynamo can handle, set it to the maximum instead of exploding the
            // turbine
            // Raising the maximum allowed flow rate to account for the efficiency changes beyond the optimal flow
            // When the max fuel consumption rate was increased, turbines could explode on world load
            if (tEU > getMaximumOutput()) {
                tEU = GT_Utility.safeInt(getMaximumOutput());
            }
            return tEU;
        }
        return 0;
    }

    @Override
    float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        // overflowMultiplier changes how quickly the turbine loses efficiency after flow goes beyond the optimal value
        // At the default value of 1, any flow will generate less EU/t than optimal flow, regardless of the amount of
        // fuel used
        // The bigger this number is, the slower efficiency loss happens as flow moves beyond the optimal value
        // Gases are the second most efficient in this regard, with plasma being the most efficient
        float efficiency = 0;

        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow))
                    / ((float) actualOptimalFlow * ((overflowMultiplier * 3) - 1));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }

        return efficiency;
    }
}
