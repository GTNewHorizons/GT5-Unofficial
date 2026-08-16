package tectech.thing.metaTileEntity.multi.godforge;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.casing.Casings.BoundlessGravitationallySeveredStructureCasing;
import static gregtech.api.casing.Casings.CelestialMatterGuidanceCasing;
import static gregtech.api.casing.Casings.HarmonicPhononTransmissionConduit;
import static gregtech.api.casing.Casings.SingularityReinforcedStellarShieldingCasing;
import static gregtech.api.casing.Casings.StellarEnergySiphonCasing;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_MULTISTEP;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_TIER;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.math.BigInteger;
import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEPlasmaModuleGui;
import tectech.loader.ConfigHandler;
import tectech.recipe.TecTechRecipeMaps;

public class MTEPlasmaModule extends MTEBaseModule {

    private long EUt = 0;
    private int currentParallel = 0;
    private int inputMaxParallel = 0;

    public MTEPlasmaModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPlasmaModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPlasmaModule(mName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                BigInteger powerForRecipe = BigInteger.valueOf(getSafeProcessingVoltage())
                    .multiply(BigInteger.valueOf(getActualParallel()))
                    .multiply(BigInteger.valueOf(recipe.mDuration));
                if (getUserEU(userUUID).compareTo(powerForRecipe) < 0) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                }
                if (recipe.getMetadataOrDefault(FOG_PLASMA_TIER, 0) > getPlasmaTier()
                    || (recipe.getMetadataOrDefault(FOG_PLASMA_MULTISTEP, false) && !isMultiStepPlasmaCapable)) {
                    return SimpleCheckRecipeResult.ofFailure("missing_upgrades");
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
                addToRecipeTally(calculatedParallels);
                currentParallel = calculatedParallels;
                EUt = calculatedEut;
                overwriteCalculatedEut(0);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(getSafeProcessingVoltage())
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

    public int getInputMaxParallel() {
        return inputMaxParallel;
    }

    public void setInputMaxParallelDebug(int val) {
        // need to check server side if we have permission
        if (GTUtility.isClient() || GTUtility.isServer() && ConfigHandler.debug.DEBUG_MODE) inputMaxParallel = val;
    }

    public void setPlasmaTierDebug(int tier) {
        // need to check server side if we have permission
        if (GTUtility.isClient() || GTUtility.isServer() && ConfigHandler.debug.DEBUG_MODE) setPlasmaTier(tier);
    }

    public void setMultiStepPlasmaDebug(boolean isCapable) {
        // need to check server side if we have permission
        if (GTUtility.isClient() || GTUtility.isServer() && ConfigHandler.debug.DEBUG_MODE)
            setMultiStepPlasma(isCapable);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEPlasmaModuleGui(this);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.godforgePlasmaRecipes;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>();
        str.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.progress",
                GREEN + formatNumber(mProgresstime / 20) + RESET,
                YELLOW + formatNumber(mMaxProgresstime / 20) + RESET));
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.currently_using",
                RED + (getBaseMetaTileEntity().isActive() ? formatNumber(EUt) : "0") + RESET));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.max_parallel",
                RESET + formatNumber(getActualParallel())));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "GT5U.infodata.parallel.current",
                RESET + (getBaseMetaTileEntity().isActive() ? formatNumber(currentParallel) : "0")));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.multiplier.recipe_time",
                RESET + formatNumber(getSpeedBonus())));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.multiplier.energy",
                RESET + formatNumber(getEnergyDiscount())));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.divisor.recipe_time.non_perfect_oc",
                RESET + formatNumber(getOverclockTimeFactor())));
        return str.toArray(new String[0]);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.plasma_fabricator"))
            .addMarkdown(new ResourceLocation("gregtech", "godforge-plasma-module"))
            .beginStructureBlock(7, 7, 13, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_4th_layer"))
            .addCasing("0-20", SingularityReinforcedStellarShieldingCasing.getLocalizedName(), false)
            .addCasing("20", BoundlessGravitationallySeveredStructureCasing.getLocalizedName(), false)
            .addCasing("5", CelestialMatterGuidanceCasing.getLocalizedName(), false)
            .addCasing("5", HarmonicPhononTransmissionConduit.getLocalizedName(), false)
            .addCasing("1", StellarEnergySiphonCasing.getLocalizedName(), false)
            .addInputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .addInputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .addOutputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

}
