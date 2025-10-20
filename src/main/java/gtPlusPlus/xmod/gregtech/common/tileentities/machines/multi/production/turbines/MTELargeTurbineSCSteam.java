package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TurbineStatCalculator;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;

public class MTELargeTurbineSCSteam extends MTELargerTurbineBase {

    private boolean hasConsumedSteam;
    private boolean isUsingDenseSteam;

    public MTELargeTurbineSCSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineSCSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineSCSteam(mName);
    }

    @Override
    public int getCasingMeta() {
        return 15;
    }

    @Override
    public int getCasingTextureIndex() {
        return 1538;
    }

    @Override
    protected boolean requiresOutputHatch() {
        return true;
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        return 0;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {

        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        float denseFlow = 0;
        float steamFlowForNextSteam = 0;
        int steamInHatch = 0;
        // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = getSpeedMultiplier()
            * (looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow());
        // this.realOptFlow = (double) aOptFlow * (double) flowMultipliers[0];
        // Will there be an multiplier for SC?
        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to use up to
        // 125% of optimal flow.
        float remainingDenseFlow = 0;

        storedFluid = 0;
        FluidStack tSCSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i)
                .getFluid()
                .getUnlocalizedName(aFluids.get(i));
            if (GTUtility.areFluidsEqual(aFluids.get(i), tSCSteam, true)) {
                if (!hasConsumedSteam) {
                    hasConsumedSteam = true;
                    isUsingDenseSteam = false;
                } else if (isUsingDenseSteam) {
                    continue;
                }
                flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
            } else if (fluidName.equals("fluid.densesupercriticalsteam")) {
                if (!hasConsumedSteam) {
                    hasConsumedSteam = true;
                    isUsingDenseSteam = true;
                } else if (!isUsingDenseSteam) {
                    continue;
                }
                steamInHatch = aFluids.get(i).amount;
                remainingDenseFlow = (float) remainingFlow / 1000; // Dense Steam is 1000x the EU value
                denseFlow = Math.min(steamInHatch, remainingDenseFlow); // try to use up w/o exceeding
                                                                        // remainingDenseFlow
                depleteInput(new FluidStack(aFluids.get(i), (int) denseFlow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= denseFlow * 1000; // track amount we're allowed to continue depleting from hatches
                totalFlow += denseFlow * 1000; // track total input used
                steamFlowForNextSteam += denseFlow;
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow; // SC Steam has 1 EU per litre so the flow equals base EU produced
        if (isUsingDenseSteam) {
            addOutput(Materials.DenseSuperheatedSteam.getGas((long) steamFlowForNextSteam));
        } else {
            addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", totalFlow));

        }
        if (totalFlow != realOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
            // if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(
                1,
                MathUtils.safeInt(
                    (long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency()))));
        } else {
            tEU = MathUtils
                .safeInt((long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency())));
        }
        return tEU;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && GTPPCore.RANDOM.nextInt(4) == 0) ? 0 : 1;
    }

    @Override
    public String getMachineType() {
        return "Large Supercritical Steam Turbine, XLSCT";
    }

    @Override
    protected String getTurbineType() {
        return "Supercritical Steam";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced SC Turbine Casing";
    }

    @Override
    protected boolean isDenseSteam() {
        return isUsingDenseSteam;
    }
}
