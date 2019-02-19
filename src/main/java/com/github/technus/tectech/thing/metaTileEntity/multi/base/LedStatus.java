package com.github.technus.tectech.thing.metaTileEntity.multi.base;

public enum LedStatus {
    STATUS_UNUSED("Unused",true),
    STATUS_TOO_LOW("Too Low",false),
    STATUS_LOW("Low",true),
    STATUS_WRONG("Wrong",false),
    STATUS_OK("Valid",true),
    STATUS_TOO_HIGH("Too High",false),
    STATUS_HIGH("High",true),
    STATUS_UNDEFINED("Unknown",false),
    STATUS_NEUTRAL("Neutral",true);

    public final String name;
    public final boolean isOk;

    LedStatus(String name,boolean ok){
        this.name=name;
        this.isOk=ok;
    }

    public boolean isOk(){
        return isOk;
    }

    public boolean isBad(){
        return !isOk;
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

    public static LedStatus fromLimitsInclusiveOuterBoundary(double value, double min,double low, double high, double max, double... excludedNumbers){
        if(value<min) return STATUS_TOO_LOW;
        if(value>max) return STATUS_TOO_HIGH;

        if(value<low) return STATUS_LOW;
        if(value>high) return STATUS_HIGH;
        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        if(Double.isNaN(value)) return STATUS_WRONG;
        return STATUS_UNDEFINED;
    }

    public static LedStatus fromLimitsExclusiveOuterBoundary(double value, double min,double low, double high,double max, double... excludedNumbers){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=min) return STATUS_TOO_LOW;
        if(value>=max) return STATUS_TOO_HIGH;

        if(value<low) return STATUS_LOW;
        if(value>high) return STATUS_HIGH;
        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        if(Double.isNaN(value)) return STATUS_WRONG;
        return STATUS_OK;
    }

    public static LedStatus fromLimitsInclusiveBoundary(double value, double min, double max, double... excludedNumbers){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=min) return STATUS_TOO_LOW;
        else if(value==min)
            if(value>=max) return STATUS_TOO_HIGH;

        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        if(Double.isNaN(value)) return STATUS_WRONG;
        return STATUS_OK;
    }

    public static LedStatus fromLimitsExclusiveBoundary(double value, double min, double max, double... excludedNumbers){
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=min) return STATUS_TOO_LOW;
        else if(value==min)
            if(value>=max) return STATUS_TOO_HIGH;

        for (double val : excludedNumbers) {
            if(val==value) return STATUS_WRONG;
        }
        if(Double.isNaN(value)) return STATUS_WRONG;
        return STATUS_OK;
    }
}
