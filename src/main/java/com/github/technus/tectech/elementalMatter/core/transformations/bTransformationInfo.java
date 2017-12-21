package com.github.technus.tectech.elementalMatter.core.transformations;

import com.github.technus.tectech.elementalMatter.core.stacks.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

/**
 * Created by Tec on 26.05.2017.
 */
public class bTransformationInfo {
    final static public HashMap<Integer,aFluidQuantizationInfo> fluidQuantization=new HashMap<>(32);
    public HashMap<iElementalDefinition,aFluidDequantizationInfo> fluidDequantization;

    final static public HashMap<aItemQuantizationInfo,aItemQuantizationInfo> itemQuantization=new HashMap<>(32);
    public HashMap<iElementalDefinition,aItemDequantizationInfo> itemDequantization;

    final static public HashMap<Integer,aOredictQuantizationInfo> oredictQuantization=new HashMap<>(32);
    public HashMap<iElementalDefinition,aOredictDequantizationInfo> oredictDequantization;

    public bTransformationInfo(){
        fluidDequantization=new HashMap<>();
        itemDequantization=new HashMap<>();
        oredictDequantization=new HashMap<>();
    }

    public bTransformationInfo(int fluidCap,int itemCap, int oreCap){
        if(fluidCap>0)fluidDequantization=new HashMap<>(fluidCap);
        if(itemCap>0)itemDequantization=new HashMap<>(itemCap);
        if(oreCap>0)oredictDequantization=new HashMap<>(oreCap);
    }

    public void addFluid(iHasElementalDefinition em, FluidStack fluidStack){
        fluidQuantization.put(fluidStack.getFluidID(),new aFluidQuantizationInfo(fluidStack,em));
        fluidDequantization.put(em.getDefinition(),new aFluidDequantizationInfo(em,fluidStack));
    }

    public void addFluid(iHasElementalDefinition em ,int fluidID,int fluidAmount) {
        fluidQuantization.put(fluidID,new aFluidQuantizationInfo(fluidID,fluidAmount,em));
        fluidDequantization.put(em.getDefinition(),new aFluidDequantizationInfo(em,fluidID,fluidAmount));
    }

    public void addFluid(iHasElementalDefinition em, Fluid fluid, int fluidAmount){
        fluidQuantization.put(fluid.getID(),new aFluidQuantizationInfo(fluid,fluidAmount,em));
        fluidDequantization.put(em.getDefinition(),new aFluidDequantizationInfo(em,fluid,fluidAmount));
    }

    public void addItemQuantization(aItemQuantizationInfo aIQI){
        itemQuantization.put(aIQI,aIQI);
    }

    public void addItem(iHasElementalDefinition em, ItemStack itemStack, boolean skipNBT){
        addItemQuantization(new aItemQuantizationInfo(itemStack,skipNBT,em));
        itemDequantization.put(em.getDefinition(),new aItemDequantizationInfo(em,itemStack));
    }

    public void addItem(iHasElementalDefinition em, OrePrefixes prefix, Materials material, int amount, boolean skipNBT){
        addItemQuantization(new aItemQuantizationInfo(prefix,material,amount,skipNBT,em));
        itemDequantization.put(em.getDefinition(),new aItemDequantizationInfo(em,prefix,material,amount));
    }

    public void addOredict(iHasElementalDefinition em, int id, int qty){
        oredictQuantization.put(id,new aOredictQuantizationInfo(id,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,id,qty));
    }

    public void addOredict(iHasElementalDefinition em, String name, int qty){
        oredictQuantization.put(OreDictionary.getOreID(name),new aOredictQuantizationInfo(name,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,name,qty));
    }

    public void addOredict(iHasElementalDefinition em, OrePrefixes prefix, Materials material, int qty){
        oredictQuantization.put(OreDictionary.getOreID(prefix.name() + material.mName),new aOredictQuantizationInfo(prefix,material,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,prefix,material,qty));
    }

    public void addOredict(iHasElementalDefinition em, OrePrefixes prefix, String materialName, int qty){
        oredictQuantization.put(OreDictionary.getOreID(prefix.name() + materialName),new aOredictQuantizationInfo(prefix,materialName,qty,em));
        oredictDequantization.put(em.getDefinition(),new aOredictDequantizationInfo(em,prefix,materialName,qty));
    }
}
