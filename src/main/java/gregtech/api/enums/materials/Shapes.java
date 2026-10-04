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
                .displayName("%s Bolt")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_bolt")
                .build());
        crushed = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushed")
                .displayName("Crushed %s Ore")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.crushed_material_ore")
                .build());
        crushedCentrifuged = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedCentrifuged")
                .displayName("Centrifuged %s Ore")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.centrifuged_material_ore")
                .build());
        crushedPurified = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedPurified")
                .displayName("Purified %s Ore")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.purified_material_ore")
                .build());
        dust = reg(
            MaterialLibAPI.newItemShape("gregtech", "dust")
                .displayName("%s Dust")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_dust")
                .build());
        dustImpure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustImpure")
                .displayName("Impure Pile of %s Dust")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.impure_pile_of_material_dust")
                .build());
        dustPure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustPure")
                .displayName("Purified Pile of %s Dust")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.purified_pile_of_material_dust")
                .build());
        dustSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustSmall")
                .displayName("Small Pile of %s Dust")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.small_pile_of_material_dust")
                .build());
        dustTiny = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustTiny")
                .displayName("Tiny Pile of %s Dust")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.tiny_pile_of_material_dust")
                .build());
        foil = reg(
            MaterialLibAPI.newItemShape("gregtech", "foil")
                .displayName("%s Foil")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_foil")
                .build());
        gearGt = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGt")
                .displayName("%s Gear")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_gear")
                .build());
        gearGtSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGtSmall")
                .displayName("Small %s Gear")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.small_material_gear")
                .build());
        gem = reg(
            MaterialLibAPI.newItemShape("gregtech", "gem")
                .displayName("%s")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material")
                .build());
        gemChipped = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemChipped")
                .displayName("Chipped %s")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.chipped_material")
                .build());
        gemExquisite = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemExquisite")
                .displayName("Exquisite %s")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.exquisite_material")
                .build());
        gemFlawed = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawed")
                .displayName("Flawed %s")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.flawed_material")
                .build());
        gemFlawless = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawless")
                .displayName("Flawless %s")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.flawless_material")
                .build());
        ingot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingot")
                .displayName("%s Ingot")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_ingot")
                .build());
        ingotHot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingotHot")
                .displayName("Hot %s Ingot")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.hot_material_ingot")
                .build());
        itemCasing = reg(
            MaterialLibAPI.newItemShape("gregtech", "itemCasing")
                .displayName("%s Casing")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_casing")
                .build());
        lens = reg(
            MaterialLibAPI.newItemShape("gregtech", "lens")
                .displayName("%s Lens")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_lens")
                .build());
        milled = reg(
            MaterialLibAPI.newItemShape("gregtech", "milled")
                .displayName("Milled %s")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.milled_material")
                .build());
        nanite = reg(
            MaterialLibAPI.registerItemShape(new GTNaniteShapeItem("gregtech", "nanite", "%s Nanites", "nanite"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_nanites"));
        nugget = reg(
            MaterialLibAPI.newItemShape("gregtech", "nugget")
                .displayName("%s Nugget")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_nugget")
                .build());
        plate = reg(
            MaterialLibAPI.newItemShape("gregtech", "plate")
                .displayName("%s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_plate")
                .build());
        plateDense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDense")
                .displayName("Dense %s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.dense_material_plate")
                .build());
        plateDouble = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDouble")
                .displayName("Double %s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.double_material_plate")
                .build());
        plateQuadruple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuadruple")
                .displayName("Quadruple %s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.quadruple_material_plate")
                .build());
        plateQuintuple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuintuple")
                .displayName("Quintuple %s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.quintuple_material_plate")
                .build());
        plateSuperdense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateSuperdense")
                .displayName("Superdense %s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.superdense_material_plate")
                .build());
        plateTriple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateTriple")
                .displayName("Triple %s Plate")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.triple_material_plate")
                .build());
        rawOre = reg(
            MaterialLibAPI.newItemShape("gregtech", "rawOre")
                .displayName("Raw %s Ore")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.raw_material_ore")
                .build());
        ring = reg(
            MaterialLibAPI.newItemShape("gregtech", "ring")
                .displayName("%s Ring")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_ring")
                .build());
        rotor = reg(
            MaterialLibAPI.newItemShape("gregtech", "rotor")
                .displayName("%s Rotor")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_rotor")
                .build());
        round = reg(
            MaterialLibAPI.newItemShape("gregtech", "round")
                .displayName("%s Round")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_round")
                .build());
        screw = reg(
            MaterialLibAPI.newItemShape("gregtech", "screw")
                .displayName("%s Screw")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_screw")
                .build());
        spring = reg(
            MaterialLibAPI.newItemShape("gregtech", "spring")
                .displayName("%s Spring")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_spring")
                .build());
        springSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "springSmall")
                .displayName("Small %s Spring")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.small_material_spring")
                .build());
        stick = reg(
            MaterialLibAPI.newItemShape("gregtech", "stick")
                .displayName("%s Rod")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_rod")
                .build());
        stickLong = reg(
            MaterialLibAPI.newItemShape("gregtech", "stickLong")
                .displayName("Long %s Rod")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.long_material_rod")
                .build());
        toolHeadBuzzSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadBuzzSaw")
                .displayName("%s Buzzsaw Blade")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_buzzsaw_blade")
                .build());
        toolHeadChainsaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadChainsaw")
                .displayName("%s Chainsaw Tip")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_chainsaw_tip")
                .build());
        toolHeadDrill = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadDrill")
                .displayName("%s Drill Tip")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_drill_tip")
                .build());
        toolHeadFile = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadFile")
                .displayName("%s File Head")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_file_head")
                .build());
        toolHeadHammer = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadHammer")
                .displayName("%s Hammer Head")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_hammer_head")
                .build());
        toolHeadSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadSaw")
                .displayName("%s Saw Blade")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_saw_blade")
                .build());
        toolHeadWrench = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadWrench")
                .displayName("%s Wrench Tip")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_wrench_tip")
                .build());
        turbineBlade = reg(
            MaterialLibAPI.newItemShape("gregtech", "turbineBlade")
                .displayName("%s Turbine Blade")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_turbine_blade")
                .build());
        wireFine = reg(
            MaterialLibAPI.newItemShape("gregtech", "wireFine")
                .displayName("Fine %s Wire")
                .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.fine_material_wire")
                .build());
    }

    private Shapes() {}
}
