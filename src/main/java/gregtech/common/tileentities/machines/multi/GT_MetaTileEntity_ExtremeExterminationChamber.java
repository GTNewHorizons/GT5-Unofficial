package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import crazypants.enderio.EnderIO;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GT_MetaTileEntity_ExtremeExterminationChamber extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_ExtremeExterminationChamber> {

    public static final GT_Recipe.GT_Recipe_Map EECRecipeMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(4), "gt.recipe.eec", "Extreme Extermination Chamber", null, RES_PATH_GUI + "basicmachines/Default", 1, 4, 1, 0, 1, E, 0, E, false, true);

    public GT_MetaTileEntity_ExtremeExterminationChamber(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ExtremeExterminationChamber(String aName) {
        super(aName);
    }

    private static final Item poweredSpawnerItem = Item.getItemFromBlock(EnderIO.blockPoweredSpawner);
    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_ExtremeExterminationChamber> STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_ExtremeExterminationChamber>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
            {"ccccc", "ccccc", "ccccc", "ccccc", "ccccc"},
            {"ccccc", "c---c", "c---c", "c---c", "ccccc"},
            {"ccccc", "c---c", "c---c", "c---c", "ccccc"},
            {"ccccc", "c---c", "c---c", "c---c", "ccccc"},
            {"ccccc", "c---c", "c---c", "c---c", "ccccc"},
            {"ccccc", "csssc", "csssc", "csssc", "ccccc"},
            {"CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"},
        }))
        .addElement('c', ofBlock(GregTech_API.sBlockCasings2, 0))
        .addElement('C', ofChain(
            ofBlock(GregTech_API.sBlockCasings2, 0),
            ofHatchAdder(GT_MetaTileEntity_ExtremeExterminationChamber::addInputToMachineList, CASING_INDEX, 1),
            ofHatchAdder(GT_MetaTileEntity_ExtremeExterminationChamber::addEnergyInputToMachineList, CASING_INDEX, 1),
            ofHatchAdder(GT_MetaTileEntity_ExtremeExterminationChamber::addMaintenanceToMachineList, CASING_INDEX, 1)
        ))
        .addElement('s', isAir()) // Maybe spikes ?
        .build();

    @Override
    public IStructureDefinition<GT_MetaTileEntity_ExtremeExterminationChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.
            addMachineType("Powered Spawner").
            addInfo("Controller block for Extreme Extermination Chamber").
            addInfo("Spawns and Exterminates monster's for you").
            addInfo("Author: " + EnumChatFormatting.GOLD + "kuba6000").
            addSeparator().
            beginStructureBlock(5, 7, 5, true).
            addController("Front Bottom Center").
            addCasingInfo("Solid Steel Machine Casing", 10).
            addOutputBus("Any casing", 1).
            addEnergyHatch("Any casing", 1).
            addMaintenanceHatch("Any casing", 1).
            toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 6, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ExtremeExterminationChamber(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    private static class fakeRand extends Random{
        public int maxbound = 1;
        public int overridenext = 0;
        @Override
        public int nextInt(int bound) {
            if(maxbound < bound)
                maxbound = bound;
            return overridenext >= bound ? bound - 1 : overridenext;
        }
    }

    private static void initializeRecipeMap(){
        if(!EECRecipeMap.mRecipeList.isEmpty())
            return;
        World f = new GT_DummyWorld()
        {

        };
        f.rand = new fakeRand();

        EntityList.stringToClassMapping.forEach((k, v) -> {
            if(!(v instanceof Class))
                return;

            EntityLiving e = null;
            try {
                e = (EntityLiving)((Class)v).getConstructor(new Class[] {World.class}).newInstance(new Object[] {f});
            }
            catch (ClassCastException ex)
            {
                // not a EntityLiving
                return;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if(e == null)
                return;

            int id = EntityList.getEntityID(e);

            e.captureDrops = true;

            // POWERFULL GENERATION

            ((fakeRand)f.rand).maxbound = 1;
            ((fakeRand)f.rand).overridenext = 0;
            Class s = e.getClass();
            while(!s.equals(EntityLiving.class))
                s = s.getSuperclass();
            Method dropFewItems;
            try {
                dropFewItems = s.getDeclaredMethod("dropFewItems", boolean.class, int.class);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return;
            }
            dropFewItems.setAccessible(true);
            try {
                dropFewItems.invoke(e, true, 0);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return;
            }
            HashMap<GT_Utility.ItemId, ItemStack> drops = new HashMap<>();
            HashMap<GT_Utility.ItemId, Integer> dropcount = new HashMap<>();
            Consumer<EntityItem> addDrop = (entityItem)->{
                ItemStack stack = entityItem.getEntityItem();
                if(stack == null)
                    return;
                GT_Utility.ItemId itemId = GT_Utility.ItemId.createNoCopy(stack);
                drops.putIfAbsent(itemId, stack);
                dropcount.merge(itemId, 1, Integer::sum);
            };
            e.capturedDrops.forEach(addDrop);
            if(((fakeRand)f.rand).maxbound > 1)
            {
                e.capturedDrops.clear();
                for(int i = 1; i < ((fakeRand)f.rand).maxbound; i++)
                {
                    ((fakeRand)f.rand).overridenext = i;
                    try {
                        dropFewItems.invoke(e, true, 0);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return;
                    }
                    e.capturedDrops.forEach(addDrop);
                    e.capturedDrops.clear();
                }
            }

            if(drops.isEmpty())
                return;

            double maxchance = ((fakeRand)f.rand).maxbound;

            ItemStack[] outputs = new ItemStack[drops.size()];
            int[] outputchances = new int[drops.size()];
            int i = 0;
            for (Map.Entry<GT_Utility.ItemId, ItemStack> entry : drops.entrySet()) {
                GT_Utility.ItemId kk = entry.getKey();
                ItemStack vv = entry.getValue();
                outputs[i] = vv;
                outputchances[i++] = (int) ((dropcount.get(kk).doubleValue() / maxchance) * 10000);
            }
            EECRecipeMap.addFakeRecipe(false, new ItemStack[] { new ItemStack(Items.spawn_egg, 1, id) }, outputs, null, outputchances, new FluidStack[0], new FluidStack[0], 40, 10, 0);



        });

    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        initializeRecipeMap();
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if(aStack == null)
            return false;

        if(aStack.getItem().equals(poweredSpawnerItem))
            return false;

        if(aStack.getTagCompound() == null)
            return false;
        String mobType = aStack.getTagCompound().getString("mobType");
        if(mobType.isEmpty())
            return false;

        // do something ?

        this.mEUt = 8000;
        this.mMaxProgresstime = 40;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if(!checkPiece(STRUCTURE_PIECE_MAIN, 2, 6, 0))
            return false;
        return mMaintenanceHatches.size() == 1 && mEnergyHatches.size() > 0;
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

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return EECRecipeMap;
    }
}
