package mc.headshot;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Headshot implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(HeadshotConfig.class, JanksonConfigSerializer::new);
    }
}
