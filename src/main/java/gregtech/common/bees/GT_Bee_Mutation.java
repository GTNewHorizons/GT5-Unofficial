package gregtech.common.bees;

import forestry.api.apiculture.*;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutationCondition;
import forestry.apiculture.genetics.BeeMutation;
import forestry.core.genetics.mutations.Mutation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.List;


public class GT_Bee_Mutation extends BeeMutation {

    private int split = 1;

    public GT_Bee_Mutation(IAlleleBeeSpecies bee0, IAlleleBeeSpecies bee1, IAllele[] result, int chance, int split) {
        super(bee0, bee1, result, chance);
        this.split = split;
        BeeManager.beeRoot.registerMutation(this);
    }

    @Override
    public float getBaseChance() {
        return ((float) ((float)super.getBaseChance() / ((float)split)));
    }

    @Override
    public float getChance(IBeeHousing housing, IAlleleBeeSpecies allele0, IAlleleBeeSpecies allele1, IBeeGenome genome0, IBeeGenome genome1) {
        World world = housing.getWorld();
        ChunkCoordinates housingCoordinates = housing.getCoordinates();
        int x = housingCoordinates.posX;
        int y = housingCoordinates.posY;
        int z = housingCoordinates.posZ;

        float processedChance = getBasicChance(world, x, y, z, allele0, allele1, genome0, genome1);


        if (processedChance <= 0f) {
            return 0f;
        }

        IBeeModifier beeHousingModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
        IBeeModifier beeModeModifier = BeeManager.beeRoot.getBeekeepingMode(world).getBeeModifier();

        processedChance *= beeHousingModifier.getMutationModifier(genome0, genome1, processedChance);
        processedChance *= beeModeModifier.getMutationModifier(genome0, genome1, processedChance);

        return processedChance;
    }

    protected float getBasicChance(World world, int x, int y, int z, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1) {
        float mutationChance = this.getBaseChance();
        List<IMutationCondition> mutationConditions = null;
        try {
            Field f = Mutation.class.getDeclaredField("mutationConditions");
            f.setAccessible(true);
            Object o = f.get(this);
            mutationConditions = o instanceof List ? (List) o : null ;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (mutationConditions != null)
        for (IMutationCondition mutationCondition : mutationConditions) {
            mutationChance *= mutationCondition.getChance(world, x, y, z, allele0, allele1, genome0, genome1);
            if (mutationChance == 0) {
                return 0;
            }
        }
        return mutationChance;
    }
}
