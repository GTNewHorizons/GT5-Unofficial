package com.github.technus.tectech.mechanics.elementalMatter.definitions.complex;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_Y__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static com.github.technus.tectech.util.XSTR.XSTR_INSTANCE;
import static gregtech.api.enums.Materials.Aluminium;
import static gregtech.api.enums.Materials.Americium;
import static gregtech.api.enums.Materials.Antimony;
import static gregtech.api.enums.Materials.Argon;
import static gregtech.api.enums.Materials.Arsenic;
import static gregtech.api.enums.Materials.Barium;
import static gregtech.api.enums.Materials.Beryllium;
import static gregtech.api.enums.Materials.Bismuth;
import static gregtech.api.enums.Materials.Boron;
import static gregtech.api.enums.Materials.Cadmium;
import static gregtech.api.enums.Materials.Caesium;
import static gregtech.api.enums.Materials.Calcium;
import static gregtech.api.enums.Materials.Carbon;
import static gregtech.api.enums.Materials.Cerium;
import static gregtech.api.enums.Materials.Chrome;
import static gregtech.api.enums.Materials.Cobalt;
import static gregtech.api.enums.Materials.Copper;
import static gregtech.api.enums.Materials.Deuterium;
import static gregtech.api.enums.Materials.Dysprosium;
import static gregtech.api.enums.Materials.Erbium;
import static gregtech.api.enums.Materials.Europium;
import static gregtech.api.enums.Materials.Fluorine;
import static gregtech.api.enums.Materials.Gadolinium;
import static gregtech.api.enums.Materials.Gallium;
import static gregtech.api.enums.Materials.Gold;
import static gregtech.api.enums.Materials.Helium;
import static gregtech.api.enums.Materials.Helium_3;
import static gregtech.api.enums.Materials.Holmium;
import static gregtech.api.enums.Materials.Hydrogen;
import static gregtech.api.enums.Materials.Indium;
import static gregtech.api.enums.Materials.Iridium;
import static gregtech.api.enums.Materials.Iron;
import static gregtech.api.enums.Materials.Lanthanum;
import static gregtech.api.enums.Materials.Lead;
import static gregtech.api.enums.Materials.Lithium;
import static gregtech.api.enums.Materials.Lutetium;
import static gregtech.api.enums.Materials.Magnesium;
import static gregtech.api.enums.Materials.Manganese;
import static gregtech.api.enums.Materials.Mercury;
import static gregtech.api.enums.Materials.Molybdenum;
import static gregtech.api.enums.Materials.Neodymium;
import static gregtech.api.enums.Materials.Nickel;
import static gregtech.api.enums.Materials.Niobium;
import static gregtech.api.enums.Materials.Nitrogen;
import static gregtech.api.enums.Materials.Osmium;
import static gregtech.api.enums.Materials.Oxygen;
import static gregtech.api.enums.Materials.Palladium;
import static gregtech.api.enums.Materials.Phosphorus;
import static gregtech.api.enums.Materials.Platinum;
import static gregtech.api.enums.Materials.Plutonium;
import static gregtech.api.enums.Materials.Plutonium241;
import static gregtech.api.enums.Materials.Potassium;
import static gregtech.api.enums.Materials.Praseodymium;
import static gregtech.api.enums.Materials.Promethium;
import static gregtech.api.enums.Materials.Radon;
import static gregtech.api.enums.Materials.Rubidium;
import static gregtech.api.enums.Materials.Samarium;
import static gregtech.api.enums.Materials.Scandium;
import static gregtech.api.enums.Materials.Silicon;
import static gregtech.api.enums.Materials.Silver;
import static gregtech.api.enums.Materials.Sodium;
import static gregtech.api.enums.Materials.Strontium;
import static gregtech.api.enums.Materials.Sulfur;
import static gregtech.api.enums.Materials.Tantalum;
import static gregtech.api.enums.Materials.Tellurium;
import static gregtech.api.enums.Materials.Terbium;
import static gregtech.api.enums.Materials.Thorium;
import static gregtech.api.enums.Materials.Thulium;
import static gregtech.api.enums.Materials.Tin;
import static gregtech.api.enums.Materials.Titanium;
import static gregtech.api.enums.Materials.Tritium;
import static gregtech.api.enums.Materials.Tungsten;
import static gregtech.api.enums.Materials.Uranium;
import static gregtech.api.enums.Materials.Uranium235;
import static gregtech.api.enums.Materials.Vanadium;
import static gregtech.api.enums.Materials.Ytterbium;
import static gregtech.api.enums.Materials.Yttrium;
import static gregtech.api.enums.Materials.Zinc;
import static gregtech.api.enums.OrePrefixes.dust;
import static java.lang.Math.abs;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMComplexTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMIndirectType;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition;
import com.github.technus.tectech.util.TT_Utility;
import com.github.technus.tectech.util.XSTR;

/**
 * Created by danie_000 on 18.11.2016.
 */
public class EMAtomDefinition extends EMComplexTemplate {

    private static final String[] SYMBOL = new String[] { "Nt", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne",
            "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni",
            "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd",
            "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd",
            "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb",
            "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm",
            "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts",
            "Og" };
    private static final String[] NAME = new String[] { "Neutronium", "Hydrogen", "Helium", "Lithium", "Beryllium",
            "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminium", "Silicon",
            "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium",
            "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic",
            "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum",
            "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony",
            "Tellurium", "Iodine", "Xenon", "Caesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodymium",
            "Promethium", "Samarium", "Europium", "Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium",
            "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum",
            "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium",
            "Actinium", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium",
            "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium",
            "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium",
            "Copernicium", "Nihonium", "Flerovium", "Moscovium", "Livermorium", "Tennessine", "Oganesson" };
    private static final String[] SYMBOL_IUPAC = new String[] { "n", "u", "b", "t", "q", "p", "h", "s", "o", "e", "N",
            "U", "B", "T", "Q", "P", "H", "S", "O", "E" };

    public static final long ATOM_COMPLEXITY_LIMIT = 65536L;
    private static final byte BYTE_OFFSET = 32;

    private final int hash;
    public static double refMass, refUnstableMass;

    private static final String nbtType = "a";
    private static final Random xstr = new XSTR(); // NEEDS SEPARATE!
    private static Map<Integer, TreeSet<Integer>> stableIsotopes = new HashMap<>();
    private static final Map<Integer, EMAtomDefinition> stableAtoms = new HashMap<>();
    private static Map<Integer, TreeMap<Double, Integer>> mostStableUnstableIsotopes = new HashMap<>();
    private static final Map<Integer, EMAtomDefinition> unstableAtoms = new HashMap<>();
    private static EMDefinitionStack alpha, deuterium, tritium, helium_3, beryllium_8, carbon_14, neon_24, silicon_34,
            uranium_238, uranium_235, plutonium_239, plutonium_241;
    private static final HashMap<EMAtomDefinition, Double> lifetimeOverrides = new HashMap<>();

    private final EMNuclideIAEA iaea;

    private static EMAtomDefinition somethingHeavy;

    public static EMAtomDefinition getSomethingHeavy() {
        return somethingHeavy;
    }

    private static final ArrayList<Runnable> overrides = new ArrayList<>();

    public static void addOverride(EMAtomDefinition atom, double rawLifeTime) {
        lifetimeOverrides.put(atom, rawLifeTime);
    }

    // float-mass in eV/c^2
    private final double mass;
    // public final int charge;
    private final int charge;
    // int -electric charge in 1/3rds of electron charge for optimization
    private final int chargeLeptons;
    private final double rawLifeTime;
    // generation max present inside - minus if contains any anti quark
    private final byte type;

    private final byte decayMode; // t neutron to proton+,0,f proton to neutron
    // public final boolean stable;

    private final int neutralCount;
    private final int element;

    private final boolean iaeaDefinitionExistsAndHasEnergyLevels;

    private final EMConstantStackMap elementalStacks;

    // stable is rawLifeTime>=10^9

    public EMAtomDefinition(EMDefinitionStack... things) throws EMException {
        this(true, new EMConstantStackMap(things));
    }

    public EMAtomDefinition(EMConstantStackMap things) throws EMException {
        this(true, things);
    }

    private EMAtomDefinition(boolean check, EMConstantStackMap things) throws EMException {
        if (check && !canTheyBeTogether(things)) {
            throw new EMException("Atom Definition error");
        }
        elementalStacks = things;

        double mass = 0;
        int cLeptons = 0;
        int cNucleus = 0;
        int neutralCount = 0, element = 0;
        int type = 0;
        boolean containsAnti = false;
        for (EMDefinitionStack stack : elementalStacks.valuesToArray()) {
            IEMDefinition def = stack.getDefinition();
            int amount = (int) stack.getAmount();
            if ((int) stack.getAmount() != stack.getAmount()) {
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            mass += stack.getMass();
            if (def.getGeneration() < 0) {
                containsAnti = true;
            }
            type = Math.max(type, abs(def.getGeneration()));

            if (def instanceof EMLeptonDefinition) {
                cLeptons += stack.getCharge();
            } else {
                cNucleus += stack.getCharge();
                if (def.getCharge() == 3) {
                    element += amount;
                } else if (def.getCharge() == -3) {
                    element -= amount;
                } else if (def.getCharge() == 0) {
                    neutralCount += amount;
                }
            }
        }
        this.type = containsAnti ? (byte) -type : (byte) type;
        // this.mass = mass;
        chargeLeptons = cLeptons;
        charge = cNucleus + cLeptons;
        this.neutralCount = neutralCount;
        this.element = element;

        element = abs(element);

        // stability curve
        int StableIsotope = stableIzoCurve(element);
        int izoDiff = neutralCount - StableIsotope;
        int izoDiffAbs = abs(izoDiff);

        xstr.setSeed((element + 1L) * (neutralCount + 100L));
        iaea = EMNuclideIAEA.get(element, neutralCount);
        if (getIaea() != null) {
            if (Double.isNaN(getIaea().getMass())) {
                this.mass = mass;
            } else {
                this.mass = getIaea().getMass();
            }

            if (Double.isNaN(getIaea().getHalfTime())) {
                Double overriddenLifeTime = lifetimeOverrides.get(this);
                double rawLifeTimeTemp;
                if (overriddenLifeTime != null) {
                    rawLifeTimeTemp = overriddenLifeTime;
                } else {
                    rawLifeTimeTemp = calculateLifeTime(izoDiff, izoDiffAbs, element, neutralCount, containsAnti);
                }
                rawLifeTime = Math.min(rawLifeTimeTemp, STABLE_RAW_LIFE_TIME);
            } else {
                rawLifeTime = containsAnti ? getIaea().getHalfTime() * 1.5514433E-21d * (1d + xstr.nextDouble() * 9d)
                        : getIaea().getHalfTime();
            }
            iaeaDefinitionExistsAndHasEnergyLevels = getIaea().getEnergeticStatesArray().length > 1;
        } else {
            this.mass = mass;

            Double overriddenLifeTime = lifetimeOverrides.get(this);
            double rawLifeTimeTemp;
            if (overriddenLifeTime != null) {
                rawLifeTimeTemp = overriddenLifeTime;
            } else {
                rawLifeTimeTemp = calculateLifeTime(izoDiff, izoDiffAbs, element, neutralCount, containsAnti);
            }
            rawLifeTime = Math.min(rawLifeTimeTemp, STABLE_RAW_LIFE_TIME);

            iaeaDefinitionExistsAndHasEnergyLevels = false;
        }

        if (getIaea() == null || getIaea().getEnergeticStatesArray()[0].energy != 0) {
            if (izoDiff == 0) {
                decayMode = 0;
            } else {
                decayMode = izoDiff > 0 ? (byte) Math.min(2, 1 + izoDiffAbs / 4)
                        : (byte) -Math.min(2, 1 + izoDiffAbs / 4);
            }
        } else {
            decayMode = izoDiff > 0 ? (byte) (Math.min(2, 1 + izoDiffAbs / 4) + BYTE_OFFSET)
                    : (byte) (-Math.min(2, 1 + izoDiffAbs / 4) + BYTE_OFFSET);
        }
        // this.stable = this.rawLifeTime >= STABLE_RAW_LIFE_TIME;
        hash = super.hashCode();
    }

    private static int stableIzoCurve(int element) {
        return (int) Math.round(
                -1.19561E-06D * Math.pow(element, 4D) + 1.60885E-04D * Math.pow(element, 3D)
                        + 3.76604E-04D * Math.pow(element, 2D)
                        + 1.08418E+00D * (double) element);
    }

    private static double calculateLifeTime(int izoDiff, int izoDiffAbs, int element, int isotope,
            boolean containsAnti) {
        double rawLifeTime;

        if (element <= 83 && isotope < 127
                && (izoDiffAbs == 0 || element == 1 && isotope == 0
                        || element == 2 && isotope == 1
                        || izoDiffAbs == 1 && element > 2 && element % 2 == 1
                        || izoDiffAbs == 3 && element > 30 && element % 2 == 0
                        || izoDiffAbs == 5 && element > 30 && element % 2 == 0
                        || izoDiffAbs == 2 && element > 20 && element % 2 == 1)) {
            rawLifeTime = (1D + xstr.nextDouble() * 9D) * (containsAnti ? 2.381e4D : 1.5347e25D);
        } else {
            // Y = (X-A)/(B-A) * (D-C) + C
            double unstabilityEXP;
            if (element == 0) {
                return 1e-35D;
            } else if (element == 1) {
                unstabilityEXP = 1.743D - abs(izoDiff - 1) * 9.743D;
            } else if (element == 2) {
                switch (isotope) {
                    case 4:
                        unstabilityEXP = 1.61D;
                        break;
                    case 5:
                        unstabilityEXP = -7.523D;
                        break;
                    case 6:
                        unstabilityEXP = -1.51D;
                        break;
                    default:
                        unstabilityEXP = -(izoDiffAbs * 6.165D);
                        break;
                }
            } else if (element <= 83 || isotope <= 127 && element <= 120) {
                double elementPow4 = Math.pow(element, 4);

                unstabilityEXP = Math.min(element / 2.4D, 6 + ((element + 1) % 2) * 3e6D / elementPow4)
                        + -izoDiff * elementPow4 / 1e8D
                        - abs(izoDiff - 1 + element / 60D) * (3D - element / 12.5D + element * element / 1500D);
            } else if (element < 180) {
                unstabilityEXP = Math.min((element - 85) * 2, 16 + ((isotope + 1) % 2) * 2.5D - (element - 85) / 3D)
                        - abs(izoDiff) * (3D - element / 13D + element * element / 1600D);
            } else {
                return -1;
            }
            if ((isotope == 127 || isotope == 128) && element < 120 && element > 83) {
                unstabilityEXP -= 1.8D;
            }
            if (element > 83 && element < 93 && isotope % 2 == 0 && izoDiff == 3) {
                unstabilityEXP += 6;
            }
            if (element > 93 && element < 103 && isotope % 2 == 0 && izoDiff == 4) {
                unstabilityEXP += 6;
            }
            rawLifeTime = (containsAnti ? 1e-8D : 1) * Math.pow(10D, unstabilityEXP) * (1D + xstr.nextDouble() * 9D);
        }

        if (rawLifeTime < 8e-15D) {
            return 1e-35D;
        }
        if (rawLifeTime > 8e28D) {
            return 8e30D;
        }
        return rawLifeTime;
    }

    private static boolean canTheyBeTogether(EMConstantStackMap stacks) {
        boolean nuclei = false;
        long qty = 0;
        for (EMDefinitionStack stack : stacks.valuesToArray()) {
            if (stack.getDefinition() instanceof EMHadronDefinition) {
                if (((EMHadronDefinition) stack.getDefinition()).getAmount() != 3) {
                    return false;
                }
                nuclei = true;
            } else if (!(stack.getDefinition() instanceof EMLeptonDefinition)) {
                return false;
            }
            if ((int) stack.getAmount() != stack.getAmount()) {
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            qty += stack.getAmount();
        }
        return nuclei && qty < ATOM_COMPLEXITY_LIMIT;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    public int getChargeLeptons() {
        return chargeLeptons;
    }

    public int getChargeHadrons() {
        return getCharge() - getChargeLeptons();
    }

    public int getIonizationElementWise() {
        return getElement() * 3 + getChargeLeptons();
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public int getGeneration() {
        return type;
    }

    @Override
    public double getRawTimeSpan(long currentEnergy) {
        if (currentEnergy <= 0) {
            return rawLifeTime;
        }
        if (iaeaDefinitionExistsAndHasEnergyLevels) {
            if (currentEnergy >= getIaea().getEnergeticStatesArray().length) {
                return getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length - 1].Thalf
                        / (currentEnergy - getIaea().getEnergeticStatesArray().length + 1);
            }
            return getIaea().getEnergeticStatesArray()[(int) currentEnergy].Thalf;
        }
        return rawLifeTime / (currentEnergy + 1);
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }

    @Override
    public int getMaxColors() {
        return -10;
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.Element");
    }

    @Override
    public String getShortLocalizedName() {
        int element = abs(getElement());
        boolean anti = getElement() < 0;
        boolean weird = abs(getGeneration()) != 1;
        if (element >= NAME.length) {
            StringBuilder s = new StringBuilder();
            if (anti) {
                s.append(translateToLocal("tt.IUPAC.Anti"));
                do {
                    s.append(translateToLocal("tt.IUPAC." + SYMBOL_IUPAC[element % 10]));
                    element = element / 10;
                } while (element > 0);
            } else {
                while (element >= 10) {
                    s.append(translateToLocal("tt.IUPAC." + SYMBOL_IUPAC[element % 10]));
                    element = element / 10;
                }
                s.append(translateToLocal("tt.IUPAC." + SYMBOL_IUPAC[element + 10]));
            }
            if (weird) {
                s.append(translateToLocal("tt.keyword.Weird"));
            }
            return s.toString();
        }
        return translateToLocal("tt.element." + (anti ? "Anti" : "") + NAME[element])
                + (weird ? translateToLocal("tt.keyword.Weird") : "");
    }

    @Override
    public String getSymbol() {
        String ionName = "";
        int ionization = getCharge() / 3;
        if (ionization > 0) {
            ionName = ionization == 1 ? TT_Utility.toSuperscript("+") : TT_Utility.toSuperscript(ionization + "+");
        } else if (ionization < 0) {
            ionName = ionization == -1 ? TT_Utility.toSuperscript("-") : TT_Utility.toSuperscript(-ionization + "-");
        }
        return TT_Utility.toSuperscript(Long.toString(getNeutralCount() + (long) getElement())) + getShortSymbol()
                + ionName;
    }

    @Override
    public String getShortSymbol() {
        int element = abs(getElement());
        boolean anti = getElement() < 0;
        boolean weird = abs(getGeneration()) != 1;
        if (element >= SYMBOL.length) {
            StringBuilder s = new StringBuilder(anti ? "~" : "");
            while (element >= 10) {
                s.append(SYMBOL_IUPAC[element % 10]);
                element = element / 10;
            }
            s.append(SYMBOL_IUPAC[element + 10]);
            if (weird) {
                s.append(translateToLocal("tt.keyword.Weird"));
            }
            return s.toString();
        }
        return (anti ? "~" : "") + SYMBOL[element] + (weird ? translateToLocal("tt.keyword.Weird") : "");
    }

    @Override
    public EMConstantStackMap getSubParticles() {
        return elementalStacks.clone();
    }

    @Override
    public EMDecay[] getDecayArray() {
        ArrayList<EMDecay> decaysList = new ArrayList<>(4);
        return getDecayArray(decaysList, getDecayMode(), true);
    }

    private EMDecay[] getDecayArray(ArrayList<EMDecay> decaysList, int decayMode, boolean tryAnti) { // todo?
        if (getGeneration() == 1) {
            switch (decayMode) {
                case -2:
                    if (TecTech.RANDOM.nextBoolean() && ElectronCapture(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    } else if (PbetaDecay(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case -1:
                    if (Emmision(decaysList, EMHadronDefinition.hadron_p1)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case 0:
                    if (alphaDecay(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case 1:
                    if (Emmision(decaysList, EMHadronDefinition.hadron_n1)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case 2:
                    if (MbetaDecay(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                default:
                    if (decayMode > 8) {
                        if (iaeaDecay(decaysList, 0)) {
                            return decaysList.toArray(EMDecay.NO_PRODUCT);
                        }
                        return getDecayArray(decaysList, decayMode - BYTE_OFFSET, false);
                    }
            }
            return EMDecay.NO_DECAY;
        } else if (getGeneration() == -1) {
            EMAtomDefinition anti = getAnti();
            if (anti != null) {
                return anti.getDecayArray(decaysList, decayMode, false);
            }
        }
        return getNaturalDecayInstant();
    }

    private boolean iaeaDecay(ArrayList<EMDecay> decaysList, long energy) {
        EMNuclideIAEA.energeticState state;
        if (energy > getIaea().getEnergeticStatesArray().length) {
            state = getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length - 1];
        } else if (energy <= 0) {
            state = getIaea().getEnergeticStatesArray()[0];
        } else {
            state = getIaea().getEnergeticStatesArray()[(int) energy];
        }
        for (int i = 0; i < state.decaymodes.length; i++) {
            if (!getDecayFromIaea(decaysList, state.decaymodes[i], energy)) {
                decaysList.clear();
                return false;
            }
        }
        return !decaysList.isEmpty();
    }

    private boolean getDecayFromIaea(ArrayList<EMDecay> decaysList, EMNuclideIAEA.iaeaDecay decay, long energy) {
        EMDefinitionStackMap withThis = elementalStacks.toMutable(), newStuff = new EMDefinitionStackMap();
        switch (decay.decayName) {
            case "D": {
                if (withThis.removeAllAmountsExact(deuterium.getDefinition().getSubParticles())) {
                    withThis.putReplace(deuterium);
                    decaysList.add(
                            new EMDecay(
                                    decay.chance,
                                    withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            }
                break;
            case "3H": {
                if (withThis.removeAllAmountsExact(tritium.getDefinition().getSubParticles())) {
                    withThis.putReplace(tritium);
                    decaysList.add(
                            new EMDecay(
                                    decay.chance,
                                    withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            }
                break;
            case "3HE": {
                if (withThis.removeAllAmountsExact(helium_3.getDefinition().getSubParticles())) {
                    withThis.putReplace(helium_3);
                    decaysList.add(
                            new EMDecay(
                                    decay.chance,
                                    withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            }
                break;
            case "8BE": {
                if (withThis.removeAllAmountsExact(beryllium_8.getDefinition().getSubParticles())) {
                    withThis.putReplace(beryllium_8);
                    decaysList.add(
                            new EMDecay(
                                    decay.chance,
                                    withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            }
                break;
            case "14C": {
                if (withThis.removeAllAmountsExact(carbon_14.getDefinition().getSubParticles())) {
                    newStuff.putReplace(carbon_14);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "24NE": {
                if (withThis.removeAllAmountsExact(neon_24.getDefinition().getSubParticles())) {
                    newStuff.putReplace(neon_24);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "34SI": {
                if (withThis.removeAllAmountsExact(silicon_34.getDefinition().getSubParticles())) {
                    newStuff.putReplace(silicon_34);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "A":
            case "A?": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2, EMHadronDefinition.hadron_p2)) {
                    newStuff.putReplace(alpha);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "2B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_2);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B-": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "2B-": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e2);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "EC": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1, EMLeptonDefinition.lepton_e1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "2EC": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e2)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B++EC":
            case "EC+B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B+A": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p3, EMHadronDefinition.hadron_n1)) {
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(alpha);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B+P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B+2P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p3)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B-A": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n3, EMHadronDefinition.hadron_p1)) {
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(alpha);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B-N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(EMHadronDefinition.hadron_n1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B-2N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n3)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(EMHadronDefinition.hadron_n2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "B-P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)) {
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "ECA": {
                if (withThis.removeAllAmountsExact(
                        EMHadronDefinition.hadron_n1,
                        EMLeptonDefinition.lepton_e1,
                        EMHadronDefinition.hadron_p3)) {
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(alpha);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "ECP": {
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e1, EMHadronDefinition.hadron_p2)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "EC2P": {
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e1, EMHadronDefinition.hadron_p3)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "ECP+EC2P": { // todo look at branching ratios
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e2, EMHadronDefinition.hadron_p5)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    newStuff.putReplace(EMHadronDefinition.hadron_p3);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)) {
                    newStuff.putReplace(EMHadronDefinition.hadron_n1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "2N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2)) {
                    newStuff.putReplace(EMHadronDefinition.hadron_n2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1)) {
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "2P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2)) {
                    newStuff.putReplace(EMHadronDefinition.hadron_p2);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "SF": {
                if (Fission(decaysList, withThis, newStuff, decay.chance, false)) {
                    return true;
                }
            }
                break;
            case "B-F": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    try {
                        if (Fission(decaysList, withThis, newStuff, decay.chance, false)) {
                            return true;
                        }
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "ECF":
            case "ECSF":
            case "EC(+SF)": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1, EMLeptonDefinition.lepton_e1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    try {
                        if (Fission(decaysList, withThis, newStuff, decay.chance, false)) {
                            return true;
                        }
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "SF(+EC+B+)":
            case "SF+EC+B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try {
                        if (Fission(decaysList, withThis, newStuff, decay.chance, false)) {
                            return true;
                        }
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "SF+EC+B-": {
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e1)) {
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    try {
                        if (Fission(decaysList, withThis, newStuff, decay.chance, false)) {
                            return true;
                        }
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "IT":
            case "IT?":
            case "G": {
                if (energy > 0) {
                    decaysList.add(new EMDecay(decay.chance, this, boson_Y__));
                } else {
                    if (DEBUG_MODE) {
                        TecTech.LOGGER.info("Tried to emit Gamma from ground state");
                    }
                    decaysList.add(new EMDecay(decay.chance, this));
                }
                return true;
            } // break;
            case "IT+EC+B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e1)) {
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    newStuff.putReplace(EMGaugeBosonDefinition.boson_Y__1);
                    try {
                        newStuff.putReplace(
                                new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree())
                                        .getStackForm(1));
                        decaysList.add(
                                new EMDecay(
                                        decay.chance,
                                        newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    } catch (Exception e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                break;
            case "DEAD_END":
                decaysList.add(deadEnd);
                return true;
            default:
                throw new Error(
                        "Unsupported decay mode: " + decay.decayName + ' ' + getNeutralCount() + ' ' + getElement());
        }
        if (DEBUG_MODE) {
            TecTech.LOGGER.info("Failed to decay " + getElement() + ' ' + getNeutralCount() + ' ' + decay.decayName);
        }
        return false;
    }

    private boolean Emmision(ArrayList<EMDecay> decaysList, EMDefinitionStack emit) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAmountExact(emit)) {
            try {
                decaysList.add(
                        new EMDecay(
                                1,
                                new EMDefinitionStack(
                                        new EMAtomDefinition(
                                                tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                                        1),
                                emit));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean alphaDecay(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAllAmountsExact(alpha.getDefinition().getSubParticles())) {
            try {
                decaysList.add(
                        new EMDecay(
                                1,
                                new EMDefinitionStack(
                                        new EMAtomDefinition(
                                                tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                                        1),
                                alpha));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean MbetaDecay(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAmountExact(EMHadronDefinition.hadron_n1)) {
            try {
                tree.putUnifyExact(EMHadronDefinition.hadron_p1);
                decaysList.add(
                        new EMDecay(
                                1,
                                new EMDefinitionStack(
                                        new EMAtomDefinition(
                                                tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                                        1),
                                EMLeptonDefinition.lepton_e1,
                                EMNeutrinoDefinition.lepton_Ve_1));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean PbetaDecay(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAmountExact(EMHadronDefinition.hadron_p1)) {
            try {
                tree.putUnifyExact(EMHadronDefinition.hadron_n1);
                decaysList.add(
                        new EMDecay(
                                1,
                                new EMDefinitionStack(
                                        new EMAtomDefinition(
                                                tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                                        1),
                                EMLeptonDefinition.lepton_e_1,
                                EMNeutrinoDefinition.lepton_Ve1));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean ElectronCapture(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAllAmountsExact(EMHadronDefinition.hadron_p1, EMLeptonDefinition.lepton_e1)) {
            try {
                tree.putUnifyExact(EMHadronDefinition.hadron_n1);
                decaysList.add(
                        new EMDecay(
                                1,
                                new EMDefinitionStack(
                                        new EMAtomDefinition(
                                                tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                                        1),
                                EMNeutrinoDefinition.lepton_Ve1));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean Fission(ArrayList<EMDecay> decaysList, EMDefinitionStackMap fissile, EMDefinitionStackMap particles,
            double probability, boolean spontaneousCheck) {
        EMDefinitionStackMap heavy = new EMDefinitionStackMap();
        double[] liquidDrop = liquidDropFunction(abs(getElement()) <= 97);

        for (EMDefinitionStack stack : fissile.valuesToArray()) {
            if (spontaneousCheck && stack.getDefinition() instanceof EMHadronDefinition
                    && (stack.getAmount() <= 80
                            || stack.getAmount() < 90 && XSTR_INSTANCE.nextInt(10) < stack.getAmount() - 80)) {
                return false;
            }
            if (stack.getDefinition().getCharge() == 0) {
                // if(stack.definition instanceof dHadronDefinition){
                double neutrals = stack.getAmount() * liquidDrop[2];
                int neutrals_cnt = (int) Math.floor(neutrals);
                neutrals_cnt += neutrals - neutrals_cnt > XSTR_INSTANCE.nextDouble() ? 1 : 0;
                particles.putUnifyExact(new EMDefinitionStack(stack.getDefinition(), neutrals_cnt));

                int heavy_cnt = (int) Math.ceil(stack.getAmount() * liquidDrop[1]);
                while (heavy_cnt + neutrals_cnt > stack.getAmount()) {
                    heavy_cnt--;
                }
                fissile.removeAmountExact(new EMDefinitionStack(stack.getDefinition(), heavy_cnt + neutrals_cnt));
                heavy.putReplace(new EMDefinitionStack(stack.getDefinition(), heavy_cnt));
                // }else{
                // particles.add(stack);
                // light.remove(stack.definition);
                // }
            } else {
                int heavy_cnt = (int) Math.ceil(stack.getAmount() * liquidDrop[0]);
                if (heavy_cnt % 2 == 1 && XSTR_INSTANCE.nextDouble() > 0.05D) {
                    heavy_cnt--;
                }
                EMDefinitionStack new_stack = new EMDefinitionStack(stack.getDefinition(), heavy_cnt);
                fissile.removeAmountExact(new_stack);
                heavy.putReplace(new_stack);
            }
        }

        try {
            particles.putReplace(
                    new EMDefinitionStack(
                            new EMAtomDefinition(fissile.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                            1));
            particles.putReplace(
                    new EMDefinitionStack(
                            new EMAtomDefinition(heavy.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),
                            1));
            decaysList
                    .add(new EMDecay(probability, particles.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
            return true;
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static double[] liquidDropFunction(boolean asymmetric) {
        double[] out = new double[3];

        out[0] = XSTR_INSTANCE.nextGaussian();

        if (out[0] < 1 && out[0] >= -1) {
            if (XSTR_INSTANCE.nextBoolean()) {
                out[0] = XSTR_INSTANCE.nextDouble() * 2d - 1d;
            }
        }

        if (asymmetric && out[0] > XSTR_INSTANCE.nextDouble() && XSTR_INSTANCE.nextInt(4) == 0) {
            out[0] = -out[0];
        }

        // scale to splitting ratio
        out[0] = out[0] * 0.05d + .6d;

        if (out[0] < 0 || out[0] > 1) {
            return liquidDropFunction(asymmetric);
        }
        if (out[0] < .5d) {
            out[0] = 1d - out[0];
        }

        // extra neutrals
        out[2] = 0.012d + XSTR_INSTANCE.nextDouble() * 0.01d;

        if (asymmetric) {
            out[1] = out[0];
        } else {
            out[1] = out[0] - out[2] * .5d;
        }

        return out;
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        if (iaeaDefinitionExistsAndHasEnergyLevels) {
            ArrayList<EMDecay> decays = new ArrayList<>(4);
            if (iaeaDecay(decays, energyLevel)) {
                return decays.toArray(EMDecay.NO_PRODUCT);
            }
        }
        if (energyLevel < abs(getCharge()) / 3 + getNeutralCount()) {
            return new EMDecay[] { new EMDecay(1, this, boson_Y__) };
        }
        return getNaturalDecayInstant();
    }

    @Override
    public double getEnergyDiffBetweenStates(long currentEnergyLevel, long newEnergyLevel) {
        if (iaeaDefinitionExistsAndHasEnergyLevels) {
            double result = 0;
            boolean backwards = newEnergyLevel < currentEnergyLevel;
            if (backwards) {
                long temp = currentEnergyLevel;
                currentEnergyLevel = newEnergyLevel;
                newEnergyLevel = temp;
            }

            if (currentEnergyLevel <= 0) {
                if (newEnergyLevel <= 0) {
                    return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
                } else {
                    result += IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * -currentEnergyLevel;
                }
            } else {
                result -= getIaea().getEnergeticStatesArray()[(int) Math
                        .min(getIaea().getEnergeticStatesArray().length - 1, currentEnergyLevel)].energy;
            }
            if (newEnergyLevel >= getIaea().getEnergeticStatesArray().length) {
                if (currentEnergyLevel >= getIaea().getEnergeticStatesArray().length) {
                    return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
                } else {
                    result += IEMDefinition.DEFAULT_ENERGY_REQUIREMENT
                            * (newEnergyLevel - getIaea().getEnergeticStatesArray().length + 1);
                }
                result += getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length - 1].energy;
            } else {
                result += getIaea().getEnergeticStatesArray()[(int) newEnergyLevel].energy;
            }

            return backwards ? -result : result;
        }
        return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
    }

    @Override
    public boolean usesSpecialEnergeticDecayHandling() {
        return iaeaDefinitionExistsAndHasEnergyLevels;
    }

    @Override
    public boolean usesMultipleDecayCalls(long energyLevel) {
        if (!iaeaDefinitionExistsAndHasEnergyLevels) return false;
        EMNuclideIAEA.energeticState state;
        if (energyLevel > getIaea().getEnergeticStatesArray().length) {
            state = getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length - 1];
        } else if (energyLevel <= 0) {
            state = getIaea().getEnergeticStatesArray()[0];
        } else {
            state = getIaea().getEnergeticStatesArray()[(int) energyLevel];
        }
        for (EMNuclideIAEA.iaeaDecay decay : state.decaymodes) {
            if (decay.decayName.contains("F")) return true; // if is fissile
        }
        return false;
    }

    @Override
    public boolean decayMakesEnergy(long energyLevel) {
        return iaeaDefinitionExistsAndHasEnergyLevels;
    }

    @Override
    public boolean fusionMakesEnergy(long energyLevel) {
        return getIaea() != null || iaeaDefinitionExistsAndHasEnergyLevels;
    }

    @Override
    public EMDecay[] getNaturalDecayInstant() {
        // disembody
        ArrayList<EMDefinitionStack> decaysInto = new ArrayList<>();
        for (EMDefinitionStack elementalStack : elementalStacks.valuesToArray()) {
            if (elementalStack.getDefinition().getGeneration() == 1
                    || elementalStack.getDefinition().getGeneration() == -1) {
                // covers both quarks and antiquarks
                decaysInto.add(elementalStack);
            } else {
                // covers both quarks and antiquarks
                decaysInto.add(new EMDefinitionStack(boson_Y__, 2));
            }
        }
        return new EMDecay[] { new EMDecay(0.75D, decaysInto.toArray(new EMDefinitionStack[0])), deadEnd };
    }

    @Override
    public EMAtomDefinition getAnti() {
        EMDefinitionStackMap anti = new EMDefinitionStackMap();
        for (EMDefinitionStack stack : elementalStacks.valuesToArray()) {
            anti.putReplace(new EMDefinitionStack(stack.getDefinition().getAnti(), stack.getAmount()));
        }
        try {
            return new EMAtomDefinition(anti.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public EMNuclideIAEA getIaea() {
        return iaea;
    }

    public byte getDecayMode() {
        return decayMode;
    }

    public int getNeutralCount() {
        return neutralCount;
    }

    public int getElement() {
        return element;
    }

    @Override
    protected String getTagValue() {
        return nbtType;
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(
                nbtType,
                new EMIndirectType(
                        (definitionsRegistry,
                                nbt) -> new EMAtomDefinition(EMConstantStackMap.fromNBT(definitionsRegistry, nbt)),
                        EMAtomDefinition.class,
                        "tt.keyword.Element"));
        EMNuclideIAEA.run();

        for (Runnable r : overrides) {
            r.run();
        }

        for (Map.Entry<EMAtomDefinition, Double> entry : lifetimeOverrides.entrySet()) {
            try {
                lifetimeOverrides.put(new EMAtomDefinition(entry.getKey().elementalStacks), entry.getValue());
            } catch (EMException e) {
                e.printStackTrace(); // Impossible
            }
        }

        // populate stable isotopes
        for (int element = 1; element < 83; element++) { // Up to Bismuth exclusive
            for (int isotope = 0; isotope < 130; isotope++) {
                xstr.setSeed((long) (element + 1) * (isotope + 100));
                // stability curve
                int StableIsotope = stableIzoCurve(element);
                int izoDiff = isotope - StableIsotope;
                int izoDiffAbs = abs(izoDiff);
                double rawLifeTime = calculateLifeTime(izoDiff, izoDiffAbs, element, isotope, false);
                EMNuclideIAEA nuclide = EMNuclideIAEA.get(element, isotope);
                if (rawLifeTime >= STABLE_RAW_LIFE_TIME
                        || nuclide != null && nuclide.getHalfTime() >= STABLE_RAW_LIFE_TIME) {
                    TreeSet<Integer> isotopes = stableIsotopes.computeIfAbsent(element, k -> new TreeSet<>());
                    isotopes.add(isotope);
                }
            }
        }

        // populate unstable isotopes
        for (int element = 1; element < 150; element++) {
            for (int isotope = 100; isotope < 180; isotope++) {
                xstr.setSeed((long) (element + 1) * (isotope + 100));
                // stability curve
                int Isotope = stableIzoCurve(element);
                int izoDiff = isotope - Isotope;
                int izoDiffAbs = abs(izoDiff);
                double rawLifeTime = calculateLifeTime(izoDiff, izoDiffAbs, element, isotope, false);
                TreeMap<Double, Integer> isotopes = mostStableUnstableIsotopes
                        .computeIfAbsent(element, k -> new TreeMap<>());
                isotopes.put(rawLifeTime, isotope); // todo dont add stable ones
            }
        }

        try {
            for (Map.Entry<Integer, TreeSet<Integer>> integerTreeSetEntry : stableIsotopes.entrySet()) {
                stableAtoms.put(
                        integerTreeSetEntry.getKey(),
                        new EMAtomDefinition(
                                new EMDefinitionStack(EMHadronDefinition.hadron_p, integerTreeSetEntry.getKey()),
                                new EMDefinitionStack(
                                        EMHadronDefinition.hadron_n,
                                        integerTreeSetEntry.getValue().first()),
                                new EMDefinitionStack(EMLeptonDefinition.lepton_e, integerTreeSetEntry.getKey())));
                if (DEBUG_MODE) {
                    TecTech.LOGGER.info(
                            "Added Stable Atom:" + integerTreeSetEntry.getKey()
                                    + ' '
                                    + integerTreeSetEntry.getValue().first()
                                    + ' '
                                    + stableAtoms.get(integerTreeSetEntry.getKey()).getMass());
                }
            }
            for (Map.Entry<Integer, TreeMap<Double, Integer>> integerTreeMapEntry : mostStableUnstableIsotopes
                    .entrySet()) {
                unstableAtoms.put(
                        integerTreeMapEntry.getKey(),
                        new EMAtomDefinition(
                                new EMDefinitionStack(EMHadronDefinition.hadron_p, integerTreeMapEntry.getKey()),
                                new EMDefinitionStack(
                                        EMHadronDefinition.hadron_n,
                                        integerTreeMapEntry.getValue().lastEntry().getValue()),
                                new EMDefinitionStack(EMLeptonDefinition.lepton_e, integerTreeMapEntry.getKey())));
                if (DEBUG_MODE) {
                    TecTech.LOGGER.info(
                            "Added Unstable Atom:" + integerTreeMapEntry.getKey()
                                    + ' '
                                    + integerTreeMapEntry.getValue().lastEntry().getValue()
                                    + ' '
                                    + unstableAtoms.get(integerTreeMapEntry.getKey()).getMass());
                }
            }

            deuterium = new EMAtomDefinition(
                    EMHadronDefinition.hadron_p1,
                    EMHadronDefinition.hadron_n1,
                    EMLeptonDefinition.lepton_e1).getStackForm(1);
            registry.registerForDisplay(deuterium.getDefinition());

            tritium = new EMAtomDefinition(
                    EMHadronDefinition.hadron_p1,
                    EMHadronDefinition.hadron_n2,
                    EMLeptonDefinition.lepton_e1).getStackForm(1);
            registry.registerForDisplay(tritium.getDefinition());

            helium_3 = new EMAtomDefinition(
                    EMHadronDefinition.hadron_p2,
                    EMHadronDefinition.hadron_n1,
                    EMLeptonDefinition.lepton_e2).getStackForm(1);
            registry.registerForDisplay(helium_3.getDefinition());

            alpha = new EMAtomDefinition(EMHadronDefinition.hadron_p2, EMHadronDefinition.hadron_n2).getStackForm(1);
            registry.registerForDisplay(alpha.getDefinition());

            beryllium_8 = new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 4),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 4),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 4)).getStackForm(1);
            registry.registerForDisplay(beryllium_8.getDefinition());

            carbon_14 = new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 6),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 8),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 6)).getStackForm(1);
            registry.registerForDisplay(carbon_14.getDefinition());

            neon_24 = new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 10),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 14),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 10)).getStackForm(1);
            registry.registerForDisplay(neon_24.getDefinition());

            silicon_34 = new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 14),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 20),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 14)).getStackForm(1);
            registry.registerForDisplay(silicon_34.getDefinition());

            uranium_238 = new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 146)).getStackForm(1);
            registry.registerForDisplay(uranium_238.getDefinition());

            uranium_235 = new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 143)).getStackForm(1);
            registry.registerForDisplay(uranium_235.getDefinition());

            TecTech.LOGGER.info(
                    "Diff Mass U : " + (uranium_238.getDefinition().getMass() - uranium_235.getDefinition().getMass()));

            plutonium_239 = new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 145)).getStackForm(1);
            registry.registerForDisplay(plutonium_239.getDefinition());

            plutonium_241 = new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 147)).getStackForm(1);
            registry.registerForDisplay(plutonium_241.getDefinition());

            TecTech.LOGGER.info(
                    "Diff Mass Pu: "
                            + (plutonium_241.getDefinition().getMass() - plutonium_239.getDefinition().getMass()));

            somethingHeavy = (EMAtomDefinition) plutonium_241.getDefinition();

        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }

        if (DEBUG_MODE) {
            TecTech.LOGGER.info("Registered Elemental Matter Class: Atom " + nbtType + ' ' + getClassTypeStatic());
        }

        for (int i = 1; i <= 118; i++) {
            EMAtomDefinition firstStableIsotope = getFirstStableIsotope(i);
            if (firstStableIsotope == null) {
                firstStableIsotope = getBestUnstableIsotope(i);
                if (firstStableIsotope == null) {
                    continue;
                }
            }
            registry.registerForDisplay(firstStableIsotope);
        }
    }

    public static void setTransformations(EMTransformationRegistry transformationInfo) {
        /*----STABLE ATOMS----**/
        refMass = getFirstStableIsotope(1).getMass() * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;

        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(1), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Hydrogen.mGas,
                144);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(2), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Helium.mGas,
                144);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(3), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Lithium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(4), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Beryllium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(5), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Boron,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(6), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Carbon,
                1);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(7), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Nitrogen.mGas,
                144);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(8), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Oxygen.mGas,
                144);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(9), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Fluorine.mGas,
                144);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(11), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Sodium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(12), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Magnesium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(13), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Aluminium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(14), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Silicon,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(15), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Phosphorus,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(16), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Sulfur,
                1);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(17), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Argon.mGas,
                144);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(19), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Potassium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(20), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Calcium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(21), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Scandium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(22), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Titanium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(23), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Vanadium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(24), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Chrome,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(25), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Manganese,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(26), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Iron,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(27), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Cobalt,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(28), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Nickel,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(29), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Copper,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(30), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Zinc,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(31), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Gallium,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(33), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Arsenic,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(37), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Rubidium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(38), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Strontium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(39), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Yttrium,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(41), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Niobium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(42), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Molybdenum,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(46), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Palladium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(47), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Silver,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(48), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Cadmium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(49), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Indium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(50), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Tin,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(51), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Antimony,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(52), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Tellurium,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(55), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Caesium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(56), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Barium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(57), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Lanthanum,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(58), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Cerium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(59), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Praseodymium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(60), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Neodymium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(62), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Samarium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(63), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Europium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(64), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Gadolinium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(65), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Terbium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(66), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Dysprosium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(67), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Holmium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(68), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Erbium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(69), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Thulium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(70), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Ytterbium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(71), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Lutetium,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(73), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Tantalum,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(74), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Tungsten,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(76), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Osmium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(77), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Iridium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(78), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Platinum,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(79), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Gold,
                1);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(80), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Mercury.mFluid,
                144);

        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(82), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Lead,
                1);

        /*----UNSTABLE ATOMS----**/
        refUnstableMass = getFirstStableIsotope(82).getMass() * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;

        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(61), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Promethium,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(83), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Bismuth,
                1);

        transformationInfo.addFluid(
                new EMDefinitionStack(getBestUnstableIsotope(86), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Radon.mGas,
                144);

        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(90), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Thorium,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(95), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Americium,
                1);

        transformationInfo.addFluid(
                new EMDefinitionStack(deuterium.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Deuterium.mGas,
                144);
        transformationInfo.addFluid(
                new EMDefinitionStack(tritium.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Tritium.mGas,
                144);
        transformationInfo.addFluid(
                new EMDefinitionStack(helium_3.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                Helium_3.mGas,
                144);

        transformationInfo.addOredict(
                new EMDefinitionStack(uranium_238.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Uranium /* 238 */,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(uranium_235.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Uranium235,
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(plutonium_239.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Plutonium /* 239 */,
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(plutonium_241.getDefinition(), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                dust,
                Plutonium241,
                1);
    }

    public static EMAtomDefinition getFirstStableIsotope(int element) {
        return stableAtoms.get(element);
    }

    public static EMAtomDefinition getBestUnstableIsotope(int element) {
        return unstableAtoms.get(element);
    }

    @Override
    public int getMatterMassType() {
        return getClassTypeStatic();
    }

    public static int getClassTypeStatic() {
        return 64;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
