package gtPlusPlus.core.material;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayList;
import java.util.Set;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.bolts.BaseItemBolt;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.dusts.BaseItemDust.DustState;
import gtPlusPlus.core.item.base.gears.BaseItemGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotHot;
import gtPlusPlus.core.item.base.nugget.BaseItemNugget;
import gtPlusPlus.core.item.base.ore.BaseItemCentrifugedCrushedOre;
import gtPlusPlus.core.item.base.ore.BaseItemCrushedOre;
import gtPlusPlus.core.item.base.ore.BaseItemImpureDust;
import gtPlusPlus.core.item.base.ore.BaseItemPurifiedCrushedOre;
import gtPlusPlus.core.item.base.ore.BaseItemPurifiedDust;
import gtPlusPlus.core.item.base.ore.BaseItemRawOre;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDense;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.base.rings.BaseItemRing;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.item.base.rotors.BaseItemRotor;
import gtPlusPlus.core.item.base.screws.BaseItemScrew;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenAlloySmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenAssembler;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenBlastSmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenDustGeneration;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenExtruder;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenFluids;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenMaterialProcessing;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenMetalRecipe;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenOre;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenPlasma;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenPlates;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenRecycling;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenShapedCrafting;

public class MaterialGenerator {

    public static final ArrayList<Set<Runnable>> mRecipeMapsToGenerate = new ArrayList<>();

    /// Whether `matInfo`'s `prefix` part has cut over to MaterialLib (see
    /// `MaterialReconstruction#isPartCutOver`) and legacy `Base*` construction for it should therefore be
    /// skipped. Third-party runtime-constructed materials (unknown to `MaterialReconstruction`) always keep
    /// the legacy path.
    private static boolean cutOver(Material matInfo, OrePrefixes prefix) {
        return MaterialReconstruction.isPartCutOver(matInfo.getUnlocalizedName(), prefix);
    }

    public static void generate(final Material matInfo) {
        generate(matInfo, true);
    }

    public static void generate(final Material matInfo, final boolean generateEverything) {
        generate(matInfo, generateEverything, true);
    }

    public static void generate(final Material matInfo, final boolean generateEverything,
        final boolean generateBlastSmelterRecipes) {
        try {
            final String unlocalizedName = matInfo.getUnlocalizedName();
            final String materialName = matInfo.getDefaultLocalName();
            final short[] C = matInfo.getRGBA();
            final boolean hotIngot = matInfo.requiresBlastFurnace();

            int sRadiation = 0;
            if (ItemUtils.getRadioactivityLevel(materialName) > 0 || (matInfo.vRadiationLevel != 0)) {
                sRadiation = matInfo.vRadiationLevel;
            }

            if (matInfo.getState() == MaterialState.SOLID) {
                if (generateEverything) {
                    if (sRadiation >= 1) {
                        new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                        if (!cutOver(matInfo, OrePrefixes.ingot)) new BaseItemIngot(matInfo);

                        if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.nugget)) new BaseItemNugget(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.plate)) new BaseItemPlate(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.stick)) new BaseItemRod(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.stickLong)) new BaseItemRodLong(matInfo);
                    } else {
                        new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                        new BlockBaseModular(matInfo, BlockTypes.FRAME);
                        if (!cutOver(matInfo, OrePrefixes.ingot)) new BaseItemIngot(matInfo);
                        if (hotIngot) {
                            if (!cutOver(matInfo, OrePrefixes.ingotHot)) new BaseItemIngotHot(matInfo);
                        }
                        if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.nugget)) new BaseItemNugget(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.plate)) new BaseItemPlate(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.plateDouble)) new BaseItemPlateDouble(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.bolt)) new BaseItemBolt(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.stick)) new BaseItemRod(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.stickLong)) new BaseItemRodLong(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.ring)) new BaseItemRing(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.screw)) new BaseItemScrew(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.rotor)) new BaseItemRotor(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.gearGt)) new BaseItemGear(matInfo);
                        if (!cutOver(matInfo, OrePrefixes.plateDense)) new BaseItemPlateDense(matInfo);
                    }
                } else {
                    new BlockBaseModular(matInfo, BlockTypes.STANDARD);

                    if (!cutOver(matInfo, OrePrefixes.ingot)) new BaseItemIngot(matInfo);
                    if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
                    if (!cutOver(matInfo, OrePrefixes.nugget)) new BaseItemNugget(matInfo);
                    if (!cutOver(matInfo, OrePrefixes.plate)) new BaseItemPlate(matInfo);
                    if (!cutOver(matInfo, OrePrefixes.plateDouble)) new BaseItemPlateDouble(matInfo);
                }
            } else if (matInfo.getState() == MaterialState.LIQUID) {
                if (generateEverything) {
                    new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                }
                if (!cutOver(matInfo, OrePrefixes.ingot)) new BaseItemIngot(matInfo);
                if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
                if (!cutOver(matInfo, OrePrefixes.nugget)) new BaseItemNugget(matInfo);
                if (!cutOver(matInfo, OrePrefixes.plate)) new BaseItemPlate(matInfo);
                if (!cutOver(matInfo, OrePrefixes.plateDouble)) new BaseItemPlateDouble(matInfo);
            } else if (matInfo.getState() == MaterialState.GAS) {
                if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
                FluidUtils.generateGas(unlocalizedName, materialName, matInfo.getMeltingPointK(), C, true);
            } else if (matInfo.getState() == MaterialState.PURE_GAS) {
                FluidUtils.generateGas(unlocalizedName, materialName, matInfo.getMeltingPointK(), C, true);
                return;
            } else if (matInfo.getState() == MaterialState.PURE_LIQUID) {
                FluidUtils.generateFluidNoPrefix(unlocalizedName, materialName, matInfo.getMeltingPointK(), C);
                return;
            }
            // Add A jillion Recipes - old code
            new RecipeGenAlloySmelter(matInfo);
            new RecipeGenAssembler(matInfo);
            if (generateBlastSmelterRecipes) {
                new RecipeGenBlastSmelter(matInfo);
            }
            new RecipeGenMetalRecipe(matInfo);
            new RecipeGenExtruder(matInfo);
            new RecipeGenFluids(matInfo);
            new RecipeGenPlates(matInfo);
            new RecipeGenShapedCrafting(matInfo);
            new RecipeGenMaterialProcessing(matInfo);

            new RecipeGenDustGeneration(matInfo);
            new RecipeGenRecycling(matInfo);
            new RecipeGenPlasma(matInfo);

        } catch (final Exception ignored) {}
    }

    public static void generateDusts(final Material matInfo) {
        if (matInfo.getState() == MaterialState.SOLID) {
            if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
        }

        // Add A jillion Recipes - old code
        try {
            RecipeGenDustGeneration.addMixerRecipe_Standalone(matInfo);
            new RecipeGenFluids(matInfo);
            new RecipeGenMaterialProcessing(matInfo);
        } catch (Exception t) {
            t.printStackTrace();
        }
        // RecipeGen_Recycling.generateRecipes(matInfo);
    }

    public static void generateNuclearDusts(final Material matInfo) {
        generateNuclearDusts(matInfo, true);
    }

    public static void generateNuclearDusts(final Material matInfo, boolean generateDehydratorRecipe) {
        generateNuclearMaterial(matInfo, false, true, false, false, true, true);
        if (generateDehydratorRecipe && matInfo.getFluid() != null && matInfo.getDust(0) != null) {
            GTValues.RA.stdBuilder()
                .circuit(20)
                .itemOutputs(matInfo.getDust(1))
                .fluidInputs(matInfo.getFluidStack(1 * INGOTS))
                .eut(matInfo.vVoltageMultiplier)
                .duration(10 * (matInfo.vVoltageMultiplier / 5))
                .addTo(chemicalDehydratorRecipes);
        }
    }

    public static void generateNuclearMaterial(final Material matInfo, final boolean generatePlates) {
        generateNuclearMaterial(matInfo, true, true, true, generatePlates, true, true);
    }

    public static void generateNuclearMaterial(final Material matInfo, final boolean generateBlock,
        final boolean generateDusts, final boolean generateIngot, final boolean generatePlates,
        final boolean generateRods, final boolean disableOptionalRecipes) {
        try {

            if (generateBlock) {
                new BlockBaseModular(matInfo, BlockTypes.STANDARD);
            }
            if (generateDusts) {
                if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
            }
            if (generateIngot) {
                if (!cutOver(matInfo, OrePrefixes.ingot)) new BaseItemIngot(matInfo);
                if (!cutOver(matInfo, OrePrefixes.nugget)) new BaseItemNugget(matInfo);
            }

            if (generatePlates) {
                if (!cutOver(matInfo, OrePrefixes.plate)) new BaseItemPlate(matInfo);
                if (!cutOver(matInfo, OrePrefixes.plateDouble)) new BaseItemPlateDouble(matInfo);
                new RecipeGenPlates(matInfo);
                new RecipeGenExtruder(matInfo);
                new RecipeGenAssembler(matInfo);
            }

            if (generateRods) {
                if (!cutOver(matInfo, OrePrefixes.stick)) new BaseItemRod(matInfo);
                if (!cutOver(matInfo, OrePrefixes.stickLong)) new BaseItemRodLong(matInfo);
            }

            if (!disableOptionalRecipes) {
                new RecipeGenShapedCrafting(matInfo);
                new RecipeGenMaterialProcessing(matInfo);
            }

            new RecipeGenRecycling(matInfo);
            new RecipeGenExtruder(matInfo);
            new RecipeGenFluids(matInfo);
            new RecipeGenMetalRecipe(matInfo);
            new RecipeGenDustGeneration(matInfo, disableOptionalRecipes);
            new RecipeGenPlasma(matInfo);

        } catch (final Throwable ignored) {}
    }

    public static void generateOreMaterial(final Material matInfo) {
        generateOreMaterial(matInfo, true, true, true);
    }

    public static void generateOreMaterial(final Material matInfo, boolean generateOre, boolean generateDust,
        boolean generateSmallTinyDusts) {
        try {

            if (generateOre) {
                new BlockBaseOre(matInfo, BlockTypes.ORE);
                matInfo.setHasOre();
            }

            DustState aState = new DustState(generateDust, generateSmallTinyDusts, generateSmallTinyDusts);

            if (!aState.generatesDust()) {
                if (aState.generatesSmallDust() && !cutOver(matInfo, OrePrefixes.dustSmall)) {
                    new BaseItemComponent(matInfo, ComponentTypes.DUSTSMALL);
                }
                if (aState.generatesTinyDust() && !cutOver(matInfo, OrePrefixes.dustTiny)) {
                    new BaseItemComponent(matInfo, ComponentTypes.DUSTTINY);
                }
            } else if (!cutOver(matInfo, OrePrefixes.dust)) {
                new BaseItemDust(aState, matInfo);
            }

            if (!cutOver(matInfo, OrePrefixes.crushed)) new BaseItemCrushedOre(matInfo);
            if (!cutOver(matInfo, OrePrefixes.crushedCentrifuged)) new BaseItemCentrifugedCrushedOre(matInfo);
            if (!cutOver(matInfo, OrePrefixes.crushedPurified)) new BaseItemPurifiedCrushedOre(matInfo);
            if (!cutOver(matInfo, OrePrefixes.dustImpure)) new BaseItemImpureDust(matInfo);
            if (!cutOver(matInfo, OrePrefixes.dustPure)) new BaseItemPurifiedDust(matInfo);
            if (!cutOver(matInfo, OrePrefixes.rawOre)) new BaseItemRawOre(matInfo);

            new RecipeGenOre(matInfo);

            // Fluorite Dehydrator
            if (matInfo.getLocalizedName()
                .equals("Fluorite (F)")) {
                GTValues.RA.stdBuilder()
                    .itemInputs(matInfo.getDust(37))
                    .itemOutputs(
                        MaterialLibAPI.getStack(Materials2Materials.Gypsum, Materials2Shapes.shapeDust, (int) (15)),
                        MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, (int) (1)),
                        MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, (int) (2)),
                        MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, (int) (1)),
                        MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, (int) (2)))
                    .outputChances(10000, 1000, 1000, 3000, 2000)
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.SulfuricAcid,
                            Materials2FluidShapes.shapeFluidLiquid,
                            (int) (8000)))
                    .fluidOutputs(Materials.HydrofluoricAcid.getFluid(16000))
                    .eut(TierEU.RECIPE_HV / 2)
                    .duration(10 * MINUTES)
                    .addTo(chemicalDehydratorRecipes);
            }

        } catch (final Exception t) {
            t.printStackTrace();
        }
    }

    public static void generateOreMaterialWithAllExcessComponents(final Material matInfo) {
        try {
            if (matInfo == null) {
                return;
            }

            new BlockBaseOre(matInfo, BlockTypes.ORE);
            matInfo.setHasOre();
            new BlockBaseModular(matInfo, BlockTypes.STANDARD);
            if (!cutOver(matInfo, OrePrefixes.ingot)) new BaseItemIngot(matInfo);
            if (!cutOver(matInfo, OrePrefixes.dust)) new BaseItemDust(matInfo);
            if (!cutOver(matInfo, OrePrefixes.nugget)) new BaseItemNugget(matInfo);
            if (!cutOver(matInfo, OrePrefixes.plate)) new BaseItemPlate(matInfo);
            if (!cutOver(matInfo, OrePrefixes.plateDouble)) new BaseItemPlateDouble(matInfo);

            if (!cutOver(matInfo, OrePrefixes.crushed)) new BaseItemCrushedOre(matInfo);
            if (!cutOver(matInfo, OrePrefixes.crushedCentrifuged)) new BaseItemCentrifugedCrushedOre(matInfo);
            if (!cutOver(matInfo, OrePrefixes.crushedPurified)) new BaseItemPurifiedCrushedOre(matInfo);
            if (!cutOver(matInfo, OrePrefixes.dustImpure)) new BaseItemImpureDust(matInfo);
            if (!cutOver(matInfo, OrePrefixes.dustPure)) new BaseItemPurifiedDust(matInfo);
            if (!cutOver(matInfo, OrePrefixes.rawOre)) new BaseItemRawOre(matInfo);

            new RecipeGenOre(matInfo, true);
            new RecipeGenAlloySmelter(matInfo);
            new RecipeGenAssembler(matInfo);
            new RecipeGenBlastSmelter(matInfo);
            new RecipeGenMetalRecipe(matInfo);
            new RecipeGenExtruder(matInfo);
            new RecipeGenFluids(matInfo);
            new RecipeGenPlates(matInfo);
            new RecipeGenShapedCrafting(matInfo);
            new RecipeGenMaterialProcessing(matInfo);
            new RecipeGenDustGeneration(matInfo);
            new RecipeGenRecycling(matInfo);
            new RecipeGenPlasma(matInfo);
        } catch (final Exception t) {
            t.printStackTrace();
        }
    }
}
