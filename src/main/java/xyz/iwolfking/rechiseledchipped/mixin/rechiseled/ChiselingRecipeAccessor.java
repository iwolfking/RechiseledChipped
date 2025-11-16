package xyz.iwolfking.rechiseledchipped.mixin.rechiseled;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ChiselingRecipe.class, remap = false)
public interface ChiselingRecipeAccessor {
    @Accessor
    ResourceLocation getParentRecipeId();
}
