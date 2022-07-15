package goodgenerator.client.GUI;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.main.GoodGenerator;
import goodgenerator.network.MessageSetNeutronSensorData;
import goodgenerator.util.CharExchanger;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class NeutronSensorGUIClient extends GT_GUIContainerMetaTile_Machine {

    protected final String mName;

    private GuiTextField TextBox;
    private String context;

    public NeutronSensorGUIClient(
            InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aTexture, String text) {
        super(aInventoryPlayer, aTileEntity, aTexture);
        this.mName = "Neutron Sensor";
        this.mContainer.detectAndSendChanges();
        if (text == null) this.context = "";
        else this.context = text;
    }

    public void initGui() {
        super.initGui();
        this.TextBox = new GuiTextField(this.fontRendererObj, 8, 48, 100, 18);
        TextBox.setMaxStringLength(20);
        TextBox.setText(context);
        this.TextBox.setFocused(true);
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString(this.mName, 8, 4, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.NeutronSensor.0"), 8, 16, 0x000000);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.NeutronSensor.1"), 8, 28, 0x000000);
        this.TextBox.drawTextBox();
        if (TextBox.getText() != null) {
            if (isValidSuffix(TextBox.getText())) {
                if (CharExchanger.isValidCompareExpress(rawProcessExp(TextBox.getText())))
                    this.fontRendererObj.drawString(
                            StatCollector.translateToLocal("gui.NeutronSensor.2"), 120, 53, 0x077d02);
                else
                    this.fontRendererObj.drawString(
                            StatCollector.translateToLocal("gui.NeutronSensor.3"), 120, 53, 0xff0000);
            } else
                this.fontRendererObj.drawString(
                        StatCollector.translateToLocal("gui.NeutronSensor.3"), 120, 53, 0xff0000);
        }
        this.mc
                .getTextureManager()
                .bindTexture(new ResourceLocation(GoodGenerator.MOD_ID + ":textures/gui/NeutronSensorGUI.png"));
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    protected void keyTyped(char par1, int par2) {
        if (!this.TextBox.isFocused()) super.keyTyped(par1, par2);
        if (par2 == 1) this.mc.thePlayer.closeScreen();
        this.TextBox.textboxKeyTyped(par1, par2);
    }

    public void updateScreen() {
        super.updateScreen();
        this.TextBox.updateCursorCounter();
    }

    protected void mouseClicked(int x, int y, int btn) {
        super.mouseClicked(x, y, btn);
        this.TextBox.mouseClicked(x - this.getLeft(), y - this.getTop(), btn);
    }

    @Override
    public void onGuiClosed() {
        if (CharExchanger.isValidCompareExpress(rawProcessExp(TextBox.getText())))
            GoodGenerator.CHANNEL.sendToServer(
                    new MessageSetNeutronSensorData(mContainer.mTileEntity, TextBox.getText()));
        super.onGuiClosed();
    }

    protected String rawProcessExp(String exp) {
        StringBuilder ret = new StringBuilder();
        for (char c : exp.toCharArray()) {
            if (exp.length() - ret.length() == 3) {
                if (Character.isDigit(c)) ret.append(c);
                break;
            }
            ret.append(c);
        }
        return ret.toString();
    }

    protected boolean isValidSuffix(String exp) {
        int index;
        index = exp.length() - 1;
        if (index < 0) return false;
        if (exp.charAt(index) != 'V' && exp.charAt(index) != 'v') return false;
        index = exp.length() - 2;
        if (index < 0) return false;
        if (exp.charAt(index) != 'E' && exp.charAt(index) != 'e') return false;
        index = exp.length() - 3;
        if (index < 0) return false;
        return exp.charAt(index) == 'M'
                || exp.charAt(index) == 'm'
                || exp.charAt(index) == 'K'
                || exp.charAt(index) == 'k'
                || Character.isDigit(exp.charAt(index));
    }
}
