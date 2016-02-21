# 2DRacingGame
A 2D racing game, created for [course TDA367](http://www.cse.chalmers.se/edu/course/TDA367/)/LSP310.

## Packaging the project

### How to play?
Run the jar-file "2DRacingGame.jar" on the root level of this repo.


You can also create a runnable JAR yourself by executing:<br />
`./gradlew desktop:dist` (output can be found in: `desktop/build/libs/`)<br />
The jar can then be run by simply double clicking it or by running `java -jar path/to/jar.jar`.

## Running unit tests
All unit tests can be run by executing:
`./gradlew test` (or through your favourite IDE).<br />
Be aware that Gradle is smart enough to not run the tests twice unless they have changed. The tests cache can be cleared by running:
`./gradlew cleanTest`

## Documentation, RAD, SDD
Link to RAD, SDD and meeting protocols:
https://drive.google.com/folderview?id=0BxRJZY1j9wVVflVyM0huMmhwYWRxbURRVnZUSGE5aXZZOW9BYkVOUG5QdHpVT0dqYWZqVDQ&usp=sharing

## Third-party credits
* Nemo (2015). Car. http://pixabay.com/p-30984/?no_redirect
* Mohammad Seirafian (2014). Magic carpet. http://commons.wikimedia.org/wiki/File:Beautiful_silk_rug_by_Mohammad_Seirafian,_Isfahan,_Iran.JPG
* Brian Snelson (2009). Formel 1. http://en.wikipedia.org/wiki/Ferrari_F300#/media/File:1998_F1_car_Ferrari_F300_Goodwood
* Monster truck. http://upload.wikimedia.org/wikipedia/commons/c/c9/Monster_truck_2.JPG
* Simon Broen (2008). Motorcycle. http://commons.wikimedia.org/wiki/File:Megelli_Sports_motorcycle.jpg
