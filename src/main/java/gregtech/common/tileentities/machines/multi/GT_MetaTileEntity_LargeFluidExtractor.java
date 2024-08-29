package gregtech.common.tileentities.machines.multi;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.ofGlassTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_StructureUtility.ofSolenoidCoil;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_LargeFluidExtractor
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_LargeFluidExtractor>
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
    private static final IStructureDefinition<GT_MetaTileEntity_LargeFluidExtractor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_LargeFluidExtractor>builder()
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
            buildHatchAdder(GT_MetaTileEntity_LargeFluidExtractor.class)
                .atLeast(InputBus, OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(CASING_INDEX) // Robust Tungstensteel Machine Casing
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_LargeFluidExtractor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 0))) // Robust Tungstensteel Machine Casing
        )
        .addElement(
            'g',
            withChannel(
                "glass",
                ofGlassTiered(
                    (byte) 1, (byte) 127, (byte) 0,
                    GT_MetaTileEntity_LargeFluidExtractor::setGlassTier,
                    GT_MetaTileEntity_LargeFluidExtractor::getGlassTier,
                    2))
        )
        .addElement(
            'h',
            withChannel(
                "coil",
                ofCoil(
                    GT_MetaTileEntity_LargeFluidExtractor::setCoilLevel,
                    GT_MetaTileEntity_LargeFluidExtractor::getCoilLevel))
        )
        .addElement(
            's',
            withChannel(
                "solenoid",
                ofSolenoidCoil(
                    GT_MetaTileEntity_LargeFluidExtractor::setSolenoidLevel,
                    GT_MetaTileEntity_LargeFluidExtractor::getSolenoidLevel))
        )
        .addElement(
            'f',
            ofFrame(Materials.BlackSteel)
        )
        .build();
    // spotless:on

    private byte mGlassTier = 0;
    @Nullable
    private HeatingCoilLevel mCoilLevel = null;
    @Nullable
    private Byte mSolenoidLevel = null;
    private int mCasingAmount;
    private boolean mStructureBadGlassTier = false, mStructureBadCasingCount = false;

    public GT_MetaTileEntity_LargeFluidExtractor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeFluidExtractor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_LargeFluidExtractor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        mCasingAmount = 0;
        mStructureBadGlassTier = false;
        mStructureBadCasingCount = false;
        mGlassTier = 0;
        mCoilLevel = null;
        mSolenoidLevel = null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 8, 0)) {
            return false;
        }

        if (mCasingAmount < (BASE_CASING_COUNT - MAX_HATCHES_ALLOWED)) {
            mStructureBadCasingCount = true;
        }

        for (var energyHatch : mEnergyHatches) {
            if (energyHatch.getBaseMetaTileEntity() == null) {
                continue;
            }

            if (mGlassTier < 10 && energyHatch.getTierForStructure() > mGlassTier) {
                mStructureBadGlassTier = true;
            }
        }

        return !mStructureBadGlassTier && !mStructureBadCasingCount;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 8, 0, elementBudget, env, false, true);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        super.setProcessingLogicPower(logic);
        logic.setAvailableAmperage(mEnergyHatches.size());
        logic.setEuModifier((float) (getEUMultiplier()));
        logic.setMaxParallel(getParallels());
        logic.setSpeedBonus(1.0f / (float) (getSpeedBonus()));
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeFluidExtractor(this.mName);
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private byte getGlassTier() {
        return mGlassTier;
    }

    private void setGlassTier(byte tier) {
        mGlassTier = tier;
    }

    private HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    private void setCoilLevel(HeatingCoilLevel level) {
        mCoilLevel = level;
    }

    private Byte getSolenoidLevel() {
        return mSolenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        mSolenoidLevel = level;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) {
                return new ITexture[] { getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                    .extFacing()
                    .build() };
            } else {
                return new ITexture[] { getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
            }
        }
        return new ITexture[] { getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();

        // spotless:off
        tt.addMachineType("Fluid Extractor")
            .addInfo("Controller block for the Large Fluid Extractor")
            .addInfo(String.format(
                "%d%% faster than single block machines of the same voltage",
                (int) Math.round((BASE_SPEED_BONUS - 1) * 100)
            ))
            .addInfo(String.format(
                "Only uses %d%% of the EU/t normally required",
                (int) Math.round(BASE_EU_MULTIPLIER * 100)
            ))
            .addInfo(String.format(
                "Every coil tier gives a +%d%% speed bonus and a %d%% EU/t discount (multiplicative)",
                (int) Math.round(SPEED_PER_COIL * 100),
                (int) Math.round((1 - HEATING_COIL_EU_MULTIPLIER) * 100)
            ))
            .addInfo(String.format(
                "Every solenoid tier gives +%d parallels",
                (int) PARALLELS_PER_SOLENOID
            ))
            .addInfo(String.format(
                "The EU multiplier is %s%.2f * (%.2f ^ Heating Coil Tier)%s, prior to overclocks",
                EnumChatFormatting.ITALIC,
                BASE_EU_MULTIPLIER,
                HEATING_COIL_EU_MULTIPLIER,
                EnumChatFormatting.GRAY
            ))
            .addInfo("The energy hatch tier is limited by the glass tier. UEV glass unlocks all tiers.")
            .addSeparator()
            .beginStructureBlock(5, 9, 5, false)
            .addController("Front Center (Bottom Layer)")
            .addCasingInfoMin("Robust Tungstensteel Machine Casing", BASE_CASING_COUNT - MAX_HATCHES_ALLOWED, false)
            .addCasingInfoExactly("Borosilicate Glass (any)", 9 * 4, true)
            .addCasingInfoExactly("Solenoid Superconducting Coil (any)", 7, true)
            .addCasingInfoExactly("Heating Coils (any)", 8 * 3, true)
            .addCasingInfoExactly("Black Steel Frame Box", 3 * 8, false)
            .addInputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addOutputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addOutputHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addEnergyHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
            .toolTipFinisher("GregTech");
        // spotless:on

        return tt;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widgets(TextWidget.dynamicString(() -> {
            if (mStructureBadCasingCount) {
                return String.format(
                    "%sNot enough casings: need %d, but\nhave %d.%s",
                    EnumChatFormatting.DARK_RED,
                    BASE_CASING_COUNT - MAX_HATCHES_ALLOWED,
                    mCasingAmount,
                    RESET);
            }

            if (mStructureBadGlassTier) {
                int hatchTier = 0;

                for (var hatch : mEnergyHatches) {
                    if (hatch.mTier > hatchTier) hatchTier = hatch.mTier;
                }

                return String.format(
                    "%sEnergy hatch tier (%s) is too high\nfor the glass tier (%s).%s",
                    EnumChatFormatting.DARK_RED,
                    VN[hatchTier],
                    VN[mGlassTier],
                    RESET);
            }

            return "";
        })
            .setTextAlignment(Alignment.CenterLeft));
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10_000;
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
    public String[] getInfoData() {
        var data = new ArrayList<String>();

        data.addAll(Arrays.asList(super.getInfoData()));

        data.add(String.format("Max Parallels: %s%d%s", YELLOW, getParallels(), RESET));
        data.add(String.format("Heating Coil Speed Bonus: +%s%.0f%s %%", YELLOW, getCoilSpeedBonus() * 100, RESET));
        data.add(String.format("Total Speed Multiplier: %s%.0f%s %%", YELLOW, getSpeedBonus() * 100, RESET));
        data.add(String.format("Total EU/t Multiplier: %s%.0f%s %%", YELLOW, getEUMultiplier() * 100, RESET));

        return data.toArray(new String[data.size()]);
    }

    public int getParallels() {
        return Math.max(1, mSolenoidLevel == null ? 0 : (PARALLELS_PER_SOLENOID * mSolenoidLevel));
    }

    public float getCoilSpeedBonus() {
        return (float) ((mCoilLevel == null ? 0 : SPEED_PER_COIL * mCoilLevel.getTier()));
    }

    public float getSpeedBonus() {
        return (float) (BASE_SPEED_BONUS + getCoilSpeedBonus());
    }

    public float getEUMultiplier() {
        double heatingBonus = (mCoilLevel == null ? 0 : Math.pow(HEATING_COIL_EU_MULTIPLIER, mCoilLevel.getTier()));

        return (float) (BASE_EU_MULTIPLIER * heatingBonus);
    }
}
