package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumberCompact;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gtnhlanth.common.beamline.Particle.getParticleFromId;

import java.util.Map;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter;
import gtnhlanth.common.beamline.Particle;

public class MTEBeamCrafterGui extends MTEMultiBlockBaseGui<MTEBeamCrafter> {

    public MTEBeamCrafterGui(MTEBeamCrafter multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        Map<Integer, Integer> bufferMap = multiblock.getBufferMap();
        for (Integer key : bufferMap.keySet()) {
            syncManager.syncValue("particleID" + key, new IntSyncValue(() -> key));
        }
        for (Integer key : bufferMap.keySet()) {
            syncManager.syncValue(
                "valueID" + key,
                new IntSyncValue(() -> multiblock.bufferMap.get(key), i -> multiblock.bufferMap.put(key, i)));
        }

        syncManager.syncValue("currentRecipeParticleIDA", new IntSyncValue(multiblock::getCurrentRecipeParticleIDA));
        syncManager.syncValue("currentRecipeParticleIDB", new IntSyncValue(multiblock::getCurrentRecipeParticleIDB));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        Map<Integer, Integer> bufferMap = multiblock.getBufferMap();
        ListWidget<IWidget, ?> outputWidget = new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        IKey guiHeaderKeyCrafting = IKey.dynamic(this::formatGuiHeaderCrafting);
        IKey guiHeaderKeyBuffer = IKey.dynamic(this::formatGuiHeaderBuffer);

        IntSyncValue syncIDA = syncManager.findSyncHandler("currentRecipeParticleIDA", IntSyncValue.class);
        IntSyncValue syncIDB = syncManager.findSyncHandler("currentRecipeParticleIDB", IntSyncValue.class);

        outputWidget.child(new TextWidget<>(guiHeaderKeyCrafting).marginBottom(4));
        IKey particleKeyA = IKey.dynamic(() -> EnumChatFormatting.AQUA + getParticleNameFromID(syncIDA.getIntValue()));
        IKey particleKeyB = IKey.dynamic(() -> EnumChatFormatting.AQUA + getParticleNameFromID(syncIDB.getIntValue()));
        outputWidget.child(new TextWidget<>(particleKeyA));
        outputWidget.child(new TextWidget<>(particleKeyB));

        outputWidget.child(
            new TextWidget<>(guiHeaderKeyBuffer).marginTop(4)
                .marginBottom(4));

        for (Integer key : bufferMap.keySet()) {

            IntSyncValue valueSync = syncManager.findSyncHandler("valueID" + key, IntSyncValue.class);

            IKey particleKey = IKey.dynamic(
                () -> EnumChatFormatting.WHITE + getParticleNameFromID(key)
                    + ": "
                    + formatNumberCompact(valueSync.getValue()));

            outputWidget.child(
                new IDrawable.DrawableWidget(getParticleTexture(key)).setEnabledIf(w -> valueSync.getIntValue() > 0));
            outputWidget.child(new TextWidget<>(particleKey).setEnabledIf(w -> valueSync.getIntValue() > 0));
        }

        return outputWidget;
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager.syncedPanel(
            "statsPanel",
            true,
            (p_syncManager, syncHandler) -> openInfoPanel(p_syncManager, parent, syncManager));
        return new ButtonWidget<>().size(18, 18)
            .topRel(0)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/cyclic"))
            .onMousePressed(d -> {
                if (!statsPanel.isPanelOpen()) {
                    statsPanel.openPanel();
                } else {
                    statsPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.machineinfo")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openInfoPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        return new ModularPanel("statsPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .width(110)
            .height(160)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new Column().size(100, 120)
                            .paddingLeft(20)
                            .child(
                                new TextWidget<>(
                                    IKey.dynamic(
                                        () -> StatCollector.translateToLocalFormatted(
                                            "gt.blockmachines.multimachine.beamcrafting.beamcrafter.dumpbuffer")))
                                                .size(100, 20)
                                                .alignment(Alignment.CENTER))
                            .child(createParticleButtonGrid(syncManager))));
    }

    private IWidget createParticleButtonGrid(PanelSyncManager syncManager) {

        Column column = new Column();

        Particle[] particles = Particle.values();
        int index = 0;

        for (int row = 0; row < 4; row++) {
            Row r = (Row) new Row().size(100, 20);
            for (int col = 0; col < 5; col++) {
                if (index >= particles.length) break;

                Particle particle = particles[index++];

                r.child(createButtonForParticle(syncManager, particle))
                    .marginRight(2);
            }

            column.child(r)
                .marginBottom(2);
        }

        return column;
    }

    protected IWidget createButtonForParticle(PanelSyncManager syncManager, Particle particle) {
        return new ButtonWidget<>().size(18, 18)
            .topRel(0)
            .overlay(particle.getTexture())
            .onMousePressed(d -> {
                IntSyncValue valueSync = syncManager.findSyncHandler("valueID" + particle.getId(), IntSyncValue.class);
                // int id = particle.getId();
                valueSync.setIntValue(0);
                // multiblock.setBufferToZeroForParticle(particle.getId());
                // System.out.println("particle" + particle.getId() + "set to 0:" + valueSync.getIntValue());
                return true;
            });
    }

    private String formatGuiHeaderBuffer() {
        return EnumChatFormatting.GOLD
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheaderbuffer");
    }

    private String formatGuiHeaderCrafting() {
        return EnumChatFormatting.GOLD
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheadercrafting");
    }

    private String getParticleNameFromID(int particleID) {
        return getParticleFromId(particleID).getLocalisedName();
    }

    private UITexture getParticleTexture(int particleID) {
        return getParticleFromId(particleID).getTexture();
    }
}
