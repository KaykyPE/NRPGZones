package com.kaykype.nrpgzones.utils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class zoneManager {
    public static Map<UUID, Integer> pendingDeletions = new HashMap<>();
    public static int pedingConsoleDeletion = 0;

    public static String zoneDangerString(int danger_level) {
        String dangerString = "";

        switch (danger_level) {
            case 0:
                dangerString = "Zona Verde";
                break;
            case 1:
                dangerString = "Zona Amarela";
                break;
            case 2:
                dangerString = "Zona Vermelha";
                break;
            case 3:
                dangerString = "Zona Preta";
        }

        return dangerString;
    }
}
