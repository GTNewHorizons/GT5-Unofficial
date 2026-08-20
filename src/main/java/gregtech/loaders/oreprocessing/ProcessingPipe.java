package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingPipe implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingPipe INSTANCE;

    public ProcessingPipe() {
        INSTANCE = this;
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
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        long bits = getPipeBits(prefix, material);

        switch (prefix.getName()) {
            case "pipeHuge" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeHuge, material, 1L),
                        bits,
                        new Object[] { "DhD", "D D", "DwD", 'D',
                            MaterialParts.craftIngredient(OrePrefixes.plateDouble, material) });
                }
            }
            case "pipeLarge" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeLarge, material, 1L),
                        bits,
                        new Object[] { "PHP", "P P", "PWP", 'P',
                            material == Materials.Wood ? MaterialParts.craftIngredient(OrePrefixes.plank, material)
                                : MaterialParts.craftIngredient(OrePrefixes.plate, material),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, material, 2L),
                        bits,
                        new Object[] { "PPP", "W H", "PPP", 'P',
                            material == Materials.Wood ? MaterialParts.craftIngredient(OrePrefixes.plank, material)
                                : MaterialParts.craftIngredient(OrePrefixes.plate, material),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, material, 6L),
                        bits,
                        new Object[] { "PWP", "P P", "PHP", 'P',
                            material == Materials.Wood ? MaterialParts.craftIngredient(OrePrefixes.plank, material)
                                : MaterialParts.craftIngredient(OrePrefixes.plate, material),
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
                        GTOreDictUnificator.get(OrePrefixes.pipeTiny, material, 8L),
                        bits,
                        new Object[] { "PPP", "h w", "PPP", 'P',
                            MaterialParts.craftIngredient(OrePrefixes.plate, material) });
                }
            }
            case "pipeRestrictiveHuge", "pipeRestrictiveLarge", "pipeRestrictiveMedium", "pipeRestrictiveSmall", "pipeRestrictiveTiny" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.getStack(
                            Materials.Steel,
                            Shapes.ring,
                            (int) (prefix.mSecondaryMaterial.mAmount / OrePrefixes.ring.getMaterialAmount())),
                        GTOreDictUnificator.get(oreDictName.replaceFirst("Restrictive", ""), null, 1L, false, true))
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
                        GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, material, 1),
                        bits,
                        new Object[] { "MM ", "MM ", "   ", 'M',
                            GTOreDictUnificator.get(OrePrefixes.pipeMedium, material, 1) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, material, 4))
                    .circuit(9)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, material, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(assemblerRecipes);
            }
            case "pipeNonuple" -> {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, stack),
                        bits,
                        new Object[] { "PPP", "PPP", "PPP", 'P', GTOreDictUnificator
                            .get(oreDictName.replaceFirst("Nonuple", "Small"), null, 1L, false, true) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, material, 9))
                    .circuit(9)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeNonuple, material, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
            }
            default -> {}
        }
    }
}
