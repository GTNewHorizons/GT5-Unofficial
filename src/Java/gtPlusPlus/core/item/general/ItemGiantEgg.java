package gtPlusPlus.core.item.general;

import static gtPlusPlus.core.lib.CORE.RANDOM;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;

import gtPlusPlus.core.entity.item.ItemEntityGiantEgg;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class ItemGiantEgg extends BaseItemBurnable {

	public ItemGiantEgg(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize, int maxDmg,
			String description, String oredictName, int burnTime, int meta) {
		super(unlocalizedName, displayName, creativeTab, stackSize, maxDmg, description, oredictName, burnTime, meta);
		this.setMaxStackSize(1);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		String localName = super.getItemStackDisplayName(aStack);
		nbtWork(aStack);
		int size = 1;
		if (NBTUtils.hasKey(aStack, "size")){
			size = NBTUtils.getInteger(aStack, "size");			
			return ""+size+" "+localName;
		}
		return "?? "+localName;
	}

	private static ItemStack mCorrectEgg;
	private static ItemStack mCorrectStemCells;

	@Override
	public void onUpdate(ItemStack aStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {

		if (world.isRemote) {
			super.onUpdate(aStack, world, entityHolding, p_77663_4_, p_77663_5_);
			return;
		}
		try {
			boolean player = (entityHolding != null && entityHolding instanceof EntityPlayer);

			if (player) {
				NBTUtils.setBoolean(aStack, "playerHeld", true);
			}
			else {
				NBTUtils.setBoolean(aStack, "playerHeld", false);
			}		

			nbtWork(aStack);

			int age = NBTUtils.hasKey(aStack, "mAge") ? NBTUtils.getInteger(aStack, "mAge") : 0;
			if (player) {
				NBTUtils.setInteger(aStack, "mAge", age+1);

				//Set the correct egg for future hatches
				if (mCorrectEgg == null) {
					if (NBTUtils.hasKey(aStack, "mAge") && NBTUtils.hasKey(aStack, "mEggAge")) {
						if (NBTUtils.getInteger(aStack, "mAge") >= NBTUtils.getInteger(aStack, "mEggAge")) {
							for (int g=0;g<128;g++) {
								ItemStack mSpawn = ItemUtils.simpleMetaStack(Items.spawn_egg, g, 1);
								if (mSpawn != null) {
									String s = ("" + StatCollector.translateToLocal(mSpawn.getUnlocalizedName() + ".name")).trim();
									String s1 = EntityList.getStringFromID(mSpawn.getItemDamage());
									if (s1 != null){
										s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
										if (s1.equalsIgnoreCase("bigChickenFriendly")) {
											mCorrectEgg = mSpawn;
											break;
										}
									}
								}						
							}
						}
					}
				}

				if (mCorrectEgg != null) {
					if (NBTUtils.hasKey(aStack, "mAge") && NBTUtils.hasKey(aStack, "mEggAge")) {
						if (NBTUtils.getInteger(aStack, "mAge") >= NBTUtils.getInteger(aStack, "mEggAge")) {
							if (MathUtils.randInt(0, 1000) >= 990) {
								if (NBTUtils.hasKey(aStack, "size")) {
									if ((NBTUtils.getInteger(aStack, "size")+1) >= MathUtils.randInt(0, 9)) {
										((EntityPlayer) entityHolding).inventory.addItemStackToInventory((mCorrectEgg));
										((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
									}
									else {
										((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);								
									}
								}	
							}
						}
					}
				}
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		super.onUpdate(aStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}

	@Override
	public void onCreated(ItemStack p_77622_1_, World p_77622_2_, EntityPlayer p_77622_3_) {
		super.onCreated(p_77622_1_, p_77622_2_, p_77622_3_);
	}

	public void nbtWork(ItemStack aStack) {
		if (NBTUtils.hasKey(aStack, "playerHeld")) {
			boolean player = NBTUtils.getBoolean(aStack, "playerHeld");			
			if (player && !NBTUtils.hasKey(aStack, "size")) {
				NBTUtils.setInteger(aStack, "size", MathUtils.randInt(1, 8));
			}
			if (player && !NBTUtils.hasKey(aStack, "mEggAge") && NBTUtils.hasKey(aStack, "size")) {
				NBTUtils.setInteger(aStack, "mEggAge", ((MathUtils.randInt(8000, 16000)*NBTUtils.getInteger(aStack, "size"))/2));
			}

			if (player && NBTUtils.getTagCompound(aStack, "GT.CraftingComponents") == null) {
				if (mCorrectStemCells == null) {
					if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 28) {
						ItemList xl = ItemList.valueOf("Circuit_Chip_Stemcell");
						if (xl != null && xl.hasBeenSet()) {
							mCorrectStemCells = xl.get(1);
						}
					}
					else {
						mCorrectStemCells = ItemUtils.getSimpleStack(Items.egg, 2);
					}
				}
				if (mCorrectStemCells != null) {
					int mSize = NBTUtils.getInteger(aStack, "size");
					float mSizeMod = (MathUtils.randInt(-5, 5)/10);
					mSize += mSizeMod;
					mSize = Math.max(mSize, 1);
					ItemStack eggYolks[] = new ItemStack[mSize];
					for (int u=0;u<mSize;u++) {
						eggYolks[u] = ItemUtils.getSimpleStack(mCorrectStemCells, MathUtils.randInt(1, 4));
					}

					int mexpected = 0;
					for (ItemStack e : eggYolks) {
						if (e != null) {
							mexpected += e.stackSize;
						}
					}
					if (mexpected > 0) {
						NBTUtils.setInteger(aStack, "mExpected", mexpected);
					}

					NBTUtils.writeItemsToGtCraftingComponents(aStack, eggYolks, true);					
				}
			}
			if (player && NBTUtils.getTagCompound(aStack, "GT.CraftingComponents") != null) {

			}
		}		
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {

		if (location instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) location;

			if (itemstack == null) {
				return null;
			}
			else if (itemstack.stackSize == 0) {
				return null;
			}
			else {
				ItemEntityGiantEgg entityitem = new ItemEntityGiantEgg(world, player.posX, player.posY - 0.30000001192092896D + (double)player.getEyeHeight(), player.posZ, itemstack);
				entityitem.delayBeforeCanPickup = 40;
				entityitem.func_145799_b(player.getCommandSenderName());
				float f = 0.1F;
				float f1;
				f = 0.3F;
				entityitem.motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f);
				entityitem.motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f);
				entityitem.motionY = (double)(-MathHelper.sin(player.rotationPitch / 180.0F * (float)Math.PI) * f + 0.1F);
				f = 0.02F;
				f1 = RANDOM.nextFloat() * (float)Math.PI * 2.0F;
				f *= RANDOM.nextFloat();
				entityitem.motionX += Math.cos((double)f1) * (double)f;
				entityitem.motionY += (double)((RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.1F);
				entityitem.motionZ += Math.sin((double)f1) * (double)f;
				return entityitem;
			}
		}
		return null;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		int size = 0;
		if (NBTUtils.hasKey(stack, "size")){
			size = NBTUtils.getInteger(stack, "size");
		}
		int age = 0;
		if (NBTUtils.hasKey(stack, "mAge")){
			age = NBTUtils.getInteger(stack, "mAge");
		}
		int life = 0;
		if (NBTUtils.hasKey(stack, "mEggAge")){
			life = NBTUtils.getInteger(stack, "mEggAge");
		}
		int expected = 0;
		if (NBTUtils.hasKey(stack, "mExpected")){
			expected = NBTUtils.getInteger(stack, "mExpected");
		}
		list.add("Egg Size: "+size+" ounces");
		list.add("Expected Stem Cells: "+expected);
		list.add("Age: "+(age/20)+"s"+" / "+(life/20)+"s");
		list.add("Larger eggs take longer to hatch,");
		list.add("but have a better chance of hatching.");		
		super.addInformation(stack, aPlayer, list, bool);
	}

}
