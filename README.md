# score-minion-android
Android app for Score Minion

See online readme: /Users/martincochran/gae-docs/cloud.google.com/appengine/docs/python/endpoints/gen\_clients 

Initial set up for adding the client libraries:
Generate the endpoints library (TODO: add command).
~/google-cloud-sdk/platform/google\_appengine/endpointscfg.py get\_client\_lib java -bs gradle scores\_api.ScoresApi

Unzip the library in /tmp
Import the library into Android Studio as a Gradle-based module.
Add the new module as a dependency of the app module.
