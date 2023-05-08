package dev.dqw4w9wgxcq.runeliteinjector.hookdefinition;

import lombok.Data;

import java.util.Map;

@Data
public class Hooks {
    private final int rev;
    private final Map<String, Hook> hooks;
}
