package io.github.cottonmc.edibles;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Edibles implements ModInitializer {
    public static ModLogger logger = new ModLogger("Edibles", "EDIBLES");
    public static EdiblesConfig config;

    public static final Item JELLY = register("jelly", new JellyItem());

    public static RecipeType<AddJellyRecipe> ADD_JELLY = register("add_jelly");
    public static RecipeSerializer<AddJellyRecipe> ADD_JELLY_SERIALIZER = register("add_jelly", new SpecialRecipeSerializer<>(AddJellyRecipe::new));

    public static Item register(String name, Item item) {
        Registry.register(Registry.ITEM, "edibles:" + name, item);
        return item;
    }

    public static Item mixRegister(String name, Item item) {
        Registry.register(Registry.ITEM, "minecraft:" + name, item);
        return item;
    }

    public static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, "edibles:"+name, serializer);
    }

    @Override
    public void onInitialize() {
        logger.setPrefixFormat(Ansi.Blue);
        config = ConfigManager.loadConfig(EdiblesConfig.class);
        if (config.omnivoreEnabled) {
            logger.info("You're feeling hungry...");
        }
    }
}
