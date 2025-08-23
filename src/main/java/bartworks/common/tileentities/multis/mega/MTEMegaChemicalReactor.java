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

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;

public class MTEMegaChemicalReactor extends MTEExtendedPowerMultiBlockBase<MTEMegaChemicalReactor>
    implements ISurvivalConstructable {

    private int glassTier = -1;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMegaChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaChemicalReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    // spotless:off
                    {"TTTTT","DPTPD","DPTPD","DPTPD","DPTPD","DPTPD","DPTPD","DPTPD","TTTTT"},
                    {"TGGGT"," GGG "," GGG "," GGG "," GGG "," GGG "," GGG "," GGG ","TEEET"},
                    {"TG~GT"," GCG "," GCG "," GCG "," GCG "," GCG "," GCG "," GCG ","TEEET"},
                    {"TGGGT"," GGG "," GGG "," GGG "," GGG "," GGG "," GGG "," GGG ","TEEET"},
                    {"TTTTT","DPTPD","DPTPD","DPTPD","DPTPD","DPTPD","DPTPD","DPTPD","TTTTT"}}))
                    // spotless:on
        .addElement('P', Casings.PipeCasingPTFE.asElement())
        .addElement('T', Casings.ChemicallyInertCasing.asElement())
        .addElement(
            'D',
            buildHatchAdder(MTEMegaChemicalReactor.class).atLeast(InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(Casings.ChemicallyInertCasing.textureId)
                .dot(1)
                .buildAndChain(Casings.ChemicallyInertCasing.asElement()))
        .addElement(
            'E',
            buildHatchAdder(MTEMegaChemicalReactor.class)
                .atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputHatch, OutputBus, Maintenance)
                .casingIndex(Casings.ChemicallyInertCasing.textureId)
                .dot(2)
                .buildAndChain(Casings.ChemicallyInertCasing.asElement()))
        .addElement('C', ofChain(ofBlock(GregTechAPI.sBlockCasings4, 7), ofBlock(GregTechAPI.sBlockCasings5, 13)))
        .addElement('G', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .build();

    public MTEMegaChemicalReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaChemicalReactor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEMegaChemicalReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaChemicalReactor(mName);
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

    // TODO: change TT after chrombread's tooltip improvements
    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Reactor, MCR")
            .addInfo("What molecule do you want to synthesize ?")
            .addInfo("Or you want to replace something in this molecule ?")
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addGlassEnergyLimitInfo()
            .addPerfectOCInfo()
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addUnlimitedTierSkips()
            .beginStructureBlock(5, 5, 9, false)
            .addController("Front center")
            .addCasingInfoMin("Chemically Inert Machine Casing", 46, false)
            .addCasingInfoExactly("Fusion Coil Block", 7, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 28, false)
            .addCasingInfoExactly("Any Tiered Glass", 64, true)
            .addEnergyHatch("Hint block ", 2)
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
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.multiblockChemicalReactorRecipes;
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
        buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 0, realBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        glassTier = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 0)) return false;
        if (mMaintenanceHatches.size() != 1) return false;

        for (MTEHatchEnergy mEnergyHatch : mEnergyHatches) {
            if (mEnergyHatch.mTier >= glassTier) {
                return false;
            }
        }
        if (glassTier < VoltageIndex.UV && !mExoticEnergyHatches.isEmpty()) return false;
        return true;
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
