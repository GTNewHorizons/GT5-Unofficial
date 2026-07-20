package gtnhlanth.common.hatch;

import static tectech.util.CommonValues.MOVE_AT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;
import gtnhlanth.common.beamline.BeamInformation;
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
        List<String> lines = new ArrayList<>();

        if (this.dataPacket != null) {
            BeamInformation content = this.dataPacket.getContent();
            lines.add(IGregTechDeviceInformation.encode("tt.keyword.Content.fmt", " "));
            lines.add(
                IGregTechDeviceInformation
                    .encode("beamline.energy.keV.fmt", Math.floor(content.getEnergy() * 1000000) / 1000));
            lines.add(
                content.getParticle()
                    .encodeInfoData());
            lines.add(IGregTechDeviceInformation.encode("beamline.amount.fmt", content.getRate()));
            lines
                .add(IGregTechDeviceInformation.encode("beamline.focus.fmt", Math.floor(content.getFocus() * 10) / 10));

            lines
                .add(IGregTechDeviceInformation.encode("tt.keyword.PacketHistory.fmt", this.dataPacket.getTraceSize()));
        } else {
            lines.add(IGregTechDeviceInformation.encode("tt.keyword.Content.fmt", 0));
            lines.add(IGregTechDeviceInformation.encode("tt.keyword.PacketHistory.fmt", 0));
        }

        return lines.toArray(String[]::new);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Text description shouldn't be seen, report to Tec", "High speed fibre optics connector.",
            EnumChatFormatting.AQUA + "Must be painted to work" };
    }
}
