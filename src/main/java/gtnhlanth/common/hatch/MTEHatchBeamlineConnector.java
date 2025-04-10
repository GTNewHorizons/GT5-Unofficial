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
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.IConnectsToBeamline;

public abstract class MTEHatchBeamlineConnector extends MTEHatch implements IConnectsToBeamline {

    private String clientLocale = "en_US";
    public BeamLinePacket dataPacket = null;

    protected MTEHatchBeamlineConnector(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
    }

    protected MTEHatchBeamlineConnector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (this.dataPacket != null) {
            aNBT.setTag("eDATA", this.dataPacket.toNbt());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("eDATA")) {
            this.dataPacket = new BeamLinePacket(aNBT.getCompoundTag("eDATA"));
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (MOVE_AT == aTick % 20) {
                if (this.dataPacket == null) {
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
            this.clientLocale = ((EntityPlayerMPAccessor) aPlayer).gt5u$getTranslator();
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
        return new String[] {
            translateToLocalFormatted("tt.keyword.Content", this.clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + (this.dataPacket != null ? this.dataPacket.getContentString() : 0),
            translateToLocalFormatted("tt.keyword.PacketHistory", this.clientLocale) + ": "
                + EnumChatFormatting.RED
                + (this.dataPacket != null ? this.dataPacket.getTraceSize() : 0), };
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Text description shouldn't be seen, report to Tec", "High speed fibre optics connector.",
            EnumChatFormatting.AQUA + "Must be painted to work" };
    }
}
