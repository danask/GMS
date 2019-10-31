var express = require('express');
var bodyParser = require('body-parser');
var app = express();
// var fs = require("fs");

var nmongoGMS = require("./serverScripts/nGMSDB");

var port = 8000 ;

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(express.static(__dirname));


var http = require('http').createServer(app);
var io = require('socket.io')(http);


// var server = app.listen(port, () => {
//     console.log('server is listening on port', server.address().port)
// })

http.listen(port, function() {
   console.log('listening on *:8000');
});


var mongoClient = require('mongodb').MongoClient;

var url = "mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority";



var dbo;
var change_streams;

var mg = mongoClient.connect(url, {
				useNewUrlParser: true,
				useUnifiedTopology: true
			}, function(err, db) {
	if (err) throw err;
	dbo = db.db("gms_data");
	console.log('mongo conn!');

	change_streams = dbo.collection('status').watch();
});

io.on('connection', function (socket) {
	var output= [];
	console.log('Connection!');
	
	change_streams.on('change', function(change){
		console.log(JSON.stringify(change));


        // status.find({}, (err, data) => {
        //   if (err) throw err;
    
        //   if (data) {
        //       // RESEND ALL USERS
        //     //   socket.emit('status', data);
		//   }
		var query = {} ; 
		dbo.collection("status").find(query, {projection: {_id:0, type:1, firstLine:1, secondLine:1}}).
					toArray((err, result) => {
				if (err) throw err;
			
				console.log(result);
				console.log(result.length);
				output = result;
				
				// response.send(JSON.stringify(result));

				// db.close();	
				socket.emit('message', output);//{ message: 'test'});	
			});
			
		console.log(output);
		// socket.send('messageDisp');
		// socket.emit('message', output);//{ message: 'test'});			
		
  	});
});

// app.post('/:code', (req, res) =>{
//     console.log("code: " + req.params.code);


// 	// if (req.params.code == 1)
// 	// 	nmongoGMS.getWater(req, res); 
// 	// else if (req.params.code == 2)
// 	// 	nmongoGMS.sendPasscode(req, res); 
// 	// else if (req.params.code == 5)
// 	// {
//     //     console.log("data param: " + req.body.param);
// 	// 	nmongoGMS.getStatus(req, res);  
// 	// }
// 	// else if (req.params.code == 6)
// 	// {
// 	//     console.log("data param: " + req.body.cycle);
// 	// 	nmongoGMS.updateMotor(req, res);  		
// 	// }
// 	// else if (req.params.code == 7)
// 	// {
// 	// 	nmongoGMS.sendMessage(req, res);  		
// 	// }
	
// });

app.post('/getWater', (req, res) =>{
	nmongoGMS.getWater(req, res); 
});

app.get('/getStatus', (req, res) =>{
	nmongoGMS.getStatus(req, res);  
});

app.post('/dismissAlarm', (req, res) =>{
	nmongoGMS.sendPasscode(req, res);  
});

app.post('/updateMotor', (req, res) =>{
	nmongoGMS.updateMotor(req, res);  
});

app.post('/sendMessage', (req, res) =>{
	nmongoGMS.sendMessage(req, res);  
});

app.get('/getHistory', (req, res) =>{
	nmongoGMS.getHistory(req, res);  
});

app.post('/retryAction', (req, res) =>{
	nmongoGMS.retryAction(req, res);  
});


////////////////////////////////


