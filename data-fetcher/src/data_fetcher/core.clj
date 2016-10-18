(ns data-fetcher.core
  (:use net.cgrand.enlive-html)
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [base64-clj.core :as base64])
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

(defn download-image [image-url]
  (let [headers {"Host" "www.fifaindex.com" "Referer" "http://www.fifaindex.com/pt-br/teams/"}
        resp (http/get image-url {:as :byte-array :headers headers})]
          (println "Finished download of " image-url)
          (-> (:body @resp) base64/encode-bytes String.)))

(defn fill-image [teams base-url]
  (map (fn [team]
    (merge team {:image (download-image (str base-url (:image team)))})) teams))

(defn get-teams [base-url teams-url]
  (-> (map parse-team (get-team-rows base-url teams-url []))
      (fill-image base-url)))

(defn save-teams-in-json-file [teams filename]
  (spit filename (json/write-str teams) :append false))

(defn -main
  [& args]
  (println "Fetching teams from " base-url)
  (save-teams-in-json-file (get-teams base-url teams-url) filename)
  (println "Teams saved into " filename))
