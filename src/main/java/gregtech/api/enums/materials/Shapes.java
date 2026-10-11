package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.common.items.GTNaniteShapeItem;

/// Basic item [Shape]s
public class Shapes {

    // spotless:off
    public static Shape bolt;
    public static Shape crushed;
    public static Shape crushedCentrifuged;
    public static Shape crushedPurified;
    public static Shape dust;
    public static Shape dustImpure;
    public static Shape dustPure;
    public static Shape dustSmall;
    public static Shape dustTiny;
    public static Shape foil;
    public static Shape gearGt;
    public static Shape gearGtSmall;
    public static Shape gem;
    public static Shape gemChipped;
    public static Shape gemExquisite;
    public static Shape gemFlawed;
    public static Shape gemFlawless;
    public static Shape ingot;
    public static Shape ingotHot;
    public static Shape itemCasing;
    public static Shape lens;
    public static Shape milled;
    public static Shape nanite;
    public static Shape nugget;
    public static Shape plate;
    public static Shape plateDense;
    public static Shape plateDouble;
    public static Shape plateQuadruple;
    public static Shape plateQuintuple;
    public static Shape plateSuperdense;
    public static Shape plateTriple;
    public static Shape rawOre;
    public static Shape ring;
    public static Shape rotor;
    public static Shape round;
    public static Shape screw;
    public static Shape spring;
    public static Shape springSmall;
    public static Shape stick;
    public static Shape stickLong;
    public static Shape toolHeadBuzzSaw;
    public static Shape toolHeadChainsaw;
    public static Shape toolHeadDrill;
    public static Shape toolHeadFile;
    public static Shape toolHeadHammer;
    public static Shape toolHeadSaw;
    public static Shape toolHeadWrench;
    public static Shape turbineBlade;
    public static Shape wireFine;
    // spotless:on

    public static void init() {
        bolt = reg(
            MaterialLibAPI.newItemShape("gregtech", "bolt")
                .displayNameKey("gt.oreprefix.material_bolt")
                .build());
        crushed = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushed")
                .displayNameKey("gt.oreprefix.crushed_material_ore")
                .build());
        crushedCentrifuged = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedCentrifuged")
                .displayNameKey("gt.oreprefix.centrifuged_material_ore")
                .build());
        crushedPurified = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedPurified")
                .displayNameKey("gt.oreprefix.purified_material_ore")
                .build());
        dust = reg(
            MaterialLibAPI.newItemShape("gregtech", "dust")
                .displayNameKey("gt.oreprefix.material_dust")
                .build());
        dustImpure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustImpure")
                .displayNameKey("gt.oreprefix.impure_pile_of_material_dust")
                .build());
        dustPure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustPure")
                .displayNameKey("gt.oreprefix.purified_pile_of_material_dust")
                .build());
        dustSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustSmall")
                .displayNameKey("gt.oreprefix.small_pile_of_material_dust")
                .build());
        dustTiny = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustTiny")
                .displayNameKey("gt.oreprefix.tiny_pile_of_material_dust")
                .build());
        foil = reg(
            MaterialLibAPI.newItemShape("gregtech", "foil")
                .displayNameKey("gt.oreprefix.material_foil")
                .build());
        gearGt = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGt")
                .displayNameKey("gt.oreprefix.material_gear")
                .build());
        gearGtSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGtSmall")
                .displayNameKey("gt.oreprefix.small_material_gear")
                .build());
        gem = reg(
            MaterialLibAPI.newItemShape("gregtech", "gem")
                .displayNameKey("gt.oreprefix.material")
                .build());
        gemChipped = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemChipped")
                .displayNameKey("gt.oreprefix.chipped_material")
                .build());
        gemExquisite = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemExquisite")
                .displayNameKey("gt.oreprefix.exquisite_material")
                .build());
        gemFlawed = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawed")
                .displayNameKey("gt.oreprefix.flawed_material")
                .build());
        gemFlawless = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawless")
                .displayNameKey("gt.oreprefix.flawless_material")
                .build());
        ingot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingot")
                .displayNameKey("gt.oreprefix.material_ingot")
                .build());
        ingotHot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingotHot")
                .displayNameKey("gt.oreprefix.hot_material_ingot")
                .build());
        itemCasing = reg(
            MaterialLibAPI.newItemShape("gregtech", "itemCasing")
                .displayNameKey("gt.oreprefix.material_casing")
                .build());
        lens = reg(
            MaterialLibAPI.newItemShape("gregtech", "lens")
                .displayNameKey("gt.oreprefix.material_lens")
                .build());
        milled = reg(
            MaterialLibAPI.newItemShape("gregtech", "milled")
                .displayNameKey("gt.oreprefix.milled_material")
                .build());
        nanite = reg(
            MaterialLibAPI.registerItemShape(new GTNaniteShapeItem("gregtech", "nanite", "nanite"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_nanites"));
        nugget = reg(
            MaterialLibAPI.newItemShape("gregtech", "nugget")
                .displayNameKey("gt.oreprefix.material_nugget")
                .build());
        plate = reg(
            MaterialLibAPI.newItemShape("gregtech", "plate")
                .displayNameKey("gt.oreprefix.material_plate")
                .build());
        plateDense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDense")
                .displayNameKey("gt.oreprefix.dense_material_plate")
                .build());
        plateDouble = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDouble")
                .displayNameKey("gt.oreprefix.double_material_plate")
                .build());
        plateQuadruple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuadruple")
                .displayNameKey("gt.oreprefix.quadruple_material_plate")
                .build());
        plateQuintuple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuintuple")
                .displayNameKey("gt.oreprefix.quintuple_material_plate")
                .build());
        plateSuperdense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateSuperdense")
                .displayNameKey("gt.oreprefix.superdense_material_plate")
                .build());
        plateTriple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateTriple")
                .displayNameKey("gt.oreprefix.triple_material_plate")
                .build());
        rawOre = reg(
            MaterialLibAPI.newItemShape("gregtech", "rawOre")
                .displayNameKey("gt.oreprefix.raw_material_ore")
                .build());
        ring = reg(
            MaterialLibAPI.newItemShape("gregtech", "ring")
                .displayNameKey("gt.oreprefix.material_ring")
                .build());
        rotor = reg(
            MaterialLibAPI.newItemShape("gregtech", "rotor")
                .displayNameKey("gt.oreprefix.material_rotor")
                .build());
        round = reg(
            MaterialLibAPI.newItemShape("gregtech", "round")
                .displayNameKey("gt.oreprefix.material_round")
                .build());
        screw = reg(
            MaterialLibAPI.newItemShape("gregtech", "screw")
                .displayNameKey("gt.oreprefix.material_screw")
                .build());
        spring = reg(
            MaterialLibAPI.newItemShape("gregtech", "spring")
                .displayNameKey("gt.oreprefix.material_spring")
                .build());
        springSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "springSmall")
                .displayNameKey("gt.oreprefix.small_material_spring")
                .build());
        stick = reg(
            MaterialLibAPI.newItemShape("gregtech", "stick")
                .displayNameKey("gt.oreprefix.material_rod")
                .build());
        stickLong = reg(
            MaterialLibAPI.newItemShape("gregtech", "stickLong")
                .displayNameKey("gt.oreprefix.long_material_rod")
                .build());
        toolHeadBuzzSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadBuzzSaw")
                .displayNameKey("gt.oreprefix.material_buzzsaw_blade")
                .build());
        toolHeadChainsaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadChainsaw")
                .displayNameKey("gt.oreprefix.material_chainsaw_tip")
                .build());
        toolHeadDrill = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadDrill")
                .displayNameKey("gt.oreprefix.material_drill_tip")
                .build());
        toolHeadFile = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadFile")
                .displayNameKey("gt.oreprefix.material_file_head")
                .build());
        toolHeadHammer = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadHammer")
                .displayNameKey("gt.oreprefix.material_hammer_head")
                .build());
        toolHeadSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadSaw")
                .displayNameKey("gt.oreprefix.material_saw_blade")
                .build());
        toolHeadWrench = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadWrench")
                .displayNameKey("gt.oreprefix.material_wrench_tip")
                .build());
        turbineBlade = reg(
            MaterialLibAPI.newItemShape("gregtech", "turbineBlade")
                .displayNameKey("gt.oreprefix.material_turbine_blade")
                .build());
        wireFine = reg(
            MaterialLibAPI.newItemShape("gregtech", "wireFine")
                .displayNameKey("gt.oreprefix.fine_material_wire")
                .build());
    }

    private Shapes() {}
}
