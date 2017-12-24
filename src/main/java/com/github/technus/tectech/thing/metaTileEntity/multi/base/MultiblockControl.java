package com.github.technus.tectech.thing.metaTileEntity.multi.base;

/**
 * Created by danie_000 on 23.12.2017.
 */

public class MultiblockControl<T>{
    private final int[] controls=new int[7];
    private final T[] values;

    public MultiblockControl(T[] values, int EUt, int amperes, int requiredData, int effIncrease, int maxProgressTime){
        this.values = values;
        controls[0]=EUt;
        controls[1]=amperes;
        controls[2]=requiredData;
        controls[3]=effIncrease;
        controls[4]=maxProgressTime;
        controls[5]=0;
        controls[6]=Float.floatToIntBits(0);
    }

    public MultiblockControl(T[] values, int EUt, int amperes, int requiredData, int effIncrease, int maxProgressTime, int pollutionToAdd, float excessMass){
        this.values = values;
        controls[0]=EUt;
        controls[1]=amperes;
        controls[2]=requiredData;
        controls[3]=effIncrease;
        controls[4]=maxProgressTime;
        controls[5]=pollutionToAdd;
        controls[6]=Float.floatToIntBits(excessMass);
    }

    public T[] getValues() {
        return values;
    }

    public int getEUT(){
        return controls[0];
    }

    public int getAmperage(){
        return controls[1];
    }

    public int getRequiredData(){
        return controls[2];
    }

    public int getEffIncrease(){
        return controls[3];
    }

    public int getMaxProgressTime(){
        return controls[4];
    }

    public int getPollutionToAdd(){
        return controls[5];
    }

    public float getExcessMass(){
        return Float.intBitsToFloat(controls[6]);
    }
}
