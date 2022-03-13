package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

import java.util.Arrays;

public enum CombType {
    //Organic Line
    LIGNIE(0, "lignite", true, Materials.Lignite, 100, 0x58300B, 0x906237),
    COAL(1, "coal", true, Materials.Coal, 100, 0x525252, 0x666666),
    STICKY(2, "stickyresin", true, Materials._NULL, 50, 0x2E8F5B, 0xDCC289),
    OIL(3, "oil", true, Materials._NULL, 100, 0x333333, 0x4C4C4C),
    APATITE(4, "apatite", true, Materials.Apatite, 100, 0xc1c1f6, 0x676784),
    ASH(5, "ash", true, Materials.Ash, 100, 0x1e1a18, 0xc6c6c6),

    //IC2 Line
    COOLANT(6, "coolant", true, Materials._NULL, 100, 0x144F5A, 0x2494A2),
    ENERGY(7, "energy", true, Materials._NULL, 80, 0xC11F1F, 0xEBB9B9),
    LAPOTRON(8, "lapotron", true, Materials._NULL, 60, 0x1414FF, 0x6478FF),
    PYROTHEUM(9, "pyrotheum", true, Materials.Pyrotheum, 50, 0xffebc4, 0xe36400),
    CRYOTHEUM(10, "cryotheum", true, Materials.Pyrotheum, 50, 0x2660ff, 0x5af7ff),

    //Alloy Line
    REDALLOY(11, "redalloy", true, Materials.RedAlloy, 100, 0xE60000, 0xB80000),
    REDSTONEALLOY(12, "redstonealloy", true, Materials.RedstoneAlloy, 90, 0xB80000, 0xA50808),
    CONDUCTIVEIRON(13, "conductiveiron", true, Materials.ConductiveIron, 80, 0x817671, 0xCEADA3),
    VIBRANTALLOY(14, "vibrantalloy", true, Materials.VibrantAlloy, 50, 0x86A12D, 0xC4F2AE),
    ENERGETICALLOY(15, "energeticalloy", true, Materials.EnergeticAlloy, 70, 0xFF9933, 0xFFAD5C),
    ELECTRICALSTEEL(16, "electricalsteel", true, Materials.ElectricalSteel, 90, 0x787878, 0xD8D8D8),
    DARKSTEEL(17, "darksteel", true, Materials.DarkSteel, 80, 0x252525, 0x443B44),
    PULSATINGIRON(18, "pulsatingiron", true, Materials.PulsatingIron, 80, 0x006600, 0x6DD284),
    STAINLESSSTEEL(19, "stainlesssteel", true, Materials.StainlessSteel, 75, 0x778899, 0xC8C8DC),
    ENDERIUM(20, "enderium", true, Materials.Enderium, 40, 0x2E8B57, 0x599087),

    //Thaumcraft Line
    THAUMIUMDUST(21, "thaumiumdust", true, Materials.Thaumium, 100, 0x7A007A, 0x5C005C),
    THAUMIUMSHARD(22, "thaumiumshard", true, Materials._NULL, 85, 0x9966FF, 0xAD85FF),
    AMBER(23, "amber", true, Materials.Amber, 90, 0x774B15, 0xEE7700),
    QUICKSILVER(24, "quicksilver", true, Materials.Mercury, 90, 0xc7c7ea, 0xb5b3df),
    SALISMUNDUS(25, "salismundus", true, Materials._NULL, 75, 0xF7ADDE, 0x592582),
    TAINTED(26, "tainted", true, Materials._NULL, 80, 0x904BB8, 0xE800FF),
    MITHRIL(27, "mithril", true, Materials.Mithril, 70, 0xF0E68C, 0xFFFFD2),
    ASTRALSILVER(28, "astralsilver", true, Materials.AstralSilver, 70, 0xAFEEEE, 0xE6E6FF),
    THAUMINITE(29, "thauminite", true, Materials._NULL, 50, 0x2E2D79, 0x7581E0),
    SHADOWMETAL(30, "shadowmetal", true, Materials.Shadow, 50, 0x100322, 0x100342),
    DIVIDED(31, "divided", true, Materials.Unstable, 40, 0xF0F0F0, 0xDCDCDC),
    SPARKELING(32, "sparkling", true, Materials.NetherStar, 40, 0x7A007A, 0xFFFFFF),

    //Gem Line
    STONE(33, "stone", true, Materials._NULL, 70, 0x808080, 0x999999),
    CERTUS(34, "certus", true, Materials.CertusQuartz, 100, 0x57CFFB, 0xBBEEFF),
    FLUIX(35, "fluix", true, Materials.Fluix, 100, 0xA375FF, 0xB591FF),
    REDSTONE(36, "redstone", true, Materials.Redstone, 100, 0x7D0F0F, 0xD11919),
    RAREEARTH(37, "rareearth", true, Materials.RareEarth, 100, 0x555643, 0x343428),
    LAPIS(38, "lapis", true, Materials.Lapis, 100, 0x1947D1, 0x476CDA),
    RUBY(39, "ruby", true, Materials.Ruby, 100, 0xE6005C, 0xCC0052),
    REDGARNET(40, "redgarnet", true, Materials.GarnetRed, 100, 0xBD4C4C, 0xECCECE),
    YELLOWGARNET(41, "yellowgarnet", true, Materials.GarnetYellow, 100, 0xA3A341, 0xEDEDCE),
    SAPPHIRE(42, "sapphire", true, Materials.Sapphire, 100, 0x0033CC, 0x00248F),
    DIAMOND(43, "diamond", true, Materials.Diamond, 100, 0xCCFFFF, 0xA3CCCC),
    OLIVINE(44, "olivine", true, Materials.Olivine, 100, 0x248F24, 0xCCFFCC),
    EMERALD(45, "emerald", true, Materials.Emerald, 100, 0x248F24, 0x2EB82E),
    PYROPE(46, "pyrope", true, Materials.Pyrope, 100, 0x763162, 0x8B8B8B),
    GROSSULAR(47, "grossular", true, Materials.Grossular, 100, 0x9B4E00, 0x8B8B8B),
    FIRESTONE(48, "firestone", true, Materials.Firestone, 100, 0xC00000, 0xFF0000),

    //Metals Line
    SLAG(49, "slag", true, Materials._NULL, 50, 0xD4D4D4, 0x58300B),
    COPPER(50, "copper", true, Materials.Copper, 100, 0xFF6600, 0xE65C00),
    TIN(51, "tin", true, Materials.Tin, 100, 0xD4D4D4, 0xDDDDDD),
    LEAD(52, "lead", true, Materials.Lead, 100, 0x666699, 0xA3A3CC),
    IRON(53, "iron", true, Materials.Iron, 100, 0xDA9147, 0xDE9C59),
    STEEL(54, "steel", true, Materials.Steel, 95, 0x808080, 0x999999),
    NICKEL(55, "nickel", true, Materials.Nickel, 100, 0x8585AD, 0x9D9DBD),
    ZINC(56, "zinc", true, Materials.Zinc, 100, 0xF0DEF0, 0xF2E1F2),
    SILVER(57, "silver", true, Materials.Silver, 100, 0xC2C2D6, 0xCECEDE),
    GOLD(58, "gold", true, Materials.Gold, 100, 0xE6B800, 0xCFA600),
    SULFUR(59, "sulfur", true, Materials.Sulfur, 100, 0x6F6F01, 0x8B8B8B),
    GALLIUM(60, "gallium", true, Materials.Gallium, 75, 0x8B8B8B, 0xC5C5E4),
    ARSENIC(61, "arsenic", true, Materials.Arsenic, 75, 0x736C52, 0x292412),

    //Rare Metals Line
    BAUXITE(62, "bauxite", true, Materials.Bauxite, 85, 0x6B3600, 0x8B8B8B),
    ALUMINIUM(63, "aluminium", true, Materials.Aluminium, 60, 0x008AB8, 0xD6D6FF),
    MANGANESE(64, "manganese", true, Materials.Manganese, 30, 0xD5D5D5, 0xAAAAAA),
    MAGNESIUM(65, "magnesium", true, Materials.Magnesium, 75, 0xF1D9D9, 0x8B8B8B),
    TITANIUM(66, "titanium", true, Materials.Ilmenite, 100, 0xCC99FF, 0xDBB8FF),
    CHROME(67, "chromium", true, Materials.Chrome, 50, 0xEBA1EB, 0xF2C3F2),
    TUNGSTEN(68, "tungsten", true, Materials.Tungstate, 100, 0x62626D, 0x161620),
    PLATINUM(69, "platinum", true, Materials.Platinum, 40, 0xE6E6E6, 0xFFFFCC),
    IRIDIUM(70, "iridium", true, Materials.Iridium, 20, 0xDADADA, 0xD1D1E0),
    MOLYBDENUM(71, "molybdenum", true, Materials.Molybdenum, 20, 0xAEAED4, 0x8B8B8B),
    OSMIUM(72, "osmium", true, Materials.Osmium, 15, 0x2B2BDA, 0x8B8B8B),
    LITHIUM(73, "lithium", true, Materials.Lithium, 75, 0xF0328C, 0xE1DCFF),
    SALT(74, "salt", true, Materials.Salt, 90, 0xF0C8C8, 0xFAFAFA),
    ELECTROTINE(75, "electrotine", true, Materials.Electrotine, 75, 0x1E90FF, 0x3CB4C8),
    ALMANDINE(76, "almandine", true, Materials.Almandine, 85, 0xC60000, 0x8B8B8B),

    //Radioactive Line
    URANIUM(77, "uranium", true, Materials.Uranium, 50, 0x19AF19, 0x169E16),
    PLUTONIUM(78, "plutonium", true, Materials.Plutonium, 10, 0x240000, 0x570000),
    NAQUADAH(79, "naquadah", true, Materials.Naquadah, 10, 0x000000, 0x004400),
    NAQUADRIA(80, "naquadria", true, Materials.Naquadria, 5, 0x000000, 0x002400),
    DOB(81, "d-o-b", true, Materials._NULL, 50, 0x007700, 0x002400),
    THORIUM(82, "thorium", true, Materials.Thorium, 75, 0x001E00, 0x005000),
    LUTETIUM(83, "lutetium", true, Materials.Lutetium, 10, 0xE6FFE6, 0xFFFFFF),
    AMERICIUM(84, "americium", true, Materials.Americium, 5, 0xE6E6FF, 0xC8C8C8),
    NEUTRONIUM(85, "neutronium", true, Materials.Neutronium, 2, 0xFFF0F0, 0xFAFAFA),

    //Twilight
    NAGA(86, "naga", true, Materials._NULL, 100, 0x0D5A0D, 0x28874B),
    LICH(87, "lich", true, Materials._NULL, 90, 0x5C605E, 0xC5C5C5),
    HYDRA(88, "hydra", true, Materials._NULL, 80, 0x872836, 0xB8132C),
    URGHAST(89, "urghast", true, Materials._NULL, 70, 0x7C0618, 0xA7041C),
    SNOWQUEEN(90, "snowqueen", true, Materials._NULL, 60, 0x9C0018, 0xD02001),

    //Space
    SPACE(91, "space", true, Materials._NULL, 100, 0x003366, 0xC0C0C0),
    METEORICIRON(92, "meteoriciron", true, Materials.MeteoricIron, 100, 0x321928, 0x643250),
    DESH(93, "desh", true, Materials.Desh, 90, 0x282828, 0x323232),
    LEDOX(94, "ledox", true, Materials.Ledox, 75, 0x0000CD, 0x0074FF),
    CALLISTOICE(95, "callistoice", true, Materials.CallistoIce, 75, 0x0074FF, 0x1EB1FF),
    MYTRYL(96, "mytryl", true, Materials.Mytryl, 65, 0xDAA520, 0xF26404),
    QUANTIUM(97, "quantium", true, Materials.Quantium, 50, 0x00FF00, 0x00D10B),
    ORIHARUKON(98, "oriharukon", true, Materials.Oriharukon, 50, 0x228B22, 0x677D68),
    MYSTERIOUSCRYSTAL(99, "mysteriouscrystal", true, Materials.MysteriousCrystal, 45, 0x3CB371, 0x16856C),
    BLACKPLUTONIUM(100, "blackplutonium", true, Materials.Quantium, 25, 0x000000, 0x323232),
    TRINIUM(101, "trinium", true, Materials.Trinium, 25, 0xB0E0E6, 0xC8C8D2),

    //Planet
    MERCURY(102, "mercury", true, Materials._NULL, 65, 0x4A4033, 0xB5A288),
    VENUS(103, "venus", true, Materials._NULL, 65, 0x120E07, 0x272010),
    MOON(104, "moon", true, Materials._NULL, 90, 0x373735, 0x7E7E78),
    MARS(105, "mars", true, Materials._NULL, 80, 0x220D05, 0x3A1505),
    JUPITER(106, "jupiter", true, Materials._NULL, 75, 0x734B2E, 0xD0CBC4),
    SATURN(107, "saturn", true, Materials._NULL, 55, 0xD2A472, 0xF8C37B),
    URANUS(108, "uranus", true, Materials._NULL, 45, 0x75C0C9, 0x84D8EC),
    NEPTUN(109, "neptun", true, Materials._NULL, 35, 0x334CFF, 0x576DFF),
    PLUTO(110, "pluto", true, Materials._NULL, 25, 0x34271E, 0x69503D),
    HAUMEA(111, "haumea", true, Materials._NULL, 20, 0x1C1413, 0x392B28),
    MAKEMAKE(112, "makemake", true, Materials._NULL, 20, 0x301811, 0x120A07),
    CENTAURI(113, "centauri", true, Materials._NULL, 15, 0x2F2A14, 0xB06B32),
    TCETI(114, "tceti", true, Materials._NULL, 10, 0x46241A, 0x7B412F),
    BARNARDA(115, "barnarda", true, Materials._NULL, 10, 0x0D5A0D, 0xE6C18D),
    VEGA(116, "vega", true, Materials._NULL, 10, 0x1A2036, 0xB5C0DE),

    //Infinity
    COSMICNEUTRONIUM(117, "cosmicneutronium", true, Materials._NULL, 5, 0x484848, 0x323232),
    INFINITYCATALYST(118, "infinitycatalyst", true, Materials._NULL, 2, 0xFFFFFF, 0xFFFFFF),
    INFINITY(119, "infinity", true, Materials._NULL, 1, 0xFFFFFF, 0xFFFFFF),

    //HEE
    ENDDUST(120, "enddust", true, Materials._NULL, 50, 0x003A7D, 0xCC00FA),
    ECTOPLASMA(121, "ectoplasma", true, Materials._NULL, 35, 0x381C40, 0xDCB0E5),
    ARCANESHARD(122, "arcaneshard", true, Materials._NULL, 35, 0x333D82, 0x9010AD),
    STARDUST(123, "stardust", true, Materials._NULL, 60, 0xDCBE13, 0xffff00),
    DRAGONESSENCE(124, "dragonessence", true, Materials._NULL, 30, 0x911ECE, 0xFFA12B),
    ENDERMAN(125, "enderman", true, Materials._NULL, 25, 0x6200e7, 0x161616),
    SILVERFISH(126, "silverfish", true, Materials._NULL, 25, 0x0000000, 0xEE053D),
    ENDIUM(127, "endium", true, Materials.HeeEndium, 50, 0x2F5A6C, 0xa0ffff),
    RUNEI(128, "rune1", true, Materials._NULL, 10, 0x0104D9, 0xE31010),
    RUNEII(129, "rune2", true, Materials._NULL, 10, 0xE31010, 0x0104D9),
    FIREESSENSE(130, "fireessence", true, Materials._NULL, 30, 0xFFA157, 0xD41238),
    CRYOLITE(131, "cryolite", true, Materials.Cryolite, 90, 0xBFEFFF, 0x73B9D0),
    _NULL(-1, "INVALIDCOMB", false, Materials._NULL, 0, 0, 0);

    public boolean showInList;
    public Materials material;
    public int chance;

    private final int id;
    private final String name;
    private final int[] color;

    CombType(String pName, boolean show, Materials material, int chance, int... color) {
        this.id = ordinal();
        this.name = pName;
        this.material = material;
        this.chance = chance;
        this.showInList = show;
        this.color = color;
    }

    CombType(int id, String pName, boolean show, Materials material, int chance, int... color) {
        if (id < 0 && !"INVALIDCOMB".equals(pName))
            throw new IllegalArgumentException();
        this.id = id;
        this.name = pName;
        this.material = material;
        this.chance = chance;
        this.showInList = show;
        this.color = color;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {

        return GT_LanguageManager.addStringLocalization("comb." + this.name, this.name.substring(0, 1).toUpperCase() + this.name.substring(1) + " Comb");
    }

    public int[] getColours() {
        return color == null || color.length != 2 ? new int[]{0, 0} : color;
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
            int biggestId = Arrays.stream(CombType.values()).mapToInt(CombType::getId).max().getAsInt();
            VALUES = new CombType[biggestId + 1];
            Arrays.fill(VALUES, _NULL);
            for (CombType type : CombType.values()) {
                if (type != _NULL)
                    VALUES[type.getId()] = type;
            }
        }
    }
}
