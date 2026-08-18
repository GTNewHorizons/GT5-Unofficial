package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

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
                .build());
        crushed = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushed")
                .displayName("Crushed %s Ore")
                .build());
        crushedCentrifuged = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedCentrifuged")
                .displayName("Centrifuged %s Ore")
                .build());
        crushedPurified = reg(
            MaterialLibAPI.newItemShape("gregtech", "crushedPurified")
                .displayName("Purified %s Ore")
                .build());
        dust = reg(
            MaterialLibAPI.newItemShape("gregtech", "dust")
                .displayName("%s Dust")
                .build());
        dustImpure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustImpure")
                .displayName("Impure Pile of %s Dust")
                .build());
        dustPure = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustPure")
                .displayName("Purified Pile of %s Dust")
                .build());
        dustSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustSmall")
                .displayName("Small Pile of %s Dust")
                .build());
        dustTiny = reg(
            MaterialLibAPI.newItemShape("gregtech", "dustTiny")
                .displayName("Tiny Pile of %s Dust")
                .build());
        foil = reg(
            MaterialLibAPI.newItemShape("gregtech", "foil")
                .displayName("%s Foil")
                .build());
        gearGt = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGt")
                .displayName("%s Gear")
                .build());
        gearGtSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "gearGtSmall")
                .displayName("Small %s Gear")
                .build());
        gem = reg(
            MaterialLibAPI.newItemShape("gregtech", "gem")
                .displayName("%s")
                .build());
        gemChipped = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemChipped")
                .displayName("Chipped %s")
                .build());
        gemExquisite = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemExquisite")
                .displayName("Exquisite %s")
                .build());
        gemFlawed = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawed")
                .displayName("Flawed %s")
                .build());
        gemFlawless = reg(
            MaterialLibAPI.newItemShape("gregtech", "gemFlawless")
                .displayName("Flawless %s")
                .build());
        ingot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingot")
                .displayName("%s Ingot")
                .build());
        ingotHot = reg(
            MaterialLibAPI.newItemShape("gregtech", "ingotHot")
                .displayName("Hot %s Ingot")
                .build());
        itemCasing = reg(
            MaterialLibAPI.newItemShape("gregtech", "itemCasing")
                .displayName("%s Casing")
                .build());
        lens = reg(
            MaterialLibAPI.newItemShape("gregtech", "lens")
                .displayName("%s Lens")
                .build());
        milled = reg(
            MaterialLibAPI.newItemShape("gregtech", "milled")
                .displayName("Milled %s")
                .build());
        nugget = reg(
            MaterialLibAPI.newItemShape("gregtech", "nugget")
                .displayName("%s Nugget")
                .build());
        plate = reg(
            MaterialLibAPI.newItemShape("gregtech", "plate")
                .displayName("%s Plate")
                .build());
        plateDense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDense")
                .displayName("Dense %s Plate")
                .build());
        plateDouble = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateDouble")
                .displayName("Double %s Plate")
                .build());
        plateQuadruple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuadruple")
                .displayName("Quadruple %s Plate")
                .build());
        plateQuintuple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateQuintuple")
                .displayName("Quintuple %s Plate")
                .build());
        plateSuperdense = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateSuperdense")
                .displayName("Superdense %s Plate")
                .build());
        plateTriple = reg(
            MaterialLibAPI.newItemShape("gregtech", "plateTriple")
                .displayName("Triple %s Plate")
                .build());
        rawOre = reg(
            MaterialLibAPI.newItemShape("gregtech", "rawOre")
                .displayName("Raw %s Ore")
                .build());
        ring = reg(
            MaterialLibAPI.newItemShape("gregtech", "ring")
                .displayName("%s Ring")
                .build());
        rotor = reg(
            MaterialLibAPI.newItemShape("gregtech", "rotor")
                .displayName("%s Rotor")
                .build());
        round = reg(
            MaterialLibAPI.newItemShape("gregtech", "round")
                .displayName("%s Round")
                .build());
        screw = reg(
            MaterialLibAPI.newItemShape("gregtech", "screw")
                .displayName("%s Screw")
                .build());
        spring = reg(
            MaterialLibAPI.newItemShape("gregtech", "spring")
                .displayName("%s Spring")
                .build());
        springSmall = reg(
            MaterialLibAPI.newItemShape("gregtech", "springSmall")
                .displayName("Small %s Spring")
                .build());
        stick = reg(
            MaterialLibAPI.newItemShape("gregtech", "stick")
                .displayName("%s Rod")
                .build());
        stickLong = reg(
            MaterialLibAPI.newItemShape("gregtech", "stickLong")
                .displayName("Long %s Rod")
                .build());
        toolHeadBuzzSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadBuzzSaw")
                .displayName("%s Buzzsaw Blade")
                .build());
        toolHeadChainsaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadChainsaw")
                .displayName("%s Chainsaw Tip")
                .build());
        toolHeadDrill = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadDrill")
                .displayName("%s Drill Tip")
                .build());
        toolHeadFile = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadFile")
                .displayName("%s File Head")
                .build());
        toolHeadHammer = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadHammer")
                .displayName("%s Hammer Head")
                .build());
        toolHeadSaw = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadSaw")
                .displayName("%s Saw Blade")
                .build());
        toolHeadWrench = reg(
            MaterialLibAPI.newItemShape("gregtech", "toolHeadWrench")
                .displayName("%s Wrench Tip")
                .build());
        turbineBlade = reg(
            MaterialLibAPI.newItemShape("gregtech", "turbineBlade")
                .displayName("%s Turbine Blade")
                .build());
        wireFine = reg(
            MaterialLibAPI.newItemShape("gregtech", "wireFine")
                .displayName("Fine %s Wire")
                .build());
    }

    private Shapes() {}
}
