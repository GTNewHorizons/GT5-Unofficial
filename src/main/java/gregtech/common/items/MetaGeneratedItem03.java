package gregtech.common.items;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_METRICS_TRANSMITTER;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UEV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UHV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UIV;
import static gregtech.client.GTTooltipHandler.Tier.EV;
import static gregtech.client.GTTooltipHandler.Tier.HV;
import static gregtech.client.GTTooltipHandler.Tier.IV;
import static gregtech.client.GTTooltipHandler.Tier.LV;
import static gregtech.client.GTTooltipHandler.Tier.LuV;
import static gregtech.client.GTTooltipHandler.Tier.MAX;
import static gregtech.client.GTTooltipHandler.Tier.MV;
import static gregtech.client.GTTooltipHandler.Tier.UEV;
import static gregtech.client.GTTooltipHandler.Tier.UHV;
import static gregtech.client.GTTooltipHandler.Tier.UIV;
import static gregtech.client.GTTooltipHandler.Tier.ULV;
import static gregtech.client.GTTooltipHandler.Tier.UMV;
import static gregtech.client.GTTooltipHandler.Tier.UV;
import static gregtech.client.GTTooltipHandler.Tier.UXV;
import static gregtech.client.GTTooltipHandler.Tier.ZPM;
import static gregtech.client.GTTooltipHandler.registerTieredTooltip;
import static gregtech.common.items.IDMetaItem03.Activated_Carbon_Filter_Mesh;
import static gregtech.common.items.IDMetaItem03.Circuit_Biomainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_Bioprocessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Biowarecomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Biowaresupercomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Bio;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Bio_Ultra;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Coated_Basic;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Epoxy_Advanced;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Fiberglass_Advanced;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Multifiberglass_Elite;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Optical;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Phenolic_Good;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Plastic;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Plastic_Advanced;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Wetware;
import static gregtech.common.items.IDMetaItem03.Circuit_Board_Wetware_Extreme;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_BioCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_Biocell;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_CPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_CrystalCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_CrystalSoC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_CrystalSoC2;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_HPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_ILC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_LPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_NAND;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_NOR;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_NPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_NanoCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_NeuroCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_Optical;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_PIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_PPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_QPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_QuantumCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_Ram;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_Simple_SoC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_SoC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_SoC2;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_Stemcell;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_UHPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Chip_ULPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_CosmicAssembly;
import static gregtech.common.items.IDMetaItem03.Circuit_CosmicComputer;
import static gregtech.common.items.IDMetaItem03.Circuit_CosmicMainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_CosmicProcessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Crystalcomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Crystalmainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_Crystalprocessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Elitenanocomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_ExoticAssembly;
import static gregtech.common.items.IDMetaItem03.Circuit_ExoticComputer;
import static gregtech.common.items.IDMetaItem03.Circuit_ExoticMainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_ExoticProcessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Integrated_Good;
import static gregtech.common.items.IDMetaItem03.Circuit_Masterquantumcomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Microprocessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Nanocomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Nanoprocessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Neuroprocessor;
import static gregtech.common.items.IDMetaItem03.Circuit_OpticalAssembly;
import static gregtech.common.items.IDMetaItem03.Circuit_OpticalComputer;
import static gregtech.common.items.IDMetaItem03.Circuit_OpticalMainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_OpticalProcessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_CapacitorASMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_CapacitorSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_CapacitorXSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_Chip_Bioware;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_Coil;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_DiodeASMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_DiodeSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_DiodeXSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_GlassFiber;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_Glass_Tube;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_InductorASMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_InductorSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_InductorXSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_PetriDish;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_RawCrystalChip;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_RawCrystalParts;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_Reinforced_Glass_Tube;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_ResistorASMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_ResistorSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_ResistorXSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_TransistorASMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_TransistorSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Parts_TransistorXSMD;
import static gregtech.common.items.IDMetaItem03.Circuit_Processor;
import static gregtech.common.items.IDMetaItem03.Circuit_Quantumcomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Quantummainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_Quantumprocessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Ingot;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Ingot2;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Ingot3;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Ingot4;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Ingot5;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Ingot6;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer2;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer3;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer4;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer5;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer6;
import static gregtech.common.items.IDMetaItem03.Circuit_Silicon_Wafer7;
import static gregtech.common.items.IDMetaItem03.Circuit_TranscendentAssembly;
import static gregtech.common.items.IDMetaItem03.Circuit_TranscendentComputer;
import static gregtech.common.items.IDMetaItem03.Circuit_TranscendentMainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_TranscendentProcessor;
import static gregtech.common.items.IDMetaItem03.Circuit_Ultimatecrystalcomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_Bioware;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_CPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_HPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_ILC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_LPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_NAND;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_NOR;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_NPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_NanoCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_PIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_PPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_QPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_QuantumCPU;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_Ram;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_Simple_SoC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_SoC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_SoC2;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_UHPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wafer_ULPIC;
import static gregtech.common.items.IDMetaItem03.Circuit_Wetwarecomputer;
import static gregtech.common.items.IDMetaItem03.Circuit_Wetwaremainframe;
import static gregtech.common.items.IDMetaItem03.Circuit_Wetwaresupercomputer;
import static gregtech.common.items.IDMetaItem03.Cover_Metrics_Transmitter;
import static gregtech.common.items.IDMetaItem03.Cover_SolarPanel_UEV;
import static gregtech.common.items.IDMetaItem03.Cover_SolarPanel_UHV;
import static gregtech.common.items.IDMetaItem03.Cover_SolarPanel_UIV;
import static gregtech.common.items.IDMetaItem03.EV_Coil;
import static gregtech.common.items.IDMetaItem03.GalliumArsenideCrystal;
import static gregtech.common.items.IDMetaItem03.GalliumArsenideCrystalSmallPart;
import static gregtech.common.items.IDMetaItem03.HV_Coil;
import static gregtech.common.items.IDMetaItem03.IV_Coil;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_AUTOMATION;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_1;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_2;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_3;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_4;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_5;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_6;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_7;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_8;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Acceleration_8_Upgraded;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_COOLER;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_DESERT;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_DRYER;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_FLOWERING;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_Frame;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_HEATER;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_HELL;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_HUMIDIFIER;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_JUNGLE;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_LIFESPAN;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_LIGHT;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_OCEAN;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_PLAINS;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_POLLEN;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_PRODUCTION;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_SEAL;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_SIEVE;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_SKY;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_STABILIZER;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_TERRITORY;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_UNLIGHT;
import static gregtech.common.items.IDMetaItem03.IndustrialApiary_Upgrade_WINTER;
import static gregtech.common.items.IDMetaItem03.KevlarFiber;
import static gregtech.common.items.IDMetaItem03.LV_Coil;
import static gregtech.common.items.IDMetaItem03.LuV_Coil;
import static gregtech.common.items.IDMetaItem03.MV_Coil;
import static gregtech.common.items.IDMetaItem03.NandChip;
import static gregtech.common.items.IDMetaItem03.NuclearStar;
import static gregtech.common.items.IDMetaItem03.Optical_Cpu_Containment_Housing;
import static gregtech.common.items.IDMetaItem03.Optically_Compatible_Memory;
import static gregtech.common.items.IDMetaItem03.Optically_Perfected_CPU;
import static gregtech.common.items.IDMetaItem03.Phononic_Seed_Crystal;
import static gregtech.common.items.IDMetaItem03.Quark_Catalyst_Housing;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Bottom;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Charm;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Down;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Strange;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Top;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Unaligned;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Up;
import static gregtech.common.items.IDMetaItem03.Relativistic_Heat_Capacitor;
import static gregtech.common.items.IDMetaItem03.Spinneret;
import static gregtech.common.items.IDMetaItem03.Thermal_Superconductor;
import static gregtech.common.items.IDMetaItem03.Timepiece;
import static gregtech.common.items.IDMetaItem03.Transdimensional_Alignment_Matrix;
import static gregtech.common.items.IDMetaItem03.Tube_Wires;
import static gregtech.common.items.IDMetaItem03.UHV_Coil;
import static gregtech.common.items.IDMetaItem03.ULV_Coil;
import static gregtech.common.items.IDMetaItem03.UV_Coil;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Axe;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Block;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Bolt;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Bottle;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Casing;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Cell;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_File;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Gear;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Hammer;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Hoe;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Ingot;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Pickaxe;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Pipe_Huge;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Pipe_Large;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Pipe_Medium;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Pipe_Small;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Pipe_Tiny;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Plate;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Ring;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Rod;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Rotor;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Saw;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Shovel;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Small_Gear;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Sword;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_ToolHeadDrill;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Turbine_Blade;
import static gregtech.common.items.IDMetaItem03.White_Dwarf_Shape_Extruder_Wire;
import static gregtech.common.items.IDMetaItem03.WovenKevlar;
import static gregtech.common.items.IDMetaItem03.ZPM_Coil;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.covers.CoverMetricsTransmitter;
import gregtech.common.covers.CoverSolarPanel;

public class MetaGeneratedItem03 extends MetaGeneratedItemX32 {

    public static MetaGeneratedItem03 INSTANCE;

    public MetaGeneratedItem03() {
        super(
            "metaitem.03",
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.nanite,
            OrePrefixes.rawOre);
        INSTANCE = this;
        Object[] o = new Object[0];

        /*
         * circuit boards tier 1-7: coated circuit board / wood plate + resin Plastic Circuit Board / Plastic + Copper
         * Foil + Sulfuric Acid phenolic circuit board /carton+glue+chemical bath epoxy circuit board /epoxy plate +
         * copper foil + sulfuric acid fiberglass circuit board (simple + multilayer) / glass + plastic + electrum foil
         * + sulfuric acid wetware lifesupport board / fiberglass CB + teflon +
         */
        ItemList.Circuit_Board_Wetware.set(
            addItem(Circuit_Board_Wetware.ID, "Wetware Lifesupport Circuit Board", "The Board that keeps life", o));
        ItemList.Circuit_Board_Plastic
            .set(addItem(Circuit_Board_Plastic.ID, "Plastic Circuit Board", "A Good Board", o));
        ItemList.Circuit_Board_Bio
            .set(addItem(Circuit_Board_Bio.ID, "Bio Circuit Board", "Bio genetic mutated Board", o));

        /*
         * electronic components: vacuum tube (glass tube + red alloy cables) basic electronic circuits normal+smd coils
         * diodes normal+smd transistors normal+smd capacitors normal+smd Glass Fibers
         */
        ItemList.Circuit_Parts_ResistorSMD.set(
            addItem(
                Circuit_Parts_ResistorSMD.ID,
                "SMD Resistor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Resistor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_Glass_Tube.set(addItem(Circuit_Parts_Glass_Tube.ID, "Glass Tube", "", o));
        ItemList.Circuit_Parts_Coil.set(addItem(Circuit_Parts_Coil.ID, "Small Coil", "Basic Electronic Component", o));
        ItemList.Circuit_Parts_DiodeSMD.set(
            addItem(
                Circuit_Parts_DiodeSMD.ID,
                "SMD Diode",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Diode),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_TransistorSMD.set(
            addItem(
                Circuit_Parts_TransistorSMD.ID,
                "SMD Transistor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Transistor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_CapacitorSMD.set(
            addItem(
                Circuit_Parts_CapacitorSMD.ID,
                "SMD Capacitor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Capacitor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_GlassFiber
            .set(addItem(Circuit_Parts_GlassFiber.ID, "Glass Fiber", Materials.BorosilicateGlass.mChemicalFormula, o));
        ItemList.Circuit_Parts_PetriDish
            .set(addItem(Circuit_Parts_PetriDish.ID, "Petri Dish", "For cultivating cells", o));
        ItemList.Circuit_Parts_Reinforced_Glass_Tube
            .set(addItem(Circuit_Parts_Reinforced_Glass_Tube.ID, "Reinforced Glass Tube", "", o));

        ItemList.Circuit_Parts_ResistorASMD
            .set(addItem(Circuit_Parts_ResistorASMD.ID, "Advanced SMD Resistor", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_DiodeASMD
            .set(addItem(Circuit_Parts_DiodeASMD.ID, "Advanced SMD Diode", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_TransistorASMD.set(
            addItem(Circuit_Parts_TransistorASMD.ID, "Advanced SMD Transistor", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_CapacitorASMD
            .set(addItem(Circuit_Parts_CapacitorASMD.ID, "Advanced SMD Capacitor", "Advanced Electronic Component", o));

        ItemList.Circuit_Parts_ResistorXSMD.set(
            addItem(Circuit_Parts_ResistorXSMD.ID, "Optical SMD Resistor", "Highly Advanced Electronic Component", o));
        ItemList.Circuit_Parts_DiodeXSMD
            .set(addItem(Circuit_Parts_DiodeXSMD.ID, "Optical SMD Diode", "Highly Advanced Electronic Component", o));
        ItemList.Circuit_Parts_TransistorXSMD.set(
            addItem(
                Circuit_Parts_TransistorXSMD.ID,
                "Optical SMD Transistor",
                "Highly Advanced Electronic Component",
                o));
        ItemList.Circuit_Parts_CapacitorXSMD.set(
            addItem(
                Circuit_Parts_CapacitorXSMD.ID,
                "Optical SMD Capacitor",
                "Highly Advanced Electronic Component",
                o));

        ItemList.Circuit_Parts_InductorSMD.set(
            addItem(
                Circuit_Parts_InductorSMD.ID,
                "SMD Inductor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Inductor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_InductorASMD
            .set(addItem(Circuit_Parts_InductorASMD.ID, "Advanced SMD Inductor", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_InductorXSMD.set(
            addItem(Circuit_Parts_InductorXSMD.ID, "Optical SMD Inductor", "Highly Advanced Electronic Component", o));

        /*
         * ICs Lenses made from perfect crystals first instead of plates Monocrystalline silicon ingot
         * (normal+glowstone+naquadah) EBF, normal silicon no EBF need anymore wafer(normal+glowstone+naquadah) cut mono
         * silicon ingot in cutting machine
         * Integrated Logic Circuit(8bit DIP) RAM NAND Memory NOR Memory CPU (4 sizes) SoCs(2 sizes, high tier cheap low
         * tech component) Power IC/High Power IC/Ultra High power
         * nanotube interconnected circuit (H-IC + nanotubes)
         * quantum chips
         */
        ItemList.Circuit_Silicon_Ingot
            .set(addItem(Circuit_Silicon_Ingot.ID, "Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot2.set(
            addItem(Circuit_Silicon_Ingot2.ID, "Phosphorus doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot3
            .set(addItem(Circuit_Silicon_Ingot3.ID, "Naquadah doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot4
            .set(addItem(Circuit_Silicon_Ingot4.ID, "Europium doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot5
            .set(addItem(Circuit_Silicon_Ingot5.ID, "Americium doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot6
            .set(addItem(Circuit_Silicon_Ingot6.ID, "Optically Enriched Crystalline Boule", "Raw Circuit", o));

        ItemList.Circuit_Silicon_Wafer.set(addItem(Circuit_Silicon_Wafer.ID, "Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer2
            .set(addItem(Circuit_Silicon_Wafer2.ID, "Phosphorus doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer3
            .set(addItem(Circuit_Silicon_Wafer3.ID, "Naquadah doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer4
            .set(addItem(Circuit_Silicon_Wafer4.ID, "Europium doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer5
            .set(addItem(Circuit_Silicon_Wafer5.ID, "Americium doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer6
            .set(addItem(Circuit_Silicon_Wafer6.ID, "Photonically Prepared Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer7
            .set(addItem(Circuit_Silicon_Wafer7.ID, "Photonically Enhanced Wafer", "Raw Circuit", o));

        ItemList.Circuit_Wafer_ILC
            .set(addItem(Circuit_Wafer_ILC.ID, "Integrated Logic Circuit (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_ILC
            .set(addItem(Circuit_Chip_ILC.ID, "Integrated Logic Circuit", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_Ram
            .set(addItem(Circuit_Wafer_Ram.ID, "Random Access Memory Chip (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_Ram
            .set(addItem(Circuit_Chip_Ram.ID, "Random Access Memory Chip", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_NAND.set(addItem(Circuit_Wafer_NAND.ID, "NAND Memory Chip (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_NAND.set(addItem(Circuit_Chip_NAND.ID, "NAND Memory Chip", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_NOR.set(addItem(Circuit_Wafer_NOR.ID, "NOR Memory Chip (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_NOR.set(addItem(Circuit_Chip_NOR.ID, "NOR Memory Chip", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_CPU
            .set(addItem(Circuit_Wafer_CPU.ID, "Central Processing Unit (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_CPU.set(addItem(Circuit_Chip_CPU.ID, "Central Processing Unit", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_SoC.set(addItem(Circuit_Wafer_SoC.ID, "SoC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_SoC.set(addItem(Circuit_Chip_SoC.ID, "SoC", "System on a Chip", o));

        ItemList.Circuit_Wafer_SoC2.set(addItem(Circuit_Wafer_SoC2.ID, "ASoC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_SoC2.set(addItem(Circuit_Chip_SoC2.ID, "ASoC", "Advanced System on a Chip", o));

        ItemList.Circuit_Wafer_PIC.set(addItem(Circuit_Wafer_PIC.ID, "PIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_PIC.set(addItem(Circuit_Chip_PIC.ID, "Power IC", "Power Circuit", o));

        ItemList.Circuit_Wafer_HPIC.set(addItem(Circuit_Wafer_HPIC.ID, "HPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_HPIC.set(addItem(Circuit_Chip_HPIC.ID, "High Power IC", "High Power Circuit", o));

        ItemList.Circuit_Wafer_NanoCPU.set(addItem(Circuit_Wafer_NanoCPU.ID, "NanoCPU Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_NanoCPU
            .set(addItem(Circuit_Chip_NanoCPU.ID, "Nanocomponent Central Processing Unit", "Power Circuit", o));

        ItemList.Circuit_Wafer_QuantumCPU.set(addItem(Circuit_Wafer_QuantumCPU.ID, "QBit Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_QuantumCPU
            .set(addItem(Circuit_Chip_QuantumCPU.ID, "QBit Processing Unit", "Quantum CPU", o));

        ItemList.Circuit_Wafer_UHPIC.set(addItem(Circuit_Wafer_UHPIC.ID, "UHPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_UHPIC
            .set(addItem(Circuit_Chip_UHPIC.ID, "Ultra High Power IC", "Ultra High Power Circuit", o));

        ItemList.Circuit_Wafer_Simple_SoC
            .set(addItem(Circuit_Wafer_Simple_SoC.ID, "Simple SoC Wafer", "Raw Primitive Circuit", o));
        ItemList.Circuit_Chip_Simple_SoC
            .set(addItem(Circuit_Chip_Simple_SoC.ID, "Simple SoC", "Simple System on a Chip", o));

        ItemList.Circuit_Wafer_ULPIC.set(addItem(Circuit_Wafer_ULPIC.ID, "ULPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_ULPIC
            .set(addItem(Circuit_Chip_ULPIC.ID, "Ultra Low Power IC", "Ultra Low Power Circuit", o));

        ItemList.Circuit_Wafer_LPIC.set(addItem(Circuit_Wafer_LPIC.ID, "LPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_LPIC.set(addItem(Circuit_Chip_LPIC.ID, "Low Power IC", "Low Power Circuit", o));

        ItemList.Circuit_Wafer_NPIC.set(addItem(Circuit_Wafer_NPIC.ID, "NPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_NPIC.set(addItem(Circuit_Chip_NPIC.ID, "Nano Power IC", "Nano Power Circuit", o));

        ItemList.Circuit_Wafer_PPIC.set(addItem(Circuit_Wafer_PPIC.ID, "PPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_PPIC.set(addItem(Circuit_Chip_PPIC.ID, "Piko Power IC", "Piko Power Circuit", o));

        ItemList.Circuit_Wafer_QPIC.set(addItem(Circuit_Wafer_QPIC.ID, "QPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_QPIC.set(addItem(Circuit_Chip_QPIC.ID, "Quantum Power IC", "Quantum Power Circuit", o));

        ItemList.Circuit_Wafer_Bioware.set(addItem(Circuit_Wafer_Bioware.ID, "Living Bio Wafer", "Raw Circuit", o));
        ItemList.Circuit_Parts_Chip_Bioware
            .set(addItem(Circuit_Parts_Chip_Bioware.ID, "Living Bio Chip", "Needed for Circuits", o));
        /*
         * Engraved Crystal Chip Engraved Lapotron Chip Crystal CPU SoCrystal stem cells (disassemble eggs)
         */
        ItemList.Circuit_Chip_CrystalSoC2.set(
            addItem(Circuit_Chip_CrystalSoC2.ID, "Raw Advanced Crystal Chip", "Raw Advanced Crystal Processor", o));
        ItemList.Circuit_Parts_RawCrystalChip
            .set(addItem(Circuit_Parts_RawCrystalChip.ID, "Raw Crystal Chip", "Raw Crystal Processor", o));
        ItemList.Circuit_Chip_CrystalCPU
            .set(addItem(Circuit_Chip_CrystalCPU.ID, "Crystal Processing Unit", "Crystal CPU", o)); // Crystal
        // chip
        // elite
        // part
        ItemList.Circuit_Chip_CrystalSoC
            .set(addItem(Circuit_Chip_CrystalSoC.ID, "Crystal SoC", "Crystal System on a Chip", o));
        ItemList.Circuit_Chip_NeuroCPU.set(addItem(Circuit_Chip_NeuroCPU.ID, "Neuro Processing Unit", "Neuro CPU", o));
        ItemList.Circuit_Chip_Stemcell.set(addItem(Circuit_Chip_Stemcell.ID, "Stemcells", "Raw inteligence", o));
        ItemList.Circuit_Parts_RawCrystalParts
            .set(addItem(Circuit_Parts_RawCrystalParts.ID, "Raw Crystal Chip Parts", "Raw Crystal Processor Parts", o));
        ItemList.Circuit_Chip_Biocell.set(addItem(Circuit_Chip_Biocell.ID, "Biocells", "Mutated Raw inteligence", o));
        ItemList.Circuit_Chip_BioCPU.set(addItem(Circuit_Chip_BioCPU.ID, "Bio Processing Unit", "Bio CPU", o));
        ItemList.Circuit_Chip_Optical
            .set(addItem(Circuit_Chip_Optical.ID, "Raw Exposed Optical Chip", "Raw Optical Chip", o));

        // Nand Chip
        ItemList.NandChip.set(
            addItem(
                NandChip.ID,
                "NAND Chip",
                "A very simple Circuit",
                OrePrefixes.circuit.get(Materials.ULV),
                SubTag.NO_UNIFICATION));

        // Vacuum Tube Item01
        // Basic Circuit IC2
        // Good Circuit Item01

        // Integrated Logic Circuit Item01
        ItemList.Circuit_Integrated_Good.set(
            addItem(
                Circuit_Integrated_Good.ID,
                "Good Integrated Circuit",
                "Good Circuit",
                OrePrefixes.circuit.get(Materials.MV),
                SubTag.NO_UNIFICATION));

        // Good Integrated Circuit Item01
        // Advanced Circuit IC2

        ItemList.Circuit_Microprocessor.set(
            addItem(
                Circuit_Microprocessor.ID,
                "Microprocessor",
                "A Basic Circuit",
                OrePrefixes.circuit.get(Materials.LV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Processor.set(
            addItem(
                Circuit_Processor.ID,
                "Integrated Processor",
                "A Good Circuit",
                OrePrefixes.circuit.get(Materials.MV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Nanoprocessor.set(
            addItem(
                Circuit_Nanoprocessor.ID,
                "Nanoprocessor",
                "An Advanced Circuit",
                OrePrefixes.circuit.get(Materials.HV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Nanocomputer.set(
            addItem(
                Circuit_Nanocomputer.ID,
                "Nanoprocessor Assembly",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.EV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Elitenanocomputer.set(
            addItem(
                Circuit_Elitenanocomputer.ID,
                "Elite Nanocomputer",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));

        // Quantum circuits
        ItemList.Circuit_Quantumprocessor.set(
            addItem(
                Circuit_Quantumprocessor.ID,
                "Quantumprocessor",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.EV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Quantumcomputer.set(
            addItem(
                Circuit_Quantumcomputer.ID,
                "Quantumprocessor Assembly",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Masterquantumcomputer.set(
            addItem(
                Circuit_Masterquantumcomputer.ID,
                "Master Quantumcomputer",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Quantummainframe.set(
            addItem(
                Circuit_Quantummainframe.ID,
                "Quantumprocessor Mainframe",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        // Crystal circuits
        ItemList.Circuit_Crystalprocessor.set(
            addItem(
                Circuit_Crystalprocessor.ID,
                "Crystalprocessor",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Crystalcomputer.set(
            addItem(
                Circuit_Crystalcomputer.ID,
                "Crystalprocessor Assembly",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Ultimatecrystalcomputer.set(
            addItem(
                Circuit_Ultimatecrystalcomputer.ID,
                "Ultimate Crystalcomputer",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Crystalmainframe.set(
            addItem(
                Circuit_Crystalmainframe.ID,
                "Crystalprocessor Mainframe",
                "A Super Circuit",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        // ???? Scared to remove.
        ItemList.Circuit_Ultimate.set(ItemList.Circuit_Ultimatecrystalcomputer.get(1L)); // maybe should be removed

        // Wetware circuits
        ItemList.Circuit_Neuroprocessor.set(
            addItem(
                Circuit_Neuroprocessor.ID,
                "Wetwareprocessor",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Wetwarecomputer.set(
            addItem(
                Circuit_Wetwarecomputer.ID,
                "Wetwareprocessor Assembly",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Wetwaresupercomputer.set(
            addItem(
                Circuit_Wetwaresupercomputer.ID,
                "Wetware Supercomputer",
                "A Super Circuit",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Wetwaremainframe.set(
            addItem(
                Circuit_Wetwaremainframe.ID,
                "Wetware Mainframe",
                "An Infinite Circuit",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));

        // Bioware circuits.
        ItemList.Circuit_Bioprocessor.set(
            addItem(
                Circuit_Bioprocessor.ID,
                "Bioprocessor",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Biowarecomputer.set(
            addItem(
                Circuit_Biowarecomputer.ID,
                "Biowareprocessor Assembly",
                "A Super Circuit",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Biowaresupercomputer.set(
            addItem(
                Circuit_Biowaresupercomputer.ID,
                "Bioware Supercomputer",
                "An Infinite Circuit",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Biomainframe.set(
            addItem(
                Circuit_Biomainframe.ID,
                "Bio Mainframe",
                "A Bio Circuit",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Board_Coated_Basic
            .set(addItem(Circuit_Board_Coated_Basic.ID, "Circuit Board", "A basic Circuit Board", o));
        ItemList.Circuit_Board_Phenolic_Good
            .set(addItem(Circuit_Board_Phenolic_Good.ID, "Good Circuit Board", "A good Circuit Board", o));
        ItemList.Circuit_Board_Epoxy_Advanced
            .set(addItem(Circuit_Board_Epoxy_Advanced.ID, "Advanced Circuit Board", "A advanced Circuit Board", o));
        ItemList.Circuit_Board_Fiberglass_Advanced.set(
            addItem(
                Circuit_Board_Fiberglass_Advanced.ID,
                "More Advanced Circuit Board",
                "A more advanced Circuit Board",
                o));
        ItemList.Circuit_Board_Multifiberglass_Elite
            .set(addItem(Circuit_Board_Multifiberglass_Elite.ID, "Elite Circuit Board", "A elite Circuit Board", o));
        ItemList.Circuit_Board_Wetware_Extreme.set(
            addItem(
                Circuit_Board_Wetware_Extreme.ID,
                "Extreme Wetware Lifesupport Circuit Board",
                "The Board that keeps life",
                o));
        ItemList.Circuit_Board_Plastic_Advanced
            .set(addItem(Circuit_Board_Plastic_Advanced.ID, "Plastic Circuit Board", "A good Board", o));
        ItemList.Circuit_Board_Bio_Ultra.set(
            addItem(Circuit_Board_Bio_Ultra.ID, "Ultra Bio Mutated Circuit Board", "Bio genetic mutated Board", o));
        ItemList.Circuit_Board_Optical
            .set(addItem(Circuit_Board_Optical.ID, "Optical Circuit Board", "Optically Infused Board", o));

        // Optical circuits
        ItemList.Circuit_OpticalProcessor.set(
            addItem(
                Circuit_OpticalProcessor.ID,
                "Optical Processor",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_OpticalAssembly.set(
            addItem(
                Circuit_OpticalAssembly.ID,
                "Optical Assembly",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_OpticalComputer.set(
            addItem(
                Circuit_OpticalComputer.ID,
                "Optical Computer",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_OpticalMainframe.set(
            addItem(
                Circuit_OpticalMainframe.ID,
                "Optical Mainframe",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));

        // Exotic circuits
        ItemList.Circuit_ExoticProcessor.set(
            addItem(
                Circuit_ExoticProcessor.ID,
                "Exotic Processor",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_ExoticAssembly.set(
            addItem(
                Circuit_ExoticAssembly.ID,
                "Exotic Assembly",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_ExoticComputer.set(
            addItem(
                Circuit_ExoticComputer.ID,
                "Exotic Computer",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_ExoticMainframe.set(
            addItem(
                Circuit_ExoticMainframe.ID,
                "Exotic Mainframe",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.UMV),
                SubTag.NO_UNIFICATION));

        // Cosmic circuits
        ItemList.Circuit_CosmicProcessor.set(
            addItem(
                Circuit_CosmicProcessor.ID,
                "Cosmic Processor",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_CosmicAssembly.set(
            addItem(
                Circuit_CosmicAssembly.ID,
                "Cosmic Assembly",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_CosmicComputer.set(
            addItem(
                Circuit_CosmicComputer.ID,
                "Cosmic Computer",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.UMV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_CosmicMainframe.set(
            addItem(
                Circuit_CosmicMainframe.ID,
                "Cosmic Mainframe",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.UXV),
                SubTag.NO_UNIFICATION));

        // Transcendent circuits
        ItemList.Circuit_TranscendentProcessor.set(
            addItem(
                Circuit_TranscendentProcessor.ID,
                "Temporally Transcendent Processor",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_TranscendentAssembly.set(
            addItem(
                Circuit_TranscendentAssembly.ID,
                "Temporally Transcendent Assembly",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.UMV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_TranscendentComputer.set(
            addItem(
                Circuit_TranscendentComputer.ID,
                "Temporally Transcendent Computer",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.UXV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_TranscendentMainframe.set(
            addItem(
                Circuit_TranscendentMainframe.ID,
                "Temporally Transcendent Mainframe",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.MAX),
                SubTag.NO_UNIFICATION));

        ItemList.Tube_Wires.set(addItem(Tube_Wires.ID, "Tube Wires", "For the Vacuum Tubes", o));

        ItemList.Cover_SolarPanel_UHV.set(
            addItem(
                Cover_SolarPanel_UHV.ID,
                "Solar Panel (UHV)",
                "Ultimate High Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 128L)));
        ItemList.Cover_SolarPanel_UEV.set(
            addItem(
                Cover_SolarPanel_UEV.ID,
                "Solar Panel (UEV)",
                "Ultimate Extreme Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 256L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 256L)));
        ItemList.Cover_SolarPanel_UIV.set(
            addItem(
                Cover_SolarPanel_UIV.ID,
                "Solar Panel (UIV)",
                "Ultimate Insane Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 512L)));

        ItemList.ULV_Coil.set(addItem(ULV_Coil.ID, "Ultra Low Voltage Coil", "Primitive Coil", o));
        ItemList.LV_Coil.set(addItem(LV_Coil.ID, "Low Voltage Coil", "Basic Coil", o));
        ItemList.MV_Coil.set(addItem(MV_Coil.ID, "Medium Voltage Coil", "Good Coil", o));
        ItemList.HV_Coil.set(addItem(HV_Coil.ID, "High Voltage Coil", "Advanced Coil", o));
        ItemList.EV_Coil.set(addItem(EV_Coil.ID, "Extreme Voltage Coil", "Extreme Coil", o));
        ItemList.IV_Coil.set(addItem(IV_Coil.ID, "Insane Voltage Coil", "Elite Coil", o));
        ItemList.LuV_Coil.set(addItem(LuV_Coil.ID, "Ludicrous Voltage Coil", "Master Coil", o));
        ItemList.ZPM_Coil.set(addItem(ZPM_Coil.ID, "ZPM Voltage Coil", "Ultimate Coil", o));
        ItemList.UV_Coil.set(addItem(UV_Coil.ID, "Ultimate Voltage Coil", "Super Coil", o));
        ItemList.UHV_Coil.set(addItem(UHV_Coil.ID, "Highly Ultimate Voltage Coil", "Infinite Coil", o));

        ItemList.GalliumArsenideCrystal
            .set(addItem(GalliumArsenideCrystal.ID, "Gallium Arsenide Crystal", "For making boules", o));
        ItemList.GalliumArsenideCrystalSmallPart
            .set(addItem(GalliumArsenideCrystalSmallPart.ID, "Small Gallium Arsenide Crystal", "For making boules", o));
        ItemList.KevlarFiber.set(addItem(KevlarFiber.ID, "Kevlar Fiber", "For making Kevlar Plates", o));
        ItemList.WovenKevlar.set(addItem(WovenKevlar.ID, "Woven Kevlar", "For making Kevlar Plates", o));
        ItemList.Spinneret.set(addItem(Spinneret.ID, "Spinneret", "For making Kevlar Fiber", o));

        ItemList.IndustrialApiary_Upgrade_Frame
            .set(addItem(IndustrialApiary_Upgrade_Frame.ID, "Upgrade Frame", "Crafting component", o));

        ItemList.IndustrialApiary_Upgrade_Acceleration_1.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_1.ID,
                "Acceleration Upgrade x2",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 2x acceleration level/n * Energy Consumption +1 AMP LV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_2.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_2.ID,
                "Acceleration Upgrade x4",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 4x acceleration level/n * Energy Consumption +1 AMP MV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_3.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_3.ID,
                "Acceleration Upgrade x8",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 8x acceleration level/n * Energy Consumption +1 AMP HV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_4.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_4.ID,
                "Acceleration Upgrade x16",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 16x acceleration level/n * Energy Consumption +1 AMP EV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_5.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_5.ID,
                "Acceleration Upgrade x32",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 32x acceleration level/n * Energy Consumption +1 AMP IV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_6.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_6.ID,
                "Acceleration Upgrade x64",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 64x acceleration level/n * Energy Consumption +1 AMP LuV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_7.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_7.ID,
                "Acceleration Upgrade x128",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 128x acceleration level/n * Energy Consumption +1 AMP ZPM",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_8.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_8.ID,
                "Acceleration Upgrade x256",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 256x acceleration level/n * Energy Consumption +1 AMP UV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.set(
            addItem(
                IndustrialApiary_Upgrade_Acceleration_8_Upgraded.ID,
                "Upgraded Acceleration Upgrade x256",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 256x acceleration level/n * Will also grant 8x production upgrade/n * Energy Consumption +1 AMP UV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_PRODUCTION.set(
            addItem(
                IndustrialApiary_Upgrade_PRODUCTION.ID,
                "Production Upgrade",
                "Production upgrade for Industrial Apiary/n Maximum Installed: 8/n Increases production modifier by 0.25/n Energy Consumption +40%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_PLAINS.set(
            addItem(
                IndustrialApiary_Upgrade_PLAINS.ID,
                "Plains Emulation Upgrade",
                "Plains emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Plains/n * Energy Consumption +40%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_LIGHT.set(
            addItem(
                IndustrialApiary_Upgrade_LIGHT.ID,
                "Light Upgrade",
                "Light upgrade for Industrial Apiary/n Maximum Installed: 1/n * Internal Lighting/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_FLOWERING.set(
            addItem(
                IndustrialApiary_Upgrade_FLOWERING.ID,
                "Flowering Upgrade",
                "Flowering upgrade for Industrial Apiary/n Maximum Installed: 8/n * Flowering and Pollination +20%/n * Energy Consumption +10%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_WINTER.set(
            addItem(
                IndustrialApiary_Upgrade_WINTER.ID,
                "Winter Emulation Upgrade",
                "Winter emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Taiga/n * Energy Consumption +50%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_DRYER.set(
            addItem(
                IndustrialApiary_Upgrade_DRYER.ID,
                "Dryer Upgrade",
                "Dryer upgrade for Industrial Apiary/n Maximum Installed: 16/n * Humidity -12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_AUTOMATION.set(
            addItem(
                IndustrialApiary_Upgrade_AUTOMATION.ID,
                "Automation Upgrade",
                "Automation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Automation/n * Energy Consumption +10%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.set(
            addItem(
                IndustrialApiary_Upgrade_HUMIDIFIER.ID,
                "Humidifier Upgrade",
                "Humidifier upgrade for Industrial Apiary/n Maximum Installed: 16/n * Humidity +12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_HELL.set(
            addItem(
                IndustrialApiary_Upgrade_HELL.ID,
                "HELL Emulation Upgrade",
                "HELL emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: HELL/n * Energy Consumption +50%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_POLLEN.set(
            addItem(
                IndustrialApiary_Upgrade_POLLEN.ID,
                "Pollen Scrubber Upgrade",
                "Pollen scrubber upgrade for Industrial Apiary/n Maximum Installed: 1/n * Flowering and Pollination -100%/n * Energy Consumption +30%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_DESERT.set(
            addItem(
                IndustrialApiary_Upgrade_DESERT.ID,
                "Desert Emulation Upgrade",
                "Desert emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Desert/n * Energy Consumption +20%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_COOLER.set(
            addItem(
                IndustrialApiary_Upgrade_COOLER.ID,
                "Cooler Upgrade",
                "Cooler upgrade for Industrial Apiary/n Maximum Installed: 16/n * Temperature -12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_LIFESPAN.set(
            addItem(
                IndustrialApiary_Upgrade_LIFESPAN.ID,
                "Lifespan Upgrade",
                "Lifespan upgrade for Industrial Apiary/n Maximum Installed: 4/n * Lifespan -33%/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_SEAL.set(
            addItem(
                IndustrialApiary_Upgrade_SEAL.ID,
                "Seal Upgrade",
                "Seal upgrade for Industrial Apiary/n Maximum Installed: 1/n * Rain Protection/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_STABILIZER.set(
            addItem(
                IndustrialApiary_Upgrade_STABILIZER.ID,
                "Genetic Stabilizer Upgrade",
                "Genetic stabilizer upgrade for Industrial Apiary/n Maximum Installed: 1/n * Genetic Decay -100%/n * Energy Consumption +150%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_JUNGLE.set(
            addItem(
                IndustrialApiary_Upgrade_JUNGLE.ID,
                "Jungle Emulation Upgrade",
                "Jungle emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Jungle/n * Energy Consumption +20%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_TERRITORY.set(
            addItem(
                IndustrialApiary_Upgrade_TERRITORY.ID,
                "Territory Upgrade",
                "Territory upgrade for Industrial Apiary/n Maximum Installed: 4/n * Territory +50%/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_OCEAN.set(
            addItem(
                IndustrialApiary_Upgrade_OCEAN.ID,
                "Ocean Emulation Upgrade",
                "Ocean emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Ocean/n * Energy Consumption +20%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_SKY.set(
            addItem(
                IndustrialApiary_Upgrade_SKY.ID,
                "Open Sky Upgrade",
                "Open sky upgrade for Industrial Apiary/n Maximum Installed: 1/n * Open Sky Simulation/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_HEATER.set(
            addItem(
                IndustrialApiary_Upgrade_HEATER.ID,
                "Heater Upgrade",
                "Heater upgrade for Industrial Apiary/n Maximum Installed: 16/n * Temperature +12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_SIEVE.set(
            addItem(
                IndustrialApiary_Upgrade_SIEVE.ID,
                "Sieve Upgrade",
                "Sieve upgrade for Industrial Apiary/n Maximum Installed: 1/n * Pollen Collection/n * Energy Consumption +25%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_UNLIGHT.set(
            addItem(
                IndustrialApiary_Upgrade_UNLIGHT.ID,
                "Night Upgrade",
                "Night upgrade for Industrial Apiary/n Maximum Installed: 1/n * Internal Darkness/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));

        ItemList.NuclearStar.set(
            addItem(
                NuclearStar.ID,
                "Nuclear Star",
                "By the powers of Greg, I command this star to be really hot.",
                SubTag.NO_UNIFICATION));

        ItemList.Cover_Metrics_Transmitter.set(
            addItem(
                Cover_Metrics_Transmitter.ID,
                "Metrics Transmitter Cover",
                String.join(
                    "/n ",
                    "Taking Information Panels to the next level!",
                    "Creates a GregTech Advanced Sensor Card when attached",
                    "Works across dimensions or if machine is dismantled",
                    "Removing this cover will destroy the linked card",
                    GTValues.AuthorQuerns)));

        ItemList.ActivatedCarbonFilterMesh.set(
            addItem(
                Activated_Carbon_Filter_Mesh.ID,
                "Activated Carbon Filter Mesh",
                "The most granular filter you could possibly make.",
                SubTag.NO_UNIFICATION));

        ItemList.Quark_Catalyst_Housing.set(
            addItem(
                Quark_Catalyst_Housing.ID,
                "Empty Quark Release Catalyst Housing",
                "Capable of holding Quark Release Catalysts",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Up.set(
            addItem(
                Quark_Creation_Catalyst_Up.ID,
                "Up-Quark Releasing Catalyst",
                "Can release up-quarks into environment to reshape matter",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Down.set(
            addItem(
                Quark_Creation_Catalyst_Down.ID,
                "Down-Quark Releasing Catalyst",
                "Can release down-quarks into environment to reshape matter",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Strange.set(
            addItem(
                Quark_Creation_Catalyst_Strange.ID,
                "Strange-Quark Releasing Catalyst",
                "Can release strange-quarks into environment to reshape matter",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Charm.set(
            addItem(
                Quark_Creation_Catalyst_Charm.ID,
                "Charm-Quark Releasing Catalyst",
                "Can release charm-quarks into environment to reshape matter",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Bottom.set(
            addItem(
                Quark_Creation_Catalyst_Bottom.ID,
                "Bottom-Quark Releasing Catalyst",
                "Can release top-quarks into environment to reshape matter",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Top.set(
            addItem(
                Quark_Creation_Catalyst_Top.ID,
                "Top-Quark Releasing Catalyst",
                "Can release bottom-quarks into environment to reshape matter",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Unaligned.set(
            addItem(
                Quark_Creation_Catalyst_Unaligned.ID,
                "Unaligned Quark Releasing Catalyst",
                "Needs to be realigned before use",
                SubTag.NO_UNIFICATION));

        ItemList.Optical_Cpu_Containment_Housing
            .set(addItem(Optical_Cpu_Containment_Housing.ID, "Optical CPU Containment Housing", "CPU Housing", o));
        ItemList.Optically_Perfected_CPU
            .set(addItem(Optically_Perfected_CPU.ID, "Optically Perfected CPU", "Perfected CPU!", o));
        ItemList.Optically_Compatible_Memory
            .set(addItem(Optically_Compatible_Memory.ID, "Optically Compatible Memory", "Its in the name!", o));

        ItemList.Timepiece.set(addItem(Timepiece.ID, "Timepiece", "Beware of the kid with the hat", o));
        ItemList.Transdimensional_Alignment_Matrix.set(
            addItem(
                Transdimensional_Alignment_Matrix.ID,
                "Transdimensional Alignment Matrix",
                String.join(
                    "/n ",
                    "Device supporting the alignment of transdimensional spaces.",
                    "Enables Dimensional Convergence on the DTPF."),
                o));

        ItemList.Thermal_Superconductor
            .set(addItem(Thermal_Superconductor.ID, "Thermal Superconductor", "With the power of second sound!", o));
        ItemList.Relativistic_Heat_Capacitor
            .set(addItem(Relativistic_Heat_Capacitor.ID, "Relativistic Heat Capacitor", "Thermal Resonance?", o));
        ItemList.Phononic_Seed_Crystal
            .set(addItem(Phononic_Seed_Crystal.ID, "Phononic Seed Crystal", "Perfect Thermal Conductance", o));

        ItemList.White_Dwarf_Shape_Extruder_Plate.set(
            addItem(
                White_Dwarf_Shape_Extruder_Plate.ID,
                "White Dwarf Extruder Shape (Plate)",
                "Ultra Strong Stellar Shape for making Plates"));
        ItemList.White_Dwarf_Shape_Extruder_Rod.set(
            addItem(
                White_Dwarf_Shape_Extruder_Rod.ID,
                "White Dwarf Extruder Shape (Rod)",
                "Ultra Strong Stellar Shape for making Rods"));
        ItemList.White_Dwarf_Shape_Extruder_Bolt.set(
            addItem(
                White_Dwarf_Shape_Extruder_Bolt.ID,
                "White Dwarf Extruder Shape (Bolt)",
                "Ultra Strong Stellar Shape for making Bolts"));
        ItemList.White_Dwarf_Shape_Extruder_Ring.set(
            addItem(
                White_Dwarf_Shape_Extruder_Ring.ID,
                "White Dwarf Extruder Shape (Ring)",
                "Ultra Strong Stellar Shape for making Rings"));
        ItemList.White_Dwarf_Shape_Extruder_Cell.set(
            addItem(
                White_Dwarf_Shape_Extruder_Cell.ID,
                "White Dwarf Extruder Shape (Cell)",
                "Ultra Strong Stellar Shape for making Cells"));
        ItemList.White_Dwarf_Shape_Extruder_Ingot.set(
            addItem(
                White_Dwarf_Shape_Extruder_Ingot.ID,
                "White Dwarf Extruder Shape (Ingot)",
                "Ultra Strong Stellar Shape for, wait, can't we just use a Furnace?"));
        ItemList.White_Dwarf_Shape_Extruder_Wire.set(
            addItem(
                White_Dwarf_Shape_Extruder_Wire.ID,
                "White Dwarf Extruder Shape (Wire)",
                "Ultra Strong Stellar Shape for making Wires"));
        ItemList.White_Dwarf_Shape_Extruder_Casing.set(
            addItem(
                White_Dwarf_Shape_Extruder_Casing.ID,
                "White Dwarf Extruder Shape (Casing)",
                "Ultra Strong Stellar Shape for making Item Casings"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Tiny.set(
            addItem(
                White_Dwarf_Shape_Extruder_Pipe_Tiny.ID,
                "White Dwarf Extruder Shape (Tiny Pipe)",
                "Ultra Strong Stellar Shape for making tiny Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Small.set(
            addItem(
                White_Dwarf_Shape_Extruder_Pipe_Small.ID,
                "White Dwarf Extruder Shape (Small Pipe)",
                "Ultra Strong Stellar Shape for making small Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Medium.set(
            addItem(
                White_Dwarf_Shape_Extruder_Pipe_Medium.ID,
                "White Dwarf Extruder Shape (Normal Pipe)",
                "Ultra Strong Stellar Shape for making Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Large.set(
            addItem(
                White_Dwarf_Shape_Extruder_Pipe_Large.ID,
                "White Dwarf Extruder Shape (Large Pipe)",
                "Ultra Strong Stellar Shape for making large Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Huge.set(
            addItem(
                White_Dwarf_Shape_Extruder_Pipe_Huge.ID,
                "White Dwarf Extruder Shape (Huge Pipe)",
                "Ultra Strong Stellar Shape for making full Block Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Block.set(
            addItem(
                White_Dwarf_Shape_Extruder_Block.ID,
                "White Dwarf Extruder Shape (Block)",
                "Ultra Strong Stellar Shape for making Blocks"));
        ItemList.White_Dwarf_Shape_Extruder_Sword.set(
            addItem(
                White_Dwarf_Shape_Extruder_Sword.ID,
                "White Dwarf Extruder Shape (Sword Blade)",
                "Ultra Strong Stellar Shape for making Swords"));
        ItemList.White_Dwarf_Shape_Extruder_Pickaxe.set(
            addItem(
                White_Dwarf_Shape_Extruder_Pickaxe.ID,
                "White Dwarf Extruder Shape (Pickaxe Head)",
                "Ultra Strong Stellar Shape for making Pickaxes"));
        ItemList.White_Dwarf_Shape_Extruder_Shovel.set(
            addItem(
                White_Dwarf_Shape_Extruder_Shovel.ID,
                "White Dwarf Extruder Shape (Shovel Head)",
                "Ultra Strong Stellar Shape for making Shovels"));
        ItemList.White_Dwarf_Shape_Extruder_Axe.set(
            addItem(
                White_Dwarf_Shape_Extruder_Axe.ID,
                "White Dwarf Extruder Shape (Axe Head)",
                "Ultra Strong Stellar Shape for making Axes"));
        ItemList.White_Dwarf_Shape_Extruder_Hoe.set(
            addItem(
                White_Dwarf_Shape_Extruder_Hoe.ID,
                "White Dwarf Extruder Shape (Hoe Head)",
                "Ultra Strong Stellar Shape for making Hoes"));
        ItemList.White_Dwarf_Shape_Extruder_Hammer.set(
            addItem(
                White_Dwarf_Shape_Extruder_Hammer.ID,
                "White Dwarf Extruder Shape (Hammer Head)",
                "Ultra Strong Stellar Shape for making Hammers"));
        ItemList.White_Dwarf_Shape_Extruder_File.set(
            addItem(
                White_Dwarf_Shape_Extruder_File.ID,
                "White Dwarf Extruder Shape (File Head)",
                "Ultra Strong Stellar Shape for making Files"));
        ItemList.White_Dwarf_Shape_Extruder_Saw.set(
            addItem(
                White_Dwarf_Shape_Extruder_Saw.ID,
                "White Dwarf Extruder Shape (Saw Blade)",
                "Ultra Strong Stellar Shape for making Saws"));
        ItemList.White_Dwarf_Shape_Extruder_Gear.set(
            addItem(
                White_Dwarf_Shape_Extruder_Gear.ID,
                "White Dwarf Extruder Shape (Gear)",
                "Ultra Strong Stellar Shape for making Gears"));
        ItemList.White_Dwarf_Shape_Extruder_Bottle.set(
            addItem(
                White_Dwarf_Shape_Extruder_Bottle.ID,
                "White Dwarf Extruder Shape (Bottle)",
                "Ultra Strong Stellar Shape for making Bottles"));
        ItemList.White_Dwarf_Shape_Extruder_Rotor.set(
            addItem(
                White_Dwarf_Shape_Extruder_Rotor.ID,
                "White Dwarf Extruder Shape (Rotor)",
                "Ultra Strong Stellar Shape for a Rotor"));
        ItemList.White_Dwarf_Shape_Extruder_Small_Gear.set(
            addItem(
                White_Dwarf_Shape_Extruder_Small_Gear.ID,
                "White Dwarf Extruder Shape (Small Gear)",
                "Ultra Strong Stellar Shape for a Small Gear"));
        ItemList.White_Dwarf_Shape_Extruder_Turbine_Blade.set(
            addItem(
                White_Dwarf_Shape_Extruder_Turbine_Blade.ID,
                "White Dwarf Extruder Shape (Turbine Blade)",
                "Ultra Strong Stellar Extruder Shape for a Turbine Blade"));
        ItemList.White_Dwarf_Shape_Extruder_ToolHeadDrill.set(
            addItem(
                White_Dwarf_Shape_Extruder_ToolHeadDrill.ID,
                "White Dwarf Extruder Shape (Drill Head)",
                "White Dwarf Extruder Shape for a Drill Head"));
        registerAllTieredTooltips();
        registerAllAnimatedTooltips();
        initOrePrefixes();
        initOreDictUnificatorEntries();
        registerCovers();
    }

    private void registerAllTieredTooltips() {
        registerTieredTooltip(ItemList.NandChip.get(1), ULV);
        registerTieredTooltip(ItemList.Circuit_Integrated_Good.get(1), MV);
        registerTieredTooltip(ItemList.Circuit_Microprocessor.get(1), LV);
        registerTieredTooltip(ItemList.Circuit_Processor.get(1), MV);
        registerTieredTooltip(ItemList.Circuit_Nanoprocessor.get(1), HV);
        registerTieredTooltip(ItemList.Circuit_Nanocomputer.get(1), EV);
        registerTieredTooltip(ItemList.Circuit_Elitenanocomputer.get(1), IV);
        registerTieredTooltip(ItemList.Circuit_Quantumprocessor.get(1), EV);
        registerTieredTooltip(ItemList.Circuit_Quantumcomputer.get(1), IV);
        registerTieredTooltip(ItemList.Circuit_Masterquantumcomputer.get(1), LuV);
        registerTieredTooltip(ItemList.Circuit_Quantummainframe.get(1), ZPM);
        registerTieredTooltip(ItemList.Circuit_Crystalprocessor.get(1), IV);
        registerTieredTooltip(ItemList.Circuit_Crystalcomputer.get(1), LuV);
        registerTieredTooltip(ItemList.Circuit_Ultimatecrystalcomputer.get(1), ZPM);
        registerTieredTooltip(ItemList.Circuit_Crystalmainframe.get(1), UV);
        registerTieredTooltip(ItemList.Circuit_Neuroprocessor.get(1), LuV);
        registerTieredTooltip(ItemList.Circuit_Wetwarecomputer.get(1), ZPM);
        registerTieredTooltip(ItemList.Circuit_Wetwaresupercomputer.get(1), UV);
        registerTieredTooltip(ItemList.Circuit_Wetwaremainframe.get(1), UHV);
        registerTieredTooltip(ItemList.Circuit_Bioprocessor.get(1), ZPM);
        registerTieredTooltip(ItemList.Circuit_Biowarecomputer.get(1), UV);
        registerTieredTooltip(ItemList.Circuit_Biowaresupercomputer.get(1), UHV);
        registerTieredTooltip(ItemList.Circuit_Biomainframe.get(1), UEV);
        registerTieredTooltip(ItemList.Circuit_OpticalProcessor.get(1), UV);
        registerTieredTooltip(ItemList.Circuit_OpticalAssembly.get(1), UHV);
        registerTieredTooltip(ItemList.Circuit_OpticalComputer.get(1), UEV);
        registerTieredTooltip(ItemList.Circuit_OpticalMainframe.get(1), UIV);
        registerTieredTooltip(ItemList.Circuit_ExoticProcessor.get(1), UHV);
        registerTieredTooltip(ItemList.Circuit_ExoticAssembly.get(1), UEV);
        registerTieredTooltip(ItemList.Circuit_ExoticComputer.get(1), UIV);
        registerTieredTooltip(ItemList.Circuit_ExoticMainframe.get(1), UMV);
        registerTieredTooltip(ItemList.Circuit_CosmicProcessor.get(1), UEV);
        registerTieredTooltip(ItemList.Circuit_CosmicAssembly.get(1), UIV);
        registerTieredTooltip(ItemList.Circuit_CosmicComputer.get(1), UMV);
        registerTieredTooltip(ItemList.Circuit_CosmicMainframe.get(1), UXV);
        registerTieredTooltip(ItemList.Circuit_TranscendentProcessor.get(1), UIV);
        registerTieredTooltip(ItemList.Circuit_TranscendentAssembly.get(1), UMV);
        registerTieredTooltip(ItemList.Circuit_TranscendentComputer.get(1), UXV);
        registerTieredTooltip(ItemList.Circuit_TranscendentMainframe.get(1), MAX);
    }

    private void registerAllAnimatedTooltips() {
        addItemTooltip(ItemList.Transdimensional_Alignment_Matrix.get(1), GTValues.AuthorCloud);
    }

    private void initOreDictUnificatorEntries() {
        GTOreDictUnificator
            .set(OrePrefixes.componentCircuit, Materials.Resistor, ItemList.Circuit_Parts_Resistor.get(1L));
        GTOreDictUnificator.set(OrePrefixes.componentCircuit, Materials.Diode, ItemList.Circuit_Parts_Diode.get(1L));
        GTOreDictUnificator
            .set(OrePrefixes.componentCircuit, Materials.Transistor, ItemList.Circuit_Parts_Transistor.get(1L));
        GTOreDictUnificator.set(OrePrefixes.componentCircuit, Materials.Inductor, ItemList.Circuit_Parts_Coil.get(1L));
        GTOreDictUnificator
            .set(OrePrefixes.componentCircuit, Materials.Capacitor, ItemList.Circuit_Parts_Capacitor.get(1L));

        GTOreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Resistor,
            ItemList.Circuit_Parts_ResistorSMD.get(1L),
            true);
        GTOreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Diode,
            ItemList.Circuit_Parts_DiodeSMD.get(1L),
            true);
        GTOreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Transistor,
            ItemList.Circuit_Parts_TransistorSMD.get(1L),
            true);
        GTOreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Capacitor,
            ItemList.Circuit_Parts_CapacitorSMD.get(1L),
            true);
        GTOreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Inductor,
            ItemList.Circuit_Parts_InductorSMD.get(1L),
            true);

    }

    private void initOrePrefixes() {
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_1.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_2.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_3.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_4.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_5.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_6.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_7.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_8.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_PRODUCTION.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_PLAINS.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_LIGHT.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_FLOWERING.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_WINTER.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_DRYER.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_AUTOMATION.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_HELL.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_POLLEN.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_DESERT.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_COOLER.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_LIFESPAN.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_SEAL.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_STABILIZER.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_JUNGLE.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_TERRITORY.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_OCEAN.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_SKY.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_HEATER.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_SIEVE.get(1L));
        OrePrefixes.apiaryUpgrade.add(ItemList.IndustrialApiary_Upgrade_UNLIGHT.get(1L));
    }

    private void registerCovers() {
        GregTechAPI.registerCover(
            ItemList.Cover_Metrics_Transmitter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_METRICS_TRANSMITTER)),
            new CoverMetricsTransmitter(TextureFactory.of(OVERLAY_METRICS_TRANSMITTER)));
        GregTechAPI.registerCover(
            ItemList.Cover_SolarPanel_UHV.get(1L),
            TextureFactory.of(SOLARPANEL_UHV),
            new CoverSolarPanel(2097152));
        GregTechAPI.registerCover(
            ItemList.Cover_SolarPanel_UEV.get(1L),
            TextureFactory.of(SOLARPANEL_UEV),
            new CoverSolarPanel(8388608));
        GregTechAPI.registerCover(
            ItemList.Cover_SolarPanel_UIV.get(1L),
            TextureFactory.of(SOLARPANEL_UIV),
            new CoverSolarPanel(33554432));
    }

    @Override
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return aDoShowAllItems || (aPrefix.toString()
            .toLowerCase()
            .contains("nanite"))
            || (aPrefix.toString()
                .toLowerCase()
                .contains("rawore"));
    }
}
