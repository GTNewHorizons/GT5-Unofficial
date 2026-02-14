package gregtech.common.tileentities.machines.multi.beamcrafting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.gui.modularui.hatch.MTEHatchAdvancedOutputBeamlineGui;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public class MTEHatchAdvancedOutputBeamline extends MTEHatchOutputBeamline {

    // specifically for the Particle value in the input map, will save with id
    private final String NBT_KEY_DESCRIPTOR = "KEY";
    // specifically for the Boolean value in the input map
    private final String NBT_VALUE_DESCRIPTOR = "VALUE";

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
    }

    public MTEHatchAdvancedOutputBeamline(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEHatchAdvancedOutputBeamline(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    public Map<Particle, Boolean> getParticleMap() {
        return this.acceptedInputMap;
    }

    public void setAcceptedInputMap(Map<Particle, Boolean> inMap) {
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

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        return new MTEHatchAdvancedOutputBeamlineGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            StatCollector.translateToLocalFormatted(
                "gt.blockmachines.multimachine.beamcrafting.ttbeamouthatchfiltered.description1"),
            StatCollector.translateToLocalFormatted(
                "gt.blockmachines.multimachine.beamcrafting.ttbeamouthatchfiltered.description2") };
    }

}
