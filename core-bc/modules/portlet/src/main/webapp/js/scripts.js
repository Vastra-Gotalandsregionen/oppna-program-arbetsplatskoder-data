if(console)
  console.log('Hello world!');


function loadDoc() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        console.log(this.responseText);
    }
  };
  xhttp.open("GET", "find/Agarform", true);
  xhttp.send();
}