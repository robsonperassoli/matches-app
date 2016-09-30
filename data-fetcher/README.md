# data-fetcher
Fetch teams information from http://www.fifaindex.com

## Installation
Clone the repository and compile using `$ lein uberjar` command

## Usage
To download the team information just execute the jar in target/uberjar folder like: `$ java -jar data-fetcher-0.1.0-standalone.jar `, at the end of execution a file named teams.json is created.

### Sample file (teams.json)
```json
[{
	"name": "FC Bayern",
	"league": "Bundesliga",
	"image": "\/static\/FIFA16\/images\/crest\/50\/light\/21.png"
}, {
	"name": "FC Barcelona",
	"league": "Liga BBVA",
	"image": "\/static\/FIFA16\/images\/crest\/50\/light\/241.png"
}, {
	"name": "Real Madrid",
	"league": "Liga BBVA",
	"image": "\/static\/FIFA16\/images\/crest\/50\/light\/243.png"
}, {
	"name": "Paris SG",
	"league": "Ligue 1",
	"image": "\/static\/FIFA16\/images\/crest\/50\/light\/73.png"
}, {
	"name": "Chelsea",
	"league": "Barclays PL",
	"image": "\/static\/FIFA16\/images\/crest\/50\/light\/5.png"
}]
```
