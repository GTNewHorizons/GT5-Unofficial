package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
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

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MagHatch;
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
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_IndustrialElectromagneticSeparator>
    implements ISurvivalConstructable {

    public enum MagnetTiers {

        Iron(6, 0.8F, 1.5F, false),
        Steel(24, 0.75F, 2F, false),
        Neodymium(48, 0.7F, 2.5F, false),
        Samarium(96, 0.6F, 3F, false),
        Tengam(256, 0.5F, 5F, true);

        final int maxParallel;
        final float euModifier, speedBoost;
        final boolean supportsExotic;

        MagnetTiers(int maxParallel, float euModifier, float speedBoost, boolean supportsExotic) {
            this.maxParallel = maxParallel;
            this.euModifier = euModifier;
            this.speedBoost = 1F / speedBoost;
            this.supportsExotic = supportsExotic;
        }
    }

    private final ArrayList<GT_MetaTileEntity_MagHatch> mMagHatches = new ArrayList<>();
    private MagnetTiers magnetTier = null;
    private boolean polarizerMode = false;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialElectromagneticSeparator>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
        .addElement(
            'C',
            ofChain(
                buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                    .adder(GT_MetaTileEntity_IndustrialElectromagneticSeparator::addMagHatch)
                    .hatchClass(GT_MetaTileEntity_MagHatch.class)
                    .shouldReject(t -> !t.mMagHatches.isEmpty())
                    .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(0))
                    .dot(1)
                    .build(),
                buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy, ExoticEnergy, Muffler)
                    .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(
                        onElementPass(
                            GT_MetaTileEntity_IndustrialElectromagneticSeparator::onCasingAdded,
                            ofBlock(GregTech_API.sBlockCasings10, 0)))))
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
            .addInfo("With Tengam electromagnet, exotic hatches are allowed")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Electromagnetic Casings", 6, false)
            .addOtherStructurePart("Electromagnet Housing", "1 Only, Any Casing")
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionIndustrialElectromagneticSeparatorPerSecond;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mMagHatches.clear();

        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0) && mCasingAmount >= 6 && mMagHatches.size() == 1;
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
                    maxParallel = magnetTier.maxParallel;
                    euModifier = magnetTier.euModifier;
                    speedBoost = magnetTier.speedBoost;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return SimpleCheckRecipeResult.ofFailure("electromagnet_missing");
            }
        };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return polarizerMode ? RecipeMaps.polarizerRecipes : RecipeMaps.electroMagneticSeparatorRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.polarizerRecipes, RecipeMaps.electroMagneticSeparatorRecipes);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("polarizerMode", polarizerMode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        polarizerMode = aNBT.getBoolean("polarizerMode");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        polarizerMode = !polarizerMode;
        if (polarizerMode) {
            PlayerUtils.messagePlayer(aPlayer, "Now running in Polarizing Mode.");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Now running in Separating Mode.");
        }
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
        tag.setBoolean("mode", polarizerMode);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal(
                    "GT5U.INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR.mode." + (tag.getBoolean("mode") ? 1 : 0))
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

    private void findMagnet() {
        magnetTier = null;
        if (mMagHatches.size() == 1) {
            GT_MetaTileEntity_MagHatch aBus = mMagHatches.get(0);
            if (aBus != null) {
                ItemStack aSlot = aBus.getStackInSlot(0);
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
    }

    public static boolean isValidElectromagnet(ItemStack aMagnet) {
        return aMagnet != null && aMagnet.getItem() instanceof GT_MetaGenerated_Item_01
            && aMagnet.getItemDamage() >= 32345
            && aMagnet.getItemDamage() <= 32349;
    }

    private boolean addMagHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_MagHatch aMagHatch) {
                ((GT_MetaTileEntity_MagHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                mMagHatches.add(aMagHatch);
                return true;
            }
        }
        return false;
    }
}
