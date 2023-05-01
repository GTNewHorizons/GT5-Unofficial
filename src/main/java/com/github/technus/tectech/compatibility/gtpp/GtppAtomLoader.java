package com.github.technus.tectech.compatibility.gtpp;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.getBestUnstableIsotope;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.getFirstStableIsotope;

import java.lang.reflect.Method;

import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;

import gregtech.api.enums.OrePrefixes;

public class GtppAtomLoader {

    // region reflect a bit
    private Class<?> ELEMENT;
    private Object ELEMENT_INSTANCE;
    private Method getUnlocalizedName, getFluid, generate;

    private String getUnlocalizedName(String elementName) {
        try {
            return (String) getUnlocalizedName.invoke(ELEMENT.getField(elementName).get(ELEMENT_INSTANCE));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private FluidStack getFluid(String elementName, int fluidAmount) {
        try {
            return (FluidStack) getFluid.invoke(ELEMENT.getField(elementName).get(ELEMENT_INSTANCE), fluidAmount);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generate(String elementName, boolean generateAll, boolean generateBlastRecipes) {
        try {
            generate.invoke(
                    null,
                    ELEMENT.getField(elementName).get(ELEMENT_INSTANCE),
                    generateAll,
                    generateBlastRecipes);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    // endregion

    public void setTransformations(EMTransformationRegistry transformationInfo) {
        // region reflect a bit
        try {
            ELEMENT = Class.forName("gtPlusPlus.core.material.ELEMENT");
            ELEMENT_INSTANCE = ELEMENT.getMethod("getInstance").invoke(null);

            Class<?> clazz = Class.forName("gtPlusPlus.core.material.Material");
            getUnlocalizedName = clazz.getMethod("getUnlocalizedName");
            try {
                getFluid = clazz.getMethod("getFluidStack", int.class);
            } catch (Exception e) {
                getFluid = clazz.getMethod("getFluid", int.class);
            }

            clazz = Class.forName("gtPlusPlus.core.material.MaterialGenerator");
            generate = clazz.getMethod(
                    "generate",
                    Class.forName("gtPlusPlus.core.material.Material"),
                    boolean.class,
                    boolean.class);
        } catch (Exception e) {
            throw new Error(e);
        }
        // endregion

        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(10), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                getFluid("NEON", 144));
        generate("GERMANIUM", true, true);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(32), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("GERMANIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(34), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("SELENIUM"),
                1);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(35), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                getFluid("BROMINE", 144));
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(36), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                getFluid("KRYPTON", 144));
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(40), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("ZIRCONIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(43), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("TECHNETIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(44), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("RUTHENIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(45), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("RHODIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(53), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("IODINE"),
                1);
        transformationInfo.addFluid(
                new EMDefinitionStack(getFirstStableIsotope(54), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                getFluid("XENON", 144));
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(72), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("HAFNIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(75), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("RHENIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getFirstStableIsotope(81), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("THALLIUM"),
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(84), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("POLONIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(85), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("ASTATINE"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(87), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("FRANCIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(88), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("RADIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(89), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("ACTINIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(91), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("PROTACTINIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(93), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("NEPTUNIUM"),
                1);

        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(96), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("CURIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(97), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("BERKELIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(98), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("CALIFORNIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(99), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("EINSTEINIUM"),
                1);
        transformationInfo.addOredict(
                new EMDefinitionStack(getBestUnstableIsotope(100), EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED),
                OrePrefixes.dust,
                getUnlocalizedName("FERMIUM"),
                1);
    }
}
