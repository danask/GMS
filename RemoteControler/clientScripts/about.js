class About extends React.Component {

  constructor () {
      super()
    }

    render() {
      return (
        // className, not class
        <div className="col-sm-12"> 

        <img src={this.props.img} alt="photo" id="aboutImage"/>
        
        <p>
        <br/>
          GMS stands for Gardening Management System. 
        </p>
        <p>
          This is an integrated solution program to provide ease
          of plant management with the environmental parameters such as temperature, humidity,
          and daily weather information.User can check the status on their web browser out and the
          system recommends an appropriate action as the situation. 
        </p>
        <p>
          This application helps people who are interested in the advanced management of gardening to monitor their plant
          more efficiently and flexibly. Moreover, this will be suitable for implementing and
          extending various programming language and network knowledge which we have learnt. 
        </p>
        <hr/>
        <p id="aboutFooter">
          Copyright CSIS4280 Project © 2019. 
          All rights reserved.  
        </p>
      </div>
    );
  }
}

ReactDOM.render(
  <About img="../resources/images/gms_main2.jpg" />,
      document.getElementById('about')
)
