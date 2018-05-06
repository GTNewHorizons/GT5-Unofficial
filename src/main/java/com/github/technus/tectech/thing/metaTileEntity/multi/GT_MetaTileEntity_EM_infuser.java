package com.github.technus.tectech.thing.metaTileEntity.multi;

import cofh.api.energy.IEnergyContainerItem;
import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Reference;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static gregtech.api.GregTech_API.mEUtoRF;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_infuser extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region Structure
    private static final String[][] shape = new String[][]{
            {"   ", "000", "1.1", "000", "   ",},
            {"   ", "010", "111", "010", "   ",},
            {"   ", "000", "111", "000", "   ",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{7, 4};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_infuser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        minRepairStatus = (byte) getIdealStatus();
        eDismantleBoom=true;
    }

    public GT_MetaTileEntity_EM_infuser(String aName) {
        super(aName);
        minRepairStatus = (byte) getIdealStatus();
        eDismantleBoom=true;
    }


    public final static ResourceLocation activitySound=new ResourceLocation(Reference.MODID+":fx_whooum");

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound(){
        return activitySound;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_infuser(mName);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity,true,false,true);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png",true,false,true);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta,1, 2, 0, getBaseMetaTileEntity(),this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        if (itemStack != null && itemStack.stackSize == 1) {
            Item ofThis = itemStack.getItem();
            if (ofThis instanceof IElectricItem) {
                mEfficiencyIncrease = 10000;
                mMaxProgresstime = 20;
                return true;
            } else if (TecTech.hasCOFH && ofThis instanceof IEnergyContainerItem) {
                mEfficiencyIncrease = 10000;
                mMaxProgresstime = 20;
                return true;
            }
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        ItemStack itemStack = mInventory[1];
        if (itemStack != null && itemStack.stackSize == 1) {
            Item ofThis = itemStack.getItem();
            if (ofThis instanceof IElectricItem) {
                if (doChargeItemStack((IElectricItem) ofThis, itemStack) == 0) {
                    getBaseMetaTileEntity().disableWorking();
                }
                return;
            } else if (TecTech.hasCOFH && ofThis instanceof IEnergyContainerItem) {
                if (doChargeItemStackRF((IEnergyContainerItem) ofThis, itemStack) == 0) {
                    getBaseMetaTileEntity().disableWorking();
                }
                return;
            }
        }
        getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                "Power Transfer Extreme!",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Insanely fast charging!",
                EnumChatFormatting.BLUE + "Doesn't work while broken!",
                EnumChatFormatting.BLUE + "Power loss is a thing."
        };
    }

    private long doChargeItemStack(IElectricItem item, ItemStack stack) {
        try {
            double euDiff = item.getMaxCharge(stack) - ElectricItem.manager.getCharge(stack);
            if (euDiff > 0) {
                setEUVar(getEUVar() - (getEUVar() >> 5));
            }
            long remove = (long) Math.ceil(
                    ElectricItem.manager.charge(stack,
                            Math.min(euDiff, getEUVar())
                            , item.getTier(stack), true, false));
            setEUVar(getEUVar() - remove);
            if (getEUVar() < 0) {
                setEUVar(0);
            }
            return remove;
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private long doChargeItemStackRF(IEnergyContainerItem item, ItemStack stack) {
        try {
            long RF = Math.min(item.getMaxEnergyStored(stack) - item.getEnergyStored(stack), getEUVar() * mEUtoRF / 100L);
            //if(RF>0)this.setEUVar(this.getEUVar()-this.getEUVar()>>10);
            RF = item.receiveEnergy(stack, RF > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) RF, false);
            RF = RF * 100L / mEUtoRF;
            setEUVar(getEUVar() - RF);
            if (getEUVar() < 0) {
                setEUVar(0);
            }
            return RF;
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
