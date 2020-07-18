package com.github.technus.tectech.compatibility.gtpp;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import gregtech.api.enums.OrePrefixes;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Method;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo.AVOGADRO_CONSTANT_144;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.dAtomDefinition.*;

public class GtppAtomLoader implements Runnable{
    //region reflect a bit
    private Class ELEMENT;
    private Object ELEMENT_INSTANCE;
    private Method getUnlocalizedName,getFluid,generate;

    private String getUnlocalizedName(String elementName){
        try {
            return (String) getUnlocalizedName.invoke(ELEMENT.getField(elementName).get(ELEMENT_INSTANCE));
        }catch (Exception e){
            throw new Error(e);
        }
    }

    private FluidStack getFluid(String elementName, int fluidAmount){
        try {
            return (FluidStack) getFluid.invoke(ELEMENT.getField(elementName).get(ELEMENT_INSTANCE),fluidAmount);
        }catch (Exception e){
            throw new Error(e);
        }
    }

    private void generate(String elementName,boolean generateAll, boolean generateBlastRecipes){
        try {
            generate.invoke(null,ELEMENT.getField(elementName).get(ELEMENT_INSTANCE),generateAll,generateBlastRecipes);
        }catch (Exception e){
            throw new Error(e);
        }
    }
    //endregion

    @Override
    public void run() {
        //region reflect a bit
        try{
            ELEMENT=Class.forName("gtPlusPlus.core.material.ELEMENT");
            ELEMENT_INSTANCE=ELEMENT.getMethod("getInstance").invoke(null);

            Class clazz=Class.forName("gtPlusPlus.core.material.Material");
            getUnlocalizedName=clazz.getMethod("getUnlocalizedName");
            getFluid=clazz.getMethod("getFluid", int.class);

            clazz=Class.forName("gtPlusPlus.core.material.MaterialGenerator");
            generate=clazz.getMethod("generate", Class.forName("gtPlusPlus.core.material.Material"), boolean.class, boolean.class);
        }catch (Exception e){
            throw new Error(e);
        }
        //endregion

        TRANSFORMATION_INFO.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(10), AVOGADRO_CONSTANT_144), getFluid("NEON",144));
        generate("GERMANIUM",true,true);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(32), AVOGADRO_CONSTANT_144), OrePrefixes.dust, getUnlocalizedName("GERMANIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(34), AVOGADRO_CONSTANT_144), OrePrefixes.dust, getUnlocalizedName("SELENIUM"),1);
        TRANSFORMATION_INFO.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(35), AVOGADRO_CONSTANT_144), getFluid("BROMINE",144));
        TRANSFORMATION_INFO.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(36), AVOGADRO_CONSTANT_144), getFluid("KRYPTON",144));
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(40), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("ZIRCONIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(43), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("TECHNETIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(44), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RUTHENIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(45), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RHODIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(53), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("IODINE"),1);
        TRANSFORMATION_INFO.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(54), AVOGADRO_CONSTANT_144),getFluid("XENON",144));
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(72), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("HAFNIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(75), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RHENIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(81), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("THALLIUM"),1);

        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(84), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("POLONIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(85), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("ASTATINE"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(87), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("FRANCIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(88), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RADIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(89), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("ACTINIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(91), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("PROTACTINIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(93), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("NEPTUNIUM"),1);

        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(96), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("CURIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(97), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("BERKELIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(98), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("CALIFORNIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(99), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("EINSTEINIUM"),1);
        TRANSFORMATION_INFO.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(100), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("FERMIUM"),1);
    }
}
