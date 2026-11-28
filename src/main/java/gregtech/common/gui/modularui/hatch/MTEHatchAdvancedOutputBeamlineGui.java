package gregtech.common.gui.modularui.hatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericMapSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEHatchAdvancedOutputBeamline;
import gtnhlanth.common.beamline.Particle;

public class MTEHatchAdvancedOutputBeamlineGui extends MTEHatchBaseGui<MTEHatchAdvancedOutputBeamline> {

    private static final int BUTTONS_PER_ROW = 7;
    // list sync is separate, so that the dynamic widget don't need to refresh if the list is unchanged.
    // Also avoids potential map reordering.
    private GenericListSyncHandler<Particle> listSyncHandler;
    private GenericMapSyncHandler<Particle, Boolean> mapSyncHandler;

    public MTEHatchAdvancedOutputBeamlineGui(MTEHatchAdvancedOutputBeamline base) {
        super(base);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        listSyncHandler = new GenericListSyncHandler.Builder<Particle>().getter(machine::getParticleList)
            .adapter(new ParticleAdapter())
            .build();
        syncManager.syncValue("inputList", listSyncHandler);
        mapSyncHandler = new GenericMapSyncHandler.Builder<Particle, Boolean>().getter(machine::getParticleMap)
            .setter(machine::setAcceptedInputMap)
            .valueAdapter(ByteBufAdapters.BOOL)
            .keyAdapter(new ParticleAdapter())
            .build()
            .allowC2S();
        syncManager.syncValue("inputMap", mapSyncHandler);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            Flow.column()
                .coverChildren()
                .childPadding(4)
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .child(
                    IKey.lang("GT5U.gui.text.BeamlineHatch.blacklist")
                        .asWidget())
                .child(createBlacklistWidget())
                .setEnabledIf(
                    widget -> !listSyncHandler.getValue()
                        .isEmpty()))
            .child(
                IKey.lang("GT5U.gui.text.BeamlineHatch.emptylist")
                    .asWidget()
                    .setEnabledIf(
                        widget -> listSyncHandler.getValue()
                            .isEmpty()));
    }

    protected IWidget createBlacklistWidget() {
        DynamicSyncHandler blacklistSyncer = new DynamicSyncHandler()
            .widgetProvider(
                (syncManager, packet) -> new Grid()
                    .gridOfWidthElements(
                        BUTTONS_PER_ROW,
                        listSyncHandler.getValue(),
                        ($x, $y, $i, particle) -> createButtonForParticle(particle))
                    .coverChildren())
            .allowC2S();
        listSyncHandler.setChangeListener(() -> blacklistSyncer.notifyUpdate(packet -> {}));
        return new DynamicSyncedWidget<>().syncHandler(blacklistSyncer)
            .coverChildren();
    }

    private Map<Particle, Boolean> copyWith(Map<Particle, Boolean> syncedMap, Particle particle, Boolean bool) {
        Map<Particle, Boolean> returnMap = new HashMap<>(syncedMap);
        returnMap.replace(particle, bool);
        return returnMap;
    }

    protected ToggleButton createButtonForParticle(Particle particle) {
        return new ToggleButton().invertSelected(true)
            .overlay(particle.getTexture())
            .value(
                new BoolValue.Dynamic(
                    () -> mapSyncHandler.getValue()
                        .getOrDefault(particle, false),
                    bool -> mapSyncHandler.setValue(copyWith(mapSyncHandler.getValue(), particle, bool))))
            .addTooltipLine(particle.getLocalisedName());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    // this serializer is here since it only really pertains to this class
    protected static class ParticleAdapter implements IByteBufAdapter<Particle> {

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
}
