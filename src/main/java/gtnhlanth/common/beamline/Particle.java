package gtnhlanth.common.beamline;

import net.minecraft.util.StatCollector;

public enum Particle {

    // restMass is in MeV/c^2
    // maxSourceEnergy is in keV/c^2

    // Gauge Bosons
    PHOTON(false, 0, 0, 0, "photon",
        "\u03B3", 0, null,1000),
    ZBOSON(false, 1, 91188.00f, 0, "zboson",
        "Z\u2070", 0, null,15),
    WBOSON(true, 2, 80369.20f, 0, "wboson",
        "W\u207B", 0, null,20),
    HIGGS(false, 3, 125110.00f, 0, "higgs",
        "H\u2070", 0, null,5),
    GRAVITON(false, 4, 0, 0, "graviton",
        "G", 0, null,50),
    // Leptons
    ELECTRON(true, 5, 0.511f, 5000, "electron",
        "e\u207B", -1, null,800),
    MUON(true, 6, 105.66f, 0, "muon",
        "\u03BC \u207B", -1, null,300),
    TAU(true, 7, 1776.89f, 0, "tau",
        "\u03C4 \u207B", -1, null,100),
    ELECTRONNEUTRINO(false, 8, 0, 0, "electron neutrino",
        "\u03BD e", 0, null,250),
    MUONNEUTRINO(false, 9, 0, 0, "muon neutrino",
        "\u03BD \u03BC", 0, null,200),
    TAUNEUTRINO(false, 10, 0, 0, "tau neutrino",
        "\u03BD \u03C4", 0, null,150),
    // Baryons
    PROTON(true, 11, 938.27f, 1000000, "proton",
        "p\u207A", 1, null,600),
    NEUTRON(false, 12, 939.57f, 15000, "neutron",
        "n\u2070", 0, null,500),
    LAMBDA(false, 13, 1115.68f, 0, "lambda",
        "\u039B \u2070", 0, null,80),
    OMEGA(true, 14, 1672.45f, 0, "omega",
        "\u03A9 \u207B", -1, null,60),
    // Mesons
    ETA(false, 15, 547.86f, 0, "eta",
        "\u03B7", 0, null,400),
    JPSI(false, 16, 3096.92f, 0, "j/psi",
        "J/\u03C8", 0, null,50),
    UPSILON(false, 17, 9460.3f, 0, "upsilon",
        "\u03A5", 0, null,40),
    // Nuclei
    ALPHA(true, 18, 3727.38f, 8000, "alpha",
        "\u03B1", 2, null,0);
    //todo: quantum anomaly
    //todo: gregs boson

    // The IDs must be in order.

    public static final Particle[] VALUES = values();

    private final boolean canAcc;

    private final float restMass; // in MeV

    private final float maxSourceEnergy; // in keV

    private final String name;
    private final String shortName;

    private final float charge; // in multiples of elemental charge

    private final String chargeSpecial;

    private final int LHCWeight;

    private final int id;

    Particle(boolean canAcc, int id, float restMass, float maxSourceEnergy, String name, String shortName, float charge,
             String chargeSpecial, int lhcWeight) {
        this.canAcc = canAcc;
        this.id = id;
        this.restMass = restMass;
        this.maxSourceEnergy = maxSourceEnergy;
        this.name = name;
        this.shortName = shortName;
        this.charge = charge;
        this.chargeSpecial = chargeSpecial;
        this.LHCWeight = lhcWeight;
    }

    public int getId() { return this.id; }

    public float getMass() {
        return this.restMass;
    }

    public float getCharge() {
        return this.charge;
    }

    public String getChargeSpecial() {
        return this.chargeSpecial;
    }

    public int getLHCWeight() {
        return this.LHCWeight;
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

    private static final Particle[] ParticleArray = values();

    public static Particle getParticleFromId(int id) {
        return ParticleArray[id];
    }
}
