(ns reagent-dcom.cards
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reagent-dcom.core :as core]
            [cljs-time.core :as t]
            [cljs-time.coerce :refer [to-long]]
            )
  (:require-macros
   [devcards.core
    :as dc
    :refer [defcard defcard-doc defcard-rg deftest]]))

(defn mock-message [from-self i]
  {:time (to-long (t/date-time 1986 10 14 i))
   :text "keep in mind is most (all?) of these functions take the string to act on as the first paramter"
   :self from-self
   :sender "ffff"
   :color "#ff0"})

(def convo
  (shuffle
   [
     (mock-message true 0)
     (mock-message true 1)
     (mock-message true 2)
     (mock-message false 3)
     (mock-message false 4)
     (mock-message false 5)
     (mock-message false 6)]))

(defcard-rg room-card
  [core/conversation convo])
        
(defcard-rg msg-card
  [core/message (mock-message false 0)])

(defcard-rg home-page-card
  [core/home-page])

(reagent/render [:div] (.getElementById js/document "app"))

;; remember to run 'lein figwheel devcards' and then browse to
;; http://localhost:3449/cards
