package gtPlusPlus.core.item.materials;

import static gtPlusPlus.core.util.minecraft.ItemUtils.getSimpleStack;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.handler.Recipes.DecayableRecipe;
import gtPlusPlus.core.item.base.BaseItemTickable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class DustDecayable extends BaseItemTickable {

	private final Item turnsIntoItem;
	private final int radLevel;
	
	public DustDecayable(String unlocal, int colour, int maxTicks, String[] desc1, Item turnsInto, int radLevel) {
		super(true, true, unlocal, colour, (maxTicks/1), desc1);
		this.turnsIntoItem = turnsInto;
		this.radLevel = radLevel;
		GT_OreDictUnificator.registerOre(unlocal, ItemUtils.getSimpleStack(this));
		new DecayableRecipe(maxTicks, getSimpleStack(this), getSimpleStack(turnsInto));
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		String gt = "gregtech" + ":" + "materialicons/"+"NUCLEAR"+"/" + "dust";
		this.mIcon[0] = reg.registerIcon(gt);
		String gt2 = "gregtech" + ":" + "materialicons/"+"NUCLEAR"+"/" + "dust" + "_OVERLAY";
		this.mIcon[1] = reg.registerIcon(gt2);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		super.addInformation(stack, player, list, bool);
		if (this.radLevel > 0) {
			list.add(CORE.GT_Tooltip_Radioactive);
		}
	}
	
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (world == null || iStack == null) {
			return;
		}	
		if (world.isRemote) {
			return;
		}
		
		if (entityHolding instanceof EntityPlayer){
			if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode){
				EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.radLevel, world, entityHolding);	
			}
		}
		boolean a1, a2;
		
		a1 = this.getIsActive(world, iStack);
		a2 = tickItemTag(world, iStack);
		
		if (!a1 && !a2) {
			if (entityHolding instanceof EntityPlayer){
				ItemStack replacement = ItemUtils.getSimpleStack(getDecayResult());
				//Logger.INFO("Replacing "+iStack.getDisplayName()+" with "+replacement.getDisplayName()+".");
				final ItemStack tempTransform = replacement;
				if (iStack.stackSize > 1){
					int u = iStack.stackSize;
					tempTransform.stackSize = u;
					((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
					for (int l=0;l<u;l++){
						((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
					}

				}
				else {
					tempTransform.stackSize=1;
					((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
					((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
				}
			}
		}
	}

	public Item getDecayResult() {
		return turnsIntoItem;
	}
	
}
