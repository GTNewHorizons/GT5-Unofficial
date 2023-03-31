package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class CompressorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(GTPlusPlus.modID, "blockRainforestOakSapling", 8, 0)
            )
            .itemOutputs(
                ItemList.IC2_Plantball.get(1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.IC2_Compressed_Coal_Chunk.get(1)
            )
            .itemOutputs(
                ItemList.IC2_Industrial_Diamond.get(1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("Uran238", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("Uran235", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("Plutonium", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("smallUran235", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("smallPlutonium", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.ice, 2, 32767)
            )
            .itemOutputs(
                new ItemStack(Blocks.packed_ice, 1, 0)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1)
            )
            .itemOutputs(
                new ItemStack(Blocks.ice, 1, 0)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 4)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "tile.BlockQuartz", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 8L, 10)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "tile.BlockQuartz", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 8L, 11)
            )
            .itemOutputs(
                new ItemStack(Blocks.quartz_block, 1, 0)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 8L, 12)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "tile.BlockFluix", 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.quartz, 4, 0)
            )
            .itemOutputs(
                new ItemStack(Blocks.quartz_block, 1, 0)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

/*        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.wheat, 9, 0)
            )
            .itemOutputs(
                new ItemStack(Blocks.hay_block, 1, 0)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);
 */

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4)
            )
            .itemOutputs(
                new ItemStack(Blocks.glowstone, 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Fireclay.getDust(1)
            )
            .itemOutputs(
                ItemList.CompressedFireclay.get(1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(4 * SECONDS)
            .eut(4)
            .addTo(sCompressorRecipes);

        if (Railcraft.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    RailcraftToolItems.getCoalCoke(9)
                )
                .itemOutputs(
                    EnumCube.COKE_BLOCK.getItem()
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sCompressorRecipes);
        }
    }
}
