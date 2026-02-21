package gtPlusPlus.core.material;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayList;
import java.util.Set;

import gregtech.api.enums.GTValues;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
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
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenAlloySmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenAssembler;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenBlastSmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenDustGeneration;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenExtruder;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenFluids;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenFluorite;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenMaterialProcessing;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenMetalRecipe;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenOre;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenPlasma;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenPlates;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenRecycling;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenShapedCrafting;

public class MaterialGenerator {

    public static final ArrayList<Set<RunnableWithInfo<Material>>> mRecipeMapsToGenerate = new ArrayList<>();

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
            final String materialName = matInfo.getLocalizedName();
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
                        new BaseItemIngot(matInfo);

                        new BaseItemDust(matInfo);
                        new BaseItemNugget(matInfo);
                        new BaseItemPlate(matInfo);
                        new BaseItemRod(matInfo);
                        new BaseItemRodLong(matInfo);
                    } else {
                        new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                        new BlockBaseModular(matInfo, BlockTypes.FRAME);
                        new BaseItemIngot(matInfo);
                        if (hotIngot) {
                            new BaseItemIngotHot(matInfo);
                        }
                        new BaseItemDust(matInfo);
                        new BaseItemNugget(matInfo);
                        new BaseItemPlate(matInfo);
                        new BaseItemPlateDouble(matInfo);
                        new BaseItemBolt(matInfo);
                        new BaseItemRod(matInfo);
                        new BaseItemRodLong(matInfo);
                        new BaseItemRing(matInfo);
                        new BaseItemScrew(matInfo);
                        new BaseItemRotor(matInfo);
                        new BaseItemGear(matInfo);
                        new BaseItemPlateDense(matInfo);
                    }
                } else {
                    new BlockBaseModular(matInfo, BlockTypes.STANDARD);

                    new BaseItemIngot(matInfo);
                    new BaseItemDust(matInfo);
                    new BaseItemNugget(matInfo);
                    new BaseItemPlate(matInfo);
                    new BaseItemPlateDouble(matInfo);
                }
            } else if (matInfo.getState() == MaterialState.LIQUID) {
                if (generateEverything) {
                    new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                }
                new BaseItemIngot(matInfo);
                new BaseItemDust(matInfo);
                new BaseItemNugget(matInfo);
                new BaseItemPlate(matInfo);
                new BaseItemPlateDouble(matInfo);
            } else if (matInfo.getState() == MaterialState.GAS) {
                new BaseItemDust(matInfo);
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

        } catch (final Exception t) {
            Logger.MATERIALS(matInfo.getLocalizedName() + " failed to generate.");

        }
    }

    public static void generateDusts(final Material matInfo) {
        final String materialName = matInfo.getLocalizedName();

        if (matInfo.getState() == MaterialState.SOLID) {
            new BaseItemDust(matInfo);
        }

        // Add A jillion Recipes - old code
        try {
            RecipeGenDustGeneration.addMixerRecipe_Standalone(matInfo);
            new RecipeGenFluids(matInfo);
            new RecipeGenMaterialProcessing(matInfo);
        } catch (Exception t) {
            Logger.MATERIALS("Failed to generate some recipes for " + materialName);
            Logger.ERROR("Failed to generate some recipes for " + materialName);
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
        } else {
            Logger.INFO(
                "Nuclear Dehydrator: Did not generate recipe for " + matInfo.getLocalizedName()
                    + " | Null Fluid? "
                    + (matInfo.getFluid() == null)
                    + " | Null Dust? "
                    + (matInfo.getDust(0) == null));
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
                new BaseItemDust(matInfo);
            }
            if (generateIngot) {
                new BaseItemIngot(matInfo);
                new BaseItemNugget(matInfo);
            }

            if (generatePlates) {
                new BaseItemPlate(matInfo);
                new BaseItemPlateDouble(matInfo);
                new RecipeGenPlates(matInfo);
                new RecipeGenExtruder(matInfo);
                new RecipeGenAssembler(matInfo);
            }

            if (generateRods) {
                new BaseItemRod(matInfo);
                new BaseItemRodLong(matInfo);
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

        } catch (final Exception t) {
            Logger.MATERIALS(matInfo.getLocalizedName() + " failed to generate.");
        }
    }

    public static void generateOreMaterial(final Material matInfo) {
        generateOreMaterial(matInfo, true, true, true);
    }

    public static void generateOreMaterial(final Material matInfo, boolean generateOre, boolean generateDust,
        boolean generateSmallTinyDusts) {
        try {

            if (matInfo == null) {
                Logger.DEBUG_MATERIALS("Invalid Material while constructing null material.");
            }

            int sRadiation = 0;
            if (matInfo.vRadiationLevel > 0) {
                sRadiation = matInfo.vRadiationLevel;
            }

            if (generateOre) {
                new BlockBaseOre(matInfo, BlockTypes.ORE);
                matInfo.setHasOre();
            }

            DustState aState = new DustState(generateDust, generateSmallTinyDusts, generateSmallTinyDusts);

            if (!aState.generatesDust()) {
                if (aState.generatesSmallDust()) {
                    new BaseItemComponent(matInfo, ComponentTypes.DUSTSMALL);
                }
                if (aState.generatesTinyDust()) {
                    new BaseItemComponent(matInfo, ComponentTypes.DUSTTINY);
                }
            } else {
                new BaseItemDust(aState, matInfo);
            }

            new BaseItemCrushedOre(matInfo);
            new BaseItemCentrifugedCrushedOre(matInfo);
            new BaseItemPurifiedCrushedOre(matInfo);
            new BaseItemImpureDust(matInfo);
            new BaseItemPurifiedDust(matInfo);
            new BaseItemRawOre(matInfo);

            Logger.MATERIALS(
                "Generated all ore components for " + matInfo.getLocalizedName()
                    + ", now generating processing recipes.");

            if (matInfo == MaterialsFluorides.FLUORITE) {
                new RecipeGenFluorite(matInfo);
            } else {
                new RecipeGenOre(matInfo);
            }

        } catch (final Exception t) {
            Logger.MATERIALS(
                "[Error] " + (matInfo != null ? matInfo.getLocalizedName() : "Null Material") + " failed to generate.");
            t.printStackTrace();
        }
    }

    public static void generateOreMaterialWithAllExcessComponents(final Material matInfo) {
        try {
            if (matInfo == null) {
                Logger.DEBUG_MATERIALS("Invalid Material while constructing null material.");
                return;
            }

            new BlockBaseOre(matInfo, BlockTypes.ORE);
            matInfo.setHasOre();
            new BlockBaseModular(matInfo, BlockTypes.STANDARD);
            new BaseItemIngot(matInfo);
            new BaseItemDust(matInfo);
            new BaseItemNugget(matInfo);
            new BaseItemPlate(matInfo);
            new BaseItemPlateDouble(matInfo);

            new BaseItemCrushedOre(matInfo);
            new BaseItemCentrifugedCrushedOre(matInfo);
            new BaseItemPurifiedCrushedOre(matInfo);
            new BaseItemImpureDust(matInfo);
            new BaseItemPurifiedDust(matInfo);
            new BaseItemRawOre(matInfo);

            Logger.MATERIALS(
                "Generated all ore & base components for " + matInfo.getLocalizedName()
                    + ", now generating processing recipes.");

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
            Logger.MATERIALS(matInfo.getLocalizedName() + " failed to generate.");
            t.printStackTrace();
        }
    }
}
