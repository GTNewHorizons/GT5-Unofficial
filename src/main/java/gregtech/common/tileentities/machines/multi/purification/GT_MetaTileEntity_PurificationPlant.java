package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;

public class GT_MetaTileEntity_PurificationPlant
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PurificationPlant> {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private int mCasingAmount;

    // Maximum distance between the purification plant main controller and the controller blocks of the
    // purification plant units.
    public static final int MAX_UNIT_DISTANCE = 16;

    private final List<LinkedPurificationUnit> mLinkedUnits = new ArrayList<>();

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationPlant> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationPlant>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A A", "AAA" }, { "AAA", "AAA", "AAA" } })
        .addElement(
            'A',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationPlant>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .casingIndex(48)
                        .dot(1)
                        .build()),
                onElementPass(t -> t.mCasingAmount++, ofBlock(GregTech_API.sBlockCasings4, 0))))
        .build();

    public GT_MetaTileEntity_PurificationPlant(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationPlant(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationPlant> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Plant")
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .toolTipFinisher(
                EnumChatFormatting.WHITE + "Not"
                    + EnumChatFormatting.DARK_AQUA
                    + "A"
                    + EnumChatFormatting.AQUA
                    + "Penguin");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationPlant(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        // TODO: Proper textures instead of copying PA textures.
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    private List<IHatchElement<? super GT_MetaTileEntity_PurificationPlant>> getAllowedHatches() {
        return ImmutableList.of(Maintenance, Energy, ExoticEnergy);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check self
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0)) {
            return false;
        }

        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Only do slower checks every 100 ticks to avoid lag.
            // This check tests if aTick mod 100 = 10 to avoid these checks happening in the same ticks
            // as all other machines that do checks every 100 ticks, to spread out the workload further.
            if (aTick % 100 == 10) {
                // Update status of potentially deformed units
                for (LinkedPurificationUnit unit : mLinkedUnits) {
                    if (!unit.metaTileEntity().mMachine) {
                        unit.setStatus(PurificationUnitStatus.INCOMPLETE_STRUCTURE);
                    } else {
                        unit.setStatus(PurificationUnitStatus.ONLINE);
                    }
                }
            }
        }
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

    public void registerLinkedUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        PurificationUnitStatus status = unit.mMachine ? PurificationUnitStatus.ONLINE
            : PurificationUnitStatus.INCOMPLETE_STRUCTURE;
        this.mLinkedUnits.add(new LinkedPurificationUnit(unit, status));
    }

    public void unregisterLinkedUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        this.mLinkedUnits.remove(unit);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        // Save link data to data stick, very similar to Crafting Input Buffer.

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "PurificationPlant");
        tag.setInteger("x", aBaseMetaTileEntity.getXCoord());
        tag.setInteger("y", aBaseMetaTileEntity.getYCoord());
        tag.setInteger("z", aBaseMetaTileEntity.getZCoord());

        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName(
            "Purification Plant Link Data Stick (" + aBaseMetaTileEntity
                .getXCoord() + ", " + aBaseMetaTileEntity.getYCoord() + ", " + aBaseMetaTileEntity.getZCoord() + ")");
        aPlayer.addChatMessage(new ChatComponentText("Saved Link Data to Data Stick"));
    }

    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        ret.add("Linked Purification Units: ");
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            String text = EnumChatFormatting.AQUA + unit.metaTileEntity()
                .getLocalName() + ": ";
            if (unit.status() == PurificationUnitStatus.ONLINE) {
                text = text + EnumChatFormatting.GREEN + "Online";
            } else if (unit.status() == PurificationUnitStatus.INCOMPLETE_STRUCTURE) {
                text = text + EnumChatFormatting.RED + "Incomplete Structure";
            }
            ret.add(text);
        }
        return ret.toArray(new String[0]);
    }

    @Override
    public void onBlockDestroyed() {
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            unit.metaTileEntity()
                .unlinkController();
        }
        super.onBlockDestroyed();
    }
}
