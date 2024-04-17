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
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class GT_MetaTileEntity_PurificationPlant
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PurificationPlant> {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /**
     * Used to keep track of casing amount, so we can impose a minimum number of casings
     */
    private int mCasingAmount;

    /**
     * Maximum distance in each axis between the purification plant main controller and the controller blocks of the
     * purification plant units.
     */
    public static final int MAX_UNIT_DISTANCE = 16;

    /**
     * Time in ticks for a full processing cycle to complete.
     */
    public static final int CYCLE_TIME_TICKS = 10 * 20; // TODO: Set to proper value after debugging

    /**
     * Stores all purification units linked to this controller.
     * Normally all units in this list should be valid and unique, if not then there is a bug where they are not being
     * unlinked properly on block destruction/relinking.
     */
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

        if (!checkHatches()) {
            return false;
        }

        return true;
    }

    private boolean checkHatches() {
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        updateCycleProgress();
        if (mMaxProgresstime > 0) {
            mEfficiency = Math.max(
                0,
                Math.min(
                    mEfficiency + mEfficiencyIncrease,
                    getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
        }
    }

    private void updateCycleProgress() {
        // Since the plant does not run recipes directly, we just continuously loop the base cycle
        if (mMachine) {
            // cycle is running, so simply advance it
            if (mMaxProgresstime > 0) {
                // onRunningTick is responsible for draining power
                if (onRunningTick(mInventory[1])) {
                    markDirty();
                    mProgresstime += 1;
                    // Update progress time for active units
                    for (LinkedPurificationUnit unit : this.mLinkedUnits) {
                        if (unit.isActive()) {
                            GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
                            metaTileEntity.mProgresstime = mProgresstime;
                        }
                    }
                    // Cycle finished
                    if (mProgresstime >= mMaxProgresstime) {
                        this.endCycle();
                    }
                } else {
                    // Power drain failed, shut down all other units due to power loss.
                    for (LinkedPurificationUnit unit : mLinkedUnits) {
                        if (unit.isActive()) {
                            unit.metaTileEntity()
                                .stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                        }
                    }
                }
            }

            // No cycle running, start a new cycle if the machine is turned on
            if (mMaxProgresstime == 0 && isAllowedToWork()) {
                this.startCycle();
            }
        }
    }

    private void startCycle() {
        mProgresstime = 0;
        mMaxProgresstime = CYCLE_TIME_TICKS;
        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);

        // Find active units and notify them that the cycle started
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
            PurificationUnitStatus status = metaTileEntity.status();
            // Unit needs to be online to be considered active.
            if (status == PurificationUnitStatus.ONLINE) {
                // Perform recipe check for unit and start it if successful
                CheckRecipeResult result = metaTileEntity.checkProcessing();
                if (result.wasSuccessful()) {
                    unit.setActive(true);
                    metaTileEntity.startCycle(mMaxProgresstime, mProgresstime);
                }
            }
        }

        // After activating all units, calculate power usage
        lEUt = -calculateEffectivePowerUsage();
    }

    private void endCycle() {
        mMaxProgresstime = 0;

        // Mark all units as inactive and reset their progress time
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
            // If this unit was active, end the cycle
            if (unit.isActive()) {
                metaTileEntity.endCycle();
            }
            unit.setActive(false);
        }
    }

    /**
     * Calculate power usage of all units
     */
    private long calculateEffectivePowerUsage() {
        long euT = 0;
        for (LinkedPurificationUnit unit : mLinkedUnits) {
            if (unit.isActive()) {
                euT += unit.metaTileEntity()
                    .getActivePowerUsage();
            }
        }
        return euT;
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
        this.mLinkedUnits.add(new LinkedPurificationUnit(unit));
    }

    public void unregisterLinkedUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        this.mLinkedUnits.removeIf(link -> link.metaTileEntity() == unit);
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
        // Show linked purification units and their status
        ret.add("Linked Purification Units: ");
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            String text = EnumChatFormatting.AQUA + unit.metaTileEntity()
                .getLocalName() + ": ";
            PurificationUnitStatus status = unit.metaTileEntity()
                .status();
            switch (status) {
                case ONLINE -> {
                    text = text + EnumChatFormatting.GREEN + "Online";
                }
                case DISABLED -> {
                    text = text + EnumChatFormatting.YELLOW + "Disabled";
                }
                case INCOMPLETE_STRUCTURE -> {
                    text = text + EnumChatFormatting.RED + "Incomplete Structure";
                }
            }
            ret.add(text);
        }
        return ret.toArray(new String[0]);
    }

    @Override
    public void onBlockDestroyed() {
        // When the controller is destroyed we want to notify all currently linked units
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            unit.metaTileEntity()
                .unlinkController();
        }
        super.onBlockDestroyed();
    }
}
