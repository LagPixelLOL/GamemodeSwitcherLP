# GamemodeSwitcherLP
Serverside mod for switching gamemodes with luckperms support.
##
Now you can finally control your player's ability to change gamemodes without /op! \
Also have some QoL features!

*Currently you can't change other player's gamemode with these commands, but I will add it if there are actually some downloads.
## Features:
### Added:
added commands /gm s/c/sp/a /gms /gmc /gmsp /gm - change gamemodes. \
added command /suicide - no comment. \
added commands /spec /s - the first time you run it will change your gamemode to spectator, the second time you run it will teleport you back to your starting location then change back your gamemode, players without gslp.admin permission can't change their gamemode while spectating. \
added permission for /me and /say - you can use this to toggle player's ability to run /me and/or /say. \
added speed limiter for players - you can control player's max speed with permissions. \
added command /speedlimit - toggles or sets arguments for speed limiter(blocks per second, notify console). \
added notification for server list ping - you can see who are refreshing their server lists :D. \
added command /serverlistpingnotifier - toggles or sets arguments for Server List Ping Notifier, use "/serverlistpingnotifier set 0" to turn it off. \
added packet limiter for players - you can control player's max packets per second with permissions. \
added command /packetlimit - toggles or sets arguments for packet limiter(packets per second, exceeded command(use "%player%" to represent the player name, use "&" to change colour, use "\\&" to represent "&")).
### Fixed:
fixed vanilla bug - desync exp bar when teleporting across dimensions. \
fixed vanilla bug - desync potion effect when teleporting across dimensions. \
fixed vanilla bug - desync player ability when teleporting across dimensions.
## Permissions:
gslp.admin - set GamemodeSwitcherLP admin permission. \
gslp.gamemode.survival - set gamemode to survival permission. \
gslp.gamemode.creative - set gamemode to creative permission. \
gslp.gamemode.spectator - set gamemode to spectator permission. \
gslp.gamemode.adventure - set gamemode to adventure permission. \
gslp.spectate - /spec /s permission. \
gslp.suicide - /suicide permission. \
gslp.speedlimit.info - /speedlimit info permission. \
gslp.speedlimit.bypass - set bypass speed limit permission. \
gslp.speedlimit.notify - set show players that are moving too fast notification permission. \
gslp.packetlimit.info - /packetlimit info permission. \
gslp.packetlimit.bypass - set bypass packet limit permission. \
gslp.packetlimit.notify - set show players that exceeded packet limit notification permission. \
minecraft.me - /me permission. \
minecraft.say - /say permission.
## Note:
You can only install this mod on a server, this mod will not work on client! \
Use luckperms for the best experience! https://luckperms.net/
