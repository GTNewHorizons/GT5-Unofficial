package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.dataFramework.InventoryDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.pipe.iConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

import static com.github.technus.tectech.CommonValues.MOVE_AT;

public class GT_MetaTileEntity_Hatch_InputDataAccess extends GT_MetaTileEntity_Hatch_DataAccess implements iConnectsToDataPipe {
    private final String mDescription;
    public boolean delDelay;
    private ItemStack[] stacks;

    public GT_MetaTileEntity_Hatch_InputDataAccess(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
        mDescription="ItemStack Data Input for Multiblocks";
    }

    public GT_MetaTileEntity_Hatch_InputDataAccess(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        mDescription=aDescription;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputDataAccess(this.mName, this.mTier, mDescription, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        } else {
            aBaseMetaTileEntity.openGUI(aPlayer);
            return true;
        }
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return false;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean canConnect(byte side) {
        return isInputFacing(side);
    }

    @Override
    public iConnectsToDataPipe getNext(iConnectsToDataPipe source) {
        return null;
    }

    public void setContents(InventoryDataPacket q){
        if(q==null){
            stacks=null;
        }else{
            if(q.getContent().length>0) {
                stacks = q.getContent();
                delDelay=true;
            }else{
                stacks=null;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        NBTTagCompound stacksTag=new NBTTagCompound();
        if(stacks!=null && stacks.length>0){
            stacksTag.setInteger("count",stacks.length);
            for(int i=0;i<stacks.length;i++){
                stacksTag.setTag(Integer.toString(i),stacks[i].writeToNBT(new NBTTagCompound()));
            }
        }
        aNBT.setTag("data_stacks",stacksTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        NBTTagCompound stacksTag=new NBTTagCompound();
        int count=stacksTag.getInteger("count");
        if(count>0){
            ArrayList<ItemStack> stacks=new ArrayList<>();
            for(int i=0;i<count;i++){
                ItemStack stack=ItemStack.loadItemStackFromNBT(stacksTag.getCompoundTag(Integer.toString(i)));
                if(stack!=null){
                    stacks.add(stack);
                }
            }
            if(stacks.size()>0) {
                this.stacks = stacks.toArray(new ItemStack[0]);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return stacks!=null?stacks.length:0;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        return stacks!=null && aIndex<stacks.length?stacks[aIndex]:null;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity,aTick);
        if(MOVE_AT == aTick % 20) {
            if (delDelay) {
                delDelay = false;
            } else {
                setContents(null);
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                mDescription,
                "High speed fibre optics connector.",
                EnumChatFormatting.AQUA + "Must be painted to work"
        };
    }
}
