package com.github.technus.tectech.recipe;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.item.ElementalDefinitionContainer_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_crafting;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_RecipeAdder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TT_recipeAdder extends GT_RecipeAdder {
    public static final ItemStack[] nullItem = new ItemStack[0];
    public static final FluidStack[] nullFluid = new FluidStack[0];

    public static boolean addResearchableAssemblylineRecipe(
            ItemStack aResearchItem,
            int totalComputationRequired,
            int computationRequiredPerSec,
            int researchEUt,
            int researchAmperage,
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            ItemStack aOutput,
            int assDuration,
            int assEUt) {
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
                TecTech.LOGGER.error("addResearchableAssemblingLineRecipe " + aResearchItem.getDisplayName() + " --> "
                        + aOutput.getUnlocalizedName() + " there is some null item in that recipe");
            }
        }
        researchAmperage = GT_Utility.clamp(researchAmperage, 1, Short.MAX_VALUE);
        computationRequiredPerSec = GT_Utility.clamp(computationRequiredPerSec, 1, Short.MAX_VALUE);
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {aResearchItem},
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Writes Research result")},
                null,
                null,
                totalComputationRequired,
                researchEUt,
                researchAmperage | computationRequiredPerSec << 16);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
                false,
                aInputs,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Reads Research result")},
                aFluidInputs,
                null,
                assDuration,
                assEUt,
                0,
                true);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(new GT_Recipe.GT_Recipe_AssemblyLine(
                CustomItemList.UnusedStuff.get(1),
                totalComputationRequired / computationRequiredPerSec,
                aInputs,
                aFluidInputs,
                aOutput,
                assDuration,
                assEUt));
        TT_recipe.GT_Recipe_MapTT.sAssemblylineRecipes.add(new GT_Recipe.GT_Recipe_AssemblyLine(
                aResearchItem,
                totalComputationRequired / computationRequiredPerSec,
                aInputs,
                aFluidInputs,
                aOutput,
                assDuration,
                assEUt));
        return true;
    }

    public static boolean addResearchableAssemblylineRecipe(
            ItemStack aResearchItem,
            int totalComputationRequired,
            int computationRequiredPerSec,
            int researchEUt,
            int researchAmperage,
            Object[] aInputs,
            FluidStack[] aFluidInputs,
            ItemStack aOutput,
            int assDuration,
            int assEUt) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchItem == null
                || totalComputationRequired <= 0
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
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tInputs[i], true, false);
                continue;
            } else if (obj instanceof ItemStack[]) {
                ItemStack[] aStacks = (ItemStack[]) obj;
                if (aStacks.length > 0) {
                    tInputs[i] = aStacks[0];
                    tAlts[i] = Arrays.copyOf(aStacks, aStacks.length);
                    for (ItemStack tAlt : tAlts[i]) {
                        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tAlt, true, false);
                    }
                    tPersistentHash *= 31;
                    continue;
                }
            } else if (obj instanceof Object[]) {
                Object[] objs = (Object[]) obj;
                List<ItemStack> tList;
                if (objs.length >= 2 && !(tList = GT_OreDictUnificator.getOres(objs[0])).isEmpty()) {
                    try {
                        // sort the output, so the hash code is stable across launches
                        tList.sort(Comparator.<ItemStack, String>comparing(
                                        s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparingInt(Items.feather::getDamage)
                                .thenComparingInt(s -> s.stackSize));
                        int tAmount = ((Number) objs[1]).intValue();
                        List<ItemStack> uList = new ArrayList<>();
                        for (ItemStack tStack : tList) {
                            ItemStack uStack = GT_Utility.copyAmount(tAmount, tStack);
                            if (GT_Utility.isStackValid(uStack)) {
                                uList.add(uStack);
                                if (tInputs[i] == null) tInputs[i] = uStack;
                            }
                        }
                        tAlts[i] = uList.toArray(new ItemStack[0]);
                        tPersistentHash = tPersistentHash * 31 + (objs[0] == null ? "" : objs[0].toString()).hashCode();
                        tPersistentHash = tPersistentHash * 31 + tAmount;
                        continue;
                    } catch (Exception t) {
                        TecTech.LOGGER.error("addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                                + " --> there is some ... in that recipe");
                    }
                }
            }
            TecTech.LOGGER.error("addAssemblingLineRecipe " + aResearchItem.getDisplayName() + " --> "
                    + aOutput.getUnlocalizedName() + " there is some null item in that recipe");
        }
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
        for (FluidStack tFluidInput : aFluidInputs) {
            if (tFluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tFluidInput, true, false);
        }
        researchAmperage = GT_Utility.clamp(researchAmperage, 1, Short.MAX_VALUE);
        computationRequiredPerSec = GT_Utility.clamp(computationRequiredPerSec, 1, Short.MAX_VALUE);
        tPersistentHash = tPersistentHash * 31 + totalComputationRequired;
        tPersistentHash = tPersistentHash * 31 + computationRequiredPerSec;
        tPersistentHash = tPersistentHash * 31 + researchAmperage;
        tPersistentHash = tPersistentHash * 31 + researchEUt;
        tPersistentHash = tPersistentHash * 31 + assDuration;
        tPersistentHash = tPersistentHash * 31 + assEUt;
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {aResearchItem},
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Writes Research result")},
                null,
                null,
                totalComputationRequired,
                researchEUt,
                researchAmperage | computationRequiredPerSec << 16);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
                false,
                tInputs,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Reads Research result")},
                aFluidInputs,
                null,
                assDuration,
                assEUt,
                0,
                tAlts,
                true);
        GT_Recipe_AssemblyLine recipeGT = new GT_Recipe_AssemblyLine(
                CustomItemList.UnusedStuff.get(1),
                totalComputationRequired / computationRequiredPerSec,
                tInputs,
                aFluidInputs,
                aOutput,
                assDuration,
                assEUt,
                tAlts);
        recipeGT.setPersistentHash(tPersistentHash);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(recipeGT);
        GT_Recipe_AssemblyLine recipeTT = new GT_Recipe_AssemblyLine(
                aResearchItem,
                totalComputationRequired / computationRequiredPerSec,
                tInputs,
                aFluidInputs,
                aOutput,
                assDuration,
                assEUt,
                tAlts);
        recipeTT.setPersistentHash(tPersistentHash);
        TT_recipe.GT_Recipe_MapTT.sAssemblylineRecipes.add(recipeTT);
        return true;
    }

    /*
    (boolean aOptimize,
     ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
     FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue,
     cElementalDefinitionStackMap[] in, cElementalDefinitionStackMap[] out, cElementalDefinitionStackMap[] catalyst, IAdditionalCheck check)
     */

    public static boolean addResearchableEMmachineRecipe(
            ItemStack aResearchItem,
            int totalComputationRequired,
            int computationRequiredPerSec,
            int researchEUt,
            int researchAmperage,
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            EMConstantStackMap[] eInputs,
            ItemStack aOutput,
            int machineDuration,
            int machineEUt,
            int machineAmperage) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchItem == null || totalComputationRequired <= 0 || aOutput == null) {
            return false;
        }
        for (ItemStack tItem : aInputs) {
            if (tItem == null) {
                TecTech.LOGGER.error("addResearchableEMmachineRecipe " + aResearchItem.getDisplayName() + " --> "
                        + aOutput.getUnlocalizedName() + " there is some null item in that recipe");
            }
        }
        if (researchAmperage <= 0) {
            researchAmperage = 1;
        } else if (researchAmperage > Short.MAX_VALUE) {
            researchAmperage = Short.MAX_VALUE;
        }
        if (computationRequiredPerSec <= 0) {
            computationRequiredPerSec = 1;
        } else if (computationRequiredPerSec > Short.MAX_VALUE) {
            computationRequiredPerSec = Short.MAX_VALUE;
        }
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {aResearchItem},
                new ItemStack[] {aOutput},
                new ItemStack[] {
                    ItemList.Tool_DataOrb.getWithName(
                            1L, "Writes Research result for " + GT_MetaTileEntity_EM_machine.machine)
                },
                null,
                null,
                totalComputationRequired,
                researchEUt,
                researchAmperage | computationRequiredPerSec << 16);
        TT_recipe.TT_Recipe_Map.sMachineRecipes.add(new TT_recipe.TT_assLineRecipe(
                false,
                aResearchItem,
                aInputs,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                aFluidInputs,
                machineDuration,
                machineEUt,
                machineAmperage,
                eInputs));
        return true;
    }

    public static boolean addResearchableEMcrafterRecipe(
            ItemStack aResearchItem,
            int totalComputationRequired,
            int computationRequiredPerSec,
            int researchEUt,
            int researchAmperage,
            EMConstantStackMap[] eInputs,
            EMConstantStackMap[] catalyst,
            TT_recipe.IAdditionalCheck check,
            ItemStack aOutput,
            int crafterDuration,
            int crafterEUt,
            int crafterAmperage) {
        if (aResearchItem == null || totalComputationRequired <= 0 || aOutput == null) {
            return false;
        }
        if (researchAmperage <= 0) {
            researchAmperage = 1;
        } else if (researchAmperage > Short.MAX_VALUE) {
            researchAmperage = Short.MAX_VALUE;
        }
        if (computationRequiredPerSec <= 0) {
            computationRequiredPerSec = 1;
        } else if (computationRequiredPerSec > Short.MAX_VALUE) {
            computationRequiredPerSec = Short.MAX_VALUE;
        }
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {aResearchItem},
                new ItemStack[] {aOutput},
                new ItemStack[] {
                    ItemList.Tool_DataOrb.getWithName(
                            1L, "Writes Research result for " + GT_MetaTileEntity_EM_crafting.crafter)
                },
                null,
                null,
                totalComputationRequired,
                researchEUt,
                researchAmperage | computationRequiredPerSec << 16);
        TT_recipe.TT_Recipe_Map.sCrafterRecipes.add(new TT_recipe.TT_assLineRecipe(
                false,
                aResearchItem,
                null,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                null,
                crafterDuration,
                crafterEUt,
                crafterAmperage,
                eInputs,
                null,
                catalyst,
                check));
        return true;
    }

    public static boolean addScannableEMmachineRecipe(
            IEMDefinition aResearchEM,
            int totalComputationRequired,
            int computationRequiredPerSec,
            int researchEUt,
            int researchAmperage,
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            EMConstantStackMap[] eInputs,
            ItemStack aOutput,
            int machineDuration,
            int machineEUt,
            int machineAmperage) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchEM == null || totalComputationRequired <= 0 || aOutput == null) {
            return false;
        }
        for (ItemStack tItem : aInputs) {
            if (tItem == null) {
                TecTech.LOGGER.error("addScannableEMmachineRecipe " + aResearchEM + " --> "
                        + aOutput.getUnlocalizedName() + " there is some null item in that recipe");
            }
        }
        if (researchAmperage <= 0) {
            researchAmperage = 1;
        } else if (researchAmperage > Short.MAX_VALUE) {
            researchAmperage = Short.MAX_VALUE;
        }
        if (computationRequiredPerSec <= 0) {
            computationRequiredPerSec = 1;
        } else if (computationRequiredPerSec > Short.MAX_VALUE) {
            computationRequiredPerSec = Short.MAX_VALUE;
        }
        ItemStack placeholder = new ItemStack(ElementalDefinitionContainer_EM.INSTANCE);
        ElementalDefinitionContainer_EM.setContent(
                placeholder, new EMConstantStackMap(new EMDefinitionStack(aResearchEM, 1)));
        GT_Recipe thisRecipe = TT_recipe.GT_Recipe_MapTT.sScannableFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {placeholder},
                new ItemStack[] {aOutput},
                new ItemStack[] {
                    ItemList.Tool_DataOrb.getWithName(
                            1L, "Writes Research result for " + GT_MetaTileEntity_EM_machine.machine)
                },
                null,
                null,
                totalComputationRequired,
                researchEUt,
                researchAmperage | computationRequiredPerSec << 16);
        TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.add(new TT_recipe.TT_EMRecipe(
                false,
                thisRecipe,
                aResearchEM,
                aInputs,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                aFluidInputs,
                machineDuration,
                machineEUt,
                machineAmperage,
                eInputs));
        return true;
    }

    public static boolean addScannableEMcrafterRecipe(
            IEMDefinition aResearchEM,
            int totalComputationRequired,
            int computationRequiredPerSec,
            int researchEUt,
            int researchAmperage,
            EMConstantStackMap[] eInputs,
            EMConstantStackMap[] catalyst,
            TT_recipe.IAdditionalCheck check,
            ItemStack aOutput,
            int crafterDuration,
            int crafterEUt,
            int crafterAmperage) {
        if (aResearchEM == null || totalComputationRequired <= 0 || aOutput == null) {
            return false;
        }
        if (researchAmperage <= 0) {
            researchAmperage = 1;
        } else if (researchAmperage > Short.MAX_VALUE) {
            researchAmperage = Short.MAX_VALUE;
        }
        if (computationRequiredPerSec <= 0) {
            computationRequiredPerSec = 1;
        } else if (computationRequiredPerSec > Short.MAX_VALUE) {
            computationRequiredPerSec = Short.MAX_VALUE;
        }
        ItemStack placeholder = new ItemStack(ElementalDefinitionContainer_EM.INSTANCE);
        ElementalDefinitionContainer_EM.setContent(
                placeholder, new EMConstantStackMap(new EMDefinitionStack(aResearchEM, 1)));
        GT_Recipe thisRecipe = TT_recipe.GT_Recipe_MapTT.sScannableFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {placeholder},
                new ItemStack[] {aOutput},
                new ItemStack[] {
                    ItemList.Tool_DataOrb.getWithName(
                            1L, "Writes Research result for " + GT_MetaTileEntity_EM_crafting.crafter)
                },
                null,
                null,
                totalComputationRequired,
                researchEUt,
                researchAmperage | computationRequiredPerSec << 16);
        TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM.add(new TT_recipe.TT_EMRecipe(
                false,
                thisRecipe,
                aResearchEM,
                null,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                null,
                crafterDuration,
                crafterEUt,
                crafterAmperage,
                eInputs,
                null,
                catalyst,
                check));
        return true;
    }
}
