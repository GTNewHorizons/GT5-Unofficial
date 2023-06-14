package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class Pulverizer implements Runnable {

    @Override
    public void run() {
        // recycling Long Distance Pipes
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 19))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 7))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Steel, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * TICKS)
                .eut(4)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tin, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Steel, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * TICKS)
                .eut(4)
                .addTo(sMaceratorRecipes);
        }

        // marbe dust( stone dust
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Marble, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Marble, 1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(8 * SECONDS)
            .eut(4)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Thaumcraft.ID, "ItemResource", 1, 18))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(4)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Cupronickel.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cupronickel, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 2))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Kanthal.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Kanthal, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cupronickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 3))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Nichrome.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nichrome, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Kanthal, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 4))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_TungstenSteel.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TPV, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nichrome, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 5))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSG.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TPV, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 6))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSS.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSS, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 7))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Naquadah.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSS, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 8))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_NaquadahAlloy.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahAlloy, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 9))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Trinium.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahAlloy, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 10))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_ElectrumFlux.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 11))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_AwakenedDraconium.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 12))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .addTo(sMaceratorRecipes);

        if (Railcraft.isModLoaded()) {
            // recycling RC Tanks
            // Iron

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            // Steel

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Steel, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            // Aluminium

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(8)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(8)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Aluminium, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(8)
                .addTo(sMaceratorRecipes);

            // Stainless Steel

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(16)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(16)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.StainlessSteel, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(16)
                .addTo(sMaceratorRecipes);

            // Titanium

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(30)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(30)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Titanium, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(30)
                .addTo(sMaceratorRecipes);

            // Tungesten Steel

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(30)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(30)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.TungstenSteel, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(30)
                .addTo(sMaceratorRecipes);

            // Palladium

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(64)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(64)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NiobiumTitanium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Chrome, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(64)
                .addTo(sMaceratorRecipes);

            // Iridium

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iridium, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sMaceratorRecipes);

            // Osmium

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(256)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(256)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Osmium, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(256)
                .addTo(sMaceratorRecipes);

            // Neutronium

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 2))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neutronium, 3))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "cube.crushed.obsidian", 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
        }

        if (AppliedEnergistics2.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(AppliedEnergistics2.ID, "tile.BlockSkyStone", 1L, 32767))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 45))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(AppliedEnergistics2.ID, "tile.BlockSkyChest", 1L, 32767))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8L, 45))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.blaze_rod, 1))
            .itemOutputs(new ItemStack(Items.blaze_powder, 3), new ItemStack(Items.blaze_powder, 1))
            .outputChances(10000, 5000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.web, 1, 0))
            .itemOutputs(new ItemStack(Items.string, 1), new ItemStack(Items.string, 1))
            .outputChances(10000, 5000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_mushroom, 1, 32767))
            .itemOutputs(ItemList.IC2_Grin_Powder.get(1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.item_frame, 1, 32767))
            .itemOutputs(
                new ItemStack(Items.leather, 1),
                GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 4L))
            .outputChances(10000, 9500)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bow, 1, 0))
            .itemOutputs(
                new ItemStack(Items.string, 3),
                GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 3))
            .outputChances(10000, 9500)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Brick.getIngots(1))
            .itemOutputs(Materials.Brick.getDustSmall(1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brick_stairs, 1, 0))
            .itemOutputs(Materials.Brick.getDustSmall(6))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.CompressedFireclay.get(1))
            .itemOutputs(Materials.Fireclay.getDustSmall(1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Firebrick.get(1))
            .itemOutputs(Materials.Brick.getDust(1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Firebricks.get(1))
            .itemOutputs(Materials.Brick.getDust(4))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Machine_Bricked_BlastFurnace.get(1))
            .itemOutputs(Materials.Brick.getDust(8), Materials.Iron.getDust(1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sMaceratorRecipes);

        if (HardcoreEnderExpansion.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(HardcoreEnderExpansion.ID, "endium_ore", 1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1))
                .outputChances(10000, 5000)
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
        }
    }
}
