package gtPlusPlus.core.material;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class Particle {

    public static final Particle GRAVITON;

    public static final Particle UP;
    public static final Particle DOWN;
    public static final Particle CHARM;
    public static final Particle STRANGE;
    public static final Particle TOP;
    public static final Particle BOTTOM;

    public static final Particle ELECTRON;
    public static final Particle ELECTRON_NEUTRINO;
    public static final Particle MUON;
    public static final Particle MUON_NEUTRINO;
    public static final Particle TAU;
    public static final Particle TAU_NEUTRINO;

    public static final Particle GLUON;
    public static final Particle PHOTON;
    public static final Particle Z_BOSON;
    public static final Particle W_BOSON;
    public static final Particle HIGGS_BOSON;

    public static final Particle PROTON;
    public static final Particle NEUTRON;
    public static final Particle LAMBDA;
    public static final Particle OMEGA;

    public static final Particle PION;
    public static final Particle ETA_MESON;

    public static final Particle UNKNOWN;

    public static final ArrayList<Particle> aMap = new ArrayList<>();

    static {

        /*
         * Standard Model of Physics
         */

        // I exist, because I must.
        GRAVITON = new Particle(ElementaryGroup.BOSON, "Graviton");

        // Quarks
        UP = new Particle(ElementaryGroup.QUARK, "Up");
        DOWN = new Particle(ElementaryGroup.QUARK, "Down");
        CHARM = new Particle(ElementaryGroup.QUARK, "Charm");
        STRANGE = new Particle(ElementaryGroup.QUARK, "Strange");
        TOP = new Particle(ElementaryGroup.QUARK, "Top");
        BOTTOM = new Particle(ElementaryGroup.QUARK, "Bottom");

        // Leptons
        ELECTRON = new Particle(ElementaryGroup.LEPTON, "Electron");
        MUON = new Particle(ElementaryGroup.LEPTON, "Muon");
        TAU = new Particle(ElementaryGroup.LEPTON, "Tau");
        ELECTRON_NEUTRINO = new Particle(ElementaryGroup.LEPTON, "Electron Neutrino");
        MUON_NEUTRINO = new Particle(ElementaryGroup.LEPTON, "Muon Neutrino");
        TAU_NEUTRINO = new Particle(ElementaryGroup.LEPTON, "Tau Neutrino");

        // Bosons
        GLUON = new Particle(ElementaryGroup.BOSON, "Gluon");
        PHOTON = new Particle(ElementaryGroup.BOSON, "Photon");
        Z_BOSON = new Particle(ElementaryGroup.BOSON, "Z Boson");
        W_BOSON = new Particle(ElementaryGroup.BOSON, "W Boson");
        HIGGS_BOSON = new Particle(ElementaryGroup.BOSON, "Higgs Boson");

        /*
         * Composite Particles
         */

        // Baryons
        PROTON = new Particle(ElementaryGroup.BARYON, "Proton", new Particle[] { UP, UP, DOWN });
        NEUTRON = new Particle(ElementaryGroup.BARYON, "Neutron", new Particle[] { UP, DOWN, DOWN });
        LAMBDA = new Particle(ElementaryGroup.BARYON, "Lambda", new Particle[] { UP, DOWN, STRANGE });
        OMEGA = new Particle(ElementaryGroup.BARYON, "Omega", new Particle[] { STRANGE, STRANGE, STRANGE });

        // Mesons
        PION = new Particle(ElementaryGroup.MESON, "Pion", new Particle[] { MUON, MUON_NEUTRINO });
        ETA_MESON = new Particle(ElementaryGroup.MESON, "ETA Meson", new Particle[] { PION, PION, PION });

        // Wildcard
        UNKNOWN = new Particle(ElementaryGroup.UNKNOWN, "Unknown");
    }

    public static enum ElementaryGroup {
        QUARK,
        LEPTON,
        BOSON,
        BARYON,
        MESON,
        UNKNOWN;
    }

    public final ElementaryGroup mParticleType;
    public final String mParticleName;
    public final Particle[] mComposition;

    public Particle(ElementaryGroup aParticleType, String aParticleName) {
        this(aParticleType, aParticleName, null);
    }

    public Particle(ElementaryGroup aParticleType, String aParticleName, Particle[] aComposition) {
        mParticleType = aParticleType;
        mParticleName = aParticleName;
        mComposition = aComposition == null ? new Particle[] { this } : aComposition;
        aMap.add(this);
    }

    public static ItemStack getIon(String aElementName, int aCharge) {
        for (String g : gtPlusPlus.core.item.chemistry.IonParticles.NameToMetaMap.keySet()) {
            if (g.toLowerCase()
                .equals(Utils.sanitizeString(aElementName.toLowerCase()))) {
                Integer meta = gtPlusPlus.core.item.chemistry.IonParticles.NameToMetaMap
                    .get(Utils.sanitizeString(aElementName.toLowerCase()));
                if (meta == null) {
                    meta = 0;
                }
                ItemStack aIon = ItemUtils.simpleMetaStack(ModItems.itemIonParticleBase, meta, 1);
                if (aCharge != 0) {
                    IonParticles.setChargeState(aIon, aCharge);
                }
                return aIon;
            }
        }
        return null;
    }

    public static ItemStack getBaseParticle(Particle aParticle) {
        String aPartName = Utils.sanitizeString(aParticle.mParticleName.toLowerCase());
        for (String g : gtPlusPlus.core.item.chemistry.StandardBaseParticles.NameToMetaMap.keySet()) {
            if (g.toLowerCase()
                .equals(aPartName)) {
                Integer meta = gtPlusPlus.core.item.chemistry.StandardBaseParticles.NameToMetaMap.get(aPartName);
                if (meta == null) {
                    meta = 0;
                }
                return ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, meta, 1);
            }
        }
        return null;
    }
}
