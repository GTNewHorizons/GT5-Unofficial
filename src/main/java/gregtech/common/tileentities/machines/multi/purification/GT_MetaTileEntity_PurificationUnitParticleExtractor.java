package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
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

        public static ItemList[] CATALYST_ITEMS = new ItemList[] { ItemList.Quark_Creation_Catalyst_Up,
            ItemList.Quark_Creation_Catalyst_Down, ItemList.Quark_Creation_Catalyst_Bottom,
            ItemList.Quark_Creation_Catalyst_Top, ItemList.Quark_Creation_Catalyst_Strange,
            ItemList.Quark_Creation_Catalyst_Charm };

        public CatalystCombination(ItemStack first, ItemStack second) {
            firstCatalyst = first;
            secondCatalyst = second;
        }

        public boolean matches(ItemStack a, ItemStack b) {
            return (a.isItemEqual(firstCatalyst) && b.isItemEqual(secondCatalyst))
                || (b.isItemEqual(firstCatalyst) && a.isItemEqual(secondCatalyst));
        }

        public NBTTagCompound saveToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound first = new NBTTagCompound();
            NBTTagCompound second = new NBTTagCompound();
            firstCatalyst.writeToNBT(first);
            secondCatalyst.writeToNBT(second);
            nbt.setTag("first", first);
            nbt.setTag("second", second);
            return nbt;
        }

        public static CatalystCombination readFromNBT(NBTTagCompound nbt) {
            NBTTagCompound first = nbt.getCompoundTag("first");
            NBTTagCompound second = nbt.getCompoundTag("second");
            return new CatalystCombination(
                ItemStack.loadItemStackFromNBT(first),
                ItemStack.loadItemStackFromNBT(second));
        }
    }

    private static CatalystCombination generateNewCombination() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // Generate two unique indices into the list
        int firstIndex = random.nextInt(0, CatalystCombination.CATALYST_ITEMS.length);
        int secondIndex = random.nextInt(0, CatalystCombination.CATALYST_ITEMS.length);
        while (secondIndex != firstIndex) {
            secondIndex = random.nextInt(0, CatalystCombination.CATALYST_ITEMS.length);
        }

        return new CatalystCombination(
            CatalystCombination.CATALYST_ITEMS[firstIndex].get(1),
            CatalystCombination.CATALYST_ITEMS[secondIndex].get(1));
    }

    private CatalystCombination currentCombination = null;

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
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitParticleExtractor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit");
        tt.toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (this.currentCombination != null) {
            aNBT.setTag("currentCombination", this.currentCombination.saveToNBT());
        }
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("currentCombination")) {
            currentCombination = CatalystCombination.readFromNBT(aNBT.getCompoundTag("currentCombination"));
        }
        super.loadNBTData(aNBT);
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
