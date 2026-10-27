package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.TranslatableStructureError;
import gregtech.api.structure.error.TranslatableText;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialChemicalBath extends MTEExtendedPowerMultiBlockBase<MTEIndustrialChemicalBath>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int casingAmount;
    private int copperTier = TIER_NOT_SET;
    private int zincTier = TIER_NOT_SET;
    private boolean needsWaterFill = false;
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int TIER_NOT_SET = -1;
    private static final int TIER_BASE = 0;
    private static final int TIER_UPGRADED = 1;

    private static final String[][] structure = new String[][] { { "AABAA", "AEEEA", "AE~EA", "AEEEA" },
        { "  B  ", "EFDFE", "EFDFE", "EEEEE" }, { "     ", "EFFFE", "EFFFE", "EEEEE" },
        { "  B  ", "EFCFE", "EFCFE", "EEEEE" }, { "AABAA", "AEEEA", "AEEEA", "AEEEA" } };

    // Lazy allocation since ofFrame requires late-registering GTPP MaterialsAlloy
    private static IStructureDefinition<MTEIndustrialChemicalBath> STRUCTURE_DEFINITION = null;

    public MTEIndustrialChemicalBath(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialChemicalBath(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialChemicalBath(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Bath, ICB")
            .addBulkMachineInfo(4, 5f, 1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo(
                TooltipHelper.NAME_COLOR + "Octiron Blocks and Six-Phased Copper Blocks"
                    + EnumChatFormatting.GRAY
                    + " allow a single "
                    + TooltipHelper.HATCH_COLOR
                    + "Multi-Amp Energy Hatch")
            .beginStructureBlock(4, 5, 5, false)
            .addController("Front center, 2nd layer")
            .addCasing("30-39", "Wash Plant Casing", false)
            .addCasing("20", "Watertight Steel Frame Box", false)
            .addCasing("14", "Water", false)
            .addCasing("4", "Chemically Inert Machine Casing", false)
            .addCasing("2", "Block of Zinc or Octiron", false)
            .addCasing("2", "Block of Copper or Six-Phased Copper", false)
            .addEnergyHatch("1+", "Any wash plant casing", 1)
            .addMaintenanceHatch("1", "Any wash plant casing", 1)
            .addMufflerHatch("1", "Any wash plant casing", 1)
            .addInputBus("0+", "Any wash plant casing", 1)
            .addInputHatch("1+", "Any wash plant casing", 1)
            .addOutputAny("1+", "Any wash plant casing", 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.WaterCost"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialChemicalBath> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialChemicalBath>builder()
                .addShape(STRUCTURE_PIECE_MAIN, structure)
                .addElement('A', ofFrame(MaterialsAlloy.AQUATIC_STEEL))
                .addElement('B', Casings.ChemicallyInertMachineCasing.asElement())
                .addElement('C', copperOrSixPhasedCopper())
                .addElement('D', zincOrOctiron())
                .addElement(
                    'E',
                    buildHatchAdder(MTEIndustrialChemicalBath.class)
                        .atLeast(
                            InputBus,
                            InputHatch,
                            OutputHatch,
                            OutputBus,
                            Maintenance,
                            Energy.or(MultiAmpEnergy),
                            Muffler)
                        .casingIndex(114) // WashPlantCasing
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.WashPlantCasing.asElement())))
                .addElement('F', ofChain(ofAnyWater(false), isAir()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private static IStructureElement<MTEIndustrialChemicalBath> copperOrSixPhasedCopper() {
        ITierConverter<Integer> tierConverter = (block, meta) -> {
            if (block == GregTechAPI.sBlockMetal2 && meta == 7) return TIER_BASE;
            if (block == GregTechAPI.sBlockMetal9 && meta == 11) return TIER_UPGRADED;
            return null;
        };
        return ofBlocksTiered(
            tierConverter,
            ImmutableList.of(Pair.of(GregTechAPI.sBlockMetal2, 7), Pair.of(GregTechAPI.sBlockMetal9, 11)),
            TIER_NOT_SET,
            (x, tier) -> x.copperTier = tier,
            x -> x.copperTier);
    }

    private static IStructureElement<MTEIndustrialChemicalBath> zincOrOctiron() {
        return lazy(() -> {
            ItemStack octiron = MaterialsAlloy.OCTIRON.getBlock(1);
            Block octironBlock = Block.getBlockFromItem(octiron.getItem());
            int octironMeta = octiron.getItemDamage();
            ITierConverter<Integer> tierConverter = (block, meta) -> {
                if (block == GregTechAPI.sBlockMetal8 && meta == 6) return TIER_BASE;
                if (block == octironBlock && meta == octironMeta) return TIER_UPGRADED;
                return null;
            };
            return ofBlocksTiered(
                tierConverter,
                ImmutableList.of(Pair.of(GregTechAPI.sBlockMetal8, 6), Pair.of(octironBlock, octironMeta)),
                TIER_NOT_SET,
                (x, tier) -> x.zincTier = tier,
                x -> x.zincTier);
        });
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        needsWaterFill = false;
        casingAmount = 0;
        copperTier = TIER_NOT_SET;
        zincTier = TIER_NOT_SET;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) {
            needsWaterFill = GTStructureUtility.hasWaterAtStructurePosition(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'F');
            return;
        }
        checkCasingMin(errors, casingAmount, 30);
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) errors.add(StructureErrorRegistry.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER);
            if (mExoticEnergyHatches.size() != 1) errors.add(StructureErrorRegistry.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER);
            if (copperTier != TIER_UPGRADED || zincTier != TIER_UPGRADED) {
                errors.add(
                    new TranslatableStructureError(
                        TranslatableText.literal("Need Octiron and Six-Phased Copper Blocks for Multi-Amp")));
            }
        } else {
            checkHasEnergyHatch(errors);
        }
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputHatch(errors);
        checkHasAnyOutput(errors);
        if (!errors.isEmpty()) return;
        needsWaterFill = true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            TexturesGtBlock.oMCDIndustrialWashPlant,
            TexturesGtBlock.oMCDIndustrialWashPlantGlow,
            TexturesGtBlock.oMCDIndustrialWashPlantActive,
            TexturesGtBlock.oMCDIndustrialWashPlantActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return TextureFactory.of(ModBlocks.blockCasings2Misc, 4);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.chemicalBathRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1.0D / 5.0D)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsWaterFill && aTick % 20 == 0) {
            if (GTStructureUtility.fillStructureWithWater(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'F')) {
                needsWaterFill = false;
            }
        }
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_ORE_WASHER_PLANT_LOOP;
    }
}
