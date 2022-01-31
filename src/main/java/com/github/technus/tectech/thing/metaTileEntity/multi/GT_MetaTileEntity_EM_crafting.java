package com.github.technus.tectech.thing.metaTileEntity.multi;

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
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_crafting extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public static final String crafter = "EM Crafting";
    //endregion

    //region structure
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.crafter.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.crafter.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_crafting> STRUCTURE_DEFINITION=
            IStructureDefinition.<GT_MetaTileEntity_EM_crafting>builder()
            .addShape("main", transpose(new String[][]{
                    {" AAA ","AAAAA","A   A","BBBBB","BGCGB","BGGGB","BGCGB","BBBBB","A   A","AAAAA"," AAA "},
                    {"AHHHA","AAAAA"," FFF ","BBBBB","GGGGG","GGGGG","GGGGG","BBBBB"," FFF ","AAAAA","AHHHA"},
                    {"AH~HA","AAAAA"," FEF ","BBEBB","CGEGC","GGDGG","CGEGC","BBEBB"," FEF ","AAAAA","AHHHA"},
                    {"AHHHA","AAAAA"," FFF ","BBBBB","GGGGG","GGGGG","GGGGG","BBBBB"," FFF ","AAAAA","AHHHA"},
                    {" AAA ","AAAAA","A   A","BBBBB","BGCGB","BGGGB","BGCGB","BBBBB","A   A","AAAAA"," AAA "}
            }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4))
            .addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement('C', ofBlock(sBlockCasingsTT, 6))
            .addElement('D', ofBlock(sBlockCasingsTT, 9))
            .addElement('E', ofBlock(sBlockCasingsTT, 10))
            .addElement('F', ofHatchAdderOptional(GT_MetaTileEntity_EM_crafting::addElementalToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .addElement('G', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement('H', ofHatchAdderOptional(GT_MetaTileEntity_EM_crafting::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .build();
    //endregion

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
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.crafter.desc.0"),//The most precise way of making stuff.
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.crafter.desc.1")//
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
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
