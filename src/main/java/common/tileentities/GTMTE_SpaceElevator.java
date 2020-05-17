package common.tileentities;

import common.Blocks;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;
import util.MultiBlockTooltipBuilder;
import util.Vector3i;
import util.Vector3ic;

import java.util.ArrayList;

public class GTMTE_SpaceElevator extends GT_MetaTileEntity_MultiBlockBase {

    private static final Block BASE_BLOCK = Blocks.spaceElevatorStructure;
    private static final int BASE_META = 0;
    private static final int COIL_HOLDER_META = 1;
    private static final int HATCH_OVERLAY_ID = 16;

    private long lastLaunchEUCost = 0;

    public GTMTE_SpaceElevator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTMTE_SpaceElevator(String aName) { super(aName); }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new GTMTE_SpaceElevator((super.mName));
    }

    @Override
    public String[] getDescription() {
        final MultiBlockTooltipBuilder b = new MultiBlockTooltipBuilder();
        b.addInfo("Access for your Space Station!")
                .addInfo("Check out the wiki on my github if you are having trouble with the structure")
                .addInfo("Regenerative Breaking will recover up to X% of the energy spent on launch")
                .addInfo("Energy recovered depends on coil tier: +10% per coil tier, up to 90%")
                .addSeparator()
                .beginStructureBlock(15, 11, 15)
                .addController("Bottom Center")
                .addEnergyHatch("Instead of any casing in the bottom floor")
                .addMaintenanceHatch("Instead of any casing in the bottom floor")
                .addCasingInfo("Solid Steel Machine Casing", 320)
                .addOtherStructurePart("Any EBF coil", "40x, have to be all the same")
                .addOtherStructurePart("Space Elevator Tether", "4x")
                .addOtherStructurePart("Space Elevator Cabin Block", "42x")
                .addOtherStructurePart("Space Elevator Cabin Guide", "8x")
                .signAndFinalize("Kekzdealer");
        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return b.getInformation();
        } else {
            return b.getStructureInformation();
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
                                 boolean aActive, boolean aRedstone) {
        ITexture[] sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
                Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
        if (aSide == aFacing && aActive) {
            sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW,
                    Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
        }
        return sTexture;
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
                "MultiblockDisplay.png");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack stack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack stack) {
        this.mProgresstime = 1;
        this.mMaxProgresstime = 1;
        this.mEUt = 0;
        this.mEfficiencyIncrease = 10000;
        return true;
    }

    public Vector3ic rotateOffsetVector(Vector3ic forgeDirection, int x, int y, int z) {
        final Vector3i offset = new Vector3i();
        // either direction on y-axis
        if (forgeDirection.y() == -1) {
            offset.x = x;
            offset.y = z;
            offset.z = y;
        }

        return offset;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {
        // Make sure the controller is either facing up or down
        if(thisController.getFrontFacing() > 1) {
            return false;
        }

        // Figure out the vector for the direction the back face of the controller is facing
        final Vector3ic forgeDirection = new Vector3i(
                ForgeDirection.getOrientation(thisController.getBackFacing()).offsetX,
                ForgeDirection.getOrientation(thisController.getBackFacing()).offsetY,
                ForgeDirection.getOrientation(thisController.getBackFacing()).offsetZ
        );
        boolean formationChecklist = true;
        int minCasingAmount = 320;
        int firstCoilMeta = -1;

        // Base floor
        for(int X = -7; X <= 7; X++){
            for(int Z = -7; Z <= 7; Z++){
                if(X == 0 && Z == 0){
                    continue; // Skip controller
                }

                final Vector3ic offset = rotateOffsetVector(forgeDirection, X, 0, Z);
                final IGregTechTileEntity currentTE =
                        thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());

                // Tries to add TE as either of those kinds of hatches.
                // The number is the texture index number for the texture that needs to be painted over the hatch texture
                if (   !super.addMaintenanceToMachineList(currentTE, HATCH_OVERLAY_ID)
                        && !this.addEnergyInputToMachineList(currentTE, HATCH_OVERLAY_ID)) {

                    // If it's not a hatch, is it the right casing for this machine? Check block and block meta.
                    if ((thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == BASE_BLOCK)
                            && (thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z()) == BASE_META)) {
                        // Seems to be valid casing. Decrement counter.
                        minCasingAmount--;
                    } else {
                        formationChecklist = false;
                    }
                }
            }
        }
        KekzCore.LOGGER.info("Space Elevator Base accepted");
        // Capacitor banks

        // Anchor

        // Coil holders

        // Coils

        if(minCasingAmount > 0) {
            formationChecklist = false;
        }

        return formationChecklist;
    }

    @Override
    public String[] getInfoData() {
        final ArrayList<String> ll = new ArrayList<>();
        ll.add(EnumChatFormatting.YELLOW + "Operational Data:" + EnumChatFormatting.RESET);

        ll.add("Maintenance Status: " + ((super.getRepairStatus() == super.getIdealStatus())
                ? EnumChatFormatting.GREEN + "Working perfectly" + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + "Has Problems" + EnumChatFormatting.RESET));
        ll.add("---------------------------------------------");

        final String[] a = new String[ll.size()];
        return ll.toArray(a);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        nbt = (nbt == null) ? new NBTTagCompound() : nbt;

        super.saveNBTData(nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        nbt = (nbt == null) ? new NBTTagCompound() : nbt;

        super.loadNBTData(nbt);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack stack) { return 10000; }

    @Override
    public int getPollutionPerTick(ItemStack stack) { return 0; }

    @Override
    public int getDamageToComponent(ItemStack stack) { return 0; }

    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) { return false; }
}
