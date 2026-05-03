package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialCokeOven extends MTEExtendedPowerMultiBlockBase<MTEIndustrialCokeOven>
    implements ISurvivalConstructable {

    private int tier = 0;
    private int width = 0;
    private int casingAmount;
    private HeatingCoilLevel coilLevel;
    private static final int MAX_LENGTH = 16;
    private static final float EU_MODIFIER = 0.98f;
    private static final int PARALLELS_T1 = 16;
    private static final int PARALLELS_T2 = 32;
    private static final int SLICE_PARALLELS_T1 = 8;
    private static final int SLICE_PARALLELS_T2 = 16;

    private static final int OFFSET_X_MAIN = 1;
    private static final int OFFSET_Y_MAIN = 5;
    private static final int OFFSET_Z_MAIN = 1;

    private static final int OFFSET_X_SLICE = -1;
    private static final int OFFSET_Y_SLICE = 5;
    private static final int OFFSET_Z_SLICE = 1;
    private static final String STRUCTURE_PIECE_FIRST = "main";
    private static final String STRUCTURE_PIECE_NEXT = "slice";

    public MTEIndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialCokeOven(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCokeOven(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Coke Oven, ICO")
            .addInfo("Processes Logs and Coal into Charcoal and Coal Coke.")
            .addInfo(
                TooltipHelper.parallelText(PARALLELS_T1) + " base and +"
                    + TooltipHelper.parallelText(SLICE_PARALLELS_T1)
                    + " Parallels per extra slice with Heat Resistant Casings")
            .addInfo(
                TooltipHelper.parallelText(PARALLELS_T2) + " base and +"
                    + TooltipHelper.parallelText(SLICE_PARALLELS_T2)
                    + " Parallels per extra slice with Heat Proof Casings")
            .addInfo(
                EnumChatFormatting.AQUA + "-2% "
                    + EnumChatFormatting.GRAY
                    + "EU Usage per "
                    + EnumChatFormatting.WHITE
                    + "Heating Coil"
                    + EnumChatFormatting.GRAY
                    + " Tier (multiplicatively)")
            .addInfo("Max 15 additional slices, eternal coils unlock unlimited slices")
            .addInfo("Infinity Coils and higher allow for single multi-amp energy hatch")
            .addMultiAmpHatchInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 7, 5, false)
            .addController("Front left center")
            .addStructureInfo(EnumChatFormatting.BLUE + "Base Structure:")
            .addCasingInfoMin("Structural Coke Oven Casing", 35, false)
            .addCasingInfoExactly("Heat Resistant/Proof Coke Oven Casing", 8, true)
            .addCasingInfoExactly("Heating Coils", 8, true)
            .addCasingInfoExactly("Steel Pipe Casing", 7, false)
            .addCasingInfoExactly("Steel Frame Box", 10, false)
            .addStructureInfo(EnumChatFormatting.BLUE + "Each additional slice:")
            .addCasingInfoExactly("Structural Coke Oven Casing", 19, false)
            .addCasingInfoExactly("Heat Resistant/Proof Coke Oven Casing", 5, true)
            .addCasingInfoExactly("Heating Coils", 8, true)
            .addCasingInfoExactly("Steel Pipe Casing", 3, false)
            .addCasingInfoExactly("Steel Frame Box", 10, false)
            .addInputBus("Any Structural Coke Oven Casing of the base structure", 1)
            .addOutputBus("Any Structural Coke Oven Casing of the base structure", 1)
            .addInputHatch("Any Structural Coke Oven Casing of the base structure", 1)
            .addOutputHatch("Any Structural Coke Oven Casing of the base structure", 1)
            .addEnergyHatch("Any Structural Coke Oven Casing of the base structure", 1)
            .addMaintenanceHatch("Any Structural Coke Oven Casing of the base structure", 1)
            .addMufflerHatch("Any Structural Coke Oven Casing of the base structure", 1)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addSubChannelUsage(GTStructureChannels.COKE_OVEN_CASING)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Nicouuuuu")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialCokeOven> getStructureDefinition() {
        IStructureDefinition<MTEIndustrialCokeOven> STRUCTURE_DEFINITION = StructureDefinition
            .<MTEIndustrialCokeOven>builder()
            .addShape(
                STRUCTURE_PIECE_FIRST,
                new String[][] { { "      ", "   C  ", "   C  ", "   C  ", "   C  ", "   C  ", "   DDD" },
                    { "      ", "   E  ", "  DBD ", " DDBD ", "DDDBD ", "D~DBD ", "DDDDDD" },
                    { " AAA  ", " A A  ", " AD D ", " AE E ", "D E E ", "D E E ", "DDDDDD" },
                    { "      ", "   E  ", "  DBD ", " DDBD ", "DDDBD ", "DDDBD ", "DDDDDD" },
                    { "      ", "   C  ", "   C  ", "   C  ", "   C  ", "   C  ", "   DDD" } })
            .addShape(
                STRUCTURE_PIECE_NEXT,
                new String[][] { { "    ", " C  ", " C  ", " C  ", " C  ", " C  ", " FFF" },
                    { "    ", " E  ", " BF ", " BF ", " BF ", " BF ", " FFF" },
                    { "AA  ", " A  ", "  F ", "  E ", "  E ", "  E ", " FFF" },
                    { "    ", " E  ", " BF ", " BF ", " BF ", " BF ", " FFF" },
                    { "    ", " C  ", " C  ", " C  ", " C  ", " C  ", " FFF" } })
            .addElement(
                'D',
                buildHatchAdder(MTEIndustrialCokeOven.class)
                    .atLeast(
                        InputBus,
                        OutputBus,
                        InputHatch,
                        OutputHatch,
                        Maintenance,
                        Energy.or(MultiAmpEnergy),
                        Muffler)
                    .casingIndex(Casings.StructuralCokeOvenCasing.textureId)
                    .hint(1)
                    .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.StructuralCokeOvenCasing.asElement())))
            .addElement('A', Casings.SteelPipeCasing.asElement())
            .addElement(
                'B',
                GTStructureChannels.HEATING_COIL
                    .use(activeCoils(ofCoil(MTEIndustrialCokeOven::setCoilLevel, MTEIndustrialCokeOven::getCoilLevel))))
            .addElement('C', ofFrame(Materials.Steel))
            .addElement(
                'E',
                GTStructureChannels.COKE_OVEN_CASING.use(
                    ofBlocksTiered(
                        (block, meta) -> block == ModBlocks.blockCasingsMisc ? (meta == 2 ? 0 : meta == 3 ? 1 : null)
                            : null,
                        ImmutableList
                            .of(Pair.of(ModBlocks.blockCasingsMisc, 2), Pair.of(ModBlocks.blockCasingsMisc, 3)),
                        -1,
                        (t, tier) -> t.tier = tier,
                        t -> t.tier)))
            .addElement('F', onElementPass(x -> ++x.casingAmount, Casings.StructuralCokeOvenCasing.asElement()))
            .build();
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int extraSlices;
        if (getCoilTier() >= HeatingCoilLevel.MAX.getTier() + 1) {
            extraSlices = stackSize.stackSize;
        } else {
            extraSlices = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 1, MAX_LENGTH);
        }
        buildPiece(STRUCTURE_PIECE_FIRST, stackSize, hintsOnly, OFFSET_X_MAIN, OFFSET_Y_MAIN, OFFSET_Z_MAIN);
        for (int i = 1; i < extraSlices; i++) {
            buildPiece(
                STRUCTURE_PIECE_NEXT,
                stackSize,
                hintsOnly,
                OFFSET_X_SLICE - (i * 2),
                OFFSET_Y_SLICE,
                OFFSET_Z_SLICE);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int extraSlices;

        if (getCoilTier() >= HeatingCoilLevel.MAX.getTier() + 1) {
            extraSlices = stackSize.stackSize;
        } else {
            extraSlices = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 1, MAX_LENGTH);
        }
        int built = 0, temp;

        temp = survivalBuildPiece(
            STRUCTURE_PIECE_FIRST,
            stackSize,
            OFFSET_X_MAIN,
            OFFSET_Y_MAIN,
            OFFSET_Z_MAIN,
            elementBudget - built,
            env,
            false,
            true);
        if (temp > -1) built += temp;
        if (elementBudget - built <= 0) return built;

        for (int i = 1; i < extraSlices; i++) {
            temp = survivalBuildPiece(
                STRUCTURE_PIECE_NEXT,
                stackSize,
                OFFSET_X_SLICE - (i * 2),
                OFFSET_Y_SLICE,
                OFFSET_Z_SLICE,
                elementBudget - built,
                env,
                false,
                true);
            if (temp > -1) built += temp;
            if (elementBudget - built <= 0) return built;
        }

        return built == 0 ? -1 : built;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        tier = -1;
        width = 0;
        setCoilLevel(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_FIRST, OFFSET_X_MAIN, OFFSET_Y_MAIN, OFFSET_Z_MAIN, errors)) {
            return;
        }

        if (getCoilTier() >= HeatingCoilLevel.MAX.getTier() + 1) {
            while (checkPiece(STRUCTURE_PIECE_NEXT, OFFSET_X_SLICE - (width + 1) * 2, OFFSET_Y_SLICE, OFFSET_Z_SLICE, errors)) {
                width++;
            }
        } else {
            while (width < MAX_LENGTH - 1
                && checkPiece(STRUCTURE_PIECE_NEXT, OFFSET_X_SLICE - (width + 1) * 2, OFFSET_Y_SLICE, OFFSET_Z_SLICE, errors)) {
                width++;
            }
        }
        errors.clear();

        if (casingAmount < 35) errors.add(StructureErrors.missingCasings(casingAmount, 35));
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) errors.add(StructureErrorRegistry.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER);
            if (mExoticEnergyHatches.size() != 1) errors.add(StructureErrorRegistry.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER);
        } else {
            checkHasEnergyHatch(errors);
        }
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_OP_CLICK;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] { Casings.StructuralCokeOvenCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCACokeOvenActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCACokeOvenActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.StructuralCokeOvenCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCACokeOven)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCACokeOvenGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.StructuralCokeOvenCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.cokeOvenRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifierSupplier(this::getEuModifier);
    }

    @Override
    public int getMaxParallelRecipes() {
        int base = (tier == 0) ? PARALLELS_T1 : PARALLELS_T2;
        int perSlice = (tier == 0) ? SLICE_PARALLELS_T1 : SLICE_PARALLELS_T2;
        return base + (width * perSlice);
    }

    public double getEuModifier() {
        return Math.pow(EU_MODIFIER, getCoilTier());
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCokeOven;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    public int getCoilTier() {
        return this.coilLevel == null ? 0 : (int) this.coilLevel.getTier() + 1;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }

    public float euModifier(int coilTier) {
        return (float) Math.pow(EU_MODIFIER, coilTier);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("coilTier", getCoilTier());
        tag.setInteger("parallels", getMaxParallelRecipes());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.coilLevel") + ": "
                + EnumChatFormatting.WHITE
                + Math.max(0, tag.getInteger("coilTier")));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.euModifier") + ": "
                + EnumChatFormatting.WHITE
                + formatNumber(euModifier(tag.getInteger("coilTier")) * 100)
                + "%");
    }

    @Override
    public boolean supportsInputSeparation() {
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
    public boolean supportsBatchMode() {
        return true;
    }
}
