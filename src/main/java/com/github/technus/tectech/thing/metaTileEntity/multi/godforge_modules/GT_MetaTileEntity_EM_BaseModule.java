package com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.processInitialSettings;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_StructureUtility;

public class GT_MetaTileEntity_EM_BaseModule extends GT_MetaTileEntity_MultiblockBase_EM {

    protected final int tier = getTier();
    protected boolean isConnected = false;
    protected boolean isUpgrade83Unlocked = false;
    protected UUID userUUID;
    protected int machineHeat = 0;
    protected int maximumParallel = 0;
    protected float processingSpeedBonus = 0;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_EM_BaseModule> STRUCTURE_DEFINITION = StructureDefinition
            .<GT_MetaTileEntity_EM_BaseModule>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    StructureUtility.transpose(
                            new String[][] { { "H", "H" }, { "~", "H" }, { "H", "H" }, { "H", "H" }, { "H", "H" } }))
            .addElement(
                    'H',
                    GT_StructureUtility.ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_BaseModule::addClassicToMachineList,
                            texturePage << 7,
                            1,
                            TT_Container_Casings.sBlockCasingsBA0,
                            12))
            .build();

    public GT_MetaTileEntity_EM_BaseModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_BaseModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_BaseModule(mName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && isConnected) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            if (aTick % 400 == 0) fixAllIssues();
            if (mEfficiency < 0) mEfficiency = 0;
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public boolean drainEnergyInput(long EUtEffective, long Amperes) {
        long EU_drain = EUtEffective * Amperes;
        if (EU_drain == 0L) {
            return true;
        } else {
            if (EU_drain > 0L) {
                EU_drain = -EU_drain;
            }
            return addEUToGlobalEnergyMap(userUUID, EU_drain);
        }
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }

    public void setHeat(Integer heat) {
        machineHeat = heat;
    }

    public Integer getHeat() {
        return machineHeat;
    }

    public void setMaxParallel(Integer parallel) {
        maximumParallel = parallel;
    }

    public Integer getMaxParallel() {
        return maximumParallel;
    }

    public void setSpeedBonus(Float bonus) {
        processingSpeedBonus = bonus;
    }

    public Float getSpeedBonus() {
        return processingSpeedBonus;
    }

    public void setUpgrade83(Boolean upgrade) {
        isUpgrade83Unlocked = upgrade;
    }

    protected void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public long getMaxInputVoltage() {
        return gregtech.api.enums.GT_Values.V[tier];
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 0, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        fixAllIssues();
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, 0, 1, 0);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            userUUID = processInitialSettings(aBaseMetaTileEntity);
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK).setPos(4, 4).setSize(190, 85));
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1);
        builder.widget(inventorySlot.setPos(173, 167).setBackground(GT_UITextures.SLOT_DARK_GRAY));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, inventorySlot);
        builder.widget(screenElements);

        builder.widget(createPowerSwitchButton(builder)).widget(createVoidExcessButton(builder))
                .widget(createInputSeparationButton(builder)).widget(createBatchModeButton(builder))
                .widget(createLockToSingleRecipeButton(builder));
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {}

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setBoolean("isConnected", isConnected);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        isConnected = NBT.getBoolean("isConnected");
        super.loadNBTData(NBT);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(texturePage << 7),
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                                    : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(texturePage << 7) };
    }
}
