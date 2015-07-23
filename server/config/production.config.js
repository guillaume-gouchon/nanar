"use strict";
module.exports = {

    server: {
        port : 7000,

        logs: {
            console: 'warning',
            file: 'debug',
            fileName: 'node-prod.log'
        }
    },

    db: {
        name : 'nanar_prod',
        host : 'nanarmongo'
    }

};
