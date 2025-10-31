package gregtech.common.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.GTGenericItem;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;
import gregtech.client.hud.elements.ButtonElement;
import gregtech.client.hud.elements.ChamferedRectElement;
import gregtech.client.hud.elements.CheckboxElement;
import gregtech.client.hud.elements.ColumnElement;
import gregtech.client.hud.elements.DroplistElement;
import gregtech.client.hud.elements.DynamicItemElement;
import gregtech.client.hud.elements.GraphElement;
import gregtech.client.hud.elements.RectElement;
import gregtech.client.hud.elements.SliderElement;
import gregtech.client.hud.elements.TextElement;
import gregtech.client.hud.elements.TextInputElement;

public class ItemDashboardTool extends GTGenericItem {

    public static boolean dashboardEnabled = false;

    public ItemDashboardTool(String unlocalized, String english, String tooltip) {
        super(unlocalized, english, tooltip);
        setMaxStackSize(1);

        HUDManager hud = HUDManager.getInstance();
        hud.register();
        hud.setEditGuiSupplier(() -> new HUDGui.GuiHUDEdit(hud));
    }

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        list.add("Author: §9Vortex");
        list.add("Right-click to toggle dashboard HUD");
        list.add("Press RSHIFT to toggle edit mode");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            dashboardEnabled = !dashboardEnabled;
            HUDManager hud = HUDManager.getInstance();
            hud.setEnabled(dashboardEnabled);

            if (dashboardEnabled && hud.getWidgets()
                .isEmpty()) {
                createWidgets();
            }
        }

        return stack;
    }

    private double lastPlayerY = 0.0;
    private boolean testFeatureEnabled = false;
    private double sliderValue = 50.0;
    private String inputText = "Hello World";
    private List<Double> columnData = Arrays.asList(25.0, 45.0, 60.0, 30.0, 75.0, 90.0, 15.0);

    @SideOnly(Side.CLIENT)
    private void createWidgets() {
        HUDManager hud = HUDManager.getInstance();
        hud.clearWidgets();

        hud.addWidget(
            new CompositeWidget(10, 10).isConfigurable()
                .addElement(
                    new RectElement().size(300, 200)
                        .offset(-10, -10)
                        .colorRGBA(0.1f, 0.1f, 0.2f, 0.8f))
                .addElement(
                    new TextElement("Player Statistics").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(new GraphElement(() -> {
                    if (Minecraft.getMinecraft().thePlayer != null) {
                        lastPlayerY = Minecraft.getMinecraft().thePlayer.posY;
                    }
                    return lastPlayerY;
                }).size(280, 80)
                    .offsetVertical(20)
                    .setDelay(5)
                    .setDataSize(50)
                    .setDynamicColor(true)
                    .setRenderLines(true)
                    .setRenderDots(false))
                .addElement(
                    new GraphElement(
                        () -> (double) (Runtime.getRuntime()
                            .totalMemory()
                            - Runtime.getRuntime()
                                .freeMemory())).size(280, 80)
                                    .offsetVertical(110)
                                    .setDelay(2)
                                    .setDataSize(100)
                                    .setDynamicColor(false)
                                    .colorRGBA(0.0f, 1.0f, 0.5f, 0.8f)));

        hud.addWidget(
            new CompositeWidget(320, 10).isConfigurable()
                .addElement(
                    new RectElement().size(200, 120)
                        .offset(-10, -10)
                        .colorRGBA(0.15f, 0.15f, 0.25f, 0.8f))
                .addElement(
                    new TextElement("Text & Input Panel").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(
                    new TextElement(() -> "Player Y: " + String.format("%.3f", lastPlayerY)).offsetVertical(20)
                        .colorSupplier(() -> lastPlayerY > 70 ? 0xFFFF00 : 0xFFFFFF))
                .addElement(
                    new TextElement(
                        () -> "Time: " + (Minecraft.getMinecraft().theWorld != null
                            ? Minecraft.getMinecraft().theWorld.getWorldTime() % 24000
                            : 0)).offsetVertical(35))
                .addElement(new TextInputElement(text -> {
                    inputText = text;
                    System.out.println("Text changed to: " + text);
                }).size(180, 18)
                    .offsetVertical(55)
                    .colorRGBA(0.2f, 0.2f, 0.3f, 1.0f))
                .addElement(new TextElement(() -> "Input: " + inputText).offsetVertical(80)));

        hud.addWidget(
            new CompositeWidget(320, 150).isConfigurable()
                .addElement(
                    new RectElement().size(200, 100)
                        .offset(-10, -10)
                        .colorRGBA(0.15f, 0.2f, 0.15f, 0.8f))
                .addElement(
                    new TextElement("Controls").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(new ButtonElement("Toggle Feature", () -> {
                    testFeatureEnabled = !testFeatureEnabled;
                    System.out.println("Feature enabled: " + testFeatureEnabled);
                }).size(120, 20)
                    .offsetVertical(20))
                .addElement(new CheckboxElement("Advanced Mode", testFeatureEnabled, checked -> {
                    testFeatureEnabled = checked;
                    System.out.println("Advanced mode: " + checked);
                }).size(150, 16)
                    .offsetVertical(50))
                .addElement(new ButtonElement("Reset Data", () -> {
                    columnData = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
                    System.out.println("Data reset");
                }).size(100, 18)
                    .offsetVertical(75)));

        hud.addWidget(
            new CompositeWidget(320, 270).isConfigurable()
                .addElement(
                    new RectElement().size(200, 120)
                        .offset(-10, -10)
                        .colorRGBA(0.2f, 0.15f, 0.15f, 0.8f))
                .addElement(
                    new TextElement("Selection & Sliders").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(
                    new DroplistElement(
                        () -> Arrays.asList("Easy", "Normal", "Hard", "Expert"),
                        1,
                        selected -> System.out.println("Difficulty: " + selected)).size(120, 18)
                            .offsetVertical(20))
                .addElement(new SliderElement(() -> sliderValue, value -> {
                    sliderValue = value;
                    System.out.println("Slider value: " + value);
                }, 0.0, 100.0).size(150, 16)
                    .offsetVertical(50))
                .addElement(new TextElement(() -> "Value: " + String.format("%.1f", sliderValue)).offsetVertical(70)));

        hud.addWidget(
            new CompositeWidget(530, 10).isConfigurable()
                .addElement(
                    new RectElement().size(180, 200)
                        .offset(-10, -10)
                        .colorRGBA(0.15f, 0.15f, 0.2f, 0.8f))
                .addElement(
                    new TextElement("Data Visualization").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(
                    new ColumnElement(() -> columnData, 0.0, 100.0).size(160, 150)
                        .offsetVertical(30)
                        .setCellSize(20, 12)
                        .setSpacing(2))
                .addElement(new ButtonElement("Add Data", () -> {
                    List<Double> newData = new ArrayList<>(columnData);
                    newData.add(Math.random() * 100);
                    if (newData.size() > 10) newData.remove(0);
                    columnData = newData;
                }).size(80, 16)
                    .offsetVertical(185)));

        hud.addWidget(
            new CompositeWidget(530, 230).isConfigurable()
                .addElement(
                    new RectElement().size(180, 100)
                        .offset(-10, -10)
                        .colorRGBA(0.2f, 0.2f, 0.1f, 0.8f))
                .addElement(
                    new TextElement("Status Monitor").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(
                    new TextElement(() -> "Height: " + Minecraft.getMinecraft().thePlayer.height).offsetVertical(20))
                .addElement(
                    new TextElement(
                        () -> "Memory: " + (Runtime.getRuntime()
                            .totalMemory()
                            - Runtime.getRuntime()
                                .freeMemory())
                            / 1024
                            / 1024 + "MB").offsetVertical(35))
                .addElement(new TextElement(() -> testFeatureEnabled ? "§aACTIVE" : "§cINACTIVE").offsetVertical(55)));

        hud.addWidget(
            new CompositeWidget(10, 230).isConfigurable()
                .addElement(
                    new ChamferedRectElement().size(150, 100)
                        .setCornerCut(15)
                        .setOutlineThickness(2.0f)
                        .colorRGBA(0.3f, 0.2f, 0.4f, 0.6f))
                .addElement(
                    new TextElement("Styled Panel").offsetVertical(10)
                        .offsetHorizontal(10))
                .addElement(
                    new ChamferedRectElement().size(130, 40)
                        .offsetVertical(40)
                        .offsetHorizontal(10)
                        .setCornerCut(8)
                        .colorRGBA(0.4f, 0.3f, 0.5f, 0.8f))
                .addElement(
                    new TextElement("Content Area").offsetVertical(50)
                        .offsetHorizontal(20)));

        hud.addWidget(
            new CompositeWidget(180, 230).isConfigurable()
                .addElement(
                    new RectElement().size(120, 100)
                        .offset(-10, -10)
                        .colorRGBA(0.25f, 0.25f, 0.3f, 0.8f))
                .addElement(
                    new TextElement("Dynamic Content").offsetVertical(-5)
                        .offsetHorizontal(10))
                .addElement(
                    new DynamicItemElement(
                        () -> Minecraft.getMinecraft().thePlayer != null
                            ? Minecraft.getMinecraft().thePlayer.getHeldItem()
                            : null).offsetVertical(20)
                                .offsetHorizontal(40)
                                .colorRGBA(1.0f, 1.0f, 1.0f, 1.0f))
                .addElement(new TextElement(() -> {
                    if (Minecraft.getMinecraft().thePlayer == null) return "No item";
                    ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
                    return stack != null ? stack.getDisplayName() : "Empty hand";
                }).offsetVertical(70)
                    .offsetHorizontal(10)));
    }
}
