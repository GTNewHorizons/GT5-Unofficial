package com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_EM_SmeltingModule extends GT_MetaTileEntity_EM_BaseModule {

    private long EUt = 0;
    private long currentParallel = 0;
    private boolean furnaceMode = false;

    public GT_MetaTileEntity_EM_SmeltingModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_SmeltingModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_SmeltingModule(mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.blastFurnaceRecipes, RecipeMaps.furnaceRecipes);
    }

    private static RecipeMap<?> getRecipeMap(boolean furnaceMode) {
        if (furnaceMode) {
            return RecipeMaps.furnaceRecipes;
        } else {
            return RecipeMaps.blastFurnaceRecipes;
        }
    }

    long wirelessEUt = 0;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected Stream<GT_Recipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                RecipeMap<?> recipes = getRecipeMap(furnaceMode);
                return super.findRecipeMatches(recipes);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {

                if (recipe.mSpecialValue > getHeat()) {
                    return CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
                }

                wirelessEUt = (long) recipe.mEUt * getMaxParallel();
                if (getUserEU(userUUID).compareTo(BigInteger.valueOf(wirelessEUt * recipe.mDuration)) < 0) {
                    return CheckRecipeResultRegistry.insufficientPower(wirelessEUt * recipe.mDuration);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GT_Recipe recipe) {
                if (!addEUToGlobalEnergyMap(userUUID, -calculatedEut * duration)) {
                    return CheckRecipeResultRegistry.insufficientPower(calculatedEut * duration);
                }
                currentParallel = calculatedParallels;
                EUt = calculatedEut;
                setCalculatedEut(0);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(TierEU.MAX).setRecipeHeat(recipe.mSpecialValue)
                        .setHeatOC(true).setHeatDiscount(true).setMachineHeat(getHeat());
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
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(texturePage << 7),
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                                    : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(texturePage << 7) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(furnaceSwitch(builder));

    }

    protected ButtonWidget furnaceSwitch(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> furnaceMode = !furnaceMode)
                .setPlayClickSound(isFurnaceModeOn()).setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    if (isFurnaceModeOn()) {
                        ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                        ret.add(GT_UITextures.OVERLAY_BUTTON_CHECKMARK);
                    } else {
                        ret.add(GT_UITextures.BUTTON_STANDARD);
                        ret.add(GT_UITextures.OVERLAY_BUTTON_CROSS);

                    }
                    return ret.toArray(new IDrawable[0]);
                }).attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isFurnaceModeOn, this::setFurnaceMode), builder)
                .addTooltip(translateToLocal("fog.button.furnacemode.tooltip")).setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(174, 91).setSize(16, 16);
        return (ButtonWidget) button;
    }

    private boolean isFurnaceModeOn() {
        return furnaceMode;
    }

    private void setFurnaceMode(boolean enabled) {
        furnaceMode = enabled;
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
                "Progress: " + GREEN
                        + GT_Utility.formatNumbers(mProgresstime / 20)
                        + RESET
                        + " s / "
                        + YELLOW
                        + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                        + RESET
                        + " s");
        str.add("Currently using: " + RED + formatNumbers(EUt) + RESET + " EU/t");
        str.add(YELLOW + "Max Parallel: " + RESET + formatNumbers(getMaxParallel()));
        str.add(YELLOW + "Current Parallel: " + RESET + formatNumbers(currentParallel));
        str.add(YELLOW + "Heat Capacity: " + RESET + formatNumbers(getHeat()));
        str.add(YELLOW + "Recipe time multiplier: " + RESET + formatNumbers(getSpeedBonus()));
        return str.toArray(new String[0]);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Smelting Module") // Machine Type:
                .addInfo("Controller block of the Smelting Module") // Controller
                .addInfo("Uses a Star to Smelt Metals").addSeparator().beginStructureBlock(1, 4, 2, false)
                .addEnergyHatch("Any Infinite Spacetime Casing", 1) // Energy Hatch: Any
                .addMaintenanceHatch("Any Infinite Spacetime Casing", 1) // Maintenance
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

}
