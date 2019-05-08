package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

public enum CombType {
    //Organic
    LIGNIE("lignite", true, Materials.Lignite, 100),
    COAL("coal", true, Materials.Coal, 100),
    STICKY("stickyresin", true, Materials._NULL, 50),
    OIL("oil", true, Materials._NULL, 100),
    
    //Gem Line
    STONE("stone", true, Materials._NULL, 70),
    CERTUS("certus", true, Materials.CertusQuartz, 100),
    REDSTONE("redstone", true, Materials.Redstone, 100),
    LAPIS("lapis", true, Materials.Lapis, 100),
    RUBY("ruby", true, Materials.Ruby, 100),
    SAPPHIRE("sapphire", true, Materials.Sapphire, 100),
    DIAMOND("diamond", true, Materials.Diamond, 100),
    OLIVINE("olivine", true, Materials.Olivine, 100),
    EMERALD("emerald", true, Materials.Emerald, 100),

    // Metals Line
    SLAG("slag", true, Materials._NULL, 50),
    COPPER("coppon", true, Materials.Copper, 100),
    TIN("tine", true, Materials.Tin, 100),
    LEAD("plumbilia", true, Materials.Lead, 100),
    IRON("ferru", true, Materials.Iron, 100),
    STEEL("steeldust", true, Materials.Steel, 100),
    NICKEL("nickeldust", true, Materials.Nickel, 100),
    ZINC("galvania", true, Materials.Zinc, 100),
    SILVER("argentia", true, Materials.Silver, 100),
    GOLD("aurelia", true, Materials.Gold, 100),

    // Rare Metals Line    
    ALUMINIUM("bauxia", true, Materials.Aluminium, 60),
    MANGANESE("pyrolusium", true, Materials.Manganese, 30),
    TITANIUM("titanium", true, Materials.Ilmenite, 100),
    CHROME("chromium", true, Materials.Chrome, 50),
    TUNGSTEN("scheelinium", true, Materials.Tungstate, 100),
    PLATINUM("platina", true, Materials.Platinum, 40),
    IRIDIUM("quantaria", true, Materials.Iridium, 20),

    // Radioactive Line
    URANIUM("urania", true, Materials.Uranium, 50),
    PLUTONIUM("plutonium", true, Materials.Plutonium, 10),
    NAQUADAH("stargatium", true, Materials.Naquadah, 10),
    
    // Organic 2
    APATITE("apatite", true, Materials.Apatite, 100),
    ASH("ash", true, Materials.Ash, 100),
    
    //IC2 Line
    COOLANT("coolant", true, Materials._NULL, 100),
    ENERGY("energy", true, Materials._NULL, 80),
    LAPOTRON("lapotron", true, Materials._NULL, 60),
    
    //Alloy Line
    REDALLOY("redalloy", true, Materials.RedAlloy, 100),
    REDSTONEALLOY("redstonealloy", true, Materials.RedstoneAlloy, 90),
    CONDUCTIVEIRON("conductiveiron", true, Materials.ConductiveIron, 80),
    VIBRANTALLOY("vibrantalloy", true, Materials.VibrantAlloy, 50),
    ENERGETICALLOY("energeticalloy", true, Materials.EnergeticAlloy, 70),
    ELECTRICALSTEEL("electricalsteel", true, Materials.ElectricalSteel, 90),
    DARKSTEEL("darksteel", true, Materials.DarkSteel, 80),
    PULSATINGIRON("pulsatingiron", true, Materials.PulsatingIron, 80),
    STAINLESSSTEEL("stainlesssteel", true, Materials.StainlessSteel, 75),
    ENDERIUM("enderium", true, Materials.Enderium, 40),

    //Gem Line 2
    FLUIX("fluix", true, Materials.Fluix, 100),
    RAREEARTH("rareearth", true, Materials.RareEarth, 100),
    REDGARNET("redgarnet", true, Materials.GarnetRed,100),
    YELLOWGARNET("yellowgarnet", true, Materials.GarnetYellow,100),
    PYROPE("pyrope", true, Materials.Pyrope, 100),
    GROSSULAR("grossular", true, Materials.Grossular, 100),
    
    //Metal Line 2
    SULFUR("sulfur", true, Materials.Sulfur, 100),
    GALLIUM ("gallium", true, Materials.Gallium, 75),
    ARSENIC ("arsenic", true, Materials.Arsenic, 75),
    
    //Rare Material Line 2
    MOLYBDENUM("molybdenum", true, Materials.Molybdenum, 20),
    OSMIUM("osmium", true, Materials.Osmium, 15),
    LITHIUM("lithium", true, Materials.Lithium, 75),
    SALT("salt", true, Materials.Salt, 90),
    ALMANDINE("almandine", true, Materials.Almandine, 85),
    
    //Radioactive Line 2
    NAQUADRIA("naquadria", true, Materials.Naquadria, 5),
    THORIUM("thorium", true, Materials.Thorium, 75),
    LUTETIUM("lutetium", true, Materials.Lutetium, 10),
    AMERICUM("americum", true, Materials.Americium, 5),
    NEUTRONIUM("neutronium", true, Materials.Neutronium, 2),
    
    //Space Line
    SPACE("space", true, Materials._NULL, 100),
    METEORICIRON("meteoriciron",true, Materials.MeteoricIron, 100),
    DESH("desh",true, Materials.Desh, 90),
    LEDOX("ledox",true, Materials.Ledox, 75),
    QUANTIUM("quantium",true, Materials.Quantium, 50),
    ORIHARUKON("oriharukon",true, Materials.Oriharukon, 50),
    MYSTERIOUSCRYSTAL("mysteriouscrystal",true, Materials.MysteriousCrystal, 45),
    BLACKPLUTONIUM("blackplutonium",true, Materials.BlackPlutonium, 25),
    
    //Planet Line
    MOON("moon",true, Materials._NULL, 90),
    MARS("mars",true, Materials._NULL, 80),
    ASTEROID("asteroid",true, Materials._NULL, 70)
    
    ;

    private static int[][] colours = new int[][]{
    	  //Organic
            {0x906237, 0x58300B},
            {0x666666, 0x525252},
            {0x2E8F5B, 0xDCC289},
            {0x4C4C4C, 0x333333},
          //Gem Line
            {0x808080, 0x999999},
            {0x57CFFB, 0xBBEEFF},
            {0x7D0F0F, 0xD11919},
            {0x1947D1, 0x476CDA},
            {0xE6005C, 0xCC0052},
            {0x0033CC, 0x00248F},
            {0xCCFFFF, 0xA3CCCC},
            {0x248F24, 0xCCFFCC},
            {0x248F24, 0x2EB82E},
         // Metals Line
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
         // Rare Metals Line
            {0x008AB8, 0xD6D6FF},
            {0xD5D5D5, 0xAAAAAA},
            {0xCC99FF, 0xDBB8FF},
            {0xEBA1EB, 0xF2C3F2},
            {0x62626D, 0x161620},
            {0xE6E6E6, 0xFFFFCC},
            {0xDADADA, 0xD1D1E0},
         // Radioactive Line
            {0x19AF19, 0x169E16},
            {0x335C33, 0x6B8F00},
            {0x003300, 0x002400},
         // Organic 2
            {0xc1c1f6, 0x676784},
            {0x1e1a18, 0xc6c6c6},
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
          //gemline 2
            {0xA375FF, 0xB591FF},
            {0x555643, 0x343428},
            {0xBD4C4C, 0xECCECE},
            {0xA3A341, 0xEDEDCE},
            {0x763162, 0x8B8B8B},
            {0x9B4E00, 0x8B8B8B},
          //metalline 2
            {0x6F6F01, 0x8B8B8B},
            {0x8B8B8B, 0xC5C5E4},
            {0x736C52, 0x292412},
          //rarematerialline 2
            {0xAEAED4, 0x8B8B8B},
            {0x2B2BDA, 0x8B8B8B},
            {0xF0328C, 0xE1DCFF},
            {0xF0C8C8, 0xFAFAFA},
            {0xC60000, 0x8B8B8B},
          //radioactive line 2
            {0x000000, 0x002400},
            {0x001E00, 0x005000},
            {0xE6FFE6, 0xFFFFFF},
            {0xE6E6FF, 0xC8C8C8},
            {0xFFF0F0, 0xFAFAFA},
          //space line
            {0x003366, 0xC0C0C0},
            {0x321928, 0x643250},
            {0x282828, 0x323232},
            {0x0000CD, 0x0074FF},
            {0x00FF00, 0x00D10B},
            {0x228B22, 0x677D68},
            {0x3CB371, 0x16856C},
            {0x000000, 0x323232},
          //planet line
            {0x373735, 0x7E7E78},
            {0x220D05, 0x3A1505},
            {0x000000, 0x323232},
            
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