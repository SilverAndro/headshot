package mc.headshot;

import mc.microconfig.Comment;
import mc.microconfig.ConfigData;

public class HeadshotConfig implements ConfigData {
    @Comment("Damage multiplier done on headshots")
    public double damageMultiplier = 0.05;
    
    @Comment("Whether to apply blindness when headshot or not")
    public boolean doBlind = false;
    
    @Comment("How long to blind when headshot in ticks (if enabled)")
    public int blindTicks = 35;
    
    @Comment("Whether to apply nausea when headshot or not")
    public boolean doNausea = false;
    
    @Comment("How long to nausea when headshot in ticks (if enabled)")
    public int nauseaTicks = 35;
}
