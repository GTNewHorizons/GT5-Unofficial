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

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aPrefix.getName()) {
            case "pipeHuge" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L),
                        new Object[] { "DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.get(aMaterial) });
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Finished adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                }
            }
            case "pipeLarge" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTLoggers.GT_RECIPE_REMOVAL_LOGGER.fatal("Adding {} for material {}", aPrefix.getName(), aMaterial.getName());
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 2L),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 6L),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 8L),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1),
                        GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
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
                        GTUtility.copyAmount(1, aStack),
                        GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
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
