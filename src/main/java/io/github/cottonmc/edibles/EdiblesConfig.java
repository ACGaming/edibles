package io.github.cottonmc.edibles;

import blue.endless.jankson.Comment;

public class EdiblesConfig {

	@Comment(value="Allow hoppers to automatically harvest crops above them.")
	public boolean hopperHarvest = true;

	@Comment(value = "Make iron and gold nuggets edible.")
	public boolean edibleNuggets = false;

	@Comment(value = "Allow eating of (nearly) any item in the game.\n" +
			"Whether this works on a given item will depend on how it's coded.")
	public boolean omnivoreEnabled = false;

	@Comment(value = "How much hunger an omnivore food item should restore.\n" +
			"Should be an integer value from 0 to 20.")
	public int omnivoreFoodRestore = 2;

	@Comment(value = "How much saturation an omnivore food item should restore.\n" +
			"Should be a percentage formatted as a decimal.\n" +
			"Vanilla will not let you overfill a player's saturation.")
	public float omnivoreSaturationRestore = 0.25f;

	@Comment(value = "How much damage an item with durability should take when you eat it.\n" +
			"Should be an integer value above 0, or a decimal value from 0 to 1.\n" +
			"A decimal value will be used to calculate what percentage of an item's damage to consume.\n" +
			"Set to 0 to disable.")
	public double omnivoreItemDamage = 30;

}
