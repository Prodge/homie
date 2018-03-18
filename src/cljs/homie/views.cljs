(ns homie.views
  (:require [re-frame.core :as re-frame]
            [reagent-material-ui.core :as ui]
            [cljs-time.format :as tf]

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
        [ui/Toggle {:toggled (= "on" (:power @info)) :onToggle #(re-frame/dispatch [:yeelight-power id])}]]
      [ui/TableRowColumn
        [ui/Slider {:defaultValue (int (:bright @info)) :step 1 :min 0 :max 100 :onChange #(re-frame/dispatch [:yeelight-brightness id %2])}]]
      [ui/TableRowColumn (:rgb @info)]
      [ui/TableRowColumn (tf/unparse (tf/formatters :hour-minute-second) (tf/parse (tf/formatters :basic-date-time) (:last-seen @info)))]]))


(defn menu-drawer []
  (let [open? (re-frame/subscribe [:menu-open?])]
    [ui/Drawer {:onRequestChange #(re-frame/dispatch [:toggle-menu-drawer]) :open @open? :docked false}
     [ui/MenuItem "Homie"]
     [ui/MenuItem "Yeelight"]
     [ui/MenuItem "Cameras"]]))

(defn main-panel []
  (let [yeelight-ids (re-frame/subscribe [:yeelight-ids])]
	[ui/MuiThemeProvider theme-defaults
      [:div
        [ui/AppBar {:title "Homie" :onLeftIconButtonTouchTap #(re-frame/dispatch [:toggle-menu-drawer])}]
        [menu-drawer]
        [ui/Table {:selectable false}
          [ui/TableHeader {:adjustForCheckbox false :displaySelectAll false}
           [ui/TableRow
            [ui/TableHeaderColumn "Location"]
            [ui/TableHeaderColumn "On/Off"]
            [ui/TableHeaderColumn "Brightness"]
            [ui/TableHeaderColumn "Colour"]
            [ui/TableHeaderColumn "Last Seen"]]]
          [ui/TableBody
            (for [id @yeelight-ids]
              [yeelight-info id])]]]]))
