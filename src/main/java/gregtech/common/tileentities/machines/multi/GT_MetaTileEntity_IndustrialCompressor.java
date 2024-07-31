package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.GT_Values.Ollie;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.GT_Block_Casings2;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_IndustrialCompressor extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_IndustrialCompressor> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_HIP = "hip";
    private static final String STRUCTURE_PIECE_BLACKHOLE = "blackhole";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A A", "AAA" }, { "AAA", "AAA", "AAA" } }))
        .addShape(STRUCTURE_PIECE_HIP, (new String[][] { { " AA", "  C", " CC", " C ", " on" } }))
        .addShape(STRUCTURE_PIECE_BLACKHOLE, (new String[][] { { "AA ", " A ", " b ", " A ", "AAA" } }))
        .addElement(
            'A',
            buildHatchAdder(GT_MetaTileEntity_IndustrialCompressor.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch, OutputHatch)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_IndustrialCompressor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0))))
        .addElement(
            'b',
            buildHatchAdder(GT_MetaTileEntity_IndustrialCompressor.class)
                .adder(GT_MetaTileEntity_IndustrialCompressor::addBlackHoleHatch)
                .hatchClass(GT_MetaTileEntity_Hatch_Input.class)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .build())
        .addElement(
            'C',
            ofCoil(
                GT_MetaTileEntity_IndustrialCompressor::setCoilLevel,
                GT_MetaTileEntity_IndustrialCompressor::getCoilLevel))
        .addElement(
            'n',
            buildHatchAdder(GT_MetaTileEntity_IndustrialCompressor.class)
                .adder(GT_MetaTileEntity_IndustrialCompressor::addNeutroniumHatch)
                .hatchClass(GT_MetaTileEntity_Hatch_InputBus.class)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .build())
        .addElement(
            'o',
            buildHatchAdder(GT_MetaTileEntity_IndustrialCompressor.class)
                .adder(GT_MetaTileEntity_IndustrialCompressor::addNeutroniumOutput)
                .hatchClass(GT_MetaTileEntity_Hatch_OutputBus.class)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .build())
        .build();

    private boolean hipEnabled = false;
    private HeatingCoilLevel heatLevel;
    private int coolingCounter = 0;

    private boolean blackholeEnabled = false;
    private boolean blackholeOn = false;
    private GT_MetaTileEntity_Hatch_Input blackHoleHatch;

    private int tier = 0;
    private int heat = 0;
    private boolean cooling = false;

    private final FluidStack blackholeMaintainCost = new FluidStack((MaterialsUEVplus.SpaceTime).getMolten(1), 1);
    private final FluidStack blackholeCost = new FluidStack((MaterialsUEVplus.SpaceTime).getMolten(1), 16000);

    private boolean addBlackHoleHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                blackHoleHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
                blackHoleHatch.updateTexture(aBaseCasingIndex);
                return true;
            }
        }
        return false;
    }

    public GT_MetaTileEntity_IndustrialCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialCompressor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialCompressor(this.mName);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        boolean oCooling = cooling;
        cooling = (aValue & 1) == 1;
        if (oCooling != cooling) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) (cooling ? 1 : 0);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (cooling) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Compressor")
            .addInfo("Controller Block for the Big Ol Compressor Fella")
            .addInfo(AuthorFourIsTheNumber + EnumChatFormatting.RESET + " & " + Ollie)
            .addSeparator()
            .beginStructureBlock(7, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addOutputHatch("Any Solid Steel Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        switch (tier) {
            case 1 -> buildPiece(STRUCTURE_PIECE_HIP, stackSize, hintsOnly, -3, 3, -1);
            case 2 -> buildPiece(STRUCTURE_PIECE_BLACKHOLE, stackSize, hintsOnly, 5, 3, -1);
            default -> buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        switch (tier) {
            case 1 -> {
                return survivialBuildPiece(STRUCTURE_PIECE_HIP, stackSize, -3, 3, -1, elementBudget, env, false, true);
            }
            case 2 -> {
                return survivialBuildPiece(
                    STRUCTURE_PIECE_BLACKHOLE,
                    stackSize,
                    5,
                    3,
                    -1,
                    elementBudget,
                    env,
                    false,
                    true);
            }
            default -> {
                return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
            }
        }
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        setCoilLevel(HeatingCoilLevel.None);
        mCasingAmount = 0;
        mEnergyHatches.clear();
        tier = 0;
        hipEnabled = false;
        blackholeEnabled = false;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0)) return false;
        tier = 1;
        if (checkPiece(STRUCTURE_PIECE_HIP, -3, 3, -1)) {
            tier = 2;
            hipEnabled = true;
            if (checkPiece(STRUCTURE_PIECE_BLACKHOLE, 5, 3, -1)) {
                tier = 3;
                blackholeEnabled = true;
            }
        }
        if (mCasingAmount < 0) return false;

        // All checks passed!
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", tier);
        tag.setInteger("heat", heat);
        tag.setBoolean("cooling", cooling);
        tag.setBoolean("hipEnabled", hipEnabled);
        tag.setBoolean("blackholeEnabled", blackholeEnabled);
        tag.setBoolean("blackholeOn", blackholeOn);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("hipEnabled")) {
            if (tag.getBoolean("cooling")) currentTip.add(
                "HIP Heat: " + EnumChatFormatting.RED
                    + EnumChatFormatting.BOLD
                    + tag.getInteger("heat")
                    + "%"
                    + EnumChatFormatting.RESET);
            else currentTip.add(
                "HIP Heat: " + EnumChatFormatting.AQUA
                    + EnumChatFormatting.BOLD
                    + tag.getInteger("heat")
                    + "%"
                    + EnumChatFormatting.RESET);
        }
        if (tag.getBoolean("blackholeEnabled")) {
            if (tag.getBoolean("blackholeOn")) currentTip.add(EnumChatFormatting.DARK_PURPLE + "Black Hole Stabilized");

            else currentTip.add(EnumChatFormatting.DARK_PURPLE + "Black Hole Offline");
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            protected CheckRecipeResult onRecipeStart(@Nonnull GT_Recipe recipe) {
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setSpeedBonus(1F / 2F);
        // .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (cooling) {
            stopMachine(SimpleShutDownReason.ofCritical("overheated"));
        } else {
            heat = heat + 1;
            if (heat >= 100) {
                heat = 100;
                cooling = true;
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Updates every 10 sec
            if (mUpdate <= -150) mUpdate = 50;
        }

        if (hipEnabled) {
            checkNeutroniumHatch();
            if (coolingCounter >= 4) {
                coolingCounter = 0;
                heat -= 1;
                if (heat <= 0) {
                    heat = 0;
                    cooling = false;
                }
            } else coolingCounter += 1;
        }
        if (blackholeOn) {
            if (blackHoleHatch != null) {
                if (drain(blackHoleHatch, blackholeMaintainCost, false)) {
                    drain(blackHoleHatch, blackholeMaintainCost, true);
                    return;
                }
            }
            explodeMultiblock();
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> toggleBlackHole())
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (!blackholeOn) ret.add(GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_NONE);
                    else ret.add(GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_ALL);
                    return ret.toArray(new IDrawable[0]);
                })
                .setPos(80, 91)
                .setSize(16, 16));
    }

    private void toggleBlackHole() {
        if (blackholeEnabled) {
            if (blackholeOn) {
                blackholeOn = false;
            } else {
                if (blackHoleHatch != null) {
                    if (drain(blackHoleHatch, blackholeCost, false)) {
                        drain(blackHoleHatch, blackholeCost, true);
                        blackholeOn = true;
                    }
                }
            }
        }
    }

    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
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

    public HeatingCoilLevel getCoilLevel() {
        return heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        heatLevel = aCoilLevel;
    }

    /*
     * NEUTRONIUM COMPRESSOR LOGIC BELOW
     */

    GT_MetaTileEntity_Hatch_InputBus neutroniumHatch;
    GT_MetaTileEntity_Hatch_OutputBus neutroniumOutput;
    int ironBlocks = 0;

    private boolean addNeutroniumHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                neutroniumHatch = (GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity;
                neutroniumHatch.updateTexture(aBaseCasingIndex);
                return true;
            }
        }
        return false;
    }

    private boolean addNeutroniumOutput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
                neutroniumOutput = (GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity;
                neutroniumOutput.updateTexture(aBaseCasingIndex);
                return true;
            }
        }
        return false;
    }

    private void checkNeutroniumHatch() {
        if (neutroniumHatch != null) {
            if (neutroniumHatch.getBaseMetaTileEntity().hasInventoryBeenModified()) {
                for (int i = 0; i < neutroniumHatch.mInventory.length; i++) {
                    if (neutroniumHatch.mInventory[i] != null) {
                        if (neutroniumHatch.mInventory[i].getUnlocalizedName() == "tile.blockiron") ;
                        {
                            ironBlocks += neutroniumHatch.mInventory[i].stackSize;
                            neutroniumHatch.mInventory[i] = null;

                            if (ironBlocks >= 300) {
                                ironBlocks -= 300;

                            }
                        }
                    }
                }
            }
        }
    }
}
