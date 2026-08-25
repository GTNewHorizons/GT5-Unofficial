package gregtech.crossmod.ae2;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.gtnewhorizon.gtnhlib.chat.AbstractChatComponentCustom;
import com.gtnewhorizon.gtnhlib.chat.customcomponents.AbstractChatComponentBuffer;

import cpw.mods.fml.common.network.ByteBufUtils;

/**
 * Carries an item so that its display name is resolved on the client. Interface names are built on the server, where
 * client language files are not loaded, so a name resolved there would stay in the server's language.
 */
public class ChatComponentItemDisplayName extends AbstractChatComponentBuffer<ChatComponentItemDisplayName> {

    public ItemStack stack = null;
    /**
     * If true and the display name ends with a closing parenthesis, only the contents of the last pair of parentheses
     * are kept, so "Mold (Ingot)" becomes "Ingot" and the interface name stays readable.
     */
    public boolean lastParenthesesOnly = false;

    public ChatComponentItemDisplayName() {}

    public ChatComponentItemDisplayName(ItemStack stack, boolean lastParenthesesOnly) {
        this.stack = stack;
        this.lastParenthesesOnly = lastParenthesesOnly;
    }

    @Override
    public String getID() {
        return "gregtech:ChatComponentItemDisplayName";
    }

    @Override
    protected AbstractChatComponentCustom copySelf() {
        return new ChatComponentItemDisplayName(stack == null ? null : stack.copy(), lastParenthesesOnly);
    }

    @Override
    public String getUnformattedTextForChat() {
        if (stack == null) return "";
        String name = stack.getDisplayName();
        if (name == null || name.isEmpty()) return "";
        if (!lastParenthesesOnly || !name.endsWith(")")) return name;
        int open = name.lastIndexOf('(');
        if (open < 0) return name;
        String inner = name.substring(open + 1, name.length() - 1)
            .trim();
        return inner.isEmpty() ? name : inner;
    }

    @Override
    public void encode(PacketBuffer buf) {
        ByteBufUtils.writeItemStack(buf, this.stack);
        buf.writeBoolean(this.lastParenthesesOnly);
    }

    @Override
    public void decode(PacketBuffer buf) {
        this.stack = ByteBufUtils.readItemStack(buf);
        this.lastParenthesesOnly = buf.readBoolean();
    }
}
