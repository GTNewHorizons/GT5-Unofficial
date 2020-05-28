package gtPlusPlus.preloader.asm.helpers;

import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import gtPlusPlus.preloader.Preloader_Logger;
import net.minecraft.client.gui.inventory.GuiContainer;

public class MethodHelper_CC {


    public void mouseUp2(int mousex, int mousey, int button) {
    	MethodHelper_CC.mouseUp(mousex, mousey, button);
    }
	
    public static void mouseUp(int mousex, int mousey, int button) {
    	GuiContainerManager aManager = codechicken.nei.guihook.GuiContainerManager.getManager();
    	if (aManager != null) {
    		GuiContainer aWindow = aManager.window;    	
    		for (IContainerInputHandler inputhander : GuiContainerManager.inputHandlers) {
            	Preloader_Logger.INFO("Found Handler: "+aWindow.getClass().getName() + " | "+inputhander.getClass().getName());
            }
            for (IContainerInputHandler inputhander : GuiContainerManager.inputHandlers) {
            	Preloader_Logger.INFO("Trying to handle events for "+aWindow.getClass().getName() + " | "+inputhander.getClass().getName());
                inputhander.onMouseUp(aWindow, mousex, mousey, button);        	
            }
    	}
    }
	
}
