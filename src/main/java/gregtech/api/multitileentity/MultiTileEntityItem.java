package gregtech.api.multitileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.SIDE_TOP;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.interfaces.IItemUpdatable;

public class MultiTileEntityItem extends ItemBlock implements IFluidContainerItem, IItemUpdatable {

    public final MultiTileEntityBlock block;

    public MultiTileEntityItem(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        this.block = (MultiTileEntityBlock) block;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean aF3_H) {
        MultiTileEntity te = block.getRegistry()
            .getCachedTileEntity(stack);
        if (te != null) {
            te.addToolTip(list);
        }
        final NBTTagCompound aNBT = stack.getTagCompound();
        CoverableTileEntity.addInstalledCoversInformation(aNBT, list);
    }

    @Override
    public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int ordinalSide, float aHitX, float aHitY, float aHitZ) {

        if (aY < 0 || aY > aWorld.getHeight()) return false;

        if (aPlayer == null) return false;

        try {
            ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            final Block tClickedBlock = aWorld.getBlock(aX, aY, aZ);

            if (tClickedBlock instanceof BlockSnow && (aWorld.getBlockMetadata(aX, aY, aZ) & 7) < 1) {
                ordinalSide = SIDE_TOP;
                side = ForgeDirection.UP;
            } else if (tClickedBlock != Blocks.vine && tClickedBlock != Blocks.tallgrass
                && tClickedBlock != Blocks.deadbush
                && !tClickedBlock.isReplaceable(aWorld, aX, aY, aZ)) {
                    aX += side.offsetX;
                    aY += side.offsetY;
                    aZ += side.offsetZ;
                }
            final Block tReplacedBlock = aWorld.getBlock(aX, aY, aZ);

            if (!tReplacedBlock.isReplaceable(aWorld, aX, aY, aZ)
                || !block.canReplace(aWorld, aX, aY, aZ, ordinalSide, aStack)) {
                return false;
            }

            if (aStack.stackSize == 0 || (!aPlayer.canPlayerEdit(aX, aY, aZ, ordinalSide, aStack))) {
                return false;
            }

            if (!aWorld.setBlock(aX, aY, aZ, block, Items.feather.getDamage(aStack), 2)) {
                return false;
            }

            try {
                if (!aWorld.isRemote) {
                    aWorld.notifyBlockChange(aX, aY, aZ, tReplacedBlock);
                    aWorld.func_147453_f(aX, aY, aZ, block);/* updateNeighborsAboutBlockChange */
                }
            } catch (Throwable e) {
                GT_FML_LOGGER.error("notifyBlockChange", e);
            }
            try {
                aWorld.func_147451_t /* updateAllLightTypes */(aX, aY, aZ);
            } catch (Throwable e) {
                GT_FML_LOGGER.error("updateAllLightTypes", e);
            }

            aStack.stackSize--;
            return true;

        } catch (Throwable e) {
            GT_FML_LOGGER.error("onItemUse", e);
        }
        return false;
    }

    @Override
    public void updateItemStack(ItemStack aStack) {}

    @Override
    public void updateItemStack(ItemStack aStack, World aWorld, int aX, int aY, int aZ) {}

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        return 64;
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        updateItemStack(aStack);
    }

    @Override
    public FluidStack getFluid(ItemStack aStack) {
        MultiTileEntity te = block.getRegistry()
            .getCachedTileEntity(aStack);
        if (te instanceof IFluidContainerItem fluidContainerItem) {
            final FluidStack rFluid = fluidContainerItem.getFluid(aStack);
            updateItemStack(aStack);
            return rFluid;
        }
        return null;
    }

    @Override
    public int getCapacity(ItemStack aStack) {
        MultiTileEntity te = block.getRegistry()
            .getCachedTileEntity(aStack);
        if (te instanceof IFluidContainerItem fluidContainerItem) {
            final int rCapacity = fluidContainerItem.getCapacity(aStack);
            updateItemStack(aStack);
            return rCapacity;
        }
        return 0;
    }

    @Override
    public int fill(ItemStack aStack, FluidStack aFluid, boolean aDoFill) {
        MultiTileEntity te = block.getRegistry()
            .getCachedTileEntity(aStack);
        if (te instanceof IFluidContainerItem fluidContainerItem) {
            final int tFilled = fluidContainerItem.fill(aStack, aFluid, aDoFill);
            updateItemStack(aStack);
            return tFilled;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack aStack, int aMaxDrain, boolean aDoDrain) {
        MultiTileEntity te = block.getRegistry()
            .getCachedTileEntity(aStack);
        if (te instanceof IFluidContainerItem fluidContainerItem) {
            final FluidStack rFluid = fluidContainerItem.drain(aStack, aMaxDrain, aDoDrain);
            updateItemStack(aStack);
            return rFluid;
        }
        return null;
    }

    @Override
    public final String getUnlocalizedName() {
        super.getUnlocalizedName();
        return block.getRegistry().internalName;
    }

    @Override
    public final String getUnlocalizedName(ItemStack aStack) {
        return block.getRegistry().getClassContainer(Items.feather.getDamage(aStack)).getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMeta) {
        super.getIconFromDamage(aMeta);
        itemIcon = Items.bread.getIconFromDamage(0);
        return itemIcon; /* Fixes Eating Animation Particles. */
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack aStack) {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        return null;
    }

    @Override
    public final boolean hasContainerItem(ItemStack aStack) {
        return getContainerItem(aStack) != null;
    }

    @Override
    public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {
        return false;
    }
}
