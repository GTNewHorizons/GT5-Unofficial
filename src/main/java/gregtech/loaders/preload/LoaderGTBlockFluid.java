package gregtech.loaders.preload;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.FluidState.LIQUID;
import static gregtech.api.enums.FluidState.SLURRY;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
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
import net.minecraftforge.fluids.FluidContainerRegistry;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.items.BlockLongDistancePipe;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.ItemBreederCell;
import gregtech.api.items.ItemCoolantCellIC;
import gregtech.api.items.ItemRadioactiveCellIC;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OreDictUtils;
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
import gregtech.common.blocks.BlockFenceMetal;
import gregtech.common.blocks.BlockGlass1;
import gregtech.common.blocks.BlockGranites;
import gregtech.common.blocks.BlockLaser;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.blocks.BlockPad;
import gregtech.common.blocks.BlockReinforced;
import gregtech.common.blocks.BlockRenderer;
import gregtech.common.blocks.BlockStones;
import gregtech.common.blocks.BlockTintedIndustrialGlass;
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
        GT_FML_LOGGER.debug("GTMod: Register Books.");

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

        GT_FML_LOGGER.debug("GTMod: Register Items.");

        new ItemIntegratedCircuit();

        new MetaGeneratedItem01();
        new MetaGeneratedItem02();
        new MetaGeneratedItem03();
        // GT_MetaGenerated_Item_98 is initialized in GTProxy.onPostInitialization()
        // because we need to wait for fluids to be registered.
        // Pre-initialization needs to happen before then, though, because the cell icons get deleted at some point
        // between load and post-load.
        MetaGeneratedItem98.preInit();
        new MetaGeneratedTool01();
        new ItemFluidDisplay();
        new ItemWirelessHeadphones();
        new ItemMagLevHarness();
        MechArmorLoader.run();
        new CircuitComponentFakeItem();

        // Tiered recipe materials actually appear to be set in MTEBasicMachineWithRecipe, making these
        // unused
        ItemList.Rotor_LV.set(MaterialLibAPI.getStack(Materials.Tin, Shapes.rotor, 1));
        ItemList.Rotor_MV.set(MaterialLibAPI.getStack(Materials.Bronze, Shapes.rotor, 1));
        ItemList.Rotor_HV.set(MaterialLibAPI.getStack(Materials.Steel, Shapes.rotor, 1));
        ItemList.Rotor_EV.set(MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.rotor, 1));
        ItemList.Rotor_IV.set(MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.rotor, 1));

        ItemList.VOLUMETRIC_FLASK.set(new ItemVolumetricFlask("Volumetric_Flask", 1_000));

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

        GT_FML_LOGGER.debug("GTMod: Adding Blocks.");
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
        GregTechAPI.sBlockPad = new BlockPad();
        GregTechAPI.sBlockFenceMetal = new BlockFenceMetal();
        GregTechAPI.sBlockGlass1 = new BlockGlass1();
        GregTechAPI.sBlockTintedGlass = new BlockTintedIndustrialGlass();
        GregTechAPI.sLaserRender = new BlockLaser();
        GT_FML_LOGGER.debug("GTMod: Adding Renderer Blocks.");
        GregTechAPI.sWormholeRender = new BlockRenderer<>("wormholerenderer", RenderingTileEntityWormhole::new);
        GregTechAPI.sBlackholeRender = new BlockRenderer<>("blackholerenderer", RenderingTileEntityBlackhole::new);
        GregTechAPI.nanoForgeRender = new BlockRenderer<>("nanoforgerenderer", RenderingTileEntityNanoForge::new);

        GTOreAdapter.INSTANCE.registerOreNames();

        GregTechAPI.sBlockReinforced = new BlockReinforced("gt.blockreinforced");

        GT_FML_LOGGER.debug("GTMod: Register TileEntities.");

        BaseMetaTileEntity tBaseMetaTileEntity = GregTechAPI.constructBaseMetaTileEntity();

        GT_FML_LOGGER.debug("GTMod: Registering the BaseMetaTileEntity.");
        GameRegistry.registerTileEntity(tBaseMetaTileEntity.getClass(), "BaseMetaTileEntity");
        FMLInterModComms.sendMessage(
            AppliedEnergistics2.ID,
            "whitelist-spatial",
            tBaseMetaTileEntity.getClass()
                .getName());

        GT_FML_LOGGER.debug("GTMod: Registering the LaserRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityLaser.class, "LaserRenderer");

        GT_FML_LOGGER.debug("GTMod: Registering the WormholeRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityWormhole.class, "WormholeRender");

        GT_FML_LOGGER.debug("GTMod: Registering the BlackholeRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityBlackhole.class, "BlackholeRender");

        GT_FML_LOGGER.debug("GTMod: Registering the NanoForgeRender.");
        GameRegistry.registerTileEntity(RenderingTileEntityNanoForge.class, "NanoForgeRender");

        GT_FML_LOGGER.debug("GTMod: Registering the BaseMetaPipeEntity.");
        GameRegistry.registerTileEntity(BaseMetaPipeEntity.class, "BaseMetaPipeEntity");
        FMLInterModComms.sendMessage(AppliedEnergistics2.ID, "whitelist-spatial", BaseMetaPipeEntity.class.getName());

        GT_FML_LOGGER.debug("GTMod: Registering the Ore TileEntity.");

        GT_FML_LOGGER.debug("GTMod: Registering Fluids.");

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getEmptyCell(1L))
            .itemOutputs(MetaGeneratedItem98.FluidCell.STEAM.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Steam, FluidShapes.fluidGas, 1_000))
            .duration(16 * TICKS)
            .eut(1)
            .addTo(cannerRecipes);

        ItemList.sOilExtraHeavy = MaterialLibAPI.getFluid(Materials.OilExtraHeavy, FluidShapes.fluidLiquid);
        ItemList.sEpichlorhydrin = MaterialLibAPI.getFluid(Materials.Epichlorohydrin, FluidShapes.fluidLiquid);
        ItemList.sDrillingFluid = GTFluidFactory.of("liquid_drillingfluid", "Drilling Fluid", LIQUID, 295);
        ItemList.sToluene = MaterialLibAPI.getFluid(Materials.Toluene, FluidShapes.fluidLiquid);
        ItemList.sNitrationMixture = MaterialLibAPI.getFluid(Materials.NitrationMixture, FluidShapes.fluidLiquid);

        ItemList.sHydricSulfur = MaterialLibAPI.getFluid(Materials.HydricSulfide, FluidShapes.fluidGas);

        if (ItemList.TF_Vial_FieryBlood.get(1L) != null) {
            FluidContainerRegistry.registerFluidContainer(
                new FluidContainerRegistry.FluidContainerData(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, FluidShapes.fluidLiquid, 250),
                    ItemList.TF_Vial_FieryBlood.get(1L),
                    ItemList.Bottle_Empty.get(1L)));
        }

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.Milk, FluidShapes.fluidLiquid, 1_000),
                new ItemStack(Items.milk_bucket, 1),
                new ItemStack(Items.bucket, 1)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.Milk, FluidShapes.fluidLiquid, 250),
                ItemList.Bottle_Milk.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.HolyWater, FluidShapes.fluidLiquid, 250),
                ItemList.Bottle_Holy_Water.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.McGuffium239, FluidShapes.fluidLiquid, 250),
                ItemList.McGuffium_239.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.Fuel, FluidShapes.fluidLiquid, 100),
                ItemList.Tool_Lighter_Invar_Full.get(1L),
                ItemList.Tool_Lighter_Invar_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.Fuel, FluidShapes.fluidLiquid, 1_000),
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
            .addLocalizedName()
            .registerBContainers(
                MaterialLibAPI.getStack(Materials.Ice, CellShapes.cellSolid, 1),
                ItemList.Cell_Empty.get(1L));

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 250),
                ItemList.Bottle_Salt_Water.get(1L),
                ItemList.Bottle_Empty.get(1L)));

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
        FluidContainerRegistry.registerFluidContainer(
            MaterialLibAPI.getFluidStack(Materials.Vinegar, FluidShapes.fluidLiquid, 250),
            ItemList.Bottle_Vinegar.get(1L),
            ItemList.Bottle_Empty.get(1L));
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

        if (!GTValues.D1) {
            if (Mods.NotEnoughItems.isModLoaded()) {
                GT_FML_LOGGER.debug("GTMod: Hiding certain Items from NEI.");
                API.hideItem(ItemList.Display_Fluid.getWildcard(1L));
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 1, WILDCARD))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1), new ItemStack(Items.flint, 1))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Special secondary oredict for Sodium Hydroxide
        OreDictUtils
            .registerOre("dustSodiumHydroxide", MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1));
        OreDictUtils.registerOre("dustCalciumSulfate", MaterialLibAPI.getStack(Materials.Gypsum, Shapes.dust, 1));
        OreDictUtils.registerOre(
            "dustSmallSodiumHydroxide",
            MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustSmall, 1));
        OreDictUtils.registerOre(
            "dustTinySodiumHydroxide",
            MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1));

        OreDictUtils.registerOre(
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
