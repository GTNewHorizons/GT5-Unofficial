package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import appeng.api.config.TunnelType;
import appeng.core.Api;
import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

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
        if (GT_Mod.gregtechproxy.mAE2Integration) {
            if (tt == TunnelType.ME) {
                try {
                    tt = TunnelType.valueOf("GT_POWER");
                } catch (IllegalArgumentException ignored) {
                    tt = TunnelType.IC2_POWER;
                }
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
                        if (GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 2L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8))
                                .addTo(sBenderRecipes);
                        }
                    }

                    // Wiremill Recipes
                    {
                        if (GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 1L) != null) {
                            GT_Values.RA.addWiremillRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                GT_Utility.getIntegratedCircuit(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 4L),
                                200,
                                calculateRecipeEU(aMaterial, 8));
                        }
                    }
                }

                // crafting recipe
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)
                    && (aMaterial.getProcessingMaterialTierEU() < TierEU.IV)) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "Xx", 'X', OrePrefixes.plate.get(aMaterial) });

                }

                // Assembler recipes
                {
                    GT_Values.RA.addAssemblerRecipe(
                        GT_Utility.copyAmount(2L, aStack),
                        GT_Utility.getIntegratedCircuit(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L),
                        150,
                        calculateRecipeEU(aMaterial, 8));
                    GT_Values.RA.addAssemblerRecipe(
                        GT_Utility.copyAmount(4L, aStack),
                        GT_Utility.getIntegratedCircuit(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L),
                        200,
                        calculateRecipeEU(aMaterial, 8));
                    GT_Values.RA.addAssemblerRecipe(
                        GT_Utility.copyAmount(8L, aStack),
                        GT_Utility.getIntegratedCircuit(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L),
                        300,
                        calculateRecipeEU(aMaterial, 8));
                    GT_Values.RA.addAssemblerRecipe(
                        GT_Utility.copyAmount(12L, aStack),
                        GT_Utility.getIntegratedCircuit(12),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L),
                        400,
                        calculateRecipeEU(aMaterial, 8));
                    GT_Values.RA.addAssemblerRecipe(
                        GT_Utility.copyAmount(16L, aStack),
                        GT_Utility.getIntegratedCircuit(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L),
                        500,
                        calculateRecipeEU(aMaterial, 8));
                }
            }
            case wireGt02 -> {
                cableWidth = 2;
                correspondingCable = OrePrefixes.cableGt02;
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    // Shapeless crafting recipes
                    {
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 2L),
                            new Object[] { aOreDictName });

                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
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
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 4L),
                            new Object[] { aOreDictName });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            new Object[] { OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial) });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
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
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 8L),
                            new Object[] { aOreDictName });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            new Object[] { OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),
                                OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial) });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
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
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 12L),
                            new Object[] { aOreDictName });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
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
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 16L),
                            new Object[] { aOreDictName });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            new Object[] { OrePrefixes.wireGt08.get(aMaterial), OrePrefixes.wireGt08.get(aMaterial) });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            new Object[] { OrePrefixes.wireGt12.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial) });
                    }

                    if (GT_Mod.gregtechproxy.mAE2Integration) {
                        AE2addNewAttunement(aStack);
                    }
                }
            }
            default -> {
                GT_Log.err.println(
                    "OrePrefix " + aPrefix.name() + " cannot be registered as a cable for Material " + aMaterial.mName);
                return;
            }
        }

        int costMultiplier = cableWidth / 4 + 1;

        switch (aMaterial.mName) {
            case "RedAlloy", "Cobalt", "Lead", "Tin", "Zinc", "SolderingAlloy":
                ArrayList<Object> craftingListRubber = new ArrayList<>();
                craftingListRubber.add(aOreDictName);
                for (int i = 0; i < costMultiplier; i++) {
                    craftingListRubber.add(OrePrefixes.plate.get(Materials.Rubber));
                }

            // shapeless crafting
            {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    craftingListRubber.toArray());
            }

            // Packer recipe
            {
                GT_Values.RA.addBoxingRecipe(
                    GT_Utility.copyAmount(1L, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.Rubber), costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
            }

            // alloy smelter recipes
            {
                GT_Values.RA.addAlloySmelterRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, aMaterial, 1L),
                    100,
                    8);
                GT_Values.RA.addAlloySmelterRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt02, aMaterial, 1L),
                    200,
                    16);
                GT_Values.RA.addAlloySmelterRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, aMaterial, 1L),
                    300,
                    30);
            }
            case "Iron", "Nickel", "Cupronickel", "Copper", "AnnealedCopper", "Kanthal", "Gold", "Electrum", "Silver", "Blue Alloy", "Nichrome", "Steel", "BlackSteel", "Titanium", "Aluminium", "BlueAlloy":
            // Assembler recipes
            {
                GT_Values.RA.addAssemblerRecipe(
                    aStack,
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.Rubber.getMolten(144 * costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
                GT_Values.RA.addAssemblerRecipe(
                    aStack,
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.StyreneButadieneRubber.getMolten(108 * costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
                GT_Values.RA.addAssemblerRecipe(
                    aStack,
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.Silicone.getMolten(72 * costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier) },
                            syntheticRubber.getMolten(costMultiplier * 144),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L),
                            400,
                            8);
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { aStack, dielectric.getDustSmall(costMultiplier) },
                            syntheticRubber.getMolten(costMultiplier * 36),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                            100,
                            8);
                    }
                }
            }

            case "RedstoneAlloy":
            // Assembler recipes
            {
                GT_Values.RA.addAssemblerRecipe(
                    aStack,
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.Rubber.getMolten(144 * costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
                GT_Values.RA.addAssemblerRecipe(
                    aStack,
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.StyreneButadieneRubber.getMolten(108 * costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
                GT_Values.RA.addAssemblerRecipe(
                    aStack,
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.Silicone.getMolten(72 * costMultiplier),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    8);
                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier) },
                            syntheticRubber.getMolten(costMultiplier * 144),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L),
                            400,
                            8);
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { aStack, dielectric.getDustSmall(costMultiplier) },
                            syntheticRubber.getMolten(costMultiplier * 36),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                            100,
                            8);
                    }
                }
            }
                break;
            default:
            // Assembler recipes
            {
                GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { aStack, GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier),
                        GT_Utility.getIntegratedCircuit(24) },
                    Materials.Silicone.getMolten(costMultiplier * 72),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    calculateRecipeEU(aMaterial, 8));
                GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { aStack,
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier),
                        GT_Utility.getIntegratedCircuit(24) },
                    Materials.Silicone.getMolten(costMultiplier * 72),
                    GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                    100,
                    calculateRecipeEU(aMaterial, 8));
                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier),
                                GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier * 4) },
                            syntheticRubber.getMolten(costMultiplier * 144),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L),
                            400,
                            calculateRecipeEU(aMaterial, 8));
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier),
                                GT_OreDictUnificator
                                    .get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier * 4) },
                            syntheticRubber.getMolten(costMultiplier * 144),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L),
                            400,
                            calculateRecipeEU(aMaterial, 8));
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { aStack, dielectric.getDustSmall(costMultiplier),
                                GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier) },
                            syntheticRubber.getMolten(costMultiplier * 36),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                            100,
                            calculateRecipeEU(aMaterial, 8));
                        GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { aStack, dielectric.getDustSmall(costMultiplier),
                                GT_OreDictUnificator
                                    .get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier) },
                            syntheticRubber.getMolten(costMultiplier * 36),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
                            100,
                            calculateRecipeEU(aMaterial, 8));
                    }
                }
            }
                break;
        }

        // Honestly when can this machine be removed? );
        GT_Values.RA.addUnboxingRecipe(
            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),
            GT_Utility.copyAmount(1L, aStack),
            null,
            100,
            calculateRecipeEU(aMaterial, 8));

        if (GT_Mod.gregtechproxy.mAE2Integration) {
            AE2AddNetAttunementCable(aStack, correspondingCable, aMaterial);
        }
    }

    // region AE2 compat
    static {
        if (GT_Mod.gregtechproxy.mAE2Integration) setAE2Field();
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
            .addNewAttunement(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), (TunnelType) tt);
    }
    // end region
}
