(ns reagent-dcom.core
    (:require [reagent.core :as reagent :refer [atom]]
              [clojure.string :as str]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljs-time.coerce :refer [from-long]]
              [clojure.string :refer [join]]
              ))

;; -------------------------
;; Views

(defn message [msg]
  "Makes a div for a single message."
  [:div
   {:className "message"
    :style
            (if (:self msg)
              {
               :border-right "5px solid"
               :border-right-color (:color msg)
               :padding-right "15px"
               :text-align "right"
               }
              {
               :border-left "5px solid"
               :border-left-color (:color msg)
               :padding-left "15px"
               }
              )}
               
   [:p
    {:style {:margin-bottom "-10px"
             :color "#ccc"
             }}
    (str
     (from-long (:time msg))
     " "
     (:sender msg)
     )]
   (if (:texts msg)
     (for [t (:texts msg)]
       [:p (:text msg)])
     [:p (:text msg)])])

(defn group [messages]
  "Groups sequential messages by the same sender."
  (let [concatinate (fn [msgs]
                      (assoc (first msgs) :texts
                             (map :text msgs)))
        grouped-msgs (partition-by :sender messages)]
    (map concatinate grouped-msgs)))

(defn conversation [msgs]
  "Takes a list of messages and produces a conversation."
  (let [sorted (sort-by :time msgs)
        grouped (group sorted)]
    [:div
     (for [msg grouped]
       ^{:key msg} [message msg])]))


(defn home-page []
  (let [room (atom nil)]
    (fn []
      [:div [:h2 "dcom"]
       [:div
        [:p "enter a room you'd like to join"]
        [:input {:type "text"
                 :value @room
                 :on-change #(reset! room
                                     (-> % .-target .-value))}]]
       (if (not (str/blank? @room))
         [:div
          [:p (str "join the room " @room)]
          [:button "join!"]])])))
        

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
