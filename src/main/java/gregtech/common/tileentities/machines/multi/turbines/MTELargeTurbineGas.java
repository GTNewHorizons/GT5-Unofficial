package gregtech.common.tileentities.machines.multi.turbines;

import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_EMPTY5;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;

public class MTELargeTurbineGas extends MTELargeTurbineBase {

    public MTELargeTurbineGas(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineGas(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineGas(mName);
    }

    @Override
    public Casings getTurbineCasing() {
        return Casings.StainlessSteelTurbineCasing;
    }

    @Override
    public Materials getFrameMaterial() {
        return Materials.StainlessSteel;
    }

    @Override
    public Casings getPipeCasing() {
        return Casings.InsulatedFluidPipeCasing;
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
        tt.addMachineType("Gas Turbine, LGT")
            .addInfo("Needs a Turbine, place inside controller")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 6, false)
            .addController("Front center")
            .addCasingInfoRange("Stainless Steel Turbine Casing", 8, 16, false)
            .addCasingInfoExactly("Stainless Steel Frame Box", 14, false)
            .addCasingInfoExactly("Insulated Fluid Pipe Casing", 12, false)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Any Stainless Steel Turbine Casing except the front 8", 2)
            .addMufflerHatch("Stainless Steel Turbine Casing except the front 8", 2)
            .addInputHatch("Gas Fuel, Stainless Steel Turbine Casing except the front 8", 2)
            .addOtherStructurePart("Air", "3x3 area in front of controller")
            .addStructureAuthors(EnumChatFormatting.GOLD + "hugetrust")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.gasTurbineFuels;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionLargeGasTurbinePerSecond;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GTRecipe tFuel = RecipeMaps.gasTurbineFuels.getBackend()
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

        // If the turbine is too weak for the fuel, consume exactly 1L and output
        // the turbine's optimal EU/t directly rather than trying to scale flow
        if (turbine.getOptimalGasEUt() < fuelValue) {
            this.realOptFlow = 1;
            depleteInput(new FluidStack(firstFuelType, 1));
            this.storedFluid += 1;
            return GTUtility.safeInt((long) turbine.getOptimalGasEUt());
        }

        int actualOptimalFlow = GTUtility
            .safeInt((long) ((looseFit ? turbine.getOptimalLooseGasFlow() : turbine.getOptimalGasFlow()) / fuelValue));
        this.realOptFlow = actualOptimalFlow;

        // Allowed up to 450% optimal flow depending on overflowMultiplier:
        // - 150% if overflowMultiplier = 1
        // - 300% if overflowMultiplier = 2
        // - 450% if overflowMultiplier = 3
        int remainingFlow = GTUtility.safeInt((long) (actualOptimalFlow * (1.5f * turbine.getOverflowEfficiency())));
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

        if (totalFlow <= 0) return 0;

        int tEU = GTUtility.safeInt((long) totalFlow * fuelValue);

        if (totalFlow != actualOptimalFlow) {
            float efficiency = getOverflowEfficiency(totalFlow, actualOptimalFlow, turbine.getOverflowEfficiency());
            tEU *= efficiency;
        }

        tEU = GTUtility
            .safeInt((long) (tEU * (looseFit ? turbine.getLooseGasEfficiency() : turbine.getGasEfficiency())));

        if (tEU > getMaximumOutput()) {
            tEU = GTUtility.safeInt(getMaximumOutput());
        }

        return tEU;
    }

    @Override
    protected float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        // Gas is second most efficient after plasma
        // Uses (overflowMultiplier * 3) - 1 vs plasma's * 3 + 1
        float efficiency;
        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow))
                / ((float) actualOptimalFlow * ((overflowMultiplier * 3) - 1));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }
        return efficiency;
    }
}
