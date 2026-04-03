package tectech.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import tectech.thing.CustomItemList;
import tectech.thing.gui.TecTechUITextures;

public class TecTechRecipeMaps {

    public static class TTResearchStationALRecipe extends GTRecipe.RecipeAssemblyLine {

        public long mComputation;
        public long mAmperage;
        public long mComputationRequiredPerSec;

        public TTResearchStationALRecipe(ItemStack aResearchItem, int aResearchTime, int aResearchVoltage,
            ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt,
            long aComputation, int aAmperage, int aComputationRequiredPerSec) {
            super(aResearchItem, aResearchTime, aResearchVoltage, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
            this.mComputation = aComputation;
            this.mAmperage = aAmperage;
            this.mComputationRequiredPerSec = aComputationRequiredPerSec;
        }

        public TTResearchStationALRecipe(ItemStack aResearchItem, int aResearchTime, int aResearchVoltage,
            ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt,
            ItemStack[][] aAlt, long aComputation, int aAmperage, int aComputationRequiredPerSec) {
            super(
                aResearchItem,
                aResearchTime,
                aResearchVoltage,
                aInputs,
                aFluidInputs,
                aOutput,
                aDuration,
                aEUt,
                aAlt);
            this.mComputation = aComputation;
            this.mAmperage = aAmperage;
            this.mComputationRequiredPerSec = aComputationRequiredPerSec;
        }
    }

    public static void init() {}

    public static final List<TTResearchStationALRecipe> researchableALRecipeList = new ArrayList<>();

    public static final RecipeMap<RecipeMapBackend> eyeOfHarmonyRecipes = RecipeMapBuilder.of("gt.recipe.eyeofharmony")
        .maxIO(
            EyeOfHarmonyFrontend.maxItemInputs,
            EyeOfHarmonyFrontend.maxItemOutputs,
            EyeOfHarmonyFrontend.maxFluidInputs,
            EyeOfHarmonyFrontend.maxFluidOutputs)
        .minInputs(1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
        .progressBarPos(78, 24 + 2)
        .logoPos(10, 10)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(CustomItemList.Machine_Multi_EyeOfHarmony.get(1))
                .setHeight(314)
                .setMaxRecipesPerPage(1))
        .frontend(EyeOfHarmonyFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> researchStationFakeRecipes = RecipeMapBuilder
        .of("gt.recipe.researchStation")
        .maxIO(1, 1, 0, 0)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GTUITextures.OVERLAY_SLOT_DATA_ORB;
            }
            if (isOutput) {
                return TecTechUITextures.OVERLAY_SLOT_MESH;
            }
            return GTUITextures.OVERLAY_SLOT_MICROSCOPE;
        })
        .addSpecialTexture(19, 12, 84, 60, TecTechUITextures.PICTURE_HEAT_SINK)
        .addSpecialTexture(41, 22, 40, 40, TecTechUITextures.PICTURE_RACK_LARGE)
        .logo(TecTechUITextures.PICTURE_TECTECH_LOGO)
        .logoSize(18, 18)
        .logoPos(151, 63)
        .neiTransferRect(81, 33, 25, 18)
        .neiTransferRect(124, 33, 18, 29)
        .frontend(ResearchStationFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(CustomItemList.Machine_Multi_Research.get(1)))
        .build();

    public static final RecipeMap<RecipeMapBackend> godforgePlasmaRecipes = RecipeMapBuilder.of("gt.recipe.fog_plasma")
        .maxIO(1, 1, 1, 1)
        .progressBar(TecTechUITextures.PROGRESSBAR_GODFORGE_PLASMA, ProgressBar.Direction.RIGHT)
        .progressBarPos(78, 33)
        .neiTransferRect(78, 33, 20, 20)
        .frontend(GodforgePlasmaFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> godforgeExoticMatterRecipes = RecipeMapBuilder
        .of("gt.recipe.fog_exotic")
        .maxIO(1, 1, 2, 1)
        .progressBar(TecTechUITextures.PROGRESSBAR_GODFORGE_PLASMA, ProgressBar.Direction.RIGHT)
        .progressBarPos(78, 33)
        .neiTransferRect(78, 33, 20, 20)
        .frontend(GodforgeExoticFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> godforgeMoltenRecipes = RecipeMapBuilder.of("gt.recipe.fog_molten")
        .maxIO(6, 6, 1, 2)
        .minInputs(1, 0)
        .progressBar(TecTechUITextures.PROGRESSBAR_GODFORGE_PLASMA, ProgressBar.Direction.RIGHT)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .logo(TecTechUITextures.PICTURE_GODFORGE_LOGO)
        .logoSize(18, 18)
        .logoPos(151, 63)
        .build();

    public static final RecipeMap<RecipeMapBackend> godforgeFakeUpgradeCostRecipes = RecipeMapBuilder
        .of("gt.recipe.upgrade_costs")
        .maxIO(12, 2, 0, 2)
        .addSpecialTexture(83, 38, 30, 13, GTUITextures.PICTURE_ARROW_GRAY)
        .dontUseProgressBar()
        .neiTransferRect(83, 38, 30, 13)
        .frontend(GodforgeUpgradeCostFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(CustomItemList.Machine_Multi_ForgeOfGods.get(1))
                .setHeight(100))
        .build();

}
