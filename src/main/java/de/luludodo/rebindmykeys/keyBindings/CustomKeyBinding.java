package de.luludodo.rebindmykeys.keyBindings;

import net.minecraft.client.settings.KeyBinding;

public class CustomKeyBinding extends KeyBinding {
    private final Type type;

    public CustomKeyBinding(String description, int keyCode, Type type) {
        super(description, keyCode, "key.categories.misc");
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean matchesKey(int keyCode) {
        return keyCode == getKeyCode();
    }

    public boolean isHeld() {
        return org.lwjgl.input.Keyboard.isKeyDown(getKeyCode());
    }

    public static Type getType(KeyBinding keyBinding) {
        if (keyBinding instanceof CustomKeyBinding) {
            return ((CustomKeyBinding) keyBinding).getType();
        }
        String desc = keyBinding.getKeyDescription();
        if ("key.fullscreen".equals(desc) || "key.screenshot".equals(desc)) {
            return Type.EVERYWHERE;
        }
        return Type.GAME;
    }

    public static boolean conflicts(KeyBinding keyBinding1, KeyBinding keyBinding2) {
        return keyBinding1.getKeyCode() == keyBinding2.getKeyCode()
                && getType(keyBinding1).conflictsWith(getType(keyBinding2));
    }
}
