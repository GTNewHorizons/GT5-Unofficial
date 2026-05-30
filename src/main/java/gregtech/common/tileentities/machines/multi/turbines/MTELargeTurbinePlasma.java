package gregtech.common.tileentities.machines.multi.turbines;

import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_EMPTY5;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;

public class MTELargeTurbinePlasma extends MTELargeTurbineBase {

    public MTELargeTurbinePlasma(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbinePlasma(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbinePlasma(mName);
    }

    @Override
    public Casings getTurbineCasing() {
        return Casings.TungstensteelTurbineCasing;
    }

    @Override
    public Materials getFrameMaterial() {
        return Materials.TungstenSteel;
    }

    @Override
    public Casings getPipeCasing() {
        return Casings.TungstensteelPipeCasing;
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
        tt.addMachineType("Plasma Turbine, LPT")
            .addInfo("Needs a Turbine, place inside controller")
            .addInfo("Use your Fusion Reactor to produce the Plasma")
            .beginStructureBlock(3, 3, 6, false)
            .addController("Front center")
            .addCasingInfoRange("Tungstensteel Turbine Casing", 8, 16, false)
            .addCasingInfoExactly("Tungstensteel Frame Box", 14, false)
            .addCasingInfoExactly("Tungstensteel Pipe Casing", 12, false)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Any Tungstensteel Turbine Casing except the front 8", 2)
            .addInputHatch("Plasma Fluid, Any Tungstensteel Turbine Casing except the front 8", 2)
            .addOutputHatch("Molten Fluid, optional, Any Tungstensteel Turbine Casing except the front 8", 2)
            .addOtherStructurePart("Air", "3x3 area in front of controller")
            .addStructureAuthors(EnumChatFormatting.GOLD + "hugetrust")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.plasmaFuels;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GTRecipe tFuel = RecipeMaps.plasmaFuels.getBackend()
            .findFuel(aLiquid);
        if (tFuel != null) return tFuel.mSpecialValue;
        return 0;
    }

    @Override
    public int fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        if (aFluids.isEmpty()) return 0;

        FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0);
        int fuelValue = getFuelValue(firstFuelType);
        if (fuelValue <= 0) return 0;

        int actualOptimalFlow = GTUtility.safeInt(
            (long) Math.ceil(
                (double) (looseFit ? turbine.getOptimalLoosePlasmaFlow() : turbine.getOptimalPlasmaFlow()) * 20
                    / (double) fuelValue));
        this.realOptFlow = actualOptimalFlow;

        int remainingFlow = GTUtility
            .safeInt((long) (actualOptimalFlow * (1.5f * turbine.getOverflowEfficiency() + 1)));
        int flow = 0;
        int totalFlow = 0;

        storedFluid = 0;
        for (FluidStack aFluid : aFluids) {
            if (aFluid.isFluidEqual(firstFuelType)) {
                flow = Math.min(aFluid.amount, remainingFlow);
                depleteInput(new FluidStack(aFluid, flow));
                this.storedFluid += aFluid.amount;
                remainingFlow -= flow;
                totalFlow += flow;
            }
        }

        // Output the depowered fluid (e.g. plasma.helium -> helium or molten.helium)
        String fn = FluidRegistry.getFluidName(firstFuelType);
        String[] nameSegments = fn.split("\\.", 2);
        if (nameSegments.length == 2) {
            String outputName = nameSegments[1];
            FluidStack output = FluidRegistry.getFluidStack(outputName, totalFlow);
            if (output == null) {
                output = FluidRegistry.getFluidStack("molten." + outputName, totalFlow);
            }
            if (output != null) addOutput(output);
        }

        if (totalFlow <= 0) return 0;

        int tEU = GTUtility.safeInt((long) ((fuelValue / 20D) * totalFlow));

        if (totalFlow != actualOptimalFlow) {
            float efficiency = getOverflowEfficiency(totalFlow, actualOptimalFlow, turbine.getOverflowEfficiency());
            tEU = (int) (tEU * efficiency);
        }

        tEU = GTUtility
            .safeInt((long) ((looseFit ? turbine.getLoosePlasmaEfficiency() : turbine.getPlasmaEfficiency()) * tEU));

        if (tEU > getMaximumOutput()) {
            tEU = GTUtility.safeInt(getMaximumOutput());
        }

        return tEU;
    }

    @Override
    protected float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        float efficiency;
        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow))
                / ((float) actualOptimalFlow * ((overflowMultiplier * 3) + 1));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }
        return efficiency;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        CheckRecipeResult status = super.checkProcessing();
        if (status == CheckRecipeResultRegistry.GENERATING) {
            // Plasma turbine runs once every 20 ticks instead of every tick
            this.mMaxProgresstime = 20;
            this.mEfficiencyIncrease = 200;
        }
        return status;
    }
}
