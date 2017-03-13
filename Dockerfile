FROM node:7-alpine

ENV NODE_ENV $NODE_ENV

WORKDIR /usr/src/app
COPY . /usr/src/app

CMD [ "npm", "start" ]

EXPOSE 8080
