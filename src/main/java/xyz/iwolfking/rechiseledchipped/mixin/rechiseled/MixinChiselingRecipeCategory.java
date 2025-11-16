 package xyz.iwolfking.rechiseledchipped.mixin.rechiseled;
 
 import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
 import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
 import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
 import java.util.ArrayList;
 import java.util.List;
 import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
 import mezz.jei.api.recipe.IFocusGroup;
 import mezz.jei.api.recipe.RecipeIngredientRole;
 import mezz.jei.api.recipe.category.IRecipeCategory;
 import net.minecraft.world.item.ItemStack;
 import net.minecraft.world.level.ItemLike;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Overwrite;
 
 
 
 
 @Mixin(value = {ChiselingRecipeCategory.class}, remap = false)
 public abstract class MixinChiselingRecipeCategory
   implements IRecipeCategory<ChiselingRecipe>
 {
   /**
    * @author iwolfking
    * @reason Replace Rechiseled Recipe category to allow displaying more options.
    */
   @Overwrite
   public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ChiselingRecipe recipe, IFocusGroup focusGroup) {
     List<ItemStack> inputs = new ArrayList<>();
     List<List<ItemStack>> outputs = new ArrayList<>();
     
     int jei_index = 0;
     int jei_loop_amount = 0;
     
     for (ChiselingEntry entry : recipe.getEntries()) {
       List<ItemStack> output = new ArrayList<>();
       
       if (entry.hasRegularItem()) {
         inputs.add(new ItemStack((ItemLike)entry.getRegularItem()));
       }
       if (entry.hasConnectingItem()) {
         inputs.add(new ItemStack((ItemLike)entry.getConnectingItem()));
       }
       
       if (entry.hasRegularItem())
       { output.add(new ItemStack((ItemLike)entry.getRegularItem())); }
       else { output.add(new ItemStack((ItemLike)entry.getConnectingItem())); }
       
       if (entry.hasConnectingItem())
       { output.add(new ItemStack((ItemLike)entry.getConnectingItem())); }
       else { output.add(new ItemStack((ItemLike)entry.getRegularItem())); }
       
       if (jei_loop_amount >= 1) {
         List<ItemStack> source = outputs.get(jei_index);
         for (ItemStack stack : output) {
           source.add(stack);
         }
       } else {
         
         outputs.add(output);
       } 
       
       if (jei_index == 27) {
         jei_index = 0;
         jei_loop_amount++;
         continue;
       } 
       jei_index++;
     } 
 
 
     
     if (jei_loop_amount != 0) {
       while (jei_index != 28) {
         List<ItemStack> source = outputs.get(jei_index);
         source.add(ItemStack.EMPTY);
         source.add(ItemStack.EMPTY);
         jei_index++;
       } 
     }
 
 
 
     
     recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 1, 28).addItemStacks(inputs);



       for (int i = 0; i < outputs.size(); i++) {
           int x = 49 + 18 * (i % 7);
           int y = 1 + 18 * (i / 7);
           recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                   .addItemStacks(outputs.get(i));
       }

   }
 }


