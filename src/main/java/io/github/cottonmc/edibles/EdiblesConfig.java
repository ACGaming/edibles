package io.github.cottonmc.edibles;

import io.github.cottonmc.repackage.blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = "Edibles")
public class EdiblesConfig {
	
	@Comment(value="Allow eating of (nearly) any item in the game.\nWhether this works on a given item will depend on how it's coded.")
	public boolean omnivoreEnabled = false;

	@Comment(value="How much hunger an omnivore food item should restore.\nShould be an integer value from 0 to 20.")
	public int omnivoreFoodRestore = 2;

	@Comment(value="How much saturation an omnivore food item should restore.\nShould be a decimal value from 0 to 20.\nEach 1.5 saturation restores 0.5 hearts.")
	public float omnivoreSaturationRestore = 0.5f;

	@Comment(value="How much damage an item with durability should take when you eat it.\nShould be an integer value above 0.\n Set to 0 to disable.")
	public int omnivoreItemDamage = 30;

}
