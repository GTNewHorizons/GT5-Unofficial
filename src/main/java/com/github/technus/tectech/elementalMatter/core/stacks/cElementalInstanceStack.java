package com.github.technus.tectech.elementalMatter.core.stacks;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class cElementalInstanceStack implements iHasElementalDefinition {
    public final iElementalDefinition definition;
    //energy - if positive then particle should try to decay
    private long energy;
    //byte color; 0=Red 1=Green 2=Blue 0=Cyan 1=Magenta 2=Yellow, else ignored (-1 - uncolorable)
    private byte color;
    public long age;
    public long amount;
    private float lifeTime;
    private float lifeTimeMult;

    public cElementalInstanceStack(cElementalDefinitionStack stackSafe) {
        this(stackSafe.definition, stackSafe.amount, 1F, 0, 0);
    }

    public cElementalInstanceStack(cElementalDefinitionStack stackSafe, float lifeTimeMult, long age, long energy) {
        this(stackSafe.definition, stackSafe.amount, lifeTimeMult, age, energy);
    }

    public cElementalInstanceStack(iElementalDefinition defSafe, long amount) {
        this(defSafe, amount, 1F, 0, 0);
    }

    public cElementalInstanceStack(iElementalDefinition defSafe, long amount, float lifeTimeMult, long age, long energy) {
        this.definition = defSafe == null ? null__ : defSafe;
        byte color = definition.getColor();
        if (color < 0 || color > 2) {//transforms colorable??? into proper color
            this.color = color;
        } else {
            this.color = (byte) (TecTech.Rnd.nextInt(3));
        }
        this.lifeTimeMult = lifeTimeMult;
        this.energy = energy;
        this.lifeTime = definition.getRawTimeSpan(energy) * this.lifeTimeMult;
        this.age = age;
        this.amount = amount;
    }

    //Clone proxy
    private cElementalInstanceStack(cElementalInstanceStack stack) {
        definition = stack.definition;
        energy = stack.energy;
        color = stack.color;
        age = stack.age;
        amount = stack.amount;
        lifeTime = stack.lifeTime;
        lifeTimeMult = stack.lifeTimeMult;
    }

    @Override
    public final cElementalInstanceStack clone() {
        return new cElementalInstanceStack(this);
    }

    @Override
    public long getAmount() {
        return amount;
    }

    public long getCharge() {
        return definition.getCharge() * amount;
    }

    public float getMass() {
        return definition.getMass() * amount;
    }

    public long getEnergy() {
        return energy;
    }

    public void setEnergy(long newEnergyLevel){
        energy=newEnergyLevel;
        setLifeTimeMultipleOfBaseValue(getLifeTimeMult());
    }

    @Deprecated //can be done from definition
    public float getEnergySettingCost(long currentEnergyLevel, long newEnergyLevel){
        return definition.getEnergyDiffBetweenStates(currentEnergyLevel,newEnergyLevel);
    }

    @Deprecated //can be done from definition
    public float getEnergySettingCost(long newEnergyLevel){
        return definition.getEnergyDiffBetweenStates(energy,newEnergyLevel);
    }

    public cElementalDefinitionStack getDefinitionStack() {
        return new cElementalDefinitionStack(definition, amount);
    }

    @Override
    public iElementalDefinition getDefinition() {
        return definition;
    }

    public byte getColor() {
        return color;
    }

    public byte setColor(byte color) {//does not allow changing magic element
        if (this.color < 0 || this.color > 2 || color < 0 || color >= 3) return this.color;
        return this.color = color;
    }

    public byte nextColor() {//does not allow changing magic element
        if (this.color < 0 || this.color > 2) return this.color;
        return this.color = (byte) (TecTech.Rnd.nextInt(3));
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public float setLifeTimeMultipleOfBaseValue(float mult) {
        if(mult<=0) //since infinity*0=nan
            throw new IllegalArgumentException("mult must be >0");
        this.lifeTimeMult = mult;
        if (definition.getRawTimeSpan(energy) <= 0) return this.lifeTime;
        this.lifeTime = definition.getRawTimeSpan(energy) * this.lifeTimeMult;
        return this.lifeTime;
    }

    public float getLifeTimeMult() {
        return lifeTimeMult;
    }

    public cElementalInstanceStackMap decay() {
        return decay(1F, age, 0);//try to decay without changes
    }

    public cElementalInstanceStackMap decay(long apparentAge, long postEnergize) {
        return decay(1F,apparentAge,postEnergize);
    }

    public cElementalInstanceStackMap decay(float lifeTimeMult, long apparentAge, long postEnergize) {
        long newEnergyLevel=postEnergize+this.energy;
        if(newEnergyLevel>0) newEnergyLevel-=1;
        else if(newEnergyLevel<0) newEnergyLevel+=1;
        if (this.energy > 0 && !definition.usesSpecialEnergeticDecayHandling()) {
            setLifeTimeMultipleOfBaseValue(getLifeTimeMult());
            return decayCompute(definition.getEnergyInducedDecay(this.energy), lifeTimeMult, -1, newEnergyLevel);
        }else if (definition.getRawTimeSpan(energy) < 0) {
            return null;//return null, decay cannot be achieved
        } else if(definition.isTimeSpanHalfLife()){
            return exponentialDecayCompute(energy>0?definition.getEnergyInducedDecay(this.energy):definition.getDecayArray(), lifeTimeMult, -1, newEnergyLevel);
        } else{
            if (1F > this.lifeTime) {
                return decayCompute(energy>0?definition.getEnergyInducedDecay(this.energy):definition.getNaturalDecayInstant(), lifeTimeMult, 0, newEnergyLevel);
            } else if (((float) apparentAge) > this.lifeTime) {
                return decayCompute(energy>0?definition.getEnergyInducedDecay(this.energy):definition.getDecayArray(), lifeTimeMult, 0, newEnergyLevel);
            }
        }
        return null;//return null since decay cannot be achieved
    }

    //Use to get direct decay output providing correct decay array
    private cElementalInstanceStackMap exponentialDecayCompute(cElementalDecay[] decays, float lifeTimeMult, long newProductsAge, long energy) {
        double decayInverseRatio=Math.pow(2d,1d/* 1 second *//(double)lifeTime);
        double newAmount=(double)amount/decayInverseRatio;
        long amountRemaining=((long)Math.floor(newAmount))+(TecTech.Rnd.nextDouble()<=newAmount-Math.floor(newAmount)?1:0);
        if(amountRemaining==amount) return null;//nothing decayed
        else if(amountRemaining<=0) return decayCompute(decays,lifeTimeMult,newProductsAge,energy);
        //split to non decaying and decaying part
        long amount=this.amount;
        this.amount-=amountRemaining;
        cElementalInstanceStackMap products=decayCompute(decays,lifeTimeMult,newProductsAge,energy);
        this.amount=amountRemaining;
        products.putUnify(this.clone());
        this.amount=amount;
        return products;
    }

    //Use to get direct decay output providing correct decay array
    public cElementalInstanceStackMap decayCompute(cElementalDecay[] decays, float lifeTimeMult, long newProductsAge, long energy) {
        if (decays == null) return null;//Can not decay so it wont
        else if (decays.length == 0)
            return new cElementalInstanceStackMap();//provide non null 0 length array for annihilation
        else if (decays.length == 1) {//only one type of decay :D, doesn't need dead end
            cElementalInstanceStackMap products=decays[0].getResults(lifeTimeMult, newProductsAge, energy, this.amount);
            if(newProductsAge<0){
                for(cElementalInstanceStack s:products.values()){
                    if(s.definition.equals(definition)){
                        s.age=this.age;
                        s.energy=this.energy;
                    }
                }
            }else{
                for(cElementalInstanceStack s:products.values()){
                    if(s.definition.equals(definition)){
                        s.energy=this.energy;
                    }
                }
            }
            return products;
        } else {
            cElementalInstanceStackMap output = new cElementalInstanceStackMap();
            final int differentDecays = decays.length;
            long[] qttyOfDecay = new long[differentDecays];
            long amountRemaining = this.amount, amount = this.amount;
            float remainingProbability = 1F;

            for (int i = 0; i < differentDecays; i++) {
                if (decays[i].probability > 1F) {
                    long thisDecayAmount = (long) (Math.floor(remainingProbability * (double) amount));
                    if (thisDecayAmount == 0) {
                        //remainingProbability=something;
                        break;
                    } else if (thisDecayAmount <= amountRemaining) {
                        amountRemaining -= thisDecayAmount;
                        qttyOfDecay[i] += thisDecayAmount;
                        break;
                    }
                    //in case too much was made
                    qttyOfDecay[i] += amountRemaining;
                    amountRemaining = 0;
                    //remainingProbability=0;
                    break;
                }
                long thisDecayAmount = (long) (Math.floor(decays[i].probability * (double) amount));
                if (thisDecayAmount <= amountRemaining && thisDecayAmount > 0) {//some was made
                    remainingProbability -= (decays[i].probability);
                    amountRemaining -= thisDecayAmount;
                    qttyOfDecay[i] += thisDecayAmount;
                } else if (thisDecayAmount > amountRemaining) {//too much was made
                    qttyOfDecay[i] += amountRemaining;
                    amountRemaining = 0;
                    //remainingProbability=0;
                    break;
                }//if 0
            }

            for (int i = 0; i < amountRemaining; i++) {
                double rand = TecTech.Rnd.nextDouble();
                for (int j = 0; j < differentDecays; j++) {//looking for the thing it decayed into
                    rand -= decays[j].probability;
                    if (rand <= 0D) {
                        qttyOfDecay[j]++;
                        break;
                    }
                }
            }

            for (int i = 0; i < differentDecays; i++) {
                if (qttyOfDecay[i] > 0)
                    output.putUnifyAll(decays[i].getResults(lifeTimeMult, newProductsAge, energy, qttyOfDecay[i]));
            }

            if(newProductsAge<0){
                for(cElementalInstanceStack s:output.values()){
                    if(s.definition.equals(definition)){
                        s.age=this.age;
                        s.energy=this.energy;
                    }
                }
            }else{
                for(cElementalInstanceStack s:output.values()){
                    if(s.definition.equals(definition)){
                        s.energy=this.energy;
                    }
                }
            }
            return output;
        }
    }

    public cElementalInstanceStack unifyIntoThis(cElementalInstanceStack... instances) {
        if (instances == null) return this;
        //returns with the definition from the first object passed
        long energy = this.energy * this.amount;
        float lifeTimeMul = this.lifeTimeMult;

        for (cElementalInstanceStack instance : instances) {
            if (instance != null && this.compareTo(instance) == 0) {
                this.amount += instance.amount;
                energy += instance.energy * instance.amount;
                lifeTimeMul = Math.min(lifeTimeMul, instance.lifeTimeMult);
                this.age = Math.max(this.age, instance.age);
            }
        }

        if (amount != 0) energy /= Math.abs(amount);

        this.energy = energy;
        this.setLifeTimeMultipleOfBaseValue(lifeTimeMul);
        return this;
    }

    public void addScanResults(ArrayList<String> lines, int[] detailsOnDepthLevels){
        final int capabilities=detailsOnDepthLevels[0];
        if(Util.areBitsSet(SCAN_GET_DEPTH_LEVEL,capabilities))
            lines.add("DEPTH = "+0);
        definition.addScanResults(lines,capabilities,energy);
        if(Util.areBitsSet(SCAN_GET_TIMESPAN_MULT,capabilities)) {
            lines.add("TIME SPAN MULTIPLIER = " + lifeTimeMult);
            if(Util.areBitsSet(SCAN_GET_TIMESPAN_INFO,capabilities))
                lines.add("TIME SPAN MULTIPLIED = "+lifeTime+" s");
        }
        if(Util.areBitsSet(SCAN_GET_AGE,capabilities))
            lines.add("AGE = " + age+" s");
        if(Util.areBitsSet(SCAN_GET_COLOR,capabilities))
            lines.add("COLOR = "+color+" RGB or CMY");
        if(Util.areBitsSet(SCAN_GET_ENERGY_LEVEL,capabilities))
            lines.add("ENERGY LEVEL = "+energy);
        if(Util.areBitsSet(SCAN_GET_AMOUNT,capabilities))
            lines.add("AMOUNT = "+amount);
        lines.add("");//def separator
        scanContents(lines,definition.getSubParticles(),1,detailsOnDepthLevels);
    }

    private void scanContents(ArrayList<String> lines, cElementalDefinitionStackMap definitions, int depth, int[] detailsOnDepthLevels){
        if(definitions!=null && depth<detailsOnDepthLevels.length){
            final int deeper=depth+1;
            for(cElementalDefinitionStack definitionStack:definitions.values()) {
                if(Util.areBitsSet(SCAN_GET_DEPTH_LEVEL,detailsOnDepthLevels[depth]))
                    lines.add("DEPTH = " + depth);
                definition.addScanResults(lines,detailsOnDepthLevels[depth],energy);
                if(Util.areBitsSet(SCAN_GET_AMOUNT,detailsOnDepthLevels[depth]))
                    lines.add("AMOUNT = "+definitionStack.amount);
                lines.add("");//def separator
                scanContents(lines,definitionStack.definition.getSubParticles(),deeper,detailsOnDepthLevels);
            }
        }
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", definition.toNBT());
        nbt.setLong("q", amount);
        nbt.setLong("e", energy);
        nbt.setByte("c", color);
        nbt.setLong("a", age);
        nbt.setFloat("m", lifeTimeMult);
        return nbt;
    }

    public static cElementalInstanceStack fromNBT(NBTTagCompound nbt) {
        NBTTagCompound definition = nbt.getCompoundTag("d");
        cElementalInstanceStack instance = new cElementalInstanceStack(
                cElementalDefinition.fromNBT(definition),
                nbt.getLong("q"),
                nbt.getFloat("m"),
                nbt.getLong("a"),
                nbt.getLong("e"));
        instance.setColor(nbt.getByte("c"));
        return instance;
    }

    @Override
    public int compareTo(iHasElementalDefinition o) {//use for unification
        return definition.compareTo(o.getDefinition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition)
            return definition.compareTo((iElementalDefinition) obj) == 0;
        if (obj instanceof iHasElementalDefinition)
            return definition.compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure, DOES NOT CARE ABOUT INSTANCE INFO
    @Override
    public int hashCode() {
        return definition.hashCode();
    }

    @Override
    public String toString() {
        return definition.getName()+ '\n' + definition.getSymbol() + '\n' + amount + '\n' + getMass();
    }
}
