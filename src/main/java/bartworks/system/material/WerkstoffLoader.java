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

package bartworks.system.material;

import static bartworks.util.BWUtil.subscriptNumbers;
import static bartworks.util.BWUtil.superscriptNumbers;
import static gregtech.api.enums.Mods.BetterLoadingScreen;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.bolt;
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
import static gregtech.api.enums.OrePrefixes.ingotHot;
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
import static gregtech.api.enums.OrePrefixes.wireFine;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import com.google.common.collect.HashBiMap;

import bartworks.API.WerkstoffAdderRegistry;
import bartworks.MainMod;
import bartworks.system.material.CircuitGeneration.BWMetaItems;
import bartworks.system.material.gtenhancement.GTMetaItemEnhancer;
import bartworks.system.material.processingLoaders.AdditionalRecipes;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.system.material.werkstoff_loaders.recipe.AspectLoader;
import bartworks.system.material.werkstoff_loaders.recipe.BlockLoader;
import bartworks.system.material.werkstoff_loaders.recipe.CasingLoader;
import bartworks.system.material.werkstoff_loaders.recipe.CellLoader;
import bartworks.system.material.werkstoff_loaders.recipe.CraftingMaterialLoader;
import bartworks.system.material.werkstoff_loaders.recipe.CrushedLoader;
import bartworks.system.material.werkstoff_loaders.recipe.DustLoader;
import bartworks.system.material.werkstoff_loaders.recipe.GemLoader;
import bartworks.system.material.werkstoff_loaders.recipe.MetalLoader;
import bartworks.system.material.werkstoff_loaders.recipe.MoltenCellLoader;
import bartworks.system.material.werkstoff_loaders.recipe.MultipleMetalLoader;
import bartworks.system.material.werkstoff_loaders.recipe.RawOreLoader;
import bartworks.system.material.werkstoff_loaders.recipe.SimpleMetalLoader;
import bartworks.system.material.werkstoff_loaders.recipe.ToolLoader;
import bartworks.system.material.werkstoff_loaders.registration.AssociationLoader;
import bartworks.system.material.werkstoff_loaders.registration.BridgeMaterialsLoader;
import bartworks.system.material.werkstoff_loaders.registration.CasingRegistrator;
import bartworks.system.oredict.OreDictHandler;
import bartworks.util.BWColorUtil;
import bartworks.util.EnumUtils;
import bartworks.util.log.DebugLog;
import bwcrossmod.cls.CLSCompat;
import codechicken.nei.api.API;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Element;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.OreInfo;

@SuppressWarnings("deprecation")
public class WerkstoffLoader {

    private WerkstoffLoader() {}

    public static final SubTag NOBLE_GAS = SubTag.getNewSubTag("NobleGas");
    public static final SubTag ANAEROBE_GAS = SubTag.getNewSubTag("AnaerobeGas");
    /**
     * Was used to add Nitrogen, Xenon and Oganesson to blast furnace smelting recipe. Now it just adds all types of
     * gasses.
     */
    public static final SubTag ANAEROBE_SMELTING = SubTag.getNewSubTag("AnaerobeSmelting");
    /**
     * Was used to add noble gasses to blast furnace smelting recipe. Now it just adds all types of gasses.
     */
    public static final SubTag NOBLE_GAS_SMELTING = SubTag.getNewSubTag("NobleGasSmelting");
    public static final SubTag NO_BLAST = SubTag.getNewSubTag("NoBlast");

    public static void setUp() {
        // add tiberium
        EnumUtils.createNewElement("Tr", 123L, 203L, 0L, -1L, null, "Tiberium", false);

        Werkstoff.GenerationFeatures.initPrefixLogic();
        BWGTMaterialReference.init();
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
        Pair.of(Materials.Bismuth, 2),
        Pair.of(Materials.Oxygen, 2),
        Pair.of(Materials.CarbonDioxide, 2));
    public static final Werkstoff Bismuthinit = new Werkstoff(
        new short[] { 192, 192, 192, 0 },
        "Bismuthinite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        2,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Bismuth, Materials.Sulfur),
        Pair.of(Materials.Bismuth, 2),
        Pair.of(Materials.Sulfur, 3));
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
        Pair.of(WerkstoffLoader.Zirconium, 1),
        Pair.of(Materials.Oxygen, 2));
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
        Pair.of(Materials.Sodium, 1),
        Pair.of(Materials.Iron, 3),
        Pair.of(Materials.Aluminium, 6),
        Pair.of(Materials.Silicon, 6),
        Pair.of(Materials.Boron, 3),
        Pair.of(Materials.Oxygen, 30),
        Pair.of(Materials.Fluorine, 1));
    public static final Werkstoff YttriumOxide = new Werkstoff(
        new short[] { 255, 255, 255, 0 },
        "Yttrium Oxide",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .enforceUnification(), // No autoadd here to gate this material
        // by hand
        6,
        TextureSet.SET_DULL,
        Pair.of(Materials.Yttrium, 2),
        Pair.of(Materials.Oxygen, 3));
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
        Pair.of(Materials.Sodium, 1),
        Pair.of(Materials.Chrome, 3),
        Pair.of(Materials.Magnalium, 6),
        Pair.of(Materials.Silicon, 6),
        Pair.of(Materials.Boron, 3),
        Pair.of(Materials.Oxygen, 31),
        Pair.of(Materials.Hydrogen, 3));
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
        Pair.of(Materials.Sodium, 1),
        Pair.of(Materials.Vanadium, 3),
        Pair.of(Materials.Magnalium, 6),
        Pair.of(Materials.Silicon, 6),
        Pair.of(Materials.Boron, 3),
        Pair.of(Materials.Oxygen, 31),
        Pair.of(Materials.Hydrogen, 3));
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
        Pair.of(Materials.Sodium, 1),
        Pair.of(Materials.Aluminium, 9),
        Pair.of(Materials.Silicon, 6),
        Pair.of(Materials.Boron, 3),
        Pair.of(Materials.Oxygen, 31),
        Pair.of(Materials.Hydrogen, 1));
    public static final Werkstoff Arsenopyrite = new Werkstoff(
        new short[] { 0xB0, 0xB0, 0xB0, 0 },
        "Arsenopyrite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        10,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Sulfur, Materials.Arsenic, Materials.Iron),
        Pair.of(Materials.Iron, 1),
        Pair.of(Materials.Arsenic, 1),
        Pair.of(Materials.Sulfur, 1));
    public static final Werkstoff Ferberite = new Werkstoff(
        new short[] { 0xB0, 0xB0, 0xB0, 0 },
        "Ferberite",
        Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND)
            .setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        11,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Iron, Materials.Tungsten),
        Pair.of(Materials.Iron, 1),
        Pair.of(Materials.Tungsten, 1),
        Pair.of(Materials.Oxygen, 3));
    public static final Werkstoff Loellingit = new Werkstoff(
        new short[] { 0xD0, 0xD0, 0xD0, 0 },
        "Loellingite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        12,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Iron, Materials.Arsenic),
        Pair.of(Materials.Iron, 1),
        Pair.of(Materials.Arsenic, 2));
    public static final Werkstoff Roquesit = new Werkstoff(
        new short[] { 0xA0, 0xA0, 0xA0, 0 },
        "Roquesite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        13,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Sulfur),
        Pair.of(Materials.Copper, 1),
        Pair.of(Materials.Indium, 1),
        Pair.of(Materials.Sulfur, 2));
    public static final Werkstoff Bornite = new Werkstoff(
        new short[] { 0x97, 0x66, 0x2B, 0 },
        "Bornite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        14,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Iron, Materials.Sulfur),
        Pair.of(Materials.Copper, 5),
        Pair.of(Materials.Iron, 1),
        Pair.of(Materials.Sulfur, 4));
    public static final Werkstoff Wittichenit = new Werkstoff(
        Materials.Copper.mRGBa,
        "Wittichenite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        15,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Bismuth, Materials.Sulfur),
        Pair.of(Materials.Copper, 5),
        Pair.of(Materials.Bismuth, 1),
        Pair.of(Materials.Sulfur, 4));
    public static final Werkstoff Djurleit = new Werkstoff(
        new short[] { 0x60, 0x60, 0x60, 0 },
        "Djurleite",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        16,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Copper, Materials.Copper, Materials.Sulfur),
        Pair.of(Materials.Copper, 31),
        Pair.of(Materials.Sulfur, 16));
    public static final Werkstoff Huebnerit = new Werkstoff(
        new short[] { 0x80, 0x60, 0x60, 0 },
        "Huebnerite",
        Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND)
            .setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        17,
        TextureSet.SET_METALLIC,
        Arrays.asList(Materials.Manganese, Materials.Tungsten),
        Pair.of(Materials.Manganese, 1),
        Pair.of(Materials.Tungsten, 1),
        Pair.of(Materials.Oxygen, 3));
    public static final Werkstoff Thorianit = new Werkstoff(
        new short[] { 0x30, 0x30, 0x30, 0 },
        "Thorianite",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        18,
        TextureSet.SET_METALLIC,
        Collections.singletonList(Materials.Thorium),
        Pair.of(Materials.Thorium, 1),
        Pair.of(Materials.Oxygen, 2));
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
        Pair.of(WerkstoffLoader.Zirconium, 1),
        Pair.of(Materials.Silicon, 1),
        Pair.of(Materials.Oxygen, 4));

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
        Pair.of(Materials.Salt, 1));
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
        Pair.of(Materials.Spodumene, 1));
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
        Pair.of(Materials.RockSalt, 1));

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
        Pair.of(Materials.Iron, 2),
        Pair.of(Materials.Silicon, 1),
        Pair.of(Materials.Oxygen, 4));
    public static final Werkstoff Forsterit = new Werkstoff(
        new short[] { 255, 255, 255, 0 },
        "Forsterite",
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        24,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Magnesium, Materials.SiliconDioxide),
        Pair.of(Materials.Magnesium, 2),
        Pair.of(Materials.Silicon, 1),
        Pair.of(Materials.Oxygen, 4));
    public static final Werkstoff Hedenbergit = new Werkstoff(
        new short[] { 100, 150, 100, 0 },
        "Hedenbergite",
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        25,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Iron, Materials.Calcium, Materials.SiliconDioxide),
        Pair.of(Materials.Calcium, 1),
        Pair.of(Materials.Iron, 1),
        Pair.of(Materials.Silicon, 2),
        Pair.of(Materials.Oxygen, 6));
    public static final Werkstoff DescloiziteZNVO4 = new Werkstoff(
        new short[] { 0xBF, 0x18, 0x0F, 0 },
        "Red Descloizite", // Pb(Zn,Cu)[OH|VO4
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        26,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Lead, Materials.Copper, Materials.Vanadium),
        Pair.of(Materials.Lead, 1),
        Pair.of(Materials.Zinc, 1),
        Pair.of(Materials.Vanadium, 1),
        Pair.of(Materials.Oxygen, 4));
    public static final Werkstoff DescloiziteCUVO4 = new Werkstoff(
        new short[] { 0xf9, 0x6d, 0x18, 0 },
        "Orange Descloizite", // Pb(Zn,Cu)[OH|VO4
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        27,
        TextureSet.SET_QUARTZ,
        Arrays.asList(Materials.Lead, Materials.Zinc, Materials.Vanadium),
        Pair.of(Materials.Lead, 1),
        Pair.of(Materials.Copper, 1),
        Pair.of(Materials.Vanadium, 1),
        Pair.of(Materials.Oxygen, 4));
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
        Pair.of(Materials.Potassium, 1),
        Pair.of(Materials.Aluminium, 3),
        Pair.of(Materials.Silicon, 3),
        Pair.of(Materials.Oxygen, 12),
        Pair.of(Materials.Hydrogen, 2));

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
        Pair.of(Materials.Potassium, 1),
        Pair.of(Materials.Chrome, 3),
        Pair.of(Materials.Silicon, 3),
        Pair.of(Materials.Oxygen, 12),
        Pair.of(Materials.Hydrogen, 2));

    public static final Werkstoff Thorium232 = new Werkstoff(
        new short[] { 0, 64, 0, 0 },
        "Thorium 232",
        superscriptNumbers("232Th"),
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
        Pair.of(Materials.Bismuth, 2),
        Pair.of(Materials.Tellurium, 3));
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
        Pair.of(Materials.Tellurium, 1));
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
        Pair.of(Materials.Bismuth, 2),
        Pair.of(Materials.Boron, 1),
        Pair.of(Materials.Hydrogen, 1));
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
        Pair.of(Materials.IndiumGalliumPhosphide, 1),
        Pair.of(WerkstoffLoader.BismuthHydroBorat, 3),
        Pair.of(WerkstoffLoader.BismuthTellurite, 2));

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
        Pair.of(Materials.Silicon, 5),
        Pair.of(Materials.Oxygen, 10),
        Pair.of(Materials.Iron, 1));

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
        Pair.of(WerkstoffLoader.Prasiolite, 3),
        Pair.of(WerkstoffLoader.BismuthTellurite, 4),
        Pair.of(WerkstoffLoader.CubicZirconia, 1),
        Pair.of(Materials.SteelMagnetic, 1));
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
            .addMetalCraftingSolidifierRecipes()
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
        Pair.of(Materials.Calcium, 1));
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
        Pair.of(Materials.Boron, 1),
        Pair.of(Materials.Argon, 1),
        Pair.of(Materials.Titanium, 1),
        Pair.of(Materials.Magic, 1),
        Pair.of(Materials.Europium, 1),
        Pair.of(Materials.Sulfur, 1),
        Pair.of(WerkstoffLoader.Neon, 1),
        Pair.of(Materials.Potassium, 1));
    public static final Werkstoff PTConcentrate = new Werkstoff(
        Materials.Platinum.getRGBA(),
        "Platinum Concentrate",
        subscriptNumbers("Pt?Pd?") + "??",
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
        subscriptNumbers("Pt?") + "??",
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
        subscriptNumbers("Pt?") + "??",
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
        subscriptNumbers("Pt?Pd?Ir?Os?Rh?Ru?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        47,
        TextureSet.SET_METALLIC,
        // No Byproducts
        Pair.of(Materials.Platinum, 1),
        Pair.of(Materials.Stone, 2));
    // TODO: If there is a moment where we are happy with breaking everyone's platline, change Aqua Regia recipes to
    // satisfy Chem
    // TODO: Balance with formula (HCl)3(HNO3) and then add the correct formula to the material
    public static final Werkstoff AquaRegia = new Werkstoff(
        new short[] { 0xff, 0xb1, 0x32 },
        "Aqua Regia",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        48,
        TextureSet.SET_FLUID);
    public static final Werkstoff PTResidue = new Werkstoff(
        new short[] { 0x64, 0x63, 0x2E },
        "Platinum Residue",
        subscriptNumbers("Ir?Os?Rh?Ru?") + "??",
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
        Pair.of(Materials.Ammonium, 1),
        Pair.of(Materials.HydrochloricAcid, 1));
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
        subscriptNumbers("Pd?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        52,
        TextureSet.SET_FLUID,
        // No Byproducts
        Pair.of(Materials.Ammonium, 1),
        Pair.of(Materials.Palladium, 1));
    public static final Werkstoff PDMetallicPowder = new Werkstoff(
        Materials.Palladium.getRGBA(),
        "Palladium Metallic Powder",
        subscriptNumbers("Pd?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        53,
        TextureSet.SET_METALLIC,
        // No Byproducts
        Pair.of(Materials.Palladium, 1),
        Pair.of(Materials.Stone, 2));
    public static final Werkstoff PDRawPowder = new Werkstoff(
        Materials.Palladium.getRGBA(),
        "Reprecipitated Palladium",
        subscriptNumbers("Pd?") + "??",
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
        subscriptNumbers("Pd?") + "??",
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
        Pair.of(Materials.SodiumHydroxide, 1),
        Pair.of(Materials.CarbonMonoxide, 1));
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
        Pair.of(Materials.Sodium, 2),
        Pair.of(Materials.Sulfur, 1),
        Pair.of(Materials.Oxygen, 4));
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
        Pair.of(Materials.Carbon, 1),
        Pair.of(Materials.Hydrogen, 2),
        Pair.of(Materials.Oxygen, 2));
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
        Pair.of(Materials.Potassium, 2),
        Pair.of(Materials.Sulfur, 2),
        Pair.of(Materials.Oxygen, 7));
    public static final Werkstoff LeachResidue = new Werkstoff(
        new short[] { 0x64, 0x46, 0x29 },
        "Leach Residue",
        subscriptNumbers("Is?Os?Ru?") + "??",
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
        subscriptNumbers("Rh?K?S?O?") + "??",
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
        subscriptNumbers("Rh?S?O?") + "??",
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
        Pair.of(Materials.Calcium, 1),
        Pair.of(Materials.Chlorine, 2)
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
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .enforceUnification(),
        64,
        TextureSet.SET_METALLIC
    // No Byproducts
    );
    public static final Werkstoff SodiumRuthenate = new Werkstoff(
        new short[] { 0x3a, 0x40, 0xcb },
        "Sodium Ruthenate",
        subscriptNumbers("Na?Ru?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        65,
        TextureSet.SET_SHINY
    // No Byproducts
    );
    public static final Werkstoff RutheniumTetroxide = new Werkstoff(
        new short[] { 0xc7, 0xc7, 0xc7 },
        "Ruthenium Tetroxide",
        subscriptNumbers("Ru?O?") + "??",
        new Werkstoff.Stats().setMeltingPoint(313),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addCells(),
        66,
        TextureSet.SET_DULL
    // No Byproducts
    );
    public static final Werkstoff HotRutheniumTetroxideSollution = new Werkstoff(
        new short[] { 0xc7, 0xc7, 0xc7 },
        "Hot Ruthenium Tetroxide Solution",
        subscriptNumbers("Ru?O?") + "??",
        new Werkstoff.Stats().setGas(true)
            .setMeltingPoint(700),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        67,
        TextureSet.SET_FLUID
    // No Byproducts
    );
    public static final Werkstoff RutheniumTetroxideSollution = new Werkstoff(
        new short[] { 0xc7, 0xc7, 0xc7 },
        "Ruthenium Tetroxide Solution",
        subscriptNumbers("Ru?O?") + "??",
        new Werkstoff.Stats().setMeltingPoint(313),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        68,
        TextureSet.SET_FLUID
    // No Byproducts
    );
    public static final Werkstoff IrOsLeachResidue = new Werkstoff(
        new short[] { 0x64, 0x46, 0x29 },
        "Rarest Metal Residue",
        subscriptNumbers("Os?Ir?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        69,
        TextureSet.SET_ROUGH,
        // No Byproducts
        Pair.of(Materials.Osmiridium, 1),
        Pair.of(Materials.Stone, 2));
    public static final Werkstoff IrLeachResidue = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Iridium Metal Residue",
        subscriptNumbers("Ir?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        70,
        TextureSet.SET_ROUGH,
        Pair.of(Materials.Iridium, 1),
        Pair.of(Materials.Stone, 2)
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
        Pair.of(Materials.SiliconDioxide, 3),
        Pair.of(Materials.Gold, 2));
    public static final Werkstoff AcidicOsmiumSolution = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Acidic Osmium Solution",
        subscriptNumbers("Os?H?Cl?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        72,
        TextureSet.SET_FLUID);
    public static final Werkstoff IridiumDioxide = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Iridium Dioxide",
        subscriptNumbers("Ir?O?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        73,
        TextureSet.SET_FLUID);
    public static final Werkstoff OsmiumSolution = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Osmium Solution",
        subscriptNumbers("Os?Cl?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        74,
        TextureSet.SET_FLUID);
    public static final Werkstoff AcidicIridiumSolution = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Acidic Iridium Solution",
        subscriptNumbers("Ir?Cl?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        75,
        TextureSet.SET_FLUID);
    public static final Werkstoff IridiumChloride = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Iridium Chloride",
        subscriptNumbers("Ir?Cl?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        76,
        TextureSet.SET_LAPIS);
    public static final Werkstoff PGSDResidue2 = new Werkstoff(
        new short[] { 0x84, 0x66, 0x49 },
        "Metallic Sludge Dust Residue",
        new Werkstoff.Stats().setCentrifuge(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        77,
        TextureSet.SET_DULL,
        Pair.of(Materials.Nickel, 1),
        Pair.of(Materials.Copper, 1));
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
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .enforceUnification(),
        78,
        TextureSet.SET_METALLIC);
    public static final Werkstoff CrudeRhMetall = new Werkstoff(
        new short[] { 0x66, 0x66, 0x66 },
        "Crude Rhodium Metal",
        subscriptNumbers("Rh?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        79,
        TextureSet.SET_DULL,
        Pair.of(Rhodium, 1),
        Pair.of(Materials.Stone, 1));
    public static final Werkstoff RHSalt = new Werkstoff(
        new short[] { 0x84, 0x84, 0x84 },
        "Rhodium Salt",
        subscriptNumbers("Rh?Na?Cl?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        80,
        TextureSet.SET_GEM_VERTICAL);
    public static final Werkstoff RHSaltSolution = new Werkstoff(
        new short[] { 0x66, 0x77, 0x88 },
        "Rhodium Salt Solution",
        subscriptNumbers("Rh?Na?Cl?") + "??",
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
        Pair.of(Materials.Sodium, 1),
        Pair.of(Materials.Nitrogen, 1),
        Pair.of(Materials.Oxygen, 3));
    public static final Werkstoff RHNitrate = new Werkstoff(
        new short[] { 0x77, 0x66, 0x49 },
        "Rhodium Nitrate",
        subscriptNumbers("Rh?N?O?") + "??",
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
        Pair.of(Materials.Zinc, 1),
        Pair.of(Materials.Sulfur, 1),
        Pair.of(Materials.Oxygen, 4));
    public static final Werkstoff RhFilterCake = new Werkstoff(
        new short[] { 0x77, 0x66, 0x49 },
        "Rhodium Filter Cake",
        subscriptNumbers("Rh?N?O?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        85,
        TextureSet.SET_QUARTZ);
    public static final Werkstoff RHFilterCakeSolution = new Werkstoff(
        new short[] { 0x66, 0x77, 0x88 },
        "Rhodium Filter Cake Solution",
        subscriptNumbers("Rh?N?") + "??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        86,
        TextureSet.SET_FLUID);
    public static final Werkstoff ReRh = new Werkstoff(
        new short[] { 0x77, 0x66, 0x49 },
        "Reprecipitated Rhodium",
        subscriptNumbers("Rh?N?H?") + "??",
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
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        88,
        TextureSet.SET_METALLIC,
        Pair.of(Materials.Palladium, 3),
        Pair.of(WerkstoffLoader.Rhodium, 1));
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
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        90,
        TextureSet.SET_METALLIC,
        Pair.of(WerkstoffLoader.Ruthenium, 2),
        Pair.of(Materials.Iridium, 1));
    public static final Werkstoff Fluorspar = new Werkstoff(
        new short[] { 185, 69, 251 },
        "Fluorspar",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addGems(),
        91,
        TextureSet.SET_GEM_VERTICAL,
        Pair.of(Materials.Calcium, 1),
        Pair.of(Materials.Fluorine, 2));
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
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        92,
        TextureSet.SET_SHINY,
        Pair.of(Materials.TungstenSteel, 12),
        Pair.of(Materials.HSSE, 9),
        Pair.of(Materials.HSSG, 6),
        Pair.of(WerkstoffLoader.Ruridit, 3),
        Pair.of(WerkstoffLoader.MagnetoResonaticDust, 2),
        Pair.of(Materials.Plutonium, 1));
    public static final Werkstoff Atheneite = new Werkstoff(
        new short[] { 175, 175, 175 },
        "Atheneite",
        subscriptNumbers("(Pd,Hg)3As"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        93,
        TextureSet.SET_SHINY,
        Pair.of(WerkstoffLoader.PDMetallicPowder, 3),
        Pair.of(Materials.Mercury, 3),
        Pair.of(Materials.Arsenic, 1));
    public static final Werkstoff Temagamite = new Werkstoff(
        new short[] { 245, 245, 245 },
        "Temagamite",
        subscriptNumbers("Pd3HgTe"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        94,
        TextureSet.SET_ROUGH,
        Pair.of(WerkstoffLoader.PDMetallicPowder, 3),
        Pair.of(Materials.Mercury, 1),
        Pair.of(Materials.Tellurium, 1));
    public static final Werkstoff Terlinguaite = new Werkstoff(
        new short[] { 245, 245, 245 },
        "Terlinguaite",
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures(),
        95,
        TextureSet.SET_GEM_HORIZONTAL,
        Pair.of(Materials.Mercury, 2),
        Pair.of(Materials.Chlorine, 1),
        Pair.of(Materials.Oxygen, 1));
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
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        96,
        TextureSet.SET_METALLIC,
        Pair.of(Materials.Steel, 2),
        Pair.of(Materials.VanadiumSteel, 1),
        Pair.of(Materials.DamascusSteel, 1),
        Pair.of(Materials.Carbon, 4));
    public static final Werkstoff RawAdemicSteel = new Werkstoff(
        new short[] { 0xed, 0xed, 0xed },
        "Raw Ademic Steel",
        new Werkstoff.Stats().setCentrifuge(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMixerRecipes(),
        97,
        TextureSet.SET_ROUGH,
        Pair.of(Materials.Steel, 2),
        Pair.of(Materials.VanadiumSteel, 1),
        Pair.of(Materials.DamascusSteel, 1));
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
        Pair.of(Materials.Hydrogen, 2),
        Pair.of(Materials.Silicon, 1),
        Pair.of(Materials.Fluorine, 6));
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
        Pair.of(Materials.Potassium, 2),
        Pair.of(Materials.Silicon, 1),
        Pair.of(Materials.Fluorine, 6));
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
        Pair.of(Materials.Potassium, 2),
        Pair.of(Materials.Carbon, 1),
        Pair.of(Materials.Oxygen, 3));
    public static final Werkstoff RawFluorophlogopite = new Werkstoff(
        new short[] { 0x36, 0x51, 0x0b },
        "Raw Fluorophlogopite",
        subscriptNumbers("K4Al2(SiO2)F6"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        102,
        TextureSet.SET_DULL);
    public static final Werkstoff HotFluorophlogopite = new Werkstoff(
        new short[] { 0xbf, 0xd3, 0x55 },
        "Unformed Fluorophlogopite",
        subscriptNumbers("KMg3(Si3Al)O10F2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        103,
        TextureSet.SET_FLUID);
    public static final Werkstoff Fluorophlogopite = new Werkstoff(
        new short[] { 0xbf, 0xd3, 0x55 },
        "Fluorophlogopite",
        subscriptNumbers("KMg3(Si3Al)O10F2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMetalItems(),
        104,
        TextureSet.SET_SHINY);

    // Extracted from GalaxySpace
    public static final Werkstoff LiquidHelium = new Werkstoff(
        new short[] { 210, 230, 250 },
        "Liquid Helium",
        "He",
        new Werkstoff.Stats().setBoilingPoint(4)
            .setGas(false)
            .setMeltingPoint(1),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        11500,
        TextureSet.SET_FLUID);

    public static final Werkstoff HafniumCarbide = new Werkstoff(
        new short[] { 125, 135, 125 },
        "Hafnium Carbide",
        "HfC",
        new Werkstoff.Stats().setMass(192),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust(),
        11501,
        TextureSet.SET_METALLIC);

    public static final Werkstoff TantalumCarbideHafniumCarbideMixture = new Werkstoff(
        new short[] { 75, 85, 75 },
        "Tantalum Carbide / Hafnium Carbide Mixture",
        subscriptNumbers("(TaC)4HfC"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust(),
        11502,
        TextureSet.SET_METALLIC);

    public static final Werkstoff TantalumHafniumCarbide = new Werkstoff(
        new short[] { 80, 90, 80 },
        "Tantalum Hafnium Carbide",
        subscriptNumbers("Ta4HfC5"),
        new Werkstoff.Stats().setMass(962)
            .setMeltingPoint(4263)
            .setDurOverride(4608)
            .setSpeedOverride(12)
            .setQualityOverride((byte) 7),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMetalItems()
            .addMolten(),
        11503,
        TextureSet.SET_METALLIC);

    public static HashMap<OrePrefixes, BWMetaGeneratedItems> items = new HashMap<>();
    public static HashBiMap<Werkstoff, Fluid> fluids = HashBiMap.create();
    public static HashBiMap<Werkstoff, Fluid> molten = HashBiMap.create();
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
            ItemStack ret = GTOreDictUnificator.get(orePrefixes, werkstoff.getBridgeMaterial(), amount);
            if (ret != null) return ret;
            ret = OreDictHandler.getItemStack(werkstoff.getVarName(), orePrefixes, amount);
            if (ret != null) return ret;
        }

        if (orePrefixes == ore || orePrefixes == oreSmall) {
            try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
                info.material = werkstoff;
                info.isSmall = orePrefixes == oreSmall;

                return BWOreAdapter.INSTANCE.getStack(info, amount);
            }
        }

        if (orePrefixes == block) {
            return new ItemStack(WerkstoffLoader.BWBlocks, amount, werkstoff.getmID());
        }
        if (orePrefixes == OrePrefixes.blockCasing) {
            return new ItemStack(WerkstoffLoader.BWBlockCasings, amount, werkstoff.getmID());
        }
        if (orePrefixes == OrePrefixes.blockCasingAdvanced) {
            return new ItemStack(WerkstoffLoader.BWBlockCasingsAdvanced, amount, werkstoff.getmID());
        }
        if (orePrefixes == OrePrefixes.sheetmetal) {
            return new ItemStack(GregTechAPI.sBlockSheetmetalBW, amount, werkstoff.getmID());
        }

        if (WerkstoffLoader.items.get(orePrefixes) == null) {
            return null;
        }

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

    /// Forces this class to be loaded
    public static void load() {

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
            if (BetterLoadingScreen.isModLoaded()) {
                CLSCompat.initCls();
            }

            IWerkstoffRunnable[] werkstoffRunnables = { new ToolLoader(), new DustLoader(), new GemLoader(),
                new SimpleMetalLoader(), new CasingLoader(), new AspectLoader(), new RawOreLoader(),
                new CrushedLoader(), new CraftingMaterialLoader(), new CellLoader(), new MoltenCellLoader(),
                new MultipleMetalLoader(), new MetalLoader(), new BlockLoader() };

            long timepreone = 0;
            int pos = 0;
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
                if (BetterLoadingScreen.isModLoaded()) {
                    CLSCompat.updateDisplay(werkstoff, pos);
                }
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
                pos++;
            }
            DebugLog.log("Loading New Circuits" + " " + (System.nanoTime() - timepreone));
            BWMetaItems.init();

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
                    Fluid fluid = GTFluidFactory.builder(werkstoff.getDefaultName())
                        .withDefaultLocalName(werkstoff.getDefaultName())
                        .withStateAndTemperature(
                            werkstoff.getStats()
                                .getFluidState(),
                            300)
                        .withColorRGBA(werkstoff.getRGBA())
                        .withTextureName("autogenerated")
                        .buildAndRegister()
                        .addLocalizedName(werkstoff)
                        .asFluid();
                    WerkstoffLoader.fluids.put(werkstoff, fluid);
                } else {
                    WerkstoffLoader.fluids.put(werkstoff, FluidRegistry.getFluid(werkstoff.getDefaultName()));
                }
            }
            if (werkstoff.hasItemType(OrePrefixes.cellMolten)) {
                if (!FluidRegistry.isFluidRegistered("molten." + werkstoff.getDefaultName())) {
                    DebugLog.log("Adding new Molten: " + werkstoff.getDefaultName());
                    Fluid fluid = GTFluidFactory.builder("molten." + werkstoff.getDefaultName())
                        .withDefaultLocalName("Molten " + werkstoff.getDefaultName())
                        .withStateAndTemperature(
                            FluidState.MOLTEN,
                            werkstoff.getStats()
                                .getMeltingPoint() > 0 ? werkstoff.getStats()
                                    .getMeltingPoint() : 300)
                        .withColorRGBA(werkstoff.getRGBA())
                        .withTextureName("molten.autogenerated")
                        .buildAndRegister()
                        .addLocalizedName(werkstoff)
                        .asFluid();
                    WerkstoffLoader.molten.put(werkstoff, fluid);
                } else {
                    WerkstoffLoader.molten.put(werkstoff, FluidRegistry.getFluid(werkstoff.getDefaultName()));
                }
            }
            for (OrePrefixes p : OrePrefixes.VALUES) if (Materials.get(werkstoff.getDefaultName()) != null
                && Materials.get(werkstoff.getDefaultName()).mMetaItemSubID != -1
                && (werkstoff.getGenerationFeatures().toGenerate & p.getMaterialGenerationBits()) != 0
                && OreDictHandler.getItemStack(werkstoff.getDefaultName(), p, 1) != null) {
                    DebugLog.log(
                        "Found: " + p
                            + werkstoff.getVarName()
                            + " in GT material system, disable and reroute my Items to that, also add a Tooltip.");
                    werkstoff.getGenerationFeatures()
                        .removePrefix(p);
                }
            WerkstoffLoader.toGenerateGlobal = WerkstoffLoader.toGenerateGlobal
                | werkstoff.getGenerationFeatures().toGenerate;
        }
        DebugLog.log("GlobalGeneration: " + WerkstoffLoader.toGenerateGlobal);
        if ((WerkstoffLoader.toGenerateGlobal & 0b1) != 0) {
            WerkstoffLoader.items.put(dust, new BWMetaGeneratedItems(dust));
            WerkstoffLoader.items.put(dustTiny, new BWMetaGeneratedItems(dustTiny));
            WerkstoffLoader.items.put(dustSmall, new BWMetaGeneratedItems(dustSmall));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10) != 0) {
            WerkstoffLoader.items.put(ingot, new BWMetaGeneratedItems(ingot));
            WerkstoffLoader.items.put(ingotHot, new BWMetaGeneratedItems(ingotHot)); // 1750
            WerkstoffLoader.items.put(nugget, new BWMetaGeneratedItems(nugget));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100) != 0) {
            WerkstoffLoader.items.put(gem, new BWMetaGeneratedItems(gem));
            WerkstoffLoader.items.put(gemChipped, new BWMetaGeneratedItems(gemChipped));
            WerkstoffLoader.items.put(gemExquisite, new BWMetaGeneratedItems(gemExquisite));
            WerkstoffLoader.items.put(gemFlawed, new BWMetaGeneratedItems(gemFlawed));
            WerkstoffLoader.items.put(gemFlawless, new BWMetaGeneratedItems(gemFlawless));
            WerkstoffLoader.items.put(lens, new BWMetaGeneratedItems(lens));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000) != 0) {
            gameRegistryHandler();
            WerkstoffLoader.items.put(crushed, new BWMetaGeneratedItems(crushed));
            WerkstoffLoader.items.put(crushedPurified, new BWMetaGeneratedItems(crushedPurified));
            WerkstoffLoader.items.put(crushedCentrifuged, new BWMetaGeneratedItems(crushedCentrifuged));
            WerkstoffLoader.items.put(dustPure, new BWMetaGeneratedItems(dustPure));
            WerkstoffLoader.items.put(dustImpure, new BWMetaGeneratedItems(dustImpure));
            WerkstoffLoader.items.put(rawOre, new BWMetaGeneratedItems(rawOre));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000) != 0) {
            WerkstoffLoader.items.put(cell, new BWMetaGeneratedItems(cell));
            if (Forestry.isModLoaded()) {
                BWMetaGeneratedItems capsuleClass = new BWMetaGeneratedItems(capsule);
                API.hideItem(new ItemStack(capsuleClass, 1, WILDCARD));
                WerkstoffLoader.items.put(capsule, capsuleClass);
            }
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100000) != 0) {
            WerkstoffLoader.items.put(cellPlasma, new BWMetaGeneratedItems(cellPlasma));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000000) != 0) {
            WerkstoffLoader.items.put(OrePrefixes.cellMolten, new BWMetaGeneratedItems(OrePrefixes.cellMolten));
            if (Forestry.isModLoaded()) {
                BWMetaGeneratedItems capsuleMoltenClass = new BWMetaGeneratedItems(OrePrefixes.capsuleMolten);
                API.hideItem(new ItemStack(capsuleMoltenClass, 1, WILDCARD));
                WerkstoffLoader.items.put(OrePrefixes.capsuleMolten, capsuleMoltenClass);
            }
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000000) != 0) {
            WerkstoffLoader.items.put(plate, new BWMetaGeneratedItems(plate));
            WerkstoffLoader.items.put(foil, new BWMetaGeneratedItems(foil));
            WerkstoffLoader.items.put(stick, new BWMetaGeneratedItems(stick));
            WerkstoffLoader.items.put(stickLong, new BWMetaGeneratedItems(stickLong));
            WerkstoffLoader.items.put(toolHeadWrench, new BWMetaGeneratedItems(toolHeadWrench));
            WerkstoffLoader.items.put(toolHeadHammer, new BWMetaGeneratedItems(toolHeadHammer));
            WerkstoffLoader.items.put(toolHeadSaw, new BWMetaGeneratedItems(toolHeadSaw));
            WerkstoffLoader.items.put(turbineBlade, new BWMetaGeneratedItems(turbineBlade));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b100000000) != 0) {
            WerkstoffLoader.items.put(gearGt, new BWMetaGeneratedItems(gearGt));
            WerkstoffLoader.items.put(gearGtSmall, new BWMetaGeneratedItems(gearGtSmall));
            WerkstoffLoader.items.put(bolt, new BWMetaGeneratedItems(bolt));
            WerkstoffLoader.items.put(screw, new BWMetaGeneratedItems(screw));
            WerkstoffLoader.items.put(ring, new BWMetaGeneratedItems(ring));
            WerkstoffLoader.items.put(spring, new BWMetaGeneratedItems(spring));
            WerkstoffLoader.items.put(springSmall, new BWMetaGeneratedItems(springSmall));
            WerkstoffLoader.items.put(rotor, new BWMetaGeneratedItems(rotor));
            WerkstoffLoader.items.put(wireFine, new BWMetaGeneratedItems(wireFine));
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b1000000000) != 0) {
            WerkstoffLoader.items.put(plateDense, new BWMetaGeneratedItems(plateDense));;
        }
        if ((WerkstoffLoader.toGenerateGlobal & 0b10000000000) != 0) {
            WerkstoffLoader.items.put(plateDouble, new BWMetaGeneratedItems(plateDouble));
            WerkstoffLoader.items.put(plateTriple, new BWMetaGeneratedItems(plateTriple));
            WerkstoffLoader.items.put(plateQuadruple, new BWMetaGeneratedItems(plateQuadruple));
            WerkstoffLoader.items.put(plateQuintuple, new BWMetaGeneratedItems(plateQuintuple));
        }
        ENABLED_ORE_PREFIXES.addAll(WerkstoffLoader.items.keySet());
        ENABLED_ORE_PREFIXES.add(ore);
        ENABLED_ORE_PREFIXES.add(oreSmall);
        WerkstoffLoader.runGTItemDataRegistrator();
    }

    static void gameRegistryHandler() {
        GameRegistry.registerTileEntity(BWTileEntityMetaGeneratedWerkstoffBlock.class, "bw.werkstoffblockTE");
        GameRegistry.registerTileEntity(BWTileEntityMetaGeneratedBlocksCasing.class, "bw.werkstoffblockcasingTE");
        GameRegistry.registerTileEntity(
            BWTileEntityMetaGeneratedBlocksCasingAdvanced.class,
            "bw.werkstoffblockscasingadvancedTE");

        BWOreAdapter.INSTANCE.init();

        WerkstoffLoader.BWBlocks = new BWMetaGeneratedWerkstoffBlocks(
            Material.iron,
            BWTileEntityMetaGeneratedWerkstoffBlock.class,
            "bw.werkstoffblocks");
        WerkstoffLoader.BWBlockCasings = new BWMetaGeneratedBlocksCasing(
            Material.iron,
            BWTileEntityMetaGeneratedBlocksCasing.class,
            "bw.werkstoffblockscasing",
            OrePrefixes.blockCasing);
        WerkstoffLoader.BWBlockCasingsAdvanced = new BWMetaGeneratedBlocksCasing(
            Material.iron,
            BWTileEntityMetaGeneratedBlocksCasingAdvanced.class,
            "bw.werkstoffblockscasingadvanced",
            OrePrefixes.blockCasingAdvanced);

        GameRegistry.registerBlock(WerkstoffLoader.BWBlocks, BWItemMetaGeneratedBlock.class, "bw.werkstoffblocks.01");
        GameRegistry.registerBlock(
            WerkstoffLoader.BWBlockCasings,
            BWItemMetaGeneratedBlock.class,
            "bw.werkstoffblockscasing.01");
        GameRegistry.registerBlock(
            WerkstoffLoader.BWBlockCasingsAdvanced,
            BWItemMetaGeneratedBlock.class,
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
        addOreByProductsForBridgeMaterials();
    }

    private static void addOreByProductsForBridgeMaterials() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            Materials bridgeMaterial = werkstoff.getBridgeMaterial();
            List<Materials> mOreByProducts = bridgeMaterial.mOreByProducts;
            if (mOreByProducts.size() > 0) continue; // Not to add if there're already oreByProducts.

            int size = werkstoff.getNoOfByProducts();
            for (int i = 0; i < size; i++) {
                ISubTagContainer material = werkstoff.getOreByProductRaw(i); // At least not duplicate now.
                if (material instanceof Materials) mOreByProducts.add(((Materials) material));
                else if (material instanceof Werkstoff) mOreByProducts.add(((Werkstoff) material).getBridgeMaterial());
                else throw new ClassCastException();
            }
            if (size < 3) mOreByProducts.add(werkstoff.getBridgeMaterial());
            // So it should be the same to Materials' mOreByProducts.
        }
    }

    private static void runAdditionalOreDict() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.hasItemType(ore)) {
                werkstoff.getAdditionalOredict()
                    .forEach(e -> OreDictionary.registerOre(ore + e, werkstoff.get(ore)));
                werkstoff.getAdditionalOredict()
                    .forEach(e -> OreDictionary.registerOre(oreSmall + e, werkstoff.get(oreSmall)));
            }

            if (werkstoff.hasItemType(gem)) OreDictionary.registerOre(
                "craftingLens" + BWColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""),
                werkstoff.get(lens));

            if (werkstoff.hasItemType(gem) || werkstoff.hasItemType(ingot)) {
                GTOreDictUnificator.registerOre(block + werkstoff.getVarName(), werkstoff.get(block));
                werkstoff.getAdditionalOredict()
                    .forEach(e -> OreDictionary.registerOre(block + e, werkstoff.get(block)));
            }

            werkstoff.getAdditionalOredict()
                .forEach(
                    s -> ENABLED_ORE_PREFIXES.stream()
                        .filter(o -> Objects.nonNull(werkstoff.get(o)))
                        .forEach(od -> OreDictionary.registerOre(od + s, werkstoff.get(od))));
        }

        GTOreDictUnificator.registerOre("craftingIndustrialDiamond", WerkstoffLoader.CubicZirconia.get(gemExquisite));
        BWOreAdapter.INSTANCE.registerOredict();
    }
}
