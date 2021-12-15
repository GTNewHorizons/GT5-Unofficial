package gtPlusPlus.core.item.wearable.armour.tinfoil;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.wearable.armour.ArmourLoader;
import gtPlusPlus.core.item.wearable.armour.base.BaseArmourHelm;
import gtPlusPlus.core.lib.CORE;
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
import net.minecraft.world.World;

public class ArmourTinFoilHat extends BaseArmourHelm {

	public IIcon iconHelm;

	public ArmourTinFoilHat() {
		super(ArmourLoader.TinFoilArmour, 0);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir) {
		this.iconHelm = ir.registerIcon(CORE.MODID + ":itemHatTinFoil");
	}

	@Override
	public int getRenderIndex() {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return this.iconHelm;
	}

	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return CORE.MODID + ":textures/models/TinFoil.png";
	}

	public EnumRarity getRarity(ItemStack itemstack) {
		return EnumRarity.uncommon;
	}

	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return super.getArmorDisplay(player, armor, slot);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
		aList.add("DoomSquirter's protection against cosmic radiation!");
		aList.add("General paranoia makes the wearer unable to collect xp");
		aList.add("Movement speed is also reduced, to keep you safe");
		aList.add("This hat may also have other strange powers");
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
			if (player instanceof EntityPlayer) {

				// Apply Slow
				if (!GT_Utility.getPotion(player, Potion.moveSlowdown.id)) {
					player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 2, 1, true));
				}

				// Move Xp orbs away
				try {
					AxisAlignedBB box = player.boundingBox;
					box.maxX = player.posX + 5;
					box.maxY = player.posY + 5;
					box.maxZ = player.posZ + 5;
					box.minX = player.posX - 5;
					box.minY = player.posY - 5;
					box.minZ = player.posZ - 5;
					@SuppressWarnings("unchecked")
					List<Entity> g = world.getEntitiesWithinAABBExcludingEntity(player, box);
					if (g.size() > 0) {
						for (Entity e : g) {
							if (e != null) {
								if (
										!EntityXPOrb.class.isInstance(e) &&
										!EntityBoat.class.isInstance(e) &&
										!EntitySnowball.class.isInstance(e) &&
										!EntityFireball.class.isInstance(e) &&
										!EntityEgg.class.isInstance(e) &&
										!EntityExpBottle.class.isInstance(e) &&
										!EntityEnderEye.class.isInstance(e) &&
										!EntityEnderPearl.class.isInstance(e)
										) {
									continue;									
								}
								else {
									//Logger.INFO("Found "+e.getClass().getName());
									double distX = player.posX - e.posX;
									double distZ = player.posZ - e.posZ;
									double distY = e.posY + 1.5D - player.posY;
									double dir = Math.atan2(distZ, distX);
									double speed = 1F / e.getDistanceToEntity(player) * 0.5;
									speed = -speed;
									if (distY < 0) {
										e.motionY += speed;
									}
									e.motionX = Math.cos(dir) * speed;
									e.motionZ = Math.sin(dir) * speed;
								}


							}
						}
					}
				} catch (Throwable t) {
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
