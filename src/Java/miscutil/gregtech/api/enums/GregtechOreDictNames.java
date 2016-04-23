package miscutil.gregtech.api.enums;


public enum GregtechOreDictNames {
	buffer_core, itemGregConduit;

	public String unlocalisedName;

	private void ModObject() {
		unlocalisedName = name();
	}

}