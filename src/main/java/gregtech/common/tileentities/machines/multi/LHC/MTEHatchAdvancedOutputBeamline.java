package gregtech.common.tileentities.machines.multi.LHC;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.google.common.collect.ImmutableList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuis;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MTEHatchAdvancedOutputBeamline extends MTEHatchOutputBeamline {

    List<Pair<Particle,Boolean>> acceptedInputs = new ArrayList<>();

    public void setInitialParticleList(List<Particle> initialParticleList) {

        List<Pair<Particle,Boolean>> list = new ArrayList<>(initialParticleList.size());

        for (Particle p : initialParticleList) {
            list.add(Pair.of(p,true));
        }

        acceptedInputs = list;

    }

    public MTEHatchAdvancedOutputBeamline(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
        setInitialParticleList(Arrays.asList(Particle.values()));
    }

    public MTEHatchAdvancedOutputBeamline(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        setInitialParticleList(Arrays.asList(Particle.values()));
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
            .doesAddGregTechLogo(false)
            .build()
            .child(
                new TextWidget("Blacklist Selection")
                    .size(120,18).padding(4,0)
            )
            .child(
                createBlacklistWidget(syncManager)
            )

            ;
    }

    protected ListWidget<IWidget, ?>  createBlacklistWidget(PanelSyncManager syncManager){
        ListWidget<IWidget, ?> blacklistOptions = new ListWidget<>();
        // for every four particles in the allowed particle list, make a new row
        // add each row to the blacklistOptions

        blacklistOptions.addChild(createButtonForParticle(syncManager, acceptedInputs.get(0)),0);


        return blacklistOptions;
    }

    protected IWidget createButtonForParticle(PanelSyncManager syncManager, Pair<Particle,Boolean> particleState){
        return new ToggleButton().value(new BooleanSyncValue(particleState::getValue, particleState::setValue))
            .tooltipBuilder(t -> t.addLine(particleState.getKey().getLocalisedName()));
    }


}
