package miscutil.core.util.debug;

import miscutil.core.item.tool.staballoy.StaballoyPickaxe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DEBUG_ScreenOverlay extends Gui {

	int width, height;
	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void eventHandler(RenderGameOverlayEvent.Text event)
	{

		//if (mc.thePlayer.getHeldItem().equals(ModItems.itemStaballoyPickaxe)){
		ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		FontRenderer fontRender = mc.fontRenderer;
		this.width = res.getScaledWidth();
		this.height = res.getScaledHeight();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		String str = "Words";
		Item heldItem = null;

		try{heldItem = mc.thePlayer.getHeldItem().getItem();

		if (heldItem != null){
			if (heldItem instanceof StaballoyPickaxe){

				int dmg =((StaballoyPickaxe) heldItem).getDamage(((StaballoyPickaxe) heldItem).thisPickaxe);

				((StaballoyPickaxe) heldItem).checkFacing(((StaballoyPickaxe) heldItem).localWorld);
				str = "DAMAGE: "+ dmg +" | FACING: "+((StaballoyPickaxe) heldItem).FACING+" | FACING_HORIZONTAL: "+((StaballoyPickaxe) heldItem).FACING_HORIZONTAL+" | LOOKING DIRECTION: "+((StaballoyPickaxe) heldItem).lookingDirection;

				drawString(fontRender, str, (this.width - fontRender.getStringWidth(str)) / 2, this.height / 10, 0xFFAA00);
			}
		}
		}catch(NullPointerException e){}

	}
}