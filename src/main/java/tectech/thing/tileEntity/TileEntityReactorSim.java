package tectech.thing.tileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.core.IC2;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import tectech.Reference;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class TileEntityReactorSim extends TileEntityNuclearReactorElectric {

    private boolean hadRedstone = true;

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating() && addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            // this.addedToEnergyNet = false;
        }
    }

    @Override
    public void onUnloaded() {
        addedToEnergyNet = false;
        super.onUnloaded();
    }

    @Override
    public String getInventoryName() {
        return "Nuclear Reactor Simulator";
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return false;
    }

    @Override
    public double getOfferedEnergy() {
        return 0;
    }

    @Override
    public double getReactorEUEnergyOutput() {
        return getReactorEnergyOutput() * 5.0F
            * ConfigUtil.getDouble(MainConfig.get(), "balance/energy/generator/nuclear");
    }

    @Override
    protected void updateEntityServer() {
        if (updateTicker++ % getTickRate() == 0) {
            if (!worldObj.isRemote && worldObj.doChunksNearChunkExist(xCoord, yCoord, zCoord, 2)) {
                if (hadRedstone && !receiveredstone()) {
                    hadRedstone = false;
                } else if (!hadRedstone && receiveredstone()) {
                    doUpdates();
                    hadRedstone = true;
                }
                markDirty();
            }
        }
    }

    @Override
    public boolean calculateHeatEffects() {
        if (heat >= 4000 && IC2.platform.isSimulating()) {
            float power = (float) heat / (float) maxHeat;
            if (power >= 1.0F) {
                explode(); // ding
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    // new method
    private void doUpdates() {
        heat = 0;
        do {
            dropAllUnfittingStuff();
            output = 0.0F;
            maxHeat = 10000;
            hem = 1.0F;
            processChambers();
        } while (!calculateHeatEffects() && output > 0);
    }

    @Override
    public void explode() {
        getWorld().playSoundEffect(xCoord, yCoord, zCoord, Reference.MODID + ":microwave_ding", 1, 1);
    }

    @Override
    public void addEmitHeat(int heat) {}

    @Override
    public boolean isFluidCooled() {
        return false;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}
