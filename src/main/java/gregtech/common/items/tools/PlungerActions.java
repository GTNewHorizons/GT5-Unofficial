package gregtech.common.items.tools;

import java.util.HashMap;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityItemPipe;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.util.GTUtility;

/**
 * The "right click a pipe to unclog it" half of a plunger, moved out of {@code BehaviourPlungerItem} and
 * {@code BehaviourPlungerFluid}. The Thaumcraft essentia case lives in {@link PlungerEssentiaActions}, which must
 * only be named when Thaumcraft is present.
 * <p/>
 * The item tries these in the order the behaviours were registered in: items, then fluids, then essentia.
 */
public final class PlungerActions {

    public static final int PLUNGER_DRAIN_AMOUNT = 1_000;

    private PlungerActions() {}

    /** Dumps the contents of an item pipe network onto the ground. */
    public static boolean pullItems(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side,
        float hitX, float hitY, float hitZ, BooleanSupplier pay) {
        if (world.isRemote) return false;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IGregTechTileEntity gtTE)) return false;
        IMetaTileEntity metaTileEntity = gtTE.getMetaTileEntity();
        if (!(metaTileEntity instanceof IMetaTileEntityItemPipe)) return false;

        for (IMetaTileEntityItemPipe pipe : GTUtility
            .sortMapByValuesAcending(
                IMetaTileEntityItemPipe.Util
                    .scanPipes((IMetaTileEntityItemPipe) metaTileEntity, new HashMap<>(), 0L, false, true))
            .keySet()) {
            for (int i = 0, j = pipe.getSizeInventory(); i < j; i++) {
                if (!pipe.isValidSlot(i)) continue;
                if (pipe.getStackInSlot(i) == null || !pay.getAsBoolean()) continue;
                final ItemStack stack = pipe.decrStackSize(i, 64);
                if (stack != null) {
                    final EntityItem entity = new EntityItem(
                        world,
                        gtTE.getOffsetX(side, 1) + 0.5D,
                        gtTE.getOffsetY(side, 1) + 0.5D,
                        gtTE.getOffsetZ(side, 1) + 0.5D,
                        stack);
                    entity.motionX = 0.0D;
                    entity.motionY = 0.0D;
                    entity.motionZ = 0.0D;
                    world.spawnEntityInWorld(entity);
                    GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_PLUNGER, 1.0F, -1.0F, hitX, hitY, hitZ);
                }
                return true;
            }
        }
        return false;
    }

    /** Drains a tank or fluid pipe; sneaking empties it completely rather than a bucket's worth. */
    public static boolean pullFluid(EntityPlayer player, World world, int x, int y, int z, float hitX, float hitY,
        float hitZ, BooleanSupplier pay) {
        if (world.isRemote) return false;
        final int drainAmount = player.isSneaking() ? Integer.MAX_VALUE : PLUNGER_DRAIN_AMOUNT;
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof IFluidHandler fluidHandler) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (fluidHandler.drain(direction, drainAmount, false) != null && pay.getAsBoolean()) {
                    fluidHandler.drain(direction, drainAmount, true);
                    GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_PLUNGER, 1.0F, 1.0F, hitX, hitY, hitZ);
                    return true;
                }
            }
        }
        if (tileEntity instanceof IGregTechTileEntity gtTE
            && gtTE.getMetaTileEntity() instanceof MTEBasicTank machine) {
            // Note: this branch never charged the tool, and still does not.
            if (machine.mFluid != null && machine.mFluid.amount > 0) {
                machine.mFluid.amount = machine.mFluid.amount - Math.min(machine.mFluid.amount, drainAmount);
            }
            GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_PLUNGER, 1.0F, 1.0F, hitX, hitY, hitZ);
            return true;
        }
        return false;
    }
}
