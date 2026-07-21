package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
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
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        switch (prefix.getName()) {
            case "pipeHuge", "pipeLarge", "pipeMedium", "pipeSmall", "pipeTiny" -> {
                if (material.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeTiny, material, 8L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "h w", "PPP", 'P', OrePrefixes.plate.ingredient(material) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, material, 6L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PWP", "P P", "PHP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'H',
                            MU.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', MU.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, material, 2L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "W H", "PPP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'H',
                            MU.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', MU.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeLarge, material, 1L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PHP", "P P", "PWP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'H',
                            MU.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSoftMallet
                                : ToolDictNames.craftingToolHardHammer,
                            'W', MU.hasFlag(material, GTMaterialFlag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeHuge, material, 1L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.ingredient(material) });
                }
            }
            case "pipeRestrictiveHuge", "pipeRestrictiveLarge", "pipeRestrictiveMedium", "pipeRestrictiveSmall", "pipeRestrictiveTiny" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.getStack(
                            Materials2Materials.Steel,
                            Materials2Shapes.ring,
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
                if (material.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, material, 1),
                        GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
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
                if (material.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, stack),
                        GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
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
