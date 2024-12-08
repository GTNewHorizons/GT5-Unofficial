package gregtech.api.interfaces;

import com.google.common.collect.ImmutableList;

public interface IMaterial {
    
    public String getLocalizedName();

    public int getId();

    public String getInternalName();

    public short[] getRGBA();

    public boolean isValidForStone(IStoneType stoneType);

    public ImmutableList<IStoneType> getValidStones();
}
