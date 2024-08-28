/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.superscriptNumbers;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.BetterLoadingScreen;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.bolt;
import static gregtech.api.enums.OrePrefixes.bottle;
import static gregtech.api.enums.OrePrefixes.capsule;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.cellPlasma;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedCentrifuged;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.foil;
import static gregtech.api.enums.OrePrefixes.gearGt;
import static gregtech.api.enums.OrePrefixes.gearGtSmall;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.gemChipped;
import static gregtech.api.enums.OrePrefixes.gemExquisite;
import static gregtech.api.enums.OrePrefixes.gemFlawed;
import static gregtech.api.enums.OrePrefixes.gemFlawless;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.ingotDouble;
import static gregtech.api.enums.OrePrefixes.ingotHot;
import static gregtech.api.enums.OrePrefixes.ingotQuadruple;
import static gregtech.api.enums.OrePrefixes.ingotQuintuple;
import static gregtech.api.enums.OrePrefixes.ingotTriple;
import static gregtech.api.enums.OrePrefixes.lens;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.ore;
import static gregtech.api.enums.OrePrefixes.oreSmall;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.plateDense;
import static gregtech.api.enums.OrePrefixes.plateDouble;
import static gregtech.api.enums.OrePrefixes.plateQuadruple;
import static gregtech.api.enums.OrePrefixes.plateQuintuple;
import static gregtech.api.enums.OrePrefixes.plateTriple;
import static gregtech.api.enums.OrePrefixes.rawOre;
import static gregtech.api.enums.OrePrefixes.ring;
import static gregtech.api.enums.OrePrefixes.rotor;
import static gregtech.api.enums.OrePrefixes.screw;
import static gregtech.api.enums.OrePrefixes.spring;
import static gregtech.api.enums.OrePrefixes.springSmall;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.enums.OrePrefixes.toolHeadHammer;
import static gregtech.api.enums.OrePrefixes.toolHeadSaw;
import static gregtech.api.enums.OrePrefixes.toolHeadWrench;
import static gregtech.api.enums.OrePrefixes.turbineBlade;
import static gregtech.api.enums.OrePrefixes.values;
import static gregtech.api.enums.OrePrefixes.wireFine;
import static gregtech.api.util.GT_RecipeBuilder.WILDCARD;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import codechicken.nei.api.API;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_Renderer_Block_Ores;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_CircuitsLoader;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.GTMetaItemEnhancer;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.AdditionalRecipes;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.AspectLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.BlockLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.CasingLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.CellLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.CraftingMaterialLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.CrushedLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.DustLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.GemLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.MetalLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.MoltenCellLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.MultipleMetalLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.OreLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.RawOreLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.SimpleMetalLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe.ToolLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.registration.AssociationLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.registration.BridgeMaterialsLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.registration.CasingRegistrator;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.EnumUtils;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.bartworks.util.log.DebugLog;
import com.github.bartimaeusnek.crossmod.cls.CLSCompat;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Element;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.fluid.GT_FluidFactory;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;

@SuppressWarnings("deprecation")
public class WerkstoffLoader {

    private WerkstoffLoader() {}

    public static final SubTag NOBLE_GAS = SubTag.getNewSubTag("NobleGas");
    public static final SubTag ANAEROBE_GAS = SubTag.getNewSubTag("AnaerobeGas");
    public static final SubTag ANAEROBE_SMELTING = SubTag.getNewSubTag("AnaerobeSmelting");
    public static final SubTag NOBLE_GAS_SMELTING = SubTag.getNewSubTag("NobleGasSmelting");
    public static final SubTag NO_BLAST = SubTag.getNewSubTag("NoBlast");
    public static ItemList rotorMold;
    public static ItemList rotorShape;
    public static ItemList smallGearShape;
    public static ItemList ringMold;
    public static ItemList boltMold;

    public static void setUp() {

        OrePrefixes.cellMolten.mMaterialGenerationBits = 0b1000000;
        OrePrefixes.capsuleMolten.mMaterialGenerationBits = 0b1000000;

        try {
            WerkstoffLoader.rotorMold = Enum.valueOf(ItemList.class, "Shape_Mold_Rotor");
            WerkstoffLoader.rotorShape = Enum.valueOf(ItemList.class, "Shape_Extruder_Rotor");
            WerkstoffLoader.smallGearShape = Enum.valueOf(ItemList.class, "Shape_Extruder_Small_Gear");
            WerkstoffLoader.ringMold = Enum.valueOf(ItemList.class, "Shape_Mold_Ring");
            WerkstoffLoader.boltMold = Enum.valueOf(ItemList.class, "Shape_Mold_Bolt");
        } catch (NullPointerException | IllegalArgumentException ignored) {}

        // add tiberium
        EnumUtils.createNewElement("Tr", 123L, 203L, 0L, -1L, null, "Tiberium", false);

        bottle.mDefaultStackSize = 1;
        Werkstoff.GenerationFeatures.initPrefixLogic();
        BW_GT_MaterialReference.init();
    }

    // TODO:
    // FREE ID RANGE: 12_000-28_998
    // bartimaeusnek reserved 0-10_000
    // GlodBlock reserved range 10_001-10_999
    // Elisis reserved range 11_000-11_499
    // glowredman reserved range 11_500-11_999
    // bot reserved range 29_899-29_999
    // Tec & basdxz reserved range 30_000-31_000
    // GT Material range reserved on 31_767-32_767
    public static final Werkstoff Bismutite = new Werkstoff(
        new short[] { 255, 233, 0, 0 },
        "Bismutite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        1,
        TextureSet.SET_FLINT,
        Collections.singletonList(Materials.Bismuth),
        new Pair<>(Materials.Bismuth, 2),
        new Pair<>(Materials.Oxygen, 2),
        new Pair<>(Materials.CarbonDioxide, 2));
    public static final Werkstoff Bismuthinit = new Werkstoff(
        new short[] { 192, 192, 192, 0 },
        "Bismuthinite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        2,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Bismuth, Materials.Sulfur),
        new Pair<>(Materials.Bismuth, 2),
        new Pair<>(Materials.Sulfur, 3));
    public static final Werkstoff Zirconium = new Werkstoff(
        new short[] { 175, 175, 175, 0 },
        "Zirconium",
        "Zr",
        new Werkstoff.Stats().setProtons(40)
            .setBlastFurnace(true)
            .setMeltingPoint(2130)
            .setMeltingVoltage(480),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems()
            .enforceUnification(),
        3,
        TextureSet.SET_METALLIC
    // No Byproducts
    );
    public static final Werkstoff CubicZirconia = new Werkstoff(
        new short[] { 255, 255, 255, 0 },
        "Cubic Zirconia",
        Werkstoff.Types.COMPOUND,
        3273,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addGems()
            .enforceUnification(),
        4,
        TextureSet.SET_DIAMOND,
        Collections.singletonList(WerkstoffLoader.Zirconium),
        new Pair<>(WerkstoffLoader.Zirconium, 1),
        new Pair<>(Materials.Oxygen, 2));
    public static final Werkstoff FluorBuergerit = new Werkstoff(
        new short[] { 0x20, 0x20, 0x20, 0 },
        "Fluor-Buergerite",
        subscriptNumbers("NaFe3Al6(Si6O18)(BO3)3O3F"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        5,
        TextureSet.SET_RUBY,
        Arrays.asList(Materials.Sodium, Materials.Boron, Materials.SiliconDioxide),
        new Pair<>(Materials.Sodium, 1),
        new Pair<>(Materials.Iron, 3),
        new Pair<>(Materials.Aluminium, 6),
        new Pair<>(Materials.Silicon, 6),
        new Pair<>(Materials.Boron, 3),
        new Pair<>(Materials.Oxygen, 30),
        new Pair<>(Materials.Fluorine, 1));
    public static final Werkstoff YttriumOxide = new Werkstoff(
        new short[] { 255, 255, 255, 0 },
        "Yttrium Oxide",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .enforceUnification(), // No autoadd here to gate this material
                                   // by hand
        6,
        TextureSet.SET_DULL,
        new Pair<>(Materials.Yttrium, 2),
        new Pair<>(Materials.Oxygen, 3));
    public static final Werkstoff ChromoAluminoPovondrait = new Werkstoff(
        new short[] { 0, 0x79, 0x6A, 0 },
        "Chromo-Alumino-Povondraite",
        subscriptNumbers("NaCr3(Al4Mg2)(Si6O18)(BO3)3(OH)3O"),
        Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND)
            .setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        7,
        TextureSet.SET_RUBY,
        Arrays.asList(Materials.Sodium, Materials.Boron, Materials.SiliconDioxide),
        new Pair<>(Materials.Sodium, 1),
        new Pair<>(Materials.Chrome, 3),
        new Pair<>(Materials.Magnalium, 6),
        new Pair<>(Materials.Silicon, 6),
        new Pair<>(Materials.Boron, 3),
        new Pair<>(Materials.Oxygen, 31),
        new Pair<>(Materials.Hydrogen, 3));
    public static final Werkstoff VanadioOxyDravit = new Werkstoff(
        new short[] { 0x60, 0xA0, 0xA0, 0 },
        "Vanadio-Oxy-Dravite",
        subscriptNumbers("NaV3(Al4Mg2)(Si6O18)(BO3)3(OH)3O"),
        Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND)
            .setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        8,
        TextureSet.SET_RUBY,
        Arrays.asList(Materials.Sodium, Materials.Boron, Materials.SiliconDioxide),
        new Pair<>(Materials.Sodium, 1),
        new Pair<>(Materials.Vanadium, 3),
        new Pair<>(Materials.Magnalium, 6),
        new Pair<>(Materials.Silicon, 6),
        new Pair<>(Materials.Boron, 3),
        new Pair<>(Materials.Oxygen, 31),
        new Pair<>(Materials.Hydrogen, 3));
    public static final Werkstoff Olenit = new Werkstoff(
        new short[] { 210, 210, 210, 0 },
        "Olenite",
        subscriptNumbers("NaAl3Al6(Si6O18)(BO3)3O3OH"),
        Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND)
            .setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        9,
        TextureSet.SET_RUBY,
        Arrays.asList(Materials.Sodium, Materials.Boron, Materials.SiliconDioxide),
        new Pair<>(Materials.Sodium, 1),
        new Pair<>(Materials.Aluminium, 9),
        new Pair<>(Materials.Silicon, 6),
        new Pair<>(Materials.Boron, 3),
        new Pair<>(Materials.Oxygen, 31),
        new Pair<>(Materials.Hydrogen, 1));
    public static final Werkstoff Arsenopyrite = new Werkstoff(
        new short[] { 0xB0, 0xB0, 0xB0, 0 },
        "Arsenopyrite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        10,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Sulfur, Materials.Arsenic, Materials.Iron),
        new Pair<>(Materials.Iron, 1),
        new Pair<>(Materials.Arsenic, 1),
        new Pair<>(Materials.Sulfur, 1));
    public static final Werkstoff Ferberite = new Werkstoff(
        new short[] { 0xB0, 0xB0, 0xB0, 0 },
        "Ferberite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        11,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Iron, Materials.Tungsten),
        new Pair<>(Materials.Iron, 1),
        new Pair<>(Materials.Tungsten, 1),
        new Pair<>(Materials.Oxygen, 3));
    public static final Werkstoff Loellingit = new Werkstoff(
        new short[] { 0xD0, 0xD0, 0xD0, 0 },
        "Loellingite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        12,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Iron, Materials.Arsenic),
        new Pair<>(Materials.Iron, 1),
        new Pair<>(Materials.Arsenic, 2));
    public static final Werkstoff Roquesit = new Werkstoff(
        new short[] { 0xA0, 0xA0, 0xA0, 0 },
        "Roquesite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        13,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Sulfur),
        new Pair<>(Materials.Copper, 1),
        new Pair<>(Materials.Indium, 1),
        new Pair<>(Materials.Sulfur, 2));
    public static final Werkstoff Bornite = new Werkstoff(
        new short[] { 0x97, 0x66, 0x2B, 0 },
        "Bornite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        14,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Iron, Materials.Sulfur),
        new Pair<>(Materials.Copper, 5),
        new Pair<>(Materials.Iron, 1),
        new Pair<>(Materials.Sulfur, 4));
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
        new Pair<>(Materials.Sulfur, 4));
    public static final Werkstoff Djurleit = new Werkstoff(
        new short[] { 0x60, 0x60, 0x60, 0 },
        "Djurleite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        16,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Copper, Materials.Sulfur),
        new Pair<>(Materials.Copper, 31),
        new Pair<>(Materials.Sulfur, 16));
    public static final Werkstoff Huebnerit = new Werkstoff(
        new short[] { 0x80, 0x60, 0x60, 0 },
        "Huebnerite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        17,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Manganese, Materials.Tungsten),
        new Pair<>(Materials.Manganese, 1),
        new Pair<>(Materials.Tungsten, 1),
        new Pair<>(Materials.Oxygen, 3));
    public static final Werkstoff Thorianit = new Werkstoff(
        new short[] { 0x30, 0x30, 0x30, 0 },
        "Thorianite",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        18,
        TextureSet.SET_METALLIC,
        Collections.singletonList(Materials.Thorium),
        new Pair<>(Materials.Thorium, 1),
        new Pair<>(Materials.Oxygen, 2));
    public static final Werkstoff RedZircon = new Werkstoff(
        new short[] { 195, 19, 19, 0 },
        "Red Zircon",
        new Werkstoff.Stats().setElektrolysis(false)
            .setMeltingPoint(2130),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        19,
        TextureSet.SET_GEM_VERTICAL,
        Arrays.asList(WerkstoffLoader.Zirconium, Materials.SiliconDioxide),
        new Pair<>(WerkstoffLoader.Zirconium, 1),
        new Pair<>(Materials.Silicon, 1),
        new Pair<>(Materials.Oxygen, 4));

    // GT Enhancements
    public static final Werkstoff Salt = new Werkstoff(
        Materials.Salt.mRGBa,
        "Salt",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addGems()
            .addSifterRecipes(),
        20,
        TextureSet.SET_FLINT,
        Arrays.asList(Materials.RockSalt, Materials.Borax),
        new Pair<>(Materials.Salt, 1));
    public static final Werkstoff Spodumen = new Werkstoff(
        Materials.Spodumene.mRGBa,
        "Spodumene",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addGems()
            .addSifterRecipes(),
        21,
        TextureSet.SET_FLINT,
        Collections.singletonList(Materials.Spodumene),
        new Pair<>(Materials.Spodumene, 1));
    public static final Werkstoff RockSalt = new Werkstoff(
        Materials.RockSalt.mRGBa,
        "Rock Salt",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addGems()
            .addSifterRecipes(),
        22,
        TextureSet.SET_FLINT,
        Arrays.asList(Materials.RockSalt, Materials.Borax),
        new Pair<>(Materials.RockSalt, 1));

    // More NonGT Stuff
    public static final Werkstoff Fayalit = new Werkstoff(
        new short[] { 50, 50, 50, 0 },
        "Fayalite",
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        23,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Iron, Materials.SiliconDioxide),
        new Pair<>(Materials.Iron, 2),
        new Pair<>(Materials.Silicon, 1),
        new Pair<>(Materials.Oxygen, 4));
    public static final Werkstoff Forsterit = new Werkstoff(
        new short[] { 255, 255, 255, 0 },
        "Forsterite",
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        24,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Magnesium, Materials.SiliconDioxide),
        new Pair<>(Materials.Magnesium, 2),
        new Pair<>(Materials.Silicon, 1),
        new Pair<>(Materials.Oxygen, 4));
    public static final Werkstoff Hedenbergit = new Werkstoff(
        new short[] { 100, 150, 100, 0 },
        "Hedenbergite",
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        25,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Iron, Materials.Calcium, Materials.SiliconDioxide),
        new Pair<>(Materials.Calcium, 1),
        new Pair<>(Materials.Iron, 1),
        new Pair<>(Materials.Silicon, 2),
        new Pair<>(Materials.Oxygen, 6));
    public static final Werkstoff DescloiziteZNVO4 = new Werkstoff(
        new short[] { 0xBF, 0x18, 0x0F, 0 },
        "Red Descloizite", // Pb(Zn,Cu)[OH|VO4
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        26,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Lead, Materials.Copper, Materials.Vanadium),
        new Pair<>(Materials.Lead, 1),
        new Pair<>(Materials.Zinc, 1),
        new Pair<>(Materials.Vanadium, 1),
        new Pair<>(Materials.Oxygen, 4));
    public static final Werkstoff DescloiziteCUVO4 = new Werkstoff(
        new short[] { 0xf9, 0x6d, 0x18, 0 },
        "Orange Descloizite", // Pb(Zn,Cu)[OH|VO4
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        27,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Lead, Materials.Zinc, Materials.Vanadium),
        new Pair<>(Materials.Lead, 1),
        new Pair<>(Materials.Copper, 1),
        new Pair<>(Materials.Vanadium, 1),
        new Pair<>(Materials.Oxygen, 4));
    public static final Werkstoff FuchsitAL = new Werkstoff(
        new short[] { 0x4D, 0x7F, 0x64, 0 },
        "Green Fuchsite",
        subscriptNumbers("KAl3Si3O10(OH)2"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        28,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Potassium, Materials.Aluminiumoxide, Materials.SiliconDioxide),
        new Pair<>(Materials.Potassium, 1),
        new Pair<>(Materials.Aluminium, 3),
        new Pair<>(Materials.Silicon, 3),
        new Pair<>(Materials.Oxygen, 12),
        new Pair<>(Materials.Hydrogen, 2));

    public static final Werkstoff FuchsitCR = new Werkstoff(
        new short[] { 128, 0, 0, 0 },
        "Red Fuchsite",
        subscriptNumbers("KCr3Si3O10(OH)2"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        29,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Potassium, Materials.Chrome, Materials.SiliconDioxide),
        new Pair<>(Materials.Potassium, 1),
        new Pair<>(Materials.Chrome, 3),
        new Pair<>(Materials.Silicon, 3),
        new Pair<>(Materials.Oxygen, 12),
        new Pair<>(Materials.Hydrogen, 2));

    public static final Werkstoff Thorium232 = new Werkstoff(
        new short[] { 0, 64, 0, 0 },
        "Thorium 232",
        superscriptNumbers("Th232"),
        new Werkstoff.Stats().setRadioactive(true)
            .setBlastFurnace(true)
            .setMass(232)
            .setProtons(Element.Th.mProtons),
        Werkstoff.Types.ISOTOPE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems()
            .enforceUnification(),
        30,
        TextureSet.SET_METALLIC
    // No Byproducts
    );
    public static final Werkstoff BismuthTellurite = new Werkstoff(
        new short[] { 32, 72, 32, 0 },
        // Telluride is correct, tellurite is not.
        // Only the display name gets renamed to avoid problems in other mods
        "Bismuth Telluride",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addChemicalRecipes(),
        31,
        TextureSet.SET_METALLIC,
        // No Byproducts
        new Pair<>(Materials.Bismuth, 2),
        new Pair<>(Materials.Tellurium, 3));
    public static final Werkstoff Tellurium = new Werkstoff(
        new short[] { 0xff, 0xff, 0xff, 0 },
        "Tellurium",
        new Werkstoff.Stats(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().addMetalItems()
            .removePrefix(ore),
        32,
        TextureSet.SET_METALLIC,
        // No Byproducts
        new Pair<>(Materials.Tellurium, 1));
    public static final Werkstoff BismuthHydroBorat = new Werkstoff(
        new short[] { 72, 144, 72, 0 },
        "Dibismuthhydroborat",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addChemicalRecipes(),
        33,
        TextureSet.SET_METALLIC,
        // No Byproducts
        new Pair<>(Materials.Bismuth, 2),
        new Pair<>(Materials.Boron, 1),
        new Pair<>(Materials.Hydrogen, 1));
    public static final Werkstoff ArInGaPhoBiBoTe = new Werkstoff(
        new short[] { 36, 36, 36, 0 },
        "Circuit Compound MK3",
        new Werkstoff.Stats().setCentrifuge(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMixerRecipes(),
        34,
        TextureSet.SET_METALLIC,
        // No Byproducts
        new Pair<>(Materials.IndiumGalliumPhosphide, 1),
        new Pair<>(WerkstoffLoader.BismuthHydroBorat, 3),
        new Pair<>(WerkstoffLoader.BismuthTellurite, 2));

    public static final Werkstoff Prasiolite = new Werkstoff(
        new short[] { 0xD0, 0xDD, 0x95, 0 },
        "Prasiolite",
        new Werkstoff.Stats().setElektrolysis(false)
            .setMeltingPoint(1923),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        35,
        TextureSet.SET_QUARTZ,
        // No Byproducts
        new Pair<>(Materials.Silicon, 5),
        new Pair<>(Materials.Oxygen, 10),
        new Pair<>(Materials.Iron, 1));

    public static final Werkstoff MagnetoResonaticDust = new Werkstoff(
        new short[] { 0xDD, 0x77, 0xDD, 0 },
        "Magneto Resonatic",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMixerRecipes()
            .addGems(),
        36,
        TextureSet.SET_MAGNETIC,
        // No Byproducts
        new Pair<>(WerkstoffLoader.Prasiolite, 3),
        new Pair<>(WerkstoffLoader.BismuthTellurite, 4),
        new Pair<>(WerkstoffLoader.CubicZirconia, 1),
        new Pair<>(Materials.SteelMagnetic, 1));
    public static final Werkstoff Xenon = new Werkstoff(
        new short[] { 0x14, 0x39, 0x7F, 0 },
        "Xenon",
        "Xe",
        new Werkstoff.Stats().setProtons(54)
            .setMass(131)
            .setGas(true)
            .setEbfGasRecipeTimeMultiplier(0.4d)
            .setEbfGasRecipeConsumedAmountMultiplier(0.25d),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells()
            .enforceUnification(),
        37,
        TextureSet.SET_FLUID
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff Oganesson = new Werkstoff(
        new short[] { 0x14, 0x39, 0x7F, 0 },
        "Oganesson",
        "Og",
        new Werkstoff.Stats().setProtons(118)
            .setMass(294)
            .setGas(true)
            .setEbfGasRecipeTimeMultiplier(0.3d)
            .setEbfGasRecipeConsumedAmountMultiplier(0.1d),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        38,
        TextureSet.SET_FLUID
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff Californium = new Werkstoff(
        new short[] { 0xAA, 0xAA, 0xAA, 0 },
        "Californium",
        "Cf",
        new Werkstoff.Stats().setProtons(98)
            .setMass(251)
            .setBlastFurnace(true)
            .setMeltingPoint(900),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems()
            .addMolten()
            .enforceUnification(),
        39,
        TextureSet.SET_METALLIC
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff Calcium = new Werkstoff(
        Materials.Calcium.mRGBa,
        "Calcium",
        "Ca",
        new Werkstoff.Stats().setProtons(Element.Ca.mProtons)
            .setMass(Element.Ca.getMass())
            .setBlastFurnace(true)
            .setMeltingPoint(1115)
            .setBoilingPoint(1757),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems()
            .addMolten(),
        40,
        Materials.Calcium.mIconSet,
        // No Byproducts
        new Pair<>(Materials.Calcium, 1));
    public static final Werkstoff Neon = new Werkstoff(
        new short[] { 0xff, 0x07, 0x3a },
        "Neon",
        "Ne",
        new Werkstoff.Stats().setProtons(Element.Ne.mProtons)
            .setMass(Element.Ne.getMass())
            .setGas(true)
            .setEbfGasRecipeTimeMultiplier(0.6d)
            .setEbfGasRecipeConsumedAmountMultiplier(0.55d),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells()
            .enforceUnification(),
        41,
        TextureSet.SET_FLUID
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff Krypton = new Werkstoff(
        new short[] { 0xb1, 0xff, 0x32 },
        "Krypton",
        "Kr",
        new Werkstoff.Stats().setProtons(Element.Kr.mProtons)
            .setMass(Element.Kr.getMass())
            .setGas(true)
            .setEbfGasRecipeTimeMultiplier(0.5d)
            .setEbfGasRecipeConsumedAmountMultiplier(0.4d),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells()
            .enforceUnification(),
        42,
        TextureSet.SET_FLUID
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff BArTiMaEuSNeK = new Werkstoff(
        new short[] { 0x00, 0xff, 0x00 },
        "BArTiMaEuSNeK",
        "Are you serious?",
        new Werkstoff.Stats().setMeltingPoint(9001)
            .setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingVoltage(1920),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems()
            .addMetalItems()
            .addMolten(),
        43,
        TextureSet.SET_DIAMOND,
        Arrays.asList(Materials.Boron, Materials.Titanium, Materials.Europium),
        new Pair<>(Materials.Boron, 1),
        new Pair<>(Materials.Argon, 1),
        new Pair<>(Materials.Titanium, 1),
        new Pair<>(Materials.Magic, 1),
        new Pair<>(Materials.Europium, 1),
        new Pair<>(Materials.Sulfur, 1),
        new Pair<>(WerkstoffLoader.Neon, 1),
        new Pair<>(Materials.Potassium, 1));
    public static final Werkstoff PTConcentrate = new Werkstoff(
        Materials.Platinum.getRGBA(),
        "Platinum Concentrate",
        "",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        44,
        TextureSet.SET_FLUID
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff PTSaltCrude = new Werkstoff(
        Materials.Platinum.getRGBA(),
        "Platinum Salt",
        "",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        45,
        TextureSet.SET_DULL
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff PTSaltRefined = new Werkstoff(
        Materials.Platinum.getRGBA(),
        "Refined Platinum Salt",
        "",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        46,
        TextureSet.SET_METALLIC
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff PTMetallicPowder = new Werkstoff(
        Materials.Platinum.getRGBA(),
        "Platinum Metallic Powder",
        "??PtPdIrOsRhRu??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        47,
        TextureSet.SET_METALLIC,
        // No Byproducts
        new Pair<>(Materials.Platinum, 1),
        new Pair<>(Materials.Stone, 2));
    public static final Werkstoff AquaRegia = new Werkstoff(
        new short[] { 0xff, 0xb1, 0x32 },
        "Aqua Regia",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        48,
        TextureSet.SET_FLUID,
        // No Byproducts
        new Pair<>(Materials.DilutedSulfuricAcid, 1),
        new Pair<>(Materials.NitricAcid, 1));
    public static final Werkstoff PTResidue = new Werkstoff(
        new short[] { 0x64, 0x63, 0x2E },
        "Platinum Residue",
        "??IrOsRhRu??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        49,
        TextureSet.SET_ROUGH
    // No Byproducts
    );
    public static final Werkstoff AmmoniumChloride = new Werkstoff(
        new short[] { 0xff, 0xff, 0xff },
        "Ammonium Chloride",
        subscriptNumbers("NH4Cl"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        50,
        TextureSet.SET_FLUID,
        // No Byproducts
        new Pair<>(Materials.Ammonium, 1),
        new Pair<>(Materials.HydrochloricAcid, 1));
    public static final Werkstoff PTRawPowder = new Werkstoff(
        Materials.Platinum.getRGBA(),
        "Reprecipitated Platinum",
        "PtCl",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        51,
        TextureSet.SET_METALLIC
    // No Byproducts
    );
    public static final Werkstoff PDAmmonia = new Werkstoff(
        Materials.Palladium.getRGBA(),
        "Palladium Enriched Ammonia",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        52,
        TextureSet.SET_FLUID,
        // No Byproducts
        new Pair<>(Materials.Ammonium, 1),
        new Pair<>(Materials.Palladium, 1));
    public static final Werkstoff PDMetallicPowder = new Werkstoff(
        Materials.Palladium.getRGBA(),
        "Palladium Metallic Powder",
        "??Pd??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        53,
        TextureSet.SET_METALLIC,
        // No Byproducts
        new Pair<>(Materials.Palladium, 1),
        new Pair<>(Materials.Stone, 2));
    public static final Werkstoff PDRawPowder = new Werkstoff(
        Materials.Palladium.getRGBA(),
        "Reprecipitated Palladium",
        subscriptNumbers("Pd2NH4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        54,
        TextureSet.SET_METALLIC
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff PDSalt = new Werkstoff(
        Materials.Palladium.getRGBA(),
        "Palladium Salt",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        55,
        TextureSet.SET_METALLIC
    // No Byproducts
    // No Ingredients
    );
    public static final Werkstoff Sodiumformate = new Werkstoff(
        new short[] { 0xff, 0xaa, 0xaa },
        "Sodium Formate",
        "HCOONa",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        56,
        TextureSet.SET_FLUID,
        // No Byproducts
        new Pair<>(Materials.SodiumHydroxide, 1),
        new Pair<>(Materials.CarbonMonoxide, 1));
    public static final Werkstoff Sodiumsulfate = new Werkstoff(
        new short[] { 0xff, 0xff, 0xff },
        "Sodium Sulfate",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        57,
        TextureSet.SET_FLUID,
        // No Byproducts
        new Pair<>(Materials.Sodium, 2),
        new Pair<>(Materials.Sulfur, 1),
        new Pair<>(Materials.Oxygen, 4));
    public static final Werkstoff FormicAcid = new Werkstoff(
        new short[] { 0xff, 0xaa, 0x77 },
        "Formic Acid",
        subscriptNumbers("CH2O2"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        58,
        TextureSet.SET_FLUID,
        // No Byproducts
        new Pair<>(Materials.Carbon, 1),
        new Pair<>(Materials.Hydrogen, 2),
        new Pair<>(Materials.Oxygen, 2));
    public static final Werkstoff PotassiumDisulfate = new Werkstoff(
        new short[] { 0xfb, 0xbb, 0x66 },
        "Potassium Disulfate",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten()
            .addChemicalRecipes(),
        59,
        TextureSet.SET_DULL,
        // No Byproducts
        new Pair<>(Materials.Potassium, 2),
        new Pair<>(Materials.Sulfur, 2),
        new Pair<>(Materials.Oxygen, 7));
    public static final Werkstoff LeachResidue = new Werkstoff(
        new short[] { 0x64, 0x46, 0x29 },
        "Leach Residue",
        "??IrOsRu??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        60,
        TextureSet.SET_ROUGH
    // No Byproducts
    );
    public static final Werkstoff RHSulfate = new Werkstoff(
        new short[] { 0xee, 0xaa, 0x55 },
        "Rhodium Sulfate",
        new Werkstoff.Stats().setGas(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        61,
        TextureSet.SET_FLUID
    // No Byproducts
    );
    public static final Werkstoff RHSulfateSolution = new Werkstoff(
        new short[] { 0xff, 0xbb, 0x66 },
        "Rhodium Sulfate Solution",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        62,
        TextureSet.SET_FLUID
    // No Byproducts
    );
    public static final Werkstoff CalciumChloride = new Werkstoff(
        new short[] { 0xff, 0xff, 0xff },
        "Calcium Chloride",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addCells(),
        63,
        TextureSet.SET_DULL,
        new Pair<>(Materials.Calcium, 1),
        new Pair<>(Materials.Chlorine, 2)
    // No Byproducts
    );
    public static final Werkstoff Ruthenium = new Werkstoff(
        new short[] { 0x64, 0x64, 0x64 },
        "Ruthenium",
        "Ru",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(2607)
            .setMass(Element.Ru.getMass())
            .setProtons(Element.Ru.mProtons),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .enforceUnification(),
        64,
        TextureSet.SET_METALLIC
    // No Byproducts
    );
    public static final Werkstoff SodiumRuthenate = new Werkstoff(
        new short[] { 0x3a, 0x40, 0xcb },
        "Sodium Ruthenate",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        65,
        TextureSet.SET_SHINY,
        new Pair<>(Materials.Sodium, 2),
        new Pair<>(Ruthenium, 1),
        new Pair<>(Materials.Oxygen, 3)
    // No Byproducts
    );
    public static final Werkstoff RutheniumTetroxide = new Werkstoff(
        new short[] { 0xc7, 0xc7, 0xc7 },
        "Ruthenium Tetroxide",
        new Werkstoff.Stats().setMeltingPoint(313),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addCells(),
        66,
        TextureSet.SET_DULL,
        new Pair<>(WerkstoffLoader.Ruthenium, 1),
        new Pair<>(Materials.Oxygen, 4)
    // No Byproducts
    );
    public static final Werkstoff HotRutheniumTetroxideSollution = new Werkstoff(
        new short[] { 0xc7, 0xc7, 0xc7 },
        "Hot Ruthenium Tetroxide Solution",
        "???",
        new Werkstoff.Stats().setGas(true)
            .setMeltingPoint(700),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        67,
        TextureSet.SET_FLUID,
        new Pair<>(WerkstoffLoader.Ruthenium, 1),
        new Pair<>(Materials.Oxygen, 4),
        new Pair<>(Materials.Chlorine, 2),
        new Pair<>(Materials.Sodium, 2),
        new Pair<>(Materials.Water, 2)
    // No Byproducts
    );
    public static final Werkstoff RutheniumTetroxideSollution = new Werkstoff(
        new short[] { 0xc7, 0xc7, 0xc7 },
        "Ruthenium Tetroxide Solution",
        "???",
        new Werkstoff.Stats().setMeltingPoint(313),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        68,
        TextureSet.SET_FLUID,
        new Pair<>(Ruthenium, 1),
        new Pair<>(Materials.Oxygen, 4),
        new Pair<>(Materials.Chlorine, 2),
        new Pair<>(Materials.Sodium, 2),
        new Pair<>(Materials.Water, 2)
    // No Byproducts
    );
    public static final Werkstoff IrOsLeachResidue = new Werkstoff(
        new short[] { 0x64, 0x46, 0x29 },
        "Rarest Metal Residue",
        "??OsIr??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        69,
        TextureSet.SET_ROUGH,
        // No Byproducts
        new Pair<>(Materials.Osmiridium, 1),
        new Pair<>(Materials.Stone, 2));
    public static final Werkstoff IrLeachResidue = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Iridium Metal Residue",
        "??Ir??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        70,
        TextureSet.SET_ROUGH,
        new Pair<>(Materials.Iridium, 1),
        new Pair<>(Materials.Stone, 2)
    // No Byproducts
    );
    public static final Werkstoff PGSDResidue = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Sludge Dust Residue",
        new Werkstoff.Stats().setCentrifuge(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        71,
        TextureSet.SET_DULL,
        new Pair<>(Materials.SiliconDioxide, 3),
        new Pair<>(Materials.Gold, 2));
    public static final Werkstoff AcidicOsmiumSolution = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Acidic Osmium Solution",
        "???",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        72,
        TextureSet.SET_FLUID,
        new Pair<>(Materials.Osmium, 1),
        new Pair<>(Materials.HydrochloricAcid, 1));
    public static final Werkstoff IridiumDioxide = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Iridium Dioxide",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        73,
        TextureSet.SET_FLUID,
        new Pair<>(Materials.Iridium, 1),
        new Pair<>(Materials.Oxygen, 2));
    public static final Werkstoff OsmiumSolution = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Osmium Solution",
        "???",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        74,
        TextureSet.SET_FLUID,
        new Pair<>(Materials.Osmium, 1),
        new Pair<>(Materials.Hydrogen, 1));
    public static final Werkstoff AcidicIridiumSolution = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Acidic Iridium Solution",
        "???",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        75,
        TextureSet.SET_FLUID,
        new Pair<>(Materials.Iridium, 1),
        new Pair<>(Materials.Hydrogen, 1));
    public static final Werkstoff IridiumChloride = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Iridium Chloride",
        subscriptNumbers("IrCl3"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        76,
        TextureSet.SET_LAPIS,
        new Pair<>(Materials.Iridium, 1),
        new Pair<>(Materials.Chlorine, 3));
    public static final Werkstoff PGSDResidue2 = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Metallic Sludge Dust Residue",
        new Werkstoff.Stats().setCentrifuge(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        77,
        TextureSet.SET_DULL,
        new Pair<>(Materials.Nickel, 1),
        new Pair<>(Materials.Copper, 1));
    public static final Werkstoff Rhodium = new Werkstoff(
        new short[] { 0xF4, 0xF4, 0xF4 },
        "Rhodium",
        "Rh",
        new Werkstoff.Stats().setProtons(Element.Rh.mProtons)
            .setMass(Element.Rh.getMass())
            .setBlastFurnace(true)
            .setMeltingPoint(2237),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems()
            .addMolten()
            .enforceUnification(),
        78,
        TextureSet.SET_METALLIC);
    public static final Werkstoff CrudeRhMetall = new Werkstoff(
        new short[] { 0x66, 0x66, 0x66 },
        "Crude Rhodium Metal",
        "??Rh??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        79,
        TextureSet.SET_DULL,
        new Pair<>(Rhodium, 1),
        new Pair<>(Materials.Stone, 1));
    public static final Werkstoff RHSalt = new Werkstoff(
        new short[] { 0x84, 0x84, 0x84 },
        "Rhodium Salt",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        80,
        TextureSet.SET_GEM_VERTICAL);
    public static final Werkstoff RHSaltSolution = new Werkstoff(
        new short[] { 0x66, 0x77, 0x88 },
        "Rhodium Salt Solution",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        81,
        TextureSet.SET_FLUID);
    public static final Werkstoff SodiumNitrate = new Werkstoff(
        new short[] { 0x84, 0x66, 0x84 },
        "Sodium Nitrate",
        subscriptNumbers("NaNO3"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        82,
        TextureSet.SET_ROUGH,
        new Pair<>(Materials.Sodium, 1),
        new Pair<>(Materials.Nitrogen, 1),
        new Pair<>(Materials.Oxygen, 3));
    public static final Werkstoff RHNitrate = new Werkstoff(
        new short[] { 0x77, 0x66, 0x49 },
        "Rhodium Nitrate",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        83,
        TextureSet.SET_QUARTZ);
    public static final Werkstoff ZincSulfate = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Zinc Sulfate",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        84,
        TextureSet.SET_QUARTZ,
        new Pair<>(Materials.Zinc, 1),
        new Pair<>(Materials.Sulfur, 1),
        new Pair<>(Materials.Oxygen, 4));
    public static final Werkstoff RhFilterCake = new Werkstoff(
        new short[] { 0x77, 0x66, 0x49 },
        "Rhodium Filter Cake",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        85,
        TextureSet.SET_QUARTZ);
    public static final Werkstoff RHFilterCakeSolution = new Werkstoff(
        new short[] { 0x66, 0x77, 0x88 },
        "Rhodium Filter Cake Solution",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        86,
        TextureSet.SET_FLUID);
    public static final Werkstoff ReRh = new Werkstoff(
        new short[] { 0x77, 0x66, 0x49 },
        "Reprecipitated Rhodium",
        subscriptNumbers("Rh2NH4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        87,
        TextureSet.SET_QUARTZ);
    public static final Werkstoff LuVTierMaterial = new Werkstoff(
        Materials.Chrome.getRGBA(),
        "Rhodium-Plated Palladium",
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(4500)
            .setMeltingVoltage(480),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten()
            .addMetalItems()
            .addMixerRecipes((short) 1)
            .addSimpleMetalWorkingItems()
            .addCraftingMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems(),
        88,
        TextureSet.SET_METALLIC,
        new Pair<>(Materials.Palladium, 3),
        new Pair<>(WerkstoffLoader.Rhodium, 1));
    public static final Werkstoff Tiberium = new Werkstoff(
        new short[] { 0x22, 0xEE, 0x22 },
        "Tiberium",
        "Tr",
        new Werkstoff.Stats().setProtons(123)
            .setMass(326)
            .setBlastFurnace(true)
            .setMeltingPoint(1800)
            .setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().addGems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems(),
        89,
        TextureSet.SET_DIAMOND);
    public static final Werkstoff Ruridit = new Werkstoff(
        new short[] { 0xA4, 0xA4, 0xA4 },
        "Ruridit",
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(4500)
            .setMeltingVoltage(480),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten()
            .addMetalItems()
            .addMixerRecipes((short) 1)
            .addSimpleMetalWorkingItems()
            .addCraftingMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems(),
        90,
        TextureSet.SET_METALLIC,
        new Pair<>(WerkstoffLoader.Ruthenium, 2),
        new Pair<>(Materials.Iridium, 1));
    public static final Werkstoff Fluorspar = new Werkstoff(
        new short[] { 185, 69, 251 },
        "Fluorspar",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        91,
        TextureSet.SET_GEM_VERTICAL,
        new Pair<>(Materials.Calcium, 1),
        new Pair<>(Materials.Fluorine, 2));
    public static final Werkstoff HDCS = new Werkstoff(
        new short[] { 0x33, 0x44, 0x33 },
        "High Durability Compound Steel",
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(9000)
            .setMeltingVoltage(7680),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten()
            .addMetalItems()
            .addMixerRecipes()
            .addSimpleMetalWorkingItems()
            .addCraftingMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems(),
        92,
        TextureSet.SET_SHINY,
        new Pair<>(Materials.TungstenSteel, 12),
        new Pair<>(Materials.HSSE, 9),
        new Pair<>(Materials.HSSG, 6),
        new Pair<>(WerkstoffLoader.Ruridit, 3),
        new Pair<>(WerkstoffLoader.MagnetoResonaticDust, 2),
        new Pair<>(Materials.Plutonium, 1));
    public static final Werkstoff Atheneite = new Werkstoff(
        new short[] { 175, 175, 175 },
        "Atheneite",
        subscriptNumbers("(Pd,Hg)3As"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        93,
        TextureSet.SET_SHINY,
        new Pair<>(WerkstoffLoader.PDMetallicPowder, 3),
        new Pair<>(Materials.Mercury, 3),
        new Pair<>(Materials.Arsenic, 1));
    public static final Werkstoff Temagamite = new Werkstoff(
        new short[] { 245, 245, 245 },
        "Temagamite",
        subscriptNumbers("Pd3HgTe"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        94,
        TextureSet.SET_ROUGH,
        new Pair<>(WerkstoffLoader.PDMetallicPowder, 3),
        new Pair<>(Materials.Mercury, 1),
        new Pair<>(Materials.Tellurium, 1));
    public static final Werkstoff Terlinguaite = new Werkstoff(
        new short[] { 245, 245, 245 },
        "Terlinguaite",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        95,
        TextureSet.SET_GEM_HORIZONTAL,
        new Pair<>(Materials.Mercury, 2),
        new Pair<>(Materials.Chlorine, 1),
        new Pair<>(Materials.Oxygen, 1));
    public static final Werkstoff AdemicSteel = new Werkstoff(
        new short[] { 0xcc, 0xcc, 0xcc },
        "Ademic Steel",
        "The break in the line",
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setDurOverride(6144)
            .setMeltingPoint(1800)
            .setSpeedOverride(12)
            .setQualityOverride((byte) 4)
            .setMeltingVoltage(1920),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMolten()
            .addSimpleMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems(),
        96,
        TextureSet.SET_METALLIC,
        new Pair<>(Materials.Steel, 2),
        new Pair<>(Materials.VanadiumSteel, 1),
        new Pair<>(Materials.DamascusSteel, 1),
        new Pair<>(Materials.Carbon, 4));
    public static final Werkstoff RawAdemicSteel = new Werkstoff(
        new short[] { 0xed, 0xed, 0xed },
        "Raw Ademic Steel",
        new Werkstoff.Stats().setCentrifuge(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMixerRecipes(),
        97,
        TextureSet.SET_ROUGH,
        new Pair<>(Materials.Steel, 2),
        new Pair<>(Materials.VanadiumSteel, 1),
        new Pair<>(Materials.DamascusSteel, 1));
    public static final Werkstoff HexafluorosilicicAcid = new Werkstoff(
        new short[] { 0x2c, 0x70, 0xb5 },
        "Hexafluorosilicic Acid",
        subscriptNumbers("H2SiF6"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        98,
        TextureSet.SET_FLUID,
        new Pair<>(Materials.Hydrogen, 2),
        new Pair<>(Materials.Silicon, 1),
        new Pair<>(Materials.Fluorine, 6));
    public static final Werkstoff Potassiumfluorosilicate = new Werkstoff(
        new short[] { 0x2e, 0x97, 0xb2 },
        "Potassiumfluorosilicate",
        subscriptNumbers("K2SiF6"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        99,
        TextureSet.SET_SHINY,
        new Pair<>(Materials.Potassium, 2),
        new Pair<>(Materials.Silicon, 1),
        new Pair<>(Materials.Fluorine, 6));
    public static final Werkstoff Alumina = new Werkstoff(
        new short[] { 0xa0, 0xad, 0xb1 },
        "Alumina",
        subscriptNumbers("Al2O3"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        100,
        TextureSet.SET_DULL);
    public static final Werkstoff PotassiumCarbonate = new Werkstoff(
        new short[] { 0x7b, 0x96, 0x4f },
        "Potassium Carbonate",
        subscriptNumbers("K2CO3"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        101,
        TextureSet.SET_DULL,
        new Pair<>(Materials.Potassium, 2),
        new Pair<>(Materials.Carbon, 1),
        new Pair<>(Materials.Oxygen, 3));
    public static final Werkstoff RawFluorophlogopite = new Werkstoff(
        new short[] { 0x36, 0x51, 0x0b },
        "Raw Fluorophlogopite",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        102,
        TextureSet.SET_DULL);
    public static final Werkstoff HotFluorophlogopite = new Werkstoff(
        new short[] { 0xbf, 0xd3, 0x55 },
        "Unformed Fluorophlogopite",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        103,
        TextureSet.SET_FLUID);
    public static final Werkstoff Fluorophlogopite = new Werkstoff(
        new short[] { 0xbf, 0xd3, 0x55 },
        "Fluorophlogopite",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems(),
        104,
        TextureSet.SET_SHINY);

    public static HashMap<OrePrefixes, BW_MetaGenerated_Items> items = new HashMap<>();
    public static HashBiMap<Werkstoff, Fluid> fluids = HashBiMap.create();
    public static HashBiMap<Werkstoff, Fluid> molten = HashBiMap.create();
    public static Block BWOres;
    public static Block BWSmallOres;
    public static Block BWBlocks;
    public static Block BWBlockCasings;
    public static Block BWBlockCasingsAdvanced;
    public static boolean registered;
    public static final HashSet<OrePrefixes> ENABLED_ORE_PREFIXES = new HashSet<>();

    public static Werkstoff getWerkstoff(String Name) {
        try {
            Field f = WerkstoffLoader.class.getField(Name);
            return (Werkstoff) f.get(null);
        } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
            MainMod.LOGGER.catching(e);
        }
        return Werkstoff.default_null_Werkstoff;
    }

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff) {
        return WerkstoffLoader.getCorrespondingItemStack(orePrefixes, werkstoff, 1);
    }

    public static ItemStack getCorrespondingItemStackUnsafe(OrePrefixes orePrefixes, Werkstoff werkstoff, int amount) {
        if (!werkstoff.getGenerationFeatures().enforceUnification) {
            ItemStack ret = GT_OreDictUnificator.get(orePrefixes, werkstoff.getBridgeMaterial(), amount);
            if (ret != null) return ret;
            ret = OreDictHandler.getItemStack(werkstoff.getVarName(), orePrefixes, amount);
            if (ret != null) return ret;
        }
        if (orePrefixes == ore) return new ItemStack(WerkstoffLoader.BWOres, amount, werkstoff.getmID());
        if (orePrefixes == oreSmall) return new ItemStack(WerkstoffLoader.BWSmallOres, amount, werkstoff.getmID());
        else if (orePrefixes == block) return new ItemStack(WerkstoffLoader.BWBlocks, amount, werkstoff.getmID());
        else if (orePrefixes == OrePrefixes.blockCasing)
            return new ItemStack(WerkstoffLoader.BWBlockCasings, amount, werkstoff.getmID());
        else if (orePrefixes == OrePrefixes.blockCasingAdvanced)
            return new ItemStack(WerkstoffLoader.BWBlockCasingsAdvanced, amount, werkstoff.getmID());
        else if (WerkstoffLoader.items.get(orePrefixes) == null) return null;
        return new ItemStack(WerkstoffLoader.items.get(orePrefixes), amount, werkstoff.getmID()).copy();
    }

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff, int amount) {
        ItemStack stack = getCorrespondingItemStackUnsafe(orePrefixes, werkstoff, amount);
        if (stack != null) return stack;
        MainMod.LOGGER.catching(
            Level.ERROR,
            new Exception(
                "NO SUCH ITEM! " + orePrefixes
                    + werkstoff.getVarName()
                    + " If you encounter this as a user, make sure to contact the authors of the pack/the mods you're playing! "
                    + "If you are a Developer, you forgot to enable "
                    + orePrefixes
                    + " OrePrefix for Werkstoff "
                    + werkstoff.getDefaultName()));
        return new ItemStack(WerkstoffLoader.items.get(orePrefixes), amount, werkstoff.getmID()).copy();
    }

    public static void runInit() {
        MainMod.LOGGER.info("Making Meta Items for BW Materials");
        long timepre = System.nanoTime();
        WerkstoffAdderRegistry.run();
        addSubTags();
        addItemsForGeneration();
        addBridgeSubTags();
        runAdditionalOreDict();
        long timepost = System.nanoTime();
        MainMod.LOGGER.info(
            "Making Meta Items for BW Materials took " + (timepost - timepre)
                + "ns/"
                + (timepost - timepre) / 1000000
                + "ms/"
                + (timepost - timepre) / 1000000000
                + "s!");
    }

    public static void run() {
        if (!registered) {
            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials");
            long timepre = System.nanoTime();
            ProgressManager.ProgressBar progressBar = ProgressManager
                .push("Register BW Materials", Werkstoff.werkstoffHashSet.size() + 1);
            DebugLog.log("Loading Recipes" + (System.nanoTime() - timepre));
            Integer[] clsArr = {};
            int size = 0;
            if (BetterLoadingScreen.isModLoaded()) clsArr = CLSCompat.initCls();

            IWerkstoffRunnable[] werkstoffRunnables = { new ToolLoader(), new DustLoader(), new GemLoader(),
                new SimpleMetalLoader(), new CasingLoader(), new AspectLoader(), new OreLoader(), new RawOreLoader(),
                new CrushedLoader(), new CraftingMaterialLoader(), new CellLoader(), new MoltenCellLoader(),
                new MultipleMetalLoader(), new MetalLoader(), new BlockLoader() };

            long timepreone = 0;
            for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
                timepreone = System.nanoTime();
                DebugLog.log(
                    "Werkstoff is null or id < 0 ? " + (werkstoff == null || werkstoff.getmID() < 0)
                        + " "
                        + (System.nanoTime() - timepreone));
                if (werkstoff == null || werkstoff.getmID() < 0) {
                    progressBar.step("");
                    continue;
                }
                if (BetterLoadingScreen.isModLoaded()) size = CLSCompat.invokeStepSize(werkstoff, clsArr, size);
                DebugLog.log("Werkstoff: " + werkstoff.getDefaultName() + " " + (System.nanoTime() - timepreone));
                for (IWerkstoffRunnable runnable : werkstoffRunnables) {
                    String loaderName = runnable.getClass()
                        .getSimpleName();
                    DebugLog.log(loaderName + " started " + (System.nanoTime() - timepreone));
                    runnable.run(werkstoff);
                    DebugLog.log(loaderName + " done " + (System.nanoTime() - timepreone));
                }
                DebugLog.log("Done" + " " + (System.nanoTime() - timepreone));
                progressBar.step(werkstoff.getDefaultName());
            }
            DebugLog.log("Loading New Circuits" + " " + (System.nanoTime() - timepreone));
            BW_CircuitsLoader.initNewCircuits();

            if (BetterLoadingScreen.isModLoaded()) {
                CLSCompat.disableCls();
            }

            progressBar.step("Load Additional Recipes");
            AdditionalRecipes.run();
            ProgressManager.pop(progressBar);
            long timepost = System.nanoTime();
            MainMod.LOGGER.info(
                "Loading Processing Recipes for BW Materials took " + (timepost - timepre)
                    + "ns/"
                    + (timepost - timepre) / 1000000
                    + "ms/"
                    + (timepost - timepre) / 1000000000
                    + "s!");
            registered = true;
        }
    }

    @SuppressWarnings("unchecked")
    private static void addSubTags() {
        WerkstoffLoader.CubicZirconia.getStats()
            .setDurOverride(Materials.Diamond.mDurability);
        WerkstoffLoader.HDCS.getStats()
            .setSpeedOverride(Materials.HSSS.mToolSpeed);
        WerkstoffLoader.HDCS.getStats()
            .setDurMod(10f);
        Materials.Helium.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Neon.add(WerkstoffLoader.NOBLE_GAS);
        Materials.Argon.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Krypton.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Xenon.add(WerkstoffLoader.NOBLE_GAS, WerkstoffLoader.ANAEROBE_GAS);
        Materials.Radon.add(WerkstoffLoader.NOBLE_GAS);
        WerkstoffLoader.Oganesson.add(WerkstoffLoader.NOBLE_GAS, WerkstoffLoader.ANAEROBE_GAS);

        Materials.Nitrogen.add(WerkstoffLoader.ANAEROBE_GAS);

        WerkstoffLoader.Calcium.add(WerkstoffLoader.ANAEROBE_SMELTING);

        WerkstoffLoader.LuVTierMaterial.add(WerkstoffLoader.NOBLE_GAS_SMELTING);
        WerkstoffLoader.Ruridit.add(WerkstoffLoader.NOBLE_GAS_SMELTING);
        WerkstoffLoader.AdemicSteel.add(WerkstoffLoader.NOBLE_GAS_SMELTING);

        WerkstoffLoader.MagnetoResonaticDust.add(WerkstoffLoader.NO_BLAST);

        // Calcium Smelting block
        Materials.Calcium.mBlastFurnaceRequired = true;

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
            for (Pair<ISubTagContainer, Integer> pair : W.getContents()
                .getValue()
                .toArray(new Pair[0])) {

                if (pair.getKey() instanceof Materials && pair.getKey() == Materials.Neodymium) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM);
                    break;
                }
                if (pair.getKey() instanceof Materials && pair.getKey() == Materials.Iron) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_IRON);
                    break;
                } else if (pair.getKey() instanceof Materials && pair.getKey() == Materials.Gold) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD);
                    break;
                }
            }
            if (W.hasItemType(gem)) {
                W.add(SubTag.CRYSTAL);
                W.add(SubTag.CRYSTALLISABLE);
            }
        }
    }

    private static void addBridgeSubTags() {
        // add specific GT materials subtags to various werkstoff bridgematerials

        SubTag.METAL.addTo(LuVTierMaterial.getBridgeMaterial());
    }

    public static long toGenerateGlobal;

    private static void addItemsForGeneration() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.hasItemType(cell)) {
                if (!FluidRegistry.isFluidRegistered(werkstoff.getDefaultName())) {
                    DebugLog.log("Adding new Fluid: " + werkstoff.getDefaultName());
                    Fluid fluid = GT_FluidFactory.builder(werkstoff.getDefaultName())
                        .withLocalizedName(werkstoff.getDefaultName())
                        .withStateAndTemperature(
                            werkstoff.getStats()
                                .getFluidState(),
                            300)
                        .withColorRGBA(werkstoff.getRGBA())
                        .withTextureName("autogenerated")
                        .buildAndRegister()
                        .asFluid();
                    WerkstoffLoader.fluids.put(werkstoff, fluid);
                } else {
                    WerkstoffLoader.fluids.put(werkstoff, FluidRegistry.getFluid(werkstoff.getDefaultName()));
                }
            }
            if (werkstoff.hasItemType(OrePrefixes.cellMolten)) {
                if (!FluidRegistry.isFluidRegistered("molten." + werkstoff.getDefaultName())) {
                    DebugLog.log("Adding new Molten: " + werkstoff.getDefaultName());
                    Fluid fluid = GT_FluidFactory.builder("molten." + werkstoff.getDefaultName())
                        .withLocalizedName("Molten " + werkstoff.getDefaultName())
                        .withStateAndTemperature(
                            FluidState.MOLTEN,
                            werkstoff.getStats()
                                .getMeltingPoint() > 0 ? werkstoff.getStats()
                                    .getMeltingPoint() : 300)
                        .withColorRGBA(werkstoff.getRGBA())
                        .withTextureName("molten.autogenerated")
                        .buildAndRegister()
                        .asFluid();
                    WerkstoffLoader.molten.put(werkstoff, fluid);
                } else {
                    WerkstoffLoader.molten.put(werkstoff, FluidRegistry.getFluid(werkstoff.getDefaultName()));
                }
            }
            for (OrePrefixes p : values()) if (Materials.get(werkstoff.getDefaultName()) != null
                && Materials.get(werkstoff.getDefaultName()).mMetaItemSubID != -1
                && (werkstoff.getGenerationFeatures().toGenerate & p.mMaterialGenerationBits) != 0
                && OreDictHandler.getItemStack(werkstoff.getDefaultName(), p, 1) != null) {
                    DebugLog.log(
                        "Found: " + p
                            + werkstoff.getVarName()
                            + " in GT material system, disable and reroute my Items to that, also add a Tooltip.");
                    werkstoff.getGenerationFeatures()
                        .setBlacklist(p);
                }
            WerkstoffLoader.toGenerateGlobal = WerkstoffLoader.toGenerateGlobal
                | werkstoff.getGenerationFeatures().toGenerate;
        }
        DebugLog.log("GlobalGeneration: " + WerkstoffLoader.toGenerateGlobal);
        if ((WerkstoffLoader.toGenerateGlobal & 0b1) != 0) {
            WerkstoffLoader.items.put(dust, new BW_MetaGenerated_Items(dust));
            WerkstoffLoader.items.put(dustTiny, new BW_MetaGenerated_Items(dustTiny));
            WerkstoffLoader.items.put(dustSmall, new BW_MetaGenerated_Items(dustSmall));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10) != 0) {
            WerkstoffLoader.items.put(ingot, new BW_MetaGenerated_Items(ingot));
            WerkstoffLoader.items.put(ingotHot, new BW_MetaGenerated_Items(ingotHot)); // 1750
            WerkstoffLoader.items.put(nugget, new BW_MetaGenerated_Items(nugget));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100) != 0) {
            WerkstoffLoader.items.put(gem, new BW_MetaGenerated_Items(gem));
            WerkstoffLoader.items.put(gemChipped, new BW_MetaGenerated_Items(gemChipped));
            WerkstoffLoader.items.put(gemExquisite, new BW_MetaGenerated_Items(gemExquisite));
            WerkstoffLoader.items.put(gemFlawed, new BW_MetaGenerated_Items(gemFlawed));
            WerkstoffLoader.items.put(gemFlawless, new BW_MetaGenerated_Items(gemFlawless));
            WerkstoffLoader.items.put(lens, new BW_MetaGenerated_Items(lens));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000) != 0) {
            gameRegistryHandler();
            WerkstoffLoader.items.put(crushed, new BW_MetaGenerated_Items(crushed));
            WerkstoffLoader.items.put(crushedPurified, new BW_MetaGenerated_Items(crushedPurified));
            WerkstoffLoader.items.put(crushedCentrifuged, new BW_MetaGenerated_Items(crushedCentrifuged));
            WerkstoffLoader.items.put(dustPure, new BW_MetaGenerated_Items(dustPure));
            WerkstoffLoader.items.put(dustImpure, new BW_MetaGenerated_Items(dustImpure));
            WerkstoffLoader.items.put(rawOre, new BW_MetaGenerated_Items(rawOre));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000) != 0) {
            WerkstoffLoader.items.put(cell, new BW_MetaGenerated_Items(cell));
            if (Forestry.isModLoaded()) {
                BW_MetaGenerated_Items capsuleClass = new BW_MetaGenerated_Items(capsule);
                API.hideItem(new ItemStack(capsuleClass, 1, WILDCARD));
                WerkstoffLoader.items.put(capsule, new BW_MetaGenerated_Items(capsule));
            }
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100000) != 0) {
            WerkstoffLoader.items.put(cellPlasma, new BW_MetaGenerated_Items(cellPlasma));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000000) != 0) {
            WerkstoffLoader.items.put(OrePrefixes.cellMolten, new BW_MetaGenerated_Items(OrePrefixes.cellMolten));
            if (Forestry.isModLoaded()) {
                BW_MetaGenerated_Items capsuleMoltenClass = new BW_MetaGenerated_Items(OrePrefixes.capsuleMolten);
                API.hideItem(new ItemStack(capsuleMoltenClass, 1, WILDCARD));
                WerkstoffLoader.items.put(OrePrefixes.capsuleMolten, new BW_MetaGenerated_Items(OrePrefixes.capsuleMolten));
            }
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000000) != 0) {
            WerkstoffLoader.items.put(plate, new BW_MetaGenerated_Items(plate));
            WerkstoffLoader.items.put(foil, new BW_MetaGenerated_Items(foil));
            WerkstoffLoader.items.put(stick, new BW_MetaGenerated_Items(stick));
            WerkstoffLoader.items.put(stickLong, new BW_MetaGenerated_Items(stickLong));
            WerkstoffLoader.items.put(toolHeadWrench, new BW_MetaGenerated_Items(toolHeadWrench));
            WerkstoffLoader.items.put(toolHeadHammer, new BW_MetaGenerated_Items(toolHeadHammer));
            WerkstoffLoader.items.put(toolHeadSaw, new BW_MetaGenerated_Items(toolHeadSaw));
            WerkstoffLoader.items.put(turbineBlade, new BW_MetaGenerated_Items(turbineBlade));
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

    static void gameRegistryHandler() {
        if (SideReference.Side.Client) BW_Renderer_Block_Ores.register();

        GameRegistry.registerTileEntity(BW_MetaGeneratedOreTE.class, "bw.blockoresTE");
        GameRegistry.registerTileEntity(BW_MetaGeneratedSmallOreTE.class, "bw.blockoresSmallTE");
        GameRegistry.registerTileEntity(BW_MetaGenerated_WerkstoffBlock_TE.class, "bw.werkstoffblockTE");
        GameRegistry.registerTileEntity(BW_MetaGeneratedBlocks_Casing_TE.class, "bw.werkstoffblockcasingTE");
        GameRegistry
            .registerTileEntity(BW_MetaGeneratedBlocks_CasingAdvanced_TE.class, "bw.werkstoffblockscasingadvancedTE");

        WerkstoffLoader.BWOres = new BW_MetaGenerated_Ores(Material.rock, BW_MetaGeneratedOreTE.class, "bw.blockores");
        WerkstoffLoader.BWSmallOres = new BW_MetaGenerated_SmallOres(
            Material.rock,
            BW_MetaGeneratedSmallOreTE.class,
            "bw.blockoresSmall");
        WerkstoffLoader.BWBlocks = new BW_MetaGenerated_WerkstoffBlocks(
            Material.iron,
            BW_MetaGenerated_WerkstoffBlock_TE.class,
            "bw.werkstoffblocks");
        WerkstoffLoader.BWBlockCasings = new BW_MetaGeneratedBlocks_Casing(
            Material.iron,
            BW_MetaGeneratedBlocks_Casing_TE.class,
            "bw.werkstoffblockscasing",
            OrePrefixes.blockCasing);
        WerkstoffLoader.BWBlockCasingsAdvanced = new BW_MetaGeneratedBlocks_Casing(
            Material.iron,
            BW_MetaGeneratedBlocks_CasingAdvanced_TE.class,
            "bw.werkstoffblockscasingadvanced",
            OrePrefixes.blockCasingAdvanced);

        GameRegistry.registerBlock(WerkstoffLoader.BWOres, BW_MetaGeneratedBlock_Item.class, "bw.blockores.01");
        GameRegistry.registerBlock(WerkstoffLoader.BWSmallOres, BW_MetaGeneratedBlock_Item.class, "bw.blockores.02");
        GameRegistry.registerBlock(WerkstoffLoader.BWBlocks, BW_MetaGeneratedBlock_Item.class, "bw.werkstoffblocks.01");
        if (!ConfigHandler.disableBoltedBlocksCasing) GameRegistry.registerBlock(
            WerkstoffLoader.BWBlockCasings,
            BW_MetaGeneratedBlock_Item.class,
            "bw.werkstoffblockscasing.01");
        if (!ConfigHandler.disableReboltedBlocksCasing) GameRegistry.registerBlock(
            WerkstoffLoader.BWBlockCasingsAdvanced,
            BW_MetaGeneratedBlock_Item.class,
            "bw.werkstoffblockscasingadvanced.01");

        GTMetaItemEnhancer.addAdditionalOreDictToForestry();
        GTMetaItemEnhancer.init();
    }

    private static void runGTItemDataRegistrator() {
        IWerkstoffRunnable[] registrations = { new BridgeMaterialsLoader(), new AssociationLoader(),
            new CasingRegistrator() };
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            for (IWerkstoffRunnable registration : registrations) {
                registration.run(werkstoff);
            }
        }
        addFakeItemDataToInWorldBlocksAndCleanUpFakeData();
        addVanillaCasingsToGTOreDictUnificator();
    }

    public static void addVanillaCasingsToGTOreDictUnificator() {
        GT_OreDictUnificator
            .addAssociation(OrePrefixes.blockCasing, Materials.Aluminium, ItemList.Casing_FrostProof.get(1L), false);
        GT_OreDictUnificator
            .addAssociation(OrePrefixes.blockCasing, Materials.Nickel, ItemList.Casing_HeatProof.get(1L), false);
        GT_OreDictUnificator
            .addAssociation(OrePrefixes.blockCasing, Materials.Lead, ItemList.Casing_RadiationProof.get(1L), false);
        GT_OreDictUnificator
            .addAssociation(OrePrefixes.blockCasing, Materials.Steel, ItemList.Casing_SolidSteel.get(1L), false);
        GT_OreDictUnificator.addAssociation(
            OrePrefixes.blockCasing,
            Materials.TungstenSteel,
            ItemList.Casing_RobustTungstenSteel.get(1L),
            false);
        GT_OreDictUnificator.addAssociation(
            OrePrefixes.blockCasing,
            Materials.Polytetrafluoroethylene,
            ItemList.Casing_Chemically_Inert.get(1L),
            false);
    }

    /**
     * very hacky way to make my ores/blocks/smallores detectable by gt assosication in world, well at least the prefix.
     * used for the miners mostly removing this hacky material from the materials map instantly. we only need the item
     * data.
     */
    @SuppressWarnings("unchecked")
    private static void addFakeItemDataToInWorldBlocksAndCleanUpFakeData() {

        Map<String, Materials> MATERIALS_MAP = null;

        try {
            Field f = Materials.class.getDeclaredField("MATERIALS_MAP");
            f.setAccessible(true);
            MATERIALS_MAP = (Map<String, Materials>) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }

        if (MATERIALS_MAP == null) throw new NullPointerException("MATERIALS_MAP null!");

        Materials oreMat = new Materials(-1, null, 0, 0, 0, false, "bwores", "bwores", null, true, null);
        Materials smallOreMat = new Materials(-1, null, 0, 0, 0, false, "bwsmallores", "bwsmallores", null, true, null);
        Materials blockMat = new Materials(-1, null, 0, 0, 0, false, "bwblocks", "bwblocks", null, true, null);

        for (int i = 0; i < 16; i++) {
            GT_OreDictUnificator.addAssociation(ore, oreMat, new ItemStack(BWOres, 1, i), true);
            GT_OreDictUnificator.addAssociation(oreSmall, smallOreMat, new ItemStack(BWSmallOres, 1, i), true);
            GT_OreDictUnificator.addAssociation(block, blockMat, new ItemStack(BWBlocks, 1, i), true);
        }

        MATERIALS_MAP.remove("bwores");
        MATERIALS_MAP.remove("bwsmallores");
        MATERIALS_MAP.remove("bwblocks");
    }

    public static void removeIC2Recipes() {
        try {
            Set<Map.Entry<IRecipeInput, RecipeOutput>> remset = new HashSet<>();
            for (Map.Entry<IRecipeInput, RecipeOutput> curr : Recipes.macerator.getRecipes()
                .entrySet()) {
                if (curr.getKey() instanceof RecipeInputOreDict) {
                    if ("oreNULL".equalsIgnoreCase(((RecipeInputOreDict) curr.getKey()).input)) {
                        remset.add(curr);
                    }
                    for (ItemStack stack : curr.getValue().items) {
                        if (stack.getItem() instanceof BW_MetaGenerated_Items) remset.add(curr);
                    }
                }
            }
            Recipes.macerator.getRecipes()
                .entrySet()
                .removeAll(remset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runAdditionalOreDict() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.hasItemType(ore)) {
                GT_OreDictUnificator.registerOre(ore + werkstoff.getVarName(), werkstoff.get(ore));
                GT_OreDictUnificator.registerOre(oreSmall + werkstoff.getVarName(), werkstoff.get(oreSmall));
                werkstoff.getADDITIONAL_OREDICT()
                    .forEach(e -> OreDictionary.registerOre(ore + e, werkstoff.get(ore)));
                werkstoff.getADDITIONAL_OREDICT()
                    .forEach(e -> OreDictionary.registerOre(oreSmall + e, werkstoff.get(oreSmall)));
            }

            if (werkstoff.hasItemType(gem)) OreDictionary.registerOre(
                "craftingLens" + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""),
                werkstoff.get(lens));

            if (werkstoff.hasItemType(gem) || werkstoff.hasItemType(ingot)) {
                GT_OreDictUnificator.registerOre(block + werkstoff.getVarName(), werkstoff.get(block));
                werkstoff.getADDITIONAL_OREDICT()
                    .forEach(e -> OreDictionary.registerOre(block + e, werkstoff.get(block)));
            }

            werkstoff.getADDITIONAL_OREDICT()
                .forEach(
                    s -> ENABLED_ORE_PREFIXES.stream()
                        .filter(o -> Objects.nonNull(werkstoff.get(o)))
                        .forEach(od -> OreDictionary.registerOre(od + s, werkstoff.get(od))));
        }

        GT_OreDictUnificator.registerOre("craftingIndustrialDiamond", WerkstoffLoader.CubicZirconia.get(gemExquisite));
    }
}
