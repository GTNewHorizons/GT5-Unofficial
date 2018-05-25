package gtPlusPlus.xmod.thaumcraft.objects;

import thaumcraft.common.lib.research.ResearchNoteData;

public class ResearchNoteDataWrapper extends ResearchNoteData {

	public ResearchNoteDataWrapper() {
		super();
	}
	
	public ResearchNoteDataWrapper(ResearchNoteData data) {
		key = data.key;
		color = data.color;
		hexEntries = data.hexEntries;
		hexes = data.hexes;
		complete = data.complete;
		copies = data.copies;
	}
	
	public void completeResearch() {
		this.complete = true;
	}

	
}
