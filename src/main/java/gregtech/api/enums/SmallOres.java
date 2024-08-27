package gregtech.api.enums;

import static bloodasp.galacticgreg.api.enums.DimensionDef.Anubis;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Asteroids;
import static bloodasp.galacticgreg.api.enums.DimensionDef.BarnardE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.BarnardF;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Callisto;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Ceres;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Deimos;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Enceladus;
import static bloodasp.galacticgreg.api.enums.DimensionDef.EndAsteroids;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Ganymede;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Haumea;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Horus;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Io;
import static bloodasp.galacticgreg.api.enums.DimensionDef.KuiperBelt;
import static bloodasp.galacticgreg.api.enums.DimensionDef.MakeMake;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Mars;
import static bloodasp.galacticgreg.api.enums.DimensionDef.MehenBelt;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Mercury;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Miranda;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Moon;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Oberon;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Phobos;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Pluto;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Proteus;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Seth;
import static bloodasp.galacticgreg.api.enums.DimensionDef.TcetiE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Titan;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Triton;
import static bloodasp.galacticgreg.api.enums.DimensionDef.VegaB;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Venus;
import static gregtech.common.SmallOreBuilder.NETHER;
import static gregtech.common.SmallOreBuilder.OW;
import static gregtech.common.SmallOreBuilder.THE_END;
import static gregtech.common.SmallOreBuilder.TWILIGHT_FOREST;

import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_SmallPieces_Space;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.SmallOreBuilder;

public enum SmallOres {

    // spotless : off
    Copper(new SmallOreBuilder().name("ore.small.copper")
        .heightRange(60, 180)
        .amount(32)
        .ore(Materials.Copper)
        .enableInDim(Mars, Phobos, Proteus, Triton)
        .enableInDim(NETHER, OW, THE_END)),

    Tin(new SmallOreBuilder().name("ore.small.tin")
        .heightRange(80, 220)
        .amount(32)
        .ore(Materials.Tin)
        .enableInDim(MehenBelt, Mars, Deimos, Ganymede, Proteus, Titan, EndAsteroids)
        .enableInDim(NETHER, OW, THE_END)),

    Bismuth(new SmallOreBuilder().name("ore.small.bismuth")
        .heightRange(80, 120)
        .amount(8)
        .ore(Materials.Bismuth)
        .enableInDim(Mars, Callisto, Ceres, Io, MakeMake, Mercury, Proteus, VegaB)
        .enableInDim(NETHER)),

    Coal(new SmallOreBuilder().name("ore.small.coal")
        .heightRange(120, 250)
        .amount(24)
        .ore(Materials.Coal)
        .enableInDim(OW)),

    Iron(new SmallOreBuilder().name("ore.small.iron")
        .heightRange(40, 100)
        .amount(16)
        .ore(Materials.Iron)
        .enableInDim(
            Asteroids,
            Mars,
            BarnardE,
            BarnardF,
            Callisto,
            Enceladus,
            Ganymede,
            Haumea,
            Io,
            KuiperBelt,
            Miranda,
            Phobos,
            Titan,
            Triton)
        .enableInDim(NETHER, OW, THE_END)),

    Lead(new SmallOreBuilder().name("ore.small.lead")
        .heightRange(40, 180)
        .amount(16)
        .ore(Materials.Lead)
        .enableInDim(
            Asteroids,
            Mars,
            Ceres,
            Deimos,
            Ganymede,
            KuiperBelt,
            MakeMake,
            Mercury,
            Oberon,
            Pluto,
            Triton,
            VegaB,
            Venus,
            EndAsteroids)
        .enableInDim(NETHER, THE_END, TWILIGHT_FOREST)),

    Zinc(new SmallOreBuilder().name("ore.small.zinc")
        .heightRange(80, 210)
        .amount(24)
        .ore(Materials.Zinc)
        .enableInDim(Mars, BarnardE, Enceladus, Ganymede, Haumea, Io, Mercury, Proteus, Titan, EndAsteroids)
        .enableInDim(NETHER, OW, THE_END)),

    Gold(new SmallOreBuilder().name("ore.small.gold")
        .heightRange(20, 60)
        .amount(8)
        .ore(Materials.Gold)
        .enableInDim(
            Asteroids,
            Mars,
            BarnardF,
            Callisto,
            Ceres,
            KuiperBelt,
            Miranda,
            Phobos,
            Pluto,
            Venus,
            EndAsteroids)
        .enableInDim(OW, THE_END)),

    Silver(new SmallOreBuilder().name("ore.small.silver")
        .heightRange(20, 60)
        .amount(20)
        .ore(Materials.Silver)
        .enableInDim(Enceladus, Io, Oberon, Pluto, Proteus, Titan, Triton, VegaB, EndAsteroids)
        .enableInDim(NETHER, OW, THE_END)),

    Nickel(new SmallOreBuilder().name("ore.small.nickel")
        .heightRange(80, 150)
        .amount(8)
        .ore(Materials.Nickel)
        .enableInDim(
            MehenBelt,
            Asteroids,
            Mars,
            BarnardE,
            BarnardF,
            Ceres,
            Deimos,
            KuiperBelt,
            MakeMake,
            Mercury,
            Pluto,
            Venus,
            EndAsteroids)
        .enableInDim(OW, THE_END)),

    Lapis(new SmallOreBuilder().name("ore.small.lapis")
        .heightRange(10, 50)
        .amount(4)
        .ore(Materials.Lapis)
        .enableInDim(MehenBelt, Enceladus, Ganymede, Io, Oberon, Phobos, TcetiE)
        .enableInDim(OW)),

    Diamond(new SmallOreBuilder().name("ore.small.diamond")
        .heightRange(5, 15)
        .amount(2)
        .ore(Materials.Diamond)
        .enableInDim(Asteroids, Callisto, Ceres, Deimos, KuiperBelt, Oberon, Titan, Triton, VegaB, Venus)
        .enableInDim(OW)),

    Emerald(new SmallOreBuilder().name("ore.small.emerald")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Emerald)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Ruby(new SmallOreBuilder().name("ore.small.ruby")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Ruby)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Sapphire(new SmallOreBuilder().name("ore.small.sapphire")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Sapphire)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Greensapphire(new SmallOreBuilder().name("ore.small.greensapphire")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.GreenSapphire)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Olivine(new SmallOreBuilder().name("ore.small.olivine")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Olivine)
        .enableInDim(Horus, MehenBelt)
        .enableInDim(TWILIGHT_FOREST)),

    Topaz(new SmallOreBuilder().name("ore.small.topaz")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Topaz)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Tanzanite(new SmallOreBuilder().name("ore.small.tanzanite")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Tanzanite)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Amethyst(new SmallOreBuilder().name("ore.small.amethyst")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Amethyst)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Opal(new SmallOreBuilder().name("ore.small.opal")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Opal)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Jasper(new SmallOreBuilder().name("ore.small.jasper")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Jasper)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Bluetopaz(new SmallOreBuilder().name("ore.small.bluetopaz")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.BlueTopaz)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Amber(new SmallOreBuilder().name("ore.small.amber")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Amber)
        .enableInDim(TWILIGHT_FOREST)),

    Foolsruby(new SmallOreBuilder().name("ore.small.foolsruby")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.FoolsRuby)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Garnetred(new SmallOreBuilder().name("ore.small.garnetred")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.GarnetRed)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Garnetyellow(new SmallOreBuilder().name("ore.small.garnetyellow")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.GarnetYellow)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Redstone(new SmallOreBuilder().name("ore.small.redstone")
        .heightRange(5, 25)
        .amount(8)
        .ore(Materials.Redstone)
        .enableInDim(Mars, Ganymede, Io, Mercury, Proteus, Titan)
        .enableInDim(NETHER, OW)),

    Netherquartz(new SmallOreBuilder().name("ore.small.netherquartz")
        .heightRange(30, 120)
        .amount(64)
        .ore(Materials.NetherQuartz)
        .enableInDim(NETHER)),

    Saltpeter(new SmallOreBuilder().name("ore.small.saltpeter")
        .heightRange(10, 60)
        .amount(8)
        .ore(Materials.Saltpeter)
        .enableInDim(Mars, Deimos, Enceladus, Ganymede, Io, Proteus, Venus)
        .enableInDim(NETHER, TWILIGHT_FOREST)),

    Sulfur(new SmallOreBuilder().name("ore.small.sulfur")
        .heightRange(5, 60)
        .amount(40)
        .ore(Materials.Sulfur)
        .enableInDim(NETHER)),

    Titanium(new SmallOreBuilder().name("ore.small.titanium")
        .heightRange(10, 180)
        .amount(32)
        .ore(Materials.Titanium)
        .enableInDim(
            MehenBelt,
            Asteroids,
            Mars,
            BarnardE,
            BarnardF,
            Callisto,
            Ceres,
            Deimos,
            Enceladus,
            Ganymede,
            Haumea,
            Io,
            KuiperBelt,
            MakeMake,
            Mercury,
            Miranda,
            Oberon,
            Phobos,
            Pluto,
            Proteus,
            Titan,
            Triton,
            Venus)

    ),

    Tungsten(new SmallOreBuilder().name("ore.small.tungsten")
        .heightRange(10, 120)
        .amount(16)
        .ore(Materials.Tungsten)
        .enableInDim(Io, Venus)),

    Meteoriciron(new SmallOreBuilder().name("ore.small.meteoriciron")
        .heightRange(50, 70)
        .amount(8)
        .ore(Materials.MeteoricIron)
        .enableInDim(Moon, Mars, Deimos, Io, Phobos, Pluto, Venus)),

    Firestone(new SmallOreBuilder().name("ore.small.firestone")
        .heightRange(5, 15)
        .amount(2)
        .ore(Materials.Firestone)
        .enableInDim(Io, Venus)),

    Neutronium(new SmallOreBuilder().name("ore.small.neutronium")
        .heightRange(5, 15)
        .amount(8)
        .ore(Materials.Neutronium)
        .enableInDim(
            MehenBelt,
            BarnardE,
            BarnardF,
            Enceladus,
            Haumea,
            KuiperBelt,
            MakeMake,
            Oberon,
            Pluto,
            Proteus,
            TcetiE,
            Titan,
            Triton,
            VegaB)),

    Chromite(new SmallOreBuilder().name("ore.small.chromite")
        .heightRange(20, 40)
        .amount(8)
        .ore(Materials.Chromite)
        .enableInDim(
            MehenBelt,
            Asteroids,
            Mars,
            Callisto,
            Ceres,
            Deimos,
            Enceladus,
            Ganymede,
            Haumea,
            Io,
            KuiperBelt,
            MakeMake,
            Mercury,
            Oberon,
            Phobos,
            Pluto,
            Proteus,
            Titan,
            Triton,
            VegaB,
            Venus)),

    Tungstate(new SmallOreBuilder().name("ore.small.tungstate")
        .heightRange(20, 40)
        .amount(8)
        .ore(Materials.Tungstate)
        .enableInDim(
            Asteroids,
            Mars,
            Callisto,
            Ceres,
            Deimos,
            Enceladus,
            Ganymede,
            Io,
            KuiperBelt,
            Mercury,
            Miranda,
            Oberon,
            Phobos,
            Pluto,
            Proteus,
            Titan,
            Triton,
            Venus)),

    Naquadah(new SmallOreBuilder().name("ore.small.naquadah")
        .heightRange(5, 25)
        .amount(8)
        .ore(Materials.Naquadah)
        .enableInDim(BarnardE, BarnardF, Ceres, Enceladus, Io, KuiperBelt, Mercury, Pluto, Proteus, Venus)),

    Quantium(new SmallOreBuilder().name("ore.small.quantium")
        .heightRange(5, 25)
        .amount(6)
        .ore(Materials.Quantium)
        .enableInDim(Ceres, Haumea, Io, MakeMake, Pluto, Titan)),

    Mythril(new SmallOreBuilder().name("ore.small.mythril")
        .heightRange(5, 25)
        .amount(6)
        .ore(Materials.Mytryl)
        .enableInDim(Horus, Callisto, MakeMake, Miranda, Proteus, Venus)),

    Ledox(new SmallOreBuilder().name("ore.small.ledox")
        .heightRange(40, 60)
        .amount(4)
        .ore(Materials.Ledox)
        .enableInDim(Callisto, Enceladus, Haumea, Mercury, Oberon, Pluto)),

    Oriharukon(new SmallOreBuilder().name("ore.small.oriharukon")
        .heightRange(20, 40)
        .amount(6)
        .ore(Materials.Oriharukon)
        .enableInDim(Mars, Ceres, Haumea, MakeMake, Mercury, Titan, Triton)),

    Draconium(new SmallOreBuilder().name("ore.small.draconium")
        .heightRange(5, 15)
        .amount(4)
        .ore(Materials.Draconium)
        .enableInDim(Seth, Deimos, Ganymede, Haumea, MakeMake, Oberon, Phobos, Pluto, Venus)),

    Awdraconium(new SmallOreBuilder().name("ore.small.awdraconium")
        .heightRange(5, 15)
        .amount(2)
        .ore(Materials.DraconiumAwakened)
        .enableInDim(Seth, BarnardE, BarnardF, TcetiE, VegaB)),

    Desh(new SmallOreBuilder().name("ore.small.desh")
        .heightRange(10, 30)
        .amount(6)
        .ore(Materials.Desh)
        .enableInDim(Callisto, Deimos, Haumea, MakeMake, Mercury, Miranda, Phobos, Proteus, Triton)),

    Blackplutonium(new SmallOreBuilder().name("ore.small.blackplutonium")
        .heightRange(25, 45)
        .amount(6)
        .ore(Materials.BlackPlutonium)
        .enableInDim(BarnardE, BarnardF, Haumea, MakeMake, Pluto, Triton, VegaB)),

    Infinitycatalyst(new SmallOreBuilder().name("ore.small.infinitycatalyst")
        .heightRange(40, 80)
        .amount(6)
        .ore(Materials.InfinityCatalyst)
        .enableInDim(Anubis, VegaB)),

    Infinity(new SmallOreBuilder().name("ore.small.infinity")
        .heightRange(2, 40)
        .amount(2)
        .ore(Materials.Infinity)
        .disabledByDefault()),

    Bedrockium(new SmallOreBuilder().name("ore.small.bedrockium")
        .heightRange(5, 25)
        .amount(6)
        .ore(Materials.Bedrockium)
        .enableInDim(BarnardF)),

    Realgar(new SmallOreBuilder().name("ore.small.realgar")
        .heightRange(15, 85)
        .amount(32)
        .ore(Materials.Realgar)
        .enableInDim(BarnardF)
        .enableInDim(NETHER)),

    Certusquartz(new SmallOreBuilder().name("ore.small.certusquartz")
        .heightRange(5, 115)
        .amount(16)
        .ore(Materials.CertusQuartz)
        .enableInDim(Horus)
        .enableInDim(NETHER)),

    Jade(new SmallOreBuilder().name("ore.small.jade")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.Jade)
        .enableInDim(Horus)
        .enableInDim(TWILIGHT_FOREST)),

    Deepiron(new SmallOreBuilder().name("ore.small.deepiron")
        .heightRange(5, 40)
        .amount(8)
        .ore(Materials.DeepIron)
        .enableInDim(Mercury)),

    Redgarnet(new SmallOreBuilder().name("ore.small.redgarnet")
        .heightRange(5, 35)
        .amount(2)
        .ore(Materials.GarnetRed)
        .enableInDim(Horus)),

    Chargedcertus(new SmallOreBuilder().name("ore.small.chargedcertus")
        .heightRange(5, 115)
        .amount(4)
        .ore(Materials.CertusQuartzCharged)
        .enableInDim(Horus)),;
    // spotless : on

    public final SmallOreBuilder smallOreBuilder;

    private SmallOres(gregtech.common.SmallOreBuilder smallOreBuilder) {
        this.smallOreBuilder = smallOreBuilder;
    }

    public GT_Worldgen_GT_Ore_SmallPieces addGTSmallOre() {
        return new GT_Worldgen_GT_Ore_SmallPieces(this.smallOreBuilder);
    }

    public GT_Worldgen_GT_Ore_SmallPieces_Space addGaGregSmallOre() {
        return new GT_Worldgen_GT_Ore_SmallPieces_Space(this.smallOreBuilder);
    }
}
