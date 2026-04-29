package gregtech.common.tileentities.machines.multi.turbines;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.*;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.RenderOverlay;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtilityClient;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.items.MetaGeneratedTool01;

public abstract class MTELargeTurbineBase extends MTEExtendedPowerMultiBlockBase<MTELargeTurbineBase>
    implements ISurvivalConstructable, INEIPreviewModifier {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTELargeTurbineBase>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTELargeTurbineBase> computeValue(Class<?> type) {
            return StructureDefinition.<MTELargeTurbineBase>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "---", "---", "---" }, { "CCC", "C~C", "CCC" }, { " B ", "BAB", " B " },
                        { " B ", "BAB", " B " }, { "AEA", "EAE", "AEA" }, { "AEA", "EAE", "AEA" },
                        { "B B", "BDB", "B B" } })
                .addElement(
                    'C',
                    lazy(
                        t -> t.getTurbineCasing()
                            .asElement()))
                .addElement('B', lazy(t -> ofFrame(t.getFrameMaterial())))
                .addElement(
                    'A',
                    lazy(
                        t -> t.getPipeCasing()
                            .asElement()))
                .addElement(
                    'D',
                    lazy(
                        t -> buildHatchAdder(MTELargeTurbineBase.class).atLeast(Dynamo)
                            .casingIndex(t.getTurbineCasing().textureId)
                            .hint(1)
                            .buildAndChain(
                                t.getTurbineCasing()
                                    .asElement())))
                .addElement(
                    'E',
                    lazy(
                        t -> buildHatchAdder(MTELargeTurbineBase.class).atLeast(t.getHatchElements())
                            .casingIndex(t.getTurbineCasing().textureId)
                            .hint(2)
                            .buildAndChain(
                                t.getTurbineCasing()
                                    .asElement())))
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
    protected long maxPower = 0;

    protected final List<RenderOverlay.OverlayTicket> overlayTickets = new ArrayList<>();
    protected boolean mHasTurbine;
    protected boolean mFormed;

    public MTELargeTurbineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        usesTurbine = true;
    }

    public MTELargeTurbineBase(String aName) {
        super(aName);
        usesTurbine = true;
    }

    public abstract Casings getTurbineCasing();

    public abstract Materials getFrameMaterial();

    public abstract Casings getPipeCasing();

    @Override
    public IStructureDefinition<MTELargeTurbineBase> getStructureDefinition() {
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
    protected IHatchElement<? super MTELargeTurbineBase>[] getHatchElements() {
        if (getPollutionPerTick(null) == 0)
            return new IHatchElement[] { Maintenance, InputHatch, OutputHatch, OutputBus, InputBus };
        return new IHatchElement[] { Maintenance, InputHatch, OutputHatch, OutputBus, InputBus, Muffler };
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        maxPower = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 1) && mMaintenanceHatches.size() == 1
            && mMufflerHatches.isEmpty() == (getPollutionPerTick(null) == 0)) {
            maxPower = getMaximumOutput();
            return true;
        }
        return false;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity tTileEntity, int aBaseCasingIndex) {
        return addMaintenanceToMachineList(tTileEntity, getTurbineCasing().textureId)
            || addInputToMachineList(tTileEntity, getTurbineCasing().textureId)
            || addOutputToMachineList(tTileEntity, getTurbineCasing().textureId)
            || addMufflerToMachineList(tTileEntity, getTurbineCasing().textureId);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    /**
     * If true, baseEff is taken raw from the turbine stat.
     * If false (default), it is multiplied by 100.
     */
    protected boolean useLegacyEfficiencyScaling() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
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
                baseEff = useLegacyEfficiencyScaling() ? (int) turbine.getBaseEfficiency()
                    : (int) (100 * turbine.getBaseEfficiency());
                optFlow = (int) turbine.getOptimalFlow();
                overflowMultiplier = turbine.getOverflowEfficiency();

                if (optFlow <= 0 || baseEff <= 0) {
                    stopMachine(ShutDownReasonRegistry.NONE);
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
            } else {
                counter++;
            }
        }

        int newPower = fluidIntoPower(tFluids, turbine);
        int difference = GTUtility.safeInt((long) newPower - this.lEUt);
        int maxChangeAllowed = Math.max(10, GTUtility.safeInt((long) Math.abs(difference) / 100));

        if (Math.abs(difference) > maxChangeAllowed) {
            this.lEUt += maxChangeAllowed * (difference > 0 ? 1 : -1);
        } else {
            this.lEUt = newPower;
        }
        this.mEUt = GTUtility.safeInt(this.lEUt);

        if (this.lEUt <= 0) {
            this.lEUt = 0;
            this.mEUt = 0;
            this.mEfficiency = 0;
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        } else {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = 10;
            return CheckRecipeResultRegistry.GENERATING;
        }
    }

    public abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine);

    /**
     * Override to customize overflow efficiency calculation.
     * Not used by all subclasses, legacy turbines bake this into fluidIntoPower directly.
     */
    protected float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        return 1.0f;
    }

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
        if (GTUtility.isStackInvalid(aStack)) return 0;
        if (aStack.getItem() instanceof MetaGeneratedTool01) return 10000;
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
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
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        setTurbineOverlay();
    }

    protected void setTurbineOverlay() {
        IGregTechTileEntity tile = getBaseMetaTileEntity();
        if (tile.isServerSide()) return;

        IIconContainer[] tTextures;
        if (tile.isActive()) tTextures = getTurbineTextureActive();
        else if (hasTurbine()) tTextures = getTurbineTextureFull();
        else tTextures = getTurbineTextureEmpty();

        GTUtilityClient.setTurbineOverlay(
            tile.getWorld(),
            tile.getXCoord(),
            tile.getYCoord(),
            tile.getZCoord(),
            getExtendedFacing(),
            tTextures,
            overlayTickets);
    }

    @Override
    public void onTextureUpdate() {
        setTurbineOverlay();
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mHasTurbine = (aValue & 0x1) != 0;
        mFormed = (aValue & 0x2) != 0;
        super.onValueUpdate(aValue);
        setTurbineOverlay();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (getBaseMetaTileEntity().isClientSide()) GTUtilityClient.clearTurbineOverlay(overlayTickets);
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((hasTurbine() ? 1 : 0) | (mMachine ? 2 : 0));
    }

    public boolean hasTurbine() {
        return getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isClientSide() ? mHasTurbine
            : this.getMaxEfficiency(mInventory[1]) > 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting", looseFit);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit = aNBT.getBoolean("turbineFitting");
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
                    * MetaGeneratedTool.getToolDamage(mInventory[1]) + 1));
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
            tRunning + ": " + EnumChatFormatting.RED + formatNumber(mEUt) + EnumChatFormatting.RESET + " EU/t",
            tMaintainance,
            StatCollector.translateToLocal("GT5U.turbine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.RESET
                + "%",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(GTUtility.safeInt((long) realOptFlow))
                + EnumChatFormatting.RESET
                + " L/"
                + (this.mMaxProgresstime == 1 ? 't' : 's')
                + EnumChatFormatting.YELLOW
                + " ("
                + (looseFit ? StatCollector.translateToLocal("GT5U.turbine.loose")
                    : StatCollector.translateToLocal("GT5U.turbine.tight"))
                + ")",
            StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                + EnumChatFormatting.GOLD
                + formatNumber(storedFluid)
                + EnumChatFormatting.RESET
                + "L",
            StatCollector.translateToLocal(
                "GT5U.turbine.dmg") + ": " + EnumChatFormatting.RED + tDura + EnumChatFormatting.RESET + "%",
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatTrans(
                aPlayer,
                looseFit ? "GT5U.chat.turbine.fitting.loose" : "GT5U.chat.turbine.fitting.tight");
        }
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 1, elementBudget, env, false, true);
    }

    @Override
    public void onPreviewStructureComplete(@NotNull ItemStack trigger) {
        mFormed = true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_LARGE_TURBINES_LOOP;
    }
}
