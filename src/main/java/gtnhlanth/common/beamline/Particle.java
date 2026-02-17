package gtnhlanth.common.beamline;

import net.minecraft.util.StatCollector;

public enum Particle {

    ELECTRON(true, 0, 0.511f, 5000, "Electron", "e\u207B", -1, null),
    PHOTON(false, 1, 0, 0, "Photon", "\u03B3", 0, null),
    NEUTRON(false, 2, 939.57f, 15000, "Neutron", "n\u2070", 0, null),
    PROTON(true, 3, 938.27f, 15000, "Proton", "p\u207A", 1, null),
    ALPHA(true, 4, 3727.38f, 8000, "Alpha", "\u03B1", 2, null);

    public static final Particle[] VALUES = values();

    private final boolean canAcc;

    private final float restMass; // in MeV

    private final float maxSourceEnergy; // in keV

    private final String name;
    private final String shortName;

    private final float charge; // in multiples of elemental charge

    private final String chargeSpecial;

    Particle(boolean canAcc, int id, float restMass, float maxSourceEnergy, String name, String shortName, float charge,
        String chargeSpecial) { // ID
        // is
        // symbolic
        // only
        this.canAcc = canAcc;
        this.restMass = restMass;
        this.maxSourceEnergy = maxSourceEnergy;
        this.name = name;
        this.shortName = shortName;
        this.charge = charge;
        this.chargeSpecial = chargeSpecial;
    }

    public float getMass() {
        return this.restMass;
    }

    public float getCharge() {
        return this.charge;
    }

    public String getChargeSpecial() {
        return this.chargeSpecial;
    }

    public boolean canAccelerate() {
        return this.canAcc;
    }

    public float maxSourceEnergy() {
        return this.maxSourceEnergy;
    }

    public String getName() {
        return this.name;
    }

    public String getLocalisedName() {
        return StatCollector.translateToLocal("particle." + this.name) + " (" + this.shortName + ")";
    }

    public static Particle getParticleFromId(int id) {
        return Particle.values()[id];
    }
}
