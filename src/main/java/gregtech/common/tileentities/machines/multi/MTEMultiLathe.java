package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorVolence;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.misc.GTStructureChannels;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEMultiLathe extends MTEExtendedPowerMultiBlockBase<MTEMultiLathe> implements ISurvivalConstructable {

    public MTEMultiLathe(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiLathe(String aName) {
        super(aName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_BODY = "body";
    private static final String STRUCTURE_PIECE_BODY_ALT = "body_alt";

    protected int pipeTier = -1;

    // get tier from block meta
    @Nullable
    private static Integer getTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings11) return null;
        if (metaID < 0 || metaID > 7) return null;
        return metaID + 1;
    }

    private void setPipeTier(int tier) {
        pipeTier = tier;
    }

    private int getPipeTier() {
        return pipeTier;
    }

    private static final IStructureDefinition<MTEMultiLathe> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiLathe>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "       " }, { "       " }, { "       " }, { "       " }, { "AAA~AAA" } }))
        .addShape(
            STRUCTURE_PIECE_BODY,
            (transpose(
                new String[][] { { "       ", "AAAAAAA", "       ", "       " },
                    { "ABCCCCA", "ABCCCCA", "ABCCCCA", "       " }, { "ABCCCCA", "ABFFFFA", "ABCCCCA", "       " },
                    { "ABCCCCA", "ABCCCCA", "ABCCCCA", "       " }, { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" } })))
        .addShape(
            STRUCTURE_PIECE_BODY_ALT,
            (transpose(
                new String[][] { { "       ", "AAAAAAA", "       ", "       " },
                    { "ACCCCBA", "ACCCCBA", "ACCCCBA", "       " }, { "ACCCCBA", "AFFFFBA", "ACCCCBA", "       " },
                    { "ACCCCBA", "ACCCCBA", "ACCCCBA", "       " }, { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" } })))
        .addElement(
            'A',
            buildHatchAdder(MTEMultiLathe.class).atLeast(InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                .hint(1)
                .buildAndChain(onElementPass(MTEMultiLathe::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0))))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings3, 10)) // Steel Casings
        .addElement('C', chainAllGlasses()) // Glass
        .addElement(
            'F',
            GTStructureChannels.ITEM_PIPE_CASING.use(
                ofBlocksTiered(
                    MTEMultiLathe::getTierFromMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasings11, 0),
                        Pair.of(GregTechAPI.sBlockCasings11, 1),
                        Pair.of(GregTechAPI.sBlockCasings11, 2),
                        Pair.of(GregTechAPI.sBlockCasings11, 3),
                        Pair.of(GregTechAPI.sBlockCasings11, 4),
                        Pair.of(GregTechAPI.sBlockCasings11, 5),
                        Pair.of(GregTechAPI.sBlockCasings11, 6),
                        Pair.of(GregTechAPI.sBlockCasings11, 7)),
                    -1,
                    MTEMultiLathe::setPipeTier,
                    MTEMultiLathe::getPipeTier)))
        .build();

    @Override
    public IStructureDefinition<MTEMultiLathe> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiLathe(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Lathe, IPL")
            .addDynamicParallelInfo(8, TooltipTier.PIPE_CASING)
            .addStaticSpeedInfo(4f)
            .addStaticEuEffInfo(0.8f)
            .beginStructureBlock(7, 5, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 42, false)
            .addCasingInfoExactly("Grate Machine Casing", 9, false)
            .addCasingInfoExactly("Any Tiered Glass", 32, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.four_item_pipe_casings"),
                "Center of the glass",
                4)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.ITEM_PIPE_CASING)
            .toolTipFinisher(AuthorVolence);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        buildPiece(STRUCTURE_PIECE_BODY, stackSize, hintsOnly, 3, 4, -1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        build = survivalBuildPiece(STRUCTURE_PIECE_BODY, stackSize, 3, 4, -1, elementBudget, env, false, true);
        if (build >= 0) return build;
        build = survivalBuildPiece(STRUCTURE_PIECE_BODY_ALT, stackSize, 3, 4, -1, elementBudget, env, false, true);
        return build;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_LATHE_LOOP;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        pipeTier = -1;
        mEnergyHatches.clear();
        mCasingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) return false;
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        if (!checkPiece(STRUCTURE_PIECE_BODY, 3, 4, -1) && !checkPiece(STRUCTURE_PIECE_BODY_ALT, 3, 4, -1))
            return false;
        return this.mMaintenanceHatches.size() == 1 && pipeTier > 0 && !mEnergyHatches.isEmpty() && mCasingAmount >= 42;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 4F)
            .setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (getPipeTier() * 8);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("itemPipeTier", Math.max(0, getPipeTier()));
        tag.setFloat("speedBonus", 400);
    }

    private static final DecimalFormat dfNone = new DecimalFormat("#");

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.itemPipeTier") + ": "
                + EnumChatFormatting.WHITE
                + Math.max(0, tag.getInteger("itemPipeTier")));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.speed") + ": "
                + EnumChatFormatting.WHITE
                + dfNone.format((Math.max(0, tag.getInteger("speedBonus"))))
                + "%");
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.latheRecipes;
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }
}
