package gtPlusPlus.core.item.wearable.armor;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.wearable.base.BaseItemWearable;

public class ItemArmorTinFoilHat extends BaseItemWearable {

    public static ArmorMaterial tinFoilArmor = EnumHelper.addArmorMaterial("TINFOIL", 5, new int[] { 1, 1, 1, 1 }, 50);

    public IIcon iconHelm;

    public ItemArmorTinFoilHat() {
        super(tinFoilArmor, 0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.iconHelm = ir.registerIcon(GTPlusPlus.ID + ":itemHatTinFoil");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.iconHelm;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return GTPlusPlus.ID + ":textures/models/TinFoil.png";
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return super.getArmorDisplay(player, armor, slot);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
        Collections.addAll(aList, GTUtility.breakLines(StatCollector.translateToLocal("GTPP.tooltip.tin_foil_hat")));
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
        int slot) {
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack p_111207_1_, EntityPlayer p_111207_2_,
        EntityLivingBase p_111207_3_) {
        return super.itemInteractionForEntity(p_111207_1_, p_111207_2_, p_111207_3_);
    }

    @Override
    public void onUpdate(ItemStack aStack, World aWorld, Entity aEntity, int p_77663_4_, boolean p_77663_5_) {
        super.onUpdate(aStack, aWorld, aEntity, p_77663_4_, p_77663_5_);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return super.onEntityItemUpdate(entityItem);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (itemStack != null && player != null && world != null && !world.isRemote) {
            // Apply Slow
            if (!player.isPotionActive(Potion.moveSlowdown.id)) {
                player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 2, 1, true));
            }
            // Move Xp orbs away
            final AxisAlignedBB box = player.getBoundingBox();
            if (box != null) {
                List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(
                    player,
                    box.expand(5, 5, 5),
                    e -> e instanceof EntityXPOrb || e instanceof EntityBoat
                        || e instanceof EntitySnowball
                        || e instanceof EntityFireball
                        || e instanceof EntityEgg
                        || e instanceof EntityExpBottle
                        || e instanceof EntityEnderEye
                        || e instanceof EntityEnderPearl);
                for (Entity e : list) {
                    final float dist = e.getDistanceToEntity(player);
                    if (dist == 0) continue;
                    double distX = player.posX - e.posX;
                    double distZ = player.posZ - e.posZ;
                    double distY = e.posY + 1.5D - player.posY;
                    double dir = Math.atan2(distZ, distX);
                    double speed = 1F / dist * 0.5;
                    speed = -speed;
                    if (distY < 0) {
                        e.motionY += speed;
                    }
                    e.motionX = Math.cos(dir) * speed;
                    e.motionZ = Math.sin(dir) * speed;
                }
            }
        }
        super.onArmorTick(world, player, itemStack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
}
