package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.MetaTileEntityIDs.HATCH_ADVANCED_BEAMLINE_OUTPUT;
import static gregtech.api.enums.MetaTileEntityIDs.HATCH_BEAMLINE_INPUT;
import static gregtech.api.enums.MetaTileEntityIDs.HATCH_BEAMLINE_OUTPUT;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.util.StatCollector;

import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.util.IGTHatchAdder;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public abstract class MTEBeamMultiBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    protected MTEBeamMultiBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected boolean hasMaintenanceChecks = false;

    public final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    public final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();
    public final ArrayList<MTEHatchAdvancedOutputBeamline> mAdvancedOutputBeamline = new ArrayList<>();

    public MTEBeamMultiBase(String aName) {
        super(aName);
    }

    public boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline inputBeamline) {
            this.addIfSmartInput(mte);
            return this.mInputBeamline.add(inputBeamline);
        }

        return false;
    }

    public boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline outputBeamline) {
            return this.mOutputBeamline.add(outputBeamline);
        }

        return false;
    }

    public enum FundamentalForce {
        EM,
        Weak,
        Strong,
        Gravity,
        All
    }

    public boolean addAdvancedBeamlineOutputHatch(IGregTechTileEntity te, int casingIndex, FundamentalForce forceType) {
        // 0 = EM; 1 = Weak; 2 = Strong; 3 = Gravity; 4 = All
        // might be nice to add combinations of output sets from various forces. currently not needed
        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            hatch.updateTexture(casingIndex);
            switch (forceType) {
                case EM:
                    hatch.setInitialParticleList(LHCModule.EM.acceptedParticles);
                case Weak:
                    hatch.setInitialParticleList(LHCModule.Weak.acceptedParticles);
                case Strong:
                    hatch.setInitialParticleList(LHCModule.Strong.acceptedParticles);
                case Gravity:
                    hatch.setInitialParticleList(LHCModule.Grav.acceptedParticles);
                case All:
                    hatch.setInitialParticleList(LHCModule.AllParticles.acceptedParticles);
            }
            return this.mAdvancedOutputBeamline.add(hatch);
        }
        return false;
    }

    public BeamInformation getNthInputParticle(int n) {
        if (this.mInputBeamline.isEmpty()) return new BeamInformation(0, 0, 0, 0);
        MTEHatchInputBeamline in = this.mInputBeamline.get(n);
        if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
        return in.dataPacket.getContent();
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // cannot have maintenance issues, so do nothing for those
        if (!this.hasMaintenanceChecks) return true;
        // those that can have maintenance issues are not so lucky
        return super.doRandomMaintenanceDamage();
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        this.mInputBeamline.clear();
        this.mOutputBeamline.clear();
        this.mAdvancedOutputBeamline.clear();
    }

    protected static <T extends MTEBeamMultiBase<T>> IStructureElement<T> buildBeamlineInputHatch(Class<T> clazz,
        int casingIndex, int hint) {
        return buildHatchAdder(clazz).hatchId(HATCH_BEAMLINE_INPUT.ID)
            .casingIndex(casingIndex)
            .hint(hint)
            .adder(MTEBeamMultiBase::addBeamLineInputHatch)
            .build();
    }

    protected static <T extends MTEBeamMultiBase<T>> IStructureElement<T> buildBeamlineOutputHatch(Class<T> clazz,
        int casingIndex, int hint) {
        return buildHatchAdder(clazz).hatchId(HATCH_BEAMLINE_OUTPUT.ID)
            .casingIndex(casingIndex)
            .hint(hint)
            .adder(MTEBeamMultiBase::addBeamLineOutputHatch)
            .exclusive()
            .build();
    }

    protected static <T extends MTEBeamMultiBase<T>> IStructureElement<T> buildAdvancedBeamlineOutputHatch(
        Class<T> clazz, int casingIndex, int hint, FundamentalForce force) {
        return buildHatchAdder(clazz).hatchId(HATCH_ADVANCED_BEAMLINE_OUTPUT.ID)
            .casingIndex(casingIndex)
            .hint(hint)
            .adder((multi, te, ci) -> multi.addAdvancedBeamlineOutputHatch(te, ci, force))
            .build();
    }

    public enum BeamHatchElement implements IHatchElement<MTEBeamMultiBase<?>> {

        BeamlineInput("GT5U.MBTT.BeamlineInputHatch", MTEBeamMultiBase::addBeamLineInputHatch,
            MTEHatchInputBeamline.class) {

            @Override
            public long count(MTEBeamMultiBase<?> t) {
                return t.mInputBeamline.size();
            }
        },
        BeamlineOutput("GT5U.MBTT.BeamlineOutputHatch", MTEBeamMultiBase::addBeamLineOutputHatch,
            MTEHatchOutputBeamline.class) {

            @Override
            public long count(MTEBeamMultiBase<?> t) {
                return t.mOutputBeamline.size();
            }
        };

        private final String name;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEBeamMultiBase<?>> adder;

        @SafeVarargs
        BeamHatchElement(String name, IGTHatchAdder<MTEBeamMultiBase<?>> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.name = name;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public String getDisplayName() {
            return StatCollector.translateToLocal(name);
        }

        @Override
        public IGTHatchAdder<? super MTEBeamMultiBase<?>> adder() {
            return adder;
        }

    }

}
