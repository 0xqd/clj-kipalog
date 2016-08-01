deps:
	lein deps
.PHONY: deps

init: deps
.PHONY: init

test:
	lein test
.PHONY: test
