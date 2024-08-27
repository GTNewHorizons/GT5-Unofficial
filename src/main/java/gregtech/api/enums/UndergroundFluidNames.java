package gregtech.api.enums;

public enum UndergroundFluidNames {

    carbonDioxide("carbondioxide"),
    carbonMonoxide("carbonmonoxide"),
    chlorobenzene("chlorobenzene"),
    deuterium("deuterium"),
    distilledWater("ic2distilledwater"),
    ethane("ethane"),
    ethylene("ethylene"),
    fluorine("fluorine"),
    heavyOil("liquid_heavy_oil"),
    helium3("helium-3"),
    hydrofluoricAcid("hydrofluoricacid_gt5u"),
    hydrogen("hydrogen"),
    hydrogenSulfide("liquid_hydricsulfur"),
    lava("lava"),
    lightOil("liquid_light_oil"),
    liquidAir("liquidair"),
    mediumOil("liquid_medium_oil"),
    methane("methane"),
    moltenCopper("molten.copper"),
    moltenIron("molten.iron"),
    moltenLead("molten.lead"),
    moltenTin("molten.tin"),
    naturalGas("gas_natural_gas"),
    nitrogen("nitrogen"),
    oil("oil"),
    oxygen("oxygen"),
    saltWater("saltwater"),
    sulfuricAcid("sulfuricacid"),
    unknownWater("unknowwater"),
    veryHeavyOil("liquid_extra_heavy_oil"),

    ;

    public final String name;

    private UndergroundFluidNames(String name) {
        this.name = name;
    }
}
