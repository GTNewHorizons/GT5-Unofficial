package binnie.core.genetics;

import binnie.Binnie;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleRegistry;

public class ForestryAllele
{
  public static enum Lifespan
  {
    Shortest,  Shorter,  Short,  Shortened,  Normal,  Elongated,  Long,  Longer,  Longest;
    
    private Lifespan() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.lifespan" + toString());
    }
  }
  
  public static enum Speed
  {
    Slowest,  Slower,  Slow,  Norm,  Fast,  Faster,  Fastest;
    
    private Speed() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.speed" + toString());
    }
  }
  
  public static enum Fertility
  {
    Low,  Normal,  High,  Maximum;
    
    private Fertility() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.fertility" + toString());
    }
  }
  
  public static enum Flowering
  {
    Slowest,  Slower,  Slow,  Average,  Fast,  Faster,  Fastest,  Maximum;
    
    private Flowering() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.flowering" + toString());
    }
  }
  
  public static enum Territory
  {
    Default,  Large,  Larger,  Largest;
    
    private Territory() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.territory" + toString());
    }
  }
  
  public static enum Sappiness
  {
    Lowest,  Lower,  Low,  Average,  High,  Higher,  Highest;
    
    private Sappiness() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.sappiness" + toString());
    }
  }
  
  public static enum TreeHeight
  {
    Smallest,  Smaller,  Small,  Average,  Large,  Larger,  Largest,  Gigantic;
    
    private TreeHeight() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.height" + (this == Average ? "Max10" : toString()));
    }
  }
  
  public static enum Size
  {
    Smallest,  Smaller,  Small,  Average,  Large,  Larger,  Largest;
    
    private Size() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.size" + toString());
    }
  }
  
  public static enum Saplings
  {
    Lowest,  Lower,  Low,  Average,  High,  Higher,  Highest;
    
    private Saplings() {}
    
    public IAllele getAllele()
    {
      String s = toString();
      if (this == Average) {
        s = "Default";
      }
      if (this == High) {
        s = "Double";
      }
      if (this == Higher) {
        s = "Triple";
      }
      return AlleleManager.alleleRegistry.getAllele("forestry.saplings" + s);
    }
  }
  
  public static enum Yield
  {
    Lowest,  Lower,  Low,  Average,  High,  Higher,  Highest;
    
    private Yield() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.yield" + (this == Average ? "Default" : toString()));
    }
  }
  
  public static enum Maturation
  {
    Slowest,  Slower,  Slow,  Average,  Fast,  Faster,  Fastest;
    
    private Maturation() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.maturation" + toString());
    }
  }
  
  public static enum Bool
  {
    True,  False;
    
    private Bool() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.bool" + toString());
    }
    
    public static IAllele get(boolean bool)
    {
      return (bool ? True : False).getAllele();
    }
  }
  
  public static enum Growth
  {
    Tropical;
    
    private Growth() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.growth" + toString());
    }
  }
  
  public static enum Int
  {
    Int1,  Int2,  Int3,  Int4,  Int5,  Int6,  Int7,  Int8,  Int9,  Int10;
    
    private Int() {}
    
    public IAllele getAllele()
    {
      return AlleleManager.alleleRegistry.getAllele("forestry.i" + (ordinal() + 1) + "d");
    }
  }
  
  public static enum BeeSpecies
  {
    Modest,  Noble,  Forest,  Rural,  Marshy,  Sinister,  Tropical,  Wintry,  Merry,  Austere,  Imperial,  Ended,  Meadows,  Common,  Frugal,  Unweary,  Diligent,  Majestic,  Cultivated,  Industrious,  Valiant,  Secluded,  Hermitic,  Spectral,  Exotic,  Fiendish,  Monastic,  Steadfast,  Miry,  Farmerly,  Boggy,  Demonic;
    
    private BeeSpecies() {}
    
    public IAlleleBeeSpecies getAllele()
    {
      return (IAlleleBeeSpecies)AlleleManager.alleleRegistry.getAllele("forestry.species" + toString());
    }
    
    public IAllele[] getTemplate()
    {
      return Binnie.Genetics.getBeeRoot().getTemplate(getAllele().getUID());
    }
  }
}
