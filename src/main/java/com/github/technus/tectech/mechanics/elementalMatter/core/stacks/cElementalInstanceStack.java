package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eBosonDefinition.deadEnd;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class cElementalInstanceStack implements iHasElementalDefinition {
    public static int MIN_MULTIPLE_DECAY_CALLS=4,MAX_MULTIPLE_DECAY_CALLS=16,DECAY_CALL_PER=144;

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
        definition = defSafe == null ? null__ : defSafe;
        byte bColor = definition.getColor();
        if (bColor < 0 || bColor > 2) {//transforms colorable??? into proper color
            this.color = bColor;
        } else {
            this.color = (byte) TecTech.RANDOM.nextInt(3);
        }
        this.lifeTimeMult = lifeTimeMult;
        lifeTime = definition.getRawTimeSpan(energy) * this.lifeTimeMult;
        setEnergy(energy);
        this.age = age;
        this.amount = amount;
    }

    //Clone proxy
    private cElementalInstanceStack(cElementalInstanceStack stack) {
        definition = stack.definition;
        color = stack.color;
        age = stack.age;
        amount = stack.amount;
        lifeTime = stack.lifeTime;
        lifeTimeMult = stack.lifeTimeMult;
        energy = stack.energy;
    }

    @Override
    public cElementalInstanceStack clone() {
        return new cElementalInstanceStack(this);
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCharge() {
        return definition.getCharge() * amount;
    }

    @Override
    public float getMass() {
        return definition.getMass() * amount;
    }

    public long getEnergy() {
        return energy;
    }

    public void setEnergy(long newEnergyLevel){
        energy=newEnergyLevel;
        setLifeTimeMultiplier(getLifeTimeMultiplier());
    }

    public float getEnergySettingCost(long currentEnergyLevel, long newEnergyLevel){
        return definition.getEnergyDiffBetweenStates(currentEnergyLevel,newEnergyLevel)*amount;
    }

    public float getEnergySettingCost(long newEnergyLevel){
        return definition.getEnergyDiffBetweenStates(energy,newEnergyLevel)*amount;
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
        if (this.color < 0 || this.color > 2 || color < 0 || color >= 3) {
            return this.color;
        }
        return this.color = color;
    }

    public byte nextColor() {//does not allow changing magic element
        if (color < 0 || color > 2) {
            return color;
        }
        return color = (byte) TecTech.RANDOM.nextInt(3);
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public float setLifeTimeMultiplier(float mult) {
        if(mult<=0) //since infinity*0=nan
        {
            throw new IllegalArgumentException("multiplier must be >0");
        }
        lifeTimeMult = mult;
        if (definition.getRawTimeSpan(energy) <= 0) {
            return lifeTime;
        }
        lifeTime = definition.getRawTimeSpan(energy) * lifeTimeMult;
        return lifeTime;
    }

    public float getLifeTimeMultiplier() {
        return lifeTimeMult;
    }

    public cElementalInstanceStackMap tickStackByOneSecond(float lifeTimeMult, int postEnergize){
        return tickStack(lifeTimeMult,postEnergize,1);
    }

    public cElementalInstanceStackMap tickStack(float lifeTimeMult, int postEnergize, int seconds){
        cElementalInstanceStackMap newInstances = decay(lifeTimeMult, age += seconds, postEnergize);
        if (newInstances == null) {
            nextColor();
        } else {
            for (cElementalInstanceStack newInstance : newInstances.values()) {
                newInstance.nextColor();
            }
        }
        return newInstances;
    }

    public cElementalInstanceStackMap decay() {
        return decay(1F, age, 0);//try to decay without changes
    }

    public cElementalInstanceStackMap decay(long apparentAge, long postEnergize) {
        return decay(1F,apparentAge,postEnergize);
    }

    public cElementalInstanceStackMap decay(float lifeTimeMult, long apparentAge, long postEnergize) {
        long newEnergyLevel = postEnergize + energy;
        if (newEnergyLevel > 0) {
            newEnergyLevel -= 1;
        } else if (newEnergyLevel < 0) {
            newEnergyLevel += 1;
        }
        if(definition.usesMultipleDecayCalls(energy)){
            long amountTemp=amount;
            long decayCnt=Math.min(Math.max(amount/DECAY_CALL_PER,MIN_MULTIPLE_DECAY_CALLS),MAX_MULTIPLE_DECAY_CALLS);
            long amountPer=amount/decayCnt;
            amount-=amountPer*(--decayCnt);
            cElementalInstanceStackMap output=decayMechanics(lifeTimeMult,apparentAge,newEnergyLevel);
            if(output==null){
                amount=amountTemp;
                return null;
            }
            if(amountPer>0){
                amount=amountPer;
                for(int i=0;i<decayCnt;i++){
                    cElementalInstanceStackMap map=decayMechanics(lifeTimeMult,apparentAge,newEnergyLevel);
                    if(map!=null){
                        output.putUnifyAll(map);
                    }
                }
            }
            amount=amountTemp;
            return output;
        }else{
            return decayMechanics(lifeTimeMult,apparentAge,newEnergyLevel);
        }
    }

    private cElementalInstanceStackMap decayMechanics(float lifeTimeMult, long apparentAge, long newEnergyLevel) {
        if (energy > 0 && !definition.usesSpecialEnergeticDecayHandling()) {
            setLifeTimeMultiplier(getLifeTimeMultiplier());
            return decayCompute(definition.getEnergyInducedDecay(energy), lifeTimeMult, -1, newEnergyLevel);
        } else if (definition.getRawTimeSpan(energy) < 0) {
            return null;//return null, decay cannot be achieved
        } else if (definition.isTimeSpanHalfLife()) {
            return exponentialDecayCompute(energy > 0 ? definition.getEnergyInducedDecay(energy) : definition.getDecayArray(), lifeTimeMult, -1, newEnergyLevel);
        } else {
            if (1F > lifeTime) {
                return decayCompute(energy > 0 ? definition.getEnergyInducedDecay(energy) : definition.getNaturalDecayInstant(), lifeTimeMult, 0, newEnergyLevel);
            } else if ((float) apparentAge > lifeTime) {
                return decayCompute(energy > 0 ? definition.getEnergyInducedDecay(energy) : definition.getDecayArray(), lifeTimeMult, 0, newEnergyLevel);
            }
        }
        return null;//return null since decay cannot be achieved
    }

    //Use to get direct decay output providing correct decay array
    private cElementalInstanceStackMap exponentialDecayCompute(cElementalDecay[] decays, float lifeTimeMult, long newProductsAge, long newEnergyLevel) {
        double decayInverseRatio=Math.pow(2d,1d/* 1 second *//(double)lifeTime);
        double newAmount=(double)amount/decayInverseRatio;
        long amountRemaining= (long)Math.floor(newAmount) +(TecTech.RANDOM.nextDouble()<=newAmount-Math.floor(newAmount)?1:0);
        //if(definition.getSymbol().startsWith("U ")) {
        //    System.out.println("newAmount = " + newAmount);
        //    System.out.println("amountRemaining = " + amountRemaining);
        //    for(cElementalDecay decay:decays){
        //        System.out.println("prob = "+decay.probability);
        //        for(cElementalDefinitionStack stack:decay.outputStacks.values()){
        //            System.out.println("stack = " + stack.getDefinition().getSymbol() + " " + stack.amount);
        //        }
        //    }
        //}
        if(amountRemaining==amount) {
            return null;//nothing decayed
        } else if(amountRemaining<=0) {
            return decayCompute(decays, lifeTimeMult, newProductsAge, newEnergyLevel);
        }
        //split to non decaying and decaying part
        long amount=this.amount;
        this.amount-=amountRemaining;
        cElementalInstanceStackMap products=decayCompute(decays,lifeTimeMult,newProductsAge,newEnergyLevel);
        this.amount=amountRemaining;
        products.putUnify(clone());
        this.amount=amount;
        return products;
    }

    //Use to get direct decay output providing correct decay array
    private cElementalInstanceStackMap decayCompute(cElementalDecay[] decays, float lifeTimeMult, long newProductsAge, long newEnergyLevel) {
        if (decays == null) {
            return null;//Can not decay so it wont
        }
        if (decays.length == 0) {
            if(definition.decayMakesEnergy(this.energy)) {
                return null;
            }
            return new cElementalInstanceStackMap();//provide non null 0 length array for annihilation
        } else if (decays.length == 1) {//only one type of decay :D, doesn't need dead end
            if(decays[0]==deadEnd && definition.decayMakesEnergy(this.energy)) {
                return null;//no extra photons
            }
            cElementalInstanceStackMap products=decays[0].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, amount);
            if(newProductsAge<0){
                if(products.size()==1) {
                    if(products.size()==1 && products.get(0).definition.equals(definition)) {
                        products.get(0).setEnergy(energy);
                        products.get(0).age=age;
                    }
                }else {
                    for (cElementalInstanceStack stack : products.values()) {
                        if (stack.definition.equals(definition)) {
                            stack.age = age;
                        }
                    }
                }
            }else{
                if(products.size()==1 && products.get(0).definition.equals(definition)) {
                    products.get(0).setEnergy(energy);
                }
            }
            if(this.energy <= 0 && products.getMass() > getMass()){
                return null;//no energy usage to decay
            }
            return products;
        } else {
            cElementalInstanceStackMap output = new cElementalInstanceStackMap();
            int differentDecays = decays.length;
            long[] qttyOfDecay = new long[differentDecays];
            long amountRemaining = this.amount, amount = this.amount;
            float remainingProbability = 1F;

            for (int i = 0; i < differentDecays; i++) {
                if (decays[i].probability >= 1F) {
                    long thisDecayAmount = (long) Math.floor(remainingProbability * (double) amount);
                    if (thisDecayAmount > 0) {
                        if (thisDecayAmount <= amountRemaining) {
                            amountRemaining -= thisDecayAmount;
                            qttyOfDecay[i] += thisDecayAmount;
                        }else {//in case too much was made
                            qttyOfDecay[i] += amountRemaining;
                            amountRemaining = 0;
                            //remainingProbability=0;
                        }
                    }
                    break;
                }
                long thisDecayAmount = (long) Math.floor(decays[i].probability * (double) amount);
                if (thisDecayAmount <= amountRemaining && thisDecayAmount > 0) {//some was made
                    amountRemaining -= thisDecayAmount;
                    qttyOfDecay[i] += thisDecayAmount;
                } else if (thisDecayAmount > amountRemaining) {//too much was made
                    qttyOfDecay[i] += amountRemaining;
                    amountRemaining = 0;
                    //remainingProbability=0;
                    break;
                }
                remainingProbability -= decays[i].probability;
                if(remainingProbability<=0) {
                    break;
                }
            }

            for (int i = 0; i < amountRemaining; i++) {
                double rand = TecTech.RANDOM.nextDouble();
                for (int j = 0; j < differentDecays; j++) {//looking for the thing it decayed into
                    rand -= decays[j].probability;
                    if (rand <= 0D) {
                        qttyOfDecay[j]++;
                        break;
                    }
                }
            }

            if(definition.decayMakesEnergy(this.energy)){
                for (int i = 0; i < differentDecays; i++) {
                    if (qttyOfDecay[i] > 0) {
                        if(decays[i]==deadEnd){
                            cElementalInstanceStack clone=this.clone();
                            clone.amount=qttyOfDecay[i];
                            output.putUnify(clone);
                        }else {
                            output.putUnifyAll(decays[i].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, qttyOfDecay[i]));
                        }
                    }
                }
            }else{
                for (int i = 0; i < differentDecays; i++) {
                    if (qttyOfDecay[i] > 0) {
                        output.putUnifyAll(decays[i].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, qttyOfDecay[i]));
                    }
                }
            }

            if(newProductsAge<0) {
                if (output.size() == 1 && output.get(0).definition.equals(definition)) {
                    output.get(0).setEnergy(energy);
                    output.get(0).age = age;
                } else {
                    for (cElementalInstanceStack stack : output.values()) {
                        if (stack.definition.equals(definition)) {
                            stack.age = age;
                        }
                    }
                }
            }else{
                if(output.size()==1 && output.get(0).definition.equals(definition)) {
                    output.get(0).setEnergy(energy);
                    output.get(0).age=age;
                }
            }
            if(this.energy <= 0 && output.getMass() > getMass()){
                return null;//no energy usage to decay
            }
            return output;
        }
    }

    public cElementalInstanceStack unifyIntoThis(cElementalInstanceStack... instances) {
        if (instances == null) {
            return this;
        }
        //returns with the definition from the first object passed
        double energy = this.energy * amount;
        long maxEnergy=this.energy;
        float lifeTimeMul = lifeTimeMult;

        for (cElementalInstanceStack instance : instances) {
            if (instance != null && compareTo(instance) == 0) {
                amount += instance.amount;
                energy += instance.energy * instance.amount;
                if(instance.energy>maxEnergy){
                    maxEnergy=instance.energy;
                }
                lifeTimeMul = Math.min(lifeTimeMul, instance.lifeTimeMult);
                age = Math.max(age, instance.age);
            }
        }

        if (amount != 0) {
            energy /= Math.abs(amount);
        }

        double wholeParts=Math.floor(energy);
        energy=Math.min(energy-wholeParts,1)+(wholeParts>=0?-0.11709966304863834:0.11709966304863834);
        setEnergy(Math.min(maxEnergy,(long)wholeParts+(energy>TecTech.RANDOM.nextDouble()?1:0)));
        return this;
    }

    public void addScanShortSymbols(ArrayList<String> lines, int[] detailsOnDepthLevels){
        int capabilities=detailsOnDepthLevels[0];
        definition.addScanShortSymbols(lines,capabilities,energy);
        //scanShortSymbolsContents(lines,definition.getSubParticles(),1,detailsOnDepthLevels);
    }

    //private void scanShortSymbolsContents(ArrayList<String> lines, cElementalDefinitionStackMap definitions, int depth, int[] detailsOnDepthLevels){
    //    if(definitions!=null && depth<detailsOnDepthLevels.length){
    //        int deeper=depth+1;
    //        for(cElementalDefinitionStack definitionStack:definitions.values()) {
    //            definition.addScanShortSymbols(lines,detailsOnDepthLevels[depth],energy);
    //            scanSymbolsContents(lines,definitionStack.definition.getSubParticles(),deeper,detailsOnDepthLevels);
    //        }
    //    }
    //}

    public void addScanResults(ArrayList<String> lines, int[] detailsOnDepthLevels){
        int capabilities=detailsOnDepthLevels[0];
        if(Util.areBitsSet(SCAN_GET_DEPTH_LEVEL,capabilities)) {
            lines.add("DEPTH = " + 0);
        }
        definition.addScanResults(lines,capabilities,energy);
        if(Util.areBitsSet(SCAN_GET_TIMESPAN_MULT,capabilities)) {
            lines.add("TIME MULT = " + lifeTimeMult);
            if(Util.areBitsSet(SCAN_GET_TIMESPAN_INFO,capabilities)) {
                lines.add("TIME SPAN = " + lifeTime + " s");
            }
        }
        if(Util.areBitsSet(SCAN_GET_AGE,capabilities)) {
            lines.add("AGE = " + age + " s");
        }
        if(Util.areBitsSet(SCAN_GET_COLOR,capabilities)) {
            lines.add("COLOR = " + color + " RGB or CMY");
        }
        if(Util.areBitsSet(SCAN_GET_ENERGY_LEVEL,capabilities)) {
            lines.add("ENERGY = " + energy);
        }
        if(Util.areBitsSet(SCAN_GET_AMOUNT,capabilities)) {
            lines.add("AMOUNT = " + amount);
        }
        scanContents(lines,definition.getSubParticles(),1,detailsOnDepthLevels);
    }

    private void scanContents(ArrayList<String> lines, cElementalDefinitionStackMap definitions, int depth, int[] detailsOnDepthLevels){
        if(definitions!=null && depth<detailsOnDepthLevels.length){
            int deeper=depth+1;
            for(cElementalDefinitionStack definitionStack:definitions.values()) {
                lines.add("");//def separator
                if(Util.areBitsSet(SCAN_GET_DEPTH_LEVEL,detailsOnDepthLevels[depth])) {
                    lines.add("DEPTH = " + depth);
                }
                definition.addScanResults(lines,detailsOnDepthLevels[depth],energy);
                if(Util.areBitsSet(SCAN_GET_AMOUNT,detailsOnDepthLevels[depth])) {
                    lines.add("AMOUNT = " + definitionStack.amount);
                }
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
        if (obj instanceof iElementalDefinition) {
            return definition.compareTo((iElementalDefinition) obj) == 0;
        }
        if (obj instanceof iHasElementalDefinition) {
            return definition.compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        }
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure, DOES NOT CARE ABOUT creativeTabTecTech INFO
    @Override
    public int hashCode() {
        return definition.hashCode();
    }

    @Override
    public String toString() {
        return definition.getName()+ '\n' + definition.getSymbol() + '\n' + amount + '\n' + getMass();
    }
}
