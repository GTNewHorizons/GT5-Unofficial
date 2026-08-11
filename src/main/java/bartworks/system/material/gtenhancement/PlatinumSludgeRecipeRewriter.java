/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.gtenhancement;

import static bartworks.system.material.WerkstoffLoader.AcidicIridiumSolution;
import static bartworks.system.material.WerkstoffLoader.AcidicOsmiumSolution;
import static bartworks.system.material.WerkstoffLoader.CrudeRhMetall;
import static bartworks.system.material.WerkstoffLoader.IrLeachResidue;
import static bartworks.system.material.WerkstoffLoader.IrOsLeachResidue;
import static bartworks.system.material.WerkstoffLoader.LeachResidue;
import static bartworks.system.material.WerkstoffLoader.PDMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.PTConcentrate;
import static bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.Rhodium;
import static bartworks.system.material.WerkstoffLoader.Ruthenium;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedCentrifuged;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustRefined;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.rawOre;
import static gregtech.api.recipe.RecipeMaps.alloyBlastSmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.quantumForceTransformerRecipes;
import static gregtech.api.recipe.RecipeMaps.replicatorRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFurnaceRecipes;
import static gtPlusPlus.core.material.MaterialsAlloy.HELICOPTER;
import static kubatech.loaders.HTGRLoader.HTGRRecipes;
import static tectech.recipe.TecTechRecipeMaps.eyeOfHarmonyRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

import bartworks.MainMod;
import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import bartworks.util.BWUtil;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.items.GTGenericItem;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.GTBlockOre;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.item.base.BaseItemComponent;
import kubatech.loaders.item.htgritem.HTGRItem;

final class PlatinumSludgeRecipeRewriter {

    private static final List<Materials> MATERIALS_BLACKLIST = Arrays.asList(
        Materials.HSSS,
        Materials.EnderiumBase,
        Materials.Osmiridium,
        Materials.TPV,
        Materials.SuperconductorEVBase,
        Materials.SuperconductorZPMBase,
        Materials.SuperconductorUVBase);

    private static final List<OrePrefixes> ORE_PREFIXES_BLACKLIST = Arrays.asList(
        crushedCentrifuged,
        crushed,
        crushedPurified,
        dustPure,
        dustImpure,
        dustRefined,
        dust,
        dustTiny,
        dustSmall);

    private PlatinumSludgeRecipeRewriter() {}

    private static boolean isMapIgnored(RecipeMap<?> map) {
        return map == fusionRecipes || map == unpackagerRecipes
            || map == packagerRecipes
            || map == replicatorRecipes
            || map == eyeOfHarmonyRecipes
            || map == quantumForceTransformerRecipes
            || map == fluidExtractionRecipes
            || map == alloyBlastSmelterRecipes
            || map == HTGRRecipes
            || map == vacuumFurnaceRecipes;
    }

    private static String displayRecipe(GTRecipe recipe) {
        StringBuilder result = new StringBuilder();
        // item inputs
        result.append("Item inputs: ");
        for (ItemStack itemstack : recipe.mInputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid inputs
        result.append(" Fluid inputs: ");
        for (FluidStack fluidStack : recipe.mFluidInputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // item outputs
        result.append(" Item outputs: ");
        for (ItemStack itemstack : recipe.mOutputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid outputs
        result.append(" Fluid outputs: ");
        for (FluidStack fluidStack : recipe.mFluidOutputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        return result.toString();
    }

    static void rewriteLegacyRecipes() {
        final ArrayList<ItemStack> availableItemList = collectAvailableItemList();
        replaceFurnaceRecipes(availableItemList);
        replaceCraftingRecipes();
        replaceMachineRecipes(availableItemList);
    }

    private static ArrayList<ItemStack> collectAvailableItemList() {
        final ItemList[] itemList = ItemList.values();
        final ArrayList<ItemStack> availableItemList = new ArrayList<>(itemList.length);
        for (ItemList item : itemList) {
            if (item.hasBeenSet()) {
                availableItemList.add(item.get(1));
            }
        }
        return availableItemList;
    }

    private static void replaceFurnaceRecipes(List<ItemStack> availableItemList) {
        // furnace
        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            ItemStack input = entry.getKey();
            ItemStack output = entry.getValue();

            if (!GTUtility.isStackValid(input)) continue;
            if (!GTUtility.isStackValid(output)) continue;

            ItemData outputAssociation = GTOreDictUnificator.getAssociation(output);
            if (!BWUtil.checkStackAndPrefix(outputAssociation)) continue;

            final Werkstoff newOutput;
            if (outputAssociation.mMaterial.mMaterial == Materials.Platinum) {
                newOutput = PTMetallicPowder;
            } else if (outputAssociation.mMaterial.mMaterial == Materials.Palladium) {
                newOutput = PDMetallicPowder;
            } else {
                continue;
            }

            ItemData inputAssociation = GTOreDictUnificator.getAssociation(input);
            if (!BWUtil.checkStackAndPrefix(inputAssociation)) continue;

            if (inputAssociation.mMaterial.mMaterial == Materials.Platinum) {
                if (inputAssociation.mPrefix == dust || inputAssociation.mPrefix == dustTiny) {
                    continue;
                }
            }

            if (isInBlackList(input, availableItemList)) continue;

            OrePrefixes prefix = outputAssociation.mPrefix == nugget ? dustTiny : dust;
            entry.setValue(newOutput.get(prefix, output.stackSize * 2));
        }
    }

    private static void replaceCraftingRecipes() {
        // vanilla crafting
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(PlatinumSludgeRecipeRewriter::setNewMaterialInRecipe);

        // gt crafting
        GTModHandler.sBufferRecipeList.forEach(PlatinumSludgeRecipeRewriter::setNewMaterialInRecipe);
    }

    private static void replaceMachineRecipes(List<ItemStack> availableItemList) {
        // gt machines
        maploop: for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            if (isMapIgnored(map)) continue;

            GTLog.out.println("Processing recipe map: " + map.unlocalizedName);

            ArrayList<GTRecipe> toDelete = new ArrayList<>();

            recipeloop: for (GTRecipe recipe : map.getAllRecipes()) {
                if (recipe.mFakeRecipe) continue maploop;

                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                    if (map == multiblockChemicalReactorRecipes || map == chemicalReactorRecipes) {
                        if (GTUtility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                            || GTUtility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])) {
                            toDelete.add(recipe);
                            GTLog.out.println("Recipe marked for deletion: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = AcidicIridiumSolution.getFluidOrGas(1_000);
                            recipe.reloadOwner();
                            GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = PTConcentrate.getFluidOrGas(2_000);
                            recipe.reloadOwner();
                            GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = AcidicOsmiumSolution.getFluidOrGas(1_000);
                            recipe.reloadOwner();
                            GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                        }
                    } else if (GTUtility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i])) {
                            toDelete.add(recipe);
                            GTLog.out.println("Recipe marked for deletion: " + displayRecipe(recipe));
                        }
                }

                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(recipe.mOutputs[i])) continue;

                    if ((GTUtility.areStacksEqual(Ruthenium.get(dust), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Ruthenium.get(dustImpure), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Ruthenium.get(dustPure), recipe.mOutputs[i]))
                        && !GTUtility.areStacksEqual(Ruthenium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (isInBlackList(mInput, availableItemList)) continue recipeloop;
                        }
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = LeachResidue.get(dust, amount);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }

                    if ((GTUtility.areStacksEqual(Rhodium.get(dust), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Rhodium.get(dustImpure), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Rhodium.get(dustPure), recipe.mOutputs[i]))
                        && !GTUtility.areStacksEqual(Rhodium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (isInBlackList(mInput, availableItemList)) continue recipeloop;
                        }
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = CrudeRhMetall.get(dust, amount);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }

                    ItemData association = GTOreDictUnificator.getAssociation(recipe.mOutputs[i]);
                    if (!BWUtil.checkStackAndPrefix(association)) continue;

                    final Werkstoff replacementMaterial;
                    if (association.mMaterial.mMaterial == Materials.Platinum) {
                        replacementMaterial = PTMetallicPowder;
                    } else if (association.mMaterial.mMaterial == Materials.Palladium) {
                        replacementMaterial = PDMetallicPowder;
                    } else if (association.mMaterial.mMaterial == Materials.Osmium) {
                        replacementMaterial = IrOsLeachResidue;
                    } else if (association.mMaterial.mMaterial == Materials.Iridium) {
                        replacementMaterial = IrLeachResidue;
                    } else {
                        continue;
                    }

                    for (ItemStack mInput : recipe.mInputs) {
                        if (isInBlackList(mInput, availableItemList)) continue recipeloop;
                    }

                    if (association.mPrefix == dust || association.mPrefix == dustImpure
                        || association.mPrefix == dustPure) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = BWUtil.setStackSize(replacementMaterial.get(dust), amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    } else if (association.mPrefix == dustSmall) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = BWUtil.setStackSize(replacementMaterial.get(dustSmall), amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    } else if (association.mPrefix == dustTiny) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = BWUtil.setStackSize(replacementMaterial.get(dustTiny), amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }
                }
            }

            map.getBackend()
                .removeRecipes(toDelete);
        }
        // TODO: remove EnderIO recipes
    }

    static void replaceHVCircuitInputs() {
        GTLog.out.println("Processing hv circuit materials (circuit assembler map)");
        for (GTRecipe recipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (recipe.mEUt > 512) continue;
            if (BWUtil.checkStackAndPrefix(recipe.mOutputs[0])) {
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    ItemStack stack = recipe.mInputs[i];
                    ItemData association = GTOreDictUnificator.getAssociation(stack);
                    if (!BWUtil.checkStackAndPrefix(association)) continue;

                    if (association.mMaterial.mMaterial.equals(Materials.Platinum)) {
                        recipe.mInputs[i] = GTOreDictUnificator
                            .get(association.mPrefix, Materials.BlueAlloy, stack.stackSize);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }
                }
            }
        }
    }

    private static void setNewMaterialInRecipe(IRecipe recipe) {
        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();
        if (input == null) {
            return;
        }

        ItemStack output = recipe.getRecipeOutput();

        if (GTUtility.areStacksEqual(output, Materials.Platinum.getDust(1), true)) {
            if (BWUtil.areCraftingInputsOnlyMaterial(input, Materials.Platinum)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(PTMetallicPowder.get(dust, output.stackSize * 2));
        } else if (GTUtility.areStacksEqual(output, Materials.Palladium.getDust(1), true)) {
            if (BWUtil.areCraftingInputsOnlyMaterial(input, Materials.Palladium)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(PDMetallicPowder.get(dust, output.stackSize * 2));
        } else if (GTUtility.areStacksEqual(output, Materials.Iridium.getDust(1), true)) {
            if (BWUtil.areCraftingInputsOnlyMaterial(input, Materials.Iridium)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(IrLeachResidue.get(dust, output.stackSize));
        } else if (GTUtility.areStacksEqual(output, Materials.Osmium.getDust(1), true)) {
            if (BWUtil.areCraftingInputsOnlyMaterial(input, Materials.Osmium)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(IrOsLeachResidue.get(dust, output.stackSize));
        }
    }

    private static boolean isInBlackList(ItemStack stack, List<ItemStack> availableItemList) {
        if (stack == null) return true;

        final Item item = stack.getItem();
        if (item == null) return true;
        if (item instanceof BWMetaGeneratedItems) return true;

        final String itemModId = GameRegistry.findUniqueIdentifierFor(item).modId;
        if (MainMod.MOD_ID.equals(itemModId) || BartWorksCrossmod.MOD_ID.equals(itemModId)) return true;

        final String stackUnlocalizedName = stack.getUnlocalizedName();
        if (NewHorizonsCoreMod.ID.equals(itemModId) && !stackUnlocalizedName.contains("dust")
            && !stackUnlocalizedName.contains("Dust")) return true;

        Block block = Block.getBlockFromItem(item);
        if (block instanceof GTGenericBlock && !(block instanceof GTBlockOre)) return true;

        ItemData association = GTOreDictUnificator.getAssociation(stack);
        boolean isAssociationValid = BWUtil.checkStackAndPrefix(association);

        if (!isAssociationValid) {
            for (ItemStack itemStack : availableItemList) {
                if (GTUtility.areStacksEqual(itemStack, stack, true)) {
                    return true;
                }
            }
        }

        if (item instanceof GTGenericItem) {
            if (!isAssociationValid) return false;
            if (association.mPrefix != rawOre) {
                return !ORE_PREFIXES_BLACKLIST.contains(association.mPrefix)
                    || MATERIALS_BLACKLIST.contains(association.mMaterial.mMaterial);
            }
        }

        if (item instanceof BaseItemComponent && !stackUnlocalizedName.contains("dust")
            && !stackUnlocalizedName.contains("Dust")) {
            return true;
        }
        if (block instanceof BlockBaseModular) {
            return true;
        }
        if (block instanceof BlockFrameBox) {
            return true;
        }
        if (item == HELICOPTER.getDust(1)
            .getItem()) {
            return true;
        }
        if (item == HTGRItem.BURNED_TRISO) {
            return true;
        }
        if (Railcraft.isModLoaded()) {
            if (block.getUnlocalizedName()
                .equals("tile.railcraft.machine.zeta")
                || block.getUnlocalizedName()
                    .equals("tile.railcraft.machine.eta")) {
                return true;
            }
        }
        if (GalaxySpace.isModLoaded()) {
            if (item == GTModHandler.getModItem(GalaxySpace.ID, "metalsblock", 1L, 7)
                .getItem()) {
                return true;
            }
        }
        if (NewHorizonsCoreMod.isModLoaded()) {
            if (item == GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IndustryFrame", 1L)
                .getItem()) {
                return true;
            }
        }
        if (!isAssociationValid) {
            return false;
        }
        return MATERIALS_BLACKLIST.contains(association.mMaterial.mMaterial);
    }
}
