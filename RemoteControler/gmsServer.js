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
	change_streams_sensors = dbo.collection('sensors').watch();
});

io.on('connection', function (socket) {
	var output = [];
	var alramOutput = [];
	console.log('Connection!');
	
	change_streams.on('change', function(change){
		console.log(JSON.stringify(change));

		var query = {} ; 
		dbo.collection("status").find(query, {projection: {_id:0, type:1, firstLine:1, secondLine:1}}).
					toArray((err, result) => {
				if (err) throw err;
			
				console.log(result);
				console.log(result.length);
				output = result;
				
				// response.send(JSON.stringify(result));

				// db.close();	
				socket.emit('lcdMessage', output);//{ message: 'test'});	
			});
			
		console.log(output);
	});
	
	
	change_streams_sensors.on('change', function(change){
// 		console.log(JSON.stringify(change));
	    dbo.collection("sensors").find({key:"rfid"}, {projection: {_id:0, status:1}}).
				toArray((err, result) => {
			if (err) throw err;
		
			console.log(result);
			console.log(result.length);
			alramOutput = result;
			
			// response.send(JSON.stringify(result));

			// db.close();	
			socket.emit('alarmStatus', alramOutput);//{ message: 'test'});	
		});
	    console.log(output);
	    dbo.collection("sensors").find({key:"motor"}, {projection: {_id:0, status:1}}).
				toArray((err, result) => {
			if (err) throw err;
		
			console.log("motorStatus " + result);
			console.log(result.length);
			motorOutput = result;

			// db.close();	
			socket.emit('motorStatus', motorOutput);//{ message: 'test'});	
		});
  	});


  	///////////////////////////
  	
var counter = 0;
var users = [];  	
  	

   
   // ----------------------
   socket.on('init', function(name) {
     console.log('A user connected');
      //user.socket = socket.id;
      console.log(name + ", " + socket.id);
      users[name] = socket.id;
      
        for(var key in users)
            console.log(key, ": ", users[key]);
        return users[key];
   });
   // ----------------------

   //Whenever someone disconnects this piece of code executed
   socket.on('disconnect', function () {
    //  console.log('A user disconnected');
      clearInterval(myInterval);
      
      for(var key in users)
        delete users[key];
   });
   
   //Send a message after a timeout of 2 seconds
   setTimeout(function() {
      counter++ ;
      //console.log("sending count : " + counter);
      socket.send('Sending a notice - 2 seconds after connection! counter = ' + counter);
      
      //Sending an object when emmiting an event
      socket.emit('e1', { description1: 'A custom event named e1! counter = ' + counter});
     
      socket.emit('e2', { description2: 'A custom event named e2! counter = ' + counter});
   }, 2000);
   
   var myInterval = setInterval(function() { 
       var d = new Date();
       let t = d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
       //console.log(t);
       socket.emit('e3', { mytime: 'A custom event named e3! time = ' + t});
       
   }, 1000);
   
  // waiting for the client browser to send something
  // 
  socket.on('rmsg', function (arg1, arg2, to) {
    console.log('message Tag ', arg1, ' saying ', arg2, 'to', to);
   //  socket.broadcast.emit('rmsg',{m : 'message (Tag: ' + arg1 + ', saying: ' + arg2 + ')'});
  socket.broadcast.emit('rmsg', {tag: arg1, m : arg2});
//   socket.to(users[to]).emit('rmsg', {tag: arg1, m : arg2});
//   socket.to(users[to]).emit('rmsg', {m : ' just for you - message (Tag: ' 
//                             + arg1 + ', saying: ' + arg2 + ')'});                            
   console.log(to + ", " + users[to]);
  });
   	
  	
  //console.log('user connected')

  //socket.on('join', function(userNickname) {

  //        console.log(userNickname +" : has joined the chat "  );

  //        socket.broadcast.emit('userjoinedthechat',userNickname +" : has joined the chat ");
  //    });


  //socket.on('messagedetection', (senderNickname,messageContent) => {
         
  //       //log the message in console 

  //       console.log(senderNickname+" :" +messageContent)
  //        //create a message object 
  //       let  message = {"message":messageContent, "senderNickname":senderNickname}
  //          // send the message to the client side  
  //       io.emit('message', message );
       
  //      });
        
    
  // socket.on('disconnect', function() {
  //    console.log( ' user has left ')
  //    socket.broadcast.emit("userdisconnect"," user has left ");
  // });
  	
});


app.post('/getWater', (req, res) =>{
	nmongoGMS.getWater(req, res); 
});

app.get('/getStatus', (req, res) =>{
	nmongoGMS.getStatus(req, res);  
});

app.post('/getStatus', (req, res) =>{
	nmongoGMS.getStatus(req, res);  
});

app.post('/dismissAlarm', (req, res) =>{
	nmongoGMS.sendPasscode(req, res); 
    console.log("dismissAlarm: " + req.body.retry);
	if(req.body.retry == 'no')
		nmongoGMS.updateHistory(req, res);
});

app.post('/updateMotor', (req, res) =>{
	nmongoGMS.updateMotor(req, res); 
    console.log("updateMotor: " + req.body.retry);
	if(req.body.retry == 'no')
		nmongoGMS.updateHistory(req, res);
});

app.post('/sendMessage', (req, res) =>{
	nmongoGMS.sendMessage(req, res);  
    console.log("sendMessage: " + req.body.retry);
	if(req.body.retry == 'no')
		nmongoGMS.updateHistory(req, res);
});

app.get('/getHistory', (req, res) =>{
	nmongoGMS.getHistory(req, res);  
});

app.post('/getHistory', (req, res) =>{
	nmongoGMS.getHistory(req, res);  
});


app.post('/updateHistory', (req, res) =>{
	nmongoGMS.updateHistory(req, res);  
});


app.get('/getUser', (req, res) =>{
	nmongoGMS.getUser(req, res);  
});

app.post('/getUser', (req, res) =>{
	nmongoGMS.getUser(req, res);  
});

app.post('/updateUser', (req, res) =>{
	nmongoGMS.updateUser(req, res);  
});


app.post('/getWeather', (req, res) =>{
	nmongoGMS.getWeather(req, res);  
});

app.get('/getAlarmStatus', (req, res) =>{
	nmongoGMS.getAlarmStatus(req, res);  
});

app.get('/getMotorStatus', (req, res) =>{
	nmongoGMS.getMotorStatus(req, res);  
});

app.post('/getAlarmStatus', (req, res) =>{
	nmongoGMS.getAlarmStatus(req, res);  
});

app.post('/getMotorStatus', (req, res) =>{
	nmongoGMS.getMotorStatus(req, res);  
});

////////////////////////////////


