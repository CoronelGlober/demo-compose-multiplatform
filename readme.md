# Compose multiplatform demo


https://user-images.githubusercontent.com/69470506/186998390-50d0ecf3-398f-4399-9725-e43ae871ce62.mov




### Using the same user interface (UI) form android on all principal platforms

## App Features
This is a single UI codebase for an application that runs on all principal platforms, containing 3 screens:
- Seconds counter: Basic compose UI with a button and a coroutine that each seconds uptades the state.
- Shared drawing pad: Compose canvas allowing the user to make some drawings, also sends and receives the drawings to/from the TCP server so everyone connected can see and draw together.
- Chat screen: TCP chat that allows sending text messages to everyone connected to the chat room.

## App Architectural pattern
For having a pure Kotlin and reactive approach with a centralized state management, it implements a multiplatform MVI framework (if you are not familiar with it, take a look at [mobius][mobius] from Spotify) located at the common sourceSet, some of the benefits from this approach are:
- It makes it easy and organized to run background tasks via coroutines with a background dispatcher (connecting to a server or quering from a database) inside the Effects Processor.
- Makes sure the state is only updated in a single place, the Updater class.
- Delivers the state from the ViewModel in a reactive way using StateFlow.
- Convenient way of mapping models from the domain into a UI layer.

## App theming for each platform
The current theming is only changing colors and compositions, for example:
- On iOS the look and feel of the TopBar is obtained purely with colors and back arrow manipulation, instead of using the default material back arrow like Android and Desktop does, a combination of SVG and text is used


Theming related elements are defined on each platform´s sourceSet as following:

<img src="https://github.com/CoronelGlober/demo-compose-multiplatform/blob/master/artwork/themes.png" width=400 />

## Project Features
This is a fullstack (? - pending web) solution written in kotlin, using coroutines and flow showcasing the current capabilities of [Jetbrains compose][composeUrl] containing the following elements:

- TCP server: for allowing apps communicate between them.
- Android Client app
- iOS Client app
- Windows Client app
- Mac Client app
- Linux client app

Each client runs the same compose base code with their own theming (mostly varying color and some UI effects), and for desktop, multiple desktop-only features are added like:
- Desktop notifications
- Tray section icon display
- Window title bar actions menu
- Mouse pointer events handling
- Local image resource displaying

## Running it all
1) Start TCP server and configure it to your IP address and desired port: e.g. 192.168.0.10 8081 

    - from console at the root of the solution: `./gradlew :server:run`
    
    - from gradle tasks panel inside IntelliJ
    
    <img src="https://github.com/CoronelGlober/demo-compose-multiplatform/blob/master/artwork/server.png" width=400 />

2) Launching Android app:
    
   - from the run section on IntelliJ
   
   <img src="https://github.com/CoronelGlober/demo-compose-multiplatform/blob/master/artwork/android.png" width=400 />

3) Launching iOS app (works only on Mac computers, requires XCode and XCode command line tools installed and configured):

   - from the tasks panel on IntelliJ
   
   <img src="https://github.com/CoronelGlober/demo-compose-multiplatform/blob/master/artwork/ios.png" width=400 />

4) Launching Desktop app (same for Windows, Mac, Linux):

   - from the tasks panel on IntelliJ
   
   <img src="https://github.com/CoronelGlober/demo-compose-multiplatform/blob/master/artwork/desktop.png" width=400 />

The first time you go into the chat or drawing screen you will need to define the server address and port, after they have been set you will be able to move back and forward without doing it again 
## Credits
- Jetbrains!
- Google
- [Chat UI][chatUI]
- [Drawing App][drawingApp]

## License
MIT

[chatUI]:<https://github.com/SmartToolFactory/Compose-Speech-Bubble>
[drawingApp]:<https://github.com/SmartToolFactory/Compose-Drawing-App>
[composeUrl]:<https://www.jetbrains.com/lp/compose-mpp>
[mobius]:<https://github.com/spotify/mobius>
