<img src="https://user-images.githubusercontent.com/1439229/44613748-affbdb00-a818-11e8-8a01-213e77638d14.png" width="300">

# Wakelock - Power Manager
[![Build Status](https://travis-ci.org/d4rken/wakelock-revamp.svg?branch=dev)](https://travis-ci.org/d4rken/wakelock-revamp)

Wakelock gives you control over the Android Power- and WifiManager.
For example, you can force the PowerManager to keep the screen on or have the CPU still running in standby mode or make sure the Wifi connection keeps running at full performance.

## FAQ

* What's up with the Samsung Galaxy Note 4?

At some point there was an OTA Update (likely 5.1 or later) which caused the Note 4 to randomly freeze and reboot. By chance a user noticed that acquiring a partial wakelock fixes this issue, which is exactly what this app can do. I've been asked multiple times why this works, but I actually don't know for sure. I would guess that the update made changes to power-saving states and battery saving measures (unclocking/volting?) that made the system unstable such that it will crash when going into stand-by mode. A partial wakelock will keep the system from going into standby. Some users reported that putting the phone shortly in the freezer (without the battery) would temporarily improve their chances of booting successfully which would point to a hardware related issue, possibly production variance.


Here is a 67 page thread of people with this issue: [XDA-Thread](https://forum.xda-developers.com/note-4/help/note-4-freezing-restarting-t3348821)

* Can I hide the notification icon?

Theoretically yes, but it will make the app more unreliable. A visible notification increases an apps priority such that it is less likely to be killed by the system to free resources if another app needs more resources.


## Contributions
* Are you multilingual? [Translate this app](https://crowdin.com/project/wakelock) to make it more accessible.
* If you can code, maybe submit a PR fixing an [open issue](https://github.com/d4rken/wakelock-revamp/issues).

## Download
* [Google Play](https://play.google.com/store/apps/details?id=eu.thedarken.wldonate)
* [GitHub](https://github.com/d4rken/wakelock-revamp/releases/latest)

## Screenshots
<img src="https://user-images.githubusercontent.com/1439229/44613749-b0947180-a818-11e8-8dd4-cf4c28414aaa.png" width="400"><img src="https://user-images.githubusercontent.com/1439229/44613750-b0947180-a818-11e8-8e71-9d5345154b74.png" width="400">
