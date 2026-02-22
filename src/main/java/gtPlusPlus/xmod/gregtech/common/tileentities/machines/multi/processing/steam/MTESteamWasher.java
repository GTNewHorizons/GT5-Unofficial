package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.GregTechAPI.*;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
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
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTESteamMultiBaseGui;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamWasher extends MTESteamMultiBase<MTESteamWasher> implements ISurvivalConstructable {

    public MTESteamWasher(String aName) {
        super(aName);
    }

    public MTESteamWasher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamWasher(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Ore Washer, Simple Washer";
    }

    private static final String STRUCTUR_PIECE_MAIN = "main";

    private IStructureDefinition<MTESteamWasher> STRUCTURE_DEFINITION = null;

    private final String[][] shape = new String[][] {
        { "         ", "         ", " CCCCCC  ", "         ", "         " },
        { "         ", "         ", " C    C  ", "         ", "         " },
        { "     AAA ", "    A   A", " C  A C A", "    A   A", "     AAA " },
        { "    ADDDA", "AAA D   D", "AAA D C D", "AAA D   D", "    ADDDA" },
        { "    ADDDA", "A~A DEEED", "AAA DECED", "AAA DEEED", "    ADDDA" },
        { "    AAAAA", "AAA ABBBA", "AAA ABABA", "AAA ABBBA", "    AAAAA" } };

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 4;
    private static final int DEPTH_OFF_SET = 1;

    private static final int MACHINEMODE_OREWASH = 0;
    private static final int MACHINEMODE_SIMPLEWASH = 1;

    private boolean isBroken = true;

    private int tierGearBoxCasing = -1;
    private int tierPipeCasing = -1;
    private int tierMachineCasing = -1;
    private int tierMachine = 1;

    private int tCountCasing = 0;

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
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER_ACTIVE;
    }

    @Override
    public IStructureDefinition<MTESteamWasher> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {

            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamWasher>builder()

                .addShape(STRUCTUR_PIECE_MAIN, transpose(shape))
                .addElement(
                    'B',
                    ofBlocksTiered(
                        MTESteamWasher::getTierGearBoxCasing,
                        ImmutableList.of(Pair.of(sBlockCasings2, 2), Pair.of(sBlockCasings2, 3)),
                        -1,
                        (t, m) -> t.tierGearBoxCasing = m,
                        t -> t.tierGearBoxCasing))
                .addElement(
                    'C',
                    ofBlocksTiered(
                        MTESteamWasher::getTierPipeCasing,
                        ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                        -1,
                        (t, m) -> t.tierPipeCasing = m,
                        t -> t.tierPipeCasing))
                .addElement('D', chainAllGlasses())
                .addElement('E', ofChain(isAir(), ofAnyWater()))
                .addElement(
                    'A',
                    ofChain(
                        buildSteamInput(MTESteamWasher.class).casingIndex(10)
                            .hint(1)
                            .allowOnly(ForgeDirection.NORTH)
                            .build(),
                        buildHatchAdder(MTESteamWasher.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam, InputHatch)
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
        this.buildPiece(STRUCTUR_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
            STRUCTUR_PIECE_MAIN,
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
        tierGearBoxCasing = -1;
        tierPipeCasing = -1;
        tierMachineCasing = -1;
        tCountCasing = 0;
        if (!checkPiece(STRUCTUR_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (tierGearBoxCasing == 1 && tierPipeCasing == 1
            && tierMachineCasing == 1
            && tCountCasing >= 55
            && checkHatches()) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierGearBoxCasing == 2 && tierPipeCasing == 2
            && tierMachineCasing == 2
            && tCountCasing >= 55
            && checkHatches()) {
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
            && !mInputHatches.isEmpty();
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (machineMode == MACHINEMODE_SIMPLEWASH) {
            return GTPPRecipeMaps.simpleWasherRecipes;
        }
        return RecipeMaps.oreWasherRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTPPRecipeMaps.simpleWasherRecipes, RecipeMaps.oreWasherRecipes);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {

        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (isBroken) {
                    checkForWater();
                    isBroken = false;
                } else if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                } else return CheckRecipeResultRegistry.SUCCESSFUL;
                return SimpleCheckRecipeResult.ofFailure("no_water");
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
            .addInfo("Mode can be switched by using a screwdriver on the controller")
            .beginStructureBlock(5, 5, 5, false)
            .addSteamInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addSteamOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Basic " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(EnumChatFormatting.GOLD + "55-59x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo(EnumChatFormatting.GOLD + "24x" + EnumChatFormatting.GRAY + " Any Tiered Glass")
            .addStructureInfo(EnumChatFormatting.GOLD + "12x" + EnumChatFormatting.GRAY + " Bronze Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "8x" + EnumChatFormatting.GRAY + " Bronze Gear Box Casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "High Pressure " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "55-59x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "24x" + EnumChatFormatting.GRAY + " Any Tiered Glass")
            .addStructureInfo(EnumChatFormatting.GOLD + "12x" + EnumChatFormatting.GRAY + " Steel Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "8x" + EnumChatFormatting.GRAY + " Steel Gear Box Casing")
            .toolTipFinisher(GTValues.AuthorEvgenWarGold);
        return tt;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(
            translateToLocalFormatted("gtpp.infodata.multi.steam.tier", "" + EnumChatFormatting.YELLOW + tierMachine));
        info.add(
            translateToLocalFormatted(
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
            StatCollector.translateToLocal("GT5U.multiblock.runningMode") + " "
                + EnumChatFormatting.WHITE
                + tag.getString("mode")
                + EnumChatFormatting.RESET);
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
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierMachine", tierMachine);
        aNBT.setInteger("mMode", machineMode);
        aNBT.setInteger("tierMachineCasing", tierMachineCasing);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierMachine = aNBT.getInteger("tierMachine");
        machineMode = aNBT.getInteger("mMode");
        tierMachineCasing = aNBT.getInteger("tierMachineCasing");
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated();
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_STEAM_WASHER_LOOP;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_OREWASH) return MACHINEMODE_SIMPLEWASH;
        else return MACHINEMODE_OREWASH;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER);
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.GTPP_MULTI_WASH_PLANT.mode." + machineMode);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mUpdate < -250) mUpdate = 50;
            if ((aTick % 1200) == 0) {
                isBroken = true;
            }
        }
    }

    private void checkForWater() {
        ExtendedFacing facing = getExtendedFacing();
        final ForgeDirection frontFacing = facing.getDirection();
        final Flip curFlip = facing.getFlip();
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        double xOffset = getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = getExtendedFacing().getRelativeBackInWorld().offsetY;

        switch (frontFacing) {
            case WEST -> {
                xOffset -= 1;
                zOffset += curFlip.isHorizontallyFlipped() ? -6 : 4;
            }
            case EAST -> {
                xOffset -= 1;
                zOffset += curFlip.isHorizontallyFlipped() ? 4 : -6;
            }
            case NORTH -> {
                zOffset -= 1;
                xOffset += curFlip.isHorizontallyFlipped() ? 4 : -6;
            }
            case SOUTH -> {
                zOffset -= 1;
                xOffset += curFlip.isHorizontallyFlipped() ? -6 : 4;
            }
        }

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Block tBlock = this.getBaseMetaTileEntity()
                    .getWorld()
                    .getBlock((int) (i + xOffset + x), (int) (y + yOffset), (int) (j + zOffset + z));
                if (tBlock == Blocks.air) {
                    if (tryConsumeWater()) {
                        this.getBaseMetaTileEntity()
                            .getWorld()
                            .setBlock(
                                (int) (i + xOffset + x),
                                (int) (y + yOffset),
                                (int) (j + zOffset + z),
                                Blocks.water);
                    }
                }
            }
        }
    }

    private boolean tryConsumeWater() {
        if (getStoredFluids() != null) {
            for (FluidStack waterCapacity : this.getStoredFluids()) {
                if (waterCapacity.isFluidEqual(Materials.Water.getFluid(1_000))) {
                    if (waterCapacity.amount >= 1000) {
                        waterCapacity.amount -= 1000;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTESteamMultiBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER);
    }

    @Override
    public int getThemeTier() {
        return tierMachineCasing;
    }
}
