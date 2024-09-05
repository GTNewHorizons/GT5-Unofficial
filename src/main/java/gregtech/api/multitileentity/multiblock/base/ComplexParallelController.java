package gregtech.api.multitileentity.multiblock.base;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class ComplexParallelController<C extends ComplexParallelController<C, P>, P extends ComplexParallelProcessingLogic<P>>
    extends Controller<C, P> {

    protected int maxComplexParallels = 0;
    protected int currentComplexParallels = 0;

    public ComplexParallelController() {
        isSimpleMachine = false;
    }

    protected void setMaxComplexParallels(int parallel, boolean stopMachine) {
        if (parallel != maxComplexParallels && maxComplexParallels != 0 && stopMachine) {
            stopMachine(false);
        }
        maxComplexParallels = parallel;
        setProcessingUpdate(true);
    }

    @Override
    protected void stopMachine(boolean powerShutDown) {
        super.stopMachine(powerShutDown);
    }

    protected boolean hasPerfectOverclock() {
        return false;
    }

    @Override
    protected void addProgressStringToScanner(EntityPlayer player, int logLevel, ArrayList<String> list) {
        P processing = getProcessingLogic();
        for (int i = 0; i < maxComplexParallels; i++) {
            list.add(
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + " "
                    + (i + 1)
                    + ": "
                    + EnumChatFormatting.GREEN
                    + GTUtility.formatNumbers(
                        processing.getProgress(i) > 20 ? processing.getProgress(i) / 20 : processing.getProgress(i))
                    + EnumChatFormatting.RESET
                    + (processing.getProgress(i) > 20 ? " s / " : " ticks / ")
                    + EnumChatFormatting.YELLOW
                    + GTUtility.formatNumbers(
                        processing.getDuration(i) > 20 ? processing.getDuration(i) / 20 : processing.getDuration(i))
                    + EnumChatFormatting.RESET
                    + (processing.getDuration(i) > 20 ? " s" : " ticks"));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        P processing = getProcessingLogic();
        tag.setInteger("maxComplexParallels", maxComplexParallels);
        for (int i = 0; i < maxComplexParallels; i++) {
            tag.setInteger("maxProgress" + i, processing.getDuration(i));
            tag.setInteger("progress" + i, processing.getProgress(i));
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        maxComplexParallels = tag.getInteger("maxComplexParallels");
        for (int i = 0; i < maxComplexParallels; i++) {
            long maxProgress = tag.getInteger("maxProgress" + i);
            long progress = tag.getInteger("progress" + i);
            currentTip.add(
                "Process " + (i + 1)
                    + ": "
                    + GTWaila
                        .getMachineProgressString(maxProgress > 0 && maxProgress >= progress, maxProgress, progress));
        }
    }

    @Override
    public void setProcessingLogicPower(@Nonnull P processingLogic) {
        processingLogic.setAmperageOC(true);
        processingLogic.setAvailableAmperage(getPowerLogic().getMaxAmperage() / maxComplexParallels);
        processingLogic.setAvailableVoltage(getPowerLogic().getVoltage() / maxComplexParallels);
    }

    @Override
    public void updateProcessingLogic(@Nonnull P processingLogic) {
        processingLogic.setMaxComplexParallel(maxComplexParallels);
    }
}
