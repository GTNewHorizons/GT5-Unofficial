package gregtech.common.blocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.HarvestTool;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;

/**
 * The purpose of this test is twofold:
 * <ol>
 * <li>Showcase how to add a subclass with a custom TileEntity that still supports MTE</li>
 * <li>Make sure future refactorings don't break this use case</li>
 * </ol>
 */
public class BlockMachinesSubclassTest {

    @Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = "ChiselAPI")
    private static class MyBlockMachines extends BlockMachines implements ITileEntityProvider {

        public MyBlockMachines() {
            super(ItemBlock.class, "modid.machines", Material.wood);
        }

        @Override
        public TileEntity createNewTileEntity(World aWorld, int aMeta) {
            return new MyTileEntity();
        }
    }

    private static class MyTileEntity extends BaseMetaTileEntity {

        public MyTileEntity() {
            mID = 18000; // Just needs a random non-zero number to make base methods happy.
            createNewMetatileEntity(mID);
        }

        @Override
        protected boolean createNewMetatileEntity(short aID) {
            assert aID != 0;
            templateInstance.newMetaEntity(this)
                .setBaseMetaTileEntity(this);
            return true;
        }
    }

    private static MyMTE templateInstance;

    private static class MyMTE extends MetaTileEntity {

        public static MyMTE createTemplateInstance() {
            return new MyMTE();
        }

        private MyMTE() {
            super("my.mte", 0); // Don't go through the 4-arg constructor. It would register this MTE in the
                                // METATILEENTITIES.

            // Other init stuff here.
            GTLanguageManager.addStringLocalization("my.mte.name", "The localized name");
        }

        /** Copy constructor for newMetaEntity as to not duplicate args */
        private MyMTE(String aName, int aInvSlotCount) {
            super(aName, aInvSlotCount);
        }

        @Override
        public byte getTileEntityBaseType() {
            return HarvestTool.WrenchLevel0.toTileEntityBaseType();
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
            return new MyMTE(mName, mInventory.length);
        }

        @Override
        public void saveNBTData(NBTTagCompound aNBT) {

        }

        @Override
        public void loadNBTData(NBTTagCompound aNBT) {

        }

        @Override
        public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
            return false;
        }

        @Override
        public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
            return false;
        }

        @Override
        public String[] getDescription() {
            return new String[0];
        }

        @Override
        public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean active, boolean redstoneLevel) {
            return new ITexture[0];
        }
    }

    @Test
    void testCanInstantiateThings() {
        templateInstance = MyMTE.createTemplateInstance();

        // Mocked GameRegistry for the block construction.
        // And apparently the mocked getIC2Coolant is enough to let us instantiate real BaseMetaTileEntity classes.
        // Huzzah!
        try (MockedStatic<GameRegistry> gameRegistry = mockStatic(GameRegistry.class);
            MockedStatic<GTModHandler> modHandler = mockStatic(GTModHandler.class)) {
            modHandler.when(() -> GTModHandler.getIC2Coolant(anyLong()))
                .thenReturn(mock(FluidStack.class));

            MyBlockMachines block = new MyBlockMachines();
            TileEntity tileEntity = block.createNewTileEntity(mock(World.class), 0);

            assertInstanceOf(MyTileEntity.class, tileEntity);

            MyTileEntity myTileEntity = (MyTileEntity) tileEntity;
            assertInstanceOf(MyMTE.class, myTileEntity.getMetaTileEntity());
            assertSame(
                myTileEntity,
                myTileEntity.getMetaTileEntity()
                    .getBaseMetaTileEntity());
        }
    }
}
