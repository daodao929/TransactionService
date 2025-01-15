#!/usr/bin/env bash

docker build -t transactionservice .
docker run -d -p 8080:8080 --name transactionservice transactionservice