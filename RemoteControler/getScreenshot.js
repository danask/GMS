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
  
  class CapturedImage extends React.Component {
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
  
    render() {
      return (
        <div>
          <img src={ this.state.lastImage } alt="flag" width="200px"height="150px"/>
        </div>
      );
    }
  }
//   Hello a<br />
//   <img src={ this.state.watering } alt="flag" />
//   <br />
  ReactDOM.render(
    <CapturedImage name="img" />,
        document.getElementById('app')
  )