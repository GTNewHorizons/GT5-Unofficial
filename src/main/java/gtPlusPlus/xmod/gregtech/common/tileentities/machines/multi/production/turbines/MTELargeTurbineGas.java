package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import static gtPlusPlus.core.lib.GTPPCore.RANDOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TurbineStatCalculator;

public class MTELargeTurbineGas extends MTELargerTurbineBase {

    private static final HashSet<Fluid> BLACKLIST = new HashSet<>();

    static {
        BLACKLIST.add(
            Materials.Benzene.getFluid(0)
                .getFluid());
    }

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
    public int getCasingMeta() {
        return 3;
    }

    @Override
    public int getCasingTextureIndex() {
        return 58;
    }

    @Override
    protected boolean requiresOutputHatch() {
        return false;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 4000;
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) {
            return 0;
        }
        GTRecipe tFuel = getRecipeMap().getBackend()
            .findFuel(aLiquid);
        if (tFuel != null) {
            return tFuel.mSpecialValue;
        }
        return 0;
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return RecipeMaps.gasTurbineFuels;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -20;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        List<FluidStack> fluids = getStoredFluids();
        for (FluidStack fluid : fluids) {
            if (fluid != null && BLACKLIST.contains(fluid.getFluid())) {
                return SimpleCheckRecipeResult.ofFailure("fuel_blacklisted");
            }
        }
        return super.checkProcessing();
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        if (!aFluids.isEmpty()) {
            int tEU = 0;
            int actualOptimalFlow = 0;
            FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0); // Identify a SINGLE type of fluid to process.
                                                                          // Doesn't matter which one. Ignore the rest!
            int fuelValue = getFuelValue(firstFuelType);
            // log("Fuel Value of "+aFluids.get(0).getLocalizedName()+" is "+fuelValue+"eu");
            if (fuelValue <= 0) {
                return 0;
            }

            if (turbine.getOptimalGasEUt() < fuelValue) {
                // turbine too weak and/or fuel too powerful
                // at least consume 1L
                this.realOptFlow = 1;
                // wastes the extra fuel and generate aOptFlow directly
                depleteInput(new FluidStack(firstFuelType, 1));
                this.storedFluid += 1;
                return GTUtility.safeInt((long) (turbine.getOptimalGasEUt()));
            }

            actualOptimalFlow = GTUtility.safeInt(
                (long) (getSpeedMultiplier()
                    * ((isLooseMode() ? turbine.getOptimalLooseGasFlow() : turbine.getOptimalGasFlow()) / fuelValue)));
            this.realOptFlow = actualOptimalFlow;

            int remainingFlow = GTUtility.safeInt((long) (actualOptimalFlow * 1.25f)); // Allowed to use up to 125% of
                                                                                       // optimal flow. Variable
                                                                                       // required outside of loop for
            // multi-hatch scenarios.
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow); // try to use up to 125% of optimal flow w/o
                                                                   // exceeding remainingFlow
                    depleteInput(new FluidStack(aFluid, flow)); // deplete that amount
                    this.storedFluid += aFluid.amount;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GTUtility.safeInt((long) totalFlow * fuelValue);

            if (totalFlow != actualOptimalFlow) {
                float efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
                tEU *= efficiency;
            }
            tEU = GTUtility
                .safeInt((long) (tEU * (isLooseMode() ? turbine.getLooseGasEfficiency() : turbine.getGasEfficiency())));

            return tEU;
        }
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (RANDOM.nextInt(4) == 0) ? 0 : 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public String getMachineType() {
        return "Large Gas Turbine, XLGT";
    }

    @Override
    protected String getTurbineType() {
        return "Gas";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced Gas Turbine Casing";
    }

    @Override
    protected boolean isDenseSteam() {
        return false;
    }
}
