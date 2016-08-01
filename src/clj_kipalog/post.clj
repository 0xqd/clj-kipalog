(ns clj-kipalog.post
  (:require
   [clojure.string :as str]))

(defn valid?
  [{:keys [title content status]}]
  (not (or (str/blank? title)
           (str/blank? content)
           (str/blank? status))))
