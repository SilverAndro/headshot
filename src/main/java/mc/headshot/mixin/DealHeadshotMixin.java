package mc.headshot.mixin;

import mc.headshot.Headshot;
import mc.headshot.HeadshotConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class DealHeadshotMixin {
    // Used to prevent StackOverflows from recursive, infinite damage
    @Unique
    private static boolean ignoreDamageHeadshot = false;
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"), method = "damage")
    void dealExtraHeadshotDamageIfApplicable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HeadshotConfig headshotConfig = Headshot.config;
        
        // Make sure the shield didn't block anything
        //noinspection ConstantConditions
        if ((Object)this instanceof ServerPlayerEntity && !((BlockedByShieldInvoker)this).invokeBlockedByShield(source)) {
            if (!ignoreDamageHeadshot && source instanceof ProjectileDamageSource && source.getSource() != null) {
                ServerPlayerEntity pe = (ServerPlayerEntity)(Object)this;
                if (Headshot.calculateIsHeadHit(source.getSource().getPos(), pe)) {
                    pe.sendMessage(new LiteralText("You got headshot!"), true);
                    if (source.getAttacker() instanceof PlayerEntity) {
                        ((PlayerEntity)source.getAttacker()).sendMessage(new LiteralText("Headshot " + pe.getEntityName() + "!"), true);
                    }
                    ignoreDamageHeadshot = true;
                    pe.damage(source, (float)(amount * headshotConfig.damageMultiplier));
                    
                    if (headshotConfig.doBlind) {
                        pe.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, headshotConfig.blindTicks, 3));
                    }
                    if (headshotConfig.doNausea) {
                        pe.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, headshotConfig.nauseaTicks, 2));
                    }
                    
                    return;
                }
            }
            ignoreDamageHeadshot = false;
        }
    }
}
