 package xyz.iwolfking.rechiseledchipped.mixin;
 
 import earth.terrarium.chipped.Chipped;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import xyz.iwolfking.rechiseledchipped.RechiseledChipped;
 import xyz.iwolfking.rechiseledchipped.data.Recipes;
 
 @Mixin(value = {Chipped.class}, remap = false)
 public class MixinChipped {
   @Inject(method = {"init"}, at = {@At("TAIL")})
   private static void init(CallbackInfo ci) {
     Recipes.init();
     RechiseledChipped.REGISTRATION.registerDataProviders();
   }
 }


