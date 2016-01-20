package binnie.core.genetics;

import binnie.Binnie;
import binnie.genetics.api.IGene;
import forestry.api.core.INBTTagable;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.nbt.NBTTagCompound;

public class Gene
  implements INBTTagable, IGene
{
  private IAllele allele;
  private IChromosomeType chromosome;
  private ISpeciesRoot root;
  
  public ISpeciesRoot getSpeciesRoot()
  {
    return this.root;
  }
  
  public String toString()
  {
    return getAlleleName();
  }
  
  public Gene(IAllele allele, IChromosomeType chromosome, ISpeciesRoot root)
  {
    this.allele = allele;
    this.chromosome = chromosome;
    this.root = root;
  }
  
  public Gene(NBTTagCompound nbt)
  {
    readFromNBT(nbt);
  }
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    this.allele = AlleleManager.alleleRegistry.getAllele(nbt.getString("allele"));
    
    this.root = AlleleManager.alleleRegistry.getSpeciesRoot(nbt.getString("root"));
    
    int chromoID = nbt.getByte("chromo");
    if ((this.root != null) && (chromoID >= 0) && (chromoID < this.root.getKaryotype().length)) {
      this.chromosome = this.root.getKaryotype()[chromoID];
    }
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    nbt.setString("allele", this.allele.getUID());
    nbt.setString("root", this.root.getUID());
    nbt.setByte("chromo", (byte)this.chromosome.ordinal());
  }
  
  public boolean isCorrupted()
  {
    return (this.allele == null) || (this.chromosome == null) || (this.root == null);
  }
  
  public static Gene create(NBTTagCompound nbt)
  {
    Gene gene = new Gene(nbt);
    return gene.isCorrupted() ? null : gene;
  }
  
  public static Gene create(IAllele allele, IChromosomeType chromosome, ISpeciesRoot root)
  {
    Gene gene = new Gene(allele, chromosome, root);
    return gene.isCorrupted() ? null : gene;
  }
  
  public NBTTagCompound getNBTTagCompound()
  {
    NBTTagCompound nbt = new NBTTagCompound();
    writeToNBT(nbt);
    return nbt;
  }
  
  public String getName()
  {
    return Binnie.Genetics.getSystem(this.root).getAlleleName(this.chromosome, this.allele);
  }
  
  public BreedingSystem getSystem()
  {
    return Binnie.Genetics.getSystem(this.root);
  }
  
  public IChromosomeType getChromosome()
  {
    return this.chromosome;
  }
  
  public IAllele getAllele()
  {
    return this.allele;
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Gene)) {
      return false;
    }
    Gene g = (Gene)obj;
    return (this.allele == g.allele) && (this.chromosome.ordinal() == g.chromosome.ordinal()) && (this.root == g.root);
  }
  
  public String getAlleleName()
  {
    return getSystem().getAlleleName(this.chromosome, this.allele);
  }
  
  public String getChromosomeName()
  {
    return getSystem().getChromosomeName(this.chromosome);
  }
  
  public String getShortChromosomeName()
  {
    return getSystem().getChromosomeShortName(this.chromosome);
  }
}
