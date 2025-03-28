 package xyz.iwolfking.rechiseledchipped.mixin;
 
 import com.mojang.datafixers.util.Pair;
 import earth.terrarium.chipped.common.palette.Palette;
 import earth.terrarium.chipped.common.registry.ModBlocks;
 import earth.terrarium.chipped.common.registry.base.ChippedPaletteRegistry;
 import java.util.function.BiFunction;
 import java.util.function.Function;
 import net.minecraft.core.particles.ParticleOptions;
 import net.minecraft.world.level.block.Block;
 import net.minecraft.world.level.block.state.BlockBehaviour;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
 import xyz.iwolfking.rechiseledchipped.data.ChippedData;
 
 
 
 
 @Mixin(value = {ModBlocks.class}, remap = false)
 public class MixinChippedModBlocks
 {
   @Inject(method = {"createRegistry(Lnet/minecraft/world/level/block/Block;Learth/terrarium/chipped/common/palette/Palette;Ljava/util/function/Function;)Learth/terrarium/chipped/common/registry/base/ChippedPaletteRegistry;"}, at = {@At("TAIL")})
   private static void catchChippedRegistry(Block ref, Palette palette, Function<BlockBehaviour.Properties, Block> blockType, CallbackInfoReturnable<ChippedPaletteRegistry<Block>> cir) {
     ChippedData.addEntry((Block)((ChippedPaletteRegistry)cir.getReturnValue()).getBase());
     ChippedData.addRegistry(cir.getReturnValue());
   }
   
   @Inject(method = {"createTorchRegistry"}, at = {@At("TAIL")})
   private static void catchChippedTorchRegistry(Block ref1, Block ref2, Palette palette, BiFunction<BlockBehaviour.Properties, ParticleOptions, Block> blockType1, BiFunction<BlockBehaviour.Properties, ParticleOptions, Block> blockType2, CallbackInfoReturnable<Pair<ChippedPaletteRegistry<Block>, ChippedPaletteRegistry<Block>>> cir) {
     ChippedData.addEntry((Block)((ChippedPaletteRegistry)((Pair)cir.getReturnValue()).getFirst()).getBase());
     ChippedData.addRegistry((ChippedPaletteRegistry)((Pair)cir.getReturnValue()).getFirst());
     ChippedData.addEntry((Block)((ChippedPaletteRegistry)((Pair)cir.getReturnValue()).getSecond()).getBase());
     ChippedData.addRegistry((ChippedPaletteRegistry)((Pair)cir.getReturnValue()).getSecond());
   }
 }


