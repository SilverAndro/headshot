package mc.headshot.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class DealHeadshotMixin {
    private static boolean ignore = false;
    
    @Inject(at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropShoulderEntities()V"), method = "damage")
    void dealExtraHeadshotDamageIfApplicable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!ignore && source instanceof ProjectileDamageSource && source.getSource() != null) {
            PlayerEntity pe = (PlayerEntity)(Object)this;
            double playerHeadStart = pe.getPos().add(0.0, pe.getEyeHeight(pe.getPose()), 0.0).y - 0.17;
            if (source.getSource().getPos().y > playerHeadStart) {
                pe.sendMessage(new LiteralText("You got headshot!"), true);
                if (source.getAttacker() instanceof PlayerEntity) {
                    ((PlayerEntity)source.getAttacker()).sendMessage(new LiteralText("Headshot!"), true);
                }
                ignore = true;
                pe.damage(source, amount * 1.05f);
                return;
            }
        }
        ignore = false;
    }
}
