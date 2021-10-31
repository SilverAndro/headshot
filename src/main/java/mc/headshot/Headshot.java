package mc.headshot;

import mc.microconfig.MicroConfig;
import net.fabricmc.api.ModInitializer;

public class Headshot implements ModInitializer {
    public static HeadshotConfig config;
    
    @Override
    public void onInitialize() {
        config = MicroConfig.getOrCreate("headshot", new HeadshotConfig());
    }
}
