package gtPlusPlus.core.util.minecraft;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InventoryUtils {

    private static final Random mRandom = new Random();

    public static void dropInventoryItems(World world, int x, int y, int z, Block block) {
        TileEntity tileentity = world.getTileEntity(x, y, z);

        if (tileentity instanceof IInventory aTileInv && ((IInventory) tileentity).getSizeInventory() > 0) {

            int aMinSlot = 0;
            int aMaxSlot = aTileInv.getSizeInventory() - 1;

            for (int i1 = aMinSlot; i1 < aMaxSlot; ++i1) {
                ItemStack itemstack = aTileInv.getStackInSlot(i1);

                if (itemstack != null) {
                    float f = mRandom.nextFloat() * 0.8F + 0.1F;
                    float f1 = mRandom.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = mRandom.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world
                        .spawnEntityInWorld(entityitem)) {
                        int j1 = mRandom.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(
                            world,
                            x + f,
                            y + f1,
                            z + f2,
                            new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) mRandom.nextGaussian() * f3;
                        entityitem.motionY = (float) mRandom.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) mRandom.nextGaussian() * f3;

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem()
                                .setTagCompound(
                                    (NBTTagCompound) itemstack.getTagCompound()
                                        .copy());
                        }
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }
    }

}
