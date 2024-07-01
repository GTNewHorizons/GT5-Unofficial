package gregtech.loaders.misc.bees;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;
import forestry.core.genetics.EffectData;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;

public class GT_EffectMachineBoost extends GT_AlleleEffect {

    public GT_EffectMachineBoost() {
        super("effectMachineBoost", false, 600);
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (storedData == null) {
            storedData = new EffectData(1, 1);
        }
        return storedData;
    }

    @Override
    protected IEffectData doEffectTickThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        World world = housing.getWorld();
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

        // Get random coords within territory
        int xRange = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[0]);
        int yRange = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[1]);
        int zRange = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[2]);

        int xCoord = coords.posX + world.rand.nextInt(xRange) - xRange / 2;
        int yCoord = coords.posY + world.rand.nextInt(yRange) - yRange / 2;
        int zCoord = coords.posZ + world.rand.nextInt(zRange) - zRange / 2;

        // If gt machine of tier lower equal than ev, boost recipe by 2s
        TileEntity tileEntity = world.getTileEntity(xCoord, yCoord, zCoord);
        if (tileEntity instanceof BaseMetaTileEntity machine) {
            if (machine.getMetaTileEntity() instanceof GT_MetaTileEntity_TieredMachineBlock tieredMachine) {
                if (tieredMachine.mTier <= 4) { // EV
                    if (machine.isActive() && machine.getMaxProgress() > 60) {
                        machine.increaseProgress(40);
                    }
                }
            }
        }

        storedData.setInteger(0, 0);

        return storedData;
    }
}
