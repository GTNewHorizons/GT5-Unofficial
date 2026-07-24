package gregtech.api.enums;

import static galacticgreg.api.enums.DimensionDef.Anubis;
import static galacticgreg.api.enums.DimensionDef.Asteroids;
import static galacticgreg.api.enums.DimensionDef.BarnardC;
import static galacticgreg.api.enums.DimensionDef.BarnardE;
import static galacticgreg.api.enums.DimensionDef.BarnardF;
import static galacticgreg.api.enums.DimensionDef.Callisto;
import static galacticgreg.api.enums.DimensionDef.CentauriBb;
import static galacticgreg.api.enums.DimensionDef.Ceres;
import static galacticgreg.api.enums.DimensionDef.Deimos;
import static galacticgreg.api.enums.DimensionDef.DimNames.ASTEROIDS;
import static galacticgreg.api.enums.DimensionDef.DimNames.EVERGLADES;
import static galacticgreg.api.enums.DimensionDef.DimNames.KUIPERBELT;
import static galacticgreg.api.enums.DimensionDef.DimNames.NETHER;
import static galacticgreg.api.enums.DimensionDef.DimNames.OW;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128B;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128BA;
import static galacticgreg.api.enums.DimensionDef.DimNames.THE_END;
import static galacticgreg.api.enums.DimensionDef.DimNames.TWILIGHT_FOREST;
import static galacticgreg.api.enums.DimensionDef.Enceladus;
import static galacticgreg.api.enums.DimensionDef.EndAsteroids;
import static galacticgreg.api.enums.DimensionDef.Ganymede;
import static galacticgreg.api.enums.DimensionDef.Haumea;
import static galacticgreg.api.enums.DimensionDef.Horus;
import static galacticgreg.api.enums.DimensionDef.Io;
import static galacticgreg.api.enums.DimensionDef.KuiperBelt;
import static galacticgreg.api.enums.DimensionDef.Maahes;
import static galacticgreg.api.enums.DimensionDef.MakeMake;
import static galacticgreg.api.enums.DimensionDef.Mars;
import static galacticgreg.api.enums.DimensionDef.MehenBelt;
import static galacticgreg.api.enums.DimensionDef.Mercury;
import static galacticgreg.api.enums.DimensionDef.Miranda;
import static galacticgreg.api.enums.DimensionDef.Moon;
import static galacticgreg.api.enums.DimensionDef.Neper;
import static galacticgreg.api.enums.DimensionDef.Oberon;
import static galacticgreg.api.enums.DimensionDef.Phobos;
import static galacticgreg.api.enums.DimensionDef.Pluto;
import static galacticgreg.api.enums.DimensionDef.Proteus;
import static galacticgreg.api.enums.DimensionDef.Seth;
import static galacticgreg.api.enums.DimensionDef.TcetiE;
import static galacticgreg.api.enums.DimensionDef.Titan;
import static galacticgreg.api.enums.DimensionDef.Triton;
import static galacticgreg.api.enums.DimensionDef.TwilightForest;
import static galacticgreg.api.enums.DimensionDef.VegaB;
import static galacticgreg.api.enums.DimensionDef.Venus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import bartworks.system.material.WerkstoffLoader;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.OreMixBuilder;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.loaders.materials.RecognitionMaterials;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtneioreplugin.util.DimensionHelper;

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
        .primary(Materials2Materials.Naquadah)
        .secondary(Materials2Materials.Naquadah)
        .inBetween(Materials2Materials.Naquadah)
        .sporadic(Materials2Materials.NaquadahEnriched)),

    LigniteCoal(new OreMixBuilder().name("ore.mix.lignite")
        .heightRange(80, 210)
        .weight(160)
        .density(7)
        .size(32)
        .enableInDim(OW)
        .enableInDim(BarnardC)
        .primary(Materials2Materials.Lignite)
        .secondary(Materials2Materials.Lignite)
        .inBetween(Materials2Materials.Lignite)
        .sporadic(Materials2Materials.Coal)),

    Coal(new OreMixBuilder().name("ore.mix.coal")
        .heightRange(30, 80)
        .weight(80)
        .density(5)
        .size(32)
        .enableInDim(OW, TWILIGHT_FOREST)
        .heightRangeOverride(TwilightForest, 15, 40)
        .primary(Materials2Materials.Coal)
        .secondary(Materials2Materials.Coal)
        .inBetween(Materials2Materials.Coal)
        .sporadic(Materials2Materials.Lignite)),

    Magnetite(new OreMixBuilder().name("ore.mix.magnetite")
        .heightRange(60, 180)
        .weight(160)
        .density(2)
        .size(32)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(Seth, BarnardE, Ceres, Deimos, Io, MakeMake, TcetiE)
        .heightRangeOverride(TwilightForest, 20, 32)
        .primary(Materials2Materials.Magnetite)
        .secondary(Materials2Materials.Magnetite)
        .inBetween(Materials2Materials.Iron)
        .sporadic(Materials2Materials.VanadiumMagnetite)),

    Gold(new OreMixBuilder().name("ore.mix.gold")
        .heightRange(30, 60)
        .weight(160)
        .density(2)
        .size(32)
        .enableInDim(OW, THE_END, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Seth, Asteroids, Mars, BarnardF, Callisto, Phobos, Pluto, TcetiE, Triton, VegaB)
        .heightRangeOverride(TwilightForest, 15, 30)
        .primary(Materials2Materials.Magnetite)
        .secondary(Materials2Materials.Magnetite)
        .inBetween(Materials2Materials.VanadiumMagnetite)
        .sporadic(Materials2Materials.Gold)
        .setLocalizedName(Materials2Materials.Gold)),

    Iron(new OreMixBuilder().name("ore.mix.iron")
        .heightRange(10, 40)
        .weight(120)
        .density(3)
        .size(24)
        .enableInDim(OW, NETHER, TWILIGHT_FOREST)
        .enableInDim(Mars, Callisto, Ceres, Ganymede, Mercury, Oberon, Pluto)
        .heightRangeOverride(TwilightForest, 5, 20)
        .primary(Materials2Materials.BrownLimonite)
        .secondary(Materials2Materials.YellowLimonite)
        .inBetween(Materials2Materials.BandedIron)
        .sporadic(Materials2Materials.Malachite)
        .setLocalizedName(Materials2Materials.Iron)),

    Cassiterite(new OreMixBuilder().name("ore.mix.cassiterite")
        .heightRange(60, 220)
        .weight(50)
        .density(4)
        .size(24)
        .enableInDim(EndAsteroids, MehenBelt, Seth, Moon, Io, Miranda, TcetiE, Venus)
        .enableInDim(OW, THE_END, TWILIGHT_FOREST)
        .heightRangeOverride(TwilightForest, 20, 32)
        .primary(Materials2Materials.Tin)
        .secondary(Materials2Materials.Tin)
        .inBetween(Materials2Materials.Cassiterite)
        .sporadic(Materials2Materials.Tin)
        .setLocalizedName(Materials2Materials.Cassiterite)),

    Tetrahedrite(new OreMixBuilder().name("ore.mix.tetrahedrite")
        .heightRange(80, 120)
        .weight(70)
        .density(3)
        .size(24)
        .enableInDim(NETHER, THE_END)
        .enableInDim(EndAsteroids, Asteroids, Mars, CentauriBb, Deimos, Ganymede, KuiperBelt, Miranda, VegaB, Venus)
        .primary(Materials2Materials.Tetrahedrite)
        .secondary(Materials2Materials.Tetrahedrite)
        .inBetween(Materials2Materials.Copper)
        .sporadic(Materials2Materials.Stibnite)),

    NetherQuartz(new OreMixBuilder().name("ore.mix.netherquartz")
        .heightRange(40, 80)
        .weight(80)
        .density(4)
        .size(24)
        .enableInDim(NETHER)
        .enableInDim(Neper, CentauriBb)
        .primary(Materials2Materials.NetherQuartz)
        .secondary(Materials2Materials.NetherQuartz)
        .inBetween(Materials2Materials.NetherQuartz)
        .sporadic(Materials2Materials.Quartzite)),

    Sulfur(new OreMixBuilder().name("ore.mix.sulfur")
        .heightRange(5, 20)
        .weight(100)
        .density(4)
        .size(24)
        .enableInDim(NETHER)
        .enableInDim(Anubis, Mars, CentauriBb, Deimos, Io, Phobos, Venus)
        .primary(Materials2Materials.Sulfur)
        .secondary(Materials2Materials.Sulfur)
        .inBetween(Materials2Materials.Pyrite)
        .sporadic(Materials2Materials.Sphalerite)),

    Copper(new OreMixBuilder().name("ore.mix.copper")
        .heightRange(5, 60)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(EndAsteroids, Moon, BarnardE, BarnardF, Callisto, Ceres, Enceladus, Proteus)
        .enableInDim(OW, NETHER, THE_END)
        .primary(Materials2Materials.Chalcopyrite)
        .secondary(Materials2Materials.Iron)
        .inBetween(Materials2Materials.Pyrite)
        .sporadic(Materials2Materials.Copper)
        .setLocalizedName(Materials2Materials.Copper)),

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
        .primary(Materials2Materials.Bauxite)
        .secondary(Materials2Materials.Ilmenite)
        .inBetween(Materials2Materials.Aluminium)
        .sporadic(Materials2Materials.Ilmenite)),

    Salts(new OreMixBuilder().name("ore.mix.salts")
        .heightRange(50, 70)
        .weight(50)
        .density(2)
        .size(24)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(Mars, BarnardC, TcetiE)
        .heightRangeOverride(TwilightForest, 25, 45)
        .primary(Materials2Materials.RockSalt)
        .secondary(Materials2Materials.Salt)
        .inBetween(Materials2Materials.Lepidolite)
        .sporadic(Materials2Materials.Spodumene)
        .setLocalizedName(Materials2Materials.Salt)),

    Redstone(new OreMixBuilder().name("ore.mix.redstone")
        .heightRange(5, 40)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(OW, NETHER)
        .enableInDim(Mars, BarnardF, CentauriBb, Ganymede, Mercury, Miranda, VegaB, Venus)
        .primary(Materials2Materials.Redstone)
        .secondary(Materials2Materials.Redstone)
        .inBetween(Materials2Materials.Ruby)
        .sporadic(Materials2Materials.Cinnabar)),

    Soapstone(new OreMixBuilder().name("ore.mix.soapstone")
        .heightRange(20, 50)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(Anubis, Maahes, Ceres)
        .heightRangeOverride(TwilightForest, 10, 25)
        .primary(Materials2Materials.Soapstone)
        .secondary(Materials2Materials.Talc)
        .inBetween(Materials2Materials.Glauconite)
        .sporadic(Materials2Materials.Pentlandite)),

    Nickel(new OreMixBuilder().name("ore.mix.nickel")
        .heightRange(10, 40)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(THE_END, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, MehenBelt, Mars, Deimos, KuiperBelt, Phobos, Titan, Triton, Venus)
        .heightRangeOverride(TwilightForest, 5, 20)
        .primary(Materials2Materials.Garnierite)
        .secondary(Materials2Materials.Nickel)
        .inBetween(Materials2Materials.Cobaltite)
        .sporadic(Materials2Materials.Pentlandite)
        .setLocalizedName(Materials2Materials.Nickel)),

    Platinum(new OreMixBuilder().name("ore.mix.platinum")
        .heightRange(40, 50)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(EndAsteroids, Asteroids, Maahes, MehenBelt)
        .primary(Materials2Materials.Cooperite)
        .secondary(Materials2Materials.Palladium)
        .inBetween(Materials2Materials.Platinum)
        .sporadic(Materials2Materials.Iridium)
        .setLocalizedName(Materials2Materials.Platinum)),

    Pitchblende(new OreMixBuilder().name("ore.mix.pitchblende")
        .heightRange(30, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Mars, BarnardF, CentauriBb, Haumea, Io, KuiperBelt, MakeMake, Oberon, Phobos, VegaB, Venus)
        .primary(Materials2Materials.Pitchblende)
        .secondary(Materials2Materials.Pitchblende)
        .inBetween(Materials2Materials.Uraninite)
        .sporadic(Materials2Materials.Uraninite)),

    Monazite(new OreMixBuilder().name("ore.mix.monazite")
        .heightRange(20, 40)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(Seth, Moon, BarnardF, Callisto, Deimos, Enceladus, Haumea, Io, MakeMake, Titan, Triton, Venus)
        .primary(Materials2Materials.Bastnasite)
        .secondary(Materials2Materials.Bastnasite)
        .inBetween(Materials2Materials.Monazite)
        .sporadic(Materials2Materials.Neodymium)
        .setLocalizedName(Materials2Materials.Monazite)),

    Molybdenum(new OreMixBuilder().name("ore.mix.molybdenum")
        .heightRange(20, 50)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(NETHER, THE_END, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Moon, BarnardE, Ceres, Mercury, Phobos, Pluto, Proteus, Titan)
        .heightRangeOverride(TwilightForest, 10, 25)
        .primary(Materials2Materials.Wulfenite)
        .secondary(Materials2Materials.Molybdenite)
        .inBetween(Materials2Materials.Molybdenum)
        .sporadic(Materials2Materials.Powellite)
        .setLocalizedName(Materials2Materials.Molybdenum)),

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
        .primary(Materials2Materials.Scheelite)
        .secondary(Materials2Materials.Scheelite)
        .inBetween(Materials2Materials.Tungstate)
        .sporadic(Materials2Materials.Lithium)
        .setLocalizedName(Materials2Materials.Tungstate)),

    Sapphire(new OreMixBuilder().name("ore.mix.sapphire")
        .heightRange(10, 25)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Horus)
        .primary(Materials2Materials.Almandine)
        .secondary(Materials2Materials.Pyrope)
        .inBetween(Materials2Materials.Sapphire)
        .sporadic(Materials2Materials.GreenSapphire)
        .setLocalizedName(Materials2Materials.Sapphire)),

    Manganese(new OreMixBuilder().name("ore.mix.manganese")
        .heightRange(20, 30)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(OW, NETHER)
        .enableInDim(EndAsteroids, BarnardE, BarnardF, CentauriBb, Ceres, Io, Oberon, Titan, Triton)
        .primary(Materials2Materials.Grossular)
        .secondary(Materials2Materials.Spessartine)
        .inBetween(Materials2Materials.Pyrolusite)
        .sporadic(Materials2Materials.Tantalite)
        .setLocalizedName(Materials2Materials.Manganese)),

    Quartz(new OreMixBuilder().name("ore.mix.quartz")
        .heightRange(80, 120)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(NETHER)
        .enableInDim(Neper)
        .primary(Materials2Materials.Quartzite)
        .secondary(Materials2Materials.Barite)
        .inBetween(Materials2Materials.CertusQuartz)
        .sporadic(Materials2Materials.CertusQuartz)),

    Diamond(new OreMixBuilder().name("ore.mix.diamond")
        .heightRange(5, 20)
        .weight(40)
        .density(1)
        .size(16)
        .enableInDim(BarnardF, Ganymede, KuiperBelt, Mercury, Miranda, Phobos, Pluto, Proteus, Titan)
        .enableInDim(OW, TWILIGHT_FOREST)
        .primary(Materials2Materials.Graphite)
        .secondary(Materials2Materials.Graphite)
        .inBetween(Materials2Materials.Diamond)
        .sporadic(Materials2Materials.Coal)
        .setLocalizedName(Materials2Materials.Diamond)),

    Olivine(new OreMixBuilder().name("ore.mix.olivine")
        .heightRange(10, 25)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, Anubis, MehenBelt, BarnardE, Ceres, Haumea, MakeMake)
        .primary(Materials2Materials.Bentonite)
        .secondary(Materials2Materials.Magnesite)
        .inBetween(Materials2Materials.Olivine)
        .sporadic(Materials2Materials.Glauconite)
        .setLocalizedName(Materials2Materials.Olivine)),

    Apatite(new OreMixBuilder().name("ore.mix.apatite")
        .heightRange(40, 60)
        .weight(60)
        .density(2)
        .size(16)
        .enableInDim(TcetiE)
        .enableInDim(OW, TWILIGHT_FOREST)
        .heightRangeOverride(TwilightForest, 20, 30)
        .primary(Materials2Materials.Apatite)
        .secondary(Materials2Materials.Apatite)
        .inBetween(Materials2Materials.TricalciumPhosphate)
        .sporadic(Materials2Materials.Pyrochlore)),

    Galena(new OreMixBuilder().name("ore.mix.galena")
        .heightRange(5, 45)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(Moon, Mars, Ganymede, Oberon, Triton, VegaB, Venus)
        .primary(Materials2Materials.Galena)
        .secondary(Materials2Materials.Galena)
        .inBetween(Materials2Materials.Silver)
        .sporadic(Materials2Materials.Lead)),

    Lapis(new OreMixBuilder().name("ore.mix.lapis")
        .heightRange(20, 50)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(OW, TWILIGHT_FOREST)
        .enableInDim(EndAsteroids, MehenBelt, Ceres, Deimos, Enceladus, VegaB)
        .heightRangeOverride(TwilightForest, 10, 25)
        .primary(Materials2Materials.Lazurite)
        .secondary(Materials2Materials.Sodalite)
        .inBetween(Materials2Materials.Lapis)
        .sporadic(Materials2Materials.Calcite)
        .setLocalizedName(Materials2Materials.Lapis)),

    Beryllium(new OreMixBuilder().name("ore.mix.beryllium")
        .heightRange(5, 30)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(NETHER, THE_END)
        .enableInDim(EndAsteroids, Mars, BarnardF, CentauriBb, Ceres, Haumea, MakeMake, Pluto, Titan, Venus)
        .primary(Materials2Materials.Beryllium)
        .secondary(Materials2Materials.Beryllium)
        .inBetween(Materials2Materials.Emerald)
        .sporadic(Materials2Materials.Thorium)),

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
        .primary(Materials2Materials.Uraninite)
        .secondary(Materials2Materials.Uraninite)
        .inBetween(Materials2Materials.Uranium)
        .sporadic(Materials2Materials.Uranium)
        .setLocalizedName(Materials2Materials.Uranium)),
    OilSand(new OreMixBuilder().name("ore.mix.oilsand")
        .heightRange(50, 80)
        .weight(40)
        .density(5)
        .size(16)
        .enableInDim(OW)
        .enableInDim(BarnardC, TcetiE)
        .primary(Materials2Materials.Oilsands)
        .secondary(Materials2Materials.Oilsands)
        .inBetween(Materials2Materials.Oilsands)
        .sporadic(Materials2Materials.Oilsands)),

    Neutronium(new OreMixBuilder().name("ore.mix.neutronium")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(Maahes, MehenBelt, BarnardE, BarnardF, Haumea, KuiperBelt, MakeMake, Pluto, Proteus, Triton, VegaB)
        .primary(Materials2Materials.Neutronium)
        .secondary(Materials2Materials.Adamantium)
        .inBetween(Materials2Materials.Naquadah)
        .sporadic(Materials2Materials.Titanium)),

    AquaIgnis(new OreMixBuilder().name("ore.mix.aquaignis")
        .heightRange(5, 20)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(Neper)
        .enableInDim(TWILIGHT_FOREST)
        .primary(Materials2Materials.InfusedWater)
        .secondary(Materials2Materials.InfusedFire)
        .inBetween(Materials2Materials.Amber)
        .sporadic(Materials2Materials.Cinnabar)
        .setLocalizedName(Materials2Materials.InfusedFire, Materials2Materials.Amber)),

    TerraAer(new OreMixBuilder().name("ore.mix.terraaer")
        .heightRange(5, 20)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(Neper)
        .enableInDim(TWILIGHT_FOREST)
        .primary(Materials2Materials.InfusedEarth)
        .secondary(Materials2Materials.InfusedAir)
        .inBetween(Materials2Materials.Amber)
        .sporadic(Materials2Materials.Cinnabar)
        .setLocalizedName(Materials2Materials.InfusedAir)),

    PerditioOrdo(new OreMixBuilder().name("ore.mix.perditioordo")
        .heightRange(5, 20)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Neper)
        .primary(Materials2Materials.InfusedEntropy)
        .secondary(Materials2Materials.InfusedOrder)
        .inBetween(Materials2Materials.Amber)
        .sporadic(Materials2Materials.Cinnabar)
        .setLocalizedName(Materials2Materials.InfusedOrder)),

    CopperTin(new OreMixBuilder().name("ore.mix.coppertin")
        .heightRange(80, 200)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .primary(Materials2Materials.Chalcopyrite)
        .secondary(Materials2Materials.Vermiculite)
        .inBetween(Materials2Materials.Cassiterite)
        .sporadic(Materials2Materials.Alunite)
        .setLocalizedName(Materials2Materials.Vermiculite)),

    TitaniumChrome(new OreMixBuilder().name("ore.mix.titaniumchrome")
        .heightRange(10, 70)
        .weight(16)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Moon, Asteroids, Callisto, Ganymede, Mercury, Miranda, Pluto, Proteus, TcetiE, Titan)
        .primary(Materials2Materials.Ilmenite)
        .secondary(Materials2Materials.Chromite)
        .inBetween(Materials2Materials.Uvarovite)
        .sporadic(Materials2Materials.Perlite)),

    MineralSand(new OreMixBuilder().name("ore.mix.mineralsand")
        .heightRange(50, 60)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .enableInDim(Anubis, Maahes, BarnardC, DimensionDef.Europa)
        .primary(Materials2Materials.BasalticMineralSand)
        .secondary(Materials2Materials.GraniticMineralSand)
        .inBetween(Materials2Materials.FullersEarth)
        .sporadic(Materials2Materials.Gypsum)),

    GarnetTin(new OreMixBuilder().name("ore.mix.garnettin")
        .heightRange(50, 60)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(OW)
        .enableInDim(CentauriBb, DimensionDef.Europa)
        .primary(Materials2Materials.CassiteriteSand)
        .secondary(Materials2Materials.GarnetSand)
        .inBetween(Materials2Materials.Asbestos)
        .sporadic(Materials2Materials.Diatomite)
        .setLocalizedName(Materials2Materials.Tin)),

    KaoliniteZeolite(new OreMixBuilder().name("ore.mix.kaolinitezeolite")
        .heightRange(50, 70)
        .weight(60)
        .density(4)
        .size(16)
        .enableInDim(OW)
        .enableInDim(Neper, TcetiE)
        .primary(Materials2Materials.Kaolinite)
        .secondary(Materials2Materials.Zeolite)
        .inBetween(Materials2Materials.FullersEarth)
        .sporadic(Materials2Materials.GlauconiteSand)),

    Mica(new OreMixBuilder().name("ore.mix.mica")
        .heightRange(20, 40)
        .weight(20)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Neper, BarnardC)
        .enableInDim(OW)
        .primary(Materials2Materials.Kyanite)
        .secondary(Materials2Materials.Mica)
        .inBetween(Materials2Materials.Cassiterite)
        .sporadic(Materials2Materials.Pollucite)
        .setLocalizedName(Materials2Materials.Mica)),

    Dolomite(new OreMixBuilder().name("ore.mix.dolomite")
        .heightRange(150, 200)
        .weight(40)
        .density(4)
        .size(24)
        .enableInDim(OW)
        .enableInDim(Anubis, Neper)
        .primary(Materials2Materials.Dolomite)
        .secondary(Materials2Materials.Wollastonite)
        .inBetween(Materials2Materials.Trona)
        .sporadic(Materials2Materials.Andradite)),

    PlatinumChrome(new OreMixBuilder().name("ore.mix.platinumchrome")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(Asteroids, MehenBelt, Seth, Callisto, Ceres, Ganymede, Io, KuiperBelt, Mercury, Oberon, Pluto)
        .primary(Materials2Materials.Platinum)
        .secondary(Materials2Materials.Chrome)
        .inBetween(Materials2Materials.Cooperite)
        .sporadic(Materials2Materials.Palladium)
        .setLocalizedName(Materials2Materials.Palladium)),

    IridiumMytryl(new OreMixBuilder().name("ore.mix.iridiummytryl")
        .heightRange(15, 40)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, Enceladus, Io, KuiperBelt, Mercury, Miranda, Titan, Triton, Venus)
        .primary(Materials2Materials.Nickel)
        .secondary(Materials2Materials.Iridium)
        .inBetween(Materials2Materials.Palladium)
        .sporadic(Materials2Materials.Mithril)
        .setLocalizedName(Materials2Materials.Iridium)),

    Osmium(new OreMixBuilder().name("ore.mix.osmium")
        .heightRange(5, 30)
        .weight(10)
        .density(2)
        .size(16)
        .enableInDim(MehenBelt, Seth, BarnardC, Enceladus, KuiperBelt, Miranda, Oberon, Pluto, Proteus, Titan)
        .primary(Materials2Materials.Nickel)
        .secondary(Materials2Materials.Osmium)
        .inBetween(Materials2Materials.Iridium)
        .sporadic(Materials2Materials.Nickel)
        .setLocalizedName(Materials2Materials.Osmium)),

    SaltPeterElectrotine(new OreMixBuilder().name("ore.mix.saltpeterelectrotine")
        .heightRange(5, 45)
        .weight(40)
        .density(3)
        .size(16)
        .enableInDim(NETHER)
        .enableInDim(CentauriBb, Ceres)
        .primary(Materials2Materials.Saltpeter)
        .secondary(Materials2Materials.Diatomite)
        .inBetween(Materials2Materials.Electrotine)
        .sporadic(Materials2Materials.Alunite)
        .setLocalizedName(Materials2Materials.Electrotine)),

    Desh(new OreMixBuilder().name("ore.mix.desh")
        .heightRange(5, 40)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Mars, Miranda)
        .primary(Materials2Materials.Desh)
        .secondary(Materials2Materials.Desh)
        .inBetween(Materials2Materials.Scheelite)
        .sporadic(Materials2Materials.Tungstate)),

    Draconium(new OreMixBuilder().name("ore.mix.draconium")
        .heightRange(20, 40)
        .weight(40)
        .density(1)
        .size(16)
        .enableInDim(Horus, Seth, Deimos, Mercury, Miranda, Phobos)
        .primary(Materials2Materials.Draconium)
        .secondary(Materials2Materials.Electrotine)
        .inBetween(Materials2Materials.Jade)
        .sporadic(Materials2Materials.Vinteum)),

    Quantium(new OreMixBuilder().name("ore.mix.quantium")
        .heightRange(5, 25)
        .weight(30)
        .density(3)
        .size(24)
        .enableInDim(Horus, Maahes, Venus)
        .primary(Materials2Materials.Quantium)
        .secondary(Materials2Materials.Amethyst)
        .inBetween(Materials2Materials.Rutile)
        .sporadic(Materials2Materials.Ardite)),

    CallistoIce(new OreMixBuilder().name("ore.mix.callistoice")
        .heightRange(40, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Maahes, Callisto)
        .primary(Materials2Materials.CallistoIce)
        .secondary(Materials2Materials.Topaz)
        .inBetween(Materials2Materials.BlueTopaz)
        .sporadic(Materials2Materials.Alduorite)),

    Mytryl(new OreMixBuilder().name("ore.mix.mytryl")
        .heightRange(10, 30)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Anubis, Horus, Io)
        .primary(Materials2Materials.Mytryl)
        .secondary(Materials2Materials.Jasper)
        .inBetween(Materials2Materials.Ceruclase)
        .sporadic(Materials2Materials.Vulcanite)),

    Ledox(new OreMixBuilder().name("ore.mix.ledox")
        .heightRange(55, 65)
        .weight(30)
        .density(2)
        .size(24)
        .enableInDim(Horus, Enceladus, DimensionDef.Europa)
        .primary(Materials2Materials.Ledox)
        .secondary(Materials2Materials.Opal)
        .inBetween(Materials2Materials.Orichalcum)
        .sporadic(Materials2Materials.Rubracium)),

    Oriharukon(new OreMixBuilder().name("ore.mix.oriharukon")
        .heightRange(30, 60)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Horus, Deimos, Phobos)
        .primary(Materials2Materials.Oriharukon)
        .secondary(Materials2Materials.Tanzanite)
        .inBetween(Materials2Materials.Vyroxeres)
        .sporadic(Materials2Materials.Mirabilite)),

    BlackPlutonium(new OreMixBuilder().name("ore.mix.blackplutonium")
        .heightRange(5, 25)
        .weight(40)
        .density(2)
        .size(24)
        .enableInDim(Horus, BarnardC, CentauriBb, MakeMake, Pluto, TcetiE)
        .primary(Materials2Materials.BlackPlutonium)
        .secondary(Materials2Materials.GarnetRed)
        .inBetween(Materials2Materials.GarnetYellow)
        .sporadic(Materials2Materials.Borax)),

    InfusedGold(new OreMixBuilder().name("ore.mix.infusedgold")
        .heightRange(15, 40)
        .weight(30)
        .density(2)
        .size(16)
        .enableInDim(BarnardE, Haumea, Io, Proteus, Titan, VegaB)
        .primary(Materials2Materials.Gold)
        .secondary(Materials2Materials.Gold)
        .inBetween(Materials2Materials.InfusedGold)
        .sporadic(Materials2Materials.Platinum)
        .setLocalizedName(Materials2Materials.InfusedGold)),

    Niobium(new OreMixBuilder().name("ore.mix.niobium")
        .heightRange(5, 30)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(BarnardF, MakeMake, Triton, VegaB)
        .primary(Materials2Materials.Niobium)
        .secondary(Materials2Materials.Yttrium)
        .inBetween(Materials2Materials.Gallium)
        .sporadic(Materials2Materials.Gallium)),

    TungstenIrons(new OreMixBuilder().name("ore.mix.tungstenirons")
        .heightRange(5, 25)
        .weight(16)
        .density(2)
        .size(30)
        .enableInDim(Neper, BarnardC, BarnardE, BarnardF, Oberon, Pluto, Proteus, Triton)
        .primary(Materials2Materials.Tungsten)
        .secondary(Materials2Materials.Silicon)
        .inBetween(Materials2Materials.DeepIron)
        .sporadic(Materials2Materials.ShadowIron)),

    UraniumGTNH(new OreMixBuilder().name("ore.mix.uraniumgtnh")
        .heightRange(10, 30)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(BarnardE, BarnardF, Haumea, Pluto, Triton, VegaB)
        .primary(Materials2Materials.Thorium)
        .secondary(Materials2Materials.Uranium)
        .inBetween(Materials2Materials.Plutonium241)
        .sporadic(Materials2Materials.Uranium235)),

    VanadiumGold(new OreMixBuilder().name("ore.mix.vanadiumgold")
        .heightRange(10, 50)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(Maahes, BarnardF, Enceladus, MakeMake, Proteus, VegaB)
        .primary(Materials2Materials.Vanadium)
        .secondary(Materials2Materials.Magnetite)
        .inBetween(Materials2Materials.Gold)
        .sporadic(Materials2Materials.Chrome)),

    NetherStar(new OreMixBuilder().name("ore.mix.netherstar")
        .heightRange(20, 60)
        .weight(60)
        .density(2)
        .size(24)
        .enableInDim(Horus, BarnardE, Haumea, TcetiE, VegaB)
        .primary(Materials2Materials.GarnetSand)
        .secondary(Materials2Materials.NetherStar)
        .inBetween(Materials2Materials.GarnetRed)
        .sporadic(Materials2Materials.GarnetYellow)
        .setLocalizedName(Materials2Materials.NetherStar)),

    Garnet(new OreMixBuilder().name("ore.mix.garnet")
        .heightRange(10, 30)
        .weight(40)
        .density(2)
        .size(16)
        .enableInDim(Horus, BarnardF, MakeMake, VegaB)
        .primary(Materials2Materials.GarnetRed)
        .secondary(Materials2Materials.GarnetYellow)
        .inBetween(Materials2Materials.Chrysotile)
        .sporadic(Materials2Materials.Realgar)),

    RareEarth(new OreMixBuilder().name("ore.mix.rareearth")
        .heightRange(30, 60)
        .weight(40)
        .density(2)
        .size(24)
        .enableInDim(BarnardE, BarnardF, CentauriBb, VegaB)
        .primary(Materials2Materials.Cadmium)
        .secondary(Materials2Materials.Caesium)
        .inBetween(Materials2Materials.Lanthanum)
        .sporadic(Materials2Materials.Cerium)
        .setLocalizedName(Materials2Materials.RareEarth)),

    RichNuclear(new OreMixBuilder().name("ore.mix.richnuclear")
        .heightRange(5, 40)
        .weight(5)
        .density(2)
        .size(8)
        .enableInDim(Callisto, Ceres, Ganymede, Io)
        .primary(Materials2Materials.Uranium)
        .secondary(Materials2Materials.Plutonium)
        .inBetween(Materials2Materials.Thorium)
        .sporadic(Materials2Materials.Thorium)
        .setLocalizedName(Materials2Materials.Plutonium)),

    HeavyPentele(new OreMixBuilder().name("ore.mix.heavypentele")
        .heightRange(40, 60)
        .weight(60)
        .density(5)
        .size(32)
        .enableInDim(Neper, Mars, BarnardC, Mercury, Phobos, Titan, VegaB)
        .primary(Materials2Materials.Arsenic)
        .secondary(Materials2Materials.Bismuth)
        .inBetween(Materials2Materials.Antimony)
        .sporadic(Materials2Materials.Antimony)),

    Europa(new OreMixBuilder().name("ore.mix.europa")
        .heightRange(55, 65)
        .weight(110)
        .density(4)
        .size(24)
        .enableInDim(Horus, DimensionDef.Europa, TcetiE)
        .primary(Materials2Materials.Magnesite)
        .secondary(Materials2Materials.BandedIron)
        .inBetween(Materials2Materials.Sulfur)
        .sporadic(Materials2Materials.Opal)),

    EuropaCore(new OreMixBuilder().name("ore.mix.europacore")
        .heightRange(5, 15)
        .weight(5)
        .density(2)
        .size(16)
        .enableInDim(Maahes, DimensionDef.Europa, TcetiE)
        .primary(Materials2Materials.Chrome)
        .secondary(Materials2Materials.Tungstate)
        .inBetween(Materials2Materials.Molybdenum)
        .sporadic(Materials2Materials.Manganese)),

    SecondLanthanid(new OreMixBuilder().name("ore.mix.secondlanthanid")
        .heightRange(10, 40)
        .weight(10)
        .density(3)
        .size(24)
        .enableInDim(Seth, BarnardC, CentauriBb)
        .primary(Materials2Materials.Samarium)
        .secondary(Materials2Materials.Neodymium)
        .inBetween(Materials2Materials.Tartarite)
        .sporadic(Materials2Materials.Tartarite)),

    QuartzSpace(new OreMixBuilder().name("ore.mix.quartzspace")
        .heightRange(40, 80)
        .weight(20)
        .density(3)
        .size(16)
        .enableInDim(Horus, Moon, Mars, CentauriBb, Io, Phobos, Proteus, TcetiE, Venus)
        .primary(Materials2Materials.Quartzite)
        .secondary(Materials2Materials.Barite)
        .inBetween(Materials2Materials.CertusQuartz)
        .sporadic(Materials2Materials.CertusQuartz)
        .setLocalizedName(RecognitionMaterials.Quartz)),

    Rutile(new OreMixBuilder().name("ore.mix.rutile")
        .heightRange(5, 20)
        .weight(8)
        .density(4)
        .size(12)
        .enableInDim(Anubis, Titan, Venus)
        .primary(Materials2Materials.Rutile)
        .secondary(Materials2Materials.Titanium)
        .inBetween(Materials2Materials.Bauxite)
        .sporadic(Materials2Materials.MeteoricIron)),

    TFGalena(new OreMixBuilder().name("ore.mix.tfgalena")
        .heightRange(5, 20)
        .weight(40)
        .density(4)
        .size(16)
        .enableInDim(TWILIGHT_FOREST)
        .enableInDim(Anubis, Maahes)
        .heightRangeOverride(TwilightForest, 5, 30)
        .primary(Materials2Materials.Galena)
        .secondary(Materials2Materials.Silver)
        .inBetween(Materials2Materials.Lead)
        .sporadic(Materials2Materials.Cryolite)
        .setLocalizedName(Materials2Materials.Cryolite)),

    LuVTantalite(new OreMixBuilder().name("ore.mix.luvtantalite")
        .heightRange(20, 30)
        .weight(10)
        .density(4)
        .size(16)
        .enableInDim(Io, Miranda)
        .primary(Materials2Materials.Pyrolusite)
        .secondary(Materials2Materials.Apatite)
        .inBetween(Materials2Materials.Tantalite)
        .sporadic(Materials2Materials.Pyrochlore)),

    CertusQuartz(new OreMixBuilder().name("ore.mix.certusquartz")
        .heightRange(40, 80)
        .weight(60)
        .density(5)
        .size(32)
        .enableInDim(Horus, Neper)
        .primary(Materials2Materials.CertusQuartz)
        .secondary(Materials2Materials.CertusQuartz)
        .inBetween(Materials2Materials.ChargedCertusQuartz)
        .sporadic(Materials2Materials.QuartzSand)),

    InfinityCatalyst(new OreMixBuilder().name("ore.mix.infinitycatalyst")
        .heightRange(5, 20)
        .weight(15)
        .density(2)
        .size(16)
        .enableInDim(Anubis)
        .primary(Materials2Materials.Neutronium)
        .secondary(Materials2Materials.Adamantium)
        .inBetween(Materials2Materials.InfinityCatalyst)
        .sporadic(Materials2Materials.Bedrockium)
        .setLocalizedName(Materials2Materials.InfinityCatalyst)),

    CosmicNeutronium(new OreMixBuilder().name("ore.mix.cosmicneutronium")
        .heightRange(5, 20)
        .weight(15)
        .density(2)
        .size(16)
        .enableInDim(Horus)
        .primary(Materials2Materials.Neutronium)
        .secondary(Materials2Materials.CosmicNeutronium)
        .inBetween(Materials2Materials.BlackPlutonium)
        .sporadic(Materials2Materials.Bedrockium)
        .setLocalizedName(Materials2Materials.CosmicNeutronium)),

    Dilithium(new OreMixBuilder().name("ore.mix.dilithium")
        .heightRange(30, 100)
        .weight(30)
        .density(3)
        .size(24)
        .enableInDim(Neper)
        .primary(Materials2Materials.Dilithium)
        .secondary(Materials2Materials.Dilithium)
        .inBetween(Materials2Materials.MysteriousCrystal)
        .sporadic(Materials2Materials.Vinteum)),

    Naquadria(new OreMixBuilder().name("ore.mix.naquadria")
        .heightRange(10, 90)
        .weight(40)
        .density(4)
        .size(24)
        .enableInDim(Maahes)
        .primary(Materials2Materials.Naquadah)
        .secondary(Materials2Materials.NaquadahEnriched)
        .inBetween(Materials2Materials.Naquadria)
        .sporadic(Materials2Materials.Trinium)
        .setLocalizedName(Materials2Materials.Naquadria)),

    AwakenedDraconium(new OreMixBuilder().name("ore.mix.awakeneddraconium")
        .heightRange(20, 40)
        .weight(20)
        .density(3)
        .size(16)
        .enableInDim(MehenBelt)
        .primary(Materials2Materials.Draconium)
        .secondary(Materials2Materials.Draconium)
        .inBetween(Materials2Materials.DraconiumAwakened)
        .sporadic(Materials2Materials.NetherStar)
        .setLocalizedName(Materials2Materials.DraconiumAwakened)),

    Tengam(new OreMixBuilder().name("ore.mix.tengam")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(32)
        .enableInDim(Seth)
        .primary(Materials2Materials.TengamRaw)
        .secondary(Materials2Materials.TengamRaw)
        .inBetween(Materials2Materials.Electrotine)
        .sporadic(Materials2Materials.Samarium)),

    NitrogenIce(new OreMixBuilder().name("ore.mix.nitrogenice")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(16)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials2Materials.Nitrogen)
        .secondary(Materials2Materials.Ammonia)
        .inBetween(Materials2Materials.Hydrogen)
        .sporadic(Materials2Materials.Hydrogen)
        .stoneCategory(StoneCategory.Ice)),

    HydrocarbonIce(new OreMixBuilder().name("ore.mix.hydrocarbonice")
        .heightRange(30, 180)
        .weight(40)
        .density(2)
        .size(12)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials2Materials.Methane)
        .secondary(Materials2Materials.Hydrogen)
        .inBetween(Materials2Materials.Carbon)
        .sporadic(Materials2Materials.Carbon)
        .stoneCategory(StoneCategory.Ice)),

    CarbonIce(new OreMixBuilder().name("ore.mix.carbonice")
        .heightRange(30, 180)
        .weight(40)
        .density(2)
        .size(12)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials2Materials.CarbonDioxide)
        .secondary(Materials2Materials.Oxygen)
        .inBetween(Materials2Materials.Carbon)
        .sporadic(Materials2Materials.Carbon)
        .stoneCategory(StoneCategory.Ice)),

    HHOIce(new OreMixBuilder().name("ore.mix.hhoice")
        .heightRange(30, 180)
        .weight(80)
        .density(2)
        .size(16)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials2Materials.Oxygen)
        .secondary(Materials2Materials.Hydrogen)
        .inBetween(Materials2Materials.Oxygen)
        .sporadic(Materials2Materials.Hydrogen)
        .stoneCategory(StoneCategory.Ice)),

    SulfurIce(new OreMixBuilder().name("ore.mix.sulfurice")
        .heightRange(30, 180)
        .weight(20)
        .density(2)
        .size(8)
        .enableInDim(ASTEROIDS, KUIPERBELT)
        .primary(Materials2Materials.SulfurDioxide)
        .secondary(Materials2Materials.Oxygen)
        .inBetween(Materials2Materials.Sulfur)
        .sporadic(Materials2Materials.Oxygen)
        .stoneCategory(StoneCategory.Ice)),

    GTPP0(new OreMixBuilder().name("ore.mix.gtpp0")
        .heightRange(20, 40)
        .weight(1)
        .density(1)
        .size(128)
        .enableInDim(EVERGLADES)
        .primary(Materials2Materials.Iron)
        .secondary(Materials2Materials.Iron)
        .inBetween(Materials2Materials.Iron)
        .sporadic(Materials2Materials.Iron)),

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
        .heightRange(15, 58)
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
        .heightRange(20, 58)
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
        .heightRange(18, 58)
        .weight(20)
        .density(1)
        .size(48)
        .enableInDim(EVERGLADES)
        .primary(MaterialsFluorides.FLUORITE)
        .secondary(MaterialsOres.KASHINITE)
        .inBetween(MaterialsOres.ZIRCON)
        .sporadic(MaterialsOres.CRYOLITE)),

    GTPP12(new OreMixBuilder().name("ore.mix.gtpp12")
        .heightRange(22, 58)
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
        .secondary(Materials2Materials.Uraninite)
        .inBetween(Materials2Materials.Lepidolite)
        .sporadic(Materials2Materials.Spodumene)),

    RossCarbon(new OreMixBuilder().name("ore.mix.ross128.carbon")
        .heightRange(5, 25)
        .weight(5)
        .density(4)
        .size(12)
        .enableInDim(ROSS128B)
        .primary(Materials2Materials.Graphite)
        .secondary(Materials2Materials.Diamond)
        .inBetween(Materials2Materials.Coal)
        .sporadic(Materials2Materials.Graphite)),

    Bismuth(new OreMixBuilder().name("ore.mix.ross128.bismuth")
        .heightRange(5, 80)
        .weight(30)
        .density(1)
        .size(16)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Bismuthinit)
        .secondary(Materials2Materials.Stibnite)
        .inBetween(Materials2Materials.Bismuth)
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
        .sporadic(Materials2Materials.Scheelite)),

    CopperSulfits(new OreMixBuilder().name("ore.mix.ross128.CopperSulfits")
        .heightRange(40, 70)
        .weight(80)
        .density(3)
        .size(24)
        .enableInDim(ROSS128B)
        .primary(WerkstoffLoader.Djurleit)
        .secondary(WerkstoffLoader.Bornite)
        .inBetween(WerkstoffLoader.Wittichenit)
        .sporadic(Materials2Materials.Tetrahedrite)),

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
        .inBetween(Materials2Materials.NaquadahEnriched)
        .sporadic(Materials2Materials.NaquadahEnriched)),

    Ross128baTungstate(new OreMixBuilder().name("ore.mix.ross128ba.Tungstate")
        .heightRange(5, 40)
        .weight(60)
        .density(4)
        .size(14)
        .enableInDim(ROSS128BA)
        .primary(WerkstoffLoader.Ferberite)
        .secondary(WerkstoffLoader.Huebnerit)
        .inBetween(WerkstoffLoader.Loellingit)
        .sporadic(Materials2Materials.Scheelite)),

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
        .primary(Materials2Materials.Amethyst)
        .secondary(Materials2Materials.Olivine)
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
        .sporadic(Materials2Materials.Tetrahedrite)),

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
        .primary(Materials2Materials.Galena)
        .secondary(Materials2Materials.Sphalerite)
        .inBetween(WerkstoffLoader.Fluorspar)
        .sporadic(Materials2Materials.Barite));
    // spotless : on

    public static final OreMixes[] VALUES = values();

    public final OreMixBuilder oreMixBuilder;

    OreMixes(OreMixBuilder oreMixBuilder) {
        this.oreMixBuilder = oreMixBuilder;
    }

    public WorldgenGTOreLayer addGTOreLayer() {
        return new WorldgenGTOreLayer(this.oreMixBuilder);
    }

    // used in the coremod, for the shuttle schematic recipe
    /**
     * Give all the stone variants of an ore, with the specified stack size.
     *
     * @param material  The material of the ore
     * @param stackSize The stacksize of the variants
     * @return The array containing all the stone variants of the given ore, with the given stack size
     */
    public static ItemStack[] getOreVariants(Materials material, int stackSize) {
        return getOreVariants(MU.toMaterial(material), stackSize);
    }

    /**
     * Give all the stone variants of an ore, with the specified stack size.
     *
     * @param material  The material of the ore
     * @param stackSize The stacksize of the variants
     * @return The array containing all the stone variants of the given ore, with the given stack size
     */
    public static ItemStack[] getOreVariants(Material material, int stackSize) {
        List<ItemStack> variants = new ArrayList<>();
        Set<StoneType> stoneTypes = getStoneTypesFromMixes(material);
        for (StoneType stoneType : stoneTypes) {
            OrePrefixes prefix = stoneType.getPrefix();
            ItemStack ore = GTOreDictUnificator.get(prefix, material, 1L);
            if (!GTUtility.isStackValid(ore)) {
                continue;
            }
            for (ItemStack variant : GTOreDictUnificator.getNonUnifiedStacks(ore)) {
                ItemStack sizedVariant = GTUtility.copyAmount(stackSize, variant);
                if (!GTUtility.isStackValid(sizedVariant) || containsStack(variants, sizedVariant)) {
                    continue;
                }
                variants.add(sizedVariant);
            }
        }

        return variants.toArray(new ItemStack[0]);
    }

    public static boolean containsStack(List<ItemStack> stacks, ItemStack candidate) {
        for (ItemStack stack : stacks) {
            if (GTUtility.areStacksEqual(stack, candidate, true)) {
                return true;
            }
        }
        return false;
    }

    public static List<OreMixes> getOreMixes(Materials material) {
        return getOreMixes(MU.toMaterial(material));
    }

    public static List<OreMixes> getOreMixes(Material material) {
        List<OreMixes> mixes = new ArrayList<>();
        for (OreMixes mix : OreMixes.VALUES) {
            if (mix.containMaterial(material)) {
                mixes.add(mix);
            }
        }

        return mixes;
    }

    public static Set<StoneType> getStoneTypesFromMixes(Materials material) {
        return getStoneTypesFromMixes(MU.toMaterial(material));
    }

    public static Set<StoneType> getStoneTypesFromMixes(Material material) {
        List<OreMixes> mixes = getOreMixes(material);
        Set<StoneType> stoneTypes = new HashSet<>();
        for (OreMixes mix : mixes) {
            for (String dim : mix.oreMixBuilder.dimsEnabled) {
                for (StoneType stoneType : getStoneTypesForDim(dim)) {
                    if (mix.oreMixBuilder.stoneCategories.contains(stoneType.getCategory())) {
                        stoneTypes.add(stoneType);
                    }
                }
            }
        }

        return stoneTypes;
    }

    private static Set<StoneType> getStoneTypesForDim(String dim) {
        Set<StoneType> stoneTypes = new HashSet<>();
        String fullName = DimensionHelper.getDimFullName(dim);
        stoneTypes.addAll(
            DimensionHelper.REGISTRY.get(fullName)
                .stoneTypes());
        addGTStoneTypes(stoneTypes, dim);
        addAsteroidStoneTypes(stoneTypes, dim);
        return stoneTypes;
    }

    private static void addGTStoneTypes(Set<StoneType> stoneTypes, String dim) {
        // To account for all the GTStones entries, which apply only either in OW or Nether
        Integer dimensionId = switch (dim) {
            case OW -> 0;
            case NETHER -> -1;
            default -> null;
        };

        if (dimensionId == null) return;

        for (GTStones gtStone : GTStones.VALUES) {
            if (gtStone.stone.enabledByDefault && gtStone.stone.dimension == dimensionId) {
                StoneType stoneType = StoneType.findStoneType(gtStone.stone.block, gtStone.stone.blockMeta);
                if (stoneType != null) stoneTypes.add(stoneType);
            }
        }
    }

    private static void addAsteroidStoneTypes(Set<StoneType> stoneTypes, String dim) {
        for (DimensionDef def : DimensionDef.VALUES) {
            if (!def.modDimensionDef.getDimensionName()
                .equals(dim)) continue;

            for (IStoneType stoneType : def.modDimensionDef.getAsteroidMaterials()) {
                if (stoneType instanceof StoneType gtStoneType) stoneTypes.add(gtStoneType);
            }
            return;
        }
    }

    /// The builder stores MaterialLib materials; a legacy caller's [Materials] resolves to its MaterialLib
    /// backing (a canonical singleton) before the identity comparison.
    public boolean containMaterial(Materials material) {
        return containMaterial(MU.toMaterial(material));
    }

    public boolean containMaterial(Material material) {
        if (material == null) return false;
        return oreMixBuilder.primary == material || oreMixBuilder.secondary == material
            || oreMixBuilder.sporadic == material
            || oreMixBuilder.between == material;
    }
}
