package com.github.technus.tectech.thing.metaTileEntity.multi;

import cofh.api.energy.IEnergyContainerItem;
import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.thing.metaTileEntity.constructableTT;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.GregTech_API.mEUtoRF;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_infuser extends GT_MetaTileEntity_MultiblockBase_EM implements constructableTT {
    //region Structure
    private static final String[][] shape = new String[][]{
            {"   ", "000", "1.1", "000", "   ",},
            {"   ", "010", "111", "010", "   ",},
            {"   ", "000", "111", "000", "   ",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{7, 4};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final byte[] casingTextures = new byte[]{textureOffset};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0};
    //endregion

    public GT_MetaTileEntity_EM_infuser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        minRepairStatus = (byte) getIdealStatus();
    }

    public GT_MetaTileEntity_EM_infuser(String aName) {
        super(aName);
        minRepairStatus = (byte) getIdealStatus();
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_infuser(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, 0);
    }

    @Override
    public void construct(int qty) {
        StructureBuilder(shape, blockType, blockMeta,1, 2, 0, getBaseMetaTileEntity());
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        if (itemStack != null && itemStack.stackSize == 1) {
            Item ofThis = itemStack.getItem();
            if (ofThis instanceof IElectricItem) {
                mEfficiencyIncrease = 10000;
                mMaxProgresstime = 20;
                eDismatleBoom = true;
                return true;
            } else if (TecTech.hasCOFH && ofThis instanceof IEnergyContainerItem) {
                mEfficiencyIncrease = 10000;
                mMaxProgresstime = 20;
                eDismatleBoom = true;
                return true;
            }
        }
        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        eDismatleBoom = false;
        eAmpereFlow = 0;
        mEUt = 0;
        return false;
    }

    @Override
    public void EM_outputFunction() {
        ItemStack itemStack = mInventory[1];
        if (itemStack != null && itemStack.stackSize == 1) {
            Item ofThis = itemStack.getItem();
            if (ofThis instanceof IElectricItem) {
                if (doChargeItemStack((IElectricItem) ofThis, itemStack) == 0)
                    this.getBaseMetaTileEntity().disableWorking();
                return;
            } else if (TecTech.hasCOFH && ofThis instanceof IEnergyContainerItem) {
                if (doChargeItemStackRF((IEnergyContainerItem) ofThis, itemStack) == 0)
                    this.getBaseMetaTileEntity().disableWorking();
                return;
            }
        }
        this.getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((aTick & 31) == 31) {
            eSafeVoid = false;
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Power Transfer Extreme!",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Insanely fast charging!",
                EnumChatFormatting.BLUE + "Doesn't work while broken!",
                EnumChatFormatting.BLUE + "Power loss is a thing."
        };
    }

    private long doChargeItemStack(IElectricItem item, ItemStack stack) {
        try {
            double euDiff = item.getMaxCharge(stack) - ElectricItem.manager.getCharge(stack);
            if (euDiff > 0) this.setEUVar(this.getEUVar() - this.getEUVar() >> 5);
            long remove = (long) Math.ceil(
                    ElectricItem.manager.charge(stack,
                            Math.min(euDiff, this.getEUVar())
                            , item.getTier(stack), true, false));
            this.setEUVar(this.getEUVar() - remove);
            if (this.getEUVar() < 0) this.setEUVar(0);
            return remove;
        } catch (Exception e) {
            if (TecTechConfig.DEBUG_MODE)
                e.printStackTrace();
        }
        return 0;
    }

    private long doChargeItemStackRF(IEnergyContainerItem item, ItemStack stack) {
        try {
            long RF = Math.min(item.getMaxEnergyStored(stack) - item.getEnergyStored(stack), this.getEUVar() * mEUtoRF / 100L);
            //if(RF>0)this.setEUVar(this.getEUVar()-this.getEUVar()>>10);
            RF = item.receiveEnergy(stack, RF > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) RF, false);
            RF = RF * 100L / mEUtoRF;
            this.setEUVar(this.getEUVar() - RF);
            if (this.getEUVar() < 0) this.setEUVar(0);
            return RF;
        } catch (Exception e) {
            if (TecTechConfig.DEBUG_MODE)
                e.printStackTrace();
        }
        return 0;
    }
}
