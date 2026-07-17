package gregtech.api.util.tooltip.icon;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonObject;
import com.slprime.chromatictooltips.api.ITooltipComponent;
import com.slprime.chromatictooltips.api.TooltipContext;
import com.slprime.chromatictooltips.api.TooltipStyle;
import com.slprime.chromatictooltips.util.TooltipAlign;
import com.slprime.chromatictooltips.util.TooltipTexture;

public class IconComponent implements ITooltipComponent {

    protected final TooltipTexture texture;
    protected final int size;

    public IconComponent(String path, int size) {
        ensurePathExists(path);
        this.size = size;

        JsonObject json = new JsonObject();
        json.addProperty("path", path);
        this.texture = new TooltipTexture(new TooltipStyle(json));
    }

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    @Override
    public int getSpacing() {
        return 0;
    }

    @Override
    public void draw(int x, int y, int availableWidth, TooltipContext context) {
        texture.draw(x, y, size, size, TooltipAlign.START, TooltipAlign.START, 0xFFFFFFFF);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IconComponent ico)) return false;
        return Objects.equals(this.texture, ico.texture);
    }

    @Override
    public int hashCode() {
        return texture.hashCode();
    }

    private static void ensurePathExists(String resourceLocation) {
        ResourceLocation rl = new ResourceLocation(resourceLocation);

        try {
            Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(rl);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Cannot find resource located at %s", resourceLocation), e);
        }
    }
}
