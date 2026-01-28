package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.GregTechAPI.sBlockCasings3;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
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
import org.jetbrains.annotations.NotNull;

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
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamFurnaceMulti extends MTESteamMultiBase<MTESteamFurnaceMulti> implements ISurvivalConstructable {

    public MTESteamFurnaceMulti(String aName) {
        super(aName);
    }

    public MTESteamFurnaceMulti(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTESteamFurnaceMulti(this.mName);
    }

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 1;
    private static final int DEPTH_OFF_SET = 0;

    private int casingCount = 0;

    private int tierMachine = 1;

    private int tierMachineCasing = -1;
    private int tierPipeCasing = -1;
    private int tierGearboxCasing = -1;
    private int tierFireboxCasing = -1;

    private static final int MACHINEMODE_FURNACE = 0;
    private static final int MACHINEMODE_BLASTING = 1;
    private static final int MACHINEMODE_SMOKER = 2;
    private static final SoundResource startSound = Railcraft.isModLoaded() ? SoundResource.RAILCRAFT_STEAM_BURST
        : null;

    @Override
    public String getMachineType() {
        return "Furnace, Blaster, Smoker";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private IStructureDefinition<MTESteamFurnaceMulti> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shape =new String[][]{
        {" B ","B B"," B "},
        {"A~A","D D","ADA"},
        {"ACA","CAC","ACA"}
    };
    //spotless:on

    @Override
    public IStructureDefinition<MTESteamFurnaceMulti> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamFurnaceMulti>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    ofChain(
                        buildSteamInput(MTESteamFurnaceMulti.class).casingIndex(10)
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTESteamFurnaceMulti.class)
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
                    'B', // Gearbox
                    ofBlocksTiered(
                        this::getTierGearboxCasing,
                        ImmutableList.of(Pair.of(sBlockCasings2, 2), Pair.of(sBlockCasings2, 3)),
                        -1,
                        (t, m) -> t.tierGearboxCasing = m,
                        t -> t.tierGearboxCasing))
                .addElement(
                    'C',
                    ofBlocksTiered(
                        this::getTierPipeCasing,
                        ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                        -1,
                        (t, m) -> t.tierPipeCasing = m,
                        t -> t.tierPipeCasing))
                .addElement(
                    'D', // Firebox
                    ofBlocksTiered(
                        this::getTierFireboxCasing,
                        ImmutableList.of(Pair.of(sBlockCasings3, 13), Pair.of(sBlockCasings3, 14)),
                        -1,
                        (t, m) -> t.tierFireboxCasing = m,
                        t -> t.tierFireboxCasing))
                .addElement('G', chainAllGlasses())
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addSteamBulkMachineInfo(8, 1.25f, 0.625f);
        if (EtFuturumRequiem.isModLoaded()) {
            tt.addInfo(
                "Can operate in " + EnumChatFormatting.RED
                    + "Blasting"
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Smoking"
                    + EnumChatFormatting.GRAY
                    + " modes, which double "
                    + EnumChatFormatting.GREEN
                    + "Speed"
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.AQUA
                    + "Steam Usage")
                .addInfo(
                    EnumChatFormatting.RED + "Blasting"
                        + EnumChatFormatting.GRAY
                        + " mode can only process "
                        + EnumChatFormatting.RED
                        + "Metals")
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE + "Smoking"
                        + EnumChatFormatting.GRAY
                        + " can only process "
                        + EnumChatFormatting.LIGHT_PURPLE
                        + "Food Items")
                .addInfo("Mode can be switched by using a screwdriver on the controller")
                .addSeparator();
        }

        tt.addInfo(HIGH_PRESSURE_TOOLTIP_NOTICE)
            .beginStructureBlock(3, 3, 4, false)
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
            .addStructureInfo(EnumChatFormatting.GOLD + "2-6" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo(EnumChatFormatting.GOLD + "4x" + EnumChatFormatting.GRAY + " Bronze Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "4x" + EnumChatFormatting.GRAY + " Bronze Gearbox Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "3x" + EnumChatFormatting.GRAY + " Bronze Firebox Casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "High Pressure " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(EnumChatFormatting.GOLD + "2-6" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "4x" + EnumChatFormatting.GRAY + " Steel Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "4x" + EnumChatFormatting.GRAY + " Steel Gearbox Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "3x" + EnumChatFormatting.GRAY + " Steel Firebox Casing")
            .toolTipFinisher();
        return tt;
    }

    @Nullable
    public Integer getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) {
            casingCount++;
            return 1;
        }
        if (block == sBlockCasings2 && 0 == meta) {
            casingCount++;
            return 2;
        }
        return null;
    }

    @Nullable
    public Integer getTierPipeCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 12 == meta) return 1;
        if (block == sBlockCasings2 && 13 == meta) return 2;
        return null;
    }

    @Nullable
    public Integer getTierGearboxCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 2 == meta) return 1;
        if (block == sBlockCasings2 && 3 == meta) return 2;
        return null;
    }

    @Nullable
    public Integer getTierFireboxCasing(Block block, int meta) {
        if (block == sBlockCasings3 && 13 == meta) return 1;
        if (block == sBlockCasings3 && 14 == meta) return 2;
        return null;
    }

    protected void updateHatchTexture() {
        int textureID = getCasingTextureId();
        for (MTEHatch h : mSteamInputs) h.updateTexture(textureID);
        for (MTEHatch h : mSteamOutputs) h.updateTexture(textureID);
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(textureID);
    }

    @Override
    protected int getCasingTextureId() {
        if (tierMachineCasing == 2) return ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0);
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
    protected IIconContainer getInactiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_MULTI_GLOW;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_MULTI;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_MULTI_GLOW_ACTIVE;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_MULTI_ACTIVE;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
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

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingCount = 0;
        tierMachineCasing = -1;
        tierPipeCasing = -1;
        tierGearboxCasing = -1;
        tierFireboxCasing = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (tierMachineCasing == 1 && tierPipeCasing == 1
            && tierFireboxCasing == 1
            && tierGearboxCasing == 1
            && casingCount >= 2
            && checkHatches()) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierMachineCasing == 2 && tierPipeCasing == 2
            && tierFireboxCasing == 2
            && tierGearboxCasing == 2
            && casingCount >= 2
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
            && mInputHatches.isEmpty();
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return EtFuturumRequiem.isModLoaded();
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_FURNACE) return MACHINEMODE_BLASTING;
        else if (machineMode == MACHINEMODE_BLASTING) return MACHINEMODE_SMOKER;
        else return MACHINEMODE_FURNACE;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        if (!EtFuturumRequiem.isModLoaded()) return Arrays.asList(RecipeMaps.furnaceRecipes);

        return Arrays.asList(RecipeMaps.furnaceRecipes, RecipeMaps.efrBlastingRecipes, RecipeMaps.efrSmokingRecipes);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (!EtFuturumRequiem.isModLoaded()) return RecipeMaps.furnaceRecipes;
        return switch (machineMode) {
            case MACHINEMODE_SMOKER -> RecipeMaps.efrSmokingRecipes;
            case MACHINEMODE_BLASTING -> RecipeMaps.efrBlastingRecipes;
            default -> RecipeMaps.furnaceRecipes;
        };

    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.GTPP_MULTI_STEAM_FURNACE.mode." + machineMode);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return startSound;
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
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.25 * tierMachine * (machineMode == MACHINEMODE_FURNACE ? 1 : 2))
                    .setDurationModifier(1.6 / tierMachine);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getTierRecipes() {
        return 1;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.multi.steam.tier",
                "" + EnumChatFormatting.YELLOW + tierMachine));
        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.multi.steam.parallel",
                "" + EnumChatFormatting.YELLOW + getMaxParallelRecipes()));
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
            translateToLocal("GTPP.machines.tier") + ": "
                + EnumChatFormatting.YELLOW
                + getSteamTierTextForWaila(tag)
                + EnumChatFormatting.RESET);
        currenttip.add(
            translateToLocal("GT5U.multiblock.curparallelism") + ": "
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
    public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
            final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
            aBaseMetaTileEntity.getWorld()
                .spawnParticle(
                    "largesmoke",
                    (aBaseMetaTileEntity.getXCoord() + (getExtendedFacing().getRelativeBackInWorld().offsetX) + 0.8F)
                        - (tRandom.nextFloat() * 0.6F),
                    (aBaseMetaTileEntity.getYCoord() + 0.3f) + (tRandom.nextFloat() * 0.2F),
                    (aBaseMetaTileEntity.getZCoord() + (getExtendedFacing().getRelativeBackInWorld().offsetZ) + 1.2F)
                        - (tRandom.nextFloat() * 1.6F),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierMachine", tierMachine);
        aNBT.setInteger("tierMachineCasing", tierMachineCasing);
        aNBT.setInteger("machineMode", machineMode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierMachine = aNBT.getInteger("tierMachine");
        tierMachineCasing = aNBT.getInteger("tierMachineCasing");
        machineMode = aNBT.getInteger("machineMode");
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        MTEMultiBlockBaseGui gui = super.getGui();
        if (EtFuturumRequiem.isModLoaded()) gui.withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
        return gui;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FURNACE;
    }

    @Override
    public int getThemeTier() {
        return tierMachineCasing;
    }

}
