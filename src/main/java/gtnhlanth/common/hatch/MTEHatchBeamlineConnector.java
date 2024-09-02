package gtnhlanth.common.hatch;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.util.CommonValues.MOVE_AT;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gtnhlanth.common.beamline.IConnectsToBeamline;
import tectech.mechanics.dataTransport.DataPacket;
import tectech.util.TTUtility;

public abstract class MTEHatchBeamlineConnector<T extends DataPacket> extends MTEHatch implements IConnectsToBeamline {

    private String clientLocale = "en_US";

    public T q;

    public short id = -1;

    protected MTEHatchBeamlineConnector(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
        TTUtility.setTier(aTier, this);
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
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
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
