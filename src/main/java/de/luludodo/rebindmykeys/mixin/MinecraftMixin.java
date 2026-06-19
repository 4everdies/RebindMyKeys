package de.luludodo.rebindmykeys.mixin;

import de.luludodo.rebindmykeys.RebindMyKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import org.lwjgl.input.Keyboard;

import java.io.File;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.lib.Opcodes;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public GameSettings gameSettings;
    @Shadow private File mcDataDir;
    @Shadow private int displayWidth;
    @Shadow private int displayHeight;

    @Unique
    private boolean processedByMod;

    @Inject(method = "displayInGameMenu", at = @At("HEAD"), cancellable = true)
    private void onDisplayInGameMenu(CallbackInfo ci) {
        if (RebindMyKeys.escapeKey.getKeyCode() != 1) {
            ci.cancel();
        }
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void onRunTickEnd(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.currentScreen == null && RebindMyKeys.escapeKey.isPressed()) {
            mc.displayGuiScreen(new GuiIngameMenu());
        }
    }

    @Overwrite
    public void dispatchKeypresses() {
        processedByMod = false;
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
        boolean flag = Keyboard.getEventKeyState();

        if (flag) {
            Minecraft mc = (Minecraft) (Object) this;

            if (RebindMyKeys.toggleHudKey.isPressed()) {
                this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                processedByMod = true;
            }

            if (this.gameSettings.keyBindScreenshot.isPressed()) {
                Minecraft mcScreenshot = (Minecraft) (Object) this;
                ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, mcScreenshot.getFramebuffer());
                processedByMod = true;
            }

            if (RebindMyKeys.debugKey.isPressed()) {
                this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                processedByMod = true;
            }

            if (RebindMyKeys.reloadChunksKey.isPressed()) {
                mc.renderGlobal.loadRenderers();
                processedByMod = true;
            }

            if (RebindMyKeys.showHitboxKey.isPressed()) {
                boolean box = !mc.getRenderManager().isDebugBoundingBox();
                mc.getRenderManager().setDebugBoundingBox(box);
                processedByMod = true;
            }

            if (RebindMyKeys.clearChatKey.isPressed()) {
                mc.ingameGUI.getChatGUI().clearChatMessages();
                processedByMod = true;
            }

            if (RebindMyKeys.toggleChunkBordersKey.isPressed()) {
                toggleChunkBorders(mc.renderGlobal);
                processedByMod = true;
            }

            if (RebindMyKeys.toggleAdvancedTooltipsKey.isPressed()) {
                this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                this.gameSettings.saveOptions();
                processedByMod = true;
            }
        }
    }

    private static boolean toggleChunkBorders(RenderGlobal renderGlobal) {
        String[] fieldNames = {"debugFixTerrainFrustum", "field_175002_T"};
        for (String name : fieldNames) {
            try {
                java.lang.reflect.Field field = RenderGlobal.class.getDeclaredField(name);
                field.setAccessible(true);
                boolean current = field.getBoolean(renderGlobal);
                field.setBoolean(renderGlobal, !current);
                return !current;
            } catch (Exception ignored) {}
        }
        return false;
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;hideGUI:Z", opcode = Opcodes.PUTFIELD))
    private void redirectHideGUI(GameSettings instance, boolean newValue) {
        if (!processedByMod && RebindMyKeys.toggleHudKey.getKeyCode() == Keyboard.KEY_F1) {
            instance.hideGUI = newValue;
        }
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;showDebugInfo:Z", opcode = Opcodes.PUTFIELD))
    private void redirectShowDebugInfo(GameSettings instance, boolean newValue) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.showDebugInfo = newValue;
        }
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;showDebugProfilerChart:Z", opcode = Opcodes.PUTFIELD))
    private void redirectShowDebugProfilerChart(GameSettings instance, boolean newValue) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.showDebugProfilerChart = newValue;
        }
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;showLagometer:Z", opcode = Opcodes.PUTFIELD))
    private void redirectShowLagometer(GameSettings instance, boolean newValue) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.showLagometer = newValue;
        }
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;advancedItemTooltips:Z", opcode = Opcodes.PUTFIELD))
    private void redirectAdvancedItemTooltips(GameSettings instance, boolean newValue) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.advancedItemTooltips = newValue;
        }
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;loadRenderers()V"))
    private void redirectLoadRenderers(RenderGlobal instance) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.loadRenderers();
        }
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;setDebugBoundingBox(Z)V"))
    private void redirectSetDebugBoundingBox(RenderManager instance, boolean value) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.setDebugBoundingBox(value);
        }
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;clearChatMessages()V"))
    private void redirectClearChatMessages(GuiNewChat instance) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.clearChatMessages();
        }
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;saveOptions()V"))
    private void redirectSaveOptions(GameSettings instance) {
        if (!processedByMod && RebindMyKeys.debugKey.getKeyCode() == Keyboard.KEY_F3) {
            instance.saveOptions();
        }
    }
}
