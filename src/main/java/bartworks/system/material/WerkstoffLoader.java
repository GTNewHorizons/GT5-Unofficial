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
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.gemExquisite;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.lens;
import static gregtech.api.enums.OrePrefixes.ore;
import static gregtech.api.enums.OrePrefixes.oreSmall;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;

import com.google.common.collect.HashBiMap;

import bartworks.MainMod;
import bartworks.system.material.CircuitGeneration.CircuitPartsItem;
import bartworks.system.material.processingLoaders.AdditionalRecipes;
import bartworks.system.oredict.OreDictHandler;
import bartworks.util.BWColorUtil;
import bartworks.util.log.DebugLog;
import bwcrossmod.cls.CLSCompat;
import codechicken.nei.api.API;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.loaders.materials.LegacyNameDomain;
import gregtech.loaders.postload.LoaderWerkstoffRegistrations;

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

    // GT Enhancements

    // More NonGT Stuff

    // TODO: If there is a moment where we are happy with breaking everyone's platline, change Aqua Regia recipes to
    // satisfy Chem
    // TODO: Balance with formula (HCl)3(HNO3) and then add the correct formula to the material

    // Extracted from GalaxySpace

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
        // Item/ore/block/casing/sheetmetal/frame cutover: a werkstoff's item prefixes resolve to the MaterialLib
        // stack via WerkstoffReconstruction#materialLibOf (the reconstruction pairing for a reconstructed
        // werkstoff, falling back to the bridge material for a proxy; a third-party werkstoff resolves to null
        // and falls through to the legacy paths). `ore`/`oreSmall`/`block`/`blockCasing`/`blockCasingAdvanced`
        // resolve through MU too (Materials2OreShapes/Materials2BlockShapes; the multiblock structure matchers
        // referencing the casing blocks by identity resolve dynamically as well, see Casings#bwCasing).
        // `sheetmetal`/`frameGt` resolve through MU for the materials
        // Materials2PipeMaterials#werkstoffFrameAndSheetmetalMaterials declares; the sBlockSheetmetalBW/
        // sBlockFramesBW fallback below still serves third-party WerkstoffAdder materials outside that set.
        ItemStack mlStack = MU.stack(orePrefixes, WerkstoffReconstruction.materialLibOf(werkstoff), amount);
        if (mlStack != null) return mlStack;
        if (!werkstoff.getGenerationFeatures().enforceUnification) {
            ItemStack ret = GTOreDictUnificator
                .get(orePrefixes, WerkstoffReconstruction.materialLibOf(werkstoff), amount);
            if (ret != null) return ret;
            ret = OreDictHandler.getItemStack(werkstoff.getVarName(), orePrefixes, amount);
            if (ret != null) return ret;
        }

        if (orePrefixes == ore || orePrefixes == oreSmall) {
            try (OreInfo<com.ruling_0.materiallib.api.Material> info = OreInfo.getNewInfo()) {
                info.material = WerkstoffReconstruction.materialLibOf(werkstoff);
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

        return null;
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
        return null;
    }

    /// Forces this class to be loaded
    public static void load() {

    }

    public static void runInit() {
        MainMod.LOGGER.info("Making Meta Items for BW Materials");
        long timepre = System.nanoTime();
        addItemsForGeneration();
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

    public static long toGenerateGlobal;

    private static void addItemsForGeneration() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            // Reconstructed werkstoffe resolve their fluids from the registry: MaterialLib registered every
            // werkstoff fluid at its own preInit (each bridge mirror's LEGACY_FLUIDS capture), and
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
            com.ruling_0.materiallib.api.Material werkstoffNamedMaterial = LegacyNameDomain
                .lookup(werkstoff.getDefaultName());
            for (OrePrefixes p : OrePrefixes.VALUES)
                if (werkstoffNamedMaterial != null && MU.oldSubId(werkstoffNamedMaterial) != -1
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
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.METALS) != 0) {}
        if ((WerkstoffLoader.toGenerateGlobal & Werkstoff.GenerationFeatures.ORES) != 0) {
            gameRegistryHandler();
        }
        for (com.ruling_0.materiallib.api.Material material : com.ruling_0.materiallib.api.MaterialLibAPI
            .getMaterials()) {
            java.util.List<String> prefixes = material
                .getProperty(gregtech.api.material.GTMaterialProperties.WERKSTOFF_PREFIXES);
            if (prefixes == null) continue;
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                if (prefixes.contains(prefix.name())) ENABLED_ORE_PREFIXES.add(prefix);
            }
        }
        ENABLED_ORE_PREFIXES.add(ore);
        ENABLED_ORE_PREFIXES.add(oreSmall);
        LoaderWerkstoffRegistrations.run();
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

    /// Hides the legacy storage/casing block slot of every werkstoff whose prefix now resolves to a MaterialLib
    /// block (see [MU]), mirroring [gregtech.common.ores.GTOreAdapter#hideOres]'s NEI-hiding precedent. A slot
    /// that stays legacy-canonical (a third-party werkstoff, or a prefix that has not cut over) remains visible.
    /// Called late, from NEI's own config loading, well after every werkstoff's bridge material is assigned.
    public static void hideBlocks() {
        for (Werkstoff w : Werkstoff.werkstoffHashSet) {
            if (w == null) continue;

            hideBlockSlot(w, block, WerkstoffLoader.BWBlocks);
            hideBlockSlot(w, OrePrefixes.blockCasing, WerkstoffLoader.BWBlockCasings);
            hideBlockSlot(w, OrePrefixes.blockCasingAdvanced, WerkstoffLoader.BWBlockCasingsAdvanced);
        }
    }

    private static void hideBlockSlot(Werkstoff w, OrePrefixes prefix, Block legacyBlock) {
        if (!w.hasItemType(prefix)) return;
        if (MU.stack(prefix, WerkstoffReconstruction.materialLibOf(w), 1) == null) return;

        API.hideItem(new ItemStack(legacyBlock, 1, w.getmID()));
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

        GTOreDictUnificator
            .registerOre("craftingIndustrialDiamond", MU.stack(gemExquisite, Materials2Materials.CubicZirconia, 1));
        BWOreAdapter.INSTANCE.registerOredict();
    }

    /// Resolves a reconstructed werkstoff's MaterialLib-registered fluid, fail-loud (mirrors
    /// `LegacyMaterials#wireFluids`): the registered name is the Forge-lowercased default name with the given
    /// prefix, exactly what MaterialLib's fluid shapes registered.
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
