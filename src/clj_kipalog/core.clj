(ns clj-kipalog.core
  (:require
   [clj-kipalog.post :as p]
   [clojure.string :as str]
   [cheshire.core :as json]
   [byte-streams :as bs]
   [aleph.http :as http]))

(def ^:const end-point "http://kipalog.com//api/v1")

(defn make-path
  [x]
  (str end-point x))

(defn- debug-err
  [x]
  (prn (json/parse-string (slurp (:body (ex-data x))) true)))

(defn- resp->json
  [resp]
  (-> resp :body bs/to-string (json/parse-string true)))

(defn- get
  ([path] (get path nil nil))
  ([path params headers]
   (resp->json
    (try @(http/get (make-path  "/post/hot")
                    (cond-> {:accept :json}
                      (map? headers) (merge headers)
                      (map? params)  (merge params)))
         (catch Exception e (throw e))))))

(defn- post
  ([path] (post path nil nil))
  ([path body params]
   (resp->json
    (try @(http/post (make-path path)
                     (cond-> {:accept :json}
                       (map? params) (merge params)
                       (map? body)   (merge {:body (json/encode body)})))
         (catch Exception e (throw e))))))

(defn valid-api?
  [x]
  (not (str/blank? x)))

(defn hot-posts
  [api-key]
  {:pre [(valid-api? api-key)]}
  (get "/post/hot"
       nil
       {:headers {"X-Kipalog-Token" api-key}}))

(defn newest-posts
  [api-key]
  {:pre [(valid-api? api-key)]}
  (get "/post/newest"
       nil
       {:headers {"X-Kipalog-Token" api-key}}))

(defn preview-post
  [api-key]
  {:pre [(valid-api? api-key)]}
  (post "/post/preview"
        {:content "**content of a post (markdown)**"}
        {:headers {"X-Kipalog-Token" api-key}}))

(defn post-by-tag
  [api-key tag]
  {:pre [(valid-api? api-key)]}
  (post "/post/bytag"
        {:tag_name (str tag)}
        {:headers {"X-Kipalog-Token" api-key}}))

(defn create-post
  [api-key post]
  {:pre [(valid-api? api-key)
         (map? post)]}
  (when (p/valid? post)
    (post "/post"
          post
          {:headers {"X-Kipalog-Token" api-key}})))


(comment
  (create-post "your-api")
  )
