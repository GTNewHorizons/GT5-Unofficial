package miscutil.core.gui.beta;
import miscutil.core.interfaces.IGuiManagerMiscUtils;

public class MU_GuiId
  {
    private final int id;
    private final Gui_Types MU_GuiType;
    private final Class<? extends IGuiManagerMiscUtils> guiHandlerClass;
    
    MU_GuiId(int id, Gui_Types MU_GuiType, Class<? extends IGuiManagerMiscUtils> guiHandlerClass)
    {
      this.id = id;
      this.MU_GuiType = MU_GuiType;
      this.guiHandlerClass = guiHandlerClass;
    }
    
    public Gui_Types getGuiType()
    {
      return this.MU_GuiType;
    }
    
    public Class<? extends IGuiManagerMiscUtils> getGuiHandlerClass()
    {
      return this.guiHandlerClass;
    }
    
    public int getId()
    {
      return this.id;
    }
  }

