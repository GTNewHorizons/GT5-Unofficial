package gtPlusPlus.core.gui.machine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.container.Container_VolumetricFlaskSetter;
import gtPlusPlus.core.gui.widget.GuiValueField;
import gtPlusPlus.core.handler.PacketHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.network.packet.Packet_VolumetricFlaskGui;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GUI_VolumetricFlaskSetter extends GuiContainer {

	private GuiTextField mText;
	private TileEntityVolumetricFlaskSetter mTile;
	private Container_VolumetricFlaskSetter mContainer;
	private static final ResourceLocation mGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/VolumetricFlaskSetter.png");

	public GUI_VolumetricFlaskSetter(Container_VolumetricFlaskSetter aContainer){
		super(aContainer);
		mContainer = aContainer;
		mTile = mContainer.mTileEntity;
	}

	public void initGui(){
		super.initGui();
		this.mText = new GuiValueField(this.fontRendererObj, 26, 31, this.width / 2 - 62, this.height/2-52, 106, 14);
		mText.setMaxStringLength(5);
		mText.setText("0");
		mText.setFocused(true);
	}

	protected void keyTyped(char par1, int par2){
		if (!isNumber(par1) && par2 != Keyboard.KEY_BACK && par2 != Keyboard.KEY_RETURN) {
			mText.setFocused(false);
			super.keyTyped(par1, par2);			
		}
		else {
			if (par2 == Keyboard.KEY_RETURN) {
				if (mText.isFocused()) {
					mText.setFocused(false);
				}
			}
			else if (par2 == Keyboard.KEY_BACK) {
				String aCurrentText = getText();
				if (aCurrentText.length() > 0) {
					this.mText.setText(aCurrentText.substring(0, aCurrentText.length() - 1));	
					if (getText().length() <= 0) {
						this.mText.setText("0");
					}
				}
			}
			else {
				if (this.mText.getText().equals("0")) {
					this.mText.setText(""+par1);
				}
				else {
					this.mText.textboxKeyTyped(par1, par2);					
				}
			}	
			sendUpdateToServer();
		}
	}

	public void updateScreen(){
		super.updateScreen();
		this.mText.updateCursorCounter();
	}

	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();
		super.drawScreen(par1, par2, par3);
		
		
	}

	protected void mouseClicked(int x, int y, int btn) {
		super.mouseClicked(x, y, btn);
		this.mText.mouseClicked(x, y, btn);
	}




	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j){
		super.drawGuiContainerForegroundLayer(i, j);
		this.mText.drawTextBox();
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
		

		// Check TextBox Value is correct
		short aCustomValue = 0;
		if (getText().length() > 0) {
			try {
				aCustomValue = Short.parseShort(getText());
				short aTileValue = ((Container_VolumetricFlaskSetter) mContainer).mCustomValue;
		        if (mContainer != null) {
		            if (aTileValue != aCustomValue){
						this.mText.setText(""+aTileValue);
		            }
		        }
			} catch (NumberFormatException ex) {

			}
		}		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j){
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(mGuiTextures);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	public boolean isNumber(char c) {
		return ((c >= 48 && c <= 57) || c == 45);
	}

	protected String getText() {
		return this.mText.getText();
	}

    protected void sendUpdateToServer() {
        short aCustomValue = 0;
        if (getText().length() > 0) {
            try {
                aCustomValue = Short.parseShort(getText());
                PacketHandler.sendToServer(new Packet_VolumetricFlaskGui(mTile, aCustomValue));
            } catch (NumberFormatException ex) {
 
            }
        }
    }

}