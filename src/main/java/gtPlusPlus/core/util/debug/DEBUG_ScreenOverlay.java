package gtPlusPlus.core.util.debug;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class DEBUG_ScreenOverlay extends Gui {

	int width, height;
	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void eventHandler(final RenderGameOverlayEvent.Text event)
	{

		//if (mc.thePlayer.getHeldItem().equals(ModItems.itemStaballoyPickaxe)){
		final ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		final FontRenderer fontRender = this.mc.fontRenderer;
		this.width = res.getScaledWidth();
		this.height = res.getScaledHeight();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		final String str = "Words";
		Item heldItem = null;

		try{heldItem = this.mc.thePlayer.getHeldItem().getItem();

		if (heldItem != null){
			/*if (heldItem instanceof StaballoyPickaxe){

				int dmg =((StaballoyPickaxe) heldItem).getDamage(((StaballoyPickaxe) heldItem).thisPickaxe);

				((StaballoyPickaxe) heldItem).checkFacing(((StaballoyPickaxe) heldItem).localWorld);
				str = "DAMAGE: "+ dmg +" | FACING: "+((StaballoyPickaxe) heldItem).FACING+" | FACING_HORIZONTAL: "+((StaballoyPickaxe) heldItem).FACING_HORIZONTAL+" | LOOKING DIRECTION: "+((StaballoyPickaxe) heldItem).lookingDirection;

				drawString(fontRender, str, (this.width - fontRender.getStringWidth(str)) / 2, this.height / 10, 0xFFAA00);
			}*/
		}
		}catch(final NullPointerException e){}

	}
}