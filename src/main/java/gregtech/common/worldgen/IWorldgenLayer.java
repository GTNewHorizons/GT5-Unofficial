package gregtech.common.worldgen;

import com.google.common.collect.ImmutableList;

import gregtech.api.interfaces.IMaterial;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.interfaces.IStoneType;

public interface IWorldgenLayer {
    
    public int getMinY();
    public int getMaxY();
    public int getWeight();
    public float getSize();
    public float getDensity();
    public boolean canGenerateIn(String dimName);
    public boolean canGenerateIn(IStoneType stoneType);
    public boolean canGenerateIn(IStoneCategory stoneType);
    public boolean isStoneSpecific();
    public boolean generatesBigOre();
    public boolean contains(IMaterial ore);
    public ImmutableList<IMaterial> getOres();
    public IMaterial getOre(float k);
    public String getName();
}
