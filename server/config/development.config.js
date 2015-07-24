"use strict";
module.exports = {

    server: {
        port : 7000,

        logs: {
            console: 'debug',
            file: 'debug',
            fileName: 'node-dev.log'
        }
    },

    db: {
        name : 'nanar_dev',
        host : 'localhost:27017'
    }

};
