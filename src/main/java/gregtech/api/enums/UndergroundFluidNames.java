package gregtech.api.enums;

public enum UndergroundFluidNames {

    carbonDioxide("carbondioxide", new short[] { 0x69, 0x69, 0x69 }),
    carbonMonoxide("carbonmonoxide", new short[] { 0x10, 0x4E, 0x8B }),
    chlorobenzene("chlorobenzene", new short[] { 0x40, 0x80, 0x40 }),
    deuterium("deuterium", new short[] { 0xFF, 0xE3, 0x9F }),
    distilledWater("ic2distilledwater", new short[] { 0x1E, 0x90, 0xFF }),
    ethane("ethane", new short[] { 0x40, 0x80, 0x20 }),
    ethylene("ethylene", new short[] { 0xd0, 0xd0, 0xd0 }),
    fluorine("fluorine", new short[] { 0x99, 0xC1, 0xAD }),
    heavyOil("liquid_heavy_oil", new short[] { 10, 10, 10 }),
    helium3("helium-3", new short[] { 0x80, 0x20, 0xe0 }),
    hydrofluoricAcid("hydrofluoricacid_gt5u", new short[] { 0x00, 0xCE, 0xD1 }),
    hydrogen("hydrogen", new short[] { 0x32, 0x32, 0xD6 }),
    hydrogenSulfide("liquid_hydricsulfur", null),
    lava("lava", new short[] { 0xFF, 0x00, 0x00 }),
    lightOil("liquid_light_oil", new short[] { 0xff, 0xff, 0x00 }),
    liquidAir("liquidair", new short[] { 0x99, 0x99, 0xEA }),
    mediumOil("liquid_medium_oil", new short[] { 0x00, 0xFF, 0x00 }),
    methane("methane", new short[] { 0x80, 0x20, 0x20 }),
    moltenCopper("molten.copper", new short[] { 0xFF, 0x7F, 0x24 }),
    moltenIron("molten.iron", new short[] { 0x8B, 0x88, 0x78 }),
    moltenLead("molten.lead", new short[] { 0xd0, 0xd0, 0xd0 }),
    moltenTin("molten.tin", new short[] { 0xE7, 0xE7, 0xE4 }),
    naturalGas("gas_natural_gas", new short[] { 0x00, 0xff, 0xff }),
    nitrogen("nitrogen", new short[] { 0x00, 0x80, 0xd0 }),
    oil("oil", new short[] { 0x00, 0x00, 0x00 }),
    oxygen("oxygen", new short[] { 0x40, 0x40, 0xA0 }),
    saltWater("saltwater", new short[] { 0x80, 0xff, 0x80 }),
    sulfuricAcid("sulfuricacid", new short[] { 0xFF, 0xB9, 0x0F }),
    unknownWater("unknowwater", new short[] { 0x8A, 0x2B, 0xE2 }),
    veryHeavyOil("liquid_extra_heavy_oil", new short[] { 0x00, 0x00, 0x50 });

    public final String name;
    // color override used for detrav ore scanner
    public final short[] renderColor;

    UndergroundFluidNames(String name, short[] renderColor) {
        this.name = name;
        this.renderColor = renderColor;
    }
}
