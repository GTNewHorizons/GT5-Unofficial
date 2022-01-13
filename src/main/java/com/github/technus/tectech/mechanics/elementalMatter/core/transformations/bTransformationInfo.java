package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iHasElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM.STACKS_REGISTERED;
import static java.lang.Math.pow;

/**
 * Created by Tec on 26.05.2017.
 */
public class bTransformationInfo {
    /**
     * Atom count per Mol
     */
    public static final double AVOGADRO_CONSTANT =6.02214076e23D;
    /**
     * Min. chance of existing
     */
    public static final double AVOGADRO_CONSTANT_UNCERTAINTY =(144*1000) / AVOGADRO_CONSTANT;
    /**
     * Quantity considered to be indifferent when computing stuff
     */
    public static final double AVOGADRO_CONSTANT_EPSILON = AVOGADRO_CONSTANT / pow(2,48);
    public static final double AVOGADRO_CONSTANT_DIMINISHED = AVOGADRO_CONSTANT - AVOGADRO_CONSTANT_EPSILON;
    public static final double AVOGADRO_CONSTANT_144 = AVOGADRO_CONSTANT *144D;
    public static final double AVOGADRO_CONSTANT_144_DIMINISHED = AVOGADRO_CONSTANT_144 - AVOGADRO_CONSTANT_EPSILON*144D;
    public static final double AVOGADRO_CONSTANT_1000 = AVOGADRO_CONSTANT *1000D;
    public static final double AVOGADRO_CONSTANT_1000_DIMINISHED = AVOGADRO_CONSTANT_1000 - AVOGADRO_CONSTANT_EPSILON*1000D;

    public static bTransformationInfo TRANSFORMATION_INFO = new bTransformationInfo();

    public Map<Integer,aFluidQuantizationInfo> fluidQuantization;
    public Map<aItemQuantizationInfo,aItemQuantizationInfo> itemQuantization;
    public Map<Integer,aOredictQuantizationInfo> oredictQuantization;

    public Map<iElementalDefinition,aFluidDequantizationInfo> fluidDequantization;
    public Map<iElementalDefinition,aItemDequantizationInfo> itemDequantization;
    public Map<iElementalDefinition,aOredictDequantizationInfo> oredictDequantization;

    private bTransformationInfo() {
        this(
                new HashMap<>(16), new HashMap<>(16), new HashMap<>(64),
                new HashMap<>(16), new HashMap<>(16), new HashMap<>(64)
        );
    }

    public bTransformationInfo(
            Map<Integer, aFluidQuantizationInfo> fluidQuantization,
            Map<aItemQuantizationInfo, aItemQuantizationInfo> itemQuantization,
            Map<Integer, aOredictQuantizationInfo> oredictQuantization,
            Map<iElementalDefinition, aFluidDequantizationInfo> fluidDequantization,
            Map<iElementalDefinition, aItemDequantizationInfo> itemDequantization,
            Map<iElementalDefinition, aOredictDequantizationInfo> oredictDequantization) {
        this.fluidQuantization = fluidQuantization;
        this.itemQuantization = itemQuantization;
        this.oredictQuantization = oredictQuantization;
        this.fluidDequantization = fluidDequantization;
        this.itemDequantization = itemDequantization;
        this.oredictDequantization = oredictDequantization;
    }

    public void addFluid(iHasElementalDefinition em, FluidStack fluidStack){
        fluidQuantization.put(fluidStack.getFluidID(),new aFluidQuantizationInfo(fluidStack,em));
        fluidDequantization.put(em.getDefinition(),new aFluidDequantizationInfo(em,fluidStack));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addFluid(iHasElementalDefinition em, Fluid fluid, int fluidAmount){
        fluidQuantization.put(fluid.getID(),new aFluidQuantizationInfo(fluid,fluidAmount,em));
        fluidDequantization.put(em.getDefinition(),new aFluidDequantizationInfo(em,fluid,fluidAmount));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    private void addItemQuantization(aItemQuantizationInfo aIQI){
        itemQuantization.put(aIQI,aIQI);
    }

    public void addItem(iHasElementalDefinition em, ItemStack itemStack, boolean skipNBT){
        addItemQuantization(new aItemQuantizationInfo(itemStack,skipNBT,em));
        itemDequantization.put(em.getDefinition(),new aItemDequantizationInfo(em,itemStack));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addItem(iHasElementalDefinition em, OrePrefixes prefix, Materials material, int amount, boolean skipNBT){
        addItemQuantization(new aItemQuantizationInfo(prefix,material,amount,skipNBT,em));
        itemDequantization.put(em.getDefinition(),new aItemDequantizationInfo(em,prefix,material,amount));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addOredict(iHasElementalDefinition em, String name, int qty){
        oredictQuantization.put(OreDictionary.getOreID(name),new aOredictQuantizationInfo(name,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,name,qty));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addOredict(iHasElementalDefinition em, OrePrefixes prefix, Materials material, int qty){
        oredictQuantization.put(OreDictionary.getOreID(prefix.name() + material.mName),new aOredictQuantizationInfo(prefix,material,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,prefix,material,qty));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addOredict(iHasElementalDefinition em, OrePrefixes prefix, String materialName, int qty){
        oredictQuantization.put(OreDictionary.getOreID(prefix.name() + materialName),new aOredictQuantizationInfo(prefix,materialName,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,prefix,materialName,qty));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }
}
