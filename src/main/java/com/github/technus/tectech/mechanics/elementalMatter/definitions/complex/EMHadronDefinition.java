package com.github.technus.tectech.mechanics.elementalMatter.definitions.complex;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_Y__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.oredict.OreDictionary;

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
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMOredictQuantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.OreDictionaryStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

/**
 * Created by danie_000 on 17.11.2016.
 */
public class EMHadronDefinition extends EMComplexTemplate { // TODO Optimize map i/o

    private final int hash;

    private static final String nbtType = "h";
    // Helpers
    public static final Map<EMHadronDefinition, String> SYMBOL_MAP = new HashMap<>();
    public static final Map<EMHadronDefinition, String> UNLOCALIZED_NAME_MAP = new HashMap<>();
    public static EMHadronDefinition hadron_p, hadron_n, hadron_p_, hadron_n_;
    public static EMDefinitionStack hadron_p1, hadron_n1, hadron_p2, hadron_n2, hadron_p3, hadron_n3, hadron_p5;
    private static double protonMass = 0D;
    private static double neutronMass = 0D;
    private static final double actualProtonMass = 938272081.3D;
    private static final double actualNeutronMass = 939565413.3D;

    // float-mass in eV/c^2
    private final double mass;
    // int -electric charge in 1/3rds of electron charge for optimization
    private final int charge;
    private final double rawLifeTime;
    private final int amount;
    // generation max present inside - minus if contains any antiquark
    private final byte type;


    private final EMConstantStackMap quarkStacks;

    public EMHadronDefinition(EMDefinitionStack... quarks) throws EMException {
        this(true, new EMConstantStackMap(quarks));
    }

    private EMHadronDefinition(boolean check, EMDefinitionStack... quarks) throws EMException {
        this(check, new EMConstantStackMap(quarks));
    }

    public EMHadronDefinition(EMConstantStackMap quarks) throws EMException {
        this(true, quarks);
    }

    private EMHadronDefinition(boolean check, EMConstantStackMap quarks) throws EMException {
        if (check && !canTheyBeTogether(quarks)) {
            throw new EMException("Hadron Definition error");
        }
        quarkStacks = quarks;

        int amount = 0;
        int charge = 0;
        int type = 0;
        boolean containsAnti = false;
        double mass = 0;
        for (EMDefinitionStack quarkStack : quarkStacks.valuesToArray()) {
            amount += quarkStack.getAmount();
            if ((int) quarkStack.getAmount() != quarkStack.getAmount()) {
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            mass += quarkStack.getMass();
            charge += quarkStack.getCharge();
            type = Math.max(Math.abs(quarkStack.getDefinition().getGeneration()), type);
            if (quarkStack.getDefinition().getGeneration() < 0) {
                containsAnti = true;
            }
        }
        this.amount = amount;
        this.charge = charge;
        this.type = containsAnti ? (byte) -type : (byte) type;
        long mult = (long) this.getAmount() * this.getAmount() * (this.getAmount() - 1);
        mass = mass * 5.543D * mult; // yes it becomes heavier

        if (mass == protonMass && this.getAmount() == 3) {
            rawLifeTime = IEMDefinition.STABLE_RAW_LIFE_TIME;
            mass = actualProtonMass;
        } else if (mass == neutronMass && this.getAmount() == 3) {
            rawLifeTime = 882D;
            mass = actualNeutronMass;
        } else {
            if (this.getAmount() == 3) {
                rawLifeTime = 1.34D / mass * Math.pow(9.81, charge);
            } else if (this.getAmount() == 2) {
                rawLifeTime = 1.21D / mass / Math.pow(19.80, charge);
            } else {
                rawLifeTime = 1.21D / mass / Math.pow(9.80, charge);
            }
        }
        this.mass = mass;
        hash = super.hashCode();
    }

    // public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(EMConstantStackMap stacks) {
        long amount = 0;
        for (EMDefinitionStack quarks : stacks.valuesToArray()) {
            if (!(quarks.getDefinition() instanceof EMQuarkDefinition)) {
                return false;
            }
            if ((int) quarks.getAmount() != quarks.getAmount()) {
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            amount += quarks.getAmount();
        }
        return amount >= 2 && amount <= 12;
    }

    @Override
    public String getShortLocalizedName() {
        StringBuilder name = new StringBuilder();
        String sym = translateToLocal(UNLOCALIZED_NAME_MAP.get(this));
        if (sym != null) {
            name.append(' ').append(sym);
        } else {
            for (EMDefinitionStack quark : quarkStacks.valuesToArray()) {
                name.append(' ').append(quark.getDefinition().getSymbol()).append((int) quark.getAmount());
            }
        }
        return name.toString();
    }

    @Override
    public String getLocalizedTypeName() {
        switch (getAmount()) {
            case 2:
                return translateToLocal("tt.keyword.Meson");
            case 3:
                return translateToLocal("tt.keyword.Baryon");
            case 4:
                return translateToLocal("tt.keyword.Tetraquark");
            case 5:
                return translateToLocal("tt.keyword.Pentaquark");
            case 6:
                return translateToLocal("tt.keyword.Hexaquark");
            default:
                return translateToLocal("tt.keyword.Hadron");
        }
    }

    @Override
    public String getSymbol() {
        String sym = SYMBOL_MAP.get(this);
        if (sym != null) {
            return sym;
        } else {
            StringBuilder symbol = new StringBuilder(8);
            for (EMDefinitionStack quark : quarkStacks.valuesToArray()) {
                for (int i = 0; i < quark.getAmount(); i++) {
                    symbol.append(quark.getDefinition().getSymbol());
                }
            }
            return symbol.toString();
        }
    }

    @Override
    public String getShortSymbol() {
        String sym = SYMBOL_MAP.get(this);
        if (sym != null) {
            return sym;
        } else {
            StringBuilder symbol = new StringBuilder(8);
            for (EMDefinitionStack quark : quarkStacks.valuesToArray()) {
                for (int i = 0; i < quark.getAmount(); i++) {
                    symbol.append(quark.getDefinition().getShortSymbol());
                }
            }
            return symbol.toString();
        }
    }

    @Override
    public int getMaxColors() {
        return -7;
    }

    @Override
    public EMConstantStackMap getSubParticles() {
        return quarkStacks;
    }

    @Override
    public EMDecay[] getNaturalDecayInstant() {
        EMDefinitionStack[] quarkStacks = this.quarkStacks.valuesToArray();
        if (getAmount() == 2 && quarkStacks.length == 2
                && quarkStacks[0].getDefinition().getMass() == quarkStacks[1].getDefinition().getMass()
                && quarkStacks[0].getDefinition().getGeneration() == -quarkStacks[1].getDefinition().getGeneration()) {
            return EMDecay.NO_PRODUCT;
        }
        ArrayList<EMDefinitionStack> decaysInto = new ArrayList<>();
        for (EMDefinitionStack quarks : quarkStacks) {
            if (quarks.getDefinition().getGeneration() == 1 || quarks.getDefinition().getGeneration() == -1) {
                // covers both quarks and antiquarks
                decaysInto.add(quarks);
            } else {
                // covers both quarks and antiquarks
                decaysInto.add(new EMDefinitionStack(boson_Y__, 2));
            }
        }
        return new EMDecay[] { new EMDecay(0.75D, decaysInto.toArray(new EMDefinitionStack[0])), deadEnd };
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        EMDefinitionStack[] quarkStacks = this.quarkStacks.valuesToArray();
        if (getAmount() == 2 && quarkStacks.length == 2
                && quarkStacks[0].getDefinition().getMass() == quarkStacks[1].getDefinition().getMass()
                && quarkStacks[0].getDefinition().getGeneration() == -quarkStacks[1].getDefinition().getGeneration()) {
            return EMDecay.NO_PRODUCT;
        }
        return new EMDecay[] { new EMDecay(0.75D, quarkStacks), deadEnd }; // decay into quarks
    }

    @Override
    public double getEnergyDiffBetweenStates(long currentEnergyLevel, long newEnergyLevel) {
        return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
    }

    @Override
    public boolean usesSpecialEnergeticDecayHandling() {
        return false;
    }

    @Override
    public boolean usesMultipleDecayCalls(long energyLevel) {
        return false;
    }

    @Override
    public boolean decayMakesEnergy(long energyLevel) {
        return false;
    }

    @Override
    public boolean fusionMakesEnergy(long energyLevel) {
        return false;
    }

    @Override
    public EMDecay[] getDecayArray() {
        EMDefinitionStack[] quarkStacks = this.quarkStacks.valuesToArray();
        if (getAmount() == 2 && quarkStacks.length == 2
                && quarkStacks[0].getDefinition().getMass() == quarkStacks[1].getDefinition().getMass()
                && quarkStacks[0].getDefinition().getGeneration() == -quarkStacks[1].getDefinition().getGeneration()) {
            return EMDecay.NO_PRODUCT;
        } else if (getAmount() != 3) {
            return new EMDecay[] { new EMDecay(0.95D, quarkStacks), deadEnd }; // decay into quarks
        } else {
            ArrayList<EMQuarkDefinition> newBaryon = new ArrayList<>();
            IEMDefinition[] Particles = new IEMDefinition[2];
            for (EMDefinitionStack quarks : quarkStacks) {
                for (int i = 0; i < quarks.getAmount(); i++) {
                    newBaryon.add((EMQuarkDefinition) quarks.getDefinition());
                }
            }
            // remove last
            EMQuarkDefinition lastQuark = newBaryon.remove(2);

            EMDefinitionStack[] decay;
            if (Math.abs(lastQuark.getGeneration()) > 1) {
                decay = lastQuark.getDecayArray()[1].getOutputStacks().valuesToArray();
            } else {
                decay = lastQuark.getDecayArray()[2].getOutputStacks().valuesToArray();
            }
            newBaryon.add((EMQuarkDefinition) decay[0].getDefinition());
            Particles[0] = decay[1].getDefinition();
            Particles[1] = decay[2].getDefinition();

            EMDefinitionStack[] contentOfBaryon = newBaryon.stream()
                    .map(eQuarkDefinition -> new EMDefinitionStack(eQuarkDefinition, 1))
                    .toArray(EMDefinitionStack[]::new);

            try {
                return new EMDecay[] {
                        new EMDecay(
                                0.001D,
                                new EMHadronDefinition(false, contentOfBaryon),
                                Particles[0],
                                Particles[1],
                                boson_Y__),
                        new EMDecay(0.99D, new EMHadronDefinition(false, contentOfBaryon), Particles[0], Particles[1]),
                        deadEnd };
            } catch (EMException e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return new EMDecay[] { deadEnd };
            }
        }
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public double getRawTimeSpan(long currentEnergy) {
        return getRawLifeTime();
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }

    @Override
    public int getGeneration() {
        return type;
    }

    @Override
    public IEMDefinition getAnti() {
        EMDefinitionStackMap anti = new EMDefinitionStackMap();
        for (EMDefinitionStack stack : quarkStacks.valuesToArray()) {
            anti.putReplace(new EMDefinitionStack(stack.getDefinition().getAnti(), stack.getAmount()));
        }
        try {
            return new EMHadronDefinition(anti.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
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
                                nbt) -> new EMHadronDefinition(EMConstantStackMap.fromNBT(definitionsRegistry, nbt)),
                        EMHadronDefinition.class,
                        "tt.keyword.Hadron"));
        try {
            hadron_p = new EMHadronDefinition(
                    new EMConstantStackMap(
                            EMQuarkDefinition.quark_u.getStackForm(2),
                            EMQuarkDefinition.quark_d.getStackForm(1)));
            protonMass = hadron_p.getMass();
            // redefine the proton with proper lifetime (the lifetime is based on mass comparison)
            hadron_p = new EMHadronDefinition(
                    new EMConstantStackMap(
                            EMQuarkDefinition.quark_u.getStackForm(2),
                            EMQuarkDefinition.quark_d.getStackForm(1)));
            SYMBOL_MAP.put(hadron_p, "p");
            UNLOCALIZED_NAME_MAP.put(hadron_p, "tt.keyword.Proton");
            registry.registerForDisplay(hadron_p);
            registry.registerDirectDefinition("p", hadron_p);

            hadron_p_ = (EMHadronDefinition) hadron_p.getAnti();
            SYMBOL_MAP.put(hadron_p_, "~p");
            UNLOCALIZED_NAME_MAP.put(hadron_p_, "tt.keyword.AntiProton");
            registry.registerForDisplay(hadron_p_);
            registry.registerDirectDefinition("~p", hadron_p_);

            hadron_n = new EMHadronDefinition(
                    new EMConstantStackMap(
                            EMQuarkDefinition.quark_u.getStackForm(1),
                            EMQuarkDefinition.quark_d.getStackForm(2)));
            neutronMass = hadron_n.getMass();
            // redefine the neutron with proper lifetime (the lifetime is based on mass comparison)
            hadron_n = new EMHadronDefinition(
                    new EMConstantStackMap(
                            EMQuarkDefinition.quark_u.getStackForm(1),
                            EMQuarkDefinition.quark_d.getStackForm(2)));
            SYMBOL_MAP.put(hadron_n, "n");
            UNLOCALIZED_NAME_MAP.put(hadron_n, "tt.keyword.Neutron");
            registry.registerForDisplay(hadron_n);
            registry.registerDirectDefinition("n", hadron_n);

            hadron_n_ = (EMHadronDefinition) hadron_n.getAnti();
            SYMBOL_MAP.put(hadron_n_, "~n");
            UNLOCALIZED_NAME_MAP.put(hadron_n_, "tt.keyword.AntiNeutron");
            registry.registerForDisplay(hadron_n_);
            registry.registerDirectDefinition("~n", hadron_n_);

            TecTech.LOGGER.info("Old Neutron Mass: " + neutronMass);
            TecTech.LOGGER.info("Old Proton Mass: " + protonMass);
            TecTech.LOGGER.info("New Neutron Mass: " + EMHadronDefinition.hadron_n.getMass());
            TecTech.LOGGER.info("New Proton Mass: " + EMHadronDefinition.hadron_p.getMass());
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            protonMass = -1;
            neutronMass = -1;
        }
        hadron_p1 = new EMDefinitionStack(hadron_p, 1D);
        hadron_n1 = new EMDefinitionStack(hadron_n, 1D);
        hadron_p2 = new EMDefinitionStack(hadron_p, 2D);
        hadron_n2 = new EMDefinitionStack(hadron_n, 2D);
        hadron_p3 = new EMDefinitionStack(hadron_p, 3D);
        hadron_n3 = new EMDefinitionStack(hadron_n, 3D);
        hadron_p5 = new EMDefinitionStack(hadron_p, 5D);

        if (DEBUG_MODE) {
            TecTech.LOGGER.info("Registered Elemental Matter Class: Hadron " + nbtType + ' ' + getClassTypeStatic());
        }
    }

    public static void setTransformations(EMTransformationRegistry transformationInfo) { // Todo use Neutronium atom?
        // Added to atom map, but should be in its own
        EMDefinitionStack neutrons = new EMDefinitionStack(hadron_n, 1000 * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED);
        EMDequantizationInfo emDequantizationInfo = new EMDequantizationInfo(neutrons);
        emDequantizationInfo.setOre(
                new OreDictionaryStack(
                        1,
                        OreDictionary.getOreID(OrePrefixes.dust.name() + Materials.Neutronium.mName))); // todo shitty
                                                                                                        // looking, but
                                                                                                        // works...
        transformationInfo.getInfoMap().put(neutrons.getDefinition(), emDequantizationInfo);
        transformationInfo.getOredictQuantization().put(
                OreDictionary.getOreID(OrePrefixes.ingotHot.name() + Materials.Neutronium.mName),
                new EMOredictQuantizationInfo(OrePrefixes.ingotHot, Materials.Neutronium, 1, neutrons));
    }

    @Override
    public int getMatterMassType() {
        return getClassTypeStatic();
    }

    public static int getClassTypeStatic() {
        return -64;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public double getRawLifeTime() {
        return rawLifeTime;
    }

    public int getAmount() {
        return amount;
    }
}
