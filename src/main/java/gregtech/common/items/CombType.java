package gregtech.common.items;

import java.util.Arrays;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

public enum CombType {

    // Organic Line
    LIGNIE(0, "lignite", true, Materials.Lignite, 100, 0x58300B, 0x906237, ItemComb.Voltage.LV),
    COAL(1, "coal", true, Materials.Coal, 100, 0x525252, 0x666666, ItemComb.Voltage.LV),
    STICKY(2, "stickyresin", true, Materials._NULL, 50, 0x2E8F5B, 0xDCC289, ItemComb.Voltage.LV),
    OIL(3, "oil", true, Materials._NULL, 100, 0x333333, 0x4C4C4C, ItemComb.Voltage.LV),
    APATITE(4, "apatite", true, Materials.Apatite, 100, 0xc1c1f6, 0x676784, ItemComb.Voltage.LV),
    ASH(5, "ash", true, Materials.Ash, 100, 0x1e1a18, 0xc6c6c6, ItemComb.Voltage.LV),

    // IC2 Line
    COOLANT(6, "coolant", true, Materials._NULL, 100, 0x144F5A, 0x2494A2, ItemComb.Voltage.HV),
    ENERGY(7, "energy", true, Materials._NULL, 80, 0xC11F1F, 0xEBB9B9, ItemComb.Voltage.HV),
    LAPOTRON(8, "lapotron", true, Materials._NULL, 60, 0x1414FF, 0x6478FF, ItemComb.Voltage.HV),
    PYROTHEUM(9, "pyrotheum", true, Materials.Pyrotheum, 50, 0xffebc4, 0xe36400, ItemComb.Voltage.HV),
    CRYOTHEUM(10, "cryotheum", true, Materials.Cryotheum, 50, 0x2660ff, 0x5af7ff, ItemComb.Voltage.HV),

    // Alloy Line
    REDALLOY(11, "redalloy", true, Materials.RedAlloy, 100, 0xE60000, 0xB80000, ItemComb.Voltage.LV),
    REDSTONEALLOY(12, "redstonealloy", true, Materials.RedstoneAlloy, 90, 0xB80000, 0xA50808, ItemComb.Voltage.LV),
    CONDUCTIVEIRON(13, "conductiveiron", true, Materials.ConductiveIron, 80, 0x817671, 0xCEADA3, ItemComb.Voltage.MV),
    VIBRANTALLOY(14, "vibrantalloy", true, Materials.VibrantAlloy, 50, 0x86A12D, 0xC4F2AE, ItemComb.Voltage.HV),
    ENERGETICALLOY(15, "energeticalloy", true, Materials.EnergeticAlloy, 70, 0xFF9933, 0xFFAD5C, ItemComb.Voltage.HV),
    ELECTRICALSTEEL(16, "electricalsteel", true, Materials.ElectricalSteel, 90, 0x787878, 0xD8D8D8,
        ItemComb.Voltage.LV),
    DARKSTEEL(17, "darksteel", true, Materials.DarkSteel, 80, 0x252525, 0x443B44, ItemComb.Voltage.MV),
    PULSATINGIRON(18, "pulsatingiron", true, Materials.PulsatingIron, 80, 0x006600, 0x6DD284, ItemComb.Voltage.HV),
    STAINLESSSTEEL(19, "stainlesssteel", true, Materials.StainlessSteel, 75, 0x778899, 0xC8C8DC, ItemComb.Voltage.HV),
    ENDERIUM(20, "enderium", true, Materials.Enderium, 40, 0x2E8B57, 0x599087, ItemComb.Voltage.HV),

    // Thaumcraft Line
    THAUMIUMDUST(21, "thaumiumdust", true, Materials.Thaumium, 100, 0x7A007A, 0x5C005C, ItemComb.Voltage.MV),
    THAUMIUMSHARD(22, "thaumiumshard", true, Materials._NULL, 85, 0x9966FF, 0xAD85FF, ItemComb.Voltage.LV),
    AMBER(23, "amber", true, Materials.Amber, 90, 0x774B15, 0xEE7700, ItemComb.Voltage.LV),
    QUICKSILVER(24, "quicksilver", true, Materials.Mercury, 90, 0xc7c7ea, 0xb5b3df, ItemComb.Voltage.LV),
    SALISMUNDUS(25, "salismundus", true, Materials._NULL, 75, 0xF7ADDE, 0x592582, ItemComb.Voltage.MV),
    TAINTED(26, "tainted", true, Materials._NULL, 80, 0x904BB8, 0xE800FF, ItemComb.Voltage.LV),
    MITHRIL(27, "mithril", true, Materials.Mithril, 70, 0xF0E68C, 0xFFFFD2, ItemComb.Voltage.HV),
    ASTRALSILVER(28, "astralsilver", true, Materials.AstralSilver, 70, 0xAFEEEE, 0xE6E6FF, ItemComb.Voltage.HV),
    THAUMINITE(29, "thauminite", true, Materials._NULL, 50, 0x2E2D79, 0x7581E0, ItemComb.Voltage.HV),
    SHADOWMETAL(30, "shadowmetal", true, Materials.Shadow, 50, 0x100322, 0x100342, ItemComb.Voltage.HV),
    DIVIDED(31, "divided", true, Materials.Unstable, 40, 0xF0F0F0, 0xDCDCDC, ItemComb.Voltage.HV),
    SPARKELING(32, "sparkling", true, Materials.NetherStar, 40, 0x7A007A, 0xFFFFFF, ItemComb.Voltage.EV),

    // Gem Line
    STONE(33, "stone", true, Materials._NULL, 70, 0x808080, 0x999999, ItemComb.Voltage.LV),
    CERTUS(34, "certus", true, Materials.CertusQuartz, 100, 0x57CFFB, 0xBBEEFF, ItemComb.Voltage.LV),
    FLUIX(35, "fluix", true, Materials.Fluix, 100, 0xA375FF, 0xB591FF, ItemComb.Voltage.LV),
    REDSTONE(36, "redstone", true, Materials.Redstone, 100, 0x7D0F0F, 0xD11919, ItemComb.Voltage.LV),
    RAREEARTH(37, "rareearth", true, Materials.RareEarth, 100, 0x555643, 0x343428, ItemComb.Voltage.LV),
    LAPIS(38, "lapis", true, Materials.Lapis, 100, 0x1947D1, 0x476CDA, ItemComb.Voltage.LV),
    RUBY(39, "ruby", true, Materials.Ruby, 100, 0xE6005C, 0xCC0052, ItemComb.Voltage.LV),
    REDGARNET(40, "redgarnet", true, Materials.GarnetRed, 100, 0xBD4C4C, 0xECCECE, ItemComb.Voltage.LV),
    YELLOWGARNET(41, "yellowgarnet", true, Materials.GarnetYellow, 100, 0xA3A341, 0xEDEDCE, ItemComb.Voltage.LV),
    SAPPHIRE(42, "sapphire", true, Materials.Sapphire, 100, 0x0033CC, 0x00248F, ItemComb.Voltage.LV),
    DIAMOND(43, "diamond", true, Materials.Diamond, 100, 0xCCFFFF, 0xA3CCCC, ItemComb.Voltage.LV),
    OLIVINE(44, "olivine", true, Materials.Olivine, 100, 0x248F24, 0xCCFFCC, ItemComb.Voltage.LV),
    EMERALD(45, "emerald", true, Materials.Emerald, 100, 0x248F24, 0x2EB82E, ItemComb.Voltage.LV),
    PYROPE(46, "pyrope", true, Materials.Pyrope, 100, 0x763162, 0x8B8B8B, ItemComb.Voltage.LV),
    GROSSULAR(47, "grossular", true, Materials.Grossular, 100, 0x9B4E00, 0x8B8B8B, ItemComb.Voltage.LV),
    FIRESTONE(48, "firestone", true, Materials.Firestone, 100, 0xC00000, 0xFF0000, ItemComb.Voltage.LV),

    // Metals Line
    SLAG(49, "slag", true, Materials._NULL, 50, 0xD4D4D4, 0x58300B, ItemComb.Voltage.LV),
    COPPER(50, "copper", true, Materials.Copper, 100, 0xFF6600, 0xE65C00, ItemComb.Voltage.LV),
    TIN(51, "tin", true, Materials.Tin, 100, 0xD4D4D4, 0xDDDDDD, ItemComb.Voltage.LV),
    LEAD(52, "lead", true, Materials.Lead, 100, 0x666699, 0xA3A3CC, ItemComb.Voltage.LV),
    IRON(53, "iron", true, Materials.Iron, 100, 0xDA9147, 0xDE9C59, ItemComb.Voltage.LV),
    STEEL(54, "steel", true, Materials.Steel, 95, 0x808080, 0x999999, ItemComb.Voltage.LV),
    NICKEL(55, "nickel", true, Materials.Nickel, 100, 0x8585AD, 0x9D9DBD, ItemComb.Voltage.LV),
    ZINC(56, "zinc", true, Materials.Zinc, 100, 0xF0DEF0, 0xF2E1F2, ItemComb.Voltage.LV),
    SILVER(57, "silver", true, Materials.Silver, 100, 0xC2C2D6, 0xCECEDE, ItemComb.Voltage.LV),
    GOLD(58, "gold", true, Materials.Gold, 100, 0xE6B800, 0xCFA600, ItemComb.Voltage.LV),
    SULFUR(59, "sulfur", true, Materials.Sulfur, 100, 0x6F6F01, 0x8B8B8B, ItemComb.Voltage.LV),
    GALLIUM(60, "gallium", true, Materials.Gallium, 75, 0x8B8B8B, 0xC5C5E4, ItemComb.Voltage.LV),
    ARSENIC(61, "arsenic", true, Materials.Arsenic, 75, 0x736C52, 0x292412, ItemComb.Voltage.LV),

    // Rare Metals Line
    BAUXITE(62, "bauxite", true, Materials.Bauxite, 85, 0x6B3600, 0x8B8B8B, ItemComb.Voltage.LV),
    ALUMINIUM(63, "aluminium", true, Materials.Aluminium, 60, 0x008AB8, 0xD6D6FF, ItemComb.Voltage.LV),
    MANGANESE(64, "manganese", true, Materials.Manganese, 30, 0xD5D5D5, 0xAAAAAA, ItemComb.Voltage.LV),
    MAGNESIUM(65, "magnesium", true, Materials.Magnesium, 75, 0xF1D9D9, 0x8B8B8B, ItemComb.Voltage.LV),
    TITANIUM(66, "titanium", true, Materials.Ilmenite, 100, 0xCC99FF, 0xDBB8FF, ItemComb.Voltage.IV),
    CHROME(67, "chromium", true, Materials.Chrome, 50, 0xEBA1EB, 0xF2C3F2, ItemComb.Voltage.HV),
    TUNGSTEN(68, "tungsten", true, Materials.Tungstate, 100, 0x62626D, 0x161620, ItemComb.Voltage.IV),
    PLATINUM(69, "platinum", true, Materials.Platinum, 40, 0xE6E6E6, 0xFFFFCC, ItemComb.Voltage.HV),
    IRIDIUM(70, "iridium", true, Materials.Iridium, 20, 0xDADADA, 0xD1D1E0, ItemComb.Voltage.IV),
    MOLYBDENUM(71, "molybdenum", true, Materials.Molybdenum, 20, 0xAEAED4, 0x8B8B8B, ItemComb.Voltage.LV),
    OSMIUM(72, "osmium", true, Materials.Osmium, 15, 0x2B2BDA, 0x8B8B8B, ItemComb.Voltage.IV),
    LITHIUM(73, "lithium", true, Materials.Lithium, 75, 0xF0328C, 0xE1DCFF, ItemComb.Voltage.MV),
    SALT(74, "salt", true, Materials.Salt, 90, 0xF0C8C8, 0xFAFAFA, ItemComb.Voltage.MV),
    ELECTROTINE(75, "electrotine", true, Materials.Electrotine, 75, 0x1E90FF, 0x3CB4C8, ItemComb.Voltage.HV),
    ALMANDINE(76, "almandine", true, Materials.Almandine, 85, 0xC60000, 0x8B8B8B, ItemComb.Voltage.LV),

    // Radioactive Line
    URANIUM(77, "uranium", true, Materials.Uranium, 50, 0x19AF19, 0x169E16, ItemComb.Voltage.IV),
    PLUTONIUM(78, "plutonium", true, Materials.Plutonium, 10, 0x240000, 0x570000, ItemComb.Voltage.IV),
    NAQUADAH(79, "naquadah", true, Materials.Naquadah, 10, 0x000000, 0x004400, ItemComb.Voltage.IV),
    NAQUADRIA(80, "naquadria", true, Materials.Naquadria, 5, 0x000000, 0x002400, ItemComb.Voltage.IV),
    DOB(81, "d-o-b", true, Materials._NULL, 50, 0x007700, 0x002400, ItemComb.Voltage.LV),
    THORIUM(82, "thorium", true, Materials.Thorium, 75, 0x001E00, 0x005000, ItemComb.Voltage.EV),
    LUTETIUM(83, "lutetium", true, Materials.Lutetium, 10, 0xE6FFE6, 0xFFFFFF, ItemComb.Voltage.IV),
    AMERICIUM(84, "americium", true, Materials.Americium, 5, 0xE6E6FF, 0xC8C8C8, ItemComb.Voltage.LuV),
    NEUTRONIUM(85, "neutronium", true, Materials.Neutronium, 2, 0xFFF0F0, 0xFAFAFA, ItemComb.Voltage.ZPM),

    // Twilight
    NAGA(86, "naga", true, Materials._NULL, 100, 0x0D5A0D, 0x28874B, ItemComb.Voltage.MV),
    LICH(87, "lich", true, Materials._NULL, 90, 0x5C605E, 0xC5C5C5, ItemComb.Voltage.HV),
    HYDRA(88, "hydra", true, Materials._NULL, 80, 0x872836, 0xB8132C, ItemComb.Voltage.HV),
    URGHAST(89, "urghast", true, Materials._NULL, 70, 0x7C0618, 0xA7041C, ItemComb.Voltage.EV),
    SNOWQUEEN(90, "snowqueen", true, Materials._NULL, 60, 0x9C0018, 0xD02001, ItemComb.Voltage.EV),

    // Space
    SPACE(91, "space", true, Materials._NULL, 100, 0x003366, 0xC0C0C0, ItemComb.Voltage.HV),
    METEORICIRON(92, "meteoriciron", true, Materials.MeteoricIron, 100, 0x321928, 0x643250, ItemComb.Voltage.EV),
    DESH(93, "desh", true, Materials.Desh, 90, 0x282828, 0x323232, ItemComb.Voltage.IV),
    LEDOX(94, "ledox", true, Materials.Ledox, 75, 0x0000CD, 0x0074FF, ItemComb.Voltage.IV),
    CALLISTOICE(95, "callistoice", true, Materials.CallistoIce, 75, 0x0074FF, 0x1EB1FF, ItemComb.Voltage.IV),
    MYTRYL(96, "mytryl", true, Materials.Mytryl, 65, 0xDAA520, 0xF26404, ItemComb.Voltage.IV),
    QUANTIUM(97, "quantium", true, Materials.Quantium, 50, 0x00FF00, 0x00D10B, ItemComb.Voltage.IV),
    ORIHARUKON(98, "oriharukon", true, Materials.Oriharukon, 50, 0x228B22, 0x677D68, ItemComb.Voltage.IV),
    MYSTERIOUSCRYSTAL(99, "mysteriouscrystal", true, Materials.MysteriousCrystal, 45, 0x3CB371, 0x16856C,
        ItemComb.Voltage.LuV),
    BLACKPLUTONIUM(100, "blackplutonium", true, Materials.Quantium, 25, 0x000000, 0x323232, ItemComb.Voltage.LuV),
    TRINIUM(101, "trinium", true, Materials.Trinium, 25, 0xB0E0E6, 0xC8C8D2, ItemComb.Voltage.ZPM),

    // Planet
    MERCURY(102, "mercury", true, Materials._NULL, 65, 0x4A4033, 0xB5A288, ItemComb.Voltage.EV),
    VENUS(103, "venus", true, Materials._NULL, 65, 0x120E07, 0x272010, ItemComb.Voltage.EV),
    MOON(104, "moon", true, Materials._NULL, 90, 0x373735, 0x7E7E78, ItemComb.Voltage.MV),
    MARS(105, "mars", true, Materials._NULL, 80, 0x220D05, 0x3A1505, ItemComb.Voltage.MV),
    JUPITER(106, "jupiter", true, Materials._NULL, 75, 0x734B2E, 0xD0CBC4, ItemComb.Voltage.MV),
    SATURN(107, "saturn", true, Materials._NULL, 55, 0xD2A472, 0xF8C37B, ItemComb.Voltage.IV),
    URANUS(108, "uranus", true, Materials._NULL, 45, 0x75C0C9, 0x84D8EC, ItemComb.Voltage.IV),
    NEPTUN(109, "neptun", true, Materials._NULL, 35, 0x334CFF, 0x576DFF, ItemComb.Voltage.IV),
    PLUTO(110, "pluto", true, Materials._NULL, 25, 0x34271E, 0x69503D, ItemComb.Voltage.LuV),
    HAUMEA(111, "haumea", true, Materials._NULL, 20, 0x1C1413, 0x392B28, ItemComb.Voltage.LuV),
    MAKEMAKE(112, "makemake", true, Materials._NULL, 20, 0x301811, 0x120A07, ItemComb.Voltage.LuV),
    CENTAURI(113, "centauri", true, Materials._NULL, 15, 0x2F2A14, 0xB06B32, ItemComb.Voltage.ZPM),
    TCETI(114, "tceti", true, Materials._NULL, 10, 0x46241A, 0x7B412F, ItemComb.Voltage.ZPM),
    BARNARDA(115, "barnarda", true, Materials._NULL, 10, 0x0D5A0D, 0xE6C18D, ItemComb.Voltage.ZPM),
    VEGA(116, "vega", true, Materials._NULL, 10, 0x1A2036, 0xB5C0DE, ItemComb.Voltage.ZPM),

    // Infinity
    COSMICNEUTRONIUM(117, "cosmicneutronium", true, Materials.CosmicNeutronium, 5, 0x484848, 0x323232,
        ItemComb.Voltage.UV),
    INFINITYCATALYST(118, "infinitycatalyst", true, Materials.InfinityCatalyst, 2, 0xFFFFFF, 0xFFFFFF,
        ItemComb.Voltage.UHV),
    INFINITY(119, "infinity", true, Materials.Infinity, 1, 0xFFFFFF, 0xFFFFFF, ItemComb.Voltage.UEV),

    // HEE
    ENDDUST(120, "enddust", true, Materials._NULL, 50, 0x003A7D, 0xCC00FA, ItemComb.Voltage.HV),
    ECTOPLASMA(121, "ectoplasma", true, Materials._NULL, 35, 0x381C40, 0xDCB0E5, ItemComb.Voltage.EV),
    ARCANESHARD(122, "arcaneshard", true, Materials._NULL, 35, 0x333D82, 0x9010AD, ItemComb.Voltage.EV),
    STARDUST(123, "stardust", true, Materials._NULL, 60, 0xDCBE13, 0xffff00, ItemComb.Voltage.HV),
    DRAGONESSENCE(124, "dragonessence", true, Materials._NULL, 30, 0x911ECE, 0xFFA12B, ItemComb.Voltage.IV),
    ENDERMAN(125, "enderman", true, Materials._NULL, 25, 0x6200e7, 0x161616, ItemComb.Voltage.IV),
    SILVERFISH(126, "silverfish", true, Materials._NULL, 25, 0x0000000, 0xEE053D, ItemComb.Voltage.EV),
    ENDIUM(127, "endium", true, Materials.HeeEndium, 50, 0x2F5A6C, 0xa0ffff, ItemComb.Voltage.HV),
    RUNEI(128, "rune1", true, Materials._NULL, 10, 0x0104D9, 0xE31010, ItemComb.Voltage.IV),
    RUNEII(129, "rune2", true, Materials._NULL, 10, 0xE31010, 0x0104D9, ItemComb.Voltage.IV),
    FIREESSENSE(130, "fireessence", true, Materials._NULL, 30, 0xFFA157, 0xD41238, ItemComb.Voltage.IV),
    CRYOLITE(131, "cryolite", true, Materials.Cryolite, 90, 0xBFEFFF, 0x73B9D0, ItemComb.Voltage.LV),
    // (NOBLE) GAS LINE
    HELIUM(132, "helium", true, Materials.Helium, 90, 0xFFA9FF, 0xFFFFC3, ItemComb.Voltage.HV),
    ARGON(133, "argon", true, Materials.Argon, 95, 0x89D9E1, 0x160822, ItemComb.Voltage.MV),
    // XENON, NEON and KRYPTON Fluid extractor Recipes are located in GT_MachineRecipeLoader.java
    XENON(134, "xenon", true, Materials._NULL, 85, 0x160822, 0x8A97B0, ItemComb.Voltage.IV),
    NEON(135, "neon", true, Materials._NULL, 90, 0xFF7200, 0xFFC826, ItemComb.Voltage.IV),
    KRYPTON(136, "krypton", true, Materials._NULL, 85, 0x160822, 0x8A97B0, ItemComb.Voltage.IV),
    NITROGEN(137, "nitrogen", true, Materials.Nitrogen, 100, 0xA52A2A, 0xFFC832, ItemComb.Voltage.MV),
    OXYGEN(138, "oxygen", true, Materials.Oxygen, 100, 0x8F8FFF, 0xFFFFFF, ItemComb.Voltage.MV),
    HYDROGEN(139, "hydrogen", true, Materials.Hydrogen, 100, 0xFF1493, 0xFFFFFF, ItemComb.Voltage.MV),
    // Those are supposed to be in the organic branch, but that would require shifting all comb IDs and we don't want to
    // risk it.
    PHOSPHORUS(140, "phosphorus", true, Materials.Phosphorus, 100, 0xC1C1F6, 0xFFC826, ItemComb.Voltage.HV),
    MICA(141, "mica", true, Materials.Mica, 100, 0x8A97B0, 0x2F3641, ItemComb.Voltage.HV),
    // Seaweed is located in the planet line
    SEAWEED(142, "seaweed", true, Materials._NULL, 90, 0x83FF83, 0xCBCBCB, ItemComb.Voltage.UV),
    // just Walrus
    WALRUS(143, "walrus", true, Materials._NULL, 100, 0xB5CFC9, 0xD6D580, ItemComb.Voltage.LV),
    // TC infused Air shards line. Recipes in GT_MachineRecipeLoader.java Lines 1500+ + Nether/Endshard
    INFUSEDAER(144, "infusedair", true, Materials._NULL, 100, 0x60602F, 0xFFFF7E, ItemComb.Voltage.LV),
    INFUSEDTERRA(145, "infusedterra", true, Materials._NULL, 100, 0x003300, 0x008600, ItemComb.Voltage.LV),
    INFUSEDIGNIS(146, "infusedignis", true, Materials._NULL, 100, 0x3B0E00, 0xED3801, ItemComb.Voltage.LV),
    INFUSEDAQUA(147, "infusedaqua", true, Materials._NULL, 100, 0x002542, 0x0090FF, ItemComb.Voltage.LV),
    INFUSEDORDO(148, "infusedordo", true, Materials._NULL, 100, 0x5C5F62, 0x8A97B0, ItemComb.Voltage.LV),
    INFUSEDPERDITIO(149, "infusedperditio", true, Materials._NULL, 100, 0x232129, 0x2E2E41, ItemComb.Voltage.LV),
    FLUORINE(150, "fluorine", true, Materials.Fluorine, 100, 0xFF6D00, 0x86AFF0, ItemComb.Voltage.MV),
    BEDROCKIUM(151, "bedrockium", true, Materials.Bedrockium, 100, 0xC6C6C6, 0x0C0C0C, ItemComb.Voltage.EV),
    NETHERSHARD(152, "nethershard", true, Materials.Netherrack, 100, 0x350211, 0xBE0135, ItemComb.Voltage.HV),
    ENDSHARD(153, "endshard", true, Materials.EnderEye, 100, 0x232129, 0x2E2E41, ItemComb.Voltage.HV),
    CAELESTISRED(154, "caelestisred", true, Materials._NULL, 100, 0xFF0000, 0xFF00FF, ItemComb.Voltage.LV),
    CAELESTISGREEN(155, "caelestisgreen", true, Materials._NULL, 100, 0x00FF00, 0xB233FF, ItemComb.Voltage.LV),
    CAELESTISBLUE(156, "caelestisblue", true, Materials._NULL, 100, 0x0000FF, 0xFF99A5, ItemComb.Voltage.LV),
    UNKNOWNWATER(157, "unknownwater", true, Materials._NULL, 100, 0x36ABFF, 0x4333A5, ItemComb.Voltage.ZPM),
    // ESSENTIA gets a use soon. Dont remove.
    ESSENTIA(158, "essentia", true, Materials._NULL, 100, 0xED3601, 0xFF6D50, ItemComb.Voltage.MV),
    INDIUM(159, "indium", true, Materials.Indium, 100, 0x8F5D99, 0xFFA9FF, ItemComb.Voltage.ZPM),
    BLIZZ(160, "blizz", true, Materials.Blizz, 50, 0xFF99A5, 0x5af7ff, ItemComb.Voltage.MV),
    KEVLAR(161, "kevlar", true, Materials._NULL, 50, 0xa2baa3, 0x2d542f, ItemComb.Voltage.MV),
    DRACONIC(162, "draconium", true, Materials.Draconium, 50, 0x161616, 0x6200e7, ItemComb.Voltage.MV),
    AWAKENEDDRACONIUM(163, "awakeneddraconium", true, Materials.DraconiumAwakened, 50, 0xD41238, 0xFFA157,
        ItemComb.Voltage.MV),
    PALLADIUM(164, "palladium", true, Materials.Palladium, 50, 0x8B8B8B, 0xF1D9D9, ItemComb.Voltage.MV),
    INFUSEDGOLD(165, "infusedgold", true, Materials.InfusedGold, 50, 0x80641E, 0xFFC83C, ItemComb.Voltage.IV),
    DIDDY(166, "diddy", true, Materials._NULL, 100, 0x552582, 0xFDB927, ItemComb.Voltage.MV),

    // ALWAYS KEEP _NULL AT THE BOTTOM
    _NULL(-1, "INVALIDCOMB", false, Materials._NULL, 0, 0, 0);

    public boolean showInList;
    public final ItemComb.Voltage voltage;
    public final Materials material;
    public final int chance;

    private final int id;
    private final String localizedName;
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
        this.localizedName = GT_LanguageManager.addStringLocalization(
            "comb." + pName,
            pName.substring(0, 1)
                .toUpperCase() + pName.substring(1)
                + " Comb");
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {

        return this.localizedName;
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
