package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.commonValues.DEBUGMODE;

/**
 * Created by danie_000 on 22.10.2016.
 * EXTEND THIS TO ADD NEW PRIMITIVES, WATCH OUT FOR ID'S!!!  (-1 to 32 can be assumed as used)
 */
public abstract class cElementalPrimitive extends cElementalDefinition {
    private static final byte nbtType=(byte) 'p';

    public static final TreeMap<Integer, iElementalDefinition> bindsBO = new TreeMap<>();


    public final String name;
    public final String symbol;
    //float-mass in eV/c^2
    public final float mass;
    //int -electric charge in 1/3rds of electron charge for optimization
    public final byte charge;
    //-1 if is not colorable
    public final byte color;
    //-1/-2/-3 anti matter generations, +1/+2/+3 matter generations, 0 self anti
    public final byte type;
    private cElementalPrimitive anti;
    private cElementalDecay[] elementalDecays;
    private byte naturalDecayInstant;
    private byte energeticDecayInstant;
    private float rawLifeTime;

    public final int ID;

    //no _ at end - normal particle
    //   _ at end - anti particle
    //  __ at end - self is antiparticle

    protected cElementalPrimitive(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        this.name = name;
        this.symbol = symbol;
        this.type = (byte) type;
        this.mass = mass;
        this.charge = (byte) charge;
        this.color = (byte) color;
        this.ID = ID;
        if(bindsBO.put(ID, this)!=null) Minecraft.getMinecraft().crashed(new CrashReport("Primitive definition",new tElementalException("Duplicate ID")));
    }

    //
    protected void init(cElementalPrimitive antiParticle, float rawLifeTime, int naturalInstant, int energeticInstant, cElementalDecay... elementalDecaysArray) {
        this.anti = antiParticle;
        this.rawLifeTime = rawLifeTime;
        this.naturalDecayInstant = (byte) naturalInstant;
        this.energeticDecayInstant = (byte) energeticInstant;
        this.elementalDecays = elementalDecaysArray;
    }

    @Override
    public String getName() {
        return "Undefined: " + name;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public iElementalDefinition getAnti() {
        return anti;//no need for copy
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public byte getColor() {
        return color;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        if (naturalDecayInstant < 0) return elementalDecays;
        return new cElementalDecay[]{elementalDecays[naturalDecayInstant]};
    }

    @Override
    public cElementalDecay[] getEnergeticDecayInstant() {
        if (energeticDecayInstant < 0) return elementalDecays;
        return new cElementalDecay[]{elementalDecays[energeticDecayInstant]};
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        return elementalDecays;
    }

    @Override
    public float getRawLifeTime() {
        return rawLifeTime;
    }

    @Override
    public final cElementalDefinitionStackTree getSubParticles() {
        return null;
    }

    @Override
    public FluidStack materializesIntoFluid() {
        return null;
    }

    @Override
    public ItemStack materializesIntoItem() {
        return null;
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public final NBTTagCompound toNBT() {
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setByte("t",nbtType);
        nbt.setInteger("c",ID);
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound content) {
        return bindsBO.get(content.getInteger("c"));
    }

    @Override
    public final byte getClassType() {
        return -128;
    }

    @Override
    public final int compareTo(iElementalDefinition o) {
        if(getClassType()==o.getClassType()){
            int oID=((cElementalPrimitive)o).ID;
            if (ID > oID) return 1;
            if (ID < oID) return -1;
            return 0;
        }
        return compareClasses(o);
    }

    public static void run() {
        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, cElementalPrimitive.class.getMethod("fromNBT", NBTTagCompound.class));
        } catch (Exception e) {
            if(DEBUGMODE)e.printStackTrace();
        }
    }
}