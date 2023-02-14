package gregtech.common.gui.modularui.widget;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.common.widget.ExpandTab;
import gregtech.api.gui.modularui.ISidebarWidget;

public class SidebarTab extends ExpandTab implements ISidebarWidget {

    protected final String id;
    @NotNull
    protected Supplier<IDrawable> iconGetter = () -> IDrawable.EMPTY;

    public static final int DEFAULT_SIZE = 20;

    public SidebarTab(String id) {
        this.id = id;
        // default properties
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        setExpandedSize(100, 50);
        setExpandedPos(DEFAULT_SIZE - 100, 0);
        setBackground(ModularUITextures.VANILLA_BACKGROUND);
    }

    @Override
    public void onFrameUpdate() {
        super.onFrameUpdate();
        setNormalTexture(iconGetter.get());
    }

    @Override
    public void buildTooltip(List<Text> tooltip) {
        tooltip.add(new Text(getDescription()).format(EnumChatFormatting.WHITE));
        super.buildTooltip(tooltip);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal(String.format("GT5U.machines.sidebar.%s.description", id));
    }

    public SidebarTab setIcon(@NotNull Supplier<IDrawable> iconGetter) {
        this.iconGetter = iconGetter;
        return this;
    }

    public SidebarTab setIcon(IDrawable icon) {
        return setIcon(() -> icon);
    }
}
