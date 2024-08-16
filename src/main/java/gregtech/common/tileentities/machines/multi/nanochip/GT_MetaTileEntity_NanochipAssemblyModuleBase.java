package gregtech.common.tileentities.machines.multi.nanochip;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.common.tileentities.machines.multi.nanochip.GT_MetaTileEntity_NanochipAssemblyComplex.CASING_INDEX_BASE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor_Input;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor_Output;
import gregtech.common.tileentities.machines.multi.nanochip.util.VacuumConveyorHatchMap;

public abstract class GT_MetaTileEntity_NanochipAssemblyModuleBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> implements ISurvivalConstructable {

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String[][] base_structure = new String[][] { { "V~V" }, { "VVV" }, { "VVV" } };

    protected static final int BASE_STRUCTURE_OFFSET_X = 1;
    protected static final int BASE_STRUCTURE_OFFSET_Y = 0;
    protected static final int BASE_STRUCTURE_OFFSET_Z = 0;

    private boolean isConnected = false;

    protected final VacuumConveyorHatchMap<GT_MetaTileEntity_Hatch_VacuumConveyor_Input> vacuumConveyorInputs = new VacuumConveyorHatchMap<>();
    protected final VacuumConveyorHatchMap<GT_MetaTileEntity_Hatch_VacuumConveyor_Output> vacuumConveyorOutputs = new VacuumConveyorHatchMap<>();

    public static <B extends GT_MetaTileEntity_NanochipAssemblyModuleBase<B>> StructureDefinition.Builder<B> addBaseStructure(
        StructureDefinition.Builder<B> structure) {
        return structure.addShape(STRUCTURE_PIECE_BASE, base_structure)
            .addElement(
                'V',
                GT_HatchElementBuilder.<B>builder()
                    .atLeast(ModuleHatchElement.VacuumConveyorHatch)
                    .casingIndex(CASING_INDEX_BASE)
                    .dot(2)
                    .buildAndChain(ofBlock(GregTech_API.sBlockCasings4, 0)));
    }

    public enum ModuleHatchElement implements IHatchElement<GT_MetaTileEntity_NanochipAssemblyModuleBase<?>> {

        VacuumConveyorHatch(GT_MetaTileEntity_NanochipAssemblyModuleBase::addConveyorToMachineList,
            GT_MetaTileEntity_NanochipAssemblyModuleBase.class) {

            @Override
            public long count(GT_MetaTileEntity_NanochipAssemblyModuleBase<?> tileEntity) {
                return tileEntity.vacuumConveyorInputs.size() + tileEntity.vacuumConveyorOutputs.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_NanochipAssemblyModuleBase<?>> adder;

        @SafeVarargs
        ModuleHatchElement(IGT_HatchAdder<GT_MetaTileEntity_NanochipAssemblyModuleBase<?>> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_NanochipAssemblyModuleBase<?>> adder() {
            return adder;
        }
    }

    /**
     * Create new nanochip assembly module
     *
     * @param aID           ID of this module
     * @param aName         Name of this module
     * @param aNameRegional Localized name of this module
     */
    protected GT_MetaTileEntity_NanochipAssemblyModuleBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_NanochipAssemblyModuleBase(String aName) {
        super(aName);
    }

    // Only checks the base structure piece
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.vacuumConveyorInputs.clear();
        this.vacuumConveyorOutputs.clear();
        fixAllIssues();
        return checkPiece(
            STRUCTURE_PIECE_BASE,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z);
    }

    public boolean addConveyorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_VacuumConveyor_Input hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            return vacuumConveyorInputs.addHatch(hatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_VacuumConveyor_Output hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            return vacuumConveyorOutputs.addHatch(hatch);
        }
        return false;
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // Does not get have maintenance issues
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }
}
