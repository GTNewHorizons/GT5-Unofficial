package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.widget.Interactable;
import com.gtnewhorizons.modularui.api.widget.Widget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.ModularUI.GT_UITextures;
import gregtech.api.gui.ModularUI.IGT_CoverDataDependentWidget;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;

/**
 * Determines button state only by cover data.
 * @see gregtech.api.gui.ModularUI.IGT_CoverDataDependentWidget
 */
public class GT_CoverDataDependentCycleButtonWidget extends GT_CoverCycleButtonWidget
        implements IGT_CoverDataDependentWidget<GT_CoverDataDependentCycleButtonWidget> {

    private BiConsumer<ClickData, Widget> onClick;

    public GT_CoverDataDependentCycleButtonWidget() {
        super();
        setSetter(val -> {}); // fake setter; actually not used
        setSynced(false, true);
    }

    @Override
    public void draw(float partialTicks) {
        updateState();
        super.draw(partialTicks);
    }

    @Override
    public GT_CoverDataDependentCycleButtonWidget setStateGetter(Supplier<Integer> stateGetter) {
        setGetter(stateGetter::get);
        return this;
    }

    @Override
    public GT_CoverDataDependentCycleButtonWidget setOnClick(BiConsumer<ClickData, Widget> onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    protected boolean onClickImpl(int buttonId) {
        Widget.ClickData clickData = Widget.ClickData.create(buttonId, false);
        syncToServer(0, clickData::writeToPacket);
        Interactable.playButtonClickSound();
        return true;
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) {
        if (id == 0) {
            Widget.ClickData clickData = Widget.ClickData.readPacket(buf);
            onClick.accept(clickData, this);
        }
    }

    /**
     * Server update is handled by {@link #onClick}.
     */
    @SideOnly(Side.CLIENT)
    @Override
    protected void updateState() {
        setState(getter.getAsInt(), false, false);
    }

    public GT_CoverDataDependentCycleButtonWidget addCoverTooltip(String... tooltip) {
        addTooltips(Arrays.asList(tooltip));
        return this;
    }

    public static class GT_CoverDataDependentToggleButtonWidget extends GT_CoverDataDependentCycleButtonWidget {
        @Override
        protected boolean canClick() {
            return getState() == 0;
        }

        @Override
        public IDrawable[] getBackground() {
            if (!canClick()) return new IDrawable[] {GT_UITextures.BUTTON_COVER_NORMAL_DISABLED};
            return super.getBackground();
        }
    }
}
