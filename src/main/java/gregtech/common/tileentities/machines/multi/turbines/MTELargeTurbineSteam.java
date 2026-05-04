package gregtech.common.tileentities.machines.multi.turbines;

import static gregtech.api.enums.GTValues.STEAM_PER_WATER;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_EMPTY5;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;

public class MTELargeTurbineSteam extends MTELargeTurbineBase {

    private int excessWater;
    private boolean achievement = false;

    public MTELargeTurbineSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineSteam(mName);
    }

    @Override
    public Casings getTurbineCasing() {
        return Casings.TurbineCasing;
    }

    @Override
    public Materials getFrameMaterial() {
        return Materials.Iron;
    }

    @Override
    public Casings getPipeCasing() {
        return Casings.SteelPipeCasing;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return new ITexture[] { MACHINE_CASINGS[1][colorIndex + 1],
            aFacing == side ? (aActive ? TextureFactory.builder()
                .addIcon(LARGETURBINE_NEW_ACTIVE5)
                .build()
                : hasTurbine() ? TextureFactory.builder()
                    .addIcon(LARGETURBINE_NEW5)
                    .build()
                    : TextureFactory.builder()
                        .addIcon(LARGETURBINE_NEW_EMPTY5)
                        .build())
                : getTurbineCasing().getCasingTexture() };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Steam Turbine, LST")
            .addInfo("Needs a Turbine, place inside controller")
            .addInfo("Outputs Distilled Water as well as producing power")
            .addInfo("Power output depends on turbine and fitting")
            .addInfo("Use screwdriver to adjust fitting of turbine")
            .beginStructureBlock(3, 3, 6, false)
            .addController("Front center")
            .addCasingInfoRange("Turbine Casing", 8, 16, false)
            .addCasingInfoExactly("Iron Frame Box", 14, false)
            .addCasingInfoExactly("Steel Pipe Casing", 12, false)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Any Turbine Casing except the front 8", 2)
            .addInputHatch("Steam, Any Turbine Casing except the front 8", 2)
            .addOutputHatch("Distilled Water, Any Turbine Casing except the front 8", 2)
            .addOtherStructurePart("Air", "3x3 area in front of controller")
            .addStructureAuthors(EnumChatFormatting.GOLD + "hugetrust")
            .toolTipFinisher();
        return tt;
    }

    private int condenseSteam(int steam) {
        excessWater += steam;
        int water = excessWater / STEAM_PER_WATER;
        excessWater %= STEAM_PER_WATER;
        return water;
    }

    @Override
    public int fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        int tEU = 0;
        int totalFlow = 0;
        int flow = 0;

        this.realOptFlow = looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow();
        int remainingFlow = GTUtility.safeInt((long) (realOptFlow * (0.5f * turbine.getOverflowEfficiency() + 1)));

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            final FluidStack aFluidStack = aFluids.get(i);
            if (GTModHandler.isAnySteam(aFluidStack)) {
                flow = Math.min(aFluidStack.amount, remainingFlow);
                depleteInput(new FluidStack(aFluidStack, flow));
                this.storedFluid += aFluidStack.amount;
                remainingFlow -= flow;
                totalFlow += flow;
                if (!achievement) {
                    GTMod.achievements.issueAchievement(
                        this.getBaseMetaTileEntity()
                            .getWorld()
                            .getPlayerEntityByName(
                                this.getBaseMetaTileEntity()
                                    .getOwnerName()),
                        "muchsteam");
                    achievement = true;
                }
            } else if (GTModHandler.isSuperHeatedSteam(aFluidStack)) {
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }

        if (totalFlow <= 0) return 0;

        tEU = totalFlow;
        int waterToOutput = condenseSteam(totalFlow);
        addOutput(GTModHandler.getDistilledWater(waterToOutput));

        if (totalFlow == GTUtility.safeInt((long) realOptFlow)) {
            tEU = GTUtility.safeInt(
                (long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency()) * 0.5f));
        } else {
            float efficiency = getOverflowEfficiency(
                totalFlow,
                GTUtility.safeInt((long) realOptFlow),
                overflowMultiplier);
            tEU *= efficiency;
            tEU = Math.max(
                1,
                GTUtility.safeInt(
                    (long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency())
                        * 0.5f)));
        }

        if (tEU > getMaximumOutput()) {
            tEU = GTUtility.safeInt(getMaximumOutput());
        }

        return tEU;
    }

    @Override
    protected float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        float efficiency;
        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f
                - Math.abs((totalFlow - actualOptimalFlow)) / ((float) actualOptimalFlow * (overflowMultiplier + 1));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }
        return efficiency;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && XSTR_INSTANCE.nextInt(4) == 0) ? 0 : 1;
    }
}
