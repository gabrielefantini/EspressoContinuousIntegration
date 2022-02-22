./gradlew installAlphaDebug
sleep 1
adb shell am start -n it.feio.android.omninotes.alpha/it.feio.android.omninotes.MainActivity
./gradlew connectedCheck --info
