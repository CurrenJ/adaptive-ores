package grill24.adaptiveores.neoforge;

import grill24.adaptiveores.neoforge.platform.NeoForgeDataGeneratorHelper;
import grill24.adaptiveores.platform.IDataGeneratorHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class SidedDataGeneratorImpl {
    public static IDataGeneratorHelper create(Object object) {
        if (!(object instanceof GatherDataEvent gatherDataEvent)) {
            throw new IllegalArgumentException("Expected GatherDataEvent but got " + object);
        }

        return new NeoForgeDataGeneratorHelper(gatherDataEvent);
    }
}
