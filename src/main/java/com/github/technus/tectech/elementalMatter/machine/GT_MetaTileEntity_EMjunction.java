package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.blocks.QuantumGlass;
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
        mEUt=-(int)V[9];
    }

    public GT_MetaTileEntity_EMjunction(String aName) {
        super(aName);
        mEUt=-(int)V[9];
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EMjunction(this.mName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir)!= QuantumGlass.INSTANCE) {
            return false;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if (    (!addMaintenanceToMachineList(tTileEntity, 83)) &&
                                (!addElementalInputToMachineList(tTileEntity, 83)) &&
                                (!addElementalOutputToMachineList(tTileEntity, 83)) &&
                                (!addMufflerToMachineList(tTileEntity, 83)) &&
                                (!addEnergyIOToMachineList(tTileEntity, 83))) {
                            if (    iGregTechTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GT_Container_CasingsTT.sBlockCasingsTT ||
                                    iGregTechTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 3) {
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
            if(eParamsIn[i]<0)eParamsInStatus[i]=2;
            else if(eParamsIn[i]==0)eParamsInStatus[i]=0;
            else if(eParamsIn[i]>eInputHatches.size())eParamsInStatus[i]=4;
            else eParamsInStatus[i]=1;
        }
        for(int i=10;i<20;i++){
            if(eParamsIn[i]<0)eParamsInStatus[i]=2;
            else if(eParamsIn[i]==0)eParamsInStatus[i]=3;
            else if(eParamsIn[i]>eOutputHatches.size())eParamsInStatus[i]=4;
            else eParamsInStatus[i]=1;
        }
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        for(GT_MetaTileEntity_Hatch_InputElemental in: eInputHatches)
            if(in.getContainerHandler().hasStacks()) {
                ampereRating=1+((eInputHatches.size()+eOutputHatches.size())>>1);
                mMaxProgresstime=20;
                return true;
            }
        mMaxProgresstime=0;
        return false;
    }

    @Override
    public void EM_outputFunction() {
        for(int i=0;i<10;i++){
            if(((int) eParamsIn[i] - 1)<0 || ((int) eParamsIn[i] - 1)>=eInputHatches.size()) continue;
            GT_MetaTileEntity_Hatch_InputElemental in=eInputHatches.get((int) eParamsIn[i] - 1);
            if(eParamsIn[i+10]==0){
                cleanHatchContent(in);
            }else{
                GT_MetaTileEntity_Hatch_OutputElemental out=eOutputHatches.get((int)eParamsIn[i+10]-1);
                if (out != null) {
                    in.getContainerHandler().putUnifyAll(out.getContainerHandler());
                    out.getContainerHandler().clear();
                }
            }
        }
    }
}
