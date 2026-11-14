package com.example.mathsolver.mixin;

import com.example.mathsolver.MathSolver;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "setTitle", at = @At("HEAD"), require = 0)
    private void mathsolver$title(Text title, CallbackInfo ci) {
        if (title != null) MathSolver.handle(title.getString(), false);
    }

    @Inject(method = "setSubtitle", at = @At("HEAD"), require = 0)
    private void mathsolver$subtitle(Text subtitle, CallbackInfo ci) {
        if (subtitle != null) MathSolver.handle(subtitle.getString(), false);
    }

    @Inject(method = "setOverlayMessage", at = @At("HEAD"), require = 0)
    private void mathsolver$overlay(Text message, boolean tinted, CallbackInfo ci) {
        if (message != null) MathSolver.handle(message.getString(), false);
    }
}
