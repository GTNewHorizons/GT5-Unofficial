package miscutil.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GUI_Battery_Buffer extends GuiScreen {

	private GuiButton a;
	private GuiButton b;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/*
	@SuppressWarnings("unchecked")
	public void initGui() {
	    this.buttonList.add(this.a = new GuiButton(0, this.width / 2 - 100, this.height / 2 - 24, "This is button a"));
	    this.buttonList.add(this.b = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 4, "This is button b"));
	}*/

	/*	@Override
	protected void actionPerformed(GuiButton button) {
	    if (button == this.a) {
	        //Main.packetHandler.sendToServer(...);
	        this.mc.displayGuiScreen(null);
	        if (this.mc.currentScreen == null)
	            this.mc.setIngameFocus();
	    }
	    if (button == this.b){
	        //Main.packetHandler.sendToServer(...);
	        this.mc.displayGuiScreen(null);
	        if (this.mc.currentScreen == null)
	            this.mc.setIngameFocus();
	    }
	}*/
	/**
	 * if (worldIn.isRemote) {
            playerIn.openGui(Main.instance, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
	 */


}