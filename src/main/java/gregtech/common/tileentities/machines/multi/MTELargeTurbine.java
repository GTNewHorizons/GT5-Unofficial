package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW_EMPTY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtilityClient;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.items.MetaGeneratedTool01;

public abstract class MTELargeTurbine extends MTEEnhancedMultiBlockBase<MTELargeTurbine>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTELargeTurbine>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTELargeTurbine> computeValue(Class<?> type) {
            return StructureDefinition.<MTELargeTurbine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "     ", "     ", "     ", "     ", "     ", },
                            { " --- ", " ccc ", " hhh ", " hhh ", " hhh ", },
                            { " --- ", " c~c ", " h-h ", " h-h ", " hdh ", },
                            { " --- ", " ccc ", " hhh ", " hhh ", " hhh ", },
                            { "     ", "     ", "     ", "     ", "     ", }, }))
                .addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
                .addElement('d', lazy(t -> Dynamo.newAny(t.getCasingTextureIndex(), 1)))
                .addElement(
                    'h',
                    lazy(
                        t -> buildHatchAdder(MTELargeTurbine.class).atLeast(t.getHatchElements())
                            .casingIndex(t.getCasingTextureIndex())
                            .dot(2)
                            .buildAndChain(t.getCasingBlock(), t.getCasingMeta())))
                .build();
        }
    };

    protected int baseEff = 0;
    protected int optFlow = 0;
    protected double realOptFlow = 0;
    protected int storedFluid = 0;
    protected int counter = 0;
    protected boolean looseFit = false;
    protected int overflowMultiplier = 0;
    protected final float[] flowMultipliers = new float[] { 1, 1, 1 };

    // client side stuff
    protected boolean mHasTurbine;
    // mMachine got overwritten by StructureLib extended facing query response
    // so we use a separate field for this
    protected boolean mFormed;

    public MTELargeTurbine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbine(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public IStructureDefinition<MTELargeTurbine> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected ExtendedFacing getCorrectedAlignment(ExtendedFacing aOldFacing) {
        return aOldFacing.with(Flip.NONE)
            .with(Rotation.NORMAL);
    }

    @Override
    public boolean isFlipChangeAllowed() {
        return false;
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    @SuppressWarnings("unchecked")
    protected IHatchElement<? super MTELargeTurbine>[] getHatchElements() {
        if (getPollutionPerTick(null) == 0)
            return new IHatchElement[] { Maintenance, InputHatch, OutputHatch, OutputBus, InputBus };
        return new IHatchElement[] { Maintenance, InputHatch, OutputHatch, OutputBus, InputBus, Muffler };
    }

    @Override
    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity aBaseMetaTileEntity) {
        boolean f = super.checkStructure(aForceReset, aBaseMetaTileEntity);
        if (f && getBaseMetaTileEntity().isServerSide()) {
            // while is this a client side field, blockrenderer will reuse the server world for client side rendering
            // so we must set it as well...
            mFormed = true;
            return true;
        }
        return f;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 1) && mMaintenanceHatches.size() == 1
            && mMufflerHatches.isEmpty() == (getPollutionPerTick(null) == 0);
    }

    public abstract Block getCasingBlock();

    public abstract byte getCasingMeta();

    public abstract int getCasingTextureIndex();

    public boolean isNewStyleRendering() {
        return false;
    }

    public IIconContainer[] getTurbineTextureActive() {
        return TURBINE_NEW_ACTIVE;
    }

    public IIconContainer[] getTurbineTextureFull() {
        return TURBINE_NEW;
    }

    public IIconContainer[] getTurbineTextureEmpty() {
        return TURBINE_NEW_EMPTY;
    }

    @Override
    public boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
        if (!isNewStyleRendering() || !mFormed) return false;

        IIconContainer[] tTextures;
        if (getBaseMetaTileEntity().isActive()) tTextures = getTurbineTextureActive();
        else if (hasTurbine()) tTextures = getTurbineTextureFull();
        else tTextures = getTurbineTextureEmpty();
        GTUtilityClient
            .renderTurbineOverlay(aWorld, aX, aY, aZ, aRenderer, getExtendedFacing(), getCasingBlock(), tTextures);
        return false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mHasTurbine = (aValue & 0x1) != 0;
        mFormed = (aValue & 0x2) != 0;
        super.onValueUpdate(aValue);
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((hasTurbine() ? 1 : 0) | (mMachine ? 2 : 0));
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity tTileEntity, int aBaseCasingIndex) {
        return addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())
            || addInputToMachineList(tTileEntity, getCasingTextureIndex())
            || addOutputToMachineList(tTileEntity, getCasingTextureIndex())
            || addMufflerToMachineList(tTileEntity, getCasingTextureIndex());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ItemStack controllerSlot = getControllerSlot();
        if ((counter & 7) == 0 && (controllerSlot == null || !(controllerSlot.getItem() instanceof MetaGeneratedTool)
            || controllerSlot.getItemDamage() < 170
            || controllerSlot.getItemDamage() > 179)) {
            stopMachine(ShutDownReasonRegistry.NO_TURBINE);
            return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
        }

        TurbineStatCalculator turbine = new TurbineStatCalculator(
            (MetaGeneratedTool) controllerSlot.getItem(),
            controllerSlot);

        ArrayList<FluidStack> tFluids = getStoredFluids();
        if (!tFluids.isEmpty()) {

            if (baseEff == 0 || optFlow == 0
                || counter >= 512
                || this.getBaseMetaTileEntity()
                    .hasWorkJustBeenEnabled()
                || this.getBaseMetaTileEntity()
                    .hasInventoryBeenModified()) {
                counter = 0;
                baseEff = (int) (100 * turbine.getEfficiency());
                optFlow = (int) turbine.getOptimalFlow();

                overflowMultiplier = turbine.getOverflowEfficiency();

                if (optFlow <= 0 || baseEff <= 0) {
                    stopMachine(ShutDownReasonRegistry.NONE); // in case the turbine got removed
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
            } else {
                counter++;
            }
        }

        int newPower = fluidIntoPower(tFluids, turbine); // How much the
                                                         // turbine should
                                                         // be producing
                                                         // with this flow
        int difference = newPower - this.mEUt; // difference between current output and new output

        // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in
        // power level (per tick)
        // This is how much the turbine can actually change during this tick
        int maxChangeAllowed = Math.max(10, GTUtility.safeInt((long) Math.abs(difference) / 100));

        if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
            int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
            this.mEUt += change; // Apply the change
        } else this.mEUt = newPower;

        if (this.mEUt <= 0) {
            // stopMachine();
            this.mEUt = 0;
            this.mEfficiency = 0;
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        } else {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = 10;
            // Overvoltage is handled inside the MultiBlockBase when pushing out to dynamos. no need to do it here.
            return CheckRecipeResultRegistry.GENERATING;
        }
    }

    abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine);

    abstract float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier);

    // Gets the maximum output that the turbine currently can handle. Going above this will cause the turbine to explode
    public long getMaximumOutput() {
        long aTotal = 0;
        for (MTEHatchDynamo aDynamo : validMTEList(mDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            aTotal = aDynamo.maxAmperesOut() * aVoltage;
        }
        return aTotal;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) {
            return 0;
        }
        if (aStack.getItem() instanceof MetaGeneratedTool01) {
            return 10000;
        }
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String tRunning = mMaxProgresstime > 0
            ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.running.true")
                + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.running.false")
                + EnumChatFormatting.RESET;
        String tMaintainance = getIdealStatus() == getRepairStatus()
            ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                + EnumChatFormatting.RESET;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof MetaGeneratedTool01) {
            tDura = GTUtility.safeInt(
                (long) (100.0f / MetaGeneratedTool.getToolMaxDamage(mInventory[1])
                    * (MetaGeneratedTool.getToolDamage(mInventory[1])) + 1));
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        return new String[] {
            // 8 Lines available for information panels
            tRunning + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(((long) mEUt * mEfficiency) / 10000)
                + EnumChatFormatting.RESET
                + " EU/t", /* 1 */
            tMaintainance, /* 2 */
            StatCollector.translateToLocal("GT5U.turbine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.RESET
                + "%", /* 2 */
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + /* 3 */ EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(GTUtility.safeInt((long) realOptFlow))
                + EnumChatFormatting.RESET
                + " L/t"
                + /* 4 */ EnumChatFormatting.YELLOW
                + " ("
                + (looseFit ? StatCollector.translateToLocal("GT5U.turbine.loose")
                    : StatCollector.translateToLocal("GT5U.turbine.tight"))
                + ")", /* 5 */
            StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                + EnumChatFormatting.GOLD
                + GTUtility.formatNumbers(storedFluid)
                + EnumChatFormatting.RESET
                + "L", /* 6 */
            StatCollector.translateToLocal(
                "GT5U.turbine.dmg") + ": " + EnumChatFormatting.RED + tDura + EnumChatFormatting.RESET + "%", /* 7 */
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %" /* 8 */
        };
    }

    public boolean hasTurbine() {
        return getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isClientSide() ? mHasTurbine
            : this.getMaxEfficiency(mInventory[1]) > 0;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatToPlayer(
                aPlayer,
                looseFit ? GTUtility.trans("500", "Fitting: Loose - More Flow")
                    : GTUtility.trans("501", "Fitting: Tight - More Efficiency"));
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 1, elementBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_LARGE_TURBINES_LOOP;
    }
}
