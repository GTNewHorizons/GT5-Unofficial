package gtnhlanth.api.recipe;

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
import gregtech.api.util.GTUtility;
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
        .maxIO(1, 1, 1, 1)
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
        .maxIO(1, 2, 0, 0)
        .amperage(1)
        .frontend(SourceChamberFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLY_LINE_1)
        .neiSpecialInfoFormatter((recipeInfo) -> {

            SourceChamberMetadata metadata = recipeInfo.recipe.getMetadata(SOURCE_CHAMBER_METADATA);
            if (metadata == null) return Collections.emptyList();

            float focus = metadata.focus;
            float maxEnergy = metadata.maxEnergy;

            int amount = metadata.rate;

            Particle particle = Particle.getParticleFromId(metadata.particleID);

            return Arrays.asList(

                // StatCollector.translateToLocal("beamline.particle") + ": " + particle.getLocalisedName(),

                StatCollector.translateToLocal("beamline.energy") + ": <="
                    + formatNumber(Math.min(maxEnergy, particle.maxSourceEnergy()))
                    + " keV",

                StatCollector.translateToLocal("beamline.focus") + ": " + formatNumber(focus),

                StatCollector.translateToLocal("beamline.rate") + ": " + formatNumber(amount)

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
        .maxIO(3, 4, 0, 0)
        .frontend(TargetChamberFrontend::new)
        .neiSpecialInfoFormatter(((recipeInfo) -> {

            TargetChamberMetadata metadata = recipeInfo.recipe.getMetadata(TARGET_CHAMBER_METADATA);
            if (metadata == null) return Collections.emptyList();

            float minEnergy = metadata.minEnergy;
            float maxEnergy = metadata.maxEnergy;

            float minFocus = metadata.minFocus;

            float amount = metadata.amount;

            Particle particle = Particle.getParticleFromId(metadata.particleID);

            return Arrays.asList(

                // StatCollector.translateToLocal("beamline.particle") + ": " + particle.getLocalisedName(),

                StatCollector.translateToLocal("beamline.energy") + ": "
                    + formatNumber(minEnergy * 1000)
                    + "-"
                    + formatNumber(maxEnergy * 1000)
                    + " eV", // Note the eV unit

                StatCollector.translateToLocal("beamline.focus") + ": >=" + formatNumber(minFocus),

                StatCollector.translateToLocal("beamline.amount") + ": " + formatNumber(amount)

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
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLY_LINE_1)
        .progressBarPos(108, 22)
        .neiTransferRect(100, 22, 28, 18)
        .build();
}
