package gregtech.api.enums;

import static galacticgreg.api.enums.DimensionDef.*;
import static galacticgreg.api.enums.DimensionDef.DimNames.ASTEROIDS;
import static galacticgreg.api.enums.DimensionDef.DimNames.EVERGLADES;
import static galacticgreg.api.enums.DimensionDef.DimNames.KUIPERBELT;
import static galacticgreg.api.enums.DimensionDef.DimNames.NETHER;
import static galacticgreg.api.enums.DimensionDef.DimNames.OW;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128B;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128BA;
import static galacticgreg.api.enums.DimensionDef.DimNames.THE_END;
import static galacticgreg.api.enums.DimensionDef.DimNames.TWILIGHT_FOREST;

import bartworks.system.material.WerkstoffLoader;
import galacticgreg.api.enums.DimensionDef;
import gregtech.common.OreMixBuilder;
import gregtech.common.WorldgenGTOreLayer;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

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
        .sporadic(Materials.Gold)
        .localize(Materials.Gold)),

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
        .sporadic(Materials.Malachite)
        .localize(Materials.Iron)),

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
        .sporadic(Materials.Tin)
        .localize(Materials.Cassiterite)),

    Tetrahedrite(new OreMixBuilder().name("ore.mix.tetrahedrite")
        .heightRange(80, 120)
        .weight(70)
        .density(3)
        .size(24)
        .enableInDim(NETHER, THE_END)
        .enableInDim(EndAsteroids, Asteroids, Mars, CentauriBb, Deimos, Ganymede, KuiperBelt, Miranda, VegaB, Venus)
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
        .enableInDim(Neper, CentauriBb)
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
        .enableInDim(Anubis, Mars, CentauriBb, Deimos, Io, Phobos, Venus)
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
        .sporadic(Materials.Copper)
        .localize(Materials.Copper)),

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
        .sporadic(Materials.Spodumene)
        .localize(Materials.Salt)),

    Redstone(new OreMixBuilder().name("ore.mix.redstone")
        .heightRange(5, 40)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(OW, NETHER)
        .enableInDim(Mars, BarnardF, CentauriBb, Ganymede, Mercury, Miranda, VegaB, Venus)
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
        .sporadic(Materials.Pentlandite)
        .localize(Materials.Nickel)),

    Platinum(new OreMixBuilder().name("ore.mix.platinum")
        .heightRange(40, 50)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(EndAsteroids, Asteroids, Maahes, MehenBelt)
        .primary(Materials.Cooperite)
        .secondary(Materials.Palladium)
        .inBetween(Materials.Platinum)
        .sporadic(Materials.Iridium)
        .localize(Materials.Platinum)),

    Pitchblende(new OreMixBuilder().name("ore.mix.pitchblende")
        .heightRange(30, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Mars, BarnardF, CentauriBb, Haumea, Io, KuiperBelt, MakeMake, Oberon, Phobos, VegaB, Venus)
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
        .sporadic(Materials.Neodymium)
        .localize(Materials.Monazite)),

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
        .sporadic(Materials.Powellite)
        .localize(Materials.Molybdenum)),

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
        .sporadic(Materials.Lithium)
        .localize(Materials.Tungstate)),

    Sapphire(new OreMixBuilder().name("ore.mix.sapphire")
        .heightRange(10, 25)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Horus)
        .primary(Materials.Almandine)
        .secondary(Materials.Pyrope)
        .inBetween(Materials.Sapphire)
        .sporadic(Materials.GreenSapphire)
        .localize(Materials.Sapphire)),

    Manganese(new OreMixBuilder().name("ore.mix.manganese")
        .heightRange(20, 30)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(OW, NETHER)
        .enableInDim(EndAsteroids, BarnardE, BarnardF, CentauriBb, Ceres, Io, Oberon, Titan, Triton)
        .primary(Materials.Grossular)
        .secondary(Materials.Spessartine)
        .inBetween(Materials.Pyrolusite)
        .sporadic(Materials.Tantalite)
        .localize(Materials.Manganese)),

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
        .sporadic(Materials.Coal)
        .localize(Materials.Diamond)),

    Olivine(new OreMixBuilder().name("ore.mix.olivine")
        .heightRange(10, 25)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Anubis, MehenBelt, BarnardE, Ceres, Haumea, MakeMake)
        .primary(Materials.Bentonite)
        .secondary(Materials.Magnesite)
        .inBetween(Materials.Olivine)
        .sporadic(Materials.Glauconite)
        .localize(Materials.Olivine)),

    Apatite(new OreMixBuilder().name("ore.mix.apatite")
        .heightRange(40, 60)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TcetiE)
        .enableInDim(OW, TWILIGHT_FOREST)
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
        .sporadic(Materials.Calcite)
        .localize(Materials.Lapis)),

    Beryllium(new OreMixBuilder().name("ore.mix.beryllium")
        .heightRange(5, 30)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(NETHER, THE_END)
        .enableInDim(EndAsteroids, Mars, BarnardF, CentauriBb, Ceres, Haumea, MakeMake, Pluto, Titan, Venus)
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
        .sporadic(Materials.Uranium)
        .localize(Materials.Uranium)),
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
        .heightRange(5, 20)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(Neper)
        .enableInDim(TWILIGHT_FOREST)
        .primary(Materials.InfusedWater)
        .secondary(Materials.InfusedFire)
        .inBetween(Materials.Amber)
        .sporadic(Materials.Cinnabar)
        .localize(Materials.InfusedWater, Materials.InfusedFire, Materials.Amber)),

    TerraAer(new OreMixBuilder().name("ore.mix.terraaer")
        .heightRange(5, 20)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(Neper)
        .enableInDim(TWILIGHT_FOREST)
        .primary(Materials.InfusedEarth)
        .secondary(Materials.InfusedAir)
        .inBetween(Materials.Amber)
        .sporadic(Materials.Cinnabar)
        .localize(Materials.InfusedEarth, Materials.InfusedAir)),

    PerditioOrdo(new OreMixBuilder().name("ore.mix.perditioordo")
        .heightRange(5, 20)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Neper)
        .primary(Materials.InfusedEntropy)
        .secondary(Materials.InfusedOrder)
        .inBetween(Materials.Amber)
        .sporadic(Materials.Cinnabar)
        .localize(Materials.InfusedEntropy, Materials.InfusedOrder)),

    CopperTin(new OreMixBuilder().name("ore.mix.coppertin")
        .heightRange(80, 200)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .primary(Materials.Chalcopyrite)
        .secondary(Materials.Vermiculite)
        .inBetween(Materials.Cassiterite)
        .sporadic(Materials.Alunite)
        .localize(Materials.Vermiculite)),

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
        .enableInDim(CentauriBb, DimensionDef.Europa)
        .primary(Materials.CassiteriteSand)
        .secondary(Materials.GarnetSand)
        .inBetween(Materials.Asbestos)
        .sporadic(Materials.Diatomite)
        .localize(Materials.Tin)),

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
        .sporadic(Materials.Pollucite)
        .localize(Materials.Mica)),

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
        .enableInDim(Asteroids, MehenBelt, Seth, Callisto, Ceres, Ganymede, Io, KuiperBelt, Mercury, Oberon, Pluto)
        .primary(Materials.Platinum)
        .secondary(Materials.Chrome)
        .inBetween(Materials.Cooperite)
        .sporadic(Materials.Palladium)
        .localize(Materials.Palladium)),

    IridiumMytryl(new OreMixBuilder().name("ore.mix.iridiummytryl")
        .heightRange(15, 40)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, Enceladus, Io, KuiperBelt, Mercury, Miranda, Titan, Triton, Venus)
        .primary(Materials.Nickel)
        .secondary(Materials.Iridium)
        .inBetween(Materials.Palladium)
        .sporadic(Materials.Mithril)
        .localize(Materials.Iridium)),

    Osmium(new OreMixBuilder().name("ore.mix.osmium")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, BarnardC, Enceladus, KuiperBelt, Miranda, Oberon, Pluto, Proteus, Titan)
        .primary(Materials.Nickel)
        .secondary(Materials.Osmium)
        .inBetween(Materials.Iridium)
        .sporadic(Materials.Nickel)
        .localize(Materials.Osmium)),

    SaltPeterElectrotine(new OreMixBuilder().name("ore.mix.saltpeterelectrotine")
        .heightRange(5, 45)
        .weight(40)
        .density(3)
        .size(16)
        .enableInDim(NETHER)
        .enableInDim(CentauriBb, Ceres)
        .primary(Materials.Saltpeter)
        .secondary(Materials.Diatomite)
        .inBetween(Materials.Electrotine)
        .sporadic(Materials.Alunite)
        .localize(Materials.Electrotine)),

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
        .enableInDim(Horus, BarnardC, CentauriBb, MakeMake, Pluto, TcetiE)
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
        .sporadic(Materials.Platinum)
        .localize(Materials.InfusedGold)),

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
        .sporadic(Materials.GarnetYellow)
        .localize(Materials.NetherStar)),

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
        .enableInDim(BarnardE, BarnardF, CentauriBb, VegaB)
        .primary(Materials.Cadmium)
        .secondary(Materials.Caesium)
        .inBetween(Materials.Lanthanum)
        .sporadic(Materials.Cerium)
        .localize(Materials.RareEarth)),

    RichNuclear(new OreMixBuilder().name("ore.mix.richnuclear")
        .heightRange(5, 40)
        .weight(5)
        .density(2)
        .size(8)
        .enableInDim(Callisto, Ceres, Ganymede, Io)
        .primary(Materials.Uranium)
        .secondary(Materials.Plutonium)
        .inBetween(Materials.Thorium)
        .sporadic(Materials.Thorium)
        .localize(Materials.Plutonium)),

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
        .enableInDim(Seth, BarnardC, CentauriBb)
        .primary(Materials.Samarium)
        .secondary(Materials.Neodymium)
        .inBetween(Materials.Tartarite)
        .sporadic(Materials.Tartarite)),

    QuartzSpace(new OreMixBuilder().name("ore.mix.quartzspace")
        .heightRange(40, 80)
        .weight(20)
        .density(3)
        .size(16)
        .enableInDim(Horus, Moon, Mars, CentauriBb, Io, Phobos, Proteus, TcetiE, Venus)
        .primary(Materials.Quartzite)
        .secondary(Materials.Barite)
        .inBetween(Materials.CertusQuartz)
        .sporadic(Materials.CertusQuartz)
        .localize(Materials.Quartz)),

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
        .heightRange(5, 20)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Anubis, Maahes)
        .primary(Materials.Galena)
        .secondary(Materials.Silver)
        .inBetween(Materials.Lead)
        .sporadic(Materials.Cryolite)
        .localize(Materials.Cryolite)),

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
        .sporadic(Materials.Bedrockium)
        .localize(Materials.InfinityCatalyst)),

    CosmicNeutronium(new OreMixBuilder().name("ore.mix.cosmicneutronium")
        .heightRange(5, 20)
        .weight(15)
        .density(2)
        .size(16)
        .enableInDim(Horus)
        .primary(Materials.Neutronium)
        .secondary(Materials.CosmicNeutronium)
        .inBetween(Materials.BlackPlutonium)
        .sporadic(Materials.Bedrockium)
        .localize(Materials.CosmicNeutronium)),

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
        .sporadic(Materials.Trinium)
        .localize(Materials.Naquadria)),

    AwakenedDraconium(new OreMixBuilder().name("ore.mix.awakeneddraconium")
        .heightRange(20, 40)
        .weight(20)
        .density(3)
        .size(16)
        .enableInDim(MehenBelt)
        .primary(Materials.Draconium)
        .secondary(Materials.Draconium)
        .inBetween(Materials.DraconiumAwakened)
        .sporadic(Materials.NetherStar)
        .localize(Materials.DraconiumAwakened)),

    Tengam(new OreMixBuilder().name("ore.mix.tengam")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(32)
        .enableInDim(Seth)
        .primary(Materials.TengamRaw)
        .secondary(Materials.TengamRaw)
        .inBetween(Materials.Electrotine)
        .sporadic(Materials.Samarium)),

    NitrogenIce(new OreMixBuilder().name("ore.mix.nitrogenice")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(16)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials.Nitrogen)
        .secondary(Materials.Ammonia)
        .inBetween(Materials.Hydrogen)
        .sporadic(Materials.Hydrogen)
        .stoneCategory(StoneCategory.Ice)),

    HydrocarbonIce(new OreMixBuilder().name("ore.mix.hydrocarbonice")
        .heightRange(30, 180)
        .weight(40)
        .density(2)
        .size(12)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials.Methane)
        .secondary(Materials.Hydrogen)
        .inBetween(Materials.Carbon)
        .sporadic(Materials.Carbon)
        .stoneCategory(StoneCategory.Ice)),

    CarbonIce(new OreMixBuilder().name("ore.mix.carbonice")
        .heightRange(30, 180)
        .weight(40)
        .density(2)
        .size(12)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials.CarbonDioxide)
        .secondary(Materials.Oxygen)
        .inBetween(Materials.Carbon)
        .sporadic(Materials.Carbon)
        .stoneCategory(StoneCategory.Ice)),

    HHOIce(new OreMixBuilder().name("ore.mix.hhoice")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(16)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials.Oxygen)
        .secondary(Materials.Hydrogen)
        .inBetween(Materials.Oxygen)
        .sporadic(Materials.Hydrogen)
        .stoneCategory(StoneCategory.Ice)),

    SulfurIce(new OreMixBuilder().name("ore.mix.sulfurice")
        .heightRange(30, 180)
        .weight(20)
        .density(2)
        .size(8)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials.SulfurDioxide)
        .secondary(Materials.Oxygen)
        .inBetween(Materials.Sulfur)
        .sporadic(Materials.Oxygen)
        .stoneCategory(StoneCategory.Ice)),

    GTPP0(new OreMixBuilder().name("ore.mix.gtpp0")
        .heightRange(20, 40)
        .weight(1)
        .density(1)
        .size(128)
        .enableInDim(EVERGLADES)
        .primary(Materials.Iron)
        .secondary(Materials.Iron)
        .inBetween(Materials.Iron)
        .sporadic(Materials.Iron)),

    GTPP1(new OreMixBuilder().name("ore.mix.gtpp1")
        .heightRange(0, 60)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.AGARDITE_CD)
        .secondary(MaterialsOres.AGARDITE_LA)
        .inBetween(MaterialsOres.DEMICHELEITE_BR)
        .sporadic(MaterialsOres.IRARSITE)),

    GTPP2(new OreMixBuilder().name("ore.mix.gtpp2")
        .heightRange(0, 60)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.AGARDITE_ND)
        .secondary(MaterialsOres.AGARDITE_Y)
        .inBetween(MaterialsOres.KASHINITE)
        .sporadic(MaterialsOres.CERITE)),

    GTPP3(new OreMixBuilder().name("ore.mix.gtpp3")
        .heightRange(0, 60)
        .weight(30)
        .density(3)
        .size(32)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.CERITE)
        .secondary(MaterialsOres.NICHROMITE)
        .inBetween(MaterialsOres.XENOTIME)
        .sporadic(MaterialsOres.HIBONITE)),

    GTPP4(new OreMixBuilder().name("ore.mix.gtpp4")
        .heightRange(0, 60)
        .weight(40)
        .density(3)
        .size(32)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.GEIKIELITE)
        .secondary(MaterialsOres.CRYOLITE)
        .inBetween(MaterialsOres.GADOLINITE_CE)
        .sporadic(MaterialsOres.AGARDITE_ND)),

    GTPP5(new OreMixBuilder().name("ore.mix.gtpp5")
        .heightRange(30, 128)
        .weight(20)
        .density(2)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.HIBONITE)
        .secondary(MaterialsOres.YTTRIALITE)
        .inBetween(MaterialsOres.ZIRCONILITE)
        .sporadic(MaterialsOres.CERITE)),

    GTPP6(new OreMixBuilder().name("ore.mix.gtpp6")
        .heightRange(0, 40)
        .weight(20)
        .density(2)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.XENOTIME)
        .secondary(MaterialsOres.ZIRKELITE)
        .inBetween(MaterialsOres.CROCROITE)
        .sporadic(MaterialsOres.IRARSITE)),

    GTPP7(new OreMixBuilder().name("ore.mix.gtpp7")
        .heightRange(40, 128)
        .weight(20)
        .density(2)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.HONEAITE)
        .secondary(MaterialsOres.MIESSIITE)
        .inBetween(MaterialsOres.SAMARSKITE_Y)
        .sporadic(MaterialsOres.SAMARSKITE_YB)),

    GTPP8(new OreMixBuilder().name("ore.mix.gtpp8")
        .heightRange(0, 40)
        .weight(20)
        .density(2)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.TITANITE)
        .secondary(MaterialsOres.ZIMBABWEITE)
        .inBetween(MaterialsOres.ZIRCON)
        .sporadic(MaterialsOres.FLORENCITE)),

    GTPP9(new OreMixBuilder().name("ore.mix.gtpp9")
        .heightRange(10, 30)
        .weight(20)
        .density(1)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.LANTHANITE_CE)
        .secondary(MaterialsFluorides.FLUORITE)
        .inBetween(MaterialsOres.LAFOSSAITE)
        .sporadic(MaterialsOres.FLORENCITE)),

    GTPP10(new OreMixBuilder().name("ore.mix.gtpp10")
        .heightRange(20, 50)
        .weight(20)
        .density(2)
        .size(32)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.GEIKIELITE)
        .secondary(MaterialsOres.YTTROCERITE)
        .inBetween(MaterialsOres.LANTHANITE_LA)
        .sporadic(MaterialsOres.RADIOBARITE)),

    GTPP11(new OreMixBuilder().name("ore.mix.gtpp11")
        .heightRange(30, 70)
        .weight(20)
        .density(1)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsFluorides.FLUORITE)
        .secondary(MaterialsOres.KASHINITE)
        .inBetween(MaterialsOres.ZIRCON)
        .sporadic(MaterialsOres.CRYOLITE)),

    GTPP12(new OreMixBuilder().name("ore.mix.gtpp12")
        .heightRange(40, 80)
        .weight(20)
        .density(3)
        .size(32)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.CERITE)
        .secondary(MaterialsOres.ALBURNITE)
        .inBetween(MaterialsOres.MIESSIITE)
        .sporadic(MaterialsOres.HIBONITE)),

    GTPP13(new OreMixBuilder().name("ore.mix.gtpp13")
        .heightRange(5, 15)
        .weight(5)
        .density(1)
        .size(16)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.CRYOLITE)
        .secondary(MaterialsOres.RADIOBARITE)
        .inBetween(MaterialsOres.HONEAITE)
        .sporadic(MaterialsOres.FLORENCITE)),

    GTPP14(new OreMixBuilder().name("ore.mix.gtpp14")
        .heightRange(10, 20)
        .weight(8)
        .density(2)
        .size(16)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.DEMICHELEITE_BR)
        .secondary(MaterialsOres.PERROUDITE)
        .inBetween(MaterialsOres.IRARSITE)
        .sporadic(MaterialsOres.RADIOBARITE)),

    GTPP15(new OreMixBuilder().name("ore.mix.gtpp15")
        .heightRange(5, 25)
        .weight(5)
        .density(3)
        .size(24)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.FLUORCAPHITE)
        .secondary(MaterialsOres.LAFOSSAITE)
        .inBetween(MaterialsOres.GADOLINITE_CE)
        .sporadic(MaterialsOres.GADOLINITE_Y)),

    GTPP16(new OreMixBuilder().name("ore.mix.gtpp16")
        .heightRange(0, 25)
        .weight(4)
        .density(2)
        .size(32)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.YTTROCERITE)
        .secondary(MaterialsOres.LEPERSONNITE)
        .inBetween(MaterialsOres.LAUTARITE)
        .sporadic(MaterialsFluorides.FLUORITE)),

    GTPP17(new OreMixBuilder().name("ore.mix.gtpp17")
        .heightRange(10, 35)
        .weight(4)
        .density(1)
        .size(32)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.FLORENCITE)
        .secondary(MaterialsOres.LAUTARITE)
        .inBetween(MaterialsOres.SAMARSKITE_YB)
        .sporadic(MaterialsOres.POLYCRASE)),

    GTPP18(new OreMixBuilder().name("ore.mix.gtpp18")
        .heightRange(15, 40)
        .weight(4)
        .density(1)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsOres.GADOLINITE_CE)
        .secondary(MaterialsOres.GADOLINITE_Y)
        .inBetween(MaterialsOres.AGARDITE_LA)
        .sporadic(MaterialsOres.AGARDITE_CD)),

    GTPP19(new OreMixBuilder().name("ore.mix.gtpp19")
        .heightRange(0, 20)
        .weight(4)
        .density(1)
        .size(16)
        .enableInDim(EVERGLADES)
        .primary(MaterialsElements.STANDALONE.RUNITE)
        .secondary(MaterialsElements.STANDALONE.RUNITE)
        .inBetween(MaterialsElements.STANDALONE.RUNITE)
        .sporadic(MaterialsElements.STANDALONE.RUNITE)),

    Thorianit(new OreMixBuilder().name("ore.mix.ross128.Thorianit")
        .heightRange(30, 60)
        .weight(17)
        .density(1)
        .size(16)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Thorianit)
        .secondary(Materials.Uraninite)
        .inBetween(Materials.Lepidolite)
        .sporadic(Materials.Spodumene)),

    RossCarbon(new OreMixBuilder().name("ore.mix.ross128.carbon")
        .heightRange(5, 25)
        .weight(5)
        .density(4)
        .size(12)
        .enableInDim(ROSS128B)
        .primary(Materials.Graphite)
        .secondary(Materials.Diamond)
        .inBetween(Materials.Coal)
        .sporadic(Materials.Graphite)),

    Bismuth(new OreMixBuilder().name("ore.mix.ross128.bismuth")
        .heightRange(5, 80)
        .weight(30)
        .density(1)
        .size(16)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Bismuthinit)
        .secondary(Materials.Stibnite)
        .inBetween(Materials.Bismuth)
        .sporadic(WerkstoffLoader.Bismutite)),

    TurmalinAlkali(new OreMixBuilder().name("ore.mix.ross128.TurmalinAlkali")
        .heightRange(5, 80)
        .weight(15)
        .density(4)
        .size(48)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Olenit)
        .secondary(WerkstoffLoader.FluorBuergerit)
        .inBetween(WerkstoffLoader.ChromoAluminoPovondrait)
        .sporadic(WerkstoffLoader.VanadioOxyDravit)),

    Roquesit(new OreMixBuilder().name("ore.mix.ross128.Roquesit")
        .heightRange(30, 50)
        .weight(3)
        .density(1)
        .size(12)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Arsenopyrite)
        .secondary(WerkstoffLoader.Ferberite)
        .inBetween(WerkstoffLoader.Loellingit)
        .sporadic(WerkstoffLoader.Roquesit)),

    RossTungstate(new OreMixBuilder().name("ore.mix.ross128.Tungstate")
        .heightRange(5, 40)
        .weight(10)
        .density(4)
        .size(14)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Ferberite)
        .secondary(WerkstoffLoader.Huebnerit)
        .inBetween(WerkstoffLoader.Loellingit)
        .sporadic(Materials.Scheelite)),

    CopperSulfits(new OreMixBuilder().name("ore.mix.ross128.CopperSulfits")
        .heightRange(40, 70)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Djurleit)
        .secondary(WerkstoffLoader.Bornite)
        .inBetween(WerkstoffLoader.Wittichenit)
        .sporadic(Materials.Tetrahedrite)),

    Forsterit(new OreMixBuilder().name("ore.mix.ross128.Forsterit")
        .heightRange(20, 90)
        .weight(50)
        .density(2)
        .size(32)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Forsterit)
        .secondary(WerkstoffLoader.Fayalit)
        .inBetween(WerkstoffLoader.DescloiziteCUVO4)
        .sporadic(WerkstoffLoader.DescloiziteZNVO4)),

    Hedenbergit(new OreMixBuilder().name("ore.mix.ross128.Hedenbergit")
        .heightRange(20, 90)
        .weight(50)
        .density(2)
        .size(32)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Hedenbergit)
        .secondary(WerkstoffLoader.Fayalit)
        .inBetween(WerkstoffLoader.DescloiziteCUVO4)
        .sporadic(WerkstoffLoader.DescloiziteZNVO4)),

    RedZircon(new OreMixBuilder().name("ore.mix.ross128.RedZircon")
        .heightRange(10, 80)
        .weight(40)
        .density(3)
        .size(24)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Fayalit)
        .secondary(WerkstoffLoader.FuchsitAL)
        .inBetween(WerkstoffLoader.RedZircon)
        .sporadic(WerkstoffLoader.FuchsitCR)),

    Tiberium(new OreMixBuilder().name("ore.mix.ross128ba.tib")
        .heightRange(30, 60)
        .weight(6)
        .density(1)
        .size(16)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.Tiberium)
        .secondary(WerkstoffLoader.Tiberium)
        .inBetween(Materials.NaquadahEnriched)
        .sporadic(Materials.NaquadahEnriched)),

    Ross128baTungstate(new OreMixBuilder().name("ore.mix.ross128ba.Tungstate")
        .heightRange(5, 40)
        .weight(60)
        .density(4)
        .size(14)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.Ferberite)
        .secondary(WerkstoffLoader.Huebnerit)
        .inBetween(WerkstoffLoader.Loellingit)
        .sporadic(Materials.Scheelite)),

    Bart(new OreMixBuilder().name("ore.mix.ross128ba.bart")
        .heightRange(30, 60)
        .weight(1)
        .density(1)
        .size(1)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.BArTiMaEuSNeK)
        .secondary(WerkstoffLoader.BArTiMaEuSNeK)
        .inBetween(WerkstoffLoader.BArTiMaEuSNeK)
        .sporadic(WerkstoffLoader.BArTiMaEuSNeK)),

    Ross128baTurmalinAlkali(new OreMixBuilder().name("ore.mix.ross128ba.TurmalinAlkali")
        .heightRange(5, 80)
        .weight(60)
        .density(4)
        .size(48)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.Olenit)
        .secondary(WerkstoffLoader.FluorBuergerit)
        .inBetween(WerkstoffLoader.ChromoAluminoPovondrait)
        .sporadic(WerkstoffLoader.VanadioOxyDravit)),

    Ross128baAmethyst(new OreMixBuilder().name("ore.mix.ross128ba.Amethyst")
        .heightRange(5, 80)
        .weight(35)
        .density(2)
        .size(8)
        .enableInDim(ROSS128BA)
        .primary(Materials.Amethyst)
        .secondary(Materials.Olivine)
        .inBetween(WerkstoffLoader.Prasiolite)
        .sporadic(WerkstoffLoader.Hedenbergit)),

    Ross128baCopperSulfits(new OreMixBuilder().name("ore.mix.ross128ba.CopperSulfits")
        .heightRange(40, 70)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.Djurleit)
        .secondary(WerkstoffLoader.Bornite)
        .inBetween(WerkstoffLoader.Wittichenit)
        .sporadic(Materials.Tetrahedrite)),

    Ross128baRedZircon(new OreMixBuilder().name("ore.mix.ross128ba.RedZircon")
        .heightRange(10, 80)
        .weight(40)
        .density(3)
        .size(24)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.Fayalit)
        .secondary(WerkstoffLoader.FuchsitAL)
        .inBetween(WerkstoffLoader.RedZircon)
        .sporadic(WerkstoffLoader.FuchsitCR)),

    Fluorspar(new OreMixBuilder().name("ore.mix.ross128ba.Fluorspar")
        .heightRange(10, 80)
        .weight(35)
        .density(4)
        .size(8)
        .enableInDim(ROSS128BA)
        .primary(Materials.Galena)
        .secondary(Materials.Sphalerite)
        .inBetween(WerkstoffLoader.Fluorspar)
        .sporadic(Materials.Barite));

    // spotless : on

    public final OreMixBuilder oreMixBuilder;

    OreMixes(OreMixBuilder oreMixBuilder) {
        this.oreMixBuilder = oreMixBuilder;
    }

    public WorldgenGTOreLayer addGTOreLayer() {
        return new WorldgenGTOreLayer(this.oreMixBuilder);
    }
}
