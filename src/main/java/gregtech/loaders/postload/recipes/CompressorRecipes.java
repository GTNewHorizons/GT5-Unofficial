package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator.sapling_Rainforest;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

public class CompressorRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(ItemList.Cell_Air.get(1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 4))
            .itemOutputs(new ItemStack(Blocks.sandstone, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("iridiumShard", 9))
            .itemOutputs(GTModHandler.getIC2Item("iridiumOre", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.clay_ball, 4))
            .itemOutputs(new ItemStack(Blocks.clay, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.brick, 4))
            .itemOutputs(new ItemStack(Blocks.brick_block, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.netherbrick, 4))
            .itemOutputs(new ItemStack(Blocks.nether_brick, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("smallPlutonium", 9))
            .itemOutputs(GTModHandler.getIC2Item("Plutonium", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("smallUran235", 9))
            .itemOutputs(GTModHandler.getIC2Item("Uran235", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.water_bucket, 1))
            .itemOutputs(new ItemStack(Blocks.snow, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.snowball, 4))
            .itemOutputs(new ItemStack(Blocks.snow, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Water.get(1))
            .itemOutputs(new ItemStack(Blocks.snow, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.snow, 1))
            .itemOutputs(new ItemStack(Blocks.ice, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("carbonMesh", 1))
            .itemOutputs(GTModHandler.getIC2Item("carbonPlate", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("coalBall", 1))
            .itemOutputs(ItemList.IC2_Compressed_Coal_Ball.get(1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 8L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(sapling_Rainforest, 8))
            .itemOutputs(ItemList.IC2_Plantball.get(1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Compressed_Coal_Chunk.get(1))
            .itemOutputs(ItemList.IC2_Industrial_Diamond.get(1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1))
            .itemOutputs(GTModHandler.getIC2Item("Uran238", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1))
            .itemOutputs(GTModHandler.getIC2Item("Uran235", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1))
            .itemOutputs(GTModHandler.getIC2Item("Plutonium", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1))
            .itemOutputs(GTModHandler.getIC2Item("smallUran235", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1))
            .itemOutputs(GTModHandler.getIC2Item("smallPlutonium", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.ice, 2, 32767))
            .itemOutputs(new ItemStack(Blocks.packed_ice, 1, 0))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1))
            .itemOutputs(new ItemStack(Blocks.ice, 1, 0))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 4))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "tile.BlockQuartz", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8L, 10))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "tile.BlockQuartz", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8L, 11))
            .itemOutputs(new ItemStack(Blocks.quartz_block, 1, 0))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8L, 12))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "tile.BlockFluix", 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.quartz, 4, 0))
            .itemOutputs(new ItemStack(Blocks.quartz_block, 1, 0))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        /*
         * GT_Values.RA.stdBuilder() .itemInputs( new ItemStack(Items.wheat, 9, 0) ) .itemOutputs( new
         * ItemStack(Blocks.hay_block, 1, 0) ) .duration(15 * SECONDS) .eut(2)
         * .addTo(sCompressorRecipes);
         */

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4))
            .itemOutputs(new ItemStack(Blocks.glowstone, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Fireclay.getDust(1))
            .itemOutputs(ItemList.CompressedFireclay.get(1))
            .duration(4 * SECONDS)
            .eut(4)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.gem, 9))
            .itemOutputs(WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.block, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(RailcraftToolItems.getCoalCoke(9))
                .itemOutputs(EnumCube.COKE_BLOCK.getItem())
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }
    }
}
