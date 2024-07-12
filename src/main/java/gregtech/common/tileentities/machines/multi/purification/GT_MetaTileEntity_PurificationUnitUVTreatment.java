package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.IGT_HatchAdder;

public class GT_MetaTileEntity_PurificationUnitUVTreatment
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitUVTreatment>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX_MAIN = getTextureIndex(GregTech_API.sBlockCasings9, 11);

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 6;
    private static final int STRUCTURE_Y_OFFSET = 8;
    private static final int STRUCTURE_Z_OFFSET = 0;

    private GT_MetaTileEntity_Hatch_InputBus lensInputBus;

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "             ", "     DDD     ", "             ", "             ", "             ", "             ", "             ", "     DDD     ", "     A~A     " },
        { "     AAA     ", "   DDAAADD   ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "   DDBBBDD   ", "   AAAAAAA   " },
        { "   AAAAAAA   ", " DDAACCCAADD ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", " DDBB   BBDD ", " AAAAAAAAAAA " },
        { " AAAAAAAAAAA ", "DAACCCCCCCAAD", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", "DBB       BBD", "AAAAAAAAAAAAA" },
        { " AAAAALAAAAA ", "DACCCCCCCCCAD", " B         B ", " B         B ", " B         B ", " B         B ", " B         B ", "DB         BD", "AAAAAAAAAAAAA" },
        { " AAAAAAAAAAA ", "DAACCCCCCCAAD", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", "DBB       BBD", "AAAAAAAAAAAAA" },
        { "   AAAAAAA   ", " DDAACCCAADD ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", " DDBB   BBDD ", " AAAAAAAAAAA " },
        { "     AAA     ", "   DDAAADD   ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "   DDBBBDD   ", "   AAAAAAA   " },
        { "             ", "     DDD     ", "             ", "             ", "             ", "             ", "             ", "     DDD     ", "     AAA     " } };
        // spotless:on

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitUVTreatment> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitUVTreatment>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Naquadria-Reinforced Water Plant Casing
        .addElement('A', ofBlock(GregTech_API.sBlockCasings9, 11))
        // Neutronium-Coated UV-Resistant Glass
        .addElement('B', ofBlock(GregTech_API.sBlockGlass1, 1))
        // UV Backlight sterilizer casing
        .addElement('C', ofBlock(GregTech_API.sBlockCasings9, 12))
        .addElement('D', ofFrame(Materials.StellarAlloy))
        // Lens housing bus
        .addElement(
            'L',
            lazy(
                t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitUVTreatment>buildHatchAdder()
                    .atLeast(SpecialHatchElement.LensHousing)
                    .dot(2)
                    .cacheHint(() -> "Lens Housing")
                    .casingIndex(CASING_INDEX_MAIN)
                    .build()))
        .build();

    public GT_MetaTileEntity_PurificationUnitUVTreatment(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitUVTreatment(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitUVTreatment(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitUVTreatment> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addInfo(AuthorNotAPenguin)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 6;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_UV;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    public boolean addLensHousingToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_LensHousing) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            this.lensInputBus = (GT_MetaTileEntity_LensHousing) aMetaTileEntity;
            return true;
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<GT_MetaTileEntity_PurificationUnitUVTreatment> {

        LensHousing(GT_MetaTileEntity_PurificationUnitUVTreatment::addLensHousingToMachineList,
            GT_MetaTileEntity_LensHousing.class) {

            @Override
            public long count(
                GT_MetaTileEntity_PurificationUnitUVTreatment gtMetaTileEntityPurificationUnitUVTreatment) {
                if (gtMetaTileEntityPurificationUnitUVTreatment.lensInputBus == null) return 0;
                else return 1;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_PurificationUnitUVTreatment> adder;

        @SafeVarargs
        SpecialHatchElement(IGT_HatchAdder<GT_MetaTileEntity_PurificationUnitUVTreatment> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_PurificationUnitUVTreatment> adder() {
            return adder;
        }
    }
}
