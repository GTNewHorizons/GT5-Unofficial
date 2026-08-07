package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.VOID;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.CentrifugeRecipeKey;
import gregtech.api.render.RenderOverlay;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtilityClient;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.material.MaterialsAlloy;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialCentrifuge extends MTEExtendedPowerMultiBlockBase<MTEIndustrialCentrifuge>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String MOMENTUM_NBT = "momentum";
    private static IStructureDefinition<MTEIndustrialCentrifuge> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    protected final List<RenderOverlay.OverlayTicket> overlayTickets = new ArrayList<>();

    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 1;

    private static final int BASE_PARALLEL_PER_TIER = 4;
    private static final float SPEED = 2f;
    private static final float EXTRA_SPEED = 1f;
    private static final float MAX_SPEED = SPEED + EXTRA_SPEED;
    private static final float EU_EFFICIENCY = 0.9f;

    private int momentum = 0;
    private int runningTickCounter = 0;

    public MTEIndustrialCentrifuge(final int id, final String name, final String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTEIndustrialCentrifuge(final String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity tileEntity) {
        return new MTEIndustrialCentrifuge(this.mName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialCentrifuge>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    // spotless:off
                    new String[][]{{
                        " CCC ",
                        "C   C",
                        "C   C",
                        "C   C",
                        " CCC "
                    },{
                        " AAA ",
                        "ACCCA",
                        "AC~CA",
                        "ACCCA",
                        " AAA "
                    },{
                        "  C  ",
                        " BBB ",
                        "CBBBC",
                        " BBB ",
                        "  C  "
                    },{
                        "  C  ",
                        " BBB ",
                        "CBBBC",
                        " BBB ",
                        "  C  "
                    },{
                        " AAA ",
                        "ACCCA",
                        "ACCCA",
                        "ACCCA",
                        " AAA "
                    }})
                //spotless:on
                .addElement('A', ofFrame(MaterialsAlloy.EGLIN_STEEL))
                .addElement('B', Casings.LargeSieveGrate.asElement())
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialCentrifuge.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(Casings.CentrifugeCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(
                                MTEIndustrialCentrifuge::onCasingAdded,
                                Casings.CentrifugeCasing.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity mte, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            LARGETURBINE_NEW5,
            VOID,
            LARGETURBINE_NEW_ACTIVE5,
            VOID);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.CentrifugeCasing.getCasingTexture();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Centrifuge")
            .addInfo(
                TooltipHelper.parallelText(BASE_PARALLEL_PER_TIER) + " - "
                    + TooltipHelper.parallelText(BASE_PARALLEL_PER_TIER * 2)
                    + " Parallels per "
                    + TooltipHelper.coloredText("Voltage", TooltipHelper.TIER_COLOR)
                    + " Tier")
            .addInfo(TooltipHelper.speedText(SPEED) + " - " + TooltipHelper.speedText(MAX_SPEED) + " Speed")
            .addInfo(
                TooltipHelper.coloredText("Parallels", TooltipHelper.PARALLEL_COLOR) + " and "
                    + TooltipHelper.coloredText("Speed", TooltipHelper.SPEED_COLOR)
                    + " increase as the machine gains momentum")
            .addInfo("Momentum is lost at four times the rate it is gained")
            .addStaticEuEffInfo(EU_EFFICIENCY)
            .addInfo("Disable animations with a screwdriver")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 5, true)
            .addController("Front center, 3rd layer")
            .addCasing("6-32", "Centrifuge Casing", false)
            .addCasing("24", "Eglin Steel Frame Box", false)
            .addCasing("18", "Large Sieve Grate", false)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addMufflerHatch("1", "Any casing", 1)
            .addInputAny("1+", "Any casing", 1)
            .addOutputAny("1+", "Any casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Ducked")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                // ensures heavy mode recipes can not run
                if (recipe.getMetadataOrDefault(CentrifugeRecipeKey.INSTANCE, Boolean.FALSE))
                    return CheckRecipeResultRegistry.NO_RECIPE;
                return super.validateRecipe(recipe);
            }
        }.setEuModifier(EU_EFFICIENCY)
            .setSpeedBonusSupplier(this::getSpeedWithMomentum)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    private Double getSpeedWithMomentum() {
        return 1D / (SPEED + EXTRA_SPEED * momentum / 100);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) ((BASE_PARALLEL_PER_TIER + BASE_PARALLEL_PER_TIER * momentum / 100F)
            * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void getExtraInfoData(List<String> info) {
        info.add(IGregTechDeviceInformation.encode("GT5U.Centrifuge.momentum", formatNumber(momentum)));
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 6);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    private void updateTurbineOverlay() {
        IGregTechTileEntity tile = getBaseMetaTileEntity();
        if (tile == null || tile.isServerSide()) return;

        GTUtilityClient.setTurbineOverlay(
            tile.getWorld(),
            tile.getXCoord(),
            tile.getYCoord(),
            tile.getZCoord(),
            getExtendedFacing(),
            getBaseMetaTileEntity().isActive() ? TURBINE_NEW_ACTIVE : TURBINE_NEW,
            overlayTickets);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        updateTurbineOverlay();
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setInteger(MOMENTUM_NBT, momentum);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        if (nbt.hasKey(MOMENTUM_NBT)) momentum = nbt.getInteger(MOMENTUM_NBT);
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        runningTickCounter++;
        if (runningTickCounter % 10 == 0 && momentum < 100) {
            runningTickCounter = 0;
            momentum++;
        }
        return super.onRunningTick(stack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);
        if (!baseMetaTileEntity.isServerSide()) return;
        if (mMaxProgresstime == 0 && momentum > 0) {
            if (tick % 5 == 0) {
                momentum = Math.max(momentum - 2, 0);
            }
        }
    }

    @Override
    public void onTextureUpdate() {
        updateTurbineOverlay();
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newFacing) {
        boolean changed = newFacing != getExtendedFacing();
        super.setExtendedFacing(newFacing);
        if (changed) updateTurbineOverlay();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (getBaseMetaTileEntity().isClientSide()) GTUtilityClient.clearTurbineOverlay(overlayTickets);
    }

    @Override
    public int getPollutionPerSecond(final ItemStack stack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCentrifuge;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.centrifugeNonCellRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
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
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("momentum", momentum);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector
                .translateToLocalFormatted("GT5U.Centrifuge.momentum", formatNumber(tag.getInteger("momentum"))));
    }
}
