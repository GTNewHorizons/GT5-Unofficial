package gtPlusPlus.core.gui.machine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.container.Container_VolumetricFlaskSetter;
import gtPlusPlus.core.handler.PacketHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.network.packet.Packet_VolumetricFlaskGui;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GUI_VolumetricFlaskSetter extends GuiContainer {

	private GuiTextField text;
	private TileEntityVolumetricFlaskSetter aTile;
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/VolumetricFlaskSetter.png");

	public GUI_VolumetricFlaskSetter(final InventoryPlayer player_inventory, final TileEntityVolumetricFlaskSetter te){
		super(new Container_VolumetricFlaskSetter(player_inventory, te));
		aTile = te;
		Logger.INFO("Tile Value: "+te.getCustomValue());
	}

	public void initGui(){
		super.initGui();
		this.text = new GuiTextField(this.fontRendererObj, this.width / 2 - 62, this.height/2-52, 106, 14);
		text.setMaxStringLength(5);
		if (aTile != null) {
			Logger.INFO("Using Value from Tile: "+aTile.getCustomValue());
			text.setText(""+aTile.getCustomValue());
		}
		else {
			Logger.INFO("Using default Value: 1000");
			text.setText("1000");
		}
		this.text.setFocused(true);
	}

	protected void keyTyped(char par1, int par2){
		if (!isNumber(par1) && par2 != Keyboard.KEY_BACK && par2 != Keyboard.KEY_RETURN) {
			text.setFocused(false);
			super.keyTyped(par1, par2);			
		}
		else {
			if (par2 == Keyboard.KEY_RETURN) {
				if (text.isFocused()) {
					Logger.INFO("Removing Focus.");
					text.setFocused(false);
				}
			}
			else if (par2 == Keyboard.KEY_BACK) {
				String aCurrentText = getText();
				if (aCurrentText.length() > 0) {
					this.text.setText(aCurrentText.substring(0, aCurrentText.length() - 1));	
					if (getText().length() <= 0) {
						this.text.setText("0");
					}
				}
			}
			else {
				if (this.text.getText().equals("0")) {
					this.text.setText(""+par1);
				}
				else {
					this.text.textboxKeyTyped(par1, par2);					
				}
			}	
			sendUpdateToServer();
		}
	}

	public void updateScreen(){
		super.updateScreen();
		this.text.updateCursorCounter();
	}

	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();
		super.drawScreen(par1, par2, par3);
		this.text.drawTextBox();
	}

	protected void mouseClicked(int x, int y, int btn) {
		super.mouseClicked(x, y, btn);
		this.text.mouseClicked(x, y, btn);
	}








	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j){
		super.drawGuiContainerForegroundLayer(i, j);
		this.fontRendererObj.drawString(I18n.format("container.VolumetricFlaskSetter", new Object[0]), 4, 3, 4210752);
		int aYVal = 49;
		this.fontRendererObj.drawString(I18n.format("0 = 16l", new Object[0]), 8, aYVal, 4210752);
		this.fontRendererObj.drawString(I18n.format("4 = 576l", new Object[0]), 64, aYVal, 4210752);
		this.fontRendererObj.drawString(I18n.format("1 = 36l", new Object[0]), 8, aYVal+=8, 4210752);
		this.fontRendererObj.drawString(I18n.format("5 = 720l", new Object[0]), 64, aYVal, 4210752);
		this.fontRendererObj.drawString(I18n.format("2 = 144l", new Object[0]), 8, aYVal+=8, 4210752);
		this.fontRendererObj.drawString(I18n.format("6 = 864l", new Object[0]), 64, aYVal, 4210752);
		this.fontRendererObj.drawString(I18n.format("3 = 432l", new Object[0]), 8, aYVal+=8, 4210752);
		this.fontRendererObj.drawString(I18n.format("-> = Custom", new Object[0]), 59, aYVal, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j){
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	public boolean isNumber(char c) {
		return ((c >= 48 && c <= 57) || c == 45);
	}

	protected String getText() {
		return this.text.getText();
	}

	protected void sendUpdateToServer() {
		Logger.INFO("Trying to send packet to server from GUI.");
		int aCustomValue = 0;
		if (getText().length() > 0)
			try {
				aCustomValue = Integer.parseInt(getText());
				Logger.INFO("Got Value: "+aCustomValue);
				PacketHandler.sendToServer(new Packet_VolumetricFlaskGui(aTile, aCustomValue));
			} catch (NumberFormatException ex) {

			}
	}

}