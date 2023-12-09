package gtPlusPlus.xmod.gregtech.common.items;

import static gregtech.client.GT_TooltipHandler.Tier.EV;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;
import static gtPlusPlus.core.util.Utils.getTcAspectStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_MultiTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaItem_X32;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_Overflow;

public class MetaGeneratedGregtechItems extends Gregtech_MetaItem_X32 {

    public static final MetaGeneratedGregtechItems INSTANCE;

    static {
        INSTANCE = new MetaGeneratedGregtechItems();
    }

    public MetaGeneratedGregtechItems() {
        super("MU-metaitem.01", new OrePrefixes[] { null });
    }

    public void generateMetaItems() {
        int tLastID = 0;

        registerCustomCircuits();

        // Extruder Shape
        GregtechItemList.Shape_Extruder_WindmillShaft
                .set(this.addItem(40, "Extruder Shape (Shaft)", "Extruder Shape for making Windmill Shafts"));

        // Batteries
        GregtechItemList.Battery_RE_EV_Sodium.set(
                this.addItem(
                        tLastID = 50,
                        "Quad Cell Sodium Battery",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 2L),
                        getTcAspectStack(TC_Aspects.METALLUM, 2L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 2L)));
        this.setElectricStats(32000 + tLastID, 3200000L, GT_Values.V[4], 4L, -3L, true);
        registerTieredTooltip(GregtechItemList.Battery_RE_EV_Sodium.get(1), EV);

        GregtechItemList.Battery_RE_EV_Cadmium.set(
                this.addItem(
                        tLastID = 52,
                        "Quad Cell Cadmium Battery",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                        getTcAspectStack(TC_Aspects.METALLUM, 1L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 1L)));
        this.setElectricStats(32000 + tLastID, 4800000L, GT_Values.V[4], 4L, -3L, true);
        registerTieredTooltip(GregtechItemList.Battery_RE_EV_Cadmium.get(1), EV);

        GregtechItemList.Battery_RE_EV_Lithium.set(
                this.addItem(
                        tLastID = 54,
                        "Quad Cell Lithium Battery",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 3L),
                        getTcAspectStack(TC_Aspects.METALLUM, 3L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 3L)));
        this.setElectricStats(32000 + tLastID, 6400000L, GT_Values.V[4], 4L, -3L, true);
        registerTieredTooltip(GregtechItemList.Battery_RE_EV_Lithium.get(1), EV);

        /**
         * Power Gems
         */
        GregtechItemList.Battery_Gem_1.set(
                this.addItem(
                        tLastID = 66,
                        "Proton Cell",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 8L),
                        getTcAspectStack(TC_Aspects.METALLUM, 24L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 16L)));
        this.setElectricStats(32000 + tLastID, GT_Values.V[6] * 20 * 300 / 4, GT_Values.V[6], 6L, -3L, false);
        GregtechItemList.Battery_Gem_2.set(
                this.addItem(
                        tLastID = 68,
                        "Electron Cell",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 16L),
                        getTcAspectStack(TC_Aspects.METALLUM, 32L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 32L)));
        this.setElectricStats(32000 + tLastID, GT_Values.V[7] * 20 * 300 / 4, GT_Values.V[7], 7L, -3L, false);
        GregtechItemList.Battery_Gem_3.set(
                this.addItem(
                        tLastID = 70,
                        "Quark Entanglement",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 32L),
                        getTcAspectStack(TC_Aspects.METALLUM, 48L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 64L)));
        this.setElectricStats(32000 + tLastID, GT_Values.V[8] * 20 * 300 / 4, GT_Values.V[8], 8L, -3L, false);

        // RTG Pellet
        GregtechItemList.Pellet_RTG_PU238.set(
                this.addItem(
                        41,
                        StringUtils.superscript("238") + "Pu Pellet",
                        "",
                        getTcAspectStack(TC_Aspects.RADIO, 4L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                        getTcAspectStack(TC_Aspects.METALLUM, 2L)));
        GregtechItemList.Pellet_RTG_SR90.set(
                this.addItem(
                        42,
                        StringUtils.superscript("90") + "Sr Pellet",
                        "",
                        getTcAspectStack(TC_Aspects.RADIO, 4L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                        getTcAspectStack(TC_Aspects.METALLUM, 2L)));
        GregtechItemList.Pellet_RTG_PO210.set(
                this.addItem(
                        43,
                        StringUtils.superscript("210") + "Po Pellet",
                        "",
                        getTcAspectStack(TC_Aspects.RADIO, 4L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                        getTcAspectStack(TC_Aspects.METALLUM, 2L)));
        GregtechItemList.Pellet_RTG_AM241.set(
                this.addItem(
                        44,
                        StringUtils.superscript("241") + "Am Pellet",
                        "",
                        getTcAspectStack(TC_Aspects.RADIO, 4L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                        getTcAspectStack(TC_Aspects.METALLUM, 2L)));

        CORE.RA.addFuelForRTG(GregtechItemList.Pellet_RTG_PU238.get(1), MathUtils.roundToClosestInt(87.7f), 64);
        CORE.RA.addFuelForRTG(GregtechItemList.Pellet_RTG_SR90.get(1), MathUtils.roundToClosestInt(28.8f), 32);
        CORE.RA.addFuelForRTG(GregtechItemList.Pellet_RTG_PO210.get(1), 1, 512);
        CORE.RA.addFuelForRTG(GregtechItemList.Pellet_RTG_AM241.get(1), 216, 16);
        CORE.RA.addFuelForRTG(GT_ModHandler.getIC2Item("RTGPellets", 1), MathUtils.roundToClosestInt(2.6f), 8);

        // Computer Cube
        GregtechItemList.Gregtech_Computer_Cube.set(
                this.addItem(
                        tLastID = 55,
                        "Gregtech Computer Cube",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 8L),
                        getTcAspectStack(TC_Aspects.METALLUM, 8L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 8L)));
        this.setElectricStats(32000 + tLastID, GT_Values.V[6] * 10 * 60 * 20, GT_Values.V[5], 5L, -3L, true);

        GregtechItemList.Cover_Overflow_LV.set(
                this.addItem(
                        72,
                        "Overflow Valve (LV)",
                        "Maximum void amount: 64,000",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                        getTcAspectStack(TC_Aspects.MACHINA, 1L),
                        getTcAspectStack(TC_Aspects.ITER, 1L),
                        getTcAspectStack(TC_Aspects.AQUA, 1L)));
        GregtechItemList.Cover_Overflow_MV.set(
                this.addItem(
                        73,
                        "Overflow Valve (MV)",
                        "Maximum void amount: 512,000",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                        getTcAspectStack(TC_Aspects.MACHINA, 1L),
                        getTcAspectStack(TC_Aspects.ITER, 1L),
                        getTcAspectStack(TC_Aspects.AQUA, 1L)));
        GregtechItemList.Cover_Overflow_HV.set(
                this.addItem(
                        74,
                        "Overflow Valve (HV)",
                        "Maximum void amount: 4,096,000",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                        getTcAspectStack(TC_Aspects.MACHINA, 1L),
                        getTcAspectStack(TC_Aspects.ITER, 1L),
                        getTcAspectStack(TC_Aspects.AQUA, 1L)));
        GregtechItemList.Cover_Overflow_EV.set(
                this.addItem(
                        75,
                        "Overflow Valve (EV)",
                        "Maximum void amount: 32,768,000",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                        getTcAspectStack(TC_Aspects.MACHINA, 1L),
                        getTcAspectStack(TC_Aspects.ITER, 1L),
                        getTcAspectStack(TC_Aspects.AQUA, 1L)));
        GregtechItemList.Cover_Overflow_IV.set(
                this.addItem(
                        76,
                        "Overflow Valve (IV)",
                        "Maximum void amount: 262,144,000",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                        getTcAspectStack(TC_Aspects.MACHINA, 1L),
                        getTcAspectStack(TC_Aspects.ITER, 1L),
                        getTcAspectStack(TC_Aspects.AQUA, 1L)));

        GregTech_API.registerCover(
                GregtechItemList.Cover_Overflow_LV.get(1L),
                new GT_MultiTexture(
                        Textures.BlockIcons.MACHINE_CASINGS[4][0],
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)),
                new GTPP_Cover_Overflow(64));
        GregTech_API.registerCover(
                GregtechItemList.Cover_Overflow_MV.get(1L),
                new GT_MultiTexture(
                        Textures.BlockIcons.MACHINE_CASINGS[5][0],
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)),
                new GTPP_Cover_Overflow(512));
        GregTech_API.registerCover(
                GregtechItemList.Cover_Overflow_HV.get(1L),
                new GT_MultiTexture(
                        Textures.BlockIcons.MACHINE_CASINGS[5][0],
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)),
                new GTPP_Cover_Overflow(4096));
        GregTech_API.registerCover(
                GregtechItemList.Cover_Overflow_EV.get(1L),
                new GT_MultiTexture(
                        Textures.BlockIcons.MACHINE_CASINGS[8][0],
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)),
                new GTPP_Cover_Overflow(32768));
        GregTech_API.registerCover(
                GregtechItemList.Cover_Overflow_IV.get(1L),
                new GT_MultiTexture(
                        Textures.BlockIcons.MACHINE_CASINGS[8][0],
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)),
                new GTPP_Cover_Overflow(262144));

        // Fusion Reactor MK4 Singularity
        GregtechItemList.Compressed_Fusion_Reactor.set(
                this.addItem(
                        100,
                        "Hypervisor Matrix (Fusion)",
                        "A memory unit containing an RI (Restricted Intelligence)"));

        GregtechItemList.Laser_Lens_Special
                .set(this.addItem(105, "Quantum Anomaly", "Probably should shoot it with lasers"));

        GregtechItemList.Battery_Casing_Gem_1.set(this.addItem(106, "Containment Unit I", "Used in crafting"));
        GregtechItemList.Battery_Casing_Gem_2.set(this.addItem(107, "Containment Unit II", "Used in crafting"));
        GregtechItemList.Battery_Casing_Gem_3.set(this.addItem(108, "Advanced Containment Unit", "Used in crafting"));
        GregtechItemList.Battery_Casing_Gem_4.set(this.addItem(109, "Exotic Containment Unit", "Used in crafting"));

        GregtechItemList.Battery_Gem_4.set(
                this.addItem(
                        tLastID = 110,
                        "Graviton Anomaly",
                        "Reusable",
                        getTcAspectStack(TC_Aspects.ELECTRUM, 64L),
                        getTcAspectStack(TC_Aspects.METALLUM, 64L),
                        getTcAspectStack(TC_Aspects.POTENTIA, 64L)));
        this.setElectricStats(32000 + tLastID, (64000000000L * 16), GT_Values.V[9], 9L, -3L, false);

        /*
         * High Tier 'Saws' for the tree Farm
         */

        GregtechItemList.Laser_Lens_WoodsGlass.set(
                this.addItem(
                        140,
                        "Wood's Glass Lens",
                        "Allows UV & IF to pass through, blocks visible light spectrums"));

        // 141 now unused, was the ulv transmission component
        int aStartID = 142;
        GregtechItemList[] aTransParts = new GregtechItemList[] { GregtechItemList.TransmissionComponent_LV,
                GregtechItemList.TransmissionComponent_MV, GregtechItemList.TransmissionComponent_HV,
                GregtechItemList.TransmissionComponent_EV, GregtechItemList.TransmissionComponent_IV,
                GregtechItemList.TransmissionComponent_LuV, GregtechItemList.TransmissionComponent_ZPM,
                GregtechItemList.TransmissionComponent_UV, GregtechItemList.TransmissionComponent_UHV, };
        for (int tier = 1; tier < aTransParts.length + 1; tier++) {
            aTransParts[tier - 1].set(
                    this.addItem(
                            aStartID++,
                            "Transmission Component (" + GT_Values.VN[tier] + ")",
                            "",
                            getTcAspectStack(TC_Aspects.ELECTRUM, tier),
                            getTcAspectStack(TC_Aspects.MACHINA, tier),
                            getTcAspectStack(TC_Aspects.MAGNETO, tier)));
        }

        // Distillus Chip
        GregtechItemList.Distillus_Upgrade_Chip
                .set(this.addItem(151, "Distillus Upgrade Chip", "Used to upgrade Distillus to Tier 2"));
        GregtechItemList.Maceration_Upgrade_Chip
                .set(this.addItem(152, "Maceration Upgrade Chip", "Used to upgrade Maceration Stack to Tier 2"));
    }

    public void registerCustomCircuits() {
        if (CORE.ConfigSwitches.enableCustomCircuits) {
            GregtechItemList.Circuit_IV.set(
                    this.addItem(
                            704,
                            "Symbiotic Circuit (IV)",
                            "A Symbiotic Data Processor",
                            GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic)));
            GregtechItemList.Circuit_LuV.set(
                    this.addItem(
                            705,
                            "Neutronic Circuit (LuV)",
                            "A Neutron Particle Processor",
                            GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic)));
            GregtechItemList.Circuit_ZPM.set(
                    this.addItem(
                            706,
                            "Quantum Circuit (ZPM)",
                            "A Singlularity Processor",
                            GregtechOrePrefixes.circuit.get(GT_Materials.Quantum)));
            GregtechItemList.Circuit_Board_IV
                    .set(this.addItem(710, "IV Circuit Board", "An IV Voltage Rated Circuit Board"));
            GregtechItemList.Circuit_Board_LuV
                    .set(this.addItem(711, "LuV Circuit Board", "An LuV Voltage Rated Circuit Board"));
            GregtechItemList.Circuit_Board_ZPM
                    .set(this.addItem(712, "ZPM Processor Board", "A ZPM Voltage Rated Processor Board"));
            GregtechItemList.Circuit_Parts_Crystal_Chip_IV
                    .set(this.addItem(713, "(IV) Energized Crystal Chip", "Needed for Circuits"));
            GregtechItemList.Circuit_Parts_Crystal_Chip_LuV
                    .set(this.addItem(714, "(LuV) Neutron based Microchip", "Needed for Circuits"));
            GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM
                    .set(this.addItem(715, "(ZPM) Quantum Chip", "Needed for Circuits"));
            GregtechItemList.Circuit_Parts_IV.set(this.addItem(716, "(IV) Energized Circuit Parts", "Circuit Parts"));
            GregtechItemList.Circuit_Parts_LuV
                    .set(this.addItem(717, "(LuV) Neutron-based Circuit Parts", "Circuit Parts"));
            GregtechItemList.Circuit_Parts_ZPM.set(this.addItem(718, "(ZPM) Quantum Circuit Parts", "Circuit Parts"));
            GregtechItemList.Circuit_Parts_Wiring_IV
                    .set(this.addItem(719, "Etched IV Voltage Wiring", "Part of Circuit Boards"));
            GregtechItemList.Circuit_Parts_Wiring_LuV
                    .set(this.addItem(720, "Etched LuV Voltage Wiring", "Part of Circuit Boards"));
            GregtechItemList.Circuit_Parts_Wiring_ZPM
                    .set(this.addItem(721, "Etched ZPM Voltage Wiring", "Part of Circuit Boards"));
            ItemUtils.addItemToOreDictionary(GregtechItemList.Circuit_IV.get(1), "circuitSuperconductor");
            ItemUtils.addItemToOreDictionary(GregtechItemList.Circuit_LuV.get(1), "circuitInfinite");
        }
    }
}
