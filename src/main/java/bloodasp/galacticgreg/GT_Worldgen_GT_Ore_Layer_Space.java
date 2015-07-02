package bloodasp.galacticgreg;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.auxiliary.GTOreGroup;
import bloodasp.galacticgreg.dynconfig.DynamicOreMixWorldConfig;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;

public class GT_Worldgen_GT_Ore_Layer_Space extends GT_Worldgen {
	public static int sWeight = 0;
	public final short mMinY;
	public final short mMaxY;
	public final short mWeight;
	public final short mDensity;
	public final short mSize;
	public final short mPrimaryMeta;
	public final short mSecondaryMeta;
	public final short mBetweenMeta;
	public final short mSporadicMeta;
	
	private long mProfilingStart;
	private long mProfilingEnd;
	
	private DynamicOreMixWorldConfig _mDynWorldConfig = null;
	
	public GT_Worldgen_GT_Ore_Layer_Space(String pName, boolean pDefault, int pMinY, int pMaxY, int pWeight, int pDensity, int pSize, Materials pPrimary, Materials pSecondary, Materials pBetween, Materials pSporadic)
	{
		super(pName, GalacticGreg.oreVeinWorldgenList, pDefault);
		mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", pMinY));
		mMaxY = ((short) Math.max(this.mMinY + 5, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", pMaxY)));
		mWeight = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RandomWeight", pWeight));
		mDensity = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Density", pDensity));
		mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Size", pSize)));
		mPrimaryMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OrePrimaryLayer", pPrimary.mMetaItemSubID));
		mSecondaryMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSecondaryLayer", pSecondary.mMetaItemSubID));
		mBetweenMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSporadiclyInbetween", pBetween.mMetaItemSubID));
		mSporadicMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSporaticlyAround", pSporadic.mMetaItemSubID));
	
		_mDynWorldConfig = new DynamicOreMixWorldConfig(mWorldGenName);
		_mDynWorldConfig.InitDynamicConfig();
	
		GalacticGreg.Logger.trace("Initialized new OreLayer: %s", pName);
		
		if (mEnabled)
			sWeight += this.mWeight;

	}

	/**
	 * Check if *this* orelayer is enabled for pDimensionDef
	 * @param pDimensionDef the ChunkProvider in question
	 * @return
	 */
	public boolean isEnabledForDim(ModDimensionDef pDimensionDef)
	{
		return _mDynWorldConfig.isEnabledInDim(pDimensionDef);
	}
	
	/**
	 * Select a random ore-vein from the list
	 * 
	 * @param pDimensionDef
	 * @param pRandom
	 * @return
	 */
	public static GTOreGroup getRandomOreGroup(ModDimensionDef pDimensionDef, Random pRandom)
	{
		short primaryMeta = 0;
		short secondaryMeta = 0;
		short betweenMeta = 0;
		short sporadicMeta = 0;
		if ((GT_Worldgen_GT_Ore_Layer_Space.sWeight > 0) && (GalacticGreg.oreVeinWorldgenList.size() > 0))
		{
			GalacticGreg.Logger.trace("About to select oremix");
			boolean temp = true;
			int tRandomWeight;
			for (int i = 0; (i < 256) && (temp); i++)
			{
				tRandomWeight = pRandom.nextInt(GT_Worldgen_GT_Ore_Layer_Space.sWeight);
				for (GT_Worldgen_GT_Ore_Layer_Space tWorldGen : GalacticGreg.oreVeinWorldgenList)
				{
					tRandomWeight -= ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight;
					if (tRandomWeight <= 0)
					{
						
						try
						{
							if (tWorldGen.isEnabledForDim(pDimensionDef))
							{
								GalacticGreg.Logger.trace("Using Oremix %s for asteroid", tWorldGen.mWorldGenName);
								primaryMeta = tWorldGen.mPrimaryMeta;
								secondaryMeta = tWorldGen.mSecondaryMeta;
								betweenMeta = tWorldGen.mBetweenMeta;
								sporadicMeta = tWorldGen.mSporadicMeta;
								
								temp = false;
								break;
							}
						} catch (Throwable e) {
							e.printStackTrace(GT_Log.err);
						}
					}
				}
			}
		}
		if (primaryMeta != 0 || secondaryMeta != 0 || betweenMeta != 0 || sporadicMeta != 0)
			return new GTOreGroup(primaryMeta, secondaryMeta, betweenMeta, sporadicMeta);
		else
			return null;
	}
	
	@Override
	public boolean executeWorldgen(World pWorld, Random pRandom, String pBiome, int pDimensionType, int pChunkX, int pChunkZ, IChunkProvider pChunkGenerator, IChunkProvider pChunkProvider)
	{
		GalacticGreg.Logger.trace("Entering executeWorldgen for [%s]", mWorldGenName);
		ModDimensionDef tMDD = GalacticGregRegistry.getDimensionTypeByChunkGenerator(pChunkProvider);
		if (tMDD == null)
		{
			GalacticGreg.Logger.trace("Can't find dimension definition for ChunkProvider %s, skipping", pChunkProvider.toString());
			return false;
		}			
		
		if (!_mDynWorldConfig.isEnabledInDim(tMDD))
		{
			GalacticGreg.Logger.trace("OreGen for %s is disallowed in dimension %s, skipping", mWorldGenName, tMDD.getDimensionName());
			return false;
		}

		if (GalacticGreg.GalacticConfig.ProfileOreGen)
			mProfilingStart = System.currentTimeMillis();
		// ---------------------------
		int tMinY = this.mMinY + pRandom.nextInt(this.mMaxY - this.mMinY - 5);

		int cX = pChunkX - pRandom.nextInt(this.mSize);
		int eX = pChunkX + 16 + pRandom.nextInt(this.mSize);
		for (int tX = cX; tX <= eX; tX++) {
			int cZ = pChunkZ - pRandom.nextInt(this.mSize);
			int eZ = pChunkZ + 16 + pRandom.nextInt(this.mSize);
			for (int tZ = cZ; tZ <= eZ; tZ++) {
				if (this.mSecondaryMeta > 0) {
					for (int i = tMinY - 1; i < tMinY + 2; i++) {
						if ((pRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0)
								|| (pRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0)) {
							GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mSecondaryMeta);
						}
					}
				}
				if ((this.mBetweenMeta > 0)
						&& ((pRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (pRandom.nextInt(Math.max(1,
								Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0))) {
					GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, tMinY + 2 + pRandom.nextInt(2), tZ, this.mBetweenMeta);
				}
				if (this.mPrimaryMeta > 0) {
					for (int i = tMinY + 3; i < tMinY + 6; i++) {
						if ((pRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0)
								|| (pRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0)) {

							GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mPrimaryMeta);
						}
					}
				}
				if ((this.mSporadicMeta > 0)
						&& ((pRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (pRandom.nextInt(Math.max(1,
								Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0))) {
					GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, tMinY - 1 + pRandom.nextInt(7), tZ, this.mSporadicMeta);
				}
			}
		}
		// ---------------------------
		if (GalacticGreg.GalacticConfig.ProfileOreGen)
		{
			try {
				mProfilingEnd = System.currentTimeMillis();
				long tTotalTime = mProfilingEnd - mProfilingStart;
				GalacticGreg.Profiler.AddTimeToList(tMDD, tTotalTime);
				GalacticGreg.Logger.debug("Done with OreLayer-Worldgen in DimensionType %s. Generation took %d ms", tMDD.getDimensionName(), tTotalTime);
			} catch (Exception e) { } // Silently ignore errors
		}
	
		
		GalacticGreg.Logger.trace("Leaving executeWorldgen");
		return true;
	}
}
