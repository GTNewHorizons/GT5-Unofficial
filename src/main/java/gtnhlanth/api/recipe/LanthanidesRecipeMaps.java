package gtnhlanth.api.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.SourceChamberFrontend;
import gtnhlanth.common.tileentity.recipe.beamline.SourceChamberMetadata;
import gtnhlanth.common.tileentity.recipe.beamline.TargetChamberFrontend;
import gtnhlanth.common.tileentity.recipe.beamline.TargetChamberMetadata;

public class LanthanidesRecipeMaps {

    public static final RecipeMetadataKey<TargetChamberMetadata> TARGET_CHAMBER_METADATA = SimpleRecipeMetadataKey
        .create(TargetChamberMetadata.class, "target_chamber_metadata");
    public static final RecipeMetadataKey<SourceChamberMetadata> SOURCE_CHAMBER_METADATA = SimpleRecipeMetadataKey
        .create(SourceChamberMetadata.class, "source_chamber_metadata");

    public static final RecipeMap<RecipeMapBackend> digesterRecipes = RecipeMapBuilder.of("gtnhlanth.recipe.digester")
        .maxIO(3, 1, 1, 1)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<RecipeMapBackend> dissolutionTankRecipes = RecipeMapBuilder
        .of("gtnhlanth.recipe.disstank")
        .maxIO(2, 3, 2, 1)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.disstank"))
        .build();

    public static final RecipeMap<RecipeMapBackend> sourceChamberRecipes = RecipeMapBuilder.of("gtnhlanth.recipe.sc")
        .minInputs(0, 0)
        .maxIO(1, 2, 1, 0)
        .amperage(1)
        .neiHandlerInfo(builder -> builder.setHeight(178))
        .frontend(SourceChamberFrontend::new)
        .neiSpecialInfoFormatter((recipeInfo) -> {

            SourceChamberMetadata metadata = recipeInfo.recipe.getMetadata(SOURCE_CHAMBER_METADATA);
            if (metadata == null) return Collections.emptyList();

            Particle particle = Particle.getParticleFromId(metadata.particleID);

            return Arrays.asList(

                // StatCollector.translateToLocal("beamline.particle") + ": " + particle.getLocalisedName(),
                StatCollector.translateToLocal("beamline.focus") + ": " + formatNumber(metadata.focus),
                StatCollector.translateToLocal("beamline.rate") + ": " + formatNumber(metadata.rate),
                " ",
                StatCollector.translateToLocal("beamline.materialenergy") + ": "
                    + formatNumber(metadata.maxEnergy)
                    + " keV",
                StatCollector.translateToLocal("beamline.particleenergy") + ": "
                    + formatNumber(particle.maxSourceEnergy())
                    + " keV",
                StatCollector.translateToLocal("beamline.energyratio") + ": " + formatNumber(metadata.energyRatio)

        );
        })
        .neiItemOutputsGetter(recipe -> {
            SourceChamberMetadata metadata = recipe.getMetadata(SOURCE_CHAMBER_METADATA);
            if (metadata == null) return GTValues.emptyItemStackArray;
            ItemStack particleStack = new ItemStack(LanthItemList.PARTICLE_ITEM, 1, metadata.particleID);
            List<ItemStack> ret = new ArrayList<>(Arrays.asList(recipe.mOutputs));
            ret.add(particleStack);
            return ret.toArray(new ItemStack[0]);
        })
        .build();

    public static final RecipeMap<RecipeMapBackend> targetChamberRecipes = RecipeMapBuilder.of("gtnhlanth.recipe.tc")
        .minInputs(0, 0)
        .maxIO(3, 1, 0, 0)
        .frontend(TargetChamberFrontend::new)
        .neiSpecialInfoFormatter(((recipeInfo) -> {

            TargetChamberMetadata metadata = recipeInfo.recipe.getMetadata(TARGET_CHAMBER_METADATA);
            if (metadata == null) return Collections.emptyList();

            return Arrays.asList(

                // StatCollector.translateToLocal("beamline.particle") + ": " + particle.getLocalisedName(),
                StatCollector.translateToLocal("beamline.energy") + ": "
                    + formatNumber(metadata.minEnergy * 1000)
                    + "-"
                    + formatNumber(metadata.maxEnergy * 1000)
                    + " eV", // Note the eV unit
                StatCollector.translateToLocal("beamline.focus") + ": >=" + formatNumber(metadata.minFocus),
                StatCollector.translateToLocal("beamline.amount") + ": " + formatNumber(metadata.amount)

        );
        }))
        .neiItemInputsGetter(recipe -> {
            TargetChamberMetadata metadata = recipe.getMetadata(TARGET_CHAMBER_METADATA);
            if (metadata == null) return GTValues.emptyItemStackArray;
            ItemStack particleStack = new ItemStack(LanthItemList.PARTICLE_ITEM, 1, metadata.particleID);
            List<ItemStack> ret = new ArrayList<>();
            ret.add(particleStack);
            ret.addAll(Arrays.asList(recipe.mInputs));
            return ret.toArray(new ItemStack[0]);
        })
        .neiHandlerInfo(builder -> builder.setHeight(140))

        .build();
}
