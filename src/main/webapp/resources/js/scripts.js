$(document).ready(function() {

    $("#backOneStep").click(function(event) {
        event.preventDefault();
        history.back(1);
    });

    $('.popAdd3').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Value of this dimension for the first dataset', 'content' : 'e.g. Germany'});
    $('.popLabelLeft').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Name for the new dimension', 'content' : 'e.g. Country'});
    $('.popUrlLeft').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Identifying resource', 'content' : 'A URI: e.g. http://dbpedia.org/resource/Country'});
    $('.popAddRange').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Range of the dimensions values', 'content' : 'Indicates possibilities for later aggregation'});

    $('.popGraphAdd').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Select the graph.', 'content' : 'The new dimension will be added to this graph.'});
    $('.popLabelAdd').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Name for the new dimension', 'content' : 'e.g. Country'});
    $('.popUrlAdd').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Identifying resource', 'content' : 'A URI: e.g. http://dbpedia.org/resource/Country'});
    $('.popValueAdd').popover({'trigger' : 'hover', 'placement' : 'left', 'title' : 'Value of this dimension for the first dataset', 'content' : 'e.g. Germany'});



    $("#disambiguate").click(function(){
        $('#modelDisambiguation').modal('show');
        $("#loaderDis").show();
        var url = "disambiguate?surfaceForm=" + $("#addLabelInput").val();

        $.getJSON(url, function( data ) {
            if (data.disambiguatedSurfaceforms != null) {
                var length = data.disambiguatedSurfaceforms[0].disEntities.length;
                var k = Math.min(length, 10);
                var listItems = "";

                for (var i = 0; i < k; i++) {
                    var concept = data.disambiguatedSurfaceforms[0].disEntities[i].entityUri;
                    listItems += "<li><span style='cursor: pointer' class='disambiguated'>" + concept + "</span></li>";
                }

                $("#disambiguatedSuggestions").empty();
                $("#disambiguatedSuggestions").append(listItems);
                $("#loaderDis").hide();

                $(".disambiguated").click(function(){
                    $("#addResourceInput").val($(this).text());
                    $('#modelDisambiguation').modal('hide');
                });
            } else {
                alert("Service unavailable!");
            }
        });
    });

    $("#disambiguateMerged").click(function(){
        $('#modelDisambiguation').modal('show');
        $("#loaderDis").show();
        var url = "disambiguate?surfaceForm=" + $("#addLabelMergedInput").val();

        $.getJSON(url, function( data ) {
            if (data.disambiguatedSurfaceforms != null) {
                var length = data.disambiguatedSurfaceforms[0].disEntities.length;
                var k = Math.min(length, 10);
                var listItems = "";

                for (var i = 0; i < k; i++) {
                    var concept = data.disambiguatedSurfaceforms[0].disEntities[i].entityUri;
                    listItems += "<li><span style='cursor: pointer' class='disambiguated'>" + concept + "</span></li>";
                }

                $("#disambiguatedSuggestions").empty();
                $("#disambiguatedSuggestions").append(listItems);
                $("#loaderDis").hide();

                $(".disambiguated").click(function(){
                    $("#mergedURL").val($(this).text());
                    $('#modelDisambiguation').modal('hide');
                });
            } else {
                alert("Service unavailable!");
            }
        });
    });

    $("#askBalloon").click(function() {
        $('#modelSame').modal('show');
        $("#loaderSame").show();
        // for testing:
        // concept1 = http://vocabulary.semantic-web.at/AustrianSkiTeam/121
        // concept2 = http://rdf.freebase.com/ns/m/08zld9

        var url = "same-as?";

        $(".concept1").each(function(){
            url += "conceptList1=" + $(this).text() + "&";
        });

        $(".concept2").each(function(){
            url += "conceptList2=" + $(this).text() + "&";
        });

        console.log(url);


        $.getJSON(url, function( data ) {

            var listItems = "";

            for (var i = 0; i < data.length; i++) {
                listItems += "<li>" + data[i].c1 + "<br/> --sameAs--  <br/>" + data[i].c2 + "</li>"
            }

            if (data.length == 0) {
                listItems = "<li>No sameAs relationship could be inferred.</li>"
            }

            $("#sameSuggestions").empty();
            $("#sameSuggestions").append(listItems);
            $("#loaderSame").hide();

        });
    });
});