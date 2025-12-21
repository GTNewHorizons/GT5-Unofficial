package gregtech.loaders.postload;

import static gregtech.api.util.GTRecipeConstants.FOUNDRY_MODULE;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModules;
import tectech.thing.block.BlockGodforgeGlass;

public class FoundryFakeModuleCostLoader {

    public static void load() {
        // todo put the rest of the modules here once their structures are finalized
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SpaceTime, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 8),
                new ItemStack(BlockGodforgeGlass.INSTANCE, 11),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 24),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Mellion, 24),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Creon, 24),
                ItemList.Harmonic_Reinforcement_ExoFoundry.get(44))
            .itemOutputs(ItemList.Harmonic_Reinforcement_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModules.HARMONIC_REINFORCEMENT)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);
    }
}
