package net.glease.ggfab;

import static gregtech.api.enums.ToolDictNames.*;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.glease.ggfab.api.GGFabRecipeMaps;
import net.glease.ggfab.api.GigaGramFabAPI;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

class SingleUseToolRecipeLoader implements Runnable {

    @Override
    public void run() {
        ToolDictNames[] hardTools = new ToolDictNames[] { craftingToolHardHammer, craftingToolScrewdriver,
            craftingToolWrench, craftingToolCrowbar, craftingToolWireCutter, craftingToolFile, craftingToolSaw };
        ToolDictNames[] softTools = new ToolDictNames[] { craftingToolSoftHammer };
        addSingleUseToolRecipe(Materials.Steel, hardTools);
        addSingleUseToolRecipe(Materials.Silver, 5000, hardTools);
        addSingleUseToolRecipe(Materials.VanadiumSteel, hardTools);
        addSingleUseToolRecipe(Materials.TungstenSteel, hardTools);
        addSingleUseToolRecipe(Materials.HSSG, hardTools);
        addSingleUseToolRecipe(Materials.Rubber, softTools);
        addSingleUseToolRecipe(Materials.StyreneButadieneRubber, softTools);
        addSingleUseToolRecipe(Materials.Polybenzimidazole, softTools);

        String prefix = "Shape_One_Use_";
        for (GGItemList value : GGItemList.values()) {
            if (!value.name()
                .startsWith(prefix)) {
                continue;
            }
            ToolDictNames type = ToolDictNames.valueOf(
                value.name()
                    .substring(prefix.length()));
            GT_ModHandler
                .addCraftingRecipe(value.get(1L), new Object[] { "h", "P", "I", 'P', ItemList.Shape_Empty, 'I', type });
        }
    }

    private void addSingleUseToolRecipe(Materials material, ToolDictNames... types) {
        addSingleUseToolRecipe(material, 10000, types);
    }

    private static long findNiceFactor(long fluids, long count) {
        long end = Math.min(fluids, count);
        for (long i = count / 256; i < end; i++) {
            if (fluids % i == 0 && count % i == 0 && count / i < 256) return i;
        }
        return -1;
    }

    private void addSingleUseToolRecipe(Materials material, int outputModifier, ToolDictNames... types) {
        if (material.mStandardMoltenFluid == null) {
            throw new IllegalArgumentException("material does not have molten fluid form");
        }
        for (ToolDictNames type : types) {
            IToolStats stats = GigaGramFabAPI.SINGLE_USE_TOOLS.get(type);
            Long cost = GigaGramFabAPI.COST_SINGLE_USE_TOOLS.get(type);
            if (stats == null || cost == null) {
                throw new IllegalArgumentException(type + " not registered");
            }
            long fluids = cost * GT_Values.L / GT_Values.M, duration = 6 * SECONDS;
            long count = (long) (material.mDurability * stats.getMaxDurabilityMultiplier()
                * outputModifier
                * 100
                / stats.getToolDamagePerContainerCraft()
                / 10000);
            if (count > 64 * 4) {
                long niceFactor = findNiceFactor(fluids, count);
                if (niceFactor < 0) {
                    double mod = (double) count / (64 * 4L);
                    fluids = Math.max((long) (fluids / mod), 1L);
                    duration = Math.max((long) (duration / mod), 1L);
                    count = 64 * 4;
                } else {
                    fluids /= niceFactor;
                    duration = Math.max(duration / niceFactor, 1);
                    count /= niceFactor;
                }
            } else if (count < 128) {
                long mod = GT_Utility.ceilDiv(128, count);
                fluids *= mod;
                duration *= mod;
                count *= mod;
            }
            GT_Values.RA.stdBuilder()
                .fluidInputs(material.getMolten(fluids)) //
                .metadata(GGFabRecipeMaps.OUTPUT_TYPE, type) //
                .metadata(GGFabRecipeMaps.OUTPUT_COUNT, (int) count) //
                .eut(TierEU.RECIPE_MV)
                .duration(duration) //
                .addTo(GGFabRecipeMaps.toolCastRecipes);
        }
    }
}
