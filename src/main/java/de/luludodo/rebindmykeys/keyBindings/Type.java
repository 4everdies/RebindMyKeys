package de.luludodo.rebindmykeys.keyBindings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.entity.EntityPlayerSP;

public enum Type {
    EVERYWHERE(),
    GAME(EVERYWHERE),
    MOUNTED(GAME),
    UNMOUNTED(GAME),
    MENU(EVERYWHERE),
    MULTIPLAYER_MENU(MENU),
    DEBUG_COMBO(GAME);

    public boolean currentlyActive() {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen screen = mc.currentScreen;
        EntityPlayerSP player = mc.thePlayer;
        switch (this) {
            case EVERYWHERE:
                return true;
            case GAME:
                return screen == null;
            case MOUNTED:
                return screen == null && player != null && player.ridingEntity != null;
            case UNMOUNTED:
                return screen == null && player != null && player.ridingEntity == null;
            case MENU:
                return screen != null;
            case MULTIPLAYER_MENU:
                return screen instanceof GuiMultiplayer;
            case DEBUG_COMBO:
                return screen == null;
            default:
                return true;
        }
    }

    private final Type parent;

    Type() {
        this.parent = null;
    }

    Type(Type parent) {
        this.parent = parent;
    }

    private boolean isParentOf(Type type) {
        while (type != null) {
            if (type == this) {
                return true;
            }
            type = type.parent;
        }
        return false;
    }

    public boolean conflictsWith(Type other) {
        return isParentOf(other) || other.isParentOf(this);
    }
}
