package gtPlusPlus.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.IGearEnergyTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.ITurnable;

public abstract interface IGregtechPower
        extends IGearEnergyTileEntity, ITurnable, IGregTechDeviceInformation, IDescribable, IBasicEnergyContainer {

    @Override
    public String[] getDescription();

    @Override
    default boolean isUniversalEnergyStored(long p0) {
        return false;
    }

    @Override
    public long getOutputAmperage();

    @Override
    public long getOutputVoltage();

    @Override
    public long getInputAmperage();

    @Override
    public long getInputVoltage();

    @Override
    public boolean decreaseStoredEnergyUnits(long p0, boolean p1);

    @Override
    public boolean increaseStoredEnergyUnits(long p0, boolean p1);

    @Override
    public boolean drainEnergyUnits(ForgeDirection side, long p1, long p2);

    @Override
    public long getAverageElectricInput();

    @Override
    public long getAverageElectricOutput();

    @Override
    public long getStoredEU();

    @Override
    public long getEUCapacity();

    @Override
    public long getStoredSteam();

    @Override
    public long getSteamCapacity();

    @Override
    public boolean increaseStoredSteam(long p0, boolean p1);

    @Override
    public Block getBlockAtSide(ForgeDirection side);

    @Override
    public Block getBlockAtSideAndDistance(ForgeDirection side, int p1);

    @Override
    public Block getBlockOffset(int p0, int p1, int p2);

    @Override
    public TileEntity getTileEntity(int p0, int p1, int p2);

    @Override
    public TileEntity getTileEntityAtSide(ForgeDirection side);

    @Override
    public TileEntity getTileEntityAtSideAndDistance(ForgeDirection side, int p1);

    @Override
    public TileEntity getTileEntityOffset(int p0, int p1, int p2);

    @Override
    public World getWorld();

    @Override
    public int getXCoord();

    @Override
    public short getYCoord();

    @Override
    public int getZCoord();

    @Override
    public boolean isClientSide();

    @Override
    public boolean isDead();

    @Override
    public boolean isInvalidTileEntity();

    @Override
    public boolean isServerSide();

    @Override
    public void readFromNBT(NBTTagCompound p0);

    @Override
    public void writeToNBT(NBTTagCompound p0);

    @Override
    public boolean acceptsRotationalEnergy(ForgeDirection side);

    @Override
    public boolean injectRotationalEnergy(ForgeDirection side, long p1, long p2);

    @Override
    public long injectEnergyUnits(ForgeDirection side, long p1, long p2);

    @Override
    public boolean inputEnergyFrom(ForgeDirection side);

    @Override
    public boolean outputsEnergyTo(ForgeDirection side);

    @Override
    public String[] getInfoData();

    @Override
    public default boolean isGivingInformation() {
        return true;
    }

}
