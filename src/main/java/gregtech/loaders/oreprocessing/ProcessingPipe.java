package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

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
        switch (aPrefix) {
            case pipeHuge, pipeLarge, pipeMedium, pipeSmall, pipeTiny -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 8L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "h w", "PPP", 'P', OrePrefixes.plate.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 6L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PWP", "P P", "PHP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 2L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "W H", "PPP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PHP", "P P", "PWP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.get(aMaterial) });
                }
            }
            case pipeRestrictiveHuge, pipeRestrictiveLarge, pipeRestrictiveMedium, pipeRestrictiveSmall, pipeRestrictiveTiny -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(
                            OrePrefixes.ring,
                            Materials.Steel,
                            aPrefix.mSecondaryMaterial.mAmount / OrePrefixes.ring.mMaterialAmount),
                        GTOreDictUnificator.get(aOreDictName.replaceFirst("Restrictive", ""), null, 1L, false, true))
                    .itemOutputs(GTUtility.copyAmount(1, aStack))
                    .duration(
                        ((int) (aPrefix.mSecondaryMaterial.mAmount * 400L / OrePrefixes.ring.mMaterialAmount)) * TICKS)
                    .eut(4)
                    .addTo(assemblerRecipes);
            }
            case pipeQuadruple -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1),
                        GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "MM ", "MM ", "   ", 'M',
                            GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 4),
                        GTUtility.getIntegratedCircuit(9))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(assemblerRecipes);
            }
            case pipeNonuple -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, aStack),
                        GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "PPP", "PPP", 'P', GTOreDictUnificator
                            .get(aOreDictName.replaceFirst("Nonuple", "Small"), null, 1L, false, true) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 9),
                        GTUtility.getIntegratedCircuit(9))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeNonuple, aMaterial, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 8))
                    .addTo(assemblerRecipes);
            }
            default -> {}
        }
    }
}
