package gregtech.common.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.*;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.PacketTeleportPlayer;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase.CoverData;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;


public class ItemSniffer extends GTGenericItem implements IGuiHolder<GuiData> {
    public static final Logger LOGGER = LogManager.getLogger("SNIFFER");

    private static final Map<Integer, String> regularWirelessLabels = new HashMap<>();
    private static final Map<String, Map<Integer, Map<CoverData, String>>> advWirelessLabels = new HashMap<>();
    private final ItemStackHandler stackHandler = new ItemStackHandler(1);
    private String uuid;
    private String filter = "";

    public ItemSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        ;
    }

    public Map<CoverData, String> getCoversOnFrequency(String uuid, int frequency){
        Map<Integer, Map<CoverData, String>> publicCovers = advWirelessLabels.getOrDefault(uuid, new HashMap<>());
        return publicCovers.getOrDefault(frequency, new HashMap<>());
    }



    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            this.uuid = String.valueOf(player.getUniqueID());
            this.filter = "";
            loadFromNBT(stack.getTagCompound());
            GuiFactories.item().open(player);

        }
        return super.onItemRightClick(stack,world,player);
    }
    public static NBTTagCompound serializeRegularToNBT(NBTTagCompound base){
        NBTTagCompound regularWireless = new NBTTagCompound();
        regularWirelessLabels.forEach((frequency, label) -> {
            regularWireless.setString(String.valueOf(frequency), label);
        });
        base.setTag("Regular", regularWireless);
        return base;
    }
    public static <K, V> NBTTagCompound serializeAdvancedToNBT(NBTTagCompound base, Map<K, V> map) {
        for(Map.Entry<K, V> entry : map.entrySet()) {
            if(entry.getValue() instanceof Map mapEntry) {
                base.setTag(entry.getKey().toString(), serializeAdvancedToNBT(new NBTTagCompound(), mapEntry));
            } else if(entry.getKey() instanceof CoverData data){
                NBTTagCompound coverTag = new NBTTagCompound();
                coverTag.setInteger("x", data.x);
                coverTag.setInteger("y", data.y);
                coverTag.setInteger("z", data.z);
                coverTag.setInteger("dim", data.dim);
                coverTag.setInteger("side", data.side);
                coverTag.setString("label", (String) entry.getValue());
                base.setTag(String.valueOf(data.hashCode()), coverTag);
            } else if(entry.getValue() instanceof String stringEntry) {
                base.setString(entry.getKey().toString(), stringEntry);
            } else if(entry.getValue() instanceof Integer intEntry) {
                base.setInteger(entry.getKey().toString(), intEntry);
            }
        }
        return base;
    }

    public void loadFromNBT(NBTTagCompound tag){
        System.out.println(tag);
        NBTTagCompound regular = tag.getCompoundTag("Regular");
        if (!regular.hasNoTags()){
            regular.tagMap.keySet().forEach(frequency -> {
                regularWirelessLabels.put(Integer.valueOf((String) frequency), regular.getString(String.valueOf(frequency)));
            });
        }
        String[] owners = {"Public", this.uuid};
        for(String owner : owners){
            if (tag.hasKey(owner)){
                NBTTagCompound frequencyMap = tag.getCompoundTag(owner);
                if(frequencyMap.hasNoTags()) continue;

                frequencyMap.tagMap.keySet().forEach(frequency -> {
                    NBTTagCompound coverMap = frequencyMap.getCompoundTag((String) frequency);
                    if (coverMap.hasNoTags()) return;

                    coverMap.tagMap.keySet().forEach(cover -> {
                        NBTTagCompound data = coverMap.getCompoundTag((String) cover);
                        int x = data.getInteger("x");
                        int y = data.getInteger("y");
                        int z = data.getInteger("z");
                        int dim = data.getInteger("dim");
                        int side = data.getInteger("side");
                        CoverData coverData = new CoverData(x, y, z, dim, side);
                        Map<Integer, Map<CoverData,String>> frequencies = advWirelessLabels.computeIfAbsent(owner, k -> new HashMap<>());
                        Map<CoverData,String> covers = frequencies.computeIfAbsent(Integer.valueOf((String) frequency), k -> new HashMap<>());
                        System.out.println(data.getString("label"));
                        covers.put(coverData, data.getString("label"));
                    });

                });

            }
        }
        System.out.println(tag);
    }
//        @Override
//    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
//
//            List<String> a = IntStream.range(0,10).mapToObj(i -> "Option" + (i+1)).collect(Collectors.toList());
//            final Map<String, SortableListWidget.Item<String>> items = new Object2ObjectOpenHashMap<>();
//            for (String line : a) {
//                items.put(line, new SortableListWidget.Item<>(line).child(item -> new Row()
//                    .child(new Widget<>()
//                        .addTooltipLine(line)
//                        .background(GuiTextures.BUTTON_CLEAN)
//                        .overlay(IKey.str(line))
//                        .expanded().heightRel(1f))
//                    .child(new ButtonWidget<>()
//                        .onMousePressed(button -> {
//                            return true;})
//                        .overlay(GuiTextures.CROSS_TINY.asIcon().size(10))
//                        .width(10).heightRel(1f))));
//            }
//            SortableListWidget<String> sortableListWidget = new SortableListWidget<String>()
//                .children(a, items::get)
//                .debugName("sortable list");
//            ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
//            panel.flex()
//                .sizeRel(0.5f, 0.75f)
//                .align(Alignment.Center);
//            panel.child(new Column()
//                .sizeRel(1)
//                .child(sortableListWidget)
//            );
//            return panel;
//        }


    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        //PagedWidget.Controller controller = new PagedWidget.Controller();





        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
                .sizeRel(0.5f, 0.75f)
                .align(Alignment.Center);
//
//        PagedWidget data = new PagedWidget();
//        data.sizeRel(1,0.75f);
//        data.controller(controller);



        ScrollWidget wirelessPage = new ScrollWidget(new VerticalScrollData());
        wirelessPage.sizeRel(1, 0.8f);
        wirelessPage.getScrollArea().getScrollY().setScrollSize(35*GregTechAPI.sWirelessRedstone.size());

        AtomicInteger i = new AtomicInteger(0);

        //Process regular wireless redstone frequencies

        GregTechAPI.sWirelessRedstone.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry ->{
            int freq = entry.getKey();
            boolean isPrivate = freq > 65535;
            int displayFreq = isPrivate ? freq - 65536 : freq;
            wirelessPage.child(
                new Row()
                .sizeRel(1f,0.2f)
                .pos(0,i.get()*35)
                .child(new TextWidget(String.valueOf(displayFreq)).widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget(isPrivate ? "Yes" : "No").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextFieldWidget()
                    .align(Alignment.CenterRight)
                    .sizeRel(1.0f/3.25f, 0.5f)
                    .value(SyncHandlers.string(
                        () -> regularWirelessLabels.getOrDefault(freq, "Description"),
                        label -> {
                            regularWirelessLabels.put(freq, label);
                            if(!guiSyncManager.isClient()){
                                NBTTagCompound tag = serializeRegularToNBT(guiSyncManager.getPlayer().getHeldItem().getTagCompound());
                                System.out.println(tag);
                                guiSyncManager.getPlayer().getHeldItem().setTagCompound(tag);
                            }
                        }))
                    ));
            i.getAndIncrement();
        });


        ParentWidget regularWireless = new ParentWidget();
        regularWireless.sizeRel(1);
        regularWireless.child(new Column()
            .child(new Row()
                .margin(5)
                .heightRel(0.1f)
                .align(Alignment.TopLeft)
                .child(new TextWidget("Frequency").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Private").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Label").widthRel(1.0f/3.0f).alignment(Alignment.Center)))
            .child(wirelessPage)
        );
        // data.addPage(regularWireless);



        //Process advanced wireless redstone frequencies
        Map<Integer, Map<CoverData, Byte>> publicFreqs = GregTechAPI.sAdvancedWirelessRedstone.getOrDefault("null", new ConcurrentHashMap<>());
        Map<Integer, Map<CoverData, Byte>> privateFreqs = GregTechAPI.sAdvancedWirelessRedstone.getOrDefault(this.uuid, new ConcurrentHashMap<>());

        int advFreqSize = publicFreqs.size() + privateFreqs.size();


        i.set(0);

        Grid grid = new Grid()
            .minColWidth(0)
            .minRowHeight(0)
            .scrollable();
        grid.getScrollArea().getScrollY().setScrollSize(35*advFreqSize);

        List<List<IWidget>> matrix = (processAdvancedFrequencies(publicFreqs, "Public", grid, guiSyncManager));
        matrix.addAll((processAdvancedFrequencies(privateFreqs, this.uuid, grid, guiSyncManager)));
        grid.matrix(matrix);

//        ParentWidget advWireless = new ParentWidget();
//        advWireless.sizeRel(1);
//
//        advWireless.child(new Column()
//            .child(new Row()
//                .margin(5)
//                .heightRel(0.1f)
//                .child(new TextWidget("Owner").widthRel(0.2f).alignment(Alignment.Center))
//                .child(new TextWidget("Frequency").widthRel(0.2f).alignment(Alignment.Center))
//                .child(new TextWidget("Coords").widthRel(0.25f).alignment(Alignment.Center))
//                .child(new TextWidget("Label").widthRel(0.2f).alignment(Alignment.Center))
//                .child(new TextWidget("Action").widthRel(0.15f).alignment(Alignment.Center))
//            )
//            .child(grid.posRel(0, 0.6f).sizeRel(1,0.8f))
//        );
//
//        data.addPage(advWireless);




        //data.addPage(advWireless);
//        panel.child(new Column()
////            .child(new Row()
////                .heightRel(0.1f)
////                .margin(10)
////                .child(new PageButton(0, controller)
////                    .widthRel(0.5f)
////                    .align(Alignment.CenterLeft)
////                    .overlay(IKey.dynamic(() -> "Regular Wireless")))
////                .child(new PageButton(1, controller)
////                    .widthRel(0.5f)
////                    .align(Alignment.CenterRight)
////                    .overlay(IKey.dynamic(() -> "Advanced Wireless"))))
//                .child(new Row()
//                    .heightRel(0.1f)
//                    .child(new TextWidget("Filter frequency: ")
//                        .widthRel(0.25f)
//                        .alignment(Alignment.Center)
//                    )
//                    .child(new TextFieldWidget()
//                        .sizeRel(0.25f, 0.5f)
//                        .value(SyncHandlers.string(() -> this.filter, filter -> {
//                            this.filter = filter;
//                            if(NetworkUtils.isClient()){
//                                System.out.println("RESIZING!!!!!\n\n\n");
//                                //WidgetTree.resize(grid);
//                            }
//                        }))
//
//                    )
//                )
//                .child(new Row()
//                    .margin(5)
//                    .heightRel(0.1f)
//                    .child(new TextWidget("Owner").widthRel(0.2f).alignment(Alignment.Center))
//                    .child(new TextWidget("Frequency").widthRel(0.2f).alignment(Alignment.Center))
//                    .child(new TextWidget("Coords").widthRel(0.25f).alignment(Alignment.Center))
//                    .child(new TextWidget("Label").widthRel(0.2f).alignment(Alignment.Center))
//                    .child(new TextWidget("Action").widthRel(0.15f).alignment(Alignment.Center))
//                )
//            //.child(data)
//                .child(grid.posRel(0,0.6f).sizeRel(1, 0.5f))
//            .margin(10));
        panel
            .child(new Column()
                .margin(10)
                .child(new Row()
                    .heightRel(0.1f)
                    .child(new TextWidget("Filter frequency: ")
                        .widthRel(0.25f)
                        .alignment(Alignment.Center)
                    )
                    .child(new TextFieldWidget()
                        .sizeRel(0.25f, 0.5f)
                        .value(SyncHandlers.string(() -> this.filter, filter -> {
                            this.filter = filter;
                            if(NetworkUtils.isClient()){
                                WidgetTree.resize(grid);
                            }
                        }))
                    )
                )
                .child(new Row()
                    .margin(5)
                    .heightRel(0.1f)
                    .child(new TextWidget("Owner").widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget("Frequency").widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget("Coords").widthRel(0.25f).alignment(Alignment.Center))
                    .child(new TextWidget("Label").widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget("Action").widthRel(0.15f).alignment(Alignment.Center))
                )
                .child(new Row().heightRel(0.8f).child(grid.sizeRel(1)))
            );

        return panel;
    }

    public List<List<IWidget>> processAdvancedFrequencies(Map<Integer, Map<CoverData, Byte>> frequencyMap, String owner, Grid grid, PanelSyncManager guiSyncManager){


        AtomicInteger size = new AtomicInteger();
        frequencyMap.forEach((k, v) -> {
            size.addAndGet(v.size());
        });
        List<List<IWidget>> result = new ArrayList<>();
        frequencyMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry ->{
            int freq = entry.getKey();
            Map<CoverData, Byte> coverMap = entry.getValue();
            coverMap.forEach((cover, useless) -> {
                List<IWidget> row = new ArrayList<>();
                row.add(new Row(){
                    @Override
                    public Flow setEnabledIf(Predicate<Flow> condition) {
                        return onUpdateListener(w -> {
                            setEnabled(condition.test(w));
                            heightRel(isEnabled() ? 0.2f : 0);
                        }, true);
                    }
                }.setEnabledIf(w -> {
                        try{
                            if(Integer.parseInt(this.filter) != entry.getKey()){
                                return false;
                            }
                        } catch(NumberFormatException ignored){

                        }
                        return true;
                    })
                    .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
                    .sizeRel(1f,0.2f)
                    .expanded()
                    .child(new TextWidget(owner.equals("Public") ? "Public" : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(owner)))
                        .widthRel(0.2f)
                        .alignment(Alignment.Center))
                    .child(new TextWidget(String.valueOf(freq)).widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget(cover.getInfo()).widthRel(0.25f).alignment(Alignment.Center))
                    .child(new TextFieldWidget()
                        .sizeRel(0.2f, 0.5f)
                        .value(SyncHandlers.string(() -> {
                            Map<CoverData, String> coversOnFrequency = getCoversOnFrequency(owner, freq);
                            return coversOnFrequency.getOrDefault(cover, "Description");
                        }, label -> {
                            if(!guiSyncManager.isClient()){
                                Map<Integer, Map<CoverData,String>> frequencies = advWirelessLabels.computeIfAbsent(owner,k -> new HashMap<>());
                                Map<CoverData, String> covers = frequencies.computeIfAbsent(freq, k -> new HashMap<>());
                                covers.put(cover, label);
                                NBTTagCompound tag = serializeAdvancedToNBT(new NBTTagCompound(), advWirelessLabels);
                                guiSyncManager.getPlayer().getHeldItem().setTagCompound(tag);
                            }

                        }))
                    )
                    .child(new ButtonWidget<>()
                        .widthRel(0.12f)
                        .marginLeft(5)
                        .overlay(IKey.str("Debug"))
                        .onMousePressed(mouseButton -> {
                            LOGGER.debug("Debugging on cover");
                            GTValues.NW.sendToServer(new PacketTeleportPlayer(cover.dim, cover.x, cover.y, cover.z));
                            grid.getPanel().closeIfOpen(false);
                            return true;
                        })
                    ));
                result.add(row);
            });

        });
        return result;
    }

//    public void processAdvancedFrequencies(Map<Integer, Map<CoverData, Byte>> frequencyMap, String owner, ScrollWidget widget, AtomicInteger i, PanelSyncManager guiSyncManager){
//
//        frequencyMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(frequencyEntry ->{
//            frequencyEntry.getValue().forEach((cover,useless) ->{
//                int freq = frequencyEntry.getKey();
//                widget.child(new Row()
//                        .setEnabledIf(w -> {
//                            try{
//                                if(Integer.parseInt(this.filter) != freq){
//                                    return false;
//                                }
//                            } catch(NumberFormatException ignored){
//
//                            }
//                            return true;
//                        })
//                    .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
//                    .sizeRel(1f,0.2f)
//                    .expanded()
//                    .child(new TextWidget(owner.equals("Public") ? "Public" : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(owner)))
//                        .widthRel(0.2f)
//                        .alignment(Alignment.Center))
//                    .child(new TextWidget(String.valueOf(freq)).widthRel(0.2f).alignment(Alignment.Center))
//                    .child(new TextWidget(cover.getInfo()).widthRel(0.25f).alignment(Alignment.Center))
//                    .child(new TextFieldWidget()
//                        .sizeRel(0.2f, 0.5f)
//                        .value(SyncHandlers.string(() -> {
//                            Map<CoverData, String> coversOnFrequency = getCoversOnFrequency(owner, freq);
//                            return coversOnFrequency.getOrDefault(cover, "Description");
//                        }, label -> {
//                            if(!guiSyncManager.isClient()){
//                                Map<Integer, Map<CoverData,String>> frequencies = advWirelessLabels.computeIfAbsent(owner,k -> new HashMap<>());
//                                Map<CoverData, String> covers = frequencies.computeIfAbsent(freq, k -> new HashMap<>());
//                                covers.put(cover, label);
//                                NBTTagCompound tag = serializeAdvancedToNBT(new NBTTagCompound(), advWirelessLabels);
//                                guiSyncManager.getPlayer().getHeldItem().setTagCompound(tag);
//                            }
//
//                        }))
//                    )
//                    .child(new ButtonWidget<>()
//                        .widthRel(0.12f)
//                        .marginLeft(5)
//                        .overlay(IKey.str("Debug"))
//                        .onMousePressed(mouseButton -> {
//                            LOGGER.debug("Debugging on cover");
//                            GTValues.NW.sendToServer(new PacketTeleportPlayer(cover.dim, cover.x, cover.y, cover.z));
//                            widget.getPanel().closeIfOpen(false);
//                            return true;
//                        })
//                    )
//                );
//            });
//        });
//    }


}
