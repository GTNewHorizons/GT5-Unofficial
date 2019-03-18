package gtPlusPlus.plugin.villagers.entity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import cpw.mods.fml.common.registry.VillagerRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.villagers.NameLists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityBaseVillager extends EntityVillager {

	// public static final VillagerProfession mProfession;

	/*
	 * 
	 * Your problem is that you are extending EntityVillager, but buyingList and
	 * addDefaultEquipment are both PRIVATE members of EntityVillager - you cannot
	 * use or override them without Reflection or ASM.
	 * 
	 * What you can do, however, is override getRecipes to return your own list, but
	 * because you override EntityVillager, your mob is still using the villager's
	 * buyingList (which is NULL) when useRecipe or any other villager method is
	 * called. You either have to override every method from EntityVillager which
	 * interacts with buyingList and make it use your own list, or you need to not
	 * extend EntityVillager and just implement IMerchant instead.
	 */

	private final int mRoleID;

	public EntityBaseVillager(World aWorld){
		this(aWorld, 0);
	}

	public EntityBaseVillager(World aWorld, int aID) {
		super(aWorld, aID);
		mRoleID = aID;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound aNBT) {
		if (this.hasCustomNameTag()) {
			if (!aNBT.hasKey("aCustomName")) {
				aNBT.setString("aCustomName", this.getCommandSenderName());
			}
		}
		super.writeEntityToNBT(aNBT);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound aNBT) {
		if (aNBT.hasKey("aCustomName")) {
			if (this.getCustomNameTag() != aNBT.getString("aCustomName")) {
				this.setCustomNameTag(aNBT.getString("aCustomName"));
			}
		}
		super.readEntityFromNBT(aNBT);
	}

	@Override
	public void writeToNBT(NBTTagCompound aNBT) {
		// TODO Auto-generated method stub
		super.writeToNBT(aNBT);
	}

	@Override
	public void readFromNBT(NBTTagCompound aNBT) {
		// TODO Auto-generated method stub
		super.readFromNBT(aNBT);
	}

	@Override
	protected boolean canDespawn() {
		return !this.hasCustomNameTag();
	}

	@Override
	public void setProfession(int p_70938_1_) {
		super.setProfession(p_70938_1_);
	}

	@Override
	public int getProfession() {
		int prof = super.getProfession();
		//Logger.INFO(""+mRoleID);
		return prof < 7735 ? 7738 : prof;
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
		super.useRecipe(p_70933_1_);
	}

	@Override
	public void setRecipes(MerchantRecipeList p_70930_1_) {
		super.setRecipes(p_70930_1_);
	}

	public boolean shouldAlwaysSprint() {
		return false;
	};

	@Override
	public void onLivingUpdate() {

		// Set Custom Name
		if (!this.hasCustomNameTag()) {
			this.setCustomNameTag(NameLists.generateRandomName());
		}

		super.onLivingUpdate();

		// Make these guys always sprint
		if (shouldAlwaysSprint()) {
			if (!this.isSprinting()) {
				this.setSprinting(true);
			}
		}
		else {
			if (this.isSprinting()) {
				this.setSprinting(false);
			}
		}

	}

	@Override
	public Entity getEntityToAttack() {
		return super.getEntityToAttack();
	}

	@Override
	public boolean getAlwaysRenderNameTag() {
		return hasCustomNameTag();
	}

	@Override
	public Random getRNG() {
		return CORE.RANDOM;
	}

	@Override
	public void setSprinting(boolean bool) {
		super.setSprinting(bool);
	}

	/**
	 * Custom Shit
	 */

	protected float getField_82191_bN() {
		Field v82191 = ReflectionUtils.getField(getClass(), "field_82191_bN");
		try {
			return v82191 != null ? v82191.getFloat(this) : 0f;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return 0f;
		}
	}

	protected void setField_82191_bN(float f) {
		try {
			ReflectionUtils.setField(this, "field_82191_bN", f);
		} catch (IllegalArgumentException e) {
		}
	}

	protected boolean getNeedsInitilization() {
		Field v82191 = ReflectionUtils.getField(EntityVillager.class, "needsInitilization");
		try {
			return v82191 != null ? v82191.getBoolean(this) : false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	protected void setNeedsInitilization(boolean f) {
		try {
			ReflectionUtils.setField(this, "needsInitilization", f);
		} catch (IllegalArgumentException e) {
		}
	}

	protected MerchantRecipeList getBuyingList() {
		Field v82191;
		MerchantRecipeList o;
		v82191 = ReflectionUtils.getField(getClass(), "buyingList");
		try {
			o = (MerchantRecipeList) v82191.get(this);
			Logger.WARNING("Is BuyingList Valid? " + (v82191 != null));
			return v82191 != null ? o : null;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

	}

	protected void setBuyingList(MerchantRecipeList f) {
		try {
			Logger.WARNING("set BuyingList? "+(ReflectionUtils.setField(this, "buyingList", f)));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	protected Village getVillageObject() {
		Field v82191 = ReflectionUtils.getField(getClass(), "villageObj");
		try {
			return v82191 != null ? (Village) v82191.get(this) : null;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	protected String getLastBuyingPlayer() {
		Field v82191 = ReflectionUtils.getField(getClass(), "lastBuyingPlayer");
		try {
			return v82191 != null ? (String) v82191.get(this) : "";
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return "";
		}
	}

	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		if (getBuyingList() == null) {
			this.addDefaultEquipmentAndRecipies(1);
		}
		return getBuyingList();
	}

	/**
	 * Adjusts the probability of obtaining a given recipe being offered by a
	 * villager
	 */
	private float adjustProbability(float p_82188_1_) {
		float f1 = p_82188_1_ + getField_82191_bN();
		return f1 > 0.9F ? 0.9F - (f1 - 0.9F) : f1;
	}

	/**
	 * based on the villagers profession add items, equipment, and recipies adds
	 * par1 random items to the list of things that the villager wants to buy. (at
	 * most 1 of each wanted type is added)
	 */
	private void addDefaultEquipmentAndRecipies(int p_70950_1_) {
		if (this.getBuyingList() != null) {
			setField_82191_bN(MathHelper.sqrt_float((float) this.getBuyingList().size()) * 0.2F);
		} else {
			setField_82191_bN(0.0F);
		}

		MerchantRecipeList merchantrecipelist;
		merchantrecipelist = new MerchantRecipeList();
		VillagerRegistry.manageVillagerTrades(merchantrecipelist, this, this.getProfession(), this.rand);
		int k;
		label50:

			switch (this.getProfession()) {
			case 0:
				addPurchaseRecipe(merchantrecipelist, Items.wheat, this.rand, this.adjustProbability(0.9F));
				addPurchaseRecipe(merchantrecipelist, Item.getItemFromBlock(Blocks.wool), this.rand,
						this.adjustProbability(0.5F));
				addPurchaseRecipe(merchantrecipelist, Items.chicken, this.rand, this.adjustProbability(0.5F));
				addPurchaseRecipe(merchantrecipelist, Items.cooked_fished, this.rand, this.adjustProbability(0.4F));
				addEmeraldTrade(merchantrecipelist, Items.bread, this.rand, this.adjustProbability(0.9F));
				addEmeraldTrade(merchantrecipelist, Items.melon, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.apple, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.cookie, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.shears, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.flint_and_steel, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.cooked_chicken, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.arrow, this.rand, this.adjustProbability(0.5F));

				if (this.rand.nextFloat() < this.adjustProbability(0.5F)) {
					merchantrecipelist.add(new MerchantRecipe(new ItemStack(Blocks.gravel, 10),
							new ItemStack(Items.emerald), new ItemStack(Items.flint, 4 + this.rand.nextInt(2), 0)));
				}

				break;
			case 1:
				addPurchaseRecipe(merchantrecipelist, Items.paper, this.rand, this.adjustProbability(0.8F));
				addPurchaseRecipe(merchantrecipelist, Items.book, this.rand, this.adjustProbability(0.8F));
				addPurchaseRecipe(merchantrecipelist, Items.written_book, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Item.getItemFromBlock(Blocks.bookshelf), this.rand,
						this.adjustProbability(0.8F));
				addEmeraldTrade(merchantrecipelist, Item.getItemFromBlock(Blocks.glass), this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.compass, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.clock, this.rand, this.adjustProbability(0.2F));

				if (this.rand.nextFloat() < this.adjustProbability(0.07F)) {
					Enchantment enchantment = Enchantment.enchantmentsBookList[this.rand
					                                                           .nextInt(Enchantment.enchantmentsBookList.length)];
					int i1 = MathHelper.getRandomIntegerInRange(this.rand, enchantment.getMinLevel(),
							enchantment.getMaxLevel());
					ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i1));
					k = 2 + this.rand.nextInt(5 + i1 * 10) + 3 * i1;
					merchantrecipelist
					.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, k), itemstack));
				}

				break;
			case 2:
				addEmeraldTrade(merchantrecipelist, Items.ender_eye, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.experience_bottle, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.redstone, this.rand, this.adjustProbability(0.4F));
				addEmeraldTrade(merchantrecipelist, Item.getItemFromBlock(Blocks.glowstone), this.rand,
						this.adjustProbability(0.3F));
				Item[] aitem = new Item[] { Items.iron_sword, Items.diamond_sword, Items.iron_chestplate,
						Items.diamond_chestplate, Items.iron_axe, Items.diamond_axe, Items.iron_pickaxe,
						Items.diamond_pickaxe };
				Item[] aitem1 = aitem;
				int j = aitem.length;
				k = 0;

				while (true) {
					if (k >= j) {
						break label50;
					}

					Item item = aitem1[k];

					if (this.rand.nextFloat() < this.adjustProbability(0.05F)) {
						merchantrecipelist.add(new MerchantRecipe(new ItemStack(item, 1, 0),
								new ItemStack(Items.emerald, 2 + this.rand.nextInt(3), 0),
								EnchantmentHelper.addRandomEnchantment(this.rand, new ItemStack(item, 1, 0),
										5 + this.rand.nextInt(15))));
					}

					++k;
				}
			case 3:
				addPurchaseRecipe(merchantrecipelist, Items.coal, this.rand, this.adjustProbability(0.7F));
				addPurchaseRecipe(merchantrecipelist, Items.iron_ingot, this.rand, this.adjustProbability(0.5F));
				addPurchaseRecipe(merchantrecipelist, Items.gold_ingot, this.rand, this.adjustProbability(0.5F));
				addPurchaseRecipe(merchantrecipelist, Items.diamond, this.rand, this.adjustProbability(0.5F));
				addEmeraldTrade(merchantrecipelist, Items.iron_sword, this.rand, this.adjustProbability(0.5F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_sword, this.rand, this.adjustProbability(0.5F));
				addEmeraldTrade(merchantrecipelist, Items.iron_axe, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_axe, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.iron_pickaxe, this.rand, this.adjustProbability(0.5F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_pickaxe, this.rand, this.adjustProbability(0.5F));
				addEmeraldTrade(merchantrecipelist, Items.iron_shovel, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_shovel, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.iron_hoe, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_hoe, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.iron_boots, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_boots, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.iron_helmet, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_helmet, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.iron_chestplate, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_chestplate, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.iron_leggings, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.diamond_leggings, this.rand, this.adjustProbability(0.2F));
				addEmeraldTrade(merchantrecipelist, Items.chainmail_boots, this.rand, this.adjustProbability(0.1F));
				addEmeraldTrade(merchantrecipelist, Items.chainmail_helmet, this.rand, this.adjustProbability(0.1F));
				addEmeraldTrade(merchantrecipelist, Items.chainmail_chestplate, this.rand, this.adjustProbability(0.1F));
				addEmeraldTrade(merchantrecipelist, Items.chainmail_leggings, this.rand, this.adjustProbability(0.1F));
				break;
			case 4:
				addPurchaseRecipe(merchantrecipelist, Items.coal, this.rand, this.adjustProbability(0.7F));
				addPurchaseRecipe(merchantrecipelist, Items.porkchop, this.rand, this.adjustProbability(0.5F));
				addPurchaseRecipe(merchantrecipelist, Items.beef, this.rand, this.adjustProbability(0.5F));
				addEmeraldTrade(merchantrecipelist, Items.saddle, this.rand, this.adjustProbability(0.1F));
				addEmeraldTrade(merchantrecipelist, Items.leather_chestplate, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.leather_boots, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.leather_helmet, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.leather_leggings, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.cooked_porkchop, this.rand, this.adjustProbability(0.3F));
				addEmeraldTrade(merchantrecipelist, Items.cooked_beef, this.rand, this.adjustProbability(0.3F));
			}

		if (merchantrecipelist.isEmpty()) {
			addPurchaseRecipe(merchantrecipelist, Items.gold_ingot, this.rand, 1.0F);
		}

		Collections.shuffle(merchantrecipelist);

		if (this.getBuyingList() == null) {
			this.setBuyingList(new MerchantRecipeList());
		}

		for (int l = 0; l < p_70950_1_ && l < merchantrecipelist.size(); ++l) {
			try {
				this.getBuyingList().addToListWithCheck((MerchantRecipe) merchantrecipelist.get(l));
			}
			catch (Throwable t) {
				Logger.INFO("Villager with ID "+this.entityUniqueID.toString()+" at  |  X: "+this.posX+"   Y: "+this.posY+"   Z: "+this.posZ+" may have corrupt trades, it is advised to remove/kill it.");
			}
		}

		try {
			if (this.getBuyingList() != null) {
				for (Object g : this.getBuyingList()) {
					if (g != null) {
						if (g instanceof MerchantRecipe) {
							MerchantRecipe m = (MerchantRecipe) g;
							ItemStack selling = m.getItemToSell();
							ItemStack[] buying = new ItemStack[] {m.getItemToBuy(), m.getSecondItemToBuy() != null ? m.getSecondItemToBuy() : null};
							if (selling == null) {
								Logger.WARNING("Villager is Selling an invalid item");
							}
							else if (buying[0] == null && buying[1] == null) {
								Logger.WARNING("Villager is buying two invalid items");
							}
							else {
								Logger.WARNING("Villager is Selling x"+selling.stackSize+selling.getDisplayName()+" for x"+buying[0].stackSize+" "+buying[0].getDisplayName()+buying[1] != null ? " and for x"+buying[1].stackSize+" "+buying[1].getDisplayName() : "");
							}
						}
						else
							Logger.WARNING("Found: "+g.getClass().getName());
					}
				}
			}
			else {

			}
		}
		catch (Throwable t) {

		}

	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick() {
		if (!this.isTrading()) {
			if (this.getNeedsInitilization()) {
				if (this.getBuyingList().size() > 1) {
					Iterator<MerchantRecipe> iterator = this.getBuyingList().iterator();

					while (iterator.hasNext()) {
						MerchantRecipe merchantrecipe = (MerchantRecipe) iterator.next();

						if (merchantrecipe.isRecipeDisabled()) {
							merchantrecipe.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
						}
					}
				}

				this.addDefaultEquipmentAndRecipies(1);
				this.setNeedsInitilization(false);

				if (this.getVillageObject() != null && this.getLastBuyingPlayer() != null) {
					this.worldObj.setEntityState(this, (byte) 14);
					this.getVillageObject().setReputationForPlayer(this.getLastBuyingPlayer(), 1);
				}
			}
		}
		super.updateAITick();
	}

	public static void addEmeraldTrade(MerchantRecipeList aRecipeList, Item aItem, Random aRand, float aChance) {
		if (aRand.nextFloat() < aChance) {
			int i = getLootAmount_BlacksmithSellingList(aItem, aRand);
			ItemStack itemstack;
			ItemStack itemstack1;

			if (i < 0) {
				itemstack = new ItemStack(Items.emerald, 1, 0);
				itemstack1 = new ItemStack(aItem, -i, 0);
			} else {
				itemstack = new ItemStack(Items.emerald, i, 0);
				itemstack1 = new ItemStack(aItem, 1, 0);
			}

			aRecipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	public static void addCustomTrade(MerchantRecipeList aRecipeList, ItemStack aItem1, ItemStack aItem2, ItemStack aItem3, Random aRand, float aChance) {
		if (aRand.nextFloat() < aChance) {			
			aRecipeList.add(new MerchantRecipe(aItem1, aItem2, aItem3));
		}
	}

	private static int getLootAmount_BlacksmithSellingList(Item aItem, Random aRand) {
		Tuple tuple = (Tuple) blacksmithSellingList.get(aItem);
		return tuple == null ? 1
				: (((Integer) tuple.getFirst()).intValue() >= ((Integer) tuple.getSecond()).intValue()
				? ((Integer) tuple.getFirst()).intValue()
						: ((Integer) tuple.getFirst()).intValue() + aRand.nextInt(
								((Integer) tuple.getSecond()).intValue() - ((Integer) tuple.getFirst()).intValue()));
	}

	public static void addPurchaseRecipe(MerchantRecipeList aTradeList, Item aItem, Random aRand, float aChance) {
		if (aRand.nextFloat() < aChance) {
			aTradeList.add(new MerchantRecipe(getSimpleLootStack(aItem, aRand), Items.emerald));
		}
	}

	private static ItemStack getSimpleLootStack(Item aItem, Random aRand) {
		return new ItemStack(aItem, getLootAmount_VillagerSellingList(aItem, aRand), 0);
	}

	public static void addPurchaseRecipe(MerchantRecipeList aTradeList, Item aItem, int aMeta, Random aRand, float aChance) {
		if (aRand.nextFloat() < aChance) {
			aTradeList.add(new MerchantRecipe(getComplexLootStack(aItem, aMeta, aRand), Items.emerald));
		}
	}

	private static ItemStack getComplexLootStack(Item aItem, int aMeta, Random aRand) {
		return new ItemStack(aItem, getLootAmount_VillagerSellingList(aItem, aRand), aMeta);
	}

	private static int getLootAmount_VillagerSellingList(Item aItem, Random aRand) {
		Tuple tuple = (Tuple) villagersSellingList.get(aItem);
		return tuple == null ? 1
				: (((Integer) tuple.getFirst()).intValue() >= ((Integer) tuple.getSecond()).intValue()
				? ((Integer) tuple.getFirst()).intValue()
						: ((Integer) tuple.getFirst()).intValue() + aRand.nextInt(
								((Integer) tuple.getSecond()).intValue() - ((Integer) tuple.getFirst()).intValue()));
	}

}
