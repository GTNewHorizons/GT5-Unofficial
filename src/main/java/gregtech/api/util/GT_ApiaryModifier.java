package gregtech.api.util;

import net.minecraft.world.biome.BiomeGenBase;

public class GT_ApiaryModifier {
    public float territory = 1.0F;
    public float mutation = 1.0F;
    public float lifespan = 1.0F;
    public float production = 1.0F;
    public float flowering = 1.0F;
    public float geneticDecay = 1.0F;
    public boolean isSealed = false;
    public boolean isSelfLighted = false;
    public boolean isSunlightSimulated = false;
    public boolean isAutomated = false;
    public boolean isCollectingPollen = false;
    public BiomeGenBase biomeOverride = null;
    public float energy = 1.0F;
    public float temperature = 0.0F;
    public float humidity = 0.0F;
    public int maxSpeed = 0;
}
