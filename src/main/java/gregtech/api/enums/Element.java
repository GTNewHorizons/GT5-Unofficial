package gregtech.api.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * This is some kind of Periodic Table, which I use to determine Properties of the Materials.
 */
public enum Element {

    _NULL(0, 0, 0, "", false),
    H(1, 0, 0, "Hydrogen", false),
    D(1, 1, 0, "Deuterium", true),
    T(1, 2, 0, "Tritium", true),
    He(2, 2, 0, "Helium", false),
    He_3(2, 1, 0, "Helium-3", true),
    Li(3, 4, 0, "Lithium", false),
    Be(4, 5, 0, "Beryllium", false),
    B(5, 5, 0, "Boron", false),
    C(6, 6, 0, "Carbon", false),
    N(7, 7, 0, "Nitrogen", false),
    O(8, 8, 0, "Oxygen", false),
    F(9, 9, 0, "Fluorine", false),
    Ne(10, 10, 0, "Neon", false),
    Na(11, 11, 0, "Sodium", false),
    Mg(12, 12, 0, "Magnesium", false),
    Al(13, 13, 0, "Aluminium", false),
    Si(14, 14, 0, "Silicon", false),
    P(15, 15, 0, "Phosphorus", false),
    S(16, 16, 0, "Sulfur", false),
    Cl(17, 18, 0, "Chlorine", false),
    Ar(18, 22, 0, "Argon", false),
    K(19, 20, 0, "Potassium", false),
    Ca(20, 20, 0, "Calcium", false),
    Sc(21, 24, 0, "Scandium", false),
    Ti(22, 26, 0, "Titanium", false),
    V(23, 28, 0, "Vanadium", false),
    Cr(24, 28, 0, "Chrome", false),
    Mn(25, 30, 0, "Manganese", false),
    Fe(26, 30, 0, "Iron", false),
    Co(27, 32, 0, "Cobalt", false),
    Ni(28, 30, 0, "Nickel", false),
    Cu(29, 34, 0, "Copper", false),
    Zn(30, 35, 0, "Zinc", false),
    Ga(31, 39, 0, "Gallium", false),
    Ge(32, 40, 0, "Germanium", false),
    As(33, 42, 0, "Arsenic", false),
    Se(34, 45, 0, "Selenium", false),
    Br(35, 44, 0, "Bromine", false),
    Kr(36, 48, 0, "Krypton", false),
    Rb(37, 48, 0, "Rubidium", false),
    Sr(38, 49, 0, "Strontium", false),
    Y(39, 50, 0, "Yttrium", false),
    Zr(40, 51, 0, "Zirconium", false),
    Nb(41, 53, 0, "Niobium", false),
    Mo(42, 53, 0, "Molybdenum", false),
    Tc(43, 55, 0, "Technetium", false),
    Ru(44, 57, 0, "Ruthenium", false),
    Rh(45, 58, 0, "Rhodium", false),
    Pd(46, 60, 0, "Palladium", false),
    Ag(47, 60, 0, "Silver", false),
    Cd(48, 64, 0, "Cadmium", false),
    In(49, 65, 0, "Indium", false),
    Sn(50, 68, 0, "Tin", false),
    Sb(51, 70, 0, "Antimony", false),
    Te(52, 75, 0, "Tellurium", false),
    I(53, 74, 0, "Iodine", false),
    Xe(54, 77, 0, "Xenon", false),
    Cs(55, 77, 0, "Caesium", false),
    Ba(56, 81, 0, "Barium", false),
    La(57, 81, 0, "Lantanium", false),
    Ce(58, 82, 0, "Cerium", false),
    Pr(59, 81, 0, "Praseodymium", false),
    Nd(60, 84, 0, "Neodymium", false),
    Pm(61, 83, 0, "Promethium", false),
    Sm(62, 88, 0, "Samarium", false),
    Eu(63, 88, 0, "Europium", false),
    Gd(64, 93, 0, "Gadolinium", false),
    Tb(65, 93, 0, "Terbium", false),
    Dy(66, 96, 0, "Dysprosium", false),
    Ho(67, 97, 0, "Holmium", false),
    Er(68, 99, 0, "Erbium", false),
    Tm(69, 99, 0, "Thulium", false),
    Yb(70, 103, 0, "Ytterbium", false),
    Lu(71, 103, 0, "Lutetium", false),
    Hf(72, 106, 0, "Hafnium", false),
    Ta(73, 107, 0, "Tantalum", false),
    W(74, 109, 0, "Wolframium", false),
    Re(75, 111, 0, "Rhenium", false),
    Os(76, 114, 0, "Osmium", false),
    Ir(77, 115, 0, "Iridium", false),
    Pt(78, 117, 0, "Platinum", false),
    Au(79, 117, 0, "Gold", false),
    Hg(80, 120, 0, "Mercury", false),
    Tl(81, 123, 0, "Thallium", false),
    Pb(82, 125, 0, "Lead", false),
    Bi(83, 125, 0, "Bismuth", false),
    Po(84, 124, 0, "Polonium", false),
    At(85, 124, 0, "Astatine", false),
    Rn(86, 134, 0, "Radon", false),
    Fr(87, 134, 0, "Francium", false),
    Ra(88, 136, 0, "Radium", false),
    Ac(89, 136, 0, "Actinium", false),
    Th(90, 140, 0, "Thorium", false),
    Pa(91, 138, 0, "Protactinium", false),
    U(92, 146, 0, "Uranium", false),
    U_235(92, 143, 0, "Uranium-235", true),
    Np(93, 144, 0, "Neptunium", false),
    Pu(94, 152, 0, "Plutonium", false),
    Pu_241(94, 149, 0, "Plutonium-241", true),
    Am(95, 150, 0, "Americium", false),
    Cm(96, 153, 0, "Curium", false),
    Bk(97, 152, 0, "Berkelium", false),
    Cf(98, 153, 0, "Californium", false),
    Es(99, 153, 0, "Einsteinium", false),
    Fm(100, 157, 0, "Fermium", false),
    Md(101, 157, 0, "Mendelevium", false),
    No(102, 157, 0, "Nobelium", false),
    Lr(103, 159, 0, "Lawrencium", false),
    Rf(104, 161, 0, "Rutherfordium", false),
    Db(105, 163, 0, "Dubnium", false),
    Sg(106, 165, 0, "Seaborgium", false),
    Bh(107, 163, 0, "Bohrium", false),
    Hs(108, 169, 0, "Hassium", false),
    Mt(109, 167, 0, "Meitnerium", false),
    Ds(110, 171, 0, "Darmstadtium", false),
    Rg(111, 169, 0, "Roentgenium", false),
    Cn(112, 173, 0, "Copernicium", false),
    Nh(113, 171, 0, "Nihonium", false),
    Fl(114, 175, 0, "Flerovium", false),
    Mc(115, 173, 0, "Moscovium", false),
    Lv(116, 177, 0, "Livermorium", false),
    Ts(117, 177, 0, "Teness", false),
    Og(118, 176, 0, "Oganesson", false),
    Tn(125, 198, 0, "Tritanium", false),

    SpFe(26, 42, 0, "Meteoric Iron", false),
    De(22, 27, 0, "Desh", false),
    Oh(76, 125, 0, "Oriharukon", false),
    Di(500, 500, 0, "Dimensionally Transcendent Matter", false),

    Ma(0, 0, 100, "Magic", false),
    Nq(130, 200, 0, "Naquadah", false),
    Nt(0, 100, 0, "Neutronium", false),

    Tr(123, 203, 0, "Tiberium", false);

    public final long mProtons;
    public final long mNeutrons;
    public final long mAdditionalMass;
    public final String mName;
    public final boolean mIsIsotope;

    /**
     * Links to every pure Material containing just this Element.
     */
    // bartworks.system.material.werkstoff_loaders.registration.BridgeMaterialsLoader reassigns it, so no final here
    public ArrayList<Materials> mLinkedMaterials = new ArrayList<>();

    /**
     * @param aProtons  Amount of Protons. Antiprotons if negative.
     * @param aNeutrons Amount of Neutrons. Antineutrons if negative. (I could have made mistakes with the
     *                  Neutron amount calculation, please tell me if I did something wrong)
     * @param aName     Name of the Element
     */
    Element(long aProtons, long aNeutrons, long aAdditionalMass, String aName, boolean aIsIsotope) {
        mProtons = aProtons;
        mNeutrons = aNeutrons;
        mAdditionalMass = aAdditionalMass;
        mName = aName;
        mIsIsotope = aIsIsotope;
        Companion.VALUES.put(name(), this);
    }

    @Nonnull
    public static Element get(String aMaterialName) {
        return Companion.VALUES.getOrDefault(aMaterialName, _NULL);
    }

    public long getProtons() {
        return mProtons;
    }

    public long getNeutrons() {
        return mNeutrons;
    }

    public long getMass() {
        return mProtons + mNeutrons + mAdditionalMass;
    }

    @Override
    public String toString() {
        if (this == Element._NULL) return "Empty";
        return super.toString().replaceAll("_", "-");
    }

    /**
     * A companion object to workaround java limitations
     */
    private static final class Companion {

        /**
         * Why is this a separate map and populated by enum constructor instead of a Map prepoluated with values()?
         * Because apparently there are people hacking into this enum via EnumHelper.
         */
        private static final Map<String, Element> VALUES = new HashMap<>();
    }
}
