package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Instantiate parameters as field in parametersInstantiation_EM();
 */
public class Parameters {
    private static final Supplier<LedStatus> LED_STATUS_FUNCTION_DEFAULT = ()->LedStatus.STATUS_UNDEFINED;
    private static final int ZERO_FLOAT=Float.floatToIntBits(0);
    private final ParameterDefinition[] parameterDefinitions =new ParameterDefinition[10];

    final int[] iParamsIn = new int[20];//number I from parametrizers
    final int[] iParamsOut = new int[20];//number O to parametrizers
    final ArrayList<ParameterDefinition.In> inArrayList=new ArrayList<>();
    final ArrayList<ParameterDefinition.Out> outArrayList=new ArrayList<>();

    final boolean[] bParamsAreFloats =new boolean[10];

    //package private for use in gui
    final LedStatus[] eParamsInStatus = new LedStatus[20];//LED status for I
    final LedStatus[] eParamsOutStatus = new LedStatus[20];//LED status for O

    Parameters(){}

    void setToDefaults(boolean defaultIn, boolean defaultOut,boolean defaultAreFloats){
        for (int hatch=0;hatch<10;hatch++) {
            ParameterDefinition p= parameterDefinitions[hatch];
            if (p!=null){
                p.setToDefaults(defaultIn,defaultOut,defaultAreFloats);
            }else{
                if(defaultAreFloats){
                    bParamsAreFloats[hatch]=false;
                    if(defaultIn){
                        iParamsIn[hatch] = 0;
                        iParamsIn[hatch + 10] = 0;
                    }
                    if(defaultOut){
                        iParamsOut[hatch] = 0;
                        iParamsOut[hatch + 10] = 0;
                    }
                }else{
                    if(bParamsAreFloats[hatch]){
                        if(defaultIn){
                            iParamsIn[hatch] = ZERO_FLOAT;
                            iParamsIn[hatch + 10] = ZERO_FLOAT;
                        }
                        if(defaultOut){
                            iParamsOut[hatch] = ZERO_FLOAT;
                            iParamsOut[hatch + 10] = ZERO_FLOAT;
                        }
                    }else{
                        if(defaultIn){
                            iParamsIn[hatch] = 0;
                            iParamsIn[hatch + 10] = 0;
                        }
                        if(defaultOut){
                            iParamsOut[hatch] = 0;
                            iParamsOut[hatch + 10] = 0;
                        }
                    }
                }
            }
        }
    }

    public void ClearDefinitions(){
        setToDefaults(true,true,false);
        inArrayList.clear();
        outArrayList.clear();
        for(int i = 0; i< parameterDefinitions.length; i++){
            parameterDefinitions[i]=null;
        }
    }

    /**
     * most likely used locally in parametersInstantiation_EM()
     */
    public class ParameterDefinition {
        private final boolean bParamsDefaultsAreStoredAsFloats;
        private final int hatchNo;
        private final In[] in=new In[2];
        private final Out[] out=new Out[2];

        private ParameterDefinition(int hatchNo, boolean aParamsDefaultsAreFloats){
            if(hatchNo<0 || hatchNo>=10){
                throw new IllegalArgumentException("ParameterDefinition id must be in 0 to 9 range");
            }
            this.hatchNo=hatchNo;
            bParamsDefaultsAreStoredAsFloats =aParamsDefaultsAreFloats;
            parameterDefinitions[hatchNo]=this;
        }

        private void setToDefaults(boolean defaultIn, boolean defaultOut,boolean defaultAreFloats) {
            if(defaultAreFloats){
                bParamsAreFloats[hatchNo] = bParamsDefaultsAreStoredAsFloats;
            }
            if(defaultIn){
                for(int in=0;in<2;in++){
                    if(this.in[in]!=null){
                        this.in[in].setDefault();
                    }else {
                        if (bParamsAreFloats[hatchNo]) {
                            iParamsIn[hatchNo] = ZERO_FLOAT;
                            iParamsIn[hatchNo + 10] = ZERO_FLOAT;
                        } else {
                            iParamsIn[hatchNo] = 0;
                            iParamsIn[hatchNo + 10] = 0;
                        }
                    }
                }
            }
            if(defaultOut){
                for(int out=0;out<2;out++){
                    if(this.out[out]!=null){
                        this.out[out].setDefault();
                    }else {
                        if (bParamsAreFloats[hatchNo]) {
                            iParamsIn[hatchNo] = ZERO_FLOAT;
                            iParamsIn[hatchNo + 10] = ZERO_FLOAT;
                        } else {
                            iParamsIn[hatchNo] = 0;
                            iParamsIn[hatchNo + 10] = 0;
                        }
                    }
                }
            }
        }

        /**
         * Make a field out of this...
         */
        public class Out {
            public final int id;
            public final double defaultValue;
            private final Supplier<LedStatus> status;

            public Out(int paramID, double defaultValue, Supplier< LedStatus> status){
                this.id=hatchNo+10*paramID;
                if(paramID<0 || paramID>2){
                    throw new IllegalArgumentException("Parameter id must be in 0 to 1 range");
                }
                this.defaultValue=defaultValue;
                if(out[paramID]!=null){
                    throw new InstantiationError("This parameter already exists!");
                }
                if(status ==null){
                    this.status =LED_STATUS_FUNCTION_DEFAULT;
                }else{
                    this.status = status;
                }
                outArrayList.add(this);
                out[paramID]=this;
            }

            private void setDefault() {
                set(defaultValue);
            }

            public double get(){
                return bParamsAreFloats[hatchNo]?Float.intBitsToFloat(iParamsOut[id]):iParamsOut[id];
            }

            public void set(double value){
                if(bParamsAreFloats[hatchNo]) {
                    iParamsOut[id]=Float.floatToIntBits((float) value);
                }else{
                    iParamsOut[id]=(int)value;
                }
            }

            public LedStatus getStatus(){
                return eParamsOutStatus[id];
            }

            public void updateStatus(){
                eParamsOutStatus[id]=status.get();
            }

            public LedStatus getStaus(boolean update){
                if(update){
                    updateStatus();
                }
                return eParamsOutStatus[id];
            }
        }

        /**
         * Make a field out of this...
         */
        public class In {
            public final int id;
            public final double defaultValue;
            private final Supplier<LedStatus> status;

            public In(int paramID, double defaultValue,Supplier<LedStatus> status){
                this.id=hatchNo+10*paramID;
                if(paramID<0 || paramID>2){
                    throw new IllegalArgumentException("Parameter id must be in 0 to 1 range");
                }
                this.defaultValue=defaultValue;
                if(in[paramID]!=null){
                    throw new InstantiationError("This parameter already exists!");
                }
                if(status ==null){
                    this.status =LED_STATUS_FUNCTION_DEFAULT;
                }else{
                    this.status = status;
                }
                inArrayList.add(this);
                in[paramID]=this;
            }

            private void setDefault() {
                if(bParamsAreFloats[hatchNo]) {
                    iParamsIn[id]=Float.floatToIntBits((float) defaultValue);
                }else{
                    iParamsIn[id]=(int)defaultValue;
                }
            }

            public double get(){
                return bParamsAreFloats[hatchNo]?Float.intBitsToFloat(iParamsIn[id]):iParamsIn[id];
            }

            public void updateStatus(){
                eParamsInStatus[id]=status.get();
            }

            public LedStatus getStaus(boolean update){
                if(update){
                    updateStatus();
                }
                return eParamsInStatus[id];
            }
        }
    }
}

