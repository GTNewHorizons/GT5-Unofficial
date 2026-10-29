package gregtech.common.tileentities.machines.multi.compressor;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;
import static gregtech.api.util.GTStructureUtility.activeBlocks;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.util.MathUtils;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHeatProducer;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHeatSensor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHIPCompressor extends MTEExtendedPowerMultiBlockBase<MTEHIPCompressor>
    implements ISurvivalConstructable, IHeatProducer, ICasingTextureProvider {

    private static final double SPEED_BONUS = 1.0D / 3.5D;
    private static final double EU_MODIFIER = 0.75D;
    private static final double OVERHEATED_SPEED_BONUS = 2.5D;
    private static final double OVERHEATED_EU_MODIFIER = 1.1D;
    private static final int PARALLELS_PER_TIER = 4;
    private static final int OVERHEATED_PARALLELS_PER_TIER = 1;
    private static final int HEAT_PER_SECOND = 5;
    private static final double COIL_HEAT_FACTOR = 0.9;
    private static final int BASE_COOLING_PER_SECOND = 2;
    private static final int COOLING_INCREASE_PER_SECOND = 2;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEHIPCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEHIPCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"               ","               ","               "," CCCCCC DDDDDD ","               ","               ","               "},
                {"               ","               ","               "," C    C D    D ","               ","               ","               "},
                {"               ","      HHH      ","     HHHHH     "," C   HHHHH   D ","     HHHHH     ","      HHH      ","               "},
                {"      BBB      ","     BBBBB     ","    BBBBBBB    "," C  BBBBBBB  D ","    BBBBBBB    ","     BBBBB     ","      BBB      "},
                {"      HHH      ","     B   B     ","    BF   FB    "," C  BF   FB  D ","    BF   FB    ","     B   B     ","      HHH      "},
                {"      HAH      ","     B   B     ","    GF   FG    "," C  GF   FG  D ","    GF   FG    ","     B   B     ","      HAH      "},
                {"      HAH      ","     B   B     "," B  GF   FG  B ","BBB GF   FG BBB"," B  GF   FG  B ","     B   B     ","      HAH      "},
                {"      HAH      ","     B   B     "," A  GF   FG  A ","A A GF   FG A A"," A  GF   FG  A ","     B   B     ","      HAH      "},
                {"      HHH      ","     B   B     "," A  BF   FB  A ","A A BF   FB A A"," A  BF   FB  A ","     B   B     ","      HHH      "},
                {"      B~B      ","     BBBBB     "," B  BBBBBBB  B ","BBB BBBBBBB BBB"," B  BBBBBBB  B ","     BBBBB     ","      BBB      "}
            }))
            //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement(
            'B',
            buildHatchAdder(MTEHIPCompressor.class)
                .atLeast(Maintenance, Energy, MTEHeatSensor.HeatSensorHatchElement.HeatSensor)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(4))
                .hint(1)
                .buildAndChain(onElementPass(MTEHIPCompressor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 4))))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 9))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings10, 10))
        .addElement(
            'F',
            GTStructureChannels.HEATING_COIL
                .use(activeBlocks(ofCoil(MTEHIPCompressor::setCoilLevel, MTEHIPCompressor::getCoilLevel))))
        .addElement(
            'G',
            buildHatchAdder(MTEHIPCompressor.class).atLeast(InputBus, OutputBus, InputHatch)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(5))
                .hint(2)
                .buildAndChain(onElementPass(MTEHIPCompressor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 5))))
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings10, 5))
        .build();

    private final ArrayList<MTEHeatSensor> sensorHatches = new ArrayList<>();

    private HeatingCoilLevel heatLevel;
    private int coilTier = 0;

    private float heat = 0;
    private boolean overheated = false;

    public MTEHIPCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEHIPCompressor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEHIPCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHIPCompressor(this.mName);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        boolean oldOverheated = overheated;
        overheated = (aValue & 1) == 1;
        if (oldOverheated != overheated) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) (overheated ? 1 : 0);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive || overheated,
            OVERLAY_FRONT_MULTI_COMPRESSOR,
            OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW,
            overheated ? OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING : OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE,
            overheated ? OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING_GLOW : OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 4));
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.isostatic_press_compressor"))
            .addMarkdown(
                new ResourceLocation("gregtech", "hip-compressor"),
                ImmutableMap.<String, Object>builder()
                    .put("heat", HEAT_PER_SECOND)
                    .put("coil_factor", formatNumber(COIL_HEAT_FACTOR))
                    .put("base_cooling", BASE_COOLING_PER_SECOND)
                    .put("cooling_increase", COOLING_INCREASE_PER_SECOND)
                    .put("speed", Math.round(100 / SPEED_BONUS))
                    .put("overheated_speed", Math.round(100 / OVERHEATED_SPEED_BONUS))
                    .put("eu", Math.round(EU_MODIFIER * 100))
                    .put("overheated_eu", Math.round(OVERHEATED_EU_MODIFIER * 100))
                    .put("parallels", PARALLELS_PER_TIER)
                    .put("overheated_parallels", OVERHEATED_PARALLELS_PER_TIER)
                    .build())
            .beginStructureBlock(15, 10, 7, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("95-124", Casings.ElectricCompressorCasing.getLocalizedName(), false)
            .addCasing("60-61", Casings.CompressionPipeCasing.getLocalizedName(), false)
            .addCasing("30", StatCollector.translateToLocal("GT5U.tooltip.structure.heating_coil"), true)
            .addCasing("22", StatCollector.translateToLocal("gt.mbtt.structure.any_tiered_glass"), false)
            .addCasing("12", Casings.CoolantDuct.getLocalizedName(), false)
            .addCasing("12", Casings.HeatingDuct.getLocalizedName(), false)
            .addMiscHatch("0+", StatCollector.translateToLocal("GT5U.tooltip.structure.heat_sensor_hatch"), StatCollector.translateToLocal("gt.mbtt.structure.any_electric_compressor_casing"), 1)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_electric_compressor_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_electric_compressor_casing"), 1)
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_side_pipe_casing"), 2)
            .addInputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_side_pipe_casing"), 2)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_side_pipe_casing"), 2)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 9, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 9, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        setCoilLevel(HeatingCoilLevel.None);
        mCasingAmount = 0;
        sensorHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 9, 0, errors)) return;
        checkCasingMin(errors, mCasingAmount, 95);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setFloat("heat", heat);
        aNBT.setBoolean("cooling", overheated);
        aNBT.setInteger("coilTier", coilTier);
        aNBT.setInteger("coolingTimer", coolingTimer);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("heat")) heat = aNBT.getFloat("heat");
        if (aNBT.hasKey("cooling")) overheated = aNBT.getBoolean("cooling");
        if (aNBT.hasKey("coilTier")) coilTier = aNBT.getInteger("coilTier");
        if (aNBT.hasKey("coolingTimer")) coolingTimer = aNBT.getInteger("coolingTimer");
        super.loadNBTData(aNBT);
    }

    @Override
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setInteger("heat", Math.round(heat));
        tag.setBoolean("cooling", overheated);
    }

    @Override
    public void getExtraWailaBody(ItemStack itemStack, List<String> list, NBTTagCompound tag,
        IWailaDataAccessor accessor, IWailaConfigHandler config) {
        list.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.hip_compressor.heat",
                "" + (tag.getBoolean("cooling") ? EnumChatFormatting.RED : EnumChatFormatting.AQUA)
                    + EnumChatFormatting.BOLD
                    + tag.getInteger("heat")
                    + "%"
                    + EnumChatFormatting.RESET));
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                setSpeedBonus(SPEED_BONUS);
                setEuModifier(EU_MODIFIER);

                // Nerf when heated
                if (overheated) {
                    setSpeedBonus(OVERHEATED_SPEED_BONUS);
                    setEuModifier(OVERHEATED_EU_MODIFIER);
                }

                // If Black Hole required, no recipe
                if (recipe.getMetadataOrDefault(COMPRESSION_TIER, 0) == 2) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                return super.validateRecipe(recipe);
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                // If HIP required, check for overheat and potentially crash
                if (recipe.getMetadataOrDefault(COMPRESSION_TIER, 0) == 1) {
                    if (overheated) {
                        stopMachine(ShutDownReasonRegistry.OVERHEAT_FAIL);
                        return CheckRecipeResultRegistry.NO_RECIPE;
                    }
                }
                return super.onRecipeStart(recipe);
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    private int coolingTimer = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aTick % 20 != 0 || aBaseMetaTileEntity.isClientSide()) return;

        // Default to cooling by 2%
        float heatMod = -BASE_COOLING_PER_SECOND;

        // If the machine is running, heat by 5% x 0.90 ^ (Coil Tier)
        // Cupronickel is 0, so base will be 5% increase
        // Also reset cooling speed
        if (this.maxProgresstime() != 0) {
            heatMod = (float) (HEAT_PER_SECOND * GTUtility.powInt(COIL_HEAT_FACTOR, coilTier));
            coolingTimer = 0;
        } else {
            // If the machine isn't running, add and increment the cooling timer
            heatMod -= coolingTimer;
            coolingTimer += COOLING_INCREASE_PER_SECOND;
        }

        heat = MathUtils.clamp(heat + heatMod, 0, 100);

        // Switch overheated conditionally and reset the cooling speed
        if ((overheated && heat <= 0) || (!overheated && heat >= 100)) {
            overheated = !overheated;
            coolingTimer = 0;
        }

        // Update all the sensors
        for (MTEHeatSensor hatch : sensorHatches) {
            hatch.setHeatValue(heat);
        }

    }

    @Override
    public int getMaxParallelRecipes() {
        return (overheated ? OVERHEATED_PARALLELS_PER_TIER : PARALLELS_PER_TIER)
            * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    public HeatingCoilLevel getCoilLevel() {
        return heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        heatLevel = aCoilLevel;
        coilTier = aCoilLevel.getTier();
    }

    @Override
    public List<MTEHeatSensor> getHeatSensorHatches() {
        return Collections.unmodifiableList(sensorHatches);
    }

    @Override
    public boolean addHeatSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof MTEHeatSensor sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            return sensorHatches.add(sensor);
        }
        return false;
    }
}
