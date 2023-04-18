package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations.AspectDefinitionCompat;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMComplexTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMIndirectType;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;

/**
 * Created by Tec on 06.05.2017.
 */
public final class EMComplexAspectDefinition extends EMComplexTemplate {

    private final int hash;
    private final double mass;

    private static final String nbtType = "`";

    private final EMConstantStackMap aspectStacks;

    public EMComplexAspectDefinition(EMDefinitionStack... aspects) throws EMException {
        this(true, new EMConstantStackMap(aspects));
    }

    public EMComplexAspectDefinition(EMConstantStackMap aspects) throws EMException {
        this(true, aspects);
    }

    private EMComplexAspectDefinition(boolean check, EMConstantStackMap aspects) throws EMException {
        if (check && !canTheyBeTogether(aspects)) {
            throw new EMException("Complex Aspect Definition error");
        }
        aspectStacks = aspects;
        float mass = 0;
        for (EMDefinitionStack stack : aspects.valuesToArray()) {
            mass += stack.getMass();
        }
        this.mass = mass;
        hash = super.hashCode();
    }

    // public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(EMConstantStackMap stacks) {
        long amount = 0;
        for (EMDefinitionStack aspects : stacks.valuesToArray()) {
            if (!(aspects.getDefinition() instanceof EMComplexAspectDefinition)
                    && !(aspects.getDefinition() instanceof EMPrimalAspectDefinition)) {
                return false;
            }
            if ((int) aspects.getAmount() != aspects.getAmount()) {
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            amount += aspects.getAmount();
        }
        return amount == 2;
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.Aspect");
    }

    @Override
    public String getShortLocalizedName() {
        String name = AspectDefinitionCompat.aspectDefinitionCompat.getAspectLocalizedName(this);
        if (name != null) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return getSymbol();
        }
    }

    @Override
    public String getSymbol() {
        StringBuilder symbol = new StringBuilder(8);
        for (EMDefinitionStack aspect : aspectStacks.valuesToArray()) {
            if (aspect.getDefinition() instanceof EMPrimalAspectDefinition) {
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getSymbol());
                }
            } else {
                symbol.append('(');
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getSymbol());
                }
                symbol.append(')');
            }
        }
        return symbol.toString();
    }

    @Override
    public String getShortSymbol() {
        StringBuilder symbol = new StringBuilder(8);
        for (EMDefinitionStack aspect : aspectStacks.valuesToArray()) {
            if (aspect.getDefinition() instanceof EMPrimalAspectDefinition) {
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getShortSymbol());
                }
            } else {
                symbol.append('(');
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getShortSymbol());
                }
                symbol.append(')');
            }
        }
        return symbol.toString();
    }

    @Override
    protected String getTagValue() {
        return nbtType;
    }

    @Override
    public double getRawTimeSpan(long currentEnergy) {
        return -1;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }

    @Override
    public int getCharge() {
        return 0;
    }

    @Override
    public int getGeneration() {
        return 0;
    }

    @Override
    public int getMaxColors() {
        return -1;
    }

    @Override
    public EMConstantStackMap getSubParticles() {
        return aspectStacks;
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        return new EMDecay[] { new EMDecay(0.75F, aspectStacks), deadEnd };
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
    public EMDecay[] getNaturalDecayInstant() {
        return NO_DECAY;
    }

    @Override
    public EMDecay[] getDecayArray() {
        return NO_DECAY;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public IEMDefinition getAnti() {
        return null;
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(
                nbtType,
                new EMIndirectType(
                        (definitionsRegistry, nbt) -> new EMComplexAspectDefinition(
                                EMConstantStackMap.fromNBT(definitionsRegistry, nbt)),
                        EMComplexAspectDefinition.class,
                        "tt.keyword.Aspect"));
        if (DEBUG_MODE) {
            TecTech.LOGGER
                    .info("Registered Elemental Matter Class: ComplexAspect " + nbtType + ' ' + getClassTypeStatic());
        }
    }

    @Override
    public int getMatterMassType() {
        return getClassTypeStatic();
    }

    public static int getClassTypeStatic() {
        return -96;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
