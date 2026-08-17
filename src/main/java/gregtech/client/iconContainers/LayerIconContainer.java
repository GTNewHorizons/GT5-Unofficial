package gregtech.client.iconContainers;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.interfaces.IIconContainer;

/// One layer of another container's icon stack, presented as a container of its own so a single texture draws that
/// layer alone. Reports no overlay: a stack's trailing `_OVERLAY` is a layer like any other here.
public final class LayerIconContainer implements IIconContainer {

    private final IIconContainer container;
    private final int layer;

    public LayerIconContainer(IIconContainer container, int layer) {
        this.container = container;
        this.layer = layer;
    }

    @Override
    public IIcon getIcon() {
        return container.getLayerIcon(layer);
    }

    @Override
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return container.getTextureFile();
    }

    @Override
    public int getRenderIconPass() {
        return container.getRenderIconPass();
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return container.canRenderInPass(pass);
    }

    @Override
    public boolean hasOverrideIcon() {
        return container.hasOverrideIcon();
    }
}
