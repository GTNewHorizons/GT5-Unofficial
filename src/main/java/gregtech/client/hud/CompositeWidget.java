package gregtech.client.hud;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.elements.WidgetElement;

@SideOnly(Side.CLIENT)
public class CompositeWidget {

    public int x, y;
    public final List<WidgetElement<?>> elements = new ArrayList<>();
    private boolean isDeletable, isConfigurable;

    public enum HudAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public CompositeWidget(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CompositeWidget addElement(WidgetElement<?> e) {
        elements.add(e);
        e.updateLayout();
        return this;
    }

    public void render(Minecraft mc, FontRenderer fr, double mouseX, double mouseY) {
        elements.forEach(e -> e.render(mc, fr, x, y, mouseX, mouseY));
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public CompositeWidget isDeletable() {
        this.isDeletable = true;
        return this;
    }

    public CompositeWidget isConfigurable() {
        this.isConfigurable = true;
        return this;
    }

    public boolean getIsDeletable() {
        return isDeletable;
    }

    public boolean getIsConfigurable() {
        return isConfigurable;
    }

    public void removeSelf() {
        HUDManager.getInstance()
            .removeWidget(this);
    }

    public void updateLayouts() {
        elements.forEach(WidgetElement::updateLayout);
    }
}
