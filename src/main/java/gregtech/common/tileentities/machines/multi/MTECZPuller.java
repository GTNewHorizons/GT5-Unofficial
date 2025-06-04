package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.blocks.BlockCasings9;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.multi.gui.MTECZPullerGui;

public class MTECZPuller extends MTEEnhancedMultiBlockBase<MTECZPuller> implements ISurvivalConstructable {

    private MTEHatchInputBus mSeedInputBus;
    public int mHeat = 0;
    public int mAmount = 0;
    public int capacityLimit = 0;
    private int seedInputAmount = 0;
    private HeatingCoilLevel mCoilLevel = null;
    private static final String TIER_1 = "tier1";
    private static final String TIER_2 = "tier2";
    private static final int tier1Value = 1;
    private static final int tier2Value = 2;
    private byte mSpecialTier = 1;
    private byte mActualTier = 0;

    public String materialType;

    private String getMaterialType() {
        if (mSeedInputBus == null) return null;
        if (mSeedInputBus.mFluid == null) return null;
        if (mSeedInputBus.mFluid.getFluid() == null) return null;
        String fluidName = mSeedInputBus.mFluid.getFluid()
            .getName();
        if (fluidName.equals("Silicon") || fluidName.equals("AlGaAs")) return fluidName;
        return null;
    }

    private int materialAmount = 0;
    private static final IStructureDefinition<MTECZPuller> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECZPuller>builder()
        // spotless:off
        .addShape(
            TIER_1,
            transpose(
                new String[][] {
                    { "     ", "     ", "  Z  ", "     ", "     " },
                    { "     ", "     ", "  E  ", "     ", "     " },
                    { "     ", "  E  ", " EBE ", "  E  ", "     " },
                    { "     ", "  E  ", " E E ", "  E  ", "     " },
                    { "  A  ", " AAA ", "AA AA", " AAA ", "  A  " },
                    { " DAD ", "DCCCD", "AC CA", "DCCCD", " DAD " },
                    { " DAD ", "DCCCD", "AC CA", "DCCCD", " DAD " },
                    { " DAD ", "DCCCD", "AC CA", "DCCCD", " DAD " },
                    { " A~A ", "AAAAA", "AAAAA", "AAAAA", " AAA " }}))
        .addShape(
            TIER_2,
            transpose(
                    new String[][]{
                            {"           ","           ","           ","           ","           ","     F     ","           ","           ","           ","           ","           "},
                            {"           ","           ","           ","           ","           ","     F     ","           ","           ","           ","           ","           "},
                            {"           ","           ","           ","           ","    G G    ","     F     ","    G G    ","           ","           ","           ","           "},
                            {"           ","           ","  HGGZGGH  ","  GG   GG  ","  G G G G  ","  Z  F  Z  ","  G G G G  ","  GG   GG  ","  HGGZGGH  ","           ","           "},
                            {"           "," HG     GH "," G  HHH  G ","   GHHHG   ","  HH G HH  ","  HHGFGHH  ","  HH G HH  ","   GHHHG   "," G  HHH  G "," HG     GH ","           "},
                            {"HG       GH","G         G","    CCC    ","   CHHHC   ","  CHHHHHC  ","  CHHFHHC  ","  CHHHHHC  ","   CHHHC   ","    CCC    ","G         G","HG       GH"},
                            {"G         G","           ","    CCC    ","   CHHHC   ","  CH   HC  ","  CH   HC  ","  CH   HC  ","   CHHHC   ","    CCC    ","           ","G         G"},
                            {"G         G","           ","    FFF    ","   FHHHF   ","  FH   HF  ","  FH   HF  ","  FH   HF  ","   FHHHF   ","    FFF    ","           ","G         G"},
                            {"G         G","           ","    CCC    ","   CHHHC   ","  CH   HC  ","  CH   HC  ","  CH   HC  ","   CHHHC   ","    CCC    ","           ","G         G"},
                            {"G         G","           ","    CCC    ","   CHHHC   ","  CH   HC  ","  CH   HC  ","  CH   HC  ","   CHHHC   ","    CCC    ","           ","G         G"},
                            {"GG       GG","G         G","    HZH    ","   GHHHG   ","  HHHHHHH  ","  HHHHHHH  ","  HHHHHHH  ","   GHHHG   ","    HHH    ","G         G","GG       GG"}
                    }
            ))
        // spotless:on
        .addElement(
            'A',
            buildHatchAdder(MTECZPuller.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                .buildAndChain(onElementPass(MTECZPuller::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0))))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 3))
        .addElement(
            'C',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTECZPuller::setCoilLevel, MTECZPuller::getCoilLevel))))
        .addElement('D', ofFrame(Materials.Steel))
        .addElement(
            'E',
            buildHatchAdder(MTECZPuller.class).atLeast(InputBus)
                .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings2, 0)))
        .addElement('F', ofFrame(Materials.TengamAttuned))
        .addElement('G', ofFrame(Materials.SuperconductorZPM))
        .addElement(
            'H',
            buildHatchAdder(MTECZPuller.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(11))
                .buildAndChain(onElementPass(MTECZPuller::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings9, 11))))
        .addElement(
            'J',
            buildHatchAdder(MTECZPuller.class).atLeast(InputBus)
                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(11))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings9, 11)))
        .addElement(
            'Z',
            buildHatchAdder(MTECZPuller.class).hatchClass(MTEHatchInputBus.class)
                .adder(MTECZPuller::addSeedBusToMachineList)
                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(11))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings9, 11)))
        .build();

    public MTECZPuller(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECZPuller(String aName) {
        super(aName);
    }

    public boolean addSeedBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInputBus) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInputBus) aMetaTileEntity).mRecipeMap = null;
            mSeedInputBus = (MTEHatchInputBus) aMetaTileEntity;
            seedInputAmount++;
            return true;
        }
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECZPuller(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("CZ Puller")
            .addInfo("Growing your monocrystals")
            .addStructureInfo("")
            .toolTipFinisher("Mireska_le_Fay");
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    private int getTier() {
        return mSpecialTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        if (aEventID == 1) {
            if ((aValue & tier1Value) == tier1Value) {
                mSpecialTier = 1;
            }
            if ((aValue & tier2Value) == tier2Value) {
                mSpecialTier = 2;
            }
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.czRecipes;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mSpecialTier", mSpecialTier);
        aNBT.setString("czMaterialType", getMaterialType());
        aNBT.setInteger("czMaterialAmount", materialAmount);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mSeparate")) {
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        mSpecialTier = aNBT.getByte("mSpecialTier");
        materialType = aNBT.getString("czMaterialType");
        materialAmount = aNBT.getInteger("czMaterialAmount");

    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IStructureDefinition<MTECZPuller> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return recipe.mSpecialValue <= mSpecialTier ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.NO_RECIPE;
            }
        };
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();
        mExoticEnergyHatches.clear();
        seedInputAmount = 0;

        boolean t1 = checkPiece(TIER_1, 2, 8, 0);
        boolean t2 = checkPiece(TIER_2, 5, 10, 2);
        if (mSpecialTier == 1) {
            if (!t1) return false;
            mActualTier = 1;
        }
        if (mSpecialTier == 2) {
            if (!t2) return false;
            mActualTier = 2;
        }
        if (seedInputAmount > 1) return false;
        if (mEnergyHatches.isEmpty()) return false;
        if (mEnergyHatches.size() > 2) return false;
        if (mEnergyHatches.size() == 2 && !(mEnergyHatches.get(0).mTier == mEnergyHatches.get(1).mTier)) return false;
        if (mExoticEnergyHatches.size() > 1) return false;
        if (mMaintenanceHatches.size() > 1) return false;

        capacityLimit = switch (mSpecialTier) {
            case 1 -> 1024;
            case 2 -> 65536;
            default -> 0;
        };
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel level) {
        mCoilLevel = level;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (mSpecialTier == 1) {
            buildPiece(TIER_1, stackSize, hintsOnly, 2, 8, 0);
        }
        if (mSpecialTier == 2) {
            buildPiece(TIER_2, stackSize, hintsOnly, 5, 10, 2);
        }

    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;
        if (mMachine) return -1;
        if (mSpecialTier == 1) {
            built += survivialBuildPiece(TIER_1, stackSize, 2, 8, 0, elementBudget, env, false, true);
        }
        if (mSpecialTier == 2) {
            built += survivialBuildPiece(TIER_2, stackSize, 5, 10, 2, elementBudget, env, false, true);
        }
        return built;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public byte getUpdateData() {
        byte data = 0;
        if (mSpecialTier == 1) {
            data += tier1Value;
        }
        if (mSpecialTier == 2) {
            data += tier2Value;
        }
        return data;
    }

    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(
                    getTier() == 1 ? GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)
                        : GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 11)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(
                    getTier() == 1 ? GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)
                        : GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 11)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENGRAVER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENGRAVER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(
            mSpecialTier == 1 ? ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0)
                : ((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(11)) };
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTECZPullerGui(this);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 20 == 0) {
            materialType = getMaterialType();

        }
    }
}
