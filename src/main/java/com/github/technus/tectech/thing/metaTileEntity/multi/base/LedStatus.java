package com.github.technus.tectech.thing.metaTileEntity.multi.base;

public enum LedStatus {
    STATUS_UNUSED,
    STATUS_TOO_LOW,
    STATUS_LOW,
    STATUS_WRONG,
    STATUS_OK,
    STATUS_TOO_HIGH,
    STATUS_HIGH,
    STATUS_UNDEFINED;

    public boolean isOk(){
        return (ordinal()&1)==0;
    }

    public boolean isBad(){
        return (ordinal()&1)==1;
    }

    public byte getOrdinalByte(){
        return (byte)ordinal();
    }

    public static LedStatus getStatus(byte value){
        try{
            return LedStatus.values()[value];
        }catch (Exception e){
            return STATUS_UNDEFINED;
        }
    }

    public static LedStatus[] makeArray(int count,LedStatus defaultValue){
        LedStatus[] statuses=new LedStatus[count];
        for (int i = 0; i < count; i++) {
            statuses[i]=defaultValue;
        }
        return statuses;
    }

    public static LedStatus fromLimitsInclusiveOuterBoundary(double value, double min,double low, double high, double max, double... excludedNumbers
    ){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<min) return STATUS_TOO_LOW;
        if(value>max) return STATUS_TOO_HIGH;

        if(value<low) return STATUS_LOW;
        if(value>high) return STATUS_HIGH;
        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        return STATUS_UNDEFINED;
    }

    public static LedStatus fromLimitsExclusiveOuterBoundary(double value, double min,double low, double high,double max, double... excludedNumbers
    ){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=min) return STATUS_TOO_LOW;
        if(value>=max) return STATUS_TOO_HIGH;

        if(value<low) return STATUS_LOW;
        if(value>high) return STATUS_HIGH;
        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        return STATUS_OK;
    }

    public static LedStatus fromLimitsInclusiveOuterBoundary(double value, double min, double max, double... excludedNumbers
    ){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=min) return STATUS_TOO_LOW;
        else if(value==min)
            if(value>=max) return STATUS_TOO_HIGH;

        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        return STATUS_OK;
    }

    public static LedStatus fromLimitsExclusiveOuterBoundary(double value, double min, double max, double... excludedNumbers
    ){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=min) return STATUS_TOO_LOW;
        else if(value==min)
            if(value>=max) return STATUS_TOO_HIGH;

        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        return STATUS_OK;
    }
}
