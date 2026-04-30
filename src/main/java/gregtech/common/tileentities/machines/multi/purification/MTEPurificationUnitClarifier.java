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

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEPurificationUnitClarifier extends MTEPurificationUnitBase<MTEPurificationUnitClarifier>
    implements ISurvivalConstructable {

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
        .addElement('W', ofChain(isAir(), ofAnyWater(false)))
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
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + formatNumber(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Must be linked to a Purification Plant using a data stick to work")
            .addSeparator()
            .addInfo("Requires a filter made of Activated Carbon to work")
            .addInfo(
                "Every cycle, has a " + EnumChatFormatting.RED
                    + formatNumber(FILTER_DAMAGE_RATE)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " chance to destroy the filter")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "The first step to acquiring purified water is to filter out macroscopic contaminants through the")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "use of large physical filters. As more contaminants are captured, the efficacy of the filter")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "decreases so continual replacements must be supplied to maintain full function of the Clarifier.")
            .beginStructureBlock(11, 4, 11, false)
            .addController("Front center")
            .addCasingInfoRangeColored(
                "Reinforced Sterile Water Plant Casing",
                EnumChatFormatting.GRAY,
                123,
                131,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Filter Machine Casing",
                EnumChatFormatting.GRAY,
                21,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Iridium Frame Box",
                EnumChatFormatting.GRAY,
                12,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Damascus Steel Frame Box",
                EnumChatFormatting.GRAY,
                12,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored("PTFE Pipe Casing", EnumChatFormatting.GRAY, 3, EnumChatFormatting.GOLD, false)
            .addInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addOutputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
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
        int built = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
        if (built == -1) {
            GTUtility.sendChatTrans(env.getActor(), "GT5U.chat.auto_place.done.water");
            return 0;
        }
        return built;
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
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX) };
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        boolean valid = super.checkMachine(aBaseMetaTileEntity, aStack);
        if (valid) needsWaterFill = true;
        return valid;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsWaterFill && mMachine && aTick % 20 == 0) {
            World world = aBaseMetaTileEntity.getWorld();
            boolean allFilled = true;
            int controllerX = aBaseMetaTileEntity.getXCoord();
            int controllerY = aBaseMetaTileEntity.getYCoord();
            int controllerZ = aBaseMetaTileEntity.getZCoord();

            for (int sliceZ = 0; sliceZ < structure.length; sliceZ++) {
                String[] layers = structure[sliceZ];
                for (int layerY = 0; layerY < layers.length; layerY++) {
                    String row = layers[layerY];
                    for (int charX = 0; charX < row.length(); charX++) {
                        if (row.charAt(charX) != 'W') continue;

                        int[] abc = new int[] { charX - STRUCTURE_X_OFFSET, layerY - STRUCTURE_Y_OFFSET,
                            sliceZ - STRUCTURE_Z_OFFSET };
                        int[] xyz = new int[] { 0, 0, 0 };
                        this.getExtendedFacing()
                            .getWorldOffset(abc, xyz);
                        int wx = controllerX + xyz[0];
                        int wy = controllerY + xyz[1];
                        int wz = controllerZ + xyz[2];
                        boolean isReplaceable = GTUtility.canReplaceBlockWithWater(world, wx, wy, wz);
                        if (isReplaceable) {
                            world.setBlock(wx, wy, wz, Blocks.water, 0, 3);
                        } else {
                            allFilled = false;
                        }
                    }
                }
            }

            if (allFilled) needsWaterFill = false;
        }
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_PURIFICATIONPLANT_LOOP;
    }
}
