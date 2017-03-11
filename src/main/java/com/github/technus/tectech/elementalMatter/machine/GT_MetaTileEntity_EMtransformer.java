package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.elementalMatter.commonValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static com.github.technus.tectech.elementalMatter.commonValues.multiCheckAt;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EMtransformer extends GT_MetaTileEntity_MultiblockBase_Elemental  {
    public GT_MetaTileEntity_EMtransformer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    public GT_MetaTileEntity_EMtransformer(String aName) {
        super(aName);
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EMtransformer(this.mName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir)!=sBlockCasingsTT || iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir)!=6) return false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if (!addEnergyIOToMachineList(tTileEntity, 99)) {
                            if (    iGregTechTileEntity.getBlockOffset(xDir + i, h, zDir + j) != sBlockCasingsTT ||
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
                "Power substation",
                EnumChatFormatting.AQUA.toString()+EnumChatFormatting.BOLD+"All the transformation!",
                EnumChatFormatting.AQUA+"SafeVoid button = Soft Hammer!",
                EnumChatFormatting.BLUE+"Only 0.78125% power loss, HAYO!",
        };
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        if(ePowerPass){
            mEfficiencyIncrease=10000;
            mMaxProgresstime=20;
        }else {
            mEfficiencyIncrease=0;
            mMaxProgresstime=0;
        }
        eAmpereRating=0;
        mEUt=0;
        eDismatleBoom=ePowerPass;
        return ePowerPass;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if((aTick%20)==multiCheckAt) {
            if(eSafeVoid){
                eSafeVoid=false;
                if(aBaseMetaTileEntity.isAllowedToWork())
                    aBaseMetaTileEntity.disableWorking();
                else aBaseMetaTileEntity.enableWorking();
            }
            ePowerPass = aBaseMetaTileEntity.isAllowedToWork();
        }
    }
}
