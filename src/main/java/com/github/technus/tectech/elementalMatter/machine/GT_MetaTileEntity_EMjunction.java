package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.blocks.QuantumGlass;
import com.github.technus.tectech.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.elementalMatter.commonValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EMjunction extends GT_MetaTileEntity_MultiblockBase_Elemental  {
    public GT_MetaTileEntity_EMjunction(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EMjunction(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EMjunction(this.mName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, yDir, zDir)!= QuantumGlass.INSTANCE) return false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((i!=0 || j!=0 || h!=0)/*exclude center*/&&(xDir+i!=0 || yDir+h!=0 || zDir+j!=0)/*exclude this*/) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, yDir + h, zDir + j);
                        if (    (!addMaintenanceToMachineList(tTileEntity, 99)) &&
                                (!addElementalInputToMachineList(tTileEntity, 99)) &&
                                (!addElementalOutputToMachineList(tTileEntity, 99)) &&
                                (!addMufflerToMachineList(tTileEntity, 99)) &&
                                (!addEnergyIOToMachineList(tTileEntity, 99))) {
                            if (    iGregTechTileEntity.getBlockOffset(xDir + i, yDir + h, zDir + j) != GT_Container_CasingsTT.sBlockCasingsTT ||
                                    iGregTechTileEntity.getMetaIDOffset(xDir + i, yDir + h, zDir + j) != 3) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Reroutes Matter",
                EnumChatFormatting.AQUA.toString()+EnumChatFormatting.BOLD+"Axis aligned movement!"
        };
    }

    @Override
    public void EM_checkParams() {
        for(int i=0;i<10;i++){
            if((int)eParamsIn[i]<0)eParamsInStatus[i]=PARAM_TOO_LOW;
            else if((int)eParamsIn[i]==0)eParamsInStatus[i]=PARAM_UNUSED;
            else if((int)eParamsIn[i]>eInputHatches.size())eParamsInStatus[i]=PARAM_TOO_HIGH;
            else eParamsInStatus[i]=PARAM_OK;
        }
        for(int i=10;i<20;i++){
            if(eParamsInStatus[i-10]==PARAM_OK){
                if((int)eParamsIn[i]<0) eParamsInStatus[i] = PARAM_TOO_LOW;
                else if((int)eParamsIn[i]==0)eParamsInStatus[i]=PARAM_LOW;
                else if((int)eParamsIn[i]>eOutputHatches.size())eParamsInStatus[i]=PARAM_TOO_HIGH;
                else eParamsInStatus[i]=PARAM_OK;
            }else{
                eParamsInStatus[i]=PARAM_UNUSED;
            }
        }
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        for(GT_MetaTileEntity_Hatch_InputElemental in: eInputHatches)
            if(in.getContainerHandler().hasStacks()) {
                mEUt=-(int)V[9];
                eAmpereFlow =1+((eInputHatches.size()+eOutputHatches.size())>>1);
                mMaxProgresstime=20;
                mEfficiencyIncrease=10000;
                return true;
            }
        mMaxProgresstime=0;
        mEfficiencyIncrease=0;
        return false;
    }

    @Override
    public void EM_outputFunction() {
        for(int i=0;i<10;i++){
            final int inIndex=(int)(eParamsIn[i])-1;
            if(inIndex<0 || inIndex>eInputHatches.size()) continue;
            final int outIndex=(int)(eParamsIn[i+10])-1;
            GT_MetaTileEntity_Hatch_InputElemental in=eInputHatches.get(inIndex);
            if(outIndex==-1){
                cleanHatchContent(in);
            }else{
                GT_MetaTileEntity_Hatch_OutputElemental out=eOutputHatches.get(outIndex);
                out.getContainerHandler().putUnifyAll(in.getContainerHandler());
                in.getContainerHandler().clear();
            }
        }
    }
}
