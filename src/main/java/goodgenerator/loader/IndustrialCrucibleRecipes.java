package goodgenerator.loader;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.*;

public class IndustrialCrucibleRecipes {
    public static final RecipeMap<RecipeMapBackend> sCrucibleRecipes = RecipeMapBuilder.of("gg.recipe.industrial_crucible")
        .maxIO(12, 1, 0, 0)
        .build();

    public static class CrucibleRecipeInfo {
        public ItemStack catalyst;
        public int circuitID;
        public thaumcraft.api.aspects.AspectList aspects;
        public ItemStack output;
        public String researchKey;
    }

    public static final List<CrucibleRecipeInfo> CRUCIBLE_RECIPES = new ArrayList<>();

    public static void init() {
        loadRecipesFromThaumcraft();
        initMachineRecipes();
    }

    private static void initMachineRecipes() {
        thaumcraft.api.aspects.AspectList crucibleAspects = new thaumcraft.api.aspects.AspectList()
            .add(thaumcraft.api.aspects.Aspect.MECHANISM, 256)
            .add(thaumcraft.api.aspects.Aspect.MAGIC, 256)
            .add(thaumcraft.api.aspects.Aspect.FIRE, 256)
            .add(thaumcraft.api.aspects.Aspect.WATER, 256)
            .add(thaumcraft.api.aspects.Aspect.ORDER, 256)
            .add(thaumcraft.api.aspects.Aspect.EXCHANGE, 256);

        ItemStack circuitIV = gregtech.api.util.GTOreDictUnificator.get(gregtech.api.enums.OrePrefixes.circuit, gregtech.api.enums.Materials.IV, 1L);
        ItemStack rotorKnightmetal = gregtech.api.util.GTOreDictUnificator.get(gregtech.api.enums.OrePrefixes.rotor, gregtech.api.enums.Materials.Knightmetal, 1L);
        ItemStack mnemonicMatrix = new ItemStack(thaumcraft.common.config.ConfigBlocks.blockMetalDevice, 1, 12);

        ItemStack[] crucibleItems = new ItemStack[] {
            circuitIV, circuitIV, circuitIV, circuitIV,
            new ItemStack(thaumcraft.common.config.ConfigBlocks.blockMetalDevice, 1, 0),
            gregtech.api.enums.ItemList.Electric_Pump_IV.get(1),
            rotorKnightmetal,
            new ItemStack(thaumcraft.common.config.ConfigBlocks.blockCrystal, 1, 2),
            new ItemStack(thaumcraft.common.config.ConfigBlocks.blockStoneDevice, 1, 12),
            mnemonicMatrix, mnemonicMatrix, mnemonicMatrix, mnemonicMatrix
        };

        Object controllerRecipe = thaumcraft.api.ThaumcraftApi.addInfusionCraftingRecipe(
            "RESEARCH_IC",
            goodgenerator.util.ItemRefer.Industrial_Crucible.get(1),
            20,
            crucibleAspects,
            gregtech.api.enums.ItemList.Casing_Pipe_TungstenSteel.get(1),
            crucibleItems
        );

        thaumcraft.api.aspects.AspectList hatchAspects = new thaumcraft.api.aspects.AspectList()
            .add(thaumcraft.api.aspects.Aspect.MECHANISM, 64)
            .add(thaumcraft.api.aspects.Aspect.WATER, 128)
            .add(thaumcraft.api.aspects.Aspect.VOID, 32)
            .add(thaumcraft.api.aspects.Aspect.ORDER, 32);

        ItemStack[] hatchItems = new ItemStack[] {
            new ItemStack(thaumcraft.common.config.ConfigBlocks.blockTube, 1, 0),
            goodgenerator.util.ItemRefer.Magic_Casing.get(1),
            new ItemStack(thaumcraft.common.config.ConfigBlocks.blockTube, 1, 1),
            gregtech.api.enums.ItemList.Electric_Pump_HV.get(1)
        };

        Object hatchRecipe = thaumcraft.api.ThaumcraftApi.addInfusionCraftingRecipe(
            "RESEARCH_IC",
            goodgenerator.util.ItemRefer.Essentia_Input_Hatch.get(1),
            6,
            hatchAspects,
            gregtech.api.enums.ItemList.Hatch_Output_HV.get(1),
            hatchItems
        );

        thaumcraft.api.aspects.AspectList researchAspects = new thaumcraft.api.aspects.AspectList()
            .add(thaumcraft.api.aspects.Aspect.MECHANISM, 5)
            .add(thaumcraft.api.aspects.Aspect.MAGIC, 5)
            .add(thaumcraft.api.aspects.Aspect.CRAFT, 4)
            .add(thaumcraft.api.aspects.Aspect.WATER, 4)
            .add(thaumcraft.api.aspects.Aspect.FIRE, 4)
            .add(thaumcraft.api.aspects.Aspect.ORDER, 4);

        thaumcraft.api.research.ResearchItem icResearch = new thaumcraft.api.research.ResearchItem(
            "RESEARCH_IC",
            "ARTIFICE",
            researchAspects,
            -19, 3,
            5,
            goodgenerator.util.ItemRefer.Industrial_Crucible.get(1)
        );

        icResearch.setParents("ESSENTIA_SMELTERY");

        icResearch.setPages(
            new thaumcraft.api.research.ResearchPage("tc.research_page.RESEARCH_IC.1"),
            new thaumcraft.api.research.ResearchPage((thaumcraft.api.crafting.InfusionRecipe) controllerRecipe),
            new thaumcraft.api.research.ResearchPage((thaumcraft.api.crafting.InfusionRecipe) hatchRecipe)
        );

        icResearch.registerResearchItem();
    }
    private static void loadRecipesFromThaumcraft() {
        CRUCIBLE_RECIPES.clear();
        Map<String, Integer> totalOccurrences = new HashMap<>();
        List<thaumcraft.api.crafting.CrucibleRecipe> validRecipes = new ArrayList<>();

        Item aspectItem = GameRegistry.findItem("aspectrecipeindex", "itemAspect");
        if (aspectItem == null) aspectItem = GameRegistry.findItem("aspectrecipeindex", "aspect");

        for (Object obj : thaumcraft.api.ThaumcraftApi.getCraftingRecipes()) {
            if (obj instanceof thaumcraft.api.crafting.CrucibleRecipe) {
                thaumcraft.api.crafting.CrucibleRecipe recipe = (thaumcraft.api.crafting.CrucibleRecipe) obj;
                ItemStack catalyst = recipe.catalyst instanceof ItemStack ? (ItemStack) recipe.catalyst :
                    (recipe.catalyst instanceof List && !((List<?>) recipe.catalyst).isEmpty() ? (ItemStack) ((List<?>) recipe.catalyst).get(0) : null);

                if (catalyst == null || recipe.getRecipeOutput() == null) continue;
                validRecipes.add(recipe);
                String key = catalyst.getUnlocalizedName() + ":" + catalyst.getItemDamage();
                totalOccurrences.put(key, totalOccurrences.getOrDefault(key, 0) + 1);
            }
        }

        Map<String, Integer> currentCounts = new HashMap<>();
        for (thaumcraft.api.crafting.CrucibleRecipe recipe : validRecipes) {
            ItemStack catalyst = recipe.catalyst instanceof ItemStack ? (ItemStack) recipe.catalyst : (ItemStack) ((List<?>) recipe.catalyst).get(0);
            String key = catalyst.getUnlocalizedName() + ":" + catalyst.getItemDamage();
            int total = totalOccurrences.get(key);
            int circuitID = (total > 1) ? currentCounts.getOrDefault(key, 0) + 1 : 0;
            if (total > 1) currentCounts.put(key, circuitID);

            CrucibleRecipeInfo info = new CrucibleRecipeInfo();
            info.catalyst = catalyst.copy();
            info.circuitID = circuitID;
            info.aspects = recipe.aspects.copy();
            info.output = recipe.getRecipeOutput().copy();

            info.researchKey = recipe.key;

            CRUCIBLE_RECIPES.add(info);

            List<ItemStack> neiInputs = new ArrayList<>();
            neiInputs.add(info.catalyst.copy());
            if (circuitID > 0) neiInputs.add(GTUtility.getIntegratedCircuit(circuitID));

            for (Map.Entry<thaumcraft.api.aspects.Aspect, Integer> entry : info.aspects.aspects.entrySet()) {
                if (aspectItem != null && entry.getKey() != null) {
                    ItemStack s = new ItemStack(aspectItem, entry.getValue(), 0);
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setString("Aspect", entry.getKey().getTag());
                    s.setTagCompound(nbt);
                    neiInputs.add(s);
                }
            }
            sCrucibleRecipes.addRecipe(new GTRecipe(false, neiInputs.toArray(new ItemStack[0]), new ItemStack[]{info.output}, null, null, null, null, null, null, null, 100, 480, 0));
        }
    }
}
