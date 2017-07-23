var net = require('net');

var client = new net.Socket();
client.connect(1337, '127.0.0.1', function () {
  console.log('Connected');
  client.write("I'm client.");
});

client.on('data', function(data) {
  console.log('Received: ' + data);
  client.destroy();
});
