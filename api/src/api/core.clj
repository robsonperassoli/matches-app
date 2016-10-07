(ns api.core
  (:use compojure.core)
  (:use ring.middleware.json-params)
  (:use ring.adapter.jetty)
  (:require [clj-json.core :as json])
  (:require [api.teams :as teams])
  (:require [api.players :as players])
  (:require [api.match-results :as match-results])
  (:gen-class))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes handler
  (GET "/teams" []
    (json-response (teams/list)))

  (GET "/players" []
    (json-response (players/list)))

  (POST "/players" [player]
    (json-response (players/post player)))

  (PUT "/players/:id" [id player]
    (json-response (players/put id player)))

  (DELETE "/players/:id" [id]
    (json-response (players/delete id)))

  (GET "/match-results" []
    (json-response (match-results/list)))

  (POST "/match-results" [result]
    (json-response (match-results/post result))))

(def app
   (-> handler wrap-json-params))

(defn -main
  "Start the api server"
  [& args]
  (run-jetty app {:port 8080}))
