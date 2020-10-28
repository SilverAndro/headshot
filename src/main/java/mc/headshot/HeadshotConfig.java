package mc.headshot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "headshot")
public class HeadshotConfig implements ConfigData {
    @Comment("Damage multiplier done on headshots")
    public double damageMultiplier = 0.05;
    
    @Comment("Whether to apply blindness when headshot or not")
    public boolean doBlind = false;
    
    @Comment("How long to blind when headshot in ticks (if enabled)")
    public int blindTicks = 35;
}
