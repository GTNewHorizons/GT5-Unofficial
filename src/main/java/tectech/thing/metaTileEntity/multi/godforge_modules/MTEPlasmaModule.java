package tectech.thing.metaTileEntity.multi.godforge_modules;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import tectech.recipe.TecTechRecipeMaps;
import tectech.util.CommonValues;

public class MTEPlasmaModule extends MTEBaseModule {

    private long EUt = 0;
    private int currentParallel = 0;
    private boolean debug = false;
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

    long wirelessEUt = 0;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                wirelessEUt = (long) recipe.mEUt * getMaxParallel();
                if (getUserEU(userUUID).compareTo(BigInteger.valueOf(wirelessEUt * recipe.mDuration)) < 0) {
                    return CheckRecipeResultRegistry.insufficientPower(wirelessEUt * recipe.mDuration);
                }
                if (recipe.mSpecialValue > getPlasmaTier()
                    || Objects.equals(recipe.mSpecialItems.toString(), "true") && !isMultiStepPlasmaCapable) {
                    return SimpleCheckRecipeResult.ofFailure("missing_upgrades");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                wirelessEUt = (long) recipe.mEUt * maxParallel;
                if (!addEUToGlobalEnergyMap(userUUID, -calculatedEut * duration)) {
                    return CheckRecipeResultRegistry.insufficientPower(wirelessEUt * recipe.mDuration);
                }
                addToPowerTally(
                    BigInteger.valueOf(calculatedEut)
                        .multiply(BigInteger.valueOf(duration)));
                addToRecipeTally(calculatedParallels);
                currentParallel = calculatedParallels;
                EUt = calculatedEut;
                setCalculatedEut(0);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(getProcessingVoltage())
                    .setDurationDecreasePerOC(getOverclockTimeFactor());
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(Integer.MAX_VALUE);
        logic.setAmperageOC(false);
        logic.setMaxParallel(getMaxParallel());
        logic.setSpeedBonus(getSpeedBonus());
        logic.setEuModifier(getEnergyDiscount());
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        if (debug) {
            builder.widget(createTestButton(builder))
                .widget(createTestButton2())
                .widget(createTestButton3());
        }
    }

    protected Widget createTestButton(IWidgetBuilder<?> builder) {
        return new ButtonWidget()
            .setOnClick((clickData, widget) -> isMultiStepPlasmaCapable = !isMultiStepPlasmaCapable)
            .setPlayClickSoundResource(
                () -> isAllowedToWork() ? SoundResource.GUI_BUTTON_UP.resourceLocation
                    : SoundResource.GUI_BUTTON_DOWN.resourceLocation)
            .setBackground(() -> {
                if (isMultiStepPlasmaCapable) {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                        GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON };
                } else {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                        GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF };
                }
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isAllowedToWork, val -> {
                if (val) enableWorking();
                else disableWorking();
            }), builder)
            .addTooltip("multi-step")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 100)
            .setSize(16, 16);
    }

    protected Widget createTestButton2() {
        return new TextFieldWidget().setSetterInt(this::setPlasmaTier)
            .setGetterInt(this::getPlasmaTier)
            .setNumbers(0, 2)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .setPos(3, 18)
            .addTooltip("fusion tier")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16)
            .setPos(174, 80)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);
    }

    protected Widget createTestButton3() {
        return new TextFieldWidget().setSetterInt(val -> inputMaxParallel = val)
            .setGetterInt(() -> inputMaxParallel)
            .setNumbers(0, Integer.MAX_VALUE)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .setPos(3, 18)
            .addTooltip("parallel")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(70, 16)
            .setPos(174, 60)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.godforgePlasmaRecipes;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>();
        str.add(
            "Progress: " + GREEN
                + formatNumbers(mProgresstime / 20)
                + RESET
                + " s / "
                + YELLOW
                + formatNumbers(mMaxProgresstime / 20)
                + RESET
                + " s");
        str.add("Currently using: " + RED + formatNumbers(EUt) + RESET + " EU/t");
        str.add(YELLOW + "Max Parallel: " + RESET + formatNumbers(getMaxParallel()));
        str.add(YELLOW + "Current Parallel: " + RESET + formatNumbers(currentParallel));
        str.add(YELLOW + "Recipe time multiplier: " + RESET + formatNumbers(getSpeedBonus()));
        str.add(YELLOW + "Energy multiplier: " + RESET + formatNumbers(getEnergyDiscount()));
        str.add(YELLOW + "Recipe time divisor per non-perfect OC: " + RESET + formatNumbers(getOverclockTimeFactor()));
        return str.toArray(new String[0]);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Plasma Fabricator")
            .addInfo("Controller block for the Heliothermal Plasma Fabricator, a module of the Godforge.")
            .addInfo("Must be part of a Godforge to function.")
            .addInfo("Used for extreme temperature matter ionization.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("The third module of the Godforge, this module infuses materials with extreme amounts")
            .addInfo("of heat, ionizing and turning them into plasma directly. Not all plasmas can be produced")
            .addInfo("right away, some of them require certain upgrades to be unlocked.")
            .addInfo("This module is specialized towards energy and overclock efficiency.")
            .addInfo(TOOLTIP_BAR)
            .beginStructureBlock(7, 7, 13, false)
            .addStructureInfo("The structure is too complex! See schematic for details.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "20"
                    + EnumChatFormatting.GRAY
                    + " Singularity Reinforced Stellar Shielding Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "20"
                    + EnumChatFormatting.GRAY
                    + " Boundless Gravitationally Severed Structure Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "5" + EnumChatFormatting.GRAY + " Harmonic Phonon Transmission Conduit")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "5" + EnumChatFormatting.GRAY + " Celestial Matter Guidance Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Stellar Energy Siphon Casing")
            .toolTipFinisher(CommonValues.GODFORGE_MARK);
        return tt;
    }

}
