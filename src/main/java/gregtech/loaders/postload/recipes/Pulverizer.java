package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.MOD_ID_RC;
import static gregtech.api.enums.ModIDs.HardcoreEnderExpansion;
import static gregtech.api.enums.ModIDs.Railcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class Pulverizer implements Runnable {

    @Override
    public void run() {
        // recycling Long Distance Pipes
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Long_Distance_Pipeline_Fluid.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 19L) },
                null,
                300,
                4);

        GT_Values.RA.addPulveriserRecipe(
                ItemList.Long_Distance_Pipeline_Item.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 7L) },
                null,
                300,
                4);

        GT_Values.RA.addPulveriserRecipe(
                ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Steel, 2L) },
                null,
                10,
                4);

        GT_Values.RA.addPulveriserRecipe(
                ItemList.Long_Distance_Pipeline_Item_Pipe.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tin, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Steel, 1L) },
                null,
                10,
                4);

        // marbe dust( stone dust
        GT_Values.RA.addPulveriserRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Marble, 1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Marble, 1L) },
                null,
                160,
                4);
        GT_Values.RA.addPulveriserRecipe(
                getModItem("Thaumcraft", "ItemResource", 1, 18),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1L) },
                null,
                21,
                4);
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(Items.reeds, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L) },
                null,
                50,
                2);

        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_Cupronickel.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cupronickel, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 2) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_Kanthal.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Kanthal, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cupronickel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 3) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_Nichrome.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nichrome, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Kanthal, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 4) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_TungstenSteel.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TPV, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nichrome, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 5) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_HSSG.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TPV, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 6) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_HSSS.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSS, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 7) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_Naquadah.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSS, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 8) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_NaquadahAlloy.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 9) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_Trinium.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahAlloy, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 10) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_ElectrumFlux.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 11) },
                null,
                1500,
                80);
        GT_Values.RA.addPulveriserRecipe(
                ItemList.Casing_Coil_AwakenedDraconium.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 12) },
                null,
                1500,
                80);

        // recycling RC Tanks
        // Iron
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2L) },
                new int[] { 10000 },
                300,
                2);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                300,
                2);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 3L) },
                new int[] { 10000, 10000 },
                300,
                2);

        // Steel
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 13),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2L) },
                new int[] { 10000 },
                300,
                2);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 14),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                300,
                2);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 15),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Steel, 3L) },
                new int[] { 10000 },
                300,
                2);

        // Aluminium
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 2L) },
                new int[] { 10000 },
                450,
                8);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                450,
                8);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Aluminium, 3L) },
                new int[] { 10000, 10000 },
                450,
                8);

        // Stainless Steel
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 3),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 2L) },
                new int[] { 10000 },
                600,
                16);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 4),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                600,
                16);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 5),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.StainlessSteel, 3L) },
                new int[] { 10000 },
                600,
                16);

        // Titanium
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 6),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 2L) },
                new int[] { 10000 },
                600,
                30);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 7),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                600,
                30);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 8),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Titanium, 3L) },
                new int[] { 10000 },
                600,
                30);

        // Tungesten Steel
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 9),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 2L) },
                new int[] { 10000 },
                600,
                30);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 10),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                600,
                30);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 11),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.TungstenSteel, 3L) },
                new int[] { 10000 },
                600,
                30);

        // Palladium
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 12),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 2L) },
                new int[] { 10000 },
                750,
                64);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 13),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                750,
                64);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 14),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NiobiumTitanium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Chrome, 3L) },
                new int[] { 10000, 10000 },
                750,
                64);

        // Iridium
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 2L) },
                new int[] { 10000 },
                900,
                120);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                900,
                120);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iridium, 3L) },
                new int[] { 10000, 10000 },
                900,
                120);

        // Osmium
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 3),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 2L) },
                new int[] { 10000 },
                1050,
                256);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 4),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                1050,
                256);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 5),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Osmium, 3L) },
                new int[] { 10000, 10000 },
                1050,
                256);

        // Neutronium
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 6),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 2L) },
                new int[] { 10000 },
                1200,
                480);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 7),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L) },
                new int[] { 10000, 10000 },
                1200,
                480);
        GT_Values.RA.addPulveriserRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 8),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neutronium, 3L) },
                new int[] { 10000 },
                1200,
                480);

        if (Railcraft.isModLoaded()) {
            GT_ModHandler.addPulverisationRecipe(
                    getModItem(MOD_ID_RC, "cube.crushed.obsidian", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L),
                    GT_Values.NI,
                    0,
                    true);
        }

        GT_ModHandler.addPulverisationRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockSkyStone", 1L, 32767),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 45),
                GT_Values.NI,
                0,
                false);
        GT_ModHandler.addPulverisationRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockSkyChest", 1L, 32767),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 8L, 45),
                GT_Values.NI,
                0,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Items.blaze_rod, 1),
                new ItemStack(Items.blaze_powder, 3),
                new ItemStack(Items.blaze_powder, 1),
                50,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Blocks.web, 1, 0),
                new ItemStack(Items.string, 1),
                new ItemStack(Items.string, 1),
                50,
                false);
        GT_ModHandler
                .addPulverisationRecipe(new ItemStack(Blocks.red_mushroom, 1, 32767), ItemList.IC2_Grin_Powder.get(1L));
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Items.item_frame, 1, 32767),
                new ItemStack(Items.leather, 1),
                GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 4L),
                95,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Items.bow, 1, 0),
                new ItemStack(Items.string, 3),
                GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 3L),
                95,
                false);
        GT_ModHandler.addPulverisationRecipe(Materials.Brick.getIngots(1), Materials.Brick.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Blocks.brick_stairs, 1, 0), Materials.Brick.getDustSmall(6));
        GT_ModHandler.addPulverisationRecipe(ItemList.CompressedFireclay.get(1), Materials.Fireclay.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Firebrick.get(1), Materials.Brick.getDust(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Casing_Firebricks.get(1), Materials.Brick.getDust(4));
        GT_ModHandler.addPulverisationRecipe(
                ItemList.Machine_Bricked_BlastFurnace.get(1),
                Materials.Brick.getDust(8),
                Materials.Iron.getDust(1),
                true);

        if (HardcoreEnderExpansion.isModLoaded()) {
            GT_ModHandler.addPulverisationRecipe(
                    getModItem("HardcoreEnderExpansion", "endium_ore", 1),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1),
                    50,
                    GT_Values.NI,
                    0,
                    true);
        }
    }
}
