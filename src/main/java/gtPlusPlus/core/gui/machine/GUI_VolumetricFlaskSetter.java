package gtPlusPlus.core.gui.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.container.Container_VolumetricFlaskSetter;
import gtPlusPlus.core.gui.widget.GuiValueField;
import gtPlusPlus.core.handler.PacketHandler;
import gtPlusPlus.core.network.packet.Packet_VolumetricFlaskGui;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;

@SideOnly(Side.CLIENT)
public class GUI_VolumetricFlaskSetter extends GuiContainer {

    private static final ResourceLocation mGuiTextures = new ResourceLocation(
            GTPlusPlus.ID,
            "textures/gui/VolumetricFlaskSetter.png");
    private Container_VolumetricFlaskSetter mContainer;
    private boolean mIsOpen = false;
    private GuiValueField mText;
    private TileEntityVolumetricFlaskSetter mTile;

    public GUI_VolumetricFlaskSetter(Container_VolumetricFlaskSetter aContainer) {
        super(aContainer);
        mContainer = aContainer;
        mTile = mContainer.mTileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(mGuiTextures);
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int i, final int j) {
        super.drawGuiContainerForegroundLayer(i, j);
        this.mText.drawTextBox();
        this.fontRendererObj.drawString(I18n.format("container.VolumetricFlaskSetter", new Object[0]), 4, 3, 4210752);
        int aYVal = 49;
        this.fontRendererObj.drawString(I18n.format("0 = 16l", new Object[0]), 8, aYVal, 4210752);
        this.fontRendererObj.drawString(I18n.format("4 = 576l", new Object[0]), 64, aYVal, 4210752);
        this.fontRendererObj.drawString(I18n.format("1 = 36l", new Object[0]), 8, aYVal += 8, 4210752);
        this.fontRendererObj.drawString(I18n.format("5 = 720l", new Object[0]), 64, aYVal, 4210752);
        this.fontRendererObj.drawString(I18n.format("2 = 144l", new Object[0]), 8, aYVal += 8, 4210752);
        this.fontRendererObj.drawString(I18n.format("6 = 864l", new Object[0]), 64, aYVal, 4210752);
        this.fontRendererObj.drawString(I18n.format("3 = 432l", new Object[0]), 8, aYVal += 8, 4210752);
        this.fontRendererObj.drawString(I18n.format("-> = Custom", new Object[0]), 59, aYVal, 4210752);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.drawScreen(par1, par2, par3);
    }

    protected String getText() {
        return this.mText.getText();
    }

    @Override
    public void initGui() {
        super.initGui();
        // Keyboard.enableRepeatEvents(true);
        mIsOpen = true;
        this.mText = new GuiValueField(
                this.fontRendererObj,
                26,
                31,
                this.width / 2 - 62,
                this.height / 2 - 52,
                106,
                14,
                this);
        mText.setMaxStringLength(5);
        mText.setEnableBackgroundDrawing(true);
        mText.setText("0");
        mText.setFocused(true);
    }

    public boolean isNumber(char c) {
        boolean isNum = ((c >= 48 && c <= 57) || c == 45);
        if (isNum) {
            log("Found Digit: " + c + " | char value");
        } else {
            switch (c) {
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    log("Found Digit: " + c + " | char switch");
                    return true;
                }
            }
        }
        return isNum;
    }

    public boolean isNumber(int c) {
        switch (c) {
            case Keyboard.KEY_0, Keyboard.KEY_1, Keyboard.KEY_2, Keyboard.KEY_3, Keyboard.KEY_4, Keyboard.KEY_5, Keyboard.KEY_6, Keyboard.KEY_7, Keyboard.KEY_8, Keyboard.KEY_9, Keyboard.KEY_NUMPAD0, Keyboard.KEY_NUMPAD1, Keyboard.KEY_NUMPAD2, Keyboard.KEY_NUMPAD3, Keyboard.KEY_NUMPAD4, Keyboard.KEY_NUMPAD5, Keyboard.KEY_NUMPAD6, Keyboard.KEY_NUMPAD7, Keyboard.KEY_NUMPAD8, Keyboard.KEY_NUMPAD9 -> {
                log("Found Digit: " + Keyboard.getKeyName(c) + " | LWJGL Keybinding");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (mIsOpen) {
            log("Pressed " + par1 + " | " + par2);
            if (mText.isFocused()) {
                log("Text box has focus.");
                if (par2 == Keyboard.KEY_RETURN) {
                    log("Pressed Enter, unfocusing.");
                    mText.setFocused(false);
                } else if (par2 == Keyboard.KEY_BACK) {
                    log("Pressed Backspace.");
                    String aCurrentText = getText();
                    if (aCurrentText.length() > 0) {
                        this.mText.setText(aCurrentText.substring(0, aCurrentText.length() - 1));
                        if (getText().length() <= 0) {
                            setText(0);
                        }
                        sendUpdateToServer();
                    }
                } else {
                    if (isNumber(par2) || isNumber(par1)) {
                        log("Pressed number.");
                        if (this.mText.getText().equals("0")) {
                            this.mText.textboxKeyTyped(par1, par2);
                            sendUpdateToServer();
                        } else {
                            this.mText.textboxKeyTyped(par1, par2);
                            sendUpdateToServer();
                        }
                    } else {
                        log("Pressed unused key.");
                        super.keyTyped(par1, par2);
                    }
                }
            } else {
                log("Text box not focused.");
                super.keyTyped(par1, par2);
            }
        } else {
            log("Gui is not open?");
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        if (mIsOpen) {
            log("Clicked.");
            this.mText.mouseClicked(x, y, btn);
            if (!mText.didClickInTextField(x, y)) {
                log("Did not click in text box, passing to super.");
                super.mouseClicked(x, y, btn);
            }
        } else {
            log("Gui is not open?");
        }
    }

    @Override
    public void onGuiClosed() {
        mIsOpen = false;
        mText.setEnabled(false);
        mText.setVisible(false);
        super.onGuiClosed();
        // Keyboard.enableRepeatEvents(false);
    }

    public int parse(String aValue) {
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void sendUpdateToServer() {
        if (getText().length() > 0) {
            PacketHandler.sendToServer(new Packet_VolumetricFlaskGui(mTile, parse(getText())));
        }
    }

    public void setText(int aValue) {
        this.mText.setText("" + aValue);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        // Update Textbox to 0 if Empty
        if (getText().length() <= 0) {
            this.mText.setText("0");
            sendUpdateToServer();
        }
        this.mText.updateCursorCounter();

        // Check TextBox Value is correct
        if (getText().length() > 0) {
            int aCustomValue = parse(getText());
            int aTileValue = ((Container_VolumetricFlaskSetter) mContainer).mCustomValue;
            if (mContainer != null) {
                if (aTileValue != aCustomValue) {
                    setText(aTileValue);
                }
            }
        }
    }

    public void log(String aString) {
        Logger.INFO("[Flask-GUI] " + aString);
    }
}
