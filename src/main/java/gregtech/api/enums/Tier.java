package gregtech.api.enums;

import static gregtech.api.enums.GT_Values.V;

/**
 * Experimental Class for later
 */
public class Tier {

    public static final Tier[] ELECTRIC = new Tier[] {
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            0,
            V[0],
            1,
            1,
            1,
            Materials.WroughtIron,
            ItemList.Hull_ULV,
            OrePrefixes.cableGt01.get(Materials.Lead),
            OrePrefixes.cableGt04.get(Materials.Lead),
            OrePrefixes.circuit.get(Materials.Primitive),
            OrePrefixes.circuit.get(Materials.Basic)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            1,
            V[1],
            1,
            1,
            1,
            Materials.Steel,
            ItemList.Hull_LV,
            OrePrefixes.cableGt01.get(Materials.Tin),
            OrePrefixes.cableGt04.get(Materials.Tin),
            OrePrefixes.circuit.get(Materials.Basic),
            OrePrefixes.circuit.get(Materials.Good)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            2,
            V[2],
            1,
            1,
            1,
            Materials.Aluminium,
            ItemList.Hull_MV,
            OrePrefixes.cableGt01.get(Materials.AnyCopper),
            OrePrefixes.cableGt04.get(Materials.AnyCopper),
            OrePrefixes.circuit.get(Materials.Good),
            OrePrefixes.circuit.get(Materials.Advanced)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            3,
            V[3],
            1,
            1,
            1,
            Materials.StainlessSteel,
            ItemList.Hull_HV,
            OrePrefixes.cableGt01.get(Materials.Gold),
            OrePrefixes.cableGt04.get(Materials.Gold),
            OrePrefixes.circuit.get(Materials.Advanced),
            OrePrefixes.circuit.get(Materials.Data)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            4,
            V[4],
            1,
            1,
            1,
            Materials.Titanium,
            ItemList.Hull_EV,
            OrePrefixes.cableGt01.get(Materials.Aluminium),
            OrePrefixes.cableGt04.get(Materials.Aluminium),
            OrePrefixes.circuit.get(Materials.Data),
            OrePrefixes.circuit.get(Materials.Elite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            5,
            V[5],
            1,
            1,
            1,
            Materials.TungstenSteel,
            ItemList.Hull_IV,
            OrePrefixes.cableGt01.get(Materials.Platinum),
            OrePrefixes.cableGt04.get(Materials.Platinum),
            OrePrefixes.circuit.get(Materials.Elite),
            OrePrefixes.circuit.get(Materials.Master)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            6,
            V[6],
            1,
            1,
            1,
            Materials.Chrome,
            ItemList.Hull_LuV,
            OrePrefixes.cableGt01.get(Materials.NiobiumTitanium),
            OrePrefixes.cableGt04.get(Materials.NiobiumTitanium),
            OrePrefixes.circuit.get(Materials.Master),
            OrePrefixes.circuit.get(Materials.Ultimate)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            7,
            V[7],
            1,
            1,
            1,
            Materials.Iridium,
            ItemList.Hull_ZPM,
            OrePrefixes.cableGt01.get(Materials.Naquadah),
            OrePrefixes.cableGt04.get(Materials.Naquadah),
            OrePrefixes.circuit.get(Materials.Ultimate),
            OrePrefixes.circuit.get(Materials.SuperconductorUHV)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            8,
            V[8],
            1,
            1,
            1,
            Materials.Osmium,
            ItemList.Hull_UV,
            OrePrefixes.cableGt04.get(Materials.NaquadahAlloy),
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            9,
            V[9],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            10,
            V[10],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            11,
            V[11],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            12,
            V[12],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            13,
            V[13],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            14,
            V[14],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),
        new Tier(
            SubTag.ENERGY_ELECTRICITY,
            15,
            V[15],
            1,
            1,
            1,
            Materials.Neutronium,
            ItemList.Hull_MAX,
            OrePrefixes.wireGt01.get(Materials.SuperconductorUHV),
            OrePrefixes.wireGt04.get(Materials.SuperconductorUHV),
            OrePrefixes.circuit.get(Materials.Infinite),
            OrePrefixes.circuit.get(Materials.Infinite)),

        // READ GT_VALUES CLASS BEFORE YOU START ADDING STUFF TO TIERS 8+ - and probably dont do it in
        // GT but in GTNH core mod - that way we shouldnt need to set the tier class
    }, ROTATIONAL = new Tier[] {
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            1,
            32,
            1,
            1,
            1,
            Materials.Wood,
            OrePrefixes.frameGt.get(Materials.Wood),
            OrePrefixes.stick.get(Materials.Wood),
            OrePrefixes.ingot.get(Materials.Wood),
            OrePrefixes.gearGt.get(Materials.Wood),
            OrePrefixes.gearGt.get(Materials.Stone)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            1,
            32,
            1,
            2,
            2,
            Materials.WoodSealed,
            OrePrefixes.frameGt.get(Materials.WoodSealed),
            OrePrefixes.stick.get(Materials.WoodSealed),
            OrePrefixes.ingot.get(Materials.WoodSealed),
            OrePrefixes.gearGt.get(Materials.WoodSealed),
            OrePrefixes.gearGt.get(Materials.Stone)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            2,
            128,
            1,
            1,
            1,
            Materials.Stone,
            OrePrefixes.frameGt.get(Materials.Stone),
            OrePrefixes.stick.get(Materials.Stone),
            OrePrefixes.ingot.get(Materials.Stone),
            OrePrefixes.gearGt.get(Materials.Stone),
            OrePrefixes.gearGt.get(Materials.Bronze)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            2,
            128,
            1,
            2,
            2,
            Materials.IronWood,
            OrePrefixes.frameGt.get(Materials.IronWood),
            OrePrefixes.stick.get(Materials.IronWood),
            OrePrefixes.ingot.get(Materials.IronWood),
            OrePrefixes.gearGt.get(Materials.IronWood),
            OrePrefixes.gearGt.get(Materials.Bronze)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            3,
            512,
            1,
            1,
            1,
            Materials.Bronze,
            OrePrefixes.frameGt.get(Materials.Bronze),
            OrePrefixes.stick.get(Materials.Bronze),
            OrePrefixes.ingot.get(Materials.Bronze),
            OrePrefixes.gearGt.get(Materials.Bronze),
            OrePrefixes.gearGt.get(Materials.Steel)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            3,
            512,
            1,
            2,
            2,
            Materials.Brass,
            OrePrefixes.frameGt.get(Materials.Brass),
            OrePrefixes.stick.get(Materials.Brass),
            OrePrefixes.ingot.get(Materials.Brass),
            OrePrefixes.gearGt.get(Materials.Brass),
            OrePrefixes.gearGt.get(Materials.Steel)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            4,
            2048,
            1,
            1,
            1,
            Materials.Steel,
            OrePrefixes.frameGt.get(Materials.Steel),
            OrePrefixes.stick.get(Materials.Steel),
            OrePrefixes.ingot.get(Materials.Steel),
            OrePrefixes.gearGt.get(Materials.Steel),
            OrePrefixes.gearGt.get(Materials.TungstenSteel)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            4,
            2048,
            1,
            2,
            2,
            Materials.Titanium,
            OrePrefixes.frameGt.get(Materials.Titanium),
            OrePrefixes.stick.get(Materials.Titanium),
            OrePrefixes.ingot.get(Materials.Titanium),
            OrePrefixes.gearGt.get(Materials.Titanium),
            OrePrefixes.gearGt.get(Materials.TungstenSteel)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            5,
            8192,
            1,
            1,
            1,
            Materials.TungstenSteel,
            OrePrefixes.frameGt.get(Materials.TungstenSteel),
            OrePrefixes.stick.get(Materials.TungstenSteel),
            OrePrefixes.ingot.get(Materials.TungstenSteel),
            OrePrefixes.gearGt.get(Materials.TungstenSteel),
            OrePrefixes.gearGt.get(Materials.Iridium)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            6,
            32768,
            1,
            1,
            1,
            Materials.Iridium,
            OrePrefixes.frameGt.get(Materials.Iridium),
            OrePrefixes.stick.get(Materials.Iridium),
            OrePrefixes.ingot.get(Materials.Iridium),
            OrePrefixes.gearGt.get(Materials.Iridium),
            OrePrefixes.gearGt.get(Materials.Neutronium)),
        new Tier(
            SubTag.ENERGY_ROTATIONAL,
            9,
            Integer.MAX_VALUE - 7,
            1,
            1,
            1,
            Materials.Neutronium,
            OrePrefixes.frameGt.get(Materials.Neutronium),
            OrePrefixes.stick.get(Materials.Neutronium),
            OrePrefixes.ingot.get(Materials.Neutronium),
            OrePrefixes.gearGt.get(Materials.Neutronium),
            OrePrefixes.gearGt.get(Materials.Neutronium)), },
        STEAM = new Tier[] {
            new Tier(
                SubTag.ENERGY_STEAM,
                1,
                32,
                1,
                1,
                1,
                Materials.Bronze,
                OrePrefixes.frameGt.get(Materials.Bronze),
                OrePrefixes.pipeMedium.get(Materials.Bronze),
                OrePrefixes.pipeHuge.get(Materials.Bronze),
                OrePrefixes.pipeMedium.get(Materials.Bronze),
                OrePrefixes.pipeLarge.get(Materials.Bronze)),
            new Tier(
                SubTag.ENERGY_STEAM,
                2,
                128,
                1,
                1,
                1,
                Materials.Steel,
                OrePrefixes.frameGt.get(Materials.Steel),
                OrePrefixes.pipeMedium.get(Materials.Steel),
                OrePrefixes.pipeHuge.get(Materials.Steel),
                OrePrefixes.pipeMedium.get(Materials.Steel),
                OrePrefixes.pipeLarge.get(Materials.Steel)),
            new Tier(
                SubTag.ENERGY_STEAM,
                3,
                512,
                1,
                1,
                1,
                Materials.Titanium,
                OrePrefixes.frameGt.get(Materials.Titanium),
                OrePrefixes.pipeMedium.get(Materials.Titanium),
                OrePrefixes.pipeHuge.get(Materials.Titanium),
                OrePrefixes.pipeMedium.get(Materials.Titanium),
                OrePrefixes.pipeLarge.get(Materials.Titanium)),
            new Tier(
                SubTag.ENERGY_STEAM,
                4,
                2048,
                1,
                1,
                1,
                Materials.TungstenSteel,
                OrePrefixes.frameGt.get(Materials.TungstenSteel),
                OrePrefixes.pipeMedium.get(Materials.TungstenSteel),
                OrePrefixes.pipeHuge.get(Materials.TungstenSteel),
                OrePrefixes.pipeMedium.get(Materials.TungstenSteel),
                OrePrefixes.pipeLarge.get(Materials.TungstenSteel)),
            new Tier(
                SubTag.ENERGY_STEAM,
                5,
                8192,
                1,
                1,
                1,
                Materials.Iridium,
                OrePrefixes.frameGt.get(Materials.Iridium),
                OrePrefixes.pipeMedium.get(Materials.Iridium),
                OrePrefixes.pipeHuge.get(Materials.Iridium),
                OrePrefixes.pipeMedium.get(Materials.Iridium),
                OrePrefixes.pipeLarge.get(Materials.Iridium)),
            new Tier(
                SubTag.ENERGY_STEAM,
                9,
                Integer.MAX_VALUE - 7,
                1,
                1,
                1,
                Materials.Neutronium,
                OrePrefixes.frameGt.get(Materials.Neutronium),
                OrePrefixes.pipeMedium.get(Materials.Neutronium),
                OrePrefixes.pipeHuge.get(Materials.Neutronium),
                OrePrefixes.pipeMedium.get(Materials.Neutronium),
                OrePrefixes.pipeLarge.get(Materials.Neutronium)), };
    /**
     * Used for Crafting Recipes
     */
    public final Object mHullObject, mConductingObject, mLargerConductingObject, mManagingObject, mBetterManagingObject;

    private final SubTag mType;
    private final byte mRank;
    private final long mPrimaryValue, mSecondaryValue, mSpeedMultiplier, mEnergyCostMultiplier;
    private final Materials mMaterial;

    public Tier(SubTag aType, int aRank, long aPrimaryValue, long aSecondaryValue, long aSpeedMultiplier,
        long aEnergyCostMultiplier, Materials aMaterial, Object aHullObject, Object aConductingObject,
        Object aLargerConductingObject, Object aManagingObject, Object aBetterManagingObject) {
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
