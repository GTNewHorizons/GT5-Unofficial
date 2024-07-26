package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_MetaTileEntity_PurificationUnitParticleExtractor
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitParticleExtractor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 7;
    private static final int STRUCTURE_Y_OFFSET = 6;
    private static final int STRUCTURE_Z_OFFSET = 1;

    static final String[][] structure = new String[][] {
        // spotless:off
        { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "DDDDDDDDDDDDDDD" },
        { "               ", "               ", "               ", "               ", "               ", "      BBB      ", "      B~B      ", "      BBB      ", "DDDDDDDDDDDDDDD" },
        { "AAAAAAAAAAAAAAA", "A     BBB     A", "A     EEE     A", "A             A", "A             A", "ABE   FFF   EBA", "ABE   FCF   EBA", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "      EEE      ", "       C       ", "       C       ", " BE   FCF   EB ", " BECCCCCCCCCEB ", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "AAAAAAAAAAAAAAA", "A     BBB     A", "A     EEE     A", "A             A", "A             A", "ABE   FFF   EBA", "ABE   FCF   EBA", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "      EEE      ", "               ", "               ", "      FFF      ", "      FCF      ", "      BBB      ", "DDDDDDDDDDDDDDD" },
        { "AAAAAAAAAAAAAAA", "A     BBB     A", "A     EEE     A", "A             A", "A             A", "ABE   FFF   EBA", "ABE   FCF   EBA", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "      EEE      ", "       C       ", "       C       ", " BE   FCF   EB ", " BECCCCCCCCCEB ", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "AAAAAAAAAAAAAAA", "A     BBB     A", "A     EEE     A", "A             A", "A             A", "ABE   FFF   EBA", "ABE   FCF   EBA", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "      EEE      ", "               ", "               ", "      FFF      ", "      FCF      ", "      BBB      ", "DDDDDDDDDDDDDDD" },
        { "AAAAAAAAAAAAAAA", "A     BBB     A", "A     EEE     A", "A             A", "A             A", "ABE   FFF   EBA", "ABE   FCF   EBA", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "      EEE      ", "       C       ", "       C       ", " BE   FCF   EB ", " BECCCCCCCCCEB ", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "AAAAAAAAAAAAAAA", "A     BBB     A", "A     EEE     A", "A             A", "A             A", "ABE   FFF   EBA", "ABE   FCF   EBA", "BBE   BBB   EBB", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "      EEE      ", "               ", "               ", "      BBB      ", "      BBB      ", "      BBB      ", "DDDDDDDDDDDDDDD" },
        { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "DDDDDDDDDDDDDDD" } };
        // spotless:on

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitParticleExtractor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitParticleExtractor>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofFrame(Materials.Bedrockium))
        // Dimensionally transcendent casing (placeholder)
        .addElement('B', ofBlock(GregTech_API.sBlockCasings1, 12))
        // Dimensional bridge (placeholder)
        .addElement('C', ofBlock(GregTech_API.sBlockGlass1, 14))
        // Naquadah Water Plant Casing (maybe placeholder)
        .addElement('D', ofBlock(GregTech_API.sBlockCasings9, 7))
        // High power casing (placeholder)
        .addElement('E', ofBlock(TT_Container_Casings.sBlockCasingsTT, 0))
        // Blue glass (placeholder, currently set to vanilla glass)
        .addElement('F', ofBlock(Blocks.stained_glass, 9))
        .build();

    private static class CatalystCombination {

        public ItemStack firstCatalyst;
        public ItemStack secondCatalyst;

        public CatalystCombination(ItemStack first, ItemStack second) {
            firstCatalyst = first;
            secondCatalyst = second;
        }

        public boolean matches(ItemStack a, ItemStack b) {
            return (a.isItemEqual(firstCatalyst) && b.isItemEqual(secondCatalyst))
                || (b.isItemEqual(firstCatalyst) && a.isItemEqual(secondCatalyst));
        }
    }

    public GT_MetaTileEntity_PurificationUnitParticleExtractor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitParticleExtractor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitParticleExtractor(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48] };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitParticleExtractor> getStructureDefinition() {
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit");
        tt.toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 8;
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_UEV;
    }
}
