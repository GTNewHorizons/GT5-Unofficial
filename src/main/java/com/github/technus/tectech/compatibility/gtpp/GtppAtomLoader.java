package com.github.technus.tectech.compatibility.gtpp;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import gregtech.api.enums.OrePrefixes;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Method;

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

        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(10), 144), getFluid("NEON",144));
        generate("GERMANIUM",true,true);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(32), 144), OrePrefixes.dust, getUnlocalizedName("GERMANIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(34), 144), OrePrefixes.dust, getUnlocalizedName("SELENIUM"),1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(35), 144), getFluid("BROMINE",144));
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(36), 144), getFluid("KRYPTON",144));
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(40), 144),OrePrefixes.dust, getUnlocalizedName("ZIRCONIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(43), 144),OrePrefixes.dust, getUnlocalizedName("TECHNETIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(44), 144),OrePrefixes.dust, getUnlocalizedName("RUTHENIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(45), 144),OrePrefixes.dust, getUnlocalizedName("RHODIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(53), 144),OrePrefixes.dust, getUnlocalizedName("IODINE"),1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(54), 144),getFluid("XENON",144));
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(72), 144),OrePrefixes.dust, getUnlocalizedName("HAFNIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(75), 144),OrePrefixes.dust, getUnlocalizedName("RHENIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(81), 144),OrePrefixes.dust, getUnlocalizedName("THALLIUM"),1);

        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(84),144),OrePrefixes.dust, getUnlocalizedName("POLONIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(85),144),OrePrefixes.dust, getUnlocalizedName("ASTATINE"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(87),144),OrePrefixes.dust, getUnlocalizedName("FRANCIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(88),144),OrePrefixes.dust, getUnlocalizedName("RADIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(89),144),OrePrefixes.dust, getUnlocalizedName("ACTINIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(91),144),OrePrefixes.dust, getUnlocalizedName("PROTACTINIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(93),144),OrePrefixes.dust, getUnlocalizedName("NEPTUNIUM"),1);

        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(96),144),OrePrefixes.dust, getUnlocalizedName("CURIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(97),144),OrePrefixes.dust, getUnlocalizedName("BERKELIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(98),144),OrePrefixes.dust, getUnlocalizedName("CALIFORNIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(99),144),OrePrefixes.dust, getUnlocalizedName("EINSTEINIUM"),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(100),144),OrePrefixes.dust, getUnlocalizedName("FERMIUM"),1);
    }
}
