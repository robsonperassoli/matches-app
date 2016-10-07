(ns api.players
  (:require [monger.collection :as mc]
    [api.util :refer [identify stringfy-id stringfy-ids new-object-id]])
  (:use [api.db :refer [db]])
  (:import org.bson.types.ObjectId))

(defn list []
  (-> (mc/find-maps db :players) stringfy-ids))

(defn post [player]
    (-> (mc/insert-and-return db :players (identify player)) stringfy-id))

(defn put [id player]
  (let [oid (new-object-id id)]
    (mc/update-by-id db :players  player)
    (-> (mc/find-map-by-id db :players (new-object-id id)) stringfy-id)))

(defn delete [id]
  (mc/remove-by-id db :players (new-object-id id)))
