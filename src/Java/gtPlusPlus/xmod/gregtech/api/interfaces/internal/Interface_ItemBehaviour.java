package gtPlusPlus.xmod.gregtech.api.interfaces.internal;

import java.util.List;

import gregtech.api.enums.SubTag;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaItem_Base;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface Interface_ItemBehaviour<E extends Item> {
	public boolean canDispense(E aItem, IBlockSource aSource, ItemStack aStack);

	public List<String> getAdditionalToolTips(E aItem, List<String> aList, ItemStack aStack);

	public EntityArrow getProjectile(E aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, double aX,
			double aY, double aZ);

	public EntityArrow getProjectile(E aItem, SubTag aProjectileType, ItemStack aStack, World aWorld,
			EntityLivingBase aEntity, float aSpeed);

	public boolean hasProjectile(Gregtech_MetaItem_Base aItem, SubTag aProjectileType, ItemStack aStack);

	public boolean isItemStackUsable(E aItem, ItemStack aStack);

	public ItemStack onDispense(E aItem, IBlockSource aSource, ItemStack aStack);

	public ItemStack onItemRightClick(E aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer);

	public boolean onItemUse(E aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
			int aSide, float hitX, float hitY, float hitZ);

	public boolean onItemUseFirst(E aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
			int aSide, float hitX, float hitY, float hitZ);

	public boolean onLeftClickEntity(E aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity);

	public void onUpdate(E aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand);
}