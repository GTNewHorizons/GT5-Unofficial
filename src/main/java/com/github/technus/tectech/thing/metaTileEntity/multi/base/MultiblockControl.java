package com.github.technus.tectech.thing.metaTileEntity.multi.base;

/**
 * Created by danie_000 on 23.12.2017.
 */

public class MultiblockControl<T>{
    private final int[] controls;
    private final T[] values;

    public MultiblockControl(T[] values, int EUt, int amperes, int requiredData, int effIncrease, int maxProgressTime){
        this.values = values;
        controls=new int[5];
        controls[0]=EUt;
        controls[1]=amperes;
        controls[2]=requiredData;
        controls[3]=effIncrease;
        controls[4]=maxProgressTime;
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
}
