package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.blocks.QuantumGlass;
import com.github.technus.tectech.elementalMatter.commonValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EMdequantifier extends GT_MetaTileEntity_MultiblockBase_Elemental  {

    public GT_MetaTileEntity_EMdequantifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EMdequantifier(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EMdequantifier(this.mName);
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
                                (!addClassicOutputToMachineList(tTileEntity, 83)) &&
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
                "Conveniently convert regular stuff into quantum form.",
                EnumChatFormatting.AQUA.toString()+EnumChatFormatting.BOLD+"To make it more inconvenient."
        };
    }
}
