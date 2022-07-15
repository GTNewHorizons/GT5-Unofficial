package goodgenerator.client.GUI;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.common.container.NeutronActivatorGUIContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class NeutronActivatorGUIClient extends GT_GUIContainer_MultiMachineEM {

    private final NeutronActivatorGUIContainer containerNeutronActivator;

    public NeutronActivatorGUIClient(
            InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        super(new NeutronActivatorGUIContainer(aInventoryPlayer, aTileEntity), aName, aTextureFile, true, true, true);
        containerNeutronActivator = (NeutronActivatorGUIContainer) this.mContainer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        if (this.mContainer.mDisplayErrorCode == 0) {
            fontRendererObj.drawString(StatCollector.translateToLocal("gui.NeutronActivator.0"), 7, 40, 16448255);
            fontRendererObj.drawString(processNumber(containerNeutronActivator.getKineticE()) + "eV", 7, 48, 16448255);
        }
    }

    protected String processNumber(int num) {
        float num2;
        num2 = ((float) num) / 1000F;
        if (num2 <= 0) {
            return String.format("%d", num);
        }
        if (num2 < 1000.0) {
            return String.format("%.1fK", num2);
        }
        num2 /= 1000F;
        return String.format("%.1fM", num2);
    }
}
