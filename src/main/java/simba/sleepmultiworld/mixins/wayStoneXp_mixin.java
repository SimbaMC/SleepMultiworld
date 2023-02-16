package simba.sleepmultiworld.mixins;

import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerWaystoneManager.class)
public class wayStoneXp_mixin {

    @ModifyVariable(
            method = "getExperienceLevelCost(Lnet/minecraft/world/entity/Entity;Lnet/blay09/mods/waystones/api/IWaystone;Lnet/blay09/mods/waystones/core/WarpMode;Lnet/blay09/mods/waystones/core/WaystoneTeleportContext;)I",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/util/Mth;clamp(DDD)D"
            ),
            require = 1,
            allow = 1,
            name = "dimensionalWarpXpCost",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/blay09/mods/waystones/config/WaystonesConfigData;dimensionalWarpXpCost()I"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/blay09/mods/waystones/config/WaystonesConfigData;blocksPerXPLevel()I",
                            ordinal = 0
                    )
            )
    )
    static int coordinate_scale_addon_mixin(int dimensionalWarpXpCost){
        return 0;
    }

}
