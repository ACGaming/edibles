package io.github.cottonmc.edibles;

import io.github.cottonmc.repackage.blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = "Edibles")
public class EdiblesConfig {
	
	@Comment(value="Allow eating of (nearly) any item in the game.\n" +
			"Whether this works on a given item will depend on how it's coded.")
	public boolean omnivoreEnabled = false;

	@Comment(value="How much hunger an omnivore food item should restore.\n" +
			"Should be an integer value from 0 to 20.")
	public int omnivoreFoodRestore = 2;

	@Comment(value="How much saturation an omnivore food item should restore.\n" +
			"Should be a percentage formatted as a decimal.\n" +
			"Vanilla will not let you overfill a player's saturation.")
	public float omnivoreSaturationRestore = 0.25f;

	@Comment(value="How much damage an item with durability should take when you eat it.\n" +
			"Should be an integer value above 0.\n" +
			"Set to 0 to disable.")
	public int omnivoreItemDamage = 30;

}
