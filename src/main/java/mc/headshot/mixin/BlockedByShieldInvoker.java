package mc.headshot.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface BlockedByShieldInvoker {
    @Invoker
    boolean invokeBlockedByShield(DamageSource source);
}
