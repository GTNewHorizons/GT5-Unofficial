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
import static gregtech.common.items.IDMetaItem03.Alumina_Support_Ring;
import static gregtech.common.items.IDMetaItem03.Alumina_Support_Ring_Raw;
import static gregtech.common.items.IDMetaItem03.Beryllium_Shielding_Plate;
import static gregtech.common.items.IDMetaItem03.Brittle_Netherite_Scrap;
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
import static gregtech.common.items.IDMetaItem03.Harmonic_Compound;
import static gregtech.common.items.IDMetaItem03.Heavy_Hellish_Mud;
import static gregtech.common.items.IDMetaItem03.Hot_Netherite_Scrap;
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
import static gregtech.common.items.IDMetaItem03.Intensely_Bonded_Netherite_Nanoparticles;
import static gregtech.common.items.IDMetaItem03.KevlarFiber;
import static gregtech.common.items.IDMetaItem03.LV_Coil;
import static gregtech.common.items.IDMetaItem03.LuV_Coil;
import static gregtech.common.items.IDMetaItem03.MAX_Coil;
import static gregtech.common.items.IDMetaItem03.MV_Coil;
import static gregtech.common.items.IDMetaItem03.Manafly;
import static gregtech.common.items.IDMetaItem03.NandChip;
import static gregtech.common.items.IDMetaItem03.Naquarite_Universal_Insulator_Foil;
import static gregtech.common.items.IDMetaItem03.Netherite_Nanoparticles;
import static gregtech.common.items.IDMetaItem03.Netherite_Scrap_Seed;
import static gregtech.common.items.IDMetaItem03.NuclearStar;
import static gregtech.common.items.IDMetaItem03.Optical_Cpu_Containment_Housing;
import static gregtech.common.items.IDMetaItem03.Optically_Compatible_Memory;
import static gregtech.common.items.IDMetaItem03.Optically_Perfected_CPU;
import static gregtech.common.items.IDMetaItem03.Phononic_Seed_Crystal;
import static gregtech.common.items.IDMetaItem03.Prismarine_Precipitate;
import static gregtech.common.items.IDMetaItem03.Prismatic_Crystal;
import static gregtech.common.items.IDMetaItem03.Quark_Catalyst_Housing;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Bottom;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Charm;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Down;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Strange;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Top;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Unaligned;
import static gregtech.common.items.IDMetaItem03.Quark_Creation_Catalyst_Up;
import static gregtech.common.items.IDMetaItem03.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet;
import static gregtech.common.items.IDMetaItem03.Relativistic_Heat_Capacitor;
import static gregtech.common.items.IDMetaItem03.Spinneret;
import static gregtech.common.items.IDMetaItem03.StableBaryonContainmentUnit;
import static gregtech.common.items.IDMetaItem03.StableBosonContainmentUnit;
import static gregtech.common.items.IDMetaItem03.StableEmptyContainmentUnit;
import static gregtech.common.items.IDMetaItem03.StableLeptonContainmentUnit;
import static gregtech.common.items.IDMetaItem03.StableMesonContainmentUnit;
import static gregtech.common.items.IDMetaItem03.Thermal_Superconductor;
import static gregtech.common.items.IDMetaItem03.Timepiece;
import static gregtech.common.items.IDMetaItem03.Transdimensional_Alignment_Matrix;
import static gregtech.common.items.IDMetaItem03.Tube_Wires;
import static gregtech.common.items.IDMetaItem03.UEV_Coil;
import static gregtech.common.items.IDMetaItem03.UHV_Coil;
import static gregtech.common.items.IDMetaItem03.UIV_Coil;
import static gregtech.common.items.IDMetaItem03.ULV_Coil;
import static gregtech.common.items.IDMetaItem03.UMV_Coil;
import static gregtech.common.items.IDMetaItem03.UV_Coil;
import static gregtech.common.items.IDMetaItem03.UXV_Coil;
import static gregtech.common.items.IDMetaItem03.WovenKevlar;
import static gregtech.common.items.IDMetaItem03.ZPM_Coil;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Optional;
import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverMetricsTransmitter;
import gregtech.common.covers.CoverSolarPanel;
import gregtech.common.powergoggles.ItemPowerGoggles;
import gregtech.common.render.items.GlitchEffectMetaItemRenderer;
import gregtech.common.render.items.InfinityMetaItemRenderer;
import gregtech.common.render.items.RainbowOverlayMetaItemRenderer;
import mods.railcraft.common.items.firestone.IItemFirestoneBurning;

@Optional.Interface(
    iface = "mods.railcraft.common.items.firestone.IItemFirestoneBurning",
    modid = Mods.ModIDs.RAILCRAFT)
public class MetaGeneratedItem03 extends MetaGeneratedItemX32 implements IItemFirestoneBurning {

    public static MetaGeneratedItem03 INSTANCE;

    public MetaGeneratedItem03() {
        super(
            "metaitem.03",
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.nanite,
            OrePrefixes.rawOre,
            OrePrefixes.plateSuperdense);
        INSTANCE = this;
        Object[] o = GTValues.emptyObjectArray;
        final String RAW = "gt.item.circuit.raw_circuit.tooltip";
        ItemList.Item_Power_Goggles
            .set(new ItemPowerGoggles("Power_Goggles", "gt.item.power_goggles.name", "gt.item.power_goggles.tooltip"));
        /*
         * circuit boards tier 1-7: coated circuit board / wood plate + resin Plastic Circuit Board / Plastic + Copper
         * Foil + Sulfuric Acid phenolic circuit board /carton+glue+chemical bath epoxy circuit board /epoxy plate +
         * copper foil + sulfuric acid fiberglass circuit board (simple + multilayer) / glass + plastic + electrum foil
         * + sulfuric acid wetware lifesupport board / fiberglass CB + teflon +
         */

        ItemList.Circuit_Board_Wetware.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Wetware.ID,
                "gt.item.circuit_board.wetware.name",
                "gt.item.circuit_board.wetware.tooltip",
                o));
        ItemList.Circuit_Board_Plastic.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Plastic.ID,
                "gt.item.circuit_board.plastic.name",
                "gt.item.circuit_board.plastic.tooltip",
                o));
        ItemList.Circuit_Board_Bio.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Bio.ID,
                "gt.item.circuit_board.bio.name",
                "gt.item.circuit_board.bio.tooltip",
                o));
        /*
         * electronic components: vacuum tube (glass tube + red alloy cables) basic electronic circuits normal+smd coils
         * diodes normal+smd transistors normal+smd capacitors normal+smd Glass Fibers
         */
        ItemList.Circuit_Parts_ResistorSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_ResistorSMD.ID,
                "gt.item.circuit_part.resistor_smd.name",
                "gt.item.circuit_part.resistor_smd.tooltip",
                OrePrefixes.componentCircuit.get(Materials.Resistor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_Glass_Tube.set(
            addItemWithLocalizationKeys(Circuit_Parts_Glass_Tube.ID, "gt.item.circuit_part.glass_tube.name", "", o));
        ItemList.Circuit_Parts_Coil.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Coil.ID,
                "gt.item.circuit_part.coil.name",
                "gt.item.circuit_part.coil.tooltip",
                o));
        ItemList.Circuit_Parts_DiodeSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_DiodeSMD.ID,
                "gt.item.circuit_part.diode_smd.name",
                "gt.item.circuit_part.diode_smd.tooltip",
                OrePrefixes.componentCircuit.get(Materials.Diode),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_TransistorSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_TransistorSMD.ID,
                "gt.item.circuit_part.transistor_smd.name",
                "gt.item.circuit_part.transistor_smd.tooltip",
                OrePrefixes.componentCircuit.get(Materials.Transistor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_CapacitorSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_CapacitorSMD.ID,
                "gt.item.circuit_part.capacitor_smd.name",
                "gt.item.circuit_part.capacitor_smd.tooltip",
                OrePrefixes.componentCircuit.get(Materials.Capacitor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_GlassFiber.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_GlassFiber.ID,
                "gt.item.circuit_part.glass_fiber.name",
                Materials.BorosilicateGlass.getChemicalFormula(),
                o));
        ItemList.Circuit_Parts_PetriDish.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_PetriDish.ID,
                "gt.item.circuit_part.petri_dish.name",
                "gt.item.circuit_part.petri_dish.tooltip",
                o));
        ItemList.Circuit_Parts_Reinforced_Glass_Tube.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Reinforced_Glass_Tube.ID,
                "gt.item.circuit_part.reinforced_glass_tube.name",
                "",
                o));

        ItemList.Circuit_Parts_ResistorASMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_ResistorASMD.ID,
                "gt.item.circuit_part.resistor_asmd.name",
                "gt.item.circuit_part.resistor_asmd.tooltip",
                o));
        ItemList.Circuit_Parts_DiodeASMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_DiodeASMD.ID,
                "gt.item.circuit_part.diode_asmd.name",
                "gt.item.circuit_part.diode_asmd.tooltip",
                o));
        ItemList.Circuit_Parts_TransistorASMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_TransistorASMD.ID,
                "gt.item.circuit_part.transistor_asmd.name",
                "gt.item.circuit_part.transistor_asmd.tooltip",
                o));
        ItemList.Circuit_Parts_CapacitorASMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_CapacitorASMD.ID,
                "gt.item.circuit_part.capacitor_asmd.name",
                "gt.item.circuit_part.capacitor_asmd.tooltip",
                o));

        ItemList.Circuit_Parts_ResistorXSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_ResistorXSMD.ID,
                "gt.item.circuit_part.resistor_xsmd.name",
                "gt.item.circuit_part.resistor_xsmd.tooltip",
                o));
        ItemList.Circuit_Parts_DiodeXSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_DiodeXSMD.ID,
                "gt.item.circuit_part.diode_xsmd.name",
                "gt.item.circuit_part.diode_xsmd.tooltip",
                o));
        ItemList.Circuit_Parts_TransistorXSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_TransistorXSMD.ID,
                "gt.item.circuit_part.transistor_xsmd.name",
                "gt.item.circuit_part.transistor_xsmd.tooltip",
                o));
        ItemList.Circuit_Parts_CapacitorXSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_CapacitorXSMD.ID,
                "gt.item.circuit_part.capacitor_xsmd.name",
                "gt.item.circuit_part.capacitor_xsmd.tooltip",
                o));

        ItemList.Circuit_Parts_InductorSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_InductorSMD.ID,
                "gt.item.circuit_part.inductor_smd.name",
                "gt.item.circuit_part.inductor_smd.tooltip",
                OrePrefixes.componentCircuit.get(Materials.Inductor),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Parts_InductorASMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_InductorASMD.ID,
                "gt.item.circuit_part.inductor_asmd.name",
                "gt.item.circuit_part.inductor_asmd.tooltip",
                o));
        ItemList.Circuit_Parts_InductorXSMD.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_InductorXSMD.ID,
                "gt.item.circuit_part.inductor_xsmd.name",
                "gt.item.circuit_part.inductor_xsmd.tooltip",
                o));

        /*
         * ICs Lenses made from perfect crystals first instead of plates Monocrystalline silicon ingot
         * (normal+glowstone+naquadah) EBF, normal silicon no EBF need anymore wafer(normal+glowstone+naquadah) cut mono
         * silicon ingot in cutting machine Integrated Logic Circuit(8bit DIP) RAM NAND Memory NOR Memory CPU (4 sizes)
         * SoCs(2 sizes, high tier cheap low tech component) Power IC/High Power IC/Ultra High power nanotube
         * interconnected circuit (H-IC + nanotubes) quantum chips
         */

        ItemList.Circuit_Silicon_Ingot.set(
            addItemWithLocalizationKeys(Circuit_Silicon_Ingot.ID, "gt.item.circuit.silicon_ingot.plain.name", RAW, o));
        ItemList.Circuit_Silicon_Ingot2.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Ingot2.ID,
                "gt.item.circuit.silicon_ingot.phosphorus.name",
                RAW,
                o));
        ItemList.Circuit_Silicon_Ingot3.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Ingot3.ID,
                "gt.item.circuit.silicon_ingot.naquadah.name",
                RAW,
                o));
        ItemList.Circuit_Silicon_Ingot4.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Ingot4.ID,
                "gt.item.circuit.silicon_ingot.europium.name",
                RAW,
                o));
        ItemList.Circuit_Silicon_Ingot5.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Ingot5.ID,
                "gt.item.circuit.silicon_ingot.americium.name",
                RAW,
                o));
        ItemList.Circuit_Silicon_Ingot6.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Ingot6.ID,
                "gt.item.circuit.silicon_ingot.optical.name",
                RAW,
                o));

        ItemList.Circuit_Silicon_Wafer.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer.ID,
                "gt.item.circuit.wafer.plain.name",
                RAW,
                "waferPlain",
                "wafer"));
        ItemList.Circuit_Silicon_Wafer2.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer2.ID,
                "gt.item.circuit.wafer.phosphorus.name",
                RAW,
                "waferPhosphorus",
                "wafer"));
        ItemList.Circuit_Silicon_Wafer3.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer3.ID,
                "gt.item.circuit.wafer.naquadah.name",
                RAW,
                "waferNaquadah",
                "wafer"));
        ItemList.Circuit_Silicon_Wafer4.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer4.ID,
                "gt.item.circuit.wafer.europium.name",
                RAW,
                "waferEuropium",
                "wafer"));
        ItemList.Circuit_Silicon_Wafer5.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer5.ID,
                "gt.item.circuit.wafer.americium.name",
                RAW,
                "waferAmericium",
                "wafer"));
        ItemList.Circuit_Silicon_Wafer6.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer6.ID,
                "gt.item.circuit.wafer.optical_prepared.name",
                RAW,
                "waferPhotonicallyPrepared",
                "wafer"));
        ItemList.Circuit_Silicon_Wafer7.set(
            addItemWithLocalizationKeys(
                Circuit_Silicon_Wafer7.ID,
                "gt.item.circuit.wafer.optical_enhanced.name",
                RAW,
                "waferPhotonicallyEnhanced",
                "wafer"));

        ItemList.Circuit_Wafer_ILC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_ILC.ID,
                "gt.item.circuit.wafer.ilc.name",
                RAW,
                "waferILC",
                "wafer"));
        ItemList.Circuit_Chip_ILC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_ILC.ID,
                "gt.item.circuit.chip.ilc.name",
                "gt.item.circuit.chip.ilc.tooltip",
                "chipILC",
                "chip"));

        ItemList.Circuit_Wafer_Ram.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_Ram.ID,
                "gt.item.circuit.wafer.ram.name",
                RAW,
                "waferRAM",
                "wafer"));
        ItemList.Circuit_Chip_Ram.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_Ram.ID,
                "gt.item.circuit.chip.ram.name",
                "gt.item.circuit.chip.ram.tooltip",
                "chipRAM",
                "chip"));

        ItemList.Circuit_Wafer_NAND.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_NAND.ID,
                "gt.item.circuit.wafer.nand.name",
                RAW,
                "waferNAND",
                "wafer"));
        ItemList.Circuit_Chip_NAND.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_NAND.ID,
                "gt.item.circuit.chip.nand.name",
                "gt.item.circuit.chip.nand.tooltip",
                "chipNAND",
                "chip"));

        ItemList.Circuit_Wafer_NOR.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_NOR.ID,
                "gt.item.circuit.wafer.nor.name",
                RAW,
                "waferNOR",
                "wafer"));
        ItemList.Circuit_Chip_NOR.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_NOR.ID,
                "gt.item.circuit.chip.nor.name",
                "gt.item.circuit.chip.nor.tooltip",
                "chipNOR",
                "chip"));

        ItemList.Circuit_Wafer_CPU.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_CPU.ID,
                "gt.item.circuit.wafer.cpu.name",
                RAW,
                "waferCPU",
                "wafer"));
        ItemList.Circuit_Chip_CPU.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_CPU.ID,
                "gt.item.circuit.chip.cpu.name",
                "gt.item.circuit.chip.cpu.tooltip",
                "chipCPU",
                "chip"));

        ItemList.Circuit_Wafer_SoC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_SoC.ID,
                "gt.item.circuit.wafer.soc.name",
                RAW,
                "waferSoC",
                "wafer"));
        ItemList.Circuit_Chip_SoC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_SoC.ID,
                "gt.item.circuit.chip.soc.name",
                "gt.item.circuit.chip.soc.tooltip",
                "chipSoC",
                "chip"));

        ItemList.Circuit_Wafer_SoC2.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_SoC2.ID,
                "gt.item.circuit.wafer.asoc.name",
                RAW,
                "waferASoC",
                "wafer"));
        ItemList.Circuit_Chip_SoC2.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_SoC2.ID,
                "gt.item.circuit.chip.asoc.name",
                "gt.item.circuit.chip.asoc.tooltip",
                "chipASoC",
                "chip"));

        ItemList.Circuit_Wafer_PIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_PIC.ID,
                "gt.item.circuit.wafer.pic.name",
                RAW,
                "waferPIC",
                "wafer"));
        ItemList.Circuit_Chip_PIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_PIC.ID,
                "gt.item.circuit.chip.pic.name",
                "gt.item.circuit.chip.pic.tooltip",
                "chipPIC",
                "chip"));

        ItemList.Circuit_Wafer_HPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_HPIC.ID,
                "gt.item.circuit.wafer.hpic.name",
                RAW,
                "waferHPIC",
                "wafer"));
        ItemList.Circuit_Chip_HPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_HPIC.ID,
                "gt.item.circuit.chip.hpic.name",
                "gt.item.circuit.chip.hpic.tooltip",
                "chipHPIC",
                "chip"));

        ItemList.Circuit_Wafer_NanoCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_NanoCPU.ID,
                "gt.item.circuit.wafer.nanocpu.name",
                RAW,
                "waferNanoCPU",
                "wafer"));
        ItemList.Circuit_Chip_NanoCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_NanoCPU.ID,
                "gt.item.circuit.chip.nanocpu.name",
                "gt.item.circuit.chip.nanocpu.tooltip",
                "chipNanoCPU",
                "chip"));

        ItemList.Circuit_Wafer_QuantumCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_QuantumCPU.ID,
                "gt.item.circuit.wafer.quantumcpu.name",
                RAW,
                "waferQuantumCPU",
                "wafer"));
        ItemList.Circuit_Chip_QuantumCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_QuantumCPU.ID,
                "gt.item.circuit.chip.quantumcpu.name",
                "gt.item.circuit.chip.quantumcpu.tooltip",
                "chipQuantumCPU",
                "chip"));

        ItemList.Circuit_Wafer_UHPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_UHPIC.ID,
                "gt.item.circuit.wafer.uhpic.name",
                RAW,
                "waferUHPIC",
                "wafer"));
        ItemList.Circuit_Chip_UHPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_UHPIC.ID,
                "gt.item.circuit.chip.uhpic.name",
                "gt.item.circuit.chip.uhpic.tooltip",
                "chipUHPIC",
                "chip"));

        ItemList.Circuit_Wafer_Simple_SoC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_Simple_SoC.ID,
                "gt.item.circuit.wafer.simple_soc.name",
                RAW,
                "waferSimpleSoC",
                "wafer"));
        ItemList.Circuit_Chip_Simple_SoC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_Simple_SoC.ID,
                "gt.item.circuit.chip.simple_soc.name",
                "gt.item.circuit.chip.simple_soc.tooltip",
                "chipSimpleSoC",
                "chip"));

        ItemList.Circuit_Wafer_ULPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_ULPIC.ID,
                "gt.item.circuit.wafer.ulpic.name",
                RAW,
                "waferULPIC",
                "wafer"));
        ItemList.Circuit_Chip_ULPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_ULPIC.ID,
                "gt.item.circuit.chip.ulpic.name",
                "gt.item.circuit.chip.ulpic.tooltip",
                "chipULPIC",
                "chip"));

        ItemList.Circuit_Wafer_LPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_LPIC.ID,
                "gt.item.circuit.wafer.lpic.name",
                RAW,
                "waferLPIC",
                "wafer"));
        ItemList.Circuit_Chip_LPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_LPIC.ID,
                "gt.item.circuit.chip.lpic.name",
                "gt.item.circuit.chip.lpic.tooltip",
                "chipLPIC",
                "chip"));

        ItemList.Circuit_Wafer_NPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_NPIC.ID,
                "gt.item.circuit.wafer.npic.name",
                RAW,
                "waferNPIC",
                "wafer"));
        ItemList.Circuit_Chip_NPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_NPIC.ID,
                "gt.item.circuit.chip.npic.name",
                "gt.item.circuit.chip.npic.tooltip",
                "chipNPIC",
                "chip"));

        ItemList.Circuit_Wafer_PPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_PPIC.ID,
                "gt.item.circuit.wafer.ppic.name",
                RAW,
                "waferPPIC",
                "wafer"));
        ItemList.Circuit_Chip_PPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_PPIC.ID,
                "gt.item.circuit.chip.ppic.name",
                "gt.item.circuit.chip.ppic.tooltip",
                "chipPPIC",
                "chip"));

        ItemList.Circuit_Wafer_QPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_QPIC.ID,
                "gt.item.circuit.wafer.qpic.name",
                RAW,
                "waferQPIC",
                "wafer"));
        ItemList.Circuit_Chip_QPIC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_QPIC.ID,
                "gt.item.circuit.chip.qpic.name",
                "gt.item.circuit.chip.qpic.tooltip",
                "chipQPIC",
                "chip"));

        ItemList.Circuit_Wafer_Bioware.set(
            addItemWithLocalizationKeys(
                Circuit_Wafer_Bioware.ID,
                "gt.item.circuit.wafer.bioware.name",
                RAW,
                "waferBioware",
                "wafer"));
        ItemList.Circuit_Parts_Chip_Bioware.set(
            /*
             * Engraved Crystal Chip Engraved Lapotron Chip Crystal CPU SoCrystal stem cells (disassemble eggs)
             */
            addItemWithLocalizationKeys(
                Circuit_Parts_Chip_Bioware.ID,
                "gt.item.circuit.chip.bioware.name",
                "gt.item.circuit.chip.bioware.tooltip",
                "chipBioware",
                "chip"));

        ItemList.Circuit_Chip_CrystalSoC2.set(
            // chip
            // elite
            // part
            addItemWithLocalizationKeys(
                Circuit_Chip_CrystalSoC2.ID,
                "gt.item.circuit.chip.crystal_soc2.name",
                "gt.item.circuit.chip.crystal_soc2.tooltip",
                o));
        ItemList.Circuit_Parts_RawCrystalChip.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_RawCrystalChip.ID,
                "gt.item.circuit_part.raw_crystal_chip.name",
                "gt.item.circuit_part.raw_crystal_chip.tooltip",
                o));
        ItemList.Circuit_Chip_CrystalCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_CrystalCPU.ID,
                "gt.item.circuit.chip.crystal_cpu.name",
                "gt.item.circuit.chip.crystal_cpu.tooltip",
                o));
        ItemList.Circuit_Chip_CrystalSoC.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_CrystalSoC.ID,
                "gt.item.circuit.chip.crystal_soc.name",
                "gt.item.circuit.chip.crystal_soc.tooltip",
                o));
        ItemList.Circuit_Chip_NeuroCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_NeuroCPU.ID,
                "gt.item.circuit.chip.neuro_cpu.name",
                "gt.item.circuit.chip.neuro_cpu.tooltip",
                o));
        ItemList.Circuit_Chip_Stemcell.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_Stemcell.ID,
                "gt.item.circuit.chip.stemcell.name",
                "gt.item.circuit.chip.stemcell.tooltip",
                o));
        ItemList.Circuit_Parts_RawCrystalParts.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_RawCrystalParts.ID,
                "gt.item.circuit_part.raw_crystal_parts.name",
                "gt.item.circuit_part.raw_crystal_parts.tooltip",
                o));
        ItemList.Circuit_Chip_Biocell.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_Biocell.ID,
                "gt.item.circuit.chip.biocell.name",
                "gt.item.circuit.chip.biocell.tooltip",
                o));
        ItemList.Circuit_Chip_BioCPU.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_BioCPU.ID,
                "gt.item.circuit.chip.bio_cpu.name",
                "gt.item.circuit.chip.bio_cpu.tooltip",
                o));
        ItemList.Circuit_Chip_Optical.set(
            addItemWithLocalizationKeys(
                Circuit_Chip_Optical.ID,
                "gt.item.circuit.chip.optical.name",
                "gt.item.circuit.chip.optical.tooltip",
                o));

        // Nand Chip
        ItemList.NandChip.set(
            addItemWithLocalizationKeys(
                NandChip.ID,
                "gt.item.nand_chip.name",
                "gt.item.nand_chip.tooltip",
                OrePrefixes.circuit.get(Materials.ULV),
                SubTag.NO_UNIFICATION));

        // Vacuum Tube Item01
        // Basic Circuit IC2
        // Good Circuit Item01

        // Integrated Logic Circuit Item01

        // Good Integrated Circuit Item01
        // Advanced Circuit IC2
        ItemList.Circuit_Microprocessor.set(
            addItemWithLocalizationKeys(
                Circuit_Microprocessor.ID,
                "gt.item.circuit.microprocessor.name",
                "gt.item.circuit.microprocessor.tooltip",
                OrePrefixes.circuit.get(Materials.LV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Processor.set(
            addItemWithLocalizationKeys(
                Circuit_Processor.ID,
                "gt.item.circuit.integrated_processor.name",
                "gt.item.circuit.integrated_processor.tooltip",
                OrePrefixes.circuit.get(Materials.MV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Integrated_Good.set(
            addItemWithLocalizationKeys(
                Circuit_Integrated_Good.ID,
                "gt.item.circuit.good_integrated.name",
                "gt.item.circuit.good_integrated.tooltip",
                OrePrefixes.circuit.get(Materials.MV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Nanoprocessor.set(
            addItemWithLocalizationKeys(
                Circuit_Nanoprocessor.ID,
                "gt.item.circuit.nano_processor.name",
                "gt.item.circuit.nano_processor.tooltip",
                OrePrefixes.circuit.get(Materials.HV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Nanocomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Nanocomputer.ID,
                "gt.item.circuit.nano_assembly.name",
                "gt.item.circuit.nano_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.EV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Elitenanocomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Elitenanocomputer.ID,
                "gt.item.circuit.nano_supercomputer.name",
                "gt.item.circuit.nano_supercomputer.tooltip",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));

        // Quantum circuits
        ItemList.Circuit_Quantumprocessor.set(
            addItemWithLocalizationKeys(
                Circuit_Quantumprocessor.ID,
                "gt.item.circuit.quantum_processor.name",
                "gt.item.circuit.quantum_processor.tooltip",
                OrePrefixes.circuit.get(Materials.EV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Quantumcomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Quantumcomputer.ID,
                "gt.item.circuit.quantum_assembly.name",
                "gt.item.circuit.quantum_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Masterquantumcomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Masterquantumcomputer.ID,
                "gt.item.circuit.quantum_supercomputer.name",
                "gt.item.circuit.quantum_supercomputer.tooltip",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Quantummainframe.set(
            addItemWithLocalizationKeys(
                Circuit_Quantummainframe.ID,
                "gt.item.circuit.quantum_mainframe.name",
                "gt.item.circuit.quantum_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        // Crystal circuits
        ItemList.Circuit_Crystalprocessor.set(
            addItemWithLocalizationKeys(
                Circuit_Crystalprocessor.ID,
                "gt.item.circuit.crystal_processor.name",
                "gt.item.circuit.crystal_processor.tooltip",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Crystalcomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Crystalcomputer.ID,
                "gt.item.circuit.crystal_assembly.name",
                "gt.item.circuit.crystal_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Ultimatecrystalcomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Ultimatecrystalcomputer.ID,
                "gt.item.circuit.crystal_supercomputer.name",
                "gt.item.circuit.crystal_supercomputer.tooltip",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Crystalmainframe.set(
            addItemWithLocalizationKeys(
                Circuit_Crystalmainframe.ID,
                "gt.item.circuit.crystal_mainframe.name",
                "gt.item.circuit.crystal_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        // Wetware circuits
        ItemList.Circuit_Neuroprocessor.set(
            addItemWithLocalizationKeys(
                Circuit_Neuroprocessor.ID,
                "gt.item.circuit.wetware_processor.name",
                "gt.item.circuit.wetware_processor.tooltip",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Wetwarecomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Wetwarecomputer.ID,
                "gt.item.circuit.wetware_assembly.name",
                "gt.item.circuit.wetware_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Wetwaresupercomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Wetwaresupercomputer.ID,
                "gt.item.circuit.wetware_supercomputer.name",
                "gt.item.circuit.wetware_supercomputer.tooltip",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Wetwaremainframe.set(
            addItemWithLocalizationKeys(
                Circuit_Wetwaremainframe.ID,
                "gt.item.circuit.wetware_mainframe.name",
                "gt.item.circuit.wetware_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));

        // Bioware circuits
        ItemList.Circuit_Bioprocessor.set(
            addItemWithLocalizationKeys(
                Circuit_Bioprocessor.ID,
                "gt.item.circuit.bioware_processor.name",
                "gt.item.circuit.bioware_processor.tooltip",
                OrePrefixes.circuit.get(Materials.ZPM),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Biowarecomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Biowarecomputer.ID,
                "gt.item.circuit.bioware_assembly.name",
                "gt.item.circuit.bioware_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Biowaresupercomputer.set(
            addItemWithLocalizationKeys(
                Circuit_Biowaresupercomputer.ID,
                "gt.item.circuit.bioware_supercomputer.name",
                "gt.item.circuit.bioware_supercomputer.tooltip",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));

        ItemList.Circuit_Biomainframe.set(
            addItemWithLocalizationKeys(
                Circuit_Biomainframe.ID,
                "gt.item.circuit.bioware_mainframe.name",
                "gt.item.circuit.bioware_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));

        // Circuit Boards
        ItemList.Circuit_Board_Coated_Basic.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Coated_Basic.ID,
                "gt.item.circuit_board.coated_basic.name",
                "gt.item.circuit_board.coated_basic.tooltip",
                o));
        ItemList.Circuit_Board_Phenolic_Good.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Phenolic_Good.ID,
                "gt.item.circuit_board.phenolic_good.name",
                "gt.item.circuit_board.phenolic_good.tooltip",
                o));
        ItemList.Circuit_Board_Epoxy_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Epoxy_Advanced.ID,
                "gt.item.circuit_board.epoxy_advanced.name",
                "gt.item.circuit_board.epoxy_advanced.tooltip",
                o));
        ItemList.Circuit_Board_Fiberglass_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Fiberglass_Advanced.ID,
                "gt.item.circuit_board.fiberglass_advanced.name",
                "gt.item.circuit_board.fiberglass_advanced.tooltip",
                o));
        ItemList.Circuit_Board_Multifiberglass_Elite.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Multifiberglass_Elite.ID,
                "gt.item.circuit_board.multifiberglass_elite.name",
                "gt.item.circuit_board.multifiberglass_elite.tooltip",
                o));
        ItemList.Circuit_Board_Wetware_Extreme.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Wetware_Extreme.ID,
                "gt.item.circuit_board.wetware_extreme.name",
                "gt.item.circuit_board.wetware_extreme.tooltip",
                o));
        ItemList.Circuit_Board_Plastic_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Plastic_Advanced.ID,
                "gt.item.circuit_board.plastic_advanced.name",
                "gt.item.circuit_board.plastic_advanced.tooltip",
                o));
        ItemList.Circuit_Board_Bio_Ultra.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Bio_Ultra.ID,
                "gt.item.circuit_board.bio_ultra.name",
                "gt.item.circuit_board.bio_ultra.tooltip",
                o));
        ItemList.Circuit_Board_Optical.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Optical.ID,
                "gt.item.circuit_board.optical.name",
                "gt.item.circuit_board.optical.tooltip",
                o));

        // Optical circuits
        ItemList.Circuit_OpticalProcessor.set(
            addItemWithLocalizationKeys(
                Circuit_OpticalProcessor.ID,
                "gt.item.circuit.optical_processor.name",
                "gt.item.circuit.optical_processor.tooltip",
                OrePrefixes.circuit.get(Materials.UV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_OpticalAssembly.set(
            addItemWithLocalizationKeys(
                Circuit_OpticalAssembly.ID,
                "gt.item.circuit.optical_assembly.name",
                "gt.item.circuit.optical_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_OpticalComputer.set(
            addItemWithLocalizationKeys(
                Circuit_OpticalComputer.ID,
                "gt.item.circuit.optical_computer.name",
                "gt.item.circuit.optical_computer.tooltip",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_OpticalMainframe.set(
            addItemWithLocalizationKeys(
                Circuit_OpticalMainframe.ID,
                "gt.item.circuit.optical_mainframe.name",
                "gt.item.circuit.optical_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));

        // Exotic circuits
        ItemList.Circuit_ExoticProcessor.set(
            addItemWithLocalizationKeys(
                Circuit_ExoticProcessor.ID,
                "gt.item.circuit.exotic_processor.name",
                "gt.item.circuit.exotic_processor.tooltip",
                OrePrefixes.circuit.get(Materials.UHV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_ExoticAssembly.set(
            addItemWithLocalizationKeys(
                Circuit_ExoticAssembly.ID,
                "gt.item.circuit.exotic_assembly.name",
                "gt.item.circuit.exotic_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_ExoticComputer.set(
            addItemWithLocalizationKeys(
                Circuit_ExoticComputer.ID,
                "gt.item.circuit.exotic_computer.name",
                "gt.item.circuit.exotic_computer.tooltip",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_ExoticMainframe.set(
            addItemWithLocalizationKeys(
                Circuit_ExoticMainframe.ID,
                "gt.item.circuit.exotic_mainframe.name",
                "gt.item.circuit.exotic_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.UMV),
                SubTag.NO_UNIFICATION));

        // Cosmic circuits
        ItemList.Circuit_CosmicProcessor.set(
            addItemWithLocalizationKeys(
                Circuit_CosmicProcessor.ID,
                "gt.item.circuit.cosmic_processor.name",
                "gt.item.circuit.cosmic_processor.tooltip",
                OrePrefixes.circuit.get(Materials.UEV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_CosmicAssembly.set(
            addItemWithLocalizationKeys(
                Circuit_CosmicAssembly.ID,
                "gt.item.circuit.cosmic_assembly.name",
                "gt.item.circuit.cosmic_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_CosmicComputer.set(
            addItemWithLocalizationKeys(
                Circuit_CosmicComputer.ID,
                "gt.item.circuit.cosmic_computer.name",
                "gt.item.circuit.cosmic_computer.tooltip",
                OrePrefixes.circuit.get(Materials.UMV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_CosmicMainframe.set(
            addItemWithLocalizationKeys(
                Circuit_CosmicMainframe.ID,
                "gt.item.circuit.cosmic_mainframe.name",
                "gt.item.circuit.cosmic_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.UXV),
                SubTag.NO_UNIFICATION));

        // Transcendent circuits
        ItemList.Circuit_TranscendentProcessor.set(
            addItemWithLocalizationKeys(
                Circuit_TranscendentProcessor.ID,
                "gt.item.circuit.transcendent_processor.name",
                "gt.item.circuit.transcendent_processor.tooltip",
                OrePrefixes.circuit.get(Materials.UIV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_TranscendentAssembly.set(
            addItemWithLocalizationKeys(
                Circuit_TranscendentAssembly.ID,
                "gt.item.circuit.transcendent_assembly.name",
                "gt.item.circuit.transcendent_assembly.tooltip",
                OrePrefixes.circuit.get(Materials.UMV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_TranscendentComputer.set(
            addItemWithLocalizationKeys(
                Circuit_TranscendentComputer.ID,
                "gt.item.circuit.transcendent_computer.name",
                "gt.item.circuit.transcendent_computer.tooltip",
                OrePrefixes.circuit.get(Materials.UXV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_TranscendentMainframe.set(
            addItemWithLocalizationKeys(
                Circuit_TranscendentMainframe.ID,
                "gt.item.circuit.transcendent_mainframe.name",
                "gt.item.circuit.transcendent_mainframe.tooltip",
                OrePrefixes.circuit.get(Materials.MAX),
                SubTag.NO_UNIFICATION));

        ItemList.Tube_Wires.set(
            addItemWithLocalizationKeys(Tube_Wires.ID, "gt.item.tube_wires.name", "gt.item.tube_wires.tooltip", o));

        ItemList.Cover_SolarPanel_UHV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_UHV.ID,
                "gt.item.cover.solar_panel.uhv.name",
                "gt.item.cover.solar_panel.uhv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 128L)));
        ItemList.Cover_SolarPanel_UEV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_UEV.ID,
                "gt.item.cover.solar_panel.uev.name",
                "gt.item.cover.solar_panel.uev.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 256L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 256L)));
        ItemList.Cover_SolarPanel_UIV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_UIV.ID,
                "gt.item.cover.solar_panel.uiv.name",
                "gt.item.cover.solar_panel.uiv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 512L)));

        ItemList.ULV_Coil
            .set(addItemWithLocalizationKeys(ULV_Coil.ID, "gt.item.coil.ulv.name", "gt.item.coil.ulv.tooltip", o));
        ItemList.LV_Coil
            .set(addItemWithLocalizationKeys(LV_Coil.ID, "gt.item.coil.lv.name", "gt.item.coil.lv.tooltip", o));
        ItemList.MV_Coil
            .set(addItemWithLocalizationKeys(MV_Coil.ID, "gt.item.coil.mv.name", "gt.item.coil.mv.tooltip", o));
        ItemList.HV_Coil
            .set(addItemWithLocalizationKeys(HV_Coil.ID, "gt.item.coil.hv.name", "gt.item.coil.hv.tooltip", o));
        ItemList.EV_Coil
            .set(addItemWithLocalizationKeys(EV_Coil.ID, "gt.item.coil.ev.name", "gt.item.coil.ev.tooltip", o));
        ItemList.IV_Coil
            .set(addItemWithLocalizationKeys(IV_Coil.ID, "gt.item.coil.iv.name", "gt.item.coil.iv.tooltip", o));
        ItemList.LuV_Coil
            .set(addItemWithLocalizationKeys(LuV_Coil.ID, "gt.item.coil.luv.name", "gt.item.coil.luv.tooltip", o));
        ItemList.ZPM_Coil
            .set(addItemWithLocalizationKeys(ZPM_Coil.ID, "gt.item.coil.zpm.name", "gt.item.coil.zpm.tooltip", o));
        ItemList.UV_Coil
            .set(addItemWithLocalizationKeys(UV_Coil.ID, "gt.item.coil.uv.name", "gt.item.coil.uv.tooltip", o));
        ItemList.UHV_Coil
            .set(addItemWithLocalizationKeys(UHV_Coil.ID, "gt.item.coil.uhv.name", "gt.item.coil.uhv.tooltip", o));
        ItemList.UEV_Coil
            .set(addItemWithLocalizationKeys(UEV_Coil.ID, "gt.item.coil.uev.name", "gt.item.coil.uev.tooltip", o));
        ItemList.UIV_Coil
            .set(addItemWithLocalizationKeys(UIV_Coil.ID, "gt.item.coil.uiv.name", "gt.item.coil.uiv.tooltip", o));
        ItemList.UMV_Coil
            .set(addItemWithLocalizationKeys(UMV_Coil.ID, "gt.item.coil.umv.name", "gt.item.coil.umv.tooltip", o));
        ItemList.UXV_Coil
            .set(addItemWithLocalizationKeys(UXV_Coil.ID, "gt.item.coil.uxv.name", "gt.item.coil.uxv.tooltip", o));
        ItemList.MAX_Coil
            .set(addItemWithLocalizationKeys(MAX_Coil.ID, "gt.item.coil.max.name", "gt.item.coil.max.tooltip", o));

        ItemList.GalliumArsenideCrystal.set(
            addItemWithLocalizationKeys(
                GalliumArsenideCrystal.ID,
                "gt.item.gallium_arsenide_crystal.name",
                "gt.item.gallium_arsenide_crystal.tooltip",
                o));
        ItemList.GalliumArsenideCrystalSmallPart.set(
            addItemWithLocalizationKeys(
                GalliumArsenideCrystalSmallPart.ID,
                "gt.item.small_gallium_arsenide_crystal.name",
                "gt.item.small_gallium_arsenide_crystal.tooltip",
                o));
        ItemList.KevlarFiber.set(
            addItemWithLocalizationKeys(
                KevlarFiber.ID,
                "gt.item.kevlar_fiber.name",
                "gt.item.kevlar_fiber.tooltip",
                o));
        ItemList.WovenKevlar.set(
            addItemWithLocalizationKeys(
                WovenKevlar.ID,
                "gt.item.woven_kevlar.name",
                "gt.item.woven_kevlar.tooltip",
                o));
        ItemList.Spinneret
            .set(addItemWithLocalizationKeys(Spinneret.ID, "gt.item.spinneret.name", "gt.item.spinneret.tooltip", o));

        ItemList.IndustrialApiary_Upgrade_Frame.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Frame.ID,
                "gt.item.apiary_upgrade.frame.name",
                "gt.item.apiary_upgrade.frame.tooltip",
                o));

        ItemList.IndustrialApiary_Upgrade_Acceleration_1.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_1.ID,
                "gt.item.apiary_upgrade.acceleration_1.name",
                "gt.item.apiary_upgrade.acceleration_1.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_2.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_2.ID,
                "gt.item.apiary_upgrade.acceleration_2.name",
                "gt.item.apiary_upgrade.acceleration_2.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_3.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_3.ID,
                "gt.item.apiary_upgrade.acceleration_3.name",
                "gt.item.apiary_upgrade.acceleration_3.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_4.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_4.ID,
                "gt.item.apiary_upgrade.acceleration_4.name",
                "gt.item.apiary_upgrade.acceleration_4.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_5.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_5.ID,
                "gt.item.apiary_upgrade.acceleration_5.name",
                "gt.item.apiary_upgrade.acceleration_5.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_6.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_6.ID,
                "gt.item.apiary_upgrade.acceleration_6.name",
                "gt.item.apiary_upgrade.acceleration_6.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_7.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_7.ID,
                "gt.item.apiary_upgrade.acceleration_7.name",
                "gt.item.apiary_upgrade.acceleration_7.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_8.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_8.ID,
                "gt.item.apiary_upgrade.acceleration_8.name",
                "gt.item.apiary_upgrade.acceleration_8.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_Acceleration_8_Upgraded.ID,
                "gt.item.apiary_upgrade.acceleration_8_upgraded.name",
                "gt.item.apiary_upgrade.acceleration_8_upgraded.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_PRODUCTION.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_PRODUCTION.ID,
                "gt.item.apiary_upgrade.production.name",
                "gt.item.apiary_upgrade.production.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_PLAINS.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_PLAINS.ID,
                "gt.item.apiary_upgrade.plains.name",
                "gt.item.apiary_upgrade.plains.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_LIGHT.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_LIGHT.ID,
                "gt.item.apiary_upgrade.light.name",
                "gt.item.apiary_upgrade.light.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_FLOWERING.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_FLOWERING.ID,
                "gt.item.apiary_upgrade.flowering.name",
                "gt.item.apiary_upgrade.flowering.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_WINTER.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_WINTER.ID,
                "gt.item.apiary_upgrade.winter.name",
                "gt.item.apiary_upgrade.winter.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_DRYER.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_DRYER.ID,
                "gt.item.apiary_upgrade.dryer.name",
                "gt.item.apiary_upgrade.dryer.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_AUTOMATION.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_AUTOMATION.ID,
                "gt.item.apiary_upgrade.automation.name",
                "gt.item.apiary_upgrade.automation.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_HUMIDIFIER.ID,
                "gt.item.apiary_upgrade.humidifier.name",
                "gt.item.apiary_upgrade.humidifier.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_HELL.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_HELL.ID,
                "gt.item.apiary_upgrade.hell.name",
                "gt.item.apiary_upgrade.hell.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_POLLEN.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_POLLEN.ID,
                "gt.item.apiary_upgrade.pollen.name",
                "gt.item.apiary_upgrade.pollen.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_DESERT.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_DESERT.ID,
                "gt.item.apiary_upgrade.desert.name",
                "gt.item.apiary_upgrade.desert.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_COOLER.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_COOLER.ID,
                "gt.item.apiary_upgrade.cooler.name",
                "gt.item.apiary_upgrade.cooler.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_LIFESPAN.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_LIFESPAN.ID,
                "gt.item.apiary_upgrade.lifespan.name",
                "gt.item.apiary_upgrade.lifespan.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_SEAL.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_SEAL.ID,
                "gt.item.apiary_upgrade.seal.name",
                "gt.item.apiary_upgrade.seal.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_STABILIZER.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_STABILIZER.ID,
                "gt.item.apiary_upgrade.stabilizer.name",
                "gt.item.apiary_upgrade.stabilizer.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_JUNGLE.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_JUNGLE.ID,
                "gt.item.apiary_upgrade.jungle.name",
                "gt.item.apiary_upgrade.jungle.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_TERRITORY.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_TERRITORY.ID,
                "gt.item.apiary_upgrade.territory.name",
                "gt.item.apiary_upgrade.territory.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_OCEAN.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_OCEAN.ID,
                "gt.item.apiary_upgrade.ocean.name",
                "gt.item.apiary_upgrade.ocean.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_SKY.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_SKY.ID,
                "gt.item.apiary_upgrade.sky.name",
                "gt.item.apiary_upgrade.sky.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_HEATER.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_HEATER.ID,
                "gt.item.apiary_upgrade.heater.name",
                "gt.item.apiary_upgrade.heater.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_SIEVE.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_SIEVE.ID,
                "gt.item.apiary_upgrade.sieve.name",
                "gt.item.apiary_upgrade.sieve.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));
        ItemList.IndustrialApiary_Upgrade_UNLIGHT.set(
            addItemWithLocalizationKeys(
                IndustrialApiary_Upgrade_UNLIGHT.ID,
                "gt.item.apiary_upgrade.unlight.name",
                "gt.item.apiary_upgrade.unlight.tooltip",
                OrePrefixes.apiaryUpgrade.getName()));

        ItemList.NuclearStar
            .set(
                addItemWithLocalizationKeys(
                    NuclearStar.ID,
                    "gt.item.nuclear_star.name",
                    "gt.item.nuclear_star.tooltip",
                    SubTag.NO_UNIFICATION))
            .setRender(new InfinityMetaItemRenderer());

        ItemList.Cover_Metrics_Transmitter.set(
            addItemWithLocalizationKeys(
                Cover_Metrics_Transmitter.ID,
                "gt.item.cover.metrics_transmitter.name",
                "gt.item.cover.metrics_transmitter.tooltip"));

        ItemList.ActivatedCarbonFilterMesh.set(
            addItemWithLocalizationKeys(
                Activated_Carbon_Filter_Mesh.ID,
                "gt.item.activated_carbon_filter_mesh.name",
                "gt.item.activated_carbon_filter_mesh.tooltip",
                SubTag.NO_UNIFICATION));

        ItemList.Quark_Catalyst_Housing.set(
            addItemWithLocalizationKeys(
                Quark_Catalyst_Housing.ID,
                "gt.item.quark_catalyst_housing.empty.name",
                "gt.item.quark_catalyst_housing.empty.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Up.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Up.ID,
                "gt.item.quark_catalyst.up.name",
                "gt.item.quark_catalyst.up.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Down.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Down.ID,
                "gt.item.quark_catalyst.down.name",
                "gt.item.quark_catalyst.down.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Strange.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Strange.ID,
                "gt.item.quark_catalyst.strange.name",
                "gt.item.quark_catalyst.strange.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Charm.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Charm.ID,
                "gt.item.quark_catalyst.charm.name",
                "gt.item.quark_catalyst.charm.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Bottom.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Bottom.ID,
                "gt.item.quark_catalyst.bottom.name",
                "gt.item.quark_catalyst.bottom.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Top.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Top.ID,
                "gt.item.quark_catalyst.top.name",
                "gt.item.quark_catalyst.top.tooltip",
                SubTag.NO_UNIFICATION));
        ItemList.Quark_Creation_Catalyst_Unaligned.set(
            addItemWithLocalizationKeys(
                Quark_Creation_Catalyst_Unaligned.ID,
                "gt.item.quark_catalyst.unaligned.name",
                "gt.item.quark_catalyst.unaligned.tooltip",
                SubTag.NO_UNIFICATION));

        ItemList.Optical_Cpu_Containment_Housing.set(
            addItemWithLocalizationKeys(
                Optical_Cpu_Containment_Housing.ID,
                "gt.item.optical_cpu_containment_housing.name",
                "gt.item.optical_cpu_containment_housing.tooltip",
                o));
        ItemList.Optically_Perfected_CPU.set(
            addItemWithLocalizationKeys(
                Optically_Perfected_CPU.ID,
                "gt.item.optically_perfected_cpu.name",
                "gt.item.optically_perfected_cpu.tooltip",
                o));
        ItemList.Optically_Compatible_Memory.set(
            addItemWithLocalizationKeys(
                Optically_Compatible_Memory.ID,
                "gt.item.optically_compatible_memory.name",
                "gt.item.optically_compatible_memory.tooltip",
                o));

        ItemList.Timepiece
            .set(addItemWithLocalizationKeys(Timepiece.ID, "gt.item.timepiece.name", "gt.item.timepiece.tooltip", o))
            .setRender(new GlitchEffectMetaItemRenderer());
        ItemList.Transdimensional_Alignment_Matrix.set(
            addItemWithLocalizationKeys(
                Transdimensional_Alignment_Matrix.ID,
                "gt.item.transdimensional_alignment_matrix.name",
                "gt.item.transdimensional_alignment_matrix.tooltip",
                o));

        ItemList.Thermal_Superconductor.set(
            addItemWithLocalizationKeys(
                Thermal_Superconductor.ID,
                "gt.item.thermal_superconductor.name",
                "gt.item.thermal_superconductor.tooltip",
                o));
        ItemList.Relativistic_Heat_Capacitor.set(
            addItemWithLocalizationKeys(
                Relativistic_Heat_Capacitor.ID,
                "gt.item.relativistic_heat_capacitor.name",
                "gt.item.relativistic_heat_capacitor.tooltip",
                o));
        ItemList.Phononic_Seed_Crystal
            .set(
                addItemWithLocalizationKeys(
                    Phononic_Seed_Crystal.ID,
                    "gt.item.phononic_seed_crystal.name",
                    "gt.item.phononic_seed_crystal.tooltip",
                    o))
            .setRender(new GlitchEffectMetaItemRenderer());
        ItemList.Harmonic_Compound
            .set(
                addItemWithLocalizationKeys(
                    Harmonic_Compound.ID,
                    "gt.item.harmonic_compound.name",
                    "gt.item.harmonic_compound.tooltip",
                    "ingotHotHarmonic"))
            .setRender(new InfinityMetaItemRenderer());

        ItemList.Heavy_Hellish_Mud.set(
            addItemWithLocalizationKeys(
                Heavy_Hellish_Mud.ID,
                "gt.item.heavy_hellish_mud.name",
                "gt.item.heavy_hellish_mud.tooltip"));
        ItemList.Netherite_Scrap_Seed.set(
            addItemWithLocalizationKeys(
                Netherite_Scrap_Seed.ID,
                "gt.item.netherite_scrap_seed.name",
                "gt.item.netherite_scrap_seed.tooltip"));
        ItemList.Brittle_Netherite_Scrap.set(
            addItemWithLocalizationKeys(
                Brittle_Netherite_Scrap.ID,
                "gt.item.brittle_netherite_scrap.name",
                "gt.item.brittle_netherite_scrap.tooltip"));
        ItemList.Netherite_Nanoparticles.set(
            addItemWithLocalizationKeys(
                Netherite_Nanoparticles.ID,
                "gt.item.netherite_nanoparticles.name",
                "gt.item.netherite_nanoparticles.tooltip"));
        ItemList.Intensely_Bonded_Netherite_Nanoparticles.set(
            addItemWithLocalizationKeys(
                Intensely_Bonded_Netherite_Nanoparticles.ID,
                "gt.item.intensely_bonded_netherite_nanoparticles.name",
                "gt.item.intensely_bonded_netherite_nanoparticles.tooltip"));
        ItemList.Hot_Netherite_Scrap.set(
            addItemWithLocalizationKeys(
                Hot_Netherite_Scrap.ID,
                "gt.item.hot_netherite_scrap.name",
                "gt.item.hot_netherite_scrap.tooltip"));
        ItemList.Beryllium_Shielding_Plate.set(
            addItemWithLocalizationKeys(
                Beryllium_Shielding_Plate.ID,
                "gt.item.beryllium_shielding_plate.name",
                "gt.item.beryllium_shielding_plate.tooltip"));
        ItemList.Alumina_Support_Ring.set(
            addItemWithLocalizationKeys(
                Alumina_Support_Ring.ID,
                "gt.item.alumina_support_ring.name",
                "gt.item.alumina_support_ring.tooltip"));
        ItemList.Alumina_Support_Ring_Raw.set(
            addItemWithLocalizationKeys(
                Alumina_Support_Ring_Raw.ID,
                "gt.item.alumina_support_ring_raw.name",
                "gt.item.alumina_support_ring_raw.tooltip"));
        ItemList.Prismarine_Precipitate.set(
            addItemWithLocalizationKeys(
                Prismarine_Precipitate.ID,
                "gt.item.prismarine_precipitate.name",
                "gt.item.prismarine_precipitate.tooltip"));
        ItemList.Prismatic_Crystal.set(
            addItemWithLocalizationKeys(
                Prismatic_Crystal.ID,
                "gt.item.prismatic_crystal.name",
                "gt.item.prismatic_crystal.tooltip"));
        ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet
            .set(
                addItemWithLocalizationKeys(
                    Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.ID,
                    "gt.item.radiation_proof_composite_sheet.name",
                    "gt.item.radiation_proof_composite_sheet.tooltip"))
            .setRender(new RainbowOverlayMetaItemRenderer(new short[] { 255, 255, 255, 255 }));
        ItemList.Naquarite_Universal_Insulator_Foil.set(
            addItemWithLocalizationKeys(
                Naquarite_Universal_Insulator_Foil.ID,
                "gt.item.naquarite_universal_insulator_foil.name",
                "gt.item.naquarite_universal_insulator_foil.tooltip"));

        ItemList.ManaFly.set(
            addItemWithLocalizationKeys(
                Manafly.ID,
                "gt.item.manafly.name",
                "gt.item.manafly.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 10L)));

        ItemList.StableBaryonContainmentUnit.set(
            addItemWithLocalizationKeys(
                StableBaryonContainmentUnit.ID,
                "gt.item.stable_baryon_containment_unit.name",
                "gt.item.stable_baryon_containment_unit.tooltip"));
        ItemList.StableLeptonContainmentUnit.set(
            addItemWithLocalizationKeys(
                StableLeptonContainmentUnit.ID,
                "gt.item.stable_lepton_containment_unit.name",
                "gt.item.stable_lepton_containment_unit.tooltip"));
        ItemList.StableMesonContainmentUnit.set(
            addItemWithLocalizationKeys(
                StableMesonContainmentUnit.ID,
                "gt.item.stable_meson_containment_unit.name",
                "gt.item.stable_meson_containment_unit.tooltip"));
        ItemList.StableBosonContainmentUnit.set(
            addItemWithLocalizationKeys(
                StableBosonContainmentUnit.ID,
                "gt.item.stable_boson_containment_unit.name",
                "gt.item.stable_boson_containment_unit.tooltip"));

        ItemList.StableEmptyContainmentUnit.set(
            addItemWithLocalizationKeys(
                StableEmptyContainmentUnit.ID,
                "gt.item.stable_empty_containment_unit.name",
                "gt.item.stable_empty_containment_unit.tooltip"));

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
        addItemTooltip(
            ItemList.Transdimensional_Alignment_Matrix.get(1),
            GTAuthors.buildAuthorsWithFormatSupplier(GTAuthors.AuthorCloud));
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
        CoverRegistry.registerCover(
            ItemList.Cover_Metrics_Transmitter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_METRICS_TRANSMITTER)),
            context -> new CoverMetricsTransmitter(context, TextureFactory.of(OVERLAY_METRICS_TRANSMITTER)),
            CoverPlacer.builder()
                .onlyPlaceIf(CoverMetricsTransmitter::isCoverPlaceable)
                .build());
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_UHV.get(1L),
            TextureFactory.of(SOLARPANEL_UHV),
            context -> new CoverSolarPanel(context, 2097152),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_UEV.get(1L),
            TextureFactory.of(SOLARPANEL_UEV),
            context -> new CoverSolarPanel(context, 8388608),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_UIV.get(1L),
            TextureFactory.of(SOLARPANEL_UIV),
            context -> new CoverSolarPanel(context, 33554432),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
    }

    @Override
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        String pref = aPrefix.toString()
            .toLowerCase();
        return aDoShowAllItems || pref.contains("nanite")
            || pref.contains("rawore")
            || pref.contains("platesuperdense");
    }

    @Override
    @Optional.Method(modid = Mods.ModIDs.RAILCRAFT)
    public boolean shouldBurn(ItemStack itemStack) {
        ItemData data = GTOreDictUnificator.getAssociation(itemStack);
        if (data == null || data.mMaterial == null || data.mPrefix == null) {
            return false;
        }
        return data.mMaterial.mMaterial == Materials.Firestone && data.mPrefix == OrePrefixes.rawOre;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);

        ItemData data = GTOreDictUnificator.getItemData(aStack);

        if (data != null && data.mPrefix == OrePrefixes.nanite) {
            NaniteTier tier = NaniteTier.fromStack(aStack);

            if (tier != null) {
                aList.add(GTUtility.translate("gt.tooltip.nanite-tier", tier.tier));
            }
        }
    }
}
