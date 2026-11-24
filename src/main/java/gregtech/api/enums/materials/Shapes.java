package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.common.items.GTNaniteShapeItem;
import kubatech.loaders.item.arcfurnace.ElectrodeItem;

/// Item [Shape] declarations for GregTech's material parts, from dusts and ingots to tool heads.
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
    public static Shape electrode;
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
                .localizationFormatKey("gt.oreprefix.material_bolt")
                .build());
        crushed = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushed")
                .localizationFormatKey("gt.oreprefix.crushed_material_ore")
                .build());
        crushedCentrifuged = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedCentrifuged")
                .localizationFormatKey("gt.oreprefix.centrifuged_material_ore")
                .build());
        crushedPurified = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedPurified")
                .localizationFormatKey("gt.oreprefix.purified_material_ore")
                .build());
        dust = reg(
            MaterialLibAPI.newItemShape("gregtech", "dust")
                .localizationFormatKey("gt.oreprefix.material_dust")
                .build());
        dustImpure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustImpure")
                .localizationFormatKey("gt.oreprefix.impure_pile_of_material_dust")
                .build());
        dustPure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustPure")
                .localizationFormatKey("gt.oreprefix.purified_pile_of_material_dust")
                .build());
        dustSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustSmall")
                .localizationFormatKey("gt.oreprefix.small_pile_of_material_dust")
                .build());
        dustTiny = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustTiny")
                .localizationFormatKey("gt.oreprefix.tiny_pile_of_material_dust")
                .build());
        electrode = reg(
            MaterialLibAPI.registerItemShape(new ElectrodeItem("gregtech", "electrode", "electrode"))
                .setProperty(StandardProperties.LOCALIZATION_FORMAT_KEY, "gt.oreprefix.material_electrode"));
        foil = reg(
            MaterialLibAPI.newItemShape("gregtech", "foil")
                .localizationFormatKey("gt.oreprefix.material_foil")
                .build());
        gearGt = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGt")
                .localizationFormatKey("gt.oreprefix.material_gear")
                .build());
        gearGtSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGtSmall")
                .localizationFormatKey("gt.oreprefix.small_material_gear")
                .build());
        gem = reg(
            MaterialLibAPI.newItemShape("gregtech", "gem")
                .localizationFormatKey("gt.oreprefix.material")
                .build());
        gemChipped = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemChipped")
                .localizationFormatKey("gt.oreprefix.chipped_material")
                .build());
        gemExquisite = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemExquisite")
                .localizationFormatKey("gt.oreprefix.exquisite_material")
                .build());
        gemFlawed = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawed")
                .localizationFormatKey("gt.oreprefix.flawed_material")
                .build());
        gemFlawless = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawless")
                .localizationFormatKey("gt.oreprefix.flawless_material")
                .build());
        ingot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingot")
                .localizationFormatKey("gt.oreprefix.material_ingot")
                .build());
        ingotHot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingotHot")
                .localizationFormatKey("gt.oreprefix.hot_material_ingot")
                .build());
        itemCasing = reg(
            MaterialLibAPI.newItemShape("gregtech", "itemCasing")
                .localizationFormatKey("gt.oreprefix.material_casing")
                .build());
        lens = reg(
            MaterialLibAPI.newItemShape("gregtech", "lens")
                .localizationFormatKey("gt.oreprefix.material_lens")
                .build());
        milled = reg(
            MaterialLibAPI.newItemShape("gregtech", "milled")
                .localizationFormatKey("gt.oreprefix.milled_material")
                .build());
        nanite = reg(
            MaterialLibAPI.registerItemShape(new GTNaniteShapeItem("gregtech", "nanite", "nanite"))
                .setProperty(StandardProperties.LOCALIZATION_FORMAT_KEY, "gt.oreprefix.material_nanites"));
        nugget = reg(
            MaterialLibAPI.newItemShape("gregtech", "nugget")
                .localizationFormatKey("gt.oreprefix.material_nugget")
                .build());
        plate = reg(
            MaterialLibAPI.newItemShape("gregtech", "plate")
                .localizationFormatKey("gt.oreprefix.material_plate")
                .build());
        plateDense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDense")
                .localizationFormatKey("gt.oreprefix.dense_material_plate")
                .build());
        plateDouble = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDouble")
                .localizationFormatKey("gt.oreprefix.double_material_plate")
                .build());
        plateQuadruple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuadruple")
                .localizationFormatKey("gt.oreprefix.quadruple_material_plate")
                .build());
        plateQuintuple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuintuple")
                .localizationFormatKey("gt.oreprefix.quintuple_material_plate")
                .build());
        plateSuperdense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateSuperdense")
                .localizationFormatKey("gt.oreprefix.superdense_material_plate")
                .build());
        plateTriple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateTriple")
                .localizationFormatKey("gt.oreprefix.triple_material_plate")
                .build());
        rawOre = reg(
            MaterialLibAPI.newItemShape("gregtech", "rawOre")
                .localizationFormatKey("gt.oreprefix.raw_material_ore")
                .build());
        ring = reg(
            MaterialLibAPI.newItemShape("gregtech", "ring")
                .localizationFormatKey("gt.oreprefix.material_ring")
                .build());
        rotor = reg(
            MaterialLibAPI.newItemShape("gregtech", "rotor")
                .localizationFormatKey("gt.oreprefix.material_rotor")
                .build());
        round = reg(
            MaterialLibAPI.newItemShape("gregtech", "round")
                .localizationFormatKey("gt.oreprefix.material_round")
                .build());
        screw = reg(
            MaterialLibAPI.newItemShape("gregtech", "screw")
                .localizationFormatKey("gt.oreprefix.material_screw")
                .build());
        spring = reg(
            MaterialLibAPI.newItemShape("gregtech", "spring")
                .localizationFormatKey("gt.oreprefix.material_spring")
                .build());
        springSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "springSmall")
                .localizationFormatKey("gt.oreprefix.small_material_spring")
                .build());
        stick = reg(
            MaterialLibAPI.newItemShape("gregtech", "stick")
                .localizationFormatKey("gt.oreprefix.material_rod")
                .build());
        stickLong = reg(
            MaterialLibAPI.newItemShape("gregtech", "stickLong")
                .localizationFormatKey("gt.oreprefix.long_material_rod")
                .build());
        toolHeadBuzzSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadBuzzSaw")
                .localizationFormatKey("gt.oreprefix.material_buzzsaw_blade")
                .build());
        toolHeadChainsaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadChainsaw")
                .localizationFormatKey("gt.oreprefix.material_chainsaw_tip")
                .build());
        toolHeadDrill = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadDrill")
                .localizationFormatKey("gt.oreprefix.material_drill_tip")
                .build());
        toolHeadFile = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadFile")
                .localizationFormatKey("gt.oreprefix.material_file_head")
                .build());
        toolHeadHammer = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadHammer")
                .localizationFormatKey("gt.oreprefix.material_hammer_head")
                .build());
        toolHeadSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadSaw")
                .localizationFormatKey("gt.oreprefix.material_saw_blade")
                .build());
        toolHeadWrench = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadWrench")
                .localizationFormatKey("gt.oreprefix.material_wrench_tip")
                .build());
        turbineBlade = reg(
            MaterialLibAPI.newItemShape("gregtech", "turbineBlade")
                .localizationFormatKey("gt.oreprefix.material_turbine_blade")
                .build());
        wireFine = reg(
            MaterialLibAPI.newItemShape("gregtech", "wireFine")
                .localizationFormatKey("gt.oreprefix.fine_material_wire")
                .build());
    }

    private Shapes() {}
}
