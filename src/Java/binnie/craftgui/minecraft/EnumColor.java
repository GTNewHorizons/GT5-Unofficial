package binnie.craftgui.minecraft;

public enum EnumColor
{
  Black("Black", 0, '0'),  DarkBlue("Dark Blue", 170, '1'),  DarkGreen("Dark Green", 43520, '2'),  DarkAqua("Dark Aqua", 43690, '3'),  DarkRed("Dark Red", 11141120, '4'),  Purple("Purple", 11141290, '5'),  Gold("Gold", 16755200, '6'),  Grey("Grey", 11184810, '7'),  DarkGrey("Dark Grey", 5592405, '8'),  Blue("Blue", 5592575, '9'),  Green("Green", 5635925, 'a'),  Aqua("Aqua", 5636095, 'b'),  Red("Red", 16733525, 'c'),  Pink("Pink", 16733695, 'd'),  Yellow("Yellow", 16777045, 'e'),  White("White", 16777215, 'f');
  
  int colour;
  String name;
  char code;
  
  private EnumColor(String name, int colour, char code)
  {
    this.name = name;
    this.colour = colour;
    this.code = code;
  }
  
  public int getColour()
  {
    return this.colour;
  }
  
  public String getCode()
  {
    return "§" + this.code;
  }
  
  public String toString()
  {
    return this.name;
  }
}
