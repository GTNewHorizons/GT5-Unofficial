package gtPlusPlus.core.util.debug;

import static net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemGeneric;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class DEBUG_ITEM_ShapeSpawner extends BaseItemGeneric{

	public DEBUG_ITEM_ShapeSpawner(String s, CreativeTabs c, int stackSize, int maxDmg) {
		super(s, c, stackSize, maxDmg);
		s = "itemDebugShapeSpawner";
		c = AddToCreativeTab.tabMisc;
		stackSize = 1;
		maxDmg = 500;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player){

		if (!world.isRemote){
			Logger.INFO("Constructing the shape for the "+"VACUUM FREEZER");
			final Thread thread = new Thread(new DEBUG_TimerThread(world, player));
			thread.start();
		}
		return stack;
	}



	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void playerInteractEventHandler(final PlayerInteractEvent event)
	{
		if (event.isCanceled() || event.world.isRemote || (event.action != RIGHT_CLICK_BLOCK)) {
			return;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GOLD+"For Testing Gregtech Shapes!");
		super.addInformation(stack, aPlayer, list, bool);
	}

}
