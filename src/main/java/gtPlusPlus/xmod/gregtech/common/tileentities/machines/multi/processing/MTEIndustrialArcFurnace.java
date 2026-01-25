package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialArcFurnace extends GTPPMultiBlockBase<MTEIndustrialArcFurnace>
    implements ISurvivalConstructable {

    // 862
    private static final int mCasingTextureID = TAE.getIndexFromPage(3, 3);
    public static String mCasingName = "Tempered Arc Furnace Casing";

    final static int MACHINE_MODE_ARC = 0;
    final static int MACHINE_MODE_PLASMA = 1;

    private int mSize = 0;
    private int mCasing;
    private static IStructureDefinition<MTEIndustrialArcFurnace> STRUCTURE_DEFINITION = null;

    public MTEIndustrialArcFurnace(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialArcFurnace(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialArcFurnace(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Arc Furnace, Plasma Arc Furnace, IAF";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("gt.recipe.arcfurnace", "gt.recipe.plasmaarcfurnace", "IAF")
            .addInfo("gt.iaf.tips.1")
            .addStaticSpeedInfo(3.5f)
            .addStaticEuEffInfo(1f)
            .addInfo("gt.iaf.tips.2")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addShiftInfo("GT5U.MBTT.Dimensions", "n", "n", 3, "GT5U.MBTT.Hollow")
            .addShiftInfo("gt.iaf.info.1")
            .addShiftInfo("GT5U.MBTT.Structure")
            .addController("top_center")
            .addCasingInfoMin(mCasingName, 10, false)
            .addInputBus("<casing>", 1)
            .addOutputBus("<casing>", 1)
            .addInputHatch("<casing>", 1)
            .addOutputHatch("<casing>", 1)
            .addEnergyHatch("<casing>", 1)
            .addMaintenanceHatch("<casing>", 1)
            .addMufflerHatch("<casing>", 1)
            .toolTipFinisher();
        return tt;
    }

    /**
     * The front part of multi. Used to determine the tier, or in other words, determine the size of multi.
     */
    private static final String STRUCTURE_PIECE_FRONT = "front";
    /**
     * The rest part of multi.
     */
    private static final String STRUCTURE_PIECE_REST = "rest";
    private static final int MAX_TIER = 3;

    @Override
    public IStructureDefinition<MTEIndustrialArcFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialArcFurnace>builder()
                .addShape(STRUCTURE_PIECE_FRONT + 1, new String[][] { { "CCC", "C~C", "CCC" } })
                .addShape(STRUCTURE_PIECE_FRONT + 2, new String[][] { { "CCCCC", "C   C", "C   C", "C   C", "CCCCC" } })
                .addShape(
                    STRUCTURE_PIECE_FRONT + 3,
                    new String[][] { { "CCCCCCC", "C     C", "C     C", "C     C", "C     C", "C     C", "CCCCCCC" }, })
                .addShape(STRUCTURE_PIECE_REST + 1, new String[][] { { "CCC", "C-C", "CCC" }, { "CCC", "CCC", "CCC" } })
                .addShape(
                    STRUCTURE_PIECE_REST + 2,
                    new String[][] { { "CCCCC", "C---C", "C---C", "C---C", "CCCCC" },
                        { "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" } })
                .addShape(
                    STRUCTURE_PIECE_REST + 3,
                    new String[][] { { "CCCCCCC", "C-----C", "C-----C", "C-----C", "C-----C", "C-----C", "CCCCCCC" },
                        { "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC" }, })
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialArcFurnace.class)
                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(getCasingTextureIndex())
                        .hint(1)
                        .allowOnly(ForgeDirection.NORTH)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings4Misc, 3))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private int getTierFromHint(ItemStack stackSize) {
        if (stackSize.stackSize <= 0 || stackSize.stackSize >= MAX_TIER) {
            return MAX_TIER;
        }
        return stackSize.stackSize;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int maxTier = getTierFromHint(stackSize);
        for (int tier = 1; tier <= maxTier; tier++) {
            buildPiece(STRUCTURE_PIECE_FRONT + tier, stackSize, hintsOnly, tier, tier, 0);
        }
        buildPiece(STRUCTURE_PIECE_REST + maxTier, stackSize, hintsOnly, maxTier, maxTier, -1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int maxTier = getTierFromHint(stackSize);
        int built;
        for (int tier = 1; tier <= maxTier; tier++) {
            built = survivalBuildPiece(
                STRUCTURE_PIECE_FRONT + tier,
                stackSize,
                tier,
                tier,
                0,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }

        return survivalBuildPiece(
            STRUCTURE_PIECE_REST + maxTier,
            stackSize,
            maxTier,
            maxTier,
            -1,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mSize = 0;
        int tier = 0;
        while (tier < MAX_TIER && checkPiece(STRUCTURE_PIECE_FRONT + (tier + 1), (tier + 1), (tier + 1), 0)) {
            tier++;
        }
        if (tier <= 0) return false;
        if (checkPiece(STRUCTURE_PIECE_REST + tier, tier, tier, -1)) {
            mSize = 2 * tier + 1;
            return mCasing >= 10 && checkHatch();
        }
        return false;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialArcFurnaceActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialArcFurnaceActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialArcFurnace;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialArcFurnaceGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return mCasingTextureID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return machineMode == MACHINE_MODE_PLASMA ? RecipeMaps.plasmaArcFurnaceRecipes : RecipeMaps.arcFurnaceRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.arcFurnaceRecipes, RecipeMaps.plasmaArcFurnaceRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (this.mSize * (machineMode == MACHINE_MODE_PLASMA ? 8 : 1)
            * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialArcFurnace;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings4Misc;
    }

    public byte getCasingMeta() {
        return 3;
    }

    public byte getCasingTextureIndex() {
        return (byte) mCasingTextureID;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (mSize <= 5) {
            GTUtility.sendChatToPlayer(aPlayer, "Cannot change mode, structure not large enough.");
            return;
        }
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSize", mSize);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mPlasmaMode")) {
            machineMode = aNBT.getBoolean("mPlasmaMode") ? MACHINE_MODE_PLASMA : MACHINE_MODE_ARC;
            aNBT.removeTag("mPlasmaMode");
        }
        mSize = aNBT.getInteger("mSize");
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.GTPP_MULTI_ARC_FURNACE.mode." + machineMode);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ARC_FURNACE_LOOP;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this) {

            @Override
            protected IWidget createModeSwitchButton(PanelSyncManager syncManager) {
                IntSyncValue machineModeSyncer = syncManager.findSyncHandler("machineMode", IntSyncValue.class);
                IntSyncValue sizeSyncer = new IntSyncValue(() -> mSize);
                syncManager.syncValue("arcSize", sizeSyncer);
                return new CycleButtonWidget() {

                    @NotNull
                    @Override
                    public Result onMousePressed(int mouseButton) {
                        if (sizeSyncer.getIntValue() <= 5) return Result.IGNORE;
                        return super.onMousePressed(mouseButton);
                    }
                }.size(18, 18)
                    .syncHandler("machineMode")
                    .length(machineModeIcons.size())
                    .overlay(new DynamicDrawable(() -> {
                        UITexture mode = getMachineModeIcon(machineModeSyncer.getValue());
                        return sizeSyncer.getIntValue() <= 5
                            ? new DrawableStack(mode, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN)
                            : mode;
                    }))
                    .tooltipBuilder(this::createModeSwitchTooltip);
            }
        }.withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_ARC,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_PLASMA_ARC);
    }
}
