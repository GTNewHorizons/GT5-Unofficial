package gtnhlanth.common.hatch;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.util.CommonValues.MOVE_AT;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;
import gtnhlanth.common.beamline.IConnectsToBeamline;
import tectech.mechanics.dataTransport.DataPacket;

public abstract class MTEHatchBeamlineConnector<T extends DataPacket> extends MTEHatch implements IConnectsToBeamline {

    private String clientLocale = "en_US";

    public T q;

    public short id = -1;

    protected MTEHatchBeamlineConnector(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
    }

    protected MTEHatchBeamlineConnector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setShort("eID", id);
        if (q != null) {
            aNBT.setTag("eDATA", q.toNbt());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        id = aNBT.getShort("eID");
        if (aNBT.hasKey("eDATA")) {
            q = loadPacketFromNBT(aNBT.getCompoundTag("eDATA"));
        }
    }

    protected abstract T loadPacketFromNBT(NBTTagCompound nbt);

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (MOVE_AT == aTick % 20) {
                if (q == null) {
                    getBaseMetaTileEntity().setActive(false);
                } else {
                    getBaseMetaTileEntity().setActive(true);
                    moveAround(aBaseMetaTileEntity);
                }
            }
        }
    }

    public abstract void moveAround(IGregTechTileEntity aBaseMetaTileEntity);

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        if (aPlayer instanceof EntityPlayerMPAccessor) {
            clientLocale = ((EntityPlayerMPAccessor) aPlayer).gt5u$getTranslator();
        }
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (id > 0) {
            return new String[] {
                translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": " + EnumChatFormatting.AQUA + id,
                translateToLocalFormatted("tt.keyword.Content", clientLocale) + ": "
                    + EnumChatFormatting.AQUA
                    + (q != null ? q.getContentString() : 0),
                translateToLocalFormatted("tt.keyword.PacketHistory", clientLocale) + ": "
                    + EnumChatFormatting.RED
                    + (q != null ? q.getTraceSize() : 0), };
        }
        return new String[] {
            translateToLocalFormatted("tt.keyword.Content", clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + (q != null ? q.getContentString() : 0),
            translateToLocalFormatted("tt.keyword.PacketHistory", clientLocale) + ": "
                + EnumChatFormatting.RED
                + (q != null ? q.getTraceSize() : 0), };
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Text description shouldn't be seen, report to Tec", "High speed fibre optics connector.",
            EnumChatFormatting.AQUA + "Must be painted to work" };
    }
}
