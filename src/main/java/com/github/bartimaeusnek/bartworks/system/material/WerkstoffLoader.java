/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material;

import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_GT_ItemRenderer;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_Renderer_Block_Ores;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.log.DebugLog;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.AdditionalRecipes;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_CircuitsLoader;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictAdder;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GT_MultiTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.superscriptNumbers;
import static gregtech.api.enums.OrePrefixes.*;

public class WerkstoffLoader implements Runnable {

    private WerkstoffLoader() {}

    public static final WerkstoffLoader INSTANCE = new WerkstoffLoader();

    //TODO: FREE ID RANGE: 19-32766

    public static final Werkstoff Bismutite = new Werkstoff(
            new short[]{255, 233, 0, 0},
            "Bismutite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            1,
            TextureSet.SET_FLINT,
            Arrays.asList(Materials.Bismuth),
            new Pair<>(Materials.Bismuth, 2),
            new Pair<>(Materials.Oxygen, 2),
            new Pair<>(Materials.CarbonDioxide, 2)
    );
    public static final Werkstoff Bismuthinit = new Werkstoff(
            new short[]{192, 192, 192, 0},
            "Bismuthinite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            2,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Bismuth, Materials.Sulfur),
            new Pair<>(Materials.Bismuth, 2),
            new Pair<>(Materials.Sulfur, 3)
    );
    public static final Werkstoff Zirconium = new Werkstoff(
            new short[]{175, 175, 175, 0},
            "Zirconium",
            "Zr",
            new Werkstoff.Stats().setProtons(40),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().onlyDust(),
            3,
            TextureSet.SET_METALLIC
            //No Byproducts
    );
    public static final Werkstoff CubicZirconia = new Werkstoff(
            new short[]{255, 255, 255, 0},
            "Cubic Zirconia",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust().addGems(),
            4,
            TextureSet.SET_DIAMOND,
            Arrays.asList(WerkstoffLoader.Zirconium),
            new Pair<>(WerkstoffLoader.Zirconium, 1),
            new Pair<>(Materials.Oxygen, 2)
    );
    public static final Werkstoff FluorBuergerit = new Werkstoff(
            new short[]{0x20, 0x20, 0x20, 0},
            "Fluor-Buergerite",
            subscriptNumbers("NaFe3Al6(Si6O18)(BO3)3O3F"),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            5,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<>(Materials.Sodium, 1),
            new Pair<>(Materials.Iron, 3),
            new Pair<>(Materials.Aluminium, 6),
            new Pair<>(Materials.Silicon, 6),
            new Pair<>(Materials.Boron, 3),
            new Pair<>(Materials.Oxygen, 30),
            new Pair<>(Materials.Fluorine, 1)
    );
    public static final Werkstoff YttriumOxide = new Werkstoff(
            new short[]{255,255,255,0},
            "Yttrium Oxide",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(), //No autoadd here to gate this material by hand
            6,
            TextureSet.SET_DULL,
            new Pair<>(Materials.Yttrium, 2),
            new Pair<>(Materials.Oxygen, 3)
    );
    public static final Werkstoff ChromoAluminoPovondrait = new Werkstoff(
            new short[]{0, 0x79, 0x6A, 0},
            "Chromo-Alumino-Povondraite",
            subscriptNumbers("NaCr3(Al4Mg2)(Si6O18)(BO3)3(OH)3O"),
            Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            7,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<>(Materials.Sodium, 1),
            new Pair<>(Materials.Chrome, 3),
            new Pair<>(Materials.Magnalium, 6),
            new Pair<>(Materials.Silicon, 6),
            new Pair<>(Materials.Boron, 3),
            new Pair<>(Materials.Oxygen, 31),
            new Pair<>(Materials.Hydrogen, 3)
    );
    public static final Werkstoff VanadioOxyDravit = new Werkstoff(
            new short[]{0x60, 0xA0, 0xA0, 0},
            "Vanadio-Oxy-Dravite",
            subscriptNumbers("NaV3(Al4Mg2)(Si6O18)(BO3)3(OH)3O"),
            Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            8,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<>(Materials.Sodium, 1),
            new Pair<>(Materials.Vanadium, 3),
            new Pair<>(Materials.Magnalium, 6),
            new Pair<>(Materials.Silicon, 6),
            new Pair<>(Materials.Boron, 3),
            new Pair<>(Materials.Oxygen, 31),
            new Pair<>(Materials.Hydrogen, 3)
    );
    public static final Werkstoff Olenit = new Werkstoff(
            new short[]{210, 210, 210, 0},
            "Olenite",
            subscriptNumbers("NaAl3Al6(Si6O18)(BO3)3O3OH"),
            Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            9,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<>(Materials.Sodium, 1),
            new Pair<>(Materials.Aluminium, 9),
            new Pair<>(Materials.Silicon, 6),
            new Pair<>(Materials.Boron, 3),
            new Pair<>(Materials.Oxygen, 31),
            new Pair<>(Materials.Hydrogen, 1)
    );
    public static final Werkstoff Arsenopyrite = new Werkstoff(
            new short[]{0xB0, 0xB0, 0xB0, 0},
            "Arsenopyrite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            10,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Sulfur, Materials.Arsenic, Materials.Iron),
            new Pair<>(Materials.Iron, 1),
            new Pair<>(Materials.Arsenic, 1),
            new Pair<>(Materials.Sulfur, 1)
    );
    public static final Werkstoff Ferberite = new Werkstoff(
            new short[]{0xB0, 0xB0, 0xB0, 0},
            "Ferberite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            11,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Iron, Materials.Tungsten),
            new Pair<>(Materials.Iron, 1),
            new Pair<>(Materials.Tungsten, 1),
            new Pair<>(Materials.Oxygen, 3)
    );
    public static final Werkstoff Loellingit = new Werkstoff(
            new short[]{0xD0, 0xD0, 0xD0, 0},
            "Loellingite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            12,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Iron, Materials.Arsenic),
            new Pair<>(Materials.Iron, 1),
            new Pair<>(Materials.Arsenic, 2)
    );
    public static final Werkstoff Roquesit = new Werkstoff(
            new short[]{0xA0, 0xA0, 0xA0, 0},
            "Roquesite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            13,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Sulfur),
            new Pair<>(Materials.Copper, 1),
            new Pair<>(Materials.Indium, 1),
            new Pair<>(Materials.Sulfur, 2)
    );
    public static final Werkstoff Bornite = new Werkstoff(
            new short[]{0x97, 0x66, 0x2B, 0},
            "Bornite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            14,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Iron, Materials.Sulfur),
            new Pair<>(Materials.Copper, 5),
            new Pair<>(Materials.Iron, 1),
            new Pair<>(Materials.Sulfur, 4)
    );
    public static final Werkstoff Wittichenit = new Werkstoff(
            Materials.Copper.mRGBa,
            "Wittichenite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            15,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Bismuth, Materials.Sulfur),
            new Pair<>(Materials.Copper, 5),
            new Pair<>(Materials.Bismuth, 1),
            new Pair<>(Materials.Sulfur, 4)
    );
    public static final Werkstoff Djurleit = new Werkstoff(
            new short[]{0x60, 0x60, 0x60, 0},
            "Djurleite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            16,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Copper, Materials.Sulfur),
            new Pair<>(Materials.Copper, 31),
            new Pair<>(Materials.Sulfur, 16)
    );
    public static final Werkstoff Huebnerit = new Werkstoff(
            new short[]{0x80, 0x60, 0x60, 0},
            "Huebnerite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            17,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Manganese, Materials.Tungsten),
            new Pair<>(Materials.Manganese, 1),
            new Pair<>(Materials.Tungsten, 1),
            new Pair<>(Materials.Oxygen, 3)
    );
    public static final Werkstoff Thorianit = new Werkstoff(
            new short[]{0x30, 0x30, 0x30, 0},
            "Thorianite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            18,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Thorium),
            new Pair<>(Materials.Thorium, 1),
            new Pair<>(Materials.Oxygen, 2)
    );
    public static final Werkstoff RedZircon = new Werkstoff(
            new short[]{195, 19, 19, 0},
            "Red Zircon",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            19,
            TextureSet.SET_GEM_VERTICAL,
            Arrays.asList(WerkstoffLoader.Zirconium,Materials.Silicon),
            new Pair<>(WerkstoffLoader.Zirconium, 1),
            new Pair<>(Materials.Silicon, 1),
            new Pair<>(Materials.Oxygen, 4)
    );

    //GT Enhancements
    public static final Werkstoff Salt = new Werkstoff(
            Materials.Salt.mRGBa,
            "Salt",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addGems().addSifterRecipes(),
            20,
            TextureSet.SET_FLINT,
            Arrays.asList(Materials.RockSalt,Materials.Borax),
            new Pair<>(Materials.Salt, 1)
    );
    public static final Werkstoff Spodumen = new Werkstoff(
            Materials.Spodumene.mRGBa,
            "Spodumene",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addGems().addSifterRecipes(),
            21,
            TextureSet.SET_FLINT,
            Arrays.asList(Materials.Spodumene),
            new Pair<>(Materials.Spodumene, 1)
    );
    public static final Werkstoff RockSalt = new Werkstoff(
            Materials.RockSalt.mRGBa,
            "Rock Salt",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addGems().addSifterRecipes(),
            22,
            TextureSet.SET_FLINT,
            Arrays.asList(Materials.RockSalt,Materials.Borax),
            new Pair<>(Materials.RockSalt, 1)
    );
    public static final Werkstoff Fayalit = new Werkstoff(
            new short[]{50,50,50,0},
            "Fayalite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            23,
            TextureSet.SET_QUARTZ,
            Arrays.asList(Materials.Iron,Materials.Silicon),
            new Pair<>(Materials.Iron, 2),
            new Pair<>(Materials.Silicon, 1),
            new Pair<>(Materials.Oxygen, 4)
    );
    public static final Werkstoff Forsterit = new Werkstoff(
            new short[]{255,255,255,0},
            "Forsterite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            24,
            TextureSet.SET_QUARTZ,
            Arrays.asList(Materials.Magnesium,Materials.Silicon),
            new Pair<>(Materials.Magnesium, 2),
            new Pair<>(Materials.Silicon, 1),
            new Pair<>(Materials.Oxygen, 4)
    );
    public static final Werkstoff Hedenbergit = new Werkstoff(
            new short[]{100,150,100,0},
            "Hedenbergite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            25,
            TextureSet.SET_QUARTZ,
            Arrays.asList(Materials.Iron,Materials.Calcium,Materials.Silicon),
            new Pair<>(Materials.Calcium, 1),
            new Pair<>(Materials.Iron, 1),
            new Pair<>(Materials.Silicon, 2),
            new Pair<>(Materials.Oxygen, 6)
    );
    public static final Werkstoff DescloiziteZNVO4 = new Werkstoff(
            new short[]{0xBF,0x18,0x0F,0},
            "Red Descloizite",//Pb(Zn,Cu)[OH|VO4
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            26,
            TextureSet.SET_QUARTZ,
            Arrays.asList(Materials.Lead,Materials.Copper,Materials.Vanadium),
            new Pair<>(Materials.Lead, 1),
            new Pair<>(Materials.Zinc, 1),
            new Pair<>(Materials.Vanadium, 1),
            new Pair<>(Materials.Oxygen, 4)
    );
    public static final Werkstoff DescloiziteCUVO4 = new Werkstoff(
            new short[]{0xf9,0x6d,0x18,0},
            "Orange Descloizite",//Pb(Zn,Cu)[OH|VO4
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            27,
            TextureSet.SET_QUARTZ,
            Arrays.asList(Materials.Lead,Materials.Zinc,Materials.Vanadium),
            new Pair<>(Materials.Lead, 1),
            new Pair<>(Materials.Copper, 1),
            new Pair<>(Materials.Vanadium, 1),
            new Pair<>(Materials.Oxygen, 4)
    );
    public static final Werkstoff FuchsitAL = new Werkstoff(
            new short[]{0x4D,0x7F,0x64,0},
            "Green Fuchsite",
            subscriptNumbers("KAl3Si3O10(OH)2"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            28,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Potassium,Materials.Aluminium,Materials.Silicon),
            new Pair<>(Materials.Potassium, 1),
            new Pair<>(Materials.Aluminium, 3),
            new Pair<>(Materials.Silicon, 3),
            new Pair<>(Materials.Oxygen, 12),
            new Pair<>(Materials.Hydrogen, 2)

    );
    public static final Werkstoff FuchsitCR = new Werkstoff(
            new short[]{128,0,0,0},
            "Red Fuchsite",
            subscriptNumbers("KCr3Si3O10(OH)2"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            29,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Potassium,Materials.Chrome,Materials.Silicon),
            new Pair<>(Materials.Potassium, 1),
            new Pair<>(Materials.Chrome, 3),
            new Pair<>(Materials.Silicon, 3),
            new Pair<>(Materials.Oxygen, 12),
            new Pair<>(Materials.Hydrogen, 2)

    );
    public static final Werkstoff Thorium232 = new Werkstoff(
            new short[]{0,64,0,0},
            "Thorium 232",
            superscriptNumbers("Th232"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            30,
            TextureSet.SET_METALLIC
            //No Byproducts
    );
    public static final Werkstoff BismuthTellurite = new Werkstoff(
            new short[]{32,72,32,0},
            "Bismuth Tellurite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addChemicalRecipes(),
            31,
            TextureSet.SET_METALLIC,
            //No Byproducts
            new Pair<>(Materials.Bismuth, 2),
            new Pair<>(Materials.Tellurium, 3)
    );
    public static final Werkstoff Tellurium = new Werkstoff(
            new short[]{0xff,0xff,0xff,0},
            "Tellurium",
            new Werkstoff.Stats(),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().addMetalItems().removeOres(),
            32,
            TextureSet.SET_METALLIC,
            //No Byproducts
            new Pair<>(Materials.Tellurium, 1)
    );
    public static final Werkstoff BismuthHydroBorat = new Werkstoff(
            new short[]{72,144,72,0},
            "Dibismuthhydroborat",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addChemicalRecipes(),
            33,
            TextureSet.SET_METALLIC,
            //No Byproducts
            new Pair<>(Materials.Bismuth, 2),
            new Pair<>(Materials.Boron, 1),
            new Pair<>(Materials.Hydrogen, 1)
    );
    public static final Werkstoff ArInGaPhoBiBoTe = new Werkstoff(
            new short[]{36,36,36,0},
            "Circuit Compound MK3",
            new Werkstoff.Stats().setCentrifuge(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMixerRecipes(),
            34,
            TextureSet.SET_METALLIC,
            //No Byproducts
            new Pair<>(Materials.IndiumGalliumPhosphide, 1),
            new Pair<>(WerkstoffLoader.BismuthHydroBorat, 3),
            new Pair<>(WerkstoffLoader.BismuthTellurite, 2)
    );

    public static final Werkstoff Prasiolite = new Werkstoff(
            new short[]{0xD0,0xDD,0x95,0},
            "Prasiolite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            35,
            TextureSet.SET_QUARTZ,
            //No Byproducts
            new Pair<>(Materials.Silicon,5),
            new Pair<>(Materials.Oxygen,10),
            new Pair<>(Materials.Iron,1)
    );

    public static final Werkstoff MagnetoResonaticDust = new Werkstoff(
            new short[]{0xDD,0x77,0xDD,0},
            "Magneto Resonatic",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust().addMixerRecipes(),
            36,
            TextureSet.SET_MAGNETIC,
            //No Byproducts
            new Pair<>(WerkstoffLoader.Prasiolite,3),
            new Pair<>(WerkstoffLoader.BismuthTellurite,4),
            new Pair<>(WerkstoffLoader.CubicZirconia,1),
            new Pair<>(Materials.SteelMagnetic,1)
    );

    public static HashMap<OrePrefixes, BW_MetaGenerated_Items> items = new HashMap<>();
    public static Block BWOres;
    public boolean registered;

    public static ItemStack getCorresopndingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff) {
        return WerkstoffLoader.getCorresopndingItemStack(orePrefixes, werkstoff, 1);
    }

    public static ItemStack getCorresopndingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff, int amount) {
        ItemStack ret = OreDictHandler.getItemStack(werkstoff.getDefaultName(),orePrefixes,amount);
            if (ret != null)
                return ret;
        if (orePrefixes == ore)
            return new ItemStack(WerkstoffLoader.BWOres, amount, werkstoff.getmID());
        return new ItemStack(WerkstoffLoader.items.get(orePrefixes), amount, werkstoff.getmID()).copy();
    }

    public void init() {
        if (WerkstoffLoader.INSTANCE == null)
            MainMod.LOGGER.error("INSTANCE IS NULL THIS SHOULD NEVER HAPPEN!");
        GT_LanguageManager.addStringLocalization("metaitem.01.tooltip.purify.2","Throw into Cauldron to get clean crushed Ore");
    }

    public void runInit() {
        MainMod.LOGGER.info("Making Meta Items for BW Materials");
        long timepre = System.nanoTime();
        WerkstoffAdderRegistry.getINSTANCE().run();
        this.addSubTags();
        this.addItemsForGeneration();
        this.runAdditionalOreDict();
        long timepost = System.nanoTime();
        MainMod.LOGGER.info("Making Meta Items for BW Materials took " + (timepost - timepre) + "ns/" + ((timepost - timepre) / 1000000) + "ms/" + ((timepost - timepre) / 1000000000) + "s!");
    }

    @Override
    public void run() {
        if (!this.registered) {
            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials");
            long timepre = System.nanoTime();
            ProgressManager.ProgressBar progressBar = ProgressManager.push("Register BW Materials", Werkstoff.werkstoffHashMap.size()+1);
            DebugLog.log("Loading Recipes"+(System.nanoTime()-timepre));
            for (short i = 0; i < Werkstoff.werkstoffHashMap.size(); i++) {
                long timepreone = System.nanoTime();
                Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get(i);
                DebugLog.log("Werkstoff is null or id < 0 ? "+ (werkstoff==null || werkstoff.getmID() < 0) + " " + (System.nanoTime()-timepreone));
                if (werkstoff == null || werkstoff.getmID() < 0) {
                    progressBar.step("");
                    continue;
                }
                DebugLog.log("Werkstoff: "+ werkstoff.getDefaultName() +" " +(System.nanoTime()-timepreone));
                DebugLog.log("Loading Dusts Recipes"+" " +(System.nanoTime()-timepreone));
                this.addDustRecipes(werkstoff);
                DebugLog.log("Loading Gem Recipes"+" " +(System.nanoTime()-timepreone));
                this.addGemRecipes(werkstoff);
                DebugLog.log("Loading Ore Recipes"+" " +(System.nanoTime()-timepreone));
                this.addOreRecipes(werkstoff);
                DebugLog.log("Loading Crushed Recipes"+" " +(System.nanoTime()-timepreone));
                this.addCrushedRecipes(werkstoff);
                if (Loader.isModLoaded("Thaumcraft")) {
                    DebugLog.log("Loading Aspects"+" " +(System.nanoTime()-timepreone));
                    ThaumcraftHandler.AspectAdder.addAspectToAll(werkstoff);
                }
                DebugLog.log("Loading New Circuits"+" " +(System.nanoTime()-timepreone));
                new BW_CircuitsLoader();
                DebugLog.log("Done"+" " +(System.nanoTime()-timepreone));
                progressBar.step(werkstoff.getDefaultName());
            }
            progressBar.step("Load Additional Recipes");
            new AdditionalRecipes().run();
            ProgressManager.pop(progressBar);
            long timepost = System.nanoTime();
            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials took " + (timepost - timepre) + "ns/" + ((timepost - timepre) / 1000000) + "ms/" + ((timepost - timepre) / 1000000000) + "s!");
            this.registered = true;
        }
    }

    private void addSubTags() {
        for (Werkstoff W : Werkstoff.werkstoffHashMap.values()) {
            for (Pair<ISubTagContainer, Integer> pair : W.getContents().getValue().toArray(new Pair[0])) {

                if (pair.getKey() instanceof Materials && pair.getKey() == Materials.Neodymium) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM);
                    break;
                } else if (pair.getKey() instanceof Materials && pair.getKey() == Materials.Iron) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_IRON);
                    break;
                } else if (pair.getKey() instanceof Materials && pair.getKey() == Materials.Gold) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD);
                    break;
                }
            }
            if (W.getGenerationFeatures().hasGems()) {
                W.add(SubTag.CRYSTAL);
                W.add(SubTag.CRYSTALLISABLE);
            }
        }
    }

    public static int toGenerateGlobal;
    private void addItemsForGeneration() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            for (OrePrefixes p : values())
                if ((werkstoff.getGenerationFeatures().toGenerate & p.mMaterialGenerationBits) != 0 && OreDictHandler.getItemStack(werkstoff.getDefaultName(),p,1) != null) {
                    MainMod.LOGGER.info("Found: "+(p+werkstoff.getDefaultName().replaceAll(" ",""))+" in oreDict, disable and reroute my Items to that, also add a Tooltip.");
                    werkstoff.getGenerationFeatures().setBlacklist(p);
                }
            WerkstoffLoader.toGenerateGlobal = (WerkstoffLoader.toGenerateGlobal | werkstoff.getGenerationFeatures().toGenerate);
            //System.out.println(werkstoff.getDefaultName()+": "+werkstoff.getGenerationFeatures().toGenerate);
        }

        if ((WerkstoffLoader.toGenerateGlobal & 0b1) != 0) {
            WerkstoffLoader.items.put(dust, new BW_MetaGenerated_Items(dust));
            WerkstoffLoader.items.put(dustTiny, new BW_MetaGenerated_Items(dustTiny));
            WerkstoffLoader.items.put(dustSmall, new BW_MetaGenerated_Items(dustSmall));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10) != 0) {
            WerkstoffLoader.items.put(ingot, new BW_MetaGenerated_Items(ingot));
            WerkstoffLoader.items.put(nugget, new BW_MetaGenerated_Items(nugget));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100) != 0) {
            WerkstoffLoader.items.put(gem, new BW_MetaGenerated_Items(gem));
            WerkstoffLoader.items.put(gemChipped, new BW_MetaGenerated_Items(gemChipped));
            WerkstoffLoader.items.put(gemExquisite, new BW_MetaGenerated_Items(gemExquisite));
            WerkstoffLoader.items.put(gemFlawed, new BW_MetaGenerated_Items(gemFlawed));
            WerkstoffLoader.items.put(gemFlawless, new BW_MetaGenerated_Items(gemFlawless));
            WerkstoffLoader.items.put(lens,new BW_MetaGenerated_Items(lens));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000) != 0) {
            if (!ConfigHandler.experimentalThreadedLoader)
                this.gameRegistryHandler();
            WerkstoffLoader.items.put(crushed, new BW_MetaGenerated_Items(crushed));
            WerkstoffLoader.items.put(crushedPurified, new BW_MetaGenerated_Items(crushedPurified));
            WerkstoffLoader.items.put(crushedCentrifuged, new BW_MetaGenerated_Items(crushedCentrifuged));
            WerkstoffLoader.items.put(dustPure, new BW_MetaGenerated_Items(dustPure));
            WerkstoffLoader.items.put(dustImpure, new BW_MetaGenerated_Items(dustImpure));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000) != 0) {
            WerkstoffLoader.items.put(cell, new BW_MetaGenerated_Items(cell));
            WerkstoffLoader.items.put(bottle, new BW_MetaGenerated_Items(bottle));
            WerkstoffLoader.items.put(capsule, new BW_MetaGenerated_Items(capsule));
        }
    }

    public void gameRegistryHandler(){
        if (FMLCommonHandler.instance().getSide().isClient())
            RenderingRegistry.registerBlockHandler(BW_Renderer_Block_Ores.INSTANCE);
        GameRegistry.registerTileEntity(BW_MetaGeneratedOreTE.class, "bw.blockoresTE");
        WerkstoffLoader.BWOres = new BW_MetaGenerated_Ores(Material.rock, BW_MetaGeneratedOreTE.class, "bw.blockores");
        GameRegistry.registerBlock(WerkstoffLoader.BWOres, BW_MetaGeneratedOre_Item.class, "bw.blockores.01");
        WerkstoffLoader.runGTItemDataRegistrator();
    }

    public static void runGTItemDataRegistrator(){
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            GT_OreDictUnificator.addAssociation(ore,Materials._NULL,new ItemStack(WerkstoffLoader.BWOres,1,werkstoff.getmID()),true);
        }
    }

    public static void removeIC2Recipes() {
        try {
            Set<Map.Entry<IRecipeInput, RecipeOutput>> remset = new HashSet<>();
            for (Map.Entry<IRecipeInput, RecipeOutput> curr : Recipes.macerator.getRecipes().entrySet()) {
                if (curr.getKey() instanceof RecipeInputOreDict) {
                    if (((RecipeInputOreDict) curr.getKey()).input.equalsIgnoreCase("oreNULL")) {
                        remset.add(curr);
                    }
                }
            }
            Recipes.macerator.getRecipes().entrySet().removeAll(remset);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runAdditionalOreDict(){
        if (ConfigHandler.experimentalThreadedLoader){
            for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
                if (werkstoff.getGenerationFeatures().hasOres())
                    OreDictAdder.addToMap(new Pair<>(ore + werkstoff.getDefaultName().replaceAll(" ", ""), werkstoff.get(ore)));
                if (werkstoff.getGenerationFeatures().hasGems())
                    OreDictAdder.addToMap(new Pair<>("craftingLens" + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""), werkstoff.get(lens)));
            }

            OreDictAdder.addToMap(new Pair<>("craftingIndustrialDiamond", WerkstoffLoader.CubicZirconia.get(gemExquisite)));
        } else {
            for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
                if (werkstoff.getGenerationFeatures().hasOres())
                    GT_OreDictUnificator.registerOre(ore + werkstoff.getDefaultName().replaceAll(" ", ""), werkstoff.get(ore));
                if (werkstoff.getGenerationFeatures().hasGems())
                    OreDictionary.registerOre("craftingLens" + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""), werkstoff.get(lens));
            }

            GT_OreDictUnificator.registerOre("craftingIndustrialDiamond", WerkstoffLoader.CubicZirconia.get(gemExquisite));
        }
    }

    private void addGemRecipes(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().hasGems()) {
            if (werkstoff.getGenerationFeatures().hasSifterRecipes() || ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) != 0 && (werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0)) {

                GT_Values.RA.addSifterRecipe(
                        WerkstoffLoader.getCorresopndingItemStack(crushedPurified, werkstoff),
                        new ItemStack[]{
                                WerkstoffLoader.getCorresopndingItemStack(gemExquisite, werkstoff),
                                WerkstoffLoader.getCorresopndingItemStack(gemFlawless, werkstoff),
                                WerkstoffLoader.getCorresopndingItemStack(gem, werkstoff),
                                WerkstoffLoader.getCorresopndingItemStack(gemFlawed, werkstoff),
                                WerkstoffLoader.getCorresopndingItemStack(gemChipped, werkstoff),
                                WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff)
                        },
                        new int[]{
                                200, 1000, 2500, 2000, 4000, 5000
                        },
                        800,
                        16
                );
            }

            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemExquisite), werkstoff.get(dust, 4));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawless), werkstoff.get(dust, 2));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gem), werkstoff.get(dust));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawed), werkstoff.get(dustSmall, 1));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemChipped), werkstoff.get(dustTiny));

            GT_ModHandler.addCraftingRecipe(werkstoff.get(gemFlawless, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gemExquisite)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gem, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gemFlawless)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gemFlawed, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gem)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gemChipped, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gemFlawed)});

            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemExquisite), werkstoff.get(gemFlawless, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawless), werkstoff.get(gem, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gem), werkstoff.get(gemFlawed, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawed), werkstoff.get(gemChipped, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemChipped), werkstoff.get(dustTiny), 64, 16);

            GT_Values.RA.addImplosionRecipe(werkstoff.get(gemFlawless, 3), 8, werkstoff.get(gemExquisite), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
            GT_Values.RA.addImplosionRecipe(werkstoff.get(gem, 3), 8, werkstoff.get(gemFlawless), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
            GT_Values.RA.addImplosionRecipe(werkstoff.get(gemFlawed, 3), 8, werkstoff.get(gem), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
            GT_Values.RA.addImplosionRecipe(werkstoff.get(gemChipped, 3), 8, werkstoff.get(gemFlawed), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));

            GT_Values.RA.addImplosionRecipe(werkstoff.get(dust, 4), 24, werkstoff.get(gem, 3), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 8));

            if ((werkstoff.getGenerationFeatures().toGenerate & 2) != 0) {
                GT_Values.RA.addLatheRecipe(werkstoff.get(plate), werkstoff.get(lens), werkstoff.get(dustSmall), 1200, 120);
            }

            GT_Values.RA.addLatheRecipe(werkstoff.get(gemExquisite), werkstoff.get(lens), werkstoff.get(dust, 2), 2400, 30);
            GregTech_API.registerCover(werkstoff.get(lens), new GT_MultiTexture(Textures.BlockIcons.MACHINE_CASINGS[2][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_LENS, werkstoff.getRGBA(), false)), new gregtech.common.covers.GT_Cover_Lens(BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mIndex));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(lens), werkstoff.get(dustSmall, 3));

            for (ItemStack is : OreDictionary.getOres("craftingLens" + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""))) {
                is.stackSize = 0;
                GT_Values.RA.addLaserEngraverRecipe(werkstoff.get(gemChipped, 3), is, werkstoff.get(gemFlawed, 1), 600, 30);
                GT_Values.RA.addLaserEngraverRecipe(werkstoff.get(gemFlawed, 3), is, werkstoff.get(gem, 1), 600, 120);
                GT_Values.RA.addLaserEngraverRecipe(werkstoff.get(gem, 3), is, werkstoff.get(gemFlawless, 1), 1200, 480);
                GT_Values.RA.addLaserEngraverRecipe(werkstoff.get(gemFlawless, 3), is, werkstoff.get(gemExquisite, 1), 2400, 2000);
            }
        }
    }

    private void addDustRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0) {

            List<FluidStack> flOutputs = new ArrayList<>();
            List<ItemStack> stOutputs = new ArrayList<>();
            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
            int cells = 0;

            if (werkstoff.getStats().isElektrolysis() || werkstoff.getStats().isCentrifuge() || werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
                for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents().getValue().toArray(new Pair[0])) {
                    if (container.getKey() instanceof Materials) {
                        if (((Materials) container.getKey()).hasCorrespondingGas() || ((Materials) container.getKey()).hasCorrespondingFluid() || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID) {
                            FluidStack tmpFl = ((Materials) container.getKey()).getGas(1000 * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Materials) container.getKey()).getFluid(1000 * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Materials) container.getKey()).getCells(container.getValue()));
                                    tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(((Materials) container.getKey()).getCells(tracker.get(container.getKey()).getKey() + container.getValue()));
                                    stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Materials) container.getKey()).getDust(container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Materials) container.getKey()).getDust(tracker.get(container.getKey()).getKey() + container.getValue()));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    } else if (container.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) container.getKey()).getTexSet() == TextureSet.SET_FLUID) {
                            //not yet implemented no fluids from me...
                        } else {
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Werkstoff) container.getKey()).get(dust, container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Werkstoff) container.getKey()).get(dust, (tracker.get(container.getKey()).getKey() + container.getValue())));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    }
                }
                ItemStack input = WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff);
                input.stackSize = werkstoff.getContents().getKey();
                if (werkstoff.getStats().isElektrolysis())
                    GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.addRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().protons * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30, 0);
                if (werkstoff.getStats().isCentrifuge())
                    GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().mass * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5, 0);
                if (werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
                    if (cells > 0)
                        stOutputs.add(Materials.Empty.getCells(cells));
                    GT_Recipe.GT_Recipe_Map.sChemicalRecipes.addRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().protons * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30,0);
                }
                if (werkstoff.getGenerationFeatures().hasMixerRecipes()) {
                    if (cells > 0)
                        stOutputs.add(Materials.Empty.getCells(cells));
                    GT_Recipe.GT_Recipe_Map.sMixerRecipes.addRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().mass * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5,0);
                }
            }

            GT_ModHandler.addCraftingRecipe(WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff),  new Object[]{
                    "TTT","TTT","TTT",'T',
                    WerkstoffLoader.getCorresopndingItemStack(dustTiny, werkstoff)
            });
            GT_ModHandler.addCraftingRecipe(WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff),  new Object[]{
                    "TT ","TT ",'T',
                    WerkstoffLoader.getCorresopndingItemStack(dustSmall, werkstoff)
            });
            GT_ModHandler.addCraftingRecipe(WerkstoffLoader.getCorresopndingItemStack(dustSmall, werkstoff, 4), new Object[]{
                    " T ", 'T', WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff)
            });
            GT_ModHandler.addCraftingRecipe(WerkstoffLoader.getCorresopndingItemStack(dustTiny, werkstoff, 9), new Object[]{
                    "T  ", 'T', WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff)
            });

            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 && !werkstoff.getStats().isBlastFurnace()) {
                GT_ModHandler.addSmeltingRecipe(WerkstoffLoader.getCorresopndingItemStack(dust, werkstoff), WerkstoffLoader.getCorresopndingItemStack(ingot, werkstoff));
                GT_ModHandler.addSmeltingRecipe(WerkstoffLoader.getCorresopndingItemStack(dustTiny, werkstoff), WerkstoffLoader.getCorresopndingItemStack(nugget, werkstoff));
            }
            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0){
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(ingot),werkstoff.get(dust));
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(nugget),werkstoff.get(dustTiny));
            }
        }
    }

    private void addOreRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) != 0 && (werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 &&!werkstoff.getStats().isBlastFurnace())
            GT_ModHandler.addSmeltingRecipe(WerkstoffLoader.getCorresopndingItemStack(ore, werkstoff), WerkstoffLoader.getCorresopndingItemStack(ingot, werkstoff));

        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) != 0) {
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(ore), werkstoff.getGenerationFeatures().hasGems() ? werkstoff.get(gem) : werkstoff.get(crushed), 16, 10);
            GT_ModHandler.addPulverisationRecipe(
                    werkstoff.get(ore),
                    werkstoff.get(crushed, 2),
                    werkstoff.contains(SubTag.CRYSTAL) ? werkstoff.get(gem) : werkstoff.getOreByProduct(0, dust),
                    werkstoff.getNoOfByProducts() > 0 ? 10 : 0,
                    Materials.Stone.getDust(1),
                    50,
                    true);
        }
    }

    private void addCrushedRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) == 0 || (werkstoff.getGenerationFeatures().toGenerate & 0b1) == 0)
            return;

        if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 && !werkstoff.getStats().isBlastFurnace()) {
            if (werkstoff.getType().equals(Werkstoff.Types.ELEMENT)) {
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushed), werkstoff.get(nugget, 10));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedPurified), werkstoff.get(nugget, 10));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(nugget, 10));
            }
            else {
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushed), werkstoff.get(ingot));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedPurified), werkstoff.get(ingot));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(ingot));
            }
            GT_ModHandler.addSmeltingRecipe(werkstoff.get(dustImpure), werkstoff.get(ingot));
            GT_ModHandler.addSmeltingRecipe(werkstoff.get(dustPure), werkstoff.get(ingot));
            GT_ModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
        }

        GT_ModHandler.addCraftingRecipe(werkstoff.get(dustImpure),new Object[]{"h  ", "W  ",'W',werkstoff.get(crushed)});
        GT_ModHandler.addCraftingRecipe(werkstoff.get(dustPure),new Object[]{"h  ", "W  ",'W',werkstoff.get(crushedPurified)});
        GT_ModHandler.addCraftingRecipe(werkstoff.get(dust),new Object[]{"h  ", "W  ",'W',werkstoff.get(crushedCentrifuged)});

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushed), werkstoff.get(dustImpure), 10, 16);
        GT_ModHandler.addPulverisationRecipe(werkstoff.get(crushed), werkstoff.get(dustImpure), werkstoff.getOreByProduct(0, dust), 10, false);
        GT_ModHandler.addOreWasherRecipe(werkstoff.get(crushed), 1000, werkstoff.get(crushedPurified), werkstoff.getOreByProduct(0, dustTiny), GT_OreDictUnificator.get(dust, Materials.Stone, 1L));
        GT_ModHandler.addThermalCentrifugeRecipe(werkstoff.get(crushed), (int) Math.min(5000L, Math.abs(werkstoff.getStats().protons * 20L)), werkstoff.get(crushedCentrifuged), werkstoff.getOreByProduct(1, dustTiny), GT_OreDictUnificator.get(dust, Materials.Stone, 1L));

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushedPurified), werkstoff.get(dustPure), 10, 16);
        GT_ModHandler.addPulverisationRecipe(werkstoff.get(crushedPurified), werkstoff.get(dustPure), werkstoff.getOreByProduct(1, dust), 10, false);
        GT_ModHandler.addThermalCentrifugeRecipe(werkstoff.get(crushedPurified), (int) Math.min(5000L, Math.abs(werkstoff.getStats().protons * 20L)), werkstoff.get(crushedCentrifuged), werkstoff.getOreByProduct(1, dustTiny));

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(dust), 10, 16);
        GT_ModHandler.addPulverisationRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(dust), werkstoff.getOreByProduct(2, dust), 10, false);

        GT_Values.RA.addCentrifugeRecipe(werkstoff.get(dustImpure), 0, werkstoff.get(dust), werkstoff.getOreByProduct(0, dustTiny), null, null, null, null, (int) Math.max(1L, werkstoff.getStats().mass * 8L));
        GT_Values.RA.addCentrifugeRecipe(werkstoff.get(dustPure), 0, werkstoff.get(dust), werkstoff.getOreByProduct(1, dustTiny), null, null, null, null, (int) Math.max(1L, werkstoff.getStats().mass * 8L));

        if (werkstoff.contains(SubTag.CRYSTALLISABLE)) {
            GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustPure), Materials.Water.getFluid(200L), werkstoff.get(gem), 9000, 2000, 24);
            GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustImpure), Materials.Water.getFluid(200L), werkstoff.get(gem), 9000, 2000, 24);
            GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustPure), gregtech.api.util.GT_ModHandler.getDistilledWater(200L), werkstoff.get(gem), 9500, 1500, 24);
            GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustImpure), gregtech.api.util.GT_ModHandler.getDistilledWater(200L), werkstoff.get(gem), 9500, 1500, 24);
        }
        if (werkstoff.contains(SubTag.WASHING_MERCURY))
            GT_Values.RA.addChemicalBathRecipe(werkstoff.get(crushed), Materials.Mercury.getFluid(1000L), werkstoff.get(crushedPurified), werkstoff.getOreByProduct(1, dust), GT_OreDictUnificator.get(dust, Materials.Stone, 1L), new int[]{10000, 7000, 4000}, 800, 8);
        if (werkstoff.contains(SubTag.WASHING_SODIUMPERSULFATE))
            GT_Values.RA.addChemicalBathRecipe(werkstoff.get(crushed), Materials.SodiumPersulfate.getFluid(GT_Mod.gregtechproxy.mDisableOldChemicalRecipes ? 1000L : 100L), werkstoff.get(crushedPurified), werkstoff.getOreByProduct(1, dust), GT_OreDictUnificator.get(dust, Materials.Stone, 1L), new int[]{10000, 7000, 4000}, 800, 8);
        if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD))
            GT_Values.RA.addElectromagneticSeparatorRecipe(werkstoff.get(dustPure), werkstoff.get(dust), GT_OreDictUnificator.get(dustSmall, Materials.Gold, 1L), GT_OreDictUnificator.get(nugget, Materials.Gold, 1L), new int[]{10000, 4000, 2000}, 400, 24);
        else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON))
            GT_Values.RA.addElectromagneticSeparatorRecipe(werkstoff.get(dustPure), werkstoff.get(dust), GT_OreDictUnificator.get(dustSmall, Materials.Iron, 1L), GT_OreDictUnificator.get(nugget, Materials.Iron, 1L), new int[]{10000, 4000, 2000}, 400, 24);
        else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM))
            GT_Values.RA.addElectromagneticSeparatorRecipe(werkstoff.get(dustPure), werkstoff.get(dust), GT_OreDictUnificator.get(dustSmall, Materials.Neodymium, 1L), GT_OreDictUnificator.get(nugget, Materials.Neodymium, 1L), new int[]{10000, 4000, 2000}, 400, 24);
    }

}