package goodgenerator.client.GUI;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import goodgenerator.common.container.YOTTankGUIContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class LargeFusionComputerGUIClient extends GT_GUIContainer_MultiMachineEM {

    public LargeFusionComputerGUIClient(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        super(new YOTTankGUIContainer(aInventoryPlayer, aTileEntity), aName, aTextureFile, true, true, true);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        if (this.mContainer.mDisplayErrorCode == 0) {
            fontRendererObj.drawString(StatCollector.translateToLocal("gui.LargeFusion.0") + " " + GT_Utility.formatNumbers(this.mContainer.mStorage) + " EU", 7, 40, 16448255);
            fontRendererObj.drawString(StatCollector.translateToLocal("gui.LargeFusion.1") + " " + GT_Utility.formatNumbers(this.mContainer.mEnergy) + " EU", 7, 48, 16448255);
        }
    }

}
