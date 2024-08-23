package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
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
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MagHatch;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings10;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_IndustrialElectromagneticSeparator
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_IndustrialElectromagneticSeparator>
    implements ISurvivalConstructable {

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
            String tooltip = "Used in Magnetic Flux Exhibitor/n " + EnumChatFormatting.LIGHT_PURPLE
                + "Speed: +"
                + Math.round((1F / m.speedBoost * 100) - 100)
                + "%/n "
                + EnumChatFormatting.DARK_PURPLE
                + "EU Usage: "
                + Math.round(m.euModifier * 100)
                + "%/n "
                + EnumChatFormatting.AQUA
                + "Parallel: "
                + m.maxParallel;

            if (m.supportsExotic) tooltip = tooltip + "/n "
                + EnumChatFormatting.BOLD
                + EnumChatFormatting.GREEN
                + "Can Use Multiamp Hatches";

            return tooltip;
        }
    }

    final int MIN_CASING = 64;
    private GT_MetaTileEntity_MagHatch mMagHatch = null;
    private MagnetTiers magnetTier = null;

    private static final int MACHINEMODE_SEPARATOR = 0;
    private static final int MACHINEMODE_POLARIZER = 1;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialElectromagneticSeparator>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "  CCC  ", "       ", "       ", "       ", "       ", "  B~B  " },
                { " CC CC ", "       ", "  BBB  ", "  AAA  ", "  BEB  ", " BBBBB " },
                { "CC   CC", "       ", " BBBBB ", " A   A ", " BBBBB ", "BBBBBBB" },
                { "C     C", "C     C", "CBBBBBC", "CA C AC", "CBBBBBC", "BBBBBBB" },
                { "CC   CC", "       ", " BBBBB ", " A   A ", " BBBBB ", "BBBBBBB" },
                { " CC CC ", "       ", "  BBB  ", "  AAA  ", "  BBB  ", " BBBBB " },
                { "  CCC  ", "   C   ", "   C   ", "   C   ", "   C   ", "  BBB  " } }))
        .addElement('A', Glasses.chainAllGlasses())
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                    .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(
                        onElementPass(
                            GT_MetaTileEntity_IndustrialElectromagneticSeparator::onCasingAdded,
                            ofBlock(GregTech_API.sBlockCasings10, 0)))))
        .addElement('C', ofFrame(Materials.NeodymiumMagnetic))
        .addElement(
            'E',
            buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                .adder(GT_MetaTileEntity_IndustrialElectromagneticSeparator::addMagHatch)
                .hatchClass(GT_MetaTileEntity_MagHatch.class)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(0))
                .dot(2)
                .build())
        .build();

    public GT_MetaTileEntity_IndustrialElectromagneticSeparator(final int aID, final String aName,
        final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialElectromagneticSeparator(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialElectromagneticSeparator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 0)),
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
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 0)),
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
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 0)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Electromagnetic Separator/Polarizer")
            .addInfo("Controller Block for the Magnetic Flux Exhibitor")
            .addInfo("Use screwdriver to switch mode")
            .addInfo("Insert an electromagnet into the electromagnet housing to use")
            .addInfo("Better electromagnets give further bonuses")
            .addInfo("With Tengam electromagnet, multiamp (NOT laser) hatches are allowed")
            .addInfo(
                AuthorFourIsTheNumber + EnumChatFormatting.GRAY
                    + " & "
                    + EnumChatFormatting.GOLD
                    + "Ba"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "ps")
            .addSeparator()
            .beginStructureBlock(7, 6, 7, false)
            .addController("Front Center")
            .addCasingInfoMin("MagTech Casings", MIN_CASING, false)
            .addOtherStructurePart("Any glass", "x12")
            .addOtherStructurePart("Magnetic Neodymium Frame Box", "x37")
            .addOtherStructurePart("Electromagnet Housing", "1 Block Above/Behind Controller", 2)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 5, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 5, 0, elementBudget, env, false, true);
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

        // If there are exotic hatches, ensure there is only 1, and it is not laser. Only multiamp allowed
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            if (mExoticEnergyHatches.size() > 1) return false;
            if (mExoticEnergyHatches.get(0)
                .maxWorkingAmperesIn() > 64) return false;
        }

        // All checks passed!
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
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
        }.setMaxParallelSupplier(this::getMaxParallels);
    }

    private int getMaxParallels() {
        if (magnetTier != null) return magnetTier.maxParallel;
        return 0;
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        PlayerUtils.messagePlayer(
            aPlayer,
            String.format(StatCollector.translateToLocal("GT5U.MULTI_MACHINE_CHANGE"), getMachineModeName()));
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GT_UITextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR);
        machineModeIcons.add(GT_UITextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER);
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR.mode." + machineMode);
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
        tag.setInteger("mode", machineMode);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + StatCollector
                    .translateToLocal("GT5U.INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR.mode." + tag.getInteger("mode"))
                + EnumChatFormatting.RESET);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
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
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        // This fix works for normal energy hatches, preventing over-paralleling with 1 energy hatch
        // However, it does not work with multiamp. MuTEs can't come soon enough.

        if (mExoticEnergyHatches.isEmpty()) {
            logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
            logic.setAvailableAmperage(1L);
        } else super.setProcessingLogicPower(logic);
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
        return aMagnet != null && aMagnet.getItem() instanceof GT_MetaGenerated_Item_01
            && aMagnet.getItemDamage() >= 32345
            && aMagnet.getItemDamage() <= 32349;
    }

    private boolean addMagHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_MagHatch) {
                ((GT_MetaTileEntity_MagHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                if (mMagHatch == null) {
                    mMagHatch = (GT_MetaTileEntity_MagHatch) aMetaTileEntity;
                    return true;
                }
            }
        }
        return false;
    }
}
