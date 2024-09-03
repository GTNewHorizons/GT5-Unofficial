package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import tectech.mechanics.dataTransport.InventoryDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

public class MTEHatchDataItemsOutput extends MTEHatchDataConnector<InventoryDataPacket> {

    public MTEHatchDataItemsOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.dataoutass.desc.0"),
                translateToLocal("gt.blockmachines.hatch.dataoutass.desc.1"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.dataoutass.desc.2") });
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchDataItemsOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataItemsOutput(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    protected InventoryDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new InventoryDataPacket(nbt);
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isOutputFacing(side);
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        IConnectsToDataPipe current = this, source = this, next;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof MTEHatchDataItemsInput) {
                ((MTEHatchDataItemsInput) next).setContents(q);
                break;
            }
            source = current;
            current = next;
        }
        q = null;
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source /* ==this */) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        byte color = base.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof MTEPipeData) {
            ((MTEPipeData) meta).markUsed();
            return (IConnectsToDataPipe) meta;
        } else if (meta instanceof MTEHatchDataItemsInput && ((MTEHatchDataItemsInput) meta).getColorization() == color
            && ((MTEHatchDataItemsInput) meta).canConnectData(
                base.getFrontFacing()
                    .getOpposite())) {
                        return (IConnectsToDataPipe) meta;
                    }
        return null;
    }
}
