package com.kaykype.nrpgzones.zones.zonesProperties;

import com.kaykype.nrpgzones.zones.zonesProperties.*;

public class metods {
    public static zpHandler getProp(int danger) {
        switch (danger) {
            case 0:
                return new greenZone();
            case 1:
                return new yellowZone();
            case 2:
                return new redZone();
            case 3:
                return new blackZone();
        }

        return null;
    }
}
