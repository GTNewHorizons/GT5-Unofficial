package gregtech.api.multitileentity;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.SIDE_TOP;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;

public class MultiTileEntityItem extends ItemBlock {

    public final MultiTileEntityBlock block;

    public MultiTileEntityItem(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        this.block = (MultiTileEntityBlock) block;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean F3_H) {
        final IMultiTileEntity mute = block.getRegistry()
            .getReferenceTileEntity(stack);
        if (mute == null) {
            list.add("INVALID ITEM!");
            return;
        }
        try {
            mute.addToolTips(list, stack, F3_H);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("addInformation", e);
        }
        final NBTTagCompound aNBT = stack.getTagCompound();
        CoverableTileEntity.addInstalledCoversInformation(aNBT, list);
        // TODO: Add anything else relevant
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (MultiTileEntityClassContainer tClass : block.getRegistry().registrations) {
            if (!tClass.hidden && ((IMultiTileEntity) tClass.getReferenceTileEntity())
                .getSubItems(block, aItem, aTab, aList, tClass.getMuteID())) {
                aList.add(
                    block.getRegistry()
                        .getItem(tClass.getMuteID()));
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int ordinalSide,
        float hitX, float hitY, float hitZ) {

        if (y < 0 || y > world.getHeight()) return false;

        if (player == null) return false;

        try {
            ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            final Block clickedBlock = world.getBlock(x, y, z);

            if (clickedBlock instanceof BlockSnow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
                ordinalSide = SIDE_TOP;
                side = ForgeDirection.UP;
            } else if (clickedBlock != Blocks.vine && clickedBlock != Blocks.tallgrass
                && clickedBlock != Blocks.deadbush
                && !clickedBlock.isReplaceable(world, x, y, z)) {
                    x += side.offsetX;
                    y += side.offsetY;
                    z += side.offsetZ;
                }
            final Block tReplacedBlock = world.getBlock(x, y, z);

            if (!tReplacedBlock.isReplaceable(world, x, y, z)
                || !block.canReplace(world, x, y, z, ordinalSide, stack)) {
                return false;
            }

            if (stack.stackSize == 0 || (!player.canPlayerEdit(x, y, z, ordinalSide, stack))) {
                return false;
            }
            // final TileEntity tileEntity = block.getRegistry().getNewTileEntity(world, x, y, z, stack);

            if (!world.setBlock(x, y, z, block, Items.feather.getDamage(stack), 2)) {
                return false;
            }
            final TileEntity te = world.getTileEntity(x, y, z);
            if (!(te instanceof IMultiTileEntity mute)) {
                throw new IllegalStateException("TileEntity is not an IMultiTileEntity");
            }
            // MuTEContainer.setMultiTile(world, x, y, z);

            try {
                if (mute.onPlaced(stack, player, world, x, y, z, side, hitX, hitY, hitZ)) {
                    world.playSoundEffect(
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        block.stepSound.func_150496_b(),
                        (block.stepSound.getVolume() + 1) / 2,
                        block.stepSound.getPitch() * 0.8F);
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
            //     GregTechAPI.causeMachineUpdate(world, x, y, z);
            // } catch (Throwable e) {
            //     GT_FML_LOGGER.error("causeMachineUpdate", e);
            // }
            // spotless:on
            try {
                if (!world.isRemote) {
                    world.notifyBlockChange(x, y, z, tReplacedBlock);
                    world.func_147453_f /* updateNeighborsAboutBlockChange */(x, y, z, block);
                }
            } catch (Throwable e) {
                GT_FML_LOGGER.error("notifyBlockChange", e);
            }
            try {
                mute.onTileEntityPlaced();
            } catch (Throwable e) {
                GT_FML_LOGGER.error("onTileEntityPlaced", e);
            }
            try {
                world.func_147451_t /* updateAllLightTypes */(x, y, z);
            } catch (Throwable e) {
                GT_FML_LOGGER.error("updateAllLightTypes", e);
            }

            stack.stackSize--;
            return true;

        } catch (Throwable e) {
            GT_FML_LOGGER.error("onItemUse", e);
        }
        return false;
    }

    @Override
    public boolean func_150936_a /* canPlaceAtSide */(World aWorld, int aX, int aY, int aZ, int ordinalSide,
        EntityPlayer aPlayer, ItemStack aStack) {
        return true;
    }

    @Override
    public final String getUnlocalizedName() {
        return block.getRegistry()
            .getInternalName();
    }

    @Override
    public final String getUnlocalizedName(ItemStack aStack) {
        return block.getRegistry()
            .getInternalName() + "."
            + getDamage(aStack);
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
