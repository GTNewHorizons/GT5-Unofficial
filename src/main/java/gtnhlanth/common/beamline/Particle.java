package gtnhlanth.common.beamline;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.modularui2.GTGuiTextures;
import gtnhlanth.common.item.ItemParticle;

public enum Particle {

    // restMass is in MeV/c^2
    // maxSourceEnergy is in keV/c^2

    // Gauge Boso, GTGuiTextures.ns
    PHOTON(false, 0, 0, 0, "photon", "\u03B3", 0, null, 1000, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_PHOTON),
    ZBOSON(false, 1, 91188.00f, 0, "zboson", "Z\u2070", 0, null, 15, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_ZBOSON),
    WBOSON(true, 2, 80369.20f, 0, "wboson", "W\u207B", 0, null, 20, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_WBOSON),
    HIGGS(false, 3, 125110.00f, 0, "higgs", "H\u2070", 0, null, 5, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_HIGGS),
    GRAVITON(false, 4, 0, 0, "graviton", "G", 0, null, 50, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_GRAVITON),
    // Leptons
    ELECTRON(true, 5, 0.511f, 5000, "electron", "e\u207B", -1, null, 800,
        GTGuiTextures.OVERLAY_BUTTON_PARTICLE_ELECTRON),
    MUON(true, 6, 105.66f, 0, "muon", "\u03BC \u207B", -1, null, 300, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_MUON),
    TAU(true, 7, 1776.89f, 0, "tau", "\u03C4 \u207B", -1, null, 100, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_TAU),
    ELECTRONNEUTRINO(false, 8, 0, 0, "electron_neutrino", "\u03BD e", 0, null, 250,
        GTGuiTextures.OVERLAY_BUTTON_PARTICLE_ELECTRONNEUTRINO),
    MUONNEUTRINO(false, 9, 0, 0, "muon_neutrino", "\u03BD \u03BC", 0, null, 200,
        GTGuiTextures.OVERLAY_BUTTON_PARTICLE_MUONNEUTRINO),
    TAUNEUTRINO(false, 10, 0, 0, "tau_neutrino", "\u03BD \u03C4", 0, null, 150,
        GTGuiTextures.OVERLAY_BUTTON_PARTICLE_TAUNEUTRINO),
    // Baryons
    PROTON(true, 11, 938.27f, 1000000, "proton", "p\u207A", 1, null, 600, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_PROTON),
    NEUTRON(false, 12, 939.57f, 15000, "neutron", "n\u2070", 0, null, 500,
        GTGuiTextures.OVERLAY_BUTTON_PARTICLE_NEUTRON),
    LAMBDA(false, 13, 1115.68f, 0, "lambda", "\u039B \u2070", 0, null, 80,
        GTGuiTextures.OVERLAY_BUTTON_PARTICLE_LAMBDA),
    OMEGA(true, 14, 1672.45f, 0, "omega", "\u03A9 \u207B", -1, null, 60, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_OMEGA),
    // Mesons
    ETA(false, 15, 547.86f, 0, "eta", "\u03B7", 0, null, 400, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_ETA),
    JPSI(false, 16, 3096.92f, 0, "jpsi", "J/\u03C8", 0, null, 50, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_JPSI),
    UPSILON(false, 17, 9460.3f, 0, "upsilon", "\u03A5", 0, null, 40, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_UPSILON),
    // Nuclei
    ALPHA(true, 18, 3727.38f, 8000, "alpha", "\u03B1", 2, null, 0, GTGuiTextures.OVERLAY_BUTTON_PARTICLE_ALPHA);

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

    private final UITexture texture;

    Particle(boolean canAcc, int id, float restMass, float maxSourceEnergy, String name, String shortName, float charge,
        String chargeSpecial, int lhcWeight, UITexture texture) {
        this.canAcc = canAcc;
        this.id = id;
        this.restMass = restMass;
        this.maxSourceEnergy = maxSourceEnergy;
        this.name = name;
        this.shortName = shortName;
        this.charge = charge;
        this.chargeSpecial = chargeSpecial;
        this.LHCWeight = lhcWeight;
        this.texture = texture;
    }

    public int getId() {
        return this.id;
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

    public final UITexture getTexture() {
        return this.texture;
    }

    public static boolean isParticle(ItemStack itemstack) {
        return (itemstack.getItem() instanceof ItemParticle);
    }

}
