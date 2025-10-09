package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.COKE_OVEN_OVERLAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.COKE_OVEN_OVERLAY_INACTIVE;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.Pollution;
import gregtech.common.tileentities.machines.multi.gui.MTECokeOvenGUI;

public class MTECokeOven extends MTEEnhancedMultiBlockBase<MTECokeOven> implements ISurvivalConstructable {

    private final static int INPUT_SLOT = 0;
    private final static int OUTPUT_SLOT = 1;
    private final static int FLUID_CAPACITY = 64_000;

    private FluidStack fluid;

    public MTECokeOven(String name) {
        super(name);
    }

    public MTECokeOven(int ID, String name, String nameRegional) {
        super(ID, name, nameRegional);
    }

    // spotless:off
    private static final String[][] shape = new String[][] {
        { "CCC", "CCC", "CCC" },
        { "C~C", "C-C", "CCC" },
        { "CCC", "CCC", "CCC" } };
    //spotless:on

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTECokeOven> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECokeOven>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings12, 0))
        .build();

    @Override
    public IStructureDefinition<MTECokeOven> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public int getCapacity() {
        return FLUID_CAPACITY;
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.PRIMITIVE;
    }

    @Override
    protected @NotNull MTECokeOvenGUI getGui() {
        return new MTECokeOvenGUI(this);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("Coke Oven")
            .addPollutionAmount(GTMod.proxy.mPollutionCokeOvenPerSecond)
            .toolTipFinisher();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.cokeOvenRecipes;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECokeOven(this.mName);
    }

    private static final ITexture[] TEXTURE_CASING = {
        Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0)) };

    private static final ITexture[] TEXTURE_CONTROLLER_INACTIVE = {
        Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0)),
        TextureFactory.builder()
            .addIcon(COKE_OVEN_OVERLAY_INACTIVE)
            .extFacing()
            .build() };

    private static final ITexture[] TEXTURE_CONTROLLER_ACTIVE = {
        Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0)),
        TextureFactory.builder()
            .addIcon(COKE_OVEN_OVERLAY_ACTIVE)
            .extFacing()
            .build() };

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {

        if (side == facing) {
            return active ? TEXTURE_CONTROLLER_ACTIVE : TEXTURE_CONTROLLER_INACTIVE;
        } else {
            return TEXTURE_CASING;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        if (fluid != null) nbt.setTag("fluid", this.fluid.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        fluid = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid"));
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isClientSide()) onPostTickClient(baseMetaTileEntity, tick);
        if (baseMetaTileEntity.isServerSide()) onPostTickServer(baseMetaTileEntity, tick);
    }

    private void onPostTickClient(IGregTechTileEntity baseMetaTileEntity, long tick) {}

    private void onPostTickServer(IGregTechTileEntity baseMetaTileEntity, long tick) {
        checkRecipeProgress(baseMetaTileEntity);

        // Polling updates.
        if (tick % 20 == 0) {
            this.mMachine = checkMachine(baseMetaTileEntity, null);

            if (baseMetaTileEntity.isActive()) {
                Pollution.addPollution(baseMetaTileEntity, GTMod.proxy.mPollutionCokeOvenPerSecond);
            }
        }

        baseMetaTileEntity.setActive(mMaxProgresstime > 0 && mMachine);
    }

    private void checkRecipeProgress(IGregTechTileEntity baseMetaTileEntity) {
        if (!mMachine) return;

        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
            addOutput();
            addOutputFluid();
            mOutputItems = null;
            mOutputFluids = null;
            mProgresstime = 0;
            mMaxProgresstime = 0;
        }

        if (mMaxProgresstime == 0 && baseMetaTileEntity.isAllowedToWork()) {
            final ItemStack input = mInventory[INPUT_SLOT];
            final ItemStack output = mInventory[OUTPUT_SLOT];

            final GTRecipe recipe = getRecipeMap().findRecipeQuery()
                .items(input)
                .find();

            if (recipe == null) return;

            // Check if there is enough space for the output.
            final ItemStack recipeOutput = recipe.getOutput(0);
            if (output != null && recipeOutput != null) {
                if (output.stackSize + recipeOutput.stackSize > output.getMaxStackSize()) return;
                if (!GTUtility.areStacksEqual(output, recipeOutput)) return;
            }

            // Check if there is enough space for the fluid.
            final FluidStack recipeFluid = recipe.getFluidOutput(0);
            if (fluid != null && recipeFluid != null) {
                if (fluid.amount + recipeFluid.amount > FLUID_CAPACITY) return;
                if (!GTUtility.areFluidsEqual(fluid, recipeFluid)) return;
            }

            // Check if input quantity matches. If it is reduced to zero, remove the item stack.
            if (!recipe.isRecipeInputEqual(true, null, input)) return;
            if (input != null && input.stackSize == 0) mInventory[INPUT_SLOT] = null;

            mMaxProgresstime = recipe.mDuration;
            mOutputItems = recipe.mOutputs;
            mOutputFluids = recipe.mFluidOutputs;
        }
    }

    private void addOutput() {
        if (mOutputItems == null) return;
        if (mOutputItems.length == 0) return;

        final ItemStack output = mInventory[OUTPUT_SLOT];
        final ItemStack recipeOutput = mOutputItems[0];
        if (recipeOutput == null) return;

        if (output == null) {
            mInventory[OUTPUT_SLOT] = recipeOutput.copy();
            return;
        }

        if (GTUtility.areStacksEqual(output, recipeOutput)) {
            output.stackSize = Math.min(output.getMaxStackSize(), output.stackSize + recipeOutput.stackSize);
        }
    }

    private void addOutputFluid() {
        if (mOutputFluids == null) return;
        if (mOutputFluids.length == 0) return;

        final FluidStack recipeFluid = mOutputFluids[0];
        if (recipeFluid == null) return;

        if (fluid == null) {
            fluid = recipeFluid.copy();
            return;
        }

        if (GTUtility.areFluidsEqual(fluid, recipeFluid)) {
            fluid.amount = Math.min(FLUID_CAPACITY, fluid.amount + recipeFluid.amount);
        }
    }

    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }
}
