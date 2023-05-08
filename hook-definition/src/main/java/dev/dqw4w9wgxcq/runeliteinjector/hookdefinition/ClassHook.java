package dev.dqw4w9wgxcq.runeliteinjector.hookdefinition;

import lombok.Data;

@Data
public class ClassHook implements Hook{
    private final String definition;
    private final String name;
}
