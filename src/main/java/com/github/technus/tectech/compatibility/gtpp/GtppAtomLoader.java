package com.github.technus.tectech.compatibility.gtpp;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo;
import gregtech.api.enums.OrePrefixes;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Method;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo.AVOGADRO_CONSTANT_144;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.*;

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

    private static Method getMethodWithReplacements(Class<?> owner, String name1, String name2, Class<?>... arguments) throws ReflectiveOperationException {
        try {
            return owner.getMethod(name1, arguments);
        } catch (ReflectiveOperationException e) {
            return owner.getMethod(name2, arguments);
        }
    }

    @Override
    public void run() {
        //region reflect a bit
        try{
            ELEMENT=Class.forName("gtPlusPlus.core.material.ELEMENT");
            ELEMENT_INSTANCE=ELEMENT.getMethod("getInstance").invoke(null);

            Class<?> clazz=Class.forName("gtPlusPlus.core.material.Material");
            getUnlocalizedName=clazz.getMethod("getUnlocalizedName");
            getFluid=getMethodWithReplacements(clazz,"getFluidStack", "getFluid", int.class);

            clazz=Class.forName("gtPlusPlus.core.material.MaterialGenerator");
            generate=clazz.getMethod("generate", Class.forName("gtPlusPlus.core.material.Material"), boolean.class, boolean.class);
        }catch (Exception e){
            throw new Error(e);
        }
        //endregion

        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(10), AVOGADRO_CONSTANT_144), getFluid("NEON",144));
        generate("GERMANIUM",true,true);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(32), AVOGADRO_CONSTANT_144), OrePrefixes.dust, getUnlocalizedName("GERMANIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(34), AVOGADRO_CONSTANT_144), OrePrefixes.dust, getUnlocalizedName("SELENIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(35), AVOGADRO_CONSTANT_144), getFluid("BROMINE",144));
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(36), AVOGADRO_CONSTANT_144), getFluid("KRYPTON",144));
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(40), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("ZIRCONIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(43), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("TECHNETIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(44), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RUTHENIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(45), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RHODIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(53), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("IODINE"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(54), AVOGADRO_CONSTANT_144),getFluid("XENON",144));
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(72), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("HAFNIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(75), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RHENIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(81), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("THALLIUM"),1);

        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(84), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("POLONIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(85), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("ASTATINE"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(87), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("FRANCIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(88), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("RADIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(89), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("ACTINIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(91), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("PROTACTINIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(93), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("NEPTUNIUM"),1);

        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(96), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("CURIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(97), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("BERKELIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(98), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("CALIFORNIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(99), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("EINSTEINIUM"),1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(100), AVOGADRO_CONSTANT_144),OrePrefixes.dust, getUnlocalizedName("FERMIUM"),1);
    }
}
