package gregtech.common.tileentities.machines.multi.LHC;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.GenericMapSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuis;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public class MTEHatchAdvancedOutputBeamline extends MTEHatchOutputBeamline {

    private final String NBT_KEY_DESCRIPTOR = "KEY"; // specifically for the Particle value in the input map, will save
                                                     // with id
    private final String NBT_VALUE_DESCRIPTOR = "VALUE"; // specifically for the Boolean value in the input map

    private boolean initialized = false;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        // save all entries in acceptedInputMap
        saveInputMapToNBT(aNBT, this.acceptedInputMap);
        aNBT.setBoolean("init", initialized);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        loadInputMapFromNBT(aNBT, this.acceptedInputMap);
        initialized = aNBT.getBoolean("init");
    }

    public void saveInputMapToNBT(NBTTagCompound aNBT, Map<Particle, Boolean> map) {
        for (Map.Entry<Particle, Boolean> entry : map.entrySet()) {
            Particle key = entry.getKey();
            boolean value = entry.getValue();
            final String particleName = key.getName();
            aNBT.setInteger(NBT_KEY_DESCRIPTOR + particleName, key.getId());
            aNBT.setBoolean(NBT_VALUE_DESCRIPTOR + particleName, value);
        }
    }

    public void loadInputMapFromNBT(NBTTagCompound aNBT, Map<Particle, Boolean> map) {
        for (Particle p : Particle.VALUES) {
            final String particleName = p.getName();
            if (!aNBT.hasKey(NBT_KEY_DESCRIPTOR + particleName)) continue;
            // assume that if key exists, value exists as well
            // can skip the key nbt mapping, as the particle is in NBT
            map.put(p, aNBT.getBoolean(NBT_VALUE_DESCRIPTOR + particleName));

        }
    }

    Map<Particle, Boolean> acceptedInputMap = new HashMap<>();

    public void setInitialParticleList(List<Particle> initialParticleList) {

        if (initialized) return;

        acceptedInputMap.clear();

        for (Particle p : initialParticleList) {
            acceptedInputMap.put(p, true);
        }
        initialized = true;

    }

    public MTEHatchAdvancedOutputBeamline(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
        // setInitialParticleList(Arrays.asList(Particle.values()));
    }

    public MTEHatchAdvancedOutputBeamline(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        // setInitialParticleList(Arrays.asList(Particle.values()));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEHatchAdvancedOutputBeamline(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    private void setMap(Map<Particle, Boolean> inMap) {
        acceptedInputMap.clear();
        acceptedInputMap.putAll(inMap);
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();
        saveInputMapToNBT(data, acceptedInputMap);
        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        loadInputMapFromNBT(data, acceptedInputMap);
    }

    private final int BUTTONS_PER_ROW = 6;
    private final int PADDED_WIDTH_PER_BUTTON = 22;
    private final int PADDED_HEIGHT_PER_BUTTON = 20;

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        GenericMapSyncHandler<Particle, Boolean> mapSyncHandler = new GenericMapSyncHandler.Builder<Particle, Boolean>()
            .getter(() -> acceptedInputMap)
            .setter(this::setMap)
            .valueAdapter(new ValueBooleanAdapter())
            .keyAdapter(new KeyParticleAdapter())
            .build();

        syncManager.syncValue("inputMap", mapSyncHandler);

        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build()
            .child(
                Flow.column()
                    .align(Alignment.START)
                    .child(
                        new TextWidget("Blacklist Selection").anchorLeft(0)
                            .size(120, 18)
                            .padding(4, 0))
                    .child(
                        createBlacklistWidget(mapSyncHandler).anchorLeft(4)
                            .paddingLeft(7))
                    .setEnabledIf(widget -> !acceptedInputMap.isEmpty()))

            .child(
                new TextWidget("No List to choose from :(").anchorLeft(0)
                    .size(120, 18)
                    .padding(4, 16)
                    .setEnabledIf(widget -> acceptedInputMap.isEmpty()))

        ;
    }

    protected ListWidget<IWidget, ?> createBlacklistWidget(GenericMapSyncHandler<Particle, Boolean> map) {
        ListWidget<IWidget, ?> blacklistOptions = new ListWidget<>();
        blacklistOptions.size(PADDED_WIDTH_PER_BUTTON * BUTTONS_PER_ROW, PADDED_HEIGHT_PER_BUTTON * 3);
        // for every 6 particles in the allowed particle list, make a new row
        // add each row to the blacklistOptions
        final Set<Particle> particleSet = acceptedInputMap.keySet();

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

    /*
     * There is currently a bug with MUI2 and .overlay, it un-applies the theme after it was applied once, for some
     * reason. should be fixed and is not on me
     */
    protected IWidget createButtonForParticle(GenericMapSyncHandler<Particle, Boolean> mapSyncHandler,
        Particle particle) {
        return new ToggleButton().marginRight(2).invertSelected(true)
            .overlay(particle.getTexture())
            .value(
                new BoolValue.Dynamic(
                    () -> mapSyncHandler.getValue()
                        .get(particle),
                    bool -> mapSyncHandler.setValue(copyWith(mapSyncHandler.getValue(), particle, bool))))
            .tooltipBuilder(t -> t.addLine(particle.getLocalisedName()));
    }

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
