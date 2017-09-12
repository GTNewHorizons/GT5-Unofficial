package gtPlusPlus.core.slots;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.AchievementList;

public class SlotCraftingNoCollect extends SlotCrafting
{
	/** The craft matrix inventory linked to this result slot. */
	private final IInventory craftMatrix;
	/** The player that is using the GUI where this slot resides. */
	private EntityPlayer thePlayer;
	/** The number of items that have been crafted so far. Gets passed to ItemStack.onCrafting before being reset. */
	private int amountCrafted;

	public SlotCraftingNoCollect(EntityPlayer player, IInventory inventory, IInventory inventory2, int x, int y, int z)
	{
		super(player, inventory, inventory2, x, y, z);
		this.thePlayer = player;
		this.craftMatrix = inventory;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack p_75214_1_)
	{
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
	 * stack.
	 */
	public ItemStack decrStackSize(int amount)
	{
		if (this.getHasStack())
		{
			this.amountCrafted += Math.min(amount, this.getStack().stackSize);
		}

		return super.decrStackSize(amount);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
	 * internal count then calls onCrafting(item).
	 */
	protected void onCrafting(ItemStack output, int amount)
	{
		this.amountCrafted += amount;
		this.onCrafting(output);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	protected void onCrafting(ItemStack output)
	{
		output.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
		this.amountCrafted = 0;

		if (output.getItem() == Item.getItemFromBlock(Blocks.crafting_table))
		{
			this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
		}

		if (output.getItem() instanceof ItemPickaxe)
		{
			this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
		}

		if (output.getItem() == Item.getItemFromBlock(Blocks.furnace))
		{
			this.thePlayer.addStat(AchievementList.buildFurnace, 1);
		}

		if (output.getItem() instanceof ItemHoe)
		{
			this.thePlayer.addStat(AchievementList.buildHoe, 1);
		}

		if (output.getItem() == Items.bread)
		{
			this.thePlayer.addStat(AchievementList.makeBread, 1);
		}

		if (output.getItem() == Items.cake)
		{
			this.thePlayer.addStat(AchievementList.bakeCake, 1);
		}

		if (output.getItem() instanceof ItemPickaxe && ((ItemPickaxe)output.getItem()).func_150913_i() != Item.ToolMaterial.WOOD)
		{
			this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		}

		if (output.getItem() instanceof ItemSword)
		{
			this.thePlayer.addStat(AchievementList.buildSword, 1);
		}

		if (output.getItem() == Item.getItemFromBlock(Blocks.enchanting_table))
		{
			this.thePlayer.addStat(AchievementList.enchantments, 1);
		}

		if (output.getItem() == Item.getItemFromBlock(Blocks.bookshelf))
		{
			this.thePlayer.addStat(AchievementList.bookcase, 1);
		}
	}

	public void onPickupFromSlot(EntityPlayer player, ItemStack output)
	{
		FMLCommonHandler.instance().firePlayerCraftingEvent(player, output, craftMatrix);
		this.onCrafting(output);

		/*for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);

            if (itemstack1 != null)
            {
                this.craftMatrix.decrStackSize(i, 1);

                if (itemstack1.getItem().hasContainerItem(itemstack1))
                {
                    ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

                    if (itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage())
                    {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
                        continue;
                    }

                    if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !this.thePlayer.inventory.addItemStackToInventory(itemstack2))
                    {
                        if (this.craftMatrix.getStackInSlot(i) == null)
                        {
                            this.craftMatrix.setInventorySlotContents(i, itemstack2);
                        }
                        else
                        {
                            this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
                        }
                    }
                }
                }
            }*/        
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}
}