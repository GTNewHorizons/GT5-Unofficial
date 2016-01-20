package binnie.extrabees.genetics;

import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IClassification;
import forestry.api.genetics.IClassification.EnumClassLevel;
import java.util.LinkedHashSet;
import java.util.Set;

public enum ExtraBeesBranch
  implements IClassification
{
  BARREN("Vacapis"),  HOSTILE("Infenapis"),  ROCKY("Monapis"),  METALLIC("Lamminapis"),  METALLIC2("Metalapis"),  ALLOY("Allapis"),  PRECIOUS("Pluriapis"),  MINERAL("Niphapis"),  GEMSTONE("Gemmapis"),  NUCLEAR("Levapis"),  HISTORIC("Priscapis"),  FOSSILIZED("Fosiapis"),  REFINED("Petrapis"),  AQUATIC("Aquapis"),  SACCHARINE("Sacchapis"),  CLASSICAL("Grecapis"),  VOLCANIC("Irrapis"),  VIRULENT("Virapis"),  VISCOUS("Viscapis"),  CAUSTIC("Morbapis"),  ENERGETIC("Incitapis"),  FARMING("Agriapis"),  SHADOW("Pullapis"),  PRIMARY("Primapis"),  SECONDARY("Secapis"),  TERTIARY("Tertiapis"),  FTB("Eftebeapis"),  QUANTUM("Quantapis"),  BOTANIA("Botaniapis");
  
  private String uid = "";
  private String scientific = "";
  private Set<IAlleleBeeSpecies> speciesSet = new LinkedHashSet();
  IClassification parent;
  
  public String getUID()
  {
    return "extrabees.genus." + this.uid;
  }
  
  public String getName()
  {
    return ExtraBees.proxy.localise("branch." + toString().toLowerCase() + ".name");
  }
  
  public String getScientific()
  {
    return this.scientific;
  }
  
  public String getDescription()
  {
    return ExtraBees.proxy.localiseOrBlank("branch." + toString().toLowerCase() + ".desc");
  }
  
  private ExtraBeesBranch(String scientific)
  {
    this.scientific = scientific;
    this.uid = toString().toLowerCase();
  }
  
  public void register()
  {
    if (!this.speciesSet.isEmpty())
    {
      AlleleManager.alleleRegistry.registerClassification(this);
      IClassification parent = AlleleManager.alleleRegistry.getClassification("family.apidae");
      if (parent != null)
      {
        parent.addMemberGroup(this);
        setParent(parent);
      }
    }
  }
  
  public static void doInit()
  {
    IClassification frozenBranch = AlleleManager.alleleRegistry.getClassification("genus.bees.frozen");
    if (frozenBranch != null)
    {
      frozenBranch.addMemberSpecies(ExtraBeesSpecies.ARTIC);
      ExtraBeesSpecies.ARTIC.setBranch(frozenBranch);
      frozenBranch.addMemberSpecies(ExtraBeesSpecies.FREEZING);
      ExtraBeesSpecies.FREEZING.setBranch(frozenBranch);
    }
    IClassification agrarianBranch = AlleleManager.alleleRegistry.getClassification("genus.bees.agrarian");
    if (agrarianBranch != null)
    {
      agrarianBranch.addMemberSpecies(ExtraBeesSpecies.FARM);
      ExtraBeesSpecies.FARM.setBranch(agrarianBranch);
      ExtraBeesSpecies.GROWING.setBranch(agrarianBranch);
      ExtraBeesSpecies.THRIVING.setBranch(agrarianBranch);
      ExtraBeesSpecies.BLOOMING.setBranch(agrarianBranch);
    }
    IClassification boggyBranch = AlleleManager.alleleRegistry.getClassification("genus.bees.boggy");
    if (boggyBranch != null)
    {
      boggyBranch.addMemberSpecies(ExtraBeesSpecies.SWAMP);
      boggyBranch.addMemberSpecies(ExtraBeesSpecies.BOGGY);
      boggyBranch.addMemberSpecies(ExtraBeesSpecies.FUNGAL);
      ExtraBeesSpecies.SWAMP.setBranch(boggyBranch);
      ExtraBeesSpecies.BOGGY.setBranch(boggyBranch);
      ExtraBeesSpecies.FUNGAL.setBranch(boggyBranch);
    }
    IClassification festiveBranch = AlleleManager.alleleRegistry.getClassification("genus.bees.festive");
    if (festiveBranch != null)
    {
      festiveBranch.addMemberSpecies(ExtraBeesSpecies.CELEBRATORY);
      ExtraBeesSpecies.CELEBRATORY.setBranch(festiveBranch);
    }
    IClassification austereBranch = AlleleManager.alleleRegistry.getClassification("genus.bees.austere");
    if (austereBranch != null)
    {
      austereBranch.addMemberSpecies(ExtraBeesSpecies.HAZARDOUS);
      ExtraBeesSpecies.HAZARDOUS.setBranch(austereBranch);
    }
    FARMING.addMemberSpecies(ExtraBeesSpecies.ALCOHOL);
    FARMING.addMemberSpecies(ExtraBeesSpecies.MILK);
    FARMING.addMemberSpecies(ExtraBeesSpecies.COFFEE);
    FARMING.addMemberSpecies(ExtraBeesSpecies.CITRUS);
    FARMING.addMemberSpecies(ExtraBeesSpecies.MINT);
    FARMING.register();
    
    BARREN.addMemberSpecies(ExtraBeesSpecies.ARID);
    BARREN.addMemberSpecies(ExtraBeesSpecies.BARREN);
    BARREN.addMemberSpecies(ExtraBeesSpecies.DESOLATE);
    BARREN.addMemberSpecies(ExtraBeesSpecies.DECOMPOSING);
    BARREN.addMemberSpecies(ExtraBeesSpecies.GNAWING);
    BARREN.register();
    
    HOSTILE.addMemberSpecies(ExtraBeesSpecies.ROTTEN);
    HOSTILE.addMemberSpecies(ExtraBeesSpecies.BONE);
    HOSTILE.addMemberSpecies(ExtraBeesSpecies.CREEPER);
    HOSTILE.register();
    
    ROCKY.addMemberSpecies(ExtraBeesSpecies.ROCK);
    ROCKY.addMemberSpecies(ExtraBeesSpecies.STONE);
    ROCKY.addMemberSpecies(ExtraBeesSpecies.GRANITE);
    ROCKY.addMemberSpecies(ExtraBeesSpecies.MINERAL);
    ROCKY.register();
    
    METALLIC.addMemberSpecies(ExtraBeesSpecies.IRON);
    METALLIC.addMemberSpecies(ExtraBeesSpecies.COPPER);
    METALLIC.addMemberSpecies(ExtraBeesSpecies.TIN);
    METALLIC.addMemberSpecies(ExtraBeesSpecies.LEAD);
    METALLIC.register();
    
    METALLIC2.addMemberSpecies(ExtraBeesSpecies.NICKEL);
    METALLIC2.addMemberSpecies(ExtraBeesSpecies.ZINC);
    METALLIC2.addMemberSpecies(ExtraBeesSpecies.TUNGSTATE);
    METALLIC2.addMemberSpecies(ExtraBeesSpecies.TITANIUM);
    METALLIC2.register();
    
    ALLOY.addMemberSpecies(ExtraBeesSpecies.BRONZE);
    ALLOY.addMemberSpecies(ExtraBeesSpecies.BRASS);
    ALLOY.addMemberSpecies(ExtraBeesSpecies.STEEL);
    ALLOY.addMemberSpecies(ExtraBeesSpecies.INVAR);
    ALLOY.register();
    
    PRECIOUS.addMemberSpecies(ExtraBeesSpecies.SILVER);
    PRECIOUS.addMemberSpecies(ExtraBeesSpecies.GOLD);
    PRECIOUS.addMemberSpecies(ExtraBeesSpecies.ELECTRUM);
    PRECIOUS.addMemberSpecies(ExtraBeesSpecies.PLATINUM);
    PRECIOUS.register();
    
    MINERAL.addMemberSpecies(ExtraBeesSpecies.LAPIS);
    MINERAL.addMemberSpecies(ExtraBeesSpecies.SODALITE);
    MINERAL.addMemberSpecies(ExtraBeesSpecies.PYRITE);
    MINERAL.addMemberSpecies(ExtraBeesSpecies.BAUXITE);
    MINERAL.addMemberSpecies(ExtraBeesSpecies.CINNABAR);
    MINERAL.addMemberSpecies(ExtraBeesSpecies.SPHALERITE);
    MINERAL.register();
    
    GEMSTONE.addMemberSpecies(ExtraBeesSpecies.EMERALD);
    GEMSTONE.addMemberSpecies(ExtraBeesSpecies.RUBY);
    GEMSTONE.addMemberSpecies(ExtraBeesSpecies.SAPPHIRE);
    GEMSTONE.addMemberSpecies(ExtraBeesSpecies.OLIVINE);
    GEMSTONE.addMemberSpecies(ExtraBeesSpecies.DIAMOND);
    GEMSTONE.register();
    
    NUCLEAR.addMemberSpecies(ExtraBeesSpecies.UNSTABLE);
    NUCLEAR.addMemberSpecies(ExtraBeesSpecies.NUCLEAR);
    NUCLEAR.addMemberSpecies(ExtraBeesSpecies.RADIOACTIVE);
    NUCLEAR.addMemberSpecies(ExtraBeesSpecies.YELLORIUM);
    NUCLEAR.addMemberSpecies(ExtraBeesSpecies.CYANITE);
    NUCLEAR.addMemberSpecies(ExtraBeesSpecies.BLUTONIUM);
    NUCLEAR.register();
    
    HISTORIC.addMemberSpecies(ExtraBeesSpecies.ANCIENT);
    HISTORIC.addMemberSpecies(ExtraBeesSpecies.PRIMEVAL);
    HISTORIC.addMemberSpecies(ExtraBeesSpecies.PREHISTORIC);
    HISTORIC.addMemberSpecies(ExtraBeesSpecies.RELIC);
    HISTORIC.register();
    
    FOSSILIZED.addMemberSpecies(ExtraBeesSpecies.COAL);
    FOSSILIZED.addMemberSpecies(ExtraBeesSpecies.RESIN);
    FOSSILIZED.addMemberSpecies(ExtraBeesSpecies.OIL);
    FOSSILIZED.addMemberSpecies(ExtraBeesSpecies.PEAT);
    FOSSILIZED.register();
    
    REFINED.addMemberSpecies(ExtraBeesSpecies.DISTILLED);
    REFINED.addMemberSpecies(ExtraBeesSpecies.FUEL);
    REFINED.addMemberSpecies(ExtraBeesSpecies.CREOSOTE);
    REFINED.addMemberSpecies(ExtraBeesSpecies.LATEX);
    REFINED.register();
    
    AQUATIC.addMemberSpecies(ExtraBeesSpecies.WATER);
    AQUATIC.addMemberSpecies(ExtraBeesSpecies.RIVER);
    AQUATIC.addMemberSpecies(ExtraBeesSpecies.OCEAN);
    AQUATIC.addMemberSpecies(ExtraBeesSpecies.INK);
    AQUATIC.register();
    
    SACCHARINE.addMemberSpecies(ExtraBeesSpecies.SWEET);
    SACCHARINE.addMemberSpecies(ExtraBeesSpecies.SUGAR);
    SACCHARINE.addMemberSpecies(ExtraBeesSpecies.FRUIT);
    SACCHARINE.addMemberSpecies(ExtraBeesSpecies.RIPENING);
    SACCHARINE.register();
    
    CLASSICAL.addMemberSpecies(ExtraBeesSpecies.MARBLE);
    CLASSICAL.addMemberSpecies(ExtraBeesSpecies.ROMAN);
    CLASSICAL.addMemberSpecies(ExtraBeesSpecies.GREEK);
    CLASSICAL.addMemberSpecies(ExtraBeesSpecies.CLASSICAL);
    CLASSICAL.register();
    
    VOLCANIC.addMemberSpecies(ExtraBeesSpecies.BASALT);
    VOLCANIC.addMemberSpecies(ExtraBeesSpecies.TEMPERED);
    VOLCANIC.addMemberSpecies(ExtraBeesSpecies.ANGRY);
    VOLCANIC.addMemberSpecies(ExtraBeesSpecies.VOLCANIC);
    VOLCANIC.addMemberSpecies(ExtraBeesSpecies.GLOWSTONE);
    VOLCANIC.register();
    
    VISCOUS.addMemberSpecies(ExtraBeesSpecies.VISCOUS);
    VISCOUS.addMemberSpecies(ExtraBeesSpecies.GLUTINOUS);
    VISCOUS.addMemberSpecies(ExtraBeesSpecies.STICKY);
    VISCOUS.register();
    
    VIRULENT.addMemberSpecies(ExtraBeesSpecies.MALICIOUS);
    VIRULENT.addMemberSpecies(ExtraBeesSpecies.INFECTIOUS);
    VIRULENT.addMemberSpecies(ExtraBeesSpecies.VIRULENT);
    VIRULENT.register();
    
    CAUSTIC.addMemberSpecies(ExtraBeesSpecies.CORROSIVE);
    CAUSTIC.addMemberSpecies(ExtraBeesSpecies.CAUSTIC);
    CAUSTIC.addMemberSpecies(ExtraBeesSpecies.ACIDIC);
    CAUSTIC.register();
    
    ENERGETIC.addMemberSpecies(ExtraBeesSpecies.EXCITED);
    ENERGETIC.addMemberSpecies(ExtraBeesSpecies.ENERGETIC);
    ENERGETIC.addMemberSpecies(ExtraBeesSpecies.ECSTATIC);
    ENERGETIC.register();
    
    SHADOW.addMemberSpecies(ExtraBeesSpecies.SHADOW);
    SHADOW.addMemberSpecies(ExtraBeesSpecies.DARKENED);
    SHADOW.addMemberSpecies(ExtraBeesSpecies.ABYSS);
    SHADOW.register();
    
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.RED);
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.YELLOW);
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.BLUE);
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.GREEN);
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.BLACK);
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.WHITE);
    PRIMARY.addMemberSpecies(ExtraBeesSpecies.BROWN);
    PRIMARY.register();
    
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.ORANGE);
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.CYAN);
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.PURPLE);
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.GRAY);
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.LIGHTBLUE);
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.PINK);
    SECONDARY.addMemberSpecies(ExtraBeesSpecies.LIMEGREEN);
    SECONDARY.register();
    
    TERTIARY.addMemberSpecies(ExtraBeesSpecies.MAGENTA);
    TERTIARY.addMemberSpecies(ExtraBeesSpecies.LIGHTGRAY);
    TERTIARY.register();
    
    FTB.addMemberSpecies(ExtraBeesSpecies.JADED);
    FTB.register();
    
    QUANTUM.addMemberSpecies(ExtraBeesSpecies.UNUSUAL);
    QUANTUM.addMemberSpecies(ExtraBeesSpecies.SPATIAL);
    QUANTUM.addMemberSpecies(ExtraBeesSpecies.QUANTUM);
    QUANTUM.register();
    
    BOTANIA.addMemberSpecies(ExtraBeesSpecies.MYSTICAL);
    BOTANIA.register();
  }
  
  public IClassification.EnumClassLevel getLevel()
  {
    return IClassification.EnumClassLevel.GENUS;
  }
  
  public IClassification[] getMemberGroups()
  {
    return null;
  }
  
  public void addMemberGroup(IClassification group) {}
  
  public IAlleleSpecies[] getMemberSpecies()
  {
    return (IAlleleSpecies[])this.speciesSet.toArray(new IAlleleSpecies[0]);
  }
  
  public void addMemberSpecies(IAlleleSpecies species)
  {
    this.speciesSet.add((IAlleleBeeSpecies)species);
    if ((species instanceof ExtraBeesSpecies)) {
      ((ExtraBeesSpecies)species).setBranch(this);
    }
  }
  
  public IClassification getParent()
  {
    return this.parent;
  }
  
  public void setParent(IClassification parent)
  {
    this.parent = parent;
  }
}
