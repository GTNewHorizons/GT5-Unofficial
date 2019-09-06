package gregtech.common;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_UO_Dimension;
import gregtech.api.objects.GT_UO_Fluid;
import gregtech.api.objects.XSTR;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.common.GT_Proxy.*;

/**
 * Created by Tec on 29.04.2017.
 */
public class GT_UndergroundOil {
    public static final short DIVIDER=5000;

    public static FluidStack undergroundOilReadInformation(IGregTechTileEntity te){
        return undergroundOil(te.getWorld().getChunkFromBlockCoords(te.getXCoord(),te.getZCoord()),-1);
    }

    public static FluidStack undergroundOilReadInformation(Chunk chunk) {
        return undergroundOil(chunk,-1);
    }

    public static FluidStack undergroundOil(IGregTechTileEntity te, float readOrDrainCoefficient){
        return undergroundOil(te.getWorld().getChunkFromBlockCoords(te.getXCoord(),te.getZCoord()),readOrDrainCoefficient);
    }

    //Returns whole content for information purposes -> when drainSpeedCoefficient < 0
    //Else returns extracted fluidStack if amount > 0, or null otherwise
    public static FluidStack undergroundOil(Chunk chunk, float readOrDrainCoefficient) {
        World aWorld = chunk.worldObj;
        int dimensionId=aWorld.provider.dimensionId;
        GT_UO_Dimension dimension=GT_Mod.gregtechproxy.mUndergroundOil.GetDimension(dimensionId);
        if(dimension==null) return null;

        //Read hash map
        HashMap<ChunkCoordIntPair, int[]> chunkData = dimensionWiseChunkData.get(dimensionId);
        if(chunkData==null){
            chunkData=new HashMap<>(1024);
            dimensionWiseChunkData.put(dimensionId,chunkData);
        }

        int[] tInts = chunkData.get(chunk.getChunkCoordIntPair());

        if(tInts==null) tInts=getDefaultChunkDataOnCreation();//init if null
        else if(tInts[GTOIL]==0){//FAST stop
            //can return 0 amount stack for info :D
            return readOrDrainCoefficient>=0 ? null : new FluidStack(FluidRegistry.getFluid(tInts[GTOILFLUID]),0);
        }

        //GEN IT TO GET OBJECT...
        final XSTR tRandom = new XSTR(aWorld.getSeed() + dimensionId * 2 +
                       (chunk.getChunkCoordIntPair().chunkXPos>>3) +
                8267 * (chunk.getChunkCoordIntPair().chunkZPos>>3));

        GT_UO_Fluid uoFluid = dimension.getRandomFluid(tRandom);

        //Fluid stack holder
        FluidStack fluidInChunk;

        //Set fluid stack from uoFluid
        if (uoFluid == null || uoFluid.getFluid()==null){
            tInts[GTOILFLUID]=Integer.MAX_VALUE;//null fluid pointer... kind of
            tInts[GTOIL]=0;
            chunkData.put(chunk.getChunkCoordIntPair(),tInts);//update hash map
            return null;
        } else {
            if(tInts[GTOILFLUID]== uoFluid.getFluid().getID()){//if stored fluid matches uoFluid
                fluidInChunk = new FluidStack(uoFluid.getFluid(),tInts[GTOIL]);
            }else{
                fluidInChunk  = new FluidStack(uoFluid.getFluid(), uoFluid.getRandomAmount(tRandom));
                fluidInChunk.amount=(int)((float)fluidInChunk.amount*(0.75f+(XSTR_INSTANCE.nextFloat()/2f)));//Randomly change amounts by +/- 25%
            }
            tInts[GTOIL]=fluidInChunk.amount;
            tInts[GTOILFLUID]=fluidInChunk.getFluidID();
        }

        //do stuff on it if needed
        if(readOrDrainCoefficient>=0){
            int fluidExtracted=(int)Math.floor(fluidInChunk.amount * (double) readOrDrainCoefficient / DIVIDER);
            double averageDecrease=uoFluid.DecreasePerOperationAmount * (double)readOrDrainCoefficient;
            int decrease=(int)Math.ceil(averageDecrease);
            if(fluidExtracted<=0 || fluidInChunk.amount<=decrease){//decrease - here it is max value of extraction for easy check
                fluidInChunk=null;
                tInts[GTOIL]=0;//so in next access it will stop way above
            }else{
                fluidInChunk.amount = fluidExtracted;//give appropriate amount
                if(XSTR_INSTANCE.nextFloat()<(decrease-averageDecrease)) decrease--;//use XSTR_INSTANCE to "subtract double from int"
                //ex.
                // averageDecrease=3.9
                // decrease= ceil from 3.9 = 4
                // decrease-averageDecrease=0.1 -> chance to subtract 1
                // if XSTR_INSTANCE is < chance then subtract 1
                tInts[GTOIL]-=decrease;//diminish amount, "randomly" adjusted to double value (averageDecrease)
            }
        }else{//just get info
            if(fluidInChunk.amount<=DIVIDER){
                fluidInChunk.amount=0;//return informative stack
                tInts[GTOIL]=0;//so in next access it will stop way above
            }else{
                fluidInChunk.amount=fluidInChunk.amount/DIVIDER;//give moderate extraction speed
            }
        }

        chunkData.put(chunk.getChunkCoordIntPair(),tInts);//update hash map
        return fluidInChunk;
    }
}
