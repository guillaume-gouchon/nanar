"use strict";
module.exports = {

    server: {
        port : 8080,

        logs: {
            console: 'debug',
            file: 'debug',
            fileName: 'node-dev.log'
        }
    },

    db: {
        name : 'nanar_dev',
        host : 'localhost'
    }

};
