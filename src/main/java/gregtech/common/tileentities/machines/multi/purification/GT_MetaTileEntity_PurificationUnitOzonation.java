package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_PurificationUnitOzonation
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitOzonation>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "         ", "         ", "      A  ", "      A  ", "     AAA ", "     AAA ", "     A A ", "     A A ", "     A A ", "     A~A " },
        { "      A  ", "      A  ", "     A A ", "     A A ", "BBBBA   A", "BDDBA   A", "BBBBA D A", "E   A D A", "E   A D A", "E   AAAAA" },
        { "     AAA ", "     A A ", "    A   A", "    A   A", "BDDBA   A", "O  BA   A", "BBBBA   A", "  C A   A", "  CCA   A", "    AAAAA" },
        { "      A  ", "      A  ", "     A A ", "     A A ", "BBBBA   A", "BDDBA   A", "BBBBA   A", "E   A   A", "E   A   A", "E   AAAAA" },
        { "         ", "         ", "      A  ", "      A  ", "     AAA ", "     AAA ", "     AAA ", "     AAA ", "     AAA ", "     AAA " } };
    // spotless:on

    private static final int MAIN_CASING_INDEX = getTextureIndex(GregTech_API.sBlockCasings9, 10);

    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 9;
    private static final int OFFSET_Z = 0;

    /**
     * If the player inserts more ozone gas than this amount, the multi will explode.
     */
    public static final int MAX_OZONE_GAS_FOR_EXPLOSION = 1000 * (int) Math.pow(2, 10);

    private int casingCount = 0;
    private static final int MIN_CASING = 96;

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitOzonation> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitOzonation>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Inert Filtration Casing
        .addElement(
            'A',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitOzonation>buildHatchAdder()
                        .atLeastList(ImmutableList.of(InputHatch, OutputHatch, OutputBus))
                        .casingIndex(getTextureIndex(GregTech_API.sBlockCasings9, 10))
                        .dot(1)
                        .build()),
                onElementPass(t -> t.casingCount++, ofBlock(GregTech_API.sBlockCasings9, 10))))
        // High Pressure Resistant Casing (possibly placeholder name)
        .addElement('B', ofBlock(GregTech_API.sBlockCasings9, 9))
        // PTFE pipe casing
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 1))
        // Any tinted industrial glass
        .addElement('D', ofBlockAnyMeta(GregTech_API.sBlockTintedGlass))
        .addElement('E', ofFrame(Materials.TungstenSteel))
        // Ozone input hatch
        .addElement(
            'O',
            lazy(
                t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitOzonation>buildHatchAdder()
                    .atLeast(InputHatch)
                    .casingIndex(getTextureIndex(GregTech_API.sBlockCasings9, 9))
                    .dot(2)
                    .buildAndChain(ofBlock(GregTech_API.sBlockCasings9, 9))))
        .build();

    public GT_MetaTileEntity_PurificationUnitOzonation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitOzonation(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitOzonation(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitOzonation> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Controller block for the Ozonation Purification Unit.")
            .addInfo("Must be linked to a Purification Plant using a data stick to work.")
            .addSeparator()
            .addInfo(
                "Will explode if the input hatch contains more than " + EnumChatFormatting.RED
                    + MAX_OZONE_GAS_FOR_EXPLOSION
                    + "L "
                    + EnumChatFormatting.WHITE
                    + "Ozone Gas.")
            .addInfo(
                "Receives a " + EnumChatFormatting.RED
                    + "20%"
                    + EnumChatFormatting.GRAY
                    + " bonus to success chance for every doubling of "
                    + EnumChatFormatting.WHITE
                    + "Ozone Gas.")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "The second step in water purification is ozonation, which involves injecting large quantities of small")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "bubbles of highly reactive ozone gas into the water. This removes trace element contaminants like")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "sulfur, iron and manganese, creating insoluble oxide compounds which are then filtered out.")
            .addInfo(AuthorNotAPenguin)
            .beginStructureBlock(9, 10, 5, false)
            .addCasingInfoRangeColored(
                "Inert Filtration Casing",
                EnumChatFormatting.GRAY,
                MIN_CASING,
                102,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Reactive Gas Containment Casing",
                EnumChatFormatting.GRAY,
                27,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Any Tinted Industrial Glass",
                EnumChatFormatting.GRAY,
                9,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tungstensteel Frame Box",
                EnumChatFormatting.GRAY,
                6,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored("PTFE Pipe Casing", EnumChatFormatting.GRAY, 3, EnumChatFormatting.GOLD, false)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addOutputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addOtherStructurePart("Input Hatch (Ozone)", EnumChatFormatting.GOLD + "1", 2)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationOzonationRecipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // First do recipe checking logic
        CheckRecipeResult result = super.checkProcessing();
        if (!result.wasSuccessful()) return result;
        // Look for ozone, blow up if more than max allowed
        for (FluidStack fluid : this.storedFluids) {
            if (fluid.isFluidEqual(Materials.Ozone.getGas(1L))) {
                if (fluid.amount > MAX_OZONE_GAS_FOR_EXPLOSION) {
                    // TODO: Fix crash in hatch
                    // this.explodeMultiblock();
                }
            }
        }
        return result;
    }

    @Override
    protected ResourceLocation getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_OZONATION_LOOP.resourceLocation;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 2;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingCount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z)) return false;
        if (casingCount < MIN_CASING) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_LuV;
    }
}
