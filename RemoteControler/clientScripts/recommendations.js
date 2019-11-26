
$.ajax({
    url: document.URL.replace("?", "") + 'getWater',
    type: 'POST',
    success: function(response){
        console.log("React " + response);
        let data = JSON.parse(response);

        const recommendationList = [
            {id: 1, name: "Amount of water (liters)", value: (data[0].liters*1).toFixed(2)}, 
            {id: 2, name: "Watering time (min)", value: (((data[0].liters))*60).toFixed(2)}, 
            {id: 3, name: "Sprinkler cycles", value: ((data[0].liters)*360).toFixed(2)}
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
