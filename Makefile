.DEFAULT_GOAL := all

all: install build

install:
	@npm install
.PHONY: install

dirname   := $(notdir $(abspath .))
DOCKER_REGISTRY ?= docker.io
IMAGE_NAME ?= $(DOCKER_REGISTRY)/agilestacks/$(dirname)
container  = $(dirname)

build:
	docker build --no-cache --force-rm -t $(IMAGE_NAME) .
.PHONY: build

run:
	docker run -it --rm -p 8080:8080 --name=$(container) $(IMAGE_NAME)
.PHONY: run

push:
	docker push $(IMAGE_NAME)
.PHONY: push
