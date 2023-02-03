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
    void setDayTimemixin(ServerLevel instance, long p_8616_){
        if (serverLevelData instanceof DerivedLevelData) {
            ((DerivedLevelDataInterface) serverLevelData).getWrapped().setDayTime(p_8616_);
        } else {
            serverLevelData.setDayTime(p_8616_);
        }
    }
}
