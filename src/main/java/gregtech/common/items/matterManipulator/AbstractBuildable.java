package gregtech.common.items.matterManipulator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import appeng.api.config.Actionable;
import appeng.api.implementations.tiles.ISegmentedInventory;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.helpers.ICustomNameObject;
import appeng.parts.AEBasePart;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.GTMod;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IRedstoneEmitter;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.ItemMatterManipulator.ManipulatorTier;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import ic2.api.item.ElectricItem;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class AbstractBuildable extends MMInventory implements IBuildable {

    public AbstractBuildable(EntityPlayer player, NBTState state, ManipulatorTier tier) {
        super(player, state, tier);
    }

    protected static final double EU_PER_BLOCK = 128.0, TE_PENALTY = 16.0, EU_DISTANCE_EXP = 1.25;

    protected static final MethodHandle IS_BLOCK_CONTAINER;

    static {
        try {
            Field isBlockContainer = ReflectionHelper.findField(Block.class, "field_149758_A", "isBlockContainer");
            isBlockContainer.setAccessible(true);
            Objects.requireNonNull(isBlockContainer);
            IS_BLOCK_CONTAINER = MethodHandles.lookup()
                .unreflectGetter(isBlockContainer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not find field Block.isBlockContainer", e);
        }
    }

    public boolean tryConsumePower(ItemStack stack, PendingBlock pendingBlock) {
        double euUsage = EU_PER_BLOCK * pendingBlock.getBlock()
            .getBlockHardness(pendingBlock.getWorld(), pendingBlock.x, pendingBlock.y, pendingBlock.z);

        try {
            ItemBlock block = pendingBlock.getItem();
            if (block != null && (boolean) IS_BLOCK_CONTAINER.invoke(block.field_150939_a)) {
                euUsage *= TE_PENALTY;
            }
        } catch (Throwable e) {
            GTMod.GT_FML_LOGGER.error("Could not get Block.isBlockContainer field", e);
        }

        return tryConsumePower(stack, pendingBlock.x, pendingBlock.y, pendingBlock.z, euUsage);
    }

    public boolean tryConsumePower(ItemStack stack, double x, double y, double z, double euUsage) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }

        euUsage *= Math.pow(player.getDistance(x, y, z), EU_DISTANCE_EXP);

        return ElectricItem.manager.use(stack, euUsage, player);
    }

    protected void removeBlock(World world, int x, int y, int z, Block existing, int existingMeta) {
        TileEntity te = world.getTileEntity(x, y, z);

        emptySuperchest(te);
        emptyTileInventory(te);
        emptyTank(te);
        removeCovers(te);
        resetAEMachine(te);
        resetKeptSettings(te);

        if (existing instanceof IFluidBlock fluidBlock && fluidBlock.canDrain(world, x, y, z)) {
            givePlayerFluids(fluidBlock.drain(world, x, y, z, true));
        } else if (existing == Blocks.water || existing == Blocks.lava) {
            givePlayerFluids(new FluidStack(existing == Blocks.water ? FluidRegistry.WATER : FluidRegistry.LAVA, 1000));
        } else {
            givePlayerItems(
                existing.getDrops(world, x, y, z, existingMeta, 0)
                    .toArray(new ItemStack[0]));
        }

        world.setBlockToAir(x, y, z);
    }

    protected void emptySuperchest(TileEntity te) {
        if (te instanceof IGregTechTileEntity igte && igte.getMetaTileEntity() instanceof MTEDigitalChestBase dchest) {
            for (IAEItemStack stack : dchest.getStorageList()) {
                stack = dchest.extractItems(stack, Actionable.MODULATE, null);

                while (stack.getStackSize() > 0) {
                    ItemStack is = stack.getItemStack();
                    stack.decStackSize(is.stackSize);
                    givePlayerItems(is);
                }
            }
        }
    }

    protected void emptyTileInventory(TileEntity te) {
        if (te instanceof IInventory inv) {
            MMUtils.emptyInventory(this, inv);
        }
    }

    protected void emptyTank(TileEntity te) {
        if (te instanceof IFluidHandler handler) {
            FluidStack fluid = null;
            while ((fluid = handler.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, true)) != null
                && fluid.getFluid() != null
                && fluid.amount > 0) {
                givePlayerFluids(fluid);
            }
        }
    }

    protected void removeCovers(TileEntity te) {
        if (te instanceof ICoverable coverable) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (coverable.getCoverIDAtSide(side) != 0) {
                    ItemStack cover = coverable.removeCoverAtSide(side, true);

                    if (cover != null && cover.getItem() != null) {
                        givePlayerItems(cover);
                    }
                }
            }
        }
    }

    protected void resetAEMachine(Object machine) {
        if (machine instanceof ISegmentedInventory segmentedInventory) {
            IInventory upgrades = segmentedInventory.getInventoryByName("upgrades");

            if (upgrades != null) {
                MMUtils.emptyInventory(this, upgrades);
            }

            IInventory cells = segmentedInventory.getInventoryByName("cells");

            if (cells != null) {
                MMUtils.emptyInventory(this, cells);
            }

            IInventory patterns = segmentedInventory.getInventoryByName("patterns");

            if (patterns != null) {
                MMUtils.emptyInventory(this, patterns);
            }
        }

        if (machine instanceof ICustomNameObject customName) {
            if (customName.hasCustomName()) {
                try {
                    customName.setCustomName(null);
                } catch (IllegalArgumentException e) {
                    // hack because AEBasePart's default setCustomName impl throws an IAE when the name is null
                    if (machine instanceof AEBasePart basePart) {
                        NBTTagCompound tag = basePart.getItemStack()
                            .getTagCompound();

                        if (tag != null) {
                            tag.removeTag("display");

                            if (tag.hasNoTags()) {
                                basePart.getItemStack()
                                    .setTagCompound(null);
                            }
                        }
                    }
                }
            }
        }

        if (machine instanceof IPartHost host) {
            // intentionally includes UNKNOWN to remove any cables
            for (ForgeDirection dir : ForgeDirection.values()) {
                IPart part = host.getPart(dir);

                if (part != null) {
                    resetAEMachine(part);

                    host.removePart(dir, false);

                    givePlayerItems(part.getItemStack(PartItemStack.Break));

                    ArrayList<ItemStack> drops = new ArrayList<>();
                    part.getDrops(drops, false);

                    if (!drops.isEmpty()) {
                        givePlayerItems(drops.toArray(new ItemStack[drops.size()]));
                    }
                }
            }
        }
    }

    protected void resetKeptSettings(TileEntity te) {
        if (te instanceof IRedstoneEmitter emitter) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                emitter.setRedstoneOutputStrength(side, false);
            }
        }
    }

    private static class SoundInfo {

        private int eventCount;
        private double sumX, sumY, sumZ;
    }

    private final HashMap<Pair<SoundResource, World>, SoundInfo> pendingSounds = new HashMap<>();

    protected void playSound(World world, int x, int y, int z, SoundResource sound) {
        Pair<SoundResource, World> pair = Pair.of(sound, world);

        SoundInfo info = pendingSounds.computeIfAbsent(pair, ignored -> new SoundInfo());

        info.eventCount++;
        info.sumX += x;
        info.sumY += y;
        info.sumZ += z;
    }

    protected void playSounds() {
        pendingSounds.forEach((pair, info) -> {
            GTUtility.sendSoundToPlayers(
                pair.right(),
                pair.left(),
                5.0F,
                -1,
                (int) (info.sumX / info.eventCount),
                (int) (info.sumY / info.eventCount),
                (int) (info.sumZ / info.eventCount));
        });
        pendingSounds.clear();
    }
}
