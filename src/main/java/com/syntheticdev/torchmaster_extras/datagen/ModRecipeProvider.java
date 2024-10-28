package com.syntheticdev.torchmaster_extras.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import com.syntheticdev.torchmaster_extras.common.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        Item megatorch = net.xalcon.torchmaster.common.ModBlocks.itemMegaTorch.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COBBLEMON_TORCH.get())
                .pattern(" T ")
                .pattern("RCR")
                .define('T', megatorch)
                .define('R', CobblemonItems.RELIC_COIN)
                .define('C', Items.COBBLESTONE)
                .unlockedBy(getHasName(megatorch), has(megatorch))
                .save(consumer);
    }
}
