package miscutil.gregtech.api.enums;

/* Electric Components.
*
* usual Materials for this are:
* Primitive (Tier 1)
* Basic (Tier 2) as used by UE as well : IC2 Circuit and RE-Battery
* Good (Tier 3)
* Advanced (Tier 4) as used by UE as well : Advanced Circuit, Advanced Battery and Lithium Battery
* Data (Tier 5) : Data Storage Circuit
* Elite (Tier 6) as used by UE as well : Energy Crystal and Data Control Circuit
* Master (Tier 7) : Energy Flow Circuit and Lapotron Crystal
* Ultimate (Tier 8) : Data Orb and Lapotronic Energy Orb
* Infinite (Cheaty)
* 
Circuits
	Circuit_Primitive, Circuit_Basic, Circuit_Good, Circuit_Advanced,	
	Circuit_Data, Circuit_Elite, Circuit_Master, Tool_DataOrb, Circuit_Ultimate, Tool_DataStick, 
	Circuit_IV, Circuit_LuV, Circuit_ZPM,
Circuit Parts
	Circuit_Board_IV, Circuit_Board_LuV, Circuit_Board_ZPM, 
	Circuit_Parts_Crystal_Chip_IV, Circuit_Parts_Crystal_Chip_LuV, Circuit_Parts_Crystal_Chip_ZPM,
	Circuit_Parts_IV, Circuit_Parts_LuV, Circuit_Parts_ZPM,
	Circuit_Parts_Wiring_IV, Circuit_Parts_Wiring_LuV, Circuit_Parts_Wiring_ZPM;
*/
public enum GregtechOreDictNames {
	buffer_core, itemGregConduit, Circuit_IV, Circuit_LuV, Circuit_ZPM,
	Circuit_Board_IV, Circuit_Board_LuV, Circuit_Board_ZPM, 
	Circuit_Parts_Crystal_Chip_IV, Circuit_Parts_Crystal_Chip_LuV, Circuit_Parts_Crystal_Chip_ZPM,
	Circuit_Parts_IV, Circuit_Parts_LuV, Circuit_Parts_ZPM,
	Circuit_Parts_Wiring_IV, Circuit_Parts_Wiring_LuV, Circuit_Parts_Wiring_ZPM;

	public String unlocalisedName;

	private void ModObject() {
		unlocalisedName = name();
	}

}