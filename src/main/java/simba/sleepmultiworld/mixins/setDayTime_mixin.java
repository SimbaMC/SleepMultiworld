package simba.sleepmultiworld.mixins;

import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import simba.sleepmultiworld.DerivedLevelDataInterface;

@Mixin(DerivedLevelData.class)
public class setDayTime_mixin implements DerivedLevelDataInterface {
    @Shadow @Final
    private ServerLevelData wrapped;

    public ServerLevelData getWrapped(){
        return wrapped;
    }

}
