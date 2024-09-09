package bartworks.API.recipe;

import bartworks.API.modularUI.BWUITextures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.nei.formatter.FuelSpecialValueFormatter;

public class BartWorksRecipeMaps {

    public static final RecipeMap<RecipeMapBackend> bioLabRecipes = RecipeMapBuilder.of("bw.recipe.biolab")
        .maxIO(6, 2, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return BWUITextures.OVERLAY_SLOT_MODULE;
            }
            if (isFluid) {
                return GTUITextures.OVERLAY_SLOT_VIAL_2;
            }
            if (!isOutput) {
                switch (index) {
                    case 0:
                        return BWUITextures.OVERLAY_SLOT_DISH;
                    case 1:
                        return BWUITextures.OVERLAY_SLOT_DNA_FLASK;
                    case 2:
                        return GTUITextures.OVERLAY_SLOT_CIRCUIT;
                    case 3:
                        return GTUITextures.OVERLAY_SLOT_MOLECULAR_1;
                    case 4:
                        return GTUITextures.OVERLAY_SLOT_MOLECULAR_2;
                    case 5:
                        return GTUITextures.OVERLAY_SLOT_DATA_ORB;
                }
            }
            return null;
        })
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .logo(BWUITextures.PICTURE_BW_LOGO_47X21)
        .logoSize(47, 21)
        .logoPos(125, 3)
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<RecipeMapBackend> bacterialVatRecipes = RecipeMapBuilder.of("bw.recipe.BacteriaVat")
        .maxIO(6, 2, 1, 1)
        .minInputs(0, 1)
        .useSpecialSlot()
        .specialSlotSensitive()
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(BacterialVatFrontend::new)
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<FuelBackend> acidGenFuels = RecipeMapBuilder.of("bw.fuels.acidgens", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<RecipeMapBackend> circuitAssemblyLineRecipes = RecipeMapBuilder.of("bw.recipe.cal")
        .maxIO(6, 1, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .specialSlotSensitive()
        .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
        .build();
    public static final RecipeMap<RecipeMapBackend> radioHatchRecipes = RecipeMapBuilder.of("bw.recipe.radhatch")
        .maxIO(1, 0, 0, 0)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> BWUITextures.OVERLAY_SLOT_ROD)
        .logo(BWUITextures.PICTURE_BW_LOGO_47X21)
        .logoSize(47, 21)
        .logoPos(118, 55)
        .dontUseProgressBar()
        .addSpecialTexture(74, 20, 29, 27, BWUITextures.PICTURE_RADIATION)
        .frontend(RadioHatchFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> electricImplosionCompressorRecipes = RecipeMapBuilder
        .of("gt.recipe.electricimplosioncompressor")
        .maxIO(6, 2, 1, 1)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_IMPLOSION : null)
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .build();
    public static final RecipeMap<RecipeMapBackend> htgrFakeRecipes = RecipeMapBuilder.of("bw.recipe.htgr")
        .maxIO(1, 1, 0, 0)
        .build();
}
