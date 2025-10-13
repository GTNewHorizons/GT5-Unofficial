package gregtech.api.net;

import java.util.Date;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import gregtech.api.enums.ChatMessage;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
import gregtech.common.data.GTPowerfailTracker;
import gregtech.crossmod.navigator.PowerfailLayerManager;
import io.netty.buffer.ByteBuf;

public class GTPacketOnPowerfail extends GTPacket {

    private GTPowerfailTracker.Powerfail powerfail;

    GTPacketOnPowerfail() {}

    public GTPacketOnPowerfail(GTPowerfailTracker.Powerfail powerfail) {
        this.powerfail = powerfail.copy();
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.ON_POWERFAIL.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(powerfail.dim);
        buffer.writeInt(powerfail.x);
        buffer.writeInt(powerfail.y);
        buffer.writeInt(powerfail.z);
        buffer.writeInt(powerfail.mteId);
        buffer.writeInt(powerfail.count);
        buffer.writeLong(powerfail.lastOccurrence.getTime());
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTPowerfailTracker.Powerfail powerfail = new GTPowerfailTracker.Powerfail();

        powerfail.dim = buffer.readInt();
        powerfail.x = buffer.readInt();
        powerfail.y = buffer.readInt();
        powerfail.z = buffer.readInt();
        powerfail.mteId = buffer.readInt();
        powerfail.count = buffer.readInt();
        powerfail.lastOccurrence = new Date(buffer.readLong());

        return new GTPacketOnPowerfail(powerfail);
    }

    /**
     * Only print the help text once per game session. We don't save this anywhere because it should only exist in
     * memory, and be cleared after the game is restarted. If the player doesn't want to see the help text, they can
     * disable it in the config.
     */
    private static boolean printedHelpMessage = false;

    @Override
    public void process(IBlockAccess blockAccess) {
        EntityPlayer player = GTMod.proxy.getThePlayer();

        GTPowerfailTracker.Powerfail previous = GTMod.clientProxy().powerfailRenderer.powerfails
            .put(powerfail.getCoord(), powerfail);

        if (Client.chat.powerfailNotifications && (previous == null || previous.getSecs() > 60)) {
            GTUtility.sendChatToPlayer(
                player,
                powerfail.toDescription()
                    .toString());

            if (Client.chat.printPowerfailHelpText && !printedHelpMessage) {
                printedHelpMessage = true;
                Client.save();

                ChatMessage.PowerfailCommandHint.send(player);
            }
        }

        if (Mods.Navigator.isModLoaded()) {
            PowerfailLayerManager.INSTANCE.clearFullCache();
        }
    }
}
