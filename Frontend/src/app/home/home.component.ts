import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    this.imageModal();
  }
  public imageModal()
  {

    // Get the modal
    var modal = document.getElementById("myModal");

    // Get the image and insert it inside the modal - use its "alt" text as a caption
    var img = document.getElementById("myImg") as HTMLImageElement;
    var modalImg = document.getElementById("img01") as HTMLImageElement;
    var captionText = document.getElementById("caption") as HTMLImageElement;

    img.onclick = function(){
      modal.style.display = "block";
      modalImg.src = img.src;
      captionText.innerHTML = img.alt;
    }

    var span = document.getElementsByClassName("close")[0] as HTMLImageElement;
    span.onclick = function(){
      modal.style.display = "none";
    } 
  }
}
