package gregtech.client.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityCamera extends EntityLivingBase {

    public EntityCamera(World world) {
        super(world);
        this.noClip = false;
        this.setSize(0.2F, 0.2F);
        this.yOffset = 1.62F;
    }

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float hw = this.width / 2.0F;
        float hh = this.height / 2.0F;
        this.boundingBox.setBounds(x - hw, y - hh, z - hw, x + hw, y + hh, z + hw);
    }

    @Override
    public void moveEntity(double dx, double dy, double dz) {
        super.moveEntity(dx, dy, dz);
        this.posY = (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void onUpdate() {
        this.lastTickPosX = this.prevPosX;
        this.lastTickPosY = this.prevPosY;
        this.lastTickPosZ = this.prevPosZ;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {}

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[5];
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {}

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {}
}
