package gregtech.common.tileentities.machines.basic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEKineticRotorBase;
import gregtech.common.config.MachineStats;
import gregtech.common.gui.modularui.singleblock.kinetic.MTEKineticWaterGeneratorGui;
import io.netty.buffer.ByteBuf;

public class MTEKineticWaterGenerator extends MTEKineticRotorBase {

    private static final int SPACE_FRONT = 8;
    private static final int SPACE_SIDES = 2;

    private float biomeMulti = 0f;
    private double flowMulti = 0d;

    private static final int FLOW_CHANGE_INTERVAL = 200;
    private final double[] flowRands = { 0d, 0d, 0d, 0d, 0d };
    private int randIndex = 0;

    private static float WATER_MULTI;

    public MTEKineticWaterGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEKineticWaterGenerator(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures, SPACE_FRONT, SPACE_SIDES);
    }

    private void setBiomeMulti(IGregTechTileEntity tileEntity) {
        BiomeGenBase biome = tileEntity.getBiome();
        if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.RIVER)) {
            biomeMulti = 4f;
        } else {
            biomeMulti = 1f;
        }
    }

    private void setFlowRand(int index) {
        flowRands[index] = Math.random();
    }

    private void setNewFlowMulti(IGregTechTileEntity tileEntity) {
        double newRand = 0d;
        for (double rand : flowRands) {
            newRand += rand;
        }
        // always a value between 0.5f and 1.5f, more likely to be close to 1f
        flowMulti = (newRand / flowRands.length) + 0.5d;
        tileEntity.issueTileUpdate();
    }

    public double getFlowMulti() {
        return flowMulti;
    }

    public float getBiomeMulti() {
        return biomeMulti;
    }

    @Override
    protected String[] getExtraDescriptionInfo() {
        return new String[] { StatCollector.translateToLocal("gt.blockmachines.kinetic.water.tooltip") };
    }

    @Override
    protected long calculateCurrentKU() {
        return Math.min(
            (long) (biomeMulti * getRotorEfficiency()
                * OUTPUT_MULTI
                * tierScalar()
                * flowMulti
                * obstructedOutputMultiplier()
                * WATER_MULTI),
            maxKU());
    }

    @Override
    protected boolean isBlockValid(Block block) {
        return (block == Blocks.water || (block instanceof BlockLiquid liquid && liquid == Blocks.flowing_water));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % FLOW_CHANGE_INTERVAL == 0) {
            setNewFlowMulti(aBaseMetaTileEntity);
            setFlowRand(randIndex); // set new value for next iteration
            if (randIndex == 4) {
                randIndex = 0;
            } else {
                randIndex++;
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            setBiomeMulti(aBaseMetaTileEntity);
            for (int i = 0; i < flowRands.length; i++) {
                setFlowRand(i);
            }
            setNewFlowMulti(aBaseMetaTileEntity);
        }
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    protected double getRotationAmount() {
        return super.getRotationAmount() * (biomeMulti / 2) * flowMulti;
    }

    @Override
    public void onConfigLoad() {
        WATER_MULTI = (MachineStats.kinetics.waterKUMultiplier * 64.0f);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEKineticWaterGenerator(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("biomeMulti", biomeMulti);
        aNBT.setDouble("flowMulti", flowMulti);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        biomeMulti = aNBT.getFloat("biomeMulti");
        flowMulti = aNBT.getDouble("flowMulti");
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        buffer.writeFloat(biomeMulti);
        buffer.writeDouble(flowMulti);
    }

    @Override
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        biomeMulti = buffer.readFloat();
        flowMulti = buffer.readDouble();
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEKineticWaterGeneratorGui(this).build(guiData, syncManager, uiSettings);
    }
}
