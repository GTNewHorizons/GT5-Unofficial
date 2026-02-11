/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.multis.mega;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;

public class MTEMegaChemicalReactorLegacy extends MegaMultiBlockBase<MTEMegaChemicalReactorLegacy>
    implements ISurvivalConstructable {

    private int glassTier = -1;

    public MTEMegaChemicalReactorLegacy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaChemicalReactorLegacy(String aName) {
        super(aName);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Reactor, MCR")
            .addInfo(
                TooltipHelper.coloredText(
                    TooltipHelper.italicText("\"What molecule do you want to synthesize?\""),
                    EnumChatFormatting.DARK_GRAY))
            .addInfo(
                TooltipHelper.coloredText(
                    TooltipHelper.italicText("\"Or do you want to replace something in this molecule?\""),
                    EnumChatFormatting.DARK_GRAY))
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addPerfectOCInfo()
            .addSeparator()
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addGlassEnergyLimitInfo()
            .addUnlimitedTierSkips()
            .beginStructureBlock(5, 5, 9, false)
            .addController("Front center")
            .addCasingInfoMin("Chemically Inert Machine Casing", 46, false)
            .addCasingInfoExactly("Fusion Coil Block", 7, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 28, false)
            .addCasingInfoExactly("Any Tiered Glass", 64, true)
            .addEnergyHatch("Hint block ", 3)
            .addMaintenanceHatch("Hint block ", 2)
            .addInputHatch("Hint block ", 1)
            .addInputBus("Hint block ", 1)
            .addOutputBus("Hint block ", 1)
            .addOutputHatch("Hint block ", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaChemicalReactorLegacy(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.multiblockChemicalReactorRecipes;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            this.batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        inputSeparation = !inputSeparation;
        GTUtility.sendChatTrans(
            aPlayer,
            inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 0, realBudget, env, false, true);
    }
    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = -1;

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 0) || this.mMaintenanceHatches.size() != 1) return false;

        if (this.glassTier < VoltageIndex.UV) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
                    return false;
                }
            }
        }

        return true;
    }

    private static final int CASING_INDEX = 176;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMegaChemicalReactorLegacy> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaChemicalReactorLegacy>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ttttt", "dptpd", "dptpd", "dptpd", "dptpd", "dptpd", "dptpd", "dptpd", "ttttt" },
                    { "tgggt", " ggg ", " ggg ", " ggg ", " ggg ", " ggg ", " ggg ", " ggg ", "teeet" },
                    { "tg~gt", " gcg ", " gcg ", " gcg ", " gcg ", " gcg ", " gcg ", " gcg ", "teret" },
                    { "tgggt", " ggg ", " ggg ", " ggg ", " ggg ", " ggg ", " ggg ", " ggg ", "teeet" },
                    { "ttttt", "dptpd", "dptpd", "dptpd", "dptpd", "dptpd", "dptpd", "dptpd", "ttttt" }, }))
        .addElement('p', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement('t', ofBlock(GregTechAPI.sBlockCasings8, 0))
        .addElement(
            'd',
            buildHatchAdder(MTEMegaChemicalReactorLegacy.class).atLeast(InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement('r', Maintenance.newAny(CASING_INDEX, 2))
        .addElement(
            'e',
            buildHatchAdder(MTEMegaChemicalReactorLegacy.class)
                .atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputHatch, OutputBus)
                .casingIndex(CASING_INDEX)
                .hint(3)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement('c', ofChain(ofBlock(GregTechAPI.sBlockCasings4, 7), ofBlock(GregTechAPI.sBlockCasings5, 13)))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .build();

    @Override
    public IStructureDefinition<MTEMegaChemicalReactorLegacy> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
