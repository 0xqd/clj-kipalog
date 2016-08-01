deps:
	lein deps
.PHONY: deps

init: deps
.PHONY: init

test:
	lein autotest
.PHONY: test
