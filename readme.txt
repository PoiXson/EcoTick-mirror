This plugin intentionally lags your server when there are no players online. This reduces needless CPU load and lowers power consumption. Similar to the hibernate plugin, but possibly better, and open source.

When the last player disconnects from the server, there is a 30 second delay, then the plugin will slow the server by sleeping the main thread, and allow no more than 1 tps. It will also unload chunks and free memory every 5 minutes.

If your server console is spammed with lag warnings, this can be disabled in the bukkit.yml file, change warn-on-overload to false.

Requires pxnCommonMC plugin:
https://dev.bukkit.org/projects/pxncommonpluginmc
or https://www.spigotmc.org/resources/pxncommonpluginmc.107049/

Discord: https://discord.gg/jHgP2a3kDx

Dev Builds: http://dl.poixson.com/mcplugins/EcoTick/
