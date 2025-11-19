package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.ColorData;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;

public class CustomStarColorPanel {

    private static final int SIZE = 200;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.CUSTOM_STAR_COLOR);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Title
        panel.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal("fog.cosmetics.starcolor"))
                .alignment(Alignment.CENTER)
                .asWidget()
                .align(Alignment.TopCenter)
                .marginTop(9));

        Flow mainColumn = new Column().coverChildren()
            .marginTop(23)
            .alignX(0.5f);

        // Color rows
        ColorData colorData = new ColorData();

        PagedWidget.Controller controller = new PagedWidget.Controller();

        mainColumn.child(
            new PagedWidget<>().coverChildren()
                .marginBottom(5)
                .expanded()
                .controller(controller)
                .addPage(CustomStarColorSelector.createStarColorRGBPage(colorData))
                .addPage(CustomStarColorSelector.createStarColorHSVPage(colorData)));

        // Color preview
        mainColumn.child(CustomStarColorSelector.createColorPreviewRow(controller, colorData));

        panel.child(mainColumn);
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {

    }

    private static ForgeOfGodsStarColor getClickedStarColor(SyncHypervisor hypervisor) {
        IntSyncValue starColorClickedSyncer = SyncValues.STAR_COLOR_CLICKED
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
        return hypervisor.getData()
            .getStarColors()
            .getByIndex(starColorClickedSyncer.getIntValue());
    }
}
