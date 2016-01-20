package binnie.craftgui.resource.minecraft;

public enum CraftGUITexture
{
  Window("window"),  PanelGray("panel.gray"),  PanelBlack("panel.black"),  PanelTinted("panel.tinted"),  ButtonDisabled("button.disabled"),  Button("button"),  ButtonHighlighted("button.highlighted"),  Slot("slot"),  SlotBorder("slot.border"),  SlotOverlay("slot.overlay"),  SlotCharge("slot.charge"),  LiquidTank("liquidtank"),  LiquidTankOverlay("liquidtank.overlay"),  StateError("errorstate.error"),  StateWarning("errorstate.warning"),  StateNone("errorstate.none"),  EnergyBarBack("energybar.back"),  EnergyBarGlow("energybar.glow"),  EnergyBarGlass("energybar.glass"),  TabDisabled("tab.disabled"),  Tab("tab"),  TabHighlighted("tab.highlighted"),  TabOutline("tab.outline"),  TabSolid("tab.solid"),  ScrollDisabled("scroll.disabled"),  Scroll("scroll"),  ScrollHighlighted("scroll.highlighted"),  Outline("outline"),  HelpButton("button.help"),  InfoButton("button.info"),  UserButton("button.user"),  PowerButton("button.power"),  HorizontalLiquidTank("horizontalliquidtank"),  HorizontalLiquidTankOverlay("horizontalliquidtank.overlay"),  SlideUp("slide.up"),  SlideDown("slide.down"),  SlideLeft("slide.left"),  SlideRight("slide.right"),  Checkbox("checkbox"),  CheckboxHighlighted("checkbox.highlighted"),  CheckboxChecked("checkbox.checked"),  CheckboxCheckedHighlighted("checkbox.checked.highlighted");
  
  String name;
  
  private CraftGUITexture(String name)
  {
    this.name = name;
  }
  
  public String toString()
  {
    return this.name;
  }
}
