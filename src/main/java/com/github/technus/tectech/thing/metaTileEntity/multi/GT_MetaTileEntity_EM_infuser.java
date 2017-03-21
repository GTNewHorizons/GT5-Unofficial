package com.github.technus.tectech.thing.metaTileEntity.multi;

import cofh.api.energy.IEnergyContainerItem;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.thing.metaTileEntity.multi.gui.GT_GUIContainer_MultiMachineEM;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.GregTech_API.mEUtoRF;

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
        minRepairStatus=(byte) getIdealStatus();
    }

    public GT_MetaTileEntity_EM_infuser(String aName) {
        super(aName);
        minRepairStatus=(byte) getIdealStatus();
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
                        if ((!addMaintenanceToMachineList(tTileEntity, 99)) &&
                                (!addEnergyIOToMachineList(tTileEntity, 99))) {
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
        if (getBaseMetaTileEntity().isAllowedToWork() && itemStack!=null && itemStack.stackSize==1) {
            Item ofThis=itemStack.getItem();
            if(itemStack.getItem() instanceof IElectricItem){
                doChargeItemStack((IElectricItem) ofThis,itemStack);
            }else if(TecTech.hasCOFH && itemStack.getItem() instanceof IEnergyContainerItem){
                doChargeItemStackRF((IEnergyContainerItem) ofThis,itemStack);
            }
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
            eDismatleBoom=true;
        } else {
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
            eDismatleBoom=false;
        }
        eAmpereFlow = 0;
        return ePowerPass;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Power Transfer Extreme!",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Insanely fast charging!",
                EnumChatFormatting.BLUE + "Doesn't work while broken!",
                EnumChatFormatting.BLUE + "Power loss is a thing."
        };
    }

    private void doChargeItemStack(IElectricItem item, ItemStack stack )
    {
        try {
            double euDiff=item.getMaxCharge(stack) - ElectricItem.manager.getCharge(stack);
            if(euDiff>0)this.setEUVar(this.getEUVar()-this.getEUVar()>>5);
            this.setEUVar(
                this.getEUVar()-(long)Math.ceil(
                        ElectricItem.manager.charge(stack,
                                Math.min(euDiff,this.getEUVar())
                        ,item.getTier(stack),true,false)
                ));
        } catch( Exception e ) {
            if(TecTech.ModConfig.DEBUG_MODE)
                e.printStackTrace();
        }
    }

    private void doChargeItemStackRF(IEnergyContainerItem item,  ItemStack stack )
    {
        try {
            long RF=Math.min(item.getMaxEnergyStored(stack)-item.getEnergyStored(stack),this.getEUVar()*mEUtoRF/100L);
            //if(RF>0)this.setEUVar(this.getEUVar()-this.getEUVar()>>10);
            RF=item.receiveEnergy(stack,RF>Integer.MAX_VALUE?Integer.MAX_VALUE:(int)RF,false);
            this.setEUVar(this.getEUVar()-(RF*100L/mEUtoRF));
        } catch( Exception e ) {
            if (TecTech.ModConfig.DEBUG_MODE)
                e.printStackTrace();
        }
    }
}
