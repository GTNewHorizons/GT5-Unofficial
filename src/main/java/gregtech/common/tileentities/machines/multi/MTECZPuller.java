package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
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

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.misc.GTStructureChannels;

public class MTECZPuller extends MTEEnhancedMultiBlockBase<MTECZPuller> implements ISurvivalConstructable {

    private int mHeatingCapacity = 0;
    private static final int CASING_INDEX = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0);
    private HeatingCoilLevel mCoilLevel = null;
    @Nullable
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
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
                    { " A~A ", "AAAAA", "AAAAA", "AAAAA", " AAA " } }))
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
        .addElement('E',
            buildHatchAdder(MTECZPuller.class).atLeast(InputBus)
                .casingIndex(((BlockCasings2)GregTechAPI.sBlockCasings2).getTextureIndex(0))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings2,0)))
        .build();

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
            .toolTipFinisher("");
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
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
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IStructureDefinition<MTECZPuller> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock();
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

        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(tier1, 2, 8, 0)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

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
        buildPiece(tier1, stackSize, hintsOnly, 2, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(tier1, stackSize, 2, 8, 0, elementBudget, env, false, true);
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
}
