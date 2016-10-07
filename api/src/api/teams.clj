(ns api.teams
  (:require [clj-json.core :as json]))

(defn list []
  (json/parse-string (slurp "resources/teams.json")))
