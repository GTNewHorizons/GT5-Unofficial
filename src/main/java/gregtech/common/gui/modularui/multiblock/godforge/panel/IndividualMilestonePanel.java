package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Milestones;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Syncers;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;

public class IndividualMilestonePanel {

    private static final int SIZE = 150;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.INDIVIDUAL_MILESTONE);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground();

        // registered on the Milestone panel, look up from there
        EnumSyncValue<Milestones> milestoneSyncer = Syncers.MILESTONE_CLICKED.lookupFrom(Panels.MILESTONE, hypervisor);
        Milestones milestone = milestoneSyncer.getValue();

        // Background symbol
        panel.child(
            milestone.getSymbolBackground()
                .asWidget()
                .size(milestone.getSymbolWidth(), milestone.getSymbolHeight())
                .align(Alignment.CENTER));

        Flow row = new Row().coverChildren()
            .alignX(0.5f)
            .marginTop(10); // todo check

        // Header
        row.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal(milestone.getTitleLangKey()))
                .alignment(Alignment.CENTER)
                .asWidget()
                .alignX(0.5f));

        panel.child(ForgeOfGodsGuiUtil.panelCloseButton());
        return panel;
    }

    public static void registerSyncValues(PanelSyncManager syncManager) {

    }
}
