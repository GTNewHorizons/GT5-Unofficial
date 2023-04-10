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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.interfaces.IItemUpdatable;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_AddToolTips;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_CanPlace;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_GetMaxStackSize;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_HasMultiBlockMachineRelevantData;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_IgnoreEntityCollisionWhenPlacing;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_OnlyPlaceableWhenSneaking;

public class MultiTileEntityItemInternal extends ItemBlock implements IFluidContainerItem, IItemUpdatable {

    public final MultiTileEntityBlockInternal mBlock;

    public MultiTileEntityItemInternal(Block aBlock) {
        super(aBlock);
        setMaxDamage(0);
        setHasSubtypes(true);
        mBlock = (MultiTileEntityBlockInternal) aBlock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer == null) {
            aList.add("INVALID ITEM!");
            return;
        }
        if (tTileEntityContainer.mTileEntity instanceof IMTE_AddToolTips) {
            try {
                ((IMTE_AddToolTips) tTileEntityContainer.mTileEntity).addToolTips(aList, aStack, aF3_H);
            } catch (Throwable e) {
                GT_FML_LOGGER.error("addInformation", e);
            }
        }
        final NBTTagCompound aNBT = aStack.getTagCompound();
        CoverableTileEntity.addInstalledCoversInformation(aNBT, aList);
        // TODO: Add anything else relevant
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (MultiTileEntityClassContainer tClass : mBlock.mMultiTileEntityRegistry.mRegistrations) {
            if (!tClass.mHidden) {
                if (((IMultiTileEntity) tClass.mCanonicalTileEntity)
                    .getSubItems(mBlock, aItem, aTab, aList, tClass.mID)) {
                    aList.add(mBlock.mMultiTileEntityRegistry.getItem(tClass.mID));
                }
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide,
        float aHitX, float aHitY, float aHitZ) {
        if (aY < 0 || aY > aWorld.getHeight()) return false;
        try {
            final Block tClickedBlock = aWorld.getBlock(aX, aY, aZ);
            if (tClickedBlock instanceof BlockSnow && (aWorld.getBlockMetadata(aX, aY, aZ) & 7) < 1) {
                aSide = SIDE_TOP;
            } else if (tClickedBlock != Blocks.vine && tClickedBlock != Blocks.tallgrass
                && tClickedBlock != Blocks.deadbush
                && !tClickedBlock.isReplaceable(aWorld, aX, aY, aZ)) {
                    aX += ForgeDirection.VALID_DIRECTIONS[aSide].offsetX;
                    aY += ForgeDirection.VALID_DIRECTIONS[aSide].offsetY;
                    aZ += ForgeDirection.VALID_DIRECTIONS[aSide].offsetZ;
                }
            final Block tReplacedBlock = aWorld.getBlock(aX, aY, aZ);

            if (!tReplacedBlock.isReplaceable(aWorld, aX, aY, aZ)
                || !mBlock.canReplace(aWorld, aX, aY, aZ, aSide, aStack)) return false;
            if (aStack.stackSize == 0 || (aPlayer != null && !aPlayer.canPlayerEdit(aX, aY, aZ, aSide, aStack)))
                return false;

            final MultiTileEntityContainer aMTEContainer = mBlock.mMultiTileEntityRegistry
                .getNewTileEntityContainer(aWorld, aX, aY, aZ, aStack);

            if (aMTEContainer != null
                && (aPlayer == null || aPlayer.isSneaking()
                    || !(aMTEContainer.mTileEntity instanceof IMTE_OnlyPlaceableWhenSneaking)
                    || !((IMTE_OnlyPlaceableWhenSneaking) aMTEContainer.mTileEntity).onlyPlaceableWhenSneaking())
                && (aWorld.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1))
                    || (aMTEContainer.mTileEntity instanceof IMTE_IgnoreEntityCollisionWhenPlacing
                        && ((IMTE_IgnoreEntityCollisionWhenPlacing) aMTEContainer.mTileEntity)
                            .ignoreEntityCollisionWhenPlacing(
                                aStack,
                                aPlayer,
                                aWorld,
                                aX,
                                aY,
                                aZ,
                                (byte) aSide,
                                aHitX,
                                aHitY,
                                aHitZ)))
                && (!(aMTEContainer.mTileEntity instanceof IMTE_CanPlace) || ((IMTE_CanPlace) aMTEContainer.mTileEntity)
                    .canPlace(aStack, aPlayer, aWorld, aX, aY, aZ, (byte) aSide, aHitX, aHitY, aHitZ))
                && aWorld.setBlock(aX, aY, aZ, aMTEContainer.mBlock, 15 - aMTEContainer.mBlockMetaData, 2)) {
                aMTEContainer.setMultiTile(aWorld, aX, aY, aZ);

                try {
                    if (((IMultiTileEntity) aMTEContainer.mTileEntity)
                        .onPlaced(aStack, aPlayer, aWorld, aX, aY, aZ, (byte) aSide, aHitX, aHitY, aHitZ)) {
                        aWorld.playSoundEffect(
                            aX + 0.5,
                            aY + 0.5,
                            aZ + 0.5,
                            aMTEContainer.mBlock.stepSound.func_150496_b(),
                            (aMTEContainer.mBlock.stepSound.getVolume() + 1) / 2,
                            aMTEContainer.mBlock.stepSound.getPitch() * 0.8F);
                    }
                } catch (Throwable e) {
                    GT_FML_LOGGER.error("onPlaced", e);
                }
                try {
                    if (aMTEContainer.mTileEntity instanceof IMTE_HasMultiBlockMachineRelevantData
                        && (((IMTE_HasMultiBlockMachineRelevantData) aMTEContainer.mTileEntity)
                            .hasMultiBlockMachineRelevantData())) {
                        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
                    }
                } catch (Throwable e) {
                    GT_FML_LOGGER.error("causeMachineUpdate", e);
                }
                try {
                    if (!aWorld.isRemote) {
                        aWorld.notifyBlockChange(aX, aY, aZ, tReplacedBlock);
                        aWorld.func_147453_f /* updateNeighborsAboutBlockChange */(aX, aY, aZ, aMTEContainer.mBlock);
                    }
                } catch (Throwable e) {
                    GT_FML_LOGGER.error("notifyBlockChange", e);
                }
                try {
                    ((IMultiTileEntity) aMTEContainer.mTileEntity).onTileEntityPlaced();
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
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("onItemUse", e);
        }
        return false;
    }

    @Override
    public void updateItemStack(ItemStack aStack) {
        final MultiTileEntityClassContainer tContainer = mBlock.mMultiTileEntityRegistry.getClassContainer(aStack);
        if (tContainer == null) return;
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IItemUpdatable) {
            ((IItemUpdatable) tTileEntityContainer.mTileEntity).updateItemStack(aStack);
        }
    }

    @Override
    public void updateItemStack(ItemStack aStack, World aWorld, int aX, int aY, int aZ) {
        final MultiTileEntityClassContainer tContainer = mBlock.mMultiTileEntityRegistry.getClassContainer(aStack);
        if (tContainer == null) return;
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IItemUpdatable) {
            ((IItemUpdatable) tTileEntityContainer.mTileEntity).updateItemStack(aStack, aWorld, aX, aY, aZ);
        }
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        final MultiTileEntityClassContainer tContainer = mBlock.mMultiTileEntityRegistry.getClassContainer(aStack);
        if (tContainer == null) return 1;
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IMTE_GetMaxStackSize) {
            return ((IMTE_GetMaxStackSize) tTileEntityContainer.mTileEntity)
                .getMaxStackSize(aStack, tContainer.mStackSize);
        }
        return tContainer.mStackSize;
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        updateItemStack(aStack);
    }

    @Override
    public FluidStack getFluid(ItemStack aStack) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IFluidContainerItem) {
            final FluidStack rFluid = ((IFluidContainerItem) tTileEntityContainer.mTileEntity).getFluid(aStack);
            updateItemStack(aStack);
            return rFluid;
        }
        return null;
    }

    @Override
    public int getCapacity(ItemStack aStack) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IFluidContainerItem) {
            final int rCapacity = ((IFluidContainerItem) tTileEntityContainer.mTileEntity).getCapacity(aStack);
            updateItemStack(aStack);
            return rCapacity;
        }
        return 0;
    }

    @Override
    public int fill(ItemStack aStack, FluidStack aFluid, boolean aDoFill) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IFluidContainerItem) {
            final int tFilled = ((IFluidContainerItem) tTileEntityContainer.mTileEntity).fill(aStack, aFluid, aDoFill);
            updateItemStack(aStack);
            return tFilled;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack aStack, int aMaxDrain, boolean aDoDrain) {
        final MultiTileEntityContainer tTileEntityContainer = mBlock.mMultiTileEntityRegistry
            .getNewTileEntityContainer(aStack);
        if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity instanceof IFluidContainerItem) {
            final FluidStack rFluid = ((IFluidContainerItem) tTileEntityContainer.mTileEntity)
                .drain(aStack, aMaxDrain, aDoDrain);
            updateItemStack(aStack);
            return rFluid;
        }
        return null;
    }

    @Override
    public boolean func_150936_a /* canPlaceAtSide */(World aWorld, int aX, int aY, int aZ, int aSide,
        EntityPlayer aPlayer, ItemStack aStack) {
        return true;
    }

    @Override
    public final String getUnlocalizedName() {
        return mBlock.mMultiTileEntityRegistry.mNameInternal;
    }

    @Override
    public final String getUnlocalizedName(ItemStack aStack) {
        return mBlock.mMultiTileEntityRegistry.mNameInternal + "." + getDamage(aStack);
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
