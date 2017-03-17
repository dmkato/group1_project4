var gameModel;
var didPressScan;
var didPressPlaceShip = true;
var didPressEasy = true;
var didPressRotate = "horizontal";
var scannedCoord = null;
var gameMode = "Easy"

/* On page ready.. */
$( document ).ready(function() {
    alert("INSTRUCTIONS: \n Press 'Place Ship' button at bottom\n Choose the ship to place, displayed under 'computer ships remaining.' **Note: Must choose smallest ship first.\n Press 'Rotate Ship' to choose between Horizontal and Vertical. **Note: Default is Horizontal.\n Press 'Fire' to fire");

});

function pressedEasy(){
    didPressScan = false;
    didPressPlaceShip = false;
    didPressEasy = true;
    gameMode = "Easy";

    // Hide buttons after choosing
    $('#easyBtn').css("display", "none");
    $('#hardBtn').css("display", "none");

    // Handler for .ready() called.
    $.getJSON("model/Easy", function( json ) {
      gameModel = json;
      gmString = JSON.stringify(gameModel)
      console.log( "JSON Data: " + gmString);
    });

    // Create gameBoards
    createGameBoards();
    chooseShips();
    SetUpShipStatus();
}

function pressedHard(){
    didPressScan = false;
    didPressPlaceShip = false;
    didPressEasy = true;
    gameMode = "Hard";

    // Hide buttons after choosing
    $('#easyBtn').css("display", "none");
    $('#hardBtn').css("display", "none");

    // Handler for .ready() called.
    $.getJSON("model/Hard", function( json ) {
      gameModel = json;
      gmString = JSON.stringify(gameModel)
      console.log( "JSON Data: " + gmString);
    });

    // Create gameBoards
    createGameBoards();
    chooseShips();
    SetUpShipStatus();
}

/* Places Ship based on buttons that no longer exist */
function placeShip(ship, x, y, orientation) {
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
   scannedCoord = null;
   var lasergun = new Audio('../../../css/sounds/laser.m4a');
   var rick_talk = new Audio('../../../css/sounds/riggity.wav');
   var voice;

   var voice_delay = 500;
   lasergun.play();

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
    if(gameModel.computerMisses[i].loc.Across == x && gameModel.computerMisses[i].loc.Down == y){
      rick_talk.play();
      console.log("made it into conditional 1");
      $('footer #status').text("You have already fired at " + x + ", " + y);
      return;
    }
  }
 //check if player has hit a standard ship
    for (var i = 0; i < gameModel.computerHits.length; i++) {
      if(gameModel.computerHits[i].loc.Across == x && gameModel.computerHits[i].loc.Down == y){

             if(gameModel.computerHits[i].type == "civ"){
                    voice = new Audio('../../../css/sounds/ooh_wee.mp4');
             } else if (gameModel.computerHits[i].type == "CIA"){
                    voice = new Audio('../../../css/sounds/krom_talk.mp4');
             } else {
                    voice = new Audio('../../../css/sounds/oh_man.wav');
             }

        setTimeout(function(){
            voice.play(voice_delay);
        }, voice_delay);

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

  $( '#MyBoard td'  ).css("background-color", "#25383C");
  $( '#TheirBoard td'  ).css("background-color", "#25383C");
  $( '#MyBoard td'  ).css("background-image", "none");
  $( '#TheirBoard td'  ).css("background-image", "none");

  if(didPressScan) {
    if(gameModel.scanResult){
      $('footer #status').text("Scan found at least one Ship");
    } else {
      $('footer #status').text("Scan found no Ships");
    }
  }

  displayShip(gameModel.aircraftCarrier);
  displayShip(gameModel.battleship);
  displayShip(gameModel.submarine);
  displayShip(gameModel.clipper);
  displayShip(gameModel.dhingy);
  displayShip(gameModel.fisher);


  for (var i = 0; i < gameModel.computerMisses.length; i++) {
    $( '#TheirBoard #' + gameModel.computerMisses[i].loc.Across + '_' + gameModel.computerMisses[i].loc.Down ).css("background-image", "url(../../../css/images/rickhead.png)");

  }
  for (var i = 0; i < gameModel.computerHits.length; i++) {
    if(gameModel.computerHits[i].type == "civ"){
        $( '#TheirBoard #' + gameModel.computerHits[i].loc.Across + '_' + gameModel.computerHits[i].loc.Down ).css("background-image", "url(../../../css/images/poopyhead.png)");
    }else if(gameModel.computerHits[i].type == "CIA"){
        $( '#TheirBoard #' + gameModel.computerHits[i].loc.Across + '_' + gameModel.computerHits[i].loc.Down ).css("background-image", "url(../../../css/images/krommike.png)");
    }else{
        $( '#TheirBoard #' + gameModel.computerHits[i].loc.Across + '_' + gameModel.computerHits[i].loc.Down ).css("background-image", "url(../../../css/images/mortyhead.png)");
    }
  }

  for (var i = 0; i < gameModel.playerMisses.length; i++) {
    $( '#MyBoard #' + gameModel.playerMisses[i].loc.Across + '_' + gameModel.playerMisses[i].loc.Down ).css("background-image", "url(../../../css/images/rickhead.png)");
  }
  for (var i = 0; i < gameModel.playerHits.length; i++) {
    if(gameModel.playerHits[i].type == "civ"){
        $('#MyBoard #' + gameModel.playerHits[i].loc.Across + '_' + gameModel.playerHits[i].loc.Down).css("background-image", "url(../../../css/images/poopyhead.png)");
    }
    else if(gameModel.playerHits[i].type == "CIA"){
        $('#MyBoard #' + gameModel.playerHits[i].loc.Across + '_' + gameModel.playerHits[i].loc.Down).css("background-image", "url(../../../css/images/krommike.png)");
    }
    $( '#MyBoard #' + gameModel.playerHits[i].loc.Across + '_' + gameModel.playerHits[i].loc.Down ).css("background-color", "red");
  }

 // Show scanned area
  if(scannedCoord != null){
    // Set surrounding squres
    var surroundingCoord = "";
    for(var x = -1; x <= 1; x++){
      for(var y = -1; y <= 1; y++){
        var coords = scannedCoord.split("_");

        surroundingCoord = (parseInt(coords[0]) + x) + "_" + (parseInt(coords[1]) + y);
        $('#TheirBoard #' + surroundingCoord).css("background-color", "lightgreen");
      }
    }
  // Set scanned square
  $('#TheirBoard #' + scannedCoord).css("background-color", "green");
  }
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
      for (i = startCoordDown; i <= endCoordDown; i++) {
        $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-image", "url(../../../css/images/mortyhead.png)");
        if(ship.type == "civ"){
            $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-image", "url(../../../css/images/poopyhead.png)");
        }
        else if(ship.type == "CIA"){
            $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-image", "url(../../../css/images/krommike.png)");
        }

      }
    } else {
      for (i = startCoordAcross; i <= endCoordAcross; i++) {
        $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-image", "url(../../../css/images/mortyhead.png)");
        if(ship.type == "civ"){
            $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-image", "url(../../../css/images/poopyhead.png)");
        }
        else if(ship.type == "CIA"){
            $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-image", "url(../../../css/images/krommike.png)");
        }
      }
    }
  }
}

/* Set up Game */
function chooseShips(){
  //Make #shipStatus touchable
  $('#shipStatus').on("click", "tr", function() {

    // Choose Ship
    if(didPressPlaceShip){
        var ship = $(this).attr('id');      //works. Assigns ship1, ship2, ... ship5 to var ship
        $('footer #status').text(ship + " Choose Start Coordinate On Small Board!");
    } else {
        $('footer #status').text("That is not right.");
    }
    //make #MyBoard touchable
    $('#MyBoard').on("click", "td", function() {

      // // Display Coords in footer
      var coords = $(this).attr('id').split("_"); //works. Assigns coords to correct coordinates. Send to placeShip

      // Fire or scan Coord
      if(didPressPlaceShip){
        $('footer #status').text("Choose Start Coordinate On Small Board!");
      } else {
          $('footer #status').text("That is not right.");

      }
      var orientation = didPressRotate;

      placeShip(ship, coords[0], coords[1], orientation);
    });
  });

  // Make grid touchable to fire at
  $('#TheirBoard').on("click", "td", function() {

    // // Display Coords in footer
    var tdId = $(this).attr('id');
    var coords = tdId.split("_");

    // Fire or scan Coord
    if(didPressScan){
      scan(coords[0], coords[1]);
        $('footer #status').text("Scaned " + coords[0] + ", " + coords[1]);
        scannedCoord = tdId;
        displayGameState(gameModel);
    } else {
      fire(coords[0], coords[1]);
    }
  });

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
}

/* Is called when the user presses 'scan' button */
function pressedScan(){
  didPressScan = true;
  $('#rotateShipBtn').css("display", "none");
  $('#scanBtn').addClass('btn-success');
  $('#fireBtn').removeClass('btn-success');
  $('#placeShipBtn').removeClass('btn-success');
  $('#rotateShipBtn').removeClass('btn-success');
}

/* Is called when the user presses 'fire' button */
function pressedFire(){
  didPressScan = false;
  $('#rotateShipBtn').css("display", "none");
  $('#fireBtn').addClass('btn-success');
  $('#scanBtn').removeClass('btn-success');
  $('#placeShipBtn').removeClass('btn-success');
  $('#rotateShipBtn').removeClass('btn-success');
}

function pressedPlaceShip(){
  didPressScan = false;
  didPressPlaceShip = true;
  $('#rotateShipBtn').css("display", ""); // Empty display string reveals button

  $('#placeShipBtn').addClass('btn-success');
  $('#fireBtn').removeClass('btn-success');
  $('#scanBtn').removeClass('btn-success');
  $('#rotateShipBtn').removeClass('btn-success');
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
  var shipLengths = [1, 2, 3, 2, 4, 5];
  var shipList = ["dhingy", "fisher", "clipper", "submarine", "battleship", "aircraftCarrier"];
  var table = $("<table>").appendTo('#shipStatus');

  for (var y = 0; y < 6; y++){
    var tableRow = $("<tr id=" + shipList[y] + ">").appendTo(table);
    for (var x = 1; x <= shipLengths[y]; x++){
      tableRow.append("<td id='" + (y+1) + "_" + x + "'></td>");
    }
    table.append("</tr>");
  }
  $("</table>").appendTo('.gameBoard');
}
