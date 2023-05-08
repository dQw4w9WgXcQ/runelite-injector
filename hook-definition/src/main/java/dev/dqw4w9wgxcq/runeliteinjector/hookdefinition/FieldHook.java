package dev.dqw4w9wgxcq.runeliteinjector.hookdefinition;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class FieldHook implements Hook {
    private final String definition;
    private final String name;
    private final String owner;
    private final String type;
    private final @Nullable Long multiplier;
}
