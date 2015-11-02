/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
var httpProxy = require('http-proxy');
var fs = require('fs');
var path = require('path');

var proxy = new httpProxy.RoutingProxy();

exports.requestProxy = function (req, res) {

    // Buffer requests so that eventing and async methods still work
    // https://github.com/nodejitsu/node-http-proxy#post-requests-and-buffering
    var buffer = httpProxy.buffer(req);
    console.log('Proxying Request "' + req.path + '"');

    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

    proxy.proxyRequest(req, res, {
        host: 'localhost',
        port: 8993,
        buffer: buffer,
        changeOrigin: true,
        secure: false,
        target: {
            https: true
        }
    });
};

exports.mockRequest = function (filename) {
    var join = path.join('src/test/resources', filename);
    return function (req, res) {
        if (process.env.SAUCE_ACCESS_KEY && filename === 'config.json') {
            // Disable the large single image map tile due to limited bandwidth over
            // Sauce Connect tunnel
            fs.readFile(join, function (err, data) {
                var config = JSON.parse(data);
                config.imageryProviders[0].url = 'http://localhost:8888/images/noimage.png';
                res.json(config);
            });
        } else {
            res.sendfile(join);
        }
    };
}
