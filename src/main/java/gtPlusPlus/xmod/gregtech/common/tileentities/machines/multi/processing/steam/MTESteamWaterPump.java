package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.blocks.BlockCasings9;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamWaterPump extends MTESteamMultiBase<MTESteamWaterPump> implements ISurvivalConstructable {

    public MTESteamWaterPump(String aName) {
        super(aName);
    }

    public MTESteamWaterPump(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamWaterPump(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Water Pump";
    }

    private static IStructureDefinition<MTESteamWaterPump> STRUCTURE_DEFINITION = null;

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 2;
    private static final int DEPTH_OFF_SET = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    // Base amount of water produced per second, before applying humidity and tier modifiers.
    private static final int BASE_WATER_PER_SECOND = 1_500;
    private static final int PROGRESSION_TIME_TICKS = 20;

    private static final int BASE_STEAM_PER_SECOND = 1_500;

    private int mSetTier = -1;

    private float currentHumidity;

    private FluidStack[] getWater() {
        return new FluidStack[] { Materials.Water.getFluid(
            calculateFinalWaterOutput() <= 250 && isMinWaterAllowedDim() ? 250 : calculateFinalWaterOutput()) };
    }

    @Override
    protected int getCasingTextureId() {
        return GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 2);
    }

    private int mCountCasing;

    private boolean isMinWaterAllowedDim() {
        return !(getBaseMetaTileEntity().getWorld().provider.getDimensionName()
            .equals("Venus")
            || getBaseMetaTileEntity().getWorld().provider.getDimensionName()
                .equals("Mercury")
            || getBaseMetaTileEntity().getWorld().provider.getDimensionName()
                .equals("Mars")
            || getBaseMetaTileEntity().getWorld().provider.getDimensionName()
                .equals("Moon")
            || getBaseMetaTileEntity().getWorld().provider.getDimensionName()
                .equals("Nether"));
    }

    private float getHumidity() {
        return this.getBaseMetaTileEntity()
            .getBiome().rainfall;
    }

    private int calculateFinalWaterOutput() {
        return (int) ((currentHumidity * BASE_WATER_PER_SECOND) * mSetTier);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    // spotless:off
    @Override
    public IStructureDefinition<MTESteamWaterPump> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {

            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamWaterPump>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] {
                            { " A ", " A ", "AAA", " A " },
                            { " A ", "   ", "A A", " A " },
                            { "C~C", "CCC", "CCC", "CCC" } }))
                .addElement('A', ofBlocksTiered(MTESteamWaterPump::getFrameTier, ImmutableList.of(Pair.of(GregTechAPI.sBlockFrames, Materials.Bronze.mMetaItemSubID),
                    Pair.of(GregTechAPI.sBlockFrames, Materials.Steel.mMetaItemSubID)), -1, (pump, tier) -> pump.mSetTier = tier , pump -> pump.mSetTier))
                .addElement(
                    'C',
                        ofChain(
                            buildSteamInput(MTESteamWaterPump.class)
                                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(2))
                                .hint(1)
                                .build(),
                            buildHatchAdder(MTESteamWaterPump.class)
                                .atLeast(OutputHatch)
                                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(2))
                                .hint(1)
                                .buildAndChain(onElementPass(x -> ++x.mCountCasing, ofBlock(GregTechAPI.sBlockCasings9, 2)))
                        )
                    )
                .build();

        }
        return STRUCTURE_DEFINITION;
    }
    // spotless:on

    @Nullable
    public static Integer getFrameTier(Block block, int meta) {
        if (block == GregTechAPI.sBlockFrames) {
            if (meta == Materials.Bronze.mMetaItemSubID) return 1;
            if (meta == Materials.Steel.mMetaItemSubID) return 2;
        }
        return null;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET,
            elementBudget,
            env,
            false,
            true);
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCountCasing = 0;
        mSetTier = -1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) {
            return false;
        }

        if (this.mOutputHatches.size() != 1 || this.mSteamInputFluids.size() != 1) return false;

        currentHumidity = getHumidity();
        return mCountCasing >= 9;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP_ACTIVE;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Pumps Water based on humidity")
            .addInfo("Has 2 tiers: Bronze and Steel")
            .addInfo("Steel tier extracts 2x Water")
            .addInfo(
                EnumChatFormatting.AQUA + "Generates: "
                    + EnumChatFormatting.WHITE
                    + "tier * humidity * "
                    + BASE_WATER_PER_SECOND
                    + " L/s"
                    + EnumChatFormatting.AQUA
                    + " of Water, to a minimum of 250L/s"
                    + EnumChatFormatting.RESET)
            .addInfo(
                EnumChatFormatting.RED + "Consumes: "
                    + EnumChatFormatting.WHITE
                    + BASE_STEAM_PER_SECOND
                    + " L/s"
                    + EnumChatFormatting.RED
                    + " of Steam"
                    + EnumChatFormatting.RESET)
            .beginStructureBlock(3, 3, 4, false)
            .addOutputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 1)
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Bronze Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "9" + EnumChatFormatting.GRAY + " Wooden Casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 2)
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Steel Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "9 " + EnumChatFormatting.GRAY + " Wooden Casing")
            .toolTipFinisher(GTValues.AuthorEvgenWarGold);
        return tt;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {

        VoidProtectionHelper voidProtection = new VoidProtectionHelper().setMachine(this)
            .setFluidOutputs(getWater())
            .build();

        if (voidProtection.isFluidFull()) {
            mOutputFluids = null;
            mMaxProgresstime = 0;
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        } else {
            if (getTotalSteamStored() >= BASE_STEAM_PER_SECOND) {
                mMaxProgresstime = PROGRESSION_TIME_TICKS;
                tryConsumeSteam(BASE_STEAM_PER_SECOND);
                mOutputFluids = getWater();
                updateSlots();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            } else return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    @Override
    public int getTierRecipes() {
        return 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick % 1200) == 0) {
                currentHumidity = getHumidity();
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();

        int tierMachine = tag.getInteger("mSetTier");
        String tierMachineText;
        if (tierMachine == 1) {
            tierMachineText = "Bronze";
        } else if (tierMachine == 2) {
            tierMachineText = "Steel";
        } else {
            tierMachineText = String.valueOf(tierMachine);
        }

        currenttip.add(
            StatCollector.translateToLocal("GTPP.machines.tier") + ": "
                + EnumChatFormatting.BLUE
                + tierMachineText
                + EnumChatFormatting.RESET);
        currenttip.add(
            StatCollector.translateToLocal("GT5U.biomes.humidity") + " "
                + EnumChatFormatting.BLUE
                + tag.getFloat("humidity")
                + " %"
                + EnumChatFormatting.RESET);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setFloat("humidity", currentHumidity * 100);
        tag.setInteger("mSetTier", mSetTier);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSetTier", mSetTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSetTier = aNBT.getInteger("mSetTier");
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_WATER_PUMP_LOOP;
    }

    @Override
    public int getThemeTier() {
        return mSetTier;
    }
}
