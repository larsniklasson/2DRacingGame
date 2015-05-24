# 2DRacingGame
A 2D racing game, created for [course TDA367](http://www.cse.chalmers.se/edu/course/TDA367/)/LSP310.

## Packaging the project

### Runnable jar
A runnable JAR can be created by executing:<br />
`./gradlew desktop:dist` (output can be found in: `desktop/build/libs/`)<br />
The jar can then be run by simply double clicking it or by running `java -jar path/to/jar.jar`.

### Website (HTML/Javascript)
Packaging the project as a website through HTML/Javascript can be done by executing:<br />
`./gradlew html:dist` (output can be found in: `html/build/dist/`

### Android / iOS
Building for Android/iOS is also possible but is somewhat more complicated. You can read more about this in [the official LibGDX documentation](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline#packaging-the-project).


## Running unit tests
All unit tests can be run by executing:
`./gradlew test` (or through your favourite IDE).<br />
Be aware that Gradle is smart enough to not run the tests twice unless they have changed. The tests cache can be cleared by running:
`./gradlew cleanTest`

## Documentation, RAD, SDD
Link to RAD, SDD and meeting protocols:
https://drive.google.com/folderview?id=0BxRJZY1j9wVVflVyM0huMmhwYWRxbURRVnZUSGE5aXZZOW9BYkVOUG5QdHpVT0dqYWZqVDQ&usp=sharing
