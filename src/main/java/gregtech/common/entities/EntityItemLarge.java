package gregtech.common.entities;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityItemLarge extends EntityItem {

    public EntityItemLarge(World world) {
        super(world);
    }

    public EntityItemLarge(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityItemLarge(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }

    public static void registerCommon() {
        EntityRegistry.registerModEntity(
            EntityItemLarge.class,
            "EntityItemLarge",
            EntityID.LargeItem.ID,
            GregTech.ID,
            64,
            3,
            true);
    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        RenderingRegistry.registerEntityRenderingHandler(EntityItemLarge.class, new RenderItem());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);

        ItemStack item = this.getEntityItem();
        if (item != null) {
            tagCompound.getCompoundTag("Item")
                .setInteger("Count", item.stackSize);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        isDead = false;

        NBTTagCompound itemTag = tagCompound.getCompoundTag("Item");
        ItemStack item = ItemStack.loadItemStackFromNBT(itemTag);
        item.stackSize = itemTag.getInteger("Count");

        this.setEntityItemStack(item);

        item = getDataWatcher().getWatchableObjectItemStack(10);

        if (item == null || item.stackSize <= 0) {
            this.setDead();
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.worldObj.isRemote) {
            if (this.delayBeforeCanPickup > 0) {
                return;
            }

            EntityItemPickupEvent event = new EntityItemPickupEvent(entityIn, this);

            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }

            ItemStack itemstack = this.getEntityItem();
            int i = itemstack.stackSize;

            if (this.delayBeforeCanPickup <= 0 && (func_145798_i() == null || lifespan - this.age <= 200
                || func_145798_i().equals(entityIn.getCommandSenderName()))) {
                entityIn.inventory.addItemStackToInventory(itemstack);

                if (i == itemstack.stackSize) {
                    // couldn't store any items
                    return;
                }

                FMLCommonHandler.instance()
                    .firePlayerItemPickupEvent(entityIn, this);

                this.worldObj.playSoundAtEntity(
                    entityIn,
                    "random.pop",
                    0.2F,
                    ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

                // this just barely doesn't work, but it fixes the desync mostly so it's good enough
                entityIn.openContainer.detectAndSendChanges();
                entityIn.inventoryContainer.detectAndSendChanges();

                if (itemstack.stackSize <= 0) {
                    entityIn.onItemPickup(this, i - itemstack.stackSize);
                    this.setDead();
                }
            }
        }
    }
}
