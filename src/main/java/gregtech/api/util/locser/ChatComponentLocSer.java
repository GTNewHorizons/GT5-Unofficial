package gregtech.api.util.locser;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gtnewhorizon.gtnhlib.chat.AbstractChatComponentCustom;

/**
 * A chat component that wraps an ILocSer for localization.
 *
 * @see ILocSer
 */
public class ChatComponentLocSer extends AbstractChatComponentCustom {

    private ILocSer localized;

    public ChatComponentLocSer() {
        this.localized = new LocSerError();
    }

    public ChatComponentLocSer(ILocSer localized) {
        this.localized = localized;
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
        this.localized = ILocSerManager.decodeFromBase64(base64);
    }

    @Override
    public String getID() {
        return "gt:ChatComponentLocalized";
    }

    @Override
    protected AbstractChatComponentCustom copySelf() {
        return new ChatComponentLocSer(this.localized);
    }

    @Override
    public String getUnformattedTextForChat() {
        return this.localized.localize();
    }
}
