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

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
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

    private int mHeatingCapacity = 0;
    private HeatingCoilLevel mCoilLevel = null;
    @Nullable
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
    private static final int mTier1BitMap = 0b1;
    private static final int mTier2BitMap = 0b10;

    private enum MaterialType {
        NONE,
        SI,
        ALGaaS
    }

    private MaterialType materialType = MaterialType.NONE;
    private int materialAmount = 0;
    private static final IStructureDefinition<MTECZPuller> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECZPuller>builder()
        // spotless:off
        .addShape(
            tier1,
            transpose(
                new String[][] {
                    { "     ", "     ", "  E  ", "     ", "     " },
                    { "     ", "     ", "  E  ", "     ", "     " },
                    { "     ", "  E  ", " EBE ", "  E  ", "     " },
                    { "     ", "  E  ", " E E ", "  E  ", "     " },
                    { "  A  ", " AAA ", "AA AA", " AAA ", "  A  " },
                    { " DAD ", "DCCCD", "AC CA", "DCCCD", " DAD " },
                    { " DAD ", "DCCCD", "AC CA", "DCCCD", " DAD " },
                    { " DAD ", "DCCCD", "AC CA", "DCCCD", " DAD " },
                    { " A~A ", "AAAAA", "AAAAA", "AAAAA", " AAA " }}))
        .addShape(
            tier2,
            transpose(
                new String[][] {
                    { "           ", "           ", "           ", "           ", "           ", "     F     ", "           ", "           ", "           ", "           ", "           " },
                    { "           ", "           ", "           ", "           ", "           ", "     F     ", "           ", "           ", "           ", "           ", "           " },
                    { "           ", "           ", "           ", "           ", "    G G    ", "     F     ", "    G G    ", "           ", "           ", "           ", "           " },
                    { "           ", "           ", "  HGGHGGH  ", "  GG   GG  ", "  G G G G  ", "  H  F  H  ", "  G G G G  ", "  GG   GG  ", "  HGGHGGH  ", "           ", "           " },
                    { "           ", " HG     GH ", " G  CCC  G ", "   CJJJC   ", "  CJ G JC  ", "  CJGFGJC  ", "  CJ G JC  ", "   CJJJC   ", " G  CCC  G ", " HG     GH ", "           " },
                    { "HG       GH", "G         G", "    CCC    ", "   CHHHC   ", "  CHHHHHC  ", "  CHH HHC  ", "  CHHHHHC  ", "   CHHHC   ", "    CCC    ", "G         G", "HG       GH" },
                    { "G         G", "           ", "    FFF    ", "   FHHHF   ", "  FH   HF  ", "  FH   HF  ", "  FH   HF  ", "   FHHHF   ", "    FFF    ", "           ", "G         G" },
                    { "G         G", "           ", "    CCC    ", "   CHHHC   ", "  CH   HC  ", "  CH   HC  ", "  CH   HC  ", "   CHHHC   ", "    CCC    ", "           ", "G         G" },
                    { "G         G", "           ", "    CCC    ", "   CHHHC   ", "  CH   HC  ", "  CH   HC  ", "  CH   HC  ", "   CHHHC   ", "    CCC    ", "           ", "G         G" },
                    { "G         G", "           ", "    HHH    ", "   GHHHG   ", "  HH   HH  ", "  HH   HH  ", "  HH   HH  ", "   GHHHG   ", "    HHH    ", "           ", "G         G" },
                    { "GG       GG", "G         G", "    H~H    ", "   GHHHG   ", "  HHHHHHH  ", "  HHHHHHH  ", "  HHHHHHH  ", "   GHHHG   ", "    HHH    ", "G         G", "GG       GG" } }

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
        .build();
    private byte mSpecialTier = 0;

    public MTECZPuller(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECZPuller(String aName) {
        super(aName);
    }

    private final ArrayList<MTEHatchInputBus> seedBus = new ArrayList<>();

    private boolean addSeedBus(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            if (aTileEntity.getMetaTileEntity() instanceof MTEHatchInputBus bus) {
                bus.updateTexture(aBaseCasingIndex);
                seedBus.add(bus);
                return true;
            }
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
            if ((aValue & mTier1BitMap) == mTier1BitMap) {
                mSpecialTier = 1;
            }

            if ((aValue & mTier2BitMap) == mTier2BitMap) {
                mSpecialTier = 2;
            }
        }
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.czRecipes;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mSpecialTier", mSpecialTier);
        aNBT.setString("czMaterialType", materialType.name());
        aNBT.setInteger("czMaterialAmount", materialAmount);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mSeparate")) {
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        mSpecialTier = aNBT.getByte("mSpecialTier");
        try {
            materialType = MaterialType.valueOf(aNBT.getString("czMaterialType"));
        } catch (IllegalArgumentException ignored) {
            materialType = MaterialType.NONE;
        }

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
        mHeatingCapacity = 0;
        mEnergyHatches.clear();
        mExoticEnergyHatches.clear();
        seedBus.clear();

        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(tier1, 2, 8, 0)) return false;
        if (!checkPiece(tier2, 5, 10, 2)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

        if (seedBus.size() != 1) return false;

        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            if (mExoticEnergyHatches.size() > 1) return false;
        }
        if (!mEnergyHatches.isEmpty()) {
            if (mEnergyHatches.size() > 2) return false;

            byte tier_of_hatch = mEnergyHatches.get(0).mTier;
            for (MTEHatchEnergy energyHatch : mEnergyHatches) {
                if (energyHatch.mTier != tier_of_hatch) {
                    return false;
                }
            }
        }
        if (mMaintenanceHatches.size() > 1) return false;

        mHeatingCapacity = (int) getCoilLevel().getHeat();

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
            buildPiece(tier1, stackSize, hintsOnly, 2, 8, 0);
        }
        if (mSpecialTier == 2) {
            buildPiece(tier2, stackSize, hintsOnly, 5, 10, 2);
        }

    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;
        if (mMachine) return -1;
        if (mSpecialTier == 1) {
            built += survivialBuildPiece(tier1, stackSize, 2, 8, 0, elementBudget, env, false, true);
        }
        if (mSpecialTier == 2) {
            built += survivialBuildPiece(tier2, stackSize, 5, 10, 2, elementBudget, env, false, true);
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
            data += mTier1BitMap;
        }
        if (mSpecialTier == 2) {
            data += mTier2BitMap;
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

    // @Override
    // public Pos2d getStructureUpdateButtonPos() {
    // return new Pos2d(80, 91);
    // }

    // @Override
    // public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
    // super.addUIWidgets(builder, buildContext);
    // buildContext.addSyncedWindow(11, this::createConfigurationWindow);
    // builder.widget(
    // new ButtonWidget().setOnClick(
    // (clickData, widget) -> {
    // if (!widget.isClient()) widget.getContext()
    // .openSyncedWindow(11);
    // })
    // .setSize(16, 16)
    // .setBackground(() -> {
    // List<UITexture> ret = new ArrayList<>();
    // ret.add(GTUITextures.BUTTON_STANDARD);
    // ret.add(GTUITextures.OVERLAY_BUTTON_CYCLIC);
    // return ret.toArray(new IDrawable[0]);
    // })
    // .addTooltip("Configuration Menu")
    // .setPos(174, 130))
    // ;
    // }

    // protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
    // ModularWindow.Builder builder = ModularWindow.builder(200, 160);
    // builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
    // builder.setGuiTint(getGUIColorization());
    // builder.widget(
    // new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
    // .setPos(5, 5)
    // .setSize(16, 16))
    // .widget(new TextWidget("Configuration Menu").setPos(25, 9))
    // .widget(
    // ButtonWidget.closeWindowButton(true)
    // .setPos(185, 3))
    // .widget(new TextWidget("CZ Puller tier").setPos(25, 25))
    // .widget(
    // new ButtonWidget()
    // .setPos(10, 60)
    // .setSize(20, 18)
    // .setOnClick((clickData, widget) -> {
    // if (!widget.isClient()) {
    // System.out.println("Кнопка '1' нажата");
    // }
    // })
    // .setBackground(() -> new IDrawable[] { GTUITextures.BUTTON_STANDARD })
    // .addTooltip("Button 1")
    // );
    // return builder.build();
    // }
}
