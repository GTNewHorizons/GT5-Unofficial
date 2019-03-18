package gtPlusPlus.core.item.general.throwables;

import java.util.List;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.projectile.EntityThrowableBomb;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemThrowableBomb extends CoreItem {

	private static AutoMap<ItemStack> mLighters = new AutoMap<ItemStack>();
	public static IIcon[] icons = new IIcon[10];
	
	public ItemThrowableBomb() {
		super("gtpp.throwable.bomb", "Bomb", AddToCreativeTab.tabMisc, 16, 0, new String[] {"Just like Bomberman", "Have a fire source in inventory to prime"}, EnumRarity.uncommon, EnumChatFormatting.GRAY, false, null);
		this.setHasSubtypes(true);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		
		if (mLighters.isEmpty()) {
			mLighters.put(ItemUtils.getSimpleStack(Items.flint_and_steel));
			mLighters.put(ItemList.Tool_Lighter_Invar_Full.get(1));
			mLighters.put(ItemList.Tool_Lighter_Invar_Used.get(1));
			mLighters.put(ItemList.Tool_Lighter_Platinum_Full.get(1));
			mLighters.put(ItemList.Tool_Lighter_Platinum_Used.get(1));
			mLighters.put(ItemUtils.getSimpleStack(ModItems.itemBasicFireMaker));
		}
		
		//Unlit
		if (item.getItemDamage() == 0) {
			boolean hasLighter = false;
			for (ItemStack aPlaySlot : player.inventory.mainInventory) {
				if (aPlaySlot != null) {
					for (ItemStack aLighter : mLighters) {
						if (GT_Utility.areStacksEqual(aPlaySlot, aLighter)) {
							hasLighter = true;
							break;
						}					
					}
				}
			}		
			if (hasLighter) {
				item.setItemDamage(1);
			}
		}
		//Lit
		else if (item.getItemDamage() == 1) {
			if (!player.capabilities.isCreativeMode) {
				--item.stackSize;
			}
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntityThrowableBomb(world, player));
			}
			/*if (item.stackSize <= 0) {
				item = null;
			}*/
		}
		return item;		
	}
	


	@Override
	public void registerIcons(IIconRegister reg) {
		icons[0] = reg.registerIcon(CORE.MODID + ":" + "bomb");
		icons[1] = reg.registerIcon(CORE.MODID + ":" + "bomb_lit");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[meta];
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 2; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		String aLitStatus = "unlit";
		if (stack.getItemDamage() == 0) {
			aLitStatus = EnumChatFormatting.BLUE+"Unlit";
		}
		else if (stack.getItemDamage() == 1) {
			aLitStatus = EnumChatFormatting.RED+"Lit";
		}		
		list.add(EnumChatFormatting.GOLD+"Fuse Status: "+aLitStatus);
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		// TODO Auto-generated method stub
		return super.getItemStackDisplayName(p_77653_1_);
	}
	
	
	
}