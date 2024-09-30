package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.TurbineStatCalculator;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;

@SuppressWarnings("deprecation")
public class MTELargerTurbineSHSteam extends MTELargerTurbine {

    public boolean achievement = false;
    private boolean isUsingDenseSteam;

    public MTELargerTurbineSHSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargerTurbineSHSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargerTurbineSHSteam(mName);
    }

    @Override
    public int getCasingMeta() {
        return 2;
    }

    @Override
    public int getCasingTextureIndex() {
        return 59;
    }

    @Override
    protected boolean requiresOutputHatch() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        return 0;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {

        long tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        float denseFlow = 0;
        float steamFlowForNextSteam = 0;
        int steamInHatch = 0;

        // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = getSpeedMultiplier()
            * (looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow());

        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to use up to
        // 125% of optimal flow.
        float remainingDenseFlow = 0;

        boolean hasConsumedSteam = false;

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i)
                .getFluid()
                .getUnlocalizedName(aFluids.get(i));
            switch (fluidName) {
                case "ic2.fluidSuperheatedSteam" -> {
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
                    if (!achievement) {
                        try {
                            GTMod.achievements.issueAchievement(
                                this.getBaseMetaTileEntity()
                                    .getWorld()
                                    .getPlayerEntityByName(
                                        this.getBaseMetaTileEntity()
                                            .getOwnerName()),
                                "efficientsteam");
                        } catch (Exception e) {}
                        achievement = true;
                    }
                }
                case "fluid.densesuperheatedsteam" -> {
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
                case "fluid.steam", "ic2.fluidSteam", "fluid.mfr.steam.still.name" -> depleteInput(
                    new FluidStack(aFluids.get(i), aFluids.get(i).amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow; // SH Steam has 1 EU per litre so the flow equals base EU produced
        if (isUsingDenseSteam) {
            addOutput(Materials.DenseSteam.getGas((long) steamFlowForNextSteam));
        } else {
            addOutput(GTModHandler.getSteam(totalFlow));
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
        return "Large Super-heated Steam Turbine";
    }

    @Override
    protected String getTurbineType() {
        return "Super-heated Steam";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced HP Steam Turbine Casing";
    }
}
