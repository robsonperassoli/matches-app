(ns data-fetcher.core
  (:use net.cgrand.enlive-html)
  (:require [clojure.data.json :as json])
  (:gen-class))

(defstruct team :name :league :image)

(def base-url "http://www.fifaindex.com")
(def teams-url (str base-url "/pt-br/teams/"))
(def filename "teams.json")

(defn fetch-url [url]
  (html-resource (java.net.URL. url)))

(defn has-next-page? [page-content]
  (let [next-page-links (select page-content #{[:ul.pager :li.next :a]})]
    (not (= (first next-page-links) nil))))

(defn get-next-page-url [page-content]
  (:href (:attrs (first (select page-content #{[:ul.pager :li.next :a]})))))

(defn get-team-rows-in-page [page-content]
  (select page-content #{[:table.teams :tbody :tr]}))

(defn get-team-rows [base-url teams-url rows]
  (let [page-content (fetch-url teams-url)]
      (if (has-next-page? page-content)
          (do
            (println "get rows from " teams-url)
            (recur base-url (str base-url (get-next-page-url page-content)) (concat rows (get-team-rows-in-page page-content))))
          (do
            (println "no more rows! well done")
            rows))))

(defn parse-team [row]
  (struct team (first (select row #{[:td :a :> text-node]}))
               (second (select row #{[:td :a :> text-node]}))
               (:src (:attrs (first (select row #{[:td :a :img]}))))))

(defn full-path-images [teams]
  (map (fn [team] (update-in team [:image] #(str base-url %))) teams))

(defn get-teams [base-url teams-url]
  (let [teams (map parse-team (get-team-rows base-url teams-url []))]
       (full-path-images teams)))

(defn save-teams-in-json-file [teams filename]
  (spit filename (json/write-str teams) :append false))

(defn -main
  [& args]
  (println "Fetching teams from " base-url)
  (save-teams-in-json-file (get-teams base-url teams-url) filename)
  (println "Teams saved into " filename))
