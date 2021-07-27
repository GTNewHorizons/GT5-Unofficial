package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectSourceHelper;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class LargeEssentiaGenerator extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<LargeEssentiaGenerator> multiDefinition = null;
    protected List<Aspect> mAvailableAspects = new ArrayList<>(Aspect.aspects.size());
    protected final int MAX_RANGE = 16;
    protected final int ENERGY_PER_ESSENTIA = 512;
    protected int mStableValue = 0;

    public LargeEssentiaGenerator(String name){
        super(name);
    }

    public LargeEssentiaGenerator(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyTunnel> getTecTechEnergyTunnels() {
        return new ArrayList<>();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return new ArrayList<>();
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        structureBuild_EM(mName, 4, 0, 4, b, itemStack);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mStableValue = 0;
        return structureCheck_EM(mName, 4, 0, 4);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT){
        super.loadNBTData(aNBT);
        this.mStableValue = aNBT.getInteger("mStableValue");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT){
        super.saveNBTData(aNBT);
        aNBT.setInteger("mStableValue", this.mStableValue);
    }

    @Override
    public IStructureDefinition<LargeEssentiaGenerator> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<LargeEssentiaGenerator>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"A       A","         ","         ","         ","    ~    ","         ","         ","         ","A       A"},
                                    {"T   C   T","   CEC   ","  CEEEC  "," CEEEEEC ","CEEEEEEEC"," CEEEEEC ","  CEEEC  ","   CEC   ","T   C   T"},
                                    {"T  TXT  T","  TCCCT  "," TCCCCCT ","TCCCCCCCT","XCCCCCCCX","TCCCCCCCT"," TCCCCCT ","  TCCCT  ","T  TXT  T"}
                            })
                    ).addElement(
                            'A',
                            ofBlock(
                                    ConfigBlocks.blockCosmeticOpaque, 1
                            )
                    ).addElement(
                            'T',
                            ofBlock(
                                    ConfigBlocks.blockCosmeticSolid, 7
                            )
                    ).addElement(
                            'C',
                            ofBlock(
                                    Loaders.magicCasing, 0
                            )
                    ).addElement(
                            'E',
                            ofChain(
                                    onElementPass(
                                            x -> ++x.mStableValue,
                                            ofBlock(
                                                    Loaders.essentiaCell, 0
                                            )
                                    ),
                                    onElementPass(
                                            x -> x.mStableValue += 2,
                                            ofBlock(
                                                    Loaders.essentiaCell, 1
                                            )
                                    ),
                                    onElementPass(
                                            x -> x.mStableValue += 5,
                                            ofBlock(
                                                    Loaders.essentiaCell, 2
                                            )
                                    )
                            )
                    ).addElement(
                            'X',
                            ofChain(
                                    ofHatchAdder(
                                            LargeEssentiaGenerator::addLargeEssentiaGeneratorList,1536,
                                            1
                                    ),
                                    ofBlock(
                                            Loaders.magicCasing, 0
                                    )
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    public final boolean addLargeEssentiaGeneratorList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo)aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti)aMetaTileEntity);
            }
        }
        return false;
    }

    List<Aspect> getAvailableAspects() {
        return mAvailableAspects;
    }

    public void scanAvailableAspects() {
        IGregTechTileEntity tBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (tBaseMetaTileEntity.isInvalidTileEntity()) return;
        int tRange = MAX_RANGE;
        int tY = tBaseMetaTileEntity.getYCoord();
        int tMaxY = tBaseMetaTileEntity.getWorld().getHeight()-1;
        int rYMin = (tY - tRange >= 0) ? -tRange : -(tY);
        int rYMax = (((tY + tRange) <= tMaxY)? tRange : tMaxY - tY);
        mAvailableAspects.clear();
        for (int rX = -tRange; rX <= tRange; rX++) {
            for (int rZ = -tRange; rZ <= tRange; rZ++) {
                for (int rY = rYMin; rY < rYMax; rY++) {
                    TileEntity tTile = tBaseMetaTileEntity.getTileEntityOffset(rX, rY, rZ);
                    if (tTile instanceof IAspectContainer) {
                        AspectList tAspectList = ((IAspectContainer) tTile).getAspects();
                        if (tAspectList == null || tAspectList.aspects.isEmpty()) continue;
                        Set<Aspect> tAspects = tAspectList.aspects.keySet();
                        mAvailableAspects.addAll(tAspects);
                    }
                }
            }
        }
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        this.mEfficiency = 10000;
        this.mMaxProgresstime = 1;
        scanAvailableAspects();
        this.mEUt = (int)absorbFromEssentiaContainers();
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    private long absorbFromEssentiaContainers() {
        long tEU = 0;

        long tEUtoGen = 8192;
        List<Aspect> mAvailableEssentiaAspects = getAvailableAspects();
        for (int i = mAvailableEssentiaAspects.size() - 1; i >= 0 && tEUtoGen > 0; i--) {
            Aspect aspect = mAvailableEssentiaAspects.get(i);
            long tAspectEU = ENERGY_PER_ESSENTIA;
            if (tAspectEU <= tEUtoGen
                    && AspectSourceHelper.drainEssentia((TileEntity) this.getBaseMetaTileEntity(), aspect, ForgeDirection.UNKNOWN, MAX_RANGE)) {
                tEUtoGen -= tAspectEU;
                tEU += tAspectEU;
            }
        }
        return tEU;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeEssentiaGenerator(this.mName);
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aSide == aFacing) {
            if(aActive) return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(1536), new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG), TextureFactory.builder().addIcon(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG_GLOW).glow().build()};
            return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(1536), new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG)};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(1536)};
    }
}
