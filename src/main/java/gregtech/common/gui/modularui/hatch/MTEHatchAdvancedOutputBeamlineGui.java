package gregtech.common.gui.modularui.hatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.GenericMapSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEHatchAdvancedOutputBeamline;
import gtnhlanth.common.beamline.Particle;

public class MTEHatchAdvancedOutputBeamlineGui extends MTEHatchBaseGui<MTEHatchAdvancedOutputBeamline> {

    public MTEHatchAdvancedOutputBeamlineGui(MTEHatchAdvancedOutputBeamline base) {
        super(base);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        GenericMapSyncHandler<Particle, Boolean> mapSyncHandler = new GenericMapSyncHandler.Builder<Particle, Boolean>()
            .getter(hatch::getParticleMap)
            .setter(hatch::setAcceptedInputMap)
            .valueAdapter(new ValueBooleanAdapter())
            .keyAdapter(new KeyParticleAdapter())
            .build();

        syncManager.syncValue("inputMap", mapSyncHandler);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {

        GenericMapSyncHandler<Particle, Boolean> mapSyncHandler = syncManager
            .findSyncHandler("inputMap", GenericMapSyncHandler.class);

        return super.createContentSection(panel, syncManager).child(
            Flow.column()
                .align(Alignment.START)
                .child(
                    IKey.lang("GT5U.gui.text.BeamlineHatch.blacklist")
                        .asWidget()
                        .anchorLeft(0)
                        .size(120, 18)
                        .padding(4, 0))
                .child(createBlacklistWidget(mapSyncHandler).left(4))
                .setEnabledIf(
                    widget -> !hatch.getParticleMap()
                        .isEmpty()))

            .child(
                IKey.lang("GT5U.gui.text.BeamlineHatch.emptylist")
                    .asWidget()
                    .anchorLeft(0)
                    .size(120, 18)
                    .padding(4, 16)
                    .setEnabledIf(
                        widget -> hatch.getParticleMap()
                            .isEmpty()));
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + 16;
    }

    private static final int BUTTONS_PER_ROW = 6;
    private static final int PADDED_WIDTH_PER_BUTTON = 22;
    private static final int PADDED_HEIGHT_PER_BUTTON = 20;

    protected ListWidget<IWidget, ?> createBlacklistWidget(GenericMapSyncHandler<Particle, Boolean> map) {
        ListWidget<IWidget, ?> blacklistOptions = new ListWidget<>();
        blacklistOptions.size(PADDED_WIDTH_PER_BUTTON * BUTTONS_PER_ROW, 8 + PADDED_HEIGHT_PER_BUTTON * 3)
            .crossAxisAlignment(Alignment.CrossAxis.START);
        // for every 6 particles in the allowed particle list, make a new row
        // add each row to the blacklistOptions
        final Set<Particle> particleSet = hatch.getParticleMap()
            .keySet();

        int numRows = (int) Math.ceil((double) particleSet.size() / BUTTONS_PER_ROW); // gross java math
        for (int i = 0; i < numRows; i++) {
            blacklistOptions.addChild(
                Flow.row()
                    .size((PADDED_WIDTH_PER_BUTTON - 2) * 4, 18)
                    .marginBottom(4),
                i);
        }

        int runningCount = 0; // value to determine if we've reached the breaking point of a new row

        for (Particle p : particleSet) {
            final Flow currentParticleRow = (Flow) blacklistOptions.getChildren()
                .get(runningCount / BUTTONS_PER_ROW);
            currentParticleRow.addChild(createButtonForParticle(map, p), runningCount % BUTTONS_PER_ROW);
            runningCount++;
        }

        return blacklistOptions;
    }

    private Map<Particle, Boolean> copyWith(Map<Particle, Boolean> syncedMap, Particle particle, Boolean bool) {
        Map<Particle, Boolean> returnMap = new HashMap<>(syncedMap);
        returnMap.replace(particle, bool);
        return returnMap;
    }

    protected IWidget createButtonForParticle(GenericMapSyncHandler<Particle, Boolean> mapSyncHandler,
        Particle particle) {
        return new ToggleButton().marginRight(2)
            .invertSelected(true)
            .overlay(particle.getTexture())
            .value(
                new BoolValue.Dynamic(
                    () -> mapSyncHandler.getValue()
                        .get(particle),
                    bool -> mapSyncHandler.setValue(copyWith(mapSyncHandler.getValue(), particle, bool))))
            .tooltipBuilder(t -> t.addLine(particle.getLocalisedName()));
    }

    // these serializers are here since they only really pertain to this class.
    protected static class KeyParticleAdapter implements IByteBufAdapter<Particle> {

        @Override
        public Particle deserialize(PacketBuffer buffer) throws IOException {
            return Particle.getParticleFromId(buffer.readInt());
        }

        @Override
        public void serialize(PacketBuffer buffer, Particle u) throws IOException {
            buffer.writeInt(u.getId());
        }

        @Override
        public boolean areEqual(@NotNull Particle t1, @NotNull Particle t2) {
            return t1.getId() == t2.getId();
        }
    }

    protected static class ValueBooleanAdapter implements IByteBufAdapter<Boolean> {

        @Override
        public Boolean deserialize(PacketBuffer buffer) throws IOException {
            return buffer.readBoolean();
        }

        @Override
        public void serialize(PacketBuffer buffer, Boolean u) throws IOException {
            buffer.writeBoolean(u);
        }

        @Override
        public boolean areEqual(@NotNull Boolean t1, @NotNull Boolean t2) {
            return t1.equals(t2);
        }
    }
}
