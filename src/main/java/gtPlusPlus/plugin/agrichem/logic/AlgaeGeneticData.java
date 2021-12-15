package gtPlusPlus.plugin.agrichem.logic;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraft.nbt.NBTTagCompound;

public class AlgaeGeneticData {
	
	private final int mLifespan;	
	
	private final int mGeneration;
	
	private final boolean mIsDominant;
	
	private final boolean mRequiresLight;
	
	private final boolean mSaltWater;
	
	private final boolean mFreshWater;
	
	private final byte mTempTolerance;
	
	private final float mFertility;
	
	private final float mProductionSpeed;
	
	private final AutoMap<AlgaeGrowthRequirement> mSpecialRequirements;
	

	public AlgaeGeneticData() {
		this(true, true, true, true, (byte) 0, 1f, 1f, (byte) 30, 0, new AutoMap<AlgaeGrowthRequirement>());
	}
	
	public AlgaeGeneticData(boolean isDominant, boolean requiresLight, boolean isSalt, boolean isFresh, 
			byte aTempTolerance, float aFertility, float aSpeed, byte aLifespan, int aGeneration, AutoMap<AlgaeGrowthRequirement> aRequirements) {
		mIsDominant = isDominant;
		mRequiresLight = requiresLight;
		mSaltWater = isSalt;
		mFreshWater = isFresh;
		mTempTolerance = aTempTolerance;
		mFertility = aFertility;
		mProductionSpeed = aSpeed;
		mLifespan = aLifespan;
		mGeneration = aGeneration;
		mSpecialRequirements = aRequirements;
	}

	
	public AlgaeGeneticData(NBTTagCompound aNBT) {
		if (aNBT == null || aNBT.hasNoTags()) {
			mIsDominant = true;
			mRequiresLight = true;
			mSaltWater = true;
			mFreshWater = true;
			mTempTolerance = 0;
			mFertility = 1;
			mProductionSpeed = 1;
			mLifespan = 30;
			mGeneration = 0;
		}
		else {
			mIsDominant = aNBT.getBoolean("mIsDominant");
			mRequiresLight = aNBT.getBoolean("mRequiresLight");
			mSaltWater = aNBT.getBoolean("mSaltWater");
			mFreshWater = aNBT.getBoolean("mFreshWater");
			mTempTolerance = aNBT.getByte("mTempTolerance");
			mFertility = aNBT.getFloat("mFertility");
			mProductionSpeed = aNBT.getFloat("mProductionSpeed");
			mLifespan = aNBT.getByte("mLifespan");
			mGeneration = aNBT.getInteger("mGeneration");
		}
		mSpecialRequirements = new AutoMap<AlgaeGrowthRequirement>();
	}

	/**
	 * In MC Days
	 */
	public final int getLifespan() {
		return this.mLifespan;
	}

	public final boolean isDominant() {
		return this.mIsDominant;
	}

	public final boolean RequiresLight() {
		return this.mRequiresLight;
	}

	public final boolean isSaltWater() {
		return this.mSaltWater;
	}

	public final boolean isFreshWater() {
		return this.mFreshWater;
	}

	public final byte getTempTolerance() {
		return this.mTempTolerance;
	}

	public final float getFertility() {
		return this.mFertility;
	}

	public final float getProductionSpeed() {
		return this.mProductionSpeed;
	}

	public final int getGeneration() {
		return this.mGeneration;
	}

	public final AutoMap<AlgaeGrowthRequirement> getSpecialRequirements() {
		return this.mSpecialRequirements;
	}
	
	public NBTTagCompound writeToNBT() {
		NBTTagCompound aGenes = new NBTTagCompound();
		aGenes.setBoolean("mIsDominant", this.mIsDominant);
		aGenes.setBoolean("mRequiresLight", this.mRequiresLight);
		aGenes.setBoolean("mSaltWater", this.mSaltWater);
		aGenes.setBoolean("mFreshWater", this.mFreshWater);
		aGenes.setInteger("mLifespan", this.mLifespan);
		aGenes.setInteger("mGeneration", this.mGeneration);
		aGenes.setByte("mTempTolerance", this.mTempTolerance);
		aGenes.setFloat("mFertility", this.mFertility);
		aGenes.setFloat("mProductionSpeed", this.mProductionSpeed);		
		return aGenes;
	}
}
