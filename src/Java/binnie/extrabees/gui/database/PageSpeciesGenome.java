package binnie.extrabees.gui.database;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.PageSpecies;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IFlowerProvider;

public class PageSpeciesGenome
  extends PageSpecies
{
  ControlText pageSpeciesGenome_Title;
  ControlText pageSpeciesGenome_SpeedText;
  ControlText pageSpeciesGenome_LifespanText;
  ControlText pageSpeciesGenome_FertilityText;
  ControlText pageSpeciesGenome_FloweringText;
  ControlText pageSpeciesGenome_TerritoryText;
  ControlText pageSpeciesGenome_NocturnalText;
  ControlText pageSpeciesGenome_CaveDwellingText;
  ControlText pageSpeciesGenome_TolerantFlyerText;
  ControlText pageSpeciesGenome_FlowerText;
  ControlText pageSpeciesGenome_EffectText;
  
  public PageSpeciesGenome(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
    
    this.pageSpeciesGenome_Title = new ControlTextCentered(this, 8.0F, "Genome");
    
    new ControlText(this, new IArea(0.0F, 32.0F, 68.0F, 30.0F), "Speed:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 44.0F, 68.0F, 30.0F), "Lifespan:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 56.0F, 68.0F, 30.0F), "Fertility:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 68.0F, 68.0F, 30.0F), "Flowering:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 80.0F, 68.0F, 30.0F), "Territory:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 97.0F, 68.0F, 30.0F), "Behavior:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 109.0F, 68.0F, 30.0F), "Sunlight:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 121.0F, 68.0F, 30.0F), "Rain:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 138.0F, 68.0F, 30.0F), "Flower:", TextJustification.TopRight);
    new ControlText(this, new IArea(0.0F, 155.0F, 68.0F, 30.0F), "Effect:", TextJustification.TopRight);
    
    int x = 72;
    
    this.pageSpeciesGenome_SpeedText = new ControlText(this, new IArea(x, 32.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_LifespanText = new ControlText(this, new IArea(x, 44.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_FertilityText = new ControlText(this, new IArea(x, 56.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_FloweringText = new ControlText(this, new IArea(x, 68.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_TerritoryText = new ControlText(this, new IArea(x, 80.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_NocturnalText = new ControlText(this, new IArea(x, 97.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_CaveDwellingText = new ControlText(this, new IArea(x, 109.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_TolerantFlyerText = new ControlText(this, new IArea(x, 121.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_FlowerText = new ControlText(this, new IArea(x, 138.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
    
    this.pageSpeciesGenome_EffectText = new ControlText(this, new IArea(x, 155.0F, 72.0F, 30.0F), "", TextJustification.TopLeft);
  }
  
  public void onValueChanged(IAlleleSpecies species)
  {
    IAllele[] template = Binnie.Genetics.getBeeRoot().getTemplate(species.getUID());
    if (template != null)
    {
      IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(template);
      
      IBee bee = Binnie.Genetics.getBeeRoot().getBee(BinnieCore.proxy.getWorld(), genome);
      

      this.pageSpeciesGenome_SpeedText.setValue(rateSpeed(genome.getSpeed()));
      this.pageSpeciesGenome_LifespanText.setValue(rateLifespan(genome.getLifespan()));
      
      this.pageSpeciesGenome_FertilityText.setValue(genome.getFertility() + " children");
      
      this.pageSpeciesGenome_FloweringText.setValue(rateFlowering(genome.getFlowering()));
      int[] area = genome.getTerritory();
      this.pageSpeciesGenome_TerritoryText.setValue(area[0] + "x" + area[1] + "x" + area[2]);
      

      String behavior = "Daytime";
      if (genome.getPrimary().isNocturnal()) {
        behavior = "Nighttime";
      }
      if (genome.getNocturnal()) {
        behavior = "All Day";
      }
      this.pageSpeciesGenome_NocturnalText.setValue(behavior);
      if (genome.getCaveDwelling()) {
        this.pageSpeciesGenome_CaveDwellingText.setValue("Not Needed");
      } else {
        this.pageSpeciesGenome_CaveDwellingText.setValue("Required");
      }
      this.pageSpeciesGenome_TolerantFlyerText.setValue(tolerated(genome.getTolerantFlyer()));
      if (genome.getFlowerProvider() != null) {
        this.pageSpeciesGenome_FlowerText.setValue(genome.getFlowerProvider().getDescription());
      } else {
        this.pageSpeciesGenome_FlowerText.setValue("None");
      }
      this.pageSpeciesGenome_EffectText.setValue(genome.getEffect().getName());
    }
  }
  
  public static String rateFlowering(int flowering)
  {
    if (flowering >= 99) {
      return "Maximum";
    }
    if (flowering >= 35) {
      return "Fastest";
    }
    if (flowering >= 30) {
      return "Faster";
    }
    if (flowering >= 25) {
      return "Fast";
    }
    if (flowering >= 20) {
      return "Normal";
    }
    if (flowering >= 15) {
      return "Slow";
    }
    if (flowering >= 10) {
      return "Slower";
    }
    return "Slowest";
  }
  
  public static String rateSpeed(float speed)
  {
    if (speed >= 1.7F) {
      return "Fastest";
    }
    if (speed >= 1.4F) {
      return "Faster";
    }
    if (speed >= 1.2F) {
      return "Fast";
    }
    if (speed >= 1.0F) {
      return "Normal";
    }
    if (speed >= 0.8F) {
      return "Slow";
    }
    if (speed >= 0.6F) {
      return "Slower";
    }
    return "Slowest";
  }
  
  public static String rateLifespan(int life)
  {
    if (life >= 70) {
      return "Longest";
    }
    if (life >= 60) {
      return "Longer";
    }
    if (life >= 50) {
      return "Long";
    }
    if (life >= 45) {
      return "Elongated";
    }
    if (life >= 40) {
      return "Normal";
    }
    if (life >= 35) {
      return "Shortened";
    }
    if (life >= 30) {
      return "Short";
    }
    if (life >= 20) {
      return "Shorter";
    }
    return "Shortest";
  }
  
  public static String tolerated(boolean t)
  {
    if (t) {
      return "Tolerated";
    }
    return "Not Tolerated";
  }
}
