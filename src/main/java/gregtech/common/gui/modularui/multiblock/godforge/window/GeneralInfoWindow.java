package gregtech.common.gui.modularui.multiblock.godforge.window;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.api.modularui2.GTGuiTextures;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;

public class GeneralInfoWindow {

    private static final int SIZE = 300;
    private static final int OFFSET_SIZE = 280;

    public static ModularPanel openPanel(MTEForgeOfGods multiblock) {
        ModularPanel panel = new ModularPanel("fogGeneralInfoPanel").size(SIZE)
            .padding(10, 0, 10, 0)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground();

        ListWidget<IWidget, ?> textList = new ListWidget<>().size(OFFSET_SIZE);
        textList.child(createHeader("gt.blockmachines.multimachine.FOG.introduction"));
        textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.introductioninfotext"));

        textList.child(createTableOfContentsHeader());
        textList.child(createTableOfContentsEntry(textList, "gt.blockmachines.multimachine.FOG.fuel", 150));
        textList.child(createTableOfContentsEntry(textList, "gt.blockmachines.multimachine.FOG.modules", 400));
        textList.child(createTableOfContentsEntry(textList, "gt.blockmachines.multimachine.FOG.upgrades", 965));
        textList.child(createTableOfContentsEntry(textList, "gt.blockmachines.multimachine.FOG.milestones", 1260));
        textList.childIf(multiblock::isInversionAvailable, createTableOfContentsEntryInversion(textList));

        textList.child(createHeader("gt.blockmachines.multimachine.FOG.fuel"));
        textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.fuelinfotext"));
        textList.child(createHeader("gt.blockmachines.multimachine.FOG.modules"));
        textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.moduleinfotext"));
        textList.child(createHeader("gt.blockmachines.multimachine.FOG.upgrades"));
        textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.upgradeinfotext"));
        textList.child(createHeader("gt.blockmachines.multimachine.FOG.milestones"));
        textList.child(createTextEntry("gt.blockmachines.multimachine.FOG.milestoneinfotext"));
        textList.childIf(multiblock::isInversionAvailable, createHeaderInversion());
        textList.childIf(
            multiblock::isInversionAvailable,
            createTextEntry("gt.blockmachines.multimachine.FOG.inversioninfotext"));

        panel.child(textList);
        return panel;
    }

    public static void registerSyncValues(PanelSyncManager syncManager) {

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

    private static ParentWidget<?> createTableOfContentsEntry(ListWidget<IWidget, ?> textList, String langKey,
        int jumpPoint) {
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
                            .animateTo(textList.getScrollArea(), jumpPoint);
                        return true;
                    }));
    }

    // todo check on inversion, make sure it works right
    private static ParentWidget<?> createTableOfContentsEntryInversion(ListWidget<IWidget, ?> textList) {
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
                            .animateTo(textList.getScrollArea(), 1766);
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
