package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorJulia;
import static gregtech.api.enums.Textures.BlockIcons.COKE_OVEN_OVERLAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.COKE_OVEN_OVERLAY_INACTIVE;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraftforge.fluids.FluidContainerRegistry.fillFluidContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchCokeOven;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.gui.modularui.multiblock.MTECokeOvenGUI;
import gregtech.common.pollution.Pollution;

public class MTECokeOven extends MTEEnhancedMultiBlockBase<MTECokeOven> implements ISurvivalConstructable {

    public final static int INPUT_SLOT = 0;
    public final static int OUTPUT_SLOT = 1;
    public final static int FLUID_CAPACITY = 64_000;

    private FluidStack fluid;
    private final ArrayList<MTEHatchCokeOven> hatches = new ArrayList<>();

    public MTECokeOven(String name) {
        super(name);
    }

    public MTECokeOven(int ID, String name, String nameRegional) {
        super(ID, name, nameRegional);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("Coke Oven")
            .addInfo("Turns coal into coke and produces creosote oil")
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoRange("Coke Oven Casing", 0, 26, false)
            .addStructureInfo(
                EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.MBTT.CokeOvenHatch")
                    + ": "
                    + EnumChatFormatting.GRAY
                    + "Any Coke Oven Casing")
            .addPollutionAmount(GTMod.proxy.mPollutionCokeOvenPerSecond)
            .toolTipFinisher(AuthorJulia);
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
        .addElement(
            'C',
            buildHatchAdder(MTECokeOven.class).atLeast(new HatchElement())
                .casingIndex(1)
                .hint(1)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings12, 0)))
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
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return false;
    }

    @Override
    public int getCapacity() {
        return FLUID_CAPACITY;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
        if (side != ForgeDirection.UNKNOWN) return null;
        if (fluid.amount <= 0) return null;
        final int toDrain = Math.min(fluid.amount, maxDrain);
        if (doDrain) fluid.amount -= toDrain;
        return GTUtility.copyAmount(toDrain, fluid);
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.COKE_OVEN;
    }

    @Override
    protected @NotNull MTECokeOvenGUI getGui() {
        return new MTECokeOvenGUI(this);
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
    public boolean isValidSlot(int index) {
        return index == INPUT_SLOT || index == OUTPUT_SLOT;
    }

    @Override
    protected boolean supportsSlotAutomation(int index) {
        return index == INPUT_SLOT || index == OUTPUT_SLOT;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        // Only allow putting stacks through hatches which set the direction to `UNKNOWN`
        if (side != ForgeDirection.UNKNOWN) return false;
        return super.allowPutStack(baseMetaTileEntity, index, side, stack);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECokeOven(mName);
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
    public IAlignmentLimits getAlignmentLimits() {
        return IAlignmentLimits.UPRIGHT;
    }

    /**
     * Draws random flames and smoke particles in front of Primitive Blast Furnace when active
     * Copied from {@link MTEBrickedBlastFurnace}.
     *
     * @param baseMetaTileEntity The entity that will handle the {@link Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void onRandomDisplayTick(IGregTechTileEntity baseMetaTileEntity) {
        if (!baseMetaTileEntity.isActive()) return;

        final ForgeDirection frontFacing = baseMetaTileEntity.getFrontFacing();

        final double oX = baseMetaTileEntity.getOffsetX(frontFacing, 1) + 0.5;
        final double oY = baseMetaTileEntity.getOffsetY(frontFacing, 1);
        final double oZ = baseMetaTileEntity.getOffsetZ(frontFacing, 1) + 0.5;
        final double offset = -0.48;
        final double horizontal = XSTR_INSTANCE.nextDouble() * 8.0 / 16.0 - 4.0 / 16.0;
        final double vertical = XSTR_INSTANCE.nextDouble() * 10.0 / 16.0 + 5.0 / 16.0;

        switch (frontFacing) {
            case NORTH -> {
                final double x = oX + horizontal;
                final double y = oY + vertical;
                final double z = oZ - offset;
                createParticles(baseMetaTileEntity, x, y, z);
            }
            case SOUTH -> {
                final double x = oX + horizontal;
                final double y = oY + vertical;
                final double z = oZ + offset;
                createParticles(baseMetaTileEntity, x, y, z);
            }
            case WEST -> {
                final double x = oX - offset;
                final double y = oY + vertical;
                final double z = oZ + horizontal;
                createParticles(baseMetaTileEntity, x, y, z);
            }
            case EAST -> {
                final double x = oX + offset;
                final double y = oY + vertical;
                final double z = oZ + horizontal;
                createParticles(baseMetaTileEntity, x, y, z);
            }
            default -> throw new IllegalStateException("Unexpected facing: " + frontFacing);
        }
    }

    private void createParticles(IGregTechTileEntity baseMetaTileEntity, double x, double y, double z) {
        WorldSpawnedEventBuilder.ParticleEventBuilder particleEventBuilder = new WorldSpawnedEventBuilder.ParticleEventBuilder()
            .setMotion(0, 0, 0)
            .setPosition(x, y, z)
            .setWorld(baseMetaTileEntity.getWorld());
        particleEventBuilder.setIdentifier(ParticleFX.SMOKE)
            .run();
        particleEventBuilder.setIdentifier(ParticleFX.FLAME)
            .run();
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
    public byte getTileEntityBaseType() {
        return HarvestTool.PickaxeLevel0.toTileEntityBaseType();
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isClientSide()) onPostTickClient(baseMetaTileEntity, tick);
        if (baseMetaTileEntity.isServerSide()) onPostTickServer(baseMetaTileEntity, tick);
    }

    private void onPostTickClient(IGregTechTileEntity baseMetaTileEntity, long tick) {
        doActivitySound(SoundResource.GTCEU_LOOP_FURNACE);
    }

    private void onPostTickServer(IGregTechTileEntity baseMetaTileEntity, long tick) {
        checkRecipeProgress(baseMetaTileEntity);

        // Polling updates.
        if (tick % 20 == 0) {
            mMachine = checkMachine(baseMetaTileEntity, null);

            // Sets "Incomplete Structure" text in WAILA
            setErrorDisplayID(mMachine ? 0 : 64);

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

    private static class HatchElement implements IHatchElement<MTECokeOven> {

        public HatchElement() {}

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchCokeOven.class);
        }

        @Override
        public IGTHatchAdder<? super MTECokeOven> adder() {
            return MTECokeOven::addHatch;
        }

        @Override
        public String name() {
            return "Coke Oven Hatch";
        }

        @Override
        public long count(MTECokeOven cokeOven) {
            return cokeOven.hatches.size();
        }
    }

    private boolean addHatch(IGregTechTileEntity tileEntity, Short baseCasingIndex) {
        if (tileEntity == null) return false;
        IMetaTileEntity metaTileEntity = tileEntity.getMetaTileEntity();
        if (metaTileEntity == null) return false;
        if (metaTileEntity instanceof MTEHatchCokeOven hatch) {
            hatch.addController(this);
            return hatches.add(hatch);
        }
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (FluidContainerRegistry.isContainer(aPlayer.getHeldItem())) {
            if (aPlayer.capabilities.isCreativeMode) {
                return false;
            }
            ItemStack heldItem = aPlayer.getHeldItem();
            ItemStack singleHeldItem = heldItem.copy();
            singleHeldItem.stackSize = 1;
            if (fluid.amount < 1000) {
                return false;
            }
            FluidStack fluidBucket = fluid.copy();
            fluidBucket.amount = 1000;
            ItemStack filledFluidContainer = fillFluidContainer(fluidBucket, singleHeldItem);
            if (filledFluidContainer == null) {
                return false;
            }
            if (heldItem.stackSize == 1) {
                aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, filledFluidContainer);
            } else {
                aPlayer.inventory.decrStackSize(aPlayer.inventory.currentItem, 1);
                if (!aPlayer.inventory.addItemStackToInventory(filledFluidContainer)) {
                    aPlayer.worldObj.spawnEntityInWorld(
                        new EntityItem(
                            aPlayer.worldObj,
                            aPlayer.posX,
                            aPlayer.posY,
                            aPlayer.posZ,
                            filledFluidContainer));
                }
            }
            fluid.amount -= 1000;
            aPlayer.inventory.markDirty();
            aPlayer.inventoryContainer.detectAndSendChanges();
            return true;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }
}
