package gregtech.api.enums.materials2;

import net.minecraft.util.EnumChatFormatting;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.material.GTMaterialProperties;
import gregtech.api.util.CustomGlyphs;

// One-time output of scripts/mu/add_chemical_formulas.py (RETIRED, see its module docstring); hand-maintained
// from here -- edit this file directly.
/// The chemical-formula maintenance surface: one [GTMaterialProperties#FORMULA] entry per ported gregtech
/// material whose legacy `Materials#mChemicalFormula` was a real formula -- the explicit `MaterialsInit`
/// overrides verbatim (cross-material concatenations flattened to the literal the legacy loader computed,
/// [GTMaterialProperties#FORMULA_LOCALIZED] added where the legacy call used its localized overload), every
/// other material's constructor-derived string as pinned data. Werkstoff-backed and gtpp-only materials
/// carry no entry here: their [GTMaterialProperties#FORMULA] lines live in [Materials2Materials] alongside
/// the rest of their declaration. Kept separate from [Materials2Materials] so its hand-maintained
/// declaration blocks stay uncluttered by ~1200 formula lines.
public class Materials2Formulas {

    // spotless:off
    public static void init() {
        initPart1();
        initPart2();
        initPart3();
        initPart4();
        initPart5();
        initPart6();
        initPart7();
        initPart8();
        initPart9();
        initPart10();
        initPart11();
    }

    private static void initPart1() {
        MaterialLibAPI.editMaterial("gregtech", "Empty")
            .setProperty(GTMaterialProperties.FORMULA, "Empty");
        MaterialLibAPI.editMaterial("gregtech", "Hydrogen")
            .setProperty(GTMaterialProperties.FORMULA, "H");
        MaterialLibAPI.editMaterial("gregtech", "Deuterium")
            .setProperty(GTMaterialProperties.FORMULA, "D");
        MaterialLibAPI.editMaterial("gregtech", "Tritium")
            .setProperty(GTMaterialProperties.FORMULA, "T");
        MaterialLibAPI.editMaterial("gregtech", "Helium")
            .setProperty(GTMaterialProperties.FORMULA, "He");
        MaterialLibAPI.editMaterial("gregtech", "Helium_3")
            .setProperty(GTMaterialProperties.FORMULA, "He-3");
        MaterialLibAPI.editMaterial("gregtech", "Lithium")
            .setProperty(GTMaterialProperties.FORMULA, "Li");
        MaterialLibAPI.editMaterial("gregtech", "Beryllium")
            .setProperty(GTMaterialProperties.FORMULA, "Be");
        MaterialLibAPI.editMaterial("gregtech", "Boron")
            .setProperty(GTMaterialProperties.FORMULA, "B");
        MaterialLibAPI.editMaterial("gregtech", "Carbon")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "Nitrogen")
            .setProperty(GTMaterialProperties.FORMULA, "N");
        MaterialLibAPI.editMaterial("gregtech", "Oxygen")
            .setProperty(GTMaterialProperties.FORMULA, "O");
        MaterialLibAPI.editMaterial("gregtech", "Fluorine")
            .setProperty(GTMaterialProperties.FORMULA, "F");
        MaterialLibAPI.editMaterial("gregtech", "Sodium")
            .setProperty(GTMaterialProperties.FORMULA, "Na");
        MaterialLibAPI.editMaterial("gregtech", "Magnesium")
            .setProperty(GTMaterialProperties.FORMULA, "Mg");
        MaterialLibAPI.editMaterial("gregtech", "Aluminium")
            .setProperty(GTMaterialProperties.FORMULA, "Al");
        MaterialLibAPI.editMaterial("gregtech", "Silicon")
            .setProperty(GTMaterialProperties.FORMULA, "Si");
        MaterialLibAPI.editMaterial("gregtech", "Phosphorus")
            .setProperty(GTMaterialProperties.FORMULA, "P");
        MaterialLibAPI.editMaterial("gregtech", "Sulfur")
            .setProperty(GTMaterialProperties.FORMULA, "S");
        MaterialLibAPI.editMaterial("gregtech", "Chlorine")
            .setProperty(GTMaterialProperties.FORMULA, "Cl");
        MaterialLibAPI.editMaterial("gregtech", "Argon")
            .setProperty(GTMaterialProperties.FORMULA, "Ar");
        MaterialLibAPI.editMaterial("gregtech", "Potassium")
            .setProperty(GTMaterialProperties.FORMULA, "K");
        MaterialLibAPI.editMaterial("gregtech", "Scandium")
            .setProperty(GTMaterialProperties.FORMULA, "Sc");
        MaterialLibAPI.editMaterial("gregtech", "Titanium")
            .setProperty(GTMaterialProperties.FORMULA, "Ti");
        MaterialLibAPI.editMaterial("gregtech", "Vanadium")
            .setProperty(GTMaterialProperties.FORMULA, "V");
        MaterialLibAPI.editMaterial("gregtech", "Chrome")
            .setProperty(GTMaterialProperties.FORMULA, "Cr");
        MaterialLibAPI.editMaterial("gregtech", "Manganese")
            .setProperty(GTMaterialProperties.FORMULA, "Mn");
        MaterialLibAPI.editMaterial("gregtech", "Iron")
            .setProperty(GTMaterialProperties.FORMULA, "Fe");
        MaterialLibAPI.editMaterial("gregtech", "Cobalt")
            .setProperty(GTMaterialProperties.FORMULA, "Co");
        MaterialLibAPI.editMaterial("gregtech", "Nickel")
            .setProperty(GTMaterialProperties.FORMULA, "Ni");
        MaterialLibAPI.editMaterial("gregtech", "Copper")
            .setProperty(GTMaterialProperties.FORMULA, "Cu");
        MaterialLibAPI.editMaterial("gregtech", "Zinc")
            .setProperty(GTMaterialProperties.FORMULA, "Zn");
        MaterialLibAPI.editMaterial("gregtech", "Gallium")
            .setProperty(GTMaterialProperties.FORMULA, "Ga");
        MaterialLibAPI.editMaterial("gregtech", "Arsenic")
            .setProperty(GTMaterialProperties.FORMULA, "As");
        MaterialLibAPI.editMaterial("gregtech", "Rubidium")
            .setProperty(GTMaterialProperties.FORMULA, "Rb");
        MaterialLibAPI.editMaterial("gregtech", "Strontium")
            .setProperty(GTMaterialProperties.FORMULA, "Sr");
        MaterialLibAPI.editMaterial("gregtech", "Yttrium")
            .setProperty(GTMaterialProperties.FORMULA, "Y");
        MaterialLibAPI.editMaterial("gregtech", "Niobium")
            .setProperty(GTMaterialProperties.FORMULA, "Nb");
        MaterialLibAPI.editMaterial("gregtech", "Molybdenum")
            .setProperty(GTMaterialProperties.FORMULA, "Mo");
        MaterialLibAPI.editMaterial("gregtech", "Palladium")
            .setProperty(GTMaterialProperties.FORMULA, "Pd");
        MaterialLibAPI.editMaterial("gregtech", "Silver")
            .setProperty(GTMaterialProperties.FORMULA, "Ag");
        MaterialLibAPI.editMaterial("gregtech", "Cadmium")
            .setProperty(GTMaterialProperties.FORMULA, "Cd");
        MaterialLibAPI.editMaterial("gregtech", "Indium")
            .setProperty(GTMaterialProperties.FORMULA, "In");
        MaterialLibAPI.editMaterial("gregtech", "Tin")
            .setProperty(GTMaterialProperties.FORMULA, "Sn");
        MaterialLibAPI.editMaterial("gregtech", "Antimony")
            .setProperty(GTMaterialProperties.FORMULA, "Sb");
        MaterialLibAPI.editMaterial("gregtech", "Caesium")
            .setProperty(GTMaterialProperties.FORMULA, "Cs");
        MaterialLibAPI.editMaterial("gregtech", "Barium")
            .setProperty(GTMaterialProperties.FORMULA, "Ba");
        MaterialLibAPI.editMaterial("gregtech", "Lanthanum")
            .setProperty(GTMaterialProperties.FORMULA, "La");
        MaterialLibAPI.editMaterial("gregtech", "Cerium")
            .setProperty(GTMaterialProperties.FORMULA, "Ce");
        MaterialLibAPI.editMaterial("gregtech", "Praseodymium")
            .setProperty(GTMaterialProperties.FORMULA, "Pr");
        MaterialLibAPI.editMaterial("gregtech", "Neodymium")
            .setProperty(GTMaterialProperties.FORMULA, "Nd");
        MaterialLibAPI.editMaterial("gregtech", "Promethium")
            .setProperty(GTMaterialProperties.FORMULA, "Pm");
        MaterialLibAPI.editMaterial("gregtech", "Samarium")
            .setProperty(GTMaterialProperties.FORMULA, "Sm");
        MaterialLibAPI.editMaterial("gregtech", "Europium")
            .setProperty(GTMaterialProperties.FORMULA, "Eu");
        MaterialLibAPI.editMaterial("gregtech", "Gadolinium")
            .setProperty(GTMaterialProperties.FORMULA, "Gd");
        MaterialLibAPI.editMaterial("gregtech", "Terbium")
            .setProperty(GTMaterialProperties.FORMULA, "Tb");
        MaterialLibAPI.editMaterial("gregtech", "Dysprosium")
            .setProperty(GTMaterialProperties.FORMULA, "Dy");
        MaterialLibAPI.editMaterial("gregtech", "Holmium")
            .setProperty(GTMaterialProperties.FORMULA, "Ho");
        MaterialLibAPI.editMaterial("gregtech", "Erbium")
            .setProperty(GTMaterialProperties.FORMULA, "Er");
        MaterialLibAPI.editMaterial("gregtech", "Thulium")
            .setProperty(GTMaterialProperties.FORMULA, "Tm");
    }

    private static void initPart2() {
        MaterialLibAPI.editMaterial("gregtech", "Ytterbium")
            .setProperty(GTMaterialProperties.FORMULA, "Yb");
        MaterialLibAPI.editMaterial("gregtech", "Lutetium")
            .setProperty(GTMaterialProperties.FORMULA, "Lu");
        MaterialLibAPI.editMaterial("gregtech", "Tantalum")
            .setProperty(GTMaterialProperties.FORMULA, "Ta");
        MaterialLibAPI.editMaterial("gregtech", "Tungsten")
            .setProperty(GTMaterialProperties.FORMULA, "W");
        MaterialLibAPI.editMaterial("gregtech", "Osmium")
            .setProperty(GTMaterialProperties.FORMULA, "Os");
        MaterialLibAPI.editMaterial("gregtech", "Iridium")
            .setProperty(GTMaterialProperties.FORMULA, "Ir");
        MaterialLibAPI.editMaterial("gregtech", "Platinum")
            .setProperty(GTMaterialProperties.FORMULA, "Pt");
        MaterialLibAPI.editMaterial("gregtech", "Gold")
            .setProperty(GTMaterialProperties.FORMULA, "Au");
        MaterialLibAPI.editMaterial("gregtech", "Mercury")
            .setProperty(GTMaterialProperties.FORMULA, "Hg");
        MaterialLibAPI.editMaterial("gregtech", "Lead")
            .setProperty(GTMaterialProperties.FORMULA, "Pb");
        MaterialLibAPI.editMaterial("gregtech", "Bismuth")
            .setProperty(GTMaterialProperties.FORMULA, "Bi");
        MaterialLibAPI.editMaterial("gregtech", "Radon")
            .setProperty(GTMaterialProperties.FORMULA, "Rn");
        MaterialLibAPI.editMaterial("gregtech", "Francium_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "Fr");
        MaterialLibAPI.editMaterial("gregtech", "Thorium")
            .setProperty(GTMaterialProperties.FORMULA, "Th");
        MaterialLibAPI.editMaterial("gregtech", "Uranium235")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.SUPERSCRIPT2 + CustomGlyphs.SUPERSCRIPT3 + CustomGlyphs.SUPERSCRIPT5 + "U");
        MaterialLibAPI.editMaterial("gregtech", "Uranium")
            .setProperty(GTMaterialProperties.FORMULA, "U");
        MaterialLibAPI.editMaterial("gregtech", "Plutonium")
            .setProperty(GTMaterialProperties.FORMULA, "Pu");
        MaterialLibAPI.editMaterial("gregtech", "Plutonium241")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.SUPERSCRIPT2 + CustomGlyphs.SUPERSCRIPT4 + CustomGlyphs.SUPERSCRIPT1 + "Pu");
        MaterialLibAPI.editMaterial("gregtech", "Americium")
            .setProperty(GTMaterialProperties.FORMULA, "Am");
        MaterialLibAPI.editMaterial("gregtech", "TengamRaw")
            .setProperty(GTMaterialProperties.FORMULA, "M" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "TengamPurified")
            .setProperty(GTMaterialProperties.FORMULA, "M");
        MaterialLibAPI.editMaterial("gregtech", "TengamAttuned")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.HIGH_VOLTAGE + "M" + CustomGlyphs.MAGNET);
        MaterialLibAPI.editMaterial("gregtech", "InactiveCosmicSolder")
            .setProperty(GTMaterialProperties.FORMULA, "✦✦✦✦✦");
        MaterialLibAPI.editMaterial("gregtech", "BoundlessCosmicSolder")
            .setProperty(GTMaterialProperties.FORMULA, "✧✦✧✦✧");
        MaterialLibAPI.editMaterial("gregtech", "ComputationBase")
            .setProperty(GTMaterialProperties.FORMULA, "01010011 01000111");
        MaterialLibAPI.editMaterial("gregtech", "HellishMetal")
            .setProperty(GTMaterialProperties.FORMULA, "RhMa");
        MaterialLibAPI.editMaterial("gregtech", "ExcitedDTSC")
            .setProperty(GTMaterialProperties.FORMULA, "[-Stellar-Stellar-]")
            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true);
        MaterialLibAPI.editMaterial("gregtech", "Neutronium")
            .setProperty(GTMaterialProperties.FORMULA, "Nt");
        MaterialLibAPI.editMaterial("gregtech", "DimensionallyTranscendentStellarCatalyst")
            .setProperty(GTMaterialProperties.FORMULA, "Stellar")
            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true);
        MaterialLibAPI.editMaterial("gregtech", "SuperconductorUIVBase")
            .setProperty(GTMaterialProperties.FORMULA, "(C₁₄Os₁₁O₇Ag₃SpH₂O)₄?₁" + CustomGlyphs.SUBSCRIPT0 + "(Fs⚶)₆(⌘☯☯⌘)₅");
        MaterialLibAPI.editMaterial("gregtech", "Netherite")
            .setProperty(GTMaterialProperties.FORMULA, "NrAuMa*");
        MaterialLibAPI.editMaterial("gregtech", "SuperconductorUMVBase")
            .setProperty(GTMaterialProperties.FORMULA, "?₆Or₃(Hy⚶)₁₁(((CW)₇Ti₃)₃" + CustomGlyphs.FIRE + CustomGlyphs.EARTH + CustomGlyphs.CHAOS + ")₅۞₂");
        MaterialLibAPI.editMaterial("gregtech", "Universium")
            .setProperty(GTMaterialProperties.FORMULA, "\u03A3" + EnumChatFormatting.OBFUSCATED + "X");
        MaterialLibAPI.editMaterial("gregtech", "Eternity")
            .setProperty(GTMaterialProperties.FORMULA, "En⦼");
        MaterialLibAPI.editMaterial("gregtech", "Magmatter")
            .setProperty(GTMaterialProperties.FORMULA, "M⎋");
        MaterialLibAPI.editMaterial("gregtech", "QuarkGluonPlasma")
            .setProperty(GTMaterialProperties.FORMULA, EnumChatFormatting.OBFUSCATED + "X" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + "g" + EnumChatFormatting.OBFUSCATED + "X");
        MaterialLibAPI.editMaterial("gregtech", "PhononMedium")
            .setProperty(GTMaterialProperties.FORMULA, "((Si₅O₁" + CustomGlyphs.SUBSCRIPT0 + "Fe)₃(Bi₂Te₃)₄ZrO₂Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₅Og*Pr₁₅((C₁₄Os₁₁O₇Ag₃SpH₂O)₄?₁" + CustomGlyphs.SUBSCRIPT0 + "(Fs⚶)₆(⌘☯☯⌘)₅)₆〄₄");
        MaterialLibAPI.editMaterial("gregtech", "PhononCrystalSolution")
            .setProperty(GTMaterialProperties.FORMULA, "〄");
        MaterialLibAPI.editMaterial("gregtech", "SixPhasedCopper")
            .setProperty(GTMaterialProperties.FORMULA, "✢");
        MaterialLibAPI.editMaterial("gregtech", "Mellion")
            .setProperty(GTMaterialProperties.FORMULA, "Tn₁₁Or₈Rb₁₁(" + CustomGlyphs.BRIMSTONE + "Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₇⽕₁₃?₁₃");
        MaterialLibAPI.editMaterial("gregtech", "Creon")
            .setProperty(GTMaterialProperties.FORMULA, "⸎");
        MaterialLibAPI.editMaterial("gregtech", "protohalkonitebase")
            .setProperty(GTMaterialProperties.FORMULA, "(TsЖ)₂(W₈Nq*₇((SiO₂)₂₆₂₄₄C₉)₄C₄V₃SpPu)₂Tt₂((CW)₇Ti₃)₃" + CustomGlyphs.FIRE + CustomGlyphs.EARTH + CustomGlyphs.CHAOS + "If*");
        MaterialLibAPI.editMaterial("gregtech", "hotprotohalkonite")
            .setProperty(GTMaterialProperties.FORMULA, "(TsЖ)₂(W₈Nq*₇((SiO₂)₂₆₂₄₄C₉)₄C₄V₃SpPu)₂Tt₂((CW)₇Ti₃)₃" + CustomGlyphs.FIRE + CustomGlyphs.EARTH + CustomGlyphs.CHAOS + "If*");
        MaterialLibAPI.editMaterial("gregtech", "protohalkonite")
            .setProperty(GTMaterialProperties.FORMULA, "(TsЖ)₂(W₈Nq*₇((SiO₂)₂₆₂₄₄C₉)₄C₄V₃SpPu)₂Tt₂((CW)₇Ti₃)₃" + CustomGlyphs.FIRE + CustomGlyphs.EARTH + CustomGlyphs.CHAOS + "If*");
        MaterialLibAPI.editMaterial("gregtech", "prismaticnaquadah")
            .setProperty(GTMaterialProperties.FORMULA, "NqΔ");
        MaterialLibAPI.editMaterial("gregtech", "BiocatalyzedPropulsionFluid")
            .setProperty(GTMaterialProperties.FORMULA, "ඞ");
        MaterialLibAPI.editMaterial("gregtech", "Chlorite")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₃Mg₂(Al₂O₃)(SiO₂)₃(H₂O)₄O₅");
        MaterialLibAPI.editMaterial("gregtech", "Staurolite")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₂Al₉(SiO₂)₄O₁₆H");
        MaterialLibAPI.editMaterial("gregtech", "Cordierite")
            .setProperty(GTMaterialProperties.FORMULA, "FeMg(SiO₂)₅(Al₂O₃)O₅");
        MaterialLibAPI.editMaterial("gregtech", "Datolite")
            .setProperty(GTMaterialProperties.FORMULA, "CaB(SiO₂)O₃H");
        MaterialLibAPI.editMaterial("gregtech", "Plagioclase")
            .setProperty(GTMaterialProperties.FORMULA, "NaAlSi₃O₈");
        MaterialLibAPI.editMaterial("gregtech", "UnformedHexanite")
            .setProperty(GTMaterialProperties.FORMULA, "Hx*");
        MaterialLibAPI.editMaterial("gregtech", "Hexanite")
            .setProperty(GTMaterialProperties.FORMULA, "Hx⚶");
        MaterialLibAPI.editMaterial("gregtech", "FranciumHydroxide")
            .setProperty(GTMaterialProperties.FORMULA, "FrOH");
        MaterialLibAPI.editMaterial("gregtech", "PhosphorusPentachloride")
            .setProperty(GTMaterialProperties.FORMULA, "PCl₅");
        MaterialLibAPI.editMaterial("gregtech", "ChlorosulfonicAcid")
            .setProperty(GTMaterialProperties.FORMULA, "H₂SO₃Cl");
        MaterialLibAPI.editMaterial("gregtech", "Shijima")
            .setProperty(GTMaterialProperties.FORMULA, "((Nh₂Ma)₃" + CustomGlyphs.CIRCLE_CROSS + "C₆)₈Tb₇Tc₄(" + CustomGlyphs.FIXED_JAPANESE_OPENING_QUOTE + "Fe/C⌋)₄Fl₃If");
        MaterialLibAPI.editMaterial("gregtech", "Churitsu")
            .setProperty(GTMaterialProperties.FORMULA, "(SnFe)₈(Ru₂Ir)₇(Kn₅Nq₉)₄(Ad₅Nq₂La₃)₄Cf₃" + "(Co₇Cr₇Mn₄Ti₂)₃(" + CustomGlyphs.AIR + CustomGlyphs.EARTH + CustomGlyphs.FIRE + CustomGlyphs.WATER + ")(SiC)GaAmPdBiGe");
        MaterialLibAPI.editMaterial("gregtech", "Amalgatite")
            .setProperty(GTMaterialProperties.FORMULA, EnumChatFormatting.OBFUSCATED + "?????????");
        MaterialLibAPI.editMaterial("gregtech", "Manasteel")
            .setProperty(GTMaterialProperties.FORMULA, "Ms");
    }

    private static void initPart3() {
        MaterialLibAPI.editMaterial("gregtech", "Terrasteel")
            .setProperty(GTMaterialProperties.FORMULA, "Tr");
        MaterialLibAPI.editMaterial("gregtech", "ElvenElementium")
            .setProperty(GTMaterialProperties.FORMULA, "Ef");
        MaterialLibAPI.editMaterial("gregtech", "Livingrock")
            .setProperty(GTMaterialProperties.FORMULA, "Lv");
        MaterialLibAPI.editMaterial("gregtech", "GaiaSpirit")
            .setProperty(GTMaterialProperties.FORMULA, "Gs");
        MaterialLibAPI.editMaterial("gregtech", "Livingwood")
            .setProperty(GTMaterialProperties.FORMULA, "Lw");
        MaterialLibAPI.editMaterial("gregtech", "Dreamwood")
            .setProperty(GTMaterialProperties.FORMULA, "Dw");
        MaterialLibAPI.editMaterial("gregtech", "ManaDiamond")
            .setProperty(GTMaterialProperties.FORMULA, "Ma₄C");
        MaterialLibAPI.editMaterial("gregtech", "BotaniaDragonstone")
            .setProperty(GTMaterialProperties.FORMULA, "Dg");
        MaterialLibAPI.editMaterial("gregtech", "PlatinumGroupSludge")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "Au" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "Pt" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "Pd" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??");
        MaterialLibAPI.editMaterial("gregtech", "PotassiumHydroxide_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "KOH");
        MaterialLibAPI.editMaterial("gregtech", "CaesiumHydroxide_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "CsOH");
        MaterialLibAPI.editMaterial("gregtech", "Bronze")
            .setProperty(GTMaterialProperties.FORMULA, "SnCu₃");
        MaterialLibAPI.editMaterial("gregtech", "Brass")
            .setProperty(GTMaterialProperties.FORMULA, "ZnCu₃");
        MaterialLibAPI.editMaterial("gregtech", "Invar")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₂Ni");
        MaterialLibAPI.editMaterial("gregtech", "Electrum")
            .setProperty(GTMaterialProperties.FORMULA, "AgAu");
        MaterialLibAPI.editMaterial("gregtech", "CastIron")
            .setProperty(GTMaterialProperties.FORMULA, "Fe*");
        MaterialLibAPI.editMaterial("gregtech", "Steel")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C");
        MaterialLibAPI.editMaterial("gregtech", "StainlessSteel")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₆CrMnNi");
        MaterialLibAPI.editMaterial("gregtech", "PigIron")
            .setProperty(GTMaterialProperties.FORMULA, "¿Fe?");
        MaterialLibAPI.editMaterial("gregtech", "RedAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "Cu(Si(FeS₂)₅(CrAl₂O₃)Hg₃)₄");
        MaterialLibAPI.editMaterial("gregtech", "BlueAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "AgRp₄");
        MaterialLibAPI.editMaterial("gregtech", "Cupronickel")
            .setProperty(GTMaterialProperties.FORMULA, "CuNi");
        MaterialLibAPI.editMaterial("gregtech", "Nichrome")
            .setProperty(GTMaterialProperties.FORMULA, "Ni₄Cr");
        MaterialLibAPI.editMaterial("gregtech", "Kanthal")
            .setProperty(GTMaterialProperties.FORMULA, "FeAlCr");
        MaterialLibAPI.editMaterial("gregtech", "Magnalium")
            .setProperty(GTMaterialProperties.FORMULA, "MgAl₂");
        MaterialLibAPI.editMaterial("gregtech", "SolderingAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "Sn₉Sb");
        MaterialLibAPI.editMaterial("gregtech", "BatteryAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "Pb₄Sb");
        MaterialLibAPI.editMaterial("gregtech", "TungstenSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)W");
        MaterialLibAPI.editMaterial("gregtech", "Osmiridium")
            .setProperty(GTMaterialProperties.FORMULA, "Ir₃Os");
        MaterialLibAPI.editMaterial("gregtech", "Sunnarium")
            .setProperty(GTMaterialProperties.FORMULA, "Su");
        MaterialLibAPI.editMaterial("gregtech", "Adamantium")
            .setProperty(GTMaterialProperties.FORMULA, "Ad");
        MaterialLibAPI.editMaterial("gregtech", "ElectrumFlux")
            .setProperty(GTMaterialProperties.FORMULA, "The formula is too long...")
            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true);
        MaterialLibAPI.editMaterial("gregtech", "Enderium")
            .setProperty(GTMaterialProperties.FORMULA, "(Sn₂Ag₂Pt₂)₂(FeMa)(BeK₄N₅Ma₆)");
        MaterialLibAPI.editMaterial("gregtech", "InfusedGold")
            .setProperty(GTMaterialProperties.FORMULA, "AuMa*");
        MaterialLibAPI.editMaterial("gregtech", "Naquadah")
            .setProperty(GTMaterialProperties.FORMULA, "Nq");
        MaterialLibAPI.editMaterial("gregtech", "NaquadahAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "Nq₂KeC");
        MaterialLibAPI.editMaterial("gregtech", "NaquadahEnriched")
            .setProperty(GTMaterialProperties.FORMULA, "Nq+");
        MaterialLibAPI.editMaterial("gregtech", "Naquadria")
            .setProperty(GTMaterialProperties.FORMULA, "Nq*");
        MaterialLibAPI.editMaterial("gregtech", "Duranium")
            .setProperty(GTMaterialProperties.FORMULA, "Du");
        MaterialLibAPI.editMaterial("gregtech", "Tritanium")
            .setProperty(GTMaterialProperties.FORMULA, "Tn");
        MaterialLibAPI.editMaterial("gregtech", "Thaumium")
            .setProperty(GTMaterialProperties.FORMULA, "FeMa");
        MaterialLibAPI.editMaterial("gregtech", "Mithril")
            .setProperty(GTMaterialProperties.FORMULA, "Pt₂(FeMa)");
        MaterialLibAPI.editMaterial("gregtech", "AstralSilver")
            .setProperty(GTMaterialProperties.FORMULA, "Ag₂(FeMa)");
        MaterialLibAPI.editMaterial("gregtech", "BlackSteel")
            .setProperty(GTMaterialProperties.FORMULA, "Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃");
        MaterialLibAPI.editMaterial("gregtech", "DamascusSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₉Mn₄Cr₄CSiV");
        MaterialLibAPI.editMaterial("gregtech", "ShadowIron")
            .setProperty(GTMaterialProperties.FORMULA, "Fe(FeMa)₃");
        MaterialLibAPI.editMaterial("gregtech", "ShadowSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)(FeMa)₃");
        MaterialLibAPI.editMaterial("gregtech", "IronWood")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₉((COH₃)Ma)₉Au");
        MaterialLibAPI.editMaterial("gregtech", "Steeleaf")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)Ma");
        MaterialLibAPI.editMaterial("gregtech", "MeteoricIron")
            .setProperty(GTMaterialProperties.FORMULA, "SpFe");
        MaterialLibAPI.editMaterial("gregtech", "MeteoricSteel")
            .setProperty(GTMaterialProperties.FORMULA, "SpFe₅" + CustomGlyphs.SUBSCRIPT0 + "C");
        MaterialLibAPI.editMaterial("gregtech", "DarkIron")
            .setProperty(GTMaterialProperties.FORMULA, "Sp₆Fe" + CustomGlyphs.PICKAXE);
        MaterialLibAPI.editMaterial("gregtech", "CobaltBrass")
            .setProperty(GTMaterialProperties.FORMULA, "(ZnCu₃)₇SnCo");
        MaterialLibAPI.editMaterial("gregtech", "Ultimet")
            .setProperty(GTMaterialProperties.FORMULA, "Co₅Cr₂NiMo");
        MaterialLibAPI.editMaterial("gregtech", "AnnealedCopper")
            .setProperty(GTMaterialProperties.FORMULA, "Cu*");
        MaterialLibAPI.editMaterial("gregtech", "FierySteel")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.BRIMSTONE + "Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C");
        MaterialLibAPI.editMaterial("gregtech", "Firestone")
            .setProperty(GTMaterialProperties.FORMULA, "⽕");
        MaterialLibAPI.editMaterial("gregtech", "RedSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(CuAg₄)(BiZnCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₂(Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃)₄");
        MaterialLibAPI.editMaterial("gregtech", "BlueSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(CuAu₄)(ZnCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₂(Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃)₄");
        MaterialLibAPI.editMaterial("gregtech", "SterlingSilver")
            .setProperty(GTMaterialProperties.FORMULA, "CuAg₄");
    }

    private static void initPart4() {
        MaterialLibAPI.editMaterial("gregtech", "RoseGold")
            .setProperty(GTMaterialProperties.FORMULA, "CuAu₄");
        MaterialLibAPI.editMaterial("gregtech", "BlackBronze")
            .setProperty(GTMaterialProperties.FORMULA, "AuAgCu₃");
        MaterialLibAPI.editMaterial("gregtech", "BismuthBronze")
            .setProperty(GTMaterialProperties.FORMULA, "BiZnCu₃");
        MaterialLibAPI.editMaterial("gregtech", "IronMagnetic")
            .setProperty(GTMaterialProperties.FORMULA, "Fe" + CustomGlyphs.MAGNET);
        MaterialLibAPI.editMaterial("gregtech", "SteelMagnetic")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C" + CustomGlyphs.MAGNET);
        MaterialLibAPI.editMaterial("gregtech", "NeodymiumMagnetic")
            .setProperty(GTMaterialProperties.FORMULA, "Nd" + CustomGlyphs.MAGNET);
        MaterialLibAPI.editMaterial("gregtech", "VanadiumGallium")
            .setProperty(GTMaterialProperties.FORMULA, "V₃Ga");
        MaterialLibAPI.editMaterial("gregtech", "YttriumBariumCuprate")
            .setProperty(GTMaterialProperties.FORMULA, "YBa₂Cu₃O₇");
        MaterialLibAPI.editMaterial("gregtech", "NiobiumNitride")
            .setProperty(GTMaterialProperties.FORMULA, "NbN");
        MaterialLibAPI.editMaterial("gregtech", "NiobiumTitanium")
            .setProperty(GTMaterialProperties.FORMULA, "NbTi");
        MaterialLibAPI.editMaterial("gregtech", "ChromiumDioxide")
            .setProperty(GTMaterialProperties.FORMULA, "CrO₂");
        MaterialLibAPI.editMaterial("gregtech", "Knightmetal")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₂Ma");
        MaterialLibAPI.editMaterial("gregtech", "TinAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "SnFe");
        MaterialLibAPI.editMaterial("gregtech", "DarkSteel")
            .setProperty(GTMaterialProperties.FORMULA, "((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)CSi)C(MgFeSi₂O₈)");
        MaterialLibAPI.editMaterial("gregtech", "ElectricalSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)CSi");
        MaterialLibAPI.editMaterial("gregtech", "EnergeticAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "(((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)FeAg)Au(Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃)");
        MaterialLibAPI.editMaterial("gregtech", "VibrantAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "((((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)FeAg)Au(Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃))((BeK₄N₅Ma₆)(C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa))Cr");
        MaterialLibAPI.editMaterial("gregtech", "Shadow")
            .setProperty(GTMaterialProperties.FORMULA, "Sh₆(FeMa₃)₂");
        MaterialLibAPI.editMaterial("gregtech", "ConductiveIron")
            .setProperty(GTMaterialProperties.FORMULA, "((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)FeAg");
        MaterialLibAPI.editMaterial("gregtech", "TungstenCarbide")
            .setProperty(GTMaterialProperties.FORMULA, "WC");
        MaterialLibAPI.editMaterial("gregtech", "VanadiumSteel")
            .setProperty(GTMaterialProperties.FORMULA, "VCr(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₇");
        MaterialLibAPI.editMaterial("gregtech", "HSSG")
            .setProperty(GTMaterialProperties.FORMULA, "((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)W)₅CrMo₂V");
        MaterialLibAPI.editMaterial("gregtech", "HSSE")
            .setProperty(GTMaterialProperties.FORMULA, "(((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)W)₅CrMo₂V)₆CoMnSi");
        MaterialLibAPI.editMaterial("gregtech", "HSSS")
            .setProperty(GTMaterialProperties.FORMULA, "(((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)W)₅CrMo₂V)₆Ir₂Os");
        MaterialLibAPI.editMaterial("gregtech", "Rutile")
            .setProperty(GTMaterialProperties.FORMULA, "TiO₂");
        MaterialLibAPI.editMaterial("gregtech", "Titaniumtetrachloride")
            .setProperty(GTMaterialProperties.FORMULA, "TiCl₄");
        MaterialLibAPI.editMaterial("gregtech", "Magnesiumchloride")
            .setProperty(GTMaterialProperties.FORMULA, "MgCl₂");
        MaterialLibAPI.editMaterial("gregtech", "PulsatingIron")
            .setProperty(GTMaterialProperties.FORMULA, "Fe(BeK₄N₅Ma₆)((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)");
        MaterialLibAPI.editMaterial("gregtech", "Soularium")
            .setProperty(GTMaterialProperties.FORMULA, "?Au??");
        MaterialLibAPI.editMaterial("gregtech", "EnderiumBase")
            .setProperty(GTMaterialProperties.FORMULA, "Sn₂Ag₂Pt₂");
        MaterialLibAPI.editMaterial("gregtech", "RedstoneAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "(Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC");
        MaterialLibAPI.editMaterial("gregtech", "Ardite")
            .setProperty(GTMaterialProperties.FORMULA, "Ai");
        MaterialLibAPI.editMaterial("gregtech", "Reinforced")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₂(C(MgFeSi₂O₈)₈)");
        MaterialLibAPI.editMaterial("gregtech", "Galgadorian")
            .setProperty(GTMaterialProperties.FORMULA, "???C₉Nh₃Fe₂(C(MgFeSi₂O₈)₈)");
        MaterialLibAPI.editMaterial("gregtech", "EnhancedGalgadorian")
            .setProperty(GTMaterialProperties.FORMULA, "???C₉Nh₃Fe₂(C(MgFeSi₂O₈)₈)");
        MaterialLibAPI.editMaterial("gregtech", "Manyullyn")
            .setProperty(GTMaterialProperties.FORMULA, "AiCo");
        MaterialLibAPI.editMaterial("gregtech", "Mytryl")
            .setProperty(GTMaterialProperties.FORMULA, "SpPt₂FeMa");
        MaterialLibAPI.editMaterial("gregtech", "BlackPlutonium")
            .setProperty(GTMaterialProperties.FORMULA, "SpPu");
        MaterialLibAPI.editMaterial("gregtech", "CallistoIce")
            .setProperty(GTMaterialProperties.FORMULA, "SpH₂O");
        MaterialLibAPI.editMaterial("gregtech", "Ledox")
            .setProperty(GTMaterialProperties.FORMULA, "SpPb");
        MaterialLibAPI.editMaterial("gregtech", "Quantium")
            .setProperty(GTMaterialProperties.FORMULA, "Qt");
        MaterialLibAPI.editMaterial("gregtech", "Duralumin")
            .setProperty(GTMaterialProperties.FORMULA, "Al₆CuMnMg");
        MaterialLibAPI.editMaterial("gregtech", "Oriharukon")
            .setProperty(GTMaterialProperties.FORMULA, "Oh");
        MaterialLibAPI.editMaterial("gregtech", "InfinityCatalyst")
            .setProperty(GTMaterialProperties.FORMULA, "If");
        MaterialLibAPI.editMaterial("gregtech", "Bedrockium")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)₂₆₂₄₄C₉");
        MaterialLibAPI.editMaterial("gregtech", "Unstable")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.FIXED_JAPANESE_OPENING_QUOTE + "Fe/C⌋");
        MaterialLibAPI.editMaterial("gregtech", "Infinity")
            .setProperty(GTMaterialProperties.FORMULA, "If*");
        MaterialLibAPI.editMaterial("gregtech", "MysteriousCrystal")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.CIRCLE_STAR);
        MaterialLibAPI.editMaterial("gregtech", "SamariumMagnetic")
            .setProperty(GTMaterialProperties.FORMULA, "Sm" + CustomGlyphs.MAGNET);
        MaterialLibAPI.editMaterial("gregtech", "Alumite")
            .setProperty(GTMaterialProperties.FORMULA, "Zn₅(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₂(MgFeSi₂O₈)₂");
        MaterialLibAPI.editMaterial("gregtech", "EndSteel")
            .setProperty(GTMaterialProperties.FORMULA, "(((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)CSi)C(MgFeSi₂O₈))W?");
        MaterialLibAPI.editMaterial("gregtech", "CrudeSteel")
            .setProperty(GTMaterialProperties.FORMULA, "?(Na₂LiAl₂Si₂O₇(H₂O)₂)(SiO₂)");
        MaterialLibAPI.editMaterial("gregtech", "CrystallineAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "AuC(Fe(BeK₄N₅Ma₆)((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC))");
        MaterialLibAPI.editMaterial("gregtech", "MelodicAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "((((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)CSi)C(MgFeSi₂O₈))W?)((BeK₄N₅Ma₆)(C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa))Oh");
        MaterialLibAPI.editMaterial("gregtech", "StellarAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "(Nh₂Ma)₃" + CustomGlyphs.CIRCLE_CROSS + "C₆(((((Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)CSi)C(MgFeSi₂O₈))W?)((BeK₄N₅Ma₆)(C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa))Oh)Nq");
        MaterialLibAPI.editMaterial("gregtech", "CrystallinePinkSlime")
            .setProperty(GTMaterialProperties.FORMULA, "(AuC(Fe(BeK₄N₅Ma₆)((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)))C");
        MaterialLibAPI.editMaterial("gregtech", "EnergeticSilver")
            .setProperty(GTMaterialProperties.FORMULA, "Ag(((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)FeAg)(Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃)");
        MaterialLibAPI.editMaterial("gregtech", "VividAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "(Ag(((Si(FeS₂)₅(CrAl₂O₃)Hg₃)SiC)FeAg)(Ni(AuAgCu₃)(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₃))((BeK₄N₅Ma₆)(C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa))Cr");
        MaterialLibAPI.editMaterial("gregtech", "MTBEReactionMixture(Butane)")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₁₄O");
        MaterialLibAPI.editMaterial("gregtech", "HydricSulfide")
            .setProperty(GTMaterialProperties.FORMULA, "H₂S");
    }

    private static void initPart5() {
        MaterialLibAPI.editMaterial("gregtech", "Epoxid")
            .setProperty(GTMaterialProperties.FORMULA, "C₂₁H₂₄O₄");
        MaterialLibAPI.editMaterial("gregtech", "Silicone")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₆OSi");
        MaterialLibAPI.editMaterial("gregtech", "Polycaprolactam")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₁₁NO");
        MaterialLibAPI.editMaterial("gregtech", "Polytetrafluoroethylene")
            .setProperty(GTMaterialProperties.FORMULA, "C₂F₄");
        MaterialLibAPI.editMaterial("gregtech", "Alduorite")
            .setProperty(GTMaterialProperties.FORMULA, "SpAl");
        MaterialLibAPI.editMaterial("gregtech", "Rubracium")
            .setProperty(GTMaterialProperties.FORMULA, "SpRb");
        MaterialLibAPI.editMaterial("gregtech", "Vulcanite")
            .setProperty(GTMaterialProperties.FORMULA, "SpCu");
        MaterialLibAPI.editMaterial("gregtech", "LiquidOxygen")
            .setProperty(GTMaterialProperties.FORMULA, "O");
        MaterialLibAPI.editMaterial("gregtech", "LiquidNitrogen")
            .setProperty(GTMaterialProperties.FORMULA, "N");
        MaterialLibAPI.editMaterial("gregtech", "LiquidAir")
            .setProperty(GTMaterialProperties.FORMULA, "N₄" + CustomGlyphs.SUBSCRIPT0 + "O₁₁Ar((CO₂)₂₁He₉(CH₄)₃D)");
        MaterialLibAPI.editMaterial("gregtech", "NobleGases")
            .setProperty(GTMaterialProperties.FORMULA, "(CO₂)₂₁He₉(CH₄)₃D");
        MaterialLibAPI.editMaterial("gregtech", "CarbonDioxide")
            .setProperty(GTMaterialProperties.FORMULA, "CO₂");
        MaterialLibAPI.editMaterial("gregtech", "Diamond")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "Emerald")
            .setProperty(GTMaterialProperties.FORMULA, "Be₃Al₂Si₆O₁₈");
        MaterialLibAPI.editMaterial("gregtech", "Ruby")
            .setProperty(GTMaterialProperties.FORMULA, "CrAl₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "Sapphire")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "GreenSapphire")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "Olivine")
            .setProperty(GTMaterialProperties.FORMULA, "Mg₂Fe(SiO₂)₂");
        MaterialLibAPI.editMaterial("gregtech", "NetherStar")
            .setProperty(GTMaterialProperties.FORMULA, "(Nh₂Ma)₃" + CustomGlyphs.CIRCLE_CROSS + "C₆");
        MaterialLibAPI.editMaterial("gregtech", "Topaz")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂SiF₂H₂O₆");
        MaterialLibAPI.editMaterial("gregtech", "Tanzanite")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₂Al₃Si₃HO₁₃");
        MaterialLibAPI.editMaterial("gregtech", "Amethyst")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)₄Fe");
        MaterialLibAPI.editMaterial("gregtech", "Opal")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "Jasper")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "FoolsRuby")
            .setProperty(GTMaterialProperties.FORMULA, "MgAl₂O₄");
        MaterialLibAPI.editMaterial("gregtech", "BlueTopaz")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂SiF₂H₂O₆");
        MaterialLibAPI.editMaterial("gregtech", "Amber")
            .setProperty(GTMaterialProperties.FORMULA, "C₁" + CustomGlyphs.SUBSCRIPT0 + "H₁" + CustomGlyphs.SUBSCRIPT0 + "O₁₆");
        MaterialLibAPI.editMaterial("gregtech", "Dilithium")
            .setProperty(GTMaterialProperties.FORMULA, "∳Li∳Li∳");
        MaterialLibAPI.editMaterial("gregtech", "CertusQuartz")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "ChargedCertusQuartz")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂" + CustomGlyphs.HIGH_VOLTAGE);
        MaterialLibAPI.editMaterial("gregtech", "Forcicium")
            .setProperty(GTMaterialProperties.FORMULA, "◃◁◀");
        MaterialLibAPI.editMaterial("gregtech", "Forcillium")
            .setProperty(GTMaterialProperties.FORMULA, "▶▷▹");
        MaterialLibAPI.editMaterial("gregtech", "Monazite")
            .setProperty(GTMaterialProperties.FORMULA, "??????(PO₄)");
        MaterialLibAPI.editMaterial("gregtech", "Force")
            .setProperty(GTMaterialProperties.FORMULA, "Fc⚙");
        MaterialLibAPI.editMaterial("gregtech", "NetherQuartz")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "Quartzite")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "Lazurite")
            .setProperty(GTMaterialProperties.FORMULA, "Al₆Si₆Ca₈Na₈");
        MaterialLibAPI.editMaterial("gregtech", "Sodalite")
            .setProperty(GTMaterialProperties.FORMULA, "Al₃Si₃Na₄Cl");
        MaterialLibAPI.editMaterial("gregtech", "Lapis")
            .setProperty(GTMaterialProperties.FORMULA, "(Al₆Si₆Ca₈Na₈)₁₂(Al₃Si₃Na₄Cl)₂(FeS₂)(CaCO₃)");
        MaterialLibAPI.editMaterial("gregtech", "GarnetRed")
            .setProperty(GTMaterialProperties.FORMULA, "(Al₂Mg₃Si₃O₁₂)₃(Al₂Fe₃Si₃O₁₂)₅(Al₂Mn₃Si₃O₁₂)₈");
        MaterialLibAPI.editMaterial("gregtech", "GarnetYellow")
            .setProperty(GTMaterialProperties.FORMULA, "(Ca₃Fe₂Si₃O₁₂)₅(Ca₃Al₂Si₃O₁₂)₈(Ca₃Cr₂Si₃O₁₂)₃");
        MaterialLibAPI.editMaterial("gregtech", "Vinteum")
            .setProperty(GTMaterialProperties.FORMULA, "FeMa*");
        MaterialLibAPI.editMaterial("gregtech", "Apatite")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₅(PO₄)₃Cl");
        MaterialLibAPI.editMaterial("gregtech", "Niter")
            .setProperty(GTMaterialProperties.FORMULA, "KNO₃");
        MaterialLibAPI.editMaterial("gregtech", "EnderPearl")
            .setProperty(GTMaterialProperties.FORMULA, "BeK₄N₅Ma₆");
        MaterialLibAPI.editMaterial("gregtech", "EnderEye")
            .setProperty(GTMaterialProperties.FORMULA, "(BeK₄N₅Ma₆)(C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa)");
        MaterialLibAPI.editMaterial("gregtech", "TricalciumPhosphate")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₃(PO₄)₂");
        MaterialLibAPI.editMaterial("gregtech", "Coal")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "Charcoal")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "Jade")
            .setProperty(GTMaterialProperties.FORMULA, "NaAlSi₂O₆");
        MaterialLibAPI.editMaterial("gregtech", "Lignite")
            .setProperty(GTMaterialProperties.FORMULA, "C₃(H₂O)");
        MaterialLibAPI.editMaterial("gregtech", "InfusedAir")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.AIR);
        MaterialLibAPI.editMaterial("gregtech", "InfusedFire")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.FIRE);
        MaterialLibAPI.editMaterial("gregtech", "InfusedEarth")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.EARTH);
        MaterialLibAPI.editMaterial("gregtech", "InfusedWater")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.WATER);
        MaterialLibAPI.editMaterial("gregtech", "InfusedEntropy")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.CHAOS);
        MaterialLibAPI.editMaterial("gregtech", "InfusedOrder")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.ORDER);
        MaterialLibAPI.editMaterial("gregtech", "RoastedCopper")
            .setProperty(GTMaterialProperties.FORMULA, "Cu" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "RoastedAntimony")
            .setProperty(GTMaterialProperties.FORMULA, "Sb" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "RoastedIron")
            .setProperty(GTMaterialProperties.FORMULA, "Fe" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
    }

    private static void initPart6() {
        MaterialLibAPI.editMaterial("gregtech", "RoastedNickel")
            .setProperty(GTMaterialProperties.FORMULA, "Ni" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "RoastedZinc")
            .setProperty(GTMaterialProperties.FORMULA, "Zn" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "RoastedCobalt")
            .setProperty(GTMaterialProperties.FORMULA, "Co" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "RoastedArsenic")
            .setProperty(GTMaterialProperties.FORMULA, "As" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "RoastedLead")
            .setProperty(GTMaterialProperties.FORMULA, "Pb" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK);
        MaterialLibAPI.editMaterial("gregtech", "Grade1PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade2PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade3PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade4PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade5PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade6PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade7PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Grade8PurifiedWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "FlocculationWasteLiquid")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂(OH)₃??Cl₃");
        MaterialLibAPI.editMaterial("gregtech", "ActivatedCarbon")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "PreActivatedCarbon")
            .setProperty(GTMaterialProperties.FORMULA, "C(H₃PO₄)");
        MaterialLibAPI.editMaterial("gregtech", "carbonactivateddirty")
            .setProperty(GTMaterialProperties.FORMULA, "C(H₃PO₄)");
        MaterialLibAPI.editMaterial("gregtech", "PolyaluminiumChloride")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂(OH)₃Cl₃");
        MaterialLibAPI.editMaterial("gregtech", "AdvancedGlue")
            .setProperty(GTMaterialProperties.FORMULA, "A chemically approved glue!")
            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true);
        MaterialLibAPI.editMaterial("gregtech", "Ozone")
            .setProperty(GTMaterialProperties.FORMULA, "O₃");
        MaterialLibAPI.editMaterial("gregtech", "TPVAlloy")
            .setProperty(GTMaterialProperties.FORMULA, "Ti₃Pt₃V");
        MaterialLibAPI.editMaterial("gregtech", "TranscendentMetal")
            .setProperty(GTMaterialProperties.FORMULA, "TsЖ");
        MaterialLibAPI.editMaterial("gregtech", "EnrichedHolmium")
            .setProperty(GTMaterialProperties.FORMULA, "Nq+₄Ho₁");
        MaterialLibAPI.editMaterial("gregtech", "MagnetohydrodynamicallyConstrainedStarMatter")
            .setProperty(GTMaterialProperties.FORMULA, "⇲" + CustomGlyphs.ARROW_CORNER_SOUTH_EAST + CustomGlyphs.GALAXY + CustomGlyphs.ARROW_CORNER_NORTH_WEST + "⇱");
        MaterialLibAPI.editMaterial("gregtech", "RawStarMatter")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.GALAXY);
        MaterialLibAPI.editMaterial("gregtech", "WhiteDwarfMatter")
            .setProperty(GTMaterialProperties.FORMULA, "∅");
        MaterialLibAPI.editMaterial("gregtech", "BlackDwarfMatter")
            .setProperty(GTMaterialProperties.FORMULA, ">>∅<<");
        MaterialLibAPI.editMaterial("gregtech", "SpaceTime")
            .setProperty(GTMaterialProperties.FORMULA, "\u03A6");
        MaterialLibAPI.editMaterial("gregtech", "DimensionallyTranscendentResidue")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.SPARKLES + "-" + CustomGlyphs.EMPTY_SET);
        MaterialLibAPI.editMaterial("gregtech", "PotassiumNitrate")
            .setProperty(GTMaterialProperties.FORMULA, "KNO₃");
        MaterialLibAPI.editMaterial("gregtech", "Chromiumtrioxide")
            .setProperty(GTMaterialProperties.FORMULA, "CrO₃");
        MaterialLibAPI.editMaterial("gregtech", "2Nitrochlorobenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₄ClNO₂");
        MaterialLibAPI.editMaterial("gregtech", "Dimethylbenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "PotassiumDichromate")
            .setProperty(GTMaterialProperties.FORMULA, "K₂Cr₂O₇");
        MaterialLibAPI.editMaterial("gregtech", "phtalicacid")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₆O₄");
        MaterialLibAPI.editMaterial("gregtech", "3,3Dichlorobenzidine")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₂H₁" + CustomGlyphs.SUBSCRIPT0 + "N₂Cl₂");
        MaterialLibAPI.editMaterial("gregtech", "3,3Diaminobenzidine")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₂H₁₄N₄");
        MaterialLibAPI.editMaterial("gregtech", "DiphenylIsophtalate")
            .setProperty(GTMaterialProperties.FORMULA, "C₂" + CustomGlyphs.SUBSCRIPT0 + "H₁₄O₄");
        MaterialLibAPI.editMaterial("gregtech", "Polybenzimidazole")
            .setProperty(GTMaterialProperties.FORMULA, "C₂" + CustomGlyphs.SUBSCRIPT0 + "N₄H₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Chlorobenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₅Cl");
        MaterialLibAPI.editMaterial("gregtech", "DilutedHydrochloricAcid_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "HCl");
        MaterialLibAPI.editMaterial("gregtech", "Pyrochlore")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₂Nb₂O₇");
        MaterialLibAPI.editMaterial("gregtech", "EpoxidFiberReinforced")
            .setProperty(GTMaterialProperties.FORMULA, "C₂₁H₂₄O₄");
        MaterialLibAPI.editMaterial("gregtech", "BorosilicateGlass")
            .setProperty(GTMaterialProperties.FORMULA, "B(SiO₂)₇");
        MaterialLibAPI.editMaterial("gregtech", "FerriteMixture")
            .setProperty(GTMaterialProperties.FORMULA, "NiZnFe₄");
        MaterialLibAPI.editMaterial("gregtech", "NickelZincFerrite")
            .setProperty(GTMaterialProperties.FORMULA, "NiZnFe₄O₈");
        MaterialLibAPI.editMaterial("gregtech", "Massicot")
            .setProperty(GTMaterialProperties.FORMULA, "PbO");
        MaterialLibAPI.editMaterial("gregtech", "ArsenicTrioxide")
            .setProperty(GTMaterialProperties.FORMULA, "As₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "CobaltOxide")
            .setProperty(GTMaterialProperties.FORMULA, "CoO");
        MaterialLibAPI.editMaterial("gregtech", "Zincite")
            .setProperty(GTMaterialProperties.FORMULA, "ZnO");
        MaterialLibAPI.editMaterial("gregtech", "AntimonyTrioxide")
            .setProperty(GTMaterialProperties.FORMULA, "Sb₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "CupricOxide")
            .setProperty(GTMaterialProperties.FORMULA, "CuO");
        MaterialLibAPI.editMaterial("gregtech", "Ferrosilite")
            .setProperty(GTMaterialProperties.FORMULA, "FeSiO₃");
        MaterialLibAPI.editMaterial("gregtech", "Magnesia")
            .setProperty(GTMaterialProperties.FORMULA, "MgO");
        MaterialLibAPI.editMaterial("gregtech", "Quicklime")
            .setProperty(GTMaterialProperties.FORMULA, "CaO");
        MaterialLibAPI.editMaterial("gregtech", "Potash")
            .setProperty(GTMaterialProperties.FORMULA, "K₂O");
        MaterialLibAPI.editMaterial("gregtech", "SodaAsh")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂CO₃");
        MaterialLibAPI.editMaterial("gregtech", "Brick")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂Si₄O₁₁");
        MaterialLibAPI.editMaterial("gregtech", "Fireclay")
            .setProperty(GTMaterialProperties.FORMULA, "(Al₂Si₄O₁₁)(Na₂LiAl₂Si₂O₇(H₂O)₂)");
        MaterialLibAPI.editMaterial("gregtech", "Glycerol")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₈O₃");
    }

    private static void initPart7() {
        MaterialLibAPI.editMaterial("gregtech", "SodiumBisulfate")
            .setProperty(GTMaterialProperties.FORMULA, "NaHSO₄");
        MaterialLibAPI.editMaterial("gregtech", "PolyphenyleneSulfide")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₄S");
        MaterialLibAPI.editMaterial("gregtech", "Dichlorobenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₄Cl₂");
        MaterialLibAPI.editMaterial("gregtech", "Polydimethylsiloxane")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₆OSi");
        MaterialLibAPI.editMaterial("gregtech", "RawStyreneButadieneRubber")
            .setProperty(GTMaterialProperties.FORMULA, "(C₈H₈)(C₄H₆)₃");
        MaterialLibAPI.editMaterial("gregtech", "StyreneButadieneRubber")
            .setProperty(GTMaterialProperties.FORMULA, "(C₈H₈)(C₄H₆)₃");
        MaterialLibAPI.editMaterial("gregtech", "Polystyrene")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₈");
        MaterialLibAPI.editMaterial("gregtech", "Styrene")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₈");
        MaterialLibAPI.editMaterial("gregtech", "Isoprene")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₈");
        MaterialLibAPI.editMaterial("gregtech", "Tetranitromethane")
            .setProperty(GTMaterialProperties.FORMULA, "CN₄O₈");
        MaterialLibAPI.editMaterial("gregtech", "DilutedSulfuricAcid")
            .setProperty(GTMaterialProperties.FORMULA, "H₂SO₄");
        MaterialLibAPI.editMaterial("gregtech", "Ethenone")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Ethane")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₆");
        MaterialLibAPI.editMaterial("gregtech", "Propane")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₈");
        MaterialLibAPI.editMaterial("gregtech", "Butane")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "Butene")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₈");
        MaterialLibAPI.editMaterial("gregtech", "Butadiene")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₆");
        MaterialLibAPI.editMaterial("gregtech", "Toluene")
            .setProperty(GTMaterialProperties.FORMULA, "C₇H₈");
        MaterialLibAPI.editMaterial("gregtech", "Epichlorohydrin")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₅ClO");
        MaterialLibAPI.editMaterial("gregtech", "PolyvinylChloride")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₃Cl");
        MaterialLibAPI.editMaterial("gregtech", "VinylChloride")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₃Cl");
        MaterialLibAPI.editMaterial("gregtech", "SulfurDioxide")
            .setProperty(GTMaterialProperties.FORMULA, "SO₂");
        MaterialLibAPI.editMaterial("gregtech", "SulfurTrioxide")
            .setProperty(GTMaterialProperties.FORMULA, "SO₃");
        MaterialLibAPI.editMaterial("gregtech", "NitricAcid")
            .setProperty(GTMaterialProperties.FORMULA, "HNO₃");
        MaterialLibAPI.editMaterial("gregtech", "1,1Dimethylhydrazine")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₈N₂");
        MaterialLibAPI.editMaterial("gregtech", "Chloramine")
            .setProperty(GTMaterialProperties.FORMULA, "NH₂Cl");
        MaterialLibAPI.editMaterial("gregtech", "Dimethylamine")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₇N");
        MaterialLibAPI.editMaterial("gregtech", "DinitrogenTetroxide")
            .setProperty(GTMaterialProperties.FORMULA, "N₂O₄");
        MaterialLibAPI.editMaterial("gregtech", "NitricOxide")
            .setProperty(GTMaterialProperties.FORMULA, "NO");
        MaterialLibAPI.editMaterial("gregtech", "Ammonia")
            .setProperty(GTMaterialProperties.FORMULA, "NH₃");
        MaterialLibAPI.editMaterial("gregtech", "Dimethyldichlorosilane")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₆Cl₂Si");
        MaterialLibAPI.editMaterial("gregtech", "Chloromethane")
            .setProperty(GTMaterialProperties.FORMULA, "CH₃Cl");
        MaterialLibAPI.editMaterial("gregtech", "PhosphorousPentoxide")
            .setProperty(GTMaterialProperties.FORMULA, "P₄O₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "Tetrafluoroethylene")
            .setProperty(GTMaterialProperties.FORMULA, "C₂F₄");
        MaterialLibAPI.editMaterial("gregtech", "HydrofluoricAcid_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "HF");
        MaterialLibAPI.editMaterial("gregtech", "Chloroform")
            .setProperty(GTMaterialProperties.FORMULA, "CHCl₃");
        MaterialLibAPI.editMaterial("gregtech", "BisphenolA")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₅H₁₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "AceticAcid")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₄O₂");
        MaterialLibAPI.editMaterial("gregtech", "CalciumAcetateSolution")
            .setProperty(GTMaterialProperties.FORMULA, "CaC₄O₄H₆");
        MaterialLibAPI.editMaterial("gregtech", "Acetone")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₆O");
        MaterialLibAPI.editMaterial("gregtech", "Methanol")
            .setProperty(GTMaterialProperties.FORMULA, "CH₄O");
        MaterialLibAPI.editMaterial("gregtech", "CarbonMonoxide")
            .setProperty(GTMaterialProperties.FORMULA, "CO");
        MaterialLibAPI.editMaterial("gregtech", "MetalMixture")
            .setProperty(GTMaterialProperties.FORMULA, "Fe" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "O" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??");
        MaterialLibAPI.editMaterial("gregtech", "Ethylene")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₄");
        MaterialLibAPI.editMaterial("gregtech", "Propene")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₆");
        MaterialLibAPI.editMaterial("gregtech", "VinylAcetate")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "PolyvinylAcetate")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "MethylAcetate")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "AllylChloride")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₅Cl");
        MaterialLibAPI.editMaterial("gregtech", "HydrochloricAcid_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "HCl");
        MaterialLibAPI.editMaterial("gregtech", "HypochlorousAcid")
            .setProperty(GTMaterialProperties.FORMULA, "HClO");
        MaterialLibAPI.editMaterial("gregtech", "SodiumHydroxide_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "NaOH");
        MaterialLibAPI.editMaterial("gregtech", "Benzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₆");
        MaterialLibAPI.editMaterial("gregtech", "Phenol")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₆O");
        MaterialLibAPI.editMaterial("gregtech", "Isopropylbenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₉H₁₂");
        MaterialLibAPI.editMaterial("gregtech", "PhosphoricAcid_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "H₃PO₄");
        MaterialLibAPI.editMaterial("gregtech", "IronIIIChloride")
            .setProperty(GTMaterialProperties.FORMULA, "FeCl₃");
        MaterialLibAPI.editMaterial("gregtech", "SodiumCarbonate")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂CO₃");
        MaterialLibAPI.editMaterial("gregtech", "SodiumAluminate")
            .setProperty(GTMaterialProperties.FORMULA, "NaAlO₂");
        MaterialLibAPI.editMaterial("gregtech", "AluminiumHydroxide")
            .setProperty(GTMaterialProperties.FORMULA, "Al(OH)₃");
    }

    private static void initPart8() {
        MaterialLibAPI.editMaterial("gregtech", "Cryolite")
            .setProperty(GTMaterialProperties.FORMULA, "Na₃AlF₆");
        MaterialLibAPI.editMaterial("gregtech", "Water")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Ice")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "Ethanol")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₆O");
        MaterialLibAPI.editMaterial("gregtech", "Glyceryl")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₅N₃O₉");
        MaterialLibAPI.editMaterial("gregtech", "Methane")
            .setProperty(GTMaterialProperties.FORMULA, "CH₄");
        MaterialLibAPI.editMaterial("gregtech", "NitroCarbon")
            .setProperty(GTMaterialProperties.FORMULA, "NC");
        MaterialLibAPI.editMaterial("gregtech", "NitrogenDioxide")
            .setProperty(GTMaterialProperties.FORMULA, "NO₂");
        MaterialLibAPI.editMaterial("gregtech", "SodiumPersulfate")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂S₂O₈");
        MaterialLibAPI.editMaterial("gregtech", "SodiumSulfide")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂S");
        MaterialLibAPI.editMaterial("gregtech", "SulfuricAcid")
            .setProperty(GTMaterialProperties.FORMULA, "H₂SO₄");
        MaterialLibAPI.editMaterial("gregtech", "UUAmplifier")
            .setProperty(GTMaterialProperties.FORMULA, "Accelerates the Mass Fabricator")
            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true);
        MaterialLibAPI.editMaterial("gregtech", "Glue")
            .setProperty(GTMaterialProperties.FORMULA, "No Horses were harmed in the making of this substance")
            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true);
        MaterialLibAPI.editMaterial("gregtech", "Snow")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "HolyWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "SodiumOxide")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂O");
        MaterialLibAPI.editMaterial("gregtech", "SodiumMethoxide")
            .setProperty(GTMaterialProperties.FORMULA, "CH₃ONa");
        MaterialLibAPI.editMaterial("gregtech", "TrimethylBorate")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₉BO₃");
        MaterialLibAPI.editMaterial("gregtech", "SodiumHydride")
            .setProperty(GTMaterialProperties.FORMULA, "NaH");
        MaterialLibAPI.editMaterial("gregtech", "PhosphorusTrichloride")
            .setProperty(GTMaterialProperties.FORMULA, "PCl₃");
        MaterialLibAPI.editMaterial("gregtech", "Triphenylphosphene")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₈H₁₅P");
        MaterialLibAPI.editMaterial("gregtech", "RhodiumChloride")
            .setProperty(GTMaterialProperties.FORMULA, "RhCl₃");
        MaterialLibAPI.editMaterial("gregtech", "SodiumBorohydride")
            .setProperty(GTMaterialProperties.FORMULA, "NaBH₄");
        MaterialLibAPI.editMaterial("gregtech", "OrganorhodiumCatalyst")
            .setProperty(GTMaterialProperties.FORMULA, "RhHCO(P(C₆H₅)₃)₃");
        MaterialLibAPI.editMaterial("gregtech", "Cobalt(II)Nitrate")
            .setProperty(GTMaterialProperties.FORMULA, "Co(NO₃)₂");
        MaterialLibAPI.editMaterial("gregtech", "Cobalt(II)Acetate")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₆CoO₄");
        MaterialLibAPI.editMaterial("gregtech", "CobaltIIHydroxide")
            .setProperty(GTMaterialProperties.FORMULA, "Co(OH)₂");
        MaterialLibAPI.editMaterial("gregtech", "Cobalt(II)Naphthenate")
            .setProperty(GTMaterialProperties.FORMULA, "CoC₂₂H₁₄O₄");
        MaterialLibAPI.editMaterial("gregtech", "1,4Dimethylbenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "1,3Dimethylbenzene")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "TerephthalicAcid")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₆O₄");
        MaterialLibAPI.editMaterial("gregtech", "DimethylTerephthalate")
            .setProperty(GTMaterialProperties.FORMULA, "C₁" + CustomGlyphs.SUBSCRIPT0 + "H₁" + CustomGlyphs.SUBSCRIPT0 + "O₄");
        MaterialLibAPI.editMaterial("gregtech", "BismuthIIIOxide")
            .setProperty(GTMaterialProperties.FORMULA, "Bi₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "HeeEndium")
            .setProperty(GTMaterialProperties.FORMULA, "Em");
        MaterialLibAPI.editMaterial("gregtech", "RaneyNickelActivated")
            .setProperty(GTMaterialProperties.FORMULA, "NiAl");
        MaterialLibAPI.editMaterial("gregtech", "NickelAluminide")
            .setProperty(GTMaterialProperties.FORMULA, "NiAl₃");
        MaterialLibAPI.editMaterial("gregtech", "2Butin14diol")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "LiquidCrystalKevlar")
            .setProperty(GTMaterialProperties.FORMULA, "[-CO-C₆H₄-CO-NH-C₆H₄-NH-]n");
        MaterialLibAPI.editMaterial("gregtech", "CacliumCarbide")
            .setProperty(GTMaterialProperties.FORMULA, "CaC₂");
        MaterialLibAPI.editMaterial("gregtech", "GammaButyrolactone")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "Trimethylamine")
            .setProperty(GTMaterialProperties.FORMULA, "C₃H₉N");
        MaterialLibAPI.editMaterial("gregtech", "Methylamine")
            .setProperty(GTMaterialProperties.FORMULA, "CH₅N");
        MaterialLibAPI.editMaterial("gregtech", "pPhenylenediamine")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₈N₂");
        MaterialLibAPI.editMaterial("gregtech", "4Nitroaniline")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₆N₂O₂");
        MaterialLibAPI.editMaterial("gregtech", "Acetylene")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₂");
        MaterialLibAPI.editMaterial("gregtech", "TerephthaloylChloride")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₄Cl₂O₂");
        MaterialLibAPI.editMaterial("gregtech", "NMethylpyrolidone")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₉NO");
        MaterialLibAPI.editMaterial("gregtech", "Pentaerythritol")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₁₂O₄");
        MaterialLibAPI.editMaterial("gregtech", "Acetaldehyde")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₄O");
        MaterialLibAPI.editMaterial("gregtech", "EthyleneGlycol")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₆O₂");
        MaterialLibAPI.editMaterial("gregtech", "EthyleneOxide")
            .setProperty(GTMaterialProperties.FORMULA, "C₂H₄O");
        MaterialLibAPI.editMaterial("gregtech", "NickelTetracarbonyl")
            .setProperty(GTMaterialProperties.FORMULA, "C₄NiO₄");
        MaterialLibAPI.editMaterial("gregtech", "Isobutyraldehyde")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₈O");
        MaterialLibAPI.editMaterial("gregtech", "Butyraldehyde")
            .setProperty(GTMaterialProperties.FORMULA, "C₄H₈O");
        MaterialLibAPI.editMaterial("gregtech", "DiphenylmethaneDiisocyanateMixture")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₅H₁" + CustomGlyphs.SUBSCRIPT0 + "N₂O₂");
        MaterialLibAPI.editMaterial("gregtech", "DiaminodiphenylmethanMixture")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₃H₁₄N₂");
        MaterialLibAPI.editMaterial("gregtech", "DiphenylmethaneDiisocyanate")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₅H₁" + CustomGlyphs.SUBSCRIPT0 + "N₂O₂");
        MaterialLibAPI.editMaterial("gregtech", "CalciumHydride")
            .setProperty(GTMaterialProperties.FORMULA, "CaH₂");
        MaterialLibAPI.editMaterial("gregtech", "Silane")
            .setProperty(GTMaterialProperties.FORMULA, "SiH₄");
        MaterialLibAPI.editMaterial("gregtech", "Dichlorosilane")
            .setProperty(GTMaterialProperties.FORMULA, "SiH₂Cl₂");
    }

    private static void initPart9() {
        MaterialLibAPI.editMaterial("gregtech", "Blaze")
            .setProperty(GTMaterialProperties.FORMULA, "C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa");
        MaterialLibAPI.editMaterial("gregtech", "Flint")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "Sugar")
            .setProperty(GTMaterialProperties.FORMULA, "C₂(H₂O)₅O₂₅");
        MaterialLibAPI.editMaterial("gregtech", "Obsidian")
            .setProperty(GTMaterialProperties.FORMULA, "MgFeSi₂O₈");
        MaterialLibAPI.editMaterial("gregtech", "Clay")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂LiAl₂Si₂O₇(H₂O)₂");
        MaterialLibAPI.editMaterial("gregtech", "Bone")
            .setProperty(GTMaterialProperties.FORMULA, "Ca");
        MaterialLibAPI.editMaterial("gregtech", "Wood")
            .setProperty(GTMaterialProperties.FORMULA, "");
        MaterialLibAPI.editMaterial("gregtech", "Redstone")
            .setProperty(GTMaterialProperties.FORMULA, "Si(FeS₂)₅(CrAl₂O₃)Hg₃");
        MaterialLibAPI.editMaterial("gregtech", "Electrotine")
            .setProperty(GTMaterialProperties.FORMULA, "Rp");
        MaterialLibAPI.editMaterial("gregtech", "Ash")
            .setProperty(GTMaterialProperties.FORMULA, "??");
        MaterialLibAPI.editMaterial("gregtech", "DarkAsh")
            .setProperty(GTMaterialProperties.FORMULA, "C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??");
        MaterialLibAPI.editMaterial("gregtech", "HydratedCoal")
            .setProperty(GTMaterialProperties.FORMULA, "C₈(H₂O)");
        MaterialLibAPI.editMaterial("gregtech", "Graphene")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "Almandine")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂Fe₃Si₃O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Andradite")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₃Fe₂Si₃O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Bauxite")
            .setProperty(GTMaterialProperties.FORMULA, "(TiO₂)₂Al₁₆H₁" + CustomGlyphs.SUBSCRIPT0 + "O₁₁");
        MaterialLibAPI.editMaterial("gregtech", "Calcite")
            .setProperty(GTMaterialProperties.FORMULA, "CaCO₃");
        MaterialLibAPI.editMaterial("gregtech", "Cassiterite")
            .setProperty(GTMaterialProperties.FORMULA, "SnO₂");
        MaterialLibAPI.editMaterial("gregtech", "Chromite")
            .setProperty(GTMaterialProperties.FORMULA, "FeCr₂O₄");
        MaterialLibAPI.editMaterial("gregtech", "Cinnabar")
            .setProperty(GTMaterialProperties.FORMULA, "HgS");
        MaterialLibAPI.editMaterial("gregtech", "Cobaltite")
            .setProperty(GTMaterialProperties.FORMULA, "CoAsS");
        MaterialLibAPI.editMaterial("gregtech", "Cooperite")
            .setProperty(GTMaterialProperties.FORMULA, "Pt₃NiSPd");
        MaterialLibAPI.editMaterial("gregtech", "DeepIron")
            .setProperty(GTMaterialProperties.FORMULA, "Sp₂Fe");
        MaterialLibAPI.editMaterial("gregtech", "Galena")
            .setProperty(GTMaterialProperties.FORMULA, "PbS");
        MaterialLibAPI.editMaterial("gregtech", "Grossular")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₃Al₂Si₃O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "LiveRoot")
            .setProperty(GTMaterialProperties.FORMULA, "(COH₃)Ma");
        MaterialLibAPI.editMaterial("gregtech", "Phosphate")
            .setProperty(GTMaterialProperties.FORMULA, "PO₄");
        MaterialLibAPI.editMaterial("gregtech", "Pyrite")
            .setProperty(GTMaterialProperties.FORMULA, "FeS₂");
        MaterialLibAPI.editMaterial("gregtech", "Pyrope")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂Mg₃Si₃O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Saltpeter")
            .setProperty(GTMaterialProperties.FORMULA, "KNO₃");
        MaterialLibAPI.editMaterial("gregtech", "SiliconDioxide")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "Spessartine")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂Mn₃Si₃O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Sphalerite")
            .setProperty(GTMaterialProperties.FORMULA, "ZnS");
        MaterialLibAPI.editMaterial("gregtech", "Tetrahedrite")
            .setProperty(GTMaterialProperties.FORMULA, "Cu₃SbS₃Fe");
        MaterialLibAPI.editMaterial("gregtech", "Tungstate")
            .setProperty(GTMaterialProperties.FORMULA, "WLi₂O₄");
        MaterialLibAPI.editMaterial("gregtech", "Uvarovite")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₃Cr₂Si₃O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Pyrotheum")
            .setProperty(GTMaterialProperties.FORMULA, "C(Si(FeS₂)₅(CrAl₂O₃)Hg₃)(C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??SMa)S");
        MaterialLibAPI.editMaterial("gregtech", "Basalt")
            .setProperty(GTMaterialProperties.FORMULA, "(Mg₂Fe(SiO₂)₂)(CaCO₃)₃(SiO₂)₈C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??₄");
        MaterialLibAPI.editMaterial("gregtech", "Marble")
            .setProperty(GTMaterialProperties.FORMULA, "Mg(CaCO₃)₇");
        MaterialLibAPI.editMaterial("gregtech", "Redrock")
            .setProperty(GTMaterialProperties.FORMULA, "(CaCO₃)₂(SiO₂)(Na₂LiAl₂Si₂O₇(H₂O)₂)");
        MaterialLibAPI.editMaterial("gregtech", "PotassiumFeldspar")
            .setProperty(GTMaterialProperties.FORMULA, "KAlSi₃O₈");
        MaterialLibAPI.editMaterial("gregtech", "Biotite")
            .setProperty(GTMaterialProperties.FORMULA, "KMg₃Al₃F₂Si₃O₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "GraniteBlack")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)₄(KMg₃Al₃F₂Si₃O₁" + CustomGlyphs.SUBSCRIPT0 + ")");
        MaterialLibAPI.editMaterial("gregtech", "GraniteRed")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂(KAlSi₃O₈)O₃");
        MaterialLibAPI.editMaterial("gregtech", "Blizz")
            .setProperty(GTMaterialProperties.FORMULA, "❆Ma");
        MaterialLibAPI.editMaterial("gregtech", "Chalcopyrite")
            .setProperty(GTMaterialProperties.FORMULA, "CuFeS₂");
        MaterialLibAPI.editMaterial("gregtech", "SiliconSolarGrade")
            .setProperty(GTMaterialProperties.FORMULA, "Si*");
        MaterialLibAPI.editMaterial("gregtech", "Epidote")
            .setProperty(GTMaterialProperties.FORMULA, "Ca₂Al₃(SiO₂)₃OH");
        MaterialLibAPI.editMaterial("gregtech", "Graphite")
            .setProperty(GTMaterialProperties.FORMULA, "C");
        MaterialLibAPI.editMaterial("gregtech", "Trinium")
            .setProperty(GTMaterialProperties.FORMULA, "Ke");
        MaterialLibAPI.editMaterial("gregtech", "Magnetite")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₃O₄");
        MaterialLibAPI.editMaterial("gregtech", "Malachite")
            .setProperty(GTMaterialProperties.FORMULA, "Cu₂CH₂O₅");
        MaterialLibAPI.editMaterial("gregtech", "Pitchblende")
            .setProperty(GTMaterialProperties.FORMULA, "(UO₂)₃ThPb");
        MaterialLibAPI.editMaterial("gregtech", "Plastic")
            .setProperty(GTMaterialProperties.FORMULA, "CH₂");
        MaterialLibAPI.editMaterial("gregtech", "Soapstone")
            .setProperty(GTMaterialProperties.FORMULA, "Mg₃Si₄H₂O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Rubber")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₈");
        MaterialLibAPI.editMaterial("gregtech", "Wulfenite")
            .setProperty(GTMaterialProperties.FORMULA, "PbMoO₄");
        MaterialLibAPI.editMaterial("gregtech", "Powellite")
            .setProperty(GTMaterialProperties.FORMULA, "CaMoO₄");
        MaterialLibAPI.editMaterial("gregtech", "Desh")
            .setProperty(GTMaterialProperties.FORMULA, "De");
        MaterialLibAPI.editMaterial("gregtech", "WoodSealed")
            .setProperty(GTMaterialProperties.FORMULA, "");
    }

    private static void initPart10() {
        MaterialLibAPI.editMaterial("gregtech", "Glass")
            .setProperty(GTMaterialProperties.FORMULA, "SiO₂");
        MaterialLibAPI.editMaterial("gregtech", "RareEarth")
            .setProperty(GTMaterialProperties.FORMULA, "??????");
        MaterialLibAPI.editMaterial("gregtech", "RawRubber")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₈");
        MaterialLibAPI.editMaterial("gregtech", "Cryotheum")
            .setProperty(GTMaterialProperties.FORMULA, "(KNO₃)((Al₆Si₆Ca₈Na₈)₁₂(Al₃Si₃Na₄Cl)₂(FeS₂)(CaCO₃))(H₂O)(❆Ma)");
        MaterialLibAPI.editMaterial("gregtech", "Mirabilite")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂S(H₂O)₁" + CustomGlyphs.SUBSCRIPT0 + "O₄");
        MaterialLibAPI.editMaterial("gregtech", "Mica")
            .setProperty(GTMaterialProperties.FORMULA, "KAl₃Si₃F₂O₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "Talc")
            .setProperty(GTMaterialProperties.FORMULA, "Mg₃Si₄H₂O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Trona")
            .setProperty(GTMaterialProperties.FORMULA, "Na₃C₂H(H₂O)₂O₆");
        MaterialLibAPI.editMaterial("gregtech", "Barite")
            .setProperty(GTMaterialProperties.FORMULA, "BaSO₄");
        MaterialLibAPI.editMaterial("gregtech", "Bastnasite")
            .setProperty(GTMaterialProperties.FORMULA, "CeCFO₃");
        MaterialLibAPI.editMaterial("gregtech", "Garnierite")
            .setProperty(GTMaterialProperties.FORMULA, "NiO");
        MaterialLibAPI.editMaterial("gregtech", "Lepidolite")
            .setProperty(GTMaterialProperties.FORMULA, "KLi₃Al₄F₂O₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "Magnesite")
            .setProperty(GTMaterialProperties.FORMULA, "MgCO₃");
        MaterialLibAPI.editMaterial("gregtech", "Pentlandite")
            .setProperty(GTMaterialProperties.FORMULA, "Ni₉S₈");
        MaterialLibAPI.editMaterial("gregtech", "Scheelite")
            .setProperty(GTMaterialProperties.FORMULA, "WCaO₄");
        MaterialLibAPI.editMaterial("gregtech", "Alunite")
            .setProperty(GTMaterialProperties.FORMULA, "KAl₃Si₂H₆O₁₄");
        MaterialLibAPI.editMaterial("gregtech", "Chrysotile")
            .setProperty(GTMaterialProperties.FORMULA, "Mg₃Si₂H₄O₉");
        MaterialLibAPI.editMaterial("gregtech", "Realgar")
            .setProperty(GTMaterialProperties.FORMULA, "As₄S₄");
        MaterialLibAPI.editMaterial("gregtech", "Dolomite")
            .setProperty(GTMaterialProperties.FORMULA, "CaMgC₂O₆");
        MaterialLibAPI.editMaterial("gregtech", "Wollastonite")
            .setProperty(GTMaterialProperties.FORMULA, "CaSiO₃");
        MaterialLibAPI.editMaterial("gregtech", "Zeolite")
            .setProperty(GTMaterialProperties.FORMULA, "NaCa₄Si₂₇Al₉O₇₂");
        MaterialLibAPI.editMaterial("gregtech", "BandedIron")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₂O₃");
        MaterialLibAPI.editMaterial("gregtech", "Ilmenite")
            .setProperty(GTMaterialProperties.FORMULA, "FeTiO₃");
        MaterialLibAPI.editMaterial("gregtech", "Pollucite")
            .setProperty(GTMaterialProperties.FORMULA, "Cs₂Al₂Si₄(H₂O)₂O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Tantalite")
            .setProperty(GTMaterialProperties.FORMULA, "MnTa₂O₆");
        MaterialLibAPI.editMaterial("gregtech", "Uraninite")
            .setProperty(GTMaterialProperties.FORMULA, "UO₂");
        MaterialLibAPI.editMaterial("gregtech", "VanadiumMagnetite")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₃O₄)V");
        MaterialLibAPI.editMaterial("gregtech", "Kyanite")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂SiO₅");
        MaterialLibAPI.editMaterial("gregtech", "Perlite")
            .setProperty(GTMaterialProperties.FORMULA, "(MgFeSi₂O₈)₂(H₂O)");
        MaterialLibAPI.editMaterial("gregtech", "Bentonite")
            .setProperty(GTMaterialProperties.FORMULA, "NaMg₆Si₁₂H₆(H₂O)₅O₃₆");
        MaterialLibAPI.editMaterial("gregtech", "FullersEarth")
            .setProperty(GTMaterialProperties.FORMULA, "MgSi₄H(H₂O)₄O₁₁");
        MaterialLibAPI.editMaterial("gregtech", "Kaolinite")
            .setProperty(GTMaterialProperties.FORMULA, "Al₂Si₂H₄O₉");
        MaterialLibAPI.editMaterial("gregtech", "BrownLimonite")
            .setProperty(GTMaterialProperties.FORMULA, "FeHO₂");
        MaterialLibAPI.editMaterial("gregtech", "YellowLimonite")
            .setProperty(GTMaterialProperties.FORMULA, "FeHO₂");
        MaterialLibAPI.editMaterial("gregtech", "Vermiculite")
            .setProperty(GTMaterialProperties.FORMULA, "Fe₃Al₄Si₄H₂(H₂O)₄O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Glauconite")
            .setProperty(GTMaterialProperties.FORMULA, "KMg₂Al₄H₂O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Gypsum")
            .setProperty(GTMaterialProperties.FORMULA, "CaSO₄(H₂O)₂");
        MaterialLibAPI.editMaterial("gregtech", "BasalticMineralSand")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₃O₄)((Mg₂Fe(SiO₂)₂)(CaCO₃)₃(SiO₂)₈C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??₄)");
        MaterialLibAPI.editMaterial("gregtech", "GraniticMineralSand")
            .setProperty(GTMaterialProperties.FORMULA, "(Fe₃O₄)((SiO₂)₄(KMg₃Al₃F₂Si₃O₁" + CustomGlyphs.SUBSCRIPT0 + "))");
        MaterialLibAPI.editMaterial("gregtech", "CassiteriteSand")
            .setProperty(GTMaterialProperties.FORMULA, "SnO₂");
        MaterialLibAPI.editMaterial("gregtech", "GarnetSand")
            .setProperty(GTMaterialProperties.FORMULA, "((Al₂Mg₃Si₃O₁₂)₃(Al₂Fe₃Si₃O₁₂)₅(Al₂Mn₃Si₃O₁₂)₈)((Ca₃Fe₂Si₃O₁₂)₅(Ca₃Al₂Si₃O₁₂)₈(Ca₃Cr₂Si₃O₁₂)₃)");
        MaterialLibAPI.editMaterial("gregtech", "QuartzSand")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??");
        MaterialLibAPI.editMaterial("gregtech", "VolcanicAsh")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)₆FeMg");
        MaterialLibAPI.editMaterial("gregtech", "Borax")
            .setProperty(GTMaterialProperties.FORMULA, "Na₂B₄O₇(H₂O)₁" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "Molybdenite")
            .setProperty(GTMaterialProperties.FORMULA, "MoS₂");
        MaterialLibAPI.editMaterial("gregtech", "Pyrolusite")
            .setProperty(GTMaterialProperties.FORMULA, "MnO₂");
        MaterialLibAPI.editMaterial("gregtech", "Stibnite")
            .setProperty(GTMaterialProperties.FORMULA, "Sb₂S₃");
        MaterialLibAPI.editMaterial("gregtech", "Asbestos")
            .setProperty(GTMaterialProperties.FORMULA, "Mg₃Si₂H₄O₉");
        MaterialLibAPI.editMaterial("gregtech", "Diatomite")
            .setProperty(GTMaterialProperties.FORMULA, "(SiO₂)₈(Fe₂O₃)(Al₂O₃)");
        MaterialLibAPI.editMaterial("gregtech", "GlauconiteSand")
            .setProperty(GTMaterialProperties.FORMULA, "KMg₂Al₄H₂O₁₂");
        MaterialLibAPI.editMaterial("gregtech", "Vyroxeres")
            .setProperty(GTMaterialProperties.FORMULA, "SpBe");
        MaterialLibAPI.editMaterial("gregtech", "Ceruclase")
            .setProperty(GTMaterialProperties.FORMULA, "SpAg");
        MaterialLibAPI.editMaterial("gregtech", "Tartarite")
            .setProperty(GTMaterialProperties.FORMULA, "Tt");
        MaterialLibAPI.editMaterial("gregtech", "Orichalcum")
            .setProperty(GTMaterialProperties.FORMULA, "SpBi");
        MaterialLibAPI.editMaterial("gregtech", "SiliconTetrafluoride")
            .setProperty(GTMaterialProperties.FORMULA, "SiF₄");
        MaterialLibAPI.editMaterial("gregtech", "SiliconTetrachloride")
            .setProperty(GTMaterialProperties.FORMULA, "SiCl₄");
        MaterialLibAPI.editMaterial("gregtech", "Aluminiumfluoride")
            .setProperty(GTMaterialProperties.FORMULA, "AlF₃");
        MaterialLibAPI.editMaterial("gregtech", "Void")
            .setProperty(GTMaterialProperties.FORMULA, "ShFeMa₃");
        MaterialLibAPI.editMaterial("gregtech", "CalciumDisilicide")
            .setProperty(GTMaterialProperties.FORMULA, "CaSi₂");
        MaterialLibAPI.editMaterial("gregtech", "Trichlorosilane")
            .setProperty(GTMaterialProperties.FORMULA, "HSiCl₃");
    }

    private static void initPart11() {
        MaterialLibAPI.editMaterial("gregtech", "Hexachlorodisilane")
            .setProperty(GTMaterialProperties.FORMULA, "Si₂Cl₆");
        MaterialLibAPI.editMaterial("gregtech", "SuperconductorUEVBase")
            .setProperty(GTMaterialProperties.FORMULA, "D*₅If*₅(✦◆✦)(⚷⚙⚷Ni4Ti6)");
        MaterialLibAPI.editMaterial("gregtech", "Draconium")
            .setProperty(GTMaterialProperties.FORMULA, "D");
        MaterialLibAPI.editMaterial("gregtech", "DraconiumAwakened")
            .setProperty(GTMaterialProperties.FORMULA, "D*");
        MaterialLibAPI.editMaterial("gregtech", "BloodInfusedIron")
            .setProperty(GTMaterialProperties.FORMULA, CustomGlyphs.BRIMSTONE + "Fe");
        MaterialLibAPI.editMaterial("gregtech", "Ichorium")
            .setProperty(GTMaterialProperties.FORMULA, "IcMa");
        MaterialLibAPI.editMaterial("gregtech", "RadoxPoly")
            .setProperty(GTMaterialProperties.FORMULA, "C₁₄Os₁₁O₇Ag₃SpH₂O");
        MaterialLibAPI.editMaterial("gregtech", "GalliumArsenide")
            .setProperty(GTMaterialProperties.FORMULA, "AsGa");
        MaterialLibAPI.editMaterial("gregtech", "IndiumGalliumPhosphide")
            .setProperty(GTMaterialProperties.FORMULA, "InGaP");
        MaterialLibAPI.editMaterial("gregtech", "CosmicNeutronium")
            .setProperty(GTMaterialProperties.FORMULA, "SpNt");
        MaterialLibAPI.editMaterial("gregtech", "MTBEReactionMixture(Butene)")
            .setProperty(GTMaterialProperties.FORMULA, "C₅H₁₂O");
        MaterialLibAPI.editMaterial("gregtech", "Flerovium_GT5U")
            .setProperty(GTMaterialProperties.FORMULA, "Fl");
        MaterialLibAPI.editMaterial("gregtech", "Longasssuperconductornameforuhvwire")
            .setProperty(GTMaterialProperties.FORMULA, "D₆(SpNt)₇Tn₅Am₆");
        MaterialLibAPI.editMaterial("gregtech", "Longasssuperconductornameforuvwire")
            .setProperty(GTMaterialProperties.FORMULA, "Nq*₄(Ir₃Os)₃EuSm");
        MaterialLibAPI.editMaterial("gregtech", "Pentacadmiummagnesiumhexaoxid")
            .setProperty(GTMaterialProperties.FORMULA, "Cd₅MgO₆");
        MaterialLibAPI.editMaterial("gregtech", "Titaniumonabariumdecacoppereikosaoxid")
            .setProperty(GTMaterialProperties.FORMULA, "TiBa₉Cu₁" + CustomGlyphs.SUBSCRIPT0 + "O₂" + CustomGlyphs.SUBSCRIPT0);
        MaterialLibAPI.editMaterial("gregtech", "Uraniumtriplatinid")
            .setProperty(GTMaterialProperties.FORMULA, "UPt₃");
        MaterialLibAPI.editMaterial("gregtech", "Vanadiumtriindinid")
            .setProperty(GTMaterialProperties.FORMULA, "VIn₃");
        MaterialLibAPI.editMaterial("gregtech", "Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid")
            .setProperty(GTMaterialProperties.FORMULA, "In₄Sn₂Ba₂TiCu₇O₁₄");
        MaterialLibAPI.editMaterial("gregtech", "Tetranaquadahdiindiumhexaplatiumosminid")
            .setProperty(GTMaterialProperties.FORMULA, "Nq₄In₂Pd₆Os");
        MaterialLibAPI.editMaterial("gregtech", "NitrousOxide")
            .setProperty(GTMaterialProperties.FORMULA, "N₂O");
        MaterialLibAPI.editMaterial("gregtech", "EthylTertButylEther")
            .setProperty(GTMaterialProperties.FORMULA, "C₆H₁₄O");
        MaterialLibAPI.editMaterial("gregtech", "Octane")
            .setProperty(GTMaterialProperties.FORMULA, "C₈H₁₈");
        MaterialLibAPI.editMaterial("gregtech", "Air")
            .setProperty(GTMaterialProperties.FORMULA, "N₄" + CustomGlyphs.SUBSCRIPT0 + "O₁₁Ar((CO₂)₂₁He₉(CH₄)₃D)");
        MaterialLibAPI.editMaterial("gregtech", "Magic")
            .setProperty(GTMaterialProperties.FORMULA, "Ma");
        MaterialLibAPI.editMaterial("gregtech", "FreshWater")
            .setProperty(GTMaterialProperties.FORMULA, "H₂O");
        MaterialLibAPI.editMaterial("gregtech", "NitroCoalFuel")
            .setProperty(GTMaterialProperties.FORMULA, "(C₃H₅N₃O₉)?₄");
        MaterialLibAPI.editMaterial("gregtech", "SodiumPotassium")
            .setProperty(GTMaterialProperties.FORMULA, "NaK");
        MaterialLibAPI.editMaterial("gregtech", "Vis")
            .setProperty(GTMaterialProperties.FORMULA, "Ma");
        MaterialLibAPI.editMaterial("gregtech", "NULL")
            .setProperty(GTMaterialProperties.FORMULA, "Empty");
    }

    // spotless:on

    private Materials2Formulas() {}
}
