package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.GTPlusPlus;
import static gregtech.api.enums.ModIDs.HardcoreEnderExpansion;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;

public class ForgeHammerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stonebrick, 1, 0),
                new ItemStack(Blocks.stonebrick, 1, 2),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stone, 1, 0),
                new ItemStack(Blocks.cobblestone, 1, 0),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.cobblestone, 1, 0),
                new ItemStack(Blocks.gravel, 1, 0),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.gravel, 1, 0), new ItemStack(Blocks.sand, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.sandstone, 1, 32767),
                new ItemStack(Blocks.sand, 1, 0),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.ice, 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.packed_ice, 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 2L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.brick_block, 1, 0),
                new ItemStack(Items.brick, 3, 0),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.nether_brick, 1, 0),
                new ItemStack(Items.netherbrick, 3, 0),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stained_glass, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.glass, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L),
                10,
                10);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stained_glass_pane, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.glass_pane, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(Materials.Brick.getIngots(1), Materials.Brick.getDustSmall(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Firebrick.get(1), Materials.Brick.getDust(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Casing_Firebricks.get(1), ItemList.Firebrick.get(3), 10, 16);

        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack[] { ItemList.Tesseract.get(1L), getModItem(GTPlusPlus.modID, "MU-metaitem.01", 1, 32105) },
                new FluidStack[] { Materials.SpaceTime.getMolten(2880L) },
                null,
                new FluidStack[] { Materials.Space.getMolten(1440L), Materials.Time.getMolten(1440L) },
                10 * 20,
                (int) TierEU.RECIPE_UXV);

        if (HardcoreEnderExpansion.isModLoaded()) {
            GT_Values.RA.addForgeHammerRecipe(
                    getModItem(HardcoreEnderExpansion.modID, "endium_ore", 1),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 1),
                    16,
                    10);
        }
    }
}
