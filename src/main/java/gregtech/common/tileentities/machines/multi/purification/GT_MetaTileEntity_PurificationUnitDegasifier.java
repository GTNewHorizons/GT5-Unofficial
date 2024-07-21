package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;

public class GT_MetaTileEntity_PurificationUnitDegasifier
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitDegasifier>
    implements ISurvivalConstructable {

    // TODO
    private static final int CASING_INDEX_MAIN = getTextureIndex(GregTech_API.sBlockCasings9, 11);

    private static final String STRUCTURE_PIECE_MAIN = "main";

    // Temporary, while testing
    // spotless:off
    private static final String[][] structure = new String[][] {
        { "           ", "           ", "           ", "     E     ", "           ", "           ", "           ", "           ", "   AAAAA   ", "  AAA~AAA  ", " AAAAAAAAA " },
        { "           ", "           ", "     E     ", "           ", "           ", "   CCCCC   ", "   CDCDC   ", "   CCCCC   ", "  ACCCCCA  ", " AAAAAAAAA ", "AAAAAAAAAAA" },
        { "           ", "           ", "     E     ", "           ", "           ", "  CAAAAAC  ", "  C     C  ", "  C     C  ", " AC     CA ", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "     E     ", "           ", "    CCC    ", "   FCCCF   ", " CAA   AAC ", " C       C ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "     E     ", "           ", "    BBB    ", "   C   C   ", "   C   C   ", " CA     AC ", " D       D ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "     E     ", "     E     ", "    BEB    ", "   C E C   ", "   C E C   ", " CA  E  AC ", " C   E   C ", " C   E   C ", "AC   E   CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "    BBB    ", "   C   C   ", "   C   C   ", " CA     AC ", " D       D ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "    CCC    ", "   FCCCF   ", " CAA   AAC ", " C       C ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "           ", "           ", "  CAAAAAC  ", "  C     C  ", "  C     C  ", " AC     CA ", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "           ", "           ", "   CCCCC   ", "   CDCDC   ", "   CCCCC   ", "  ACCCCCA  ", " AAAAAAAAA ", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "   AAAAA   ", "  AAAAAAA  ", " AAAAAAAAA " } };
    // spotless:on

    private static final int STRUCTURE_X_OFFSET = 5;
    private static final int STRUCTURE_Y_OFFSET = 9;
    private static final int STRUCTURE_Z_OFFSET = 0;

    // Supplier because werkstoff loads later than multiblock controllers... fml
    private static final Supplier<FluidStack[]> INERT_GASES = () -> new FluidStack[] { Materials.Helium.getGas(10000L),
        WerkstoffLoader.Neon.getFluidOrGas(7500), WerkstoffLoader.Krypton.getFluidOrGas(5000),
        WerkstoffLoader.Xenon.getFluidOrGas(2500) };

    private static final class SuperconductorMaterial {

        public FluidStack fluid;
        public float multiplier;

        SuperconductorMaterial(FluidStack fluid, float multiplier) {
            this.fluid = fluid;
            this.multiplier = multiplier;
        }
    }

    private static final long SUPERCON_FLUID_AMOUNT = 1440L;

    private static final Supplier<SuperconductorMaterial[]> SUPERCONDUCTOR_MATERIALS = () -> new SuperconductorMaterial[] {
        new SuperconductorMaterial(Materials.Longasssuperconductornameforuvwire.getMolten(SUPERCON_FLUID_AMOUNT), 1.0f),
        new SuperconductorMaterial(
            Materials.Longasssuperconductornameforuhvwire.getMolten(SUPERCON_FLUID_AMOUNT),
            1.25f),
        new SuperconductorMaterial(Materials.SuperconductorUEVBase.getMolten(SUPERCON_FLUID_AMOUNT), 1.5f),
        new SuperconductorMaterial(Materials.SuperconductorUIVBase.getMolten(SUPERCON_FLUID_AMOUNT), 1.75f),
        new SuperconductorMaterial(Materials.SuperconductorUMVBase.getMolten(SUPERCON_FLUID_AMOUNT), 2.0f), };

    private static final FluidStack CATALYST_FLUID = Materials.Steel.getMolten(14400L);
    private static final FluidStack COOLANT_FLUID = Materials.SuperCoolant.getFluid(10000L);

    private static final long CONSUME_INTERVAL = 20;

    private static class ControlSignal {

        private byte signal;

        public ControlSignal(byte sig) {
            signal = sig;
        }

        public void randomize() {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            signal = (byte) random.nextInt(0, 16);
        }

        public boolean getBit(int bit) {
            if (bit < 0 || bit > 3) {
                throw new IllegalArgumentException("Invalid bit index for degasser control signal");
            }

            // Shift signal so the requested bit is in the lowermost bit
            // Then only keep the lowermost bit
            // Then test if this bit is on.
            return ((signal >> bit) & 1) == 1;
        }

        public byte getSignal() {
            return signal;
        }

        // Get integer value representing control bits 1 and 2
        public int getControlBit12() {
            return (signal >> 1) & 0b11;
        }

        public boolean isZero() {
            return signal == (byte) 0;
        }

        @Override
        public String toString() {
            return Integer.toBinaryString(((int) signal) & 0b1111);
        }
    }

    private static class ControlBitStatus {

        public FluidStack stack;
        public boolean satisfied;

        public ControlBitStatus(FluidStack stack, boolean satisfied) {
            this.stack = stack;
            this.satisfied = satisfied;
        }
    }

    private ControlSignal controlSignal = new ControlSignal((byte) 0);

    private final HashMap<Fluid, FluidStack> insertedStuffThisCycle = new HashMap<>();

    private float outputMultiplier = 1.0f;

    private GT_MetaTileEntity_Hatch_DegasifierControlHatch controlHatch = null;

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitDegasifier> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitDegasifier>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // PLACEHOLDER ELEMENTS
        .addElement(
            'A',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitDegasifier>buildHatchAdder()
                        .atLeastList(Arrays.asList(InputHatch, OutputHatch, SpecialHatchElement.ControlHatch))
                        .casingIndex(1)
                        .dot(1)
                        .cacheHint(() -> "Input Hatch, Output Hatch, Control Hatch")
                        .build()),
                ofBlock(GregTech_API.sBlockCasings8, 0)))
        .addElement('B', ofBlock(GregTech_API.sBlockCasings8, 1))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 7))
        .addElement('D', ofBlockAnyMeta(GregTech_API.sBlockTintedGlass, 0))
        .addElement('E', ofFrame(Materials.Longasssuperconductornameforuvwire))
        .addElement('F', ofFrame(Materials.Carbon))
        .build();

    public GT_MetaTileEntity_PurificationUnitDegasifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitDegasifier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitDegasifier(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
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
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN) };
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
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitDegasifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Controller block for the Degasser Purification Unit.")
            .addInfo("Must be linked to a Purification Plant to work.")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA
                    + "Removes excess oxygen and other gases from the water by using complex methods.")
            .addSeparator()
            .addInfo(
                "At the start of the operation, the " + EnumChatFormatting.WHITE
                    + "Degasser Control Hatch"
                    + EnumChatFormatting.GRAY
                    + " will output a redstone signal")
            .addInfo("To succeed the recipe, you will need to successfully decode the instructions in the signal.")
            .addInfo("To decode the signal, interpret the signal strength as a 4-bit number from 0-15.")
            .addInfo("Denote the lowest bit as bit 1, and the highest as bit 4.")
            .addSeparator()
            .addInfo(EnumChatFormatting.WHITE + "" + EnumChatFormatting.BOLD + "Bit 1: Sparging by Inert Gas")
            .addInfo(
                "If this bit is on, you must insert an " + EnumChatFormatting.WHITE
                    + "inert gas"
                    + EnumChatFormatting.GRAY
                    + " into the machine.")
            .addInfo(
                "To determine which gas to insert, interpret bits " + EnumChatFormatting.WHITE
                    + "2-3"
                    + EnumChatFormatting.GRAY
                    + " as a 2-bit number.")
            .addInfo(
                EnumChatFormatting.WHITE + "0: "
                    + EnumChatFormatting.RED
                    + "10000L Helium"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.WHITE
                    + "1: "
                    + EnumChatFormatting.RED
                    + "7500L Neon"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.WHITE
                    + "2: "
                    + EnumChatFormatting.RED
                    + "5000L Krypton"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.WHITE
                    + "3: "
                    + EnumChatFormatting.RED
                    + "2500L Xenon")
            .addSeparator()
            .addInfo(EnumChatFormatting.WHITE + "" + EnumChatFormatting.BOLD + "Bit 2: Superconductive Deionization")
            .addInfo(
                "If this bit is on, you must insert " + EnumChatFormatting.RED
                    + "1440L "
                    + EnumChatFormatting.WHITE
                    + "Molten Superconductor Base.")
            .addInfo("Using higher tier superconductor provides bonus output.")
            .addInfo(
                "Output multiplier: " + EnumChatFormatting.DARK_GREEN
                    + "UV"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.WHITE
                    + "1x"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.DARK_RED
                    + "UHV"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.WHITE
                    + "1.25x"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.DARK_PURPLE
                    + "UEV"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.WHITE
                    + "1.5x"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.DARK_BLUE
                    + ""
                    + EnumChatFormatting.BOLD
                    + "UIV"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.WHITE
                    + "1.75x"
                    + EnumChatFormatting.GRAY
                    + " / "
                    + EnumChatFormatting.RED
                    + ""
                    + EnumChatFormatting.BOLD
                    + "UMV"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.WHITE
                    + "2x")
            .addSeparator()
            .addInfo(AuthorNotAPenguin)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        RecipeMap<?> recipeMap = this.getRecipeMap();

        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .fluids(
                this.getStoredFluids()
                    .toArray(new FluidStack[] {}))
            .find();

        this.endRecipeProcessing();
        if (recipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (this.protectsExcessFluid() && !this.canOutputAll(recipe.mFluidOutputs)) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        if (this.protectsExcessItem() && !this.canOutputAll(recipe.mOutputs)) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        this.currentRecipe = recipe;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationDegasifierRecipes;
    }

    // Whether this fluid stack is accepted as a fluid in the inputs that the unit requires at runtime.
    public static boolean isValidFluid(FluidStack stack) {
        return stack.isFluidEqual(CATALYST_FLUID) || stack.isFluidEqual(COOLANT_FLUID)
            || Arrays.stream(INERT_GASES.get())
                .anyMatch(stack::isFluidEqual)
            || Arrays.stream(SUPERCONDUCTOR_MATERIALS.get())
                .anyMatch(mat -> stack.isFluidEqual(mat.fluid));
    }

    // Check if an exact match for this FluidStack was found in the map of inserted fluids
    private boolean wasFluidInsertedExact(FluidStack toFind) {
        FluidStack candidate = insertedStuffThisCycle.get(toFind.getFluid());
        // Fluid was inserted if found and the amount matches
        return candidate != null && candidate.amount == toFind.amount;
    }

    private ControlBitStatus isBit0Satisfied() {
        // Check if instructions matching the first bit are satisfied.
        // Instructions:
        // If bit 0 is on, insert an inert gas. Bits 1-2 of the control signal determine which inert
        // gas to insert.

        if (controlSignal.getBit(0)) {
            // Grab the gas to insert from the control bits
            int gasToInsert = controlSignal.getControlBit12();
            FluidStack gasStack = INERT_GASES.get()[gasToInsert];
            // Check if it was inserted
            if (wasFluidInsertedExact(gasStack)) new ControlBitStatus(gasStack, true);
            else return new ControlBitStatus(null, false);
        }

        // Bit 0 is not on, so this is always satisfied
        return new ControlBitStatus(null, true);
    }

    private ControlBitStatus isBit1Satisfied() {
        // Check if instructions matching the second bit (bit 1) are satisfied.
        // Instructions:
        // If bit 1 is on, insert molten superconductor.
        // Higher tier superconductor gives a better bonus.
        // Only one type of superconductor may be inserted or the operation fails,
        // so we don't care about the order in which we find it.
        if (controlSignal.getBit(1)) {
            // Find the first superconductor material in the list that was inserted with an exact match
            Optional<SuperconductorMaterial> material = Arrays.stream(SUPERCONDUCTOR_MATERIALS.get())
                .filter(candidate -> wasFluidInsertedExact(candidate.fluid))
                .findFirst();
            if (material.isPresent()) {
                // Get the material and set the output multiplier, then
                // report success with the matching fluid.
                SuperconductorMaterial scMaterial = material.get();
                this.outputMultiplier = scMaterial.multiplier;
                return new ControlBitStatus(scMaterial.fluid, true);
            }
            // No superconductor was inserted but bit is on fail.
            return new ControlBitStatus(null, false);
        }

        return new ControlBitStatus(null, true);
    }

    private ControlBitStatus isBit2Satisfied() {
        // Check if instructions matching the third bit (bit 2) are satisfied.
        // Instructions:
        // If bit 2 is on, insert molten steel.
        if (controlSignal.getBit(2)) {
            // If steel was inserted, return it and report success.
            if (wasFluidInsertedExact(CATALYST_FLUID)) return new ControlBitStatus(CATALYST_FLUID, true);
            // Otherwise report failure.
            return new ControlBitStatus(null, false);
        }

        return new ControlBitStatus(null, true);
    }

    private ControlBitStatus isBit3Satisfied() {
        // Check if instructions matching the fourth bit (bit 3) are satisfied.
        // Instructions:
        // If bit 3 is on, do not insert anything.
        if (controlSignal.getBit(3)) {
            // Simply check if the map of inserted fluids is empty
            if (insertedStuffThisCycle.isEmpty()) return new ControlBitStatus(null, true);
            return new ControlBitStatus(null, false);
        }
        return new ControlBitStatus(null, true);
    }

    private boolean areAllBitsSatisfied() {
        // Check if each individual bit is satisfied.
        // Additional instructions: If no bits are on, insert super coolant

        if (controlSignal.isZero()) {
            return wasFluidInsertedExact(COOLANT_FLUID);
        }

        ControlBitStatus bit0 = isBit0Satisfied();
        ControlBitStatus bit1 = isBit1Satisfied();
        ControlBitStatus bit2 = isBit2Satisfied();
        ControlBitStatus bit3 = isBit3Satisfied();

        // If bit 3 is satisfied and on, all other bits are automatically satisfied,
        // with no fluids being allowed to be inserted.
        if (controlSignal.getBit(3) && bit3.satisfied) {
            bit0 = bit1 = bit2 = new ControlBitStatus(null, true);
        }

        if (bit0.satisfied && bit1.satisfied && bit2.satisfied && bit3.satisfied) {
            // Check if the map contains any other stacks than the ones in the control bit statuses
            for (FluidStack inserted : insertedStuffThisCycle.values()) {
                // If the inserted stack is null, or any of the fluids required, this stack is fine.
                if (inserted == null) continue;
                if (bit0.stack != null && inserted.isFluidEqual(bit0.stack)) continue;
                if (bit1.stack != null && inserted.isFluidEqual(bit1.stack)) continue;
                if (bit2.stack != null && inserted.isFluidEqual(bit2.stack)) continue;
                if (bit3.stack != null && inserted.isFluidEqual(bit3.stack)) continue;
                // Otherwise it's a nonrequested stack and the recipe should fail.
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public void startCycle(int cycleTime, int progressTime) {
        super.startCycle(cycleTime, progressTime);
        this.controlSignal.randomize();
        this.insertedStuffThisCycle.clear();
        this.outputMultiplier = 1.0f;
        // Make sure to output the hatch control signal.
        this.controlHatch.updateOutputSignal(this.controlSignal.getSignal());
    }

    private static ArrayList<FluidStack> getDrainableFluidsFromHatch(GT_MetaTileEntity_Hatch_Input hatch) {
        // Need special handling for quad input hatches, otherwise it only returns the first fluid in the hatch
        if (hatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
            return new ArrayList<>(Arrays.asList(((GT_MetaTileEntity_Hatch_MultiInput) hatch).getStoredFluid()));
        }
        return new ArrayList<>(Collections.singletonList(hatch.getFluid()));
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);

        // If machine is running, continuously consume all valid inputs
        if (mMaxProgresstime > 0 && aTick % CONSUME_INTERVAL == 0) {
            // For each hatch, check if each fluid inside is one of the valid fluids. If so, consume it all.
            for (GT_MetaTileEntity_Hatch_Input hatch : mInputHatches) {
                ArrayList<FluidStack> drainableFluids = getDrainableFluidsFromHatch(hatch);
                for (FluidStack fluid : drainableFluids) {
                    if (fluid != null && isValidFluid(fluid)) {
                        // Apparently this parameter is mostly ignored, but might as well get it right.
                        ForgeDirection front = hatch.getBaseMetaTileEntity()
                            .getFrontFacing();
                        // Drain the fluid and save it
                        FluidStack drainedFluid = hatch.drain(front, fluid, true);
                        // If the fluid does not yet exist in the map, insert it.
                        // Otherwise, merge the amounts
                        insertedStuffThisCycle.merge(
                            fluid.getFluid(),
                            drainedFluid,
                            (a, b) -> new FluidStack(a.getFluid(), a.amount + b.amount));
                    }
                }
            }
        }
    }

    @Override
    public void addRecipeOutputs() {
        super.addRecipeOutputs();
        if (outputMultiplier > 1.01f) {
            FluidStack waterOutput = currentRecipe.mFluidOutputs[0];
            FluidStack bonusOutput = new FluidStack(
                waterOutput.getFluid(),
                (int) (waterOutput.amount * (outputMultiplier - 1.0f)));
            this.addOutput(bonusOutput);
        }
    }

    @Override
    public float calculateFinalSuccessChance() {
        // Success chance is 100% when all bits are satisfied, 0% otherwise.
        if (areAllBitsSatisfied()) {
            return 100.0f;
        } else {
            return 0.0f;
        }
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 7;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_UHV;
    }

    public boolean addControlHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DegasifierControlHatch) {
            // Only allow a single control hatch, so fail structure check if there is already one
            if (this.controlHatch == null) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                this.controlHatch = (GT_MetaTileEntity_Hatch_DegasifierControlHatch) aMetaTileEntity;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.controlHatch = null;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        controlSignal = new ControlSignal(aNBT.getByte("controlSignal"));
        outputMultiplier = aNBT.getFloat("outputMultiplier");
        NBTTagCompound fluidMap = aNBT.getCompoundTag("insertedFluidMap");
        for (String key : fluidMap.func_150296_c()) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidMap.getCompoundTag(key));
            // Ignore if fluid failed to load, for example if the fluid ID was changed between versions
            if (fluid == null) {
                continue;
            }
            insertedStuffThisCycle.put(fluid.getFluid(), fluid);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("controlSignal", controlSignal.getSignal());
        aNBT.setFloat("outputMultiplier", outputMultiplier);
        NBTTagCompound fluidMap = new NBTTagCompound();
        for (FluidStack stack : insertedStuffThisCycle.values()) {
            NBTTagCompound compound = new NBTTagCompound();
            stack.writeToNBT(compound);
            fluidMap.setTag(
                stack.getFluid()
                    .getName(),
                compound);
        }
        aNBT.setTag("insertedFluidMap", fluidMap);
    }

    private static String generateInfoStringForBit(int i, ControlBitStatus status) {
        String base = "Bit " + (i + 1) + " status: ";
        if (status.satisfied) {
            return base + EnumChatFormatting.GREEN + "OK";
        } else {
            return base + EnumChatFormatting.RED + "NOT OK";
        }
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Current control signal: " + EnumChatFormatting.YELLOW + controlSignal.toString());
        info.add("Current output multiplier: " + EnumChatFormatting.YELLOW + outputMultiplier);
        for (FluidStack stack : insertedStuffThisCycle.values()) {
            info.add(
                "Fluid inserted this cycle: " + EnumChatFormatting.YELLOW
                    + stack.amount
                    + "L "
                    + stack.getLocalizedName());
        }
        info.add(generateInfoStringForBit(0, isBit0Satisfied()));
        info.add(generateInfoStringForBit(1, isBit1Satisfied()));
        info.add(generateInfoStringForBit(2, isBit2Satisfied()));
        info.add(generateInfoStringForBit(3, isBit3Satisfied()));
        return info.toArray(new String[] {});
    }

    private enum SpecialHatchElement implements IHatchElement<GT_MetaTileEntity_PurificationUnitDegasifier> {

        ControlHatch(GT_MetaTileEntity_PurificationUnitDegasifier::addControlHatchToMachineList,
            GT_MetaTileEntity_Hatch_DegasifierControlHatch.class) {

            @Override
            public long count(GT_MetaTileEntity_PurificationUnitDegasifier mte) {
                return mte.controlHatch == null ? 0 : 1;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_PurificationUnitDegasifier> adder;

        @SafeVarargs
        SpecialHatchElement(IGT_HatchAdder<GT_MetaTileEntity_PurificationUnitDegasifier> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_PurificationUnitDegasifier> adder() {
            return adder;
        }
    }
}
