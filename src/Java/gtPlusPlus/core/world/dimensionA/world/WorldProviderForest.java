package gtPlusPlus.core.world.dimensionA.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.world.DimensionIDs;
import gtPlusPlus.core.world.dimensionA.world.renderers.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.DimensionManager;

public class WorldProviderForest extends WorldProvider{
	
	@Override
	/** tells Minecraft to use our new Terrain Generator */
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderForest(this.worldObj, this.worldObj.getSeed(), true);
	}

	@Override
	/** tells Minecraft to use our new WorldChunkManager **/
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerForest(worldObj.getSeed(), terrainType);
		this.dimensionId = DimensionIDs.Dimension_A;
	}
	
	/** Get Provider for Dimension **/
	public static WorldProvider getProviderForDimension(int id)
	{
		return DimensionManager.createProviderFor(DimensionIDs.Dimension_A);
	}

	@Override
	/**
	 * @return the name of the dimension
	 */
	public String getDimensionName() {
		return "Light Forest";
	}

	@Override
	/** sets/creates the save folder */
	public String getSaveFolder() {
			return "DIM" + DimensionIDs.Dimension_A;
	}

	@SideOnly(Side.CLIENT)
	/** should stars be rendered? */
	public boolean renderStars() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/** @return the player speed */
	public double getMovementFactor() {
		return 0.1;
	}

	@SideOnly(Side.CLIENT)
	/** @return the light value of the stars*/
	public float getStarBrightness(World world, float f) {
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	/** should clouds be rendered? */
	public boolean renderClouds() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean renderVoidFog() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	/** should the end sky be rendered or the overworld sky? */
	public boolean renderEndSky() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	/** @return the size of the sun */
	public float setSunSize() {
		return 0.5F;
	}

	@SideOnly(Side.CLIENT)
	/** @return the size of the moon */
	public float setMoonSize() {
		return 4.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * @return the sky color
	 */
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		return worldObj.getSkyColorBody(cameraEntity, partialTicks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/** should a color for the sky be rendered? */
	public boolean isSkyColored()
	{
		return true;
	}

	@Override
	/** can the player respawn in this dimension? */
	public boolean canRespawnHere()
	{
		return true;
	}

	@Override
	/** is this a surface world or an underworld */
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/** @return the high of the clouds */
	public float getCloudHeight()
	{
		return this.terrainType.getCloudHeight();
	}

	@Override
	public ChunkCoordinates getEntrancePortalLocation()
	{
		return new ChunkCoordinates(50, 5, 0);
	}

	@Override
	/** the light value in this dimension */
	protected void generateLightBrightnessTable()
	{
		float f = 0.0F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
	}

	@Override
	@SideOnly(Side.CLIENT)
	/** @return the dimension join message */
	public String getWelcomeMessage()
	{
		return "Entering the Forest";
	}

	@Override
	@SideOnly(Side.CLIENT)
	/** @return the dimension leave message */
	public String getDepartMessage()
	{
		return "Leaving the Forest";
	}

	@Override
	public IRenderHandler getSkyRenderer() {
		return new SkyRenderer();
	}

	@Override
	public IRenderHandler getCloudRenderer() {
		return new CloudRenderer();
	}

	@Override
	public IRenderHandler getWeatherRenderer() {
		return new WeatherRenderer();
	}

	@Override
	public Vec3 drawClouds(float partialTicks) {
		// TODO Auto-generated method stub
		return super.drawClouds(partialTicks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1, float par2)
	{
		 float f2 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

	        if (f2 < 0.0F)
	        {
	            f2 = 0.0F;
	        }

	        if (f2 > 1.0F)
	        {
	            f2 = 1.0F;
	        }

	        float f3 = 0.7529412F;
	        float f4 = 0.84705883F;
	        float f5 = 1.0F;
	        f3 *= f2 * 0.94F + 0.06F;
	        f4 *= f2 * 0.94F + 0.06F;
	        f5 *= f2 * 0.91F + 0.09F;
	        return Vec3.createVectorHelper(f3, f4, f5);
	}
}
