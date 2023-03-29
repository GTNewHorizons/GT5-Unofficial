package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.Forestry;
import static gregtech.api.enums.ModIDs.NotEnoughItems;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class CuttingRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addCutterRecipe(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 4L) },
                400,
                (int) TierEU.RECIPE_LV,
                false);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot2.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer2.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 8L) },
                800,
                (int) TierEU.RECIPE_MV,
                true);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot3.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer3.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 16L) },
                1600,
                (int) TierEU.RECIPE_HV,
                true);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot4.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer4.get(64), ItemList.Circuit_Silicon_Wafer4.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32L) },
                2400,
                (int) TierEU.RECIPE_EV,
                true);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot5.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer5.get(64), ItemList.Circuit_Silicon_Wafer5.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64L) },
                3200,
                (int) TierEU.RECIPE_IV,
                true);

        GT_Values.RA.addCutterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Graphite, 9L),
                GT_Values.NI,
                500,
                48);

        GT_Values.RA.addCutterRecipe(
                getModItem("BuildCraft|Transport", "item.buildcraftPipe.pipestructurecobblestone", 1L, 0),
                getModItem("BuildCraft|Transport", "pipePlug", 8L, 0),
                GT_Values.NI,
                32,
                16);
        for (int i = 0; i < 16; i++) {
            GT_Values.RA.addCutterRecipe(
                    new ItemStack(Blocks.stained_glass, 3, i),
                    new ItemStack(Blocks.stained_glass_pane, 8, i),
                    GT_Values.NI,
                    50,
                    8);
        }
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.glass, 3, 0),
                new ItemStack(Blocks.glass_pane, 8, 0),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                getModItem("TConstruct", "GlassBlock", 3L, 0),
                getModItem("TConstruct", "GlassPane", 8L, 0),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.stone, 1, 0),
                new ItemStack(Blocks.stone_slab, 2, 0),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.stone_slab, 2, 1),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.cobblestone, 1, 0),
                new ItemStack(Blocks.stone_slab, 2, 3),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.brick_block, 1, 0),
                new ItemStack(Blocks.stone_slab, 2, 4),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.stonebrick, 1, 0),
                new ItemStack(Blocks.stone_slab, 2, 5),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.nether_brick, 1, 0),
                new ItemStack(Blocks.stone_slab, 2, 6),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.quartz_block, 1, 32767),
                new ItemStack(Blocks.stone_slab, 2, 7),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.glowstone, 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glowstone, 4L),
                GT_Values.NI,
                100,
                16);

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            GT_Values.RA.addCutterRecipe(
                    new ItemStack(Blocks.wool, 1, i),
                    new ItemStack(Blocks.carpet, 2, i),
                    GT_Values.NI,
                    50,
                    8);
        }
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 0),
                ItemList.Plank_Oak.get(2L),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 1),
                ItemList.Plank_Spruce.get(2L),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 2),
                ItemList.Plank_Birch.get(2L),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 3),
                ItemList.Plank_Jungle.get(2L),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 4),
                ItemList.Plank_Acacia.get(2L),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 5),
                ItemList.Plank_DarkOak.get(2L),
                GT_Values.NI,
                50,
                8);

        ItemStack[] coverIDs = { ItemList.Plank_Larch.get(2L), ItemList.Plank_Teak.get(2L),
                ItemList.Plank_Acacia_Green.get(2L), ItemList.Plank_Lime.get(2L), ItemList.Plank_Chestnut.get(2L),
                ItemList.Plank_Wenge.get(2L), ItemList.Plank_Baobab.get(2L), ItemList.Plank_Sequoia.get(2L),
                ItemList.Plank_Kapok.get(2L), ItemList.Plank_Ebony.get(2L), ItemList.Plank_Mahagony.get(2L),
                ItemList.Plank_Balsa.get(2L), ItemList.Plank_Willow.get(2L), ItemList.Plank_Walnut.get(2L),
                ItemList.Plank_Greenheart.get(2L), ItemList.Plank_Cherry.get(2L), ItemList.Plank_Mahoe.get(2L),
                ItemList.Plank_Poplar.get(2L), ItemList.Plank_Palm.get(2L), ItemList.Plank_Papaya.get(2L),
                ItemList.Plank_Pine.get(2L), ItemList.Plank_Plum.get(2L), ItemList.Plank_Maple.get(2L),
                ItemList.Plank_Citrus.get(2L) };
        int i = 0;
        for (ItemStack cover : coverIDs) {
            if (Forestry.isModLoaded()) {
                ItemStack slabWood = getModItem(Forestry.modID, "slabs", 1, i);
                ItemStack slabWoodFireproof = getModItem(Forestry.modID, "slabsFireproof", 1, i);
                GT_ModHandler.addCraftingRecipe(
                        cover,
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                        new Object[] { "s ", " P", 'P', slabWood });
                GT_ModHandler.addCraftingRecipe(
                        cover,
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                        new Object[] { "s ", " P", 'P', slabWoodFireproof });
                GT_Values.RA.addCutterRecipe(slabWood, cover, null, 40, 8);
                GT_Values.RA.addCutterRecipe(slabWoodFireproof, cover, null, 40, 8);
            } else if (NotEnoughItems.isModLoaded()) {
                API.hideItem(cover);
            }
            i++;
        }
    }
}
