package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public class GeneralInfoPanel {

    private static final int SIZE = 300;
    private static final int OFFSET_SIZE = 280;

    public static ModularPanel openModulePanel(SyncHypervisor hypervisor, Modules<?> module) {
        ModularPanel panel = hypervisor.getModularPanel(module, Panels.GENERAL_INFO);

        registerSyncValues(module, hypervisor);

        panel.size(SIZE)
            .padding(10, 0, 10, 0)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        BooleanSyncValue inversionSyncer = SyncValues.INVERSION.lookupFrom(module, Panels.GENERAL_INFO, hypervisor);

        DynamicSyncHandler handler = new DynamicSyncHandler().widgetProvider(($, $$) -> {
            ListWidget<IWidget, ?> textList = new ListWidget<>().size(OFFSET_SIZE);
            textList.child(createHeader("gt.blockmachines.multimachine.FOG.introduction"));
            textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.introductioninfotext"));

            TextWidget<?> fuelHeader = createHeader("gt.blockmachines.multimachine.FOG.fuel");
            TextWidget<?> fuelText = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext");
            ButtonWidget<?> fuelToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.fuel", fuelHeader);

            TextWidget<?> moduleHeader = createHeader("gt.blockmachines.multimachine.FOG.modules");
            TextWidget<?> moduleText = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext");
            ButtonWidget<?> moduleToC = createToCEntry(
                textList,
                "gt.blockmachines.multimachine.FOG.modules",
                moduleHeader);

            TextWidget<?> upgradeHeader = createHeader("gt.blockmachines.multimachine.FOG.upgrades");
            TextWidget<?> upgradeText = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext");
            ButtonWidget<?> upgradeToC = createToCEntry(
                textList,
                "gt.blockmachines.multimachine.FOG.upgrades",
                upgradeHeader);

            TextWidget<?> milestoneHeader = createHeader("gt.blockmachines.multimachine.FOG.milestones");
            TextWidget<?> milestoneText = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext");
            ButtonWidget<?> milestoneToC = createToCEntry(
                textList,
                "gt.blockmachines.multimachine.FOG.milestones",
                milestoneHeader);

            TextWidget<?> inversionHeader = createHeaderInversion();
            TextWidget<?> inversionText = createTextEntry("gt.blockmachines.multimachine.FOG.inversioninfotext");
            ButtonWidget<?> inversionToC = createToCEntryInversion(textList, inversionHeader);

            textList.child(createTableOfContentsHeader());
            textList.child(fuelToC);
            textList.child(moduleToC);
            textList.child(upgradeToC);
            textList.child(milestoneToC);
            textList.childIf(inversionSyncer.getBoolValue(), ()->inversionToC);

            textList.child(fuelHeader);
            textList.child(fuelText);
            textList.child(moduleHeader);
            textList.child(moduleText);
            textList.child(upgradeHeader);
            textList.child(upgradeText);
            textList.child(milestoneHeader);
            textList.child(milestoneText);
            textList.childIf(inversionSyncer.getBoolValue(), ()->inversionHeader);
            textList.childIf(inversionSyncer.getBoolValue(), ()->inversionText);

            return textList;
        });

        inversionSyncer.setChangeListener(() -> handler.notifyUpdate($ -> {}));

        panel.child(
            new DynamicSyncedWidget<>().coverChildren()
                .syncHandler(handler));

        return panel;
    }

    private static void registerSyncValues(Modules<?> module, SyncHypervisor hypervisor) {
        SyncValues.INVERSION.registerFor(module, Panels.GENERAL_INFO, hypervisor);
    }

    private static TextWidget<?> createHeader(String langKey) {
        return IKey.lang(langKey)
            .style(EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.BOLD, EnumChatFormatting.UNDERLINE)
            .asWidget()
            .alignX(Alignment.CENTER)
            .marginBottom(8);
    }

    private static TextWidget<?> createTextEntry(String langKey) {
        return IKey.lang(langKey)
            .style(EnumChatFormatting.GOLD)
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(OFFSET_SIZE)
            .marginBottom(8);
    }

    private static TextWidget<?> createTableOfContentsHeader() {
        return IKey.lang("gt.blockmachines.multimachine.FOG.tableofcontents")
            .style(EnumChatFormatting.AQUA, EnumChatFormatting.BOLD)
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
                    .style(EnumChatFormatting.AQUA, EnumChatFormatting.BOLD)
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

    private static ButtonWidget<?> createToCEntryInversion(ListWidget<IWidget, ?> textList, TextWidget<?> jumpPoint) {
        return new ButtonWidget<>().width(OFFSET_SIZE)
            .background(IDrawable.EMPTY)
            .overlay(
                IKey.str(getInversionHeaderText())
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

    private static TextWidget<?> createHeaderInversion() {
        return IKey.str(getInversionHeaderText())
            .asWidget()
            .alignX(Alignment.CENTER)
            .marginBottom(8);
    }

    private static String getInversionHeaderText() {
        return EnumChatFormatting.DARK_GRAY + ""
            + EnumChatFormatting.BOLD
            + EnumChatFormatting.OBFUSCATED
            + "2"
            + EnumChatFormatting.RESET
            + EnumChatFormatting.WHITE
            + EnumChatFormatting.BOLD
            + translateToLocal("gt.blockmachines.multimachine.FOG.inversion")
            + EnumChatFormatting.DARK_GRAY
            + EnumChatFormatting.BOLD
            + EnumChatFormatting.OBFUSCATED
            + "2";
    }
}
