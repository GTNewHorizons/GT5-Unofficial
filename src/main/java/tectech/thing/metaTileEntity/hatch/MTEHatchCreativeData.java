package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.gui.modularui.hatch.MTEHatchCreativeDataGui;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 27.10.2016.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchCreativeData extends MTEHatchDataInput {

    private long production = Long.MAX_VALUE;

    public MTEHatchCreativeData(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchCreativeData(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCreativeData(mName, mTier, getDescription(), mTextures);
    }

    public long getProduction() {
        return production;
    }

    public void setProduction(long production) {
        this.production = production;
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.debug.tt.data.desc.0"),
            translateToLocal("gt.blockmachines.debug.tt.data.desc.1"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.data.desc.2") };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("computation", production);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("computation")) {
            production = Math.max(1, aNBT.getLong("computation"));
        }
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.isAllowedToWork()) {
                if (CommonValues.MOVE_AT == aTick % 20) {
                    setContents(new QuantumDataPacket(production));
                }
            } else {
                setContents(null);
                updateComputationHistory(0);
                aBaseMetaTileEntity.setActive(false);
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aPlayer.isUsingItem()) {
            openGui(aPlayer);
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchCreativeDataGui(this).build(guiData, syncManager, uiSettings);
    }
}
