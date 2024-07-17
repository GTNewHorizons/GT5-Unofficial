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

    private static final SuperconductorMaterial[] SUPERCONDUCTOR_MATERIALS = new SuperconductorMaterial[] {
        new SuperconductorMaterial(Materials.Longasssuperconductornameforuvwire.getFluid(SUPERCON_FLUID_AMOUNT), 1.0f),
        new SuperconductorMaterial(
            Materials.Longasssuperconductornameforuhvwire.getFluid(SUPERCON_FLUID_AMOUNT),
            1.25f),
        new SuperconductorMaterial(Materials.SuperconductorUEVBase.getFluid(SUPERCON_FLUID_AMOUNT), 1.5f),
        new SuperconductorMaterial(Materials.SuperconductorUIVBase.getFluid(SUPERCON_FLUID_AMOUNT), 1.75f),
        new SuperconductorMaterial(Materials.SuperconductorUMVBase.getFluid(SUPERCON_FLUID_AMOUNT), 2.0f), };

    private static final FluidStack CATALYST_FLUID = Materials.Steel.getFluid(14400L);
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
                throw new IllegalArgumentException("Invalid bit index for degasifier control signal");
            }

            // Shift signal so the requested bit is in the lowermost bit
            // Then only keep the lowermost bit
            // Then test if this bit is on.
            return ((signal >> bit) & 1) == 1;
        }

        public byte getSignal() {
            return signal;
        }

        @Override
        public String toString() {
            return Integer.toBinaryString(((int) signal) & 0b1111);
        }
    }

    private ControlSignal controlSignal = new ControlSignal((byte) 0);

    private final HashMap<Fluid, FluidStack> insertedStuffThisCycle = new HashMap<>();

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
            || Arrays.stream(SUPERCONDUCTOR_MATERIALS)
                .anyMatch(mat -> stack.isFluidEqual(mat.fluid));
    }

    @Override
    public void startCycle(int cycleTime, int progressTime) {
        super.startCycle(cycleTime, progressTime);
        this.controlSignal.randomize();
        this.insertedStuffThisCycle.clear();
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
                        hatch.drain(front, fluid, true);
                        // If the fluid does not yet exist in the map, insert it.
                        // Otherwise, merge the amounts
                        insertedStuffThisCycle.merge(fluid.getFluid(), fluid, (a, b) -> {
                            a.amount += b.amount;
                            return a;
                        });
                    }
                }
            }
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
        aNBT.setByte("controlSignal", controlSignal.getSignal());
        NBTTagCompound fluidMap = aNBT.getCompoundTag("insertedFluidMap");
        for (Object uglyKey : fluidMap.tagMap.keySet()) {
            String key = (String) uglyKey;
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
        controlSignal = new ControlSignal(aNBT.getByte("controlSignal"));
        NBTTagCompound fluidMap = new NBTTagCompound();
        for (FluidStack stack : insertedStuffThisCycle.values()) {
            stack.writeToNBT(fluidMap);
        }
        aNBT.setTag("insertedFluidMap", fluidMap);
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Current control signal: " + EnumChatFormatting.YELLOW + controlSignal.toString());
        for (FluidStack stack : insertedStuffThisCycle.values()) {
            info.add(
                "Fluid inserted this cycle: " + EnumChatFormatting.YELLOW
                    + stack.amount
                    + "L "
                    + stack.getLocalizedName());
        }
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
