(ns app.client
  (:require
    ;; project libs
    [app.secrets :as secrets]
    [app.utils :as utils :refer [clog]]
    [app.pathom :as p :refer [pathom-api]]
    [app.temp-db :refer [temp-db]]

    ;; external libs
    [clojure.string :as str]

    ;; internal libs
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.mutations :as m]))

;; TODO add workspaces to the project

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; COMPONENTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FirstComponent Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def temp-db-data (atom {}))

(m/defmutation update-temp-db [_]
  (action [{:keys [state]}]
          (clog {:message "[FirstComponent] MUTATION update-temp-db" :color "magenta" :props state})
          (reset! temp-db-data temp-db)))

(comment
  (comp/transact! APP [(update-temp-db)])
  @temp-db-data

  (app/schedule-render! APP)
  )

(defsc FirstComponent [this props]
  {#_#_:query []
   #_#_:ident []
   :initial-state      {}
   :initLocalState     (fn [this]
                         (clog {:message "[FirstComponent]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[FirstComponent] MOUNTED" :props p :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[FirstComponent]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/div)))


(def ui-first-component (comp/factory FirstComponent))


(comment

  (comp/transact! APP [(update-temp-db)])

  @temp-db-data

  (reset! temp-db-data {})

  (reset! (::app/state-atom APP) @temp-db-data)

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (reset! (::app/state-atom APP) {:one 1})

  (app/schedule-render! APP)

  )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this props]
  {#_#_:query []
   #_#_:ident []

   :initial-state      {}
   :initLocalState     (fn [this]
                         (clog {:message "[Root]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[Root] MOUNTED (with TimeStamp)" :props (js/Date.) :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[Root]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/h1 :.ui.header "Hello, Fulcro!")
    #_(ui-first-component temp-db)))


(comment

  (comp/transact! APP [(update-temp-db)])

  @temp-db-data

  (reset! temp-db-data {})

  (reset! (::app/state-atom APP) @temp-db-data)

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (reset! (::app/state-atom APP) {:one 1})

  (app/schedule-render! APP)

  )



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APP definition and init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defonce APP (app/fulcro-app))

(defn ^:export init []
  (app/mount! APP Root "app"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(comment
  (shadow/repl :main)

  ;; js hard reset
  (.reload js/location true)

  (-> APP
      (::app/state-atom)
      deref)

  (app/schedule-render! APP {:force-root? true})

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  )

