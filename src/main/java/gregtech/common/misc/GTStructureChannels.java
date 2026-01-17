package gregtech.common.misc;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.structure.IStructureChannels;

/*
 * To unofficial addon authors: Do not add to this enum with EnumHelper or equivalent. Just copy this class into your
 * namespace, and replace the constants
 */
/*
 * Dev notes: Q1: central manage indicator item or in each blocks' constructor? A1: before this is merged #4067 happens.
 * we can build on this info and central manage it EDIT: I ended up with a registerAsIndicator() method here Q2: default
 * tooltip in MBTT builder? A2: Yes Q3: multi specific tier managed here or in individual controller? A3: here, because
 * it needs to be registered to a central location, so it would be nice to have a central location with an easy
 * overview. Plus, it's possible these multi-specific tiers would be become reused by others as development carries on,
 * e.g. PRASS_UNIT_CASING
 */
public enum GTStructureChannels implements IStructureChannels {

    // Order of enum constants does not matter
    QFT_MANIPULATOR("manipulator", "Manipulator Tier"),
    QFT_SHIELDING("shielding", "Shielding Tier"),
    HEATING_COIL("coil", "Heating Coils Tier"),
    BOROGLASS("glass", "Glass Tier"),
    PRASS_UNIT_CASING("unit_casing", "Precise Electronic Unit Casing Tier"),
    METAL_MACHINE_CASING("casing", "Metal Machine Casing Tier"),
    TIER_MACHINE_CASING("machine_casing", "Machine Casing Tier"),
    TIER_CASING("casing", "Machine Casing Tier"),
    SOLENOID("solenoid", "Solenoid Tier"),
    LSC_CAPACITOR("capacitor", "Capacitor Tier"),
    STRUCTURE_HEIGHT("height", "Structure Height"),
    STRUCTURE_LENGTH("length", "Structure Length"),
    PIPE_CASING("pipe", "Pipe Casing Tier"),
    ITEM_PIPE_CASING("item_pipe", "Item Pipe Casing Tier"),
    PSS_CELL("cell", "Vanadium Redox Power Cell Tier"),
    SYNCHROTRON_ANTENNA("antenna", "Antenna Casing Tier"),
    EOH_COMPRESSION("spacetime_compression", "Spacetime Compression Field Generator Tier"),
    EOH_STABILISATION("stabilisation", "Stabilisation Field Generator Tier"),
    EOH_DILATION("time_dilation", "Time Dilation Field Generator Tier"),
    HATCH("gt_hatch", "Hatch placement"),
    TFFT_FIELD("field", "Storage Field Tier"),
    EIC_PISTON("piston_block", "Metal Block Tier"),
    ALCHEMICAL_CASING("alchemical_casing", "Alchemical Casing Tier"),
    ALCHEMICAL_CONSTRUCT("construct", "Alchemical Construct Tier"),
    SUPER_CHEST("super_chest", "Super Chest Tier"),
    MAGNETIC_CHASSIS("chassis", "Magnetic Chassis Tier"),
    COMPONENT_ASSEMBLYLINE_CASING("component_casing", "Component Assembly Line Casing Tier")
    //
    ;

    private final String channel;
    private final String defaultTooltip;

    GTStructureChannels(String aChannel, String defaultTooltip) {
        channel = aChannel;
        this.defaultTooltip = defaultTooltip;
    }

    @Override
    public String get() {
        return channel;
    }

    @Override
    public String getDefaultTooltip() {
        return defaultTooltip;
    }

    @Override
    public void registerAsIndicator(ItemStack indicator, int channelValue) {
        StructureLibAPI.registerChannelItem(get(), Mods.ModIDs.GREG_TECH, channelValue, indicator);
    }

    public static void register() {
        for (GTStructureChannels value : values()) {
            StructureLibAPI.registerChannelDescription(
                value.get(),
                Mods.ModIDs.GREG_TECH,
                "channels." + Mods.GregTech.ID + "." + value.get());
        }
    }
}
