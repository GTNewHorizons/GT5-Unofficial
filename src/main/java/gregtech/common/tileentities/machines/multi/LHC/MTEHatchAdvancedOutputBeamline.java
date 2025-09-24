package gregtech.common.tileentities.machines.multi.LHC;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MTEHatchAdvancedOutputBeamline extends MTEHatchOutputBeamline {

    private final String NBT_KEY_DESCRIPTOR = "KEY"; //specifically for the Particle value in the input map, will save with id
    private final String NBT_VALUE_DESCRIPTOR = "VALUE"; //specifically for the Boolean value in the input map

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        //save all entries in acceptedInputMap
        saveInputMapToNBT(aNBT,this.acceptedInputMap);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        loadInputMapFromNBT(aNBT,this.acceptedInputMap);
    }

    public void saveInputMapToNBT(NBTTagCompound aNBT, Map<Particle,Boolean> map)
    {
        for(Map.Entry<Particle,Boolean> entry : map.entrySet())
        {
            Particle key = entry.getKey();
            boolean value = entry.getValue();
            final String particleName = key.getName();
            aNBT.setInteger(NBT_KEY_DESCRIPTOR+particleName,key.getId());
            aNBT.setBoolean(NBT_VALUE_DESCRIPTOR+particleName, value);
        }
    }

    public void loadInputMapFromNBT(NBTTagCompound aNBT, Map<Particle,Boolean> map)
    {
        for(Particle p : Particle.VALUES)
        {
            final String particleName = p.getName();
            if (!aNBT.hasKey(NBT_KEY_DESCRIPTOR + particleName)) continue;
            //assume that if key exists, value exists as well
            //can skip the key nbt mapping, as the particle is in NBT
            map.put(p, aNBT.getBoolean(NBT_VALUE_DESCRIPTOR+particleName));

        }
    }



    Map<Particle,Boolean> acceptedInputMap = new HashMap<>();

    public void setInitialParticleList(List<Particle> initialParticleList) {

        acceptedInputMap.clear();

        for (Particle p : initialParticleList) {
            acceptedInputMap.put(p,true);
        }

    }

    public MTEHatchAdvancedOutputBeamline(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
      //  setInitialParticleList(Arrays.asList(Particle.values()));
    }

    public MTEHatchAdvancedOutputBeamline(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
 //       setInitialParticleList(Arrays.asList(Particle.values()));
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

    private void setMap(Map<Particle,Boolean> inMap)
    {
        acceptedInputMap = inMap;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        syncManager.syncValue("inputMap", new GenericSyncValue<>(() -> acceptedInputMap, this::setMap, new AcceptedInputMapAdapter()));
        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build()
            .child(Flow.column().align(Alignment.START).child(
                new TextWidget("Blacklist Selection").anchorLeft(0)
                    .size(120,18).padding(4,0)
            ) .child(createBlacklistWidget(syncManager).anchorLeft(4).paddingLeft(7)
            ))



            ;
    }

    private final String PARTICLE_IDENTIFIER = "PARTICLE";

    protected ListWidget<IWidget, ?>  createBlacklistWidget(PanelSyncManager syncManager){
        ListWidget<IWidget, ?> blacklistOptions = new ListWidget<>();
        blacklistOptions.size(22*4,20*3);
        // for every four particles in the allowed particle list, make a new row
        // add each row to the blacklistOptions
        final Set<Particle> particleSet = acceptedInputMap.keySet();

        int numRows = (int) Math.ceil((double) particleSet.size() / 4); //gross java math
        for(int i = 0 ; i < numRows; i++)
        {
            blacklistOptions.addChild(Flow.row().size(20*4,18).marginBottom(4),i);
        }

        int runningCount = 0; //value to determine if we've reached the breaking point of a new row

        for(Particle p : particleSet)
        {
            syncManager.syncValue(PARTICLE_IDENTIFIER+p.getId(),new BooleanSyncValue(()->acceptedInputMap.get(p),bool -> acceptedInputMap.replace(p,bool)));
            final Flow currentParticleRow = (Flow) blacklistOptions.getChildren().get(runningCount/4);
            currentParticleRow.addChild(createButtonForParticle(syncManager,p),runningCount%4);
            runningCount++;
        }

        return blacklistOptions;
    }

    protected IWidget createButtonForParticle(PanelSyncManager syncManager, Particle particle){
        BooleanSyncValue syncer = (BooleanSyncValue) syncManager.getSyncHandler(PARTICLE_IDENTIFIER+particle.getId());
        return new ToggleButton().marginRight(2).value(syncer)
            .tooltipBuilder(t -> t.addLine(particle.getLocalisedName()));
    }


    protected class AcceptedInputMapAdapter implements IByteBufAdapter<Map<Particle,Boolean>>
    {

        @Override
        public Map<Particle, Boolean> deserialize(PacketBuffer buffer) throws IOException {
            NBTTagCompound data = buffer.readNBTTagCompoundFromBuffer();
            Map<Particle,Boolean> mapToReturn = new HashMap<>();
            loadInputMapFromNBT(data,mapToReturn);
            return mapToReturn;
        }

        @Override
        public void serialize(PacketBuffer buffer, Map<Particle, Boolean> u) throws IOException {

            NBTTagCompound data = new NBTTagCompound();
            saveInputMapToNBT(data, u);
            buffer.writeNBTTagCompoundToBuffer(data);
        }

        @Override
        public boolean areEqual(@NotNull Map<Particle, Boolean> t1, @NotNull Map<Particle, Boolean> t2) {
            return t1.equals(t2);
        }
    }
}
