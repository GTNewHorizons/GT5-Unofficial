package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.metatileentity.implementations.MTEBasicMachine.isValidForLowGravity;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ProcessingArrayManager;
import gregtech.common.blocks.ItemMachines;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@Deprecated
public class MTEProcessingArray extends MTEExtendedPowerMultiBlockBase<MTEProcessingArray>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEProcessingArray> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEProcessingArray>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "hhh", "hhh", "hhh" }, { "h~h", "h-h", "hhh" }, { "hhh", "hhh", "hhh" }, }))
        .addElement(
            'h',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEProcessingArray>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .casingIndex(48)
                        .dot(1)
                        .build()),
                onElementPass(t -> t.mCasingAmount++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
        .build();

    private int mCasingAmount = 0;

    private RecipeMap<?> mLastRecipeMap;
    private ItemStack lastControllerStack;
    private int tTier = 0;
    private int mMult = 0;
    private boolean downtierUEV = true;

    public MTEProcessingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEProcessingArray(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEProcessingArray(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Processing Array")
            .addInfo("Runs supplied machines as if placed in the world")
            .addInfo("Place up to 64 singleblock GT machines into the controller")
            .addInfo("Note that you still need to supply power to them all")
            .addInfo("Use a screwdriver to enable separate input busses")
            .addInfo("Use a wire cutter to disable UEV+ downtiering")
            .addInfo("Doesn't work on certain machines, deal with it")
            .addInfo("Use it if you hate GT++, or want even more speed later on")
            .addInfo(
                EnumChatFormatting.GOLD
                    + "On the way to be slowly removed. Use it strictly if you have no alternative.")
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoRange("Robust Tungstensteel Machine Casing", 14, 24, false)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addInputBus("Any casing", 1)
            .addInputHatch("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addOutputHatch("Any casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48] };
    }

    private RecipeMap<?> fetchRecipeMap() {
        if (isCorrectMachinePart(getControllerSlot())) {
            // Gets the recipe map for the given machine through its unlocalized name
            return ProcessingArrayManager.giveRecipeMap(ProcessingArrayManager.getMachineName(getControllerSlot()));
        }
        return null;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return mLastRecipeMap;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return aStack != null && aStack.getUnlocalizedName()
            .startsWith("gt.blockmachines.");
    }

    @Override
    protected void sendStartMultiBlockSoundLoop() {
        SoundResource sound = ProcessingArrayManager
            .getSoundResource(ProcessingArrayManager.getMachineName(getControllerSlot()));
        if (sound != null) {
            sendLoopStart((byte) sound.id);
        }
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        SoundResource sound = SoundResource.get(aIndex < 0 ? aIndex + 256 : 0);
        if (sound != null) {
            GTUtility.doSoundAtClient(sound, getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (!GTUtility.areStacksEqual(lastControllerStack, getControllerSlot())) {
            // controller slot has changed
            lastControllerStack = getControllerSlot();
            mLastRecipeMap = fetchRecipeMap();
            setTierAndMult();
        }
        if (mLastRecipeMap == null) return SimpleCheckRecipeResult.ofFailure("no_machine");
        if (mLockedToSingleRecipe && mSingleRecipeCheck != null) {
            if (mSingleRecipeCheck.getRecipeMap() != mLastRecipeMap) {
                return SimpleCheckRecipeResult.ofFailure("machine_mismatch");
            }
        }

        return super.checkProcessing();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (GTMod.gregtechproxy.mLowGravProcessing && recipe.mSpecialValue == -100
                    && !isValidForLowGravity(recipe, getBaseMetaTileEntity().getWorld().provider.dimensionId)) {
                    return SimpleCheckRecipeResult.ofFailure("high_gravity");
                }
                if (recipe.mEUt > availableVoltage) return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallel);
    }

    @Override
    protected boolean canUseControllerSlotForRecipe() {
        return false;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTValues.V[tTier] * (mLastRecipeMap != null ? mLastRecipeMap.getAmperage() : 1));
        logic.setAvailableAmperage(getMaxParallel());
        logic.setAmperageOC(false);
    }

    private void setTierAndMult() {
        IMetaTileEntity aMachine = ItemMachines.getMetaTileEntity(getControllerSlot());
        if (aMachine instanceof MTETieredMachineBlock) {
            tTier = ((MTETieredMachineBlock) aMachine).mTier;
        } else {
            tTier = 0;
        }
        mMult = 0;
        if (downtierUEV && tTier > 9) {
            tTier--; // Lowers down the tier by 1 to allow for bigger parallel
            mMult = 2; // Multiplies Parallels by 4x, keeping the energy cost
        }
    }

    private int getMaxParallel() {
        if (getControllerSlot() == null) {
            return 0;
        }
        return getControllerSlot().stackSize << mMult;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (mMachine && aTick % 20 == 0) {
            for (MTEHatchInputBus tInputBus : mInputBusses) {
                tInputBus.mRecipeMap = mLastRecipeMap;
            }
            for (MTEHatchInput tInputHatch : mInputHatches) {
                tInputHatch.mRecipeMap = mLastRecipeMap;
            }
        }
    }

    @Override
    public IStructureDefinition<MTEProcessingArray> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    private boolean checkHatches() {
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("downtierUEV", downtierUEV);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mSeparate")) {
            // backward compatibility
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        if (aNBT.hasKey("mUseMultiparallelMode")) {
            // backward compatibility
            batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
        downtierUEV = aNBT.getBoolean("downtierUEV");
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            // Lock to single recipe
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
        } else {
            inputSeparation = !inputSeparation;
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatToPlayer(aPlayer, "Batch recipes");
            } else {
                GTUtility.sendChatToPlayer(aPlayer, "Don't batch recipes");
            }
        } else {
            downtierUEV = !downtierUEV;
            GTUtility.sendChatToPlayer(aPlayer, "Treat UEV+ machines as multiple UHV " + downtierUEV);
        }
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    private List<IHatchElement<? super MTEProcessingArray>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy, ExoticEnergy);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mExoticEnergyHatches.clear();
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0) && mCasingAmount >= 14 && checkHatches();
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatch tHatch : filterValidMTEs(mExoticEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility
                    .formatNumbers(ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList()))
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + GTUtility
                    .formatNumbers(ExoticEnergyInputHelper.getMaxInputAmpsMulti(getExoticAndNormalEnergyHatchList()))
                + "A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility
                    .getTier(ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList()))]
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.PA.machinetier") + ": "
                + EnumChatFormatting.GREEN
                + tTier
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.PA.discount")
                + ": "
                + EnumChatFormatting.GREEN
                + 1
                + EnumChatFormatting.RESET
                + " x",
            StatCollector.translateToLocal("GT5U.PA.parallel") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(getMaxParallel())
                + EnumChatFormatting.RESET };
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    protected boolean supportsSlotAutomation(int aSlot) {
        return aSlot == getControllerSlotIndex();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            downtierUEV = !downtierUEV;
            setTierAndMult();
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                if (downtierUEV) {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                        GTUITextures.OVERLAY_BUTTON_DOWN_TIERING_ON };
                } else {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                        GTUITextures.OVERLAY_BUTTON_DOWN_TIERING_OFF };
                }
            })
            .setPos(80, 91)
            .setSize(16, 16)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.down_tier"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> downtierUEV, val -> downtierUEV = val));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (mLastRecipeMap != null && getControllerSlot() != null) {
            tag.setString("type", getControllerSlot().getDisplayName());
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("type")) {
            currentTip.add("Machine: " + EnumChatFormatting.YELLOW + tag.getString("type"));
        } else {
            currentTip.add("Machine: " + EnumChatFormatting.YELLOW + "None");
        }
    }
}
