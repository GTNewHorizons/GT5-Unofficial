package binnie.craftgui.core.geometry;

public enum TextJustification
{
  TopLeft(0.0F, 0.0F),  TopCenter(0.5F, 0.0F),  TopRight(1.0F, 0.0F),  MiddleLeft(0.0F, 0.5F),  MiddleCenter(0.5F, 0.5F),  MiddleRight(1.0F, 0.5F),  BottomLeft(0.0F, 1.0F),  BottomCenter(0.5F, 1.0F),  BottomRight(1.0F, 1.0F);
  
  float xOffset;
  float yOffset;
  
  private TextJustification(float xOffset, float yOffset)
  {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }
  
  public float getXOffset()
  {
    return this.xOffset;
  }
  
  public float getYOffset()
  {
    return this.yOffset;
  }
}
