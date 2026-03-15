package gtPlusPlus.xmod.gregtech.common.items;

import static gregtech.api.util.GTRecipeConstants.RTG_DURATION_IN_DAYS;
import static gregtech.client.GTTooltipHandler.Tier.EV;
import static gregtech.client.GTTooltipHandler.registerTieredTooltip;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.rtgFuels;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Casing_Gem_1;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Casing_Gem_2;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Casing_Gem_3;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Casing_Gem_4;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Gem_1;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Gem_2;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Gem_3;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_Gem_4;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_RE_EV_Cadmium;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_RE_EV_Lithium;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Battery_RE_EV_Sodium;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Compressed_Fusion_Reactor;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Cover_Overflow_Valve_EV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Cover_Overflow_Valve_HV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Cover_Overflow_Valve_IV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Cover_Overflow_Valve_LV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Cover_Overflow_Valve_MV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Gregtech_Computer_Cube;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Laser_Lens_WoodsGlass;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Maceration_Upgrade_Chip;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Pellet_RTG_AM241;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Pellet_RTG_PO210;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Pellet_RTG_PU238;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Pellet_RTG_SR90;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.Shape_Extruder_WindmillShaft;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_EV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_HV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_IV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_LV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_LuV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_MV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_UHV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_UV;
import static gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedItemIDs.TransmissionComponent_ZPM;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.items.GTMetaItemX32;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.covers.CoverOverflowValve;

public class MetaGeneratedGregtechItems extends GTMetaItemX32 {

    public static final MetaGeneratedGregtechItems INSTANCE;

    static {
        INSTANCE = new MetaGeneratedGregtechItems();
    }

    public MetaGeneratedGregtechItems() {
        super("MU-metaitem.01", new OrePrefixes[] { null });
    }

    public void generateMetaItems() {

        // Extruder Shape
        GregtechItemList.Shape_Extruder_WindmillShaft.set(
            this.addItem(
                Shape_Extruder_WindmillShaft.ID,
                "Extruder Shape (Shaft)",
                "Extruder Shape for making Windmill Shafts"));

        // Batteries
        GregtechItemList.Battery_RE_EV_Sodium.set(
            this.addItem(
                Battery_RE_EV_Sodium.ID,
                "Quad Cell Sodium Battery",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TC_AspectStack(TCAspects.METALLUM, 2L),
                new TC_AspectStack(TCAspects.POTENTIA, 2L)));

        GregtechItemList.Battery_RE_EV_Cadmium.set(
            this.addItem(
                Battery_RE_EV_Cadmium.ID,
                "Quad Cell Cadmium Battery",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TC_AspectStack(TCAspects.METALLUM, 1L),
                new TC_AspectStack(TCAspects.POTENTIA, 1L)));

        GregtechItemList.Battery_RE_EV_Lithium.set(
            this.addItem(
                Battery_RE_EV_Lithium.ID,
                "Quad Cell Lithium Battery",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 3L),
                new TC_AspectStack(TCAspects.METALLUM, 3L),
                new TC_AspectStack(TCAspects.POTENTIA, 3L)));

        /**
         * Power Gems
         */
        GregtechItemList.Battery_Gem_1.set(
            this.addItem(
                Battery_Gem_1.ID,
                "Proton Cell",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TC_AspectStack(TCAspects.METALLUM, 24L),
                new TC_AspectStack(TCAspects.POTENTIA, 16L)));

        GregtechItemList.Battery_Gem_2.set(
            this.addItem(
                Battery_Gem_2.ID,
                "Electron Cell",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TC_AspectStack(TCAspects.METALLUM, 32L),
                new TC_AspectStack(TCAspects.POTENTIA, 32L)));

        GregtechItemList.Battery_Gem_3.set(
            this.addItem(
                Battery_Gem_3.ID,
                "Quark Entanglement",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TC_AspectStack(TCAspects.METALLUM, 48L),
                new TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // RTG Pellet
        GregtechItemList.Pellet_RTG_PU238.set(
            this.addItem(
                Pellet_RTG_PU238.ID,
                StringUtils.superscript("238") + "Pu Pellet",
                "",
                new TC_AspectStack(TCAspects.RADIO, 4L),
                new TC_AspectStack(TCAspects.POTENTIA, 2L),
                new TC_AspectStack(TCAspects.METALLUM, 2L)));

        GregtechItemList.Pellet_RTG_SR90.set(
            this.addItem(
                Pellet_RTG_SR90.ID,
                StringUtils.superscript("90") + "Sr Pellet",
                "",
                new TC_AspectStack(TCAspects.RADIO, 4L),
                new TC_AspectStack(TCAspects.POTENTIA, 2L),
                new TC_AspectStack(TCAspects.METALLUM, 2L)));

        GregtechItemList.Pellet_RTG_PO210.set(
            this.addItem(
                Pellet_RTG_PO210.ID,
                StringUtils.superscript("210") + "Po Pellet",
                "",
                new TC_AspectStack(TCAspects.RADIO, 4L),
                new TC_AspectStack(TCAspects.POTENTIA, 2L),
                new TC_AspectStack(TCAspects.METALLUM, 2L)));

        GregtechItemList.Pellet_RTG_AM241.set(
            this.addItem(
                Pellet_RTG_AM241.ID,
                StringUtils.superscript("241") + "Am Pellet",
                "",
                new TC_AspectStack(TCAspects.RADIO, 4L),
                new TC_AspectStack(TCAspects.POTENTIA, 2L),
                new TC_AspectStack(TCAspects.METALLUM, 2L)));

        // Computer Cube
        GregtechItemList.Gregtech_Computer_Cube.set(
            this.addItem(
                Gregtech_Computer_Cube.ID,
                "Gregtech Computer Cube",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TC_AspectStack(TCAspects.METALLUM, 8L),
                new TC_AspectStack(TCAspects.POTENTIA, 8L)));

        GregtechItemList.Cover_Overflow_Valve_LV.set(
            this.addItem(
                Cover_Overflow_Valve_LV.ID,
                "Overflow Valve (LV)",
                "Maximum void amount: 64,000",
                new TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TC_AspectStack(TCAspects.MACHINA, 1L),
                new TC_AspectStack(TCAspects.ITER, 1L),
                new TC_AspectStack(TCAspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_Valve_MV.set(
            this.addItem(
                Cover_Overflow_Valve_MV.ID,
                "Overflow Valve (MV)",
                "Maximum void amount: 512,000",
                new TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TC_AspectStack(TCAspects.MACHINA, 1L),
                new TC_AspectStack(TCAspects.ITER, 1L),
                new TC_AspectStack(TCAspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_Valve_HV.set(
            this.addItem(
                Cover_Overflow_Valve_HV.ID,
                "Overflow Valve (HV)",
                "Maximum void amount: 4,096,000",
                new TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TC_AspectStack(TCAspects.MACHINA, 1L),
                new TC_AspectStack(TCAspects.ITER, 1L),
                new TC_AspectStack(TCAspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_Valve_EV.set(
            this.addItem(
                Cover_Overflow_Valve_EV.ID,
                "Overflow Valve (EV)",
                "Maximum void amount: 32,768,000",
                new TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TC_AspectStack(TCAspects.MACHINA, 1L),
                new TC_AspectStack(TCAspects.ITER, 1L),
                new TC_AspectStack(TCAspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_Valve_IV.set(
            this.addItem(
                Cover_Overflow_Valve_IV.ID,
                "Overflow Valve (IV)",
                "Maximum void amount: 262,144,000",
                new TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TC_AspectStack(TCAspects.MACHINA, 1L),
                new TC_AspectStack(TCAspects.ITER, 1L),
                new TC_AspectStack(TCAspects.AQUA, 1L)));

        // Fusion Reactor MK4 Singularity
        GregtechItemList.Compressed_Fusion_Reactor.set(
            this.addItem(
                Compressed_Fusion_Reactor.ID,
                "Hypervisor Matrix (Fusion)",
                "A memory unit containing an RI (Restricted Intelligence)"));

        GregtechItemList.Laser_Lens_Special
            .set(this.addItem(Laser_Lens_Special.ID, "Quantum Anomaly", "Probably should shoot it with lasers"));

        GregtechItemList.Battery_Casing_Gem_1
            .set(this.addItem(Battery_Casing_Gem_1.ID, "Containment Unit I", "Used in crafting"));
        GregtechItemList.Battery_Casing_Gem_2
            .set(this.addItem(Battery_Casing_Gem_2.ID, "Containment Unit II", "Used in crafting"));
        GregtechItemList.Battery_Casing_Gem_3
            .set(this.addItem(Battery_Casing_Gem_3.ID, "Advanced Containment Unit", "Used in crafting"));
        GregtechItemList.Battery_Casing_Gem_4
            .set(this.addItem(Battery_Casing_Gem_4.ID, "Exotic Containment Unit", "Used in crafting"));

        GregtechItemList.Battery_Gem_4.set(
            this.addItem(
                Battery_Gem_4.ID,
                "Graviton Anomaly",
                "Reusable",
                new TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TC_AspectStack(TCAspects.METALLUM, 64L),
                new TC_AspectStack(TCAspects.POTENTIA, 64L)));

        /*
         * High Tier 'Saws' for the tree Farm
         */

        GregtechItemList.Laser_Lens_WoodsGlass.set(
            this.addItem(
                Laser_Lens_WoodsGlass.ID,
                "Wood's Glass Lens",
                "Allows UV & IF to pass through, blocks visible light spectrums"));

        GregtechItemList[] aTransParts = new GregtechItemList[] { GregtechItemList.TransmissionComponent_LV,
            GregtechItemList.TransmissionComponent_MV, GregtechItemList.TransmissionComponent_HV,
            GregtechItemList.TransmissionComponent_EV, GregtechItemList.TransmissionComponent_IV,
            GregtechItemList.TransmissionComponent_LuV, GregtechItemList.TransmissionComponent_ZPM,
            GregtechItemList.TransmissionComponent_UV, GregtechItemList.TransmissionComponent_UHV, };

        int[] IDs = new int[] { TransmissionComponent_LV.ID, TransmissionComponent_MV.ID, TransmissionComponent_HV.ID,
            TransmissionComponent_EV.ID, TransmissionComponent_IV.ID, TransmissionComponent_LuV.ID,
            TransmissionComponent_ZPM.ID, TransmissionComponent_UV.ID, TransmissionComponent_UHV.ID, };
        for (int tier = 1; tier < aTransParts.length + 1; tier++) {
            aTransParts[tier - 1].set(
                this.addItem(
                    IDs[tier - 1],
                    "Transmission Component (" + GTValues.VN[tier] + ")",
                    "",
                    new TC_AspectStack(TCAspects.ELECTRUM, tier),
                    new TC_AspectStack(TCAspects.MACHINA, tier),
                    new TC_AspectStack(TCAspects.MAGNETO, tier)));
        }

        // Maceration Stack Upgrade Chip
        GregtechItemList.Maceration_Upgrade_Chip.set(
            this.addItem(
                Maceration_Upgrade_Chip.ID,
                "Maceration Upgrade Chip",
                "Used to upgrade Maceration Stack to Tier 2"));

        setAllElectricStats();
        registerTieredTooltips();
        registerFuelRTGRecipes();
        registerCovers();
    }

    private void setAllElectricStats() {
        this.setElectricStats(32000 + Battery_RE_EV_Cadmium.ID, 4800000L, GTValues.V[4], 4L, -3L, true);
        this.setElectricStats(32000 + Battery_RE_EV_Sodium.ID, 3200000L, GTValues.V[4], 4L, -3L, true);
        this.setElectricStats(32000 + Battery_RE_EV_Lithium.ID, 6400000L, GTValues.V[4], 4L, -3L, true);
        this.setElectricStats(32000 + Battery_Gem_1.ID, GTValues.V[6] * 20 * 300 / 4, GTValues.V[6], 6L, -3L, false);
        this.setElectricStats(32000 + Battery_Gem_2.ID, GTValues.V[7] * 20 * 300 / 4, GTValues.V[7], 7L, -3L, false);
        this.setElectricStats(32000 + Battery_Gem_3.ID, GTValues.V[8] * 20 * 300 / 4, GTValues.V[8], 8L, -3L, false);
        this.setElectricStats(32000 + Battery_Casing_Gem_4.ID, (64000000000L * 16), GTValues.V[9], 9L, -3L, false);
        this.setElectricStats(
            32000 + Gregtech_Computer_Cube.ID,
            GTValues.V[6] * 10 * 60 * 20,
            GTValues.V[5],
            5L,
            -3L,
            false);
    }

    private void registerTieredTooltips() {
        registerTieredTooltip(GregtechItemList.Battery_RE_EV_Sodium.get(1), EV);
        registerTieredTooltip(GregtechItemList.Battery_RE_EV_Cadmium.get(1), EV);
        registerTieredTooltip(GregtechItemList.Battery_RE_EV_Lithium.get(1), EV);
    }

    private void registerFuelRTGRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_PU238.get(1))
            .duration(0)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(RTG_DURATION_IN_DAYS, MathUtils.roundToClosestInt(87.7f))
            .addTo(rtgFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .duration(0)
            .eut(TierEU.RECIPE_LV)
            .metadata(RTG_DURATION_IN_DAYS, MathUtils.roundToClosestInt(28.8f))
            .addTo(rtgFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_PO210.get(1))
            .duration(0)
            .eut(TierEU.RECIPE_HV)
            .metadata(RTG_DURATION_IN_DAYS, 1)
            .addTo(rtgFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_AM241.get(1))
            .duration(0)
            .eut(TierEU.RECIPE_LV / 2)
            .metadata(RTG_DURATION_IN_DAYS, 216)
            .addTo(rtgFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("RTGPellets", 1))
            .duration(0)
            .eut(8)
            .metadata(RTG_DURATION_IN_DAYS, MathUtils.roundToClosestInt(2.6f))
            .addTo(rtgFuels);
    }

    private void registerCovers() {
        registerOverflowValveCover(GregtechItemList.Cover_Overflow_Valve_LV, 4, 64_000);
        registerOverflowValveCover(GregtechItemList.Cover_Overflow_Valve_MV, 5, 512_000);
        registerOverflowValveCover(GregtechItemList.Cover_Overflow_Valve_HV, 5, 4_096_000);
        registerOverflowValveCover(GregtechItemList.Cover_Overflow_Valve_EV, 8, 32_768_000);
        registerOverflowValveCover(GregtechItemList.Cover_Overflow_Valve_IV, 8, 262_144_000);
    }

    private static void registerOverflowValveCover(GregtechItemList cover, int tier, int maxOverflowPoint) {
        CoverRegistry.registerCover(
            cover.get(1L),
            TextureFactory.of(
                Textures.BlockIcons.MACHINE_CASINGS[tier][0],
                TextureFactory.of(TexturesGtBlock.Overlay_Overflow_Valve)),
            context -> new CoverOverflowValve(context, maxOverflowPoint));
    }

}
