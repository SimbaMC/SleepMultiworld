package simba.sleepmultiworld.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import simba.sleepmultiworld.DerivedLevelDataInterface;

@Mixin(ServerLevel.class)
public class ServerLevel_tick_mixin {
    @Shadow @Final
    private ServerLevelData serverLevelData;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
    void setDayTimeMixin(ServerLevel instance, long p_8616_){
        if (this.serverLevelData instanceof DerivedLevelData) {
            ((DerivedLevelDataInterface) this.serverLevelData).getWrapped().setDayTime(p_8616_);
        } else {
            this.serverLevelData.setDayTime(p_8616_);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;resetWeatherCycle()V"))
    void resetWeatherCycleMixin(ServerLevel instance){
        if (this.serverLevelData instanceof DerivedLevelData) {
            ServerLevelData wrappedLevel = ((DerivedLevelDataInterface) this.serverLevelData).getWrapped();
            wrappedLevel.setRainTime(0);
            wrappedLevel.setRaining(false);
            wrappedLevel.setThunderTime(0);
            wrappedLevel.setThundering(false);
        } else {
            this.serverLevelData.setRainTime(0);
            this.serverLevelData.setRaining(false);
            this.serverLevelData.setThunderTime(0);
            this.serverLevelData.setThundering(false);
        }
    }
}
