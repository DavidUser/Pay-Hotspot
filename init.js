var express = require('express');
var app = express();
var bodyParser = require('body-parser');

app.use(bodyParser.json())

var macs = [];

var pos = {
  ip : '192.168.43.22',
  port : 1337
}

// Connection on wifi of client.

app.post('/macs', function (request, response) {
  var mac = request.body.mac;
  macs[macs.length] = mac;
  console.log(macs);

  var net = require('net');

  var client = new net.Socket();
  client.connect(pos.port, pos.ip, function () {
    console.log('Send MAC to ' + pos.ip + ':' + pos.port);
    client.write(mac + '\n');
    client.destroy();
  });

  response.end('ok');
});

app.listen(12345, function () {
});

