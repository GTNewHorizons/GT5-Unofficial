package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Instantiate parameters as field in parametersInstantiation_EM();
 */
public class Parameters {
    public static final Function<GT_MetaTileEntity_MultiblockBase_EM,LedStatus> LED_STATUS_FUNCTION_DEFAULT = o->LedStatus.STATUS_UNDEFINED;
    public static final Function<GT_MetaTileEntity_MultiblockBase_EM,String> NAME_FUNCTION_DEFAULT=o->"Undefined";
    public static final int ZERO_FLOAT=Float.floatToIntBits(0);

    final Group[] groups = new Group[10];

    final int[] iParamsIn = new int[20];//number I from parametrizers
    final int[] iParamsOut = new int[20];//number O to parametrizers
    final ArrayList<Group.ParameterIn> parameterInArrayList =new ArrayList<>();
    final ArrayList<Group.ParameterOut> parameterOutArrayList =new ArrayList<>();

    final boolean[] bParamsAreFloats =new boolean[10];

    //package private for use in gui
    final LedStatus[] eParamsInStatus = LedStatus.makeArray(20,LedStatus.STATUS_UNUSED);//LED status for I
    final LedStatus[] eParamsOutStatus = LedStatus.makeArray(20,LedStatus.STATUS_UNUSED);//LED status for O

    private final GT_MetaTileEntity_MultiblockBase_EM parent;

    Parameters(GT_MetaTileEntity_MultiblockBase_EM parent){
        this.parent=parent;
    }

    public void setToDefaults(boolean defaultIn, boolean defaultOut,boolean defaultAreFloats){
        for (int hatch=0;hatch<10;hatch++) {
            Group p= groups[hatch];
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
        parameterInArrayList.clear();
        parameterOutArrayList.clear();
        for(int i = 0; i< groups.length; i++){
            groups[i]=null;
        }
    }

    public Group makeGroup(int hatchNo, boolean aParamsDefaultsAreFloats){
        return new Group( hatchNo,  aParamsDefaultsAreFloats);
    }

    /**
     * most likely used locally in parametersInstantiation_EM()
     */
    public class Group {
        private final boolean bParamsDefaultsAreStoredAsFloats;
        private final int hatchNo;
        final ParameterIn[] parameterIn =new ParameterIn[2];
        final ParameterOut[] parameterOut =new ParameterOut[2];

        private Group(int hatchNo, boolean aParamsDefaultsAreFloats){
            if(hatchNo<0 || hatchNo>=10){
                throw new IllegalArgumentException("ParameterGroup id must be in 0 to 9 range");
            }
            this.hatchNo=hatchNo;
            bParamsDefaultsAreStoredAsFloats =aParamsDefaultsAreFloats;
            groups[hatchNo]=this;
        }

        public ParameterIn makeInParameter(int paramID, double defaultValue, Function<? extends GT_MetaTileEntity_MultiblockBase_EM,String> name, Function<? extends GT_MetaTileEntity_MultiblockBase_EM,LedStatus> status){
            return new ParameterIn(paramID, defaultValue,name, status);
        }

        public ParameterOut makeOutParameter(int paramID, double defaultValue, Function<? extends GT_MetaTileEntity_MultiblockBase_EM,String> name, Function<? extends GT_MetaTileEntity_MultiblockBase_EM,LedStatus> status){
            return new ParameterOut(paramID, defaultValue, name, status);
        }

        public void setToDefaults(boolean defaultIn, boolean defaultOut,boolean defaultAreFloats) {
            if(defaultAreFloats){
                bParamsAreFloats[hatchNo] = bParamsDefaultsAreStoredAsFloats;
            }
            if(defaultIn){
                for(int in=0;in<2;in++){
                    if(this.parameterIn[in]!=null){
                        this.parameterIn[in].setDefault();
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
                    if(this.parameterOut[out]!=null){
                        this.parameterOut[out].setDefault();
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
        public class ParameterOut {
            public final int id;
            public final double defaultValue;
            Function<GT_MetaTileEntity_MultiblockBase_EM,LedStatus> status;
            Function<GT_MetaTileEntity_MultiblockBase_EM,String> name;

            @SuppressWarnings("unchecked")
            private ParameterOut(int paramID, double defaultValue, Function name, Function status){
                this.name= name;
                this.id=hatchNo+10*paramID;
                if(paramID<0 || paramID>2){
                    throw new IllegalArgumentException("Parameter id must be in 0 to 1 range");
                }
                this.defaultValue=defaultValue;
                if(parameterOut[paramID]!=null){
                    throw new InstantiationError("This parameter already exists!");
                }
                this.status = status;
                parameterOutArrayList.add(this);
                parameterOut[paramID]=this;
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

            public void updateStatus(){
                eParamsOutStatus[id]=status.apply(parent);
            }

            public LedStatus getStatus(boolean update){
                if(update){
                    updateStatus();
                }
                return eParamsOutStatus[id];
            }

            public String getBrief(){
                return name.apply(parent);
            }
        }

        /**
         * Make a field out of this...
         */
        public class ParameterIn {
            public final int id;
            public final double defaultValue;
            Function<GT_MetaTileEntity_MultiblockBase_EM,LedStatus> status;
            Function<GT_MetaTileEntity_MultiblockBase_EM,String> name;

            @SuppressWarnings("unchecked")
            private ParameterIn(int paramID, double defaultValue, Function name, Function status){
                this.name= name;
                this.id=hatchNo+10*paramID;
                if(paramID<0 || paramID>2){
                    throw new IllegalArgumentException("Parameter id must be in 0 to 1 range");
                }
                this.defaultValue=defaultValue;
                if(parameterIn[paramID]!=null){
                    throw new InstantiationError("This parameter already exists!");
                }
                this.status = status;
                parameterInArrayList.add(this);
                parameterIn[paramID]=this;
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
                eParamsInStatus[id]=status.apply(parent);
            }

            public LedStatus getStatus(boolean update){
                if(update){
                    updateStatus();
                }
                return eParamsInStatus[id];
            }

            public String getBrief(){
                return name.apply(parent);
            }
        }
    }
}

