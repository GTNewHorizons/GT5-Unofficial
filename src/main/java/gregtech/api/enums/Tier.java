package gregtech.api.enums;

/**
 * Experimental Class for later
 */
public class Tier {
    public static final Tier[]
            ELECTRIC = new Tier[]{
            new Tier(SubTag.ENERGY_ELECTRICITY, 0, 8, 1, 1, 1, Materials.WroughtIron, ItemList.Hull_ULV, OrePrefixes.cableGt01.get(Materials.Lead), OrePrefixes.cableGt04.get(Materials.Lead), OrePrefixes.circuit.get(Materials.Primitive), OrePrefixes.circuit.get(Materials.Basic)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 1, 32, 1, 1, 1, Materials.Steel, ItemList.Hull_LV, OrePrefixes.cableGt01.get(Materials.Tin), OrePrefixes.cableGt04.get(Materials.Tin), OrePrefixes.circuit.get(Materials.Basic), OrePrefixes.circuit.get(Materials.Good)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 2, 128, 1, 1, 1, Materials.Aluminium, ItemList.Hull_MV, OrePrefixes.cableGt01.get(Materials.Copper), OrePrefixes.cableGt04.get(Materials.Copper), OrePrefixes.circuit.get(Materials.Good), OrePrefixes.circuit.get(Materials.Advanced)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 3, 512, 1, 1, 1, Materials.StainlessSteel, ItemList.Hull_HV, OrePrefixes.cableGt01.get(Materials.Gold), OrePrefixes.cableGt04.get(Materials.Gold), OrePrefixes.circuit.get(Materials.Advanced), OrePrefixes.circuit.get(Materials.Data)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 4, 2048, 1, 1, 1, Materials.Titanium, ItemList.Hull_EV, OrePrefixes.cableGt01.get(Materials.Aluminium), OrePrefixes.cableGt04.get(Materials.Aluminium), OrePrefixes.circuit.get(Materials.Data), OrePrefixes.circuit.get(Materials.Elite)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 5, 8192, 1, 1, 1, Materials.TungstenSteel, ItemList.Hull_IV, OrePrefixes.cableGt01.get(Materials.Platinum), OrePrefixes.cableGt04.get(Materials.Platinum), OrePrefixes.circuit.get(Materials.Elite), OrePrefixes.circuit.get(Materials.Master)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 6, 32768, 1, 1, 1, Materials.Chrome, ItemList.Hull_LuV, OrePrefixes.cableGt01.get(Materials.NiobiumTitanium), OrePrefixes.cableGt04.get(Materials.NiobiumTitanium), OrePrefixes.circuit.get(Materials.Master), OrePrefixes.circuit.get(Materials.Ultimate)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 7, 131072, 1, 1, 1, Materials.Iridium, ItemList.Hull_ZPM, OrePrefixes.cableGt01.get(Materials.Naquadah), OrePrefixes.cableGt04.get(Materials.Naquadah), OrePrefixes.circuit.get(Materials.Ultimate), OrePrefixes.circuit.get(Materials.Superconductor)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 8, 524288, 1, 1, 1, Materials.Osmium, ItemList.Hull_UV, OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), OrePrefixes.cableGt01.get(Materials.ElectrumFlux), OrePrefixes.circuit.get(Materials.Superconductor), OrePrefixes.circuit.get(Materials.Infinite)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 9, 2097152, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.Europium), OrePrefixes.wireGt04.get(Materials.Europium), OrePrefixes.circuit.get(Materials.Infinite), OrePrefixes.circuit.get(Materials.Bio)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 10, 8388608, 1, 1, 1, Materials.Phoenixite, ItemList.Hull_UEV, OrePrefixes.wireGt01.get(Materials.Diamericiumtitanium), OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium), OrePrefixes.circuit.get(Materials.Bio), OrePrefixes.circuit.get(Materials.Nano)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 11, 33554432, 1, 1, 1, Materials.BlackPlutonium, ItemList.Hull_UIV, OrePrefixes.wireGt01.get(Materials.Neutronium), OrePrefixes.wireGt04.get(Materials.Neutronium), OrePrefixes.circuit.get(Materials.Nano), OrePrefixes.circuit.get(Materials.Piko)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 12, 134217728, 1, 1, 1, Materials.Neutronium, ItemList.Hull_UMV, OrePrefixes.wireGt01.get(Materials.Quantium), OrePrefixes.wireGt04.get(Materials.Quantium), OrePrefixes.circuit.get(Materials.Piko), OrePrefixes.circuit.get(Materials.Quantum)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 13, 536870912, 1, 1, 1, Materials.Neutronium, ItemList.Hull_UXV, OrePrefixes.wireGt01.get(Materials.BlackPlutonium), OrePrefixes.wireGt04.get(Materials.BlackPlutonium), OrePrefixes.circuit.get(Materials.Quantum), OrePrefixes.circuit.get(Materials.Quantum)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 14, 1073741824, 1, 1, 1, Materials.Neutronium, ItemList.Hull_OPV, OrePrefixes.wireGt01.get(Materials.Superconductor), OrePrefixes.wireGt04.get(Materials.Superconductor), OrePrefixes.circuit.get(Materials.Quantum), OrePrefixes.circuit.get(Materials.Quantum)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 15, Integer.MAX_VALUE, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAXV, OrePrefixes.wireGt01.get(Materials.Superconductor), OrePrefixes.wireGt04.get(Materials.Superconductor), OrePrefixes.circuit.get(Materials.Quantum), OrePrefixes.circuit.get(Materials.Quantum)),
            
    }, ROTATIONAL = new Tier[]{
            new Tier(SubTag.ENERGY_ROTATIONAL, 1, 32, 1, 1, 1, Materials.Wood, OrePrefixes.frameGt.get(Materials.Wood), OrePrefixes.stick.get(Materials.Wood), OrePrefixes.ingot.get(Materials.Wood), OrePrefixes.gearGt.get(Materials.Wood), OrePrefixes.gearGt.get(Materials.Stone)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 1, 32, 1, 2, 2, Materials.WoodSealed, OrePrefixes.frameGt.get(Materials.WoodSealed), OrePrefixes.stick.get(Materials.WoodSealed), OrePrefixes.ingot.get(Materials.WoodSealed), OrePrefixes.gearGt.get(Materials.WoodSealed), OrePrefixes.gearGt.get(Materials.Stone)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 2, 128, 1, 1, 1, Materials.Stone, OrePrefixes.frameGt.get(Materials.Stone), OrePrefixes.stick.get(Materials.Stone), OrePrefixes.ingot.get(Materials.Stone), OrePrefixes.gearGt.get(Materials.Stone), OrePrefixes.gearGt.get(Materials.Bronze)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 2, 128, 1, 2, 2, Materials.IronWood, OrePrefixes.frameGt.get(Materials.IronWood), OrePrefixes.stick.get(Materials.IronWood), OrePrefixes.ingot.get(Materials.IronWood), OrePrefixes.gearGt.get(Materials.IronWood), OrePrefixes.gearGt.get(Materials.Bronze)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 3, 512, 1, 1, 1, Materials.Bronze, OrePrefixes.frameGt.get(Materials.Bronze), OrePrefixes.stick.get(Materials.Bronze), OrePrefixes.ingot.get(Materials.Bronze), OrePrefixes.gearGt.get(Materials.Bronze), OrePrefixes.gearGt.get(Materials.Steel)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 3, 512, 1, 2, 2, Materials.Brass, OrePrefixes.frameGt.get(Materials.Brass), OrePrefixes.stick.get(Materials.Brass), OrePrefixes.ingot.get(Materials.Brass), OrePrefixes.gearGt.get(Materials.Brass), OrePrefixes.gearGt.get(Materials.Steel)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 4, 2048, 1, 1, 1, Materials.Steel, OrePrefixes.frameGt.get(Materials.Steel), OrePrefixes.stick.get(Materials.Steel), OrePrefixes.ingot.get(Materials.Steel), OrePrefixes.gearGt.get(Materials.Steel), OrePrefixes.gearGt.get(Materials.Titanium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 4, 2048, 1, 2, 2, Materials.Titanium, OrePrefixes.frameGt.get(Materials.Titanium), OrePrefixes.stick.get(Materials.Titanium), OrePrefixes.ingot.get(Materials.Titanium), OrePrefixes.gearGt.get(Materials.Titanium), OrePrefixes.gearGt.get(Materials.TungstenSteel)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 5, 8192, 1, 1, 1, Materials.TungstenSteel, OrePrefixes.frameGt.get(Materials.TungstenSteel), OrePrefixes.stick.get(Materials.TungstenSteel), OrePrefixes.ingot.get(Materials.TungstenSteel), OrePrefixes.gearGt.get(Materials.TungstenSteel), OrePrefixes.gearGt.get(Materials.Iridium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 6, 32768, 1, 2, 2, Materials.Iridium, OrePrefixes.frameGt.get(Materials.Iridium), OrePrefixes.stick.get(Materials.Iridium), OrePrefixes.ingot.get(Materials.Iridium), OrePrefixes.gearGt.get(Materials.Iridium), OrePrefixes.gearGt.get(Materials.Osmium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 7, 131072, 1, 1, 1, Materials.Osmium, OrePrefixes.frameGt.get(Materials.Osmium), OrePrefixes.stick.get(Materials.Osmium), OrePrefixes.ingot.get(Materials.Osmium), OrePrefixes.gearGt.get(Materials.Osmium), OrePrefixes.gearGt.get(Materials.Europium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 8, 524288, 1, 2, 2, Materials.Europium, OrePrefixes.frameGt.get(Materials.Europium), OrePrefixes.stick.get(Materials.Europium), OrePrefixes.ingot.get(Materials.Europium), OrePrefixes.gearGt.get(Materials.Europium), OrePrefixes.gearGt.get(Materials.Neutronium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 9, 2097152, 1, 1, 1, Materials.Neutronium, OrePrefixes.frameGt.get(Materials.Neutronium), OrePrefixes.stick.get(Materials.Neutronium), OrePrefixes.ingot.get(Materials.Neutronium), OrePrefixes.gearGt.get(Materials.Neutronium), OrePrefixes.gearGt.get(Materials.Phoenixite)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 10, 8388608, 1, 2, 2, Materials.Phoenixite, OrePrefixes.frameGt.get(Materials.Phoenixite), OrePrefixes.stick.get(Materials.Phoenixite), OrePrefixes.ingot.get(Materials.Phoenixite), OrePrefixes.gearGt.get(Materials.Phoenixite), OrePrefixes.gearGt.get(Materials.BlackPlutonium)),
           
    }, STEAM = new Tier[]{
            new Tier(SubTag.ENERGY_STEAM, 1, 32, 1, 1, 1, Materials.Bronze, OrePrefixes.frameGt.get(Materials.Bronze), OrePrefixes.pipeMedium.get(Materials.Bronze), OrePrefixes.pipeHuge.get(Materials.Bronze), OrePrefixes.pipeMedium.get(Materials.Bronze), OrePrefixes.pipeLarge.get(Materials.Bronze)),
            new Tier(SubTag.ENERGY_STEAM, 2, 128, 1, 1, 1, Materials.Steel, OrePrefixes.frameGt.get(Materials.Steel), OrePrefixes.pipeMedium.get(Materials.Steel), OrePrefixes.pipeHuge.get(Materials.Steel), OrePrefixes.pipeMedium.get(Materials.Steel), OrePrefixes.pipeLarge.get(Materials.Steel)),
            new Tier(SubTag.ENERGY_STEAM, 3, 512, 1, 1, 1, Materials.Titanium, OrePrefixes.frameGt.get(Materials.Titanium), OrePrefixes.pipeMedium.get(Materials.Titanium), OrePrefixes.pipeHuge.get(Materials.Titanium), OrePrefixes.pipeMedium.get(Materials.Titanium), OrePrefixes.pipeLarge.get(Materials.Titanium)),
            new Tier(SubTag.ENERGY_STEAM, 4, 2048, 1, 1, 1, Materials.TungstenSteel, OrePrefixes.frameGt.get(Materials.TungstenSteel), OrePrefixes.pipeMedium.get(Materials.TungstenSteel), OrePrefixes.pipeHuge.get(Materials.TungstenSteel), OrePrefixes.pipeMedium.get(Materials.TungstenSteel), OrePrefixes.pipeLarge.get(Materials.TungstenSteel)),
            new Tier(SubTag.ENERGY_STEAM, 5, 8192, 1, 1, 1, Materials.Iridium, OrePrefixes.frameGt.get(Materials.Iridium), OrePrefixes.pipeMedium.get(Materials.Iridium), OrePrefixes.pipeHuge.get(Materials.Iridium), OrePrefixes.pipeMedium.get(Materials.Iridium), OrePrefixes.pipeLarge.get(Materials.Iridium)),
            new Tier(SubTag.ENERGY_STEAM, 9, 2097152, 1, 1, 1, Materials.Neutronium, OrePrefixes.frameGt.get(Materials.Neutronium), OrePrefixes.pipeMedium.get(Materials.Neutronium), OrePrefixes.pipeHuge.get(Materials.Neutronium), OrePrefixes.pipeMedium.get(Materials.Neutronium), OrePrefixes.pipeLarge.get(Materials.Neutronium)),
            
    };
    /**
     * Used for Crafting Recipes
     */
    public final Object mHullObject, mConductingObject, mLargerConductingObject, mManagingObject, mBetterManagingObject;
    private final SubTag mType;
    private final byte mRank;
    private final long mPrimaryValue, mSecondaryValue, mSpeedMultiplier, mEnergyCostMultiplier;
    private final Materials mMaterial;

    public Tier(SubTag aType, int aRank, long aPrimaryValue, long aSecondaryValue, long aSpeedMultiplier, long aEnergyCostMultiplier, Materials aMaterial, Object aHullObject, Object aConductingObject, Object aLargerConductingObject, Object aManagingObject, Object aBetterManagingObject) {
        mType = aType;
        mRank = (byte) aRank;
        mPrimaryValue = aPrimaryValue;
        mSecondaryValue = aSecondaryValue;
        mSpeedMultiplier = aSpeedMultiplier;
        mEnergyCostMultiplier = Math.max(mSpeedMultiplier, aEnergyCostMultiplier);
        mMaterial = aMaterial;

        mHullObject = aHullObject;
        mConductingObject = aConductingObject;
        mManagingObject = aManagingObject;
        mBetterManagingObject = aBetterManagingObject;
        mLargerConductingObject = aLargerConductingObject;
    }

    public byte getRank() {
        return mRank;
    }

    public SubTag getEnergyType() {
        return mType;
    }

    public long getEnergyPrimary() {
        return mPrimaryValue;
    }

    public long getEnergySecondary() {
        return mSecondaryValue;
    }

    public long getSpeedMultiplier() {
        return mSpeedMultiplier;
    }

    public long getEnergyCostMultiplier() {
        return mEnergyCostMultiplier;
    }

    public Materials getMaterial() {
        return mMaterial;
    }
}
