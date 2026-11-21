package gregtech.common.tileentities.machines.multi.xlturbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;

@IMetaTileEntity.SkipGenerateDescription
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
        tt.addMachineType(StatCollector.translateToLocalFormatted("gt.mbtt.machine_type.steam_turbine", "XLST-SC"))
            .addMarkdown(new ResourceLocation("gregtech", "xl-sc-steam-turbine"))
            .addSupportAny()
            .beginStructureBlock(9, 9, 29, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_5th_layer"))
            .addCasing(minCasingAmount() + "-440", getCasing().getLocalizedName(), false)
            .addCasing("100", getRotorCasing().getLocalizedName(), false)
            .addCasing("36", StatCollector.translateToLocal("gt.mbtt.structure.any_tiered_glass"), false)
            .addCasing("34", OrePrefixes.frameGt.getLocalizedNameForItem(getFrameMaterial()), false)
            .addCasing("20", Casings.MVSolenoidSuperconductorCoil.getLocalizedName(), false)
            .addCasing("16", Casings.TurbineShaft.getLocalizedName(), false)
            .addDynamoHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_turbine_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_turbine_casing"), 1)
            .addInputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_turbine_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_turbine_casing"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_turbine_casing"), 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
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
                denseFlow = Math.min(
                    aFluids.get(i).amount,
                    MathUtils.safeInt((long) Math.ceil(remainingFlow / (double) DENSE_STEAM_RATIO)));
                if (denseFlow <= 0) {
                    continue;
                }
                int effectiveFlow = Math.min(remainingFlow, denseFlow * DENSE_STEAM_RATIO);
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
            addOutputPartial(Materials.DenseSuperheatedSteam.getGas((long) steamFlowForNextSteam));
        } else {
            addOutputPartial(FluidRegistry.getFluidStack("ic2superheatedsteam", totalFlow));
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
