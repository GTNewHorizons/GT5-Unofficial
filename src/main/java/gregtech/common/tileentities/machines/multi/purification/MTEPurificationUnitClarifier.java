package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEPurificationUnitClarifier extends MTEPurificationUnitBase<MTEPurificationUnitClarifier>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int STRUCTURE_X_OFFSET = 5;
    private static final int STRUCTURE_Y_OFFSET = 2;
    private static final int STRUCTURE_Z_OFFSET = 1;

    // Chance that the filter is damaged every cycle.
    public static final float FILTER_DAMAGE_RATE = 20.0f;

    private boolean needsWaterFill = false;

    private static final int CASING_TEXTURE_INDEX = getTextureIndex(GregTechAPI.sBlockCasings9, 5);

    private static final String[][] structure =
        // spotless:off
        new String[][] {
            { "           ", "           ", "           ", "           " },
            { "           ", "   AAAAA   ", "   AH~HA   ", "   AAAAA   " },
            { "           ", "  A     A  ", "  AWWWWWA  ", "  AAAAAAA  " },
            { "           ", " A       A ", " AWWWWWWWA ", " AAAAAAAAA " },
            { "           ", "A         A", "AWWWCCCWWWA", "AAAAFFFAAAA" },
            { "    DDD    ", "A         A", "HWWCWWWCWWH", "AAAFFFFFAAA" },
            { "DDDDDBD    ", "A    B    A", "AWWCWBWCWWA", "AAAFFFFFAAA" },
            { "    DDD    ", "A         A", "HWWCWWWCWWH", "AAAFFFFFAAA" },
            { "           ", "A         A", "AWWWCCCWWWA", "AAAAFFFAAAA" },
            { "           ", " A       A ", " AWWWWWWWA ", " AAAAAAAAA " },
            { "           ", "  A     A  ", "  AWWWWWA  ", "  AAAAAAA  " },
            { "           ", "   AAAAA   ", "   AHAHA   ", "   AAAAA   " } };
    // spotless:on

    private static final IStructureDefinition<MTEPurificationUnitClarifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPurificationUnitClarifier>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Hatches
        .addElement(
            'H',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitClarifier>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .casingIndex(CASING_TEXTURE_INDEX)
                        .hint(1)
                        .build()),
                // Reinforced Sterile Water Plant Casing
                ofBlock(GregTechAPI.sBlockCasings9, 5)))
        // Reinforced Sterile Water Plant Casing
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings9, 5))
        // PTFE pipe casing
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement('C', ofFrame(Materials.Iridium))
        .addElement('D', ofFrame(Materials.DamascusSteel))
        .addElement('W', ofChain(ofAnyWater(false), isAir()))
        // Filter machine casing
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings3, 11))
        .build();

    public MTEPurificationUnitClarifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPurificationUnitClarifier(String aName) {
        super(aName);
    }

    @Override
    public int getWaterTier() {
        return 1;
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_LuV;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationClarifierRecipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult overrideRecipeCheck() {
        // Clarifier needs to check item inputs from recipe as well to find filter item
        return findRecipeForInputs(
            this.storedFluids.toArray(new FluidStack[] {}),
            this.getStoredInputs()
                .toArray(new ItemStack[] {}));
    }

    @Override
    public void depleteRecipeInputs() {
        super.depleteRecipeInputs();

        // Now do random roll to determine if the filter should be destroyed
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int roll = random.nextInt(1, 101);
        if (roll < FILTER_DAMAGE_RATE) {
            this.depleteInput(this.currentRecipe.mInputs[0]);
        }
    }

    @Override
    public IStructureDefinition<MTEPurificationUnitClarifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // Rotated sifter not allowed, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.purif_unit")
            .addInfo("gt.pu_clarifier.tips", formatNumber(getWaterTier()), formatNumber(FILTER_DAMAGE_RATE))
            .beginStructureBlock(11, 11, 4, false)
            .addController("Front center, 2nd layer")
            .addCasing("123-128", "Reinforced Sterile Water Plant Casing", false)
            .addCasing("21", "Filter Machine Casing", false)
            .addCasing("12", "Iridium Frame Box", false)
            .addCasing("12", "Damascus Steel Frame Box", false)
            .addCasing("3", "PTFE Pipe Casing", false)
            .addInputBus("1+", "Any center side casing", 1)
            .addInputHatch("1+", "Any center side casing", 1)
            .addOutputBus("0+", "Any center side casing", 1)
            .addOutputHatch("1+", "Any center side casing", 1)
            .addStructureInfo("")
            .addStructureFooter(gregtech.api.util.GTUtility.nestParams("GT5U.MBTT.Structure.WaterFree"))
            .addStructureFooter(gregtech.api.util.GTUtility.nestParams("GT5U.MBTT.Structure.DataStick.Waterline"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPurificationUnitClarifier(this.mName);
    }

    private List<IHatchElement<? super MTEPurificationUnitClarifier>> getAllowedHatches() {
        return ImmutableList.of(InputBus, InputHatch, OutputBus, OutputHatch);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            sideDirection,
            facingDirection,
            active,
            OVERLAY_FRONT_DISTILLATION_TOWER,
            OVERLAY_FRONT_DISTILLATION_TOWER_GLOW,
            OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE,
            OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        needsWaterFill = false;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET, errors)) {
            needsWaterFill = GTStructureUtility.hasWaterAtStructurePosition(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                STRUCTURE_X_OFFSET,
                STRUCTURE_Y_OFFSET,
                STRUCTURE_Z_OFFSET,
                'W');
            return;
        }
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
        if (!errors.isEmpty()) return;
        needsWaterFill = true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsWaterFill && aTick % 20 == 0) {
            if (GTStructureUtility.fillStructureWithWater(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                STRUCTURE_X_OFFSET,
                STRUCTURE_Y_OFFSET,
                STRUCTURE_Z_OFFSET,
                'W')) {
                needsWaterFill = false;
            }
        }
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_PURIFICATIONPLANT_LOOP;
    }
}
