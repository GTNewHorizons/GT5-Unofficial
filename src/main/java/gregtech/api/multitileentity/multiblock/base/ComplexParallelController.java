package gregtech.api.multitileentity.multiblock.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Waila;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class ComplexParallelController<T extends ComplexParallelController<T, P>, P extends ComplexParallelProcessingLogic<P>>
    extends PowerController<T, P> {

    protected int maxComplexParallels = 0;
    protected int currentComplexParallels = 0;
    protected int[] maxProgressTimes = new int[0];
    protected int[] progressTimes = new int[0];

    public ComplexParallelController() {
        isSimpleMachine = false;
    }

    protected void setMaxComplexParallels(int parallel, boolean stopMachine) {
        if (parallel != maxComplexParallels) {
            if (maxComplexParallels != 0 && stopMachine) {
                stopMachine(false);
            }
            maxProgressTimes = new int[parallel];
            progressTimes = new int[parallel];
        }
        maxComplexParallels = parallel;
    }

    @Override
    public boolean hasThingsToDo() {
        return IntStream.of(maxProgressTimes)
            .sum() > 0;
    }

    @Override
    protected void stopMachine(boolean powerShutDown) {
        super.stopMachine(powerShutDown);
        Arrays.fill(maxProgressTimes, 0);
    }

    protected void setDuration(int index, int duration) {
        if (duration < 0) {
            duration = -duration;
        }
        if (index >= 0 && index < maxComplexParallels) {
            maxProgressTimes[index] = duration;
        }
    }

    protected ItemStack[] getInputItems(int index) {
        return getInputItems();
    }

    protected FluidStack[] getInputFluids(int index) {
        return getInputFluids();
    }

    protected boolean isVoidProtectionEnabledForItem(int index) {
        return protectsExcessItem();
    }

    protected boolean isVoidProtectionEnabledForFluid(int index) {
        return protectsExcessFluid();
    }

    protected boolean hasPerfectOverclock() {
        return false;
    }

    protected long getEutForComplexParallel(int index) {
        // As default behavior we'll give the parallel all remaining EU we have
        return GT_Values.V[tier] - eut;
    }

    @Override
    protected void addProgressStringToScanner(EntityPlayer player, int logLevel, ArrayList<String> list) {
        for (int i = 0; i < maxComplexParallels; i++) {
            list.add(
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + " "
                    + (i + 1)
                    + ": "
                    + EnumChatFormatting.GREEN
                    + GT_Utility.formatNumbers(progressTimes[i] > 20 ? progressTimes[i] / 20 : progressTimes[i])
                    + EnumChatFormatting.RESET
                    + (progressTimes[i] > 20 ? " s / " : " ticks / ")
                    + EnumChatFormatting.YELLOW
                    + GT_Utility
                        .formatNumbers(maxProgressTimes[i] > 20 ? maxProgressTimes[i] / 20 : maxProgressTimes[i])
                    + EnumChatFormatting.RESET
                    + (maxProgressTimes[i] > 20 ? " s" : " ticks"));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("maxComplexParallels", maxComplexParallels);
        for (int i = 0; i < maxComplexParallels; i++) {
            tag.setInteger("maxProgress" + i, maxProgressTimes[i]);
            tag.setInteger("progress" + i, progressTimes[i]);
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
                    + GT_Waila
                        .getMachineProgressString(maxProgress > 0 && maxProgress >= progress, maxProgress, progress));
        }
    }
}
