package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import appeng.api.config.TunnelType;
import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final Materials[] dielectrics = { Materials.PolyvinylChloride, Materials.Polydimethylsiloxane };
    private final Materials[] syntheticRubbers = { Materials.StyreneButadieneRubber, Materials.Silicone };

    private static Object tt;

    public ProcessingWire() {
        OrePrefixes.wireGt01.add(this);
        OrePrefixes.wireGt02.add(this);
        OrePrefixes.wireGt04.add(this);
        OrePrefixes.wireGt08.add(this);
        OrePrefixes.wireGt12.add(this);
        OrePrefixes.wireGt16.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (tt == TunnelType.ME) {
            try {
                tt = TunnelType.valueOf("GT_POWER");
            } catch (IllegalArgumentException ignored) {
                tt = TunnelType.IC2_POWER;
            }
        }

        int cableWidth;
        OrePrefixes correspondingCable;

        switch (aPrefix) {
            case wireGt01 -> {
                cableWidth = 1;
                correspondingCable = OrePrefixes.cableGt01;
                if (!aMaterial.contains(SubTag.NO_SMASHING)) {
                    // Bender recipes
                    {
                        if (GTOreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 2L))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8))
                                .addTo(benderRecipes);
                        }
                    }

                    // Wiremill Recipes
                    {
                        if (GTOreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 4L))
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8))
                                .addTo(wiremillRecipes);
                        }
                    }
                }

                // crafting recipe
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)
                    && (aMaterial.getProcessingMaterialTierEU() < TierEU.IV)) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L),
                        GTProxy.tBits,
                        new Object[] { "Xx", 'X', OrePrefixes.plate.get(aMaterial) });

                }

                // Assembler recipes
                {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, aStack), GTUtility.getIntegratedCircuit(2))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L))
                        .duration(7 * SECONDS + 10 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(assemblerRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(4, aStack), GTUtility.getIntegratedCircuit(4))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(assemblerRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(8, aStack), GTUtility.getIntegratedCircuit(8))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L))
                        .duration(15 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(assemblerRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(12, aStack), GTUtility.getIntegratedCircuit(12))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L))
                        .duration(20 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(assemblerRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(16, aStack), GTUtility.getIntegratedCircuit(16))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L))
                        .duration(25 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(assemblerRecipes);
                }
            }
            case wireGt02 -> {
                cableWidth = 2;
                correspondingCable = OrePrefixes.cableGt02;
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    // Shapeless crafting recipes
                    {
                        GTModHandler.addShapelessCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 2L),
                            new Object[] { aOreDictName });

                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial) });
                    }
                }
            }
            case wireGt04 -> {
                cableWidth = 4;
                correspondingCable = OrePrefixes.cableGt04;
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    // Shapeless crafting recipes
                    {
                        GTModHandler.addShapelessCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 4L),
                            new Object[] { aOreDictName });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial) });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt02.get(aMaterial), OrePrefixes.wireGt02.get(aMaterial) });
                    }
                }
            }
            case wireGt08 -> {
                cableWidth = 8;
                correspondingCable = OrePrefixes.cableGt08;
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    // Shapeless crafting recipes
                    {
                        GTModHandler.addShapelessCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 8L),
                            new Object[] { aOreDictName });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial) });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt04.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial) });
                    }
                }
            }
            case wireGt12 -> {
                cableWidth = 12;
                correspondingCable = OrePrefixes.cableGt12;
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    // Shapeless crafting recipes
                    {
                        GTModHandler.addShapelessCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 12L),
                            new Object[] { aOreDictName });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt08.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial) });
                    }
                }
            }
            case wireGt16 -> {
                cableWidth = 16;
                correspondingCable = OrePrefixes.cableGt16;
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    // Shapeless crafting recipes
                    {
                        GTModHandler.addShapelessCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 16L),
                            new Object[] { aOreDictName });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt08.get(aMaterial), OrePrefixes.wireGt08.get(aMaterial) });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new Object[] { OrePrefixes.wireGt12.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial) });
                    }

                    AE2addNewAttunement(aStack);
                }
            }
            default -> {
                GTLog.err.println(
                    "OrePrefix " + aPrefix.name() + " cannot be registered as a cable for Material " + aMaterial.mName);
                return;
            }
        }

        int costMultiplier = cableWidth / 4 + 1;

        switch (aMaterial.mName) {
            case "RedAlloy", "Cobalt", "Lead", "Tin", "Zinc", "SolderingAlloy" -> {
                ArrayList<Object> craftingListRubber = new ArrayList<>();
                craftingListRubber.add(aOreDictName);
                for (int i = 0; i < costMultiplier; i++) {
                    craftingListRubber.add(OrePrefixes.plate.get(Materials.Rubber));
                }

                // shapeless crafting
                if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) != null) {
                    GTModHandler.addShapelessCraftingRecipe(
                        GTOreDictUnificator.get(correspondingCable, aMaterial, 1L),
                        craftingListRubber.toArray());
                }

                // Packer recipe
                if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTUtility.copyAmount(1, aStack),
                            GTOreDictUnificator.get(OrePrefixes.plate.get(Materials.Rubber), costMultiplier))
                        .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                        .duration(5 * SECONDS)
                        .eut(8)
                        .addTo(packagerRecipes);
                }
                // alloy smelter recipes
                {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L),
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cableGt01, aMaterial, 1L))
                        .duration(5 * SECONDS)
                        .eut(8)
                        .addTo(alloySmelterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L),
                            GTOreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cableGt02, aMaterial, 1L))
                        .duration(10 * SECONDS)
                        .eut(16)
                        .addTo(alloySmelterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 4L),
                            GTOreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cableGt04, aMaterial, 1L))
                        .duration(15 * SECONDS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(alloySmelterRecipes);
                }
                // Assembler recipes
                {
                    if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(aStack, GTUtility.getIntegratedCircuit(24))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                            .fluidInputs(Materials.Rubber.getMolten(144L * costMultiplier))
                            .duration(5 * SECONDS)
                            .eut(8)
                            .addTo(assemblerRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(aStack, GTUtility.getIntegratedCircuit(24))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                            .fluidInputs(Materials.StyreneButadieneRubber.getMolten(108L * costMultiplier))
                            .duration(5 * SECONDS)
                            .eut(8)
                            .addTo(assemblerRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(aStack, GTUtility.getIntegratedCircuit(24))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                            .fluidInputs(Materials.Silicone.getMolten(72L * costMultiplier))
                            .duration(5 * SECONDS)
                            .eut(8)
                            .addTo(assemblerRecipes);

                        for (Materials dielectric : dielectrics) {
                            for (Materials syntheticRubber : syntheticRubbers) {

                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(4, aStack), dielectric.getDust(costMultiplier))
                                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 4L))
                                    .fluidInputs(syntheticRubber.getMolten(costMultiplier * 144L))
                                    .duration(20 * SECONDS)
                                    .eut(8)
                                    .addTo(assemblerRecipes);

                                GTValues.RA.stdBuilder()
                                    .itemInputs(aStack, dielectric.getDustSmall(costMultiplier))
                                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                                    .fluidInputs(syntheticRubber.getMolten(costMultiplier * 36L))
                                    .duration(5 * SECONDS)
                                    .eut(8)
                                    .addTo(assemblerRecipes);
                            }
                        }
                    }
                }
            }
            case "Iron", "Nickel", "Cupronickel", "Copper", "AnnealedCopper", "Kanthal", "Gold", "Electrum", "Silver", "Nichrome", "Steel", "BlackSteel", "Titanium", "Aluminium", "BlueAlloy", "NetherStar", "RedstoneAlloy" -> {

                if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) == null) {
                    break;
                }
                // Assembler recipes
                GTValues.RA.stdBuilder()
                    .itemInputs(aStack, GTUtility.getIntegratedCircuit(24))
                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                    .fluidInputs(Materials.Rubber.getMolten(144 * costMultiplier))
                    .duration(5 * SECONDS)
                    .eut(8)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(aStack, GTUtility.getIntegratedCircuit(24))
                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                    .fluidInputs(Materials.StyreneButadieneRubber.getMolten(108 * costMultiplier))
                    .duration(5 * SECONDS)
                    .eut(8)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(aStack, GTUtility.getIntegratedCircuit(24))
                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                    .fluidInputs(Materials.Silicone.getMolten(72 * costMultiplier))
                    .duration(5 * SECONDS)
                    .eut(8)
                    .addTo(assemblerRecipes);

                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {

                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, aStack), dielectric.getDust(costMultiplier))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 4L))
                            .fluidInputs(syntheticRubber.getMolten(costMultiplier * 144L))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(assemblerRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(aStack, dielectric.getDustSmall(costMultiplier))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                            .fluidInputs(syntheticRubber.getMolten(costMultiplier * 36L))
                            .duration(5 * SECONDS)
                            .eut(8)
                            .addTo(assemblerRecipes);
                    }
                }
            }

            default -> {
                if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) == null) {
                    break;
                }

                // Assembler recipes
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        aStack,
                        GTOreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier),
                        GTUtility.getIntegratedCircuit(24))
                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                    .fluidInputs(Materials.Silicone.getMolten(costMultiplier * 72))
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 8))
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        aStack,
                        GTOreDictUnificator.get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier),
                        GTUtility.getIntegratedCircuit(24))
                    .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                    .fluidInputs(Materials.Silicone.getMolten(costMultiplier * 72))
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 8))
                    .addTo(assemblerRecipes);

                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, aStack),
                                dielectric.getDust(costMultiplier),
                                GTOreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier * 4L))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 4L))
                            .fluidInputs(syntheticRubber.getMolten(costMultiplier * 144L))
                            .duration(20 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(assemblerRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, aStack),
                                dielectric.getDust(costMultiplier),
                                GTOreDictUnificator
                                    .get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier * 4L))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 4L))
                            .fluidInputs(syntheticRubber.getMolten(costMultiplier * 144L))
                            .duration(20 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(assemblerRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                aStack,
                                dielectric.getDustSmall(costMultiplier),
                                GTOreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                            .fluidInputs(syntheticRubber.getMolten(costMultiplier * 36L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(assemblerRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                aStack,
                                dielectric.getDustSmall(costMultiplier),
                                GTOreDictUnificator
                                    .get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier))
                            .itemOutputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                            .fluidInputs(syntheticRubber.getMolten(costMultiplier * 36L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(assemblerRecipes);
                    }
                }
            }
        }

        if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L))
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 8))
                .addTo(unpackagerRecipes);
        }

        if (GTOreDictUnificator.get(correspondingCable, aMaterial, 1L) != null) {
            AE2AddNetAttunementCable(aStack, correspondingCable, aMaterial);
        }
    }

    // region AE2 compat
    static {
        setAE2Field();
    }

    private static void setAE2Field() {
        tt = TunnelType.ME;
    }

    private void AE2addNewAttunement(ItemStack aStack) {
        Api.INSTANCE.registries()
            .p2pTunnel()
            .addNewAttunement(aStack, (TunnelType) tt);
    }

    private void AE2AddNetAttunementCable(ItemStack aStack, OrePrefixes correspondingCable, Materials aMaterial) {
        Api.INSTANCE.registries()
            .p2pTunnel()
            .addNewAttunement(aStack, (TunnelType) tt);
        Api.INSTANCE.registries()
            .p2pTunnel()
            .addNewAttunement(GTOreDictUnificator.get(correspondingCable, aMaterial, 1L), (TunnelType) tt);
    }
    // end region
}
