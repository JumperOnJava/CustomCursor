package io.github.jumperonjava.customcursor.mixin;

import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.DeltaTracker;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class CobblemonFix {
    @Shadow
    @Final
    private Minecraft minecraft;
    boolean locked = false;

    //? if >= 26.1 {
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    //? } else {
    /*@Inject(method = "render", at = @At("HEAD"))
    *///? }

    //? if >= 26.2 {
    /*void lock(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci)
    *///?} else if >= 26.1 {
    void lock(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci)
    //?} else if >= 1.21.10 {
    /*void lock(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci)
    *///?} else if >= 1.21 {
    /*void lock(GuiGraphicsExtractor context, DeltaTracker tickCounter, CallbackInfo ci)
    *///?} else {
    /*void lock(GuiGraphicsExtractor context, float tickDelta, CallbackInfo ci)
    *///?}
    {
        if (VF.getScreen() == null) {
            if (locked)
                return;
            minecraft.mouseHandler.grabMouse();
            locked = true;
            var x = (double) (this.minecraft.getWindow().getWidth() / 2);
            var y = (double) (this.minecraft.getWindow().getHeight() / 2);
            //? if <= 1.21.8 {
            /*
            InputConstants.grabOrReleaseMouse(this.minecraft.getWindow(), 212995, x, y);
            *///?} else {
            //InputConstants.releaseMouse(this.minecraft.getWindow(), x, y);
            //?}
        } else {
            locked = false;
        }
    }
}
