package gregtech.common.items;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_METRICS_TRANSMITTER;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UEV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UHV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UIV;
import static gregtech.client.GT_TooltipHandler.Tier.EV;
import static gregtech.client.GT_TooltipHandler.Tier.HV;
import static gregtech.client.GT_TooltipHandler.Tier.IV;
import static gregtech.client.GT_TooltipHandler.Tier.LV;
import static gregtech.client.GT_TooltipHandler.Tier.LuV;
import static gregtech.client.GT_TooltipHandler.Tier.MAX;
import static gregtech.client.GT_TooltipHandler.Tier.MV;
import static gregtech.client.GT_TooltipHandler.Tier.UEV;
import static gregtech.client.GT_TooltipHandler.Tier.UHV;
import static gregtech.client.GT_TooltipHandler.Tier.UIV;
import static gregtech.client.GT_TooltipHandler.Tier.ULV;
import static gregtech.client.GT_TooltipHandler.Tier.UMV;
import static gregtech.client.GT_TooltipHandler.Tier.UV;
import static gregtech.client.GT_TooltipHandler.Tier.UXV;
import static gregtech.client.GT_TooltipHandler.Tier.ZPM;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.items.GT_MetaGenerated_Item_X32;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.covers.GT_Cover_Metrics_Transmitter;
import gregtech.common.covers.GT_Cover_SolarPanel;

public class GT_MetaGenerated_Item_03 extends GT_MetaGenerated_Item_X32 {

    public static GT_MetaGenerated_Item_03 INSTANCE;

    public GT_MetaGenerated_Item_03() {
        super(
            "metaitem.03",
            OrePrefixes.crateGtDust,
            OrePrefixes.crateGtIngot,
            OrePrefixes.crateGtGem,
            OrePrefixes.crateGtPlate,
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
        ItemList.Circuit_Board_Wetware
            .set(addItem(6, "Wetware Lifesupport Circuit Board", "The Board that keeps life", o));
        ItemList.Circuit_Board_Plastic.set(addItem(7, "Plastic Circuit Board", "A Good Board", o));
        ItemList.Circuit_Board_Bio.set(addItem(8, "Bio Circuit Board", "Bio genetic mutated Board", o));

        /*
         * electronic components: vacuum tube (glass tube + red alloy cables) basic electronic circuits normal+smd coils
         * diodes normal+smd transistors normal+smd capacitors normal+smd Glass Fibers
         */
        ItemList.Circuit_Parts_ResistorSMD.set(
            addItem(
                11,
                "SMD Resistor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Resistor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_Glass_Tube.set(addItem(12, "Glass Tube", "", o));
        ItemList.Circuit_Parts_Coil.set(addItem(14, "Small Coil", "Basic Electronic Component", o));
        ItemList.Circuit_Parts_DiodeSMD.set(
            addItem(
                16,
                "SMD Diode",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Diode),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_TransistorSMD.set(
            addItem(
                18,
                "SMD Transistor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Transistor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_CapacitorSMD.set(
            addItem(
                20,
                "SMD Capacitor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Capacitor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_GlassFiber
            .set(addItem(21, "Glass Fiber", Materials.BorosilicateGlass.mChemicalFormula, o));
        ItemList.Circuit_Parts_PetriDish.set(addItem(22, "Petri Dish", "For cultivating cells", o));
        ItemList.Circuit_Parts_Reinforced_Glass_Tube.set(addItem(23, "Reinforced Glass Tube", "", o));

        ItemList.Circuit_Parts_ResistorASMD
            .set(addItem(24, "Advanced SMD Resistor", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_DiodeASMD.set(addItem(25, "Advanced SMD Diode", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_TransistorASMD
            .set(addItem(26, "Advanced SMD Transistor", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_CapacitorASMD
            .set(addItem(27, "Advanced SMD Capacitor", "Advanced Electronic Component", o));

        ItemList.Circuit_Parts_ResistorXSMD
            .set(addItem(178, "Optical SMD Resistor", "Highly Advanced Electronic Component", o));
        ItemList.Circuit_Parts_DiodeXSMD
            .set(addItem(179, "Optical SMD Diode", "Highly Advanced Electronic Component", o));
        ItemList.Circuit_Parts_TransistorXSMD
            .set(addItem(180, "Optical SMD Transistor", "Highly Advanced Electronic Component", o));
        ItemList.Circuit_Parts_CapacitorXSMD
            .set(addItem(181, "Optical SMD Capacitor", "Highly Advanced Electronic Component", o));

        ItemList.Circuit_Parts_InductorSMD.set(
            addItem(
                182,
                "SMD Inductor",
                "Electronic Component",
                OrePrefixes.componentCircuit.get(Materials.Inductor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_InductorASMD
            .set(addItem(183, "Advanced SMD Inductor", "Advanced Electronic Component", o));
        ItemList.Circuit_Parts_InductorXSMD
            .set(addItem(184, "Optical SMD Inductor", "Highly Advanced Electronic Component", o));

        GT_OreDictUnificator
            .set(OrePrefixes.componentCircuit, Materials.Resistor, ItemList.Circuit_Parts_Resistor.get(1L));
        GT_OreDictUnificator.set(OrePrefixes.componentCircuit, Materials.Diode, ItemList.Circuit_Parts_Diode.get(1L));
        GT_OreDictUnificator
            .set(OrePrefixes.componentCircuit, Materials.Transistor, ItemList.Circuit_Parts_Transistor.get(1L));
        GT_OreDictUnificator.set(OrePrefixes.componentCircuit, Materials.Inductor, ItemList.Circuit_Parts_Coil.get(1L));
        GT_OreDictUnificator
            .set(OrePrefixes.componentCircuit, Materials.Capacitor, ItemList.Circuit_Parts_Capacitor.get(1L));

        GT_OreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Resistor,
            ItemList.Circuit_Parts_ResistorSMD.get(1L),
            true);
        GT_OreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Diode,
            ItemList.Circuit_Parts_DiodeSMD.get(1L),
            true);
        GT_OreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Transistor,
            ItemList.Circuit_Parts_TransistorSMD.get(1L),
            true);
        GT_OreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Capacitor,
            ItemList.Circuit_Parts_CapacitorSMD.get(1L),
            true);
        GT_OreDictUnificator.addAssociation(
            OrePrefixes.componentCircuit,
            Materials.Inductor,
            ItemList.Circuit_Parts_InductorSMD.get(1L),
            true);

        /*
         * ICs Lenses made from perfect crystals first instead of plates Monocrystalline silicon ingot
         * (normal+glowstone+naquadah) EBF, normal silicon no EBF need anymore wafer(normal+glowstone+naquadah) cut mono
         * silicon ingot in cutting machine
         * Integrated Logic Circuit(8bit DIP) RAM NAND Memory NOR Memory CPU (4 sizes) SoCs(2 sizes, high tier cheap low
         * tech component) Power IC/High Power IC/Ultra High power
         * nanotube interconnected circuit (H-IC + nanotubes)
         * quantum chips
         */
        ItemList.Circuit_Silicon_Ingot.set(addItem(30, "Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot2
            .set(addItem(31, "Phosphorus doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot3
            .set(addItem(32, "Naquadah doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot4
            .set(addItem(150, "Europium doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot5
            .set(addItem(152, "Americium doped Monocrystalline Silicon Boule", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Ingot6.set(addItem(721, "Optically Enriched Crystalline Boule", "Raw Circuit", o));

        ItemList.Circuit_Silicon_Wafer.set(addItem(33, "Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer2.set(addItem(34, "Phosphorus doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer3.set(addItem(35, "Naquadah doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer4.set(addItem(151, "Europium doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer5.set(addItem(153, "Americium doped Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer6.set(addItem(722, "Photonically Prepared Wafer", "Raw Circuit", o));
        ItemList.Circuit_Silicon_Wafer7.set(addItem(723, "Photonically Enhanced Wafer", "Raw Circuit", o));

        ItemList.Circuit_Wafer_ILC.set(addItem(36, "Integrated Logic Circuit (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_ILC.set(addItem(37, "Integrated Logic Circuit", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_Ram.set(addItem(38, "Random Access Memory Chip (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_Ram.set(addItem(39, "Random Access Memory Chip", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_NAND.set(addItem(40, "NAND Memory Chip (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_NAND.set(addItem(41, "NAND Memory Chip", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_NOR.set(addItem(42, "NOR Memory Chip (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_NOR.set(addItem(43, "NOR Memory Chip", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_CPU.set(addItem(44, "Central Processing Unit (Wafer)", "Raw Circuit", o));
        ItemList.Circuit_Chip_CPU.set(addItem(45, "Central Processing Unit", "Integrated Circuit", o));

        ItemList.Circuit_Wafer_SoC.set(addItem(46, "SoC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_SoC.set(addItem(47, "SoC", "System on a Chip", o));

        ItemList.Circuit_Wafer_SoC2.set(addItem(48, "ASoC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_SoC2.set(addItem(49, "ASoC", "Advanced System on a Chip", o));

        ItemList.Circuit_Wafer_PIC.set(addItem(50, "PIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_PIC.set(addItem(51, "Power IC", "Power Circuit", o));

        ItemList.Circuit_Wafer_HPIC.set(addItem(52, "HPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_HPIC.set(addItem(53, "High Power IC", "High Power Circuit", o));

        ItemList.Circuit_Wafer_NanoCPU.set(addItem(54, "NanoCPU Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_NanoCPU.set(addItem(55, "Nanocomponent Central Processing Unit", "Power Circuit", o));

        ItemList.Circuit_Wafer_QuantumCPU.set(addItem(56, "QBit Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_QuantumCPU.set(addItem(57, "QBit Processing Unit", "Quantum CPU", o));

        ItemList.Circuit_Wafer_UHPIC.set(addItem(58, "UHPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_UHPIC.set(addItem(59, "Ultra High Power IC", "Ultra High Power Circuit", o));

        ItemList.Circuit_Wafer_Simple_SoC.set(addItem(60, "Simple SoC Wafer", "Raw Primitive Circuit", o));
        ItemList.Circuit_Chip_Simple_SoC.set(addItem(61, "Simple SoC", "Simple System on a Chip", o));

        ItemList.Circuit_Wafer_ULPIC.set(addItem(62, "ULPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_ULPIC.set(addItem(63, "Ultra Low Power IC", "Ultra Low Power Circuit", o));

        ItemList.Circuit_Wafer_LPIC.set(addItem(64, "LPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_LPIC.set(addItem(65, "Low Power IC", "Low Power Circuit", o));

        ItemList.Circuit_Wafer_NPIC.set(addItem(160, "NPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_NPIC.set(addItem(161, "Nano Power IC", "Nano Power Circuit", o));

        ItemList.Circuit_Wafer_PPIC.set(addItem(162, "PPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_PPIC.set(addItem(163, "Piko Power IC", "Piko Power Circuit", o));

        ItemList.Circuit_Wafer_QPIC.set(addItem(164, "QPIC Wafer", "Raw Circuit", o));
        ItemList.Circuit_Chip_QPIC.set(addItem(165, "Quantum Power IC", "Quantum Power Circuit", o));

        ItemList.Circuit_Wafer_Bioware.set(addItem(188, "Living Bio Wafer", "Raw Circuit", o));
        ItemList.Circuit_Parts_Chip_Bioware.set(addItem(189, "Living Bio Chip", "Needed for Circuits", o));
        /*
         * Engraved Crystal Chip Engraved Lapotron Chip Crystal CPU SoCrystal stem cells (disassemble eggs)
         */
        ItemList.Circuit_Chip_CrystalSoC2
            .set(addItem(68, "Raw Advanced Crystal Chip", "Raw Advanced Crystal Processor", o));
        ItemList.Circuit_Parts_RawCrystalChip.set(addItem(69, "Raw Crystal Chip", "Raw Crystal Processor", o));
        ItemList.Circuit_Chip_CrystalCPU.set(addItem(70, "Crystal Processing Unit", "Crystal CPU", o)); // Crystal
                                                                                                        // chip
                                                                                                        // elite
                                                                                                        // part
        ItemList.Circuit_Chip_CrystalSoC.set(addItem(71, "Crystal SoC", "Crystal System on a Chip", o));
        ItemList.Circuit_Chip_NeuroCPU.set(addItem(72, "Neuro Processing Unit", "Neuro CPU", o));
        ItemList.Circuit_Chip_Stemcell.set(addItem(73, "Stemcells", "Raw inteligence", o));
        ItemList.Circuit_Parts_RawCrystalParts
            .set(addItem(74, "Raw Crystal Chip Parts", "Raw Crystal Processor Parts", o));
        ItemList.Circuit_Chip_Biocell.set(addItem(76, "Biocells", "Mutated Raw inteligence", o));
        ItemList.Circuit_Chip_BioCPU.set(addItem(77, "Bio Processing Unit", "Bio CPU", o));
        ItemList.Circuit_Chip_Optical.set(addItem(724, "Raw Exposed Optical Chip", "Raw Optical Chip", o));

        // Nand Chip
        ItemList.NandChip.set(
            addItem(
                75,
                "NAND Chip",
                "A very simple Circuit",
                OrePrefixes.circuit.get(Materials.Primitive),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.NandChip.get(1), ULV);
        // Vacuum Tube Item01
        // Basic Circuit IC2
        // Good Circuit Item01

        // Integrated Logic Circuit Item01
        ItemList.Circuit_Integrated_Good.set(
            addItem(
                79,
                "Good Integrated Circuit",
                "Good Circuit",
                OrePrefixes.circuit.get(Materials.Good),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Integrated_Good.get(1), MV);
        // Good Integrated Circuit Item01
        // Advanced Circuit IC2

        ItemList.Circuit_Microprocessor.set(
            addItem(
                78,
                "Microprocessor",
                "A Basic Circuit",
                OrePrefixes.circuit.get(Materials.Basic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Microprocessor.get(1), LV);
        ItemList.Circuit_Processor.set(
            addItem(
                80,
                "Integrated Processor",
                "A Good Circuit",
                OrePrefixes.circuit.get(Materials.Good),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Processor.get(1), MV);
        // ItemList.Circuit_Computer.set(addItem(tLastID = 81, "Processor Assembly", "Advanced Circuit", new
        // Object[]{OrePrefixes.circuit.get(Materials.Advanced), SubTag.NO_UNIFICATION}));
        // Workstation/ Item01 Datacircuit
        // Mainframe Item01 DataProcessor

        ItemList.Circuit_Nanoprocessor.set(
            addItem(
                82,
                "Nanoprocessor",
                "An Advanced Circuit",
                OrePrefixes.circuit.get(Materials.Advanced),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Nanoprocessor.get(1), HV);
        ItemList.Circuit_Nanocomputer.set(
            addItem(
                83,
                "Nanoprocessor Assembly",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.Data),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Nanocomputer.get(1), EV);
        ItemList.Circuit_Elitenanocomputer.set(
            addItem(
                84,
                "Elite Nanocomputer",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.Elite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Elitenanocomputer.get(1), IV);
        // Nanoprocessor Mainframe Item01 Energy Flow Circuit

        // Quantum circuits
        ItemList.Circuit_Quantumprocessor.set(
            addItem(
                85,
                "Quantumprocessor",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.Data),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Quantumprocessor.get(1), EV);
        ItemList.Circuit_Quantumcomputer.set(
            addItem(
                86,
                "Quantumprocessor Assembly",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.Elite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Quantumcomputer.get(1), IV);
        ItemList.Circuit_Masterquantumcomputer.set(
            addItem(
                87,
                "Master Quantumcomputer",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.Master),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Masterquantumcomputer.get(1), LuV);
        ItemList.Circuit_Quantummainframe.set(
            addItem(
                88,
                "Quantumprocessor Mainframe",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.Ultimate),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Quantummainframe.get(1), ZPM);

        // Crystal circuits
        ItemList.Circuit_Crystalprocessor.set(
            addItem(
                89,
                "Crystalprocessor",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.Elite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Crystalprocessor.get(1), IV);
        ItemList.Circuit_Crystalcomputer.set(
            addItem(
                96,
                "Crystalprocessor Assembly",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.Master),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Crystalcomputer.get(1), LuV);
        ItemList.Circuit_Ultimatecrystalcomputer.set(
            addItem(
                90,
                "Ultimate Crystalcomputer",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.Ultimate),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Ultimatecrystalcomputer.get(1), ZPM);
        ItemList.Circuit_Crystalmainframe.set(
            addItem(
                91,
                "Crystalprocessor Mainframe",
                "A Super Circuit",
                OrePrefixes.circuit.get(Materials.SuperconductorUHV),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Crystalmainframe.get(1), UV);

        // ???? Scared to remove.
        ItemList.Circuit_Ultimate.set(ItemList.Circuit_Ultimatecrystalcomputer.get(1L)); // maybe should be removed

        // Wetware circuits
        ItemList.Circuit_Neuroprocessor.set(
            addItem(
                92,
                "Wetwareprocessor",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.Master),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Neuroprocessor.get(1), LuV);
        ItemList.Circuit_Wetwarecomputer.set(
            addItem(
                93,
                "Wetwareprocessor Assembly",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.Ultimate),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Wetwarecomputer.get(1), ZPM);
        ItemList.Circuit_Wetwaresupercomputer.set(
            addItem(
                94,
                "Wetware Supercomputer",
                "A Super Circuit",
                OrePrefixes.circuit.get(Materials.SuperconductorUHV),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Wetwaresupercomputer.get(1), UV);
        ItemList.Circuit_Wetwaremainframe.set(
            addItem(
                95,
                "Wetware Mainframe",
                "An Infinite Circuit",
                OrePrefixes.circuit.get(Materials.Infinite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Wetwaremainframe.get(1), UHV);

        // Bioware circuits.
        ItemList.Circuit_Bioprocessor.set(
            addItem(
                97,
                "Bioprocessor",
                "An Ultimate Circuit",
                OrePrefixes.circuit.get(Materials.Ultimate),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Bioprocessor.get(1), ZPM);
        ItemList.Circuit_Biowarecomputer.set(
            addItem(
                98,
                "Biowareprocessor Assembly",
                "A Super Circuit",
                OrePrefixes.circuit.get(Materials.SuperconductorUHV),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Biowarecomputer.get(1), UV);
        ItemList.Circuit_Biowaresupercomputer.set(
            addItem(
                99,
                "Bioware Supercomputer",
                "An Infinite Circuit",
                OrePrefixes.circuit.get(Materials.Infinite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Biowaresupercomputer.get(1), UHV);
        ItemList.Circuit_Biomainframe.set(
            addItem(
                120,
                "Bio Mainframe",
                "A Bio Circuit",
                OrePrefixes.circuit.get(Materials.Bio),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Biomainframe.get(1), UEV);

        ItemList.Circuit_Board_Coated_Basic.set(addItem(100, "Circuit Board", "A basic Circuit Board", o));
        ItemList.Circuit_Board_Phenolic_Good.set(addItem(101, "Good Circuit Board", "A good Circuit Board", o));
        ItemList.Circuit_Board_Epoxy_Advanced
            .set(addItem(102, "Advanced Circuit Board", "A advanced Circuit Board", o));
        ItemList.Circuit_Board_Fiberglass_Advanced
            .set(addItem(103, "More Advanced Circuit Board", "A more advanced Circuit Board", o));
        ItemList.Circuit_Board_Multifiberglass_Elite
            .set(addItem(104, "Elite Circuit Board", "A elite Circuit Board", o));
        ItemList.Circuit_Board_Wetware_Extreme
            .set(addItem(105, "Extreme Wetware Lifesupport Circuit Board", "The Board that keeps life", o));
        ItemList.Circuit_Board_Plastic_Advanced.set(addItem(106, "Plastic Circuit Board", "A good Board", o));
        ItemList.Circuit_Board_Bio_Ultra
            .set(addItem(107, "Ultra Bio Mutated Circuit Board", "Bio genetic mutated Board", o));
        ItemList.Circuit_Board_Optical.set(addItem(728, "Optical Circuit Board", "Optically Infused Board", o));

        // Optical circuits
        ItemList.Circuit_OpticalProcessor.set(
            addItem(
                154,
                "Optical Processor",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.SuperconductorUHV),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_OpticalProcessor.get(1), UV);
        ItemList.Circuit_OpticalAssembly.set(
            addItem(
                155,
                "Optical Assembly",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.Infinite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_OpticalAssembly.get(1), UHV);
        ItemList.Circuit_OpticalComputer.set(
            addItem(
                156,
                "Optical Computer",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.Bio),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_OpticalComputer.get(1), UEV);
        ItemList.Circuit_OpticalMainframe.set(
            addItem(
                157,
                "Optical Mainframe",
                "An Optical Circuit",
                OrePrefixes.circuit.get(Materials.Optical),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_OpticalMainframe.get(1), UIV);

        // Exotic circuits
        ItemList.Circuit_ExoticProcessor.set(
            addItem(
                166,
                "Exotic Processor",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.Infinite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_ExoticProcessor.get(1), UHV);
        ItemList.Circuit_ExoticAssembly.set(
            addItem(
                167,
                "Exotic Assembly",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.Bio),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_ExoticAssembly.get(1), UEV);
        ItemList.Circuit_ExoticComputer.set(
            addItem(
                168,
                "Exotic Computer",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.Optical),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_ExoticComputer.get(1), UIV);
        ItemList.Circuit_ExoticMainframe.set(
            addItem(
                169,
                "Exotic Mainframe",
                "An Exotic Circuit",
                OrePrefixes.circuit.get(Materials.Exotic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_ExoticMainframe.get(1), UMV);

        // Cosmic circuits
        ItemList.Circuit_CosmicProcessor.set(
            addItem(
                170,
                "Cosmic Processor",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.Bio),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_CosmicProcessor.get(1), UEV);
        ItemList.Circuit_CosmicAssembly.set(
            addItem(
                171,
                "Cosmic Assembly",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.Optical),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_CosmicAssembly.get(1), UIV);
        ItemList.Circuit_CosmicComputer.set(
            addItem(
                172,
                "Cosmic Computer",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.Exotic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_CosmicComputer.get(1), UMV);
        ItemList.Circuit_CosmicMainframe.set(
            addItem(
                173,
                "Cosmic Mainframe",
                "A Cosmic Circuit",
                OrePrefixes.circuit.get(Materials.Cosmic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_CosmicMainframe.get(1), UXV);

        // Transcendent circuits
        ItemList.Circuit_TranscendentProcessor.set(
            addItem(
                174,
                "Temporally Transcendent Processor",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.Optical),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_TranscendentProcessor.get(1), UIV);
        ItemList.Circuit_TranscendentAssembly.set(
            addItem(
                175,
                "Temporally Transcendent Assembly",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.Exotic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_TranscendentAssembly.get(1), UMV);
        ItemList.Circuit_TranscendentComputer.set(
            addItem(
                176,
                "Temporally Transcendent Computer",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.Cosmic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_TranscendentComputer.get(1), UXV);
        ItemList.Circuit_TranscendentMainframe.set(
            addItem(
                177,
                "Temporally Transcendent Mainframe",
                "A circuit operating outside of known spacetime",
                OrePrefixes.circuit.get(Materials.Transcendent),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_TranscendentMainframe.get(1), MAX);

        ItemList.Tube_Wires.set(addItem(110, "Tube Wires", "For the Vacuum Tubes", o));

        ItemList.Cover_SolarPanel_UHV.set(
            addItem(
                130,
                "Solar Panel (UHV)",
                "Ultimate High Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 128L)));
        ItemList.Cover_SolarPanel_UEV.set(
            addItem(
                131,
                "Solar Panel (UEV)",
                "Ultimate Extreme Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 256L)));
        ItemList.Cover_SolarPanel_UIV.set(
            addItem(
                132,
                "Solar Panel (UIV)",
                "Ultimate Insane Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 512L)));

        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_UHV.get(1L),
            TextureFactory.of(SOLARPANEL_UHV),
            new GT_Cover_SolarPanel(2097152));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_UEV.get(1L),
            TextureFactory.of(SOLARPANEL_UEV),
            new GT_Cover_SolarPanel(8388608));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_UIV.get(1L),
            TextureFactory.of(SOLARPANEL_UIV),
            new GT_Cover_SolarPanel(33554432));

        ItemList.ULV_Coil.set(addItem(140, "Ultra Low Voltage Coil", "Primitive Coil", o));
        ItemList.LV_Coil.set(addItem(141, "Low Voltage Coil", "Basic Coil", o));
        ItemList.MV_Coil.set(addItem(142, "Medium Voltage Coil", "Good Coil", o));
        ItemList.HV_Coil.set(addItem(143, "High Voltage Coil", "Advanced Coil", o));
        ItemList.EV_Coil.set(addItem(144, "Extreme Voltage Coil", "Extreme Coil", o));
        ItemList.IV_Coil.set(addItem(145, "Insane Voltage Coil", "Elite Coil", o));
        ItemList.LuV_Coil.set(addItem(146, "Ludicrous Voltage Coil", "Master Coil", o));
        ItemList.ZPM_Coil.set(addItem(147, "ZPM Voltage Coil", "Ultimate Coil", o));
        ItemList.UV_Coil.set(addItem(148, "Ultimate Voltage Coil", "Super Coil", o));
        ItemList.UHV_Coil.set(addItem(149, "Highly Ultimate Voltage Coil", "Infinite Coil", o));

        ItemList.GalliumArsenideCrystal.set(addItem(190, "Gallium Arsenide Crystal", "For making boules", o));
        ItemList.GalliumArsenideCrystalSmallPart
            .set(addItem(191, "Small Gallium Arsenide Crystal", "For making boules", o));
        ItemList.KevlarFiber.set(addItem(192, "Kevlar Fiber", "For making Kevlar Plates", o));
        ItemList.WovenKevlar.set(addItem(193, "Woven Kevlar", "For making Kevlar Plates", o));
        ItemList.Spinneret.set(addItem(194, "Spinneret", "For making Kevlar Fiber", o));

        ItemList.IndustrialApiary_Upgrade_Frame.set(addItem(199, "Upgrade Frame", "Crafting component", o));

        ItemList.IndustrialApiary_Upgrade_Acceleration_1.set(
            addItem(
                200,
                "Acceleration Upgrade x2",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 2x acceleration level/n * Energy Consumption +1 AMP LV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_2.set(
            addItem(
                201,
                "Acceleration Upgrade x4",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 4x acceleration level/n * Energy Consumption +1 AMP MV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_3.set(
            addItem(
                202,
                "Acceleration Upgrade x8",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 8x acceleration level/n * Energy Consumption +1 AMP HV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_4.set(
            addItem(
                203,
                "Acceleration Upgrade x16",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 16x acceleration level/n * Energy Consumption +1 AMP EV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_5.set(
            addItem(
                204,
                "Acceleration Upgrade x32",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 32x acceleration level/n * Energy Consumption +1 AMP IV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_6.set(
            addItem(
                205,
                "Acceleration Upgrade x64",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 64x acceleration level/n * Energy Consumption +1 AMP LuV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_7.set(
            addItem(
                206,
                "Acceleration Upgrade x128",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 128x acceleration level/n * Energy Consumption +1 AMP ZPM",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_8.set(
            addItem(
                207,
                "Acceleration Upgrade x256",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 256x acceleration level/n * Energy Consumption +1 AMP UV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.set(
            addItem(
                208,
                "Upgraded Acceleration Upgrade x256",
                "Acceleration upgrade for Industrial Apiary/n Maximum Installed: 1/n * Unlocks 256x acceleration level/n * Will also grant 8x production upgrade/n * Energy Consumption +1 AMP UV",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_PRODUCTION.set(
            addItem(
                209,
                "Production Upgrade",
                "Production upgrade for Industrial Apiary/n Maximum Installed: 8/n Increases production modifier by 0.25/n Energy Consumption +40%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_PLAINS.set(
            addItem(
                210,
                "Plains Emulation Upgrade",
                "Plains emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Plains/n * Energy Consumption +40%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_LIGHT.set(
            addItem(
                211,
                "Light Upgrade",
                "Light upgrade for Industrial Apiary/n Maximum Installed: 1/n * Internal Lighting/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_FLOWERING.set(
            addItem(
                212,
                "Flowering Upgrade",
                "Flowering upgrade for Industrial Apiary/n Maximum Installed: 8/n * Flowering and Pollination +20%/n * Energy Consumption +10%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_WINTER.set(
            addItem(
                213,
                "Winter Emulation Upgrade",
                "Winter emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Taiga/n * Energy Consumption +50%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_DRYER.set(
            addItem(
                214,
                "Dryer Upgrade",
                "Dryer upgrade for Industrial Apiary/n Maximum Installed: 16/n * Humidity -12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_AUTOMATION.set(
            addItem(
                215,
                "Automation Upgrade",
                "Automation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Automation/n * Energy Consumption +10%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.set(
            addItem(
                216,
                "Humidifier Upgrade",
                "Humidifier upgrade for Industrial Apiary/n Maximum Installed: 16/n * Humidity +12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_HELL.set(
            addItem(
                217,
                "HELL Emulation Upgrade",
                "HELL emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: HELL/n * Energy Consumption +50%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_POLLEN.set(
            addItem(
                218,
                "Pollen Scrubber Upgrade",
                "Pollen scrubber upgrade for Industrial Apiary/n Maximum Installed: 1/n * Flowering and Pollination -100%/n * Energy Consumption +30%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_DESERT.set(
            addItem(
                219,
                "Desert Emulation Upgrade",
                "Desert emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Desert/n * Energy Consumption +20%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_COOLER.set(
            addItem(
                220,
                "Cooler Upgrade",
                "Cooler upgrade for Industrial Apiary/n Maximum Installed: 16/n * Temperature -12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_LIFESPAN.set(
            addItem(
                221,
                "Lifespan Upgrade",
                "Lifespan upgrade for Industrial Apiary/n Maximum Installed: 4/n * Lifespan -33%/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_SEAL.set(
            addItem(
                222,
                "Seal Upgrade",
                "Seal upgrade for Industrial Apiary/n Maximum Installed: 1/n * Rain Protection/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_STABILIZER.set(
            addItem(
                223,
                "Genetic Stabilizer Upgrade",
                "Genetic stabilizer upgrade for Industrial Apiary/n Maximum Installed: 1/n * Genetic Decay -100%/n * Energy Consumption +150%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_JUNGLE.set(
            addItem(
                224,
                "Jungle Emulation Upgrade",
                "Jungle emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Jungle/n * Energy Consumption +20%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_TERRITORY.set(
            addItem(
                225,
                "Territory Upgrade",
                "Territory upgrade for Industrial Apiary/n Maximum Installed: 4/n * Territory +50%/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_OCEAN.set(
            addItem(
                226,
                "Ocean Emulation Upgrade",
                "Ocean emulation upgrade for Industrial Apiary/n Maximum Installed: 1/n * Biome Override: Ocean/n * Energy Consumption +20%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_SKY.set(
            addItem(
                227,
                "Open Sky Upgrade",
                "Open sky upgrade for Industrial Apiary/n Maximum Installed: 1/n * Open Sky Simulation/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_HEATER.set(
            addItem(
                228,
                "Heater Upgrade",
                "Heater upgrade for Industrial Apiary/n Maximum Installed: 16/n * Temperature +12.5%/n * Energy Consumption +2.5%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_SIEVE.set(
            addItem(
                229,
                "Sieve Upgrade",
                "Sieve upgrade for Industrial Apiary/n Maximum Installed: 1/n * Pollen Collection/n * Energy Consumption +25%",
                OrePrefixes.apiaryUpgrade.name()));
        ItemList.IndustrialApiary_Upgrade_UNLIGHT.set(
            addItem(
                231,
                "Night Upgrade",
                "Night upgrade for Industrial Apiary/n Maximum Installed: 1/n * Internal Darkness/n * Energy Consumption +5%",
                OrePrefixes.apiaryUpgrade.name()));

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

        ItemList.NuclearStar.set(
            addItem(
                230,
                "Nuclear Star",
                "By the powers of Greg, I command this star to be really hot.",
                SubTag.NO_UNIFICATION));

        ItemList.Cover_Metrics_Transmitter.set(
            addItem(
                232,
                "Metrics Transmitter Cover",
                String.join(
                    "/n ",
                    "Taking Information Panels to the next level!",
                    "Creates a GregTech Advanced Sensor Card when attached",
                    "Works across dimensions or if machine is dismantled",
                    "Removing this cover will destroy the linked card",
                    GT_Values.AuthorQuerns)));
        GregTech_API.registerCover(
            ItemList.Cover_Metrics_Transmitter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_METRICS_TRANSMITTER)),
            new GT_Cover_Metrics_Transmitter(TextureFactory.of(OVERLAY_METRICS_TRANSMITTER)));

        ItemList.Optical_Cpu_Containment_Housing.set(addItem(727, "Optical CPU Containment Housing", "CPU Housing", o));
        ItemList.Optically_Perfected_CPU.set(addItem(726, "Optically Perfected CPU", "Perfected CPU!", o));
        ItemList.Optically_Compatible_Memory.set(addItem(725, "Optically Compatible Memory", "Its in the name!", o));

        ItemList.Timepiece.set(addItem(757, "Timepiece", "Beware of the kid with the hat", o));

        int whiteDwarfIDs = 729;
        ItemList.White_Dwarf_Shape_Extruder_Plate.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Plate)",
                "Ultra Strong Stellar Shape for making Plates"));
        ItemList.White_Dwarf_Shape_Extruder_Rod.set(
            addItem(whiteDwarfIDs++, "White Dwarf Extruder Shape (Rod)", "Ultra Strong Stellar Shape for making Rods"));
        ItemList.White_Dwarf_Shape_Extruder_Bolt.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Bolt)",
                "Ultra Strong Stellar Shape for making Bolts"));
        ItemList.White_Dwarf_Shape_Extruder_Ring.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Ring)",
                "Ultra Strong Stellar Shape for making Rings"));
        ItemList.White_Dwarf_Shape_Extruder_Cell.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Cell)",
                "Ultra Strong Stellar Shape for making Cells"));
        ItemList.White_Dwarf_Shape_Extruder_Ingot.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Ingot)",
                "Ultra Strong Stellar Shape for, wait, can't we just use a Furnace?"));
        ItemList.White_Dwarf_Shape_Extruder_Wire.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Wire)",
                "Ultra Strong Stellar Shape for making Wires"));
        ItemList.White_Dwarf_Shape_Extruder_Casing.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Casing)",
                "Ultra Strong Stellar Shape for making Item Casings"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Tiny.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Tiny Pipe)",
                "Ultra Strong Stellar Shape for making tiny Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Small.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Small Pipe)",
                "Ultra Strong Stellar Shape for making small Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Medium.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Normal Pipe)",
                "Ultra Strong Stellar Shape for making Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Large.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Large Pipe)",
                "Ultra Strong Stellar Shape for making large Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Pipe_Huge.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Huge Pipe)",
                "Ultra Strong Stellar Shape for making full Block Pipes"));
        ItemList.White_Dwarf_Shape_Extruder_Block.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Block)",
                "Ultra Strong Stellar Shape for making Blocks"));
        ItemList.White_Dwarf_Shape_Extruder_Sword.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Sword Blade)",
                "Ultra Strong Stellar Shape for making Swords"));
        ItemList.White_Dwarf_Shape_Extruder_Pickaxe.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Pickaxe Head)",
                "Ultra Strong Stellar Shape for making Pickaxes"));
        ItemList.White_Dwarf_Shape_Extruder_Shovel.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Shovel Head)",
                "Ultra Strong Stellar Shape for making Shovels"));
        ItemList.White_Dwarf_Shape_Extruder_Axe.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Axe Head)",
                "Ultra Strong Stellar Shape for making Axes"));
        ItemList.White_Dwarf_Shape_Extruder_Hoe.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Hoe Head)",
                "Ultra Strong Stellar Shape for making Hoes"));
        ItemList.White_Dwarf_Shape_Extruder_Hammer.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Hammer Head)",
                "Ultra Strong Stellar Shape for making Hammers"));
        ItemList.White_Dwarf_Shape_Extruder_File.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (File Head)",
                "Ultra Strong Stellar Shape for making Files"));
        ItemList.White_Dwarf_Shape_Extruder_Saw.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Saw Blade)",
                "Ultra Strong Stellar Shape for making Saws"));
        ItemList.White_Dwarf_Shape_Extruder_Gear.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Gear)",
                "Ultra Strong Stellar Shape for making Gears"));
        ItemList.White_Dwarf_Shape_Extruder_Bottle.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Bottle)",
                "Ultra Strong Stellar Shape for making Bottles"));
        ItemList.White_Dwarf_Shape_Extruder_Rotor.set(
            addItem(whiteDwarfIDs++, "White Dwarf Extruder Shape (Rotor)", "Ultra Strong Stellar Shape for a Rotor"));
        ItemList.White_Dwarf_Shape_Extruder_Small_Gear.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Small Gear)",
                "Ultra Strong Stellar Shape for a Small Gear"));
        ItemList.White_Dwarf_Shape_Extruder_Turbine_Blade.set(
            addItem(
                whiteDwarfIDs++,
                "White Dwarf Extruder Shape (Turbine Blade)",
                "Ultra Strong Stellar Extruder Shape for a Turbine Blade"));
        ItemList.White_Dwarf_Shape_Extruder_ToolHeadDrill.set(
            addItem(
                whiteDwarfIDs,
                "White Dwarf Extruder Shape (Drill Head)",
                "White Dwarf Extruder Shape for a Drill Head"));
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
