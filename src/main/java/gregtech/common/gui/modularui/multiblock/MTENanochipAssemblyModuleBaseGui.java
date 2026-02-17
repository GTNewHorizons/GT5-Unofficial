package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;

public class MTENanochipAssemblyModuleBaseGui<T extends MTENanochipAssemblyModuleBase<?>>
    extends MTEMultiBlockBaseGui<T> {

    public MTENanochipAssemblyModuleBaseGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue connected = new BooleanSyncValue(this.multiblock::isConnected);
        syncManager.syncValue("connected", connected);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue connected = syncManager.findSyncHandler("connected", BooleanSyncValue.class);
        ListWidget<IWidget, ?> widget = new ListWidget<>().crossAxisAlignment(Alignment.CrossAxis.START);
        widget.child(
            IKey.dynamic(
                () -> connected.getBoolValue()
                    ? EnumChatFormatting.GREEN + translateToLocal("GT5U.gui.text.nac.module.status.connected")
                    : EnumChatFormatting.RED + translateToLocal("GT5U.gui.text.nac.module.status.dis_connected"))
                .asWidget());
        super.createTerminalTextWidget(syncManager, parent).getChildren()
            .forEach(widget::child);

        return widget;
    }

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler infoPanel = syncManager
            .syncedPanel("infoPanel", true, (p_syncManager, syncHandler) -> openInfoPanel(parent));

        return new ButtonWidget<>().size(24)
            .marginTop(4)
            .overlay(IDrawable.EMPTY)
            .tooltip(t -> t.addLine(translateToLocal("GT5U.gui.text.nac.info.open")))
            .background(GTGuiTextures.PICTURE_NANOCHIP_LOGO)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!infoPanel.isPanelOpen()) {
                    infoPanel.openPanel();
                } else {
                    infoPanel.closePanel();
                }
                return true;
            });
    }

    private ModularPanel openInfoPanel(ModularPanel parent) {
        ModularPanel panel = new ModularPanel("infoPanel").relative(parent)
            .size(SIZE)
            .background(GTGuiTextures.BACKGROUND_NANOCHIP)
            .padding(10);
        ListWidget<IWidget, ?> textList = new ListWidget<>().size(OFFSET_SIZE);
        textList.child(createHeader("GT5U.gui.text.nac.info.introduction.header"));
        textList.child(createTextEntry("GT5U.gui.text.nac.info.introduction.body.1"));
        TextWidget<?> vacuumBasicsHeader = createHeader("GT5U.gui.text.nac.info.nac_basics.header");
        ButtonWidget<?> vacuumBasicsToC = createToCEntry(
            textList,
            "GT5U.gui.text.nac.info.nac_basics.header",
            vacuumBasicsHeader);

        TextWidget<?> vacuumBasicsBody1 = createTextEntry("GT5U.gui.text.nac.info.nac_basics.body.1");
        TextWidget<?> vacuumBasicsBody2 = createTextEntry("GT5U.gui.text.nac.info.nac_basics.body.2");
        TextWidget<?> vacuumBasicsBody3 = createTextEntry("GT5U.gui.text.nac.info.nac_basics.body.3");
        TextWidget<?> moduleBasicsHeader = createHeader("GT5U.gui.text.nac.info.module_basics.header");
        ButtonWidget<?> moduleBasicsToC = createToCEntry(
            textList,
            "GT5U.gui.text.nac.info.module_basics.header",
            moduleBasicsHeader);
        TextWidget<?> moduleBasicsBody1 = createTextEntry("GT5U.gui.text.nac.info.module_basics.body.1");
        TextWidget<?> moduleBasicsBody2 = createTextEntry("GT5U.gui.text.nac.info.module_basics.body.2");
        TextWidget<?> moduleBasicsBody3 = createTextEntry("GT5U.gui.text.nac.info.module_basics.body.3");
        TextWidget<?> moduleBasicsBody4 = createTextEntry("GT5U.gui.text.nac.info.module_basics.body.4");
        TextWidget<?> moduleBasicsBody5 = createTextEntry("GT5U.gui.text.nac.info.module_basics.body.5");
        TextWidget<?> calibrationHeader = createHeader("GT5U.gui.text.nac.info.calibration.header");
        ButtonWidget<?> calibrationToC = createToCEntry(
            textList,
            "GT5U.gui.text.nac.info.calibration.header",
            calibrationHeader);
        TextWidget<?> calibrationBody1 = createTextEntry("GT5U.gui.text.nac.info.calibration.body.1");
        TextWidget<?> calibrationBody2 = createTextEntry("GT5U.gui.text.nac.info.calibration.body.2");

        TextWidget<?> crystalHeader = createCalibrationSubHeader("GT5U.gui.text.nac.info.calibration.crystal.header");
        TextWidget<?> crystalBody1 = createTextEntry("GT5U.gui.text.nac.info.calibration.crystal.effect1");
        TextWidget<?> crystalBody2 = createTextEntry("GT5U.gui.text.nac.info.calibration.crystal.effect2");
        TextWidget<?> crystalBody3 = createTextEntry("GT5U.gui.text.nac.info.calibration.crystal.effect3");
        TextWidget<?> wetwareHeader = createCalibrationSubHeader("GT5U.gui.text.nac.info.calibration.wetware.header");
        TextWidget<?> wetwareBody1 = createTextEntry("GT5U.gui.text.nac.info.calibration.wetware.effect1");
        TextWidget<?> wetwareBody2 = createTextEntry("GT5U.gui.text.nac.info.calibration.wetware.effect2");
        TextWidget<?> wetwareBody3 = createTextEntry("GT5U.gui.text.nac.info.calibration.wetware.effect3");
        TextWidget<?> biowareHeader = createCalibrationSubHeader("GT5U.gui.text.nac.info.calibration.optical.header");
        TextWidget<?> biowareBody1 = createTextEntry("GT5U.gui.text.nac.info.calibration.bioware.effect1");
        TextWidget<?> biowareBody2 = createTextEntry("GT5U.gui.text.nac.info.calibration.bioware.effect2");
        TextWidget<?> biowareBody3 = createTextEntry("GT5U.gui.text.nac.info.calibration.bioware.effect3");
        TextWidget<?> opticalHeader = createCalibrationSubHeader("GT5U.gui.text.nac.info.calibration.optical.header");
        TextWidget<?> opticalBody1 = createTextEntry("GT5U.gui.text.nac.info.calibration.optical.effect1");
        TextWidget<?> opticalBody2 = createTextEntry("GT5U.gui.text.nac.info.calibration.optical.effect2");
        TextWidget<?> opticalBody3 = createTextEntry("GT5U.gui.text.nac.info.calibration.optical.effect3");
        TextWidget<?> specialHeader = createCalibrationSubHeader("GT5U.gui.text.nac.info.calibration.special.header");
        TextWidget<?> specialBody1 = createTextEntry("GT5U.gui.text.nac.info.calibration.special.effect1");
        TextWidget<?> specialBody2 = createTextEntry("GT5U.gui.text.nac.info.calibration.special.effect2");
        TextWidget<?> specialBody3 = createTextEntry("GT5U.gui.text.nac.info.calibration.special.effect3");

        textList.child(createTableOfContentsHeader());
        textList.child(vacuumBasicsToC);
        textList.child(moduleBasicsToC);
        textList.child(calibrationToC);
        textList.child(vacuumBasicsHeader);
        textList.child(vacuumBasicsBody1);
        textList.child(vacuumBasicsBody2);
        textList.child(vacuumBasicsBody3);
        textList.child(moduleBasicsHeader);
        textList.child(moduleBasicsBody1);
        textList.child(moduleBasicsBody2);
        textList.child(moduleBasicsBody3);
        textList.child(moduleBasicsBody4);
        textList.child(moduleBasicsBody5);
        textList.child(calibrationHeader);
        textList.child(calibrationBody1);
        textList.child(calibrationBody2);

        textList.child(crystalHeader);
        textList.child(crystalBody1);
        textList.child(crystalBody2);
        textList.child(crystalBody3);

        textList.child(wetwareHeader);
        textList.child(wetwareBody1);
        textList.child(wetwareBody2);
        textList.child(wetwareBody3);

        textList.child(biowareHeader);
        textList.child(biowareBody1);
        textList.child(biowareBody2);
        textList.child(biowareBody3);

        textList.child(opticalHeader);
        textList.child(opticalBody1);
        textList.child(opticalBody2);
        textList.child(opticalBody3);

        textList.child(specialHeader);
        textList.child(specialBody1);
        textList.child(specialBody2);
        textList.child(specialBody3);

        panel.child(textList);
        return panel;
    }

    private static final int SIZE = 300;
    private static final int OFFSET_SIZE = SIZE - 20;

    private static TextWidget<?> createCalibrationSubHeader(String langKey) {
        return IKey.lang(langKey)
            .style(EnumChatFormatting.BOLD, EnumChatFormatting.UNDERLINE)
            .color(0xFFFFFFFF)
            .asWidget()
            .alignX(Alignment.CenterLeft)
            .marginBottom(8);
    }

    private static TextWidget<?> createHeader(String langKey) {
        return IKey.lang(langKey)
            .style(EnumChatFormatting.BOLD, EnumChatFormatting.UNDERLINE)
            .color(0xFFCE4242)
            .asWidget()
            .alignX(Alignment.CENTER)
            .marginBottom(8);
    }

    private static TextWidget<?> createTextEntry(String langKey) {
        return IKey.lang(langKey)
            .color(0xFFDBE0)
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(OFFSET_SIZE - 12)
            .marginBottom(8);
    }

    private static TextWidget<?> createTableOfContentsHeader() {
        return IKey.lang("GT5U.gui.text.nac.info.toc")
            .style(EnumChatFormatting.BOLD)
            .color(0xFFD25A7E)
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(OFFSET_SIZE)
            .marginBottom(8);
    }

    private static ButtonWidget<?> createToCEntry(ListWidget<IWidget, ?> textList, String langKey,
        TextWidget<?> jumpPoint) {
        return new ButtonWidget<>().width(OFFSET_SIZE)
            .background(IDrawable.EMPTY)
            .overlay(
                IKey.lang(langKey)
                    .color(0xFFDBE0)
                    .style(EnumChatFormatting.BOLD)
                    .alignment(Alignment.CenterLeft))
            .disableHoverBackground()
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .onMousePressed(d -> {
                textList.getScrollData()
                    .animateTo(
                        textList.getScrollArea(),
                        jumpPoint.getArea()
                            .getRelativePoint(GuiAxis.Y));
                return true;
            });
    }

}
