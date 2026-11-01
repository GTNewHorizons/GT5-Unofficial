package gregtech.api.util;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;

import com.gtnewhorizon.gtnhlib.chat.AbstractChatComponentCustom;
import com.gtnewhorizon.gtnhlib.chat.customcomponents.AbstractChatComponentBuffer;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.Mods;

/**
 * Carries a {@link Block} over the chat component JSON so that its localized name is resolved on the client.
 *
 * <p>
 * Carries the Block registry name only, and resolves the localized name on the client.
 */
public class ChatComponentBlockName extends AbstractChatComponentBuffer<ChatComponentBlockName> {

    public Block block = null;

    public ChatComponentBlockName() {}

    public ChatComponentBlockName(Block block) {
        this.block = block;
    }

    @Override
    public String getID() {
        return Mods.GregTech.ID + ":ChatComponentBlockName";
    }

    @Override
    protected AbstractChatComponentCustom copySelf() {
        // Block instances are singletons; no copy needed.
        return new ChatComponentBlockName(block);
    }

    @Override
    public String getUnformattedTextForChat() {
        return block == null ? "" : block.getLocalizedName();
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeBoolean(block == null);
        if (block != null) {
            String name = Block.blockRegistry.getNameForObject(block);
            ByteBufUtils.writeUTF8String(buf, name == null ? "" : name);
        }
    }

    @Override
    public void decode(PacketBuffer buf) {
        if (buf.readBoolean()) {
            block = null;
            return;
        }
        String name = ByteBufUtils.readUTF8String(buf);
        block = name.isEmpty() ? null : Block.getBlockFromName(name);
    }
}
