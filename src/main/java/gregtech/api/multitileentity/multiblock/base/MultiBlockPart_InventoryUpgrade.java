package gregtech.api.multitileentity.multiblock.base;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.multitileentity.base.BaseNontickableMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart.IMBP_InventoryUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

public class MultiBlockPart_InventoryUpgrade extends BaseNontickableMultiTileEntity
        implements IMultiBlockPart, IMBP_InventoryUpgrade {

    protected ChunkCoordinates mTargetPos = null;
    protected IMultiBlockController mTarget = null;
    protected String mInventoryName = null;

    @Override
    public String getTileEntityName() {
        return null;
    }

    @Override
    public void setLightValue(byte aLightValue) {}

    @Override
    public byte getComparatorValue(byte aSide) {
        return 0;
    }

    @Override
    public String getInventoryName() {
        return mInventoryName;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public IItemHandlerModifiable getInventory() {
        return null;
    }

    @Override
    public void setTargetPos(ChunkCoordinates aTargetPos) {
        mTargetPos = aTargetPos;
    }

    @Override
    public ChunkCoordinates getTargetPos() {
        return mTargetPos;
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        if (mTargetPos != null) {
            aNBT.setBoolean(NBT.TARGET, true);
            aNBT.setInteger(NBT.TARGET_X, mTargetPos.posX);
            aNBT.setShort(NBT.TARGET_Y, (short) mTargetPos.posY);
            aNBT.setInteger(NBT.TARGET_Z, mTargetPos.posZ);
        }
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        if (aNBT.hasKey(NBT.TARGET)) {
            mTargetPos = new ChunkCoordinates(
                    aNBT.getInteger(NBT.TARGET_X), aNBT.getShort(NBT.TARGET_Y), aNBT.getInteger(NBT.TARGET_Z));
        }
    }
}
