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

        DynamicSyncHandler handler = new DynamicSyncHandler().widgetProvider((_, _) -> {
            ListWidget<IWidget, ?> textList = new ListWidget<>().size(OFFSET_SIZE);
            textList.child(createHeader("gt.blockmachines.multimachine.FOG.introduction"));
            textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.introductioninfotext"));

            // spotless:off
            TextWidget<?> fuelHeader = createHeader("gt.blockmachines.multimachine.FOG.fuel");
            ButtonWidget<?> fuelToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.fuel", fuelHeader);
            TextWidget<?> fuelText1 = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext.1");
            TextWidget<?> fuelText2 = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext.2");
            TextWidget<?> fuelText3 = createFormulaEntry("gt.blockmachines.multimachine.FOG.fuelinfotext.3");
            TextWidget<?> fuelText4 = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext.4");
            TextWidget<?> fuelText5 = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext.5");
            TextWidget<?> fuelText6 = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext.6");

            TextWidget<?> moduleHeader = createHeader("gt.blockmachines.multimachine.FOG.modules");
            ButtonWidget<?> moduleToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.modules", moduleHeader);
            TextWidget<?> moduleText1 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.1");
            TextWidget<?> moduleText2 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.2");
            TextWidget<?> moduleForgeText1 = createModuleHeader("gt.blockmachines.multimachine.FOG.moduleinfotext.forge.1");
            TextWidget<?> moduleForgeText2 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.forge.2");
            TextWidget<?> moduleCoreText1 = createModuleHeader("gt.blockmachines.multimachine.FOG.moduleinfotext.core.1");
            TextWidget<?> moduleCoreText2 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.core.2");
            TextWidget<?> moduleFabText1 = createModuleHeader("gt.blockmachines.multimachine.FOG.moduleinfotext.fab.1");
            TextWidget<?> moduleFabText2 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.fab.2");
            TextWidget<?> moduleExoticText1 = createModuleHeader("gt.blockmachines.multimachine.FOG.moduleinfotext.exotic.1");
            TextWidget<?> moduleExoticText2 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.exotic.2");
            TextWidget<?> moduleExoticText3 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.exotic.3");
            TextWidget<?> moduleExoticText4 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.exotic.4");
            TextWidget<?> moduleExoticText5 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.exotic.5");
            TextWidget<?> moduleExoticText6 = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext.exotic.6");

            TextWidget<?> upgradeHeader = createHeader("gt.blockmachines.multimachine.FOG.upgrades");
            ButtonWidget<?> upgradeToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.upgrades", upgradeHeader);
            TextWidget<?> upgradeText1 = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext.1");
            TextWidget<?> upgradeText2 = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext.2");
            TextWidget<?> upgradeText3 = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext.3");
            TextWidget<?> upgradeText4 = createFormulaEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext.4");
            TextWidget<?> upgradeText5 = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext.5");
            TextWidget<?> upgradeText6 = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext.6");

            TextWidget<?> milestoneHeader = createHeader("gt.blockmachines.multimachine.FOG.milestones");
            ButtonWidget<?> milestoneToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.milestones", milestoneHeader);
            TextWidget<?> milestoneText1 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.1");
            TextWidget<?> milestoneText2 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.2");
            TextWidget<?> milestoneText3 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.3");
            TextWidget<?> milestoneText4 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.4");
            TextWidget<?> milestoneText5 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.5");
            TextWidget<?> milestoneText6 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.6");
            TextWidget<?> milestoneText7 = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext.7");

            TextWidget<?> inversionHeader = createHeaderInversion();
            ButtonWidget<?> inversionToC = createToCEntryInversion(textList, inversionHeader);
            TextWidget<?> inversionText1 = createTextEntry("gt.blockmachines.multimachine.FOG.inversioninfotext.1");
            TextWidget<?> inversionText2 = createTextEntry("gt.blockmachines.multimachine.FOG.inversioninfotext.2");
            TextWidget<?> inversionText3 = createTextEntry("gt.blockmachines.multimachine.FOG.inversioninfotext.3");
            // spotless:on

            textList.child(createTableOfContentsHeader());
            textList.child(fuelToC);
            textList.child(moduleToC);
            textList.child(upgradeToC);
            textList.child(milestoneToC);
            textList.childIf(inversionSyncer.getBoolValue(), () -> inversionToC);

            textList.child(fuelHeader);
            textList.child(fuelText1);
            textList.child(fuelText2);
            textList.child(fuelText3);
            textList.child(fuelText4);
            textList.child(fuelText5);
            textList.child(fuelText6);

            textList.child(moduleHeader);
            textList.child(moduleText1);
            textList.child(moduleText2);
            textList.child(moduleForgeText1);
            textList.child(moduleForgeText2);
            textList.child(moduleCoreText1);
            textList.child(moduleCoreText2);
            textList.child(moduleFabText1);
            textList.child(moduleFabText2);
            textList.child(moduleExoticText1);
            textList.child(moduleExoticText2);
            textList.child(moduleExoticText3);
            textList.child(moduleExoticText4);
            textList.child(moduleExoticText5);
            textList.child(moduleExoticText6);

            textList.child(upgradeHeader);
            textList.child(upgradeText1);
            textList.child(upgradeText2);
            textList.child(upgradeText3);
            textList.child(upgradeText4);
            textList.child(upgradeText5);
            textList.child(upgradeText6);

            textList.child(milestoneHeader);
            textList.child(milestoneText1);
            textList.child(milestoneText2);
            textList.child(milestoneText3);
            textList.child(milestoneText4);
            textList.child(milestoneText5);
            textList.child(milestoneText6);
            textList.childIf(!inversionSyncer.getBoolValue(), () -> milestoneText7);

            textList.childIf(inversionSyncer.getBoolValue(), () -> inversionHeader);
            textList.childIf(inversionSyncer.getBoolValue(), () -> inversionText1);
            textList.childIf(inversionSyncer.getBoolValue(), () -> inversionText2);
            textList.childIf(inversionSyncer.getBoolValue(), () -> inversionText3);

            return textList;
        })
            .allowC2S();

        inversionSyncer.setChangeListener(() -> handler.notifyUpdate(_ -> {}));

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
            .horizontalCenter()
            .marginTop(8)
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

    private static TextWidget<?> createFormulaEntry(String langKey) {
        return IKey.lang(langKey)
            .style(EnumChatFormatting.GREEN)
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(OFFSET_SIZE - 8)
            .marginBottom(8)
            .marginRight(8);
    }

    private static TextWidget<?> createModuleHeader(String langKey) {
        return IKey.lang(langKey)
            .style(EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.BOLD)
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
            .onMousePressed(_ -> {
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
            .onMousePressed(_ -> {
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
            .horizontalCenter()
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
