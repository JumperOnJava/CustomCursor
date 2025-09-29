package io.github.jumperonjava.customcursor.mixin;

//? if !forge
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jumperonjava.customcursor.CursorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Nullable protected MinecraftClient client;


    //? if = 1.20.1 {
    /*@Inject(method = "render",at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",ordinal = 1),locals = LocalCapture.CAPTURE_FAILHARD)
    void renderCursor(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack, DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,tickDelta);
    }
    *///?} elif = 1.20.4 {
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", shift = At.Shift.BEFORE))
    void renderCursor(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,tickDelta);
    }
     *///?} elif = 1.21.1 {
    
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", shift = At.Shift.BEFORE))
    void renderCursor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,tickCounter.getLastDuration());
    }
    *///?} elif < 1.21.6 {
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", shift = At.Shift.BEFORE))
    void renderCursor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,0);
    }
    *///?} else {

    //? if fabric {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    void renderCursor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,0);
    }
    //?} else if neoforge {
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;drawScreen(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    void renderCursor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,0);
    }
    *///?}

    //?}
}
