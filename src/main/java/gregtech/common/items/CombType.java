package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

public enum CombType {
    //Organic Line
    LIGNIE("lignite", true, Materials.Lignite, 100,0x58300B, 0x906237),
    COAL("coal", true, Materials.Coal, 100,0x525252, 0x666666),
    STICKY("sticky resin", true, Materials._NULL, 50,0x2E8F5B, 0xDCC289),
    OIL("oil", true, Materials._NULL, 100,0x333333, 0x4C4C4C),
    APATITE("apatite", true, Materials.Apatite, 100,0xc1c1f6, 0x676784),
    ASH("ash", true, Materials.Ash, 100,0x1e1a18, 0xc6c6c6),

    //IC2 Line
    COOLANT("coolant", true, Materials._NULL, 100,0x144F5A, 0x2494A2),
    ENERGY("energy", true, Materials._NULL, 80,0xC11F1F, 0xEBB9B9),
    LAPOTRON("lapotron", true, Materials._NULL, 60,0x1414FF, 0x6478FF),
    PYROTHEUM("pyrotheum", true, Materials.Pyrotheum, 50,0xffebc4, 0xe36400),
    CRYOTHEUM("cryotheum", true, Materials.Pyrotheum, 50,0x2660ff, 0x5af7ff),

    //Alloy Line
    REDALLOY("red alloy", true, Materials.RedAlloy, 100,0xE60000, 0xB80000),
    REDSTONEALLOY("redstone alloy", true, Materials.RedstoneAlloy, 90,0xB80000, 0xA50808),
    CONDUCTIVEIRON("conductive iron", true, Materials.ConductiveIron, 80,0x817671, 0xCEADA3),
    VIBRANTALLOY("vibrant alloy", true, Materials.VibrantAlloy, 50,0x86A12D, 0xC4F2AE),
    ENERGETICALLOY("energetic alloy", true, Materials.EnergeticAlloy, 70,0xFF9933, 0xFFAD5C),
    ELECTRICALSTEEL("electrical steel", true, Materials.ElectricalSteel, 90,0x787878, 0xD8D8D8),
    DARKSTEEL("dark steel", true, Materials.DarkSteel, 80,0x252525, 0x443B44),
    PULSATINGIRON("pulsating iron", true, Materials.PulsatingIron, 80,0x006600, 0x6DD284),
    STAINLESSSTEEL("stainless steel", true, Materials.StainlessSteel, 75,0x778899, 0xC8C8DC),
    ENDERIUM("enderium", true, Materials.Enderium, 40,0x2E8B57, 0x599087),

    //Thaumcraft Line
    THAUMIUMDUST("thaumium dust", true, Materials.Thaumium, 100,0x7A007A, 0x5C005C),
    THAUMIUMSHARD("thaumium shard", true, Materials._NULL, 85,0x9966FF, 0xAD85FF),
    AMBER("amber", true, Materials.Amber, 90,0x774B15, 0xEE7700),
    QUICKSILVER("quicksilver", true, Materials.Mercury, 90,0xc7c7ea, 0xb5b3df),
    SALISMUNDUS("salis mundus", true, Materials._NULL, 75,0xF7ADDE, 0x592582),
    TAINTED("tainted", true, Materials._NULL, 80,0x904BB8, 0xE800FF),
    MITHRIL("mithril", true, Materials.Mithril, 70,0xF0E68C, 0xFFFFD2),
    ASTRALSILVER("astral silver", true, Materials.AstralSilver, 70,0xAFEEEE, 0xE6E6FF),
    THAUMINITE("thauminite", true, Materials._NULL, 50,0x2E2D79, 0x7581E0),
    SHADOWMETAL("shadow metal", true, Materials.Shadow, 50,0x100322, 0x100342),
    DIVIDED("divided", true, Materials.Unstable, 40,0xF0F0F0, 0xDCDCDC),
    SPARKELING("sparkling", true, Materials.NetherStar, 40,0x7A007A, 0xFFFFFF),

    //Gem Line
    STONE("stone", true, Materials._NULL, 70,0x808080, 0x999999),
    CERTUS("certus", true, Materials.CertusQuartz, 100,0x57CFFB, 0xBBEEFF),
    FLUIX("fluix", true, Materials.Fluix, 100,0xA375FF, 0xB591FF),
    REDSTONE("redstone", true, Materials.Redstone, 100,0x7D0F0F, 0xD11919),
    RAREEARTH("rare earth", true, Materials.RareEarth, 100,0x555643, 0x343428),
    LAPIS("lapis", true, Materials.Lapis, 100,0x1947D1, 0x476CDA),
    RUBY("ruby", true, Materials.Ruby, 100,0xE6005C, 0xCC0052),
    REDGARNET("red garnet", true, Materials.GarnetRed,100,0xBD4C4C, 0xECCECE),
    YELLOWGARNET("yellow garnet", true, Materials.GarnetYellow,100,0xA3A341, 0xEDEDCE),
    SAPPHIRE("sapphire", true, Materials.Sapphire, 100,0x0033CC, 0x00248F),
    DIAMOND("diamond", true, Materials.Diamond, 100,0xCCFFFF, 0xA3CCCC),
    OLIVINE("olivine", true, Materials.Olivine, 100,0x248F24, 0xCCFFCC),
    EMERALD("emerald", true, Materials.Emerald, 100,0x248F24, 0x2EB82E),
    PYROPE("pyrope", true, Materials.Pyrope, 100,0x763162, 0x8B8B8B),
    GROSSULAR("grossular", true, Materials.Grossular, 100,0x9B4E00, 0x8B8B8B),
    FIRESTONE("firestone", true, Materials.Firestone, 100,0xC00000, 0xFF0000),

    //Metals Line
    SLAG("slag", true, Materials._NULL, 50,0xD4D4D4, 0x58300B),
    COPPER("copper", true, Materials.Copper, 100,0xFF6600, 0xE65C00),
    TIN("tin", true, Materials.Tin, 100,0xD4D4D4, 0xDDDDDD),
    LEAD("lead", true, Materials.Lead, 100,0x666699, 0xA3A3CC),
    IRON("iron", true, Materials.Iron, 100,0xDA9147, 0xDE9C59),
    STEEL("steel", true, Materials.Steel, 95,0x808080, 0x999999),
    NICKEL("nickel", true, Materials.Nickel, 100,0x8585AD, 0x9D9DBD),
    ZINC("zinc", true, Materials.Zinc, 100,0xF0DEF0, 0xF2E1F2),
    SILVER("silver", true, Materials.Silver, 100,0xC2C2D6, 0xCECEDE),
    GOLD("gold", true, Materials.Gold, 100,0xE6B800, 0xCFA600),
    SULFUR("sulfur", true, Materials.Sulfur, 100,0x6F6F01, 0x8B8B8B),
    GALLIUM ("gallium", true, Materials.Gallium, 75,0x8B8B8B, 0xC5C5E4),
    ARSENIC ("arsenic", true, Materials.Arsenic, 75,0x736C52, 0x292412),

    //Rare Metals Line
    BAUXITE("bauxite", true, Materials.Bauxite, 85,0x6B3600, 0x8B8B8B),
    ALUMINIUM("aluminium", true, Materials.Aluminium, 60,0x008AB8, 0xD6D6FF),
    MANGANESE("manganese", true, Materials.Manganese, 30,0xD5D5D5, 0xAAAAAA),
    MAGNESIUM("magnesium", true, Materials.Magnesium, 75,0xF1D9D9, 0x8B8B8B),
    TITANIUM("titanium", true, Materials.Ilmenite, 100,0xCC99FF, 0xDBB8FF),
    CHROME("chromium", true, Materials.Chrome, 50,0xEBA1EB, 0xF2C3F2),
    TUNGSTEN("tungsten", true, Materials.Tungstate, 100,0x62626D, 0x161620),
    PLATINUM("platinum", true, Materials.Platinum, 40,0xE6E6E6, 0xFFFFCC),
    IRIDIUM("iridium", true, Materials.Iridium, 20,0xDADADA, 0xD1D1E0),
    MOLYBDENUM("molybdenum", true, Materials.Molybdenum, 20,0xAEAED4, 0x8B8B8B),
    OSMIUM("osmium", true, Materials.Osmium, 15,0x2B2BDA, 0x8B8B8B),
    LITHIUM("lithium", true, Materials.Lithium, 75,0xF0328C, 0xE1DCFF),
    SALT("salt", true, Materials.Salt, 90,0xF0C8C8, 0xFAFAFA),
    ELECTROTINE("electrotine", true, Materials.Electrotine, 75,0x1E90FF, 0x3CB4C8),
    ALMANDINE("almandine", true, Materials.Almandine, 85,0xC60000, 0x8B8B8B),

    //Radioactive Line
    URANIUM("uranium", true, Materials.Uranium, 50,0x19AF19, 0x169E16),
    PLUTONIUM("plutonium", true, Materials.Plutonium, 10,0x240000, 0x570000),
    NAQUADAH("naquadah", true, Materials.Naquadah, 10,0x000000, 0x004400),
    NAQUADRIA("naquadria", true, Materials.Naquadria, 5,0x000000, 0x002400),
    DOB("d-o-b", true, Materials._NULL, 50,0x007700, 0x002400),
    THORIUM("thorium", true, Materials.Thorium, 75,0x001E00, 0x005000),
    LUTETIUM("lutetium", true, Materials.Lutetium, 10,0xE6FFE6, 0xFFFFFF),
    AMERICUM("americum", true, Materials.Americium, 5,0xE6E6FF, 0xC8C8C8),
    NEUTRONIUM("neutronium", true, Materials.Neutronium, 2,0xFFF0F0, 0xFAFAFA),

    //Twilight
    NAGA("naga", true, Materials._NULL, 100,0x0D5A0D, 0x28874B),
    LICH("lich", true, Materials._NULL, 90,0x5C605E, 0xC5C5C5),
    HYDRA("hydra", true, Materials._NULL, 80,0x872836, 0xB8132C),
    URGHAST("ur-ghast", true, Materials._NULL, 70,0x7C0618, 0xA7041C),
    SNOWQUEEN("snow queen", true, Materials._NULL, 60,0x9C0018, 0xD02001),

    //Space
    SPACE("space", true, Materials._NULL, 100,0x003366, 0xC0C0C0),
    METEORICIRON("meteoric iron",true, Materials.MeteoricIron, 100,0x321928, 0x643250),
    DESH("desh",true, Materials.Desh, 90,0x282828, 0x323232),
    LEDOX("ledox",true, Materials.Ledox, 75,0x0000CD, 0x0074FF),
    CALLISTOICE("callisto ice",true, Materials.CallistoIce, 75,0x0074FF, 0x1EB1FF),
    MYTRYL("mytryl",true, Materials.Mytryl, 65,0xDAA520, 0xF26404),
    QUANTIUM("quantium",true, Materials.Quantium, 50,0x00FF00, 0x00D10B),
    ORIHARUKON("oriharukon",true, Materials.Oriharukon, 50,0x228B22, 0x677D68),
    MYSTERIOUSCRYSTAL("mysterious crystal",true, Materials.MysteriousCrystal, 45,0x3CB371, 0x16856C),
    BLACKPLUTONIUM("black plutonium",true, Materials.Quantium, 25,0x000000, 0x323232),
    TRINIUM("trinium",true, Materials.Trinium, 25,0xB0E0E6, 0xC8C8D2),

    //Planet
    MERCURY("mercury", true, Materials._NULL, 65,0x4A4033, 0xB5A288),
    VENUS("venus",true, Materials._NULL, 65,0x120E07, 0x272010),
    MOON("moon",true, Materials._NULL, 90,0x373735, 0x7E7E78),
    MARS("mars",true, Materials._NULL, 80,0x220D05, 0x3A1505),
    JUPITER("jupiter",true, Materials._NULL, 75,0x734B2E, 0xD0CBC4),
    SATURN("saturn",true, Materials._NULL, 55,0xD2A472, 0xF8C37B),
    URANUS("uranus",true, Materials._NULL, 45,0x75C0C9, 0x84D8EC),
    NEPTUN("neptune",true, Materials._NULL, 35,0x334CFF, 0x576DFF),
    PLUTO("pluto",true, Materials._NULL, 25,0x34271E, 0x69503D),
    HAUMEA("haumea",true, Materials._NULL, 20,0x1C1413, 0x392B28),
    MAKEMAKE("makemake",true, Materials._NULL, 20,0x301811, 0x120A07),
    CENTAURI("centauri",true, Materials._NULL, 15,0x2F2A14, 0xB06B32),
    TCETI("tceti",true, Materials._NULL, 10,0x46241A, 0x7B412F),
    BARNARDA("barnarda",true, Materials._NULL, 10,0x0D5A0D, 0xE6C18D),
    VEGA("vega",true, Materials._NULL, 10,0x1A2036, 0xB5C0DE),

    //Infinity
    COSMICNEUTRONIUM("cosmic neutronium",true, Materials._NULL, 5,0x484848, 0x323232),
    INFINITYCATALYST("infinity catalyst",true, Materials._NULL, 2,0xFFFFFF, 0xFFFFFF),
    INFINITY("infinity",true, Materials._NULL, 1,0xFFFFFF, 0xFFFFFF),

    //HEE
    ENDDUST("end dust", true, Materials._NULL, 50,0x003A7D, 0xCC00FA),
    ECTOPLASMA("ectoplasm", true, Materials._NULL, 35,0x381C40, 0xDCB0E5),
    ARCANESHARD("arcane shard", true, Materials._NULL, 35,0x333D82, 0x9010AD),
    STARDUST("stardust", true, Materials._NULL, 60,0xDCBE13, 0xffff00),
    DRAGONESSENCE("dragon essence", true, Materials._NULL, 30,0x911ECE, 0xFFA12B),
    ENDERMAN("enderman", true, Materials._NULL, 25,0x6200e7, 0x161616),
    SILVERFISH("silverfish", true, Materials._NULL, 25,0x0000000, 0xEE053D),
    ENDIUM("endium", true, Materials.HeeEndium, 50,0x2F5A6C, 0xa0ffff),
    RUNEI("rune1", true, Materials._NULL, 10,0x0104D9, 0xE31010),
    RUNEII("rune2", true, Materials._NULL, 10,0xE31010, 0x0104D9),
    FIREESSENSE("fiery essence", true, Materials._NULL, 30,0xFFA157, 0xD41238);

    public boolean showInList;
    public Materials material;
    public int chance;

    private String name;
    private int[] color;

    CombType(String pName, boolean show, Materials material, int chance, int... color) {
        this.name = pName;
        this.material = material;
        this.chance = chance;
        this.showInList = show;
        this.color=color;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
//		return "gt.comb."+this.name;
        return GT_LanguageManager.addStringLocalization("comb." + this.name, this.name.substring(0, 1).toUpperCase() + this.name.substring(1) + " Comb");
    }

    public int[] getColours() {
        return color == null || color.length != 2 ? new int[]{0,0} : color;
    }
}
