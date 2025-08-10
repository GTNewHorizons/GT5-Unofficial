package gregtech.api.enums;

public enum UndergroundFluidNames {

    carbonDioxide("carbondioxide", 0x696969),
    carbonMonoxide("carbonmonoxide", 0x104E8B),
    chlorobenzene("chlorobenzene", 0x408040),
    deuterium("deuterium", 0xFFE39F),
    distilledWater("ic2distilledwater", 0x1E90FF),
    ethane("ethane", 0x408020),
    ethylene("ethylene", 0xd0d0d0),
    fluorine("fluorine", 0x99C1AD),
    heavyOil("liquid_heavy_oil", 0x0A0A0A),
    helium3("helium-3", 0x8020e0),
    hydrofluoricAcid("hydrofluoricacid_gt5u", 0x00CED1),
    hydrogen("hydrogen", 0x3232D6),
    hydrogenSulfide("liquid_hydricsulfur", -1),
    lava("lava", 0xFF0000),
    lightOil("liquid_light_oil", 0xFFFF00),
    liquidAir("liquidair", 0x9999EA),
    mediumOil("liquid_medium_oil", 0x00FF00),
    methane("methane", 0x802020),
    moltenCopper("molten.copper", 0xFF7F24),
    moltenIron("molten.iron", 0x8B8878),
    moltenLead("molten.lead", 0xd0d0d0),
    moltenTin("molten.tin", 0xE7E7E4),
    naturalGas("gas_natural_gas", 0x00FFFF),
    nitrogen("nitrogen", 0x0080d0),
    oil("oil", 0x000000),
    oxygen("oxygen", 0x4040A0),
    saltWater("saltwater", 0x80FF80),
    sulfuricAcid("sulfuricacid", 0xFFB90F),
    unknownWater("unknowwater", 0x8A2BE2),
    veryHeavyOil("liquid_extra_heavy_oil", 0x000050);

    public final String name;
    // color override used for detrav ore scanner
    public final int colorRGB;

    UndergroundFluidNames(String name, int colorRGB) {
        this.name = name;
        this.colorRGB = colorRGB;
    }
}
