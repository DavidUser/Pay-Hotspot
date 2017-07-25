var net = require('net');

var server = net.Server();

server.listen(1337, '192.168.43.42');

server.on('listening', function() {
  console.log('Listening...');
});

server.on('connection', function(socket) {
  console.log('Connection established.');
  socket.on('data', function(data) {
    console.log('Received: ' + data);
    socket.write('Tudo certo!\n');
    socket.destroy();
  });
});

