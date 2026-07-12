package gregtech.loaders.preload;

import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.FluidState.LIQUID;
import static gregtech.api.enums.FluidState.MOLTEN;
import static gregtech.api.enums.FluidState.PLASMA;
import static gregtech.api.enums.FluidState.SLURRY;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.Werkstoff;
import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.items.BlockLongDistancePipe;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.ItemBreederCell;
import gregtech.api.items.ItemCoolantCellIC;
import gregtech.api.items.ItemRadioactiveCellIC;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.blocks.BlockCasings11;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.blocks.BlockCasings14;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.blocks.BlockCasings3;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.blocks.BlockCasings5;
import gregtech.common.blocks.BlockCasings6;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.blocks.BlockCasings9;
import gregtech.common.blocks.BlockCasingsBEC;
import gregtech.common.blocks.BlockCasingsFoundry;
import gregtech.common.blocks.BlockCasingsNH;
import gregtech.common.blocks.BlockConcretes;
import gregtech.common.blocks.BlockCyclotronCoils;
import gregtech.common.blocks.BlockDecorativeFrame;
import gregtech.common.blocks.BlockFenceMetal;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.BlockGlass1;
import gregtech.common.blocks.BlockGranites;
import gregtech.common.blocks.BlockLaser;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.blocks.BlockMetal;
import gregtech.common.blocks.BlockOresLegacy;
import gregtech.common.blocks.BlockPad;
import gregtech.common.blocks.BlockReinforced;
import gregtech.common.blocks.BlockRenderer;
import gregtech.common.blocks.BlockSheetMetal;
import gregtech.common.blocks.BlockStones;
import gregtech.common.blocks.BlockTintedIndustrialGlass;
import gregtech.common.blocks.TileEntityOres;
import gregtech.common.fluid.GTFluid;
import gregtech.common.items.ItemAdvancedSensorCard;
import gregtech.common.items.ItemDepletedCell;
import gregtech.common.items.ItemFluidDisplay;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.items.ItemMagLevHarness;
import gregtech.common.items.ItemNeutronReflector;
import gregtech.common.items.ItemSensorCard;
import gregtech.common.items.ItemTierDrone;
import gregtech.common.items.ItemVolumetricFlask;
import gregtech.common.items.ItemWirelessHeadphones;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.items.MetaGeneratedItem02;
import gregtech.common.items.MetaGeneratedItem03;
import gregtech.common.items.MetaGeneratedItem98;
import gregtech.common.items.MetaGeneratedItem99;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.items.armor.MechArmorLoader;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.tileentities.render.RenderingTileEntityBlackhole;
import gregtech.common.tileentities.render.RenderingTileEntityLaser;
import gregtech.common.tileentities.render.RenderingTileEntityNanoForge;
import gregtech.common.tileentities.render.RenderingTileEntityWormhole;

public class LoaderGTBlockFluid implements Runnable {

    @Override
    public void run() {
        Materials.Water.mFluid = FluidRegistry.getFluid("water");
        Materials.Ice.mFluid = FluidRegistry.getFluid("water");
        Materials.Lava.mFluid = FluidRegistry.getFluid("lava");

        GTLog.out.println("GTMod: Register Books.");

        GTUtility.getWrittenBook(
            "Manual_Printer",
            "gt.book.printer_manual.name",
            "Gregorius Techneticies",
            "gt.book.printer_manual.page00",
            "gt.book.printer_manual.page01",
            "gt.book.printer_manual.page02",
            "gt.book.printer_manual.page03",
            "gt.book.printer_manual.page04",
            "gt.book.printer_manual.page05",
            "gt.book.printer_manual.page06",
            "gt.book.printer_manual.page07");

        GTUtility.getWrittenBook(
            "Manual_Punch_Cards",
            "gt.book.punch_card_manual_v0.name",
            "Gregorius Techneticies",
            "gt.book.punch_card_manual_v0.page00",
            "gt.book.punch_card_manual_v0.page01",
            "gt.book.punch_card_manual_v0.page02",
            "gt.book.punch_card_manual_v0.page03",
            "gt.book.punch_card_manual_v0.page04",
            "gt.book.punch_card_manual_v0.page05",
            "gt.book.punch_card_manual_v0.page06",
            "gt.book.punch_card_manual_v0.page07",
            "gt.book.punch_card_manual_v0.page08",
            "gt.book.punch_card_manual_v0.page09",
            "gt.book.punch_card_manual_v0.page10",
            "gt.book.punch_card_manual_v0.page11",
            "gt.book.punch_card_manual_v0.page12");

        GTUtility.getWrittenBook(
            "Manual_Microwave",
            "gt.book.microwave_oven_manual.name",
            "Kitchen Industries",
            "gt.book.microwave_oven_manual.page00",
            "gt.book.microwave_oven_manual.page01",
            "gt.book.microwave_oven_manual.page02",
            "gt.book.microwave_oven_manual.page03",
            "gt.book.microwave_oven_manual.page04",
            "gt.book.microwave_oven_manual.page05",
            "gt.book.microwave_oven_manual.page06",
            "gt.book.microwave_oven_manual.page07");

        GTLog.out.println("GTMod: Register Items.");

        new ItemIntegratedCircuit();
        new MetaGeneratedItem01();
        new MetaGeneratedItem02();
        new MetaGeneratedItem03();
        // GT_MetaGenerated_Item_98 is initialized in GTProxy.onPostInitialization()
        // because we need to wait for fluids to be registered.
        // Pre-initialization needs to happen before then, though, because the cell icons get deleted at some point
        // between load and post-load.
        MetaGeneratedItem98.preInit();
        new MetaGeneratedItem99();
        new MetaGeneratedTool01();
        new ItemFluidDisplay();
        new ItemWirelessHeadphones();
        new ItemMagLevHarness();
        MechArmorLoader.run();
        new CircuitComponentFakeItem();

        // Tiered recipe materials actually appear to be set in MTEBasicMachineWithRecipe, making these
        // unused
        ItemList.Rotor_LV.set(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.rotor, (int) (1)));
        ItemList.Rotor_MV.set(MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.rotor, (int) (1)));
        ItemList.Rotor_HV.set(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.rotor, (int) (1)));
        ItemList.Rotor_EV
            .set(MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.rotor, (int) (1)));
        ItemList.Rotor_IV
            .set(MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.rotor, (int) (1)));

        ItemList.VOLUMETRIC_FLASK.set(new ItemVolumetricFlask("Volumetric_Flask", "Volumetric flask", 1_000));

        if (Mods.IC2NuclearControl.isModLoaded()) {
            ItemList.NC_SensorCard.set(new ItemSensorCard("sensorcard", "GregTech Sensor Card"));
            ItemList.NC_AdvancedSensorCard.set(new ItemAdvancedSensorCard());
        } else {
            ItemList.NC_SensorCard
                .set(new GTGenericItem("sensorcard", "GregTech Sensor Card", "Nuclear Control not installed"));
            ItemList.NC_AdvancedSensorCard.set(
                new GTGenericItem(
                    "advancedsensorcard",
                    "GregTech Advanced Sensor Card",
                    "Nuclear Control not installed"));
        }

        ItemList.Neutron_Reflector.set(new ItemNeutronReflector("neutronreflector", "Iridium Neutron Reflector", 0));
        ItemList.Reactor_Coolant_He_1
            .set(new ItemCoolantCellIC("60k_Helium_Coolantcell", "60k He Coolant Cell", 60_000));
        ItemList.Reactor_Coolant_He_3
            .set(new ItemCoolantCellIC("180k_Helium_Coolantcell", "180k He Coolant Cell", 180_000));
        ItemList.Reactor_Coolant_He_6
            .set(new ItemCoolantCellIC("360k_Helium_Coolantcell", "360k He Coolant Cell", 360_000));
        ItemList.Reactor_Coolant_NaK_1
            .set(new ItemCoolantCellIC("60k_NaK_Coolantcell", "60k NaK Coolant Cell", 60_000));
        ItemList.Reactor_Coolant_NaK_3
            .set(new ItemCoolantCellIC("180k_NaK_Coolantcell", "180k NaK Coolant Cell", 180_000));
        ItemList.Reactor_Coolant_NaK_6
            .set(new ItemCoolantCellIC("360k_NaK_Coolantcell", "360k NaK Coolant Cell", 360_000));

        ItemList.Reactor_Coolant_Sp_1
            .set(new ItemCoolantCellIC("180k_Space_Coolantcell", "180k Sp Coolant Cell", 180_000));

        ItemList.Reactor_Coolant_Sp_2
            .set(new ItemCoolantCellIC("360k_Space_Coolantcell", "360k Sp Coolant Cell", 360_000));

        ItemList.Reactor_Coolant_Sp_3
            .set(new ItemCoolantCellIC("540k_Space_Coolantcell", "540k Sp Coolant Cell", 540_000));

        ItemList.Reactor_Coolant_Sp_6
            .set(new ItemCoolantCellIC("1080k_Space_Coolantcell", "1080k Sp Coolant Cell", 1_080_000));

        ItemList.neutroniumHeatCapacitor
            .set(new ItemCoolantCellIC("neutroniumHeatCapacitor", "1G Neutronium Heat Capacitor", 1_000_000_000));

        // Lithium
        ItemList.DepletedRodLithium.set(new ItemDepletedCell("depletedRodLithium", "Fuel Rod (Tritium)", 1));
        ItemList.RodLithium.set(
            new ItemBreederCell(
                "rodLithium",
                "Fuel Rod (Lithium)",
                "Source of tritium",
                3_000,
                1,
                10_000,
                () -> ItemList.DepletedRodLithium.get(1)));

        // Glowstone
        ItemList.DepletedRodGlowstone.set(new ItemDepletedCell("depletedRodGlowstone", "Fuel Rod (Sunnarium)", 1));
        ItemList.RodGlowstone.set(
            new ItemBreederCell(
                "rodGlowstone",
                "Fuel Rod (Glowstone)",
                "Source of sunnarium",
                3_000,
                1,
                10_000,
                () -> ItemList.DepletedRodGlowstone.get(1)));

        // Thorium
        ItemList.DepletedRodThorium.set(new ItemDepletedCell("depletedRodThorium", "Fuel Rod (Depleted Thorium)", 1));
        ItemList.DepletedRodThorium2
            .set(new ItemDepletedCell("depletedRodThorium2", "Dual Fuel Rod (Depleted Thorium)", 1));
        ItemList.DepletedRodThorium4
            .set(new ItemDepletedCell("depletedRodThorium4", "Quad Fuel Rod (Depleted Thorium)", 1));

        ItemList.RodThorium.set(
            new ItemRadioactiveCellIC(
                "rodThorium",
                "Fuel Rod (Thorium)",
                1,
                50_000,
                0.4F,
                0,
                1F,
                ItemList.DepletedRodThorium.get(1),
                false,
                1F));
        ItemList.RodThorium2.set(
            new ItemRadioactiveCellIC(
                "rodThorium2",
                "Dual Fuel Rod (Thorium)",
                2,
                50_000,
                0.4F,
                0,
                1F,
                ItemList.DepletedRodThorium2.get(1),
                false,
                1F));
        ItemList.RodThorium4.set(
            new ItemRadioactiveCellIC(
                "rodThorium4",
                "Quad Fuel Rod (Thorium)",
                4,
                50_000,
                0.4F,
                0,
                1F,
                ItemList.DepletedRodThorium4.get(1),
                false,
                1F));

        // Uranium
        ItemList.DepletedRodUranium.set(new ItemDepletedCell("depletedRodUranium", "Fuel Rod (Depleted Uranium)", 1));
        ItemList.DepletedRodUranium2
            .set(new ItemDepletedCell("depletedRodUranium2", "Dual Fuel Rod (Depleted Uranium)", 1));
        ItemList.DepletedRodUranium4
            .set(new ItemDepletedCell("depletedRodUranium4", "Quad Fuel Rod (Depleted Uranium)", 1));

        ItemList.RodUranium.set(
            new ItemRadioactiveCellIC(
                "rodUranium",
                "Fuel Rod (Uranium)",
                1,
                20_000,
                2F,
                1,
                4F,
                ItemList.DepletedRodUranium.get(1),
                false,
                1F));
        ItemList.RodUranium2.set(
            new ItemRadioactiveCellIC(
                "rodUranium2",
                "Dual Fuel Rod (Uranium)",
                2,
                20_000,
                2F,
                1,
                4F,
                ItemList.DepletedRodUranium2.get(1),
                false,
                1F));
        ItemList.RodUranium4.set(
            new ItemRadioactiveCellIC(
                "rodUranium4",
                "Quad Fuel Rod (Uranium)",
                4,
                20_000,
                2F,
                1,
                4F,
                ItemList.DepletedRodUranium4.get(1),
                false,
                1F));

        // MOX
        ItemList.DepletedRodMOX.set(new ItemDepletedCell("depletedRodMOX", "Fuel Rod (Depleted MOX)", 1));
        ItemList.DepletedRodMOX2.set(new ItemDepletedCell("depletedRodMOX2", "Dual Fuel Rod (Depleted MOX)", 1));
        ItemList.DepletedRodMOX4.set(new ItemDepletedCell("depletedRodMOX4", "Quad Fuel Rod (Depleted MOX)", 1));

        ItemList.RodMOX.set(
            new ItemRadioactiveCellIC(
                "rodMOX",
                "Fuel Rod (Mox)",
                1,
                10_000,
                2F,
                1,
                4F,
                ItemList.DepletedRodMOX.get(1),
                true,
                1.5F));
        ItemList.RodMOX2.set(
            new ItemRadioactiveCellIC(
                "rodMOX2",
                "Dual Fuel Rod (Mox)",
                2,
                10_000,
                2F,
                1,
                4F,
                ItemList.DepletedRodMOX2.get(1),
                true,
                1.5F));
        ItemList.RodMOX4.set(
            new ItemRadioactiveCellIC(
                "rodMOX4",
                "Quad Fuel Rod (Mox)",
                4,
                10_000,
                2F,
                1,
                4F,
                ItemList.DepletedRodMOX4.get(1),
                true,
                1.5F));

        // High Density Uranium
        ItemList.DepletedRodHighDensityUranium.set(
            new ItemDepletedCell("depletedRodHighDensityUranium", "Fuel Rod (Depleted High Density Uranium)", 100));
        ItemList.DepletedRodHighDensityUranium2.set(
            new ItemDepletedCell(
                "depletedRodHighDensityUranium2",
                "Dual Fuel Rod (Depleted High Density Uranium)",
                200));
        ItemList.DepletedRodHighDensityUranium4.set(
            new ItemDepletedCell(
                "depletedRodHighDensityUranium4",
                "Quad Fuel Rod (Depleted High Density Uranium)",
                400));

        ItemList.RodHighDensityUranium.set(
            new ItemRadioactiveCellIC(
                "rodHighDensityUranium",
                "Fuel Rod (High Density Uranium)",
                1,
                70_000,
                4,
                2,
                4,
                ItemList.DepletedRodHighDensityUranium.get(1),
                false,
                1F));
        ItemList.RodHighDensityUranium2.set(
            new ItemRadioactiveCellIC(
                "rodHighDensityUranium2",
                "Dual Fuel Rod (High Density Uranium)",
                2,
                70_000,
                4,
                2,
                4,
                ItemList.DepletedRodHighDensityUranium2.get(1),
                false,
                1F));
        ItemList.RodHighDensityUranium4.set(
            new ItemRadioactiveCellIC(
                "rodHighDensityUranium4",
                "Quad Fuel Rod (High Density Uranium)",
                4,
                70_000,
                4,
                2,
                4,
                ItemList.DepletedRodHighDensityUranium4.get(1),
                false,
                1F));

        // High Density Plutonium
        ItemList.DepletedRodHighDensityPlutonium.set(
            new ItemDepletedCell("depletedRodHighDensityPlutonium", "Fuel Rod (Depleted High Density Plutonium)", 120));
        ItemList.DepletedRodHighDensityPlutonium2.set(
            new ItemDepletedCell(
                "depletedRodHighDensityPlutonium2",
                "Dual Fuel Rod (Depleted High Density Plutonium)",
                240));
        ItemList.DepletedRodHighDensityPlutonium4.set(
            new ItemDepletedCell(
                "depletedRodHighDensityPlutonium4",
                "Quad Fuel Rod (Depleted High Density Plutonium)",
                480));

        ItemList.RodHighDensityPlutonium.set(
            new ItemRadioactiveCellIC(
                "rodHighDensityPlutonium",
                "Fuel Rod (High Density Plutonium)",
                1,
                70_000,
                2,
                3,
                4,
                ItemList.DepletedRodHighDensityPlutonium.get(1),
                true,
                6F));
        ItemList.RodHighDensityPlutonium2.set(
            new ItemRadioactiveCellIC(
                "rodHighDensityPlutonium2",
                "Dual Fuel Rod (High Density Plutonium)",
                2,
                70_000,
                2,
                3,
                4,
                ItemList.DepletedRodHighDensityPlutonium2.get(1),
                true,
                6F));
        ItemList.RodHighDensityPlutonium4.set(
            new ItemRadioactiveCellIC(
                "rodHighDensityPlutonium4",
                "Quad Fuel Rod (High Density Plutonium)",
                4,
                70_000,
                2,
                3,
                4,
                ItemList.DepletedRodHighDensityPlutonium4.get(1),
                true,
                6F));

        // Excited Uranium
        ItemList.DepletedRodExcitedUranium
            .set(new ItemDepletedCell("depletedRodExcitedUranium", "Fuel Rod (Depleted Excited Uranium)", 800));
        ItemList.DepletedRodExcitedUranium2
            .set(new ItemDepletedCell("depletedRodExcitedUranium2", "Dual Fuel Rod (Depleted Excited Uranium)", 1_600));
        ItemList.DepletedRodExcitedUranium4
            .set(new ItemDepletedCell("depletedRodExcitedUranium4", "Quad Fuel Rod (Depleted Excited Uranium)", 3_200));

        ItemList.RodExcitedUranium.set(
            new ItemRadioactiveCellIC(
                "rodExcitedUranium",
                "Fuel Rod (Excited Uranium)",
                1,
                6_000,
                48,
                2,
                64,
                ItemList.DepletedRodExcitedUranium.get(1),
                false,
                1F));
        ItemList.RodExcitedUranium2.set(
            new ItemRadioactiveCellIC(
                "rodExcitedUranium2",
                "Dual Fuel Rod (Excited Uranium)",
                2,
                6_000,
                48,
                2,
                64,
                ItemList.DepletedRodExcitedUranium2.get(1),
                false,
                1F));
        ItemList.RodExcitedUranium4.set(
            new ItemRadioactiveCellIC(
                "rodExcitedUranium4",
                "Quad Fuel Rod (Excited Uranium)",
                4,
                6_000,
                48,
                2,
                64,
                ItemList.DepletedRodExcitedUranium4.get(1),
                false,
                1F));

        // Excited Plutonium
        ItemList.DepletedRodExcitedPlutonium
            .set(new ItemDepletedCell("depletedRodExcitedPlutonium", "Fuel Rod (Depleted Excited Plutonium)", 1_000));
        ItemList.DepletedRodExcitedPlutonium2.set(
            new ItemDepletedCell("depletedRodExcitedPlutonium2", "Dual Fuel Rod (Depleted Excited Plutonium)", 2_000));
        ItemList.DepletedRodExcitedPlutonium4.set(
            new ItemDepletedCell("depletedRodExcitedPlutonium4", "Quad Fuel Rod (Depleted Excited Plutonium)", 4_000));

        ItemList.RodExcitedPlutonium.set(
            new ItemRadioactiveCellIC(
                "rodExcitedPlutonium",
                "Fuel Rod (Excited Plutonium)",
                1,
                10_000,
                64,
                3,
                64,
                ItemList.DepletedRodExcitedPlutonium.get(1),
                true,
                2F));
        ItemList.RodExcitedPlutonium2.set(
            new ItemRadioactiveCellIC(
                "rodExcitedPlutonium2",
                "Dual Fuel Rod (Excited Plutonium)",
                2,
                10_000,
                64,
                3,
                64,
                ItemList.DepletedRodExcitedPlutonium2.get(1),
                true,
                2F));
        ItemList.RodExcitedPlutonium4.set(
            new ItemRadioactiveCellIC(
                "rodExcitedPlutonium4",
                "Quad Fuel Rod (Excited Plutonium)",
                4,
                10_000,
                64,
                3,
                64,
                ItemList.DepletedRodExcitedPlutonium4.get(1),
                true,
                2F));

        // Naquadah
        ItemList.DepletedRodNaquadah
            .set(new ItemDepletedCell("depletedRodNaquadah", "Fuel Rod (Depleted Naquadah)", 1));
        ItemList.DepletedRodNaquadah2
            .set(new ItemDepletedCell("depletedRodNaquadah2", "Dual Fuel Rod (Depleted Naquadah)", 1));
        ItemList.DepletedRodNaquadah4
            .set(new ItemDepletedCell("depletedRodNaquadah4", "Quad Fuel Rod (Depleted Naquadah)", 1));
        ItemList.DepletedRodNaquadah32.set(new ItemDepletedCell("depletedRodNaquadah32", "The Core (Depleted)", 32));

        ItemList.RodNaquadah.set(
            new ItemRadioactiveCellIC(
                "rodNaquadah",
                "Fuel Rod (Naquadah)",
                1,
                100_000,
                4F,
                1,
                4F,
                ItemList.DepletedRodNaquadah.get(1),
                false,
                1F));
        ItemList.RodNaquadah2.set(
            new ItemRadioactiveCellIC(
                "rodNaquadah2",
                "Dual Fuel Rod (Naquadah)",
                2,
                100_000,
                4F,
                1,
                4F,
                ItemList.DepletedRodNaquadah2.get(1),
                false,
                1F));
        ItemList.RodNaquadah4.set(
            new ItemRadioactiveCellIC(
                "rodNaquadah4",
                "Quad Fuel Rod (Naquadah)",
                4,
                100_000,
                4F,
                1,
                1F,
                ItemList.DepletedRodNaquadah4.get(1),
                false,
                1F));
        ItemList.RodNaquadah32.set(
            new ItemRadioactiveCellIC(
                "rodNaquadah32",
                "The Core",
                32,
                100_000,
                8F,
                32,
                4F,
                ItemList.DepletedRodNaquadah32.get(1),
                false,
                1F));

        // Naquadria
        ItemList.DepletedRodNaquadria
            .set(new ItemDepletedCell("depletedRodNaquadria", "Fuel Rod (Depleted Naquadria)", 1));
        ItemList.DepletedRodNaquadria2
            .set(new ItemDepletedCell("depletedRodNaquadria2", "Dual Fuel Rod (Depleted Naquadria)", 1));
        ItemList.DepletedRodNaquadria4
            .set(new ItemDepletedCell("depletedRodNaquadria4", "Quad Fuel Rod (Depleted Naquadria)", 1));

        ItemList.RodNaquadria.set(
            new ItemRadioactiveCellIC(
                "rodNaquadria",
                "Fuel Rod (Naquadria)",
                1,
                100_000,
                4F,
                1,
                4F,
                ItemList.DepletedRodNaquadria.get(1),
                true,
                1.5F));
        ItemList.RodNaquadria2.set(
            new ItemRadioactiveCellIC(
                "rodNaquadria2",
                "Dual Fuel Rod (Naquadria)",
                2,
                100_000,
                4F,
                1,
                4F,
                ItemList.DepletedRodNaquadria2.get(1),
                true,
                1.5F));
        ItemList.RodNaquadria4.set(
            new ItemRadioactiveCellIC(
                "rodNaquadria4",
                "Quad Fuel Rod (Naquadria)",
                4,
                100_000,
                4F,
                1,
                4F,
                ItemList.DepletedRodNaquadria4.get(1),
                true,
                1.5F));

        // Tiberium
        ItemList.DepletedRodTiberium
            .set(new ItemDepletedCell("depletedRodTiberium", "Fuel Rod (Depleted Tiberium)", 1));
        ItemList.DepletedRodTiberium2
            .set(new ItemDepletedCell("depletedRodTiberium2", "Dual Fuel Rod (Depleted Tiberium)", 1));
        ItemList.DepletedRodTiberium4
            .set(new ItemDepletedCell("depletedRodTiberium4", "Quad Fuel Rod (Depleted Tiberium)", 1));

        ItemList.RodTiberium.set(
            new ItemRadioactiveCellIC(
                "rodTiberium",
                "Fuel Rod (Tiberium)",
                1,
                50_000,
                2F,
                1,
                2F,
                ItemList.DepletedRodTiberium.get(1),
                false,
                1F));
        ItemList.RodTiberium2.set(
            new ItemRadioactiveCellIC(
                "rodTiberium2",
                "Dual Fuel Rod (Tiberium)",
                2,
                50_000,
                2F,
                1,
                2F,
                ItemList.DepletedRodTiberium2.get(1),
                false,
                1F));
        ItemList.RodTiberium4.set(
            new ItemRadioactiveCellIC(
                "rodTiberium4",
                "Quad Fuel Rod (Tiberium)",
                4,
                50_000,
                2F,
                1,
                2F,
                ItemList.DepletedRodTiberium4.get(1),
                false,
                1F));

        GTLog.out.println("GTMod: Adding Blocks.");
        GregTechAPI.sBlockMachines = new BlockMachines();
        GregTechAPI.sBlockCasings1 = new BlockCasings1();
        GregTechAPI.sBlockCasings2 = new BlockCasings2();
        GregTechAPI.sBlockCasings3 = new BlockCasings3();
        GregTechAPI.sBlockCasings4 = new BlockCasings4();
        GregTechAPI.sBlockCasings5 = new BlockCasings5();
        GregTechAPI.sBlockCasings6 = new BlockCasings6();
        GregTechAPI.sBlockCasings8 = new BlockCasings8();
        GregTechAPI.sBlockCasings9 = new BlockCasings9();
        GregTechAPI.sBlockCasings10 = new BlockCasings10();
        GregTechAPI.sBlockCasings11 = new BlockCasings11();
        GregTechAPI.sBlockCasings12 = new BlockCasings12();
        GregTechAPI.sBlockCasings13 = new BlockCasings13();
        GregTechAPI.sBlockCasings14 = new BlockCasings14();
        GregTechAPI.sBlockCasingsNH = new BlockCasingsNH();
        GregTechAPI.sBlockCasingsFoundry = new BlockCasingsFoundry();
        GregTechAPI.sBlockCasingsBEC = new BlockCasingsBEC();
        GregTechAPI.sBlockGranites = new BlockGranites();
        GregTechAPI.sBlockLongDistancePipes = new BlockLongDistancePipe();
        GregTechAPI.sBlockConcretes = new BlockConcretes();
        GregTechAPI.sBlockStones = new BlockStones();
        GregTechAPI.sBlockOres1 = new BlockOresLegacy();
        GregTechAPI.sBlockPad = new BlockPad();
        GregTechAPI.sBlockFenceMetal = new BlockFenceMetal();
        GregTechAPI.sBlockFrames = new BlockFrameBox();
        GregTechAPI.sBlockGlass1 = new BlockGlass1();
        GregTechAPI.sBlockTintedGlass = new BlockTintedIndustrialGlass();
        GregTechAPI.sLaserRender = new BlockLaser();
        GTLog.out.println("GTMod: Adding Renderer Blocks.");
        GregTechAPI.sWormholeRender = new BlockRenderer<>("wormholerenderer", RenderingTileEntityWormhole::new);
        GregTechAPI.sBlackholeRender = new BlockRenderer<>("blackholerenderer", RenderingTileEntityBlackhole::new);
        GregTechAPI.nanoForgeRender = new BlockRenderer<>("nanoforgerenderer", RenderingTileEntityNanoForge::new);

        GTOreAdapter.INSTANCE.init();

        // meta ID order, DO NOT CHANGE ORDER

        GregTechAPI.sBlockMetal1 = new BlockMetal(
            "gt.blockmetal1",
            new Materials[] { Materials.Adamantium, Materials.Aluminium, Materials.Americium, Materials.AnnealedCopper,
                Materials.Antimony, Materials.Arsenic, Materials.AstralSilver, Materials.BatteryAlloy,
                Materials.Beryllium, Materials.Bismuth, Materials.BismuthBronze, Materials.BlackBronze,
                Materials.BlackSteel, Materials.BlueAlloy, Materials.BlueSteel, Materials.Brass },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS1);

        GregTechAPI.sBlockMetal2 = new BlockMetal(
            "gt.blockmetal2",
            new Materials[] { Materials.Bronze, Materials.Caesium, Materials.Cerium, Materials.Chrome,
                Materials.ChromiumDioxide, Materials.Cobalt, Materials.CobaltBrass, Materials.Copper,
                Materials.Cupronickel, Materials.DamascusSteel, Materials.DarkIron, Materials.DeepIron, Materials.Desh,
                Materials.Duranium, Materials.Dysprosium, Materials.Electrum },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS2);

        GregTechAPI.sBlockMetal3 = new BlockMetal(
            "gt.blockmetal3",
            new Materials[] { Materials.ElectrumFlux, Materials.Enderium, Materials.Erbium, Materials.Europium,
                Materials.FierySteel, Materials.Gadolinium, Materials.Gallium, Materials.Holmium, Materials.HSLA,
                Materials.Indium, Materials.InfusedGold, Materials.Invar, Materials.Iridium, Materials.IronMagnetic,
                Materials.IronWood, Materials.Kanthal },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS3);

        GregTechAPI.sBlockMetal4 = new BlockMetal(
            "gt.blockmetal4",
            new Materials[] { Materials.Knightmetal, Materials.Lanthanum, Materials.Lead, Materials.Lutetium,
                Materials.Magnalium, Materials.Magnesium, Materials.Manganese, Materials.MeteoricIron,
                Materials.MeteoricSteel, Materials.Trinium, Materials.Mithril, Materials.Molybdenum, Materials.Naquadah,
                Materials.NaquadahAlloy, Materials.NaquadahEnriched, Materials.Naquadria },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS4);

        GregTechAPI.sBlockMetal5 = new BlockMetal(
            "gt.blockmetal5",
            new Materials[] { Materials.Neodymium, Materials.NeodymiumMagnetic, Materials.Neutronium,
                Materials.Nichrome, Materials.Nickel, Materials.Niobium, Materials.NiobiumNitride,
                Materials.NiobiumTitanium, Materials.Osmiridium, Materials.Osmium, Materials.Palladium,
                Materials.PigIron, Materials.Platinum, Materials.Plutonium, Materials.Plutonium241,
                Materials.Praseodymium },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS5);

        GregTechAPI.sBlockMetal6 = new BlockMetal(
            "gt.blockmetal6",
            new Materials[] { Materials.Promethium, Materials.RedAlloy, Materials.RedSteel, Materials.RoseGold,
                Materials.Rubidium, Materials.Samarium, Materials.Scandium, Materials.ShadowIron, Materials.ShadowSteel,
                Materials.Silicon, Materials.Silver, Materials.SolderingAlloy, Materials.StainlessSteel,
                Materials.Steel, Materials.SteelMagnetic, Materials.SterlingSilver },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS6);

        GregTechAPI.sBlockMetal7 = new BlockMetal(
            "gt.blockmetal7",
            new Materials[] { Materials.Sunnarium, Materials.Tantalum, Materials.Tellurium, Materials.Terbium,
                Materials.Thaumium, Materials.Thorium, Materials.Thulium, Materials.Tin, Materials.TinAlloy,
                Materials.Titanium, Materials.Tritanium, Materials.Tungsten, Materials.TungstenSteel, Materials.Ultimet,
                Materials.Uranium, Materials.Uranium235 },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS7);

        GregTechAPI.sBlockMetal8 = new BlockMetal(
            "gt.blockmetal8",
            new Materials[] { Materials.Vanadium, Materials.VanadiumGallium, Materials.CastIron, Materials.Ytterbium,
                Materials.Yttrium, Materials.YttriumBariumCuprate, Materials.Zinc, Materials.TungstenCarbide,
                Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE, Materials.HSSS, Materials.Steeleaf,
                Materials.Ichorium, Materials.Firestone, Materials.Shadow },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS8);

        GregTechAPI.sBlockGem1 = new BlockMetal(
            "gt.blockgem1",
            new Materials[] { Materials.InfusedAir, Materials.Amber, Materials.Amethyst, Materials.InfusedWater,
                Materials.BlueTopaz, Materials.CertusQuartz, Materials.Dilithium, Materials.EnderEye,
                Materials.EnderPearl, Materials.Spinel, Materials.Force, Materials.Forcicium, Materials.Forcillium,
                Materials.GreenSapphire, Materials.InfusedFire, Materials.Jasper },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS9);

        GregTechAPI.sBlockGem2 = new BlockMetal(
            "gt.blockgem2",
            new Materials[] { Materials.Lazurite, Materials.Lignite, Materials.Monazite, Materials.Niter,
                Materials.Olivine, Materials.Opal, Materials.InfusedOrder, Materials.InfusedEntropy,
                Materials.TricalciumPhosphate, Materials.Quartzite, Materials.GarnetRed, Materials.Ruby,
                Materials.Sapphire, Materials.Sodalite, Materials.Tanzanite, Materials.InfusedEarth },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS10);

        GregTechAPI.sBlockGem3 = new BlockMetal(
            "gt.blockgem3",
            new Materials[] { Materials.Topaz, Materials.Vinteum, Materials.GarnetYellow, Materials.NetherStar,
                Materials.Charcoal, Materials.Blaze },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS11);

        GregTechAPI.sBlockMetal9 = new BlockMetal(
            "gt.blockmetal9",
            new Materials[] { Materials.Cryolite, Materials.SiliconSG, Materials.NickelAluminide, Materials.SpaceTime,
                Materials.TranscendentMetal, Materials.Oriharukon, Materials.WhiteDwarfMatter,
                Materials.BlackDwarfMatter, Materials.Universium, Materials.Eternity, Materials.MagMatter,
                Materials.SixPhasedCopper, Materials.HellishMetal, Materials.MHDCSM, Materials.Hexanite },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS12);

        GregTechAPI.sBlockMetal10 = new BlockMetal(
            "gt.blockmetal10",
            new Materials[] { Materials.Shijima, Materials.Churitsu },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS13);

        GregTechAPI.sBlockReinforced = new BlockReinforced("gt.blockreinforced");

        GregTechAPI.sBlockSheetmetalGT = new BlockSheetMetal(
            "gt.sheetmetal",
            meta -> GTDataUtils.getIndexSafe(GregTechAPI.sGeneratedMaterials, meta),
            1000);

        GregTechAPI.sBlockSheetmetalBW = new BlockSheetMetal(
            "bw.sheetmetal",
            meta -> Werkstoff.werkstoffHashMap.get((short) meta),
            Short.MAX_VALUE);

        GregTechAPI.sBlockFramesBW = new BlockDecorativeFrame(
            "bw.frames",
            meta -> Werkstoff.werkstoffHashMap.get((short) meta),
            Short.MAX_VALUE);

        GTLog.out.println("GTMod: Register TileEntities.");

        BaseMetaTileEntity tBaseMetaTileEntity = GregTechAPI.constructBaseMetaTileEntity();

        GTLog.out.println("GTMod: Registering the BaseMetaTileEntity.");
        GameRegistry.registerTileEntity(tBaseMetaTileEntity.getClass(), "BaseMetaTileEntity");
        FMLInterModComms.sendMessage(
            AppliedEnergistics2.ID,
            "whitelist-spatial",
            tBaseMetaTileEntity.getClass()
                .getName());

        GTLog.out.println("GTMod: Registering the LaserRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityLaser.class, "LaserRenderer");

        GTLog.out.println("GTMod: Registering the WormholeRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityWormhole.class, "WormholeRender");

        GTLog.out.println("GTMod: Registering the BlackholeRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityBlackhole.class, "BlackholeRender");

        GTLog.out.println("GTMod: Registering the NanoForgeRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityNanoForge.class, "NanoForgeRender");

        GTLog.out.println("GTMod: Registering the BaseMetaPipeEntity.");
        GameRegistry.registerTileEntity(BaseMetaPipeEntity.class, "BaseMetaPipeEntity");
        FMLInterModComms.sendMessage(AppliedEnergistics2.ID, "whitelist-spatial", BaseMetaPipeEntity.class.getName());

        GTLog.out.println("GTMod: Registering the Ore TileEntity.");
        GameRegistry.registerTileEntity(TileEntityOres.class, "GT_TileEntity_Ores");
        FMLInterModComms.sendMessage(AppliedEnergistics2.ID, "whitelist-spatial", TileEntityOres.class.getName());

        GTLog.out.println("GTMod: Registering Fluids.");
        Materials.ConstructionFoam.mFluid = GTUtility.getFluidForFilledItem(GTModHandler.getIC2Item("CFCell", 1L), true)
            .getFluid();
        Materials.UUMatter.mFluid = GTUtility.getFluidForFilledItem(GTModHandler.getIC2Item("uuMatterCell", 1L), true)
            .getFluid();

        GTFluidFactory.builder("Air")
            .withDefaultLocalName("Air")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Air)
            .addLocalizedName(Materials.Air)
            .registerContainers(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L), 2000);
        GTFluidFactory.builder("LiquidOxygen")
            .withDefaultLocalName("Liquid Oxygen")
            .withStateAndTemperature(GAS, 60)
            .buildAndRegister()
            .configureMaterials(Materials.LiquidOxygen)
            .addLocalizedName(Materials.LiquidOxygen)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.LiquidOxygen, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("LiquidNitrogen")
            .withDefaultLocalName("Liquid Nitrogen")
            .withStateAndTemperature(GAS, 77)
            .buildAndRegister()
            .configureMaterials(Materials.LiquidNitrogen)
            .addLocalizedName(Materials.LiquidNitrogen)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.LiquidNitrogen, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("LiquidAir")
            .withDefaultLocalName("Liquid Air")
            .withStateAndTemperature(LIQUID, 77)
            .buildAndRegister()
            .configureMaterials(Materials.LiquidAir)
            .addLocalizedName(Materials.LiquidAir)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.LiquidAir, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Oxygen")
            .withDefaultLocalName("Oxygen")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Oxygen)
            .addLocalizedName(Materials.Oxygen)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Hydrogen")
            .withDefaultLocalName("Hydrogen")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Hydrogen)
            .addLocalizedName(Materials.Hydrogen)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Deuterium")
            .withDefaultLocalName("Deuterium")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Deuterium)
            .addLocalizedName(Materials.Deuterium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Deuterium, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Tritium")
            .withDefaultLocalName("Tritium")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Tritium)
            .addLocalizedName(Materials.Tritium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Tritium, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Helium")
            .withDefaultLocalName("Helium")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Helium)
            .addLocalizedName(Materials.Helium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Helium, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Argon")
            .withDefaultLocalName("Argon")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Argon)
            .addLocalizedName(Materials.Argon)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Argon, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Radon")
            .withDefaultLocalName("Radon")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Radon)
            .addLocalizedName(Materials.Radon)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Radon, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("Fluorine")
            .withDefaultLocalName("Fluorine")
            .withStateAndTemperature(GAS, 53)
            .buildAndRegister()
            .configureMaterials(Materials.Fluorine)
            .addLocalizedName(Materials.Fluorine)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Fluorine, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Titaniumtetrachloride")
            .withDefaultLocalName("Titaniumtetrachloride")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Titaniumtetrachloride)
            .addLocalizedName(Materials.Titaniumtetrachloride)
            .registerBContainers(
                MaterialLibAPI
                    .getStack(Materials2Materials.Titaniumtetrachloride, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Helium-3")
            .withDefaultLocalName("Helium-3")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Helium3)
            .addLocalizedName(Materials.Helium3)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Helium3, 1L),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Methane")
            .withTextureName("methane")
            .withDefaultLocalName("Methane")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Methane)
            .addLocalizedName(Materials.Methane)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Nitrogen")
            .withDefaultLocalName("Nitrogen")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Nitrogen)
            .addLocalizedName(Materials.Nitrogen)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Nitrogen, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("NitrogenDioxide")
            .withDefaultLocalName("Nitrogen Dioxide")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NitrogenDioxide)
            .addLocalizedName(Materials.NitrogenDioxide)
            .addLocalizedName(Materials.NitrogenDioxide)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Steam")
            .withDefaultLocalName("Steam")
            .withStateAndTemperature(GAS, 375)
            .buildAndRegister()
            .configureMaterials(Materials.Water)
            .addLocalizedName(Materials.Water)
            .registerBContainers(GTModHandler.getIC2Item("steamCell", 1), Materials.Empty.getCells(1));

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(GTModHandler.getIC2Item("steamCell", 1))
            .fluidInputs(Materials.Steam.getGas(1_000))
            .duration(16 * TICKS)
            .eut(1)
            .addTo(cannerRecipes);

        Materials.Ice.mGas = Materials.Water.mGas;
        Materials.Water.mGas.setTemperature(375)
            .setGaseous(true);

        ItemList.sOilExtraHeavy = GTFluidFactory.builder("liquid_extra_heavy_oil")
            .withDefaultLocalName("Very Heavy Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilExtraHeavy)
            .addLocalizedName(Materials.OilExtraHeavy)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.OilExtraHeavy, Materials2CellShapes.cell, (int) (1)),
                Materials.Empty.getCells(1))
            .asFluid();
        ItemList.sEpichlorhydrin = GTFluidFactory.builder("liquid_epichlorhydrin")
            .withDefaultLocalName("Epichlorohydrin")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Epichlorohydrin)
            .addLocalizedName(Materials.Epichlorohydrin)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Epichlorohydrin, Materials2CellShapes.cell, (int) (1)),
                Materials.Empty.getCells(1))
            .asFluid();
        ItemList.sDrillingFluid = GTFluidFactory.of("liquid_drillingfluid", "Drilling Fluid", LIQUID, 295);
        ItemList.sToluene = GTFluidFactory.builder("liquid_toluene")
            .withDefaultLocalName("Toluene")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Toluene)
            .addLocalizedName(Materials.Toluene)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Toluene, Materials2CellShapes.cell, (int) (1)),
                Materials.Empty.getCells(1))
            .asFluid();
        ItemList.sNitrationMixture = GTFluidFactory.builder("liquid_nitrationmixture")
            .withDefaultLocalName("Nitration Mixture")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NitrationMixture)
            .addLocalizedName(Materials.NitrationMixture)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.cell, (int) (1)),
                Materials.Empty.getCells(1))
            .asFluid();

        GTFluidFactory.builder("liquid_heavy_oil")
            .withDefaultLocalName("Heavy Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilHeavy)
            .addLocalizedName(Materials.OilHeavy)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.OilHeavy, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_medium_oil")
            .withDefaultLocalName("Raw Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilMedium)
            .addLocalizedName(Materials.OilMedium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.OilMedium, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_light_oil")
            .withDefaultLocalName("Light Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilLight)
            .addLocalizedName(Materials.OilLight)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.OilLight, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("gas_natural_gas")
            .withDefaultLocalName("Natural Gas")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NaturalGas)
            .addLocalizedName(Materials.NaturalGas)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.NaturalGas, 1L),
                ItemList.Cell_Empty.get(1L));
        ItemList.sHydricSulfur = GTFluidFactory.builder("liquid_hydricsulfur")
            .withDefaultLocalName("Hydrogen Sulfide")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.HydricSulfide)
            .addLocalizedName(Materials.HydricSulfide)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L))
            .asFluid();
        GTFluidFactory.builder("gas_sulfuricgas")
            .withDefaultLocalName("Sulfuric Gas")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricGas)
            .addLocalizedName(Materials.SulfuricGas)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricGas, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("gas_gas")
            .withDefaultLocalName("Refinery Gas")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Gas)
            .addLocalizedName(Materials.Gas)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Gas, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_sulfuricnaphtha")
            .withDefaultLocalName("Sulfuric Naphtha")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricNaphtha)
            .addLocalizedName(Materials.SulfuricNaphtha)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricNaphtha, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_sufluriclight_fuel")
            .withDefaultLocalName("Sulfuric Light Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricLightFuel)
            .addLocalizedName(Materials.SulfuricLightFuel)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricLightFuel, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_sulfuricheavy_fuel")
            .withDefaultLocalName("Sulfuric Heavy Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricHeavyFuel)
            .addLocalizedName(Materials.SulfuricHeavyFuel)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricHeavyFuel, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_naphtha")
            .withDefaultLocalName("Naphtha")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Naphtha)
            .addLocalizedName(Materials.Naphtha)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Naphtha, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_light_fuel")
            .withDefaultLocalName("Light Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.LightFuel)
            .addLocalizedName(Materials.LightFuel)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_heavy_fuel")
            .withDefaultLocalName("Heavy Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.HeavyFuel)
            .addLocalizedName(Materials.HeavyFuel)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("liquid_lpg")
            .withDefaultLocalName("LPG")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.LPG)
            .addLocalizedName(Materials.LPG)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.LPG, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("charcoal_byproducts")
            .withTextureName("molten.autogenerated")
            .withDefaultLocalName("Charcoal Byproducts")
            .withColorRGBA(Materials.CharcoalByproducts.mRGBa)
            .withStateAndTemperature(GAS, 775)
            .buildAndRegister()
            .configureMaterials(Materials.CharcoalByproducts)
            .addLocalizedName(Materials.CharcoalByproducts)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.CharcoalByproducts, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("molten.bisphenol_a")
            .withTextureName("molten.autogenerated")
            .withDefaultLocalName("Molten Bisphenol A")
            .withColorRGBA(Materials.BisphenolA.mRGBa)
            .withStateAndTemperature(LIQUID, 432)
            .buildAndRegister()
            .configureMaterials(Materials.BisphenolA)
            .addLocalizedName(Materials.BisphenolA)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.BisphenolA, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("UUAmplifier")
            .withDefaultLocalName("UU Amplifier")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.UUAmplifier)
            .addLocalizedName(Materials.UUAmplifier)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.UUAmplifier, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Chlorine")
            .withDefaultLocalName("Chlorine")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Chlorine)
            .addLocalizedName(Materials.Chlorine)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Mercury")
            .withDefaultLocalName("Mercury")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Mercury)
            .addLocalizedName(Materials.Mercury)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Mercury, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("NitroFuel")
            .withDefaultLocalName("Cetane-Boosted Diesel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NitroFuel)
            .addLocalizedName(Materials.NitroFuel)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.NitroFuel, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("SodiumPersulfate")
            .withDefaultLocalName("Sodium Persulfate")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SodiumPersulfate)
            .addLocalizedName(Materials.SodiumPersulfate)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SodiumPersulfate, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Glyceryl")
            .withDefaultLocalName("Glyceryl Trinitrate")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Glyceryl)
            .addLocalizedName(Materials.Glyceryl)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Glyceryl, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("lubricant")
            .withDefaultLocalName("Lubricant")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Lubricant)
            .addLocalizedName(Materials.Lubricant)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Lubricant, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("creosote")
            .withDefaultLocalName("Creosote Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Creosote)
            .addLocalizedName(Materials.Creosote)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Creosote, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("seedoil")
            .withDefaultLocalName("Seed Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SeedOil)
            .addLocalizedName(Materials.SeedOil)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SeedOil, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("fishoil")
            .withDefaultLocalName("Fish Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.FishOil)
            .addLocalizedName(Materials.FishOil)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.FishOil, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("oil")
            .withDefaultLocalName("Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Oil)
            .addLocalizedName(Materials.Oil)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Oil, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("fuel")
            .withDefaultLocalName("Diesel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Diesel)
            .addLocalizedName(Materials.Diesel)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 1L),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("for.honey")
            .withDefaultLocalName("Honey")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Honey)
            .addLocalizedName(Materials.Honey)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Honey, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("biomass")
            .withDefaultLocalName("Biomass")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Biomass)
            .addLocalizedName(Materials.Biomass)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Biomass, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("bioethanol")
            .withDefaultLocalName("Bio Ethanol")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Ethanol)
            .addLocalizedName(Materials.Ethanol)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("sulfuricacid")
            .withDefaultLocalName("Sulfuric Acid")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricAcid)
            .addLocalizedName(Materials.SulfuricAcid)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("milk")
            .withDefaultLocalName("Milk")
            .withStateAndTemperature(LIQUID, 290)
            .buildAndRegister()
            .configureMaterials(Materials.Milk)
            .addLocalizedName(Materials.Milk)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Milk, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("mcguffium")
            .withDefaultLocalName("Mc Guffium 239")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.McGuffium239)
            .addLocalizedName(Materials.McGuffium239)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.McGuffium239, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("refinedGlue")
            .withDefaultLocalName("Refined Glue")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Glue)
            .addLocalizedName(Materials.Glue)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Glue, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("hotfryingoil")
            .withDefaultLocalName("Hot Frying Oil")
            .withStateAndTemperature(LIQUID, 400)
            .buildAndRegister()
            .configureMaterials(Materials.FryingOilHot)
            .addLocalizedName(Materials.FryingOilHot)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.FryingOilHot, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        // region NewHorizonsCoreMod fluids
        // These fluids have to get registered manually so they use the MetaGeneratedItem98 cells instead of generating
        // new MetaGeneratedItem01 fluid cells.
        GTFluidFactory.builder("SodiumPotassium")
            .withDefaultLocalName("Sodium Potassium")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SodiumPotassium)
            .addLocalizedName(Materials.SodiumPotassium)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.SodiumPotassium, 1L),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("Pollution")
            .withDefaultLocalName("Pollution")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Pollution)
            .addLocalizedName(Materials.Pollution)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Pollution, 1L),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("EnrichedBacterialSludge")
            .withDefaultLocalName("Enriched Bacterial Sludge")
            .withStateAndTemperature(LIQUID, 300)
            .buildAndRegister()
            .configureMaterials(Materials.EnrichedBacterialSludge)
            .addLocalizedName(Materials.EnrichedBacterialSludge)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.EnrichedBacterialSludge, 1L),
                ItemList.Cell_Empty.get(1L))
            .asFluid()
            .setLuminosity(15);
        GTFluidFactory.builder("FermentedBacterialSludge")
            .withDefaultLocalName("Fermented Bacterial Sludge")
            .withStateAndTemperature(LIQUID, 300)
            .buildAndRegister()
            .configureMaterials(Materials.FermentedBacterialSludge)
            .addLocalizedName(Materials.FermentedBacterialSludge)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.FermentedBacterialSludge, 1L),
                ItemList.Cell_Empty.get(1L))
            .asFluid()
            .setLuminosity(8);
        // endregion

        GTFluidFactory.builder("DimensionallyTranscendentResidue")
            .withDefaultLocalName("Dimensionally Transcendent Residue")
            .withStateAndTemperature(LIQUID, 2000000000)
            .buildAndRegister()
            .configureMaterials(Materials.DTR)
            .addLocalizedName(Materials.DTR)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.DTR, 1L),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("ExcitedDTCC")
            .withDefaultLocalName("Excited Dimensionally Transcendent Crude Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(Materials.ExcitedDTCC)
            .addLocalizedName(Materials.ExcitedDTCC)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.ExcitedDTCC, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("ExcitedDTPC")
            .withDefaultLocalName("Excited Dimensionally Transcendent Prosaic Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(Materials.ExcitedDTPC)
            .addLocalizedName(Materials.ExcitedDTPC)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.ExcitedDTPC, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("ExcitedDTRC")
            .withDefaultLocalName("Excited Dimensionally Transcendent Resplendent Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(Materials.ExcitedDTRC)
            .addLocalizedName(Materials.ExcitedDTRC)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.ExcitedDTRC, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("ExcitedDTEC")
            .withDefaultLocalName("Excited Dimensionally Transcendent Exotic Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(Materials.ExcitedDTEC)
            .addLocalizedName(Materials.ExcitedDTEC)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.ExcitedDTEC, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        GTFluidFactory.builder("ExcitedDTSC")
            .withDefaultLocalName("Excited Dimensionally Transcendent Stellar Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(Materials.ExcitedDTSC)
            .addLocalizedName(Materials.ExcitedDTSC)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.ExcitedDTSC, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder(Materials.RawStarMatter.mName)
            .withDefaultLocalName(Materials.RawStarMatter.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 10_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.RawStarMatter)
            .addLocalizedName(Materials.RawStarMatter)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.RawStarMatter, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder(Materials.Space.mName)
            .withDefaultLocalName(Materials.Space.mDefaultLocalName)
            .withStateAndTemperature(MOLTEN, 0)
            .buildAndRegister()
            .configureMaterials(Materials.Space)
            .addLocalizedName(Materials.Space)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Space, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder(Materials.Time.mName)
            .withDefaultLocalName(Materials.Time.mDefaultLocalName)
            .withStateAndTemperature(MOLTEN, 0)
            .buildAndRegister()
            .configureMaterials(Materials.Time)
            .addLocalizedName(Materials.Time)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Time, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("PrimordialMatter")
            .withDefaultLocalName(Materials.PrimordialMatter.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 2_000_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.PrimordialMatter)
            .addLocalizedName(Materials.PrimordialMatter)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.PrimordialMatter, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("QuarkGluonPlasma")
            .withDefaultLocalName(Materials.QuarkGluonPlasma.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 2_000_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.QuarkGluonPlasma)
            .addLocalizedName(Materials.QuarkGluonPlasma)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.QuarkGluonPlasma, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("PhononMedium")
            .withDefaultLocalName(Materials.PhononMedium.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 500)
            .buildAndRegister()
            .configureMaterials(Materials.PhononMedium)
            .addLocalizedName(Materials.PhononMedium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.PhononMedium, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("PhononCrystalSolution")
            .withDefaultLocalName(Materials.PhononCrystalSolution.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 500)
            .buildAndRegister()
            .configureMaterials(Materials.PhononCrystalSolution)
            .addLocalizedName(Materials.PhononCrystalSolution)
            .registerBContainers(
                MaterialLibAPI
                    .getStack(Materials2Materials.PhononCrystalSolution, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("antimatter")
            .withDefaultLocalName(Materials.Antimatter.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 1000000)
            .buildAndRegister()
            .configureMaterials(Materials.Antimatter)
            .addLocalizedName(Materials.Antimatter)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Antimatter, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("protomatter")
            .withDefaultLocalName(Materials.Protomatter.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 1)
            .buildAndRegister()
            .configureMaterials(Materials.Protomatter)
            .addLocalizedName(Materials.Protomatter)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Protomatter, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("plasma.infinity")
            .withDefaultLocalName("Infinity Plasma")
            .withStateAndTemperature(PLASMA, 10000)
            .buildAndRegister()
            .configureMaterials(Materials.Infinity)
            .addLocalizedName(Materials.Infinity)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2CellShapes.cellPlasma, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("plasma.bedrockium")
            .withDefaultLocalName("Bedrockium Plasma")
            .withStateAndTemperature(PLASMA, 10000)
            .buildAndRegister()
            .configureMaterials(Materials.Bedrockium)
            .addLocalizedName(Materials.Bedrockium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Bedrockium, Materials2CellShapes.cellPlasma, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("plasma.cosmicneutronium")
            .withDefaultLocalName("Cosmic Neutronium Plasma")
            .withStateAndTemperature(PLASMA, 10000)
            .buildAndRegister()
            .configureMaterials(Materials.CosmicNeutronium)
            .addLocalizedName(Materials.CosmicNeutronium)
            .registerBContainers(
                MaterialLibAPI
                    .getStack(Materials2Materials.CosmicNeutronium, Materials2CellShapes.cellPlasma, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("molten.cosmicneutronium")
            .withDefaultLocalName("Molten Cosmic Neutronium")
            .withStateAndTemperature(MOLTEN, 9900)
            .buildAndRegister()
            .configureMaterials(Materials.CosmicNeutronium)
            .addLocalizedName(Materials.CosmicNeutronium)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.CosmicNeutronium, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("plasma.ichorium")
            .withDefaultLocalName("Ichorium Plasma")
            .withStateAndTemperature(PLASMA, 9000)
            .buildAndRegister()
            .configureMaterials(Materials.Ichorium)
            .addLocalizedName(Materials.Ichorium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Ichorium, Materials2CellShapes.cellPlasma, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("molten.ichorium")
            .withDefaultLocalName("Molten Ichorium")
            .withStateAndTemperature(MOLTEN, 9000)
            .buildAndRegister()
            .configureMaterials(Materials.Ichorium)
            .addLocalizedName(Materials.Ichorium)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Ichorium, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("InactiveCosmicSolder")
            .withDefaultLocalName(Materials.InactiveCosmicSolder.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 1_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.InactiveCosmicSolder)
            .addLocalizedName(Materials.InactiveCosmicSolder)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.InactiveCosmicSolder, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("BoundlessCosmicSolder")
            .withDefaultLocalName(Materials.BoundlessCosmicSolder.mDefaultLocalName)
            .withStateAndTemperature(LIQUID, 1_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.BoundlessCosmicSolder)
            .addLocalizedName(Materials.BoundlessCosmicSolder)
            .registerBContainers(
                MaterialLibAPI
                    .getStack(Materials2Materials.BoundlessCosmicSolder, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("ComputationBase")
            .withDefaultLocalName(Materials.ComputationBase.mDefaultLocalName)
            .withStateAndTemperature(MOLTEN, 100_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.ComputationBase)
            .addLocalizedName(Materials.ComputationBase)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.ComputationBase, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("fieryblood")
            .withDefaultLocalName("Fiery Blood")
            .withStateAndTemperature(LIQUID, 6400)
            .buildAndRegister()
            .configureMaterials(Materials.FierySteel)
            .addLocalizedName(Materials.FierySteel)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.FierySteel, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder("holywater")
            .withDefaultLocalName("Holy Water")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.HolyWater)
            .addLocalizedName(Materials.HolyWater)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.HolyWater, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));
        if (ItemList.TF_Vial_FieryBlood.get(1L) != null) {
            FluidContainerRegistry.registerFluidContainer(
                new FluidContainerRegistry.FluidContainerData(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, (int) (250)),
                    ItemList.TF_Vial_FieryBlood.get(1L),
                    ItemList.Bottle_Empty.get(1L)));
        }

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Milk, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Milk, 1L),
                GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials2Materials.Milk, Materials2FluidShapes.fluidLiquid, (int) (250)),
                ItemList.Bottle_Milk.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HolyWater, Materials2FluidShapes.fluidLiquid, (int) (250)),
                ItemList.Bottle_Holy_Water.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, (int) (250)),
                ItemList.McGuffium_239.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.Diesel.getFluid(100L),
                ItemList.Tool_Lighter_Invar_Full.get(1L),
                ItemList.Tool_Lighter_Invar_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.Diesel.getFluid(1_000),
                ItemList.Tool_Lighter_Platinum_Full.get(1L),
                ItemList.Tool_Lighter_Platinum_Empty.get(1L)));

        Dyes.dyeBlack.addFluidDye(GTFluidFactory.of("squidink", "Squid Ink", LIQUID, 295));
        Dyes.dyeBlue.addFluidDye(GTFluidFactory.of("indigo", "Indigo Dye", LIQUID, 295));
        for (byte i = 0; i < Dyes.VALUES.length; i = (byte) (i + 1)) {
            Dyes tDye = Dyes.VALUES[i];
            tDye.addFluidDye(
                GTFluidFactory.builder(
                    "dye.watermixed." + tDye.name()
                        .toLowerCase(Locale.ENGLISH))
                    .withTextureName("dyes")
                    .withDefaultLocalName("Water Mixed " + tDye.mName + " Dye")
                    .withColorRGBA(tDye.getRGBA())
                    .withStateAndTemperature(LIQUID, 295)
                    .buildAndRegister()
                    .addLocalizedName()
                    .asFluid());
            tDye.addFluidDye(
                GTFluidFactory.builder(
                    "dye.chemical." + tDye.name()
                        .toLowerCase(Locale.ENGLISH))
                    .withTextureName("dyes")
                    .withDefaultLocalName("Chemical " + tDye.mName + " Dye")
                    .withColorRGBA(tDye.getRGBA())
                    .withStateAndTemperature(LIQUID, 295)
                    .buildAndRegister()
                    .addLocalizedName()
                    .registerContainers(ItemList.SPRAY_CAN_DYES[i].get(1L), ItemList.Spray_Empty.get(1L), 16 * INGOTS)
                    .asFluid());
        }
        GTFluidFactory.builder("ice")
            .withDefaultLocalName("Crushed Ice")
            .withStateAndTemperature(SLURRY, 270)
            .buildAndRegister()
            .configureMaterials(Materials.Ice)
            .addLocalizedName(Materials.Ice)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Ice, 1L),
                ItemList.Cell_Empty.get(1L));
        Materials.Water.mSolid = Materials.Ice.mSolid;

        GTFluidFactory.builder("molten.glass")
            .withDefaultLocalName("Molten Glass")
            .withStateAndTemperature(MOLTEN, 1500)
            .buildAndRegister()
            .configureMaterials(Materials.Glass)
            .addLocalizedName(Materials.Glass)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Glass, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS);
        GTFluidFactory.builder("molten.redstone")
            .withDefaultLocalName("Molten Redstone")
            .withStateAndTemperature(MOLTEN, 500)
            .buildAndRegister()
            .configureMaterials(Materials.Redstone)
            .addLocalizedName(Materials.Redstone)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Redstone, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS);
        GTFluidFactory.builder("molten.blaze")
            .withDefaultLocalName("Molten Blaze")
            .withStateAndTemperature(MOLTEN, 6400)
            .buildAndRegister()
            .configureMaterials(Materials.Blaze)
            .addLocalizedName(Materials.Blaze)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Blaze, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS);
        GTFluidFactory.builder("wet.concrete")
            .withDefaultLocalName("Wet Concrete")
            .withStateAndTemperature(MOLTEN, 300)
            .buildAndRegister()
            .configureMaterials(Materials.Concrete)
            .addLocalizedName(Materials.Concrete)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Concrete, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS);
        GTFluidFactory.builder("molten.granitered")
            .withDefaultLocalName("Molten Red Granite")
            .withTextureName("molten.autogenerated")
            .withColorRGBA(Materials.GraniteRed.mRGBa)
            .withStateAndTemperature(MOLTEN, 1520)
            .buildAndRegister()
            .configureMaterials(Materials.GraniteRed)
            .addLocalizedName(Materials.GraniteRed)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.GraniteRed, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS);
        GTFluidFactory.builder("molten.graniteblack")
            .withTextureName("molten.autogenerated")
            .withDefaultLocalName("Molten Black Granite")
            .withColorRGBA(Materials.GraniteBlack.mRGBa)
            .withStateAndTemperature(MOLTEN, 1520)
            .buildAndRegister()
            .configureMaterials(Materials.GraniteBlack)
            .addLocalizedName(Materials.GraniteBlack)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.GraniteBlack, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS);

        GTFluidFactory.builder("activatednetherite")
            .withDefaultLocalName(Materials.ActivatedNetherite.mDefaultLocalName)
            .withTextureName("molten.autogenerated")
            .withColorRGBA(Materials.ActivatedNetherite.mRGBa)
            .withStateAndTemperature(MOLTEN, 50_000_000)
            .buildAndRegister()
            .configureMaterials(Materials.ActivatedNetherite)
            .addLocalizedName(Materials.ActivatedNetherite)
            .registerBContainers(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.ActivatedNetherite, 1L),
                ItemList.Cell_Empty.get(1L));

        GTFluidFactory.builder(Materials.CarbonDioxide.mName.toLowerCase(Locale.ENGLISH))
            .withDefaultLocalName(Materials.CarbonDioxide.mDefaultLocalName)
            .withTextureName("carbondioxide")
            .withStateAndTemperature(GAS, Materials.CarbonDioxide.getGasTemperature())
            .buildAndRegister()
            .configureMaterials(Materials.CarbonDioxide)
            .addLocalizedName(Materials.CarbonDioxide)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        ResourceLocation stillTexture = new ResourceLocation(GregTech.ID, "fluids/fluid.ammonia.still");
        ResourceLocation flowTexture = new ResourceLocation(GregTech.ID, "fluids/fluid.ammonia.flow");

        GTFluidFactory.builder(Materials.Ammonia.mName.toLowerCase(Locale.ENGLISH))
            .withDefaultLocalName(Materials.Ammonia.mDefaultLocalName)
            .withTextures(stillTexture, flowTexture)
            .withStateAndTemperature(GAS, Materials.Ammonia.getGasTemperature())
            .buildAndRegister()
            .configureMaterials(Materials.Ammonia)
            .addLocalizedName(Materials.Ammonia)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        for (Materials tMaterial : Materials.values()) {
            // GTFluid.DUMP_MODE bypasses these mStandardMoltenFluid-wired checks (in addition to the skip
            // inside addAutogeneratedMoltenFluid itself) so the legacy builder actually runs and captures its
            // texture into GTFluid.DUMP_TEXTURES, even for materials MaterialLib already wired.
            if ((GTFluid.DUMP_MODE || tMaterial.mStandardMoltenFluid == null)
                && (MU.hasFlag(tMaterial, GTMaterialFlag.SMELTING_TO_FLUID))
                && (!MU.hasFlag(tMaterial, GTMaterialFlag.NO_SMELTING))) {
                if (!OrePrefixes.cellMolten.mNotGeneratedItems.contains(tMaterial)) {
                    GTMod.proxy.addAutogeneratedMoltenFluid(tMaterial);
                }
                Materials smeltInto = MU.smeltInto(tMaterial);
                if ((smeltInto != tMaterial) && (GTFluid.DUMP_MODE || smeltInto.mStandardMoltenFluid == null)) {
                    GTMod.proxy.addAutogeneratedMoltenFluid(smeltInto);
                }
            }
            if (tMaterial.mElement != null || (tMaterial.hasPlasma() && !tMaterial.mIconSet.is_custom)) {
                GTMod.proxy.addAutogeneratedPlasmaFluid(tMaterial);
            }
            if (tMaterial.hasCorrespondingFluid()) {
                GTMod.proxy.addAutoGeneratedCorrespondingFluid(tMaterial);
            }
            if (tMaterial.hasCorrespondingGas()) {
                GTMod.proxy.addAutoGeneratedCorrespondingGas(tMaterial);
            }
            if (tMaterial.canBeCracked()) {
                GTMod.proxy.addAutoGeneratedHydroCrackedFluids(tMaterial);
                GTMod.proxy.addAutoGeneratedSteamCrackedFluids(tMaterial);
            }
        }

        GTFluidFactory.builder("potion.awkward")
            .withDefaultLocalName("Awkward Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.thick")
            .withDefaultLocalName("Thick Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 32), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.mundane")
            .withDefaultLocalName("Mundane Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 64), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.damage")
            .withDefaultLocalName("Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8204), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.damage.strong")
            .withDefaultLocalName("Strong Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8236), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.damage.splash")
            .withDefaultLocalName("Splash Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16396), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.damage.strong.splash")
            .withDefaultLocalName("Strong Splash Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16428), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.health")
            .withDefaultLocalName("Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8197), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.health.strong")
            .withDefaultLocalName("Strong Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8229), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.health.splash")
            .withDefaultLocalName("Splash Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16389), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.health.strong.splash")
            .withDefaultLocalName("Strong Splash Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16421), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.speed")
            .withDefaultLocalName("Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8194), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.speed.strong")
            .withDefaultLocalName("Strong Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8226), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.speed.long")
            .withDefaultLocalName("Stretched Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8258), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.speed.splash")
            .withDefaultLocalName("Splash Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16386), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.speed.strong.splash")
            .withDefaultLocalName("Strong Splash Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16418), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.speed.long.splash")
            .withDefaultLocalName("Stretched Splash Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16450), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.strength")
            .withDefaultLocalName("Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8201), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.strength.strong")
            .withDefaultLocalName("Strong Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8233), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.strength.long")
            .withDefaultLocalName("Stretched Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8265), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.strength.splash")
            .withDefaultLocalName("Splash Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16393), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.strength.strong.splash")
            .withDefaultLocalName("Strong Splash Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16425), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.strength.long.splash")
            .withDefaultLocalName("Stretched Splash Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16457), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.regen")
            .withDefaultLocalName("Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8193), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.regen.strong")
            .withDefaultLocalName("Strong Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8225), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.regen.long")
            .withDefaultLocalName("Stretched Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8257), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.regen.splash")
            .withDefaultLocalName("Splash Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16385), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.regen.strong.splash")
            .withDefaultLocalName("Strong Splash Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16417), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.regen.long.splash")
            .withDefaultLocalName("Stretched Splash Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16449), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.poison")
            .withDefaultLocalName("Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8196), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.poison.strong")
            .withDefaultLocalName("Strong Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8228), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.poison.long")
            .withDefaultLocalName("Stretched Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8260), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.poison.splash")
            .withDefaultLocalName("Splash Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16388), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.poison.strong.splash")
            .withDefaultLocalName("Strong Splash Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16420), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.poison.long.splash")
            .withDefaultLocalName("Stretched Splash Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16452), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.fireresistance")
            .withDefaultLocalName("Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8195), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.fireresistance.long")
            .withDefaultLocalName("Stretched Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8259), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.fireresistance.splash")
            .withDefaultLocalName("Splash Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16387), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.fireresistance.long.splash")
            .withDefaultLocalName("Stretched Splash Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16451), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.nightvision")
            .withDefaultLocalName("Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8198), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.nightvision.long")
            .withDefaultLocalName("Stretched Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8262), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.nightvision.splash")
            .withDefaultLocalName("Splash Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16390), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.nightvision.long.splash")
            .withDefaultLocalName("Stretched Splash Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16454), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.weakness")
            .withDefaultLocalName("Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8200), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.weakness.long")
            .withDefaultLocalName("Stretched Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8264), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.weakness.splash")
            .withDefaultLocalName("Splash Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16392), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.weakness.long.splash")
            .withDefaultLocalName("Stretched Splash Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16456), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.slowness")
            .withDefaultLocalName("Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8202), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.slowness.long")
            .withDefaultLocalName("Stretched Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8266), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.slowness.splash")
            .withDefaultLocalName("Splash Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16394), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.slowness.long.splash")
            .withDefaultLocalName("Stretched Splash Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16458), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.waterbreathing")
            .withDefaultLocalName("Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8205), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.waterbreathing.long")
            .withDefaultLocalName("Stretched Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8269), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.waterbreathing.splash")
            .withDefaultLocalName("Splash Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16397), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.waterbreathing.long.splash")
            .withDefaultLocalName("Stretched Splash Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16461), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.invisibility")
            .withDefaultLocalName("Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8206), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.invisibility.long")
            .withDefaultLocalName("Stretched Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8270), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.invisibility.splash")
            .withDefaultLocalName("Splash Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16398), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.invisibility.long.splash")
            .withDefaultLocalName("Stretched Splash Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16462), ItemList.Bottle_Empty.get(1L));

        GTFluidFactory.builder("potion.purpledrink")
            .withDefaultLocalName("Purple Drink")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Purple_Drink.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.grapejuice")
            .withDefaultLocalName("Grape Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Grape_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.wine")
            .withDefaultLocalName("Wine")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Wine.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.vinegar")
            .withDefaultLocalName("Vinegar")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Vinegar)
            .addLocalizedName(Materials.Vinegar)
            .registerPContainers(ItemList.Bottle_Vinegar.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.potatojuice")
            .withDefaultLocalName("Potato Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Potato_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.vodka")
            .withDefaultLocalName("Vodka")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Vodka.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.leninade")
            .withDefaultLocalName("Leninade")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Leninade.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.mineralwater")
            .withDefaultLocalName("Mineral Water")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Mineral_Water.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.saltywater")
            .withDefaultLocalName("Salty Water")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Salty_Water.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.reedwater")
            .withDefaultLocalName("Reed Water")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Reed_Water.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.rum")
            .withDefaultLocalName("Rum")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Rum.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.piratebrew")
            .withDefaultLocalName("Pirate Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Pirate_Brew.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.hopsjuice")
            .withDefaultLocalName("Hops Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Hops_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.darkbeer")
            .withDefaultLocalName("Dark Beer")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Dark_Beer.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.dragonblood")
            .withDefaultLocalName("Dragon Blood")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Dragon_Blood.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.wheatyjuice")
            .withDefaultLocalName("Wheaty Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Wheaty_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.scotch")
            .withDefaultLocalName("Scotch")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Scotch.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.glenmckenner")
            .withDefaultLocalName("Glen McKenner")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Glen_McKenner.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.wheatyhopsjuice")
            .withDefaultLocalName("Wheaty Hops Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Wheaty_Hops_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.beer")
            .withDefaultLocalName("Beer")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Beer.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.chillysauce")
            .withDefaultLocalName("Chilly Sauce")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Chilly_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.hotsauce")
            .withDefaultLocalName("Hot Sauce")
            .withStateAndTemperature(LIQUID, 380)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Hot_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.diabolosauce")
            .withDefaultLocalName("Diabolo Sauce")
            .withStateAndTemperature(LIQUID, 385)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Diabolo_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.diablosauce")
            .withDefaultLocalName("Diablo Sauce")
            .withStateAndTemperature(LIQUID, 390)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Diablo_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.diablosauce.strong")
            .withDefaultLocalName("Old Man Snitches glitched Diablo Sauce")
            .withStateAndTemperature(LIQUID, 999)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Snitches_Glitch_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.applejuice")
            .withDefaultLocalName("Apple Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Apple_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.cider")
            .withDefaultLocalName("Cider")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Cider.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.goldenapplejuice")
            .withDefaultLocalName("Golden Apple Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Golden_Apple_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.goldencider")
            .withDefaultLocalName("Golden Cider")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Golden_Cider.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.idunsapplejuice")
            .withDefaultLocalName("Idun's Apple Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Iduns_Apple_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.notchesbrew")
            .withDefaultLocalName("Notches Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Notches_Brew.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.lemonjuice")
            .withDefaultLocalName("Lemon Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Lemon_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.limoncello")
            .withDefaultLocalName("Limoncello")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Limoncello.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.lemonade")
            .withDefaultLocalName("Lemonade")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Lemonade.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.alcopops")
            .withDefaultLocalName("Alcopops")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Alcopops.get(1L), ItemList.Bottle_Empty.get(1L));
        GTFluidFactory.builder("potion.cavejohnsonsgrenadejuice")
            .withDefaultLocalName("Cave Johnsons Grenade Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.Bottle_Cave_Johnsons_Grenade_Juice.get(1L), ItemList.Bottle_Empty.get(1L));

        GTFluidFactory.builder("potion.coffee")
            .withDefaultLocalName("Coffee")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Coffee.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.sweetcoffee")
            .withDefaultLocalName("Sweet Coffee")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Sweet_Coffee.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.Latte")
            .withDefaultLocalName("Latte")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Latte.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.sweetlatte")
            .withDefaultLocalName("Sweet Latte")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Sweet_Latte.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.sweetjesuslatte")
            .withDefaultLocalName("Sweet Jesus Latte")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Sweet_Jesus_Latte.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.darkchocolatemilk")
            .withDefaultLocalName("Dark Chocolate Milk")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Dark_Chocolate_Milk.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.chocolatemilk")
            .withDefaultLocalName("Chocolate Milk")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Chocolate_Milk.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.tea")
            .withDefaultLocalName("Tea")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Tea.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.sweettea")
            .withDefaultLocalName("Sweet Tea")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Sweet_Tea.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("potion.icetea")
            .withDefaultLocalName("Ice Tea")
            .withStateAndTemperature(LIQUID, 255)
            .buildAndRegister()
            .addLocalizedName()
            .registerPContainers(ItemList.ThermosCan_Ice_Tea.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GTFluidFactory.builder("liquid_sodium")
            .withDefaultLocalName("Liquid Sodium")
            .withStateAndTemperature(LIQUID, 495)
            .buildAndRegister()
            .configureMaterials(Materials.Sodium)
            .addLocalizedName(Materials.Sodium)
            .registerBContainers(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2CellShapes.cell, (int) (1)),
                ItemList.Cell_Empty.get(1L));

        if (!GTValues.D1) {
            if (Mods.NotEnoughItems.isModLoaded()) {
                GTLog.out.println("GTMod: Hiding certain Items from NEI.");
                API.hideItem(ItemList.Display_Fluid.getWildcard(1L));
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 1, WILDCARD))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)),
                new ItemStack(Items.flint, 1))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTOreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.FierySteel,
            GTModHandler.getModItem(TwilightForest.ID, "item.fieryIngot", 1L, 0));
        GTOreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.Knightmetal,
            GTModHandler.getModItem(TwilightForest.ID, "item.knightMetal", 1L, 0));
        GTOreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.Steeleaf,
            GTModHandler.getModItem(TwilightForest.ID, "item.steeleafIngot", 1L, 0));
        GTOreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.IronWood,
            GTModHandler.getModItem(TwilightForest.ID, "item.ironwoodIngot", 1L, 0));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedAir, GTModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 0));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedFire, GTModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 1));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedWater, GTModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 2));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedEarth, GTModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 3));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedOrder, GTModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 4));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedEntropy, GTModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 5));
        GTOreDictUnificator
            .set(OrePrefixes.nugget, Materials.Mercury, GTModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1L, 5));
        GTOreDictUnificator
            .set(OrePrefixes.nugget, Materials.Thaumium, GTModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1L, 6));
        GTOreDictUnificator
            .set(OrePrefixes.ingot, Materials.Thaumium, GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 2));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.Mercury, GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 3));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.Amber, GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 6));
        GTOreDictUnificator
            .set(OrePrefixes.gem, Materials.Firestone, GTModHandler.getModItem(Railcraft.ID, "firestone.raw", 1L));

        GTOreDictUnificator
            .set(OrePrefixes.nugget, Materials.Void, GTModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1L, 7));
        GTOreDictUnificator
            .set(OrePrefixes.ingot, Materials.Void, GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 16));

        // Special secondary oredict for Sodium Hydroxide
        GTOreDictUnificator.registerOre("dustSodiumHydroxide", Materials.SodiumHydroxide.getDust(1));
        GTOreDictUnificator.registerOre("dustSmallSodiumHydroxide", Materials.SodiumHydroxide.getDustSmall(1));
        GTOreDictUnificator.registerOre("dustTinySodiumHydroxide", Materials.SodiumHydroxide.getDustTiny(1));

        GTOreDictUnificator.set(
            OrePrefixes.dust,
            Materials.Cocoa,
            GTModHandler.getModItem(PamsHarvestCraft.ID, "cocoapowderItem", 1L, 0));

        GregTechAPI.sSolenoidCoilCasings = new BlockCyclotronCoils();
        ItemList.TierdDrone0
            .set(new ItemTierDrone("tierdDrone0", "Drone (Level 1)", "Quadcopter Stable Small Aircraft", 1));
        ItemList.TierdDrone1
            .set(new ItemTierDrone("tierdDrone1", "Drone (Level 2)", "Dual Turbo High-Ejection Medium Aircraft", 2));
        ItemList.TierdDrone2
            .set(new ItemTierDrone("tierdDrone2", "Drone (Level 3)", "Single Engine Anti-Gravity Large Aircraft", 3));
        ItemList.TierdDrone3.set(new ItemTierDrone("tierdDrone3", "Drone (Level 4)", "Warp engine FTL Shuttle", 4));
    }
}
