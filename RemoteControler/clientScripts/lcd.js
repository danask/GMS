           // client.js
            // var nmongoMovie = require("./nMongoDB1");
            import io from 'https://cdnjs.cloudflare.com/ajax/libs/socket.io/2.2.0/socket.io.dev.js';
            
            function lcdDisplay(dataFromtheServer) 
            {
                $("#originaldata").html(dataFromtheServer);
                        
                let data = JSON.parse(dataFromtheServer);
                    
                // console.log(data);            
                console.log(data.length);
                let header_set = false;
                let h = "";
                let v = "";    

                $.each(data, (key, theRow) => {
                    console.log(key + " , " + theRow); 
                    //let number = Math.floor((Math.random() * 2) + 0);
                    
                    if(key == 0)
                    {
                        $.each(theRow, (theKeyInTheRow, theValueInTheRow) => {

                            if(theKeyInTheRow != 'type')
                            {
                                if (!header_set)
                                    h += "<th>" + theKeyInTheRow + "</th>";


                                v += "<tr>" ;
                                v += "<td>" + theValueInTheRow + "</td>";
                                v += "</tr>" ;
                            }
                        });
                                    
                        if (!header_set) {
                            h = "<tr>" + h + "</tr>";
                            header_set = true;
                        }
                    }  
                            
                });

                // disiplayig the data
                $("#result thead > tr").remove();
                $("#result thead").append("<tr><th></th></tr>");
                $("#result tbody > tr").remove();
                $("#result tbody").append(v);
                            
            }


            var socket = io();


            // socket.on('message', function(data){
            //     $("#e1").html(data);
            //     console.log(data);
            // });
            socket.on('message', function(data){
                data = JSON.stringify(data);
                console.log(data);
                lcdDisplay(data);
                console.log("display");
            }); 

            // var dataInterval = setInterval(getData, 3000);

            // $('#clearGetData').click(()=>{
            //     clearInterval(dataInterval);
            // })     

            $(document).ready(function() {

                $("#getData").click(()=>{
                    
                    let selectedGenres = [];
                    let jsonGenres;
   
                    $.ajax({
                        url: document.URL + '5',
                        type: 'POST',
                        success: lcdDisplay
                        
                        });
                    
                });        

            });

            
                