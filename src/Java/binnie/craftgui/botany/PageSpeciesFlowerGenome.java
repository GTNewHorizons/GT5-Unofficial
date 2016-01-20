package binnie.craftgui.botany;

import binnie.Binnie;
import binnie.botany.api.EnumFlowerChromosome;
import binnie.botany.api.EnumFlowerStage;
import binnie.botany.api.IAlleleFlowerSpecies;
import binnie.botany.api.IFlower;
import binnie.botany.api.IFlowerGenome;
import binnie.botany.api.IFlowerRoot;
import binnie.botany.core.BotanyCore;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.language.ManagerLanguage;
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
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import net.minecraft.item.ItemStack;

public class PageSpeciesFlowerGenome
  extends PageSpecies
{
  public PageSpeciesFlowerGenome(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
  }
  
  public void onValueChanged(IAlleleSpecies species)
  {
    deleteAllChildren();
    IAllele[] template = Binnie.Genetics.getFlowerRoot().getTemplate(species.getUID());
    if (template == null) {
      return;
    }
    IFlower tree = Binnie.Genetics.getFlowerRoot().templateAsIndividual(template);
    if (tree == null) {
      return;
    }
    IFlowerGenome genome = tree.getGenome();
    IAlleleFlowerSpecies treeSpecies = genome.getPrimary();
    
    int w = 144;
    int h = 176;
    
    new ControlText(this, new IArea(0.0F, 4.0F, w, 16.0F), "Genome", TextJustification.MiddleCenter);
    
    ControlScrollableContent scrollable = new ControlScrollableContent(this, 4.0F, 20.0F, w - 8, h - 8 - 16, 12.0F);
    
    Control contents = new Control(scrollable, 0.0F, 0.0F, w - 8 - 12, h - 8 - 16);
    
    int tw = w - 8 - 12;
    int w1 = 55;
    int w2 = tw - 50;
    int y = 0;
    int th = 14;
    int th2 = 18;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), "Temp. : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), treeSpecies.getTemperature().getName(), TextJustification.MiddleLeft);
    
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), "Moist. : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), Binnie.Language.localise(treeSpecies.getMoisture()), TextJustification.MiddleLeft);
    

    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), "pH. : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), Binnie.Language.localise(treeSpecies.getPH()), TextJustification.MiddleLeft);
    
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), "Fertility : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getFertility() + "x", TextJustification.MiddleLeft);
    y += th;
    
    float lifespan = genome.getLifespan() * 68.269997F / genome.getAgeChance() / 24000.0F;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), "Lifespan : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), "" + String.format("%.2f", new Object[] { Float.valueOf(lifespan) }) + " days", TextJustification.MiddleLeft);
    
    y += th;
    
    new ControlText(contents, new IArea(0.0F, y, w1, th), "Nectar : ", TextJustification.MiddleRight);
    new ControlText(contents, new IArea(w1, y, w2, th), genome.getActiveAllele(EnumFlowerChromosome.SAPPINESS).getName(), TextJustification.MiddleLeft);
    

    y += th;
    
    int x = w1;
    int tot = 0;
    for (IIndividual vid : BotanyCore.getFlowerRoot().getIndividualTemplates()) {
      if (vid.getGenome().getPrimary() == treeSpecies)
      {
        if ((tot > 0) && (tot % 3 == 0))
        {
          x -= 54;
          y += 18;
        }
        ItemStack stack = BotanyCore.getFlowerRoot().getMemberStack((IFlower)vid, EnumFlowerStage.FLOWER.ordinal());
        ControlItemDisplay display = new ControlItemDisplay(contents, x, y);
        display.setItemStack(stack);
        tot++;
        x += 18;
      }
    }
    int numOfLines = 1 + (tot - 1) / 3;
    
    new ControlText(contents, new IArea(0.0F, y - (numOfLines - 1) * 18, w1, 4 + 18 * numOfLines), "Varieties : ", TextJustification.MiddleRight);
    

    y += th;
    
    contents.setSize(new IPoint(contents.size().x(), y));
    
    scrollable.setScrollableContent(contents);
  }
  
  public static String tolerated(boolean t)
  {
    if (t) {
      return "Tolerated";
    }
    return "Not Tolerated";
  }
}
