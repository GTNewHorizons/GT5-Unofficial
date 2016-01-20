package binnie.extrabees.genetics;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.Mods;
import binnie.core.Mods.Mod;
import binnie.core.genetics.ForestryAllele.BeeSpecies;
import binnie.core.genetics.ForestryAllele.Fertility;
import binnie.core.genetics.ForestryAllele.Flowering;
import binnie.core.genetics.ForestryAllele.Lifespan;
import binnie.core.genetics.ForestryAllele.Speed;
import binnie.core.genetics.ForestryAllele.Territory;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.genetics.Tolerance;
import binnie.core.item.IItemEnum;
import binnie.core.proxy.BinnieProxy;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.genetics.effect.ExtraBeesEffect;
import binnie.extrabees.products.EnumHoneyComb;
import binnie.extrabees.products.ItemHoneyComb.VanillaComb;
import binnie.extrabees.proxy.ExtraBeesProxy;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IIconProvider;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IClassification;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public enum ExtraBeesSpecies
  implements IAlleleBeeSpecies, IIconProvider
{
  ARID("aridus", 12511316),  BARREN("infelix", 14733923),  DESOLATE("desolo", 13744272),  GNAWING("apica", 15234224),  ROTTEN("caries", 12574902),  BONE("os", 15330792),  CREEPER("erepo", 2942485),  DECOMPOSING("aegrus", 5388049),  ROCK("saxum", 11053224),  STONE("lapis", 7697781),  GRANITE("granum", 6903125),  MINERAL("minerale", 7239037),  COPPER("cuprous", 13722376),  TIN("stannus", 12431805),  IRON("ferrous", 11038808),  LEAD("plumbous", 11373483),  ZINC("spelta", 15592447),  TITANIUM("titania", 11578083),  BRONZE,  BRASS,  STEEL,  TUNGSTATE("wolfram", 1249812),  GOLD("aureus", 15125515),  SILVER("argentus", 14408667),  ELECTRUM,  PLATINUM("platina", 14408667),  LAPIS("lazuli", 4009179),  SODALITE,  PYRITE,  BAUXITE,  CINNABAR,  SPHALERITE,  EMERALD("emerala", 1900291),  RUBY("ruba", 14024704),  SAPPHIRE("saphhira", 673791),  OLIVINE,  DIAMOND("diama", 8371706),  UNSTABLE("levis", 4099124),  NUCLEAR("nucleus", 4312111),  RADIOACTIVE("fervens", 2031360),  ANCIENT("antiquus", 15915919),  PRIMEVAL("priscus", 11773563),  PREHISTORIC("pristinus", 7232064),  RELIC("sapiens", 5062166),  COAL("carbo", 8025672),  RESIN("lacrima", 10908443),  OIL("lubricus", 5719920),  PEAT,  DISTILLED("distilli", 3498838),  FUEL("refina", 16760835),  CREOSOTE("creosota", 9936403),  LATEX("latex", 4803134),  WATER("aqua", 9741055),  RIVER("flumen", 8631252),  OCEAN("mare", 1912493),  INK("atramentum", 922695),  GROWING("tyrelli", 6024152),  THRIVING("thriva", 3466109),  BLOOMING("blooma", 704308),  SWEET("mellitus", 16536049),  SUGAR("dulcis", 15127520),  RIPENING("ripa", 11716445),  FRUIT("pomum", 14375030),  ALCOHOL("vinum", 15239777),  FARM("ager", 7723872),  MILK("lacteus", 14936296),  COFFEE("arabica", 9199152),  CITRUS,  MINT,  SWAMP("paludis", 3500339),  BOGGY("lama", 7887913),  FUNGAL("boletus", 13722112),  MARBLE,  ROMAN,  GREEK,  CLASSICAL,  BASALT("aceri", 9202025),  TEMPERED("iratus", 9062472),  ANGRY,  VOLCANIC("volcano", 5049356),  MALICIOUS("acerbus", 7875191),  INFECTIOUS("contagio", 12070581),  VIRULENT("morbus", 15733740),  VISCOUS("liquidus", 608014),  GLUTINOUS("glutina", 1936423),  STICKY("lentesco", 1565480),  CORROSIVE("corrumpo", 4873227),  CAUSTIC("torrens", 8691997),  ACIDIC("acidus", 12644374),  EXCITED("excita", 16729413),  ENERGETIC("energia", 15218119),  ECSTATIC("ecstatica", 11482600),  ARTIC("artica", 11395296),  FREEZING("glacia", 8119267),  SHADOW("shadowa", 5855577),  DARKENED("darka", 3354163),  ABYSS("abyssba", 2164769),  RED("rubra", 16711680),  YELLOW("fulvus", 16768256),  BLUE("caeruleus", 8959),  GREEN("prasinus", 39168),  BLACK("niger", 5723991),  WHITE("albus", 16777215),  BROWN("fuscus", 6042895),  ORANGE("flammeus", 16751872),  CYAN("cyana", 65509),  PURPLE("purpureus", 11403519),  GRAY("ravus", 12237498),  LIGHTBLUE("aqua", 40447),  PINK("rosaceus", 16744671),  LIMEGREEN("lima", 65288),  MAGENTA("fuchsia", 16711884),  LIGHTGRAY("canus", 13224393),  CELEBRATORY("celeba", 16386666),  JADED("jadeca", 16386666),  GLOWSTONE("glowia", 14730779),  HAZARDOUS("infensus", 11562024),  NICKEL("claro", 16768764),  INVAR,  QUANTUM("quanta", 3655131),  SPATIAL("spatia", 4987872),  UNUSUAL("daniella", 5874874),  YELLORIUM("yellori", 14019840),  CYANITE("cyanita", 34541),  BLUTONIUM("caruthus", 1769702),  MYSTICAL("mystica", 4630306);
  
  private ExtraBeesSpecies(String binomial, int colour)
  {
    this.uid = toString().toLowerCase();
    this.binomial = binomial;
    this.primaryColor = colour;
  }
  
  private ExtraBeesSpecies()
  {
    this.state = State.Deprecated;
  }
  
  private int primaryColor = 16777215;
  private int secondaryColor = 16768022;
  private EnumTemperature temperature = EnumTemperature.NORMAL;
  private EnumHumidity humidity = EnumHumidity.NORMAL;
  private boolean hasEffect = false;
  private boolean isSecret = true;
  private boolean isCounted = true;
  private String binomial = "";
  private IClassification branch = null;
  private String uid = "";
  private Achievement achievement = null;
  private boolean dominant = true;
  private HashMap<ItemStack, Integer> products = new LinkedHashMap();
  private HashMap<ItemStack, Integer> specialties = new LinkedHashMap();
  public HashMap<ItemStack, Integer> allProducts = new LinkedHashMap();
  public HashMap<ItemStack, Integer> allSpecialties = new LinkedHashMap();
  private IAllele[] template;
  
  public static enum State
  {
    Active,  Inactive,  Deprecated;
    
    private State() {}
  }
  
  public State state = State.Active;
  @SideOnly(Side.CLIENT)
  private IIcon[][] icons;
  
  public String getName()
  {
    return ExtraBees.proxy.localise("species." + name().toLowerCase() + ".name");
  }
  
  public String getDescription()
  {
    return ExtraBees.proxy.localiseOrBlank("species." + name().toLowerCase() + ".desc");
  }
  
  public EnumTemperature getTemperature()
  {
    return this.temperature;
  }
  
  public EnumHumidity getHumidity()
  {
    return this.humidity;
  }
  
  public boolean hasEffect()
  {
    return this.hasEffect;
  }
  
  public boolean isSecret()
  {
    return this.isSecret;
  }
  
  public boolean isCounted()
  {
    return this.isCounted;
  }
  
  public String getBinomial()
  {
    return this.binomial;
  }
  
  public String getAuthority()
  {
    return "Binnie";
  }
  
  public IClassification getBranch()
  {
    return this.branch;
  }
  
  public String getUID()
  {
    return "extrabees.species." + this.uid;
  }
  
  public boolean isDominant()
  {
    return this.dominant;
  }
  
  public HashMap<ItemStack, Integer> getProducts()
  {
    return this.products;
  }
  
  public HashMap<ItemStack, Integer> getSpecialty()
  {
    return this.specialties;
  }
  
  private void setState(State state)
  {
    this.state = state;
  }
  
  public void registerTemplate()
  {
    Binnie.Genetics.getBeeRoot().registerTemplate(getTemplate());
    if (this.state != State.Active) {
      AlleleManager.alleleRegistry.blacklistAllele(getUID());
    }
  }
  
  public void addProduct(ItemStack product, int chance)
  {
    if (product == null)
    {
      setState(State.Inactive);
    }
    else
    {
      this.products.put(product, Integer.valueOf(chance));
      this.allProducts.put(product, Integer.valueOf(chance));
    }
  }
  
  public void addProduct(IItemEnum product, int chance)
  {
    if (product.isActive())
    {
      addProduct(product.get(1), chance);
    }
    else
    {
      this.allProducts.put(product.get(1), Integer.valueOf(chance));
      setState(State.Inactive);
    }
  }
  
  public void addSpecialty(ItemStack product, int chance)
  {
    if (product == null)
    {
      setState(State.Inactive);
    }
    else
    {
      this.specialties.put(product, Integer.valueOf(chance));
      this.allSpecialties.put(product, Integer.valueOf(chance));
    }
  }
  
  private void addSpecialty(IItemEnum product, int chance)
  {
    if (product.isActive())
    {
      addSpecialty(product.get(1), chance);
    }
    else
    {
      setState(State.Inactive);
      this.allSpecialties.put(product.get(1), Integer.valueOf(chance));
    }
  }
  
  private void setHumidity(EnumHumidity humidity)
  {
    this.humidity = humidity;
  }
  
  private void setTemperature(EnumTemperature temperature)
  {
    this.temperature = temperature;
  }
  
  public static IAllele[] getDefaultTemplate()
  {
    return Binnie.Genetics.getBeeRoot().getDefaultTemplate();
  }
  
  public IAllele[] getTemplate()
  {
    this.template[EnumBeeChromosome.SPECIES.ordinal()] = this;
    return this.template;
  }
  
  public void importTemplate(ForestryAllele.BeeSpecies species)
  {
    importTemplate(species.getTemplate());
  }
  
  public void importTemplate(ExtraBeesSpecies species)
  {
    importTemplate(species.getTemplate());
  }
  
  public void importTemplate(IAllele[] template)
  {
    this.template = ((IAllele[])template.clone());
    setHumidity(((IAlleleSpecies)template[0]).getHumidity());
    setTemperature(((IAlleleSpecies)template[0]).getTemperature());
    setSecondaryColor(((IAlleleSpecies)template[0]).getIconColour(1));
    this.template[EnumBeeChromosome.SPECIES.ordinal()] = this;
  }
  
  public void recessive()
  {
    this.dominant = false;
  }
  
  public void setIsSecret(boolean secret)
  {
    this.isSecret = secret;
  }
  
  public void setHasEffect(boolean effect)
  {
    this.hasEffect = effect;
  }
  
  public void setSecondaryColor(int colour)
  {
    this.secondaryColor = colour;
  }
  
  public static void doInit()
  {
    for (ExtraBeesSpecies species : ) {
      species.template = getDefaultTemplate();
    }
    int aridBody = 13362036;
    int rockBody = 10066329;
    int endBody = 14278302;
    
    ARID.importTemplate(ForestryAllele.BeeSpecies.Modest);
    ARID.addProduct(EnumHoneyComb.BARREN, 30);
    ARID.setHumidity(EnumHumidity.ARID);
    ARID.setFlowerProvider(ExtraBeesFlowers.DEAD.getUID());
    ARID.setTemperatureTolerance(Tolerance.Up1);
    ARID.setSecondaryColor(aridBody);
    
    BARREN.importTemplate(ARID);
    BARREN.setFertility(ForestryAllele.Fertility.Low);
    BARREN.addProduct(EnumHoneyComb.BARREN, 30);
    
    DESOLATE.addProduct(EnumHoneyComb.BARREN, 30);
    DESOLATE.importTemplate(BARREN);
    DESOLATE.setEffect(ExtraBeesEffect.HUNGER.getUID());
    DESOLATE.recessive();
    DESOLATE.setNocturnal();
    DESOLATE.setHasEffect(true);
    
    GNAWING.importTemplate(BARREN);
    GNAWING.setFlowerProvider(ExtraBeesFlowers.WOOD.getUID());
    GNAWING.addProduct(EnumHoneyComb.BARREN, 25);
    GNAWING.addSpecialty(EnumHoneyComb.SAWDUST, 25);
    
    ROTTEN.importTemplate(DESOLATE);
    ROTTEN.setNocturnal();
    ROTTEN.setCaveDwelling();
    ROTTEN.setTolerantFlyer();
    ROTTEN.setEffect(ExtraBeesEffect.SPAWN_ZOMBIE.getUID());
    ROTTEN.addProduct(EnumHoneyComb.BARREN, 30);
    ROTTEN.addSpecialty(EnumHoneyComb.ROTTEN, 10);
    
    BONE.importTemplate(ROTTEN);
    BONE.addProduct(EnumHoneyComb.BARREN, 30);
    BONE.addSpecialty(EnumHoneyComb.BONE, 10);
    BONE.setEffect(ExtraBeesEffect.SPAWN_SKELETON.getUID());
    
    CREEPER.importTemplate(ROTTEN);
    CREEPER.setAllDay();
    CREEPER.addProduct(EnumHoneyComb.BARREN, 30);
    CREEPER.addSpecialty(ItemHoneyComb.VanillaComb.POWDERY.get(), 8);
    CREEPER.setEffect(ExtraBeesEffect.SPAWN_CREEPER.getUID());
    
    DECOMPOSING.importTemplate(BARREN);
    DECOMPOSING.addProduct(EnumHoneyComb.BARREN, 30);
    DECOMPOSING.addSpecialty(EnumHoneyComb.COMPOST, 8);
    
    ROCK.addProduct(EnumHoneyComb.STONE, 30);
    ROCK.setIsSecret(false);
    ROCK.setAllDay();
    ROCK.setCaveDwelling();
    ROCK.setTolerantFlyer();
    ROCK.setTemperatureTolerance(Tolerance.Both1);
    ROCK.setHumidityTolerance(Tolerance.Both1);
    ROCK.setFlowerProvider(ExtraBeesFlowers.ROCK.getUID());
    ROCK.setFertility(ForestryAllele.Fertility.Low);
    ROCK.setLifespan(ForestryAllele.Lifespan.Short);
    ROCK.setSecondaryColor(rockBody);
    
    STONE.addProduct(EnumHoneyComb.STONE, 30);
    STONE.importTemplate(ROCK);
    STONE.recessive();
    STONE.setSecondaryColor(rockBody);
    
    GRANITE.addProduct(EnumHoneyComb.STONE, 30);
    GRANITE.importTemplate(STONE);
    GRANITE.setTemperatureTolerance(Tolerance.Both2);
    GRANITE.setHumidityTolerance(Tolerance.Both2);
    GRANITE.setSecondaryColor(rockBody);
    
    MINERAL.addProduct(EnumHoneyComb.STONE, 30);
    MINERAL.importTemplate(GRANITE);
    MINERAL.setSecondaryColor(rockBody);
    


    COPPER.addProduct(EnumHoneyComb.STONE, 20);
    COPPER.addSpecialty(EnumHoneyComb.COPPER, 6);
    COPPER.importTemplate(MINERAL);
    COPPER.setSecondaryColor(rockBody);
    
    TIN.addProduct(EnumHoneyComb.STONE, 20);
    TIN.addSpecialty(EnumHoneyComb.TIN, 6);
    TIN.importTemplate(MINERAL);
    TIN.setSecondaryColor(rockBody);
    
    IRON.addProduct(EnumHoneyComb.STONE, 20);
    IRON.addSpecialty(EnumHoneyComb.IRON, 5);
    IRON.importTemplate(MINERAL);
    IRON.recessive();
    IRON.setSecondaryColor(rockBody);
    
    LEAD.addProduct(EnumHoneyComb.STONE, 20);
    LEAD.addSpecialty(EnumHoneyComb.LEAD, 5);
    LEAD.importTemplate(MINERAL);
    LEAD.setSecondaryColor(rockBody);
    
    NICKEL.addProduct(EnumHoneyComb.STONE, 20);
    NICKEL.addSpecialty(EnumHoneyComb.NICKEL, 5);
    NICKEL.importTemplate(MINERAL);
    NICKEL.setSecondaryColor(rockBody);
    


    ZINC.addProduct(EnumHoneyComb.STONE, 20);
    ZINC.addSpecialty(EnumHoneyComb.ZINC, 5);
    ZINC.importTemplate(MINERAL);
    ZINC.setSecondaryColor(rockBody);
    
    TITANIUM.addProduct(EnumHoneyComb.STONE, 20);
    TITANIUM.addSpecialty(EnumHoneyComb.TITANIUM, 2);
    TITANIUM.importTemplate(MINERAL);
    TITANIUM.setSecondaryColor(rockBody);
    
    TUNGSTATE.addProduct(EnumHoneyComb.STONE, 20);
    TUNGSTATE.addSpecialty(EnumHoneyComb.TUNGSTEN, 1);
    TUNGSTATE.importTemplate(MINERAL);
    TUNGSTATE.setSecondaryColor(rockBody);
    



    GOLD.addProduct(EnumHoneyComb.STONE, 20);
    GOLD.addSpecialty(EnumHoneyComb.GOLD, 2);
    GOLD.importTemplate(MINERAL);
    GOLD.setSecondaryColor(rockBody);
    
    SILVER.addProduct(EnumHoneyComb.STONE, 20);
    SILVER.addSpecialty(EnumHoneyComb.SILVER, 2);
    SILVER.importTemplate(MINERAL);
    SILVER.recessive();
    SILVER.recessive();
    SILVER.setSecondaryColor(rockBody);
    
    PLATINUM.addProduct(EnumHoneyComb.STONE, 20);
    PLATINUM.addSpecialty(EnumHoneyComb.PLATINUM, 1);
    PLATINUM.importTemplate(MINERAL);
    PLATINUM.recessive();
    PLATINUM.setSecondaryColor(rockBody);
    

    LAPIS.addProduct(EnumHoneyComb.STONE, 20);
    LAPIS.addSpecialty(EnumHoneyComb.LAPIS, 5);
    LAPIS.importTemplate(MINERAL);
    LAPIS.setSecondaryColor(rockBody);
    
    EMERALD.addProduct(EnumHoneyComb.STONE, 20);
    EMERALD.addSpecialty(EnumHoneyComb.EMERALD, 4);
    EMERALD.importTemplate(MINERAL);
    EMERALD.setSecondaryColor(rockBody);
    
    RUBY.addProduct(EnumHoneyComb.STONE, 20);
    RUBY.addSpecialty(EnumHoneyComb.RUBY, 3);
    RUBY.importTemplate(MINERAL);
    RUBY.setSecondaryColor(rockBody);
    
    SAPPHIRE.addProduct(EnumHoneyComb.STONE, 20);
    SAPPHIRE.addSpecialty(EnumHoneyComb.SAPPHIRE, 3);
    SAPPHIRE.importTemplate(MINERAL);
    SAPPHIRE.setSecondaryColor(rockBody);
    
    DIAMOND.addProduct(EnumHoneyComb.STONE, 20);
    DIAMOND.addSpecialty(EnumHoneyComb.DIAMOND, 1);
    DIAMOND.importTemplate(MINERAL);
    DIAMOND.setSecondaryColor(rockBody);
    
    UNSTABLE.importTemplate(MINERAL);
    UNSTABLE.addProduct(EnumHoneyComb.BARREN, 20);
    UNSTABLE.setEffect(ExtraBeesEffect.RADIOACTIVE.getUID());
    UNSTABLE.setFertility(ForestryAllele.Fertility.Low);
    UNSTABLE.setLifespan(ForestryAllele.Lifespan.Shortest);
    UNSTABLE.recessive();
    
    NUCLEAR.importTemplate(UNSTABLE);
    NUCLEAR.addProduct(EnumHoneyComb.BARREN, 20);
    NUCLEAR.recessive();
    
    RADIOACTIVE.importTemplate(NUCLEAR);
    RADIOACTIVE.addProduct(EnumHoneyComb.BARREN, 20);
    RADIOACTIVE.addSpecialty(EnumHoneyComb.URANIUM, 2);
    RADIOACTIVE.setHasEffect(true);
    RADIOACTIVE.recessive();
    
    ANCIENT.importTemplate(ForestryAllele.BeeSpecies.Noble);
    ANCIENT.addProduct(EnumHoneyComb.OLD, 30);
    ANCIENT.setLifespan(ForestryAllele.Lifespan.Elongated);
    
    PRIMEVAL.importTemplate(ANCIENT);
    PRIMEVAL.addProduct(EnumHoneyComb.OLD, 30);
    PRIMEVAL.setLifespan(ForestryAllele.Lifespan.Long);
    
    PREHISTORIC.importTemplate(ANCIENT);
    PREHISTORIC.addProduct(EnumHoneyComb.OLD, 30);
    PREHISTORIC.setLifespan(ForestryAllele.Lifespan.Longer);
    PREHISTORIC.setFertility(ForestryAllele.Fertility.Low);
    PREHISTORIC.recessive();
    
    RELIC.importTemplate(ANCIENT);
    RELIC.addProduct(EnumHoneyComb.OLD, 30);
    RELIC.setHasEffect(true);
    RELIC.setLifespan(ForestryAllele.Lifespan.Longest);
    
    COAL.importTemplate(ANCIENT);
    COAL.setLifespan(ForestryAllele.Lifespan.Normal);
    COAL.addProduct(EnumHoneyComb.OLD, 20);
    COAL.addSpecialty(EnumHoneyComb.COAL, 8);
    
    RESIN.importTemplate(COAL);
    RESIN.addProduct(EnumHoneyComb.OLD, 20);
    RESIN.addSpecialty(EnumHoneyComb.RESIN, 5);
    RESIN.recessive();
    
    OIL.importTemplate(COAL);
    OIL.addProduct(EnumHoneyComb.OLD, 20);
    OIL.addSpecialty(EnumHoneyComb.OIL, 5);
    





    DISTILLED.importTemplate(OIL);
    DISTILLED.addProduct(EnumHoneyComb.OIL, 10);
    DISTILLED.recessive();
    
    FUEL.importTemplate(OIL);
    FUEL.addProduct(EnumHoneyComb.OIL, 10);
    FUEL.addSpecialty(EnumHoneyComb.FUEL, 4);
    FUEL.setHasEffect(true);
    
    CREOSOTE.importTemplate(COAL);
    CREOSOTE.addProduct(EnumHoneyComb.COAL, 10);
    CREOSOTE.addSpecialty(EnumHoneyComb.CREOSOTE, 7);
    CREOSOTE.setHasEffect(true);
    
    LATEX.importTemplate(RESIN);
    LATEX.addProduct(EnumHoneyComb.RESIN, 10);
    LATEX.addSpecialty(EnumHoneyComb.LATEX, 5);
    LATEX.setHasEffect(true);
    
    WATER.addProduct(EnumHoneyComb.WATER, 30);
    WATER.setIsSecret(false);
    WATER.setTolerantFlyer();
    WATER.setHumidityTolerance(Tolerance.Both1);
    WATER.setFlowerProvider(ExtraBeesFlowers.WATER.getUID());
    WATER.setFlowering(ForestryAllele.Flowering.Slow);
    WATER.setEffect(ExtraBeesEffect.WATER.getUID());
    WATER.setHumidity(EnumHumidity.DAMP);
    
    RIVER.importTemplate(WATER);
    RIVER.addProduct(EnumHoneyComb.WATER, 30);
    RIVER.addSpecialty(EnumHoneyComb.CLAY, 20);
    RIVER.importTemplate(WATER);
    
    OCEAN.importTemplate(WATER);
    OCEAN.addProduct(EnumHoneyComb.WATER, 30);
    OCEAN.importTemplate(WATER);
    OCEAN.recessive();
    
    INK.importTemplate(OCEAN);
    INK.addProduct(EnumHoneyComb.WATER, 30);
    INK.addSpecialty(new ItemStack(Items.dye, 1, 0), 10);
    
    GROWING.importTemplate(ForestryAllele.BeeSpecies.Forest);
    GROWING.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 35);
    GROWING.setFlowering(ForestryAllele.Flowering.Average);
    GROWING.setFlowerProvider(ExtraBeesFlowers.LEAVES.getUID());
    
    THRIVING.importTemplate(GROWING);
    THRIVING.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 35);
    THRIVING.setFlowering(ForestryAllele.Flowering.Fast);
    
    BLOOMING.importTemplate(THRIVING);
    BLOOMING.setFlowering(ForestryAllele.Flowering.Fastest);
    BLOOMING.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 35);
    BLOOMING.setFlowerProvider(ExtraBeesFlowers.Sapling.getUID());
    BLOOMING.setEffect(ExtraBeesEffect.BonemealSapling.getUID());
    
    SWEET.importTemplate(ForestryAllele.BeeSpecies.Rural);
    SWEET.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 40);
    SWEET.addProduct(new ItemStack(Items.sugar, 1, 0), 10);
    SWEET.setFlowerProvider(ExtraBeesFlowers.SUGAR.getUID());
    
    SUGAR.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 40);
    SUGAR.addProduct(new ItemStack(Items.sugar, 1, 0), 20);
    SUGAR.importTemplate(SWEET);
    
    RIPENING.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 30);
    RIPENING.addProduct(new ItemStack(Items.sugar, 1, 0), 10);
    RIPENING.addSpecialty(EnumHoneyComb.FRUIT, 10);
    RIPENING.setFlowerProvider(ExtraBeesFlowers.Fruit.getUID());
    RIPENING.importTemplate(SUGAR);
    
    FRUIT.importTemplate(RIPENING);
    FRUIT.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 30);
    FRUIT.addProduct(new ItemStack(Items.sugar, 1, 0), 15);
    FRUIT.addSpecialty(EnumHoneyComb.FRUIT, 20);
    FRUIT.setEffect(ExtraBeesEffect.BonemealFruit.getUID());
    FRUIT.setHasEffect(true);
    
    ALCOHOL.importTemplate(SWEET);
    ALCOHOL.addProduct(ItemHoneyComb.VanillaComb.WHEATEN.get(), 30);
    ALCOHOL.addSpecialty(EnumHoneyComb.ALCOHOL, 10);
    ALCOHOL.setEffect("forestry.effectDrunkard");
    ALCOHOL.recessive();
    
    FARM.addProduct(ItemHoneyComb.VanillaComb.WHEATEN.get(), 30);
    FARM.addSpecialty(EnumHoneyComb.SEED, 10);
    FARM.importTemplate(ForestryAllele.BeeSpecies.Rural);
    
    MILK.addProduct(ItemHoneyComb.VanillaComb.WHEATEN.get(), 30);
    MILK.addSpecialty(EnumHoneyComb.MILK, 10);
    MILK.importTemplate(ForestryAllele.BeeSpecies.Rural);
    
    COFFEE.addProduct(ItemHoneyComb.VanillaComb.WHEATEN.get(), 30);
    COFFEE.addSpecialty(EnumHoneyComb.COFFEE, 8);
    COFFEE.importTemplate(ForestryAllele.BeeSpecies.Rural);
    








    SWAMP.addProduct(ItemHoneyComb.VanillaComb.MOSSY.get(), 30);
    SWAMP.importTemplate(ForestryAllele.BeeSpecies.Marshy);
    SWAMP.setHumidity(EnumHumidity.DAMP);
    SWAMP.setEffect(ExtraBeesEffect.SLOW.getUID());
    
    BOGGY.importTemplate(SWAMP);
    BOGGY.addProduct(ItemHoneyComb.VanillaComb.MOSSY.get(), 30);
    BOGGY.importTemplate(SWAMP);
    BOGGY.recessive();
    
    FUNGAL.importTemplate(BOGGY);
    FUNGAL.addProduct(ItemHoneyComb.VanillaComb.MOSSY.get(), 30);
    FUNGAL.addSpecialty(EnumHoneyComb.FUNGAL, 15);
    FUNGAL.importTemplate(BOGGY);
    FUNGAL.setEffect(ExtraBeesEffect.BonemealMushroom.getUID());
    FUNGAL.setHasEffect(true);
    
    BASALT.addProduct(ItemHoneyComb.VanillaComb.SIMMERING.get(), 25);
    BASALT.importTemplate(ForestryAllele.BeeSpecies.Sinister);
    BASALT.setEffect("forestry.effectAggressive");
    BASALT.setSecondaryColor(10101539);
    BASALT.setHumidity(EnumHumidity.ARID);
    BASALT.setTemperature(EnumTemperature.HELLISH);
    
    TEMPERED.addProduct(ItemHoneyComb.VanillaComb.SIMMERING.get(), 25);
    TEMPERED.importTemplate(BASALT);
    TEMPERED.setEffect(ExtraBeesEffect.METEOR.getUID());
    TEMPERED.recessive();
    TEMPERED.setSecondaryColor(10101539);
    
    VOLCANIC.importTemplate(TEMPERED);
    VOLCANIC.addProduct(ItemHoneyComb.VanillaComb.SIMMERING.get(), 25);
    VOLCANIC.addSpecialty(EnumHoneyComb.BLAZE, 10);
    VOLCANIC.setHasEffect(true);
    VOLCANIC.setSecondaryColor(10101539);
    
    MALICIOUS.importTemplate(ForestryAllele.BeeSpecies.Tropical);
    MALICIOUS.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    MALICIOUS.setSecondaryColor(431972);
    MALICIOUS.setHumidity(EnumHumidity.DAMP);
    MALICIOUS.setTemperature(EnumTemperature.WARM);
    
    INFECTIOUS.importTemplate(MALICIOUS);
    INFECTIOUS.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    INFECTIOUS.setFlowering(ForestryAllele.Flowering.Slow);
    INFECTIOUS.setSecondaryColor(431972);
    
    VIRULENT.importTemplate(INFECTIOUS);
    VIRULENT.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    VIRULENT.addSpecialty(EnumHoneyComb.VENOMOUS, 12);
    VIRULENT.setFlowering(ForestryAllele.Flowering.Average);
    VIRULENT.recessive();
    VIRULENT.setHasEffect(true);
    VIRULENT.setSecondaryColor(431972);
    
    VISCOUS.importTemplate(ForestryAllele.BeeSpecies.Tropical);
    VISCOUS.setEffect(ExtraBeesEffect.ECTOPLASM.getUID());
    VISCOUS.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    VISCOUS.setSecondaryColor(431972);
    VISCOUS.setHumidity(EnumHumidity.DAMP);
    VISCOUS.setSpeed(ForestryAllele.Speed.Slow);
    VISCOUS.setTemperature(EnumTemperature.WARM);
    
    GLUTINOUS.importTemplate(VISCOUS);
    GLUTINOUS.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    GLUTINOUS.setSpeed(ForestryAllele.Speed.Norm);
    GLUTINOUS.setSecondaryColor(431972);
    
    STICKY.importTemplate(GLUTINOUS);
    STICKY.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    STICKY.addSpecialty(EnumHoneyComb.SLIME, 12);
    STICKY.setSpeed(ForestryAllele.Speed.Fast);
    STICKY.setHasEffect(true);
    STICKY.setSecondaryColor(431972);
    
    CORROSIVE.importTemplate(STICKY);
    CORROSIVE.setHumidity(EnumHumidity.DAMP);
    CORROSIVE.setTemperature(EnumTemperature.WARM);
    CORROSIVE.setEffect(ExtraBeesEffect.ACID.getUID());
    CORROSIVE.setFlowering(ForestryAllele.Flowering.Average);
    CORROSIVE.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 20);
    CORROSIVE.recessive();
    CORROSIVE.setSecondaryColor(431972);
    
    CAUSTIC.importTemplate(CORROSIVE);
    CAUSTIC.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 25);
    CAUSTIC.addSpecialty(EnumHoneyComb.ACIDIC, 3);
    CAUSTIC.setSecondaryColor(431972);
    
    ACIDIC.importTemplate(CAUSTIC);
    ACIDIC.addProduct(ItemHoneyComb.VanillaComb.SILKY.get(), 20);
    ACIDIC.addSpecialty(EnumHoneyComb.ACIDIC, 16);
    ACIDIC.setHasEffect(true);
    ACIDIC.setSecondaryColor(431972);
    
    EXCITED.setEffect(ExtraBeesEffect.LIGHTNING.getUID());
    EXCITED.addProduct(EnumHoneyComb.REDSTONE, 10);
    EXCITED.setCaveDwelling();
    EXCITED.setFlowerProvider(ExtraBeesFlowers.REDSTONE.getUID());
    
    ENERGETIC.importTemplate(EXCITED);
    ENERGETIC.setEffect(ExtraBeesEffect.LIGHTNING.getUID());
    ENERGETIC.addProduct(EnumHoneyComb.REDSTONE, 12);
    ENERGETIC.recessive();
    
    ECSTATIC.importTemplate(ENERGETIC);
    ECSTATIC.setEffect(ExtraBeesEffect.Power.getUID());
    ECSTATIC.addProduct(EnumHoneyComb.REDSTONE, 20);
    ECSTATIC.addSpecialty(EnumHoneyComb.IC2ENERGY, 8);
    ECSTATIC.setHasEffect(true);
    
    ARTIC.importTemplate(ForestryAllele.BeeSpecies.Wintry);
    ARTIC.addProduct(ItemHoneyComb.VanillaComb.FROZEN.get(), 25);
    ARTIC.setTemperature(EnumTemperature.ICY);
    ARTIC.setSecondaryColor(14349811);
    
    FREEZING.importTemplate(ARTIC);
    FREEZING.addProduct(ItemHoneyComb.VanillaComb.FROZEN.get(), 20);
    FREEZING.addSpecialty(EnumHoneyComb.GLACIAL, 10);
    FREEZING.setSecondaryColor(14349811);
    
    SHADOW.importTemplate(BASALT);
    SHADOW.setNocturnal();
    SHADOW.addProduct(EnumHoneyComb.SHADOW, 5);
    SHADOW.setEffect(ExtraBeesEffect.BLINDNESS.getUID());
    SHADOW.setAllDay(false);
    SHADOW.recessive();
    SHADOW.setSecondaryColor(3355443);
    
    DARKENED.addProduct(EnumHoneyComb.SHADOW, 10);
    DARKENED.setNocturnal();
    DARKENED.importTemplate(SHADOW);
    DARKENED.setSecondaryColor(3355443);
    
    ABYSS.importTemplate(DARKENED);
    ABYSS.setNocturnal();
    ABYSS.addProduct(EnumHoneyComb.SHADOW, 25);
    ABYSS.setEffect(ExtraBeesEffect.WITHER.getUID());
    ABYSS.setHasEffect(true);
    ABYSS.setSecondaryColor(3355443);
    
    CELEBRATORY.importTemplate(ForestryAllele.BeeSpecies.Merry);
    CELEBRATORY.setEffect(ExtraBeesEffect.FIREWORKS.getUID());
    
    GLOWSTONE.importTemplate(BASALT);
    GLOWSTONE.addProduct(EnumHoneyComb.GLOWSTONE, 15);
    GLOWSTONE.setSecondaryColor(10101539);
    
    HAZARDOUS.importTemplate(ForestryAllele.BeeSpecies.Austere);
    HAZARDOUS.addProduct(EnumHoneyComb.SALTPETER, 12);
    
    JADED.importTemplate(ForestryAllele.BeeSpecies.Imperial);
    JADED.setFertility(ForestryAllele.Fertility.Maximum);
    JADED.setFlowering(ForestryAllele.Flowering.Maximum);
    JADED.setTerritory(ForestryAllele.Territory.Largest);
    JADED.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 30);
    JADED.addSpecialty(Mods.Forestry.stack("pollen"), 20);
    JADED.setHasEffect(true);
    JADED.setSecondaryColor(14453483);
    
    UNUSUAL.importTemplate(ForestryAllele.BeeSpecies.Ended);
    UNUSUAL.setEffect(ExtraBeesEffect.GRAVITY.getUID());
    UNUSUAL.setSecondaryColor(12231403);
    UNUSUAL.addProduct(ItemHoneyComb.VanillaComb.QUARTZ.get(), 25);
    
    SPATIAL.importTemplate(UNUSUAL);
    SPATIAL.setEffect(ExtraBeesEffect.GRAVITY.getUID());
    SPATIAL.setSecondaryColor(10768076);
    SPATIAL.addProduct(ItemHoneyComb.VanillaComb.QUARTZ.get(), 25);
    SPATIAL.addSpecialty(EnumHoneyComb.CERTUS, 5);
    
    QUANTUM.importTemplate(QUANTUM);
    QUANTUM.setEffect(ExtraBeesEffect.TELEPORT.getUID());
    QUANTUM.setSecondaryColor(13963227);
    QUANTUM.addProduct(ItemHoneyComb.VanillaComb.QUARTZ.get(), 25);
    QUANTUM.addSpecialty(EnumHoneyComb.CERTUS, 15);
    QUANTUM.addSpecialty(EnumHoneyComb.ENDERPEARL, 15);
    
    JADED.addSpecialty(EnumHoneyComb.PURPLE, 15);
    JADED.isCounted = false;
    
    YELLORIUM.importTemplate(NUCLEAR);
    YELLORIUM.addProduct(EnumHoneyComb.BARREN, 20);
    YELLORIUM.addSpecialty(EnumHoneyComb.YELLORIUM, 2);
    YELLORIUM.setEffect(ExtraBeesEffect.RADIOACTIVE.getUID());
    YELLORIUM.setFertility(ForestryAllele.Fertility.Low);
    YELLORIUM.setLifespan(ForestryAllele.Lifespan.Shortest);
    
    CYANITE.importTemplate(YELLORIUM);
    CYANITE.addProduct(EnumHoneyComb.BARREN, 20);
    CYANITE.addSpecialty(EnumHoneyComb.CYANITE, 1);
    
    BLUTONIUM.importTemplate(CYANITE);
    BLUTONIUM.addProduct(EnumHoneyComb.BARREN, 20);
    BLUTONIUM.addSpecialty(EnumHoneyComb.BLUTONIUM, 1);
    
    MYSTICAL.importTemplate(ForestryAllele.BeeSpecies.Noble);
    for (Map.Entry<ItemStack, Integer> entry : ForestryAllele.BeeSpecies.Noble.getAllele().getProducts().entrySet()) {
      MYSTICAL.addProduct((ItemStack)entry.getKey(), ((Integer)entry.getValue()).intValue());
    }
    MYSTICAL.setFlowerProvider(ExtraBeesFlowers.Mystical.getUID());
    for (ExtraBeesSpecies species : values())
    {
      if (species.state != State.Active) {
        AlleleManager.alleleRegistry.blacklistAllele(species.getUID());
      }
      for (EnumBeeChromosome chromo : EnumBeeChromosome.values()) {
        if (chromo != EnumBeeChromosome.HUMIDITY)
        {
          IAllele allele = species.template[chromo.ordinal()];
          if ((allele == null) || (!chromo.getAlleleClass().isInstance(allele))) {
            throw new RuntimeException(species.getName() + " has an invalid " + chromo.toString() + " chromosome!");
          }
        }
      }
    }
    for (int i = 0; i < 16; i++)
    {
      ExtraBeesSpecies species = values()[(RED.ordinal() + i)];
      EnumHoneyComb comb = EnumHoneyComb.values()[(EnumHoneyComb.RED.ordinal() + i)];
      species.addProduct(ItemHoneyComb.VanillaComb.HONEY.get(), 75);
      species.addSpecialty(comb, 25);
      species.setSecondaryColor(9240320);
    }
    for (ExtraBeesSpecies species : values()) {
      species.registerTemplate();
    }
  }
  
  void setBranch(IClassification branch)
  {
    this.branch = branch;
  }
  
  public boolean isJubilant(World world, int biomeid, int x, int y, int z)
  {
    return true;
  }
  
  public boolean isJubilant(IBeeGenome genome, IBeeHousing housing)
  {
    return true;
  }
  
  public int getIconColour(int renderPass)
  {
    return renderPass == 1 ? this.secondaryColor : renderPass == 0 ? this.primaryColor : 16777215;
  }
  
  public IIconProvider getIconProvider()
  {
    return this;
  }
  
  public IIcon getIcon(short texUID)
  {
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    String iconType = "default";
    String mod = "forestry";
    if (this == JADED)
    {
      iconType = "jaded";
      mod = "extrabees";
    }
    this.icons = new IIcon[EnumBeeType.values().length][3];
    
    IIcon body1 = BinnieCore.proxy.getIcon(register, mod, "bees/" + iconType + "/body1");
    for (int i = 0; i < EnumBeeType.values().length; i++) {
      if (EnumBeeType.values()[i] != EnumBeeType.NONE)
      {
        this.icons[i][0] = BinnieCore.proxy.getIcon(register, mod, "bees/" + iconType + "/" + EnumBeeType.values()[i].toString().toLowerCase(Locale.ENGLISH) + ".outline");
        

        this.icons[i][1] = (EnumBeeType.values()[i] != EnumBeeType.LARVAE ? body1 : BinnieCore.proxy.getIcon(register, mod, "bees/" + iconType + "/" + EnumBeeType.values()[i].toString().toLowerCase(Locale.ENGLISH) + ".body"));
        
        this.icons[i][2] = BinnieCore.proxy.getIcon(register, mod, "bees/" + iconType + "/" + EnumBeeType.values()[i].toString().toLowerCase(Locale.ENGLISH) + ".body2");
      }
    }
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(EnumBeeType type, int renderPass)
  {
    if (this.icons == null) {
      return ARID.getIcon(type, renderPass);
    }
    return this.icons[type.ordinal()][renderPass];
  }
  
  public IBeeRoot getRoot()
  {
    return Binnie.Genetics.getBeeRoot();
  }
  
  boolean nocturnal = false;
  
  public boolean isNocturnal()
  {
    return this.nocturnal;
  }
  
  public void setNocturnal()
  {
    this.nocturnal = true;
  }
  
  public void setAllDay()
  {
    setAllDay(true);
  }
  
  public void setAllDay(boolean allDay)
  {
    if (allDay) {
      this.template[EnumBeeChromosome.NOCTURNAL.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolTrue");
    } else {
      this.template[EnumBeeChromosome.NOCTURNAL.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolFalse");
    }
  }
  
  public void setCaveDwelling()
  {
    this.template[EnumBeeChromosome.CAVE_DWELLING.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolTrue");
  }
  
  public void setTolerantFlyer()
  {
    this.template[EnumBeeChromosome.TOLERANT_FLYER.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolTrue");
  }
  
  public void setFlowerProvider(String uid)
  {
    IAllele allele = AlleleManager.alleleRegistry.getAllele(uid);
    if ((allele instanceof IAlleleFlowers)) {
      this.template[EnumBeeChromosome.FLOWER_PROVIDER.ordinal()] = allele;
    }
  }
  
  public void setEffect(String uid)
  {
    IAllele allele = AlleleManager.alleleRegistry.getAllele(uid);
    if ((allele instanceof IAlleleBeeEffect)) {
      this.template[EnumBeeChromosome.EFFECT.ordinal()] = AlleleManager.alleleRegistry.getAllele(uid);
    }
  }
  
  private void setFertility(ForestryAllele.Fertility fert)
  {
    this.template[EnumBeeChromosome.FERTILITY.ordinal()] = fert.getAllele();
  }
  
  private void setLifespan(ForestryAllele.Lifespan fert)
  {
    this.template[EnumBeeChromosome.LIFESPAN.ordinal()] = fert.getAllele();
  }
  
  private void setSpeed(ForestryAllele.Speed fert)
  {
    this.template[EnumBeeChromosome.SPEED.ordinal()] = fert.getAllele();
  }
  
  private void setTerritory(ForestryAllele.Territory fert)
  {
    this.template[EnumBeeChromosome.TERRITORY.ordinal()] = fert.getAllele();
  }
  
  private void setFlowering(ForestryAllele.Flowering fert)
  {
    this.template[EnumBeeChromosome.FLOWERING.ordinal()] = fert.getAllele();
  }
  
  private void setHumidityTolerance(Tolerance fert)
  {
    this.template[EnumBeeChromosome.HUMIDITY_TOLERANCE.ordinal()] = fert.getAllele();
  }
  
  private void setTemperatureTolerance(Tolerance both1)
  {
    this.template[EnumBeeChromosome.TEMPERATURE_TOLERANCE.ordinal()] = both1.getAllele();
  }
  
  public float getResearchSuitability(ItemStack itemstack)
  {
    if (itemstack == null) {
      return 0.0F;
    }
    for (ItemStack stack : this.products.keySet()) {
      if (stack.isItemEqual(itemstack)) {
        return 1.0F;
      }
    }
    for (ItemStack stack : this.specialties.keySet()) {
      if (stack.isItemEqual(itemstack)) {
        return 1.0F;
      }
    }
    if (itemstack.getItem() == Items.glass_bottle) {
      return 0.9F;
    }
    if (itemstack.getItem() == Mods.Forestry.item("honeyDrop")) {
      return 0.5F;
    }
    if (itemstack.getItem() == Mods.Forestry.item("honeydew")) {
      return 0.7F;
    }
    if (itemstack.getItem() == Mods.Forestry.item("beeComb")) {
      return 0.4F;
    }
    if (AlleleManager.alleleRegistry.isIndividual(itemstack)) {
      return 1.0F;
    }
    for (Map.Entry<ItemStack, Float> entry : getRoot().getResearchCatalysts().entrySet()) {
      if (((ItemStack)entry.getKey()).isItemEqual(itemstack)) {
        return ((Float)entry.getValue()).floatValue();
      }
    }
    return 0.0F;
  }
  
  public ItemStack[] getResearchBounty(World world, GameProfile researcher, IIndividual individual, int bountyLevel)
  {
    ArrayList<ItemStack> bounty = new ArrayList();
    ItemStack research = null;
    if (world.rand.nextFloat() < 10.0F / bountyLevel)
    {
      Collection<? extends IMutation> combinations = getRoot().getCombinations(this);
      if (combinations.size() > 0)
      {
        IMutation[] candidates = (IMutation[])combinations.toArray(new IMutation[0]);
        research = AlleleManager.alleleRegistry.getMutationNoteStack(researcher, candidates[world.rand.nextInt(candidates.length)]);
      }
    }
    if (research != null) {
      bounty.add(research);
    }
    if (bountyLevel > 10) {
      for (ItemStack stack : this.specialties.keySet())
      {
        ItemStack stack2 = stack.copy();
        stack2.stackSize = (world.rand.nextInt((int)(bountyLevel / 2.0F)) + 1);
        bounty.add(stack2);
      }
    }
    for (ItemStack stack : this.products.keySet())
    {
      ItemStack stack2 = stack.copy();
      stack2.stackSize = (world.rand.nextInt((int)(bountyLevel / 2.0F)) + 1);
      bounty.add(stack2);
    }
    return (ItemStack[])bounty.toArray(new ItemStack[0]);
  }
  
  public String getEntityTexture()
  {
    return "/gfx/forestry/entities/bees/honeyBee.png";
  }
  
  public int getComplexity()
  {
    return 1 + getGeneticAdvancement(this, new ArrayList());
  }
  
  private int getGeneticAdvancement(IAllele species, ArrayList<IAllele> exclude)
  {
    int own = 1;
    int highest = 0;
    exclude.add(species);
    for (IMutation mutation : getRoot().getPaths(species, EnumBeeChromosome.SPECIES))
    {
      if (!exclude.contains(mutation.getAllele0()))
      {
        int otherAdvance = getGeneticAdvancement(mutation.getAllele0(), exclude);
        if (otherAdvance > highest) {
          highest = otherAdvance;
        }
      }
      if (!exclude.contains(mutation.getAllele1()))
      {
        int otherAdvance = getGeneticAdvancement(mutation.getAllele1(), exclude);
        if (otherAdvance > highest) {
          highest = otherAdvance;
        }
      }
    }
    return own + (highest < 0 ? 0 : highest);
  }
  
  public String getUnlocalizedName()
  {
    return getUID();
  }
  
  public Map<ItemStack, Float> getProductChances()
  {
    return null;
  }
  
  public Map<ItemStack, Float> getSpecialtyChances()
  {
    return null;
  }
}
