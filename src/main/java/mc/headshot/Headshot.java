package mc.headshot;

import mc.microconfig.MicroConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityPose;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class Headshot implements ModInitializer {
    public static HeadshotConfig config;
    
    @Override
    public void onInitialize() {
        config = MicroConfig.getOrCreate("headshot", new HeadshotConfig());
    }
    
    public static boolean calculateIsHeadHit(Vec3d sourcePos, ServerPlayerEntity player) {
        // Mostly for code cleanly-ness
        // TODO: Other poses? Generalize completely?
        if (player.getPose() == EntityPose.SWIMMING) {
            return calculateComplexHeadHit(sourcePos, player);
        } else {
            return calculateSimpleHeadHit(sourcePos, player);
        }
    }
    
    static boolean calculateSimpleHeadHit(Vec3d sourcePos, ServerPlayerEntity player) {
        double playerHeadStart = player.getPos().add(
            0.0,
            player.getDimensions(player.getPose()).height * 0.85F, 0.0
        ).y - 0.17;
        return sourcePos.y > playerHeadStart;
    }
    
    static boolean calculateComplexHeadHit(Vec3d sourcePos, ServerPlayerEntity player) {
        double headY = -Math.sin(Math.toRadians(player.pitch));
        
        double lengthMod = Math.cos(player.pitch);
        
        double headX = Math.sin(Math.toRadians(-player.yaw)) * lengthMod;
        double headZ = Math.cos(Math.toRadians(-player.yaw)) * lengthMod;
        ((ServerWorld)player.world).spawnParticles(
            ParticleTypes.BARRIER,
            player.getPos().x + headX,
            player.getPos().y + headY,
            player.getPos().z + headZ,
            1,
            0.0,
            0.0,
            0.0,
            0.0
        );
        return sourcePos.isInRange(player.getPos().add(headX, headY / 2, headZ), 0.4);
    }
}
