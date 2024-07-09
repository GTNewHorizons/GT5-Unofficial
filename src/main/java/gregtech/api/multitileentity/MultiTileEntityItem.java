package gregtech.api.multitileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.SIDE_TOP;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import gregtech.api.multitileentity.interfaces.IItemUpdatable;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;

public class MultiTileEntityItem extends ItemBlock implements IFluidContainerItem, IItemUpdatable {

    public final MultiTileEntityBlockRegistryInternal mBlock;

    public MultiTileEntityItem(Block aBlock) {
        super(aBlock);
        setMaxDamage(0);
        setHasSubtypes(true);
        mBlock = (MultiTileEntityBlockRegistryInternal) aBlock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer == null) {
            aList.add("INVALID ITEM!");
            return;
        }
        try {
            tTileEntityContainer.tileEntity.addToolTips(aList, aStack, aF3_H);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("addInformation", e);
        }
        final NBTTagCompound aNBT = aStack.getTagCompound();
        CoverableTileEntity.addInstalledCoversInformation(aNBT, aList);
        // TODO: Add anything else relevant
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (MultiTileEntityClassContainer tClass : mBlock.registry.registrations) {
            if (!tClass.hidden && ((IMultiTileEntity) tClass.referenceTileEntity)
                .getSubItems(mBlock, aItem, aTab, aList, tClass.muteID)) {
                aList.add(mBlock.registry.getItem(tClass.muteID));
            }
        }
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
                || !mBlock.canReplace(aWorld, aX, aY, aZ, ordinalSide, aStack)) {
                return false;
            }

            if (aStack.stackSize == 0 || (!aPlayer.canPlayerEdit(aX, aY, aZ, ordinalSide, aStack))) {
                return false;
            }

            final MultiTileEntityContainer MuTEContainer = mBlock.registry
                .getNewTileEntityContainer(aWorld, aX, aY, aZ, aStack);

            if (MuTEContainer == null) return false;

            if (!aWorld.setBlock(aX, aY, aZ, MuTEContainer.block, 15 - MuTEContainer.blockMetaData, 2)) {
                return false;
            }

            MuTEContainer.setMultiTile(aWorld, aX, aY, aZ);

            try {
                if (((IMultiTileEntity) MuTEContainer.tileEntity)
                    .onPlaced(aStack, aPlayer, aWorld, aX, aY, aZ, side, aHitX, aHitY, aHitZ)) {
                    aWorld.playSoundEffect(
                        aX + 0.5,
                        aY + 0.5,
                        aZ + 0.5,
                        MuTEContainer.block.stepSound.func_150496_b(),
                        (MuTEContainer.block.stepSound.getVolume() + 1) / 2,
                        MuTEContainer.block.stepSound.getPitch() * 0.8F);
                }
            } catch (Throwable e) {
                GT_FML_LOGGER.error("onPlaced", e);
            }
            // spotless:off
            /*
             * Used to be behind `IMTE_HasMultiBlockMachineRelevantData`
             *    Add back if needed
             */
            // try {
            //     GregTech_API.causeMachineUpdate(world, x, y, z);
            // } catch (Throwable e) {
            //     GT_FML_LOGGER.error("causeMachineUpdate", e);
            // }
            // spotless:on
            try {
                if (!aWorld.isRemote) {
                    aWorld.notifyBlockChange(aX, aY, aZ, tReplacedBlock);
                    aWorld.func_147453_f /* updateNeighborsAboutBlockChange */(aX, aY, aZ, MuTEContainer.block);
                }
            } catch (Throwable e) {
                GT_FML_LOGGER.error("notifyBlockChange", e);
            }
            try {
                ((IMultiTileEntity) MuTEContainer.tileEntity).onTileEntityPlaced();
            } catch (Throwable e) {
                GT_FML_LOGGER.error("onTileEntityPlaced", e);
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
    public void updateItemStack(ItemStack aStack) {
        final MultiTileEntityClassContainer tContainer = mBlock.registry.getClassContainer(aStack);
        if (tContainer == null) return;
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.tileEntity instanceof IItemUpdatable itemUpdatable) {
            itemUpdatable.updateItemStack(aStack);
        }
    }

    @Override
    public void updateItemStack(ItemStack aStack, World aWorld, int aX, int aY, int aZ) {
        final MultiTileEntityClassContainer tContainer = mBlock.registry.getClassContainer(aStack);
        if (tContainer == null) return;
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.tileEntity instanceof IItemUpdatable itemUpdatable) {
            itemUpdatable.updateItemStack(aStack, aWorld, aX, aY, aZ);
        }
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        final MultiTileEntityClassContainer tContainer = mBlock.registry.getClassContainer(aStack);
        if (tContainer == null) return 1;
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        return tContainer.maxStackSize;
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        updateItemStack(aStack);
    }

    @Override
    public FluidStack getFluid(ItemStack aStack) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer != null
            && tTileEntityContainer.tileEntity instanceof IFluidContainerItem fluidContainerItem) {
            final FluidStack rFluid = fluidContainerItem.getFluid(aStack);
            updateItemStack(aStack);
            return rFluid;
        }
        return null;
    }

    @Override
    public int getCapacity(ItemStack aStack) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer != null
            && tTileEntityContainer.tileEntity instanceof IFluidContainerItem fluidContainerItem) {
            final int rCapacity = fluidContainerItem.getCapacity(aStack);
            updateItemStack(aStack);
            return rCapacity;
        }
        return 0;
    }

    @Override
    public int fill(ItemStack aStack, FluidStack aFluid, boolean aDoFill) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer != null
            && tTileEntityContainer.tileEntity instanceof IFluidContainerItem fluidContainerItem) {
            final int tFilled = fluidContainerItem.fill(aStack, aFluid, aDoFill);
            updateItemStack(aStack);
            return tFilled;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack aStack, int aMaxDrain, boolean aDoDrain) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.registry.getCachedTileEntityContainer(aStack);
        if (tTileEntityContainer != null
            && tTileEntityContainer.tileEntity instanceof IFluidContainerItem fluidContainerItem) {
            final FluidStack rFluid = fluidContainerItem.drain(aStack, aMaxDrain, aDoDrain);
            updateItemStack(aStack);
            return rFluid;
        }
        return null;
    }

    @Override
    public boolean func_150936_a /* canPlaceAtSide */(World aWorld, int aX, int aY, int aZ, int ordinalSide,
        EntityPlayer aPlayer, ItemStack aStack) {
        return true;
    }

    @Override
    public final String getUnlocalizedName() {
        return mBlock.registry.getInternalName();
    }

    @Override
    public final String getUnlocalizedName(ItemStack aStack) {
        return mBlock.registry.getInternalName() + "." + getDamage(aStack);
    }

    @Override
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aRegister) {
        /* Empty */
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMeta) {
        itemIcon = Items.bread.getIconFromDamage(0);
        return itemIcon; /* Fixes Eating Animation Particles. */
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean getShareTag() {
        return true; // just to be sure
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial) {
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
