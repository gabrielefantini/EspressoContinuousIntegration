./gradlew installAlphaDebug
sleep 1
advmanager list device
adb shell am start -n it.feio.android.omninotes.alpha/it.feio.android.omninotes.MainActivity
./gradlew connectedAndroidTest --info
