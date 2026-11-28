package gregtech.common.tileentities.machines.multi.xlturbines;

import static gtPlusPlus.core.lib.GTPPCore.RANDOM;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.util.math.MathUtils;

@IMetaTileEntity.SkipGenerateDescription
public class MTEXLTurbineSteam extends MTEXLTurbineBase {

    private float water;
    private boolean achievement = false;
    private boolean isUsingDenseSteam;

    public MTEXLTurbineSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEXLTurbineSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEXLTurbineSteam(mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocalFormatted("gt.mbtt.machine_type.steam_turbine", "XLST"))
            .addMarkdown(new ResourceLocation("gregtech", "xl-steam-turbine"))
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

    private int useWater(float input) {
        water = water + input;
        int usage = (int) water;
        water = water - usage;
        return usage;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        long tEU = 0;
        int totalFlow = 0;
        int flow = 0;
        int denseFlow = 0;
        int steamFlowForWater = 0;

        realOptFlow = getSpeedMultiplier()
            * (looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow());
        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f));
        boolean hasConsumedSteam = false;

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i)
                .getFluid()
                .getUnlocalizedName(aFluids.get(i));
            switch (fluidName) {
                case "fluid.steam", "ic2.fluidSteam", "fluid.mfr.steam.still.name" -> {
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
                    if (!achievement) {
                        try {
                            GTMod.achievements.issueAchievement(
                                getBaseMetaTileEntity().getWorld()
                                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()),
                                "muchsteam");
                        } catch (Exception ignored) {}
                        achievement = true;
                    }
                }
                case "fluid.densesteam" -> {
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
                    steamFlowForWater += effectiveFlow;
                }
                case "ic2.fluidSuperheatedSteam" -> depleteInput(new FluidStack(aFluids.get(i), aFluids.get(i).amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = (long) (totalFlow * 0.5f);
        int waterToOutput = isUsingDenseSteam ? useWater(steamFlowForWater / 160.1f) : useWater(totalFlow / 160.0f);
        addOutputPartial(GTModHandler.getDistilledWater(waterToOutput));
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
        return (looseFit && RANDOM.nextInt(4) == 0) ? 0 : 1;
    }
}
