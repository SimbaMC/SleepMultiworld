# SleepMultiworld
Allow sleep in custom level of a datapack

Fix MC-188578 (https://bugs.mojang.com/browse/MC-188578)

When sleeping in a bed in any custom dimension that allows a user to sleep in a bed, it does set the user's spawn, but doesn't set the time to day, as it seems the weather and time of day are linked to the overworld's time and weather.

This mod fixes this issue, if players sleep in a custom level then the time of all levels which following the minecraft:overworld will be updated.

Ver 1.1, and also reset weather cycle like sleep in the main level.
