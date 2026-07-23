package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MU;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingCell implements IOreRecipeRegistrator {

    public static ProcessingCell INSTANCE;

    public ProcessingCell() {
        INSTANCE = this;
        OrePrefixes.cell.add(this);
        OrePrefixes.cellPlasma.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        switch (prefix.getName()) {
            case "cell" -> {
                if (material == MU.material(Materials.Empty)) {
                    GTModHandler.removeRecipeByOutputDelayed(stack);
                    if (modName.equalsIgnoreCase("AtomicScience")) {
                        RA.stdBuilder()
                            .itemInputs(ItemList.Cell_Empty.get(1L))
                            .itemOutputs(stack)
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(extractorRecipes);
                    }
                } else {
                    if (MU.fuelPower(material) > 0) {
                        GTRecipeBuilder recipeBuilder = RA.stdBuilder();
                        recipeBuilder.itemInputs(GTUtility.copyAmount(1, stack));
                        if (GTUtility.getFluidForFilledItem(stack, true) == null
                            && GTUtility.getContainerItem(stack, true) != null) {
                            recipeBuilder.itemOutputs(GTUtility.getContainerItem(stack, true));
                        }
                        recipeBuilder.metadata(FUEL_VALUE, MU.fuelPower(material))
                            .metadata(FUEL_TYPE, MU.fuelType(material))
                            .addTo(GTRecipeConstants.Fuel);
                    }
                    List<MaterialStack> tMaterialList = MU.materialList(material);
                    if (!((!tMaterialList.isEmpty())
                        && (MU.hasElectrolyzerRecipe(material) || MU.hasCentrifugeRecipe(material)))) {
                        break;
                    }

                    int tAllAmount = 0;
                    for (MaterialStack tMat2 : tMaterialList) {
                        tAllAmount = (int) (tAllAmount + tMat2.mAmount);
                    }
                    long tItemAmount = 0L;
                    long tCapsuleCount = (long) GTModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(stack)
                        * -tAllAmount;
                    long tDensityMultiplier = MU.density(material) > 3628800L ? MU.density(material) / 3628800L : 1L;
                    ArrayList<ItemStack> tList = new ArrayList<>();
                    for (MaterialStack tMat : tMaterialList) {
                        if (tMat.mAmount <= 0) {
                            continue;
                        }

                        ItemStack tStack;
                        if (tMat.mMaterial == MU.material(Materials.Air)) {
                            tStack = ItemList.Cell_Air.get(tMat.mAmount * tDensityMultiplier / 2L);
                        } else {
                            tStack = GTOreDictUnificator.get(OrePrefixes.dust, tMat.mMaterial, tMat.mAmount);
                            if (tStack == null) {
                                tStack = GTOreDictUnificator.get(OrePrefixes.cell, tMat.mMaterial, tMat.mAmount);
                            }
                        }
                        if (tItemAmount + tMat.mAmount * 3628800L > stack.getMaxStackSize() * MU.density(material)) {
                            continue;
                        }

                        tItemAmount += tMat.mAmount * 3628800L;

                        if (tStack == null) {
                            continue;
                        }

                        tStack.stackSize = ((int) (tStack.stackSize * tDensityMultiplier));
                        while ((tStack.stackSize > 64)
                            && (tCapsuleCount + GTModHandler.getCapsuleCellContainerCount(tStack) * 64L < 0L
                                ? tList.size() < 5
                                : tList.size() < 6)
                            && (tCapsuleCount + GTModHandler.getCapsuleCellContainerCount(tStack) * 64L <= 64L)) {
                            tCapsuleCount += GTModHandler.getCapsuleCellContainerCount(tStack) * 64L;
                            tList.add(GTUtility.copyAmount(64, tStack));
                            tStack.stackSize -= 64;
                        }
                        int tThisCapsuleCount = GTModHandler
                            .getCapsuleCellContainerCountMultipliedWithStackSize(tStack);
                        if (tStack.stackSize > 0 && tCapsuleCount + tThisCapsuleCount <= 64L) {
                            if (tCapsuleCount + tThisCapsuleCount < 0L ? tList.size() < 5 : tList.size() < 6) {
                                tCapsuleCount += tThisCapsuleCount;
                                tList.add(tStack);
                            }
                        }
                    }

                    tItemAmount = GTUtility.ceilDiv(tItemAmount * tDensityMultiplier, MU.density(material));

                    if (tList.size() == 0) {
                        break;
                    }

                    if (MU.hasElectrolyzerRecipe(material)) {

                        // Electrolyzer recipe
                        if (GTUtility.getFluidForFilledItem(stack, true) == null) {
                            // dust stuffed cell e.g. Phosphate, Phosphorous Pentoxide
                            GTRecipeBuilder recipeBuilder = RA.stdBuilder();
                            if (tCapsuleCount > 0L) {
                                recipeBuilder.itemInputs(
                                    GTUtility.copyAmount(tItemAmount, stack),
                                    ItemList.Cell_Empty.get(tCapsuleCount));
                            } else {
                                recipeBuilder.itemInputs(GTUtility.copyAmount(tItemAmount, stack));
                            }
                            if (tCapsuleCount < 0L) {
                                tList.add(ItemList.Cell_Empty.get(-tCapsuleCount));
                            }
                            ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                            recipeBuilder.itemOutputs(outputsArray)
                                .duration(Math.max(1L, Math.abs(MU.protons(material) * 2L * tItemAmount)))
                                .eut(Math.min(4, tList.size()) * 30)
                                .addTo(electrolyzerRecipes);
                        } else {
                            long tCellBalance = tCapsuleCount + tItemAmount - 1;
                            GTRecipeBuilder recipeBuilder = RA.stdBuilder();
                            if (tCellBalance > 0L) {
                                recipeBuilder.itemInputs(stack, ItemList.Cell_Empty.get(tCellBalance));
                            } else {
                                recipeBuilder.itemInputs(stack);
                            }
                            if (tCellBalance < 0L) {
                                tList.add(ItemList.Cell_Empty.get(-tCellBalance));
                            }
                            ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                            recipeBuilder.itemOutputs(outputsArray)
                                .duration(Math.max(1L, Math.abs(MU.protons(material) * 8L * tItemAmount)))
                                .eut(Math.min(4, tList.size()) * 30)
                                .addTo(electrolyzerRecipes);
                        }
                    }
                    if (MU.hasCentrifugeRecipe(material)) {
                        GTRecipeBuilder recipeBuilder = RA.stdBuilder();
                        if (tCapsuleCount > 0L) {
                            recipeBuilder.itemInputs(
                                GTUtility.copyAmount(tItemAmount, stack),
                                ItemList.Cell_Empty.get(tCapsuleCount));
                        } else {
                            recipeBuilder.itemInputs(GTUtility.copyAmount(tItemAmount, stack));
                        }
                        if (tCapsuleCount < 0L) {
                            tList.add(ItemList.Cell_Empty.get(-tCapsuleCount));
                        }
                        ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                        recipeBuilder.itemOutputs(outputsArray)
                            .duration(Math.max(1L, Math.abs(MU.mass(material) * 2L * tItemAmount)))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                }
            }
            case "cellPlasma" -> {
                if (material == MU.material(Materials.Empty)) {
                    GTModHandler.removeRecipeByOutputDelayed(stack);
                } else {
                    GTRecipeBuilder recipeBuilder = RA.stdBuilder();
                    recipeBuilder.itemInputs(GTUtility.copyAmount(1, stack));
                    if (GTUtility.getFluidForFilledItem(stack, true) == null
                        && GTUtility.getContainerItem(stack, true) != null) {
                        recipeBuilder.itemOutputs(GTUtility.getContainerItem(stack, true));
                    }
                    // Switch case to set manual values for specific plasmas and escape the formula based on mass
                    // when it doesn't make sense for powergen balance.
                    switch (MU.internalName(material)) {
                        case "Aluminium" -> recipeBuilder.metadata(FUEL_VALUE, 159_744)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Americium" -> recipeBuilder.metadata(FUEL_VALUE, 501_760)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Antimony" -> recipeBuilder.metadata(FUEL_VALUE, 309_760)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Argon" -> recipeBuilder.metadata(FUEL_VALUE, 188_416)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Arsenic" -> recipeBuilder.metadata(FUEL_VALUE, 230_400)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Barium" -> recipeBuilder.metadata(FUEL_VALUE, 342_302)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Beryllium" -> recipeBuilder.metadata(FUEL_VALUE, 110_592)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Bismuth" -> recipeBuilder.metadata(FUEL_VALUE, 425_984)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Boron" -> recipeBuilder.metadata(FUEL_VALUE, 112_640)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Cadmium" -> recipeBuilder.metadata(FUEL_VALUE, 293_601)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Caesium" -> recipeBuilder.metadata(FUEL_VALUE, 332_513)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Calcium" -> recipeBuilder.metadata(FUEL_VALUE, 188_416)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Cerium" -> recipeBuilder.metadata(FUEL_VALUE, 346_931)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Chlorine" -> recipeBuilder.metadata(FUEL_VALUE, 172_032)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Chrome" -> recipeBuilder.metadata(FUEL_VALUE, 202_342)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Cobalt" -> recipeBuilder.metadata(FUEL_VALUE, 217_497)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Copper" -> recipeBuilder.metadata(FUEL_VALUE, 219_340)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Creon" -> {}
                        case "Deuterium" -> recipeBuilder.metadata(FUEL_VALUE, 40_960)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Erbium" -> recipeBuilder.metadata(FUEL_VALUE, 376_217)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Europium" -> recipeBuilder.metadata(FUEL_VALUE, 355_635)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Fluorine" -> recipeBuilder.metadata(FUEL_VALUE, 147_456)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Force" -> recipeBuilder.metadata(FUEL_VALUE, 180_000)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Gadolinium" -> recipeBuilder.metadata(FUEL_VALUE, 366_551)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Gallium" -> recipeBuilder.metadata(FUEL_VALUE, 229_376)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Gold" -> recipeBuilder.metadata(FUEL_VALUE, 401_408)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Helium_3" -> recipeBuilder.metadata(FUEL_VALUE, 61_440)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Helium" -> recipeBuilder.metadata(FUEL_VALUE, 81_920)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Holmium" -> recipeBuilder.metadata(FUEL_VALUE, 376_176)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Hydrogen" -> recipeBuilder.metadata(FUEL_VALUE, 20_480)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Indium" -> recipeBuilder.metadata(FUEL_VALUE, 296_509)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Iridium" -> recipeBuilder.metadata(FUEL_VALUE, 397_148)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Iron" -> recipeBuilder.metadata(FUEL_VALUE, 206_438)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Lanthanum" -> recipeBuilder.metadata(FUEL_VALUE, 344_801)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Lead" -> recipeBuilder.metadata(FUEL_VALUE, 423_936)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Lithium" -> recipeBuilder.metadata(FUEL_VALUE, 100_352)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Lutetium" -> recipeBuilder.metadata(FUEL_VALUE, 381_296)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Magnesium" -> recipeBuilder.metadata(FUEL_VALUE, 152_371)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Manganese" -> recipeBuilder.metadata(FUEL_VALUE, 202_752)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Mercury" -> recipeBuilder.metadata(FUEL_VALUE, 409_600)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Molybdenum" -> recipeBuilder.metadata(FUEL_VALUE, 272_384)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Neodymium" -> recipeBuilder.metadata(FUEL_VALUE, 347_996)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Nickel" -> recipeBuilder.metadata(FUEL_VALUE, 213_811)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Niobium" -> recipeBuilder.metadata(FUEL_VALUE, 269_516)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Nitrogen" -> recipeBuilder.metadata(FUEL_VALUE, 129_024)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Osmium" -> recipeBuilder.metadata(FUEL_VALUE, 393_659)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Oxygen" -> recipeBuilder.metadata(FUEL_VALUE, 131_072)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Palladium" -> recipeBuilder.metadata(FUEL_VALUE, 282_214)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Phosphor" -> recipeBuilder.metadata(FUEL_VALUE, 165_888)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Platinum" -> recipeBuilder.metadata(FUEL_VALUE, 399_360)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Plutonium241" -> recipeBuilder.metadata(FUEL_VALUE, 497_664)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Plutonium" -> recipeBuilder.metadata(FUEL_VALUE, 503_808)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Potassium" -> recipeBuilder.metadata(FUEL_VALUE, 183_705)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Praseodymium" -> recipeBuilder.metadata(FUEL_VALUE, 346_931)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Promethium" -> recipeBuilder.metadata(FUEL_VALUE, 347_996)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Radon" -> recipeBuilder.metadata(FUEL_VALUE, 450_560)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Rubidium" -> recipeBuilder.metadata(FUEL_VALUE, 243_712)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Samarium" -> recipeBuilder.metadata(FUEL_VALUE, 353_280)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Scandium" -> recipeBuilder.metadata(FUEL_VALUE, 193_536)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Silicon" -> recipeBuilder.metadata(FUEL_VALUE, 160_563)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Silver" -> recipeBuilder.metadata(FUEL_VALUE, 282_685)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Sodium" -> recipeBuilder.metadata(FUEL_VALUE, 148_684)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Strontium" -> recipeBuilder.metadata(FUEL_VALUE, 249_446)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Sulfur" -> recipeBuilder.metadata(FUEL_VALUE, 170_393)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Tantalum" -> recipeBuilder.metadata(FUEL_VALUE, 384_000)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Terbium" -> recipeBuilder.metadata(FUEL_VALUE, 368_885)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Thorium" -> recipeBuilder.metadata(FUEL_VALUE, 471_040)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Thulium" -> recipeBuilder.metadata(FUEL_VALUE, 378_470)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Tin" -> recipeBuilder.metadata(FUEL_VALUE, 150_000)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Titanium" -> recipeBuilder.metadata(FUEL_VALUE, 196_608)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Tritium" -> recipeBuilder.metadata(FUEL_VALUE, 61_440)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Tungsten" -> recipeBuilder.metadata(FUEL_VALUE, 384_778)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Uranium235" -> recipeBuilder.metadata(FUEL_VALUE, 481_280)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Uranium" -> recipeBuilder.metadata(FUEL_VALUE, 487_424)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Vanadium" -> recipeBuilder.metadata(FUEL_VALUE, 198_451)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Ytterbium" -> recipeBuilder.metadata(FUEL_VALUE, 379_695)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Yttrium" -> recipeBuilder.metadata(FUEL_VALUE, 255_180)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        case "Zinc" -> recipeBuilder.metadata(FUEL_VALUE, 226_304)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                        default -> recipeBuilder.metadata(FUEL_VALUE, (int) Math.max(1024L, 1024L * MU.mass(material)))
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GTRecipeConstants.Fuel);
                    }
                    if (GTOreDictUnificator.get(OrePrefixes.cell, material, 1L) != null) {
                        RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, material, 1L))
                            .duration(((int) Math.max(MU.mass(material) * 2L, 1L)) * TICKS)
                            .eut(TierEU.RECIPE_MV)
                            .addTo(vacuumFreezerRecipes);
                    }
                }
            }
            default -> {}
        }
    }
}
