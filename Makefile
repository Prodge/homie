
uberjar: clean cljs garden
	lein uberjar

run:
	lein run

clean:
	lein clean

cljs:
	lein cljsbuild once min

garden:
	lein garden once
