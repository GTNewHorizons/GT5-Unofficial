package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;

public class SpecialThanksPanel {

    private static final int SIZE = 200;
    private static final int BACKGROUND_SIZE = 100;
    private static final int LIST_OFFSET = 30;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.SPECIAL_THANKS);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_RAINBOW)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Background symbol
        panel.child(
            GTGuiTextures.PICTURE_GODFORGE_THANKS.asWidget()
                .size(BACKGROUND_SIZE)
                .align(Alignment.CENTER));

        // Title
        panel.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.contributors")
                .style(EnumChatFormatting.GOLD)
                .asWidget()
                .marginTop(7)
                .align(Alignment.TopCenter));

        // Credits sections
        Flow creditsList = new Column().size(SIZE - LIST_OFFSET)
            .coverChildren()
            .leftRelOffset(0, 7)
            .marginTop(LIST_OFFSET);

        creditsList.child(
            createCreditsSection(
                "gt.blockmachines.multimachine.FOG.lead",
                createCreditsEntry(
                    EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.multimachine.FOG.cloud"))));

        creditsList.child(
            createCreditsSection(
                "gt.blockmachines.multimachine.FOG.programming",
                createCreditsEntry(translateToLocal("gt.blockmachines.multimachine.FOG.serenibyss")),
                createCreditsEntry(
                    EnumChatFormatting.DARK_AQUA + translateToLocal("gt.blockmachines.multimachine.FOG.teg"))));

        creditsList.child(
            createCreditsSection(
                "gt.blockmachines.multimachine.FOG.textures",
                createCreditsEntry(
                    EnumChatFormatting.GREEN + translateToLocal("gt.blockmachines.multimachine.FOG.ant"))));

        creditsList.child(
            createCreditsSection(
                "gt.blockmachines.multimachine.FOG.rendering",
                createCreditsEntry(
                    EnumChatFormatting.WHITE + translateToLocal("gt.blockmachines.multimachine.FOG.bucket"))));

        creditsList.child(createCreditsSection("gt.blockmachines.multimachine.FOG.lore", createDelenoName()));

        creditsList.child(
            createCreditsSection(
                "gt.blockmachines.multimachine.FOG.playtesting",
                createCreditsEntry(translateToLocal("gt.blockmachines.multimachine.FOG.misi"), 0xFFC26F)));

        panel.child(creditsList);

        // Corner message
        panel.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.thanks")
                .style(EnumChatFormatting.ITALIC)
                .alignment(Alignment.CENTER)
                .color(0xBBBDBD)
                .scale(0.8f)
                .asWidget()
                .size(100, 60)
                .marginRight(10)
                .align(Alignment.BottomRight));

        return panel;
    }

    private static Flow createCreditsSection(String titleKey, Widget<?>... entries) {
        return new Column().coverChildren()
            .marginBottom(5)
            .alignX(0)
            .child(
                IKey.lang(titleKey)
                    .style(EnumChatFormatting.GOLD, EnumChatFormatting.UNDERLINE)
                    .alignment(Alignment.CenterLeft)
                    .scale(0.8f)
                    .asWidget()
                    .marginBottom(2)
                    .alignX(0))
            .children(Arrays.asList(entries));
    }

    private static TextWidget<?> createCreditsEntry(String name) {
        return createCreditsEntry(name, -1);
    }

    private static TextWidget<?> createCreditsEntry(String name, int customColor) {
        IKey key = IKey.str(name)
            .alignment(Alignment.CenterLeft)
            .scale(0.8f);
        if (customColor != -1) {
            key.color(customColor);
        }
        return key.asWidget()
            .alignX(0);
    }

    private static Flow createDelenoName() {
        Flow flow = new Row().coverChildren()
            .alignX(0);

        String name = translateToLocal("gt.blockmachines.multimachine.FOG.deleno");
        int[] colors = new int[] { 0xFFFFFF, 0xF6FFF5, 0xECFFEC, 0xE3FFE2, 0xD9FFD9, 0xD0FFCF };
        for (int i = 0; i < name.length(); i++) {
            flow.child(
                IKey.str(Character.toString(name.charAt(i)))
                    .alignment(Alignment.CenterLeft)
                    .color(colors[i])
                    .scale(0.8f)
                    .asWidget());
        }
        return flow;
    }
}
