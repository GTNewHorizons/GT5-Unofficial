package gregtech.common.tileentities.machines.multi.LHC;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuis;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;

public class MTEHatchAdvancedOutputBeamline extends MTEHatchOutputBeamline {

    List<Particle> acceptedInputs = Arrays.asList(Particle.values());

    public void setAcceptedInputs(List<Particle> acceptedInputs) {
        this.acceptedInputs = acceptedInputs;
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
    public boolean hasBlacklist() {
        return true;
    }

    @Override
    public List<Integer> getBlacklist() {
        return ImmutableList.of();
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

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build();
    }










}
