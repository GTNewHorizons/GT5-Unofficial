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
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

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

public class MTECZPuller extends MTEEnhancedMultiBlockBase<MTECZPuller> implements ISurvivalConstructable {

    private MTEHatchInputBus mSeedInputBus;
    public int mHeat = 0;
    public int mAmount = 0;
    public int capacityLimit = 0;
    private HeatingCoilLevel mCoilLevel = null;
    private static final String TIER_1 = "tier1";
    private static final String TIER_2 = "tier2";
    private byte mSpecialTier = 1;

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
                            {"GG       GG","G         G","    H~H    ","   GHHHG   ","  HHHHHHH  ","  HHHHHHH  ","  HHHHHHH  ","   GHHHG   ","    HHH    ","G         G","GG       GG"}
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
        .addElement('G', ofFrame(Materials.NaquadahEnriched))
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
            .beginStructureBlock(0, 0, 0, true)
            .addCasingInfoMin("Antimatter Containment Casing", 512, false)
            .addCasingInfoMin("Magnetic Flux Casing", 2274, false)
            .addCasingInfoMin("Gravity Stabilization Casing", 623, false)
            .addCasingInfoMin("Protomatter Activation Coil", 126, false)
            .addInputHatch("1-6, Hint block with dot 1", 1)
            .addEnergyHatch("1-9, Hint block with dot 2", 2)
            .addOtherStructurePart(
                StatCollector.translateToLocal("gg.structure.tooltip.antimatter_hatch"),
                "16, Hint Block with dot 3",
                3)
            .toolTipFinisher("Mireska_le_Fay");
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.czRecipes;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("czMaterialType", getMaterialType());
        aNBT.setInteger("czMaterialAmount", materialAmount);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        materialType = aNBT.getString("czMaterialType");
        materialAmount = aNBT.getInteger("czMaterialAmount");
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
        if (checkTier(TIER_2, 5, 10, 2, 25)) {
            mSpecialTier = 2;
            capacityLimit = 65536;
            return true;
        }
        if (checkTier(TIER_1, 2, 8, 0, 15)) {
            mSpecialTier = 1;
            capacityLimit = 1024;
            return true;
        }
        mSpecialTier = 0;
        capacityLimit = 0;
        return false;
    }

    private boolean checkTier(String tier, int offsetX, int offsetY, int offsetZ, int minCasings) {
        mCasingAmount = 0;
        mEnergyHatches.clear();
        mExoticEnergyHatches.clear();
        mSeedInputBus = null;
        return checkPiece(tier, offsetX, offsetY, offsetZ) && mCasingAmount >= minCasings
            && mSeedInputBus != null
            && (!mEnergyHatches.isEmpty() || mExoticEnergyHatches.size() <= 1)
            && mEnergyHatches.size() <= 2
            && (mEnergyHatches.size() <= 1 || mEnergyHatches.get(0).mTier == mEnergyHatches.get(1).mTier);
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
        switch (stackSize.stackSize) {
            case 1 -> buildPiece(TIER_1, stackSize, hintsOnly, 2, 8, 0);
            default -> buildPiece(TIER_2, stackSize, hintsOnly, 5, 10, 2);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;
        if (mMachine) return -1;
        built += switch (mSpecialTier) {
            case 1 -> survivialBuildPiece(TIER_1, stackSize, 2, 8, 0, elementBudget, env, false, true);
            case 2 -> survivialBuildPiece(TIER_2, stackSize, 5, 10, 2, elementBudget, env, false, true);
            default -> 0;
        };
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

    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(
                    this.mSpecialTier == 1 ? GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)
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
                    this.mSpecialTier == 1 ? GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)
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
            this.mSpecialTier == 1 ? ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0)
                : ((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(11)) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0) {
            materialType = getMaterialType();
        }
    }
}
