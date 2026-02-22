package gregtech.common.modularui2.theme;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.JsonHelper;
import com.google.gson.JsonObject;

public class ProgressbarWidgetTheme extends WidgetTheme {

    private final UITexture emptyTexture;
    private final UITexture fullTexture;
    private final int imageSize;

    public ProgressbarWidgetTheme(UITexture emptyTexture, UITexture fullTexture, int imageSize) {
        super(0, 0, null, Color.WHITE.main, 0xFF404040, false, 0);
        this.emptyTexture = emptyTexture;
        this.fullTexture = fullTexture;
        this.imageSize = imageSize;
    }

    public ProgressbarWidgetTheme(UITexture wholeTexture, int imageSize) {
        this(wholeTexture.getSubArea(0, 0, 1, 0.5f), wholeTexture.getSubArea(0, 0.5f, 1, 1), imageSize);
    }

    public ProgressbarWidgetTheme(ProgressbarWidgetTheme parent, JsonObject json, JsonObject fallback) {
        super(parent, json, fallback);
        if (json.has("imageSize")) {
            if (json.has("wholeTexture")) {
                IDrawable wholeTexture = JsonHelper
                    .deserializeWithFallback(json, fallback, IDrawable.class, IDrawable.EMPTY, "wholeTexture");
                if (wholeTexture instanceof UITexture texture) {
                    this.emptyTexture = texture.getSubArea(0, 0, 1, 0.5f);
                    this.fullTexture = texture.getSubArea(0, 0.5f, 1, 1);
                    this.imageSize = json.get("imageSize")
                        .getAsInt();
                    return;
                }
            } else if (json.has("emptyTexture") && json.has("fullTexture")) {
                IDrawable deserializedEmptyTexture = JsonHelper
                    .deserializeWithFallback(json, fallback, IDrawable.class, IDrawable.EMPTY, "emptyTexture");
                IDrawable deserializedFullTexture = JsonHelper
                    .deserializeWithFallback(json, fallback, IDrawable.class, IDrawable.EMPTY, "fullTexture");
                if (deserializedEmptyTexture instanceof UITexture emptyUITexture
                    && deserializedFullTexture instanceof UITexture fullUITexture) {
                    this.emptyTexture = emptyUITexture;
                    this.fullTexture = fullUITexture;
                    this.imageSize = json.get("imageSize")
                        .getAsInt();
                    return;
                }
            }
        }
        this.emptyTexture = null;
        this.fullTexture = null;
        this.imageSize = 0;
    }

    public UITexture getEmptyTexture() {
        return emptyTexture;
    }

    public UITexture getFullTexture() {
        return fullTexture;
    }

    public int getImageSize() {
        return imageSize;
    }

    @Override
    public WidgetTheme withNoHoverBackground() {
        return this;
    }
}
