package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;

public class ProcessingCell implements IOreRecipeRegistrator {

    public ProcessingCell() {
        OrePrefixes.cell.add(this);
        OrePrefixes.cellPlasma.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aPrefix) {
            case cell -> {
                if (aMaterial == Materials.Empty) {
                    GT_ModHandler.removeRecipeByOutputDelayed(aStack);
                    if (aModName.equalsIgnoreCase("AtomicScience")) {
                        RA.stdBuilder()
                            .itemInputs(ItemList.Cell_Empty.get(1L))
                            .itemOutputs(aStack)
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(extractorRecipes);
                    }
                } else {
                    if (aMaterial.mFuelPower > 0) {
                        GT_RecipeBuilder recipeBuilder = RA.stdBuilder();
                        recipeBuilder.itemInputs(GT_Utility.copyAmount(1, aStack));
                        if (GT_Utility.getFluidForFilledItem(aStack, true) == null
                            && GT_Utility.getContainerItem(aStack, true) != null) {
                            recipeBuilder.itemOutputs(GT_Utility.getContainerItem(aStack, true));
                        }
                        recipeBuilder.metadata(FUEL_VALUE, aMaterial.mFuelPower)
                            .metadata(FUEL_TYPE, aMaterial.mFuelType)
                            .addTo(GT_RecipeConstants.Fuel);
                    }
                    if (!((aMaterial.mMaterialList.size() > 0) && ((aMaterial.mExtraData & 0x3) != 0))) {
                        break;
                    }

                    int tAllAmount = 0;
                    for (MaterialStack tMat2 : aMaterial.mMaterialList) {
                        tAllAmount = (int) (tAllAmount + tMat2.mAmount);
                    }
                    long tItemAmount = 0L;
                    long tCapsuleCount = (long) GT_ModHandler
                        .getCapsuleCellContainerCountMultipliedWithStackSize(aStack) * -tAllAmount;
                    long tDensityMultiplier = aMaterial.getDensity() > 3628800L ? aMaterial.getDensity() / 3628800L
                        : 1L;
                    ArrayList<ItemStack> tList = new ArrayList<>();
                    for (MaterialStack tMat : aMaterial.mMaterialList) {
                        if (tMat.mAmount <= 0) {
                            continue;
                        }

                        ItemStack tStack;
                        if (tMat.mMaterial == Materials.Air) {
                            tStack = ItemList.Cell_Air.get(tMat.mAmount * tDensityMultiplier / 2L);
                        } else {
                            tStack = GT_OreDictUnificator.get(OrePrefixes.dust, tMat.mMaterial, tMat.mAmount);
                            if (tStack == null) {
                                tStack = GT_OreDictUnificator.get(OrePrefixes.cell, tMat.mMaterial, tMat.mAmount);
                            }
                        }
                        if (tItemAmount + tMat.mAmount * 3628800L > aStack.getMaxStackSize() * aMaterial.getDensity()) {
                            continue;
                        }

                        tItemAmount += tMat.mAmount * 3628800L;

                        if (tStack == null) {
                            continue;
                        }

                        tStack.stackSize = ((int) (tStack.stackSize * tDensityMultiplier));
                        while ((tStack.stackSize > 64)
                            && (tCapsuleCount + GT_ModHandler.getCapsuleCellContainerCount(tStack) * 64L < 0L
                                ? tList.size() < 5
                                : tList.size() < 6)
                            && (tCapsuleCount + GT_ModHandler.getCapsuleCellContainerCount(tStack) * 64L <= 64L)) {
                            tCapsuleCount += GT_ModHandler.getCapsuleCellContainerCount(tStack) * 64L;
                            tList.add(GT_Utility.copyAmount(64, tStack));
                            tStack.stackSize -= 64;
                        }
                        int tThisCapsuleCount = GT_ModHandler
                            .getCapsuleCellContainerCountMultipliedWithStackSize(tStack);
                        if (tStack.stackSize > 0 && tCapsuleCount + tThisCapsuleCount <= 64L) {
                            if (tCapsuleCount + tThisCapsuleCount < 0L ? tList.size() < 5 : tList.size() < 6) {
                                tCapsuleCount += tThisCapsuleCount;
                                tList.add(tStack);
                            }
                        }
                    }

                    tItemAmount = GT_Utility.ceilDiv(tItemAmount * tDensityMultiplier, aMaterial.getDensity());

                    if (tList.size() <= 0) {
                        break;
                    }

                    if ((aMaterial.mExtraData & 0x1) != 0) {

                        // Electrolyzer recipe
                        if (GT_Utility.getFluidForFilledItem(aStack, true) == null) {
                            // dust stuffed cell e.g. Phosphate, Phosphorous Pentoxide
                            GT_RecipeBuilder recipeBuilder = RA.stdBuilder();
                            if (tCapsuleCount > 0L) {
                                recipeBuilder.itemInputs(
                                    GT_Utility.copyAmount(tItemAmount, aStack),
                                    ItemList.Cell_Empty.get(tCapsuleCount));
                            } else {
                                recipeBuilder.itemInputs(GT_Utility.copyAmount(tItemAmount, aStack));
                            }
                            if (tCapsuleCount < 0L) {
                                tList.add(ItemList.Cell_Empty.get(-tCapsuleCount));
                            }
                            ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                            recipeBuilder.itemOutputs(outputsArray)
                                .duration(Math.max(1L, Math.abs(aMaterial.getProtons() * 2L * tItemAmount)))
                                .eut(Math.min(4, tList.size()) * 30)
                                .addTo(electrolyzerRecipes);
                        } else {
                            long tCellBalance = tCapsuleCount + tItemAmount - 1;
                            GT_RecipeBuilder recipeBuilder = RA.stdBuilder();
                            if (tCellBalance > 0L) {
                                recipeBuilder.itemInputs(aStack, ItemList.Cell_Empty.get(tCellBalance));
                            } else {
                                recipeBuilder.itemInputs(aStack);
                            }
                            if (tCellBalance < 0L) {
                                tList.add(ItemList.Cell_Empty.get(-tCellBalance));
                            }
                            ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                            recipeBuilder.itemOutputs(outputsArray)
                                .duration(Math.max(1L, Math.abs(aMaterial.getProtons() * 8L * tItemAmount)))
                                .eut(Math.min(4, tList.size()) * 30)
                                .addTo(electrolyzerRecipes);
                        }
                    }
                    if ((aMaterial.mExtraData & 0x2) != 0) {
                        GT_RecipeBuilder recipeBuilder = RA.stdBuilder();
                        if (tCapsuleCount > 0L) {
                            recipeBuilder.itemInputs(
                                GT_Utility.copyAmount(tItemAmount, aStack),
                                ItemList.Cell_Empty.get(tCapsuleCount));
                        } else {
                            recipeBuilder.itemInputs(GT_Utility.copyAmount(tItemAmount, aStack));
                        }
                        if (tCapsuleCount < 0L) {
                            tList.add(ItemList.Cell_Empty.get(-tCapsuleCount));
                        }
                        ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                        recipeBuilder.itemOutputs(outputsArray)
                            .duration(Math.max(1L, Math.abs(aMaterial.getMass() * 2L * tItemAmount)))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                }
            }
            case cellPlasma -> {
                if (aMaterial == Materials.Empty) {
                    GT_ModHandler.removeRecipeByOutputDelayed(aStack);
                } else {
                    GT_RecipeBuilder recipeBuilder = RA.stdBuilder();
                    recipeBuilder.itemInputs(GT_Utility.copyAmount(1, aStack));
                    if (GT_Utility.getFluidForFilledItem(aStack, true) == null
                        && GT_Utility.getContainerItem(aStack, true) != null) {
                        recipeBuilder.itemOutputs(GT_Utility.getContainerItem(aStack, true));
                    }
                    // Switch case to set manual values for specific plasmas and escape the formula based on mass
                    // when it doesn't make sense for powergen balance.
                    switch (aMaterial.mName) {
                        case "Aluminium" -> recipeBuilder.metadata(FUEL_VALUE, 159_744)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Americium" -> recipeBuilder.metadata(FUEL_VALUE, 501_760)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Antimony" -> recipeBuilder.metadata(FUEL_VALUE, 309_760)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Argon" -> recipeBuilder.metadata(FUEL_VALUE, 188_416)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Arsenic" -> recipeBuilder.metadata(FUEL_VALUE, 230_400)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Barium" -> recipeBuilder.metadata(FUEL_VALUE, 342_302)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Beryllium" -> recipeBuilder.metadata(FUEL_VALUE, 110_592)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Bismuth" -> recipeBuilder.metadata(FUEL_VALUE, 425_984)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Boron" -> recipeBuilder.metadata(FUEL_VALUE, 112_640)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Cadmium" -> recipeBuilder.metadata(FUEL_VALUE, 293_601)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Caesium" -> recipeBuilder.metadata(FUEL_VALUE, 332_513)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Calcium" -> recipeBuilder.metadata(FUEL_VALUE, 188_416)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Cerium" -> recipeBuilder.metadata(FUEL_VALUE, 346_931)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Chlorine" -> recipeBuilder.metadata(FUEL_VALUE, 172_032)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Chrome" -> recipeBuilder.metadata(FUEL_VALUE, 202_342)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Cobalt" -> recipeBuilder.metadata(FUEL_VALUE, 217_497)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Copper" -> recipeBuilder.metadata(FUEL_VALUE, 219_340)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Deuterium" -> recipeBuilder.metadata(FUEL_VALUE, 40_960)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Erbium" -> recipeBuilder.metadata(FUEL_VALUE, 376_217)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Europium" -> recipeBuilder.metadata(FUEL_VALUE, 355_635)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Fluorine" -> recipeBuilder.metadata(FUEL_VALUE, 147_456)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Force" -> recipeBuilder.metadata(FUEL_VALUE, 180_000)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Gadolinium" -> recipeBuilder.metadata(FUEL_VALUE, 366_551)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Gallium" -> recipeBuilder.metadata(FUEL_VALUE, 229_376)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Gold" -> recipeBuilder.metadata(FUEL_VALUE, 401_408)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Helium_3" -> recipeBuilder.metadata(FUEL_VALUE, 61_440)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Helium" -> recipeBuilder.metadata(FUEL_VALUE, 81_920)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Holmium" -> recipeBuilder.metadata(FUEL_VALUE, 376_176)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Hydrogen" -> recipeBuilder.metadata(FUEL_VALUE, 20_480)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Indium" -> recipeBuilder.metadata(FUEL_VALUE, 296_509)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Iridium" -> recipeBuilder.metadata(FUEL_VALUE, 397_148)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Iron" -> recipeBuilder.metadata(FUEL_VALUE, 206_438)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Lanthanum" -> recipeBuilder.metadata(FUEL_VALUE, 344_801)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Lead" -> recipeBuilder.metadata(FUEL_VALUE, 423_936)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Lithium" -> recipeBuilder.metadata(FUEL_VALUE, 100_352)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Lutetium" -> recipeBuilder.metadata(FUEL_VALUE, 381_296)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Magnesium" -> recipeBuilder.metadata(FUEL_VALUE, 152_371)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Manganese" -> recipeBuilder.metadata(FUEL_VALUE, 202_752)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Mercury" -> recipeBuilder.metadata(FUEL_VALUE, 409_600)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Molybdenum" -> recipeBuilder.metadata(FUEL_VALUE, 272_384)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Neodymium" -> recipeBuilder.metadata(FUEL_VALUE, 347_996)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Nickel" -> recipeBuilder.metadata(FUEL_VALUE, 213_811)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Niobium" -> recipeBuilder.metadata(FUEL_VALUE, 269_516)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Nitrogen" -> recipeBuilder.metadata(FUEL_VALUE, 129_024)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Osmium" -> recipeBuilder.metadata(FUEL_VALUE, 393_659)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Oxygen" -> recipeBuilder.metadata(FUEL_VALUE, 131_072)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Palladium" -> recipeBuilder.metadata(FUEL_VALUE, 282_214)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Phosphor" -> recipeBuilder.metadata(FUEL_VALUE, 165_888)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Platinum" -> recipeBuilder.metadata(FUEL_VALUE, 399_360)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Plutonium241" -> recipeBuilder.metadata(FUEL_VALUE, 497_664)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Plutonium" -> recipeBuilder.metadata(FUEL_VALUE, 503_808)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Potassium" -> recipeBuilder.metadata(FUEL_VALUE, 183_705)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Praseodymium" -> recipeBuilder.metadata(FUEL_VALUE, 346_931)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Promethium" -> recipeBuilder.metadata(FUEL_VALUE, 347_996)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Radon" -> recipeBuilder.metadata(FUEL_VALUE, 450_560)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Rubidium" -> recipeBuilder.metadata(FUEL_VALUE, 243_712)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Samarium" -> recipeBuilder.metadata(FUEL_VALUE, 353_280)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Scandium" -> recipeBuilder.metadata(FUEL_VALUE, 193_536)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Silicon" -> recipeBuilder.metadata(FUEL_VALUE, 160_563)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Silver" -> recipeBuilder.metadata(FUEL_VALUE, 282_685)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Sodium" -> recipeBuilder.metadata(FUEL_VALUE, 148_684)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Strontium" -> recipeBuilder.metadata(FUEL_VALUE, 249_446)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Sulfur" -> recipeBuilder.metadata(FUEL_VALUE, 170_393)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Tantalum" -> recipeBuilder.metadata(FUEL_VALUE, 384_000)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Terbium" -> recipeBuilder.metadata(FUEL_VALUE, 368_885)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Thorium" -> recipeBuilder.metadata(FUEL_VALUE, 471_040)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Thulium" -> recipeBuilder.metadata(FUEL_VALUE, 378_470)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Tin" -> recipeBuilder.metadata(FUEL_VALUE, 150_000)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Titanium" -> recipeBuilder.metadata(FUEL_VALUE, 196_608)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Tritium" -> recipeBuilder.metadata(FUEL_VALUE, 61_440)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Tungsten" -> recipeBuilder.metadata(FUEL_VALUE, 384_778)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Uranium235" -> recipeBuilder.metadata(FUEL_VALUE, 481_280)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Uranium" -> recipeBuilder.metadata(FUEL_VALUE, 487_424)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Vanadium" -> recipeBuilder.metadata(FUEL_VALUE, 198_451)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Ytterbium" -> recipeBuilder.metadata(FUEL_VALUE, 379_695)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Yttrium" -> recipeBuilder.metadata(FUEL_VALUE, 255_180)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        case "Zinc" -> recipeBuilder.metadata(FUEL_VALUE, 226_304)
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                        default -> recipeBuilder
                            .metadata(FUEL_VALUE, (int) Math.max(1024L, 1024L * aMaterial.getMass()))
                            .metadata(FUEL_TYPE, 4)
                            .addTo(GT_RecipeConstants.Fuel);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L) != null) {
                        RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L))
                            .duration(((int) Math.max(aMaterial.getMass() * 2L, 1L)) * TICKS)
                            .eut(TierEU.RECIPE_MV)
                            .addTo(vacuumFreezerRecipes);
                    }
                }
            }
            default -> {}
        }
    }
}
