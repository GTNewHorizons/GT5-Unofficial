package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import java.util.List;

import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Behaviour_Grinder implements IItemBehaviour<GT_MetaBase_Item> {

	@Override
	public boolean onLeftClickEntity(GT_MetaBase_Item var1, ItemStack var2, EntityPlayer var3, Entity var4) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onItemUse(GT_MetaBase_Item var1, ItemStack var2, EntityPlayer var3, World var4, int var5, int var6,
			int var7, int var8, float var9, float var10, float var11) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onItemUseFirst(GT_MetaBase_Item var1, ItemStack var2, EntityPlayer var3, World var4, int var5,
			int var6, int var7, int var8, float var9, float var10, float var11) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack onItemRightClick(GT_MetaBase_Item var1, ItemStack var2, World var3, EntityPlayer var4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAdditionalToolTips(GT_MetaBase_Item var1, List<String> var2, ItemStack var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onUpdate(GT_MetaBase_Item var1, ItemStack var2, World var3, Entity var4, int var5, boolean var6) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isItemStackUsable(GT_MetaBase_Item var1, ItemStack var2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDispense(GT_MetaBase_Item var1, IBlockSource var2, ItemStack var3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack onDispense(GT_MetaBase_Item var1, IBlockSource var2, ItemStack var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasProjectile(GT_MetaBase_Item var1, SubTag var2, ItemStack var3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityArrow getProjectile(GT_MetaBase_Item var1, SubTag var2, ItemStack var3, World var4, double var5,
			double var7, double var9) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityArrow getProjectile(GT_MetaBase_Item var1, SubTag var2, ItemStack var3, World var4,
			EntityLivingBase var5, float var6) {
		// TODO Auto-generated method stub
		return null;
	}

}
