package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import gregtech.GTLoggers;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingPipe implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingPipe() {
        OrePrefixes.pipeHuge.add(this);
        OrePrefixes.pipeLarge.add(this);
        OrePrefixes.pipeMedium.add(this);
        OrePrefixes.pipeSmall.add(this);
        OrePrefixes.pipeTiny.add(this);
        OrePrefixes.pipeRestrictiveHuge.add(this);
        OrePrefixes.pipeRestrictiveLarge.add(this);
        OrePrefixes.pipeRestrictiveMedium.add(this);
        OrePrefixes.pipeRestrictiveSmall.add(this);
        OrePrefixes.pipeRestrictiveTiny.add(this);
        OrePrefixes.pipeQuadruple.add(this);
        OrePrefixes.pipeNonuple.add(this);
    }

    /*
     This was constructed based on data observation: somehow the materials for the corresponding prefixes
     have some manually defined recipes somewhere
    */
    private static long getPipeBits(OrePrefixes orePrefixes, Materials materials){
        String name = materials.getName();
        switch (orePrefixes.getName()){
            case "pipeHuge" -> {
                if ("Incoloy-903".equals(name) || "NetherStar".equals(name)) return GTModHandler.RecipeBits.BUFFERED;
                return GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
            }
            case "pipeLarge", "pipeMedium", "pipeSmall" -> {
                if ("Ultimate".equals(name)) return GTModHandler.RecipeBits.BUFFERED;
                return GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
            }
            case "pipeTiny" -> {
                return GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
            }
            case "pipeRestrictiveHuge", "pipeRestrictiveLarge", "pipeRestrictiveMedium", "pipeRestrictiveSmall", "pipeRestrictiveTiny"->{
                return 0;
            }
            case "pipeQuadruple", "pipeNonuple" -> {
                return GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
            }
            default -> {
                return 0;
            }
        }
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        long bits = getPipeBits(aPrefix, aMaterial);

        switch (aPrefix.getName()) {
            case "pipeHuge" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L), bits,
                        new Object[] { "DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.get(aMaterial) });
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Finished adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                }
            }
            case "pipeLarge" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L), bits,
                        new Object[] { "PHP", "P P", "PWP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Finished adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                }
            }
            case "pipeMedium" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 2L), bits,
                        new Object[] { "PPP", "W H", "PPP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Finished adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                }
            }
            case "pipeSmall" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 6L), bits,
                        new Object[] { "PWP", "P P", "PHP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Finished adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                }
            }
            case "pipeTiny" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 8L), bits,
                        new Object[] { "PPP", "h w", "PPP", 'P', OrePrefixes.plate.get(aMaterial) });
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Finished adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                }
            }
            case "pipeRestrictiveHuge", "pipeRestrictiveLarge", "pipeRestrictiveMedium", "pipeRestrictiveSmall", "pipeRestrictiveTiny" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(
                            OrePrefixes.ring,
                            Materials.Steel,
                            aPrefix.mSecondaryMaterial.mAmount / OrePrefixes.ring.getMaterialAmount()),
                        GTOreDictUnificator.get(aOreDictName.replaceFirst("Restrictive", ""), null, 1L, false, true))
                    .itemOutputs(GTUtility.copyAmount(1, aStack))
                    .duration(
                        ((int) (aPrefix.mSecondaryMaterial.mAmount * 400L / OrePrefixes.ring.getMaterialAmount()))
                            * TICKS)
                    .eut(4)
                    .addTo(assemblerRecipes);
            }
            case "pipeQuadruple" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1), bits,
                        new Object[] { "MM ", "MM ", "   ", 'M',
                            GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 4))
                    .circuit(9)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(assemblerRecipes);
            }
            case "pipeNonuple" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, aStack), bits,
                        new Object[] { "PPP", "PPP", "PPP", 'P', GTOreDictUnificator
                            .get(aOreDictName.replaceFirst("Nonuple", "Small"), null, 1L, false, true) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 9))
                    .circuit(9)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeNonuple, aMaterial, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 8))
                    .addTo(assemblerRecipes);
            }
            default -> {}
        }
    }
}
