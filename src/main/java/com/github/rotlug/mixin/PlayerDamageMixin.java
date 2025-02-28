package com.github.rotlug.mixin;

import com.github.rotlug.BossKeepInventory;
import com.github.rotlug.ConfigManager;
import com.github.rotlug.TickTimer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerDamageMixin {
    @Unique
    private final TagKey<EntityType<?>> bossTag = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("c", "bosses"));

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamageByBoss(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerPlayerEntity) {
            if (source.getAttacker() instanceof LivingEntity) {
                String mobId = Registries.ENTITY_TYPE.getId(source.getAttacker().getType()).toString();

                if (source.getAttacker().getType().isIn(bossTag) || ConfigManager.bossMobs.contains(mobId)) {
                    TickTimer.resetTimer();
                    TickTimer.start();
                    BossKeepInventory.setKeepInventory(true);
                }
            }
        }
    }
}