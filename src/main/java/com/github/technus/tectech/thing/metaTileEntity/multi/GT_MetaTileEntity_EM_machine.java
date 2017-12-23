package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.common.blocks.GT_Block_Machines;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_machine extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    public final static String machine="EM Machinery";

    //region structure
    private static final String[][] shape = new String[][]{
            {"B0","A   ","0 - 0","A   ","B0",},
            {"A000","00000","00.00","00000","A000",},
            {"A121","1C1","2C2","1C1","A121",},
            {"A131","1C1","3C3","1C1","A131",},
            {"A121","1C1","2C2","1C1","A121",},
            {"A000","00000","00A00","00000","A000",},
            {"B0","A!!!","0!\"!0","A!!!","B0",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 0, 5, 6};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList", "addElementalInputToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
            "2 - Elemental Input Hatch",
    };
    //endregion

    public GT_MetaTileEntity_EM_machine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_machine(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_machine(this.mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 1)
                && eInputHatches.size()==1 && iGregTechTileEntity.getBlockAtSideAndDistance(iGregTechTileEntity.getBackFacing(),5) instanceof GT_Block_Machines;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,2, 2, 1, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Processing quantum matter since...",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "the time u started using it."
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        Behaviour currentBehaviour=map.get(new GT_ItemStack(itemStack));
        if(currentBehaviour==null) return false;
        //mux input
        double[] parameters=new double[]{getParameterIn(0,0),getParameterIn(0,1),getParameterIn(1,0),getParameterIn(1,1),
                getParameterIn(2,0),getParameterIn(2,1),getParameterIn(3,0),getParameterIn(3,1)};
        if(!currentBehaviour.setAndCheckParametersOutAndStatuses(this,parameters))return false;
        cElementalInstanceStackMap[] handles=new cElementalInstanceStackMap[3];
        MultiblockControl<cElementalInstanceStackMap> control=currentBehaviour.process(handles,parameters);
        if(control==null) return false;
        //update other pare
        outputEM=control.getValues();
        mEUt=control.getEUT();
        eAmpereFlow=control.getAmperage();
        mMaxProgresstime=control.getMaxProgressTime();
        eRequiredData=control.getRequiredData();
        mEfficiencyIncrease=control.getEffIncrease();
        return true;
    }

    @Override
    protected void parametersLoadDefault_EM() {//default 1 to 1 routing table
        setParameterPairIn_ClearOut(4,false,1,1);//I
        setParameterPairIn_ClearOut(5,false,2,2);//I
        setParameterPairIn_ClearOut(6,false,3,3);//I
        setParameterPairIn_ClearOut(7,false,1,1);//O
        setParameterPairIn_ClearOut(8,false,2,2);//O
        setParameterPairIn_ClearOut(9,false,3,3);//O
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        //update routing
    }

    @Override
    public void outputAfterRecipe_EM() {
        //mux output
        //output
    }

    private static final HashMap<GT_ItemStack,Behaviour> map=new HashMap<>();
    public abstract class Behaviour {
        public Behaviour(ItemStack... keyItems){
            for(ItemStack is:keyItems){
                map.put(new GT_ItemStack(is.getItem(),1,is.getItemDamage()),this);
            }
        }

        public abstract boolean setAndCheckParametersOutAndStatuses(GT_MetaTileEntity_EM_machine te, double[] parameters);
        public abstract MultiblockControl<cElementalInstanceStackMap> process(cElementalInstanceStackMap[] inputs, double[] parameters);
    }
}
