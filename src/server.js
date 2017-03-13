const _ = require('lodash');
const express = require('express');
const facts = require('./facts.json');

const app = express();

app.get('/', (req, res) => {
    const fact = _.sample(facts);
    res.send({fact});
});

const port = process.env.PORT || 8080;
const server = app.listen(port, () => {
    const host = server.address().address;
    console.log(`Started at http://${host}:${port}`);
});
