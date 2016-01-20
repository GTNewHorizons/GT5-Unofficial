package binnie.core.texture;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.resource.BinnieResource;
import binnie.core.resource.IBinnieTexture;
import binnie.core.resource.ManagerResource;
import binnie.core.resource.ResourceType;

public enum BinnieCoreTexture
  implements IBinnieTexture
{
  Compartment(ResourceType.Tile, "Compartment"),  CompartmentIron(ResourceType.Tile, "CompartmentIron"),  CompartmentDiamond(ResourceType.Tile, "CompartmentDiamond"),  CompartmentCopper(ResourceType.Tile, "CompartmentCopper"),  CompartmentGold(ResourceType.Tile, "CompartmentGold"),  CompartmentBronze(ResourceType.Tile, "CompartmentBronze"),  GUIBreeding(ResourceType.GUI, "breeding"),  GUIAnalyst(ResourceType.GUI, "guianalyst");
  
  String texture;
  ResourceType type;
  
  private BinnieCoreTexture(ResourceType base, String texture)
  {
    this.texture = texture;
    this.type = base;
  }
  
  public BinnieResource getTexture()
  {
    return Binnie.Resource.getPNG(BinnieCore.instance, this.type, this.texture);
  }
}
