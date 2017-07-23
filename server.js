var express = require('express');
var app = express();

// serve static content (images, css, javascript, ...)
app.use(express.static('public'));

// treat get request to index.html
app.get('/index.htm', function (requisition, response) {
	// respond with a HTML file
	response.sendFile( __dirname + "/index.htm");
});

// treat get requisition to process_get, it is included on html form
app.post('/process_get', function (require, response) {
	// Prepare output in JSON format
	// GET method keep requisition data on query (it's the own URL)
	responseJSON = {
		first_name:require.query.first_name,
		last_name:require.query.last_name
	};
	console.log(responseJSON);
	response.end(JSON.stringify(responseJSON));
});

// module to easy read post data on body of message
var bodyParser = require('body-parser');
// Create application/x-www-form-urlencoded parser
var urlencodedParser = bodyParser.urlencoded({ extended: false });

// treat get requisition to process_get, it is included on html form
// use urlencodedParser to read post data
app.post('/process_post', urlencodedParser, function (require, response) {
	// Prepare output in JSON format
	// POST method keep resquisition data on body (hidded)
	responseJSON = {
		first_name:require.body.first_name,
		last_name:require.body.last_name
	};
	console.log(responseJSON);
	response.end(JSON.stringify(responseJSON));
});

var fs = require('fs');
var multer = require('multer');
app.use(bodyParser.urlencoded({ extended: true}));
var upload = multer({
	dest: '/tmp/'
});
app.use(upload);

app.post('/file_upload', function (request, response) {
	console.log(request.files.file.name);
	console.log(request.files.file.path);
	console.log(request.files.file.type);

	var file = __dirname + "/" + request.files.file.name;
	fs.readFile( request.files.file.path, function (err, data) {
		fs.writeFile(file, data, function (err) {
			if (err) {
				console.log(err);
			} else {
				responseJSON = {
					message:'File uploaded sucessfully',
					filename:request.files.file.name
				};
			}
			console.log(responseJSON);
			response.end(JSON.stringify( responseJSON ));
		});
	});

});

var cookieParser = require('cookie-parser');
app.use(cookieParser());

function cookiesCapture(req, res) {
	console.log("Cookies: ", req.cookies);
}

// treat get request
app.get('/', function (requisition, response) {
	console.log("Got a GET request for the homepage");
	response.send('Hello GET');
	cookiesCapture(requisition, response);
});

// treat post request
app.post('/', function (requisition, response) {
	console.log("Got a POST request for the homepage");
	response.send('Hello POST');
});

// threat delete request to user
app.delete('/del_user', function (requisition, response) {
	console.log("Got a DELETE request for the /del_user");
	response.send('Hello DELETE');
});

// threat get request to list
app.get('/list_user', function (requisition, response) {
	console.log("Got a GET request for the /list_user");
	response.send('Page Listing');
});

// threat get request to pattern
app.get('/ab*cd', function (requisition, response) {
	console.log("Got a GET request for the /ab*cd");
	response.send('Page Pattern Match');
});

// listen connections
var server = app.listen(8081, function () {
	var host = server.address().address;
	var port = server.address().port;
	console.log("Example app listening at http://%s:%s", host, port);
});
