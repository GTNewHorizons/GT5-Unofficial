package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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

                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 8L),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "h w", "PPP", 'P', OrePrefixes.plate.get(aMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 6L),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PWP", "P P", "PHP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 2L),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "W H", "PPP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PHP", "P P", "PWP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'H',
                            aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer
                                : ToolDictNames.craftingToolHardHammer,
                            'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw
                                : ToolDictNames.craftingToolWrench });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.get(aMaterial) });
                }
            }
            case pipeRestrictiveHuge, pipeRestrictiveLarge, pipeRestrictiveMedium, pipeRestrictiveSmall, pipeRestrictiveTiny -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(
                            OrePrefixes.ring,
                            Materials.Steel,
                            aPrefix.mSecondaryMaterial.mAmount / OrePrefixes.ring.mMaterialAmount),
                        GT_OreDictUnificator.get(aOreDictName.replaceFirst("Restrictive", ""), null, 1L, false, true))
                    .itemOutputs(GT_Utility.copyAmount(1L, aStack))
                    .duration(
                        ((int) (aPrefix.mSecondaryMaterial.mAmount * 400L / OrePrefixes.ring.mMaterialAmount)) * TICKS)
                    .eut(4)
                    .addTo(assemblerRecipes);
            }
            case pipeQuadruple -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1),
                        GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "MM ", "MM ", "   ", 'M',
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1) });
                }
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 4),
                        GT_Utility.getIntegratedCircuit(9))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, aMaterial, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(assemblerRecipes);
            }
            case pipeNonuple -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addCraftingRecipe(
                        GT_Utility.copyAmount(1, aStack),
                        GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPP", "PPP", "PPP", 'P', GT_OreDictUnificator
                            .get(aOreDictName.replaceFirst("Nonuple", "Small"), null, 1L, false, true) });
                }
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 9),
                        GT_Utility.getIntegratedCircuit(9))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, aMaterial, 1))
                    .duration(3 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 8))
                    .addTo(assemblerRecipes);
            }
            default -> {}
        }
    }
}
