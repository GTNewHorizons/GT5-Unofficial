package gregtech.common.misc;

import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

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
    QFT_MANIPULATOR("manipulator"),
    QFT_SHIELDING("shielding"),
    HEATING_COIL("coil"),
    BOROGLASS("glass"),
    PRASS_UNIT_CASING("unit_casing"),
    METAL_MACHINE_CASING("casing"),
    TIER_MACHINE_CASING("machine_casing"),
    TIER_CASING("casing"),
    SOLENOID("solenoid"),
    LSC_CAPACITOR("capacitor"),
    STRUCTURE_HEIGHT("height"),
    STRUCTURE_LENGTH("length"),
    PIPE_CASING("pipe"),
    ITEM_PIPE_CASING("item_pipe"),
    PSS_CELL("cell"),
    SYNCHROTRON_ANTENNA("antenna"),
    SE_MOTOR("motor"),
    EOH_COMPRESSION("spacetime_compression"),
    EOH_STABILISATION("stabilisation"),
    EOH_DILATION("time_dilation"),
    HATCH("gt_hatch"),
    TFFT_FIELD("field"),
    EIC_PISTON("piston_block"),
    ALCHEMICAL_CASING("casing"),
    ALCHEMICAL_CONSTRUCT("construct"),
    SUPER_CHEST("super_chest"),
    MAGNETIC_CHASSIS("chassis"),
    COMPONENT_ASSEMBLYLINE_CASING("component_casing"),
    LES_ESSENTIA_CELL("essentia_cell"),
    COKE_OVEN_CASING("coke_oven_casing");
    //

    private final String channel;

    GTStructureChannels(String aChannel) {
        channel = aChannel;
    }

    @Override
    public String get() {
        return channel;
    }

    // Keyed by constant name, not channel id: several constants share the "casing" channel with different names
    @Override
    public String getDefaultTooltip() {
        return StatCollector.translateToLocal("channels.gregtech.subchannel." + name().toLowerCase(Locale.ROOT));
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
