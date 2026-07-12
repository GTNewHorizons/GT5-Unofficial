package gregtech.common.tileentities.machines.multi.xlturbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;

public class MTEXLTurbineHPSteam extends MTEXLTurbineBase {

    public boolean achievement = false;
    private boolean isUsingDenseSteam;

    public MTEXLTurbineHPSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEXLTurbineHPSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEXLTurbineHPSteam(mName);
    }

    @Override
    protected Casings getCasing() {
        return Casings.ReinforcedHPSteamTurbineCasing;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Titanium;
    }

    @Override
    protected Casings getRotorCasing() {
        return Casings.TitaniumPipeCasing;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Steam Turbine, XLST-HP")
            .addInfo("Same throughput as 16 LST-HP with only 12 turbines")
            .addInfo("Generates power from regular or dense Superheated (SH) Steam based on the turbine and fitting")
            .addInfo("Outputs 1L of Steam for every 1L of Superheated Steam")
            .addInfo("Use a screwdriver to adjust the fitting of the turbines")
            .addInfo("Loose fit increases flow in exchange for efficiency")
            .addInfo("Dense types of steam are so energy packed, they only require 1/1000th of the original flow")
            .addSupportAny()
            .beginStructureBlock(29, 9, 9, true)
            .addController("Front center")
            .addCasing(minCasingAmount() + "-440", "Reinforced HP Steam Turbine Casing", false)
            .addCasing("100", "Titanium Pipe Casing", false)
            .addCasing("36", "Any Tiered Glass", false)
            .addCasing("34", "Titanium Frame Box", false)
            .addCasing("20", "MV Solenoid Superconductor Coil", false)
            .addCasing("16", "Turbine Shaft", false)
            .addDynamoHatch("1+", "Any turbine casing", 1)
            .addMaintenanceHatch("1", "Any turbine casing", 1)
            .addInputBus("0+", "Any turbine casing", 1)
            .addInputHatch("1+", "Any turbine casing", 1)
            .addOutputHatch("1+", "Any turbine casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        long tEU = 0;
        int totalFlow = 0;
        int flow = 0;
        int denseFlow = 0;
        int steamFlowForNextSteam = 0;

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
                case "ic2.fluidSuperheatedSteam" -> {
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
                                "efficientsteam");
                        } catch (Exception ignored) {}
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
                case "fluid.steam", "ic2.fluidSteam", "fluid.mfr.steam.still.name" -> depleteInput(
                    new FluidStack(aFluids.get(i), aFluids.get(i).amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        if (isUsingDenseSteam) {
            addOutputPartial(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DenseSteam,
                    Materials2FluidShapes.fluidGas,
                    (int) ((long) steamFlowForNextSteam)));
        } else {
            addOutputPartial(Materials.Steam.getGas(totalFlow));
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
