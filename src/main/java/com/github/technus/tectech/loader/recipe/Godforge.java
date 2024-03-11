package com.github.technus.tectech.loader.recipe;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.recipe.TT_recipeAdder;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.material.ELEMENT;

public class Godforge implements Runnable {

    public static final ArrayList<Materials> plasmaGTMaterialList = new ArrayList<>();
    public static final ArrayList<Integer> plasmaGTWeightList = new ArrayList<>();
    public static final HashMap<ItemStack, Integer> exoticModulePlasmaItemMap = new HashMap<>();
    public static final HashMap<FluidStack, Integer> exoticModulePlasmaFluidMap = new HashMap<>();
    public static final HashMap<ItemStack, Integer> exoticModuleMagmatterItemMap = new HashMap<>();
    public static final HashMap<FluidStack, Integer> exoticModuleMagmatterFluidMap = new HashMap<>();

    @Override
    public void run() {

        if (GTPlusPlus.isModLoaded()) {
            // Solid to plasma recipes
            {
                // Fusion tier 1-3
                {
                    // Single step
                    ItemStack[] solids_t0_1step = { Materials.Aluminium.getDust(1), Materials.Iron.getDust(1) };
                    FluidStack[] solid_plasmas_t0_1step = { Materials.Aluminium.getPlasma(144),
                            Materials.Iron.getPlasma(144) };

                    for (int i = 0; i < solids_t0_1step.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new ItemStack[] { solids_t0_1step[i] },
                                new FluidStack[] { solid_plasmas_t0_1step[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                false,
                                0);
                    }

                    // Multi-step
                    ItemStack[] solids_t0_xstep = { Materials.Force.getDust(1), Materials.Bismuth.getDust(1),
                            ELEMENT.STANDALONE.ADVANCED_NITINOL.getDust(1) };
                    FluidStack[] solid_plasmas_t0_xstep = { new FluidStack(ELEMENT.STANDALONE.FORCE.getPlasma(), 144),
                            Materials.Bismuth.getPlasma(144),
                            new FluidStack(ELEMENT.STANDALONE.ADVANCED_NITINOL.getPlasma(), 144) };

                    for (int i = 0; i < solids_t0_xstep.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new ItemStack[] { solids_t0_xstep[i] },
                                new FluidStack[] { solid_plasmas_t0_xstep[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                true,
                                0);
                    }
                }
                // Fusion tier 4-5
                {
                    // Single step
                    ItemStack[] solids_t1_1step = { Materials.Lead.getDust(1) };
                    FluidStack[] solid_plasmas_t1_1step = { Materials.Lead.getPlasma(144) };

                    for (int i = 0; i < solids_t1_1step.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new ItemStack[] { solids_t1_1step[i] },
                                new FluidStack[] { solid_plasmas_t1_1step[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                false,
                                1);
                    }

                    // Multi-step
                    ItemStack[] solids_t1_xstep = { ELEMENT.getInstance().NEPTUNIUM.getDust(1),
                            ELEMENT.getInstance().FERMIUM.getDust(1) };
                    FluidStack[] solid_plasmas_t1_xstep = {
                            new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 144),
                            new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 144) };

                    for (int i = 0; i < solids_t1_xstep.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new ItemStack[] { solids_t1_xstep[i] },
                                new FluidStack[] { solid_plasmas_t1_xstep[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                true,
                                1);
                    }
                }
                // Exotic Plasmas
                {
                    // Single step
                    ItemStack[] solids_t2_1step = { ELEMENT.STANDALONE.RHUGNOR.getDust(1) };
                    FluidStack[] solid_plasmas_t2_1step = {
                            new FluidStack(ELEMENT.STANDALONE.RHUGNOR.getPlasma(), 144) };

                    for (int i = 0; i < solids_t2_1step.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new ItemStack[] { solids_t2_1step[i] },
                                new FluidStack[] { solid_plasmas_t2_1step[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                false,
                                2);
                    }

                    // Multi-step
                    ItemStack[] solids_t2_xstep = { ELEMENT.STANDALONE.HYPOGEN.getDust(1),
                            Materials.Tritanium.getDust(1) };
                    FluidStack[] solid_plasmas_t2_xstep = { new FluidStack(ELEMENT.STANDALONE.HYPOGEN.getPlasma(), 144),
                            Materials.Tritanium.getPlasma(144) };

                    for (int i = 0; i < solids_t2_xstep.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new ItemStack[] { solids_t2_xstep[i] },
                                new FluidStack[] { solid_plasmas_t2_xstep[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                true,
                                2);
                    }

                }

            }

            // Fluid to plasma recipes
            {
                // Fusion tier 1-3
                {
                    // Single step
                    FluidStack[] fluids_t0_1step = { Materials.Helium.getGas(1000) };
                    FluidStack[] fluid_plasmas_t0_1step = { Materials.Helium.getPlasma(1000) };

                    for (int i = 0; i < fluids_t0_1step.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new FluidStack[] { fluids_t0_1step[i] },
                                new FluidStack[] { fluid_plasmas_t0_1step[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                false,
                                0);
                    }

                    // Multi-step
                    FluidStack[] fluids_t0_xstep = { ELEMENT.getInstance().NEON.getFluidStack(1000) };
                    FluidStack[] fluid_plasmas_t0_xstep = {
                            new FluidStack(ELEMENT.getInstance().NEON.getPlasma(), 1000) };

                    for (int i = 0; i < fluids_t0_xstep.length; i++) {
                        TT_recipeAdder.addFOGPlasmaRecipe(
                                new FluidStack[] { fluids_t0_xstep[i] },
                                new FluidStack[] { fluid_plasmas_t0_xstep[i] },
                                1 * SECONDS,
                                (int) TierEU.RECIPE_MAX,
                                true,
                                0);
                    }
                }
                // Fusion tier 4-5
                {
                    // Single step
                    // None yet

                    // Multi-step
                    // None yet
                }
                // Exotic
                {
                    // None yet
                }
            }

            // Exotic module fake recipes
            {
                TT_recipeAdder.addFOGExoticFakeRecipe(
                        new Object[] { Materials.Iron.getDust(1), Materials.Bismuth.getDust(1),
                                Materials.Tritanium.getDust(1) },
                        new Object[] { Materials.Helium.getGas(1000), ELEMENT.getInstance().NEON.getFluidStack(1000),
                                ELEMENT.getInstance().XENON.getFluidStack(1000) },
                        new FluidStack[] { Materials.Titanium.getMolten(1000) },
                        10 * SECONDS,
                        (int) TierEU.RECIPE_MAX,
                        1);
            }
        }

        // Exotic module plasma material maps
        // GT materials
        plasmaGTMaterialList.addAll(
                Arrays.asList(
                        Materials.Aluminium,
                        Materials.Americium,
                        Materials.Antimony,
                        Materials.Ardite,
                        Materials.Argon,
                        Materials.Arsenic,
                        Materials.Barium,
                        Materials.Beryllium,
                        Materials.Bismuth,
                        Materials.Boron,
                        Materials.Caesium,
                        Materials.Calcium,
                        Materials.Cadmium,
                        Materials.Carbon,
                        Materials.Cerium,
                        Materials.Chlorine,
                        Materials.Chrome,
                        Materials.Cobalt,
                        Materials.Copper,
                        Materials.Desh,
                        Materials.Deuterium,
                        Materials.Dysprosium,
                        Materials.Erbium,
                        Materials.Europium,
                        Materials.Fluorine,
                        Materials.Gadolinium,
                        Materials.Gallium,
                        Materials.Gold,
                        Materials.Helium,
                        Materials.Holmium,
                        Materials.Hydrogen,
                        Materials.Indium,
                        Materials.Iridium,
                        Materials.Iron,
                        Materials.Lanthanum,
                        Materials.Lead,
                        Materials.Lithium,
                        Materials.Lutetium,
                        Materials.Magnesium,
                        Materials.Manganese,
                        Materials.MeteoricIron,
                        Materials.Molybdenum,
                        Materials.Naquadah,
                        Materials.Neodymium,
                        Materials.Nickel,
                        Materials.Niobium,
                        Materials.Nitrogen,
                        Materials.Oriharukon,
                        Materials.Osmium,
                        Materials.Oxygen,
                        Materials.Palladium,
                        Materials.Phosphorus,
                        Materials.Platinum,
                        Materials.Plutonium,
                        Materials.Plutonium241,
                        Materials.Potassium,
                        Materials.Praseodymium,
                        Materials.Promethium,
                        Materials.Radon,
                        Materials.Rubidium,
                        Materials.Samarium,
                        Materials.Silicon,
                        Materials.Silver,
                        Materials.Sodium,
                        Materials.Strontium,
                        Materials.Sulfur,
                        Materials.Tantalum,
                        Materials.Tellurium,
                        Materials.Terbium,
                        Materials.Thorium,
                        Materials.Thulium,
                        Materials.Tin,
                        Materials.Titanium,
                        Materials.Tritium,
                        Materials.Tungsten,
                        Materials.Uranium235,
                        Materials.Uranium,
                        Materials.Vanadium,
                        Materials.Ytterbium,
                        Materials.Yttrium,
                        Materials.Zinc,
                        Materials.Flerovium));

        plasmaGTWeightList.addAll(
                Arrays.asList(
                        6000 /* Aluminium */,
                        10000 /* Americium */,
                        6000 /* Antimony */,
                        6000 /* Ardite */,
                        6000 /* Argon */,
                        6000 /* Arsenic */,
                        6000 /* Barium */,
                        6000 /* Beryllium */,
                        8000 /* Bismuth */,
                        8000 /* Boron */,
                        6000 /* Caesium */,
                        10000 /* Calcium */,
                        6000 /* Cadmium */,
                        6000 /* Carbon */,
                        6000 /* Cerium */,
                        6000 /* Chlorine */,
                        6000 /* Chrome */,
                        6000 /* Cobalt */,
                        6000 /* Copper */,
                        6000 /* Desh */,
                        6000 /* Deuterium */,
                        2000 /* Dysprosium */,
                        2000 /* Erbium */,
                        6000 /* Europium */,
                        6000 /* Fluorine */,
                        2000 /* Gadolinium */,
                        6000 /* Gallium */,
                        6000 /* Gold */,
                        10000 /* Helium */,
                        6000 /* Holmium */,
                        10000 /* Hydrogen */,
                        6000 /* Indium */,
                        6000 /* Iridium */,
                        10000 /* Iron */,
                        6000 /* Lanthanum */,
                        500 /* Lead */,
                        6000 /* Lithium */,
                        6000 /* Lutetium */,
                        6000 /* Magnesium */,
                        6000 /* Manganese */,
                        6000 /* Meteoric Iron */,
                        6000 /* Molybdenum */,
                        6000 /* Naquadah */,
                        6000 /* Neodymium */,
                        10000 /* Nickel */,
                        10000 /* Niobium */,
                        10000 /* Nitrogen */,
                        6000 /* Oriharukon */,
                        6000 /* Osmium */,
                        8000 /* Oxygen */,
                        6000 /* Palladium */,
                        6000 /* Phosphorus */,
                        6000 /* Platinum */,
                        6000 /* Plutonium 239 */,
                        500 /* Plutonium 241 */,
                        6000 /* Potassium */,
                        6000 /* Praseodymium */,
                        2000 /* Promethium */,
                        10000 /* Radon */,
                        2000 /* Rubidium */,
                        6000 /* Samarium */,
                        6000 /* Raw Silicon */,
                        10000 /* Silver */,
                        6000 /* Sodium */,
                        2000 /* Strontium */,
                        10000 /* Sulfur */,
                        6000 /* Tantalum */,
                        2000 /* Tellurium */,
                        1000 /* Terbium */,
                        500 /* Thorium */,
                        6000 /* Thulium */,
                        10000 /* Tin */,
                        10000 /* Titanium */,
                        6000 /* Tritium */,
                        6000 /* Tungsten */,
                        6000 /* Uranium 235 */,
                        6000 /* Uranium 238 */,
                        6000 /* Vanadium */,
                        2000 /* Ytterbium */,
                        6000 /* Yttrium */,
                        6000 /* Zinc */,
                        1000 /* Flerovium */ ));

        // GT++ materials
        if (GTPlusPlus.isModLoaded()) {
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().ZIRCONIUM.getTinyDust(1), 6000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().THORIUM232.getTinyDust(1), 6000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().GERMANIUM.getTinyDust(1), 2000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().THALLIUM.getTinyDust(1), 2000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().RUTHENIUM.getTinyDust(1), 6000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().RHENIUM.getTinyDust(1), 2000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().RHODIUM.getTinyDust(1), 6000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().IODINE.getTinyDust(1), 6000);
            exoticModulePlasmaItemMap.put(ELEMENT.STANDALONE.ASTRAL_TITANIUM.getTinyDust(1), 8000);
            exoticModulePlasmaItemMap.put(ELEMENT.STANDALONE.ADVANCED_NITINOL.getTinyDust(1), 8000);
            exoticModulePlasmaItemMap.put(ELEMENT.STANDALONE.FORCE.getTinyDust(1), 8000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().HAFNIUM.getTinyDust(1), 6000);
            exoticModulePlasmaItemMap.put(ELEMENT.getInstance().CALIFORNIUM.getTinyDust(1), 1000);
            exoticModulePlasmaItemMap.put(ELEMENT.STANDALONE.RUNITE.getTinyDust(1), 8000);

            exoticModulePlasmaFluidMap.put(new FluidStack(ELEMENT.getInstance().XENON.getFluid(), 1), 8000);
            exoticModulePlasmaFluidMap.put(new FluidStack(ELEMENT.getInstance().KRYPTON.getFluid(), 1), 8000);
            exoticModulePlasmaFluidMap.put(new FluidStack(ELEMENT.getInstance().NEON.getFluid(), 1), 8000);
            // These are here because they cant be solidified
            exoticModulePlasmaFluidMap.put(new FluidStack(ELEMENT.getInstance().CURIUM.getFluid(), 1), 10000);
            exoticModulePlasmaFluidMap.put(new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getFluid(), 1), 800);
            exoticModulePlasmaFluidMap.put(new FluidStack(ELEMENT.getInstance().FERMIUM.getFluid(), 1), 800);
        }
        // Mercury is weird, it has neither dust nor gas, so it needs to be added separately
        exoticModulePlasmaFluidMap.put(Materials.Mercury.getFluid(1), 6000);

        // Loop for adding all GT plasma materials
        for (int i = 0; i < plasmaGTMaterialList.size(); i++) {
            if (plasmaGTMaterialList.get(i).getDustTiny(1) != null) {
                exoticModulePlasmaItemMap.put(plasmaGTMaterialList.get(i).getDustTiny(1), plasmaGTWeightList.get(i));
            } else {
                exoticModulePlasmaFluidMap.put(plasmaGTMaterialList.get(i).getGas(1), plasmaGTWeightList.get(i));
            }
        }

        // Magmatter maps
        exoticModuleMagmatterItemMap.putAll(exoticModulePlasmaItemMap);
        exoticModuleMagmatterFluidMap.putAll(exoticModulePlasmaFluidMap);

        // GT materials
        exoticModuleMagmatterItemMap.put(Materials.CosmicNeutronium.getDustTiny(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Draconium.getDustTiny(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.DraconiumAwakened.getDustTiny(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Ichorium.getDustTiny(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Neutronium.getDustTiny(1), 100000);

        // GT++ materials
        if (GTPlusPlus.isModLoaded()) {
            exoticModuleMagmatterItemMap.put(ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getTinyDust(1), 100000);
            exoticModuleMagmatterFluidMap.put(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(1), 100000);
            exoticModuleMagmatterFluidMap.put(ELEMENT.STANDALONE.RHUGNOR.getFluidStack(1), 100000);
            exoticModuleMagmatterFluidMap.put(ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(1), 100000);
            exoticModuleMagmatterFluidMap.put(ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(1), 100000);
        }
    }
}
