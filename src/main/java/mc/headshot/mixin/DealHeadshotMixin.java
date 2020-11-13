package mc.headshot.mixin;

import jdk.nashorn.internal.ir.Block;
import mc.headshot.HeadshotConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class DealHeadshotMixin {
    private static boolean ignore = false;
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"), method = "damage")
    void dealExtraHeadshotDamageIfApplicable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HeadshotConfig headshotConfig = AutoConfig.getConfigHolder(HeadshotConfig.class).getConfig();
        //noinspection ConstantConditions
        if ((Object)this instanceof ServerPlayerEntity && !((BlockedByShieldInvoker)this).invokeBlockedByShield(source)) {
            if (!ignore && source instanceof ProjectileDamageSource && source.getSource() != null) {
                PlayerEntity pe = (PlayerEntity)(Object)this;
                double playerHeadStart = pe.getPos().add(0.0, pe.getDimensions(pe.getPose()).height * 0.85F, 0.0).y - 0.17;
                if (source.getSource().getPos().y > playerHeadStart) {
                    pe.sendMessage(new LiteralText("You got headshot!"), true);
                    if (source.getAttacker() instanceof PlayerEntity) {
                        ((PlayerEntity)source.getAttacker()).sendMessage(new LiteralText("Headshot!"), true);
                    }
                    ignore = true;
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
            ignore = false;
        }
    }
}
