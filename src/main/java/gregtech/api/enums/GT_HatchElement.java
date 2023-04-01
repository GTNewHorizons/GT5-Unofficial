package gregtech.api.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.IGT_HatchAdder;

public enum GT_HatchElement implements IHatchElement<GT_MetaTileEntity_MultiBlockBase> {

    Muffler(GT_MetaTileEntity_MultiBlockBase::addMufflerToMachineList, GT_MetaTileEntity_Hatch_Muffler.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mMufflerHatches.size();
        }
    },
    Maintenance(GT_MetaTileEntity_MultiBlockBase::addMaintenanceToMachineList,
            GT_MetaTileEntity_Hatch_Maintenance.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mMaintenanceHatches.size();
        }
    },
    InputHatch(GT_MetaTileEntity_MultiBlockBase::addInputHatchToMachineList, GT_MetaTileEntity_Hatch_Input.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mInputHatches.size();
        }
    },
    InputBus(GT_MetaTileEntity_MultiBlockBase::addInputBusToMachineList, GT_MetaTileEntity_Hatch_InputBus.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mInputBusses.size();
        }
    },
    OutputHatch(GT_MetaTileEntity_MultiBlockBase::addOutputHatchToMachineList, GT_MetaTileEntity_Hatch_Output.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mOutputHatches.size();
        }
    },
    OutputBus(GT_MetaTileEntity_MultiBlockBase::addOutputBusToMachineList, GT_MetaTileEntity_Hatch_OutputBus.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mOutputBusses.size();
        }
    },
    Energy(GT_MetaTileEntity_MultiBlockBase::addEnergyInputToMachineList, GT_MetaTileEntity_Hatch_Energy.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mEnergyHatches.size();
        }
    },
    Dynamo(GT_MetaTileEntity_MultiBlockBase::addDynamoToMachineList, GT_MetaTileEntity_Hatch_Dynamo.class) {

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.mDynamoHatches.size();
        }
    },
    ExoticEnergy(GT_MetaTileEntity_MultiBlockBase::addExoticEnergyInputToMachineList) {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return GT_ExoticEnergyInputHelper.getAllClasses();
        }

        @Override
        public long count(GT_MetaTileEntity_MultiBlockBase t) {
            return t.getExoticEnergyHatches()
                    .size();
        }
    },;

    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGT_HatchAdder<GT_MetaTileEntity_MultiBlockBase> adder;

    @SafeVarargs
    GT_HatchElement(IGT_HatchAdder<GT_MetaTileEntity_MultiBlockBase> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
        this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        this.adder = adder;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mteClasses;
    }

    public IGT_HatchAdder<? super GT_MetaTileEntity_MultiBlockBase> adder() {
        return adder;
    }
}
