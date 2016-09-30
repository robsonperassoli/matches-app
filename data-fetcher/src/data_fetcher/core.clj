(ns data-fetcher.core
  (:use net.cgrand.enlive-html)
  (:require [clojure.data.json :as json])
  (:gen-class))

(defstruct team :name :league :image)

(def base-url "http://www.fifaindex.com")
(def teams-url (str base-url "/pt-br/teams/"))

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

(defn get-teams [base-url teams-url]
  (map parse-team (get-team-rows base-url teams-url [])))

(defn save-teams-in-json-file [teams]
  (spit "teams.json" (json/write-str teams) :append false))

(defn -main
  [& args]
  (println "Fetching teams from " base-url)
  (save-teams-in-json-file (get-teams base-url teams-url))
  (println "Teams saved into teams.json"))
