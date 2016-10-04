# soccer-win-counter
Manage you wins/losses against your buddies on soccer games 

## API definition
### GET /teams
```json
[{
  "name": "Arsenal",
  "league": "Premiere league",
  "image": "<base64 encoded binary>" 
}]
```

### GET /players
```json
[{
  "name": "Robson"
}]
```

### GET /match-results
```json
[{
  "home_team": "<team_uid>",
  "home_team_goals": 1,
  "home_team_player": "<player_uid>",
  "away_team": "<team_uid>",
  "away_team_goals": 2,
  "home_team_player": "<player_uid>"
}]
```

