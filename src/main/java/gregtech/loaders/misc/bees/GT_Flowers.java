package gregtech.loaders.misc.bees;

import forestry.api.apiculture.FlowerManager;
import forestry.api.genetics.*;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import java.util.*;

public enum GT_Flowers implements IFlowerProvider, IAlleleFlowers, IChromosomeType {
    FLAMING;

    protected boolean dominant;

    GT_Flowers() { dominant = true; }

    public static void doInit() {
        for (GT_Flowers effect : values()) {
            effect.register();
        }
    }

    @Override
    public String getUID() {
        return "for.flowers." + toString().toLowerCase();
    }

    @Override
    public boolean isDominant() {
        return dominant;
    }

    @Override
    public IFlowerProvider getProvider() {
        return this;
    }

    @Override
    public String getDescription() {
        return GT_LanguageManager.getTranslation("for.flowers." + name().toLowerCase());
    }

    public void register() {
        for (ItemStack stack : getItemStacks()) {
            FlowerManager.flowerRegistry.registerAcceptableFlower(Block.getBlockFromItem(stack.getItem()), getUID());
        }

        AlleleManager.alleleRegistry.registerAllele(this, this);
    }

    public ItemStack[] getItemStacks() {
        switch (this) {
            case FLAMING:
                return new ItemStack[] { new ItemStack(Blocks.fire) };
        }
        return new ItemStack[0];
    }

    @Override
    public boolean isAcceptedPollinatable(World world, IPollinatable pollinatable) {
        EnumSet<EnumPlantType> types = pollinatable.getPlantType();
        return types.size() > 1 || !types.contains(EnumPlantType.Nether);
    }

    public boolean isAcceptedFlower(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block == null) {
            return false;
        }

        switch (this) {
            case FLAMING:
                return block == Blocks.fire;
        }
        return false;
    }

    @Override
    public boolean growFlower(World world, IIndividual individual, int x, int y, int z) {
        return false;
    }

    @Override
    public ItemStack[] affectProducts(World world, IIndividual individual, int x, int y, int z, ItemStack[] products) {
        return products;
    }

    @Override
    public String getName() {
        return getDescription();
    }

    @Override
    public String getUnlocalizedName() {
        return getUID();
    }

    @Override
    public String getFlowerType() {
        return getUID();
    }

    @Override
    public Set<IFlower> getFlowers() {
        return new HashSet<>();
    }

    @Override
    public Class<? extends IAllele> getAlleleClass() {
        return getClass();
    }

    @Override
    public ISpeciesRoot getSpeciesRoot() {
        return AlleleManager.alleleRegistry.getSpeciesRoot(getUID());
    }
}
