$(document).ready(function () {

    function makeHTML(response) {
        var stringHTML = "<table style=\"border: 1px solid black;\">"
            + "<tr style=\"border: 1px solid black;\">"
            + "<th style=\"border: 1px solid black;\">Identifikator obroka(id)</th>"
            + "<th style=\"border: 1px solid black;\">Naziv jela</th>"
            + "<th style=\"border: 1px solid black;\">Ime i prezime sefa kuhinje</th>"
            + "<th style=\"border: 1px solid black;\">Kolicina sastojaka</th>"
            + "<th style=\"border: 1px solid black;\">Vreme</th>"
            + "<th style=\"border: 1px solid black;\">Cena obroka[RSD]</th></tr>";
        response.forEach(element => {
            stringHTML += "<tr>";
            if (element.cena < 60) {
                stringHTML += "<td style=\"border: 1px solid black;\">" + element.id + "</td>";
                stringHTML += "<td style=\"border: 1px solid black;\">" + element.nazivJela + "</td>";
                stringHTML += "<td style=\"border: 1px solid black;\">" + element.imeIPrezimeSefaKuhinje + "</td>";
                stringHTML += "<td style=\"border: 1px solid black;\">" + element.kolicinaSastojaka+ "</td>";
                stringHTML += "<td style=\"border: 1px solid black;\">" + element.vremeSluzenja+ "</td>";
                stringHTML += "<td style=\"border: 1px solid black;\">" + element.cena + "</td>";
                if (!element.dopuna) {
                    stringHTML += "<td style=\"border: 1px solid black;\"><a href=\"#\" id=\"" + element.id + "\">Zahtevaj dopunu</a></td>";
                }
            } else {
                stringHTML += "<td style=\"border: 1px solid black; background: grey;\">" + element.id + "</td>";
                stringHTML += "<td style=\"border: 1px solid black; background: grey;\">" + element.nazivJela+ "</td>";
                stringHTML += "<td style=\"border: 1px solid black; background: grey;\">" + element.imeIPrezimeSefaKuhinje+ "</td>";
                stringHTML += "<td style=\"border: 1px solid black; background: grey;\">" + element.kolicinaSastojaka+ "</td>";
                stringHTML += "<td style=\"border: 1px solid black; background: grey;\">" + element.vremeSluzenja+ "</td>";
                stringHTML += "<td style=\"border: 1px solid black; background: grey;\">" + element.cena + "</td>";
                if (!element.dopuna) {
                    stringHTML += "<td style=\"border: 1px solid black; background: grey;\"><a href=\"#\" id=\"" + element.id + "\">Zahtevaj dopunu<</a></td>";
                }
            }
            stringHTML += "</tr>";
        });
        stringHTML += "</table>";
        stringHTML += "<h2>Pretraga po ceni obroka</h2><label for=\"cena\">Unesite maksimalnu cenu:</label><input type=\"text\" name=\"cena\" id=\"cena\"><br><input id=\"dugme\"type=\"submit\" value=\"Pretrazi\">";
        $("#original").html(stringHTML);
    }

    $("#dodajDugme").click(function (e) {
        console.log("USAO");
        e.preventDefault();
        let id = $("#id").val();
        let naziv = $("#naziv").val();
        let sef = $("#sef").val();
        let kolicina = parseFloat($("#kolicina").val());
        let vreme = $("#vreme option:selected").text();
        let cena = parseFloat($("#cena").val());
        let dopuna = false;
        let dataJSON = {id:id,nazivJela:naziv,imeIPrezimeSefaKuhinje:sef,kolicinaSastojaka:kolicina,vremeSluzenja:vreme,cena:cena,dopuna:dopuna}
        let dataString = JSON.stringify(dataJSON);
        if (id.length != 11) {
            alert("Nedovoljna duzina sifre (trazeni format XXX-XXXX-XX)!");
            return;
        }
        if (id.charAt(3) != '-' || id.charAt(8) != '-') {
            alert("Pogresno unesena sifra! (nedostaje \'-\', trazeni format XXX-XXXX-XX)");
            return;
        }
        for (let i = 0; i < id.length; i++) {
            if (i != 3 && i != 8) {
                if (!(id.charAt(i) >= '0' && id.charAt(i) <= '9')) {
                    alert("Pogresno unesena sifra! (nedostaju brojevi, trazeni format XXX-XXXX-XX)");
                    return;
                }
            }
        }
        if (isNaN(parseFloat($("#kolicina").val()))) {
            alert("Pogresno unesena kolicina!");
            return;
        }
        if (isNaN(parseFloat($("#cena").val()))) {
            alert("Pogresno unesena cena!");
            return;
        }
        if (id.trim() === "" || naziv.trim() === "" || sef.trim() === "") {
            alert("Ne ostavljajte prazna polja!");
            return;
        }

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/NjamNjam/rest/obrokservices/unesi",
            data: dataString,
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
                if (response == null) {
                    alert("Obrok sa takvom sifrom vec postoji!");
                } else {
                    makeHTML(response);
                }

            }
        });
    });

    $(document).on("click", "a", function () {
        let id = $(this).attr('id');
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/NjamNjam/rest/obrokservices/obroci/" + id,
            success: function (response) {
                makeHTML(response);
            }
        });
    });

    $(document).on("click", "#dugme", function () {
        let cena = $("#cena").val();
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/NjamNjam/rest/obrokservices/pretraga/" + cena,
            success: function (response) {
                if (response == null) {
                    $("#original").html("<p>Ne postoji obrok sa cenom manjom od unesene<p>");
                } else {
                    makeHTML(response);
                }

            }
        });
    });

});
