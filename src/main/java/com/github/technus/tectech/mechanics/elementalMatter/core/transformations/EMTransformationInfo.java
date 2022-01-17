package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.IEMDefinition;
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
public class EMTransformationInfo {
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
    public static final double AVOGADRO_CONSTANT_144_DIMINISHED = AVOGADRO_CONSTANT_144 - AVOGADRO_CONSTANT_EPSILON;
    public static final double AVOGADRO_CONSTANT_1000 = AVOGADRO_CONSTANT *1000D;
    public static final double AVOGADRO_CONSTANT_1000_DIMINISHED = AVOGADRO_CONSTANT_1000 - AVOGADRO_CONSTANT_EPSILON;

    public static EMTransformationInfo TRANSFORMATION_INFO = new EMTransformationInfo();

    private Map<Integer, EMFluidQuantizationInfo>               fluidQuantization;
    private Map<EMItemQuantizationInfo, EMItemQuantizationInfo> itemQuantization;
    private Map<Integer, EMOredictQuantizationInfo>             oredictQuantization;

    private Map<IEMDefinition, EMFluidDequantizationInfo>   fluidDequantization;
    private Map<IEMDefinition, EMItemDequantizationInfo>    itemDequantization;
    private Map<IEMDefinition, EMOredictDequantizationInfo> oredictDequantization;

    private EMTransformationInfo() {
        this(
                new HashMap<>(16), new HashMap<>(16), new HashMap<>(64),
                new HashMap<>(16), new HashMap<>(16), new HashMap<>(64)
        );
    }

    public EMTransformationInfo(
            Map<Integer, EMFluidQuantizationInfo> fluidQuantization,
            Map<EMItemQuantizationInfo, EMItemQuantizationInfo> itemQuantization,
            Map<Integer, EMOredictQuantizationInfo> oredictQuantization,
            Map<IEMDefinition, EMFluidDequantizationInfo> fluidDequantization,
            Map<IEMDefinition, EMItemDequantizationInfo> itemDequantization,
            Map<IEMDefinition, EMOredictDequantizationInfo> oredictDequantization) {
        this.setFluidQuantization(fluidQuantization);
        this.setItemQuantization(itemQuantization);
        this.setOredictQuantization(oredictQuantization);
        this.setFluidDequantization(fluidDequantization);
        this.setItemDequantization(itemDequantization);
        this.setOredictDequantization(oredictDequantization);
    }

    public void addFluid(IEMStack em, FluidStack fluidStack){
        getFluidQuantization().put(fluidStack.getFluidID(),new EMFluidQuantizationInfo(fluidStack,em));
        getFluidDequantization().put(em.getDefinition(),new EMFluidDequantizationInfo(em,fluidStack));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addFluid(IEMStack em, Fluid fluid, int fluidAmount){
        getFluidQuantization().put(fluid.getID(),new EMFluidQuantizationInfo(fluid,fluidAmount,em));
        getFluidDequantization().put(em.getDefinition(),new EMFluidDequantizationInfo(em,fluid,fluidAmount));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    private void addItemQuantization(EMItemQuantizationInfo aIQI){
        getItemQuantization().put(aIQI,aIQI);
    }

    public void addItem(IEMStack em, ItemStack itemStack, boolean skipNBT){
        addItemQuantization(new EMItemQuantizationInfo(itemStack,skipNBT,em));
        getItemDequantization().put(em.getDefinition(),new EMItemDequantizationInfo(em,itemStack));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addItem(IEMStack em, OrePrefixes prefix, Materials material, int amount, boolean skipNBT){
        addItemQuantization(new EMItemQuantizationInfo(prefix,material,amount,skipNBT,em));
        getItemDequantization().put(em.getDefinition(),new EMItemDequantizationInfo(em,prefix,material,amount));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addOredict(IEMStack em, String name, int qty){
        getOredictQuantization().put(OreDictionary.getOreID(name),new EMOredictQuantizationInfo(name,qty,em));
        getOredictDequantization().put(em.getDefinition(),new EMOredictDequantizationInfo(em,name,qty));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addOredict(IEMStack em, OrePrefixes prefix, Materials material, int qty){
        getOredictQuantization().put(OreDictionary.getOreID(prefix.name() + material.mName),new EMOredictQuantizationInfo(prefix,material,qty,em));
        getOredictDequantization().put(em.getDefinition(),new EMOredictDequantizationInfo(em,prefix,material,qty));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public void addOredict(IEMStack em, OrePrefixes prefix, String materialName, int qty){
        getOredictQuantization().put(OreDictionary.getOreID(prefix.name() + materialName),new EMOredictQuantizationInfo(prefix,materialName,qty,em));
        getOredictDequantization().put(em.getDefinition(),new EMOredictDequantizationInfo(em,prefix,materialName,qty));
        STACKS_REGISTERED.add(em.getDefinition());
        STACKS_REGISTERED.add(em.getDefinition().getAnti());
    }

    public Map<Integer, EMFluidQuantizationInfo> getFluidQuantization() {
        return fluidQuantization;
    }

    public void setFluidQuantization(Map<Integer, EMFluidQuantizationInfo> fluidQuantization) {
        this.fluidQuantization = fluidQuantization;
    }

    public Map<EMItemQuantizationInfo, EMItemQuantizationInfo> getItemQuantization() {
        return itemQuantization;
    }

    public void setItemQuantization(Map<EMItemQuantizationInfo, EMItemQuantizationInfo> itemQuantization) {
        this.itemQuantization = itemQuantization;
    }

    public Map<Integer, EMOredictQuantizationInfo> getOredictQuantization() {
        return oredictQuantization;
    }

    public void setOredictQuantization(Map<Integer, EMOredictQuantizationInfo> oredictQuantization) {
        this.oredictQuantization = oredictQuantization;
    }

    public Map<IEMDefinition, EMFluidDequantizationInfo> getFluidDequantization() {
        return fluidDequantization;
    }

    public void setFluidDequantization(Map<IEMDefinition, EMFluidDequantizationInfo> fluidDequantization) {
        this.fluidDequantization = fluidDequantization;
    }

    public Map<IEMDefinition, EMItemDequantizationInfo> getItemDequantization() {
        return itemDequantization;
    }

    public void setItemDequantization(Map<IEMDefinition, EMItemDequantizationInfo> itemDequantization) {
        this.itemDequantization = itemDequantization;
    }

    public Map<IEMDefinition, EMOredictDequantizationInfo> getOredictDequantization() {
        return oredictDequantization;
    }

    public void setOredictDequantization(Map<IEMDefinition, EMOredictDequantizationInfo> oredictDequantization) {
        this.oredictDequantization = oredictDequantization;
    }
}
