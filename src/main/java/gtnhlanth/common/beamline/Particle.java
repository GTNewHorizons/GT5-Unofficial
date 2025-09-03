package gtnhlanth.common.beamline;

import net.minecraft.util.StatCollector;

public enum Particle {

    // restMass is in MeV/c^2

    // Gauge Bosons
    PHOTON(false, 1, 0, 0, "photon", "\u03B3", 0, null),
    ZBOSON(false, 5, 91188.00f, 0, "zboson", "Z\u2070", 0, null),
    WBOSON(true, 6, 80369.20f, 0, "wboson", "W\u207B", 0, null),
    HIGGS(false, 7, 125110.00f, 0, "higgs", "H\u2070", 0, null),
    GRAVITON(false, 8, 0, 0, "graviton", "G", 0, null),
    // Leptons
    ELECTRON(true, 0, 0.511f, 5000, "electron", "e\u207B", -1, null),
    MUON(true, 9, 105.66f, 0, "muon", "\u03BC \u207B", -1, null),
    TAU(true, 10, 1776.89f, 0, "tau", "\u03C4 \u207B", -1, null),
    ELECTRONNEUTRINO(false, 11, 0f, 0, "electron neutrino", "\u03BD e", 0, null),
    MUONNEUTRINO(false, 12, 0f, 0, "muon neutrino", "\u03BD \u03BC", 0, null),
    TAUNEUTRINO(false, 13, 0f, 0, "tau neutrino", "\u03BD \u03C4", 0, null),
    // Baryons
    PROTON(true, 3, 938.27f, 15000, "proton", "p\u207A", 1, null),
    NEUTRON(false, 2, 939.57f, 15000, "neutron", "n\u2070", 0, null),
    LAMBDA(false, 14, 1115.68f, 0, "lambda", "\u039B \u2070", 0, null),
    OMEGA(true, 15, 1672.45f, 0, "omega", "\u03A9 \u207B", -1, null),
    // Mesons
    ETA(false, 16, 547.86f, 0, "eta", "\u03B7", 0, null),
    JPSI(false, 17, 3096.92f, 0, "j/psi", "J/\u03C8", 0, null),
    UPSILON(false, 18, 9460.3f, 0, "upsilon", "\u03A5", 0, null),
    // Nuclei
    ALPHA(true, 4, 3727.38f, 8000, "alpha", "\u03B1", 2, null);

    // todo: the ids are not matching up. the getParticleFromId method seems to be using a number different than 'id'...

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
