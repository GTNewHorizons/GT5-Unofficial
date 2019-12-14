/*
 * Copyright (c) 2018-2019 bartimaeusnek
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
import com.github.bartimaeusnek.bartworks.client.renderer.BW_Renderer_Block_Ores;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.log.DebugLog;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_CircuitsLoader;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.GTMetaItemEnhancer;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.AdditionalRecipes;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictAdder;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GT_Fluid;
import gregtech.api.objects.GT_MultiTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gregtech.common.GT_Proxy;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.*;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.superscriptNumbers;
import static gregtech.api.enums.OrePrefixes.*;

@SuppressWarnings({"unchecked", "unused", "deprecation"})
public class WerkstoffLoader implements Runnable {

    private WerkstoffLoader() {}

    public static final WerkstoffLoader INSTANCE = new WerkstoffLoader();
    public static final SubTag NOBLE_GAS = SubTag.getNewSubTag("NobleGas");
    public static final SubTag ANAEROBE_GAS = SubTag.getNewSubTag("AnaerobeGas");
    public static final SubTag ANAEROBE_SMELTING = SubTag.getNewSubTag("AnaerobeSmelting");
    public static final SubTag NOBLE_GAS_SMELTING = SubTag.getNewSubTag("NobleGasSmelting");
    public static final SubTag NO_BLAST = SubTag.getNewSubTag("NoBlast");
    public static OrePrefixes cellMolten;
    public static OrePrefixes capsuleMolten;
    public static ItemList rotorMold;
    public static ItemList rotorShape;
    public static ItemList smallGearShape;
    public static ItemList ringMold;
    public static ItemList boltMold;
    public static boolean gtnhGT = false;
    static {
        try {
            gtnhGT = GT_MetaGenerated_Tool_01.class.getField("SOLDERING_IRON_MV") != null;
        } catch (NoSuchFieldException ignored) {}

        //GTNH hack for molten cells
        for (OrePrefixes prefix : OrePrefixes.values()){
            if (prefix.toString().equals("cellMolten"))
                WerkstoffLoader.cellMolten = prefix;
        }
        if (WerkstoffLoader.cellMolten == null) {
            WerkstoffLoader.cellMolten = EnumHelper.addEnum(OrePrefixes.class,"cellMolten",new Class[]{String.class, String.class, String.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, int.class, long.class, int.class, int.class},new Object[]{"Cells of Molten stuff", "Molten ", " Cell", true, true, true, true, false, false, false, true, false, false, 0b1000000, 3628800L, 64, 31});
          //  GT_LanguageManager.addStringLocalization(".name", this.getDefaultLocalization(w));
        } else {
            WerkstoffLoader.cellMolten.mMaterialGenerationBits = 0b1000000;
        }
        try {
            WerkstoffLoader.rotorMold = Enum.valueOf(ItemList.class, "Shape_Mold_Rotor");
            WerkstoffLoader.rotorShape = Enum.valueOf(ItemList.class, "Shape_Extruder_Rotor");
            WerkstoffLoader.smallGearShape = Enum.valueOf(ItemList.class, "Shape_Extruder_Small_Gear");
            WerkstoffLoader.ringMold = Enum.valueOf(ItemList.class, "Shape_Mold_Ring");
            WerkstoffLoader.boltMold = Enum.valueOf(ItemList.class, "Shape_Mold_Bolt");
        } catch (NullPointerException | IllegalArgumentException ignored){}
        //add tiberium
        Element t = BW_Util.createNewElement("Tr",123L, 203L, 0L, -1L, null, "Tiberium", false);
        //add molten & regular capsuls
        if (Loader.isModLoaded("Forestry")) {
            capsuleMolten = EnumHelper.addEnum(OrePrefixes.class, "capsuleMolten", new Class[]{String.class, String.class, String.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, int.class, long.class, int.class, int.class}, new Object[]{"Capsule of Molten stuff", "Molten ", " Capsule", true, true, true, true, false, false, false, true, false, false, 0b1000000, 3628800L, 64, -1});
            capsule.mMaterialGenerationBits = 0b100000;
            capsule.mDefaultStackSize = 64;
        }
        bottle.mDefaultStackSize = 1;
    }

    //TODO:
    //FREE ID RANGE: 95-30000
    //bartimaeusnek reserved 0-10000
    //Tec & basdxz reserved range 30000-32767
    public static final Werkstoff Bismutite = new Werkstoff(
            new short[]{255, 233, 0, 0},
            "Bismutite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            1,
            TextureSet.SET_FLINT,
            Collections.singletonList(Materials.Bismuth),
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
            new Werkstoff.Stats().setProtons(40).setMeltingPoint(2130),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().onlyDust().enforceUnification(),
            3,
            TextureSet.SET_METALLIC
            //No Byproducts
    );
    public static final Werkstoff CubicZirconia = new Werkstoff(
            new short[]{255, 255, 255, 0},
            "Cubic Zirconia",
            Werkstoff.Types.COMPOUND,
            3273,
            new Werkstoff.GenerationFeatures().onlyDust().addGems().enforceUnification(),
            4,
            TextureSet.SET_DIAMOND,
            Collections.singletonList(WerkstoffLoader.Zirconium),
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
            new Werkstoff.GenerationFeatures().onlyDust().enforceUnification(), //No autoadd here to gate this material by hand
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
            Collections.singletonList(Materials.Thorium),
            new Pair<>(Materials.Thorium, 1),
            new Pair<>(Materials.Oxygen, 2)
    );
    public static final Werkstoff RedZircon = new Werkstoff(
            new short[]{195, 19, 19, 0},
            "Red Zircon",
            new Werkstoff.Stats().setElektrolysis(true).setMeltingPoint(2130),
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
            Collections.singletonList(Materials.Spodumene),
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
            Werkstoff.Types.ISOTOPE,
            new Werkstoff.GenerationFeatures().disable().onlyDust().enforceUnification(),
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
            new Werkstoff.Stats().setElektrolysis(true).setMeltingPoint(1923),
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
            new Werkstoff.GenerationFeatures().onlyDust().addMixerRecipes().addGems(),
            36,
            TextureSet.SET_MAGNETIC,
            //No Byproducts
            new Pair<>(WerkstoffLoader.Prasiolite,3),
            new Pair<>(WerkstoffLoader.BismuthTellurite,4),
            new Pair<>(WerkstoffLoader.CubicZirconia,1),
            new Pair<>(Materials.SteelMagnetic,1)
    );
    public static final Werkstoff Xenon = new Werkstoff(
            new short[]{0x14,0x39,0x7F,0},
            "Xenon",
            "Xe",
            new Werkstoff.Stats().setProtons(54).setMass(131).setGas(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().addCells().enforceUnification(),
            37,
            TextureSet.SET_FLUID
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff Oganesson = new Werkstoff(
            new short[]{0x14,0x39,0x7F,0},
            "Oganesson",
            "Og",
            new Werkstoff.Stats().setProtons(118).setMass(294).setGas(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            38,
            TextureSet.SET_FLUID
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff Californium = new Werkstoff(
            new short[]{0xAA,0xAA,0xAA,0},
            "Californium",
            "Cf",
            new Werkstoff.Stats().setProtons(98).setMass(251).setBlastFurnace(true).setMeltingPoint(900),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMetalItems().addMolten().enforceUnification(),
            39,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff Calcium = new Werkstoff(
            Materials.Calcium.mRGBa,
            "Calcium",
            "Ca",
            new Werkstoff.Stats().setProtons(Element.Ca.mProtons).setMass(Element.Ca.getMass()).setBlastFurnace(true).setMeltingPoint(1115).setBoilingPoint(1757),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMetalItems().addMolten(),
            40,
            Materials.Calcium.mIconSet
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff Neon = new Werkstoff(
            new short[]{0xff,0x07,0x3a},
            "Neon",
            "Ne",
            new Werkstoff.Stats().setProtons(Element.Ne.mProtons).setMass(Element.Ne.getMass()).setGas(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().addCells().enforceUnification(),
            41,
            TextureSet.SET_FLUID
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff Krypton = new Werkstoff(
            new short[]{0xb1,0xff,0x32},
            "Krypton",
            "Kr",
            new Werkstoff.Stats().setProtons(Element.Kr.mProtons).setMass(Element.Kr.getMass()).setGas(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().addCells().enforceUnification(),
            42,
            TextureSet.SET_FLUID
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff BArTiMaEuSNeK = new Werkstoff(
            new short[]{0x00,0xff,0x00},
            "BArTiMaEuSNeK",
            "Are you serious?",
            new Werkstoff.Stats().setMeltingPoint(9001).setCentrifuge(true).setBlastFurnace(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems().addMetalItems().addMolten(),
            43,
            TextureSet.SET_DIAMOND,
            Arrays.asList(
                    Materials.Boron,
                    Materials.Titanium,
                    Materials.Europium
            ),
            new Pair<>(Materials.Boron,1),
            new Pair<>(Materials.Argon,1),
            new Pair<>(Materials.Titanium,1),
            new Pair<>(Materials.Magic,1),
            new Pair<>(Materials.Europium,1),
            new Pair<>(Materials.Sulfur,1),
            new Pair<>(WerkstoffLoader.Neon,1),
            new Pair<>(Materials.Potassium,1)
    );
    public static final Werkstoff PTConcentrate = new Werkstoff(
            Materials.Platinum.getRGBA(),
            "Platinum Concentrate",
            "",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            44,
            TextureSet.SET_FLUID
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff PTSaltCrude = new Werkstoff(
            Materials.Platinum.getRGBA(),
            "Platinum Salt",
            "",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            45,
            TextureSet.SET_DULL
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff PTSaltRefined = new Werkstoff(
            Materials.Platinum.getRGBA(),
            "Refined Platinum Salt",
            "",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            46,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff PTMetallicPowder = new Werkstoff(
            Materials.Platinum.getRGBA(),
            "Platinum Metallic Powder",
            "",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            47,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff AquaRegia = new Werkstoff(
            new short[]{0xff,0xb1,0x32},
            "Aqua Regia",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            48,
            TextureSet.SET_FLUID,
            //No Byproducts
            new Pair<>(Materials.DilutedSulfuricAcid,1),
            new Pair<>(Materials.NitricAcid,1)
    );
    public static final Werkstoff PTResidue = new Werkstoff(
            new short[]{0x64,0x63,0x2E},
            "Platinum Residue",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            49,
            TextureSet.SET_ROUGH
            //No Byproducts
    );
    public static final Werkstoff AmmoniumChloride = new Werkstoff(
            new short[]{0xff,0xff,0xff},
            "Ammonium Chloride",
            subscriptNumbers("NH4Cl"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            50,
            TextureSet.SET_FLUID,
            //No Byproducts
            new Pair<>(Materials.Ammonium,1),
            new Pair<>(Materials.HydrochloricAcid,1)
    );
    public static final Werkstoff PTRawPowder = new Werkstoff(
            Materials.Platinum.getRGBA(),
            "Reprecipitated Platinum",
            "PtCl",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            51,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff PDAmmonia = new Werkstoff(
            Materials.Palladium.getRGBA(),
            "Palladium Enriched Ammonia",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            52,
            TextureSet.SET_FLUID,
            //No Byproducts
            new Pair<>(Materials.Ammonium,1),
            new Pair<>(Materials.Palladium,1)
    );
    public static final Werkstoff PDMetallicPowder = new Werkstoff(
            Materials.Palladium.getRGBA(),
            "Palladium Metallic Powder",
            "??Pd??",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            53,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff PDRawPowder = new Werkstoff(
            Materials.Palladium.getRGBA(),
            "Reprecipitated Palladium",
            subscriptNumbers("Pd2NH4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            54,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff PDSalt = new Werkstoff(
            Materials.Palladium.getRGBA(),
            "Palladium Salt",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            55,
            TextureSet.SET_METALLIC
            //No Byproducts
            //No Ingredients
    );
    public static final Werkstoff Sodiumformate = new Werkstoff(
            new short[]{0xff,0xaa,0xaa},
            "Sodium Formate",
            "HCOONa",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            56,
            TextureSet.SET_FLUID,
            //No Byproducts
            new Pair<>(Materials.SodiumHydroxide,1),
            new Pair<>(Materials.CarbonMonoxide,1)
    );
    public static final Werkstoff Sodiumsulfate = new Werkstoff(
            new short[]{0xff,0xff,0xff},
            "Sodium Sulfate",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            57,
            TextureSet.SET_FLUID,
            //No Byproducts
            new Pair<>(Materials.Sodium,2),
            new Pair<>(Materials.Sulfur,1),
            new Pair<>(Materials.Oxygen,4)
    );
    public static final Werkstoff FormicAcid = new Werkstoff(
            new short[]{0xff,0xaa,0x77},
            "Formic Acid",
            subscriptNumbers("CH2O2"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            58,
            TextureSet.SET_FLUID,
            //No Byproducts
            new Pair<>(Materials.Carbon,1),
            new Pair<>(Materials.Hydrogen,2),
            new Pair<>(Materials.Oxygen,2)
    );
    public static final Werkstoff PotassiumDisulfate = new Werkstoff(
            new short[]{0xfb,0xbb,0x66},
            "Potassium Disulfate",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addChemicalRecipes(),
            59,
            TextureSet.SET_DULL,
            //No Byproducts
            new Pair<>(Materials.Potassium,2),
            new Pair<>(Materials.Sulfur,2),
            new Pair<>(Materials.Oxygen,7)
    );
    public static final Werkstoff LeachResidue = new Werkstoff(
            new short[]{0x64, 0x46, 0x29},
            "Leach Residue",
            "??IrOsRhRu??",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            60,
            TextureSet.SET_ROUGH
            //No Byproducts
    );
    public static final Werkstoff RHSulfate = new Werkstoff(
            new short[]{0xee,0xaa,0x55},
            "Rhodium Sulfate",
            new Werkstoff.Stats().setGas(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            61,
            TextureSet.SET_FLUID
            //No Byproducts
    );
    public static final Werkstoff RHSulfateSolution = new Werkstoff(
            new short[]{0xff,0xbb,0x66},
            "Rhodium Sulfate Solution",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            62,
            TextureSet.SET_FLUID
            //No Byproducts
    );
    public static final Werkstoff CalciumChloride = new Werkstoff(
            new short[]{0xff,0xff,0xff},
            "Calcium Chloride",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addCells(),
            63,
            TextureSet.SET_DULL,
            new Pair<>(Materials.Calcium,1),
            new Pair<>(Materials.Chlorine,2)
            //No Byproducts
    );
    public static final Werkstoff Ruthenium = new Werkstoff(
            new short[]{0x64,0x64,0x64},
            "Ruthenium",
            "Ru",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(2607).setMass(Element.Ru.getMass()).setProtons(Element.Ru.mProtons),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().onlyDust().addMolten().addMetalItems().enforceUnification(),
            64,
            TextureSet.SET_METALLIC
            //No Byproducts
    );
    public static final Werkstoff SodiumRuthenate = new Werkstoff(
            new short[]{0x3a,0x40,0xcb},
            "Sodium Ruthenate",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            65,
            TextureSet.SET_SHINY,
            new Pair<>(Materials.Sodium,2),
            new Pair<>(Ruthenium,1),
            new Pair<>(Materials.Oxygen,3)
            //No Byproducts
    );
    public static final Werkstoff RutheniumTetroxide = new Werkstoff(
            new short[]{0xc7,0xc7,0xc7},
            "Ruthenium Tetroxide",
            new Werkstoff.Stats().setMeltingPoint(313),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addCells(),
            66,
            TextureSet.SET_DULL,
            new Pair<>(WerkstoffLoader.Ruthenium,1),
            new Pair<>(Materials.Oxygen,4)
            //No Byproducts
    );
    public static final Werkstoff HotRutheniumTetroxideSollution= new Werkstoff(
            new short[]{0xc7,0xc7,0xc7},
            "Hot Ruthenium Tetroxide Solution",
            "???",
            new Werkstoff.Stats().setGas(true).setMeltingPoint(700),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            67,
            TextureSet.SET_FLUID,
            new Pair<>(WerkstoffLoader.Ruthenium,1),
            new Pair<>(Materials.Oxygen,4),
            new Pair<>(Materials.Chlorine,2),
            new Pair<>(Materials.Sodium,2),
            new Pair<>(Materials.Water,2)
            //No Byproducts
    );
    public static final Werkstoff RutheniumTetroxideSollution = new Werkstoff(
            new short[]{0xc7,0xc7,0xc7},
            "Ruthenium Tetroxide Solution",
            "???",
            new Werkstoff.Stats().setMeltingPoint(313),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            68,
            TextureSet.SET_FLUID,
            new Pair<>(Ruthenium,1),
            new Pair<>(Materials.Oxygen,4),
            new Pair<>(Materials.Chlorine,2),
            new Pair<>(Materials.Sodium,2),
            new Pair<>(Materials.Water,2)
            //No Byproducts
    );
    public static final Werkstoff IrOsLeachResidue = new Werkstoff(
            new short[]{0x64, 0x46, 0x29},
            "Rarest Metal Residue",
            "??OsIr??",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            69,
            TextureSet.SET_ROUGH
            //No Byproducts
    );
    public static final Werkstoff IrLeachResidue = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Iridium Metal Residue",
            "??Ir??",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            70,
            TextureSet.SET_ROUGH
            //No Byproducts
    );
    public static final Werkstoff PGSDResidue = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Sludge Dust Residue",
            new Werkstoff.Stats().setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            71,
            TextureSet.SET_DULL,
            new Pair<>(Materials.SiliconDioxide,3),
            new Pair<>(Materials.Gold,2)
    );
    public static final Werkstoff AcidicOsmiumSolution = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Acidic Osmium Solution",
            "???",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            72,
            TextureSet.SET_FLUID,
            new Pair<>(Materials.Osmium,1),
            new Pair<>(Materials.HydrochloricAcid,1)
    );
    public static final Werkstoff IridiumDioxide = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Iridium Dioxide",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            73,
            TextureSet.SET_FLUID,
            new Pair<>(Materials.Iridium,1),
            new Pair<>(Materials.Oxygen,2)
    );
    public static final Werkstoff OsmiumSolution = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Osmium Solution",
            "???",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            74,
            TextureSet.SET_FLUID,
            new Pair<>(Materials.Osmium,1),
            new Pair<>(Materials.Hydrogen,1)
    );
    public static final Werkstoff AcidicIridiumSolution = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Acidic Iridium Solution",
            "???",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            75,
            TextureSet.SET_FLUID,
            new Pair<>(Materials.Iridium,1),
            new Pair<>(Materials.Hydrogen,1)
    );
    public static final Werkstoff IridiumChloride = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Iridium Chloride",
            subscriptNumbers("IrCl3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            76,
            TextureSet.SET_LAPIS,
            new Pair<>(Materials.Iridium,1),
            new Pair<>(Materials.Chlorine,3)
    );
    public static final Werkstoff PGSDResidue2 = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Metallic Sludge Dust Residue",
            new Werkstoff.Stats().setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            77,
            TextureSet.SET_DULL,
            new Pair<>(Materials.Nickel,1),
            new Pair<>(Materials.Copper,1)
    );
    public static final Werkstoff Rhodium = new Werkstoff(
            new short[]{0xF4, 0xF4, 0xF4},
            "Rhodium",
            "Rh",
            new Werkstoff.Stats().setProtons(Element.Rh.mProtons).setMass(Element.Rh.getMass()).setBlastFurnace(true).setMeltingPoint(2237),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMetalItems().addMolten().enforceUnification(),
            78,
            TextureSet.SET_METALLIC
    );
    public static final Werkstoff CrudeRhMetall = new Werkstoff(
            new short[]{0x66, 0x66, 0x66},
            "Crude Rhodium Metal",
            "??Rh??",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            79,
            TextureSet.SET_DULL
    );
    public static final Werkstoff RHSalt = new Werkstoff(
            new short[]{0x84, 0x84, 0x84},
            "Rhodium Salt",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            80,
            TextureSet.SET_GEM_VERTICAL
    );
    public static final Werkstoff RHSaltSolution = new Werkstoff(
            new short[]{0x66, 0x77, 0x88},
            "Rhodium Salt Solution",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            81,
            TextureSet.SET_FLUID
    );
    public static final Werkstoff SodiumNitrate = new Werkstoff(
            new short[]{0x84, 0x66, 0x84},
            "Sodium Nitrate",
            subscriptNumbers("NaNO3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addChemicalRecipes(),
            82,
            TextureSet.SET_ROUGH,
            new Pair<>(Materials.Sodium,1),
            new Pair<>(Materials.NitricAcid,1)
    );
    public static final Werkstoff RHNitrate = new Werkstoff(
            new short[]{0x77, 0x66, 0x49},
            "Rhodium Nitrate",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            83,
            TextureSet.SET_QUARTZ
    );
    public static final Werkstoff ZincSulfate = new Werkstoff(
            new short[]{0x84, 0x66, 0x49},
            "Zinc Sulfate",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            84,
            TextureSet.SET_QUARTZ,
            new Pair<>(Materials.Zinc,1),
            new Pair<>(Materials.Sulfur,1),
            new Pair<>(Materials.Oxygen,4)
    );
    public static final Werkstoff RhFilterCake = new Werkstoff(
            new short[]{0x77, 0x66, 0x49},
            "Rhodium Filter Cake",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            85,
            TextureSet.SET_QUARTZ
    );
    public static final Werkstoff RHFilterCakeSolution = new Werkstoff(
            new short[]{0x66, 0x77, 0x88},
            "Rhodium Filter Cake Solution",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            86,
            TextureSet.SET_FLUID
    );
    public static final Werkstoff ReRh = new Werkstoff(
            new short[]{0x77, 0x66, 0x49},
            "Reprecipitated Rhodium",
            subscriptNumbers("Rh2NH4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            87,
            TextureSet.SET_QUARTZ
    );
    public static final Werkstoff LuVTierMaterial = new Werkstoff(
            Materials.Chrome.getRGBA(),
            "Rhodium-Plated Palladium",
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(4500),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems().addMixerRecipes().addSimpleMetalWorkingItems().addCraftingMetalWorkingItems().addMultipleIngotMetalWorkingItems(),
            88,
            TextureSet.SET_METALLIC,
            new Pair<>(Materials.Palladium,3),
            new Pair<>(WerkstoffLoader.Rhodium,1)
    );
    public static final Werkstoff Tiberium = new Werkstoff(
            new short[]{0x22,0xEE,0x22},
            "Tiberium",
            "Tr",
            new Werkstoff.Stats().setProtons(123).setMass(326).setBlastFurnace(true).setMeltingPoint(1800).setRadioactive(true).setToxic(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().addGems().addCraftingMetalWorkingItems().addSimpleMetalWorkingItems(),
            89,
            TextureSet.SET_DIAMOND
    );
    public static final Werkstoff Ruridit = new Werkstoff(
            new short[]{0xA4,0xA4,0xA4},
            "Ruridit",
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(4500),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems().addMixerRecipes().addSimpleMetalWorkingItems().addCraftingMetalWorkingItems().addMultipleIngotMetalWorkingItems(),
            90,
            TextureSet.SET_METALLIC,
            new Pair<>(WerkstoffLoader.Ruthenium,2),
            new Pair<>(Materials.Iridium,1)
    );
    public static final Werkstoff Fluorspar = new Werkstoff(
            new short[]{185,69,251},
            "Fluorspar",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            91,
            TextureSet.SET_GEM_VERTICAL,
            new Pair<>(Materials.Calcium,1),
            new Pair<>(Materials.Fluorine,2)
    );
    public static final Werkstoff HDCS = new Werkstoff(
            new short[]{0x33,0x44,0x33},
            "High Durability Compound Steel",
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(9000),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems().addMixerRecipes().addSimpleMetalWorkingItems().addCraftingMetalWorkingItems().addMultipleIngotMetalWorkingItems(),
            92,
            TextureSet.SET_SHINY,
            new Pair<>(Materials.TungstenSteel,12),
            new Pair<>(Materials.HSSE,9),
            new Pair<>(Materials.HSSG,6),
            new Pair<>(WerkstoffLoader.Ruridit,3),
            new Pair<>(WerkstoffLoader.MagnetoResonaticDust,2),
            new Pair<>(Materials.Plutonium,1)
    );
    public static final Werkstoff Atheneite = new Werkstoff(
            new short[]{175,175,175},
            "Atheneite",
            subscriptNumbers("(Pd,Hg)3As"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            93,
            TextureSet.SET_SHINY,
            new Pair<>(WerkstoffLoader.PDMetallicPowder,3),
            new Pair<>(Materials.Mercury,3),
            new Pair<>(Materials.Arsenic,1)
    );
    public static final Werkstoff Temagamite = new Werkstoff(
            new short[]{245,245,245},
            "Temagamite",
            subscriptNumbers("Pd3HgTe"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            94,
            TextureSet.SET_ROUGH,
            new Pair<>(WerkstoffLoader.PDMetallicPowder,3),
            new Pair<>(Materials.Mercury,1),
            new Pair<>(Materials.Tellurium,1)
    );
    public static final Werkstoff Terlinguaite = new Werkstoff(
            new short[]{245,245,245},
            "Terlinguaite",
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            95,
            TextureSet.SET_GEM_HORIZONTAL,
            new Pair<>(Materials.Mercury,2),
            new Pair<>(Materials.Chlorine,1),
            new Pair<>(Materials.Oxygen,1)
    );
    public static final Werkstoff AdemicSteel = new Werkstoff(
            new short[]{0xcc,0xcc,0xcc},
            "Ademic Steel",
            "The break in the line",
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setDurOverride(6144).setMeltingPoint(1800).setSpeedOverride(12).setQualityOverride((byte) 4),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().onlyDust().addMetalItems().addCraftingMetalWorkingItems().addMolten().addSimpleMetalWorkingItems().addMultipleIngotMetalWorkingItems(),
            96,
            TextureSet.SET_METALLIC,
            new Pair<>(Materials.Steel ,2),
            new Pair<>(Materials.VanadiumSteel,1),
            new Pair<>(Materials.DamascusSteel,1),
            new Pair<>(Materials.Carbon,4)
    );
    public static final Werkstoff RawAdemicSteel = new Werkstoff(
            new short[]{0xed,0xed,0xed},
            "Raw Ademic Steel",
            new Werkstoff.Stats().setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().onlyDust().addMixerRecipes(),
            97,
            TextureSet.SET_ROUGH,
            new Pair<>(Materials.Steel ,2),
            new Pair<>(Materials.VanadiumSteel,1),
            new Pair<>(Materials.DamascusSteel,1)
    );

    public static HashMap<OrePrefixes, BW_MetaGenerated_Items> items = new HashMap<>();
    public static HashBiMap<Werkstoff, Fluid> fluids = HashBiMap.create();
    public static HashBiMap<Werkstoff, Fluid> molten = HashBiMap.create();
    public static Block BWOres;
    public static Block BWSmallOres;
    public static Block BWBlocks;
    public boolean registered;
    public static final HashSet<OrePrefixes> ENABLED_ORE_PREFIXES = new HashSet<>();

    public static Werkstoff getWerkstoff(String Name){
        try{
            Field f = WerkstoffLoader.class.getField(Name);
            if (f != null)
                return (Werkstoff) f.get(null);
        } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
            e.printStackTrace();
        }
        return Werkstoff.default_null_Werkstoff;
    }

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff) {
        return WerkstoffLoader.getCorrespondingItemStack(orePrefixes, werkstoff, 1);
    }

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff, int amount) {
        if (!werkstoff.getGenerationFeatures().enforceUnification) {
            ItemStack ret = OreDictHandler.getItemStack(werkstoff.getVarName(), orePrefixes, amount);
            if (ret != null)
                return ret;
        }
        if (orePrefixes == ore)
            return new ItemStack(WerkstoffLoader.BWOres, amount, werkstoff.getmID());
        else if (orePrefixes == oreSmall)
            return new ItemStack(WerkstoffLoader.BWSmallOres, amount, werkstoff.getmID());
        else if (orePrefixes == block)
            return new ItemStack(WerkstoffLoader.BWBlocks,amount,werkstoff.getmID());
        else if (WerkstoffLoader.items.get(orePrefixes) == null)
            MainMod.LOGGER.catching(Level.ERROR,new Exception("NO SUCH ITEM! "+orePrefixes+werkstoff.getVarName() +" If you encounter this as a user, make sure to contact the authors of the pack/the mods you're playing! " +
                    "If you are a Developer, you forgot to enable "+orePrefixes+" OrePrefix for Werkstoff "+werkstoff.getDefaultName()));
        return new ItemStack(WerkstoffLoader.items.get(orePrefixes), amount, werkstoff.getmID()).copy();
    }

    public void init() {
        if (WerkstoffLoader.INSTANCE == null) {
            MainMod.LOGGER.error("INSTANCE IS NULL THIS SHOULD NEVER HAPPEN!");
            FMLCommonHandler.instance().exitJava(1,true);
        }
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
            ProgressManager.ProgressBar progressBar = ProgressManager.push("Register BW Materials", Werkstoff.werkstoffHashSet.size()+1);
            DebugLog.log("Loading Recipes"+(System.nanoTime()-timepre));
            for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
                long timepreone = System.nanoTime();
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
                DebugLog.log("Loading Cell Recipes"+" " +(System.nanoTime()-timepreone));
                this.addCellRecipes(werkstoff);
                DebugLog.log("Loading Meltdown Recipes"+" " +(System.nanoTime()-timepreone));
                this.addMoltenRecipes(werkstoff);
                DebugLog.log("Loading Simple MetalWorking Recipes"+" " +(System.nanoTime()-timepreone));
                this.addSimpleMetalRecipes(werkstoff);
                DebugLog.log("Loading Crafting MetalWorking Recipes"+" " +(System.nanoTime()-timepreone));
                this.addCraftingMetalRecipes(werkstoff);
                DebugLog.log("Loading MultipleIngots MetalWorking Recipes"+" " +(System.nanoTime()-timepreone));
                this.addMultipleMetalRecipes(werkstoff);
                DebugLog.log("Loading Metal Recipes"+" " +(System.nanoTime()-timepreone));
                this.addMetalRecipes(werkstoff);
                DebugLog.log("Loading Tool Recipes"+" " +(System.nanoTime()-timepreone));
                this.addTools(werkstoff);
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

        WerkstoffLoader.CubicZirconia.getStats().setDurOverride(Materials.Diamond.mDurability);
        WerkstoffLoader.HDCS.getStats().setSpeedOverride(Materials.HSSS.mToolSpeed);
        WerkstoffLoader.HDCS.getStats().setDurMod(10f);
        Materials.Helium.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Neon.add(WerkstoffLoader.NOBLE_GAS);
        Materials.Argon.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Krypton.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Xenon.add(WerkstoffLoader.NOBLE_GAS,WerkstoffLoader.ANAEROBE_GAS);
        Materials.Radon.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Oganesson.add(WerkstoffLoader.NOBLE_GAS,WerkstoffLoader.ANAEROBE_GAS);


        Materials.Nitrogen.add(WerkstoffLoader.ANAEROBE_GAS);

        WerkstoffLoader.Calcium.add(WerkstoffLoader.ANAEROBE_SMELTING);

        WerkstoffLoader.LuVTierMaterial.add(WerkstoffLoader.NOBLE_GAS_SMELTING);
        WerkstoffLoader.Ruridit.add(WerkstoffLoader.NOBLE_GAS_SMELTING);
        WerkstoffLoader.AdemicSteel.add(WerkstoffLoader.NOBLE_GAS_SMELTING);

        WerkstoffLoader.MagnetoResonaticDust.add(WerkstoffLoader.NO_BLAST);

        //Calcium Smelting block
        Materials.Calcium.mBlastFurnaceRequired=true;

        Materials.Salt.mDurability = WerkstoffLoader.Salt.getDurability();
        Materials.Spodumene.mDurability = WerkstoffLoader.Spodumen.getDurability();
        Materials.RockSalt.mDurability = WerkstoffLoader.RockSalt.getDurability();
        Materials.Calcium.mDurability = WerkstoffLoader.Calcium.getDurability();

        Materials.Salt.mToolSpeed = WerkstoffLoader.Salt.getToolSpeed();
        Materials.Spodumene.mToolSpeed = WerkstoffLoader.Spodumen.getToolSpeed();
        Materials.RockSalt.mToolSpeed = WerkstoffLoader.RockSalt.getToolSpeed();
        Materials.Calcium.mToolSpeed = WerkstoffLoader.Calcium.getToolSpeed();

        Materials.Salt.mToolQuality = WerkstoffLoader.Salt.getToolQuality();
        Materials.Spodumene.mToolQuality = WerkstoffLoader.Spodumen.getToolQuality();
        Materials.RockSalt.mToolQuality = WerkstoffLoader.RockSalt.getToolQuality();
        Materials.Calcium.mToolQuality = WerkstoffLoader.Calcium.getToolQuality();

        for (Werkstoff W : Werkstoff.werkstoffHashSet) {
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
            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10000) != 0){
                if (!FluidRegistry.isFluidRegistered(werkstoff.getDefaultName())) {
                    DebugLog.log("Adding new Fluid: " + werkstoff.getDefaultName());
                    GT_Fluid fluid = (GT_Fluid) new GT_Fluid(werkstoff.getDefaultName(), "molten.autogenerated", werkstoff.getRGBA()).setGaseous(werkstoff.getStats().isGas());
                    FluidRegistry.registerFluid(fluid);
                    WerkstoffLoader.fluids.put(werkstoff, fluid);
                } else {
                    WerkstoffLoader.fluids.put(werkstoff, FluidRegistry.getFluid(werkstoff.getDefaultName()));
                }
            }
            if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000000) != 0){
                if (!FluidRegistry.isFluidRegistered("molten."+werkstoff.getDefaultName())) {
                    DebugLog.log("Adding new Molten: " + werkstoff.getDefaultName());
                    Fluid fluid = new GT_Fluid("molten." + werkstoff.getDefaultName(), "molten.autogenerated", werkstoff.getRGBA());
                    if (werkstoff.getStats().meltingPoint > 0)
                        fluid = fluid.setTemperature(werkstoff.getStats().meltingPoint);
                    FluidRegistry.registerFluid(fluid);
                    //GT_LanguageManager.addStringLocalization("Molten." + werkstoff.getDefaultName(), "Molten "+ werkstoff.getDefaultName());
                    GT_LanguageManager.addStringLocalization(fluid.getUnlocalizedName(), "Molten "+ werkstoff.getDefaultName());
                    WerkstoffLoader.molten.put(werkstoff, fluid);
                } else {
                    WerkstoffLoader.molten.put(werkstoff, FluidRegistry.getFluid(werkstoff.getDefaultName()));
                }
            }
            for (OrePrefixes p : values())
                if (!werkstoff.getGenerationFeatures().enforceUnification && (werkstoff.getGenerationFeatures().toGenerate & p.mMaterialGenerationBits) != 0 && OreDictHandler.getItemStack(werkstoff.getDefaultName(),p,1) != null) {
                    DebugLog.log("Found: "+(p+werkstoff.getVarName())+" in oreDict, disable and reroute my Items to that, also add a Tooltip.");
                    werkstoff.getGenerationFeatures().setBlacklist(p);
                }
            WerkstoffLoader.toGenerateGlobal = (WerkstoffLoader.toGenerateGlobal | werkstoff.getGenerationFeatures().toGenerate);
            //System.out.println(werkstoff.getDefaultName()+": "+werkstoff.getGenerationFeatures().toGenerate);
        }
        DebugLog.log("GlobalGeneration: "+WerkstoffLoader.toGenerateGlobal);
        if ((WerkstoffLoader.toGenerateGlobal & 0b1) != 0) {
            WerkstoffLoader.items.put(dust, new BW_MetaGenerated_Items(dust));
            WerkstoffLoader.items.put(dustTiny, new BW_MetaGenerated_Items(dustTiny));
            WerkstoffLoader.items.put(dustSmall, new BW_MetaGenerated_Items(dustSmall));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10) != 0) {
            WerkstoffLoader.items.put(ingot, new BW_MetaGenerated_Items(ingot));
            WerkstoffLoader.items.put(ingotHot, new BW_MetaGenerated_Items(ingotHot));    //1750
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
            //WerkstoffLoader.items.put(bottle, new BW_MetaGenerated_Items(bottle));
            if (Loader.isModLoaded("Forestry"))
                WerkstoffLoader.items.put(capsule, new BW_MetaGenerated_Items(capsule));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100000) != 0) {
            WerkstoffLoader.items.put(cellPlasma, new BW_MetaGenerated_Items(cellPlasma));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000000) != 0) {
            WerkstoffLoader.items.put(WerkstoffLoader.cellMolten, new BW_MetaGenerated_Items(WerkstoffLoader.cellMolten));
            if (Loader.isModLoaded("Forestry"))
                WerkstoffLoader.items.put(capsuleMolten, new BW_MetaGenerated_Items(capsuleMolten));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000000) != 0) {
            WerkstoffLoader.items.put(plate, new BW_MetaGenerated_Items(plate));
            WerkstoffLoader.items.put(stick, new BW_MetaGenerated_Items(stick));
            WerkstoffLoader.items.put(stickLong, new BW_MetaGenerated_Items(stickLong));
            WerkstoffLoader.items.put(toolHeadWrench, new BW_MetaGenerated_Items(toolHeadWrench));
            WerkstoffLoader.items.put(toolHeadHammer, new BW_MetaGenerated_Items(toolHeadHammer));
            WerkstoffLoader.items.put(toolHeadSaw, new BW_MetaGenerated_Items(toolHeadSaw));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100000000) != 0) {
            WerkstoffLoader.items.put(gearGt, new BW_MetaGenerated_Items(gearGt));
            WerkstoffLoader.items.put(gearGtSmall, new BW_MetaGenerated_Items(gearGtSmall));
            WerkstoffLoader.items.put(bolt, new BW_MetaGenerated_Items(bolt));
            WerkstoffLoader.items.put(screw, new BW_MetaGenerated_Items(screw));
            WerkstoffLoader.items.put(ring, new BW_MetaGenerated_Items(ring));
            WerkstoffLoader.items.put(spring, new BW_MetaGenerated_Items(spring));
            WerkstoffLoader.items.put(springSmall, new BW_MetaGenerated_Items(springSmall));
            WerkstoffLoader.items.put(rotor, new BW_MetaGenerated_Items(rotor));
            WerkstoffLoader.items.put(wireFine, new BW_MetaGenerated_Items(wireFine));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000000000) != 0) {
            WerkstoffLoader.items.put(plateDouble, new BW_MetaGenerated_Items(plateDouble));
            WerkstoffLoader.items.put(plateTriple, new BW_MetaGenerated_Items(plateTriple));
            WerkstoffLoader.items.put(plateQuadruple, new BW_MetaGenerated_Items(plateQuadruple));
            WerkstoffLoader.items.put(plateQuintuple, new BW_MetaGenerated_Items(plateQuintuple));
            WerkstoffLoader.items.put(plateDense, new BW_MetaGenerated_Items(plateDense));
            WerkstoffLoader.items.put(ingotDouble, new BW_MetaGenerated_Items(ingotDouble));
            WerkstoffLoader.items.put(ingotTriple, new BW_MetaGenerated_Items(ingotTriple));
            WerkstoffLoader.items.put(ingotQuadruple, new BW_MetaGenerated_Items(ingotQuadruple));
            WerkstoffLoader.items.put(ingotQuintuple, new BW_MetaGenerated_Items(ingotQuintuple));
        }
        ENABLED_ORE_PREFIXES.addAll(WerkstoffLoader.items.keySet());
        ENABLED_ORE_PREFIXES.add(ore);
        ENABLED_ORE_PREFIXES.add(oreSmall);
        WerkstoffLoader.runGTItemDataRegistrator();
    }

    void gameRegistryHandler(){
        if (FMLCommonHandler.instance().getSide().isClient())
            RenderingRegistry.registerBlockHandler(BW_Renderer_Block_Ores.INSTANCE);
        GameRegistry.registerTileEntity(BW_MetaGeneratedOreTE.class, "bw.blockoresTE");
        GameRegistry.registerTileEntity(BW_MetaGeneratedSmallOreTE.class, "bw.blockoresSmallTE");
        GameRegistry.registerTileEntity(BW_MetaGenerated_WerkstoffBlock_TE.class, "bw.werkstoffblockTE");
        WerkstoffLoader.BWOres = new BW_MetaGenerated_Ores(Material.rock, BW_MetaGeneratedOreTE.class, "bw.blockores");
        WerkstoffLoader.BWSmallOres = new BW_MetaGenerated_SmallOres(Material.rock, BW_MetaGeneratedSmallOreTE.class, "bw.blockoresSmall");
        WerkstoffLoader.BWBlocks = new BW_MetaGenerated_WerkstoffBlocks(Material.iron,BW_MetaGenerated_WerkstoffBlock_TE.class,"bw.werkstoffblocks");
        GameRegistry.registerBlock(WerkstoffLoader.BWOres, BW_MetaGeneratedBlock_Item.class, "bw.blockores.01");
        GameRegistry.registerBlock(WerkstoffLoader.BWSmallOres, BW_MetaGeneratedBlock_Item.class, "bw.blockores.02");
        GameRegistry.registerBlock(WerkstoffLoader.BWBlocks, BW_MetaGeneratedBlock_Item.class, "bw.werkstoffblocks.01");
        new GTMetaItemEnhancer();
    }

    private static void runGTItemDataRegistrator() {
       // HashSet<Materials> toRem = new HashSet<>();
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            //int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, String aConfigSection, boolean aCustomOre, String aCustomID
            Materials werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null ? werkstoff.getBridgeMaterial() : Materials.get(werkstoff.getVarName()) != Materials._NULL ? Materials.get(werkstoff.getVarName()) :
                    new Materials(-1, werkstoff.getTexSet(), werkstoff.getToolSpeed(), werkstoff.getDurability(), werkstoff.getToolQuality(),0, werkstoff.getRGBA()[0],werkstoff.getRGBA()[1], werkstoff.getRGBA()[2], werkstoff.getRGBA()[3], werkstoff.getVarName(), werkstoff.getDefaultName(),0,0,werkstoff.getStats().meltingPoint,werkstoff.getStats().meltingPoint,werkstoff.getStats().isBlastFurnace(),false,0,1,1,null);
            for (OrePrefixes prefixes : values()) {
                if (!(prefixes == cell && werkstoff.getType().equals(Werkstoff.Types.ELEMENT))) {
                    if (prefixes == dust && werkstoff.getType().equals(Werkstoff.Types.ELEMENT)) {
                        if (werkstoff.getType().equals(Werkstoff.Types.ELEMENT)) {
                            boolean ElementSet = false;
                            for (Element e : Element.values()) {
                                if (e.toString().equals(werkstoff.getToolTip())) {
                                    if (e.mLinkedMaterials.size() > 0)
                                        break;
                                    werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null ? werkstoff.getBridgeMaterial() : Materials.get(werkstoff.getVarName()) != Materials._NULL ? Materials.get(werkstoff.getVarName()) :
                                            new Materials(-1, werkstoff.getTexSet(), werkstoff.getToolSpeed(), werkstoff.getDurability(), werkstoff.getToolQuality(),0, werkstoff.getRGBA()[0],werkstoff.getRGBA()[1], werkstoff.getRGBA()[2], werkstoff.getRGBA()[3], werkstoff.getVarName(), werkstoff.getDefaultName(),0,0,werkstoff.getStats().meltingPoint,werkstoff.getStats().meltingPoint,werkstoff.getStats().isBlastFurnace(),false,0,1,1,null);
                                    werkstoffBridgeMaterial.mElement = e;
                                    e.mLinkedMaterials = new ArrayList<>();
                                    e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                                    if (((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(dust)) != 0 && (werkstoff.getGenerationFeatures().blacklist & Werkstoff.GenerationFeatures.prefixLogic.get(dust)) == 0 && werkstoff.get(dust) != null && werkstoff.get(dust).getItem() != null)) {
                                        GT_OreDictUnificator.addAssociation(dust, werkstoffBridgeMaterial, werkstoff.get(dust), false);
                                        GT_OreDictUnificator.set(dust, werkstoffBridgeMaterial, werkstoff.get(dust), true, true);
                                    }
                                    ElementSet = true;
                                    break;
                                }
                            }
                            if (!ElementSet)
                                continue;
//                            try {
//                                Field f = Materials.class.getDeclaredField("MATERIALS_MAP");
//                                f.setAccessible(true);
//                                Map<String, Materials> MATERIALS_MAP = (Map<String, Materials>) f.get(null);
//                                MATERIALS_MAP.remove(werkstoffBridgeMaterial.mName);
//                            } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
//                                e.printStackTrace();
//                            }
                            if (((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(dust)) != 0 && (werkstoff.getGenerationFeatures().blacklist & Werkstoff.GenerationFeatures.prefixLogic.get(dust)) == 0 && werkstoff.get(dust) != null && werkstoff.get(dust).getItem() != null)) {
                                ItemStack scannerOutput = ItemList.Tool_DataOrb.get(1L);
                                Behaviour_DataOrb.setDataTitle(scannerOutput, "Elemental-Scan");
                                Behaviour_DataOrb.setDataName(scannerOutput, werkstoff.getToolTip());
                                GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(false, new BWRecipes.DynamicGTRecipe(false, new ItemStack[]{werkstoff.get(prefixes)}, new ItemStack[]{scannerOutput}, ItemList.Tool_DataOrb.get(1L), null, null, null, (int) (werkstoffBridgeMaterial.getMass() * 8192L), 30, 0));
                                GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(false, new BWRecipes.DynamicGTRecipe(false, null, new ItemStack[]{werkstoff.get(prefixes)}, scannerOutput, null, new FluidStack[]{Materials.UUMatter.getFluid(werkstoffBridgeMaterial.getMass())}, null, (int) (werkstoffBridgeMaterial.getMass() * 512L), 30, 0));
                            }
                        }
                    }

                    if (werkstoff.getGenerationFeatures().hasCells()) {
                        werkstoffBridgeMaterial.setHasCorrespondingFluid(true);
                        werkstoffBridgeMaterial.setHasCorrespondingGas(true);
                        werkstoffBridgeMaterial.mFluid = werkstoff.getFluidOrGas(1).getFluid();
                        werkstoffBridgeMaterial.mGas = werkstoff.getFluidOrGas(1).getFluid();
                    }
                    if (werkstoff.getGenerationFeatures().hasMolten()) {
                        werkstoffBridgeMaterial.mStandardMoltenFluid = werkstoff.getMolten(1).getFluid();
                    }
                    werkstoffBridgeMaterial.mName = werkstoff.getVarName();
                    werkstoffBridgeMaterial.mDefaultLocalName = werkstoff.getDefaultName();
                    try {
                        Field f = Materials.class.getField("mLocalizedName");
                        f.set(werkstoffBridgeMaterial, werkstoff.getDefaultName());
                    } catch (NoSuchFieldException | IllegalAccessException ignored){}
                    werkstoffBridgeMaterial.mChemicalFormula = werkstoff.getToolTip();
                    if (Loader.isModLoaded("Thaumcraft"))
                        werkstoffBridgeMaterial.mAspects = werkstoff.getGTWrappedTCAspects();
                    werkstoffBridgeMaterial.mMaterialInto = werkstoffBridgeMaterial;
                    werkstoffBridgeMaterial.mHandleMaterial = werkstoff.contains(SubTag.BURNING) ? Materials.Blaze : werkstoff.contains(SubTag.MAGICAL) ? Materials.Thaumium : werkstoffBridgeMaterial.mDurability > 5120 ? Materials.TungstenSteel : werkstoffBridgeMaterial.mDurability > 1280 ? Materials.Steel : Materials.Wood;
                    //toRem.add(werkstoffBridgeMaterial);
                    if (werkstoff.getStats().isRadioactive()){
                        werkstoffBridgeMaterial.setEnchantmentForArmors(Enchantment_Radioactivity.INSTANCE,werkstoff.getStats().getEnchantmentlvl());
                        werkstoffBridgeMaterial.setEnchantmentForTools(Enchantment_Radioactivity.INSTANCE,werkstoff.getStats().getEnchantmentlvl());
                    }
                    werkstoff.setBridgeMaterial(werkstoffBridgeMaterial);
                    if (WerkstoffLoader.items.get(prefixes) != null)
                        if ((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(prefixes)) != 0 && (werkstoff.getGenerationFeatures().blacklist & Werkstoff.GenerationFeatures.prefixLogic.get(prefixes)) == 0 && werkstoff.get(prefixes) != null && werkstoff.get(prefixes).getItem() != null)
                            GT_OreDictUnificator.addAssociation(prefixes, werkstoffBridgeMaterial, werkstoff.get(prefixes), false);
                }
            }
        }
//        try {
//            Field f = Materials.class.getDeclaredField("MATERIALS_MAP");
//            f.setAccessible(true);
//            Map<String, Materials> MATERIALS_MAP = (Map<String, Materials>) f.get(null);
//            for (Materials o : toRem)
//                MATERIALS_MAP.remove(o.mName);
//        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
//            e.printStackTrace();
//        }
    }

    private void addTools(Werkstoff werkstoff){
        if (werkstoff.getBridgeMaterial().mDurability == 0)
            return;

        if (werkstoff.getGenerationFeatures().hasGems()){
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial().mHandleMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"XX ", "XXS", "XX ", 'X',  OrePrefixes.gem.get(werkstoff.getBridgeMaterial()), 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)});
            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, werkstoff.getBridgeMaterial(), 1L), GT_Proxy.tBits, new Object[]{"GGf", 'G', OrePrefixes.gem.get(werkstoff.getBridgeMaterial())});
        }

        if (!werkstoff.getGenerationFeatures().hasSimpleMetalWorkingItems())
            return;

        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.PLUNGER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"xRR", " SR", "S f", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'R', OrePrefixes.plate.get(Materials.AnyRubber)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"IhI", "III", " I ", 'I', OrePrefixes.ingot.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"hDS", "DSD", "SDf", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'D', Dyes.dyeBlue});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial().mHandleMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{" fS", " Sh", "W  ", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'W', OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.WIRECUTTER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PfP", "hPd", "STS", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'T', OrePrefixes.screw.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCOOP, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SWS", "SSS", "xSh", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'W', new ItemStack(Blocks.wool, 1, 32767)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.BRANCHCUTTER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PfP", "PdP", "STS", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'T', OrePrefixes.screw.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.KNIFE, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"fPh", " S ", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.BUTCHERYKNIFE, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial(), null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPf", "PP ", "Sh ", 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV, 1, werkstoff.getBridgeMaterial(), Materials.Rubber, new long[]{100000L, 32L, 1L, -1L}), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(Materials.AnyRubber), 'S', OrePrefixes.stick.get(Materials.Iron), 'L', ItemList.Battery_RE_LV_Lithium.get(1L)});
        if (gtnhGT) {
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(162, 1, werkstoff.getBridgeMaterial(), Materials.Rubber, new long[]{400000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(Materials.AnyRubber), 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L', ItemList.Battery_RE_MV_Lithium.get(1L)});
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(164, 1, werkstoff.getBridgeMaterial(), Materials.StyreneButadieneRubber, new long[]{1600000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(werkstoff.getBridgeMaterial()), 'P', OrePrefixes.plate.get(Materials.StyreneButadieneRubber), 'S', OrePrefixes.stick.get(Materials.StainlessSteel), 'L', ItemList.Battery_RE_HV_Lithium.get(1L)});
        }
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_LV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[]{ 100000L, 32L,  1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'G', OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_LV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[]{  75000L, 32L,  1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'G', OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_LV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[]{  50000L, 32L,  1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'G', OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_MV, 1, werkstoff.getBridgeMaterial(), Materials.Titanium,       new long[]{ 400000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Titanium      ), 'P', OrePrefixes.plate.get(Materials.Titanium      ), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium      ), 'B', ItemList.Battery_RE_MV_Lithium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_MV, 1, werkstoff.getBridgeMaterial(), Materials.Titanium,       new long[]{ 300000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Titanium      ), 'P', OrePrefixes.plate.get(Materials.Titanium      ), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium      ), 'B', ItemList.Battery_RE_MV_Cadmium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_MV, 1, werkstoff.getBridgeMaterial(), Materials.Titanium,       new long[]{ 200000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Titanium      ), 'P', OrePrefixes.plate.get(Materials.Titanium      ), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium      ), 'B', ItemList.Battery_RE_MV_Sodium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_HV, 1, werkstoff.getBridgeMaterial(), Materials.TungstenSteel,  new long[]{1600000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.TungstenSteel ), 'P', OrePrefixes.plate.get(Materials.TungstenSteel ), 'G', OrePrefixes.gearGtSmall.get(Materials.TungstenSteel ), 'B', ItemList.Battery_RE_HV_Lithium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_HV, 1, werkstoff.getBridgeMaterial(), Materials.TungstenSteel,  new long[]{1200000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.TungstenSteel ), 'P', OrePrefixes.plate.get(Materials.TungstenSteel ), 'G', OrePrefixes.gearGtSmall.get(Materials.TungstenSteel ), 'B', ItemList.Battery_RE_HV_Cadmium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.     WRENCH_HV, 1, werkstoff.getBridgeMaterial(), Materials.TungstenSteel,  new long[]{ 800000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.TungstenSteel ), 'P', OrePrefixes.plate.get(Materials.TungstenSteel ), 'G', OrePrefixes.gearGtSmall.get(Materials.TungstenSteel ), 'B', ItemList.Battery_RE_HV_Sodium.get(1L)});

        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER_LV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[]{ 100000L, 32L,  1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'G', OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER_LV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[]{  75000L, 32L,  1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'G', OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L)});
        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER_LV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[]{  50000L, 32L,  1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'G', OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L)});
        if (gtnhGT) {
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(152, 1, werkstoff.getBridgeMaterial(), Materials.Titanium,       new long[]{ 400000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Titanium      ), 'P', OrePrefixes.plate.get(Materials.Titanium      ), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium      ), 'B', ItemList.Battery_RE_MV_Lithium.get(1L)});
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(152, 1, werkstoff.getBridgeMaterial(), Materials.Titanium,       new long[]{ 300000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Titanium      ), 'P', OrePrefixes.plate.get(Materials.Titanium      ), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium      ), 'B', ItemList.Battery_RE_MV_Cadmium.get(1L)});
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(152, 1, werkstoff.getBridgeMaterial(), Materials.Titanium,       new long[]{ 200000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Titanium      ), 'P', OrePrefixes.plate.get(Materials.Titanium      ), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium      ), 'B', ItemList.Battery_RE_MV_Sodium.get(1L)});
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(154, 1, werkstoff.getBridgeMaterial(), Materials.TungstenSteel,  new long[]{1600000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.TungstenSteel ), 'P', OrePrefixes.plate.get(Materials.TungstenSteel ), 'G', OrePrefixes.gearGtSmall.get(Materials.TungstenSteel ), 'B', ItemList.Battery_RE_HV_Lithium.get(1L)});
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(154, 1, werkstoff.getBridgeMaterial(), Materials.TungstenSteel,  new long[]{1200000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.TungstenSteel ), 'P', OrePrefixes.plate.get(Materials.TungstenSteel ), 'G', OrePrefixes.gearGtSmall.get(Materials.TungstenSteel ), 'B', ItemList.Battery_RE_HV_Cadmium.get(1L)});
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(154, 1, werkstoff.getBridgeMaterial(), Materials.TungstenSteel,  new long[]{ 800000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()), 'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.TungstenSteel ), 'P', OrePrefixes.plate.get(Materials.TungstenSteel ), 'G', OrePrefixes.gearGtSmall.get(Materials.TungstenSteel ), 'B', ItemList.Battery_RE_HV_Sodium.get(1L)});
        }
        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, werkstoff.getBridgeMaterial(), 1L), GT_Proxy.tBits, new Object[]{"II ", "IIh", "II ", 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'I', OrePrefixes.ingot.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadWrench, werkstoff.getBridgeMaterial(), 1L), GT_Proxy.tBits, new Object[]{"hXW", "XRX", "WXd", 'X', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'S', OrePrefixes.plate.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'R', OrePrefixes.ring.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'W', OrePrefixes.screw.get(werkstoff.getBridgeMaterial().mHandleMaterial)});

        GT_ModHandler.addShapelessCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial().mHandleMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{toolHeadHammer.get(werkstoff.getBridgeMaterial()), OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)});

        if (!werkstoff.getGenerationFeatures().hasGems()) {
            GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial().mHandleMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"XX ", "XXS", "XX ", 'X', OrePrefixes.ingot.get(werkstoff.getBridgeMaterial()), 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)});
            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, werkstoff.getBridgeMaterial(), 1L), GT_Proxy.tBits, new Object[]{"PP ", "fh ", 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'I', OrePrefixes.ingot.get(werkstoff.getBridgeMaterial())});
        }

        GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.FILE, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial().mHandleMaterial, null), GT_ModHandler.RecipeBits.MIRRORED | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"P", "P", "S", 'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'S', OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)});

        GT_ModHandler.addShapelessCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SAW, 1, werkstoff.getBridgeMaterial(), werkstoff.getBridgeMaterial().mHandleMaterial, null), new Object[]{toolHeadSaw.get(werkstoff.getBridgeMaterial()), OrePrefixes.stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)});

        // GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(16,1, werkstoff.getBridgeMaterial(),werkstoff.getBridgeMaterial(),null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"IhI", "III", " I ", 'I', werkstoff.get(OrePrefixes.ingot)});
    }

    public static void removeIC2Recipes() {
        try {
            Set<Map.Entry<IRecipeInput, RecipeOutput>> remset = new HashSet<>();
            for (Map.Entry<IRecipeInput, RecipeOutput> curr : Recipes.macerator.getRecipes().entrySet()) {
                if (curr.getKey() instanceof RecipeInputOreDict) {
                    if (((RecipeInputOreDict) curr.getKey()).input.equalsIgnoreCase("oreNULL")) {
                        remset.add(curr);
                    }
                    for (ItemStack stack : curr.getValue().items){
                        if (stack.getItem() instanceof BW_MetaGenerated_Items)
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
                    OreDictAdder.addToMap(new Pair<>(ore + werkstoff.getVarName(), werkstoff.get(ore)));
                if (werkstoff.getGenerationFeatures().hasGems())
                    OreDictAdder.addToMap(new Pair<>("craftingLens" + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""), werkstoff.get(lens)));
                if (werkstoff.getADDITIONAL_OREDICT().size() > 0)
                    werkstoff.getADDITIONAL_OREDICT()
                        .forEach(s -> ENABLED_ORE_PREFIXES
                                .stream()
                                .filter(o -> Objects.nonNull(werkstoff.get(o)))
                                .forEach( od -> OreDictionary.registerOre(od+s, werkstoff.get(od))));
            }
            OreDictAdder.addToMap(new Pair<>("craftingIndustrialDiamond", WerkstoffLoader.CubicZirconia.get(gemExquisite)));
        } else {
            for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
                if (werkstoff.getGenerationFeatures().hasOres()) {
                    GT_OreDictUnificator.registerOre(ore + werkstoff.getVarName(), werkstoff.get(ore));
                    GT_OreDictUnificator.registerOre(oreSmall + werkstoff.getVarName(), werkstoff.get(oreSmall));
                }

                if (werkstoff.getGenerationFeatures().hasGems())
                    OreDictionary.registerOre("craftingLens" + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""), werkstoff.get(lens));

                if (werkstoff.getGenerationFeatures().hasGems() || (werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0){
                    GT_OreDictUnificator.registerOre(block + werkstoff.getVarName(), werkstoff.get(block));
                }

                werkstoff.getADDITIONAL_OREDICT()
                        .forEach(s -> ENABLED_ORE_PREFIXES
                                 .stream()
                                 .filter(o -> Objects.nonNull(werkstoff.get(o)))
                                 .forEach( od -> OreDictionary.registerOre(od+s, werkstoff.get(od))));
            }

            GT_OreDictUnificator.registerOre("craftingIndustrialDiamond", WerkstoffLoader.CubicZirconia.get(gemExquisite));
        }
    }

    private void addGemRecipes(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().hasGems()) {
            if (werkstoff.getGenerationFeatures().hasSifterRecipes() || ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) != 0 && (werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0)) {

                GT_ModHandler.addCompressionRecipe(werkstoff.get(gem, 9), werkstoff.get(block));
                GT_Values.RA.addForgeHammerRecipe(werkstoff.get(block),werkstoff.get(gem, 9), 100, 24);
                GT_Values.RA.addSifterRecipe(
                        werkstoff.get(crushedPurified),
                        new ItemStack[]{
                                werkstoff.get(gemExquisite),
                                werkstoff.get(gemFlawless),
                                werkstoff.get(gem),
                                werkstoff.get(gemFlawed),
                                werkstoff.get(gemChipped),
                                werkstoff.get(dust)
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
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawed), werkstoff.get(dustSmall, 2));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemChipped), werkstoff.get(dustSmall));

            GT_ModHandler.addCraftingRecipe(werkstoff.get(gemFlawless, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gemExquisite)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gem, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gemFlawless)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gemFlawed, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gem)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gemChipped, 2), 0, new Object[]{"h  ", "W  ", 'W', werkstoff.get(gemFlawed)});

            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemExquisite), werkstoff.get(gemFlawless, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawless), werkstoff.get(gem, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gem), werkstoff.get(gemFlawed, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawed), werkstoff.get(gemChipped, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemChipped), werkstoff.get(dustTiny), 64, 16);

            if (!werkstoff.contains(WerkstoffLoader.NO_BLAST)) {
                GT_Values.RA.addImplosionRecipe(werkstoff.get(gemFlawless, 3), 8, werkstoff.get(gemExquisite), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
                GT_Values.RA.addImplosionRecipe(werkstoff.get(gem, 3), 8, werkstoff.get(gemFlawless), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
                GT_Values.RA.addImplosionRecipe(werkstoff.get(gemFlawed, 3), 8, werkstoff.get(gem), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
                GT_Values.RA.addImplosionRecipe(werkstoff.get(gemChipped, 3), 8, werkstoff.get(gemFlawed), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));

                GT_Values.RA.addImplosionRecipe(werkstoff.get(dust, 4), 24, werkstoff.get(gem, 3), GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 8));
            }

            if (werkstoff.hasItemType(plate)) {
                GT_Values.RA.addLatheRecipe(werkstoff.get(plate), werkstoff.get(lens), werkstoff.get(dustSmall), 1200, 120);
                GT_Values.RA.addCutterRecipe(werkstoff.get(block), werkstoff.get(plate,9), null, (int) Math.max(werkstoff.getStats().getMass() * 10L, 1L), 30);
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

    private void addSimpleMetalRecipes(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(plate)) {
            if (werkstoff.hasItemType(gem)) {
                GT_Values.RA.addLatheRecipe(werkstoff.get(gem), werkstoff.get(stick), werkstoff.get(dustSmall), (int) Math.max(werkstoff.getStats().getMass() * 5L, 1L), 16);
                GT_ModHandler.addCraftingRecipe(werkstoff.get(stick, 2), GT_Proxy.tBits, new Object[]{"s", "X", 'X', werkstoff.get(stickLong)});
                GT_ModHandler.addCraftingRecipe(werkstoff.get(stick), GT_Proxy.tBits, new Object[]{"f ", " X", 'X', werkstoff.get(gem)});
                GT_Values.RA.addForgeHammerRecipe(werkstoff.get(stick, 2), werkstoff.get(stickLong), (int) Math.max(werkstoff.getStats().getMass(), 1L), 16);
                return;
            }

            GT_ModHandler.addCraftingRecipe(werkstoff.get(stick, 2), GT_Proxy.tBits, new Object[]{"s", "X", 'X', werkstoff.get(stickLong)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(stick), GT_Proxy.tBits, new Object[]{"f ", " X", 'X', werkstoff.get(ingot)});
            GT_ModHandler.addCraftingRecipe(werkstoff.get(plate), GT_Proxy.tBits, new Object[]{"h", "X", "X", 'X', werkstoff.get(ingot)});

            GT_Recipe.GT_Recipe_Map.sBenderRecipes.add(new BWRecipes.DynamicGTRecipe(true,new ItemStack[]{werkstoff.get(ingot),GT_Utility.getIntegratedCircuit(1)},new ItemStack[]{werkstoff.get(plate)},null,null,null,null, (int) Math.max(werkstoff.getStats().getMass(), 1L), 24,0));
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(ingot,3), werkstoff.get(plate,2), (int) Math.max(werkstoff.getStats().getMass(), 1L), 16);
            GregTech_API.registerCover(werkstoff.get(plate), new GT_RenderedTexture(werkstoff.getTexSet().mTextures[71], werkstoff.getRGBA(), false), null);

            GT_Values.RA.addLatheRecipe(werkstoff.get(ingot), werkstoff.get(stick), werkstoff.get(dustSmall), (int) Math.max(werkstoff.getStats().getMass() * 5L, 1L), 16);

            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(stick, 2), werkstoff.get(stickLong), (int) Math.max(werkstoff.getStats().getMass(), 1L), 16);

            GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot),ItemList.Shape_Extruder_Plate.get(0),werkstoff.get(plate),(int) Math.max(werkstoff.getStats().getMass() * 2L, 1L), 45);
            GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot),ItemList.Shape_Extruder_Rod.get(0),werkstoff.get(stick,2),(int) Math.max(werkstoff.getStats().getMass() * 2L, 1L), 45);

            GT_Values.RA.addPulveriserRecipe(werkstoff.get(ingot),new ItemStack[]{werkstoff.get(dust)},null,2,8);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(plate),new ItemStack[]{werkstoff.get(dust)},null,2,8);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(stickLong),new ItemStack[]{werkstoff.get(dust)},null,2,8);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(stick),new ItemStack[]{werkstoff.get(dustSmall,2)},null,2,8);
        }
    }

    private void addCraftingMetalRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(screw)) != 0) {
            int tVoltageMultiplier = werkstoff.getStats().meltingPoint >= 2800 ? 60 : 15;

            //bolt
            GT_Values.RA.addExtruderRecipe(werkstoff.getGenerationFeatures().hasGems() ? werkstoff.get(gem) : werkstoff.get(ingot), ItemList.Shape_Extruder_Bolt.get(0L), werkstoff.get(bolt,8), (int) Math.max(werkstoff.getStats().getMass() * 2L, 1), 8 * tVoltageMultiplier);
            GT_Values.RA.addCutterRecipe(werkstoff.get(stick), werkstoff.get(bolt,4), null, (int) Math.max(werkstoff.getStats().getMass() * 2L, 1L), 4);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(bolt),new ItemStack[]{werkstoff.get(dustTiny,1)},null,2,8);

            //screw
            GT_Values.RA.addLatheRecipe(werkstoff.get(bolt), werkstoff.get(screw),null, (int) Math.max(werkstoff.getStats().getMass() / 8L, 1L), 4);
            GT_ModHandler.addCraftingRecipe(werkstoff.get(screw), GT_Proxy.tBits, new Object[]{"fX", "X ", 'X', werkstoff.get(bolt)});
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(screw),new ItemStack[]{werkstoff.get(dustTiny,1)},null,2,8);

            if (werkstoff.getGenerationFeatures().hasGems())
                return;

            //ring
            GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot), ItemList.Shape_Extruder_Ring.get(0L), werkstoff.get(ring,4), (int) Math.max(werkstoff.getStats().getMass() * 2L, 1), 6 * tVoltageMultiplier);
            GT_ModHandler.addCraftingRecipe(werkstoff.get(ring), GT_Proxy.tBits, new Object[]{"h ", "fX", 'X', werkstoff.get(stick)});

            //Gear
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gearGt), GT_Proxy.tBits, new Object[]{"SPS", "PwP", "SPS", 'P', werkstoff.get(plate), 'S', werkstoff.get(stick)});
            GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot,4), ItemList.Shape_Extruder_Gear.get(0L), werkstoff.get(gearGt), (int) Math.max(werkstoff.getStats().getMass() * 5L, 1), 8 * tVoltageMultiplier);

            //wireFine
            GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot), ItemList.Shape_Extruder_Wire.get(0L), werkstoff.get(wireFine,8), (int) Math.max(werkstoff.getStats().getMass() * 1.5F, 1F), 8 * tVoltageMultiplier);
            GT_Values.RA.addWiremillRecipe(werkstoff.get(ingot), werkstoff.get(wireFine,8), (int) Math.max(werkstoff.getStats().getMass(), 1), 8 * tVoltageMultiplier);
            GT_Values.RA.addWiremillRecipe(werkstoff.get(stick), werkstoff.get(wireFine,4), (int) Math.max(werkstoff.getStats().getMass() * 0.5F, 1F), 8 * tVoltageMultiplier);

            //smallGear
            if (WerkstoffLoader.smallGearShape != null)
                GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot), WerkstoffLoader.smallGearShape.get(0L), werkstoff.get(gearGtSmall), (int) werkstoff.getStats().mass, 8 * tVoltageMultiplier);
            if (ConfigHandler.GTNH)
                GT_ModHandler.addCraftingRecipe(werkstoff.get(gearGtSmall), GT_Proxy.tBits, new Object[]{" S ", "hPx"," S ", 'S', werkstoff.get(stick), 'P', werkstoff.get(plate)});
            else
                GT_ModHandler.addCraftingRecipe(werkstoff.get(gearGtSmall), GT_Proxy.tBits, new Object[]{"P  "," h ", 'P', werkstoff.get(plate)});

            //Rotor
            GT_ModHandler.addCraftingRecipe(werkstoff.get(rotor), GT_Proxy.tBits, new Object[]{"PhP", "SRf", "PdP", 'P', werkstoff.get(plate), 'R', werkstoff.get(ring), 'S', werkstoff.get(screw)});
            GT_Values.RA.addAssemblerRecipe(werkstoff.get(plate,4), werkstoff.get(ring), Materials.Tin.getMolten(32), werkstoff.get(rotor), 240, 24);
            GT_Values.RA.addAssemblerRecipe(werkstoff.get(plate,4), werkstoff.get(ring), Materials.Lead.getMolten(48), werkstoff.get(rotor), 240, 24);
            GT_Values.RA.addAssemblerRecipe(werkstoff.get(plate,4), werkstoff.get(ring), Materials.SolderingAlloy.getMolten(16), werkstoff.get(rotor), 240, 24);

            if (WerkstoffLoader.rotorShape != null)
                GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot,5), WerkstoffLoader.rotorShape.get(0L), werkstoff.get(rotor), 200, 60);

            //molten -> metal
            if (werkstoff.getGenerationFeatures().hasMolten()) {
                GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Gear.get(0L), werkstoff.getMolten(576), werkstoff.get(gearGt), 128, 8);
                GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Gear_Small.get(0L), werkstoff.getMolten(144), werkstoff.get(gearGtSmall), 16, 8);
                if (WerkstoffLoader.ringMold != null)
                    GT_Values.RA.addFluidSolidifierRecipe(WerkstoffLoader.ringMold.get(0L), werkstoff.getMolten(36), werkstoff.get(ring), 100, 4 * tVoltageMultiplier);
                if (WerkstoffLoader.boltMold != null)
                    GT_Values.RA.addFluidSolidifierRecipe(WerkstoffLoader.boltMold.get(0L), werkstoff.getMolten(18), werkstoff.get(bolt), 50, 2 * tVoltageMultiplier);

                if (WerkstoffLoader.rotorMold != null)
                    GT_Values.RA.addFluidSolidifierRecipe(WerkstoffLoader.rotorMold.get(0L), werkstoff.getMolten(612), werkstoff.get(rotor), 100, 60);
            }

            GT_Values.RA.addPulveriserRecipe(werkstoff.get(gearGt),new ItemStack[]{werkstoff.get(dust,4)},null,2,8);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(gearGtSmall),new ItemStack[]{werkstoff.get(dust,1)},null,2,8);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(rotor),new ItemStack[]{werkstoff.get(dust,4),werkstoff.get(dustSmall)},null,2,8);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(ring),new ItemStack[]{werkstoff.get(dustSmall,1)},null,2,8);
        }
    }

    private void addMetalRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(ingot)) != 0) {
            GT_ModHandler.addCompressionRecipe(werkstoff.get(ingot, 9), werkstoff.get(block));
            GT_Values.RA.addExtruderRecipe(werkstoff.get(ingot, 9),ItemList.Shape_Extruder_Block.get(0), werkstoff.get(block), (int) werkstoff.getStats().mass, 8 * werkstoff.getStats().getMeltingPoint() >= 2800 ? 60 : 15);
            GT_Values.RA.addAlloySmelterRecipe(werkstoff.get(ingot, 9), ItemList.Shape_Mold_Block.get(0L), werkstoff.get(block), (int) (werkstoff.getStats().mass/2), 4 * werkstoff.getStats().getMeltingPoint() >= 2800 ? 60 : 15);
        }
    }

    private void addMultipleMetalRecipes(Werkstoff werkstoff){
        if ((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(plateDense)) != 0) {
            GT_Recipe.GT_Recipe_Map.sBenderRecipes.add(new BWRecipes.DynamicGTRecipe(true,new ItemStack[]{werkstoff.get(ingot,2),GT_Utility.getIntegratedCircuit(2)},new ItemStack[]{werkstoff.get(plateDouble)},null,null,null,null, (int) Math.max(werkstoff.getStats().getMass() * 2, 1L), 60,0));
            GregTech_API.registerCover(werkstoff.get(plateDouble), new GT_RenderedTexture(werkstoff.getTexSet().mTextures[72], werkstoff.getRGBA(), false), null);
            GT_Values.RA.addPulveriserRecipe(werkstoff.get(plateDouble),new ItemStack[]{werkstoff.get(dust,2)},null,2,8);
        }
    }

    private void addDustRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0) {
            List<FluidStack> flOutputs = new ArrayList<>();
            List<ItemStack> stOutputs = new ArrayList<>();
            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
            int cells = 0;

            if (werkstoff.getGenerationFeatures().hasMixerRecipes() || werkstoff.getStats().isElektrolysis() || werkstoff.getStats().isCentrifuge() || werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
                for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents().getValue().toArray(new Pair[0])) {
                    if (container.getKey() instanceof Materials) {
                        if (((Materials) container.getKey()).getGas(0) != null || ((Materials) container.getKey()).getFluid(0) != null || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID) {
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
                            if (((Materials) container.getKey()).getDust(container.getValue()) == null ) {
                                if (((Materials) container.getKey()).getCells(container.getValue()) != null && (((Materials) container.getKey()).getMolten(0) != null || ((Materials) container.getKey()).getSolid(0) != null)) {
                                    FluidStack tmpFl = ((Materials) container.getKey()).getMolten(1000 * container.getValue());
                                    if (tmpFl == null || tmpFl.getFluid() == null) {
                                        tmpFl = ((Materials) container.getKey()).getSolid(1000 * container.getValue());
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
                                } else
                                    continue;
                            }
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Materials) container.getKey()).getDust(container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Materials) container.getKey()).getDust(tracker.get(container.getKey()).getKey() + container.getValue()));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    } else if (container.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) container.getKey()).getStats().isGas() || ((Werkstoff) container.getKey()).getGenerationFeatures().hasCells()) {
                            FluidStack tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Werkstoff) container.getKey()).get(cell, container.getValue()));
                                    tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(((Werkstoff) container.getKey()).get(cell, tracker.get(container.getKey()).getKey() + container.getValue()));
                                    stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (!((Werkstoff) container.getKey()).getGenerationFeatures().hasDusts())
                               continue;
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
                ItemStack input = werkstoff.get(dust);
                input.stackSize = werkstoff.getContents().getKey();
                if (werkstoff.getStats().isElektrolysis())
                    GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.add(new BWRecipes.DynamicGTRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().protons / werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30, 0));
                if (werkstoff.getStats().isCentrifuge())
                    GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.add(new BWRecipes.DynamicGTRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().mass / werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5, 0));
                if (werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
                    if (cells > 0)
                        stOutputs.add(Materials.Empty.getCells(cells));
                    GT_Recipe.GT_Recipe_Map.sChemicalRecipes.add(new BWRecipes.DynamicGTRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().protons / werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30,0));
                    GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.addRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().protons / werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30,0);
                }
                if (werkstoff.getGenerationFeatures().hasMixerRecipes()) {
                    if (cells > 0)
                        stOutputs.add(Materials.Empty.getCells(cells));
                    GT_Recipe.GT_Recipe_Map.sMixerRecipes.add(new BWRecipes.DynamicGTRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().mass / werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5,0));
                }
            }

            GT_ModHandler.addCraftingRecipe(werkstoff.get(dust),  new Object[]{
                    "TTT","TTT","TTT",'T',
                    werkstoff.get(dustTiny)
            });
            GT_ModHandler.addCraftingRecipe(werkstoff.get(dust),  new Object[]{
                    "TT ","TT ",'T',
                    WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff)
            });
            GT_ModHandler.addCraftingRecipe(WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff, 4), new Object[]{
                    " T ", 'T', werkstoff.get(dust)
            });
            GT_ModHandler.addCraftingRecipe(WerkstoffLoader.getCorrespondingItemStack(dustTiny, werkstoff, 9), new Object[]{
                    "T  ", 'T', werkstoff.get(dust)
            });

            GT_Values.RA.addBoxingRecipe(werkstoff.get(dustTiny,9), ItemList.Schematic_Dust.get(0L), werkstoff.get(dust), 100, 4);
            GT_Values.RA.addBoxingRecipe(werkstoff.get(dustSmall,4), ItemList.Schematic_Dust.get(0L), werkstoff.get(dust), 100, 4);
            GT_Values.RA.addBoxingRecipe(werkstoff.get(dustTiny,9), ItemList.Schematic_3by3.get(0L), werkstoff.get(dust), 100, 4);
            GT_Values.RA.addBoxingRecipe(werkstoff.get(dustSmall,4), ItemList.Schematic_2by2.get(0L), werkstoff.get(dust), 100, 4);

            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 && !werkstoff.getStats().isBlastFurnace()) {
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(dustTiny), werkstoff.get(nugget));
            }
            else if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 && werkstoff.getStats().isBlastFurnace() && werkstoff.getStats().meltingPoint != 0){
                if (werkstoff.contains(WerkstoffLoader.ANAEROBE_SMELTING)){
                    GT_Values.RA.addBlastRecipe(werkstoff.get(dust),GT_Utility.getIntegratedCircuit(11),Materials.Nitrogen.getGas(1000),null,werkstoff.getStats().meltingPoint < 1750 ? werkstoff.get(ingot) : werkstoff.get(ingotHot),null,(int) Math.max(werkstoff.getStats().getMass() / 40L, 1L) * werkstoff.getStats().meltingPoint, 120, werkstoff.getStats().getMeltingPoint());
                }
                else if (werkstoff.contains(WerkstoffLoader.NOBLE_GAS_SMELTING)) {
                    GT_Values.RA.addBlastRecipe(werkstoff.get(dust), GT_Utility.getIntegratedCircuit(11), Materials.Argon.getGas(1000), null, werkstoff.getStats().meltingPoint < 1750 ? werkstoff.get(ingot) : werkstoff.get(ingotHot), null, (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L) * werkstoff.getStats().meltingPoint, 120, werkstoff.getStats().getMeltingPoint());
                }
                else {
                    GT_Values.RA.addBlastRecipe(werkstoff.get(dust), GT_Utility.getIntegratedCircuit(1), null, null, werkstoff.getStats().meltingPoint < 1750 ? werkstoff.get(ingot) : werkstoff.get(ingotHot), null, (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L) * werkstoff.getStats().meltingPoint, 120, werkstoff.getStats().getMeltingPoint());
                    if (werkstoff.getStats().meltingPoint <= 1000) {
                        GT_Values.RA.addPrimitiveBlastRecipe(werkstoff.get(dust), null, 9, werkstoff.get(ingot), null, (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L) * werkstoff.getStats().meltingPoint);
                        GT_ModHandler.addRCBlastFurnaceRecipe(werkstoff.get(ingot), werkstoff.get(dust), werkstoff.getStats().meltingPoint);
                    }
                }
            }

            if (werkstoff.getStats().isBlastFurnace() && werkstoff.getStats().meltingPoint > 1750){
                GT_Values.RA.addVacuumFreezerRecipe(werkstoff.get(ingotHot),werkstoff.get(ingot),(int) Math.max(werkstoff.getStats().mass * 3L, 1L));
            }

            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0){
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(ingot),werkstoff.get(dust));
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(nugget),werkstoff.get(dustTiny));
            }
            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 || werkstoff.getGenerationFeatures().hasGems() ){
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(block),werkstoff.get(dust, 9));
            }
        }
    }

    private void addOreRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) != 0 && (werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0 &&!werkstoff.getStats().isBlastFurnace())
            GT_ModHandler.addSmeltingRecipe(WerkstoffLoader.getCorrespondingItemStack(ore, werkstoff), werkstoff.get(ingot));

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

    private void addCellRecipes(Werkstoff werkstoff){
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b10000) == 0)
            return;

//        if (werkstoff.getStats().isElektrolysis() || werkstoff.getStats().isCentrifuge() || werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
//            List<FluidStack> flOutputs = new ArrayList<>();
//            List<ItemStack> stOutputs = new ArrayList<>();
//            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
//            int cells = 0;
//            for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents().getValue().toArray(new Pair[0])) {
//                if (container.getKey() instanceof Materials) {
//                    if (((Materials) container.getKey()).hasCorrespondingGas() || ((Materials) container.getKey()).hasCorrespondingFluid() || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID) {
//                        FluidStack tmpFl = ((Materials) container.getKey()).getGas(1000 * container.getValue());
//                        if (tmpFl == null || tmpFl.getFluid() == null) {
//                            tmpFl = ((Materials) container.getKey()).getFluid(1000 * container.getValue());
//                        }
//                        flOutputs.add(tmpFl);
//                        if (flOutputs.size() > 1) {
//                            if (!tracker.containsKey(container.getKey())) {
//                                stOutputs.add(((Materials) container.getKey()).getCells(container.getValue()));
//                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
//                            } else {
//                                stOutputs.add(((Materials) container.getKey()).getCells(tracker.get(container.getKey()).getKey() + container.getValue()));
//                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
//                            }
//                            cells += container.getValue();
//                        }
//                    } else {
//                        if (((Materials) container.getKey()).getDust(container.getValue()) == null)
//                            continue;
//                        if (!tracker.containsKey(container.getKey())) {
//                            stOutputs.add(((Materials) container.getKey()).getDust(container.getValue()));
//                            tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
//                        } else {
//                            stOutputs.add(((Materials) container.getKey()).getDust(tracker.get(container.getKey()).getKey() + container.getValue()));
//                            stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
//                        }
//                    }
//                } else if (container.getKey() instanceof Werkstoff) {
//                    if (((Werkstoff) container.getKey()).getStats().isGas() || ((Werkstoff) container.getKey()).getGenerationFeatures().hasCells()) {
//                        FluidStack tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
//                        if (tmpFl == null || tmpFl.getFluid() == null) {
//                            tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
//                        }
//                        flOutputs.add(tmpFl);
//                        if (flOutputs.size() > 1) {
//                            if (!tracker.containsKey(container.getKey())) {
//                                stOutputs.add(((Werkstoff) container.getKey()).get(cell, container.getValue()));
//                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
//                            } else {
//                                stOutputs.add(((Werkstoff) container.getKey()).get(cell, tracker.get(container.getKey()).getKey() + container.getValue()));
//                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
//                            }
//                            cells += container.getValue();
//                        }
//                    } else {
//                        if (!((Werkstoff) container.getKey()).getGenerationFeatures().hasDusts())
//                            continue;
//                        if (!tracker.containsKey(container.getKey())) {
//                            stOutputs.add(((Werkstoff) container.getKey()).get(dust, container.getValue()));
//                            tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
//                        } else {
//                            stOutputs.add(((Werkstoff) container.getKey()).get(dust, (tracker.get(container.getKey()).getKey() + container.getValue())));
//                            stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
//                        }
//                    }
//                }
//            }
//            ItemStack input = werkstoff.get(cell);
//            input.stackSize = werkstoff.getContents().getKey();
//            cells += werkstoff.getContents().getKey();
//            stOutputs.add(Materials.Empty.getCells(cells));
//            if (werkstoff.getStats().isElektrolysis())
//                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.add(new BWRecipes.DynamicGTRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().protons * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30, 0));
//            if (werkstoff.getStats().isCentrifuge())
//                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.add(new BWRecipes.DynamicGTRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().mass * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5, 0));
//            if (werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
//                if (cells > 0)
//                    stOutputs.add(Materials.Empty.getCells(cells));
//                GT_Recipe.GT_Recipe_Map.sChemicalRecipes.add(new BWRecipes.DynamicGTRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().protons * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30,0));
//            }
//            if (werkstoff.getGenerationFeatures().hasMixerRecipes()) {
//                if (cells > 0)
//                    stOutputs.add(Materials.Empty.getCells(cells));
//                GT_Recipe.GT_Recipe_Map.sMixerRecipes.add(new BWRecipes.DynamicGTRecipe(true, stOutputs.toArray(new ItemStack[0]),new ItemStack[]{input},null,null,new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null},null,(int) Math.max(1L, Math.abs(werkstoff.getStats().mass * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5,0));
//            }
//        }

        //Tank "Recipe"
        GT_Utility.addFluidContainerData(new FluidContainerRegistry.FluidContainerData(new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000),werkstoff.get(cell),Materials.Empty.getCells(1)));
        FluidContainerRegistry.registerFluidContainer(werkstoff.getFluidOrGas(1).getFluid(),werkstoff.get(cell),Materials.Empty.getCells(1));
        GT_Values.RA.addFluidCannerRecipe(Materials.Empty.getCells(1), werkstoff.get(cell), new FluidStack(Objects.requireNonNull(fluids.get(werkstoff)),1000), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(werkstoff.get(cell), Materials.Empty.getCells(1), GT_Values.NF, new FluidStack(Objects.requireNonNull(fluids.get(werkstoff)),1000));

        if (Loader.isModLoaded("Forestry")) {
            FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000), werkstoff.get(capsule), GT_ModHandler.getModItem("Forestry", "waxCapsule", 1), true);
            GT_Utility.addFluidContainerData(emptyData);
            FluidContainerRegistry.registerFluidContainer(emptyData);
            GT_Values.RA.addFluidCannerRecipe(werkstoff.get(capsule), GT_Values.NI, GT_Values.NF, new FluidStack(Objects.requireNonNull(fluids.get(werkstoff)), 1000));
        }

        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0){
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(dust),null,werkstoff.getFluidOrGas(1000),0,(int) werkstoff.getStats().mass,werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidSolidifierRecipe(GT_Utility.getIntegratedCircuit(1), werkstoff.getFluidOrGas(1000), werkstoff.get(dust), (int) werkstoff.getStats().mass,werkstoff.getStats().getMass() > 128 ? 64 : 30);
        }

        if (werkstoff.getType().equals(Werkstoff.Types.ELEMENT)) {
            Materials werkstoffBridgeMaterial = null;
            boolean ElementSet = false;
            for (Element e :  Element.values()){
                if (e.toString().equals(werkstoff.getToolTip())){
                    werkstoffBridgeMaterial = new Materials(-1,werkstoff.getTexSet(),0,0,0,false,werkstoff.getDefaultName(),werkstoff.getDefaultName());
                    werkstoffBridgeMaterial.mElement = e;
                    e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                    ElementSet = true;
                    break;
                }
            }
            if (!ElementSet)
                return;

            GT_OreDictUnificator.addAssociation(cell,werkstoffBridgeMaterial, werkstoff.get(cell),false);
//            try {
//                Field f = Materials.class.getDeclaredField("MATERIALS_MAP");
//                f.setAccessible(true);
//                Map<String, Materials> MATERIALS_MAP = (Map<String, Materials>) f.get(null);
//                MATERIALS_MAP.remove(werkstoffBridgeMaterial.mName);
//            } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
//                e.printStackTrace();
//            }
            ItemStack scannerOutput = ItemList.Tool_DataOrb.get(1L);
            Behaviour_DataOrb.setDataTitle(scannerOutput,"Elemental-Scan");
            Behaviour_DataOrb.setDataName(scannerOutput,werkstoff.getToolTip());
            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(false,new BWRecipes.DynamicGTRecipe(false, new ItemStack[]{werkstoff.get(cell)}, new ItemStack[]{scannerOutput}, ItemList.Tool_DataOrb.get(1L), null, null, null, (int) (werkstoffBridgeMaterial.getMass()* 8192L),30,0));
            GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(false,new BWRecipes.DynamicGTRecipe(false,new ItemStack[]{Materials.Empty.getCells(1)} ,new ItemStack[]{werkstoff.get(cell)}, scannerOutput, null, new FluidStack[]{Materials.UUMatter.getFluid(werkstoffBridgeMaterial.getMass())}, null, (int) (werkstoffBridgeMaterial.getMass() * 512L),30,0));
        }
    }
    private void addMoltenRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000000) == 0)
            return;

        //Tank "Recipe"
        final FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144),werkstoff.get(cellMolten),Materials.Empty.getCells(1));
        FluidContainerRegistry.registerFluidContainer(werkstoff.getMolten(144),werkstoff.get(cell),Materials.Empty.getCells(1));
        GT_Utility.addFluidContainerData(data);
        GT_Values.RA.addFluidCannerRecipe(Materials.Empty.getCells(1), werkstoff.get(cellMolten), new FluidStack(Objects.requireNonNull(molten.get(werkstoff)),144), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(werkstoff.get(cellMolten), Materials.Empty.getCells(1), GT_Values.NF, new FluidStack(Objects.requireNonNull(molten.get(werkstoff)),144));

        if (Loader.isModLoaded("Forestry")) {
            final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144), werkstoff.get(capsuleMolten), GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1));
            FluidContainerRegistry.registerFluidContainer(werkstoff.getMolten(144), werkstoff.get(capsuleMolten), GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1));
            GT_Utility.addFluidContainerData(emptyData);
            GT_Values.RA.addFluidCannerRecipe(werkstoff.get(capsuleMolten), GT_Values.NI, GT_Values.NF, new FluidStack(Objects.requireNonNull(molten.get(werkstoff)), 144));
        }

        if ((werkstoff.getGenerationFeatures().toGenerate & 0b10) != 0) {
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(ingot),null,werkstoff.getMolten(144),0,(int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            if ((werkstoff.getGenerationFeatures().toGenerate & 0b10000000) != 0) {
                GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(stickLong), null, werkstoff.getMolten(144), 0, (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
                GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(plate), null, werkstoff.getMolten(144), 0, (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
                GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(stick), null, werkstoff.getMolten(72), 0, (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            }
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(nugget),null,werkstoff.getMolten(16),0,(int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);

            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0), werkstoff.getMolten(144), werkstoff.get(ingot), (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            //GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0), werkstoff.getMolten(144), werkstoff.get(block), (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Nugget.get(0), werkstoff.getMolten(16), werkstoff.get(nugget), (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0), werkstoff.getMolten(1296), werkstoff.get(block), (int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
        } else if ((werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0 && (werkstoff.getGenerationFeatures().toGenerate & 0b10) == 0){
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(dust),null,werkstoff.getMolten(144),0,(int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(dustSmall),null,werkstoff.getMolten(36),0,(int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(dustTiny),null,werkstoff.getMolten(16),0,(int) werkstoff.getStats().mass, werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(block),null,werkstoff.getMolten(1296),0,288, 8);
        }
    }
}