package binnie.craftgui.extratrees.dictionary;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.genetics.BreedingSystem;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.scroll.ControlScrollableContent;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.PageSpecies;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.proxy.Proxy;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.IFruitProvider;
import forestry.api.arboriculture.IGrowthProvider;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.core.EnumTemperature;
import forestry.api.core.ForestryAPI;
import forestry.api.core.ITextureManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.EnumPlantType;

public class PageSpeciesTreeGenome
  extends PageSpecies
{
  public PageSpeciesTreeGenome(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
  }
  
  public void onValueChanged(IAlleleSpecies species)
  {
    deleteAllChildren();
    IAllele[] template = Binnie.Genetics.getTreeRoot().getTemplate(species.getUID());
    if (template == null) {
      return;
    }
    ITree tree = Binnie.Genetics.getTreeRoot().templateAsIndividual(template);
    if (tree == null) {
      return;
    }
    ITreeGenome genome = tree.getGenome();
    IAlleleTreeSpecies treeSpecies = genome.getPrimary();
    
    int w = 144;
    int h = 176;
    
    new ControlText(this, new IArea(0.0F, 4.0F, w, 16.0F), ((DatabaseTab)getValue()).toString(), TextJustification.MiddleCenter);
    
    ControlScrollableContent scrollable = new ControlScrollableContent(this, 4.0F, 20.0F, w - 8, h - 8 - 16, 12.0F);
    
    Control contents = new Control(scrollable, 0.0F, 0.0F, w - 8 - 12, h - 8 - 16);
    
    int tw = w - 8 - 12;
    int w1 = 65;
    int w2 = tw - 50;
    int y = 0;
    int th = 14;
    int th2 = 18;
    
    BreedingSystem syst = Binnie.Genetics.treeBreedingSystem;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.PLANT) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), treeSpecies.getPlantType().toString(), TextJustification.MiddleLeft);
    
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), BinnieCore.proxy.localise("gui.temperature.short") + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), treeSpecies.getTemperature().getName(), TextJustification.MiddleLeft);
    
    y += th;
    
    IIcon leaf = ForestryAPI.textureManager.getIcon(treeSpecies.getLeafIconIndex(tree, false));
    
    IIcon fruit = null;
    int fruitColour = 16777215;
    try
    {
      fruit = ForestryAPI.textureManager.getIcon(genome.getFruitProvider().getIconIndex(genome, null, 0, 0, 0, 100, false));
      fruitColour = genome.getFruitProvider().getColour(genome, null, 0, 0, 0, 100);
    }
    catch (Exception e) {}
    if (leaf != null)
    {
      new ControlText(contents, new IArea(0.0F, y, w1, th2), ExtraTrees.proxy.localise("gui.database.leaves") + " : ", TextJustification.MiddleRight);
      
      new ControlBlockIconDisplay(contents, w1, y, leaf).setColour(treeSpecies.getLeafColour(tree));
      if ((fruit != null) && (!treeSpecies.getUID().equals("forestry.treeOak"))) {
        new ControlBlockIconDisplay(contents, w1, y, fruit).setColour(fruitColour);
      }
      y += th2;
    }
    ItemStack log = treeSpecies.getLogStacks().length > 0 ? treeSpecies.getLogStacks()[0] : null;
    if (log != null)
    {
      new ControlText(contents, new IArea(0.0F, y, w1, th2), ExtraTrees.proxy.localise("gui.database.log") + " : ", TextJustification.MiddleRight);
      
      ControlItemDisplay display = new ControlItemDisplay(contents, w1, y);
      display.setItemStack(log);
      display.setTooltip();
      
      y += th2;
    }
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.GROWTH) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getGrowthProvider().getDescription(), TextJustification.MiddleLeft);
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.HEIGHT) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getHeight() + "x", TextJustification.MiddleLeft);
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.FERTILITY) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getFertility() + "x", TextJustification.MiddleLeft);
    y += th;
    
    List<ItemStack> fruits = new ArrayList();
    for (ItemStack stack : genome.getFruitProvider().getProducts()) {
      fruits.add(stack);
    }
    if (!fruits.isEmpty())
    {
      new ControlText(contents, new IArea(0.0F, y, w1, th2), syst.getChromosomeShortName(EnumTreeChromosome.FRUITS) + " : ", TextJustification.MiddleRight);
      for (ItemStack fruitw : fruits)
      {
        ControlItemDisplay display = new ControlItemDisplay(contents, w1, y);
        display.setItemStack(fruitw);
        display.setTooltip();
        
        y += th2;
      }
    }
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.YIELD) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getYield() + "x", TextJustification.MiddleLeft);
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.SAPPINESS) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getSappiness() + "x", TextJustification.MiddleLeft);
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.MATURATION) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getMaturationTime() + "x", TextJustification.MiddleLeft);
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), syst.getChromosomeShortName(EnumTreeChromosome.GIRTH) + " : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getGirth() + "x" + genome.getGirth(), TextJustification.MiddleLeft);
    y += th;
    
    contents.setSize(new IPoint(contents.size().x(), y));
    
    scrollable.setScrollableContent(contents);
  }
  
  public static String tolerated(boolean t)
  {
    if (t) {
      return BinnieCore.proxy.localise("gui.tolerated");
    }
    return BinnieCore.proxy.localise("gui.nottolerated");
  }
}
