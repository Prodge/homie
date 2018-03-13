(ns homie.views
  (:require [re-frame.core :as re-frame]
            [reagent-material-ui.core :as ui]
            [homie.subs :as subs]))


(defn icon [nme] [ui/FontIcon {:className "material-icons"} nme])
(defn color [nme] (aget ui/colors nme))

;; create a new theme based on the dark theme from Material UI
(defonce theme-defaults {:muiTheme (ui/getMuiTheme
                                    (-> ui/darkBaseTheme
                                        (js->clj :keywordize-keys true)
                                        (update :palette merge {:primary1Color (color "amber500")
                                                                :primary2Color (color "amber700")})
                                        clj->js))})

(defn yeelight-info [id]
  (let [info (re-frame/subscribe [:yeelight-info id])]
    [ui/TableRow
      [ui/TableRowColumn (:name @info)]
      [ui/TableRowColumn
        [ui/Toggle {:toggled (= "on" (:power @info)) :onToggle #(re-frame/dispatch [:toggle-yeelight id])}]]
      [ui/TableRowColumn
        [ui/Slider {:defaultValue (int (:bright @info)) :step 1 :min 0 :max 100}]]
      [ui/TableRowColumn (:rgb @info)]]
    ))

(defn main-panel []
  (let [yeelight-ids (re-frame/subscribe [:yeelight-ids])]
	[ui/MuiThemeProvider theme-defaults
      [:div
        [:h2 "Homie"]
        [ui/Table
          [ui/TableBody
            (for [id @yeelight-ids]
              [yeelight-info id])]]]]))
