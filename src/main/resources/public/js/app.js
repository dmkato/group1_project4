var gameModel;
var didPressScan;
var didPressPlaceShip;
var didPressRotate = "horizontal";

/* On page ready.. */
$( document ).ready(function() {
    alert("INSTRUCTIONS: \n Press 'Place Ship' button at bottom\n Choose the ship to place, dispayed under 'computer ships remaining.' **Note: Must choose smallest ship first.\n Press 'Rotate Ship' to choose between Horizontal and Vertical. **Note: Default is Horizontal.\n Press 'Fire' to fire");
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
    gameModel = json;
    console.log( "JSON Data: " + json );
  });

  // Create gameBoards
  createGameBoards();
  SetUpShipStatus();
});

/* Places Ship based on buttons that no longer exist */
function placeShip(ship, x, y, orientation) {
    //if()

  //var menuId = $( "ul.nav" ).first().attr( "id" );
  var request = $.ajax({
    url: "/placeShip/"+ship+"/"+x+"/"+y+"/"+orientation,
    method: "post",
    data: JSON.stringify(gameModel),
    contentType: "application/json; charset=utf-8",
    dataType: "json"
  });

  request.done(function( currModel ) {
    displayGameState(currModel);
    gameModel = currModel;

  });

  request.fail(function( jqXHR, textStatus ) {
    alert( "Request failed: " + textStatus );
  });

}

/* Fires at coordinates x, y */
function fire(x, y){

   var lasergun = new Audio('../../../css/sounds/laser.m4a');

   lasergun.play();

  console.log(x);
  console.log(y);
  //var menuId = $( "ul.nav" ).first().attr( "id" );
  var request = $.ajax({
    url: "/fire/"+x+"/"+y,
    method: "post",
    data: JSON.stringify(gameModel),
    contentType: "application/json; charset=utf-8",
    dataType: "json"
  });

  //check if player has missed there yet
  for (var i = 0; i < gameModel.computerMisses.length; i++) {
    if(gameModel.computerMisses[i].Across == x && gameModel.computerMisses[i].Down == y){
      console.log("made it into conditional 1");
      $('footer #status').text("You have already fired at " + x + ", " + y);
      return;
    }
  }
  //check if player has hit there yet
    for (var i = 0; i < gameModel.computerHits.length; i++) {
      if(gameModel.computerHits[i].Across == x && gameModel.computerHits[i].Down == y){
        console.log("made it into conditional 2");
        $('footer #status').text("You have already fired at " + x + ", " + y);
        return;
      }
    }

  request.done(function( currModel ) {
    displayGameState(currModel);
    gameModel = currModel;

  });

  request.fail(function( jqXHR, textStatus ) {
    alert( "Request failed: " + textStatus );
  });

  $('footer #status').text("Fired at " + x + ", " + y);

}

/* Scans around coordinates x, y */
function scan(x, y){
  console.log(x);
  console.log(y);
  //var menuId = $( "ul.nav" ).first().attr( "id" );
  var request = $.ajax({
    url: "/scan/"+x+"/"+y,
    method: "post",
    data: JSON.stringify(gameModel),
    contentType: "application/json; charset=utf-8",
    dataType: "json"
  });

  request.done(function( currModel ) {
    displayGameState(currModel);
    gameModel = currModel;

  });

  request.fail(function( jqXHR, textStatus ) {
    alert( "Request failed: " + textStatus );
  });

}

/* Logs to console */
function log(logContents){
  console.log(logContents);
}

/* Updates view */
function displayGameState(gameModel){

  $( '#MyBoard td'  ).css("background-image", "images/rickhead.jpg");
  $( '#TheirBoard td'  ).css("background-image", "images/mortyhead.png");

  if(didPressScan) {
    if(gameModel.scanResult){
      $('footer #status').text("Scan found at least one Ship");
    } else {
      $('footer #status').text("Scan found no Ships");
    }
  }

  displayShip(gameModel.aircraftCarrier);
  displayShip(gameModel.battleship);
  displayShip(gameModel.cruiser);
  displayShip(gameModel.destroyer);
  displayShip(gameModel.submarine);


  for (var i = 0; i < gameModel.computerMisses.length; i++) {
    $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-image", "url(../../../css/images/rickhead.png)");

  }
  for (var i = 0; i < gameModel.computerHits.length; i++) {
    $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-image", "url(../../../css/images/mortyhead.png");
    //snd = new Audio('../../../css/sounds/oh_man.wav');
  }

  for (var i = 0; i < gameModel.playerMisses.length; i++) {
    $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-image", "url(../../../css/images/rickhead.png)");
  }
  for (var i = 0; i < gameModel.playerHits.length; i++) {
    $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-image", "url(../../../css/images/mortyhead.png");
  }


  //snd.play();

}

/* Displays ship on MyBoard */
function displayShip(ship){
  startCoordAcross = ship.start.Across;
  startCoordDown = ship.start.Down;
  endCoordAcross = ship.end.Across;
  endCoordDown = ship.end.Down;
  // console.log(startCoordAcross);
  if(startCoordAcross > 0){
    if(startCoordAcross == endCoordAcross){
      for (i = startCoordDown; i < endCoordDown; i++) {
        $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-image", "url(../../../css/images/mortyhead.png)");

      }
    } else {
      for (i = startCoordAcross; i < endCoordAcross; i++) {
        $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "red");
      }
    }
  }
}

/* Creates grid of 10 squares for MyBoard and TheirBoard */
function createGameBoards() {
  var table = $("<table>").appendTo('.gameBoard');

  var songdelay = 2600; //time in milliseconds, used for delaying song playing

  var song= new Audio('../../../css/sounds/headbentover.m4a');
  var show_me = new Audio('../../../css/sounds/show_me.mp4');
  show_me.play();

  setTimeout(function(){
      song.play(songdelay);
  }, songdelay);

  // Create squares
  for (var y = 1; y <= 10; y++){
    var tableRow = $("<tr id='Row" + y + "'>").appendTo(table);
    for (var x = 1; x <= 10; x++){
        tableRow.append("<td id='" + y + "_" + x + "'></td>");
    }
    table.append("</tr>");
  }
  //Make #shipStatus touchable
  $('#shipStatus').on("click", "tr", function() {

    // // Display Coords in footer

    // Fire or scan Coord
    if(didPressPlaceShip){
        var ship = $(this).attr('id');      //works. Assigns ship1, ship2, ... ship5 to var ship
        $('footer #status').text(ship + " Choose Start Coordinate On Small Board! Choose block to left for left orientation, above for vertical orientation, etc.");
    } else {
        $('footer #status').text("That is not right.");
    }
  //make #MyBoard touchable
  $('#MyBoard').on("click", "td", function() {

    // // Display Coords in footer
    var coords = $(this).attr('id').split("_"); //works. Assigns coords to correct coordinates. Send to placeShip

    // Fire or scan Coord
    if(didPressPlaceShip){
      $('footer #status').text("Choose Start Coordinate On Small Board! Choose block to left for left orientation, above for vertical orientation, etc.");
    } else {
        $('footer #status').text("That is not right.");

    }
    var orientation = didPressRotate;

    placeShip(ship, coords[0], coords[1], orientation);
  })});
  // Make grid touchable to fire at
  $('#TheirBoard').on("click", "td", function() {

    // // Display Coords in footer
    var coords = $(this).attr('id').split("_");

    // Fire or scan Coord
    if(didPressScan){
      scan(coords[0], coords[1]);
        $('footer #status').text("Scaned " + coords[0] + ", " + coords[1]);
    } else {
      fire(coords[0], coords[1]);
    }


  });
}

/* Is called when the user presses 'scan' button */
function pressedScan(){
  didPressScan = true;
        $('footer #status').text("Selected Scan");
}

/* Is called when the user presses 'fire' button */
function pressedFire(){
  didPressScan = false;
        $('footer #status').text("Selected Fire");
}

function pressedPlaceShip(){
    didPressScan = false;
    didPressPlaceShip = true;
        $('footer #status').text("Placing Ships...");
}

function pressedRotate(){
    if(didPressRotate == "horizontal"){
        didPressRotate = "vertical";
    }
    else{
        didPressRotate = "horizontal";
    }
}
/* Sets up the ship status box */
function SetUpShipStatus(){
  var shipLengths = [2, 2, 3, 4, 5];
  var shipList = ["destroyer", "submarine", "cruiser", "battleship", "aircraftCarrier"];
  var table = $("<table>").appendTo('#shipStatus');
  for (var y = 0; y < 5; y++){
    var tableRow = $("<tr id=" + shipList[y] + ">").appendTo(table);
    for (var x = 1; x <= shipLengths[y]; x++){
      tableRow.append("<td id='" + (y+1) + "_" + x + "'></td>");
    }
    table.append("</tr>");
  }
  $("</table>").appendTo('.gameBoard');
}

