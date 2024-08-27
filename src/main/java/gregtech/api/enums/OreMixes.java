package gregtech.api.enums;

import static bloodasp.galacticgreg.api.enums.DimensionDef.*;
import static gregtech.common.OreMixBuilder.NETHER;
import static gregtech.common.OreMixBuilder.OW;
import static gregtech.common.OreMixBuilder.THE_END;
import static gregtech.common.OreMixBuilder.TWILIGHT_FOREST;

import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_Layer_Space;
import bloodasp.galacticgreg.api.enums.DimensionDef;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.OreMixBuilder;

public enum OreMixes {

    // spotless : off
    Naquadah(new OreMixBuilder().name("ore.mix.naquadah")
        .heightRange(10, 90)
        .weight(30)
        .density(4)
        .size(32)
        .enableInDim(
            EndAsteroids,
            Maahes,
            Asteroids,
            BarnardE,
            BarnardF,
            Haumea,
            KuiperBelt,
            Mercury,
            Oberon,
            Pluto,
            Titan,
            VegaB,
            Venus)
        .primary(Materials.Naquadah)
        .secondary(Materials.Naquadah)
        .inBetween(Materials.Naquadah)
        .sporadic(Materials.NaquadahEnriched)),

    LigniteCoal(new OreMixBuilder().name("ore.mix.lignite")
        .heightRange(80, 210)
        .weight(160)
        .density(7)
        .size(32)
        .enableInDim(OW)
        .enableInDim(BarnardC)
        .primary(Materials.Lignite)
        .secondary(Materials.Lignite)
        .inBetween(Materials.Lignite)
        .sporadic(Materials.Coal)),

    Coal(new OreMixBuilder().name("ore.mix.coal")
        .heightRange(30, 80)
        .weight(80)
        .density(5)
        .size(32)
        .enableInDim(OW, TWILIGHT_FOREST)
        .primary(Materials.Coal)
        .secondary(Materials.Coal)
        .inBetween(Materials.Coal)
        .sporadic(Materials.Lignite)),

    Magnetite(new OreMixBuilder().name("ore.mix.magnetite")
        .heightRange(60, 180)
        .weight(160)
        .density(2)
        .size(32)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(Seth, BarnardE, Ceres, Deimos, Io, MakeMake, TcetiE)
        .primary(Materials.Magnetite)
        .secondary(Materials.Magnetite)
        .inBetween(Materials.Iron)
        .sporadic(Materials.VanadiumMagnetite)),

    Gold(new OreMixBuilder().name("ore.mix.gold")
        .heightRange(30, 60)
        .weight(160)
        .density(2)
        .size(32)
        .enableInDim(OW, THE_END, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Seth, Asteroids, Mars, BarnardF, Callisto, Phobos, Pluto, TcetiE, Triton, VegaB)
        .primary(Materials.Magnetite)
        .secondary(Materials.Magnetite)
        .inBetween(Materials.VanadiumMagnetite)
        .sporadic(Materials.Gold)),

    Iron(new OreMixBuilder().name("ore.mix.iron")
        .heightRange(10, 40)
        .weight(120)
        .density(3)
        .size(24)
        .enableInDim(OW, NETHER, TWILIGHT_FOREST)
        .enableInDim(Mars, Callisto, Ceres, Ganymede, Mercury, Oberon, Pluto)
        .primary(Materials.BrownLimonite)
        .secondary(Materials.YellowLimonite)
        .inBetween(Materials.BandedIron)
        .sporadic(Materials.Malachite)),

    Cassiterite(new OreMixBuilder().name("ore.mix.cassiterite")
        .heightRange(60, 220)
        .weight(50)
        .density(4)
        .size(24)
        .enableInDim(EndAsteroids, MehenBelt, Seth, Moon, Io, Miranda, TcetiE, Venus)
        .enableInDim(OW, THE_END, TWILIGHT_FOREST)
        .primary(Materials.Tin)
        .secondary(Materials.Tin)
        .inBetween(Materials.Cassiterite)
        .sporadic(Materials.Tin)),

    Tetrahedrite(new OreMixBuilder().name("ore.mix.tetrahedrite")
        .heightRange(80, 120)
        .weight(70)
        .density(3)
        .size(24)
        .enableInDim(NETHER, THE_END)
        .enableInDim(EndAsteroids, Asteroids, Mars, CentauriAlpha, Deimos, Ganymede, KuiperBelt, Miranda, VegaB, Venus)
        .primary(Materials.Tetrahedrite)
        .secondary(Materials.Tetrahedrite)
        .inBetween(Materials.Copper)
        .sporadic(Materials.Stibnite)),

    NetherQuartz(new OreMixBuilder().name("ore.mix.netherquartz")
        .heightRange(40, 80)
        .weight(80)
        .density(4)
        .size(24)
        .enableInDim(NETHER)
        .enableInDim(Neper, CentauriAlpha)
        .primary(Materials.NetherQuartz)
        .secondary(Materials.NetherQuartz)
        .inBetween(Materials.NetherQuartz)
        .sporadic(Materials.Quartzite)),

    Sulfur(new OreMixBuilder().name("ore.mix.sulfur")
        .heightRange(5, 20)
        .weight(100)
        .density(4)
        .size(24)
        .enableInDim(NETHER)
        .enableInDim(Anubis, Mars, CentauriAlpha, Deimos, Io, Phobos, Venus)
        .primary(Materials.Sulfur)
        .secondary(Materials.Sulfur)
        .inBetween(Materials.Pyrite)
        .sporadic(Materials.Sphalerite)),

    Copper(new OreMixBuilder().name("ore.mix.copper")
        .heightRange(5, 60)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(EndAsteroids, Moon, BarnardE, BarnardF, Callisto, Ceres, Enceladus, Proteus)
        .enableInDim(OW, NETHER, THE_END)
        .primary(Materials.Chalcopyrite)
        .secondary(Materials.Iron)
        .inBetween(Materials.Pyrite)
        .sporadic(Materials.Copper)),

    Bauxite(new OreMixBuilder().name("ore.mix.bauxite")
        .heightRange(10, 80)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(
            Seth,
            Moon,
            Asteroids,
            Ganymede,
            Haumea,
            KuiperBelt,
            MakeMake,
            Mercury,
            Phobos,
            Pluto,
            Proteus,
            TcetiE,
            Titan)
        .primary(Materials.Bauxite)
        .secondary(Materials.Ilmenite)
        .inBetween(Materials.Aluminium)
        .sporadic(Materials.Ilmenite)),

    Salts(new OreMixBuilder().name("ore.mix.salts")
        .heightRange(50, 70)
        .weight(50)
        .density(2)
        .size(24)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(Mars, BarnardC, TcetiE)
        .primary(Materials.RockSalt)
        .secondary(Materials.Salt)
        .inBetween(Materials.Lepidolite)
        .sporadic(Materials.Spodumene)),

    Redstone(new OreMixBuilder().name("ore.mix.redstone")
        .heightRange(5, 40)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(OW, NETHER)
        .enableInDim(Mars, BarnardF, CentauriAlpha, Ganymede, Mercury, Miranda, VegaB, Venus)
        .primary(Materials.Redstone)
        .secondary(Materials.Redstone)
        .inBetween(Materials.Ruby)
        .sporadic(Materials.Cinnabar)),

    Soapstone(new OreMixBuilder().name("ore.mix.soapstone")
        .heightRange(20, 50)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(Anubis, Maahes, Ceres)
        .primary(Materials.Soapstone)
        .secondary(Materials.Talc)
        .inBetween(Materials.Glauconite)
        .sporadic(Materials.Pentlandite)),

    Nickel(new OreMixBuilder().name("ore.mix.nickel")
        .heightRange(10, 40)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(THE_END, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, MehenBelt, Mars, Deimos, KuiperBelt, Phobos, Titan, Triton, Venus)
        .primary(Materials.Garnierite)
        .secondary(Materials.Nickel)
        .inBetween(Materials.Cobaltite)
        .sporadic(Materials.Pentlandite)),

    Platinum(new OreMixBuilder().name("ore.mix.platinum")
        .heightRange(40, 50)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(EndAsteroids, Maahes, MehenBelt)
        .primary(Materials.Cooperite)
        .secondary(Materials.Palladium)
        .inBetween(Materials.Platinum)
        .sporadic(Materials.Iridium)),

    Pitchblende(new OreMixBuilder().name("ore.mix.pitchblende")
        .heightRange(60, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Mars, BarnardF, CentauriAlpha, Haumea, Io, KuiperBelt, MakeMake, Oberon, Phobos, VegaB, Venus)
        .primary(Materials.Pitchblende)
        .secondary(Materials.Pitchblende)
        .inBetween(Materials.Uraninite)
        .sporadic(Materials.Uraninite)),

    Monazite(new OreMixBuilder().name("ore.mix.monazite")
        .heightRange(20, 40)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(Seth, Moon, BarnardF, Callisto, Deimos, Enceladus, Haumea, Io, MakeMake, Titan, Triton, Venus)
        .primary(Materials.Bastnasite)
        .secondary(Materials.Bastnasite)
        .inBetween(Materials.Monazite)
        .sporadic(Materials.Neodymium)),

    Molybdenum(new OreMixBuilder().name("ore.mix.molybdenum")
        .heightRange(20, 50)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(NETHER, THE_END, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Moon, BarnardE, Ceres, Mercury, Phobos, Pluto, Proteus, Titan)
        .primary(Materials.Wulfenite)
        .secondary(Materials.Molybdenite)
        .inBetween(Materials.Molybdenum)
        .sporadic(Materials.Powellite)),

    Tungstate(new OreMixBuilder().name("ore.mix.tungstate")
        .heightRange(20, 60)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(
            EndAsteroids,
            Asteroids,
            Mars,
            Callisto,
            Deimos,
            Enceladus,
            Ganymede,
            Haumea,
            KuiperBelt,
            MakeMake,
            Oberon,
            Pluto,
            Triton,
            VegaB)
        .primary(Materials.Scheelite)
        .secondary(Materials.Scheelite)
        .inBetween(Materials.Tungstate)
        .sporadic(Materials.Lithium)),

    Sapphire(new OreMixBuilder().name("ore.mix.sapphire")
        .heightRange(10, 40)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Horus)
        .primary(Materials.Almandine)
        .secondary(Materials.Pyrope)
        .inBetween(Materials.Sapphire)
        .sporadic(Materials.GreenSapphire)),

    Manganese(new OreMixBuilder().name("ore.mix.manganese")
        .heightRange(20, 30)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(OW, NETHER)
        .enableInDim(EndAsteroids, BarnardE, BarnardF, CentauriAlpha, Ceres, Io, Oberon, Titan, Triton)
        .primary(Materials.Grossular)
        .secondary(Materials.Spessartine)
        .inBetween(Materials.Pyrolusite)
        .sporadic(Materials.Tantalite)),

    Quartz(new OreMixBuilder().name("ore.mix.quartz")
        .heightRange(80, 120)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(NETHER)
        .enableInDim(Neper)
        .primary(Materials.Quartzite)
        .secondary(Materials.Barite)
        .inBetween(Materials.CertusQuartz)
        .sporadic(Materials.CertusQuartz)),

    Diamond(new OreMixBuilder().name("ore.mix.diamond")
        .heightRange(5, 20)
        .weight(40)
        .density(1)
        .size(16)
        .enableInDim(BarnardF, Ganymede, KuiperBelt, Mercury, Miranda, Phobos, Pluto, Proteus, Titan)
        .enableInDim(OW, TWILIGHT_FOREST)
        .primary(Materials.Graphite)
        .secondary(Materials.Graphite)
        .inBetween(Materials.Diamond)
        .sporadic(Materials.Coal)),

    Olivine(new OreMixBuilder().name("ore.mix.olivine")
        .heightRange(10, 40)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Anubis, MehenBelt, BarnardE, Ceres, Haumea, MakeMake)
        .primary(Materials.Bentonite)
        .secondary(Materials.Magnesite)
        .inBetween(Materials.Olivine)
        .sporadic(Materials.Glauconite)),

    Apatite(new OreMixBuilder().name("ore.mix.apatite")
        .heightRange(40, 60)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TcetiE.modDimensionDef.getDimensionName(), OW, OreMixBuilder.TWILIGHT_FOREST)
        .primary(Materials.Apatite)
        .secondary(Materials.Apatite)
        .inBetween(Materials.TricalciumPhosphate)
        .sporadic(Materials.Pyrochlore)),

    Galena(new OreMixBuilder().name("ore.mix.galena")
        .heightRange(5, 45)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(Moon, Mars, Ganymede, Oberon, Triton, VegaB, Venus)
        .primary(Materials.Galena)
        .secondary(Materials.Galena)
        .inBetween(Materials.Silver)
        .sporadic(Materials.Lead)),

    Lapis(new OreMixBuilder().name("ore.mix.lapis")
        .heightRange(20, 50)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, MehenBelt, Ceres, Deimos, Enceladus, VegaB)
        .primary(Materials.Lazurite)
        .secondary(Materials.Sodalite)
        .inBetween(Materials.Lapis)
        .sporadic(Materials.Calcite)),

    Beryllium(new OreMixBuilder().name("ore.mix.beryllium")
        .heightRange(5, 30)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(OreMixBuilder.NETHER, OreMixBuilder.THE_END)
        .enableInDim(EndAsteroids, Mars, BarnardF, CentauriAlpha, Ceres, Haumea, MakeMake, Pluto, Titan, Venus)
        .primary(Materials.Beryllium)
        .secondary(Materials.Beryllium)
        .inBetween(Materials.Emerald)
        .sporadic(Materials.Thorium)),

    Uranium(new OreMixBuilder().name("ore.mix.uranium")
        .heightRange(20, 30)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(
            MehenBelt,
            BarnardE,
            Ceres,
            Deimos,
            Enceladus,
            Ganymede,
            Haumea,
            KuiperBelt,
            MakeMake,
            Phobos,
            Proteus)
        .primary(Materials.Uraninite)
        .secondary(Materials.Uraninite)
        .inBetween(Materials.Uranium)
        .sporadic(Materials.Uranium)),
    OilSand(new OreMixBuilder().name("ore.mix.oilsand")
        .heightRange(50, 80)
        .weight(40)
        .density(5)
        .size(16)
        .enableInDim(OW)
        .enableInDim(BarnardC, TcetiE)
        .primary(Materials.Oilsands)
        .secondary(Materials.Oilsands)
        .inBetween(Materials.Oilsands)
        .sporadic(Materials.Oilsands)),

    Neutronium(new OreMixBuilder().name("ore.mix.neutronium")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(Maahes, MehenBelt, BarnardE, BarnardF, Haumea, KuiperBelt, MakeMake, Pluto, Proteus, Triton, VegaB)
        .primary(Materials.Neutronium)
        .secondary(Materials.Adamantium)
        .inBetween(Materials.Naquadah)
        .sporadic(Materials.Titanium)),

    AquaIgnis(new OreMixBuilder().name("ore.mix.aquaignis")
        .heightRange(5, 35)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(Neper.modDimensionDef.getDimensionName(), OreMixBuilder.TWILIGHT_FOREST)
        .primary(Materials.InfusedWater)
        .secondary(Materials.InfusedFire)
        .inBetween(Materials.Amber)
        .sporadic(Materials.Cinnabar)),

    TerraAer(new OreMixBuilder().name("ore.mix.terraaer")
        .heightRange(5, 35)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(Neper)
        .enableInDim(TWILIGHT_FOREST)
        .primary(Materials.InfusedEarth)
        .secondary(Materials.InfusedAir)
        .inBetween(Materials.Amber)
        .sporadic(Materials.Cinnabar)),

    PerditioOrdo(new OreMixBuilder().name("ore.mix.perditioordo")
        .heightRange(5, 35)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Neper)
        .primary(Materials.InfusedEntropy)
        .secondary(Materials.InfusedOrder)
        .inBetween(Materials.Amber)
        .sporadic(Materials.Cinnabar)),

    CopperTin(new OreMixBuilder().name("ore.mix.coppertin")
        .heightRange(80, 200)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .primary(Materials.Chalcopyrite)
        .secondary(Materials.Vermiculite)
        .inBetween(Materials.Cassiterite)
        .sporadic(Materials.Alunite)),

    TitaniumChrome(new OreMixBuilder().name("ore.mix.titaniumchrome")
        .heightRange(10, 70)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Moon, Asteroids, Callisto, Ganymede, Mercury, Miranda, Pluto, Proteus, TcetiE, Titan)
        .primary(Materials.Ilmenite)
        .secondary(Materials.Chromite)
        .inBetween(Materials.Uvarovite)
        .sporadic(Materials.Perlite)),

    MineralSand(new OreMixBuilder().name("ore.mix.mineralsand")
        .heightRange(50, 60)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .enableInDim(Anubis, Maahes, BarnardC, DimensionDef.Europa)
        .primary(Materials.BasalticMineralSand)
        .secondary(Materials.GraniticMineralSand)
        .inBetween(Materials.FullersEarth)
        .sporadic(Materials.Gypsum)),

    GarnetTin(new OreMixBuilder().name("ore.mix.garnettin")
        .heightRange(50, 60)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .enableInDim(CentauriAlpha, DimensionDef.Europa)
        .primary(Materials.CassiteriteSand)
        .secondary(Materials.GarnetSand)
        .inBetween(Materials.Asbestos)
        .sporadic(Materials.Diatomite)),

    KaoliniteZeolite(new OreMixBuilder().name("ore.mix.kaolinitezeolite")
        .heightRange(50, 70)
        .weight(60)
        .density(4)
        .size(16)
        .enableInDim(OW)
        .enableInDim(Neper, TcetiE)
        .primary(Materials.Kaolinite)
        .secondary(Materials.Zeolite)
        .inBetween(Materials.FullersEarth)
        .sporadic(Materials.GlauconiteSand)),

    Mica(new OreMixBuilder().name("ore.mix.mica")
        .heightRange(20, 40)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Neper, BarnardC)
        .enableInDim(OW)
        .primary(Materials.Kyanite)
        .secondary(Materials.Mica)
        .inBetween(Materials.Cassiterite)
        .sporadic(Materials.Pollucite)),

    Dolomite(new OreMixBuilder().name("ore.mix.dolomite")
        .heightRange(150, 200)
        .weight(40)
        .density(4)
        .size(24)
        .enableInDim(OW)
        .enableInDim(Anubis, Neper)
        .primary(Materials.Dolomite)
        .secondary(Materials.Wollastonite)
        .inBetween(Materials.Trona)
        .sporadic(Materials.Andradite)),

    PlatinumChrome(new OreMixBuilder().name("ore.mix.platinumchrome")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, Callisto, Ceres, Ganymede, Io, KuiperBelt, Mercury, Oberon, Pluto)
        .primary(Materials.Platinum)
        .secondary(Materials.Chrome)
        .inBetween(Materials.Cooperite)
        .sporadic(Materials.Palladium)),

    IridiumMytryl(new OreMixBuilder().name("ore.mix.iridiummytryl")
        .heightRange(15, 40)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, Enceladus, Io, KuiperBelt, Mercury, Miranda, Titan, Triton, Venus)
        .primary(Materials.Nickel)
        .secondary(Materials.Iridium)
        .inBetween(Materials.Palladium)
        .sporadic(Materials.Mithril)),

    Osmium(new OreMixBuilder().name("ore.mix.osmium")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, BarnardC, Enceladus, KuiperBelt, Miranda, Oberon, Pluto, Proteus, Titan)
        .primary(Materials.Nickel)
        .secondary(Materials.Osmium)
        .inBetween(Materials.Iridium)
        .sporadic(Materials.Nickel)),

    SaltPeterElectrotine(new OreMixBuilder().name("ore.mix.saltpeterelectrotine")
        .heightRange(5, 45)
        .weight(40)
        .density(3)
        .size(16)
        .enableInDim(NETHER)
        .enableInDim(CentauriAlpha, Ceres)
        .primary(Materials.Saltpeter)
        .secondary(Materials.Diatomite)
        .inBetween(Materials.Electrotine)
        .sporadic(Materials.Alunite)),

    Desh(new OreMixBuilder().name("ore.mix.desh")
        .heightRange(5, 40)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Mars, Miranda)
        .primary(Materials.Desh)
        .secondary(Materials.Desh)
        .inBetween(Materials.Scheelite)
        .sporadic(Materials.Tungstate)),

    Draconium(new OreMixBuilder().name("ore.mix.draconium")
        .heightRange(20, 40)
        .weight(40)
        .density(1)
        .size(16)
        .enableInDim(Horus, Seth, Deimos, Mercury, Miranda, Phobos)
        .primary(Materials.Draconium)
        .secondary(Materials.Electrotine)
        .inBetween(Materials.Jade)
        .sporadic(Materials.Vinteum)),

    Quantium(new OreMixBuilder().name("ore.mix.quantium")
        .heightRange(5, 25)
        .weight(30)
        .density(3)
        .size(24)
        .enableInDim(Horus, Maahes, Venus)
        .primary(Materials.Quantium)
        .secondary(Materials.Amethyst)
        .inBetween(Materials.Rutile)
        .sporadic(Materials.Ardite)),

    CallistoIce(new OreMixBuilder().name("ore.mix.callistoice")
        .heightRange(40, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Maahes, Callisto)
        .primary(Materials.CallistoIce)
        .secondary(Materials.Topaz)
        .inBetween(Materials.BlueTopaz)
        .sporadic(Materials.Alduorite)),

    Mytryl(new OreMixBuilder().name("ore.mix.mytryl")
        .heightRange(10, 30)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Horus, Io)
        .primary(Materials.Mytryl)
        .secondary(Materials.Jasper)
        .inBetween(Materials.Ceruclase)
        .sporadic(Materials.Vulcanite)),

    Ledox(new OreMixBuilder().name("ore.mix.ledox")
        .heightRange(55, 65)
        .weight(30)
        .density(2)
        .size(24)
        .enableInDim(Horus, Enceladus, DimensionDef.Europa)
        .primary(Materials.Ledox)
        .secondary(Materials.Opal)
        .inBetween(Materials.Orichalcum)
        .sporadic(Materials.Rubracium)),

    Oriharukon(new OreMixBuilder().name("ore.mix.oriharukon")
        .heightRange(30, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Horus, Deimos, Phobos)
        .primary(Materials.Oriharukon)
        .secondary(Materials.Tanzanite)
        .inBetween(Materials.Vyroxeres)
        .sporadic(Materials.Mirabilite)),

    BlackPlutonium(new OreMixBuilder().name("ore.mix.blackplutonium")
        .heightRange(5, 25)
        .weight(40)
        .density(2)
        .size(24)
        .enableInDim(Horus, BarnardC, CentauriAlpha, MakeMake, Pluto, TcetiE)
        .primary(Materials.BlackPlutonium)
        .secondary(Materials.GarnetRed)
        .inBetween(Materials.GarnetYellow)
        .sporadic(Materials.Borax)),

    InfusedGold(new OreMixBuilder().name("ore.mix.infusedgold")
        .heightRange(15, 40)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(BarnardE, Haumea, Io, Proteus, Titan, VegaB)
        .primary(Materials.Gold)
        .secondary(Materials.Gold)
        .inBetween(Materials.InfusedGold)
        .sporadic(Materials.Platinum)),

    Niobium(new OreMixBuilder().name("ore.mix.niobium")
        .heightRange(5, 30)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(BarnardF, MakeMake, Triton, VegaB)
        .primary(Materials.Niobium)
        .secondary(Materials.Yttrium)
        .inBetween(Materials.Gallium)
        .sporadic(Materials.Gallium)),

    TungstenIrons(new OreMixBuilder().name("ore.mix.tungstenirons")
        .heightRange(5, 25)
        .weight(16)
        .density(2)
        .size(30)
        .enableInDim(Neper, BarnardC, BarnardE, BarnardF, Oberon, Pluto, Proteus, Triton)
        .primary(Materials.Tungsten)
        .secondary(Materials.Silicon)
        .inBetween(Materials.DeepIron)
        .sporadic(Materials.ShadowIron)),

    UraniumGTNH(new OreMixBuilder().name("ore.mix.uraniumgtnh")
        .heightRange(10, 30)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(BarnardE, BarnardF, Haumea, Pluto, Triton, VegaB)
        .primary(Materials.Thorium)
        .secondary(Materials.Uranium)
        .inBetween(Materials.Plutonium241)
        .sporadic(Materials.Uranium235)),

    VanadiumGold(new OreMixBuilder().name("ore.mix.vanadiumgold")
        .heightRange(10, 50)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(Maahes, BarnardF, Enceladus, MakeMake, Proteus, VegaB)
        .primary(Materials.Vanadium)
        .secondary(Materials.Magnetite)
        .inBetween(Materials.Gold)
        .sporadic(Materials.Chrome)),

    NetherStar(new OreMixBuilder().name("ore.mix.netherstar")
        .heightRange(20, 60)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(Horus, BarnardE, Haumea, TcetiE, VegaB)
        .primary(Materials.GarnetSand)
        .secondary(Materials.NetherStar)
        .inBetween(Materials.GarnetRed)
        .sporadic(Materials.GarnetYellow)),

    Garnet(new OreMixBuilder().name("ore.mix.garnet")
        .heightRange(10, 30)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Horus, BarnardF, MakeMake, VegaB)
        .primary(Materials.GarnetRed)
        .secondary(Materials.GarnetYellow)
        .inBetween(Materials.Chrysotile)
        .sporadic(Materials.Realgar)),

    RareEarth(new OreMixBuilder().name("ore.mix.rareearth")
        .heightRange(30, 60)
        .weight(40)
        .density(2)
        .size(24)
        .enableInDim(BarnardE, BarnardF, CentauriAlpha, VegaB)
        .primary(Materials.Cadmium)
        .secondary(Materials.Caesium)
        .inBetween(Materials.Lanthanum)
        .sporadic(Materials.Cerium)),

    RichNuclear(new OreMixBuilder().name("ore.mix.richnuclear")
        .heightRange(55, 120)
        .weight(5)
        .density(2)
        .size(8)
        .enableInDim(Callisto, Ceres, Ganymede, Io)
        .primary(Materials.Uranium)
        .secondary(Materials.Plutonium)
        .inBetween(Materials.Thorium)
        .sporadic(Materials.Thorium)),

    HeavyPentele(new OreMixBuilder().name("ore.mix.heavypentele")
        .heightRange(40, 60)
        .weight(60)
        .density(5)
        .size(32)
        .enableInDim(Neper, Mars, BarnardC, Mercury, Phobos, Titan, VegaB)
        .primary(Materials.Arsenic)
        .secondary(Materials.Bismuth)
        .inBetween(Materials.Antimony)
        .sporadic(Materials.Antimony)),

    Europa(new OreMixBuilder().name("ore.mix.europa")
        .heightRange(55, 65)
        .weight(110)
        .density(4)
        .size(24)
        .enableInDim(Horus, DimensionDef.Europa, TcetiE)
        .primary(Materials.Magnesite)
        .secondary(Materials.BandedIron)
        .inBetween(Materials.Sulfur)
        .sporadic(Materials.Opal)),

    EuropaCore(new OreMixBuilder().name("ore.mix.europacore")
        .heightRange(5, 15)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(Maahes, DimensionDef.Europa, TcetiE)
        .primary(Materials.Chrome)
        .secondary(Materials.Tungstate)
        .inBetween(Materials.Molybdenum)
        .sporadic(Materials.Manganese)),

    SecondLanthanid(new OreMixBuilder().name("ore.mix.secondlanthanid")
        .heightRange(10, 40)
        .weight(10)
        .density(3)
        .size(24)
        .enableInDim(Seth, BarnardC, CentauriAlpha)
        .primary(Materials.Samarium)
        .secondary(Materials.Neodymium)
        .inBetween(Materials.Tartarite)
        .sporadic(Materials.Tartarite)),

    QuartzSpace(new OreMixBuilder().name("ore.mix.quartzspace")
        .heightRange(40, 80)
        .weight(20)
        .density(3)
        .size(16)
        .enableInDim(Horus, Moon, Mars, CentauriAlpha, Io, Phobos, Proteus, TcetiE, Venus)
        .primary(Materials.Quartzite)
        .secondary(Materials.Barite)
        .inBetween(Materials.CertusQuartz)
        .sporadic(Materials.CertusQuartz)),

    Rutile(new OreMixBuilder().name("ore.mix.rutile")
        .heightRange(5, 20)
        .weight(8)
        .density(4)
        .size(12)
        .enableInDim(Anubis, Titan, Venus)
        .primary(Materials.Rutile)
        .secondary(Materials.Titanium)
        .inBetween(Materials.Bauxite)
        .sporadic(Materials.MeteoricIron)),

    TFGalena(new OreMixBuilder().name("ore.mix.tfgalena")
        .heightRange(5, 35)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Anubis, Maahes)
        .primary(Materials.Galena)
        .secondary(Materials.Silver)
        .inBetween(Materials.Lead)
        .sporadic(Materials.Cryolite)),

    LuVTantalite(new OreMixBuilder().name("ore.mix.luvtantalite")
        .heightRange(20, 30)
        .weight(10)
        .density(4)
        .size(16)
        .enableInDim(Io, Miranda)
        .primary(Materials.Pyrolusite)
        .secondary(Materials.Apatite)
        .inBetween(Materials.Tantalite)
        .sporadic(Materials.Pyrochlore)),

    CertusQuartz(new OreMixBuilder().name("ore.mix.certusquartz")
        .heightRange(40, 80)
        .weight(60)
        .density(5)
        .size(32)
        .enableInDim(Horus, Neper)
        .primary(Materials.CertusQuartz)
        .secondary(Materials.CertusQuartz)
        .inBetween(Materials.CertusQuartzCharged)
        .sporadic(Materials.QuartzSand)),

    InfinityCatalyst(new OreMixBuilder().name("ore.mix.infinitycatalyst")
        .heightRange(5, 20)
        .weight(15)
        .density(2)
        .size(16)
        .enableInDim(Anubis)
        .primary(Materials.Neutronium)
        .secondary(Materials.Adamantium)
        .inBetween(Materials.InfinityCatalyst)
        .sporadic(Materials.Bedrockium)),

    CosmicNeutronium(new OreMixBuilder().name("ore.mix.cosmicneutronium")
        .heightRange(5, 20)
        .weight(15)
        .density(2)
        .size(16)
        .enableInDim(Horus)
        .primary(Materials.Neutronium)
        .secondary(Materials.CosmicNeutronium)
        .inBetween(Materials.BlackPlutonium)
        .sporadic(Materials.Bedrockium)),

    Dilithium(new OreMixBuilder().name("ore.mix.dilithium")
        .heightRange(30, 100)
        .weight(30)
        .density(3)
        .size(24)
        .enableInDim(Neper)
        .primary(Materials.Dilithium)
        .secondary(Materials.Dilithium)
        .inBetween(Materials.MysteriousCrystal)
        .sporadic(Materials.Vinteum)),

    Naquadria(new OreMixBuilder().name("ore.mix.naquadria")
        .heightRange(10, 90)
        .weight(40)
        .density(4)
        .size(24)
        .enableInDim(Maahes)
        .primary(Materials.Naquadah)
        .secondary(Materials.NaquadahEnriched)
        .inBetween(Materials.Naquadria)
        .sporadic(Materials.Trinium)),

    AwakenedDraconium(new OreMixBuilder().name("ore.mix.awakeneddraconium")
        .heightRange(20, 40)
        .weight(20)
        .density(3)
        .size(16)
        .enableInDim(MehenBelt)
        .primary(Materials.Draconium)
        .secondary(Materials.Draconium)
        .inBetween(Materials.DraconiumAwakened)
        .sporadic(Materials.NetherStar)),

    Tengam(new OreMixBuilder().name("ore.mix.tengam")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(32)
        .enableInDim(MehenBelt)
        .primary(Materials.TengamRaw)
        .secondary(Materials.TengamRaw)
        .inBetween(Materials.Electrotine)
        .sporadic(Materials.Samarium));

    // spotless : on

    public final OreMixBuilder oreMixBuilder;

    private OreMixes(OreMixBuilder oreMixBuilder) {
        this.oreMixBuilder = oreMixBuilder;
    }

    public GT_Worldgen_GT_Ore_Layer addGTOreLayer() {
        return new GT_Worldgen_GT_Ore_Layer(this.oreMixBuilder);
    }

    public GT_Worldgen_GT_Ore_Layer_Space addGaGregOreLayer() {
        return new GT_Worldgen_GT_Ore_Layer_Space(this.oreMixBuilder);
    }
}
