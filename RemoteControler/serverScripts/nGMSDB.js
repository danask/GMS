// var express = require('express')
// var app = express();
// var http = require('http').createServer(app);
// var io = require('socket.io')(http);

var mongoClient = require('mongodb').MongoClient;

var url = "mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority";

// (function getUpdate()
// {
//   mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
    
//     if (err) throw err;
//     var dbo = db.db("gms_data");

//     let change_streams = dbo.collection('status').watch();

//     io.on('connection', function (socket) {
//       console.log('Connection!');
      
//       change_streams.on('change', function(change){
//         console.log(JSON.stringify(change));


//         status.find({}, (err, data) => {
//           if (err) throw err;
    
//           if (data) {
//               // RESEND ALL USERS
//               socket.emit('status', data);
//           }
//       });
//     });

//   });

//   });
// })();


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
      var collection = dbo.collection("history");
      
      collection.find(query, {projection: {_id:0, category:1, type:1, param1:1, param2:1, param3:1, time:1}}).
                      sort(mysort).limit(10).toArray((err, result) => {
                        if (err) throw err;
                       
                        console.log(result);
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
      let time = new Date().getFullYear()+"-"+ (new Date().getMonth()+1)+"-"+new Date().getDate();

      let dbo = db.db("gms_data");

      let newvalues = { category:category, 
                    type:type, 
                    param1:param1, 
                    param2:param2,
                    param3:param3,
                    time:time
                 };
      
      dbo.collection("history").insertOne(newvalues, function(err, res) {
                    if (err) throw err;
                    console.log("1 document updated");
                   
                    db.close();
      });
  });
}


// function getMovieData(request, response)
// {
//   var selectedGenresParam = request.body.param;
//   var selectedGenres = [];
//   selectedGenres = JSON.parse(selectedGenresParam);  

//   console.log("param in getMovieData:" + selectedGenres);
  
//   mongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
//       if (err) throw err;
      
//       var dbo = db.db("sample_mflix");
//       var query = {} ;
      
//       var collection = dbo.collection("movies");
      
//       collection.find(query, {projection: {_id:0, title:1,genres:1,poster:1,year:1}}).
//                               limit(20000).toArray((err, result) => {
//                         if (err) throw err;
//                         // console.log(result);
//                         console.log(result.length);
//                         console.log(selectedGenres.length);
                        
//                         var mydata = [];
//                         var count = 0;
                        
//                         for (let i = 0 ; i < result.length; i++) {
                            
//                             let currentGenre ="";
                            
//                             if(result[i].genres != null)
//                               currentGenre= result[i].genres.toString();
                            
//                             if(currentGenre != "")
//                             {
//                               for(let j = 0; j < selectedGenres.length; j++)
//                               {
//                                   if(currentGenre.
//                                     indexOf(selectedGenres[j].toString()) !== -1)
//                                   {
//                                     // console.log(result[i].genres + " vs " + selectedGenres[j])
                                    
//                                     // make JSON
//                                     var object = {};
//                                     object['genre'] = result[i].genres;
//                                     object['title'] = result[i].title;
//                                     object['poster'] = "No image";
                                    
//                                     if(result[i].poster != null)
//                                       object['poster'] = result[i].poster;
                                      
//                                     object['year'] = result[i].year;
                                    
//                                     mydata.push(object);
//                                     count++;
//                                     break;  // to add only once
//                                   }
//                               }
//                             }
  
//                             if(count == 50)
//                               break;
//                         }
//                         // console.log(mydata);
                        

//                         response.send(JSON.stringify(mydata));
                        
//                         db.close();
//       });
//     });
// }


exports.getHistory = getHistory;
exports.getStatus = getStatus;
exports.getWater = getWater;
exports.updateHistory = updateHistory;
exports.updateMotor = updateMotor;
exports.sendPasscode = sendPasscode;
exports.sendMessage = sendMessage;