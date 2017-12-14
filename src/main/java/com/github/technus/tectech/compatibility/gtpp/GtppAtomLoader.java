package com.github.technus.tectech.compatibility.gtpp;

import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MaterialGenerator;

import static com.github.technus.tectech.elementalMatter.definitions.complex.dAtomDefinition.*;

public class GtppAtomLoader implements Runnable{
    @Override
    public void run() {
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(10), 144), ELEMENT.getInstance().NEON.getFluid(144));
        MaterialGenerator.generate(ELEMENT.getInstance().GERMANIUM);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(32), 144), OrePrefixes.dust, ELEMENT.getInstance().GERMANIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(34), 144), OrePrefixes.dust, ELEMENT.getInstance().SELENIUM.getUnlocalizedName(),1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(35), 144), ELEMENT.getInstance().BROMINE.getFluid(144));
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(36), 144), ELEMENT.getInstance().KRYPTON.getFluid(144));
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(40), 144),OrePrefixes.dust, ELEMENT.getInstance().ZIRCONIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(43), 144),OrePrefixes.dust, ELEMENT.getInstance().TECHNETIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(44), 144),OrePrefixes.dust, ELEMENT.getInstance().RUTHENIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(45), 144),OrePrefixes.dust, ELEMENT.getInstance().RHODIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(53), 144),OrePrefixes.dust, ELEMENT.getInstance().IODINE.getUnlocalizedName(),1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(54), 144),ELEMENT.getInstance().XENON.getFluid(144));
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(72), 144),OrePrefixes.dust, ELEMENT.getInstance().HAFNIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(75), 144),OrePrefixes.dust, ELEMENT.getInstance().RHENIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(81), 144),OrePrefixes.dust, ELEMENT.getInstance().THALLIUM.getUnlocalizedName(),1);

        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(84),144),OrePrefixes.dust, ELEMENT.getInstance().POLONIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(85),144),OrePrefixes.dust, ELEMENT.getInstance().ASTATINE.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(87),144),OrePrefixes.dust, ELEMENT.getInstance().FRANCIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(88),144),OrePrefixes.dust, ELEMENT.getInstance().RADIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(89),144),OrePrefixes.dust, ELEMENT.getInstance().ACTINIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(91),144),OrePrefixes.dust, ELEMENT.getInstance().PROTACTINIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(93),144),OrePrefixes.dust, ELEMENT.getInstance().NEPTUNIUM.getUnlocalizedName(),1);

        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(96),144),OrePrefixes.dust, ELEMENT.getInstance().CURIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(97),144),OrePrefixes.dust, ELEMENT.getInstance().BERKELIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(98),144),OrePrefixes.dust, ELEMENT.getInstance().CALIFORNIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(99),144),OrePrefixes.dust, ELEMENT.getInstance().EINSTEINIUM.getUnlocalizedName(),1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(100),144),OrePrefixes.dust, ELEMENT.getInstance().FERMIUM.getUnlocalizedName(),1);
    }
}
