package com.github.technus.tectech.thing.metaTileEntity.multi.base;

public enum LedStatus {
    STATUS_UNUSED,
    STATUS_TOO_LOW,
    STATUS_LOW,
    STATUS_WRONG,
    STATUS_OK,
    STATUS_TOO_HIGH,
    STATUS_HIGH,
    STATUS_UNDEFINED,
    STATUS_NEUTRAL;

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
        }catch (IndexOutOfBoundsException e){
            return STATUS_UNDEFINED;
        }
    }
}
