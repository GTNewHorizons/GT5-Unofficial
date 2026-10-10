package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingPipe implements PartRecipeGenerator {

    public static final ProcessingPipe INSTANCE = new ProcessingPipe();

    private ProcessingPipe() {}

    /*
     * This was constructed based on data observation: somehow the materials for the corresponding prefixes
     * have some manually defined recipes somewhere
     */
    private static long getPipeBits(OrePrefixes prefix, Material material) {
        String name = MaterialUtils.internalName(material);
        switch (prefix.getName()) {
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
            case "pipeRestrictiveHuge", "pipeRestrictiveLarge", "pipeRestrictiveMedium", "pipeRestrictiveSmall", "pipeRestrictiveTiny" -> {
                return 0;
            }
            case "pipeQuadruple", "pipeNonuple" -> {
                return GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED
                    | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
            }
            default -> {
                return 0;
            }
        }
    }

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (material == null) return;

        long bits = getPipeBits(prefix, material);

        switch (prefix.getName()) {
            case "pipeHuge" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, stack),
                        bits,
                        new Object[] { "DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.ingredient(material) });
                }
            }
            case "pipeLarge" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, stack),
                        bits,
                        new Object[] { "PHP", "P P", "PWP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'H',
                            MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                }
            }
            case "pipeMedium" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(2, stack),
                        bits,
                        new Object[] { "PPP", "W H", "PPP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'H',
                            MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                }
            }
            case "pipeSmall" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(6, stack),
                        bits,
                        new Object[] { "PWP", "P P", "PHP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'H',
                            MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                }
            }
            case "pipeTiny" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(8, stack),
                        bits,
                        new Object[] { "PPP", "h w", "PPP", 'P', OrePrefixes.plate.ingredient(material) });
                }
            }
            case "pipeRestrictiveHuge", "pipeRestrictiveLarge", "pipeRestrictiveMedium", "pipeRestrictiveSmall", "pipeRestrictiveTiny" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.getStack(
                            Materials.Steel,
                            Shapes.ring,
                            (int) (prefix.mSecondaryMaterial.mAmount / OrePrefixes.ring.getMaterialAmount())),
                        MaterialLibAPI.getStack(material, unrestricted(prefix), 1))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(
                        ((int) (prefix.mSecondaryMaterial.mAmount * 400L / OrePrefixes.ring.getMaterialAmount()))
                            * TICKS)
                    .eut(4)
                    .addTo(assemblerRecipes);
            }
            case "pipeQuadruple" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.findStack(material, TEBlockShapes.pipeQuadruple, 1),
                        bits,
                        new Object[] { "MM ", "MM ", "   ", 'M',
                            MaterialLibAPI.getStack(material, TEBlockShapes.pipeMedium, 1) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(material, TEBlockShapes.pipeMedium, 4))
                    .circuit(9)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.pipeQuadruple, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(assemblerRecipes);
            }
            case "pipeNonuple" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, stack),
                        bits,
                        new Object[] { "PPP", "PPP", "PPP", 'P',
                            MaterialLibAPI.getStack(material, TEBlockShapes.pipeSmall, 1) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(material, TEBlockShapes.pipeSmall, 9))
                    .circuit(9)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.pipeNonuple, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
            }
            default -> {}
        }
    }

    /// The plain item pipe a restrictive pipe prefix is assembled from.
    private static Shape unrestricted(OrePrefixes restrictive) {
        return switch (restrictive.getName()) {
            case "pipeRestrictiveTiny" -> TEBlockShapes.itemPipeTiny;
            case "pipeRestrictiveSmall" -> TEBlockShapes.itemPipeSmall;
            case "pipeRestrictiveMedium" -> TEBlockShapes.itemPipeMedium;
            case "pipeRestrictiveLarge" -> TEBlockShapes.itemPipeLarge;
            case "pipeRestrictiveHuge" -> TEBlockShapes.itemPipeHuge;
            default -> throw new IllegalArgumentException(restrictive.getName() + " is not a restrictive pipe prefix");
        };
    }
}
