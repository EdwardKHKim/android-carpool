# Android Carpool

The repository contains an Android native mobile application that allows users (riders) to list a route they would like to carpool in and users (drivers) to select a listed route that they are driving in. The cost function is calculated using a rate that is below the gas price of the ride following British Columbia carpool regulation. 

## Demo

![Demo](https://github.com/EdwardKHKim/android_carpool/blob/master/android_carpool_demo.gif)

## Installation Instructions
1. Make directory named AndroidStudioProjects. 
```
cd
mkdir AndroidStudioProjects
cd AndroidStudioProjects
```
2. Clone this repository into AndroidStudioProjects.
```
git clone https://github.com/EdwardKHKim/android_carpool.git
```

## Development Requirements
#### Android Studio 
1. Download and install [Android Studio](https://developer.android.com/studio).

#### Mapbox 
This project use Mapbox APK 
1. Sign up for [Mapbox](https://www.mapbox.com/).
2. Get your access token and add in /res/values/strings.xml 
```
<resources>
  ...
  <string name="mapbox_access_token">YOUR_MAPBOX_ACCESS_TOKEN</string>
  ...
</resources> 
```
3. You can create a custom style map and add it in /res/values/strings.xml 
```
<resources>
  ...
  <string name="mapbox_style">YOUR_CUSTOM_STYLE_URL</string>
  ...
</resources> 
```

#### Google Firebase 
This project use Google Firebase Real-time Database as the back end. The repository includes the google-services.json file but you can also add Firebase to this project using your own Google account by following [this instruction](https://firebase.google.com/docs/android/setup).

#### Emulator 
You can either use your own Android device or the Emulators provided in Android Studio. To use an Emulator from Android Studio
1. Open AVD Manager
2. Create Virtual Device...

## Attributions
The folowing sets forth attribution notices for vector drawables from third party that are contained in this repository. 
- Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from [www.flaticon.com](www.flaticon.com)
- Icon made by [Good Ware](https://www.flaticon.com/authors/good-ware) from [www.flaticon.com](www.flaticon.com)
- <a href="https://www.freepik.com/free-photos-vectors/technology">Technology vector created by macrovector - www.freepik.com</a>
