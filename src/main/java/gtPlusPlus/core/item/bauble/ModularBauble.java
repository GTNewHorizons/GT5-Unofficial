package gtPlusPlus.core.item.bauble;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import baubles.api.BaubleType;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils.BT;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils.Modifiers;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class ModularBauble extends BaseBauble {

	@SideOnly(Side.CLIENT)
	private IIcon mTextureAmulet;
	@SideOnly(Side.CLIENT)
	private IIcon mTextureRing;
	@SideOnly(Side.CLIENT)
	private IIcon mTextureBelt;
	private IIcon iconArray[] = new IIcon[3];
	@SideOnly(Side.CLIENT)
	private IIcon mfallback;

	public ModularBauble() {
		super(BaubleType.AMULET, "Modular Bauble");
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {

		// Get Stats
		int mStatlevel = 0;
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DAMAGE)) > 0) {
			if (mStatlevel > 0 && mStatlevel < 20) {
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "AD" + mStatlevel, 1, 0));
			} else if (mStatlevel > 20 && mStatlevel < 45) {
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "AD" + mStatlevel, 2, 0));
			} else if (mStatlevel >= 45 && mStatlevel < 75) {
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "AD" + mStatlevel, 4, 0));
			} else if (mStatlevel >= 75 && mStatlevel < 99) {
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "AD" + mStatlevel, 8, 0));
			} else if (mStatlevel >= 100) {
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "AD" + mStatlevel, 16, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DEF)) > 0) {
			if (mStatlevel > 0 && mStatlevel < 20) {
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "BD" + mStatlevel, 1, 0));
			} else if (mStatlevel > 20 && mStatlevel < 45) {
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "BD" + mStatlevel, 2, 0));
			} else if (mStatlevel > 45 && mStatlevel < 75) {
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "BD" + mStatlevel, 3, 0));
			} else if (mStatlevel > 75 && mStatlevel < 99) {
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "BD" + mStatlevel, 6, 0));
			} else if (mStatlevel >= 100) {
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "BD" + mStatlevel, 10, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HP)) > 0) {
			if (mStatlevel > 0 && mStatlevel <= 100) {
				int bonus = mStatlevel / 5;
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "HP" + mStatlevel, bonus * 2, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_SPEED)) > 0) {
			if (mStatlevel > 0 && mStatlevel < 20) {
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "SP" + mStatlevel, 1, 0));
			} else if (mStatlevel > 20 && mStatlevel < 45) {
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "SP" + mStatlevel, 2, 0));
			} else if (mStatlevel > 45 && mStatlevel < 75) {
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "SP" + mStatlevel, 3, 0));
			} else if (mStatlevel > 75 && mStatlevel < 99) {
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "SP" + mStatlevel, 4, 0));
			} else if (mStatlevel >= 100) {
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),
						new AttributeModifier(getBaubleUUID(stack), "SP" + mStatlevel, 5, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_MINING)) > 0) {
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HOLY)) > 0) {
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		// Bauble Type
		if (ModularArmourUtils.getBaubleType(stack) == BaubleType.AMULET) {
			list.add(EnumChatFormatting.GRAY + "Current Form: " + EnumChatFormatting.RED + "Amulet"
					+ EnumChatFormatting.GRAY + ".");
			list.add(EnumChatFormatting.GRAY + "You can change this into a Ring or a Belt.");
		} else if (ModularArmourUtils.getBaubleType(stack) == BaubleType.RING) {
			list.add(EnumChatFormatting.GRAY + "Current Form: " + EnumChatFormatting.RED + "Ring"
					+ EnumChatFormatting.GRAY + ".");
			list.add(EnumChatFormatting.GRAY + "You can change this into an Amulet or a Belt.");
		} else if (ModularArmourUtils.getBaubleType(stack) == BaubleType.BELT) {
			list.add(EnumChatFormatting.GRAY + "Current Form: " + EnumChatFormatting.RED + "Belt"
					+ EnumChatFormatting.GRAY + ".");
			list.add(EnumChatFormatting.GRAY + "You can change this into a Ring or an Amulet.");
		}

		// Get Stats
		int mStatlevel = 0;
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DAMAGE)) > 0) {
			list.add(EnumChatFormatting.GRAY + "Damage Boost: " + EnumChatFormatting.DARK_RED + mStatlevel
					+ EnumChatFormatting.GRAY + "/100.");
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HP)) > 0) {
			list.add(EnumChatFormatting.GRAY + "Health Boost: " + EnumChatFormatting.RED + mStatlevel
					+ EnumChatFormatting.GRAY + "/100. Bonus " + (mStatlevel / 5) + " hearts.");
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_SPEED)) > 0) {
			list.add(EnumChatFormatting.GRAY + "Speed Boost: " + EnumChatFormatting.WHITE + mStatlevel
					+ EnumChatFormatting.GRAY + "/100.");
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_MINING)) > 0) {
			list.add(EnumChatFormatting.GRAY + "Mining Boost: " + EnumChatFormatting.DARK_GRAY + mStatlevel
					+ EnumChatFormatting.GRAY + "/100.");
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HOLY)) > 0) {
			list.add(EnumChatFormatting.GRAY + "Holy Boost: " + EnumChatFormatting.GOLD + mStatlevel
					+ EnumChatFormatting.GRAY + "/100.");
		}

		// Defence Boost
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DEF)) > 0) {
			list.add(EnumChatFormatting.GRAY + "Defence Boost: " + EnumChatFormatting.BLUE + mStatlevel
					+ EnumChatFormatting.GRAY + "/100.");

			if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54))) {

				if (mStatlevel >= 1) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Cactus"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 10) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Falling Blocks"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 20) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Wall Suffocation"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 35) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Drowning"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 50) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Starvation"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 60) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Falling"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 75) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Lava"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 80) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Magic"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 95) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Wither"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 100) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Fire"
							+ EnumChatFormatting.GRAY + ".");
				}
				if (mStatlevel >= 100) {
					list.add(EnumChatFormatting.GRAY + "Protected From: " + EnumChatFormatting.BLUE + "Void"
							+ EnumChatFormatting.GRAY + ".");
				}
				
			}
			else {
				list.add(EnumChatFormatting.GRAY + "<Press Shift to view protections>");
			}
		}

		if (NBTUtils.getBotanicaSoulboundOwner(stack) != null) {
			if (!NBTUtils.getBotanicaSoulboundOwner(stack).equals("")) {
				list.add(EnumChatFormatting.GRAY + "Owner: " + EnumChatFormatting.GREEN
						+ NBTUtils.getBotanicaSoulboundOwner(stack) + EnumChatFormatting.GRAY + ".");
			}
		}
		super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean addDamageNegation(DamageSource damageSource, ItemStack aStack) {

		this.clearDamageNegation();
		int mStatlevel = 0;
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(aStack, Modifiers.BOOST_HOLY)) > 0) {
			if (mStatlevel >= 1) {
				addDamageNegation(DamageSource.cactus);
			}
			if (mStatlevel >= 10) {
				addDamageNegation(DamageSource.fallingBlock);
			}
			if (mStatlevel >= 20) {
				addDamageNegation(DamageSource.inWall);
			}
			if (mStatlevel >= 35) {
				addDamageNegation(DamageSource.drown);
			}
			if (mStatlevel >= 50) {
				addDamageNegation(DamageSource.starve);
			}
			if (mStatlevel >= 60) {
				addDamageNegation(DamageSource.fall);
			}
			if (mStatlevel >= 75) {
				addDamageNegation(DamageSource.lava);
			}
			if (mStatlevel >= 80) {
				addDamageNegation(DamageSource.magic);
			}
			if (mStatlevel >= 95) {
				addDamageNegation(DamageSource.wither);
			}
			if (mStatlevel >= 100) {
				addDamageNegation(DamageSource.inFire);
			}
			if (mStatlevel >= 100) {
				addDamageNegation(DamageSource.onFire);
			}
			if (mStatlevel >= 100) {
				addDamageNegation(DamageSource.outOfWorld);
			}
		}
		return super.addDamageNegation(damageSource, null);
	}

	@Override
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
		if (ModularArmourUtils.getBaubleTypeID(arg0) == BT.TYPE_AMULET.getID()) {
			this.SetBaubleType(BT.TYPE_AMULET);
		} else if (ModularArmourUtils.getBaubleTypeID(arg0) == BT.TYPE_RING.getID()) {
			this.SetBaubleType(BT.TYPE_RING);
		} else if (ModularArmourUtils.getBaubleTypeID(arg0) == BT.TYPE_BELT.getID()) {
			this.SetBaubleType(BT.TYPE_BELT);
		} else {
			this.SetBaubleType(BT.TYPE_RING);
		}
		if (PlayerUtils.isPlayerOP((EntityPlayer) arg1)) {
			return true; // Let OPs wear other peoples shit.
		}

		String mOwner;
		if (NBTUtils.getBotanicaSoulboundOwner(arg0) == null || NBTUtils.getBotanicaSoulboundOwner(arg0).equals("")) {
			return true;
		} else if ((mOwner = NBTUtils.getBotanicaSoulboundOwner(arg0)) != null) {
			String mPlayerName = arg1.getCommandSenderName();
			if (mOwner.toLowerCase().equals(mPlayerName.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return ModularArmourUtils.getBaubleType(arg0);
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			if (NBTUtils.getBotanicaSoulboundOwner(stack) == null
					|| NBTUtils.getBotanicaSoulboundOwner(stack).equals("")) {
				NBTUtils.setBotanicaSoulboundOwner(stack, entity.getCommandSenderName());
			}
		}
		super.onEquipped(stack, entity);
	}

	/*@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		if (usingItem == null) {
			return mfallback;
		}
		try {
			if (stack.getItemDamage() == 0) {
				return mfallback = mTextureAmulet;
			}
			else if (stack.getItemDamage() == 1) {
				return mfallback = mTextureRing;
			}
			else if (stack.getItemDamage() == 2) {
				return mfallback = mTextureBelt;
			} else {
				return mfallback = mTextureRing;
			}
		} catch (Throwable t) {
			return mfallback = mTextureRing;
		}
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (meta < this.iconArray.length && this.iconArray[meta] != null){
			return this.iconArray[meta];
		}
		return this.mfallback;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < 3; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		this.mfallback = register.registerIcon("miscutils" + ":" + "itemAmulet");
		// you cannot initialize iconArray when declared nor in the constructor,
		// as it is client-side only, so do it here:
		if (LoadedMods.Thaumcraft) {
			iconArray[0] = register.registerIcon("thaumcraft" + ":" + "bauble_amulet");
			iconArray[1] = register.registerIcon("thaumcraft" + ":" + "bauble_ring");
			iconArray[2] = register.registerIcon("thaumcraft" + ":" + "bauble_belt");
		} else {
			iconArray[0] = register.registerIcon("miscutils" + ":" + "itemAmulet");
			iconArray[1] = register.registerIcon("miscutils" + ":" + "itemRingWearable");
			iconArray[2] = register.registerIcon("miscutils" + ":" + "itemBelt");
		}
		
		mTextureAmulet = iconArray[0];
		mTextureRing = iconArray[1];
		mTextureBelt = iconArray[2];

	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public Item setFull3D() {
		// TODO Auto-generated method stub
		return super.setFull3D();
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

}
