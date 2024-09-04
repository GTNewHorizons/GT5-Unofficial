package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class MTEHatchCreativeData extends MTEHatchDataConnector<QuantumDataPacket> {

    public MTEHatchCreativeData(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.debug.tt.data.desc.0"),
                translateToLocal("gt.blockmachines.debug.tt.data.desc.1"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.data.desc.2") });
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchCreativeData(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCreativeData(mName, mTier, mDescriptionArray, mTextures);
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
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    protected QuantumDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new QuantumDataPacket(nbt);
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
            if (next instanceof MTEHatchDataInput) {
                ((MTEHatchDataInput) next).setContents(q);
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
        } else if (meta instanceof MTEHatchDataInput && ((MTEHatchDataInput) meta).getColorization() == color
            && ((MTEHatchDataInput) meta).canConnectData(
                base.getFrontFacing()
                    .getOpposite())) {
                        return (IConnectsToDataPipe) meta;
                    }
        return null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (CommonValues.MOVE_AT == aTick % 20) {
                if (aBaseMetaTileEntity.isAllowedToWork()) {
                    getBaseMetaTileEntity().setActive(true);
                    if (q == null) q = new QuantumDataPacket(0xFFFFFFFFL);
                    moveAround(aBaseMetaTileEntity);
                } else {
                    q = null;
                    getBaseMetaTileEntity().setActive(false);
                }
            }
        }
    }
}
