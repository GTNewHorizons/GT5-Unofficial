package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.mechanics.constructable.Structure;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.function.Supplier;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_machine extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    public static final String machine = "EM Machinery";

    private ItemStack loadedMachine;
    private IBehaviour currentBehaviour;
    //endregion

    //region structure
    private static final String[][] shape = new String[][]{
            {"B0", "A   ", "0 - 0", "A   ", "B0",},
            {"A000", "00000", "00.00", "00000", "A000",},
            {"A121", "1---1", "2---2", "1---1", "A121",},
            {"A131", "1---1", "3-A-3", "1---1", "A131",},
            {"A121", "1---1", "2---2", "1---1", "A121",},
            {"A000", "00000", "00-00", "00000", "A000",},
            {"B0", "A!!!", "0!!!0", "A!!!", "B0",},};
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 0, 5, 6};
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addClassicToMachineList, this::addElementalToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.processing.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.processing.hint.1"),};//2 - Elemental Hatches or Molecular Casing
    //endregion

    //region parameters
    protected Parameters.Group.ParameterIn[] inputMux;
    protected Parameters.Group.ParameterIn[] outputMux;
    private static final IStatusFunction<GT_MetaTileEntity_EM_machine> SRC_STATUS =
            (base, p) -> {
                double v = p.get();
                if (Double.isNaN(v)) return STATUS_WRONG;
                v = (int) v;
                if (v < 0) return STATUS_TOO_LOW;
                if (v == 0) return STATUS_NEUTRAL;
                if (v >= base.eInputHatches.size()) return STATUS_TOO_HIGH;
                return STATUS_OK;
            };
    private static final IStatusFunction<GT_MetaTileEntity_EM_machine> DST_STATUS =
            (base, p) -> {
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
    private static final INameFunction<GT_MetaTileEntity_EM_machine> ROUTE_NAME =
            (base, p) -> (p.parameterId() == 0 ? translateToLocal("tt.keyword.Source") + " " : translateToLocal("tt.keyword.Destination") + " ") + p.hatchId();
    //endregion

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
        Supplier<IBehaviour> behaviourSupplier = GT_MetaTileEntity_EM_machine.BEHAVIOUR_MAP.get(new Util.ItemStack_NoNBT(newMachine));
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

    private static final HashMap<Util.ItemStack_NoNBT, Supplier<IBehaviour>> BEHAVIOUR_MAP = new HashMap<>();

    public static void registerBehaviour(Supplier<IBehaviour> behaviour, ItemStack is) {
        BEHAVIOUR_MAP.put(new Util.ItemStack_NoNBT(is), behaviour);
        TecTech.LOGGER.info("Registered EM machine behaviour " + behaviour.get().getClass().getSimpleName() + ' ' + new Util.ItemStack_NoNBT(is).toString());
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
        MultiblockControl<cElementalInstanceStackMap[]> process(cElementalInstanceStackMap[] inputs, GT_MetaTileEntity_EM_machine te, Parameters parameters);
    }

    private void quantumStuff(boolean shouldIExist) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.getWorld() != null) {
            int xDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetX * 2 + base.getXCoord();
            int yDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetY * 2 + base.getYCoord();
            int zDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetZ * 2 + base.getZCoord();
            Block block = base.getWorld().getBlock(xDir, yDir, zDir);
            if (shouldIExist) {
                if (block != null && block.getMaterial() == Material.air) {
                    base.getWorld().setBlock(xDir, yDir, zDir, QuantumStuffBlock.INSTANCE, 0, 2);
                }
            } else {
                if (block instanceof QuantumStuffBlock) {
                    base.getWorld().setBlock(xDir, yDir, zDir, Blocks.air, 0, 2);
                }
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_machine(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 1);
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
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.processing.desc.0"),//Processing quantum matter since...
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.processing.desc.1")//the time u started using it.
        };
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        setCurrentBehaviour();
        if (aBaseMetaTileEntity.isServerSide()) {
            quantumStuff(aBaseMetaTileEntity.isActive());
        }
    }

    @Override
    public void onRemoval() {
        quantumStuff(false);
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

        cElementalInstanceStackMap[] handles = new cElementalInstanceStackMap[6];
        for (int i = 0; i < 6; i++) {
            int pointer = (int) inputMux[i].get();
            if (pointer >= 0 && pointer < eInputHatches.size()) {
                handles[i] = eInputHatches.get(pointer).getContainerHandler();
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

        MultiblockControl<cElementalInstanceStackMap[]> control = currentBehaviour.process(handles, this, parametrization);
        if (control == null) {
            return false;
        }
        cleanMassEM_EM(control.getExcessMass());
        if (control.shouldExplode()) {
            explodeMultiblock();
            return false;
        }
        //update other parameters
        outputEM = control.getValue();
        mEUt = control.getEUT();
        eAmpereFlow = control.getAmperage();
        mMaxProgresstime = control.getMaxProgressTime();
        eRequiredData = control.getRequiredData();
        mEfficiencyIncrease = control.getEffIncrease();
        boolean polluted = polluteEnvironment(control.getPollutionToAdd());
        quantumStuff(polluted);
        return polluted;
    }

    @Override
    public void stopMachine() {
        quantumStuff(false);
        super.stopMachine();
    }

    @Override
    protected void afterRecipeCheckFailed() {
        quantumStuff(false);
        super.afterRecipeCheckFailed();
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (setCurrentBehaviour()) {
            return;
        }

        cElementalInstanceStackMap[] handles = new cElementalInstanceStackMap[6];
        for (int i = 0; i < 6; i++) {
            int pointer = (int) outputMux[i].get();
            if (pointer >= 0 && pointer < eOutputHatches.size()) {
                handles[i] = eOutputHatches.get(pointer).getContainerHandler();
            }
        }
        //output
        for (int i = 0; i < 6 && i < outputEM.length; i++) {
            if (handles[i] != null && outputEM[i] != null && outputEM[i].hasStacks()) {
                handles[i].putUnifyAll(outputEM[i]);
                outputEM[i] = null;
            }
        }
        quantumStuff(false);
        //all other are handled by base multi block code - cleaning is automatic
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
                int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2 + aBaseMetaTileEntity.getXCoord();
                int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY * 2 + aBaseMetaTileEntity.getYCoord();
                int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2 + aBaseMetaTileEntity.getZCoord();
                aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(xDir, yDir, zDir, xDir, yDir, zDir);
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        Structure.builder(shape, blockType, blockMeta, 2, 2, 1, getBaseMetaTileEntity(), getExtendedFacing(), hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }
}