package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import gregtech.api.enums.ChatMessage;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Localized;
import io.netty.buffer.ByteBuf;

/**
 * {@link net.minecraft.util.IChatComponent} has a lot of weird formatting oddities, so instead of mixin'ing the hell
 * out of it I just made this instead. This supports complete localization along with all sorts of funky stuff since you
 * can recursively nest ChatMessage's as format arguments.
 */
public class GTPacketChat extends GTPacket {

    private Localized message;

    GTPacketChat() {}

    public GTPacketChat(Localized message) {
        this.message = message;
    }

    public GTPacketChat(String key, Object... args) {
        this.message = new Localized(key, args);
    }

    public GTPacketChat(ChatMessage message, Object... args) {
        this.message = new Localized(message, args);
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.CHAT.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        message.encode(buffer);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        Localized message = new Localized();
        message.decode(buffer);

        return new GTPacketChat(message);
    }

    @Override
    public void process(IBlockAccess world) {
        // Assume we are on the client
        GTUtility.sendChatToPlayer(GTMod.proxy.getThePlayer(), message.toString());
    }
}
