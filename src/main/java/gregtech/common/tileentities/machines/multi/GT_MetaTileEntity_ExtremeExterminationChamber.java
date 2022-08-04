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
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GT_MetaTileEntity_ExtremeExterminationChamber extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_ExtremeExterminationChamber> {

    public static final GT_Recipe.GT_Recipe_Map EECRecipeMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(4), "gt.recipe.eec", "Extreme Extermination Chamber", null, RES_PATH_GUI + "basicmachines/Default", 1, 6, 1, 0, 1, E, 0, E, false, false);
    private static final HashMap<String, GT_Recipe> MobNameToRecipeMap = new HashMap<>();

    private static final HashSet<String> MobBlacklist = new HashSet<>(Arrays.asList(new String[]{"chisel.snowman"}));

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
            ofHatchAdder(GT_MetaTileEntity_ExtremeExterminationChamber::addOutputToMachineList, CASING_INDEX, 1),
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
            addInfo("Spawns and Exterminates monsters for you").
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
        fakeRand frand = new fakeRand();
        f.rand = frand;

        GT_Log.out.println("[EEC]Generating Recipe Map for Extreme Extermination Chamber");

        EntityList.stringToClassMapping.forEach((k, v) -> {
            if(!(v instanceof Class))
                return;

            if(MobBlacklist.contains((String)k))
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
                ex.printStackTrace(GT_Log.err);
                return;
            }
            if(e == null)
                return;

            int id = EntityList.getEntityID(e);

            GT_Log.out.println("[EEC]Generating entry for mob: " + (String)k);

            e.captureDrops = true;

            // POWERFULL GENERATION

            Class s = e.getClass();
            while(!s.equals(EntityLivingBase.class)) {
                if(s.equals(EntitySlime.class))
                {
                    try{
                        Method setSlimeSize = s.getDeclaredMethod("setSlimeSize", int.class);
                        setSlimeSize.setAccessible(true);
                        setSlimeSize.invoke(e, 1);
                    }
                    catch (Exception ignored){}
                }

                s = s.getSuperclass();
            }
            Method dropFewItems;
            Method dropRareDrop;
            try {
                dropFewItems = s.getDeclaredMethod("dropFewItems", boolean.class, int.class);
                dropFewItems.setAccessible(true);
                dropRareDrop = s.getDeclaredMethod("dropRareDrop", int.class);
                dropRareDrop.setAccessible(true);
                Field rand = s.getSuperclass().getDeclaredField("rand");
                rand.setAccessible(true);
                rand.set(e, frand);
            }
            catch (Exception ex)
            {
                ex.printStackTrace(GT_Log.err);
                return;
            }


            HashMap<GT_Utility.ItemId, ItemStack> drops = new HashMap<>();
            HashMap<GT_Utility.ItemId, Integer> dropcount = new HashMap<>();
            HashMap<GT_Utility.ItemId, ItemStack> raredrops = new HashMap<>();
            HashMap<GT_Utility.ItemId, Integer> raredropcount = new HashMap<>();
            Consumer<EntityItem> addDrop = (entityItem)->{
                ItemStack stack = entityItem.getEntityItem();
                if(stack == null)
                    return;
                GT_Utility.ItemId itemId = GT_Utility.ItemId.createNoCopy(stack);
                drops.putIfAbsent(itemId, stack);
                dropcount.merge(itemId, stack.stackSize, Integer::sum);
            };
            Consumer<EntityItem> addDropRare = (entityItem)->{
                ItemStack stack = entityItem.getEntityItem();
                if(stack == null)
                    return;
                GT_Utility.ItemId itemId = GT_Utility.ItemId.createNoCopy(stack);
                raredrops.putIfAbsent(itemId, stack);
                raredropcount.merge(itemId, stack.stackSize, Integer::sum);
            };

            GT_Log.out.println("[EEC]Generating normal drops");

            frand.maxbound = 1;
            frand.overridenext = 0;

            try {
                dropFewItems.invoke(e, true, 0);
            }
            catch (Exception ex)
            {
                ex.printStackTrace(GT_Log.err);
                return;
            }

            e.capturedDrops.forEach(addDrop);
            if(frand.maxbound > 1)
            {
                e.capturedDrops.clear();
                for(int i = 1; i < frand.maxbound; i++)
                {
                    frand.overridenext = i;
                    try {
                        dropFewItems.invoke(e, true, 0);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace(GT_Log.err);
                        return;
                    }
                    e.capturedDrops.forEach(addDrop);
                    e.capturedDrops.clear();
                }
            }

            double maxnormalchance = frand.maxbound;

            GT_Log.out.println("[EEC]Generating rare drops");

            frand.maxbound = 1;
            frand.overridenext = 0;

            try {
                dropRareDrop.invoke(e, 0);
            }
            catch (Exception ex)
            {
                ex.printStackTrace(GT_Log.err);
                return;
            }
            e.capturedDrops.forEach(addDropRare);
            if(frand.maxbound > 1)
            {
                e.capturedDrops.clear();
                for(int i = 1; i < frand.maxbound; i++)
                {
                    frand.overridenext = i;
                    try {
                        dropRareDrop.invoke(e, 0);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace(GT_Log.err);
                        return;
                    }
                    e.capturedDrops.forEach(addDropRare);
                    e.capturedDrops.clear();
                }
            }

            double maxrarechance = frand.maxbound;

            if(drops.isEmpty() && raredrops.isEmpty()) {
                GT_Log.out.println("[EEC]Entity " + (String)k + " doesn't drop any items, skipping");
                return;
            }

            ItemStack[] outputs = new ItemStack[drops.size() + raredrops.size()];
            int[] outputchances = new int[drops.size() + raredrops.size()];
            int i = 0;
            for (Map.Entry<GT_Utility.ItemId, ItemStack> entry : drops.entrySet()) {
                GT_Utility.ItemId kk = entry.getKey();
                ItemStack vv = entry.getValue();
                outputs[i] = vv;
                outputchances[i] = (int) ((dropcount.get(kk).doubleValue() / maxnormalchance) * 10000);
                while(outputchances[i] > 10000)
                {
                    outputs[i].stackSize *= 2;
                    outputchances[i] /= 2;
                }
                i++;
            }
            for (Map.Entry<GT_Utility.ItemId, ItemStack> entry : raredrops.entrySet()) {
                GT_Utility.ItemId kk = entry.getKey();
                ItemStack vv = entry.getValue();
                outputs[i] = vv;
                outputchances[i] = (int) ((raredropcount.get(kk).doubleValue() / maxrarechance) * 250);
                while(outputchances[i] > 10000)
                {
                    outputs[i].stackSize *= 2;
                    outputchances[i] /= 2;
                }
                i++;
            }
            MobNameToRecipeMap.put((String)k, EECRecipeMap.addFakeRecipe(true, new ItemStack[] { new ItemStack(Items.spawn_egg, 1, id).setStackDisplayName( EnumChatFormatting.RESET + (String)k) }, outputs, null, outputchances, new FluidStack[0], new FluidStack[0], (int)e.getMaxHealth() * 2, 8000, 0));
            GT_Log.out.println("[EEC]Added " + (String)k);
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

        if(aStack.getItem() != poweredSpawnerItem)
            return false;

        if(aStack.getTagCompound() == null)
            return false;
        String mobType = aStack.getTagCompound().getString("mobType");
        if(mobType.isEmpty())
            return false;

        GT_Recipe recipe = MobNameToRecipeMap.get(mobType);

        if(recipe == null)
            return false;

        ArrayList<ItemStack> outputs = new ArrayList<>(recipe.mOutputs.length);
        for (int i = 0; i < recipe.mOutputs.length; i++)
            if (getBaseMetaTileEntity().getRandomNumber(10000) < recipe.getOutputChance(i))
                outputs.add(recipe.getOutput(i));

        this.mOutputItems = outputs.toArray(new ItemStack[0]);
        calculateOverclockedNessMulti(recipe.mEUt, recipe.mDuration, 2, getMaxInputVoltage());
        if(this.mEUt > 0)
            this.mEUt = -this.mEUt;
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
}
