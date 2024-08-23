package gtPlusPlus.xmod.gregtech.common.items;

import static gregtech.api.util.GT_RecipeConstants.RTG_DURATION_IN_DAYS;
import static gregtech.client.GT_TooltipHandler.Tier.EV;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.rtgFuels;
import static gtPlusPlus.core.util.Utils.getTcAspectStack;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Casing_Gem_1;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Casing_Gem_2;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Casing_Gem_3;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Casing_Gem_4;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Gem_1;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Gem_2;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Gem_3;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_Gem_4;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_RE_EV_Cadmium;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_RE_EV_Lithium;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Battery_RE_EV_Sodium;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Compressed_Fusion_Reactor;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Cover_Overflow_EV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Cover_Overflow_HV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Cover_Overflow_IV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Cover_Overflow_LV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Cover_Overflow_MV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Distillus_Upgrade_Chip;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Gregtech_Computer_Cube;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Laser_Lens_WoodsGlass;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Maceration_Upgrade_Chip;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Pellet_RTG_AM241;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Pellet_RTG_PO210;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Pellet_RTG_PU238;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Pellet_RTG_SR90;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.Shape_Extruder_WindmillShaft;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_EV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_HV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_IV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_LV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_LuV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_MV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_UHV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_UV;
import static gtPlusPlus.xmod.gregtech.common.items.Ids_MetaGeneratedGregtechItems.TransmissionComponent_ZPM;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.GT_MultiTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
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
                getTcAspectStack(TC_Aspects.ELECTRUM, 2L),
                getTcAspectStack(TC_Aspects.METALLUM, 2L),
                getTcAspectStack(TC_Aspects.POTENTIA, 2L)));

        GregtechItemList.Battery_RE_EV_Cadmium.set(
            this.addItem(
                Battery_RE_EV_Cadmium.ID,
                "Quad Cell Cadmium Battery",
                "Reusable",
                getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                getTcAspectStack(TC_Aspects.METALLUM, 1L),
                getTcAspectStack(TC_Aspects.POTENTIA, 1L)));

        GregtechItemList.Battery_RE_EV_Lithium.set(
            this.addItem(
                Battery_RE_EV_Lithium.ID,
                "Quad Cell Lithium Battery",
                "Reusable",
                getTcAspectStack(TC_Aspects.ELECTRUM, 3L),
                getTcAspectStack(TC_Aspects.METALLUM, 3L),
                getTcAspectStack(TC_Aspects.POTENTIA, 3L)));

        /**
         * Power Gems
         */
        GregtechItemList.Battery_Gem_1.set(
            this.addItem(
                Battery_Gem_1.ID,
                "Proton Cell",
                "Reusable",
                getTcAspectStack(TC_Aspects.ELECTRUM, 8L),
                getTcAspectStack(TC_Aspects.METALLUM, 24L),
                getTcAspectStack(TC_Aspects.POTENTIA, 16L)));

        GregtechItemList.Battery_Gem_2.set(
            this.addItem(
                Battery_Gem_2.ID,
                "Electron Cell",
                "Reusable",
                getTcAspectStack(TC_Aspects.ELECTRUM, 16L),
                getTcAspectStack(TC_Aspects.METALLUM, 32L),
                getTcAspectStack(TC_Aspects.POTENTIA, 32L)));

        GregtechItemList.Battery_Gem_3.set(
            this.addItem(
                Battery_Gem_3.ID,
                "Quark Entanglement",
                "Reusable",
                getTcAspectStack(TC_Aspects.ELECTRUM, 32L),
                getTcAspectStack(TC_Aspects.METALLUM, 48L),
                getTcAspectStack(TC_Aspects.POTENTIA, 64L)));

        // RTG Pellet
        GregtechItemList.Pellet_RTG_PU238.set(
            this.addItem(
                Pellet_RTG_PU238.ID,
                StringUtils.superscript("238") + "Pu Pellet",
                "",
                getTcAspectStack(TC_Aspects.RADIO, 4L),
                getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                getTcAspectStack(TC_Aspects.METALLUM, 2L)));

        GregtechItemList.Pellet_RTG_SR90.set(
            this.addItem(
                Pellet_RTG_SR90.ID,
                StringUtils.superscript("90") + "Sr Pellet",
                "",
                getTcAspectStack(TC_Aspects.RADIO, 4L),
                getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                getTcAspectStack(TC_Aspects.METALLUM, 2L)));

        GregtechItemList.Pellet_RTG_PO210.set(
            this.addItem(
                Pellet_RTG_PO210.ID,
                StringUtils.superscript("210") + "Po Pellet",
                "",
                getTcAspectStack(TC_Aspects.RADIO, 4L),
                getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                getTcAspectStack(TC_Aspects.METALLUM, 2L)));

        GregtechItemList.Pellet_RTG_AM241.set(
            this.addItem(
                Pellet_RTG_AM241.ID,
                StringUtils.superscript("241") + "Am Pellet",
                "",
                getTcAspectStack(TC_Aspects.RADIO, 4L),
                getTcAspectStack(TC_Aspects.POTENTIA, 2L),
                getTcAspectStack(TC_Aspects.METALLUM, 2L)));

        // Computer Cube
        GregtechItemList.Gregtech_Computer_Cube.set(
            this.addItem(
                Gregtech_Computer_Cube.ID,
                "Gregtech Computer Cube",
                "Reusable",
                getTcAspectStack(TC_Aspects.ELECTRUM, 8L),
                getTcAspectStack(TC_Aspects.METALLUM, 8L),
                getTcAspectStack(TC_Aspects.POTENTIA, 8L)));

        GregtechItemList.Cover_Overflow_LV.set(
            this.addItem(
                Cover_Overflow_LV.ID,
                "Overflow Valve (LV)",
                "Maximum void amount: 64,000",
                getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                getTcAspectStack(TC_Aspects.MACHINA, 1L),
                getTcAspectStack(TC_Aspects.ITER, 1L),
                getTcAspectStack(TC_Aspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_MV.set(
            this.addItem(
                Cover_Overflow_MV.ID,
                "Overflow Valve (MV)",
                "Maximum void amount: 512,000",
                getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                getTcAspectStack(TC_Aspects.MACHINA, 1L),
                getTcAspectStack(TC_Aspects.ITER, 1L),
                getTcAspectStack(TC_Aspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_HV.set(
            this.addItem(
                Cover_Overflow_HV.ID,
                "Overflow Valve (HV)",
                "Maximum void amount: 4,096,000",
                getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                getTcAspectStack(TC_Aspects.MACHINA, 1L),
                getTcAspectStack(TC_Aspects.ITER, 1L),
                getTcAspectStack(TC_Aspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_EV.set(
            this.addItem(
                Cover_Overflow_EV.ID,
                "Overflow Valve (EV)",
                "Maximum void amount: 32,768,000",
                getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                getTcAspectStack(TC_Aspects.MACHINA, 1L),
                getTcAspectStack(TC_Aspects.ITER, 1L),
                getTcAspectStack(TC_Aspects.AQUA, 1L)));

        GregtechItemList.Cover_Overflow_IV.set(
            this.addItem(
                Cover_Overflow_IV.ID,
                "Overflow Valve (IV)",
                "Maximum void amount: 262,144,000",
                getTcAspectStack(TC_Aspects.ELECTRUM, 1L),
                getTcAspectStack(TC_Aspects.MACHINA, 1L),
                getTcAspectStack(TC_Aspects.ITER, 1L),
                getTcAspectStack(TC_Aspects.AQUA, 1L)));

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
                getTcAspectStack(TC_Aspects.ELECTRUM, 64L),
                getTcAspectStack(TC_Aspects.METALLUM, 64L),
                getTcAspectStack(TC_Aspects.POTENTIA, 64L)));

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
                    "Transmission Component (" + GT_Values.VN[tier] + ")",
                    "",
                    getTcAspectStack(TC_Aspects.ELECTRUM, tier),
                    getTcAspectStack(TC_Aspects.MACHINA, tier),
                    getTcAspectStack(TC_Aspects.MAGNETO, tier)));
        }

        // Distillus Chip
        GregtechItemList.Distillus_Upgrade_Chip.set(
            this.addItem(Distillus_Upgrade_Chip.ID, "Distillus Upgrade Chip", "Used to upgrade Distillus to Tier 2"));
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
        this.setElectricStats(32000 + Battery_RE_EV_Cadmium.ID, 4800000L, GT_Values.V[4], 4L, -3L, true);
        this.setElectricStats(32000 + Battery_RE_EV_Sodium.ID, 3200000L, GT_Values.V[4], 4L, -3L, true);
        this.setElectricStats(32000 + Battery_RE_EV_Lithium.ID, 6400000L, GT_Values.V[4], 4L, -3L, true);
        this.setElectricStats(32000 + Battery_Gem_1.ID, GT_Values.V[6] * 20 * 300 / 4, GT_Values.V[6], 6L, -3L, false);
        this.setElectricStats(32000 + Battery_Gem_2.ID, GT_Values.V[7] * 20 * 300 / 4, GT_Values.V[7], 7L, -3L, false);
        this.setElectricStats(32000 + Battery_Gem_3.ID, GT_Values.V[8] * 20 * 300 / 4, GT_Values.V[8], 8L, -3L, false);
        this.setElectricStats(32000 + Battery_Casing_Gem_4.ID, (64000000000L * 16), GT_Values.V[9], 9L, -3L, false);
        this.setElectricStats(
            32000 + Gregtech_Computer_Cube.ID,
            GT_Values.V[6] * 10 * 60 * 20,
            GT_Values.V[5],
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
        GT_Values.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_PU238.get(1))
            .duration(0)
            .eut(64)
            .metadata(RTG_DURATION_IN_DAYS, MathUtils.roundToClosestInt(87.7f))
            .addTo(rtgFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .duration(0)
            .eut(TierEU.RECIPE_LV)
            .metadata(RTG_DURATION_IN_DAYS, MathUtils.roundToClosestInt(28.8f))
            .addTo(rtgFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_PO210.get(1))
            .duration(0)
            .eut(TierEU.RECIPE_HV)
            .metadata(RTG_DURATION_IN_DAYS, 1)
            .addTo(rtgFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GregtechItemList.Pellet_RTG_AM241.get(1))
            .duration(0)
            .eut(16)
            .metadata(RTG_DURATION_IN_DAYS, 216)
            .addTo(rtgFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("RTGPellets", 1))
            .duration(0)
            .eut(8)
            .metadata(RTG_DURATION_IN_DAYS, MathUtils.roundToClosestInt(2.6f))
            .addTo(rtgFuels);
    }

    private void registerCovers() {
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
    }

}
