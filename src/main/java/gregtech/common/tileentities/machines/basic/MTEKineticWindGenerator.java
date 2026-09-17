package gregtech.common.tileentities.machines.basic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEKineticRotorBase;
import gregtech.common.gui.modularui.singleblock.kinetic.MTEKineticWindGeneratorGui;
import ic2.core.WorldData;

public class MTEKineticWindGenerator extends MTEKineticRotorBase {

    private static final int WIND_CHECK_INTERVAL = 5;
    private static final int SPACE_FRONT = 10;
    private static final int SPACE_SIDES = 4;
    private static final float WIND_MULTI = 2f;
    private double wind = 0d;

    public MTEKineticWindGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Override
    protected String[] getExtraDescriptionInfo() {
        return new String[] { StatCollector.translateToLocal("gt.blockmachines.kinetic.wind.tooltip") };
    }

    public MTEKineticWindGenerator(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures, SPACE_FRONT, SPACE_SIDES);
    }

    private void doWindSim(IGregTechTileEntity machine) {
        double newWind = WorldData.get(getBaseMetaTileEntity().getWorld()).windSim
            .getWindAt(getBaseMetaTileEntity().getYCoord());

        // only issue tile update if there is a difference in wind
        if (newWind != wind) {
            wind = newWind;
            machine.issueTileUpdate();
        }
    }

    public double getWind() {
        return wind;
    }

    @Override
    protected long calculateCurrentKU() {
        return Math.min(
            (long) (getWind() * getRotorEfficiency()
                * OUTPUT_MULTI
                * tierScalar()
                * obstructedOutputMultiplier()
                * WIND_MULTI),
            maxKU());
    }

    @Override
    protected boolean isBlockValid(Block block) {
        return block == Blocks.air;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aTick % WIND_CHECK_INTERVAL == 0) {
            doWindSim(aBaseMetaTileEntity);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    protected double getRotationAmount() {
        return super.getRotationAmount() * (wind / 50);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEKineticWindGenerator(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("wind", wind);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        wind = aNBT.getDouble("wind");
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();
        data.setDouble("wind", wind);
        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        wind = data.getDouble("wind");
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEKineticWindGeneratorGui(this).build(guiData, syncManager, uiSettings);
    }
}
