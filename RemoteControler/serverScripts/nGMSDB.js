
var requestForWeather = require('request');

var mongoClient = require('mongodb').MongoClient;

var url = "mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority";


function getStatus(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      var dbo = db.db("gms_data");
      var myquery = {type: "tempHumid"};
      
      var collection = dbo.collection("status");
      
      collection.find(myquery, {projection: {_id:0, firstLine:1,secondLine:1, type:1}}).
                              toArray((err, result) => {
                        if (err) throw err;
                       
                        console.log(result);
                        console.log(result.length);
                        
                        response.send(JSON.stringify(result));
                        
                        db.close();
      });
  });
}

function getWater(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      var dbo = db.db("gms_data");
      var query = {} ;
      
      var collection = dbo.collection("status");
      
      collection.find(query, {projection: {_id:0, type:1, liters:1}}).
                              toArray((err, result) => {
                        if (err) throw err;
                       
                        console.log(result);
                        console.log(result.length);
                        
                        response.send(JSON.stringify(result));
                        
                        db.close();
      });
  });
}

function updateMotor(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let cycleParam = request.body.cycle;
      let statusParam = request.body.status;
      let newvalues;
      
      console.log("request.body.cycle: " + cycleParam);
      console.log("request.body.status: " + statusParam);
      var dbo = db.db("gms_data");

      var myquery = { key: "motor" };
      
      if(cycleParam != "")
        newvalues = { $set: {cycle: cycleParam, status: statusParam } };
      else
        newvalues = { $set: {status: statusParam } };
      
      dbo.collection("sensors").updateOne(myquery, newvalues, function(err, res) {
                    if (err) throw err;
                    console.log("1 document updated");
                    
                    response.send(JSON.stringify({result: "ok"}));
                    
                    db.close();
      });
  });
}

function sendPasscode(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let passcodeParam = request.body.passcode;
      let newvalues;
      console.log("request.body.passcode: " + passcodeParam);
      var dbo = db.db("gms_data");

      var myquery = { key: "rfid" };

      newvalues = { $set: {value: passcodeParam } };
      
      dbo.collection("sensors").updateOne(myquery, newvalues, function(err, res) {
                    if (err) throw err;
                    console.log("1 document updated");
                    
                    response.send(JSON.stringify({result: "ok"}));
                    
                    db.close();
      });
  });
}


function sendMessage(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let firstLine = request.body.firstLine;
      let secondLine = request.body.secondLine;
      let duration = request.body.duration;
      let newvalues;
      
      console.log("request.body.firstLine: " + firstLine);
      console.log("request.body.secondLine: " + secondLine);
      var dbo = db.db("gms_data");

      var myquery = {};
      
      if(firstLine != "" || secondLine != "")
      {
        newvalues = { $set: {firstLine: firstLine, secondLine: secondLine, duration: duration } };     
        dbo.collection("status").updateMany(myquery, newvalues, function(err, res) {
                      if (err) throw err;
                      console.log("1 document updated");
                      
                      response.send(JSON.stringify({result: "ok"}));
                      
                      db.close();
       });
      }
      else{
        response.send(JSON.stringify({result: "Nothing to update"}));
      }
  });
}


function getHistory(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      var dbo = db.db("gms_data");
      var query = {} ;
      var mysort = { time: -1 };
      var collection = dbo.collection("action_history");
      
      collection.find(query, {projection: {_id:0, category:1, type:1, param1:1, param2:1, param3:1, date:1}}).
                      sort(mysort).limit(10).toArray((err, result) => {
                        if (err) throw err;
                       
                        // console.log(result);
                        console.log(result.length);
                        
                        response.send(JSON.stringify(result));
                        
                        db.close();
      });
  });
}

function updateHistory(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let category = request.body.category;
      let type = request.body.type;
      let param1 = request.body.param1;
      let param2 = request.body.param2;
      let param3 = request.body.param3;
      let date = new Date().getFullYear()+"-"+ (new Date().getMonth()+1)+"-"+new Date().getDate();
      let time = new Date().getTime();

      let dbo = db.db("gms_data");

      let newvalues = { category:category, 
                    type:type, 
                    param1:param1, 
                    param2:param2,
                    param3:param3,
                    date:date,
                    time:time
                 };
      
      dbo.collection("action_history").insertOne(newvalues, function(err, res) {
                    if (err) throw err;
                    console.log("updateHistory: 1 document updated");
                   
                    db.close();
      });
  });
}

function getUser(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let dbo = db.db("gms_data");
      let email = request.body.email;
      let password = request.body.password;
      
      console.log(email);
      let myquery = {email: email, password:password};
      
      let collection = dbo.collection("users");
      
      collection.find(myquery, {projection: {_id:0, name:1, email:1}}).
                              toArray((err, result) => {
                        if (err) throw err;
                       
                        console.log(result);
                        console.log(result.length);
                        
                        response.send(JSON.stringify(result));
                        
                        db.close();
      });
  });
}

function getWeather(request, response)
{
  let url = "http://api.openweathermap.org/data/2.5/weather?q=Vancouver&units=metric&APPID=1b5fcb8df3906c5092aca2b51707953b";
  let result;
  
  requestForWeather(url, function (err, resp, body) {
    if(err){
      console.log('error:', error);
    } else {
      console.log('body:', body);
      response.send(body);
    }
  });
  // var result = {"coord":{"lon":-123.12,"lat":49.26},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"base":"stations","main":{"temp":6.58,"pressure":1011,"humidity":87,"temp_min":6.67,"temp_max":10},"visibility":24140,"wind":{"speed":6.2,"deg":300},"rain":{"1h":0.25},"clouds":{"all":90},"dt":1574188053,"sys":{"type":1,"id":954,"country":"CA","sunrise":1574177344,"sunset":1574209618},"timezone":-28800,"id":6173331,"name":"Vancouver","cod":200};
  // response.send(result);
}


function getAlarmStatus(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let dbo = db.db("gms_data");
	    dbo.collection("sensors").find({key:"rfid"}, {projection: {_id:0, status:1}}).
				toArray((err, result) => {
			if (err) throw err;
		
			console.log("alaramStatus " + result);
			console.log(result.length);
			
			response.send(JSON.stringify(result));
			db.close();	
		});
  });  
}


function getMotorStatus(request, response)
{
  mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      
      let dbo = db.db("gms_data");
	    dbo.collection("sensors").find({key:"motor"}, {projection: {_id:0, status:1}}).
				toArray((err, result) => {
			if (err) throw err;
		
			console.log("alaramStatus " + result);
			console.log(result.length);
			
			response.send(JSON.stringify(result));
			db.close();	
		});
  });   
}

exports.getMotorStatus = getMotorStatus
exports.getAlarmStatus = getAlarmStatus;
exports.getWeather = getWeather;
exports.getUser = getUser;
exports.getHistory = getHistory;
exports.getStatus = getStatus;
exports.getWater = getWater;
exports.updateHistory = updateHistory;
exports.updateMotor = updateMotor;
exports.sendPasscode = sendPasscode;
exports.sendMessage = sendMessage;
