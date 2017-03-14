const server = require('../src/server');
const chai = require('chai');
const chaiHttp = require('chai-http');

chai.use(chaiHttp);
const {expect} = chai;

describe('GET /', () => {
    it('should generate a fact', (done) => {
        chai.request(server)
            .get('/')
            .end((err, res) => {
                expect(err).to.be.null;
                expect(res).to.have.status(200);
                expect(res).to.be.json;
                expect(res.body).to.have.property('fact');
                expect(res.body.fact).to.have.string('Chuck');
                done();
            });
    });
});
