package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_crafting extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public static final String crafter = "EM Crafting";
    // endregion

    // region structure
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.crafter.hint.0"), // 1 - Classic Hatches or High Power
                                                                                 // Casing
            translateToLocal("gt.blockmachines.multimachine.em.crafter.hint.1"), // 2 - Elemental Hatches or Molecular
                                                                                 // Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_crafting> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_crafting>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] {
                                    { " AAA ", "AAAAA", "A   A", "BBBBB", "BGCGB", "BGGGB", "BGCGB", "BBBBB", "A   A",
                                            "AAAAA", " AAA " },
                                    { "AHHHA", "AAAAA", " FFF ", "BBBBB", "GGGGG", "GGGGG", "GGGGG", "BBBBB", " FFF ",
                                            "AAAAA", "AHHHA" },
                                    { "AH~HA", "AAAAA", " FEF ", "BBEBB", "CGEGC", "GGDGG", "CGEGC", "BBEBB", " FEF ",
                                            "AAAAA", "AHHHA" },
                                    { "AHHHA", "AAAAA", " FFF ", "BBBBB", "GGGGG", "GGGGG", "GGGGG", "BBBBB", " FFF ",
                                            "AAAAA", "AHHHA" },
                                    { " AAA ", "AAAAA", "A   A", "BBBBB", "BGCGB", "BGGGB", "BGCGB", "BBBBB", "A   A",
                                            "AAAAA", " AAA " } }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4)).addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement('C', ofBlock(sBlockCasingsTT, 6)).addElement('D', ofBlock(sBlockCasingsTT, 9))
            .addElement('E', ofBlock(sBlockCasingsTT, 10)).addElement('G', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement(
                    'H',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_crafting::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement(
                    'F',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_crafting::addElementalToMachineList,
                            textureOffset + 4,
                            2,
                            sBlockCasingsTT,
                            4))
            .build();
    // endregion

    public GT_MetaTileEntity_EM_crafting(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_crafting(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_crafting(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 2, 2, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_CRAFTING");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_CRAFTING_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.crafter.name")) // Machine Type: Matter
                                                                                             // Assembler
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.crafter.desc.0")) // Controller block of the
                                                                                              // Matter Assembler
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(5, 5, 11, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.Elemental"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing2D"),
                        2) // Elemental Hatch: Any Molecular Casing with 2 dot
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                                // High Power Casing
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                     // Hatch: Any High
                                                                                                     // Power Casing
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12],
                    new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12] };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 2, 0, stackSize, hintsOnly);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_crafting> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
