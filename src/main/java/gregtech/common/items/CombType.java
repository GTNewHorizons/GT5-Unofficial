package gregtech.common.items;

import java.util.Arrays;

import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.text.WordUtils;

import gregtech.api.enums.Materials;
import gregtech.api.util.ColorUtils;
import gregtech.api.util.GTLanguageManager;

public enum CombType {

    // Organic Line
    LIGNIE(0, "lignite", true, Materials.Lignite, 100, ColorUtils.comb1Lignite.getColor(),
        ColorUtils.comb2Lignite.getColor(), ItemComb.Voltage.LV),
    COAL(1, "coal", true, Materials.Coal, 100, ColorUtils.comb1Coal.getColor(), ColorUtils.comb2Coal.getColor(),
        ItemComb.Voltage.LV),
    STICKY(2, "sticky_resin", true, Materials._NULL, 50, ColorUtils.comb1StickyResin.getColor(),
        ColorUtils.comb2StickyResin.getColor(), ItemComb.Voltage.LV),
    OIL(3, "oil", true, Materials._NULL, 100, ColorUtils.comb1Oil.getColor(), ColorUtils.comb2Oil.getColor(),
        ItemComb.Voltage.LV),
    APATITE(4, "apatite", true, Materials.Apatite, 100, ColorUtils.comb1Apatite.getColor(),
        ColorUtils.comb2Apatite.getColor(), ItemComb.Voltage.LV),
    ASH(5, "ash", true, Materials.Ash, 100, ColorUtils.comb1Ash.getColor(), ColorUtils.comb2Ash.getColor(),
        ItemComb.Voltage.LV),

    // IC2 Line
    COOLANT(6, "coolant", true, Materials._NULL, 100, ColorUtils.comb1Coolant.getColor(),
        ColorUtils.comb2Coolant.getColor(), ItemComb.Voltage.HV),
    ENERGY(7, "energy", true, Materials._NULL, 80, ColorUtils.comb1Energy.getColor(), ColorUtils.comb2Energy.getColor(),
        ItemComb.Voltage.HV),
    LAPOTRON(8, "lapotron", true, Materials._NULL, 60, ColorUtils.comb1Lapotron.getColor(),
        ColorUtils.comb2Lapotron.getColor(), ItemComb.Voltage.HV),
    PYROTHEUM(9, "pyrotheum", true, Materials.Pyrotheum, 50, ColorUtils.comb1Pyrotheum.getColor(),
        ColorUtils.comb2Pyrotheum.getColor(), ItemComb.Voltage.HV),
    CRYOTHEUM(10, "cryotheum", true, Materials.Cryotheum, 50, ColorUtils.comb1Cryotheum.getColor(),
        ColorUtils.comb2Cryotheum.getColor(), ItemComb.Voltage.HV),

    // Alloy Line
    REDALLOY(11, "red_alloy", true, Materials.RedAlloy, 100, ColorUtils.comb1RedAlloy.getColor(),
        ColorUtils.comb2RedAlloy.getColor(), ItemComb.Voltage.LV),
    REDSTONEALLOY(12, "redstone_alloy", true, Materials.RedstoneAlloy, 90, ColorUtils.comb1RedstoneAlloy.getColor(),
        ColorUtils.comb2RedstoneAlloy.getColor(), ItemComb.Voltage.LV),
    CONDUCTIVEIRON(13, "conductive_iron", true, Materials.ConductiveIron, 80, ColorUtils.comb1ConductiveIron.getColor(),
        ColorUtils.comb2ConductiveIron.getColor(), ItemComb.Voltage.MV),
    VIBRANTALLOY(14, "vibrant_alloy", true, Materials.VibrantAlloy, 50, ColorUtils.comb1VibrantAlloy.getColor(),
        ColorUtils.comb2VibrantAlloy.getColor(), ItemComb.Voltage.HV),
    ENERGETICALLOY(15, "energetic_alloy", true, Materials.EnergeticAlloy, 70, ColorUtils.comb1EnergeticAlloy.getColor(),
        ColorUtils.comb2EnergeticAlloy.getColor(), ItemComb.Voltage.HV),
    ELECTRICALSTEEL(16, "electrical_steel", true, Materials.ElectricalSteel, 90,
        ColorUtils.comb1ElectricalSteel.getColor(), ColorUtils.comb2ElectricalSteel.getColor(), ItemComb.Voltage.LV),
    DARKSTEEL(17, "dark_steel", true, Materials.DarkSteel, 80, ColorUtils.comb1DarkSteel.getColor(),
        ColorUtils.comb2DarkSteel.getColor(), ItemComb.Voltage.MV),
    PULSATINGIRON(18, "pulsating_iron", true, Materials.PulsatingIron, 80, ColorUtils.comb1PulsatingIron.getColor(),
        ColorUtils.comb2PulsatingIron.getColor(), ItemComb.Voltage.HV),
    STAINLESSSTEEL(19, "stainless_steel", true, Materials.StainlessSteel, 75, ColorUtils.comb1StainlessSteel.getColor(),
        ColorUtils.comb2StainlessSteel.getColor(), ItemComb.Voltage.HV),
    ENDERIUM(20, "enderium", true, Materials.Enderium, 40, ColorUtils.comb1Enderium.getColor(),
        ColorUtils.comb2Enderium.getColor(), ItemComb.Voltage.HV),

    // Thaumcraft Line
    THAUMIUMDUST(21, "thaumium_dust", true, Materials.Thaumium, 100, ColorUtils.comb1Thaumium.getColor(),
        ColorUtils.comb2Thaumium.getColor(), ItemComb.Voltage.MV),
    THAUMIUMSHARD(22, "thaumium_shard", true, Materials._NULL, 85, ColorUtils.comb1ThaumiumShard.getColor(),
        ColorUtils.comb2ThaumiumShard.getColor(), ItemComb.Voltage.LV),
    AMBER(23, "amber", true, Materials.Amber, 90, ColorUtils.comb1Amber.getColor(), ColorUtils.comb2Amber.getColor(),
        ItemComb.Voltage.LV),
    QUICKSILVER(24, "quicksilver", true, Materials.Mercury, 90, ColorUtils.comb1Mercury.getColor(),
        ColorUtils.comb2Mercury.getColor(), ItemComb.Voltage.LV),
    SALISMUNDUS(25, "salis_mundus", true, Materials._NULL, 75, ColorUtils.comb1SalisMundus.getColor(),
        ColorUtils.comb2SalisMundus.getColor(), ItemComb.Voltage.MV),
    TAINTED(26, "tainted", true, Materials._NULL, 80, ColorUtils.comb1Tainted.getColor(),
        ColorUtils.comb2Tainted.getColor(), ItemComb.Voltage.LV),
    MITHRIL(27, "mithril", true, Materials.Mithril, 70, ColorUtils.comb1Mithril.getColor(),
        ColorUtils.comb2Mithril.getColor(), ItemComb.Voltage.HV),
    ASTRALSILVER(28, "astral_silver", true, Materials.AstralSilver, 70, ColorUtils.comb1AstralSilver.getColor(),
        ColorUtils.comb2AstralSilver.getColor(), ItemComb.Voltage.HV),
    THAUMINITE(29, "thauminite", true, Materials._NULL, 50, ColorUtils.comb1Thauminite.getColor(),
        ColorUtils.comb2Thauminite.getColor(), ItemComb.Voltage.HV),
    SHADOWMETAL(30, "shadow_metal", true, Materials.Shadow, 50, ColorUtils.comb1Shadow.getColor(),
        ColorUtils.comb2Shadow.getColor(), ItemComb.Voltage.HV),
    DIVIDED(31, "divided", true, Materials.Unstable, 40, ColorUtils.comb1Unstable.getColor(),
        ColorUtils.comb2Unstable.getColor(), ItemComb.Voltage.HV),
    SPARKLING(32, "nether_star", true, Materials.NetherStar, 40, ColorUtils.comb1NetherStar.getColor(),
        ColorUtils.comb2NetherStar.getColor(), ItemComb.Voltage.EV),

    // Gem Line
    STONE(33, "stone", true, Materials._NULL, 70, ColorUtils.comb1Stone.getColor(), ColorUtils.comb2Stone.getColor(),
        ItemComb.Voltage.LV),
    CERTUS(34, "certus", true, Materials.CertusQuartz, 100, ColorUtils.comb1CertusQuartz.getColor(),
        ColorUtils.comb2CertusQuartz.getColor(), ItemComb.Voltage.LV),
    FLUIX(35, "fluix", true, Materials.Fluix, 100, ColorUtils.comb1Fluix.getColor(), ColorUtils.comb2Fluix.getColor(),
        ItemComb.Voltage.LV),
    REDSTONE(36, "redstone", true, Materials.Redstone, 100, ColorUtils.comb1Redstone.getColor(),
        ColorUtils.comb2Redstone.getColor(), ItemComb.Voltage.LV),
    RAREEARTH(37, "rare_earth", true, Materials.RareEarth, 100, ColorUtils.comb1RareEarth.getColor(),
        ColorUtils.comb2RareEarth.getColor(), ItemComb.Voltage.LV),
    LAPIS(38, "lapis", true, Materials.Lapis, 100, ColorUtils.comb1Lapis.getColor(), ColorUtils.comb2Lapis.getColor(),
        ItemComb.Voltage.LV),
    RUBY(39, "ruby", true, Materials.Ruby, 100, ColorUtils.comb1Ruby.getColor(), ColorUtils.comb2Ruby.getColor(),
        ItemComb.Voltage.LV),
    REDGARNET(40, "red_garnet", true, Materials.GarnetRed, 100, ColorUtils.comb1GarnetRed.getColor(),
        ColorUtils.comb2GarnetRed.getColor(), ItemComb.Voltage.LV),
    YELLOWGARNET(41, "yellow_garnet", true, Materials.GarnetYellow, 100, ColorUtils.comb1GarnetYellow.getColor(),
        ColorUtils.comb2GarnetYellow.getColor(), ItemComb.Voltage.LV),
    SAPPHIRE(42, "sapphire", true, Materials.Sapphire, 100, ColorUtils.comb1Sapphire.getColor(),
        ColorUtils.comb2Sapphire.getColor(), ItemComb.Voltage.LV),
    DIAMOND(43, "diamond", true, Materials.Diamond, 100, ColorUtils.comb1Diamond.getColor(),
        ColorUtils.comb2Diamond.getColor(), ItemComb.Voltage.LV),
    OLIVINE(44, "olivine", true, Materials.Olivine, 100, ColorUtils.comb1Olivine.getColor(),
        ColorUtils.comb2Olivine.getColor(), ItemComb.Voltage.LV),
    EMERALD(45, "emerald", true, Materials.Emerald, 100, ColorUtils.comb1Emerald.getColor(),
        ColorUtils.comb2Emerald.getColor(), ItemComb.Voltage.LV),
    PYROPE(46, "pyrope", true, Materials.Pyrope, 100, ColorUtils.comb1Pyrope.getColor(),
        ColorUtils.comb2Pyrope.getColor(), ItemComb.Voltage.LV),
    GROSSULAR(47, "grossular", true, Materials.Grossular, 100, ColorUtils.comb1Grossular.getColor(),
        ColorUtils.comb2Grossular.getColor(), ItemComb.Voltage.LV),
    FIRESTONE(48, "firestone", true, Materials.Firestone, 100, ColorUtils.comb1Firestone.getColor(),
        ColorUtils.comb2Firestone.getColor(), ItemComb.Voltage.LV),

    // Metals Line
    SLAG(49, "slag", true, Materials._NULL, 50, ColorUtils.comb1Slag.getColor(), ColorUtils.comb2Slag.getColor(),
        ItemComb.Voltage.LV),
    COPPER(50, "copper", true, Materials.Copper, 100, ColorUtils.comb1Copper.getColor(),
        ColorUtils.comb2Copper.getColor(), ItemComb.Voltage.LV),
    TIN(51, "tin", true, Materials.Tin, 100, ColorUtils.comb1Tin.getColor(), ColorUtils.comb2Tin.getColor(),
        ItemComb.Voltage.LV),
    LEAD(52, "lead", true, Materials.Lead, 100, ColorUtils.comb1Lead.getColor(), ColorUtils.comb2Lead.getColor(),
        ItemComb.Voltage.LV),
    IRON(53, "iron", true, Materials.Iron, 100, ColorUtils.comb1Iron.getColor(), ColorUtils.comb2Iron.getColor(),
        ItemComb.Voltage.LV),
    STEEL(54, "steel", true, Materials.Steel, 95, ColorUtils.comb1Steel.getColor(), ColorUtils.comb2Steel.getColor(),
        ItemComb.Voltage.LV),
    NICKEL(55, "nickel", true, Materials.Nickel, 100, ColorUtils.comb1Nickel.getColor(),
        ColorUtils.comb2Nickel.getColor(), ItemComb.Voltage.LV),
    ZINC(56, "zinc", true, Materials.Zinc, 100, ColorUtils.comb1Zinc.getColor(), ColorUtils.comb2Zinc.getColor(),
        ItemComb.Voltage.LV),
    SILVER(57, "silver", true, Materials.Silver, 100, ColorUtils.comb1Silver.getColor(),
        ColorUtils.comb2Silver.getColor(), ItemComb.Voltage.LV),
    GOLD(58, "gold", true, Materials.Gold, 100, ColorUtils.comb1Gold.getColor(), ColorUtils.comb2Gold.getColor(),
        ItemComb.Voltage.LV),
    SULFUR(59, "sulfur", true, Materials.Sulfur, 100, ColorUtils.comb1Sulfur.getColor(),
        ColorUtils.comb2Sulfur.getColor(), ItemComb.Voltage.LV),
    GALLIUM(60, "gallium", true, Materials.Gallium, 75, ColorUtils.comb1Gallium.getColor(),
        ColorUtils.comb2Gallium.getColor(), ItemComb.Voltage.LV),
    ARSENIC(61, "arsenic", true, Materials.Arsenic, 75, ColorUtils.comb1Arsenic.getColor(),
        ColorUtils.comb2Arsenic.getColor(), ItemComb.Voltage.LV),

    // Rare Metals Line
    BAUXITE(62, "bauxite", true, Materials.Bauxite, 85, ColorUtils.comb1Bauxite.getColor(),
        ColorUtils.comb2Bauxite.getColor(), ItemComb.Voltage.LV),
    ALUMINIUM(63, "aluminium", true, Materials.Aluminium, 60, ColorUtils.comb1Aluminium.getColor(),
        ColorUtils.comb2Aluminium.getColor(), ItemComb.Voltage.LV),
    MANGANESE(64, "manganese", true, Materials.Manganese, 30, ColorUtils.comb1Manganese.getColor(),
        ColorUtils.comb2Manganese.getColor(), ItemComb.Voltage.LV),
    MAGNESIUM(65, "magnesium", true, Materials.Magnesium, 75, ColorUtils.comb1Magnesium.getColor(),
        ColorUtils.comb2Magnesium.getColor(), ItemComb.Voltage.LV),
    TITANIUM(66, "titanium", true, Materials.Ilmenite, 100, ColorUtils.comb1Ilmenite.getColor(),
        ColorUtils.comb2Ilmenite.getColor(), ItemComb.Voltage.IV),
    CHROME(67, "chrome", true, Materials.Chrome, 50, ColorUtils.comb1Chrome.getColor(),
        ColorUtils.comb2Chrome.getColor(), ItemComb.Voltage.HV),
    TUNGSTEN(68, "tungsten", true, Materials.Tungstate, 100, ColorUtils.comb1Tungstate.getColor(),
        ColorUtils.comb2Tungstate.getColor(), ItemComb.Voltage.IV),
    PLATINUM(69, "platinum", true, Materials.Platinum, 40, ColorUtils.comb1Platinum.getColor(),
        ColorUtils.comb2Platinum.getColor(), ItemComb.Voltage.HV),
    IRIDIUM(70, "iridium", true, Materials.Iridium, 20, ColorUtils.comb1Iridium.getColor(),
        ColorUtils.comb2Iridium.getColor(), ItemComb.Voltage.IV),
    MOLYBDENUM(71, "molybdenum", true, Materials.Molybdenum, 20, ColorUtils.comb1Molybdenum.getColor(),
        ColorUtils.comb2Molybdenum.getColor(), ItemComb.Voltage.LV),
    OSMIUM(72, "osmium", true, Materials.Osmium, 15, ColorUtils.comb1Osmium.getColor(),
        ColorUtils.comb2Osmium.getColor(), ItemComb.Voltage.IV),
    LITHIUM(73, "lithium", true, Materials.Lithium, 75, ColorUtils.comb1Lithium.getColor(),
        ColorUtils.comb2Lithium.getColor(), ItemComb.Voltage.MV),
    SALT(74, "salt", true, Materials.Salt, 90, ColorUtils.comb1Salt.getColor(), ColorUtils.comb2Salt.getColor(),
        ItemComb.Voltage.MV),
    ELECTROTINE(75, "electrotine", true, Materials.Electrotine, 75, ColorUtils.comb1Electrotine.getColor(),
        ColorUtils.comb2Electrotine.getColor(), ItemComb.Voltage.HV),
    ALMANDINE(76, "almandine", true, Materials.Almandine, 85, ColorUtils.comb1Almandine.getColor(),
        ColorUtils.comb2Almandine.getColor(), ItemComb.Voltage.LV),

    // Radioactive Line
    URANIUM(77, "uranium", true, Materials.Uranium, 50, ColorUtils.comb1Uranium.getColor(),
        ColorUtils.comb2Uranium.getColor(), ItemComb.Voltage.IV),
    PLUTONIUM(78, "plutonium", true, Materials.Plutonium, 10, ColorUtils.comb1Plutonium.getColor(),
        ColorUtils.comb2Plutonium.getColor(), ItemComb.Voltage.IV),
    NAQUADAH(79, "naquadah", true, Materials.Naquadah, 10, ColorUtils.comb1Naquadah.getColor(),
        ColorUtils.comb2Naquadah.getColor(), ItemComb.Voltage.IV),
    NAQUADRIA(80, "naquadria", true, Materials.Naquadria, 5, ColorUtils.comb1Naquadria.getColor(),
        ColorUtils.comb2Naquadria.getColor(), ItemComb.Voltage.IV),
    DOB(81, "d-o-b", true, Materials._NULL, 50, ColorUtils.comb1DOB.getColor(), ColorUtils.comb2DOB.getColor(),
        ItemComb.Voltage.LV),
    THORIUM(82, "thorium", true, Materials.Thorium, 75, ColorUtils.comb1Thorium.getColor(),
        ColorUtils.comb2Thorium.getColor(), ItemComb.Voltage.EV),
    LUTETIUM(83, "lutetium", true, Materials.Lutetium, 10, ColorUtils.comb1Lutetium.getColor(),
        ColorUtils.comb2Lutetium.getColor(), ItemComb.Voltage.IV),
    AMERICIUM(84, "americium", true, Materials.Americium, 5, ColorUtils.comb1Americium.getColor(),
        ColorUtils.comb2Americium.getColor(), ItemComb.Voltage.LuV),
    NEUTRONIUM(85, "neutronium", true, Materials.Neutronium, 2, ColorUtils.comb1Neutronium.getColor(),
        ColorUtils.comb2Neutronium.getColor(), ItemComb.Voltage.ZPM),

    // Twilight
    NAGA(86, "naga", true, Materials._NULL, 100, ColorUtils.comb1Naga.getColor(), ColorUtils.comb2Naga.getColor(),
        ItemComb.Voltage.MV),
    LICH(87, "lich", true, Materials._NULL, 90, ColorUtils.comb1Lich.getColor(), ColorUtils.comb2Lich.getColor(),
        ItemComb.Voltage.HV),
    HYDRA(88, "hydra", true, Materials._NULL, 80, ColorUtils.comb1Hydra.getColor(), ColorUtils.comb2Hydra.getColor(),
        ItemComb.Voltage.HV),
    URGHAST(89, "ur_ghast", true, Materials._NULL, 70, ColorUtils.comb1UrGhast.getColor(),
        ColorUtils.comb2UrGhast.getColor(), ItemComb.Voltage.EV),
    SNOWQUEEN(90, "snow_queen", true, Materials._NULL, 60, ColorUtils.comb1SnowQueen.getColor(),
        ColorUtils.comb2SnowQueen.getColor(), ItemComb.Voltage.EV),

    // Space
    SPACE(91, "space", true, Materials._NULL, 100, ColorUtils.comb1Space.getColor(), ColorUtils.comb2Space.getColor(),
        ItemComb.Voltage.HV),
    METEORICIRON(92, "meteoric_iron", true, Materials.MeteoricIron, 100, ColorUtils.comb1MeteoricIron.getColor(),
        ColorUtils.comb2MeteoricIron.getColor(), ItemComb.Voltage.EV),
    DESH(93, "desh", true, Materials.Desh, 90, ColorUtils.comb1Desh.getColor(), ColorUtils.comb2Desh.getColor(),
        ItemComb.Voltage.IV),
    LEDOX(94, "ledox", true, Materials.Ledox, 75, ColorUtils.comb1Ledox.getColor(), ColorUtils.comb2Ledox.getColor(),
        ItemComb.Voltage.IV),
    CALLISTOICE(95, "callisto_ice", true, Materials.CallistoIce, 75, ColorUtils.comb1CallistoIce.getColor(),
        ColorUtils.comb2CallistoIce.getColor(), ItemComb.Voltage.IV),
    MYTRYL(96, "mytryl", true, Materials.Mytryl, 65, ColorUtils.comb1Mytryl.getColor(),
        ColorUtils.comb2Mytryl.getColor(), ItemComb.Voltage.IV),
    QUANTIUM(97, "quantium", true, Materials.Quantium, 50, ColorUtils.comb1Quantium.getColor(),
        ColorUtils.comb2Quantium.getColor(), ItemComb.Voltage.IV),
    ORIHARUKON(98, "oriharukon", true, Materials.Oriharukon, 50, ColorUtils.comb1Oriharukon.getColor(),
        ColorUtils.comb2Oriharukon.getColor(), ItemComb.Voltage.IV),
    MYSTERIOUSCRYSTAL(99, "mysterious_crystal", true, Materials.MysteriousCrystal, 45,
        ColorUtils.comb1MysteriousCrystal.getColor(), ColorUtils.comb2MysteriousCrystal.getColor(),
        ItemComb.Voltage.LuV),
    BLACKPLUTONIUM(100, "black_plutonium", true, Materials.Quantium, 25, ColorUtils.comb1Quantium.getColor(),
        ColorUtils.comb2Quantium.getColor(), ItemComb.Voltage.LuV),
    TRINIUM(101, "trinium", true, Materials.Trinium, 25, ColorUtils.comb1Trinium.getColor(),
        ColorUtils.comb2Trinium.getColor(), ItemComb.Voltage.ZPM),

    // Planet
    MERCURY(102, "mercury", true, Materials._NULL, 65, ColorUtils.comb1Mercury.getColor(),
        ColorUtils.comb2Mercury.getColor(), ItemComb.Voltage.EV),
    VENUS(103, "venus", true, Materials._NULL, 65, ColorUtils.comb1Venus.getColor(), ColorUtils.comb2Venus.getColor(),
        ItemComb.Voltage.EV),
    MOON(104, "moon", true, Materials._NULL, 90, ColorUtils.comb1Moon.getColor(), ColorUtils.comb2Moon.getColor(),
        ItemComb.Voltage.MV),
    MARS(105, "mars", true, Materials._NULL, 80, ColorUtils.comb1Mars.getColor(), ColorUtils.comb2Mars.getColor(),
        ItemComb.Voltage.MV),
    JUPITER(106, "jupiter", true, Materials._NULL, 75, ColorUtils.comb1Jupiter.getColor(),
        ColorUtils.comb2Jupiter.getColor(), ItemComb.Voltage.MV),
    SATURN(107, "saturn", true, Materials._NULL, 55, ColorUtils.comb1Saturn.getColor(),
        ColorUtils.comb2Saturn.getColor(), ItemComb.Voltage.IV),
    URANUS(108, "uranus", true, Materials._NULL, 45, ColorUtils.comb1Uranus.getColor(),
        ColorUtils.comb2Uranus.getColor(), ItemComb.Voltage.IV),
    NEPTUNE(109, "neptune", true, Materials._NULL, 35, ColorUtils.comb1Neptune.getColor(),
        ColorUtils.comb2Neptune.getColor(), ItemComb.Voltage.IV),
    PLUTO(110, "pluto", true, Materials._NULL, 25, ColorUtils.comb1Pluto.getColor(), ColorUtils.comb2Pluto.getColor(),
        ItemComb.Voltage.LuV),
    HAUMEA(111, "haumea", true, Materials._NULL, 20, ColorUtils.comb1Haumea.getColor(),
        ColorUtils.comb2Haumea.getColor(), ItemComb.Voltage.LuV),
    MAKEMAKE(112, "makemake", true, Materials._NULL, 20, ColorUtils.comb1Makemake.getColor(),
        ColorUtils.comb2Makemake.getColor(), ItemComb.Voltage.LuV),
    CENTAURI(113, "centauri", true, Materials._NULL, 15, ColorUtils.comb1Centauri.getColor(),
        ColorUtils.comb2Centauri.getColor(), ItemComb.Voltage.ZPM),
    TCETI(114, "tceti", true, Materials._NULL, 10, ColorUtils.comb1Tceti.getColor(), ColorUtils.comb2Tceti.getColor(),
        ItemComb.Voltage.ZPM),
    BARNARDA(115, "barnarda", true, Materials._NULL, 10, ColorUtils.comb1Barnarda.getColor(),
        ColorUtils.comb2Barnarda.getColor(), ItemComb.Voltage.ZPM),
    VEGA(116, "vega", true, Materials._NULL, 10, ColorUtils.comb1Vega.getColor(), ColorUtils.comb2Vega.getColor(),
        ItemComb.Voltage.ZPM),

    // Infinity
    COSMICNEUTRONIUM(117, "cosmic_neutronium", true, Materials.CosmicNeutronium, 5,
        ColorUtils.comb1CosmicNeutronium.getColor(), ColorUtils.comb2CosmicNeutronium.getColor(), ItemComb.Voltage.UV),
    INFINITYCATALYST(118, "infinity_catalyst", true, Materials.InfinityCatalyst, 2,
        ColorUtils.comb1InfinityCatalyst.getColor(), ColorUtils.comb2InfinityCatalyst.getColor(), ItemComb.Voltage.UHV),
    INFINITY(119, "infinity", true, Materials.Infinity, 1, ColorUtils.comb1Infinity.getColor(),
        ColorUtils.comb2Infinity.getColor(), ItemComb.Voltage.UEV),

    // HEE
    ENDDUST(120, "end_dust", true, Materials._NULL, 50, ColorUtils.comb1EndDust.getColor(),
        ColorUtils.comb2EndDust.getColor(), ItemComb.Voltage.HV),
    ECTOPLASMA(121, "ectoplasma", true, Materials._NULL, 35, ColorUtils.comb1Ectoplasma.getColor(),
        ColorUtils.comb2Ectoplasma.getColor(), ItemComb.Voltage.EV),
    ARCANESHARD(122, "arcane_shard", true, Materials._NULL, 35, ColorUtils.comb1ArcaneShard.getColor(),
        ColorUtils.comb2ArcaneShard.getColor(), ItemComb.Voltage.EV),
    STARDUST(123, "stardust", true, Materials._NULL, 60, ColorUtils.comb1Stardust.getColor(),
        ColorUtils.comb2Stardust.getColor(), ItemComb.Voltage.HV),
    DRAGONESSENCE(124, "dragon_essence", true, Materials._NULL, 30, ColorUtils.comb1DragonEssence.getColor(),
        ColorUtils.comb2DragonEssence.getColor(), ItemComb.Voltage.IV),
    ENDERMAN(125, "enderman", true, Materials._NULL, 25, ColorUtils.comb1Enderman.getColor(),
        ColorUtils.comb2Enderman.getColor(), ItemComb.Voltage.IV),
    SILVERFISH(126, "silverfish", true, Materials._NULL, 25, ColorUtils.comb1Silverfish.getColor(),
        ColorUtils.comb2Silverfish.getColor(), ItemComb.Voltage.EV),
    ENDIUM(127, "endium", true, Materials.Endium, 50, ColorUtils.comb1Endium.getColor(),
        ColorUtils.comb2Endium.getColor(), ItemComb.Voltage.HV),
    RUNEI(128, "rune_type_1", true, Materials._NULL, 10, ColorUtils.comb1RuneType1.getColor(),
        ColorUtils.comb2RuneType1.getColor(), ItemComb.Voltage.IV),
    RUNEII(129, "rune_type_2", true, Materials._NULL, 10, ColorUtils.comb1RuneType2.getColor(),
        ColorUtils.comb2RuneType2.getColor(), ItemComb.Voltage.IV),
    FIREESSENSE(130, "fire_essence", true, Materials._NULL, 30, ColorUtils.comb1FireEssence.getColor(),
        ColorUtils.comb2FireEssence.getColor(), ItemComb.Voltage.IV),
    CRYOLITE(131, "cryolite", true, Materials.Cryolite, 90, ColorUtils.comb1Cryolite.getColor(),
        ColorUtils.comb2Cryolite.getColor(), ItemComb.Voltage.LV),
    // (NOBLE) GAS LINE
    HELIUM(132, "helium", true, Materials.Helium, 90, ColorUtils.comb1Helium.getColor(),
        ColorUtils.comb2Helium.getColor(), ItemComb.Voltage.HV),
    ARGON(133, "argon", true, Materials.Argon, 95, ColorUtils.comb1Argon.getColor(), ColorUtils.comb2Argon.getColor(),
        ItemComb.Voltage.MV),
    // XENON, NEON and KRYPTON Fluid extractor Recipes are located in GT_MachineRecipeLoader.java
    XENON(134, "xenon", true, Materials._NULL, 85, ColorUtils.comb1Xenon.getColor(), ColorUtils.comb2Xenon.getColor(),
        ItemComb.Voltage.IV),
    NEON(135, "neon", true, Materials._NULL, 90, ColorUtils.comb1Neon.getColor(), ColorUtils.comb2Neon.getColor(),
        ItemComb.Voltage.IV),
    KRYPTON(136, "krypton", true, Materials._NULL, 85, ColorUtils.comb1Krypton.getColor(),
        ColorUtils.comb2Krypton.getColor(), ItemComb.Voltage.IV),
    NITROGEN(137, "nitrogen", true, Materials.Nitrogen, 100, ColorUtils.comb1Nitrogen.getColor(),
        ColorUtils.comb2Nitrogen.getColor(), ItemComb.Voltage.MV),
    OXYGEN(138, "oxygen", true, Materials.Oxygen, 100, ColorUtils.comb1Oxygen.getColor(),
        ColorUtils.comb2Oxygen.getColor(), ItemComb.Voltage.MV),
    HYDROGEN(139, "hydrogen", true, Materials.Hydrogen, 100, ColorUtils.comb1Hydrogen.getColor(),
        ColorUtils.comb2Hydrogen.getColor(), ItemComb.Voltage.MV),
    // Those are supposed to be in the organic branch, but that would require shifting all comb IDs and we don't want to
    // risk it.
    PHOSPHORUS(140, "phosphorus", true, Materials.Phosphorus, 100, ColorUtils.comb1Phosphorus.getColor(),
        ColorUtils.comb2Phosphorus.getColor(), ItemComb.Voltage.HV),
    MICA(141, "mica", true, Materials.Mica, 100, ColorUtils.comb1Mica.getColor(), ColorUtils.comb2Mica.getColor(),
        ItemComb.Voltage.HV),
    // Seaweed is located in the planet line
    SEAWEED(142, "seaweed", true, Materials._NULL, 90, ColorUtils.comb1Seaweed.getColor(),
        ColorUtils.comb2Seaweed.getColor(), ItemComb.Voltage.UV),
    // just Walrus
    WALRUS(143, "walrus", true, Materials._NULL, 100, ColorUtils.comb1Walrus.getColor(),
        ColorUtils.comb2Walrus.getColor(), ItemComb.Voltage.LV),
    // TC infused Air shards line. Recipes in GT_MachineRecipeLoader.java Lines 1500+ + Nether/Endershard
    INFUSEDAER(144, "infused_air", true, Materials._NULL, 100, ColorUtils.comb1InfusedAir.getColor(),
        ColorUtils.comb2InfusedAir.getColor(), ItemComb.Voltage.LV),
    INFUSEDTERRA(145, "infused_terra", true, Materials._NULL, 100, ColorUtils.comb1InfusedTerra.getColor(),
        ColorUtils.comb2InfusedTerra.getColor(), ItemComb.Voltage.LV),
    INFUSEDIGNIS(146, "infused_ignis", true, Materials._NULL, 100, ColorUtils.comb1InfusedIgnis.getColor(),
        ColorUtils.comb2InfusedIgnis.getColor(), ItemComb.Voltage.LV),
    INFUSEDAQUA(147, "infused_aqua", true, Materials._NULL, 100, ColorUtils.comb1InfusedAqua.getColor(),
        ColorUtils.comb2InfusedAqua.getColor(), ItemComb.Voltage.LV),
    INFUSEDORDO(148, "infused_ordo", true, Materials._NULL, 100, ColorUtils.comb1InfusedOrdo.getColor(),
        ColorUtils.comb2InfusedOrdo.getColor(), ItemComb.Voltage.LV),
    INFUSEDPERDITIO(149, "infused_perditio", true, Materials._NULL, 100, ColorUtils.comb1InfusedPerditio.getColor(),
        ColorUtils.comb2InfusedPerditio.getColor(), ItemComb.Voltage.LV),
    FLUORINE(150, "fluorine", true, Materials.Fluorine, 100, ColorUtils.comb1Fluorine.getColor(),
        ColorUtils.comb2Fluorine.getColor(), ItemComb.Voltage.MV),
    BEDROCKIUM(151, "bedrockium", true, Materials.Bedrockium, 100, ColorUtils.comb1Bedrockium.getColor(),
        ColorUtils.comb2Bedrockium.getColor(), ItemComb.Voltage.EV),
    NETHERSHARD(152, "nether_shard", true, Materials.Netherrack, 100, ColorUtils.comb1Netherrack.getColor(),
        ColorUtils.comb2Netherrack.getColor(), ItemComb.Voltage.HV),
    ENDERSHARD(153, "ender_shard", true, Materials.EnderEye, 100, ColorUtils.comb1EnderEye.getColor(),
        ColorUtils.comb2EnderEye.getColor(), ItemComb.Voltage.HV),
    CAELESTISRED(154, "caelestis_red", true, Materials._NULL, 100, ColorUtils.comb1CaelestisRed.getColor(),
        ColorUtils.comb2CaelestisRed.getColor(), ItemComb.Voltage.LV),
    CAELESTISGREEN(155, "caelestis_green", true, Materials._NULL, 100, ColorUtils.comb1CaelestisGreen.getColor(),
        ColorUtils.comb2CaelestisGreen.getColor(), ItemComb.Voltage.LV),
    CAELESTISBLUE(156, "caelestis_blue", true, Materials._NULL, 100, ColorUtils.comb1CaelestisBlue.getColor(),
        ColorUtils.comb2CaelestisBlue.getColor(), ItemComb.Voltage.LV),
    UNKNOWNLIQUID(157, "unknown_liquid", true, Materials._NULL, 100, ColorUtils.comb1UnknownLiquid.getColor(),
        ColorUtils.comb2UnknownLiquid.getColor(), ItemComb.Voltage.ZPM),
    // ESSENTIA gets a use soon. Dont remove.
    ESSENTIA(158, "essentia", true, Materials._NULL, 100, ColorUtils.comb1Essentia.getColor(),
        ColorUtils.comb2Essentia.getColor(), ItemComb.Voltage.MV),
    INDIUM(159, "indium", true, Materials.Indium, 100, ColorUtils.comb1Indium.getColor(),
        ColorUtils.comb2Indium.getColor(), ItemComb.Voltage.ZPM),
    BLIZZ(160, "blizz", true, Materials.Blizz, 50, ColorUtils.comb1Blizz.getColor(), ColorUtils.comb2Blizz.getColor(),
        ItemComb.Voltage.MV),
    KEVLAR(161, "kevlar", true, Materials._NULL, 50, ColorUtils.comb1Kevlar.getColor(),
        ColorUtils.comb2Kevlar.getColor(), ItemComb.Voltage.MV),
    DRACONIC(162, "draconium", true, Materials.Draconium, 50, ColorUtils.comb1Draconium.getColor(),
        ColorUtils.comb2Draconium.getColor(), ItemComb.Voltage.MV),
    AWAKENEDDRACONIUM(163, "awakened_draconium", true, Materials.DraconiumAwakened, 50,
        ColorUtils.comb1DraconiumAwakened.getColor(), ColorUtils.comb2DraconiumAwakened.getColor(),
        ItemComb.Voltage.MV),
    PALLADIUM(164, "palladium", true, Materials.Palladium, 50, ColorUtils.comb1Palladium.getColor(),
        ColorUtils.comb2Palladium.getColor(), ItemComb.Voltage.MV),
    INFUSEDGOLD(165, "infused_gold", true, Materials.InfusedGold, 50, ColorUtils.comb1InfusedGold.getColor(),
        ColorUtils.comb2InfusedGold.getColor(), ItemComb.Voltage.IV),
    // Additions to rare metals, moved here so we don't shift all comb IDs
    NEODYMIUM(166, "neodymium", true, Materials.Neodymium, 50, ColorUtils.comb1Neodymium.getColor(),
        ColorUtils.comb2Neodymium.getColor(), ItemComb.Voltage.HV),
    EUROPIUM(167, "europium", true, Materials.Europium, 10, ColorUtils.comb1Europium.getColor(),
        ColorUtils.comb2Europium.getColor(), ItemComb.Voltage.LuV),
    MACHINIST(168, "machinist", true, Materials._NULL, 100, ColorUtils.comb1Machinist.getColor(),
        ColorUtils.comb2Machinist.getColor(), ItemComb.Voltage.MV),
    NETHERITE(169, "netherite", true, Materials._NULL, 10, ColorUtils.comb1Netherite.getColor(),
        ColorUtils.comb2Netherite.getColor(), ItemComb.Voltage.LuV),
    // Gem Addition
    PRISMATIC(170, "prismatic", true, Materials._NULL, 10, ColorUtils.comb1Prismatic.getColor(),
        ColorUtils.comb2Prismatic.getColor(), ItemComb.Voltage.UHV),
    REFINEDRAREEARTH(171, "refined_rare_earth", true, Materials._NULL, 85, ColorUtils.comb1RefinedRareEarth.getColor(),
        ColorUtils.comb2RefinedRareEarth.getColor(), ItemComb.Voltage.IV),
    // Metals Addition
    PYRITE(172, "pyrite", true, Materials.Pyrite, 100, ColorUtils.comb1Pyrite.getColor(),
        ColorUtils.comb2Pyrite.getColor(), ItemComb.Voltage.HV),
    // Botania Line
    MANASTEEL(173, "manasteel", true, Materials.Manasteel, 50, ColorUtils.comb1Manasteel.getColor(),
        ColorUtils.comb2Manasteel.getColor(), ItemComb.Voltage.LV),
    MMM(174, "M-M-M", true, Materials._NULL, 50, ColorUtils.comb1MMM.getColor(), ColorUtils.comb2MMM.getColor(),
        ItemComb.Voltage.EV),
    ELVEN(175, "elven", true, Materials._NULL, 50, ColorUtils.comb1Elven.getColor(), ColorUtils.comb2Elven.getColor(),
        ItemComb.Voltage.EV),
    TERRASTEEL(176, "terrasteel", true, Materials.Terrasteel, 50, ColorUtils.comb1Terrasteel.getColor(),
        ColorUtils.comb2Terrasteel.getColor(), ItemComb.Voltage.IV),
    GAIASPIRIT(177, "gaia_spirit", true, Materials.GaiaSpirit, 50, ColorUtils.comb1GaiaSpirit.getColor(),
        ColorUtils.comb2GaiaSpirit.getColor(), ItemComb.Voltage.LuV),

    // ALWAYS KEEP _NULL AT THE BOTTOM
    _NULL(-1, "INVALIDCOMB", false, Materials._NULL, 0, 0, 0);

    public boolean showInList;
    public final ItemComb.Voltage voltage;
    public final Materials material;
    public final int chance;

    private final int id;
    private final String name;
    private final int[] color;

    CombType(int id, String pName, boolean show, Materials material, int chance, int color1, int color2) {
        this(id, pName, show, material, chance, color1, color2, null);
    }

    CombType(int id, String pName, boolean show, Materials material, int chance, int color1, int color2,
        ItemComb.Voltage voltage) {
        if (id < 0 && !"INVALIDCOMB".equals(pName)) throw new IllegalArgumentException();
        this.id = id;
        this.voltage = voltage;
        this.material = material;
        this.chance = chance;
        this.showInList = show;
        this.color = new int[] { color1, color2 };
        this.name = pName;
        GTLanguageManager
            .addStringLocalization("comb." + pName, WordUtils.capitalize(pName.replaceAll("_", " ")) + " Comb");
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal("comb." + name);
    }

    public int[] getColours() {
        return color == null || color.length != 2 ? new int[] { 0, 0 } : color;
    }

    public int getId() {
        return id;
    }

    public static CombType valueOf(int id) {
        return id < 0 || id >= Companion.VALUES.length ? _NULL : Companion.VALUES[id];
    }

    private static final class Companion {

        private static final CombType[] VALUES;

        static {
            int biggestId = Arrays.stream(CombType.values())
                .mapToInt(CombType::getId)
                .max()
                .getAsInt();
            VALUES = new CombType[biggestId + 1];
            Arrays.fill(VALUES, _NULL);
            for (CombType type : CombType.values()) {
                if (type != _NULL) VALUES[type.getId()] = type;
            }
        }
    }
}
