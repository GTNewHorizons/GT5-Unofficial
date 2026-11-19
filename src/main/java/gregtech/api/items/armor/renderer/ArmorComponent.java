package gregtech.api.items.armor.renderer;

import java.awt.image.BufferedImage;

public final class ArmorComponent {

    public final String componentId;
    public final BakedArmorModel model;
    public final BufferedImage texture;

    public ArmorComponent(String componentId, BakedArmorModel model, BufferedImage texture) {
        this.componentId = componentId;
        this.model = model;
        this.texture = texture;
    }
}
