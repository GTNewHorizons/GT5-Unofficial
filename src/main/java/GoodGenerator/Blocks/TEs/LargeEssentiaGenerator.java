package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import GoodGenerator.util.DescTextLocalization;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.List;

import static GoodGenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class LargeEssentiaGenerator extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<LargeEssentiaGenerator> multiDefinition = null;
    protected final int ENERGY_PER_ESSENTIA_DEFAULT = 8192;
    protected int mStableValue = 0;
    protected long mLeftEnergy;
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
        return structureCheck_EM(mName, 4, 0, 4) && (mDynamoHatches.size() + eDynamoMulti.size()) == 1;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mStableValue = aNBT.getInteger("mStableValue");
        this.mLeftEnergy = aNBT.getLong("mLeftEnergy");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mStableValue", this.mStableValue);
        aNBT.setLong("mLeftEnergy", this.mLeftEnergy);
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
        setEssentiaToEUVoltageAndAmp(getVoltageLimit(), getAmpLimit());
        return true;
    }

    public int getVoltageLimit() {
        long voltage = 0;
        for (GT_MetaTileEntity_Hatch tHatch : this.eDynamoMulti) {
            voltage += tHatch.maxEUOutput();
        }
        for (GT_MetaTileEntity_Hatch tHatch : this.mDynamoHatches) {
            voltage += tHatch.maxEUOutput();
        }
        if (voltage > Integer.MAX_VALUE) voltage = Integer.MAX_VALUE;
        return (int)voltage;
    }

    public int getAmpLimit() {
        long amp = 0;
        for (GT_MetaTileEntity_Hatch tHatch : this.eDynamoMulti) {
            amp += tHatch.maxAmperesOut();
        }
        for (GT_MetaTileEntity_Hatch tHatch : this.mDynamoHatches) {
            amp += tHatch.maxAmperesOut();
        }
        if (amp > Integer.MAX_VALUE) amp = Integer.MAX_VALUE;
        return (int)amp;
    }

    public long getPerAspectEnergy(Aspect aspect) {
        if (aspect.equals(Aspect.ENERGY)) return 45000 * mStableValue / 25;
        if (aspect.equals(Aspect.FIRE)) return 30000 * mStableValue / 25;
        if (aspect.equals(Aspect.GREED)) return 130000 * mStableValue / 25;
        if (aspect.equals(Aspect.AURA)) return 120000 * mStableValue / 25;
        if (aspect.equals(Aspect.TREE)) return 25000 * mStableValue / 25;
        if (aspect.equals(Aspect.AIR)) return 13000 * mStableValue / 25;
        if (aspect.equals(Aspect.MAGIC)) return 92000 * mStableValue / 25;
        if (aspect.equals(Aspect.MECHANISM)) return 40000 * mStableValue / 25;
        if (aspect.equals(TC_Aspects.ELECTRUM.mAspect)) return 131072 * mStableValue / 25;
        if (aspect.equals(TC_Aspects.RADIO.mAspect)) return 524288 * mStableValue / 25;

        return ENERGY_PER_ESSENTIA_DEFAULT * mStableValue / 25;
    }

    public void setEssentiaToEUVoltageAndAmp(long voltageLimit, long ampLimit) {
        long EUt = mLeftEnergy;
        long EUVoltage = voltageLimit, EUAmp = 1;

        for (EssentiaHatch hatch: this.mEssentiaHatch){
            AspectList aspects = hatch.getAspects();
            for (Aspect aspect: aspects.aspects.keySet()) {
                while (EUt + getPerAspectEnergy(aspect) <= (voltageLimit * ampLimit) && aspects.getAmount(aspect) > 0) {
                    EUt += getPerAspectEnergy(aspect);
                    aspects.reduce(aspect, 1);
                    if (aspects.getAmount(aspect) == 0)
                        aspects.remove(aspect);
                }
            }
            if (EUt == 0 && aspects.size() != 0) {
                EUt += getPerAspectEnergy(aspects.getAspects()[0]);
                aspects.reduce(aspects.getAspects()[0], 1);
                if (aspects.getAmount(aspects.getAspects()[0]) == 0)
                    aspects.remove(aspects.getAspects()[0]);
            }
        }

        if (EUt <= voltageLimit) {
            EUVoltage = EUt;
            EUAmp = 1;
            mLeftEnergy = 0;
        }
        else {
            while (EUVoltage * (EUAmp + 1) <= EUt && EUAmp + 1 <= ampLimit) {
                EUAmp ++;
            }
            mLeftEnergy = EUt - (EUVoltage * EUAmp);
        }

        this.mEUt = (int)EUVoltage;
        this.eAmpereFlow = (int)EUAmp;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("LargeEssentiaGenerator.hint", 6);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeEssentiaGenerator(this.mName);
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Essentia Generator")
                .addInfo("Controller block for the Large Essentia Generator")
                .addInfo("Maybe some thaumaturages are upset by it. . .")
                .addInfo("Transform essentia into energy!")
                .addInfo("You can find more information about this generator in Thaumonomicon.")
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .addMaintenanceHatch("Hint block with dot 1")
                .addInputHatch("Hint block with dot 1")
                .addDynamoHatch("Hint block with dot 1")
                .addOtherStructurePart("Essentia Input Hatch","Hint block with dot 1")
                .toolTipFinisher("Good Generator");
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return tt.getInformation();
        } else {
            return tt.getStructureInformation();
        }
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
