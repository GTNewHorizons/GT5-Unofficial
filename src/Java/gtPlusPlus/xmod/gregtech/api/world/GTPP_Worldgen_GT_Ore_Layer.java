package gtPlusPlus.xmod.gregtech.api.world;

import static gtPlusPlus.xmod.gregtech.HANDLER_GT.sCustomWorldgenFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.loaders.misc.GT_Achievements;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;

public class GTPP_Worldgen_GT_Ore_Layer
extends GTPP_Worldgen {
	public static ArrayList<GTPP_Worldgen_GT_Ore_Layer> sList = new ArrayList<GTPP_Worldgen_GT_Ore_Layer>();
	public static int sWeight = 0;
	public final short mMinY;
	public final short mMaxY;
	public final short mWeight;
	public final short mDensity;
	public final short mSize;
	public short mPrimaryMeta;
	public short mSecondaryMeta;
	public short mBetweenMeta;
	public short mSporadicMeta;
	public final String mRestrictBiome;
	public final boolean mDarkWorld;
	public final String aTextWorldgen = "worldgen.gtpp.";

	public GTPP_Worldgen_GT_Ore_Layer(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, boolean aOverworld, Materials aPrimary, Materials aSecondary, Materials aBetween, Materials aSporadic) {
		super(aName, sList, aDefault);
		this.mDarkWorld = sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Overworld", aOverworld);
		this.mMinY = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MinHeight", aMinY));
		this.mMaxY = ((short) Math.max(this.mMinY + 5, sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MaxHeight", aMaxY)));
		this.mWeight = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RandomWeight", aWeight));
		this.mDensity = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Density", aDensity));
		this.mSize = ((short) Math.max(1, sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Size", aSize)));
		this.mPrimaryMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OrePrimaryLayer", aPrimary.mMetaItemSubID));
		this.mSecondaryMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSecondaryLayer", aSecondary.mMetaItemSubID));
		this.mBetweenMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSporadiclyInbetween", aBetween.mMetaItemSubID));
		this.mSporadicMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSporaticlyAround", aSporadic.mMetaItemSubID));
		this.mRestrictBiome = sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RestrictToBiomeName", "None");

		if (this.mEnabled) {
			GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mPrimaryMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSecondaryMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mBetweenMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSporadicMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			sWeight += this.mWeight;            
		}
	}

	public GTPP_Worldgen_GT_Ore_Layer(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity,
			int aSize, Material aPrimary, Material aSecondary, Material aBetween,
			Material aSporadic) {
		super(aName, sList, aDefault);
		this.mDarkWorld = sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Darkworld", true);
		this.mMinY = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MinHeight", aMinY));
		this.mMaxY = ((short) Math.max(this.mMinY + 5, sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MaxHeight", aMaxY)));
		this.mWeight = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RandomWeight", aWeight));
		this.mDensity = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Density", aDensity));
		this.mSize = ((short) Math.max(1, sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Size", aSize)));
		/*this.mPrimaryMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OrePrimaryLayer", aPrimary.mMetaItemSubID));
		this.mSecondaryMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSecondaryLayer", aSecondary.mMetaItemSubID));
		this.mBetweenMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSporadiclyInbetween", aBetween.mMetaItemSubID));
		this.mSporadicMeta = ((short) sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSporaticlyAround", aSporadic.mMetaItemSubID));
		 */this.mRestrictBiome = sCustomWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RestrictToBiomeName", "None");

		 if (this.mEnabled) {
			 /* GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mPrimaryMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			 GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSecondaryMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			 GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mBetweenMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			 GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSporadicMeta % 1000)], aMinY, aMaxY, aWeight, false, false, false);
			  */ sWeight += this.mWeight;            
		 }
	}

	@Override
	public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		if (!this.mRestrictBiome.equals("None") && !(this.mRestrictBiome.equals(aBiome))) {
			return false; //Not the correct biome for ore mix
		}
		if (!isGenerationAllowed(aWorld, aDimensionType, ((aDimensionType == -1) && (false)) || ((aDimensionType == 0) && (this.mDarkWorld)) || ((aDimensionType == 1) && (false)) || ((aWorld.provider.getDimensionName().equals("Moon")) && (false)) || ((aWorld.provider.getDimensionName().equals("Mars")) && (false)) ? aDimensionType : aDimensionType ^ 0xFFFFFFFF)) {
			return false;
		}
		int tMinY = this.mMinY + aRandom.nextInt(this.mMaxY - this.mMinY - 5);

		int cX = aChunkX - aRandom.nextInt(this.mSize);
		int eX = aChunkX + 16 + aRandom.nextInt(this.mSize);
		for (int tX = cX; tX <= eX; tX++) {
			int cZ = aChunkZ - aRandom.nextInt(this.mSize);
			int eZ = aChunkZ + 16 + aRandom.nextInt(this.mSize);
			for (int tZ = cZ; tZ <= eZ; tZ++) {
				if (this.mSecondaryMeta > 0) {
					for (int i = tMinY - 1; i < tMinY + 2; i++) {
						if ((aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0)) {
							setOreBlock(aWorld, tX, i, tZ, this.mSecondaryMeta, false);
						}
					}
				}
				if ((this.mBetweenMeta > 0) && ((aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0))) {
					setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, this.mBetweenMeta, false);
				}
				if (this.mPrimaryMeta > 0) {
					for (int i = tMinY + 3; i < tMinY + 6; i++) {
						if ((aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0)) {
							setOreBlock(aWorld, tX, i, tZ, this.mPrimaryMeta, false);
						}
					}
				}
				if ((this.mSporadicMeta > 0) && ((aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0))) {
					setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, this.mSporadicMeta, false);
				}
			}
		}
		if (GT_Values.D1) {
			System.out.println("Generated Orevein: " + this.mWorldGenName+" "+aChunkX +" "+ aChunkZ);
		}
		return true;
	}
	private Method mSetOre = null;
	private boolean setOreBlock(World world, int x, int y, int z, int secondarymeta, boolean bool){

		if (mSetOre == null){
			try {
				mSetOre = GT_TileEntity_Ores.class.getMethod("setOreBlock", World.class, int.class, int.class, int.class, int.class, boolean.class);
			} 
			catch (SecurityException | NoSuchMethodException e) {
				try {
					mSetOre = GT_TileEntity_Ores.class.getMethod("setOreBlock", World.class, int.class, int.class, int.class, int.class);
				} 
				catch (SecurityException | NoSuchMethodException r) {}
			}		
		}

		if (mSetOre != null) {
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				try {
					return (boolean) mSetOre.invoke(world, x, y, z, secondarymeta, bool);
				} 
				catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException t) {
					return false;
				}
			}
			else {
				try {
					return (boolean) mSetOre.invoke(world, x, y, z, secondarymeta);
				} 
				catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException t) {
					return false;}
			}
		}
		else {
			return false;
		}
	}
}