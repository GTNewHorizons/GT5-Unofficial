package gtPlusPlus.australia.gen.map;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.GTplusplus_Australia;

import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class MapGenExtendedVillage extends MapGenStructure
{
    /** A list of all the biomes villages can spawn in. */
    public static List<BiomeGenBase> villageSpawnBiomes = Arrays.asList(new BiomeGenBase[] {GTplusplus_Australia.Australian_Desert_Biome_3, GTplusplus_Australia.Australian_Plains_Biome, GTplusplus_Australia.Australian_Forest_Biome, GTplusplus_Australia.Australian_Outback_Biome});
    /** World terrain type, 0 for normal, 1 for flat map */
    private int terrainType;
    private int field_82665_g;
    private int field_82666_h;

    public MapGenExtendedVillage(){
        this.field_82665_g = 8;
        this.field_82666_h = 4;
    }

    public MapGenExtendedVillage(Map<?, ?> p_i2093_1_){
        this();
        Iterator<?> iterator = p_i2093_1_.entrySet().iterator();

        Logger.INFO("Created Extended Village Object.");
        
        while (iterator.hasNext())
        {
            Entry<?, ?> entry = (Entry<?, ?>)iterator.next();

            if (((String)entry.getKey()).equals("size"))
            {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.terrainType, 0);
            }
            else if (((String)entry.getKey()).equals("distance"))
            {
                this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
            }
        }
    }

    public String func_143025_a()
    {
        return "ExtendedVillage";
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
    {
    	
    	
        int k = p_75047_1_;
        int l = p_75047_2_;

        if (p_75047_1_ < 0)
        {
            p_75047_1_ -= this.field_82665_g - 1;
        }

        if (p_75047_2_ < 0)
        {
            p_75047_2_ -= this.field_82665_g - 1;
        }

        int i1 = p_75047_1_ / this.field_82665_g;
        int j1 = p_75047_2_ / this.field_82665_g;
        Random random = this.worldObj.setRandomSeed(i1, j1, 10387312);
        i1 *= this.field_82665_g;
        j1 *= this.field_82665_g;
        i1 += random.nextInt(this.field_82665_g - this.field_82666_h);
        j1 += random.nextInt(this.field_82665_g - this.field_82666_h);

        if (k == i1 && l == j1)
        {
            boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(k * 16 + 8, l * 16 + 8, 0, villageSpawnBiomes);

            if (flag)
            {
            	Logger.INFO("Found viable biome(s) for custom village");
                return true;
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        return new MapGenExtendedVillage.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_, this.terrainType);
    }

    public static class Start extends StructureStart
        {
            /** well ... thats what it does */
            private boolean hasMoreThanTwoComponents;

            public Start() {
            }

            public Start(World p_i2092_1_, Random p_i2092_2_, int p_i2092_3_, int p_i2092_4_, int p_i2092_5_)
            {
                super(p_i2092_3_, p_i2092_4_);
                Logger.INFO("Trying to Start Village Builder.");
                List<?> list = StructureVillagePieces.getStructureVillageWeightedPieceList(p_i2092_2_, p_i2092_5_);
                StructureVillagePieces.Start start = new StructureVillagePieces.Start(p_i2092_1_.getWorldChunkManager(), 0, p_i2092_2_, (p_i2092_3_ << 4) + 2, (p_i2092_4_ << 4) + 2, list, p_i2092_5_);
                this.components.add(start);
                start.buildComponent(start, this.components, p_i2092_2_);
                List<?> list1 = start.field_74930_j;
                List<?> list2 = start.field_74932_i;
                int l;
                Logger.INFO("List1: "+list1.size()+" | List2: "+list2.size());

                while (!list1.isEmpty() || !list2.isEmpty())
                {
                    Logger.INFO("Iterating non empty list.");
                    StructureComponent structurecomponent;

                    if (list1.isEmpty())
                    {
                        l = p_i2092_2_.nextInt(list2.size());
                        structurecomponent = (StructureComponent)list2.remove(l);
                        structurecomponent.buildComponent(start, this.components, p_i2092_2_);
                    }
                    else
                    {
                        l = p_i2092_2_.nextInt(list1.size());
                        structurecomponent = (StructureComponent)list1.remove(l);
                        structurecomponent.buildComponent(start, this.components, p_i2092_2_);
                    }
                }
                Logger.INFO("Finished iterating lists, updating bounding box for structure.");

                this.updateBoundingBox();
                l = 0;
                Iterator iterator = this.components.iterator();

                while (iterator.hasNext())
                {
                    Logger.INFO("Iterating Components.");
                    StructureComponent structurecomponent1 = (StructureComponent)iterator.next();

                    if (!(structurecomponent1 instanceof StructureVillagePieces.Road))
                    {
                        ++l;
                    }
                }

            	Logger.INFO("hasMoreThanTwoComponents? "+(l > 2));
                this.hasMoreThanTwoComponents = l > 2;
            }

            /**
             * currently only defined for Villages, returns true if Village has more than 2 non-road components
             */
            public boolean isSizeableStructure()
            {
                //return this.hasMoreThanTwoComponents;
            	return true;
            }

            public void func_143022_a(NBTTagCompound p_143022_1_)
            {
                super.func_143022_a(p_143022_1_);
                p_143022_1_.setBoolean("Valid", this.hasMoreThanTwoComponents);
            }

            public void func_143017_b(NBTTagCompound p_143017_1_)
            {
                super.func_143017_b(p_143017_1_);
                this.hasMoreThanTwoComponents = p_143017_1_.getBoolean("Valid");
            }
        }
    
    /**
     * Generates structures in specified chunk next to existing structures. Does *not* generate StructureStarts.
     */
    @Override
    public boolean generateStructuresInChunk(World p_75051_1_, Random p_75051_2_, int p_75051_3_, int p_75051_4_){
    	//Logger.INFO("Try generate Structs in chunk.");
        this.callPrivateFunction1(p_75051_1_);
        int k = (p_75051_3_ << 4) + 8;
        int l = (p_75051_4_ << 4) + 8;
        boolean flag = false;
        Iterator iterator = this.structureMap.values().iterator();
        
        //Logger.INFO("Iteration Size: "+this.structureMap.values().size());
        while (iterator.hasNext())
        {
        	//Logger.INFO("Iterating.");
            StructureStart structurestart = (StructureStart)iterator.next();

            if (structurestart.isSizeableStructure() && (structurestart.getBoundingBox().intersectsWith(k, l, k + 15, l + 15) || structurestart.getBoundingBox().intersectsWith(k, l, k - 15, l - 15)))
            {
            	Logger.INFO("Iterating. 2");
                structurestart.generateStructure(p_75051_1_, p_75051_2_, new StructureBoundingBox(k, l, k + 15, l + 15));
                flag = true;
                this.callPrivateFunction2(structurestart.func_143019_e(), structurestart.func_143018_f(), structurestart);
            }
           /* else {
            	Logger.INFO("Iterating. 3");
            	Logger.INFO("structurestart.isSizeableStructure()? "+structurestart.isSizeableStructure());
            	Logger.INFO("structurestart.getBoundingBox().intersectsWith(k, l, k + 15, l + 15)? "+(structurestart.getBoundingBox().intersectsWith(k, l, k + 15, l + 15) || structurestart.getBoundingBox().intersectsWith(k, l, k - 15, l - 15)));    
            	Logger.INFO("K: "+k+" | L: "+l);
            	Logger.INFO("structure bounding box info: x-:"+structurestart.getBoundingBox().minX+"   y-:"+structurestart.getBoundingBox().minY+"   x+:"+structurestart.getBoundingBox().maxX+"   y+:"+structurestart.getBoundingBox().maxY);
            }*/
        }

        return flag;
    }
    
    Method mMethod1;
    Method mMethod2;
    private boolean callPrivateFunction1(World aWorld) {
    	if (mMethod1 == null) {
    		try {
				mMethod1 = MapGenStructure.class.getDeclaredMethod("func_143027_a", World.class);
			} catch (NoSuchMethodException | SecurityException e) {
				return false;
			}
    	}
    	if (mMethod1 != null) {
    		try {
    			//Logger.INFO("Invoking func_143027_a");
				mMethod1.invoke(this, aWorld);
				return true;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
    	}
    	return false;
    }
    
    private boolean callPrivateFunction2(int aInt1, int aInt2, StructureStart aStruct) {
    	if (mMethod2 == null) {
    		try {
    			mMethod2 = MapGenStructure.class.getDeclaredMethod("func_143026_a", int.class, int.class, StructureStart.class);
			} catch (NoSuchMethodException | SecurityException e) {
				return false;
			}
    	}
    	if (mMethod2 != null) {
    		try {
    			Logger.INFO("Invoking func_143026_a");
    			mMethod2.invoke(this, aInt1, aInt2, aStruct);
				return true;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
    	}
    	return false;
    }
}