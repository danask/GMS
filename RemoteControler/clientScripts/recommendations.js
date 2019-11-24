// reference on map: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/map

// var mongoClient = require('mongodb').MongoClient;

// var url = "mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority";

// var dbo;
// var change_streams;
// var literValueFromDB = 0;

// var mg = mongoClient.connect(url, {
// 				useNewUrlParser: true,
// 				useUnifiedTopology: true
// 			}, function(err, db) {
// 	if (err) throw err;
// 	dbo = db.db("gms_data");
// 	console.log('mongo conn!');

//     change_streams = dbo.collection('status').watch();
    
//     change_streams.on('change', function(change){
// 		console.log(JSON.stringify(change));
// 		var query = {} ; 
// 		dbo.collection("status").find(query, {projection: {_id:0, type:1, firstLine:1, secondLine:1, liters:1}}).
// 					toArray((err, result) => {
// 				if (err) throw err;
			
// 				console.log(result);
// 				console.log(result.length);
// 				// output = result;
//                 literValueFromDB = result;

// 				// socket.emit('message', output);//{ message: 'test'});	
// 			});
			
// 		console.log(literValueFromDB);
//   	});
// });

// io.on('connection', function (socket) {
// 	var output= [];
// 	console.log('Connection!');
	
// 	change_streams.on('change', function(change){
// 		console.log(JSON.stringify(change));
// 		var query = {} ; 
// 		dbo.collection("status").find(query, {projection: {_id:0, type:1, firstLine:1, secondLine:1, liters:1}}).
// 					toArray((err, result) => {
// 				if (err) throw err;
			
// 				console.log(result);
// 				console.log(result.length);
// 				output = result;

// 				socket.emit('message', output);//{ message: 'test'});	
// 			});
			
// 		console.log(output);
//   	});
// });

$.ajax({
    url: document.URL.replace("?", "") + 'getWater',
    type: 'POST',
    success: function(response){
        console.log("React " + response);
        let data = JSON.parse(response);

        const recommendationList = [
            {id: 1, name: "Amount of water (liters)", value: data[0].liters}, 
            {id: 2, name: "Watering time (min)", value: (Math.ceil(data[0].liters))*60}, 
            {id: 3, name: "Sprinkler cycles", value: Math.ceil(data[0].liters)*360}
        ];
        
        const App = ({wateringFactorList}) => (
            <ul>
                {wateringFactorList.map(factor => 
                    <li key={factor.id}>{factor.name}: <b>{factor.value}</b></li>    
                )}
            </ul>
        )
                    
        ReactDOM.render(
            <App wateringFactorList={recommendationList}/>,
                document.getElementById("recommendation")
        );
    }            
});
