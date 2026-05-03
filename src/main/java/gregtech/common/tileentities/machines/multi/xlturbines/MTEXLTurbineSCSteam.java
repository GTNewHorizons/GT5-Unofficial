package gregtech.common.tileentities.machines.multi.xlturbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;

public class MTEXLTurbineSCSteam extends MTEXLTurbineBase {

    private boolean isUsingDenseSteam;

    public MTEXLTurbineSCSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEXLTurbineSCSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEXLTurbineSCSteam(mName);
    }

    @Override
    public boolean requiresOutputHatch() {
        return true;
    }

    @Override
    protected Casings getCasing() {
        return Casings.ReinforcedSCTurbineCasing;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Naquadah;
    }

    @Override
    protected Casings getRotorCasing() {
        return Casings.BlackPlutoniumItemPipeCasing;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Steam Turbine, XLST-SC")
            .addInfo("Runs as fast as 16 Large Turbines of the same type, takes the space of 12")
            .addInfo("Right-click with screwdriver to enable loose fit")
            .addInfo("Optimal flow will increase or decrease depending on fitting")
            .addInfo("Loose fit increases flow in exchange for efficiency")
            .addInfo("Dense types of steam are so energy packed, they only require 1/1000th of the original flow")
            .addTecTechHatchInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 9, 29, false)
            .addController("Front center")
            .addCasingInfoMin("Reinforced SC Turbine Casing", 430, false)
            .addCasingInfoExactly("Black Plutonium Item Pipe Casing", 100, false)
            .addCasingInfoExactly("Any Tiered Glass", 36, false)
            .addCasingInfoExactly("Naquadah Frame Box", 34, false)
            .addCasingInfoExactly("MV Solenoid Superconductor Coil", 20, false)
            .addCasingInfoExactly("Turbine Shaft", 16, false)
            .addInputBus("Any Turbine Casing (Min 1)", 1)
            .addInputHatch("Any Turbine Casing (Min 1)", 1)
            .addOutputHatch("Any Turbine Casing (Min 1)", 1)
            .addDynamoHatch("Any Turbine Casing (Min 1)", 1)
            .addMaintenanceHatch("Any Turbine Casing (Min 1)", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        int tEU = 0;
        int totalFlow = 0;
        int flow = 0;
        int denseFlow = 0;
        int steamFlowForNextSteam = 0;
        boolean hasConsumedSteam = false;

        realOptFlow = getSpeedMultiplier()
            * (looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow());
        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f));

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
                flow = Math.min(aFluids.get(i).amount, remainingFlow);
                depleteInput(new FluidStack(aFluids.get(i), flow));
                storedFluid += flow;
                remainingFlow -= flow;
                totalFlow += flow;
            } else if (fluidName.equals("fluid.densesupercriticalsteam")) {
                if (!hasConsumedSteam) {
                    hasConsumedSteam = true;
                    isUsingDenseSteam = true;
                } else if (!isUsingDenseSteam) {
                    continue;
                }
                denseFlow = Math
                    .min(aFluids.get(i).amount, MathUtils.safeInt((long) Math.ceil(remainingFlow / 1000.0d)));
                if (denseFlow <= 0) {
                    continue;
                }
                int effectiveFlow = Math.min(remainingFlow, denseFlow * 1000);
                depleteInput(new FluidStack(aFluids.get(i), denseFlow));
                storedFluid += denseFlow;
                remainingFlow -= effectiveFlow;
                totalFlow += effectiveFlow;
                steamFlowForNextSteam += denseFlow;
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        if (isUsingDenseSteam) {
            addOutput(Materials.DenseSuperheatedSteam.getGas((long) steamFlowForNextSteam));
        } else {
            addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", totalFlow));
        }
        if (totalFlow != realOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
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
}
