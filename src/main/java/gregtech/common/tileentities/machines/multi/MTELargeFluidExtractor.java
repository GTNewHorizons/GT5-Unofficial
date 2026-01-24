package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.gui.modularui.multiblock.MTELargeFluidExtractorGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTELargeFluidExtractor extends MTEExtendedPowerMultiBlockBase<MTELargeFluidExtractor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int CASING_INDEX = 48; // Robust Tungstensteel Machine Casing
    private static final int BASE_CASING_COUNT = 24 + 24 + 9;
    private static final int MAX_HATCHES_ALLOWED = 16;

    private static final double BASE_SPEED_BONUS = 1.5;
    private static final double BASE_EU_MULTIPLIER = 0.8;

    private static final double SPEED_PER_COIL = 0.1;
    private static final int PARALLELS_PER_SOLENOID = 8;
    private static final double HEATING_COIL_EU_MULTIPLIER = 0.9;

    // spotless:off
    private static final IStructureDefinition<MTELargeFluidExtractor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeFluidExtractor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    {"     ", " ccc ", " ccc ", " ccc ", "     "},
                    {"     ", " f f ", "  s  ", " f f ", "     "},
                    {"     ", " f f ", "  s  ", " f f ", "     "},
                    {"     ", " f f ", "  s  ", " f f ", "     "},
                    {"ccccc", "ccccc", "ccscc", "ccccc", "ccccc"},
                    {"fgggf", "ghhhg", "ghshg", "ghhhg", "fgggf"},
                    {"fgggf", "ghhhg", "ghshg", "ghhhg", "fgggf"},
                    {"fgggf", "ghhhg", "ghshg", "ghhhg", "fgggf"},
                    {"cc~cc", "ccccc", "ccccc", "ccccc", "ccccc"},
                }))
        .addElement('c',
            buildHatchAdder(MTELargeFluidExtractor.class)
                .atLeast(InputBus, OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(CASING_INDEX) // Robust Tungstensteel Machine Casing
                .hint(1)
                .buildAndChain(
                    onElementPass(
                        MTELargeFluidExtractor::onCasingAdded,
                        ofBlock(GregTechAPI.sBlockCasings4, 0))) // Robust Tungstensteel Machine Casing
        )
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement(
            'h',
            GTStructureChannels.HEATING_COIL.use(
                activeCoils(
                    ofCoil(
                        MTELargeFluidExtractor::setCoilLevel,
                        MTELargeFluidExtractor::getCoilLevel)))
        )
        .addElement(
            's',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTELargeFluidExtractor::setSolenoidLevel,
                    MTELargeFluidExtractor::getSolenoidLevel))
        )
        .addElement(
            'f',
            ofFrame(Materials.BlackSteel)
        )
        .build();
    // spotless:on

    private int glassTier = -1;
    @Nullable
    private HeatingCoilLevel coilLevel = null;
    @Nullable
    private Byte solenoidLevel = null;
    private int casingAmount;
    private boolean structureBadGlassTier = false;

    public boolean getStructureBadCasingCount() {
        return structureBadCasingCount;
    }

    public boolean getStructureBadGlassTier() {
        return structureBadGlassTier;
    }

    public int getGlassTier() {
        return glassTier;
    }

    public int getCasingAmount() {
        return casingAmount;
    }

    private boolean structureBadCasingCount = false;

    public MTELargeFluidExtractor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeFluidExtractor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTELargeFluidExtractor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        casingAmount = 0;
        structureBadGlassTier = false;
        structureBadCasingCount = false;
        glassTier = -1;
        coilLevel = null;
        solenoidLevel = null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 8, 0)) {
            return false;
        }

        if (casingAmount < (BASE_CASING_COUNT - MAX_HATCHES_ALLOWED)) {
            structureBadCasingCount = true;
        }

        for (var energyHatch : mEnergyHatches) {
            if (energyHatch.getBaseMetaTileEntity() == null) {
                continue;
            }

            if (glassTier < VoltageIndex.UEV && energyHatch.getTierForStructure() > glassTier) {
                structureBadGlassTier = true;
                break;
            }
        }

        return !structureBadGlassTier && !structureBadCasingCount;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 8, 0, elementBudget, env, false, true);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setEuModifier(getEUMultiplier());
                setSpeedBonus(1.0f / getSpeedBonus());
                return super.process();
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFluidExtractor(this.mName);
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    private HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    private void setCoilLevel(HeatingCoilLevel level) {
        coilLevel = level;
    }

    private Byte getSolenoidLevel() {
        return solenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        solenoidLevel = level;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) {
                return new ITexture[] { getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCALargeFluidExtractorActive)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCALargeFluidExtractorActiveGlow)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                return new ITexture[] { getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCALargeFluidExtractor)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCALargeFluidExtractorGlow)
                        .extFacing()
                        .glow()
                        .build() };
            }
        }
        return new ITexture[] { getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        // spotless:off
        tt.addMachineType("Fluid Extractor, LFE")
            .addDynamicParallelInfo(PARALLELS_PER_SOLENOID, TooltipTier.SOLENOID)
            .addStaticSpeedInfo((float) BASE_SPEED_BONUS)
            .addStaticEuEffInfo((float) BASE_EU_MULTIPLIER)
            .addInfo(String.format(
                "Every coil tier gives a %s speed bonus and a %s EU/t discount (multiplicative)",
                TooltipHelper.speedText("+") + TooltipHelper.speedText((float) SPEED_PER_COIL),
                TooltipHelper.effText((float) (1-HEATING_COIL_EU_MULTIPLIER))
            ))
            .addInfo(String.format(
                "The EU multiplier is %s%.2f * (%.2f ^ Heating Coil Tier)%s, prior to overclocks",
                EnumChatFormatting.ITALIC,
                BASE_EU_MULTIPLIER,
                HEATING_COIL_EU_MULTIPLIER,
                EnumChatFormatting.GRAY
            ))
            .addInfo("The energy hatch tier is limited by the glass tier. UEV glass unlocks all tiers")
            .beginStructureBlock(5, 9, 5, false)
            .addController("Front Center (Bottom Layer)")
            .addCasingInfoMin("Robust Tungstensteel Machine Casing", BASE_CASING_COUNT - MAX_HATCHES_ALLOWED, false)
            .addCasingInfoExactly("Any Tiered Glass", 9 * 4, true)
            .addCasingInfoExactly("Solenoid Superconducting Coil", 7, true)
            .addCasingInfoExactly("Heating Coils", 8 * 3, true)
            .addCasingInfoExactly("Black Steel Frame Box", 3 * 8, false)
            .addInputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addOutputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addOutputHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addEnergyHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addSubChannelUsage(GTStructureChannels.SOLENOID)
            .toolTipFinisher();
        // spotless:on

        return tt;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTELargeFluidExtractorGui(this);
    }

    public int getHatchTier() {
        int hatchTier = 0;

        for (var hatch : mEnergyHatches) {
            if (hatch.mTier > hatchTier) hatchTier = hatch.mTier;
        }
        return hatchTier;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidExtractionRecipes;
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            StatCollector.translateToLocalFormatted("Max Parallels: %s%d%s", YELLOW, getMaxParallelRecipes(), RESET));
        info.add(
            StatCollector.translateToLocalFormatted(
                "Heating Coil Speed Bonus: +%s%.0f%s %%",
                YELLOW,
                getCoilSpeedBonus() * 100,
                RESET));
        info.add(
            StatCollector.translateToLocalFormatted(
                "Total Speed Multiplier: %s%.0f%s %%",
                YELLOW,
                getSpeedBonus() * 100,
                RESET));
        info.add(
            StatCollector.translateToLocalFormatted(
                "Total EU/t Multiplier: %s%.0f%s %%",
                YELLOW,
                getEUMultiplier() * 100,
                RESET));
    }

    @Override
    public int getMaxParallelRecipes() {
        return Math.max(1, solenoidLevel == null ? 0 : (PARALLELS_PER_SOLENOID * solenoidLevel));
    }

    public float getCoilSpeedBonus() {
        return (float) ((coilLevel == null ? 0 : SPEED_PER_COIL * coilLevel.getTier()));
    }

    public float getSpeedBonus() {
        return (float) (BASE_SPEED_BONUS + getCoilSpeedBonus());
    }

    public float getEUMultiplier() {
        double heatingBonus = (coilLevel == null ? 0
            : GTUtility.powInt(HEATING_COIL_EU_MULTIPLIER, coilLevel.getTier()));

        return (float) (BASE_EU_MULTIPLIER * heatingBonus);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }
}
