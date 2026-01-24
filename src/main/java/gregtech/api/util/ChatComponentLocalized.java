package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gtnewhorizon.gtnhlib.chat.AbstractChatComponentCustom;

/**
 * A chat component that represents a localized message.
 *
 * @see LocSer
 */
public class ChatComponentLocalized extends AbstractChatComponentCustom {

    private LocSer localized;

    public ChatComponentLocalized() {
        this.localized = new LocSer();
    }

    public ChatComponentLocalized(LocSer localized) {
        this.localized = localized;
    }

    public ChatComponentLocalized(ItemStack aStack) {
        this(LocSer.itemStackName(aStack));
    }

    public ChatComponentLocalized(FluidStack aStack) {
        this(LocSer.fluidStackName(aStack));
    }

    @Override
    public @NotNull JsonElement serialize() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("loc", localized.encodeToBase64());
        return obj;
    }

    @Override
    public void deserialize(@NotNull JsonElement jsonElement) {
        final JsonObject obj = jsonElement.getAsJsonObject();
        final String base64 = obj.get("loc")
            .getAsString();
        this.localized = LocSer.decodeFromBase64(base64);
    }

    @Override
    public String getID() {
        return "gt:ChatComponentLocalized";
    }

    @Override
    protected AbstractChatComponentCustom copySelf() {
        return new ChatComponentLocalized(this.localized);
    }

    @Override
    public String getUnformattedTextForChat() {
        return this.localized.display();
    }
}
