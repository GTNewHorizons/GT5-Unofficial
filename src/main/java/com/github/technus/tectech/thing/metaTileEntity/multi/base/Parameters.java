package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import java.util.ArrayList;

/**
 * Instantiate parameters as field in parametersInstantiation_EM();
 */
public class Parameters {

    private static final IStatusFunction<?> LED_STATUS_FUNCTION_DEFAULT = (b, p) -> LedStatus.STATUS_UNDEFINED;
    private static final INameFunction<?> NAME_FUNCTION_DEFAULT = (b, p) -> "Undefined";

    final Group[] groups = new Group[10];

    double[] iParamsIn = new double[20]; // number I from parametrizers
    double[] iParamsOut = new double[20]; // number O to parametrizers
    final ArrayList<Group.ParameterIn> parameterInArrayList = new ArrayList<>();
    final ArrayList<Group.ParameterOut> parameterOutArrayList = new ArrayList<>();

    // package private for use in gui
    LedStatus[] eParamsInStatus = LedStatus.makeArray(20, LedStatus.STATUS_UNUSED); // LED status for I
    LedStatus[] eParamsOutStatus = LedStatus.makeArray(20, LedStatus.STATUS_UNUSED); // LED status for O

    double getIn(int hatchNo, int parameterId) {
        return iParamsIn[hatchNo + 10 * parameterId];
    }

    double getOut(int hatchNo, int parameterId) {
        return iParamsOut[hatchNo + 10 * parameterId];
    }

    LedStatus getStatusIn(int hatchNo, int parameterId) {
        return eParamsInStatus[hatchNo + 10 * parameterId];
    }

    LedStatus getStatusOut(int hatchNo, int parameterId) {
        return eParamsOutStatus[hatchNo + 10 * parameterId];
    }

    private final GT_MetaTileEntity_MultiblockBase_EM parent;

    Parameters(GT_MetaTileEntity_MultiblockBase_EM parent) {
        this.parent = parent;
    }

    public boolean trySetParameters(int hatch, double parameter0, double parameter1) {
        Group p = groups[hatch];
        if (parent.mMaxProgresstime <= 0 || (p != null && p.updateWhileRunning)) {
            iParamsIn[hatch] = parameter0;
            iParamsIn[hatch + 10] = parameter1;
            return true;
        }
        return false;
    }

    public boolean trySetParameters(int hatchNo, int parameterId, double parameter) {
        Group p = groups[hatchNo];
        if (parent.mMaxProgresstime <= 0 || (p != null && p.updateWhileRunning)) {
            iParamsIn[hatchNo + 10 * parameterId] = parameter;
            return true;
        }
        return false;
    }

    public void setToDefaults(int hatch, boolean defaultIn, boolean defaultOut) {
        Group p = groups[hatch];
        if (p == null) {
            if (defaultIn) {
                iParamsIn[hatch] = 0;
                iParamsIn[hatch + 10] = 0;
            }
            if (defaultOut) {
                iParamsOut[hatch] = 0;
                iParamsOut[hatch + 10] = 0;
            }
        } else {
            p.setToDefaults(defaultIn, defaultOut);
        }
    }

    public void setToDefaults(boolean defaultIn, boolean defaultOut) {
        for (int hatch = 0; hatch < 10; hatch++) {
            setToDefaults(hatch, defaultIn, defaultOut);
        }
    }

    public void ClearDefinitions() {
        parameterInArrayList.clear();
        parameterOutArrayList.clear();
        for (int i = 0; i < groups.length; i++) {
            groups[i] = null;
        }
    }

    public void removeGroup(Group group) {
        if (group == groups[group.hatchNo]) {
            removeGroup(group.hatchNo);
        } else {
            throw new IllegalArgumentException("Group does not exists in this parametrization!");
        }
    }

    public void removeGroup(int hatchNo) {
        Group hatch = groups[hatchNo];
        if (hatch != null) {
            for (Group.ParameterOut p : hatch.parameterOut) {
                parameterOutArrayList.remove(p);
            }
            for (Group.ParameterIn p : hatch.parameterIn) {
                parameterInArrayList.remove(p);
            }
            groups[hatchNo] = null;
        }
    }

    public Group getGroup(int hatchNo, boolean updateWhileRunning) {
        return groups[hatchNo] != null ? groups[hatchNo] : new Group(hatchNo, updateWhileRunning);
    }

    public Group getGroup(int hatchNo) {
        return groups[hatchNo] != null ? groups[hatchNo] : new Group(hatchNo, false);
    }

    public interface IParameter {

        double get();

        double getDefault();

        void updateStatus();

        LedStatus getStatus(boolean update);

        int id();

        int hatchId();

        int parameterId();

        String getBrief();
    }

    /**
     * most likely used locally in parametersInstantiation_EM()
     */
    public class Group {

        private final int hatchNo;
        final ParameterIn[] parameterIn = new ParameterIn[2];
        final ParameterOut[] parameterOut = new ParameterOut[2];
        public boolean updateWhileRunning;

        private Group(int hatchNo, boolean updateWhileRunning) {
            if (hatchNo < 0 || hatchNo >= 10) {
                throw new IllegalArgumentException("Hatch id must be in 0 to 9 range");
            }
            this.hatchNo = hatchNo;
            this.updateWhileRunning = updateWhileRunning;
            groups[hatchNo] = this;
        }

        public ParameterIn makeInParameter(int paramID, double defaultValue, INameFunction<?> name,
                IStatusFunction<?> status) {
            return new ParameterIn(paramID, defaultValue, name, status);
        }

        public ParameterOut makeOutParameter(int paramID, double defaultValue, INameFunction<?> name,
                IStatusFunction<?> status) {
            return new ParameterOut(paramID, defaultValue, name, status);
        }

        public void setToDefaults(boolean defaultIn, boolean defaultOut) {
            if (defaultIn) {
                if (this.parameterIn[0] != null) {
                    this.parameterIn[0].setDefault();
                } else {
                    iParamsIn[hatchNo] = 0;
                }
                if (this.parameterIn[1] != null) {
                    this.parameterIn[1].setDefault();
                } else {
                    iParamsIn[hatchNo + 10] = 0;
                }
            }
            if (defaultOut) {
                if (this.parameterOut[0] != null) {
                    this.parameterOut[0].setDefault();
                } else {
                    iParamsOut[hatchNo] = 0;
                }
                if (this.parameterOut[1] != null) {
                    this.parameterOut[1].setDefault();
                } else {
                    iParamsOut[hatchNo + 10] = 0;
                }
            }
        }

        /**
         * Make a field out of this...
         */
        public class ParameterOut implements IParameter {

            public final int id;
            public final double defaultValue;
            IStatusFunction status;
            INameFunction name;

            private ParameterOut(int paramID, double defaultValue, INameFunction<?> name, IStatusFunction<?> status) {
                this.name = name == null ? NAME_FUNCTION_DEFAULT : name;
                if (paramID < 0 || paramID > 2) {
                    throw new IllegalArgumentException("Parameter id must be in 0 to 1 range");
                }
                if (parameterOut[paramID] != null) {
                    throw new IllegalArgumentException("Parameter id already occupied");
                }
                this.id = hatchNo + 10 * paramID;
                this.defaultValue = defaultValue;
                this.status = status == null ? LED_STATUS_FUNCTION_DEFAULT : status;
                parameterOutArrayList.add(this);
                parameterOut[paramID] = this;
            }

            void setDefault() {
                set(defaultValue);
            }

            @Override
            public double get() {
                return iParamsOut[id];
            }

            @Override
            public double getDefault() {
                return defaultValue;
            }

            public void set(double value) {
                iParamsOut[id] = value;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void updateStatus() {
                eParamsOutStatus[id] = status.apply(parent, this);
            }

            @Override
            public LedStatus getStatus(boolean update) {
                if (update) {
                    updateStatus();
                }
                return eParamsOutStatus[id];
            }

            @Override
            public String getBrief() {
                return name.apply(parent, this);
            }

            @Override
            public int id() {
                return id;
            }

            @Override
            public int hatchId() {
                return id % 10;
            }

            @Override
            public int parameterId() {
                return id / 10;
            }
        }

        /**
         * Make a field out of this...
         */
        public class ParameterIn implements IParameter {

            public final int id;
            public final double defaultValue;
            IStatusFunction status;
            INameFunction name;

            private ParameterIn(int paramID, double defaultValue, INameFunction<?> name, IStatusFunction<?> status) {
                this.name = name == null ? NAME_FUNCTION_DEFAULT : name;
                this.id = hatchNo + 10 * paramID;
                if (paramID < 0 || paramID > 2) {
                    throw new IllegalArgumentException("Parameter id must be in 0 to 1 range");
                }
                if (parameterIn[paramID] != null) {
                    throw new IllegalArgumentException("Parameter id already occupied");
                }
                this.defaultValue = defaultValue;
                this.status = status == null ? LED_STATUS_FUNCTION_DEFAULT : status;
                parameterInArrayList.add(this);
                parameterIn[paramID] = this;
            }

            void setDefault() {
                set(defaultValue);
            }

            @Override
            public double get() {
                return iParamsIn[id];
            }

            void set(double value) {
                iParamsIn[id] = value;
            }

            @Override
            public double getDefault() {
                return defaultValue;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void updateStatus() {
                eParamsInStatus[id] = status.apply(parent, this);
            }

            @Override
            public LedStatus getStatus(boolean update) {
                if (update) {
                    updateStatus();
                }
                return eParamsInStatus[id];
            }

            @Override
            public String getBrief() {
                return name.apply(parent, this);
            }

            @Override
            public int id() {
                return id;
            }

            @Override
            public int hatchId() {
                return id % 10;
            }

            @Override
            public int parameterId() {
                return id / 10;
            }
        }
    }
}
