package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

public enum CombType {
    //Organic Line
    LIGNIE("lignite", true, Materials.Lignite, 100),
    COAL("coal", true, Materials.Coal, 100),
    STICKY("stickyresin", true, Materials._NULL, 50),
    OIL("oil", true, Materials._NULL, 100),

    //IC2 Line
    COOLANT("coolant", true, Materials._NULL, 100),
    ENERGY("energy", true, Materials._NULL, 80),
    LAPOTRON("lapotron", true, Materials._NULL, 60),

    //Alloy Line
    REDALLOY("redalloy", true, Materials.RedAlloy, 100),
    //REDSTONEALLOY("redstonealloy", true, Materials.RedstoneAlloy, 90),
    //CONDUCTIVEIRON("conductiveiron", true, Materials.ConductiveIron, 80),
    //VIBRANTALLOY("vibrantalloy", true, Materials.VibrantAlloy, 50),
    //ENERGETICALLOY("energeticalloy", true, Materials.EnergeticAlloy, 70),
    //ELECTRICALSTEEL("electricalsteel", true, Materials.ElectricalSteel, 90),
    //DARKSTEEL("darksteel", true, Materials.DarkSteel, 80),
    //PULSATINGIRON("pulsatingiron", true, Materials.PulsatingIron, 80),
    STAINLESSSTEEL("stainlesssteel", true, Materials.StainlessSteel, 75),
    ENDERIUM("enderium", true, Materials.Enderium, 40),

    /*//Thaumcraft Line
    THAUMIUMDUST("thaumiumdust", true, Materials.Thaumium, 100),
    THAUMIUMSHARD("thaumiumshard", true, Materials._NULL, 85),
    AMBER("amber", true, Materials.Amber, 90),
    QUICKSILVER("quicksilver", true, Materials.Mercury, 90),
    SALISMUNDUS("salismundus", true, Materials._NULL, 75),
    TAINTED("tainted", true, Materials._NULL, 80),
    MITHRIL("mithril", true, Materials.Mithril, 70),
    ASTRALSILVER("astralsilver", true, Materials.AstralSilver, 70),
    THAUMINITE("thauminite", true, Materials._NULL, 50),
    //SHADOWMETAL("shadowmetal", true, Materials.Shadow, 50),
    DIVIDED("divided", true, Materials.Unstable, 40),
    SPARKELING("sparkeling", true, Materials.NetherStar, 40),
*/
    //Gem Line
    STONE("stone", true, Materials._NULL, 70),
    CERTUS("certus", true, Materials.CertusQuartz, 100),
    FLUIX("fluix", true, Materials.Fluix, 100),
    REDSTONE("redstone", true, Materials.Redstone, 100),
    RAREEARTH("rareearth", true, Materials.RareEarth, 100),
    LAPIS("lapis", true, Materials.Lapis, 100),
    RUBY("ruby", true, Materials.Ruby, 100),
    REDGARNET("redgarnet", true, Materials.GarnetRed,100),
    YELLOWGARNET("yellowgarnet", true, Materials.GarnetYellow,100),
    SAPPHIRE("sapphire", true, Materials.Sapphire, 100),
    DIAMOND("diamond", true, Materials.Diamond, 100),
    OLIVINE("olivine", true, Materials.Olivine, 100),
    EMERALD("emerald", true, Materials.Emerald, 100),
    PYROPE("pyrope", true, Materials.Pyrope, 100),
    GROSSULAR("grossular", true, Materials.Grossular, 100),
    //FIRESTONE("firestone", true, Materials.Firestone, 100),

    //Metals Line
    SLAG("slag", true, Materials._NULL, 50),
    COPPER("copper", true, Materials.Copper, 100),
    TIN("tin", true, Materials.Tin, 100),
    LEAD("lead", true, Materials.Lead, 100),
    IRON("iron", true, Materials.Iron, 100),
    STEEL("steel", true, Materials.Steel, 95),
    NICKEL("nickel", true, Materials.Nickel, 100),
    ZINC("zinc", true, Materials.Zinc, 100),
    SILVER("silver", true, Materials.Silver, 100),
    GOLD("gold", true, Materials.Gold, 100),
    SULFUR("sulfur", true, Materials.Sulfur, 100),
    GALLIUM ("gallium", true, Materials.Gallium, 75),
    ARSENIC ("arsenic", true, Materials.Arsenic, 75),

    //Rare Metals Line
    BAUXITE("bauxite", true, Materials.Bauxite, 85),
    ALUMINIUM("aluminium", true, Materials.Aluminium, 60),
    MANGANESE("manganese", true, Materials.Manganese, 30),
    MAGNESIUM("magnesium", true, Materials.Magnesium, 75),
    TITANIUM("titanium", true, Materials.Ilmenite, 100),
    CHROME("chromium", true, Materials.Chrome, 50),
    TUNGSTEN("tungsten", true, Materials.Tungstate, 100),
    PLATINUM("platinum", true, Materials.Platinum, 40),
    IRIDIUM("iridium", true, Materials.Iridium, 20),
    MOLYBDENUM("molybdenum", true, Materials.Molybdenum, 20),
    OSMIUM("osmium", true, Materials.Osmium, 15),
    LITHIUM("lithium", true, Materials.Lithium, 75),
    SALT("salt", true, Materials.Salt, 90),
    //ELECTROTINE("electrotine", true, Materials.Electrotine, 75),
    ALMANDINE("almandine", true, Materials.Almandine, 85),

    //Radioactive Line
    URANIUM("uranium", true, Materials.Uranium, 50),
    PLUTONIUM("plutonium", true, Materials.Plutonium, 10),
    NAQUADAH("naquadah", true, Materials.Naquadah, 10),
    NAQUADRIA("naquadria", true, Materials.Naquadria, 5),
    //DOB("dob", true, Materials._NULL, 50),
    THORIUM("thorium", true, Materials.Thorium, 75),
    LUTETIUM("lutetium", true, Materials.Lutetium, 10),
    AMERICUM("americum", true, Materials.Americium, 5),
    NEUTRONIUM("neutronium", true, Materials.Neutronium, 2),

    //Twilight
    /*NAGA("naga", true, Materials._NULL, 100),
    LICH("lich", true, Materials._NULL, 90),
    HYDRA("hydra", true, Materials._NULL, 80),
    URGHAST("urghast", true, Materials._NULL, 70),
    SNOWQUEEN("snowqueen", true, Materials._NULL, 60),
*/
    //Space
    SPACE("space", true, Materials._NULL, 100),
    METEORICIRON("meteoriciron",true, Materials.MeteoricIron, 100);
    //DESH("desh",true, Materials.Desh, 90),
    //LEDOX("ledox",true, Materials.Ledox, 75),
    //CALLISTOICE("callistoice",true, Materials.CallistoIce, 75),
    //MYTRYL("mytryl",true, Materials.Mytryl, 65),
    //QUANTIUM("quantium",true, Materials.Quantium, 50),
    //ORIHARUKON("oriharukon",true, Materials.Oriharukon, 50),
    //MYSTERIOUSCRYSTAL("mysteriouscrystal",true, Materials.MysteriousCrystal, 45),
    //BLACKPLUTONIUM("blackplutonium",true, Materials.Quantium, 25),
    //TRINIUM("trinium",true, Materials.Trinium, 25),

    /*//Planet
    MERCURY("mercury", true, Materials._NULL, 65),
    VENUS("venus",true, Materials._NULL, 65),
    MOON("moon",true, Materials._NULL, 90),
    MARS("mars",true, Materials._NULL, 80),
    JUPITER("jupiter",true, Materials._NULL, 75),
    SATURN("saturn",true, Materials._NULL, 55),
    URANUS("uranus",true, Materials._NULL, 45),
    NEPTUN("neptun",true, Materials._NULL, 35),
    PLUTO("pluto",true, Materials._NULL, 25),
    HAUMEA("haumea",true, Materials._NULL, 20),
    MAKEMAKE("makemake",true, Materials._NULL, 20),
    CENTAURI("centauri",true, Materials._NULL, 15),
    TCETI("tceti",true, Materials._NULL, 10),
    BARNARDA("barnarda",true, Materials._NULL, 10),
    VEGA("vega",true, Materials._NULL, 10),

    //Infinity
    COSMICNEUTRONIUM("cosmicneutronium",true, Materials._NULL, 5),
    INFINITYCATALYST("infinitycatalyst",true, Materials._NULL, 2),
    INFINITY("infinity",true, Materials._NULL, 1);
*/
    private static int[][] colours = new int[][]{
            //organic
            {0x58300B, 0x906237},
            {0x525252, 0x666666},
            {0x2E8F5B, 0xDCC289},
            {0x333333, 0x4C4C4C},
            //ic2
            {0x144F5A, 0x2494A2},
            {0xC11F1F, 0xEBB9B9},
            {0x1414FF, 0x6478FF},
            //alloy
            {0xE60000, 0xB80000},
            {0xB80000, 0xA50808},
            {0x817671, 0xCEADA3},
            {0x86A12D, 0xC4F2AE},
            {0xFF9933, 0xFFAD5C},
            {0x787878, 0xD8D8D8},
            {0x252525, 0x443B44},
            {0x006600, 0x6DD284},
            {0x778899, 0xC8C8DC},
            {0x2E8B57, 0x599087},
            //Thaumcraft
            {0x7A007A, 0x5C005C},
            {0x9966FF, 0xAD85FF},
            {0x774B15, 0xEE7700},
            {0xc7c7ea, 0xb5b3df},
            {0xF7ADDE, 0x592582},
            {0x904BB8, 0xE800FF},
            {0xF0E68C, 0xFFFFD2},
            {0xAFEEEE, 0xE6E6FF},
            {0x2E2D79, 0x7581E0},
            {0x100322, 0x100342},
            {0xF0F0F0, 0xDCDCDC},
            {0x7A007A, 0xFFFFFF},
            //gems
            {0x808080, 0x999999},
            {0x57CFFB, 0xBBEEFF},
            {0xA375FF, 0xB591FF},
            {0x7D0F0F, 0xD11919},
            {0x555643, 0x343428},
            {0x1947D1, 0x476CDA},
            {0xE6005C, 0xCC0052},
            {0xBD4C4C, 0xECCECE},
            {0xA3A341, 0xEDEDCE},
            {0x0033CC, 0x00248F},
            {0xCCFFFF, 0xA3CCCC},
            {0x248F24, 0xCCFFCC},
            {0x248F24, 0x2EB82E},
            {0x763162, 0x8B8B8B},
            {0x9B4E00, 0x8B8B8B},
            {0xC00000, 0xFF0000},
            //Metals
            {0xD4D4D4, 0x58300B},
            {0xFF6600, 0xE65C00},
            {0xD4D4D4, 0xDDDDDD},
            {0x666699, 0xA3A3CC},
            {0xDA9147, 0xDE9C59},
            {0x808080, 0x999999},
            {0x8585AD, 0x9D9DBD},
            {0xF0DEF0, 0xF2E1F2},
            {0xC2C2D6, 0xCECEDE},
            {0xE6B800, 0xCFA600},
            {0x6F6F01, 0x8B8B8B},
            {0x8B8B8B, 0xC5C5E4},
            {0x736C52, 0x292412},
            //Rare Metals
            {0x6B3600, 0x8B8B8B},
            {0x008AB8, 0xD6D6FF},
            {0xD5D5D5, 0xAAAAAA},
            {0xF1D9D9, 0x8B8B8B},
            {0xCC99FF, 0xDBB8FF},
            {0xEBA1EB, 0xF2C3F2},
            {0x62626D, 0x161620},
            {0xE6E6E6, 0xFFFFCC},
            {0xDADADA, 0xD1D1E0},
            {0xAEAED4, 0x8B8B8B},
            {0x2B2BDA, 0x8B8B8B},
            {0xF0328C, 0xE1DCFF},
            {0xF0C8C8, 0xFAFAFA},
            {0x1E90FF, 0x3CB4C8},
            {0xC60000, 0x8B8B8B},
            //Radioactive Line
            {0x19AF19, 0x169E16},
            {0x240000, 0x570000},
            {0x000000, 0x004400},
            {0x000000, 0x002400},
            {0x007700, 0x002400},
            {0x001E00, 0x005000},
            {0xE6FFE6, 0xFFFFFF},
            {0xE6E6FF, 0xC8C8C8},
            {0xFFF0F0, 0xFAFAFA},
            //Twilight
            {0x0D5A0D, 0x28874B},
            {0x5C605E, 0xC5C5C5},
            {0x872836, 0xB8132C},
            {0x7C0618, 0xA7041C},
            {0x9C0018, 0xD02001},
            //space
            {0x003366, 0xC0C0C0},
            {0x321928, 0x643250},
            {0x282828, 0x323232},
            {0x0000CD, 0x0074FF},
            {0x0074FF, 0x1EB1FF},
            {0xDAA520, 0xF26404},
            {0x00FF00, 0x00D10B},
            {0x228B22, 0x677D68},
            {0x3CB371, 0x16856C},
            {0x000000, 0x323232},
            {0xB0E0E6, 0xC8C8D2},
            //planets
            {0x4A4033, 0xB5A288},
            {0x120E07, 0x272010},
            {0x373735, 0x7E7E78},
            {0x220D05, 0x3A1505},
            {0x734B2E, 0xD0CBC4},
            {0xD2A472, 0xF8C37B},
            {0x75C0C9, 0x84D8EC},
            {0x334CFF, 0x576DFF},
            {0x34271E, 0x69503D},
            {0x1C1413, 0x392B28},
            {0x301811, 0x120A07},
            {0x2F2A14, 0xB06B32},
            {0x46241A, 0x7B412F},
            {0x0D5A0D, 0xE6C18D},
            {0x1A2036, 0xB5C0DE},
            //infinity
            {0x484848, 0x323232},
            {0xFFFFFF, 0xFFFFFF},
            {0xFFFFFF, 0xFFFFFF},
    };
    public boolean showInList;
    public Materials material;
    public int chance;
    private String name;
    private CombType(String pName, boolean show, Materials material, int chance) {
        this.name = pName;
        this.material = material;
        this.chance = chance;
        this.showInList = show;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
//		return "gt.comb."+this.name;
        return GT_LanguageManager.addStringLocalization("comb." + this.name, this.name.substring(0, 1).toUpperCase() + this.name.substring(1) + " Comb");
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}