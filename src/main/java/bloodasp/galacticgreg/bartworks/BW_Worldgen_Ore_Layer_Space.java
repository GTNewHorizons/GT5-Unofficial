package bloodasp.galacticgreg.bartworks;

import static bloodasp.galacticgreg.GalacticGreg.oreVeinWorldgenList;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGeneratedOreTE;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Ores;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_SmallOres;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_OreLayer;

import bloodasp.galacticgreg.GT_TileEntity_Ores_Space;
import bloodasp.galacticgreg.api.Enums;
import bloodasp.galacticgreg.api.ModDBMDef;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.dynconfig.DynamicOreMixWorldConfig;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.common.blocks.GT_TileEntity_Ores;

public class BW_Worldgen_Ore_Layer_Space extends BW_OreLayer {

    private ModDimensionDef pDimensionDef;
    private String name;
    private DynamicOreMixWorldConfig _mDynWorldConfig;

    /**
     * Code-only Constructor Will work with NEI
     *
     * @param aName       veinname, should start with mix.
     * @param pDefault    enabled?
     * @param pMinY
     * @param pMaxY
     * @param pWeight
     * @param pDensity
     * @param pSize       5-32 at max
     * @param primaryBW   either a werkstoff or a materials
     * @param secondaryBW either a werkstoff or a materials
     * @param betweenBW   either a werkstoff or a materials
     * @param sporadicBW  either a werkstoff or a materials
     */
    public BW_Worldgen_Ore_Layer_Space(String aName, boolean pDefault, int pMinY, int pMaxY, int pWeight, int pDensity,
        int pSize, ISubTagContainer primaryBW, ISubTagContainer secondaryBW, ISubTagContainer betweenBW,
        ISubTagContainer sporadicBW) {
        super(aName, pDefault, 0, 0, 0, 0, 0, primaryBW, secondaryBW, betweenBW, sporadicBW);
        mMinY = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "MinHeight", pMinY));
        mMaxY = ((short) Math.max(
            this.mMinY + 5,
            GregTech_API.sWorldgenFile.get("worldgen.GaGregBartworks." + this.mWorldGenName, "MaxHeight", pMaxY)));
        mWeight = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "RandomWeight", pWeight));
        mDensity = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "Density", pDensity));
        mSize = ((short) Math
            .max(1, GregTech_API.sWorldgenFile.get("worldgen.GaGregBartworks." + this.mWorldGenName, "Size", pSize)));
        if (mEnabled) {
            sWeight += this.mWeight;
            oreVeinWorldgenList.add(this);
        }
        _mDynWorldConfig = new DynamicOreMixWorldConfig(mWorldGenName, true);
        _mDynWorldConfig.InitDynamicConfig();

        StringBuilder ret = new StringBuilder();
        for (Map.Entry<String, Boolean> key : _mDynWorldConfig.get_mDynWorldConfigMap()
            .entrySet()
            .stream()
            .filter(Map.Entry::getValue)
            .collect(Collectors.toSet()))
            ret.append(
                key.getKey()
                    .split("_")[1])
                .append("; ");
        name = ret.substring(0, ret.length() - 1);
    }

    /**
     * Script Friendly Constructor, WONT WORK WITH NEI
     *
     * @param aName
     * @param pDefault
     * @param pMinY
     * @param pMaxY
     * @param pWeight
     * @param pDensity
     * @param pSize
     * @param pPrimary
     * @param pSecondary
     * @param pBetween
     * @param pSporadic
     * @param primaryBW
     * @param secondaryBW
     * @param betweenBW
     * @param sporadicBW
     */
    public BW_Worldgen_Ore_Layer_Space(String aName, boolean pDefault, int pMinY, int pMaxY, int pWeight, int pDensity,
        int pSize, int pPrimary, int pSecondary, int pBetween, int pSporadic, boolean primaryBW, boolean secondaryBW,
        boolean betweenBW, boolean sporadicBW) {
        super(
            aName,
            pDefault,
            0,
            0,
            0,
            0,
            0,
            primaryBW ? Werkstoff.default_null_Werkstoff : Materials._NULL,
            secondaryBW ? Werkstoff.default_null_Werkstoff : Materials._NULL,
            betweenBW ? Werkstoff.default_null_Werkstoff : Materials._NULL,
            sporadicBW ? Werkstoff.default_null_Werkstoff : Materials._NULL);
        mMinY = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "MinHeight", pMinY));
        mMaxY = ((short) Math.max(
            this.mMinY + 5,
            GregTech_API.sWorldgenFile.get("worldgen.GaGregBartworks." + this.mWorldGenName, "MaxHeight", pMaxY)));
        mWeight = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "RandomWeight", pWeight));
        mDensity = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "Density", pDensity));
        mSize = ((short) Math
            .max(1, GregTech_API.sWorldgenFile.get("worldgen.GaGregBartworks." + this.mWorldGenName, "Size", pSize)));
        mPrimaryMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "OrePrimaryLayer", pPrimary));
        mSecondaryMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "OreSecondaryLayer", pSecondary));
        mBetweenMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "OreSporadiclyInbetween", pBetween));
        mSporadicMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "OreSporaticlyAround", pSporadic));
        bwOres = ((byte) GregTech_API.sWorldgenFile
            .get("worldgen.GaGregBartworks." + this.mWorldGenName, "BWGTlogic", bwOres));
        _mDynWorldConfig = new DynamicOreMixWorldConfig(mWorldGenName, true);
        _mDynWorldConfig.InitDynamicConfig();
        StringBuilder ret = new StringBuilder();
        for (Map.Entry<String, Boolean> key : _mDynWorldConfig.get_mDynWorldConfigMap()
            .entrySet()
            .stream()
            .filter(Map.Entry::getValue)
            .collect(Collectors.toSet()))
            ret.append(
                key.getKey()
                    .split("_")[1])
                .append("; ");
        name = ret.length() == 0 ? "" : ret.substring(0, ret.length() - 1);
        if (mEnabled) {
            sWeight += this.mWeight;
            oreVeinWorldgenList.add(this);
        }
    }

    /**
     * Script Friendly Constructor, WONT WORK WITH NEI
     *
     * @param aName
     * @param enabled
     */
    public BW_Worldgen_Ore_Layer_Space(String aName, boolean enabled) {
        this(aName, enabled, 0, 0, 0, 0, 0, 0, 0, 0, 0, true, true, true, true);
    }

    public boolean isEnabledForDim(ModDimensionDef pDimensionDef) {
        return _mDynWorldConfig.isEnabledInDim(pDimensionDef);
    }

    @Override
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        ModDimensionDef tMDD = GalacticGregRegistry.getDimensionTypeByChunkGenerator(aChunkGenerator);
        if (tMDD == null) return false;
        pDimensionDef = tMDD;
        return super.executeWorldgen(
            aWorld,
            aRandom,
            aBiome,
            aDimensionType,
            aChunkX,
            aChunkZ,
            aChunkGenerator,
            aChunkProvider);
    }

    public boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre) {
        TileEntity te = aWorld.getTileEntity(aX, aY, aZ);
        if (!(te instanceof BW_MetaGeneratedOreTE) && !(te instanceof GT_TileEntity_Ores)) {
            if (aMetaData == this.mSporadicMeta && (this.bwOres & 1) != 0
                || aMetaData == this.mBetweenMeta && (this.bwOres & 2) != 0
                || aMetaData == this.mPrimaryMeta && (this.bwOres & 8) != 0
                || aMetaData == this.mSecondaryMeta && (this.bwOres & 4) != 0) {
                boolean wasSet;
                for (ModDBMDef e : pDimensionDef.getReplaceableBlocks()) {
                    wasSet = isSmallOre
                        ? BW_MetaGenerated_SmallOres.setOreBlock(
                            aWorld,
                            aX,
                            aY,
                            aZ,
                            aMetaData,
                            pDimensionDef.getAirSetting() == Enums.AirReplaceRule.AllowReplaceAir,
                            (Block) Block.blockRegistry.getObject(e.getBlockName()),
                            new int[] { e.getMeta() })
                        : BW_MetaGenerated_Ores.setOreBlock(
                            aWorld,
                            aX,
                            aY,
                            aZ,
                            aMetaData,
                            pDimensionDef.getAirSetting() == Enums.AirReplaceRule.AllowReplaceAir,
                            (Block) Block.blockRegistry.getObject(e.getBlockName()),
                            new int[] { e.getMeta() });
                    if (wasSet) return true;
                }
                return false;
            } else {
                return GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(
                    pDimensionDef,
                    aWorld,
                    aX,
                    aY,
                    aZ,
                    aMetaData,
                    pDimensionDef.getAirSetting() == Enums.AirReplaceRule.AllowReplaceAir);
            }
        } else {
            return true;
        }
    }

    @Override
    public Block getDefaultBlockToReplace() {
        return null;
    }

    @Override
    public int[] getDefaultDamageToReplace() {
        return null;
    }

    @Override
    public String getDimName() {
        return name;
    }

}
