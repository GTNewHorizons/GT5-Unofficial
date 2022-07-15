package goodgenerator.client.GUI;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import goodgenerator.common.container.YOTTankGUIContainer;
import goodgenerator.util.CharExchanger;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class YOTTankGUIClient extends GT_GUIContainer_MultiMachineEM {

    private final YOTTankGUIContainer containerYOTTank;

    public YOTTankGUIClient(
            InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        super(new YOTTankGUIContainer(aInventoryPlayer, aTileEntity), aName, aTextureFile, true, true, true);
        containerYOTTank = (YOTTankGUIContainer) this.mContainer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        if (this.mContainer.mDisplayErrorCode == 0) {
            fontRendererObj.drawString(
                    StatCollector.translateToLocal("gui.YOTTank.0") + " "
                            + CharExchanger.formatNumber(containerYOTTank.getCap()) + " L",
                    7,
                    40,
                    16448255);
            fontRendererObj.drawString(
                    StatCollector.translateToLocal("gui.YOTTank.1") + " "
                            + CharExchanger.formatNumber(containerYOTTank.getFluidName()),
                    7,
                    48,
                    16448255);
            fontRendererObj.drawString(
                    StatCollector.translateToLocal("gui.YOTTank.2") + " "
                            + CharExchanger.formatNumber(containerYOTTank.getStorage()) + " L",
                    7,
                    56,
                    16448255);
        }
    }
}
