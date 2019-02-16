package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Instantiate parameters as field in parametersInstantiation_EM();
 */
public class Parameters {
    private static final Supplier<LedStatus> LED_STATUS_FUNCTION_DEFAULT = ()->LedStatus.STATUS_UNDEFINED;
    private static final int ZERO_FLOAT=Float.floatToIntBits(0);

    final ParameterGroup[] parameterGroups =new ParameterGroup[10];

    final int[] iParamsIn = new int[20];//number I from parametrizers
    final int[] iParamsOut = new int[20];//number O to parametrizers
    final ArrayList<ParameterGroup.In> inArrayList=new ArrayList<>();
    final ArrayList<ParameterGroup.Out> outArrayList=new ArrayList<>();

    final boolean[] bParamsAreFloats =new boolean[10];

    //package private for use in gui
    final LedStatus[] eParamsInStatus = new LedStatus[20];//LED status for I
    final LedStatus[] eParamsOutStatus = new LedStatus[20];//LED status for O

    Parameters(){}

    public void setToDefaults(boolean defaultIn, boolean defaultOut,boolean defaultAreFloats){
        for (int hatch=0;hatch<10;hatch++) {
            ParameterGroup p= parameterGroups[hatch];
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
        for(int i = 0; i< parameterGroups.length; i++){
            parameterGroups[i]=null;
        }
    }

    public ParameterGroup makeGroup(int hatchNo, boolean aParamsDefaultsAreFloats){
        return new ParameterGroup( hatchNo,  aParamsDefaultsAreFloats);
    }

    /**
     * most likely used locally in parametersInstantiation_EM()
     */
    public class ParameterGroup {
        private final boolean bParamsDefaultsAreStoredAsFloats;
        private final int hatchNo;
        final In[] in=new In[2];
        final Out[] out=new Out[2];

        private ParameterGroup(int hatchNo, boolean aParamsDefaultsAreFloats){
            if(hatchNo<0 || hatchNo>=10){
                throw new IllegalArgumentException("ParameterGroup id must be in 0 to 9 range");
            }
            this.hatchNo=hatchNo;
            bParamsDefaultsAreStoredAsFloats =aParamsDefaultsAreFloats;
            parameterGroups[hatchNo]=this;
        }

        public In makeInParameter(int paramID, double defaultValue,Supplier<String> name, Supplier<LedStatus> status){
            return new In(paramID, defaultValue,name, status);
        }

        public Out makeOutParameter(int paramID, double defaultValue,Supplier<String> name, Supplier<LedStatus> status){
            return new Out(paramID, defaultValue, name, status);
        }

        public void setToDefaults(boolean defaultIn, boolean defaultOut,boolean defaultAreFloats) {
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
            public Supplier<String> name;

            private Out(int paramID, double defaultValue,Supplier<String> name, Supplier<LedStatus> status){
                this.name=name;
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
            public Supplier<String> name;

            private In(int paramID, double defaultValue,Supplier<String> name,Supplier<LedStatus> status){
                this.name=name;
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

