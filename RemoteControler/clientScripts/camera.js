// Initialize Firebase
const config = {
    apiKey: "AIzaSyBz-lmAUl1dH3a2qmjUv73rpYphz7HmNTI",
    authDomain: "gms-rasp.firebaseapp.com",
    databaseURL: "https://gms-rasp.firebaseio.com",
    storageBucket: "gms-rasp.appspot.com",
    messagingSenderId: "840201077359"
  }
  
  
  firebase.initializeApp(config)
  const storage = firebase.storage().ref()
  
  class Camera extends React.Component {
    constructor () {
      super()
      this.state = {
        // watering: '',
        lastImage: ''
      }
      
    //   this.getImage('watering')
      this.getImage('lastImage')//2019-07-09 00:13:40.180848')
    }
            //  function showimage() {
        //     var storageRef = firebase.storage().ref();
        //     var spaceRef = storageRef.child('sweet_gift/vytcdc.png');
        //     storageRef.child('sweet_gift/vytcdc.png').getDownloadURL().then(function(url) {
        //         var test = url;
        //         alert(url);
        //         document.querySelector('img').src = test;

        //     }).catch(function(error) {

        //     });
        // }

        // "CapturedImages/2019-07-09 00:13:40.180848.jpg"
    getImage (image) {
      let { state } = this
    //   storage.child(`CapturedImages/${image}.png`).getDownloadURL().then((url) => {
      storage.child("LastImage/lastImage.jpg").getDownloadURL().then((url) => {
        console.log("1.img: " + image)
        console.log("2.url: "+ url)

        state[image] = url
        this.setState(state)
      }).catch((error) => {
        // Handle any errors
      })
    }

    componentDidMount= function(){

      var modal = document.getElementById("myModal");
      var modalImg = document.getElementById("img01");

      document.getElementById("capturedImg").onclick = function(){
        modal.style.display = "block";
        modalImg.src = this.src;
      }

      document.getElementsByClassName("close")[0].onclick = function() { 
        modal.style.display = "none";
      }
    }
  
    render() {
      return (
        <div>
          <img id="capturedImg" src={ this.state.lastImage } alt="Captured Image" width="200px"height="150px"/>
          <div id="myModal" className="modal">
             <span className="close">&times;</span>
             <img className="modal-content" id="img01"/>
          </div>
        </div>
      );
    }
  }




  ReactDOM.render(
    <Camera name="img" />,
        document.getElementById('app')
  )




  
  // var ProductComponent = React.createClass({
  // componentDidMount: function() {
  //   this.renderBuyButton();
  // },
  // componentDidUpdate: function() {
  //   this.renderBuyButton();
  // },
  // render: function(props) {
  //   /* we need to keep a ref to the 
  //   * button-container so we can update it with jQuery
  //   */
  //   return (
  //     <li>
  //       {this.props.product.name}
  //       <span className="button-container" 
  //         ref="buttonContainer"></span>
  //     </li>
  //   );
  // },
  // renderBuyButton: function() {
  //   // render the buy button with jQuery
  //   $(this.refs.buttonContainer).html(
  //     buyButtonJquery(this.props.product)
  //   );
  // }
  // });
