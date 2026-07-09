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

import static gregtech.api.enums.Mods.BetterLoadingScreen;
import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.bolt;
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
import static gregtech.api.enums.OrePrefixes.plateSuperdense;
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

import java.lang.reflect.Field;
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

import org.apache.logging.log4j.Level;

import com.google.common.collect.HashBiMap;

import bartworks.API.WerkstoffAdderRegistry;
import bartworks.MainMod;
import bartworks.system.material.CircuitGeneration.CircuitPartsItem;
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
import bartworks.util.log.DebugLog;
import bwcrossmod.cls.CLSCompat;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.material.MU;
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
        Werkstoff.GenerationFeatures.initPrefixLogic();
        WerkstoffReconstruction.applyGenerationBits();
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
    public static final Werkstoff Bismutite = WerkstoffReconstruction.byId(1);
    public static final Werkstoff Bismuthinit = WerkstoffReconstruction.byId(2);
    public static final Werkstoff Zirconium = WerkstoffReconstruction.byId(3);
    public static final Werkstoff CubicZirconia = WerkstoffReconstruction.byId(4);
    public static final Werkstoff FluorBuergerit = WerkstoffReconstruction.byId(5);
    public static final Werkstoff YttriumOxide = WerkstoffReconstruction.byId(6);
    public static final Werkstoff ChromoAluminoPovondrait = WerkstoffReconstruction.byId(7);
    public static final Werkstoff VanadioOxyDravit = WerkstoffReconstruction.byId(8);
    public static final Werkstoff Olenit = WerkstoffReconstruction.byId(9);
    public static final Werkstoff Arsenopyrite = WerkstoffReconstruction.byId(10);
    public static final Werkstoff Ferberite = WerkstoffReconstruction.byId(11);
    public static final Werkstoff Loellingit = WerkstoffReconstruction.byId(12);
    public static final Werkstoff Roquesit = WerkstoffReconstruction.byId(13);
    public static final Werkstoff Bornite = WerkstoffReconstruction.byId(14);
    public static final Werkstoff Wittichenit = WerkstoffReconstruction.byId(15);
    public static final Werkstoff Djurleit = WerkstoffReconstruction.byId(16);
    public static final Werkstoff Huebnerit = WerkstoffReconstruction.byId(17);
    public static final Werkstoff Thorianit = WerkstoffReconstruction.byId(18);
    public static final Werkstoff RedZircon = WerkstoffReconstruction.byId(19);

    // GT Enhancements
    public static final Werkstoff Salt = WerkstoffReconstruction.byId(20);
    public static final Werkstoff Spodumen = WerkstoffReconstruction.byId(21);
    public static final Werkstoff RockSalt = WerkstoffReconstruction.byId(22);

    // More NonGT Stuff
    public static final Werkstoff Fayalit = WerkstoffReconstruction.byId(23);
    public static final Werkstoff Forsterit = WerkstoffReconstruction.byId(24);
    public static final Werkstoff Hedenbergit = WerkstoffReconstruction.byId(25);
    public static final Werkstoff DescloiziteZNVO4 = WerkstoffReconstruction.byId(26);
    public static final Werkstoff DescloiziteCUVO4 = WerkstoffReconstruction.byId(27);
    public static final Werkstoff FuchsitAL = WerkstoffReconstruction.byId(28);

    public static final Werkstoff FuchsitCR = WerkstoffReconstruction.byId(29);

    public static final Werkstoff Thorium232 = WerkstoffReconstruction.byId(30);
    public static final Werkstoff BismuthTellurite = WerkstoffReconstruction.byId(31);
    public static final Werkstoff Tellurium = WerkstoffReconstruction.byId(32);
    public static final Werkstoff BismuthHydroBorat = WerkstoffReconstruction.byId(33);
    public static final Werkstoff ArInGaPhoBiBoTe = WerkstoffReconstruction.byId(34);

    public static final Werkstoff Prasiolite = WerkstoffReconstruction.byId(35);

    public static final Werkstoff MagnetoResonaticDust = WerkstoffReconstruction.byId(36);
    public static final Werkstoff Xenon = WerkstoffReconstruction.byId(37);
    public static final Werkstoff Oganesson = WerkstoffReconstruction.byId(38);
    public static final Werkstoff Californium = WerkstoffReconstruction.byId(39);
    public static final Werkstoff Calcium = WerkstoffReconstruction.byId(40);
    public static final Werkstoff Neon = WerkstoffReconstruction.byId(41);
    public static final Werkstoff Krypton = WerkstoffReconstruction.byId(42);
    public static final Werkstoff BArTiMaEuSNeK = WerkstoffReconstruction.byId(43);
    public static final Werkstoff PTConcentrate = WerkstoffReconstruction.byId(44);
    public static final Werkstoff PTSaltCrude = WerkstoffReconstruction.byId(45);
    public static final Werkstoff PTSaltRefined = WerkstoffReconstruction.byId(46);
    public static final Werkstoff PTMetallicPowder = WerkstoffReconstruction.byId(47);
    // TODO: If there is a moment where we are happy with breaking everyone's platline, change Aqua Regia recipes to
    // satisfy Chem
    // TODO: Balance with formula (HCl)3(HNO3) and then add the correct formula to the material
    public static final Werkstoff AquaRegia = WerkstoffReconstruction.byId(48);
    public static final Werkstoff PTResidue = WerkstoffReconstruction.byId(49);
    public static final Werkstoff AmmoniumChloride = WerkstoffReconstruction.byId(50);
    public static final Werkstoff PTRawPowder = WerkstoffReconstruction.byId(51);
    public static final Werkstoff PDAmmonia = WerkstoffReconstruction.byId(52);
    public static final Werkstoff PDMetallicPowder = WerkstoffReconstruction.byId(53);
    public static final Werkstoff PDRawPowder = WerkstoffReconstruction.byId(54);
    public static final Werkstoff PDSalt = WerkstoffReconstruction.byId(55);
    public static final Werkstoff Sodiumformate = WerkstoffReconstruction.byId(56);
    public static final Werkstoff Sodiumsulfate = WerkstoffReconstruction.byId(57);
    public static final Werkstoff FormicAcid = WerkstoffReconstruction.byId(58);
    public static final Werkstoff PotassiumDisulfate = WerkstoffReconstruction.byId(59);
    public static final Werkstoff LeachResidue = WerkstoffReconstruction.byId(60);
    public static final Werkstoff RHSulfate = WerkstoffReconstruction.byId(61);
    public static final Werkstoff RHSulfateSolution = WerkstoffReconstruction.byId(62);
    public static final Werkstoff CalciumChloride = WerkstoffReconstruction.byId(63);
    public static final Werkstoff Ruthenium = WerkstoffReconstruction.byId(64);
    public static final Werkstoff SodiumRuthenate = WerkstoffReconstruction.byId(65);
    public static final Werkstoff RutheniumTetroxide = WerkstoffReconstruction.byId(66);
    public static final Werkstoff HotRutheniumTetroxideSollution = WerkstoffReconstruction.byId(67);
    public static final Werkstoff RutheniumTetroxideSollution = WerkstoffReconstruction.byId(68);
    public static final Werkstoff IrOsLeachResidue = WerkstoffReconstruction.byId(69);
    public static final Werkstoff IrLeachResidue = WerkstoffReconstruction.byId(70);
    public static final Werkstoff PGSDResidue = WerkstoffReconstruction.byId(71);
    public static final Werkstoff AcidicOsmiumSolution = WerkstoffReconstruction.byId(72);
    public static final Werkstoff IridiumDioxide = WerkstoffReconstruction.byId(73);
    public static final Werkstoff OsmiumSolution = WerkstoffReconstruction.byId(74);
    public static final Werkstoff AcidicIridiumSolution = WerkstoffReconstruction.byId(75);
    public static final Werkstoff IridiumChloride = WerkstoffReconstruction.byId(76);
    public static final Werkstoff PGSDResidue2 = WerkstoffReconstruction.byId(77);
    public static final Werkstoff Rhodium = WerkstoffReconstruction.byId(78);
    public static final Werkstoff CrudeRhMetall = WerkstoffReconstruction.byId(79);
    public static final Werkstoff RHSalt = WerkstoffReconstruction.byId(80);
    public static final Werkstoff RHSaltSolution = WerkstoffReconstruction.byId(81);
    public static final Werkstoff SodiumNitrate = WerkstoffReconstruction.byId(82);
    public static final Werkstoff RHNitrate = WerkstoffReconstruction.byId(83);
    public static final Werkstoff ZincSulfate = WerkstoffReconstruction.byId(84);
    public static final Werkstoff RhFilterCake = WerkstoffReconstruction.byId(85);
    public static final Werkstoff RHFilterCakeSolution = WerkstoffReconstruction.byId(86);
    public static final Werkstoff ReRh = WerkstoffReconstruction.byId(87);
    public static final Werkstoff RhodiumPlatedPalladium = WerkstoffReconstruction.byId(88);
    public static final Werkstoff Tiberium = WerkstoffReconstruction.byId(89);
    public static final Werkstoff Ruridit = WerkstoffReconstruction.byId(90);
    public static final Werkstoff Fluorspar = WerkstoffReconstruction.byId(91);
    public static final Werkstoff HDCS = WerkstoffReconstruction.byId(92);
    public static final Werkstoff Atheneite = WerkstoffReconstruction.byId(93);
    public static final Werkstoff Temagamite = WerkstoffReconstruction.byId(94);
    public static final Werkstoff Terlinguaite = WerkstoffReconstruction.byId(95);
    public static final Werkstoff AdemicSteel = WerkstoffReconstruction.byId(96);
    public static final Werkstoff RawAdemicSteel = WerkstoffReconstruction.byId(97);
    public static final Werkstoff HexafluorosilicicAcid = WerkstoffReconstruction.byId(98);
    public static final Werkstoff Potassiumfluorosilicate = WerkstoffReconstruction.byId(99);
    public static final Werkstoff Alumina = WerkstoffReconstruction.byId(100);
    public static final Werkstoff PotassiumCarbonate = WerkstoffReconstruction.byId(101);
    public static final Werkstoff RawFluorophlogopite = WerkstoffReconstruction.byId(102);
    public static final Werkstoff HotFluorophlogopite = WerkstoffReconstruction.byId(103);
    public static final Werkstoff Fluorophlogopite = WerkstoffReconstruction.byId(104);

    // Extracted from GalaxySpace
    public static final Werkstoff LiquidHelium = WerkstoffReconstruction.byId(11500);

    public static final Werkstoff HafniumCarbide = WerkstoffReconstruction.byId(11501);

    public static final Werkstoff TantalumCarbideHafniumCarbideMixture = WerkstoffReconstruction.byId(11502);

    public static final Werkstoff TantalumHafniumCarbide = WerkstoffReconstruction.byId(11503);

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
        // Stage-10 item/ore cutover: a werkstoff's item prefixes resolve to the MaterialLib stack (via the
        // bridge material, which maps proxies and reconstructed werkstoffe alike; a third-party werkstoff's
        // bridge is unknown to MU and falls through to the legacy paths). `ore`/`oreSmall` now resolve through
        // MU too (Materials2OreShapes; see BWOreAdapter, which still owns the legacy fallback below for
        // third-party werkstoffe). `block`/`blockCasing`/`blockCasingAdvanced`/`sheetmetal`/`frameGt` stay
        // legacy-canonical for now: multiblock structure matchers reference the legacy casing blocks by
        // identity, so their cutover is a coordinated block+structure flip, not a stack swap.
        if (orePrefixes != block && orePrefixes != OrePrefixes.blockCasing
            && orePrefixes != OrePrefixes.blockCasingAdvanced
            && orePrefixes != OrePrefixes.sheetmetal
            && orePrefixes != OrePrefixes.frameGt) {
            ItemStack mlStack = MU.stack(orePrefixes, werkstoff.getBridgeMaterial(), amount);
            if (mlStack != null) return mlStack;
        }
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
        if (orePrefixes == OrePrefixes.frameGt) {
            return new ItemStack(GregTechAPI.sBlockFramesBW, amount, werkstoff.getmID());
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
            CircuitPartsItem.init();

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

    private static void addBridgeSubTags() {
        // add specific GT materials subtags to various werkstoff bridgematerials
        SubTag.METAL.addTo(RhodiumPlatedPalladium.getBridgeMaterial());
    }

    public static long toGenerateGlobal;

    private static void addItemsForGeneration() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            // Reconstructed werkstoffe resolve their fluids from the registry: MaterialLib registered every
            // werkstoff fluid at its own preInit (each bridge mirror's stage-06 LEGACY_FLUIDS capture), and
            // rebuilding through GTFluidFactory would re-configure the live fluid from reconstructed stats
            // (whose dump-masked melting point turns legacy "unset" into 1123 K). Only a third-party
            // WerkstoffAdder's werkstoff, unknown to MaterialLib, still builds its fluid here.
            if (werkstoff.hasItemType(cell)) {
                if (WerkstoffReconstruction.isReconstructed(werkstoff)) {
                    WerkstoffLoader.fluids.put(werkstoff, resolveMaterialLibFluid(werkstoff, ""));
                } else if (!FluidRegistry.isFluidRegistered(werkstoff.getDefaultName())) {
                    DebugLog.log("Adding new Fluid: " + werkstoff.getDefaultName());
                    Fluid fluid = GTFluidFactory.builder(werkstoff.getDefaultName())
                        .withDefaultLocalName(werkstoff.getDefaultName())
                        .withStateAndTemperature(
                            werkstoff.getStats()
                                .getFluidState(),
                            getFluidTemperature(werkstoff))
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
                if (WerkstoffReconstruction.isReconstructed(werkstoff)) {
                    WerkstoffLoader.molten.put(werkstoff, resolveMaterialLibFluid(werkstoff, "molten."));
                } else if (!FluidRegistry.isFluidRegistered("molten." + werkstoff.getDefaultName())) {
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
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.DUSTS) != 0) {
            WerkstoffLoader.items.put(dust, new BWMetaGeneratedItems(dust));
            WerkstoffLoader.items.put(dustTiny, new BWMetaGeneratedItems(dustTiny));
            WerkstoffLoader.items.put(dustSmall, new BWMetaGeneratedItems(dustSmall));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.METALS) != 0) {
            WerkstoffLoader.items.put(ingot, new BWMetaGeneratedItems(ingot));
            WerkstoffLoader.items.put(ingotHot, new BWMetaGeneratedItems(ingotHot)); // 1750
            WerkstoffLoader.items.put(nugget, new BWMetaGeneratedItems(nugget));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.GEMS) != 0) {
            WerkstoffLoader.items.put(gem, new BWMetaGeneratedItems(gem));
            WerkstoffLoader.items.put(gemChipped, new BWMetaGeneratedItems(gemChipped));
            WerkstoffLoader.items.put(gemExquisite, new BWMetaGeneratedItems(gemExquisite));
            WerkstoffLoader.items.put(gemFlawed, new BWMetaGeneratedItems(gemFlawed));
            WerkstoffLoader.items.put(gemFlawless, new BWMetaGeneratedItems(gemFlawless));
            WerkstoffLoader.items.put(lens, new BWMetaGeneratedItems(lens));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.ORES) != 0) {
            gameRegistryHandler();
            WerkstoffLoader.items.put(crushed, new BWMetaGeneratedItems(crushed));
            WerkstoffLoader.items.put(crushedPurified, new BWMetaGeneratedItems(crushedPurified));
            WerkstoffLoader.items.put(crushedCentrifuged, new BWMetaGeneratedItems(crushedCentrifuged));
            WerkstoffLoader.items.put(dustPure, new BWMetaGeneratedItems(dustPure));
            WerkstoffLoader.items.put(dustImpure, new BWMetaGeneratedItems(dustImpure));
            WerkstoffLoader.items.put(rawOre, new BWMetaGeneratedItems(rawOre));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.LIQUID_CELLS) != 0) {
            WerkstoffLoader.items.put(cell, new BWMetaGeneratedItems(cell));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.PLASMA_CELLS) != 0) {
            WerkstoffLoader.items.put(cellPlasma, new BWMetaGeneratedItems(cellPlasma));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.MOLTEN_CELLS) != 0) {
            WerkstoffLoader.items.put(OrePrefixes.cellMolten, new BWMetaGeneratedItems(OrePrefixes.cellMolten));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.SIMPLE_METALWORKING) != 0) {
            WerkstoffLoader.items.put(plate, new BWMetaGeneratedItems(plate));
            WerkstoffLoader.items.put(foil, new BWMetaGeneratedItems(foil));
            WerkstoffLoader.items.put(stick, new BWMetaGeneratedItems(stick));
            WerkstoffLoader.items.put(stickLong, new BWMetaGeneratedItems(stickLong));
            WerkstoffLoader.items.put(toolHeadWrench, new BWMetaGeneratedItems(toolHeadWrench));
            WerkstoffLoader.items.put(toolHeadHammer, new BWMetaGeneratedItems(toolHeadHammer));
            WerkstoffLoader.items.put(toolHeadSaw, new BWMetaGeneratedItems(toolHeadSaw));
            WerkstoffLoader.items.put(turbineBlade, new BWMetaGeneratedItems(turbineBlade));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.CRAFTING_METALWORKING) != 0) {
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
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.DOUBLE_DENSE_PLATES) != 0) {
            WerkstoffLoader.items.put(plateDouble, new BWMetaGeneratedItems(plateDouble));
            WerkstoffLoader.items.put(plateDense, new BWMetaGeneratedItems(plateDense));
            WerkstoffLoader.items.put(plateSuperdense, new BWMetaGeneratedItems(plateSuperdense));
        }
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.MULTI_PLATES) != 0) {
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

    /// Resolves a reconstructed werkstoff's MaterialLib-registered fluid, fail-loud (mirrors
    /// `LegacyMaterials#wireFluids`): the registered name is the Forge-lowercased default name with the given
    /// prefix, exactly what the stage-06 fluid shapes registered.
    private static Fluid resolveMaterialLibFluid(Werkstoff werkstoff, String prefix) {
        String name = prefix + werkstoff.getDefaultName()
            .toLowerCase(java.util.Locale.ENGLISH);
        Fluid fluid = FluidRegistry.getFluid(name);
        if (fluid == null) throw new IllegalStateException(
            "MaterialLib did not register fluid " + name + " for werkstoff " + werkstoff.getDefaultName());
        return fluid;
    }

    private static int getFluidTemperature(Werkstoff werkstoff) {
        Werkstoff.Stats stat = werkstoff.getStats();
        int bp = stat.getBoilingPoint();
        int mp = stat.getMeltingPointDirect();
        int rt = 300; // room temperature
        if (stat.isGas()) {
            return Math.max(bp, rt);
        } else {
            if (bp <= rt && bp > 0) {
                return bp;
            } else return Math.max(mp, rt);
        }
    }
}
