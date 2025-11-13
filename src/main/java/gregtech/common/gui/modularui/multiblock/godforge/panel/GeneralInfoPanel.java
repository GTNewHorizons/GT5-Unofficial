package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class GeneralInfoPanel {

    private static final int SIZE = 300;
    private static final int OFFSET_SIZE = 280;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.GENERAL_INFO);
        ForgeOfGodsData data = hypervisor.getData();

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .padding(10, 0, 10, 0)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground();

        ListWidget<IWidget, ?> textList = new ListWidget<>().size(OFFSET_SIZE)
            .collapseDisabledChild();
        textList.child(createHeader("gt.blockmachines.multimachine.FOG.introduction"));
        textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.introductioninfotext"));

        TextWidget<?> fuelHeader = createHeader("gt.blockmachines.multimachine.FOG.fuel");
        TextWidget<?> fuelText = createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext");
        ParentWidget<?> fuelToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.fuel", fuelHeader);

        TextWidget<?> moduleHeader = createHeader("gt.blockmachines.multimachine.FOG.modules");
        TextWidget<?> moduleText = createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext");
        ParentWidget<?> moduleToC = createToCEntry(textList, "gt.blockmachines.multimachine.FOG.modules", moduleHeader);

        TextWidget<?> upgradeHeader = createHeader("gt.blockmachines.multimachine.FOG.upgrades");
        TextWidget<?> upgradeText = createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext");
        ParentWidget<?> upgradeToC = createToCEntry(
            textList,
            "gt.blockmachines.multimachine.FOG.upgrades",
            upgradeHeader);

        TextWidget<?> milestoneHeader = createHeader("gt.blockmachines.multimachine.FOG.milestones");
        TextWidget<?> milestoneText = createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext");
        ParentWidget<?> milestoneToC = createToCEntry(
            textList,
            "gt.blockmachines.multimachine.FOG.milestones",
            milestoneHeader);

        TextWidget<?> inversionHeader = createHeaderInversion();
        TextWidget<?> inversionText = createTextEntry("gt.blockmachines.multimachine.FOG.inversioninfotext");
        ParentWidget<?> inversionToC = createToCEntryInversion(textList, inversionHeader);

        textList.child(createTableOfContentsHeader());
        textList.child(fuelToC);
        textList.child(moduleToC);
        textList.child(upgradeToC);
        textList.child(milestoneToC);
        textList.child(inversionToC.setEnabledIf($ -> data.isInversion()));

        textList.child(fuelHeader);
        textList.child(fuelText);
        textList.child(moduleHeader);
        textList.child(moduleText);
        textList.child(upgradeHeader);
        textList.child(upgradeText);
        textList.child(milestoneHeader);
        textList.child(milestoneText);
        textList.child(inversionHeader.setEnabledIf($ -> data.isInversion()));
        textList.child(inversionText.setEnabledIf($ -> data.isInversion()));

        panel.child(textList);
        panel.child(ForgeOfGodsGuiUtil.panelCloseButton());
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.INVERSION.registerFor(Panels.GENERAL_INFO, hypervisor);
    }

    private static TextWidget<?> createHeader(String langKey) {
        return IKey
            .str(
                EnumChatFormatting.DARK_PURPLE + ""
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + translateToLocal(langKey))
            .asWidget()
            .alignX(Alignment.CENTER)
            .marginBottom(8);
    }

    private static TextWidget<?> createTextEntry(String langKey) {
        return IKey.str(EnumChatFormatting.GOLD + translateToLocal(langKey))
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(OFFSET_SIZE)
            .marginBottom(8);
    }

    private static TextWidget<?> createTableOfContentsHeader() {
        return IKey
            .str(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + translateToLocal("gt.blockmachines.multimachine.FOG.tableofcontents"))
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(OFFSET_SIZE)
            .marginBottom(8);
    }

    private static ParentWidget<?> createToCEntry(ListWidget<IWidget, ?> textList, String langKey,
        TextWidget<?> jumpPoint) {
        return new ParentWidget<>().coverChildren()
            .child(
                IKey.str(EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + translateToLocal(langKey))
                    .asWidget()
                    .width(OFFSET_SIZE)
                    .marginBottom(8))
            .child(
                new ButtonWidget<>().width(OFFSET_SIZE)
                    .background(IDrawable.EMPTY)
                    .disableHoverBackground()
                    .onMousePressed(d -> {
                        textList.getScrollData()
                            .animateTo(
                                textList.getScrollArea(),
                                jumpPoint.getArea()
                                    .getRelativePoint(GuiAxis.Y));
                        return true;
                    }));
    }

    // todo check on inversion, make sure it works right
    private static ParentWidget<?> createToCEntryInversion(ListWidget<IWidget, ?> textList, TextWidget<?> jumpPoint) {
        return new ParentWidget<>().coverChildren()
            .align(Alignment.CenterLeft)
            .child(
                IKey.str(getInversionHeaderText())
                    .asWidget()
                    .width(OFFSET_SIZE)
                    .marginBottom(8))
            .child(
                new ButtonWidget<>().width(OFFSET_SIZE)
                    .background(IDrawable.EMPTY)
                    .disableHoverBackground()
                    .onMousePressed(d -> {
                        textList.getScrollData()
                            .animateTo(
                                textList.getScrollArea(),
                                jumpPoint.getArea()
                                    .getRelativePoint(GuiAxis.Y));
                        return true;
                    }));
    }

    private static TextWidget<?> createHeaderInversion() {
        return IKey.str(getInversionHeaderText())
            .asWidget()
            .alignX(Alignment.CENTER)
            .marginBottom(8);
    }

    private static String getInversionHeaderText() {
        return EnumChatFormatting.BOLD + ""
            + EnumChatFormatting.OBFUSCATED
            + "2"
            + EnumChatFormatting.RESET
            + EnumChatFormatting.WHITE
            + EnumChatFormatting.BOLD
            + translateToLocal("gt.blockmachines.multimachine.FOG.inversion")
            + EnumChatFormatting.BOLD
            + EnumChatFormatting.OBFUSCATED
            + "2";
    }
}
