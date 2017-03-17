package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.thing.metaTileEntity.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.gui.GT_GUIContainer_MultiMachineEM;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.elementalMatter.commonValues.multiCheckAt;
import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_infuser extends GT_MetaTileEntity_MultiblockBase_EM {
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"",//left to right top
                    "",
                    ""},//front
            {},//behind front
            {} //behind
    };
    private static final int[] casingRequirements = new int[]{};
    private static final Block[] blockType = new Block[]{};
    private static final byte[] blockMeta = new byte[]{};

    public GT_MetaTileEntity_EM_infuser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_infuser(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_infuser(this.mName);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "EMDisplayPower.png");
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, yDir, zDir) != sBlockCasingsTT || iGregTechTileEntity.getMetaIDOffset(xDir, yDir, zDir) != 6)
            return false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((i != 0 || j != 0 || h != 0)/*exclude center*/ && (xDir + i != 0 || yDir + h != 0 || zDir + j != 0)/*exclude this*/) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, yDir + h, zDir + j);
                        if (!addEnergyIOToMachineList(tTileEntity, 99)) {
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
    public boolean EM_checkRecipe(ItemStack itemStack) {
        if (getBaseMetaTileEntity().isAllowedToWork()) {


            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
            eDismatleBoom=true;
        } else {
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
            eDismatleBoom=false;
        }
        eAmpereFlow = 0;
        mEUt = 0;
        return ePowerPass;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Power substation",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "All the transformation!",
                EnumChatFormatting.BLUE + "Insanely fast lossy charging, HAYO!",
        };
    }
}
