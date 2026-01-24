package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchMagnet;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.misc.GTStructureChannels;

public class MTEIndustrialElectromagneticSeparator
    extends MTEExtendedPowerMultiBlockBase<MTEIndustrialElectromagneticSeparator> implements ISurvivalConstructable {

    public enum MagnetTiers {

        Iron(8, 0.8F, 1.1F, false),
        Steel(24, 0.75F, 1.25F, false),
        Neodymium(48, 0.7F, 1.5F, false),
        Samarium(96, 0.6F, 2F, false),
        Tengam(256, 0.5F, 2.5F, true);

        final int maxParallel;
        final float euModifier, speedBoost;
        final boolean supportsExotic;

        MagnetTiers(int maxParallel, float euModifier, float speedBoost, boolean supportsExotic) {
            this.maxParallel = maxParallel;
            this.euModifier = euModifier;
            this.speedBoost = 1F / speedBoost;
            this.supportsExotic = supportsExotic;
        }

        public static String buildMagnetTooltip(MagnetTiers m) {
            String tooltip = "Used in Magnetic Flux Exhibitor" + GTSplit.LB
                + EnumChatFormatting.LIGHT_PURPLE
                + "Speed: +"
                + Math.round((1F / m.speedBoost * 100) - 100)
                + "%"
                + GTSplit.LB
                + EnumChatFormatting.DARK_PURPLE
                + "EU Usage: "
                + Math.round(m.euModifier * 100)
                + "%"
                + GTSplit.LB
                + EnumChatFormatting.AQUA
                + "Parallel: "
                + m.maxParallel;

            if (m.supportsExotic) tooltip = tooltip + GTSplit.LB
                + EnumChatFormatting.BOLD
                + EnumChatFormatting.GREEN
                + "Can Use Multi-Amp Hatches"
                + GTSplit.LB
                + EnumChatFormatting.RED
                + "Limit to one energy hatch if using Multi-Amp";

            return tooltip;
        }
    }

    final int MIN_CASING = 64;
    private MTEHatchMagnet mMagHatch = null;
    private MagnetTiers magnetTier = null;

    private static final int MACHINEMODE_SEPARATOR = 0;
    private static final int MACHINEMODE_POLARIZER = 1;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIndustrialElectromagneticSeparator> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialElectromagneticSeparator>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "  CCC  ", "       ", "       ", "       ", "       ", "  B~B  " },
                { " CC CC ", "       ", "  BBB  ", "  AAA  ", "  BEB  ", " BBBBB " },
                { "CC   CC", "       ", " BBBBB ", " A   A ", " BBBBB ", "BBBBBBB" },
                { "C     C", "C     C", "CBBBBBC", "CA C AC", "CBBBBBC", "BBBBBBB" },
                { "CC   CC", "       ", " BBBBB ", " A   A ", " BBBBB ", "BBBBBBB" },
                { " CC CC ", "       ", "  BBB  ", "  AAA  ", "  BBB  ", " BBBBB " },
                { "  CCC  ", "   C   ", "   C   ", "   C   ", "   C   ", "  BBB  " } }))
        .addElement('A', chainAllGlasses())
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEIndustrialElectromagneticSeparator.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy.or(MultiAmpEnergy))
                    .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(0))
                    .hint(1)
                    .buildAndChain(
                        onElementPass(
                            MTEIndustrialElectromagneticSeparator::onCasingAdded,
                            ofBlock(GregTechAPI.sBlockCasings10, 0)))))
        .addElement('C', ofFrame(Materials.NeodymiumMagnetic))
        .addElement(
            'E',
            buildHatchAdder(MTEIndustrialElectromagneticSeparator.class)
                .adder(MTEIndustrialElectromagneticSeparator::addMagHatch)
                .hatchClass(MTEHatchMagnet.class)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(0))
                .hint(2)
                .build())
        .build();

    public MTEIndustrialElectromagneticSeparator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialElectromagneticSeparator(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialElectromagneticSeparator> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialElectromagneticSeparator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 0)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Electromagnetic Separator/Polarizer, MFE")
            .addInfo("Use screwdriver to switch mode")
            .addInfo("Insert an electromagnet into the electromagnet housing to use")
            .addInfo("Better electromagnets give further bonuses")
            .addInfo("With Tengam electromagnet, multi-amp (NOT laser) hatches are allowed")
            .beginStructureBlock(7, 6, 7, false)
            .addController("Front Center")
            .addCasingInfoMin("MagTech Casings", MIN_CASING, false)
            .addCasingInfoExactly("Any Tiered Glass", 12, false)
            .addOtherStructurePart("Magnetic Neodymium Frame Box", "x37")
            .addOtherStructurePart(
                translateToLocal("GT5U.tooltip.structure.electromagnet_housing"),
                "1 Block Above/Behind Controller",
                2)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(GTAuthors.authorBaps);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 5, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 5, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mMagHatch = null;
        mExoticEnergyHatches.clear();
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 5, 0)) return false;
        if (mCasingAmount < MIN_CASING) return false;
        if (mMagHatch == null) return false;

        // If there are exotic hatches, ensure there is only 1.
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            return (mExoticEnergyHatches.size() == 1);
        }

        // All checks passed!
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                findMagnet();
                if (magnetTier != null) {
                    if (!mExoticEnergyHatches.isEmpty() && !magnetTier.supportsExotic)
                        return SimpleCheckRecipeResult.ofFailure("electromagnet_insufficient");
                    euModifier = magnetTier.euModifier;
                    speedBoost = magnetTier.speedBoost;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return SimpleCheckRecipeResult.ofFailure("electromagnet_missing");
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        findMagnet();
        if (magnetTier != null) return magnetTier.maxParallel;
        return 1;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINEMODE_POLARIZER) ? RecipeMaps.polarizerRecipes
            : RecipeMaps.electroMagneticSeparatorRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.polarizerRecipes, RecipeMaps.electroMagneticSeparatorRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("polarizerMode")) {
            machineMode = aNBT.getBoolean("polarizerMode") ? MACHINEMODE_POLARIZER : MACHINEMODE_SEPARATOR;
            aNBT.removeTag("polarizerMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER);
    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR.mode." + machineMode);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mExoticEnergyHatches.clear();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
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

    private void findMagnet() {
        magnetTier = null;
        if (mMagHatch != null) {
            ItemStack aSlot = mMagHatch.getStackInSlot(0);
            if (aSlot != null) {
                switch (aSlot.getItemDamage()) {
                    case 32345 -> magnetTier = MagnetTiers.Iron;
                    case 32346 -> magnetTier = MagnetTiers.Steel;
                    case 32347 -> magnetTier = MagnetTiers.Neodymium;
                    case 32348 -> magnetTier = MagnetTiers.Samarium;
                    case 32349 -> magnetTier = MagnetTiers.Tengam;
                    default -> magnetTier = null;
                }
            }

        }
    }

    public static boolean isValidElectromagnet(ItemStack aMagnet) {
        return aMagnet != null && aMagnet.getItem() instanceof MetaGeneratedItem01
            && aMagnet.getItemDamage() >= 32345
            && aMagnet.getItemDamage() <= 32349;
    }

    private boolean addMagHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchMagnet) {
                ((MTEHatchMagnet) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                if (mMagHatch == null) {
                    mMagHatch = (MTEHatchMagnet) aMetaTileEntity;
                    return true;
                }
            }
        }
        return false;
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

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEMultiBlockBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER);
    }
}
