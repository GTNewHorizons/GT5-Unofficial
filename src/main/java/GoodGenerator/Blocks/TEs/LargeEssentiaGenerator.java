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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class LargeEssentiaGenerator extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<LargeEssentiaGenerator> multiDefinition = null;
    protected final int ENERGY_PER_ESSENTIA = 512;
    protected int mStableValue = 0;
    protected ArrayList<EssentiaHatch> mEssentiaHatch = new ArrayList<>();

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

    public void getEssentiaHatch() {
        IGregTechTileEntity aTileEntity = getBaseMetaTileEntity();
        if (aTileEntity == null) return;
        mEssentiaHatch.clear();
        TileEntity hatch = aTileEntity.getTileEntityOffset(4,-2, 0);
        addEssentiaHatch(hatch);
        hatch = aTileEntity.getTileEntityOffset(-4,-2, 0);
        addEssentiaHatch(hatch);
        hatch = aTileEntity.getTileEntityOffset(0,-2, 4);
        addEssentiaHatch(hatch);
        hatch = aTileEntity.getTileEntityOffset(0,-2, -4);
        addEssentiaHatch(hatch);
    }

    public void addEssentiaHatch(TileEntity aHatch) {
        if (aHatch instanceof EssentiaHatch) {
            mEssentiaHatch.add((EssentiaHatch) aHatch);
        }
    }

    @Override
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aWrenchingSide == 0 || aWrenchingSide == 1) return false;
        if (getBaseMetaTileEntity().isValidFacing(aWrenchingSide)) {
            getBaseMetaTileEntity().setFrontFacing(aWrenchingSide);
            return true;
        }
        return false;
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
                                    ),
                                    ofBlock(
                                            Loaders.essentiaHatch, 0
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

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        this.mEfficiency = 10000;
        this.mMaxProgresstime = 1;
        getEssentiaHatch();
        this.mEUt = (int)getEssentiaToEU();
        return true;
    }

    public long getEssentiaToEU(){
        long EUt = 0;
        for (EssentiaHatch hatch: this.mEssentiaHatch){
            AspectList aspects = hatch.getAspects();
            for (Aspect aspect: aspects.aspects.keySet()) {
                while (EUt < 8192 && aspects.getAmount(aspect) > 0) {
                    EUt += ENERGY_PER_ESSENTIA;
                    aspects.reduce(aspect, 1);
                }
            }
        }
        return EUt;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
