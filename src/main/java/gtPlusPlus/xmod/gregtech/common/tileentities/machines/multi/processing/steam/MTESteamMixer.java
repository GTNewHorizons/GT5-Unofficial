package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
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
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamMixer extends MTESteamMultiBase<MTESteamMixer> implements ISurvivalConstructable {

    public MTESteamMixer(String aName) {
        super(aName);
    }

    public MTESteamMixer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamMixer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Mixer";
    }

    // TODO: remove old structure after 2.9/next release cycle

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_OLD = "old";

    private IStructureDefinition<MTESteamMixer> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shape = new String[][]{
        {"     ","  A  "," ABA ","  A  ","     "},
        {"  A  ","     ","A C A","     ","  A  "},
        {" A~A ","A D A","ADBDA","A D A"," AAA "},
        {" AAA ","AAAAA","AAAAA","AAAAA"," AAA "}};
    private final String[][] shape_old = new String[][]{
        {"       ","   A   ","   A   "," AAAAA ","   A   ","   A   ","       "},
        {"   A   ","   A   ","       ","AA B AA","       ","   A   ","   A   "},
        {"   A   ","       ","       ","A  C  A","       ","       ","   A   "},
        {" AAAAA ","A     A","A     A","A  C  A","A     A","A     A"," AAAAA "},
        {" AA~AA ","AE   EA","A E E A","A  B  A","A E E A","AE   EA"," AAAAA "},
        {" AAAAA ","AAAAAAA","AAAAAAA","AAAAAAA","AAAAAAA","AAAAAAA"," AAAAA "}};
    //spotless:on

    private static final int HORIZONTAL_OFFSET = 2;
    private static final int VERTICAL_OFFSET = 2;
    private static final int DEPTH_OFFSET = 0;
    private static final int HORIZONTAL_OFFSET_OLD = 3;
    private static final int VERTICAL_OFFSET_OLD = 4;
    private static final int DEPTH_OFFSET_OLD = 0;

    private int revision = 1;

    private int tierGearBoxCasing = -1;
    private int tierPipeCasing = -1;
    private int tierMachineCasing = -1;

    private int tCountCasing = 0;

    private int tierMachine = 1;

    @Nullable
    public Integer getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) {
            tCountCasing++;
            return 1;
        }
        if (block == sBlockCasings2 && 0 == meta) {
            tCountCasing++;
            return 2;
        }
        return null;
    }

    @Nullable
    public static Integer getTierGearBoxCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 2 == meta) return 1;
        if (block == sBlockCasings2 && 3 == meta) return 2;
        return null;
    }

    @Nullable
    public static Integer getTierPipeCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 12 == meta) return 1;
        if (block == sBlockCasings2 && 13 == meta) return 2;
        return null;
    }

    protected void updateHatchTexture() {
        for (MTEHatch h : mSteamInputs) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mSteamOutputs) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mOutputHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mInputHatches) h.updateTexture(getCasingTextureId());
    }

    @Override
    protected int getCasingTextureId() {
        if (tierGearBoxCasing == 2 || tierPipeCasing == 2 || tierMachineCasing == 2)
            return ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0);
        return ((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10);
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
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_CENTRIFUGE;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_CENTRIFUGE_ACTIVE;
    }

    @Override
    public IStructureDefinition<MTESteamMixer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {

            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamMixer>builder()

                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addShape(STRUCTURE_PIECE_OLD, transpose(shape_old))
                .addElement(
                    'B',
                    ofBlocksTiered(
                        MTESteamMixer::getTierGearBoxCasing,
                        ImmutableList.of(Pair.of(sBlockCasings2, 2), Pair.of(sBlockCasings2, 3)),
                        -1,
                        (t, m) -> t.tierGearBoxCasing = m,
                        t -> t.tierGearBoxCasing))
                .addElement(
                    'C',
                    ofBlocksTiered(
                        MTESteamMixer::getTierPipeCasing,
                        ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                        -1,
                        (t, m) -> t.tierPipeCasing = m,
                        t -> t.tierPipeCasing))
                .addElement('D', ofBlock(Blocks.iron_bars, 0))
                .addElement('E', ofBlock(Blocks.iron_block, 0))
                .addElement(
                    'A',
                    ofChain(
                        buildSteamInput(MTESteamMixer.class).casingIndex(10)
                            .hint(1)
                            .allowOnly(ForgeDirection.NORTH)
                            .build(),
                        buildHatchAdder(MTESteamMixer.class)
                            .atLeast(
                                SteamHatchElement.InputBus_Steam,
                                SteamHatchElement.OutputBus_Steam,
                                OutputHatch,
                                InputHatch)
                            .casingIndex(10)
                            .hint(1)
                            .allowOnly(ForgeDirection.NORTH)
                            .buildAndChain(),
                        ofBlocksTiered(
                            this::getTierMachineCasing,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            -1,
                            (t, m) -> t.tierMachineCasing = m,
                            t -> t.tierMachineCasing)))
                .build();

        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (revision >= 1) {
            this.buildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                hintsOnly,
                HORIZONTAL_OFFSET,
                VERTICAL_OFFSET,
                DEPTH_OFFSET);
        } else {
            this.buildPiece(
                STRUCTURE_PIECE_OLD,
                stackSize,
                hintsOnly,
                HORIZONTAL_OFFSET_OLD,
                VERTICAL_OFFSET_OLD,
                DEPTH_OFFSET_OLD);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        if (revision >= 1) {
            return this.survivalBuildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                HORIZONTAL_OFFSET,
                VERTICAL_OFFSET,
                DEPTH_OFFSET,
                elementBudget,
                env,
                false,
                true);
        } else {
            return this.survivalBuildPiece(
                STRUCTURE_PIECE_OLD,
                stackSize,
                HORIZONTAL_OFFSET_OLD,
                VERTICAL_OFFSET_OLD,
                DEPTH_OFFSET_OLD,
                elementBudget,
                env,
                false,
                true);
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tierGearBoxCasing = -1;
        tierPipeCasing = -1;
        tierMachineCasing = -1;
        tCountCasing = 0;
        if (!(revision >= 1 ? checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET)
            : checkPiece(STRUCTURE_PIECE_OLD, HORIZONTAL_OFFSET_OLD, VERTICAL_OFFSET_OLD, DEPTH_OFFSET_OLD)))
            return false;
        boolean casingCountValid = revision >= 1 ? tCountCasing >= 25 : tCountCasing >= 90;
        if (tierGearBoxCasing == 1 && tierPipeCasing == 1
            && tierMachineCasing == 1
            && casingCountValid
            && !mSteamInputFluids.isEmpty()) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierGearBoxCasing == 2 && tierPipeCasing == 2
            && tierMachineCasing == 2
            && casingCountValid
            && !mSteamInputFluids.isEmpty()) {
            updateHatchTexture();
            tierMachine = 2;
            return true;
        }
        return false;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.mixerNonCellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.25 * tierMachine)
                    .setDurationModifier(1.6 / tierMachine);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
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
            .beginStructureBlock(5, 4, 5, false)
            .addSteamInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addSteamOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addOutputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Basic " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(EnumChatFormatting.GOLD + "25-35x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo(EnumChatFormatting.GOLD + "2x" + EnumChatFormatting.GRAY + " Bronze Gear Box Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "1x" + EnumChatFormatting.GRAY + " Bronze Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "4x" + EnumChatFormatting.GRAY + " Iron Bars")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "High Pressure " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "25-35x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "2x" + EnumChatFormatting.GRAY + " Steel Gear Box Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "1x" + EnumChatFormatting.GRAY + " Steel Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "4x" + EnumChatFormatting.GRAY + " Iron Bars")
            .addStructureAuthors(GTValues.AuthorJL2210)
            .toolTipFinisher(GTValues.AuthorEvgenWarGold);
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
        aNBT.setInteger("revision", revision);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierMachine = aNBT.getInteger("tierMachine");
        tierMachineCasing = aNBT.getInteger("tierMachineCasing");
        revision = aNBT.hasKey("revision", NBT.TAG_INT) ? aNBT.getInteger("revision") : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_STEAM_CENTRIFUGE_LOOP;
    }

    @Override
    public int getThemeTier() {
        return tierMachineCasing;
    }

}
