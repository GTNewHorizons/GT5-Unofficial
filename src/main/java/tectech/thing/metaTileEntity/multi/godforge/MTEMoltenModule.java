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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEMoltenModuleGui;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;

@IMetaTileEntity.SkipGenerateDescription
public class MTEMoltenModule extends MTEBaseModule {

    private long EUt = 0;
    private int currentParallel = 0;
    private boolean alloyMode = false;
    private boolean alloyCapable = false;

    public MTEMoltenModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMoltenModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMoltenModule(mName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GorgeModuleProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > getHeat()) {
                    return CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
                }

                if (recipe.mEUt > getProcessingVoltage()) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }

                BigInteger powerForRecipe = predictDrainedEnergy(recipe);
                if (getUserEU(userUUID).compareTo(powerForRecipe) < 0) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                }
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

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                BigInteger powerForRecipe = BigInteger.valueOf(calculatedEut)
                    .multiply(BigInteger.valueOf(duration));
                if (!addEUToGlobalEnergyMap(userUUID, powerForRecipe.negate())) {
                    stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                    return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                }
                addToPowerTally(powerForRecipe);
                addToRecipeTally(calculatedParallels);
                currentParallel = calculatedParallels;
                EUt = calculatedEut;
                overwriteCalculatedEut(0);
                setCurrentRecipeHeat(recipe.mSpecialValue);
                return CheckRecipeResultRegistry.SUCCESSFUL;
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
        return new MTEMoltenModuleGui(this);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return alloyMode ? TecTechRecipeMaps.godforgeAlloyRecipes : TecTechRecipeMaps.godforgeMoltenRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(TecTechRecipeMaps.godforgeMoltenRecipes, TecTechRecipeMaps.godforgeAlloyRecipes);
    }

    public boolean isAlloyModeOn() {
        return alloyMode;
    }

    public void setAlloyMode(boolean enabled) {
        alloyMode = enabled;
        // The machine is using a different recipemap now
        // Clear the cached recipe
        setSingleRecipeCheck(null);
    }

    public boolean isAlloyCapable() {
        return alloyCapable;
    }

    public void setAlloyCapable(boolean capable) {
        alloyCapable = capable;
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setBoolean("alloyMode", alloyMode);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        alloyMode = NBT.getBoolean("alloyMode");
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
                .encode("tt.infodata.multi.max_parallel", RESET + formatNumber(getActualParallel())));
        str.add(
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.parallel.current",
                RESET + (getBaseMetaTileEntity().isActive() ? formatNumber(currentParallel) : "0")));
        str.add(IGregTechDeviceInformation.encode("tt.infodata.multi.capacity.heat", RESET + formatNumber(getHeat())));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.capacity.heat.effective", RESET + formatNumber(getHeatForOC())));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.multiplier.recipe_time", RESET + formatNumber(getSpeedBonus())));
        str.add(
            IGregTechDeviceInformation
                .encode("tt.infodata.multi.multiplier.energy", RESET + formatNumber(getEnergyDiscount())));
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.infodata.multi.divisor.recipe_time.non_perfect_oc",
                RESET + formatNumber(getOverclockTimeFactor())));
        return str.toArray(new String[0]);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.blast_smelter"))
            .addMarkdown(new ResourceLocation("gregtech", "godforge-molten-module"))
            .beginStructureBlock(7, 7, 13, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_4th_layer"))
            .addCasing("0-20", CustomItemList.Godforge_SingularityShieldingCasing.getDisplayName(), false)
            .addCasing("20", CustomItemList.Godforge_BoundlessStructureCasing.getDisplayName(), false)
            .addCasing("5", CustomItemList.Godforge_GuidanceCasing.getDisplayName(), false)
            .addCasing("5", CustomItemList.Godforge_HarmonicPhononTransmissionConduit.getDisplayName(), false)
            .addCasing("1", CustomItemList.Godforge_StellarEnergySiphonCasing.getDisplayName(), false)
            .addInputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .addInputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .addOutputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .addOutputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_front_shielding_casing"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

}
