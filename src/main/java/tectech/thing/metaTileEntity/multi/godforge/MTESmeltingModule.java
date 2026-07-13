package tectech.thing.metaTileEntity.multi.godforge;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTESmeltingModuleGui;

public class MTESmeltingModule extends MTEBaseModule {

    private long EUt = 0;
    private long currentParallel = 0;
    private boolean furnaceMode = false;

    public MTESmeltingModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESmeltingModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESmeltingModule(mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return furnaceMode ? RecipeMaps.furnaceRecipes : RecipeMaps.blastFurnaceRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.blastFurnaceRecipes, RecipeMaps.furnaceRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    long wirelessEUt = 0;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                if (recipe.mSpecialValue > getHeat()) {
                    return CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
                }

                if (recipe.mEUt > getProcessingVoltage()) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }

                wirelessEUt = (long) recipe.mEUt * getActualParallel();
                if (getUserEU(userUUID).compareTo(BigInteger.valueOf(wirelessEUt * recipe.mDuration)) < 0) {
                    return CheckRecipeResultRegistry.insufficientPower(wirelessEUt * recipe.mDuration);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                BigInteger powerForRecipe = BigInteger.valueOf(calculatedEut)
                    .multiply(BigInteger.valueOf(duration));
                if (!addEUToGlobalEnergyMap(userUUID, powerForRecipe.negate())) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                }
                addToPowerTally(powerForRecipe);
                if (!furnaceMode) {
                    addToRecipeTally(calculatedParallels);
                }
                currentParallel = calculatedParallels;
                EUt = calculatedEut;
                overwriteCalculatedEut(0);
                setCurrentRecipeHeat(recipe.mSpecialValue);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(getSafeProcessingVoltage())
                    .setRecipeHeat(recipe.mSpecialValue)
                    .setHeatOC(true)
                    .setHeatDiscount(true)
                    .setMachineHeat(Math.max(recipe.mSpecialValue, getHeatForOC()))
                    .setHeatDiscountMultiplier(getHeatEnergyDiscount())
                    .setDurationDecreasePerOC(getOverclockTimeFactor());
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(Integer.MAX_VALUE);
        logic.setAmperageOC(false);
        logic.setUnlimitedTierSkips();
        logic.setMaxParallel(getActualParallel());
        logic.setSpeedBonus(getSpeedBonus());
        logic.setEuModifier(getEnergyDiscount());
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTESmeltingModuleGui(this);
    }

    public boolean isFurnaceModeOn() {
        return furnaceMode;
    }

    public void setFurnaceMode(boolean enabled) {
        // TODO: Replace with machineMode
        furnaceMode = enabled;
        // The machine is using a different recipemap now
        // Clear the cached recipe
        setSingleRecipeCheck(null);
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setBoolean("furnaceMode", furnaceMode);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        furnaceMode = NBT.getBoolean("furnaceMode");
        super.loadNBTData(NBT);
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>();
        str.add(
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.progress",
                GREEN + formatNumber(mProgresstime / 20) + RESET,
                YELLOW + formatNumber(mMaxProgresstime / 20) + RESET));
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.infodata.multi.currently_using",
                RED + (getBaseMetaTileEntity().isActive() ? formatNumber(EUt) : "0") + RESET));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.max_parallel", YELLOW + formatNumber(getActualParallel()) + RESET));
        str.add(
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.parallel.current",
                YELLOW + (getBaseMetaTileEntity().isActive() ? formatNumber(currentParallel) : "0") + RESET));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.capacity.heat", YELLOW + formatNumber(getHeat()) + RESET));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.capacity.heat.effective", YELLOW + formatNumber(getHeatForOC()) + RESET));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.multiplier.recipe_time", YELLOW + formatNumber(getSpeedBonus()) + RESET));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.multiplier.energy", YELLOW + formatNumber(getEnergyDiscount()) + RESET));
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.infodata.multi.divisor.recipe_time.non_perfect_oc",
                YELLOW + formatNumber(getOverclockTimeFactor()) + RESET));
        return str.toArray(new String[0]);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Blast Furnace, Furnace")
            .addInfo("This is a module of the Godforge")
            .addInfo("Must be part of a Godforge to function")
            .addInfo("Used for basic smelting operations at various temperatures")
            .addSeparator(EnumChatFormatting.AQUA, 74)
            .addInfo("As the first of the Godforge modules, this module performs the most basic")
            .addInfo("thermal processing, namely smelting materials identically to a furnace or blast furnace")
            .addInfo("The desired method of processing can be selected in the gui")
            .addInfo("This module is specialized towards speed and high heat levels")
            .beginStructureBlock(13, 7, 7, false)
            .addController("Front center, 4th layer")
            .addCasing("0-20", "Singularity Reinforced Stellar Shielding Casing", false)
            .addCasing("20", "Boundless Gravitationally Severed Structure Casing", false)
            .addCasing("5", "Celestial Matter Guidance Casing", false)
            .addCasing("5", "Hypogen Coil Block", false)
            .addCasing("1", "Stellar Energy Siphon Casing", false)
            .addInputBus("0+", "Any front shielding casing", 1)
            .addInputHatch("0+", "Any front shielding casing", 1)
            .addOutputBus("0+", "Any front shielding casing", 1)
            .addOutputHatch("0+", "Any front shielding casing", 1)
            .toolTipFinisher();
        return tt;
    }

}
