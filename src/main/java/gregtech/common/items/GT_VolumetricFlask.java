
package gregtech.common.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.items.GT_Generic_Item;
import ic2.core.util.LiquidUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

import static gregtech.api.enums.GT_Values.RES_PATH_ITEM;
import static ic2.core.util.LiquidUtil.*;


public class GT_VolumetricFlask extends GT_Generic_Item implements IFluidContainerItem {
    private final int maxCapacity;
    @SideOnly(Side.CLIENT)
    public IIcon iconWindow;

    public GT_VolumetricFlask(String unlocalized, String english, int maxCapacity) {
        super(unlocalized, english, null);
        this.maxCapacity = maxCapacity;
        setMaxStackSize(16);
        setNoRepair();
        if (Loader.isModLoaded("NotEnoughItems")) {
            for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
                if (fluid != null) {
                    ItemStack stack = new ItemStack(this);
                    setCapacity(stack, getMaxCapacity());
                    fill(stack, new FluidStack(fluid, Integer.MAX_VALUE), true);
                    codechicken.nei.api.API.hideItem(stack);
                }
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && isEmpty(stack) && getMovingObjectPositionFromPlayer(world, player, true) == null)
            player.openGui(GT_Values.GT, 1010, world, 0, 0, 0);
        return super.onItemRightClick(stack, world, player);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
        if (player instanceof FakePlayer) {
            return false;
        }
        if (world.isRemote)
            return false;
        if (interactWithTank(stack, player, world, x, y, z, side)) {
            return true;
        }
        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, true);
        if (mop == null) {
            return false;
        }
        if (mop.typeOfHit == MovingObjectType.BLOCK) {
            x = mop.blockX;
            y = mop.blockY;
            z = mop.blockZ;
            if (!world.canMineBlock(player, x, y, z) || !player.canPlayerEdit(x, y, z, mop.sideHit, stack)) {
                return false;
            }
            if (collectFluidBlock(stack, player, world, x, y, z)) {
                return true;
            }
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[mop.sideHit];
            FluidStack fluidStack = drainContainerStack(stack, player, 1000, true);
            if (placeFluid(fluidStack, world, x, y, z) || (player.canPlayerEdit(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, mop.sideHit, stack) && placeFluid(fluidStack, world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))) {
                if (!player.capabilities.isCreativeMode)
                    drainContainerStack(stack, player, 1000, false);
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(ItemStack stack) {
        return getFluid(stack) == null;
    }

    public int getFreeSpace(ItemStack stack) {
        int capacity = getCapacity(stack);
        if (capacity > 0) {
            FluidStack fluidStack = getFluid(stack);
            return fluidStack == null ? capacity : capacity - fluidStack.amount;
        }
        return 0;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public int getCapacity(ItemStack stack) {
        int capacity = 1000;
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Capacity", 3))
                capacity = nbt.getInteger("Capacity");
        }
        return Math.min(getMaxCapacity(), capacity);
    }

    @Override
    public void registerIcons(IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        iconWindow = aIconRegister.registerIcon(RES_PATH_ITEM + "gt.Volumetric_Flask.window");
    }

    public void setCapacity(ItemStack stack, int capacity) {
        capacity = Math.min(capacity, getMaxCapacity());
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            stack.setTagCompound(nbt = new NBTTagCompound());
        }
        nbt.setInteger("Capacity", capacity);
    }

    public FluidStack getFluid(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Fluid", 10))
                return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Fluid"));
        }
        return null;
    }

    public void setFluid(ItemStack stack, FluidStack fluidStack) {
        boolean removeFluid = (fluidStack == null) || (fluidStack.amount <= 0);
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            if (removeFluid)
                return;
            stack.setTagCompound(nbt = new NBTTagCompound());
        }
        if (removeFluid) {
            nbt.removeTag("Fluid");
            if (nbt.hasNoTags()) {
                stack.setTagCompound(null);
            }
        } else {
            nbt.setTag("Fluid", fluidStack.writeToNBT(new NBTTagCompound()));
        }
    }

    public int fill(ItemStack stack, FluidStack resource, boolean doFill) {
        if (stack.stackSize != 1)
            return 0;
        if ((resource == null) || (resource.amount <= 0)) {
            return 0;
        }
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack == null) {
            fluidStack = new FluidStack(resource, 0);
        } else if (!fluidStack.isFluidEqual(resource)) {
            return 0;
        }
        int amount = Math.min(getCapacity(stack) - fluidStack.amount, resource.amount);
        if ((doFill) && (amount > 0)) {
            fluidStack.amount += amount;
            setFluid(stack, fluidStack);
        }
        return amount;
    }

    public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain) {
        if (stack.stackSize != 1)
            return null;
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack == null)
            return null;
        maxDrain = Math.min(fluidStack.amount, maxDrain);
        if (doDrain) {
            fluidStack.amount -= maxDrain;
            setFluid(stack, fluidStack);
        }
        return new FluidStack(fluidStack, maxDrain);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean b) {
        super.addInformation(stack, player, info, b);
        FluidStack fs = getFluid(stack);
        if (fs != null) {
            info.add(String.format("< %s, %d mB >", FluidRegistry.getFluidName(fs), fs.amount));
        } else {
            info.add(String.format("< Empty, %d mB >", getCapacity(stack)));
        }
        info.add("Rightclick on air to set volume (only while empty)");
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List itemList) {
        itemList.add(new ItemStack(this));
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            if (fluid != null) {
                ItemStack stack = new ItemStack(this);
                setCapacity(stack, getMaxCapacity());
                fill(stack, new FluidStack(fluid, Integer.MAX_VALUE), true);
                itemList.add(stack);
            }
        }
    }

    private boolean interactWithTank(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
        if (world.isRemote) {
            return false;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof IFluidHandler)) {
            return false;
        }
        IFluidHandler handler = (IFluidHandler) te;
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        FluidStack fs = this.getFluid(stack);
        int capacity = getCapacity(stack);
        if (fs != null && (!player.isSneaking() || fs.amount >= capacity)) {
            int amount = handler.fill(dir, fs, false);
            if (amount > 0) {
                fs = LiquidUtil.drainContainerStack(stack, player, amount, false);
                if (fs != null && fs.amount > 0) {
                    handler.fill(dir, fs, true);
                }
            }
        } else {
            int amount = fs == null ? capacity : capacity - fs.amount;
            FluidStack input = handler.drain(dir, amount, false);
            if (input != null && input.amount > 0) {
                amount = LiquidUtil.fillContainerStack(stack, player, input, false);
                if (amount > 0) {
                    handler.drain(dir, amount, true);
                }
            }
        }
        return true;
    }

    private boolean collectFluidBlock(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof IFluidBlock) {
            IFluidBlock liquid = (IFluidBlock) block;
            if (liquid.canDrain(world, x, y, z)) {
                FluidStack fluid = liquid.drain(world, x, y, z, false);
                int amount = fillContainerStack(stack, player, fluid, true);
                if (amount == fluid.amount) {
                    fillContainerStack(stack, player, fluid, false);
                    liquid.drain(world, x, y, z, true);
                    return true;
                }
            }
        } else if (world.getBlockMetadata(x, y, z) == 0) {
            FluidStack fluid = null;
            if (block != Blocks.water && block != Blocks.flowing_water) {
                if (block == Blocks.lava || block == Blocks.flowing_lava) {
                    fluid = new FluidStack(FluidRegistry.LAVA, 1000);
                }
            } else {
                fluid = new FluidStack(FluidRegistry.WATER, 1000);
            }

            if (fluid != null) {
                int amount = fillContainerStack(stack, player, fluid, true);
                if (amount == fluid.amount) {
                    fillContainerStack(stack, player, fluid, false);
                    world.setBlockToAir(x, y, z);
                    return true;
                }
            }
        }
        return false;
    }
}