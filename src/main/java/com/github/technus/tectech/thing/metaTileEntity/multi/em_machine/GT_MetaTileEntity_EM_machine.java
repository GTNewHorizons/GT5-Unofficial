package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_NEUTRAL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.HashMap;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_machine extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region variables
    public static final String machine = "EM Machinery";

    private ItemStack loadedMachine;
    private IBehaviour currentBehaviour;
    // endregion

    // region structure
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.processing.hint.0"), // 1 - Classic Hatches or High Power
                                                                                    // Casing
            translateToLocal("gt.blockmachines.multimachine.em.processing.hint.1"), // 2 - Elemental Hatches or
                                                                                    // Molecular Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_machine> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_machine>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] { { "  A  ", " AAA ", " EBE ", " ECE ", " EBE ", " AAA ", "  A  " },
                                    { " DDD ", "AAAAA", "E---E", "E---E", "E---E", "AAAAA", " FFF " },
                                    { "AD-DA", "AA~AA", "B---B", "C- -C", "B---B", "AA-AA", "AFFFA" },
                                    { " DDD ", "AAAAA", "E---E", "E---E", "E---E", "AAAAA", " FFF " },
                                    { "  A  ", " AAA ", " EBE ", " ECE ", " EBE ", " AAA ", "  A  " } }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4)).addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement('C', ofBlock(sBlockCasingsTT, 6)).addElement('E', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement(
                    'D',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_machine::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement(
                    'F',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_machine::addElementalToMachineList,
                            textureOffset + 4,
                            2,
                            sBlockCasingsTT,
                            4))
            .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn[] inputMux;
    protected Parameters.Group.ParameterIn[] outputMux;
    private static final IStatusFunction<GT_MetaTileEntity_EM_machine> SRC_STATUS = (base, p) -> {
        double v = p.get();
        if (Double.isNaN(v)) return STATUS_WRONG;
        v = (int) v;
        if (v < 0) return STATUS_TOO_LOW;
        if (v == 0) return STATUS_NEUTRAL;
        if (v >= base.eInputHatches.size()) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_EM_machine> DST_STATUS = (base, p) -> {
        if (base.inputMux[p.hatchId()].getStatus(false) == STATUS_OK) {
            double v = p.get();
            if (Double.isNaN(v)) return STATUS_WRONG;
            v = (int) v;
            if (v < 0) return STATUS_TOO_LOW;
            if (v == 0) return STATUS_LOW;
            if (v >= base.eInputHatches.size()) return STATUS_TOO_HIGH;
            return STATUS_OK;
        }
        return STATUS_NEUTRAL;
    };
    private static final INameFunction<GT_MetaTileEntity_EM_machine> ROUTE_NAME = (base,
            p) -> (p.parameterId() == 0 ? translateToLocal("tt.keyword.Source") + " "
                    : translateToLocal("tt.keyword.Destination") + " ") + p.hatchId();
    // endregion

    public GT_MetaTileEntity_EM_machine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_machine(String aName) {
        super(aName);
    }

    private boolean setCurrentBehaviour() {
        ItemStack newMachine = mInventory[1];
        if (ItemStack.areItemStacksEqual(newMachine, loadedMachine)) {
            return false;
        }
        loadedMachine = newMachine;
        Supplier<IBehaviour> behaviourSupplier = GT_MetaTileEntity_EM_machine.BEHAVIOUR_MAP
                .get(new TT_Utility.ItemStack_NoNBT(newMachine));
        if (currentBehaviour == null && behaviourSupplier == null) {
            return false;
        }
        if (currentBehaviour != null) {
            for (int i = 6; i < 10; i++) {
                parametrization.removeGroup(i);
            }
        }
        if (behaviourSupplier != null) {
            currentBehaviour = behaviourSupplier.get();
            currentBehaviour.parametersInstantiation(this, parametrization);
            for (int i = 6; i < 10; i++) {
                parametrization.setToDefaults(i, true, true);
            }
        } else {
            currentBehaviour = null;
        }

        return true;
    }

    private static final HashMap<TT_Utility.ItemStack_NoNBT, Supplier<IBehaviour>> BEHAVIOUR_MAP = new HashMap<>();

    public static void registerBehaviour(Supplier<IBehaviour> behaviour, ItemStack is) {
        BEHAVIOUR_MAP.put(new TT_Utility.ItemStack_NoNBT(is), behaviour);
        TecTech.LOGGER.info(
                "Registered EM machine behaviour " + behaviour.get().getClass().getSimpleName()
                        + ' '
                        + new TT_Utility.ItemStack_NoNBT(is).toString());
    }

    public interface IBehaviour {

        /**
         * instantiate parameters, u can also check machine tier here
         *
         * @param te
         * @param parameters
         */
        void parametersInstantiation(GT_MetaTileEntity_EM_machine te, Parameters parameters);

        /**
         * handle parameters per recipe
         *
         * @param te         this te instance
         * @param parameters of this te
         * @return return true if machine can start with current parameters, false if not
         */
        boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters);

        /**
         * do recipe handling
         *
         * @param inputs     from muxed inputs
         * @param parameters array passed from previous method!
         * @return null if recipe should not start, control object to set machine state and start recipe
         */
        MultiblockControl<EMInstanceStackMap[]> process(EMInstanceStackMap[] inputs, GT_MetaTileEntity_EM_machine te,
                Parameters parameters);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_machine(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 2, 2, 1);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (aNBT.hasKey("eLoadedMachine")) {
            loadedMachine = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("eLoadedMachine"));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (loadedMachine != null) {
            aNBT.setTag("eLoadedMachine", loadedMachine.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    protected void parametersInstantiation_EM() {
        inputMux = new Parameters.Group.ParameterIn[6];
        outputMux = new Parameters.Group.ParameterIn[6];
        for (int i = 0; i < 6; i++) {
            Parameters.Group hatch = parametrization.getGroup(i);
            inputMux[i] = hatch.makeInParameter(0, i, ROUTE_NAME, SRC_STATUS);
            outputMux[i] = hatch.makeInParameter(1, i, ROUTE_NAME, DST_STATUS);
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.processing.name")) // Machine Type: Quantum
                                                                                                // Processing machine
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.processing.desc.0")) // Controller block of
                                                                                                 // the
                // Quantum Processing machine
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(5, 5, 7, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.Elemental"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing2D"),
                        2) // Elemental Hatch: Any Molecular Casing with 2 dots
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Parametrizer"),
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"),
                        1) // Parametrizer: Any High Power Casing
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                                // High Power Casing
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                     // Hatch: Any High
                                                                                                     // Power Casing
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        setCurrentBehaviour();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        setCurrentBehaviour();
        if (currentBehaviour == null) {
            return false;
        }

        if (!currentBehaviour.checkParametersInAndSetStatuses(this, parametrization)) {
            return false;
        }

        EMInstanceStackMap[] handles = new EMInstanceStackMap[6];
        for (int i = 0; i < 6; i++) {
            int pointer = (int) inputMux[i].get();
            if (pointer >= 0 && pointer < eInputHatches.size()) {
                handles[i] = eInputHatches.get(pointer).getContentHandler();
            }
        }

        for (int i = 1; i < 6; i++) {
            if (handles[i] != null) {
                for (int j = 0; j < i; j++) {
                    if (handles[i] == handles[j]) {
                        return false;
                    }
                }
            }
        }

        MultiblockControl<EMInstanceStackMap[]> control = currentBehaviour.process(handles, this, parametrization);
        if (control == null) {
            return false;
        }
        cleanMassEM_EM(control.getExcessMass());
        if (control.shouldExplode()) {
            explodeMultiblock();
            return false;
        }
        // update other parameters
        outputEM = control.getValue();
        mEUt = control.getEUT();
        eAmpereFlow = control.getAmperage();
        mMaxProgresstime = control.getMaxProgressTime();
        eRequiredData = control.getRequiredData();
        mEfficiencyIncrease = control.getEffIncrease();
        boolean polluted = polluteEnvironment(control.getPollutionToAdd());
        return polluted;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (setCurrentBehaviour()) {
            return;
        }

        EMInstanceStackMap[] handles = new EMInstanceStackMap[6];
        for (int i = 0; i < 6; i++) {
            int pointer = (int) outputMux[i].get();
            if (pointer >= 0 && pointer < eOutputHatches.size()) {
                handles[i] = eOutputHatches.get(pointer).getContentHandler();
            }
        }
        // output
        for (int i = 0; i < 6 && i < outputEM.length; i++) {
            if (handles[i] != null && outputEM[i] != null && outputEM[i].hasStacks()) {
                handles[i].putUnifyAll(outputEM[i]);
                outputEM[i] = null;
            }
        }
        // all others are handled by base multi block code - cleaning is automatic
    }

    @Override
    public void parametersStatusesWrite_EM(boolean machineBusy) {
        if (!machineBusy) {
            setCurrentBehaviour();
        }
        super.parametersStatusesWrite_EM(machineBusy);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide() && (aTick & 0x2) == 0) {
            if ((aTick & 0x10) == 0) {
                setCurrentBehaviour();
            }
            if (aBaseMetaTileEntity.isActive()) {
                int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * 2 + aBaseMetaTileEntity.getXCoord();
                int yDir = aBaseMetaTileEntity.getBackFacing().offsetY * 2 + aBaseMetaTileEntity.getYCoord();
                int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * 2 + aBaseMetaTileEntity.getZCoord();
                aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(xDir, yDir, zDir, xDir, yDir, zDir);
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 2, 1, stackSize, hintsOnly);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_machine> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
