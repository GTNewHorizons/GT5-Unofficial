package gregtech.api.multitileentity.multiblock.base;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.GT_Values;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.util.GT_Utility;

public abstract class PowerController<T extends PowerController<T>> extends Controller<T> implements PowerLogicHost {

    public PowerController() {
        super();
        power = new PowerLogic().setType(PowerLogic.RECEIVER);
    }

    protected PowerLogic power;

    @Override
    public void writeMultiTileNBT(NBTTagCompound nbt) {
        super.writeMultiTileNBT(nbt);
        power.writeToNBT(nbt);
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);
        power.loadFromNBT(nbt);
    }

    @Override
    public PowerLogic getPowerLogic(ForgeDirection side) {
        return power;
    }

    @Override
    public boolean checkMachine() {
        boolean result = super.checkMachine();
        power.setEnergyCapacity(GT_Values.V[tier] * 2 * 60 * 20);
        power.setAmperage(2);
        power.setMaxVoltage(GT_Values.V[tier]);
        return result;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("isActive", isActive());
        if (isActive()) {
            tag.setLong("energyUsage", eut);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (actualEnergyUsage > 0) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use",
                        GT_Utility.formatNumbers(actualEnergyUsage),
                        GT_Utility.getColoredTierNameFromVoltage(actualEnergyUsage)));
            } else if (actualEnergyUsage < 0) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.produce",
                        GT_Utility.formatNumbers(-actualEnergyUsage),
                        GT_Utility.getColoredTierNameFromVoltage(-actualEnergyUsage)));
            }
        }
    }
}
