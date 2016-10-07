(ns api.db
  (:require [monger.core :as mg]))

(def db (mg/get-db (mg/connect) "soccer-mgr"))
