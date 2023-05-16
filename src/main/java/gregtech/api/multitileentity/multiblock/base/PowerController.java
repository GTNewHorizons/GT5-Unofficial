package gregtech.api.multitileentity.multiblock.base;

import static gregtech.api.enums.TickTime.MINUTE;

import java.util.List;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
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
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import org.apache.commons.lang3.tuple.Pair;

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
        updatePowerLogic();
        return result;
    }

    @Override
    protected boolean checkRecipe() {
        if (!(this instanceof ProcessingLogicHost)) {
            return false;
        }
        ProcessingLogic logic = ((ProcessingLogicHost) this).getProcessingLogic();
        logic.clear();
        boolean result = false;
        if (isSeparateInputs()) {
            // TODO: Add separation with fluids
            for (Pair<ItemStack[], String> inventory : getItemInputsForEachInventory()) {
                IItemHandlerModifiable outputInventory = multiBlockOutputInventory
                    .getOrDefault(inventory.getLeft(), null);
                result = logic.setInputItems(inventory.getLeft())
                    .setCurrentOutputItems(getOutputItems())
                    .process();
                if (result) {
                    inventoryName = inventory.getRight();
                    break;
                }
                logic.clear();
            }
        } else {
            result = logic.setInputItems(getInputItems())
                .setCurrentOutputItems(getOutputItems())
                .setInputFluids(getInputFluids())
                .setCurrentOutputFluids(getOutputFluids())
                .setVoltage(power.getVoltage())
                .setAmperage(amperage)
                .setPerfectOverclock(hasPerfectOverclock())
                .setIsCleanroom(isCleanroom)
                .process();
        }
        setDuration(logic.getDuration());
        setEut(logic.getEut());
        setItemOutputs(logic.getOutputItems());
        setFluidOutputs(logic.getOutputFluids());
        return result;
    }

    protected boolean hasPerfectOverclock() {
        return false;
    }

    protected void updatePowerLogic() {
        power.setEnergyCapacity(GT_Values.V[tier] * 2 * MINUTE);
        power.setAmperage(amperage);
        power.setMaxVoltage(GT_Values.V[tier]);
        power.setCanUseLaser(canUseLaser);
        power.setCanUseWireless(canUseWireless, getOwnerUuid());
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("isActive", isActive());
        if (isActive()) {
            tag.setLong("energyUsage", eut);
            tag.setLong("energyTier", tier);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            long energyTier = tag.getLong("energyTier");
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (actualEnergyUsage > 0) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use_with_amperage",
                        GT_Utility.formatNumbers(actualEnergyUsage),
                        GT_Utility.getAmperageForTier(actualEnergyUsage, (byte) energyTier),
                        GT_Utility.getColoredTierNameFromTier((byte) energyTier)));
            } else if (actualEnergyUsage < 0) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.produce_with_amperage",
                        GT_Utility.formatNumbers(-actualEnergyUsage),
                        GT_Utility.getAmperageForTier(-actualEnergyUsage, (byte) energyTier),
                        GT_Utility.getColoredTierNameFromTier((byte) energyTier)));
            }
        }
    }
}
