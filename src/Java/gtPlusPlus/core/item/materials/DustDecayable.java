package gtPlusPlus.core.item.materials;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.core.item.base.BaseItemTickable;
import gtPlusPlus.core.item.base.ore.BaseOreComponent.ComponentTypes;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class DustDecayable extends BaseItemTickable {

	private final Item turnsIntoItem;
	
	public DustDecayable(String unlocal, int colour, int maxTicks, String desc1, String desc2, Item turnsInto) {
		super(true, unlocal, colour, maxTicks, desc1, desc2);
		this.turnsIntoItem = turnsInto;
		GT_OreDictUnificator.registerOre(unlocal, ItemUtils.getSimpleStack(this));
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		String gt = "gregtech" + ":" + "materialicons/"+"METALLIC"+"/" + ComponentTypes.DUSTIMPURE.getComponent();
		this.mIcon[0] = reg.registerIcon(gt);
		String gt2 = "gregtech" + ":" + "materialicons/"+"METALLIC"+"/" + ComponentTypes.DUSTIMPURE.getComponent()+"_Overlay";
		this.mIcon[1] = reg.registerIcon(gt2);
	}
	
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (world == null || iStack == null) {
			return;
		}	
		if (!tickItemTag(iStack)) {
			if (entityHolding instanceof EntityPlayer){
				ItemStack replacement = ItemUtils.getSimpleStack(turnsIntoItem);
				//Logger.INFO("Replacing "+iStack.getDisplayName()+" with "+replacement.getDisplayName()+".");
				final ItemStack tempTransform = replacement;
				if (iStack.stackSize == 64){
					tempTransform.stackSize=64;
					((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
					for (int l=0;l<64;l++){
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
	
}
