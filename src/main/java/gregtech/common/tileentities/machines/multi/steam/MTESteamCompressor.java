package gregtech.common.tileentities.machines.multi.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.GregTechAPI.sBlockMetal6;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBlockBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamCompressor extends MTESteamMultiBlockBase<MTESteamCompressor> implements ISurvivalConstructable {

    public MTESteamCompressor(String aName) {
        super(aName);
    }

    public MTESteamCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTESteamCompressor(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Compressor";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_LEGACY = "legacy";

    private IStructureDefinition<MTESteamCompressor> STRUCTURE_DEFINITION = null;

    private static final int HORIZONTAL_OFFSET = 1;
    private static final int VERTICAL_OFFSET = 1;
    private static final int DEPTH_OFFSET = 0;

    private int casingAmount = 0;

    private int tierMachine = 0;

    private int tierMachineCasing = -1;
    private int tierBlock = -1;
    private int tierPipeCasing = -1;

    @Nullable
    public Integer getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) {
            casingAmount++;
            return 1;
        }
        if (block == sBlockCasings2 && 0 == meta) {
            casingAmount++;
            return 2;
        }
        return null;
    }

    @Nullable
    public Integer getTierGearboxCasing(Block block, int meta) {
        if (block == sBlockCasings2 && meta == 2) {
            return 1;
        }
        if (block == sBlockCasings2 && meta == 3) {
            return 2;
        }
        return null;
    }

    @Nullable
    public static Integer getTierBlock(Block block, int meta) {
        if (block == Blocks.iron_block) {
            return 1;
        }
        if (block == sBlockMetal6 && meta == 13) {
            return 2;
        }
        return null;
    }

    @Nullable
    public static Integer getTierPipe(Block block, int meta) {
        if (block == Casings.BronzePipeCasing.getBlock() && meta == Casings.BronzePipeCasing.getBlockMeta()) {
            return 1;
        }
        if (block == Casings.SteelPipeCasing.getBlock() && meta == Casings.SteelPipeCasing.getBlockMeta()) {
            return 2;
        }
        return null;
    }

    protected void updateHatchTexture() {
        for (MTEHatch h : mSteamInputs) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mSteamOutputs) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(getCasingTextureId());
    }

    @Override
    protected boolean isHighPressure() {
        return tierMachineCasing == 2 || tierBlock == 2 || tierPipeCasing == 2;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        tierMachineCasing = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) tierMachineCasing;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE;
    }

    @Override
    public IStructureDefinition<MTESteamCompressor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamCompressor>builder()
                // spotless:off
                .addShape(STRUCTURE_PIECE_LEGACY, transpose( new String[][] {
                    { "AAA", "AAA", "AAA", "AAA" },
                    { "A~A", "A-A", "A-A", "AAA" },
                    { "AAA", "AAA", "AAA", "AAA" }}))
                .addShape(STRUCTURE_PIECE_MAIN, new String[][]{
                    {"CCC", "C~C", "CCC"},
                    {"AAA", "AAA", "AAA"},
                    {"   ", " C ", "AAA"},
                    {"   ", " C ", "AAA"},
                    {"EEE", "EEE", "AAA"},
                    {"   ", "   ", "AAA"},
                    {"AAA", "AAA", "AAA"}})
                // spotless:on
                .addElement(
                    'A',
                    ofChain(
                        buildSteamInput(MTESteamCompressor.class).casingIndex(10)
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTESteamCompressor.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                            .casingIndex(10)
                            .hint(1)
                            .buildAndChain(),
                        ofBlocksTiered(
                            this::getTierMachineCasing,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            -1,
                            (t, m) -> t.tierMachineCasing = m,
                            t -> t.tierMachineCasing)))
                .addElement(
                    'C',
                    ofBlocksTiered(
                        MTESteamCompressor::getTierPipe,
                        ImmutableList.of(
                            Pair.of(Casings.BronzePipeCasing.getBlock(), Casings.BronzePipeCasing.getBlockMeta()),
                            Pair.of(Casings.SteelPipeCasing.getBlock(), Casings.SteelPipeCasing.getBlockMeta())),
                        -1,
                        (t, m) -> t.tierPipeCasing = m,
                        t -> t.tierPipeCasing))
                .addElement(
                    'E',
                    ofBlocksTiered(
                        MTESteamCompressor::getTierBlock,
                        ImmutableList.of(Pair.of(Blocks.iron_block, 0), Pair.of(sBlockMetal6, 13)),
                        -1,
                        (t, m) -> t.tierBlock = m,
                        t -> t.tierBlock))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Try legacy structure first
        tierMachineCasing = -1;
        tierBlock = -1;
        tierPipeCasing = -1;
        casingAmount = 0;

        if (checkPiece(STRUCTURE_PIECE_LEGACY, 1, 1, 0)) {
            if (casingAmount >= 14 && checkHatches()) {
                if (tierMachineCasing == 1) {
                    updateHatchTexture();
                    tierMachine = 1;
                    return true;
                }
                if (tierMachineCasing == 2) {
                    updateHatchTexture();
                    tierMachine = 2;
                    return true;
                }
            }
            return false;
        }

        // Try new structure
        tierMachineCasing = -1;
        tierBlock = -1;
        tierPipeCasing = -1;
        casingAmount = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET)) {
            return false;
        }

        if (tierPipeCasing == 1 && tierMachineCasing == 1 && tierBlock == 1 && casingAmount >= 14 && checkHatches()) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }

        if (tierPipeCasing == 2 && tierMachineCasing == 2 && tierBlock == 2 && casingAmount >= 14 && checkHatches()) {
            updateHatchTexture();
            tierMachine = 2;
            return true;
        }

        return false;
    }

    private boolean checkHatches() {
        return !mSteamInputFluids.isEmpty() && !mSteamInputs.isEmpty()
            && !mSteamOutputs.isEmpty()
            && mOutputHatches.isEmpty()
            && mInputHatches.isEmpty();
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
    }

    // note that a basic steam machine has .setEUtDiscount(2F).setSpeedBoost(2F). So these are bonuses.
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }
                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0)
                    return CheckRecipeResultRegistry.NO_RECIPE;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.25 * tierMachine)
                    .setDurationModifier(1.6 / tierMachine);
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getTierRecipes() {
        return 1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addSteamBulkMachineInfo(8, 1.25f, 0.625f)
            .addInfo(HIGH_PRESSURE_TOOLTIP_NOTICE)
            .beginStructureBlock(3, 4, 3, true)
            .addController("Front center")
            .addSteamInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addSteamOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Basic " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(EnumChatFormatting.GOLD + "14-30x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Bronze Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "6" + EnumChatFormatting.GRAY + " Block of Iron")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "High Pressure " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "14-30x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Steel Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "6" + EnumChatFormatting.GRAY + " Block of Steel")
            .addStructureAuthors(EnumChatFormatting.GOLD + "PCGMatt")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.multi.steam.tier",
                "" + EnumChatFormatting.YELLOW + tierMachine));
        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.multi.steam.parallel",
                "" + EnumChatFormatting.YELLOW + getMaxParallelRecipes()));
        return info.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GTPP.machines.tier") + ": "
                + EnumChatFormatting.YELLOW
                + getSteamTierTextForWaila(tag)
                + EnumChatFormatting.RESET);
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.curparallelism") + ": "
                + EnumChatFormatting.BLUE
                + tag.getInteger("parallel")
                + EnumChatFormatting.RESET);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tierMachine", tierMachine);
        tag.setInteger("parallel", getTrueParallel());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierMachine", tierMachine);
        aNBT.setInteger("tierMachineCasing", tierMachineCasing);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierMachine = aNBT.getInteger("tierMachine");
        tierMachineCasing = aNBT.getInteger("tierMachineCasing");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_COMPRESSOR;
    }

    @Override
    public int getThemeTier() {
        return tierMachine;
    }
}
