# NotificationTimer
Timer in Notification for Android that supports play, pause, stop by buttons.
</br>
</br>

Demo
-------

<img src="https://user-images.githubusercontent.com/54348567/107766943-98fa2c00-6d77-11eb-9f93-ca6432d54d2f.gif" width="300" height="620">
</br>
</br>

Dependency
--------------
Add this in your root ```build.gradle``` file (not your module ```build.gradle``` file)
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Then, Add the library to your module ```build.gradle```
```
dependencies {
    implementation 'com.github.Jun-Hub:NotificationTimer:latest.release.version'
}
```
</br>
</br>

Features
----------------
* This is Singleton Pattern which means you can't create more than one Timer.
* There are 2 types of Notification. One is 'default mode' that show a simple timer. The other one is 'control mode' that show with control buttons(play, pause, stop)

</br>
</br>

Usage
--------------
Add this in your ```manifest``` file
```
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    
<service android:name="io.joon.notificationtimer.TimerService"/>
```
And simple usage:

``` kotlin
NotificationTimer.Builder(context)
    .setSmallIcon(R.drawable.ic_timer)
    .play(timeMillis)
```
control mode usage:

``` kotlin
NotificationTimer.Builder(context)
    .setSmallIcon(R.drawable.ic_timer)
    .setPlayButtonIcon(R.drawable.ic_play_noti)     //*required. (If you set ControlMode as 'true')
    .setPauseButtonIcon(R.drawable.ic_pause_noti)   //*required. (If you set ControlMode as 'true')
    .setStopButtonIcon(R.drawable.ic_stop_noti)     //*required. (If you set ControlMode as 'true')
    .setControlMode(true)
    .play(timeMillis)
```
</br>
</br>

Detail Usage
------------------
```kotlin
val notiTimer = NotificationTimer.Builder(context)

            .setSmallIcon(R.drawable.ic_timer)              //*required

	    .setPlayButtonIcon(R.drawable.ic_play_noti)     //should use xml file (not jpg or png something)

            .setPauseButtonIcon(R.drawable.ic_pause_noti)   //should use xml file (not jpg or png something)

            .setStopButtonIcon(R.drawable.ic_stop_noti)     //should use xml file (not jpg or png something)

            .setControlMode(true)                           //set as control mode(with play, pause, stop buttons)    default value is 'false'

            .setColor(R.color.sexy_blue)                    //same as NotificationCompat.

            .setShowWhen(false)                             //same as NotificationCompat.   default value is 'false'

            .setAutoCancel(false)                           //same as NotificationCompat.   default value is 'false'

            .setOnlyAlertOnce(true)                         //same as NotificationCompat.   default value is 'true'

            .setPriority(NotificationCompat.PRIORITY_LOW)   //same as NotificationCompat.   default value is 'PRIORITY_LOW'

            .setContentIntent(pendingIntent)                //same as NotificationCompat.

            .setContentTitle("This is NotiTimer!!")         //same as NotificationCompat. 

            .setOnTickListener { untilFinish ->             //callback timeMillis until finished
                time_until_finish_text.text = untilFinish.toString() 
            }

            .setOnFinishListener {                          //callback when timer finished
                Toast.makeText(this, "Timer finished", Toast.LENGTH_SHORT).show() 
            }    

notiTimer.play(timeMillis)  //play Timer. But when Timer state is Paused, it will execute 'Replay' by itself.

notiTimer.pause()           //pause Timer.

notiTimer.stop()            //stop Timer. And set time which is you set before.

notiTimer.terminate()       //terminate Timer. And remove Notification as well.
```
What if you want current Timer state?:

```kotlin
if(TimerService.state==TimerState.RUNNING) {
    //TODO your code
}

if(TimerService.state==TimerState.PAUSED) {
    //TODO your code
}

if(TimerService.state==TimerState.STOPPED) {
    //TODO your code
}

if(TimerService.state==TimerState.TERMINATED) {
    //TODO your code
}
```
</br>
</br>

License
-----------

    Copyright 2021 Joon Lee

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
