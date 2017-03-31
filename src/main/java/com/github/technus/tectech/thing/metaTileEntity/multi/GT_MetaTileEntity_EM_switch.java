package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.elementalMatter.CommonValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_switch extends GT_MetaTileEntity_MultiblockBase_EM {
    public GT_MetaTileEntity_EM_switch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_switch(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_switch(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[96], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[96]};
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, yDir, zDir) != sBlockCasingsTT || iGregTechTileEntity.getMetaIDOffset(xDir, yDir, zDir) != 1)
            return false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((i != 0 || j != 0 || h != 0)/*exclude center*/ && (xDir + i != 0 || yDir + h != 0 || zDir + j != 0)/*exclude this*/) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, yDir + h, zDir + j);
                        if (!addEnergyIOToMachineList(tTileEntity, 96) &&
                                (!addDataConnectorToMachineList(tTileEntity,96)) &&
                                (!addMaintenanceToMachineList(tTileEntity,96))) {
                            if (iGregTechTileEntity.getBlockOffset(xDir + i, yDir + h, zDir + j) != sBlockCasingsTT ||
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
                CommonValues.tecMark,
                "User controller computation power routing",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Quality of service is a must"
        };
    }
}
