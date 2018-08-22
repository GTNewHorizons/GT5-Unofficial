package gregtech.api.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gregtech.api.objects.MaterialStack;

public class MaterialBuilder {
	public static final int DIESEL = 0, GAS = 1, THERMAL = 2, SEMIFLUID = 3, PLASMA = 4, MAGIC = 5;
	
	private int metaItemSubID;
	private TextureSet iconSet;
	private float toolSpeed = 1.0f;
	private int durability = 0;
	private int toolQuality = 0;
	private int types = 0;
	private int r = 255, g = 255, b = 255, a = 0;
	private String name;
	private String defaultLocalName;
	private int fuelType = 0;
	private int fuelPower = 0;
	private int meltingPoint = 0;
	private int blastFurnaceTemp = 0;
	private boolean blastFurnaceRequired = false;
	private boolean transparent = false;
	private int oreValue = 1;
	private int densityMultiplier = 1;
	private int densityDivider = 1;
	private Dyes color = Dyes._NULL;
	private int extraData = 0;
	private List<MaterialStack> materialList = new ArrayList<MaterialStack>();
	private List<TC_Aspects.TC_AspectStack> aspects = new ArrayList<TC_Aspects.TC_AspectStack>();
	private boolean hasCorrespondingFluid = false;
	private boolean hasCorrespondingGas = false;
	private boolean canBeCracked = false;
	private boolean canBeSteamCracked = false;
	private int liquidTemperature = 300;
	private int gasTemperature = 300;

	public MaterialBuilder(int metaItemSubID, TextureSet iconSet, String defaultLocalName) {
		this.metaItemSubID = metaItemSubID;
		this.iconSet = iconSet;
		this.name = defaultLocalName.replace(" ", "").replace("-", "");
		this.defaultLocalName = defaultLocalName;
	}

	public Materials constructMaterial() {
		return new Materials(metaItemSubID, iconSet, toolSpeed, durability, toolQuality, types, r, g, b, a, name, defaultLocalName, fuelType, fuelPower, meltingPoint, blastFurnaceTemp,
				blastFurnaceRequired, transparent, oreValue, densityMultiplier, densityDivider, color, extraData, materialList, aspects)
				.setHasCorrespondingFluid(hasCorrespondingFluid)
				.setHasCorrespondingGas(hasCorrespondingGas)
				.setCanBeCracked(canBeCracked);
	}
	
	public MaterialBuilder setName(String name){
		this.name = name;
		return this;
	}
	
	public MaterialBuilder setTypes(int types){
		this.types = types;
		return this;
	}
	
	public MaterialBuilder addDustItems(){
		types = types | 1;
		return this;
	}
	
	public MaterialBuilder addMetalItems(){
		types = types | 2;
		return this;
	}
	
	public MaterialBuilder addGemItems(){
		types = types | 4;
		return this;
	}
	
	public MaterialBuilder addOreItems(){
		types = types | 8;
		return this;
	}
	
	public MaterialBuilder addCell(){
		types = types | 16;
		return this;
	}
	
	public MaterialBuilder addPlasma(){
		types = types | 32;
		return this;
	}
	
	public MaterialBuilder addToolHeadItems(){
		types = types | 64;
		return this;
	}
	
	public MaterialBuilder addGearItems(){
		types = types | 128;
		return this;
	}
	
	public MaterialBuilder addFluid(){
		this.hasCorrespondingFluid = true;
		return this;
	}
	
	public MaterialBuilder addGas(){
		this.hasCorrespondingGas = true;
		return this;
	}
	
	
	public MaterialBuilder setRGBA(int r, int g, int b, int a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}
	
	public MaterialBuilder setRGB(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
		return this;
	}
	
	public MaterialBuilder setTransparent(boolean transparent){
		this.transparent = transparent;
		return this;
	}
	
	public MaterialBuilder setColor(Dyes color){
		this.color = color;
		return this;
	}


	public MaterialBuilder setToolSpeed(float toolSpeed) {
		this.toolSpeed = toolSpeed;
		return this;
	}

	public MaterialBuilder setDurability(int durability) {
		this.durability = durability;
		return this;
	}

	public MaterialBuilder setToolQuality(int toolQuality) {
		this.toolQuality = toolQuality;
		return this;
	}


	public MaterialBuilder setFuelType(int fuelType) {
		this.fuelType = fuelType;
		return this;
	}

	public MaterialBuilder setFuelPower(int fuelPower) {
		this.fuelPower = fuelPower;
		return this;
	}

	public MaterialBuilder setMeltingPoint(int meltingPoint) {
		this.meltingPoint = meltingPoint;
		return this;
	}

	public MaterialBuilder setBlastFurnaceTemp(int blastFurnaceTemp) {
		this.blastFurnaceTemp = blastFurnaceTemp;
		return this;
	}

	public MaterialBuilder setBlastFurnaceRequired(boolean blastFurnaceRequired) {
		this.blastFurnaceRequired = blastFurnaceRequired;
		return this;
	}

	public MaterialBuilder setOreValue(int oreValue) {
		this.oreValue = oreValue;
		return this;
	}

	public MaterialBuilder setDensityMultiplier(int densityMultiplier) {
		this.densityMultiplier = densityMultiplier;
		return this;
	}

	public MaterialBuilder setDensityDivider(int densityDivider) {
		this.densityDivider = densityDivider;
		return this;
	}

	public MaterialBuilder setExtraData(int extraData) {
		this.extraData = extraData;
		return this;
	}

	public MaterialBuilder addElectrolyzerRecipe(){
		extraData = extraData | 1;
		return this;
	}
	
	public MaterialBuilder addCentrifugeRecipe(){
		extraData = extraData | 2;
		return this;
	}
	
	public MaterialBuilder setMaterialList(List<MaterialStack> materialList) {
		this.materialList = materialList;
		return this;
	}
	
	public MaterialBuilder setMaterialList(MaterialStack ... materials) {
		this.materialList = Arrays.asList(materials);
		return this;
	}

	public MaterialBuilder setAspects(List<TC_Aspects.TC_AspectStack> aspects) {
		this.aspects = aspects;
		return this;
	}

	public int getLiquidTemperature() {
		return liquidTemperature;
	}

	public MaterialBuilder setLiquidTemperature(int liquidTemperature) {
		this.liquidTemperature = liquidTemperature;
		return this;
	}

	public int getGasTemperature() {
		return gasTemperature;
	}

	public MaterialBuilder setGasTemperature(int gasTemperature) {
		this.gasTemperature = gasTemperature;
		return this;
	}

	public boolean canBeCracked() {
		return canBeCracked;
	}

	public MaterialBuilder setCanBeCracked(boolean canBeCracked) {
		this.canBeCracked = canBeCracked;
		return this;
	}

}
