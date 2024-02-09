package gregtech.api.multitileentity.base;

import static gregtech.api.enums.GT_Values.NW;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.net.GT_Packet_SendCoverData;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;

public abstract class NonTickableMultiTileEntity extends MultiTileEntity {

    boolean mConstructed = false; // Keeps track of whether this TE has been constructed and placed in the world

    public NonTickableMultiTileEntity() {
        super(false);
    }

    @Override
    public void issueClientUpdate() {
        if (worldObj != null && !worldObj.isRemote) {
            sendClientData(null);
            sendGraphicPacket();
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        // We should have a world object and have been constructed by this point
        mConstructed = true;

        super.getDescriptionPacket();
        // We don't get ticked, so if we have any cover data that needs to be sent, send it now
        sendCoverDataIfNeeded();
        return null;
    }

    @Override
    public void issueCoverUpdate(ForgeDirection side) {
        if (!mConstructed) {
            // Queue these up and send them with the description packet
            super.issueCoverUpdate(side);
        } else {
            // Otherwise, send the data right away
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            NW.sendPacketToAllPlayersInRange(worldObj, new GT_Packet_SendCoverData(coverInfo, this), xCoord, zCoord);

            // Just in case
            coverInfo.setNeedsUpdate(false);
        }
    }

    @Override
    public void receiveCoverData(ForgeDirection coverSide, int aCoverID, ISerializableObject aCoverData,
        EntityPlayerMP aPlayer) {
        super.receiveCoverData(coverSide, aCoverID, aCoverData, aPlayer);
        // We don't get ticked so issue the texture update right away
        issueTextureUpdate();
    }
}
