(ns api.match-results)

(defn list []
  [{:home_team "2iu3h4iu2h3iu" :home_team_goals 2 :home_team_player "a65sd456a4sd"
    :away_team "a7std6a76s" :away_team_goals 1 :away_team_player "as8d7fs8d7f6" :date_time "2016-10-01 00:00:01"}])

(defn post [result]
  (get (list) 0))
