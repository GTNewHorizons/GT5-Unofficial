package gtPlusPlus.core.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.util.math.MathUtils;

public class EntityStaballoyConstruct extends EntityIronGolem {

    public EntityStaballoyConstruct(World world) {
        super(world);
        this.experienceValue = 250;
        this.getNavigator()
            .setBreakDoors(true);
        this.getNavigator()
            .setCanSwim(false);
        this.getNavigator()
            .setAvoidSun(false);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(500.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.5D);
    }

    @Override
    public boolean canAttackClass(Class<? extends Entity> clazz) {
        return !this.getClass()
            .equals(clazz);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 11) {
            return;
        }
        super.handleHealthUpdate(p_70103_1_);
    }

    @Override
    public Village getVillage() {
        return null;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int lootingChance = p_70628_2_ + 1;
        int j = this.rand.nextInt(3);
        int k;
        for (k = 0; k < j; ++k) {
            this.entityDropItem(MaterialsAlloy.STABALLOY.getBlock(1), 0f);
        }
        k = 3 + this.rand.nextInt(3);
        for (int l = 0; l < k; ++l) {
            this.entityDropItem(MaterialsAlloy.STABALLOY.getIngot(lootingChance), 0f);
            if (MathUtils.randInt(0, 2) == 0) {
                this.entityDropItem(MaterialsAlloy.STABALLOY.getPlate(lootingChance), 0f);
            }
        }
    }

    @Override
    public boolean isPlayerCreated() {
        return false;
    }

    @Override
    public void setPlayerCreated(boolean p_70849_1_) {}

    @Override
    protected boolean canDespawn() {
        return true;
    }

    @Override
    public void onEntityUpdate() {
        if (!this.isImmuneToFire) {
            this.isImmuneToFire = true;
        }
        super.onEntityUpdate();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {}

    @Override
    protected void setOnFireFromLava() {
        extinguish();
    }

    @Override
    public void setFire(int p_70015_1_) {
        extinguish();
    }

    @Override
    protected void dealFireDamage(int p_70081_1_) {}

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }
}
