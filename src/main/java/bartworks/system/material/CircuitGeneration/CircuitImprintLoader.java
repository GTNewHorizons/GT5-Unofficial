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

package bartworks.system.material.CircuitGeneration;

import static gregtech.api.enums.GTValues.VP;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTModHandler.getModItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import bartworks.API.recipe.BWNBTDependantCraftingRecipe;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.MainMod;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BWUtil;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;

public class CircuitImprintLoader {

    public static short reverseIDs = Short.MAX_VALUE - 1;

    public static final ArrayListMultimap<NBTTagCompound, GTRecipe> recipeTagMap = ArrayListMultimap.create();
    public static final HashBiMap<Short, ItemList> circuitIIconRefs = HashBiMap.create(20);
    public static final HashSet<ItemStack> blacklistSet = new HashSet<>();
    private static final HashSet<IRecipe> recipeWorldCache = new HashSet<>();
    private static final HashSet<GTRecipe> ORIGINAL_CAL_RECIPES = new HashSet<>();
    private static final HashSet<GTRecipe> MODIFIED_CAL_RECIPES = new HashSet<>();

    public static void registerItemstacks(){
        ItemList.CircuitImprint_NANDChipArray.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.NandChip.get(1)), 0, 0));
        ItemList.SlicedCircuit_NANDChipArray.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.NandChip.get(1)), 1, 0));

        ItemList.CircuitImprint_Microprocessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Microprocessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_Microprocessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Microprocessor.get(1)), 1, 0));

        ItemList.CircuitImprint_ElectronicCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("electronicCircuit", 1)), 0, 0));
        ItemList.SlicedCircuit_ElectronicCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("electronicCircuit", 1)), 1, 0));
        ItemList.CircuitImprint_GoodElectronicCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Good.get(1)), 0, 0));
        ItemList.SlicedCircuit_GoodElectronicCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Good.get(1)), 1, 0));

        ItemList.CircuitImprint_IntegratedLogicCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Chip_ILC.get(1)), 0, 0));
        ItemList.SlicedCircuit_IntegratedLogicCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Chip_ILC.get(1)), 1, 0));
        ItemList.CircuitImprint_GoodIntegratedCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Integrated_Good.get(1)), 0, 0));
        ItemList.SlicedCircuit_GoodIntegratedCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Integrated_Good.get(1)), 1, 0));
        ItemList.CircuitImprint_AdvancedCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("advancedCircuit", 1)), 0, 0));
        ItemList.SlicedCircuit_AdvancedCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("advancedCircuit", 1)), 1, 0));

        ItemList.CircuitImprint_IntegratedProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Processor.get(1)), 0, 0));
        ItemList.SlicedCircuit_IntegratedProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Processor.get(1)), 1, 0));
        ItemList.CircuitImprint_ProcessorAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Advanced.get(1)), 0, 0));
        ItemList.SlicedCircuit_ProcessorAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Advanced.get(1)), 1, 0));
        ItemList.CircuitImprint_Workstation.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Data.get(1)), 0, 0));
        ItemList.SlicedCircuit_Workstation.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Data.get(1)), 1, 0));
        ItemList.CircuitImprint_Mainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Elite.get(1)), 0, 0));
        ItemList.SlicedCircuit_Mainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Elite.get(1)), 1, 0));

        ItemList.CircuitImprint_NanoProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanoprocessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_NanoProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanoprocessor.get(1)), 1, 0));
        ItemList.CircuitImprint_NanoAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanocomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_NanoAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanocomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_NanoSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Elitenanocomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_NanoSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Elitenanocomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_NanoMainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Master.get(1)), 0, 0));
        ItemList.SlicedCircuit_NanoMainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Master.get(1)), 1, 0));

        ItemList.CircuitImprint_QuantumProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumprocessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_QuantumProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumprocessor.get(1)), 1, 0));
        ItemList.CircuitImprint_QuantumAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumcomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_QuantumAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumcomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_QuantumSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Masterquantumcomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_QuantumSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Masterquantumcomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_QuantumMainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantummainframe.get(1)), 0, 0));
        ItemList.SlicedCircuit_QuantumMainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantummainframe.get(1)), 1, 0));

        ItemList.CircuitImprint_CrystalProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalprocessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_CrystalProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalprocessor.get(1)), 1, 0));
        ItemList.CircuitImprint_CrystalAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalcomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_CrystalAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalcomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_CrystalSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Ultimatecrystalcomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_CrystalSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Ultimatecrystalcomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_CrystalMainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalmainframe.get(1)), 0, 0));
        ItemList.SlicedCircuit_CrystalMainframe.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalmainframe.get(1)), 1, 0));

        ItemList.CircuitImprint_WetwareProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Neuroprocessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_WetwareProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Neuroprocessor.get(1)), 1, 0));
        ItemList.CircuitImprint_WetwareAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwarecomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_WetwareAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwarecomputer.get(1)), 1, 0));
        ItemList.CircuitImprint_WetwareSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwaresupercomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_WetwareSupercomputer.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwaresupercomputer.get(1)), 1, 0));

        ItemList.CircuitImprint_BiowareProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Bioprocessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_BiowareProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Bioprocessor.get(1)), 1, 0));
        ItemList.CircuitImprint_BiowareAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Biowarecomputer.get(1)), 0, 0));
        ItemList.SlicedCircuit_BiowareAssembly.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_Biowarecomputer.get(1)), 1, 0));

        ItemList.CircuitImprint_OpticalProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_OpticalProcessor.get(1)), 0, 0));
        ItemList.SlicedCircuit_OpticalProcessor.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(ItemList.Circuit_OpticalProcessor.get(1)), 1, 0));

        if (Forestry.isModLoaded()) {
            ItemList.CircuitImprint_BasicCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 0)), 0, 0));
            ItemList.SlicedCircuit_BasicCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 0)), 1, 0));
            ItemList.CircuitImprint_EnhancedCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 1)), 0, 0));
            ItemList.SlicedCircuit_EnhancedCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 1)), 1, 0));
            ItemList.CircuitImprint_RefinedCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 2)), 0, 0));
            ItemList.SlicedCircuit_RefinedCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 2)), 1, 0));
            ItemList.CircuitImprint_IntricateCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 3)), 0, 0));
            ItemList.SlicedCircuit_IntricateCircuitBoard.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 3)), 1, 0));
        }

        if (Railcraft.isModLoaded()) {
            ItemList.CircuitImprint_ControllerCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 0)), 0, 0));
            ItemList.SlicedCircuit_ControllerCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 0)), 1, 0));
            ItemList.CircuitImprint_ReceiverCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 1)), 0, 0));
            ItemList.SlicedCircuit_ReceiverCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 1)), 1, 0));
            ItemList.CircuitImprint_SignalCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 2)), 0, 0));
            ItemList.SlicedCircuit_SignalCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 2)), 1, 0));
       }

        if (NewHorizonsCoreMod.isModLoaded()) {
            ItemList.CircuitImprint_HighEnergyFlowCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(getModItem(NewHorizonsCoreMod.ID, "item.HighEnergyFlowCircuit", 64, 0)), 0, 0));
            ItemList.SlicedCircuit_HighEnergyFlowCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(getModItem(NewHorizonsCoreMod.ID, "item.HighEnergyFlowCircuit", 64, 0)), 1, 0));
        }
    }

    public static void run() {
        HashSet<GTRecipe> toRem = new HashSet<>();
        HashSet<GTRecipe> toAdd = new HashSet<>();
        deleteCALRecipesAndTags();
        rebuildCircuitAssemblerMap(toRem, toAdd);
        exchangeRecipesInList(toRem, toAdd);
        makeCircuitImprintRecipes();
    }

    private static void reAddOriginalRecipes() {
        RecipeMaps.circuitAssemblerRecipes.getBackend()
            .removeRecipes(MODIFIED_CAL_RECIPES);
        ORIGINAL_CAL_RECIPES.forEach(RecipeMaps.circuitAssemblerRecipes::add);
        ORIGINAL_CAL_RECIPES.clear();
        MODIFIED_CAL_RECIPES.clear();
    }

    private static void rebuildCircuitAssemblerMap(HashSet<GTRecipe> toRem, HashSet<GTRecipe> toAdd) {
        reAddOriginalRecipes();
        RecipeMaps.circuitAssemblerRecipes.getAllRecipes()
            .forEach(e -> CircuitImprintLoader.handleCircuitRecipeRebuilding(e, toRem, toAdd));
    }

    private static void handleCircuitRecipeRebuilding(GTRecipe circuitRecipe, HashSet<GTRecipe> toRem,
        HashSet<GTRecipe> toAdd) {
        ItemStack[] outputs = circuitRecipe.mOutputs;
        boolean isOrePass = isCircuitOreDict(outputs[0]);
        String unlocalizedName = outputs[0].getUnlocalizedName();

        boolean isCircuitRecipeCandidate = isOrePass || unlocalizedName.contains("Circuit")
            || unlocalizedName.contains("circuit");

        if (!isCircuitRecipeCandidate) return;

        CircuitImprintLoader.recipeTagMap.put(CircuitImprintLoader.getTagFromStack(outputs[0]), circuitRecipe.copy());
        MainMod.LOGGER.info("treating recipe for item: " + unlocalizedName);

        FluidStack circuitFluidStack = circuitRecipe.mFluidInputs[0];

        FluidStack solderingAlloy = Materials.SolderingAlloy.getMolten(0);
        FluidStack indalloy140 = MaterialsAlloy.INDALLOY_140.getFluidStack(0);
        FluidStack mutatedLivingSolder = MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(0);

        // First compare to usual soldering
        if (circuitFluidStack.isFluidEqual(solderingAlloy) || circuitFluidStack.isFluidEqual(indalloy140)
            || circuitFluidStack.isFluidEqual(mutatedLivingSolder)) {
            // try to deduce the CAL recipe from the original circass recipe
            GTRecipe CALRecipe = CircuitImprintLoader.generateCALRecipe(circuitRecipe);
            MainMod.LOGGER.info("candidate for CAL addition + circass nerf");
            // If the CAL recipe had no blacklisted items for CAL, add it to the CAL recipe map
            if (CALRecipe != null) {
                MainMod.LOGGER.info("adding the recipe to CAL");
                BartWorksRecipeMaps.circuitAssemblyLineRecipes.addRecipe(CALRecipe);
            }

            // heavily nerf the original circass recipe if above IV tier in terms of energy
            if (circuitRecipe.mEUt > TierEU.IV) {
                MainMod.LOGGER.info("nerfed the recipe in the circass");
                toRem.add(circuitRecipe);
                toAdd.add(CircuitImprintLoader.makeMoreExpensive(circuitRecipe));
            }
        }
        // else if it has no usual soldering but above cutoff tier we yeet
        else if (circuitRecipe.mEUt > TierEU.IV) {
            toRem.add(circuitRecipe);
            MainMod.LOGGER.info("yeeting recipe for item: " + unlocalizedName);
        }
        // else do nothing
        else {
            MainMod.LOGGER.info("Doing nothing to the circass recipe for item: " + unlocalizedName);
        }

    }

    private static boolean isCircuitOreDict(ItemStack item) {
        return BWUtil.isTieredCircuit(item) || BWUtil.getOreNames(item)
            .stream()
            .anyMatch(s -> "circuitPrimitiveArray".equals(s));
    }

    private static void exchangeRecipesInList(HashSet<GTRecipe> toRem, HashSet<GTRecipe> toAdd) {
        toAdd.forEach(RecipeMaps.circuitAssemblerRecipes::add);
        RecipeMaps.circuitAssemblerRecipes.getBackend()
            .removeRecipes(toRem);
        ORIGINAL_CAL_RECIPES.addAll(toRem);
        MODIFIED_CAL_RECIPES.addAll(toAdd);
    }

    @SuppressWarnings("deprecation")
    public static GTRecipe makeMoreExpensive(GTRecipe original) {
        GTRecipe newRecipe = original.copy();
        for (ItemStack is : newRecipe.mInputs) {
            if (!BWUtil.isTieredCircuit(is)) {
                is.stackSize = Math.min(is.stackSize * 6, 64);
                if (is.stackSize > is.getItem()
                    .getItemStackLimit() || is.stackSize > is.getMaxStackSize()) is.stackSize = is.getMaxStackSize();
            }
        }
        newRecipe.mFluidInputs[0].amount *= 4;
        newRecipe.mDuration *= 4;
        return newRecipe;
    }

    public static GTRecipe generateCALRecipe(GTRecipe original) {
        ItemStack[] in = new ItemStack[6];
        BiMap<ItemList, Short> inversed = CircuitImprintLoader.circuitIIconRefs.inverse();

        for (int i = 0; i < 6; i++) {
            try {
                replaceCircuits(inversed, original, in, i);
                replaceComponents(in, original, i);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (CircuitImprintLoader.checkForBlacklistedComponents(in)) {
            return null;
        }

        return new GTRecipe(
            false,
            in,
            new ItemStack[] { getOutputMultiplied(original) },
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(CircuitImprintLoader.getTagFromStack(original.mOutputs[0]), 0, 0),
            null,
            original.mFluidInputs,
            null,
            original.mDuration * 12,
            original.mEUt,
            0);
    }

    private static ItemStack getOutputMultiplied(GTRecipe original) {
        ItemStack out = original.copy()
            .getOutput(0);
        out.stackSize *= 16;
        return out;
    }

    private static void replaceCircuits(BiMap<ItemList, Short> inversed, GTRecipe original, ItemStack[] in, int index) {
        for (ItemList il : inversed.keySet()) {
            if (GTUtility.areStacksEqual(il.get(1), replaceCircuitParts(original.mInputs[index]))) {
                in[index] = BWMetaItems.getCircuitParts()
                    .getStack(inversed.get(il), original.mInputs[index].stackSize);
            }
        }
    }

    private static final List<Pair<ItemStack, ItemStack>> circuitPartsToReplace = Collections.unmodifiableList(
        Arrays.asList(
            Pair.of(ItemList.Circuit_Parts_Resistor.get(1), ItemList.Circuit_Parts_ResistorSMD.get(1)),
            Pair.of(ItemList.Circuit_Parts_Diode.get(1), ItemList.Circuit_Parts_DiodeSMD.get(1)),
            Pair.of(ItemList.Circuit_Parts_Transistor.get(1), ItemList.Circuit_Parts_TransistorSMD.get(1)),
            Pair.of(ItemList.Circuit_Parts_Capacitor.get(1), ItemList.Circuit_Parts_CapacitorSMD.get(1)),
            Pair.of(ItemList.Circuit_Parts_Coil.get(1), ItemList.Circuit_Parts_InductorSMD.get(1))));

    private static ItemStack replaceCircuitParts(ItemStack stack) {
        for (Pair<ItemStack, ItemStack> pair : circuitPartsToReplace) {
            if (GTUtility.areStacksEqual(pair.getKey(), stack)) {
                ItemStack newStack = pair.getValue();
                newStack.stackSize = stack.stackSize;
                return newStack;
            }
        }
        return stack;
    }

    @SuppressWarnings("deprecation")
    private static void replaceComponents(ItemStack[] in, GTRecipe original, int index)
        throws ArrayIndexOutOfBoundsException {
        if (original.mInputs[index] != null && in[index] == null) {
            // big wires
            if (BWUtil.checkStackAndPrefix(original.mInputs[index])
                && GTOreDictUnificator.getAssociation(original.mInputs[index]).mPrefix == OrePrefixes.wireGt01) {
                in[index] = GTOreDictUnificator.get(
                    OrePrefixes.wireGt16,
                    GTOreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                    original.mInputs[index].stackSize);
                // fine wires
            } else if (BWUtil.checkStackAndPrefix(original.mInputs[index])
                && GTOreDictUnificator.getAssociation(original.mInputs[index]).mPrefix == OrePrefixes.wireFine) {
                    in[index] = GTOreDictUnificator.get(
                        OrePrefixes.wireGt04,
                        GTOreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                        original.mInputs[index].stackSize);
                    if (in[index] == null) {
                        in[index] = GTOreDictUnificator.get(
                            OrePrefixes.wireFine,
                            GTOreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                            original.mInputs[index].stackSize * 16L);
                    }
                    // other components
                } else {
                    in[index] = original.mInputs[index].copy();
                    in[index].stackSize *= 16;
                    if (in[index].stackSize > in[index].getItem()
                        .getItemStackLimit() || in[index].stackSize > in[index].getMaxStackSize())
                        in[index].stackSize = in[index].getMaxStackSize();
                }
        }
    }

    private static void makeCircuitImprintRecipes() {
        removeOldRecipesFromRegistries();
        CircuitImprintLoader.recipeTagMap.keySet()
            .forEach(e -> {
                makeAndAddCutterRecipe(e);
                makeAndAddCraftingRecipes(e);
            });
    }

    private static boolean checkForBlacklistedComponents(ItemStack[] itemStacks) {
        for (ItemStack is : itemStacks) {
            for (ItemStack is2 : CircuitImprintLoader.blacklistSet) {
                if (GTUtility.areStacksEqual(is, is2)) return true;
            }
        }
        return false;
    }

    private static void removeOldRecipesFromRegistries() {
        recipeWorldCache.forEach(
            CraftingManager.getInstance()
                .getRecipeList()::remove);
        GTModHandler.sBufferRecipeList.removeAll(recipeWorldCache);
        recipeWorldCache.clear();
    }

    private static void makeAndAddCutterRecipe(NBTTagCompound tag) {
        ItemStack stack = CircuitImprintLoader.getStackFromTag(tag);
        long eut = Long.MAX_VALUE;

        for (GTRecipe recipe : CircuitImprintLoader.recipeTagMap.get(tag)) {
            eut = Math.min(eut, recipe.mEUt);
        }
        int tier = BWUtil.getCircuitTierFromOreDictName(
            OreDictionary
                .getOreName(OreDictionary.getOreIDs(stack).length > 0 ? OreDictionary.getOreIDs(stack)[0] : -1));
        eut = Math.min(eut, VP[Math.max(tier, 0)]);
        GTValues.RA.stdBuilder()
            .itemInputs(stack, ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(tag, 1, 1))
            .duration(300)
            .eut(eut)
            .requiresCleanRoom()
            .addTo(cutterRecipes);
    }

    private static void makeAndAddCraftingRecipes(NBTTagCompound tag) {
        ItemStack circuit = BWMetaItems.getCircuitParts()
            .getStackWithNBT(tag, 0, 1);
        Object[] imprintRecipe = { " X ", "GPG", " X ", 'P', BWMetaItems.getCircuitParts()
            .getStackWithNBT(tag, 1, 1), 'G', WerkstoffLoader.Prasiolite.get(OrePrefixes.gemExquisite, 1), 'X',
            BWMetaItems.getCircuitParts()
                .getStack(3) };

        IRecipe bwrecipe = new BWNBTDependantCraftingRecipe(circuit, imprintRecipe);
        ShapedOreRecipe gtrecipe = BWUtil.createGTCraftingRecipe(
            circuit,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.KEEPNBT
                | GTModHandler.RecipeBits.BUFFERED,
            imprintRecipe);

        // Adds the actual recipe
        recipeWorldCache.add(bwrecipe);
        GameRegistry.addRecipe(bwrecipe);
        // Adds the NEI visual recipe
        recipeWorldCache.add(gtrecipe);
        GameRegistry.addRecipe(gtrecipe);
    }

    public static NBTTagCompound getTagFromStack(ItemStack stack) {
        if (GTUtility.isStackValid(stack)) return BWUtil.setStackSize(stack.copy(), 1)
            .writeToNBT(new NBTTagCompound());
        return new NBTTagCompound();
    }

    public static ItemStack getStackFromTag(NBTTagCompound tagCompound) {
        return ItemStack.loadItemStackFromNBT(tagCompound);
    }

    private static void deleteCALRecipesAndTags() {
        BartWorksRecipeMaps.circuitAssemblyLineRecipes.getBackend()
            .clearRecipes();
        recipeTagMap.clear();
    }
}
