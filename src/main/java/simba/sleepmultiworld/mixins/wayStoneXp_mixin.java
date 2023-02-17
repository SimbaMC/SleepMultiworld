package simba.sleepmultiworld.mixins;

import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.config.WaystonesConfigData;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneTeleportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerWaystoneManager.class)
public class wayStoneXp_mixin {

    @Redirect(
            method = "getExperienceLevelCost(Lnet/minecraft/world/entity/Entity;Lnet/blay09/mods/waystones/api/IWaystone;Lnet/blay09/mods/waystones/core/WarpMode;Lnet/blay09/mods/waystones/core/WaystoneTeleportContext;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/blay09/mods/waystones/config/WaystonesConfigData;dimensionalWarpXpCost()I",
                    remap = false
            ),
            require = 1,
            allow = 1,
            remap = false
    )
    private static int coordinate_scale_addon_mixin(WaystonesConfigData instance, Entity entity, IWaystone waystone, WarpMode warpMode, WaystoneTeleportContext context) {
        int dimensionalWarpXpCost = instance.dimensionalWarpXpCost();
        double xpLevelCost = 0;
        double minimumXpCost = instance.minimumXpCost();
        double maximumXpCost = instance.maximumXpCost();
        double targetWorld_coordinate;
        if (entity instanceof ServerPlayer player) {
            targetWorld_coordinate = player.getServer().getLevel(waystone.getDimension()).dimensionType().coordinateScale();
        } else {

            targetWorld_coordinate = switch (waystone.getDimension().location().toString()) {
                case "minecraft:the_nether", "0w0:hell" -> 8;
                default -> 1;
            };
        }
        double currentWorld_coordinate = entity.getLevel().dimensionType().coordinateScale();
        BlockPos target_pos = waystone.getPos();
        double dist_coordinate = Math.sqrt(
                Math.pow(entity.getX() * currentWorld_coordinate - target_pos.getX() * targetWorld_coordinate, 2)
                        + Math.pow(entity.getY() - target_pos.getY(), 2)
                        + Math.pow(entity.getZ() * currentWorld_coordinate - target_pos.getZ() * targetWorld_coordinate, 2)
        );
        if (instance.blocksPerXPLevel() > 0) {
            xpLevelCost = Mth.clamp(dist_coordinate / (double) ((float) instance.blocksPerXPLevel()), minimumXpCost, maximumXpCost);
            if (instance.inverseXpCost()) {
                xpLevelCost = maximumXpCost - xpLevelCost;
            }
        }
        final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Simba");
        LOGGER.warn(String.valueOf((int)Math.round(dimensionalWarpXpCost + xpLevelCost)));
        return (int)Math.round(dimensionalWarpXpCost + xpLevelCost);
    }

}
