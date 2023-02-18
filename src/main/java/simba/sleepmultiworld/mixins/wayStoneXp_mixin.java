package simba.sleepmultiworld.mixins;

import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneTeleportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(PlayerWaystoneManager.class)
public class wayStoneXp_mixin {

    /**
     * @author Simba
     * @reason Rewrite for support coordinateScale
     */
    @Overwrite(remap = false)
    public static int getExperienceLevelCost(Entity entity, IWaystone waystone, WarpMode warpMode, WaystoneTeleportContext context){
        if (!(entity instanceof Player player)) {
            return 0;
        }

        if (context.getFromWaystone() != null && waystone.getWaystoneUid().equals(context.getFromWaystone().getWaystoneUid())) {
            return 0;
        }

        boolean enableXPCost = !player.getAbilities().instabuild;

        int xpForLeashed = WaystonesConfig.getActive().xpCostPerLeashed() * context.getLeashedEntities().size();

        double xpCostMultiplier = warpMode.getXpCostMultiplier();
        if (waystone.isGlobal()) {
            xpCostMultiplier *= WaystonesConfig.getActive().globalWaystoneXpCostMultiplier();
        }

        // Insert
        double targetWorld_coordinate;
        if (entity instanceof ServerPlayer serverplayer) {
            try {
                targetWorld_coordinate = Objects.requireNonNull(Objects.requireNonNull(serverplayer.getServer()).getLevel(waystone.getDimension())).dimensionType().coordinateScale();
            } catch (NullPointerException e) {
                targetWorld_coordinate = 1;
            }
        } else {
            targetWorld_coordinate = switch (waystone.getDimension().location().toString()) {
                case "minecraft:the_nether", "0w0:hell" -> 8;
                default -> 1;
            };
        }
        double currentWorld_coordinate = entity.getLevel().dimensionType().coordinateScale();
        // End Insert

        BlockPos pos = waystone.getPos();
        double dist_coordinate = Math.sqrt(
                Math.pow(entity.getX() * currentWorld_coordinate - pos.getX() * targetWorld_coordinate, 2)
                        + Math.pow(entity.getY() - pos.getY(), 2)
                        + Math.pow(entity.getZ() * currentWorld_coordinate - pos.getZ() * targetWorld_coordinate, 2)
        );
        final double minimumXpCost = WaystonesConfig.getActive().minimumXpCost();
        final double maximumXpCost = WaystonesConfig.getActive().maximumXpCost();
        double xpLevelCost = 0;
        if (WaystonesConfig.getActive().blocksPerXPLevel() > 0) {
            xpLevelCost += dist_coordinate / (float) WaystonesConfig.getActive().blocksPerXPLevel();
            if (WaystonesConfig.getActive().inverseXpCost()) {
                xpLevelCost = maximumXpCost - xpLevelCost;
            }
        }
        if (waystone.getDimension() != player.level.dimension()) {
            int dimensionalWarpXpCost = WaystonesConfig.getActive().dimensionalWarpXpCost();
            xpLevelCost += dimensionalWarpXpCost;
        }
        xpLevelCost = Mth.clamp(xpLevelCost, minimumXpCost, maximumXpCost);
        return enableXPCost ? (int) Math.round((xpLevelCost + xpForLeashed) * xpCostMultiplier) : 0;
    }

}
