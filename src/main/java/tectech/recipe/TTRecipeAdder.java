package tectech.recipe;

import static gregtech.api.recipe.RecipeMaps.assemblylineVisualRecipes;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_STATION_DATA;
import static tectech.recipe.TecTechRecipeMaps.researchStationFakeRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTUtility;
import gregtech.common.RecipeAdder;
import tectech.TecTech;
import tectech.thing.CustomItemList;

public class TTRecipeAdder extends RecipeAdder {

    public static final ItemStack[] nullItem = new ItemStack[0];
    public static final FluidStack[] nullFluid = new FluidStack[0];

    @Deprecated
    public static boolean addResearchableAssemblylineRecipe(ItemStack aResearchItem, int totalComputationRequired,
        int computationRequiredPerSec, int researchEUt, int researchAmperage, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int assDuration, int assEUt) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchItem == null || totalComputationRequired <= 0 || aOutput == null || aInputs.length > 16) {
            return false;
        }
        for (ItemStack tItem : aInputs) {
            if (tItem == null) {
                TecTech.LOGGER.error(
                    "addResearchableAssemblingLineRecipe " + aResearchItem.getDisplayName()
                        + " --> "
                        + aOutput.getUnlocalizedName()
                        + " there is some null item in that recipe");
            }
        }
        researchAmperage = GTUtility.clamp(researchAmperage, 1, Short.MAX_VALUE);
        computationRequiredPerSec = GTUtility.clamp(computationRequiredPerSec, 1, Short.MAX_VALUE);

        GTRecipe.RecipeAssemblyLine recipeGT = new GTRecipe.RecipeAssemblyLine(
            CustomItemList.UnusedStuff.get(1),
            totalComputationRequired / computationRequiredPerSec,
            aInputs,
            aFluidInputs,
            aOutput,
            assDuration,
            assEUt);
        RecipeAssemblyLine recipeTT = new GTRecipe.RecipeAssemblyLine(
            aResearchItem,
            totalComputationRequired / computationRequiredPerSec,
            aInputs,
            aFluidInputs,
            aOutput,
            assDuration,
            assEUt);
        GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes.add(recipeGT);
        TecTechRecipeMaps.researchableALRecipeList.add(recipeTT);

        ItemStack writesDataStick = ItemList.Tool_DataStick.getWithName(1L, "Writes Research result");
        AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(writesDataStick, recipeTT, false);
        GTValues.RA.stdBuilder()
            .itemInputs(aResearchItem)
            .itemOutputs(aOutput)
            .special(writesDataStick)
            .duration(totalComputationRequired)
            .eut(researchEUt)
            .metadata(RESEARCH_STATION_DATA, researchAmperage | computationRequiredPerSec << 16)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(researchStationFakeRecipes);

        ItemStack readsDataStick = ItemList.Tool_DataStick.getWithName(1L, "Reads Research result");
        AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(readsDataStick, recipeTT, false);
        GTValues.RA.stdBuilder()
            .itemInputs(aInputs)
            .itemOutputs(aOutput)
            .fluidInputs(aFluidInputs)
            .special(readsDataStick)
            .duration(assDuration)
            .eut(assEUt)
            .ignoreCollision()
            .fake()
            .addTo(assemblylineVisualRecipes);
        return true;
    }

    @Deprecated
    public static boolean addResearchableAssemblylineRecipe(ItemStack aResearchItem, int totalComputationRequired,
        int computationRequiredPerSec, int researchEUt, int researchAmperage, Object[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int assDuration, int assEUt) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchItem == null || totalComputationRequired <= 0
            || aOutput == null
            || aInputs.length > 16
            || aFluidInputs.length > 4
            || assDuration <= 0
            || assEUt <= 0) {
            return false;
        }

        ItemStack[] tInputs = new ItemStack[aInputs.length];
        ItemStack[][] tAlts = new ItemStack[aInputs.length][];
        int tPersistentHash = 1;
        for (int i = 0; i < aInputs.length; i++) {
            Object obj = aInputs[i];
            if (obj instanceof ItemStack) {
                tInputs[i] = (ItemStack) obj;
                tAlts[i] = null;
                tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(tInputs[i], true, false);
                continue;
            } else if (obj instanceof ItemStack[]aStacks) {
                if (aStacks.length > 0) {
                    tInputs[i] = aStacks[0];
                    tAlts[i] = Arrays.copyOf(aStacks, aStacks.length);
                    for (ItemStack tAlt : tAlts[i]) {
                        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(tAlt, true, false);
                    }
                    tPersistentHash *= 31;
                    continue;
                }
            } else if (obj instanceof Object[]objs) {
                List<ItemStack> tList;
                if (objs.length >= 2 && !(tList = GTOreDictUnificator.getOres(objs[0])).isEmpty()) {
                    try {
                        // sort the output, so the hash code is stable across launches
                        tList.sort(
                            Comparator
                                .<ItemStack, String>comparing(
                                    s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparingInt(Items.feather::getDamage)
                                .thenComparingInt(s -> s.stackSize));
                        int tAmount = ((Number) objs[1]).intValue();
                        List<ItemStack> uList = new ArrayList<>();
                        for (ItemStack tStack : tList) {
                            ItemStack uStack = GTUtility.copyAmount(tAmount, tStack);
                            if (GTUtility.isStackValid(uStack)) {
                                uList.add(uStack);
                                if (tInputs[i] == null) tInputs[i] = uStack;
                            }
                        }
                        tAlts[i] = uList.toArray(new ItemStack[0]);
                        tPersistentHash = tPersistentHash * 31 + (objs[0] == null ? "" : objs[0].toString()).hashCode();
                        tPersistentHash = tPersistentHash * 31 + tAmount;
                        continue;
                    } catch (Exception t) {
                        TecTech.LOGGER.error(
                            "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                                + " --> there is some ... in that recipe");
                    }
                }
            }
            TecTech.LOGGER.error(
                "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                    + " --> "
                    + aOutput.getUnlocalizedName()
                    + " there is some null item in that recipe");
        }
        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(aOutput, true, false);
        for (FluidStack tFluidInput : aFluidInputs) {
            if (tFluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(tFluidInput, true, false);
        }
        researchAmperage = GTUtility.clamp(researchAmperage, 1, Short.MAX_VALUE);
        computationRequiredPerSec = GTUtility.clamp(computationRequiredPerSec, 1, Short.MAX_VALUE);
        tPersistentHash = tPersistentHash * 31 + totalComputationRequired;
        tPersistentHash = tPersistentHash * 31 + computationRequiredPerSec;
        tPersistentHash = tPersistentHash * 31 + researchAmperage;
        tPersistentHash = tPersistentHash * 31 + researchEUt;
        tPersistentHash = tPersistentHash * 31 + assDuration;
        tPersistentHash = tPersistentHash * 31 + assEUt;

        GTRecipe.RecipeAssemblyLine recipeGT = new GTRecipe.RecipeAssemblyLine(
            CustomItemList.UnusedStuff.get(1),
            totalComputationRequired / computationRequiredPerSec,
            tInputs,
            aFluidInputs,
            aOutput,
            assDuration,
            assEUt,
            tAlts);
        recipeGT.setPersistentHash(tPersistentHash);
        GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes.add(recipeGT);
        GTRecipe.RecipeAssemblyLine recipeTT = new GTRecipe.RecipeAssemblyLine(
            aResearchItem,
            totalComputationRequired / computationRequiredPerSec,
            tInputs,
            aFluidInputs,
            aOutput,
            assDuration,
            assEUt,
            tAlts);
        recipeTT.setPersistentHash(tPersistentHash);
        TecTechRecipeMaps.researchableALRecipeList.add(recipeTT);

        ItemStack writesDataStick = ItemList.Tool_DataStick.getWithName(1L, "Writes Research result");
        AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(writesDataStick, recipeTT, false);
        GTValues.RA.stdBuilder()
            .itemInputs(aResearchItem)
            .itemOutputs(aOutput)
            .special(writesDataStick)
            .duration(totalComputationRequired)
            .eut(researchEUt)
            .metadata(RESEARCH_STATION_DATA, researchAmperage | computationRequiredPerSec << 16)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(researchStationFakeRecipes);

        ItemStack readsDataStick = ItemList.Tool_DataStick.getWithName(1L, "Reads Research result");
        AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(readsDataStick, recipeTT, false);
        assemblylineVisualRecipes.addFakeRecipe(
            false,
            tInputs,
            new ItemStack[] { aOutput },
            new ItemStack[] { readsDataStick },
            aFluidInputs,
            null,
            assDuration,
            assEUt,
            0,
            tAlts,
            false);
        return true;
    }
}
