Vue.component("admin-restorani", {
    data() {
        return {
            restorani: [],
			slike: [],
            pretraga: {
                naziv: '',
                tip: '',
                lokacija: '',
				prosecnaOcena:0
            },
            podaciZaFiltriranjeRestorana: {
                otvoreni: "",
                tip: ""
            },
            nazivBrojac : 0,
            lokacijaBrojac : 0,
            ocenaBrojac : 0,
            dijalogZaIzmenuSakriven: true,
            restoranZaIzmenu: {
				ID: -1,
				logickiObrisan: 0,
				naziv: null,
				tip: null,
                status: null,
                broj: null,
                mesto: null,
                ulica: null,
                postanskiBroj: null,
                geografskaSirina: null,
                geografskaDuzina: null,
                putanjaDoSlike: "",
                idMenadzera: -1,
                prosecnaOcena: 0.0,
			},
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false,
			mapaZaPretraguVidljiva: false
        }
    },

    template: `
    <div id = "stilZaPregledRestorana">

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaFiltereVidljiv = !prostorZaFiltereVidljiv " class="btn"><i class="fa fa-filter" aria-hidden="true"></i> Filteri </button>
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <button type="button" class="btn"><i class="fa fa-plus" aria-hidden="true"></i><router-link to="/dodavanjeRestorana" exact style="color:black">Dodaj restoran</router-link></button>
        <br><br>

        <!-- Pretraga -->
        <div class="pretragaRestorana" v-if="prostorZaPretraguVidljiv" >
            <form method='post' >

                <input type="text" v-model="pretraga.naziv" v-bind:class="{filledInput: pretraga.naziv != '' }" placeholder="Naziv" >
                <input type="text" v-model="pretraga.tip" v-bind:class="{filledInput: pretraga.tip != '' }" placeholder="Tip" >
                <input type="text" id="gradZaPretragu" v-model="pretraga.lokacija" v-bind:class="{filledInput: pretraga.lokacija != '' }" placeholder="Grad" >           
				<input type="text" v-model="pretraga.prosecnaOcena" v-bind:class="{filledInput: pretraga.prosecnaOcena != '' }" placeholder="Prosečna ocena" >
				
				<button type="button" @click="pregledMapeZaPretragu()"><i class="fa fa-map-marker" aria-hidden="true"></i> Odaberite na mapi </button>
            </form>
        </div>
        <!-- Kraj pretrage -->
        
		<!-- Mapa za pronalazak grada -->
		<div class="okoMapeZaPretragu" v-if="mapaZaPretraguVidljiva && prostorZaPretraguVidljiv">
        <div id="mapaPretraga" class="mapaPretraga" ></div>
		</div>
		
        <!-- Filtriranje restorana -->
        <div class="filterZaRestorane" v-if="prostorZaFiltereVidljiv">
            <form method='post' 
                <select v-model="podaciZaFiltriranjeRestorana.otvoreni" @change="onchangeAktivnostRestorana()">
                    <option value="">Svi restorani</option>
                    <option>Samo otvoreni</option>
                </select>

                <select v-model="podaciZaFiltriranjeRestorana.tip" @change="onchangeTipRestorana()">
                    <option value="">Bez filtera za tip</option>
                    <option>Brza hrana</option>
                    <option>Roštilj</option>
					<option>Burgeri</option>
					<option>Italijanski</option>
					<option>Picerija</option>
                    <option>Kineski</option>
                    <option>Azijska kuhinja</option>
                </select>
            </form>
        </div>
        <!-- Kraj filtriranja restorana -->

        
        <!-- Sortiranje restorana -->
        <div v-if="prostorZaSortiranjeVidljiv" class="sortiranjeRestorani">
            <form method='post'>

                <button type="button" @click="sortirajNaziv"><i class="fa fa-sort" aria-hidden="true"></i> Naziv </button>
                <button type="button" @click="sortirajLokacija"><i class="fa fa-sort" aria-hidden="true"></i> Lokacija </button>
                <button type="button" @click="sortirajProsecnaOcena"><i class="fa fa-sort" aria-hidden="true"></i> Prosečna ocena </button>

            </form>
        </div>
        <!-- Kraj sortiranja restorana -->

		<!-- Card za restoran -->
        <ul>
            <li v-for="restoran in filtriraniRestorani">
            	<div class="cardsRestoranDiv">
                <img class="logoRestorana" v-bind:src="dobaviPutanjuSlike(restoran)">

                <table class="cardsRestoran">
                    <tr>
                        <td> Naziv : </td>
                        <td> {{ restoran.naziv }} </td>
                    </tr>

                    <tr>
                        <td> Tip: </td>
                        <td> {{ restoran.tip }} </td>
                    </tr>

					<tr>
						<td> Lokacija </td>
						<td> {{ restoran.lokacija && restoran.lokacija.adresa.mesto }}, {{ restoran.lokacija && restoran.lokacija.adresa.ulica }}, {{ restoran.lokacija && restoran.lokacija.adresa.broj }}</td>
					</tr>
                </table>

				<button type="button" v-if=" restoran.logickiObrisan == '0' " @click="pregledRestorana(restoran)" style="margin-bottom:10px" class="button">Pregled ponude</button> <br>
                <button type="button" v-if=" restoran.logickiObrisan == '0' " @click="izmeniRestoran(restoran)" class="izmenaStyle button" ><i class="fa fa-pencil" aria-hidden="true"></i>  Izmeni </button> <br>
                <button type="button" v-if=" restoran.logickiObrisan == '0' " @click="obrisiRestoran(restoran)" class="brisanjeStyle button" ><i class="fa fa-trash" aria-hidden="true"></i>  Obriši </button> <br>
            	</div>
            </li>
        </ul>
        <!-- Kraj card za restorane -->

        <!-- Modalni dijalog za izmenu restorana -->
        <div id = "dijalogZaIzmenuRestorana" v-bind:class="{bgModal: dijalogZaIzmenuSakriven, bgModalShow: !dijalogZaIzmenuSakriven}">
            <div class="modal-contents">
        
                <div class="close" @click="dijalogZaIzmenuSakriven = !dijalogZaIzmenuSakriven">+</div>

                <form method='post'>
                    
					<label for="naziv">Naziv:</label>
                    <input name="naziv" type="text" v-model="restoranZaIzmenu.naziv" placeholder="Naziv" required>
					
					<label for="tip">Tip:</label>
					<select name="tip" v-model="restoranZaIzmenu.tip" required>
                        <option>Brza hrana</option>
                        <option>Roštilj</option>
					    <option>Burgeri</option>
					    <option>Italijanski</option>
					    <option>Picerija</option>
                        <option>Kineski</option>
                        <option>Azijska kuhinja</option>
            		</select>
					<label for="status">Status:</label>
					<select name="status" v-model="restoranZaIzmenu.status" required>
                		<option>Radi</option>
                		<option>Ne radi</option>
            		</select>
                    <!-- Lokacija -->
                    <label for="mesto">Mesto:</label>
                    <input name="mesto" type="text" v-model="restoranZaIzmenu.mesto" placeholder="Mesto">
                    
                    <label for="ulica">Ulica:</label>
                    <input name="ulica" type="text" v-model="restoranZaIzmenu.ulica" placeholder="Ulica">
                    
                    <label for="broj">Broj:</label>
                    <input  name="broj" type="text" v-model="restoranZaIzmenu.broj" placeholder="Broj">
                    
                    <label for="postanskiBroj">Poštanski broj:</label>
                    <input  name="postanskiBroj" type="text" v-model="restoranZaIzmenu.postanskiBroj" placeholder="Poštanski broj">

                    <label for="geoDuzina"> Geografska dužina </label>
                    <input  name="geoDuzina" type="text" v-model="restoranZaIzmenu.geografskaDuzina" placeholder="Geografska dužina">

                    <label for="geoSirina"> Geografska dužina </label>
                    <input  name="geoSirina" type="text" v-model="restoranZaIzmenu.geografskaSirina" placeholder="Geografska širina">

                    <!-- Kraj lokacije -->

                    <!-- Logo restorana -->
                    <input type="file" onchange="slikaUUrl(this)" />

                    <button type="button" @click="potvrdiIzmene" class="btn">Potvrdi</button>
                    <button type="button" @click="dijalogZaIzmenuSakriven = !dijalogZaIzmenuSakriven" class="btn">Odustani</button>

                </form>

                <img hidden id="slikaZaIzmenu"  src="" alt="Logo restorana" width="11" height="11">

            </div>
        </div> <!-- Kraj modalnog dijaloga -->


    </div>
    `,
    methods: {
    	pregledRestorana: function(restoran){
    		axios
                .post('rest/restoran/restoranZaPregled', {restoran})
         		.then(response => {router.push("/pregledPonude")});
    	},
        dodajRestoran: function () {
            window.location.href = "http://localhost:8080/NjamNJam/admin.html#/dodavanjeRestorana";
        },
        izmeniRestoran: function (restoran) {
            this.dijalogZaIzmenuSakriven = !this.dijalogZaIzmenuSakriven;
            this.restoranZaIzmenu.ID = restoran.id;
            this.restoranZaIzmenu.tip = restoran.tip;
            this.restoranZaIzmenu.naziv = restoran.naziv;
            this.restoranZaIzmenu.mesto = restoran.lokacija.adresa.mesto;
            this.restoranZaIzmenu.ulica = restoran.lokacija.adresa.ulica;
            this.restoranZaIzmenu.broj = restoran.lokacija.adresa.broj;
            this.restoranZaIzmenu.postanskiBroj = restoran.lokacija.adresa.postanskiBroj;
            this.restoranZaIzmenu.geografskaSirina = restoran.lokacija.geografskaSirina;
            this.restoranZaIzmenu.geografskaDuzina = restoran.lokacija.geografskaDuzina;
            this.restoranZaIzmenu.putanjaDoSlike = restoran.putanjaDoSlike;
            this.restoranZaIzmenu.status = restoran.status;
            console.log(this.restoranZaIzmenu);
        },
        obrisiRestoran: function (restoran) {
            axios
                .delete('rest/restoran/obrisiRestoran', {
                    data: {
                        restoran : restoran
                    }
                })
                .then(response => {
                    this.restorani = [];
                    response.data.forEach(el => {
                            this.restorani.push(el);
                    });
                    toastr["success"]("Uspešno obrisan restoran!", "Uspešno brisanje!");

                    return this.restorani;
                });
        },
        potvrdiIzmene: function () {
			
            if (!this.restoranZaIzmenu.naziv || !this.restoranZaIzmenu.mesto || !this.restoranZaIzmenu.ulica
                || !this.restoranZaIzmenu.geografskaSirina || !this.restoranZaIzmenu.geografskaDuzina
                || !this.restoranZaIzmenu.broj || !this.restoranZaIzmenu.postanskiBroj)
            {
                	toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                	return;
            }
            this.restoranZaIzmenu.putanjaDoSlike = document.getElementById("slikaZaIzmenu").src;
			console.log(this.restoranZaIzmenu)
            axios
                .post('rest/restoran/izmeniRestoran', this.restoranZaIzmenu)
                .then(response => {
                    this.restorani = [];
                    response.data.forEach(el => {
                            this.restorani.push(el);
                    });
                    toastr["success"]("Uspešna izmena restorana!", "Uspešna izmena!");
                    location.reload();
                    return this.restorani;
                });
        },
        poklapaSeSaPretragom: function (restoran) {

            // Naziv
            if (!restoran.naziv.toLowerCase().match(this.pretraga.naziv.toLowerCase()))
                return false;

            // Tip
            if (!restoran.tip.toLowerCase().match(this.pretraga.tip.toLowerCase()))
                return false;

            // Lokacija
            if (!restoran.lokacija.adresa.mesto.toLowerCase().match(this.pretraga.lokacija.toLowerCase()))
                return false;
            
            //prosecna ocena
            if (restoran.prosecnaOcena < parseFloat(this.pretraga.prosecnaOcena))
                return false;

            return true;
        },
		onchangeTipRestorana: function () {
            if (this.podaciZaFiltriranjeRestorana.tip == "" || this.podaciZaFiltriranjeRestorana.tip == "Bez filtera za tip") {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                        response.data.forEach(el => {
                                this.restorani.push(el);
                        });
                        return this.restorani;
                    });

            } else {
                let filterRestorana = (this.restorani).filter(restoran => restoran.tip == this.podaciZaFiltriranjeKorisnika.tip);
                this.restorani = filterRestorana;
            }
        },
        onchangeAktivnostRestorana: function () {
            if (this.podaciZaFiltriranjeRestorana.otvoreni == "" || this.podaciZaFiltriranjeRestorana.otvoreni == "Svi restorani") {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                        response.data.forEach(el => {
                                this.restorani.push(el);
                        });
                        return this.restorani;
                    });

            } else {
                let filterRestorana = (this.restorani).filter(restoran => restoran.status == "Radi");
                this.restorani = filterRestorana;
            }
        },
        sortirajNaziv: function () {
            this.nazivBrojac ++;
            if(this.nazivBrojac % 3 == 0)
            {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                        response.data.forEach(el => {
                                this.restorani.push(el);
                        });
                        return this.restorani;
                    });
            }else if(this.nazivBrojac % 3 == 1)
            {
                this.multisort(this.restorani, ['naziv', 'naziv'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.restorani, ['naziv', 'naziv'], ['DESC', 'ASC']);
            }  
        },
        sortirajLokacija: function () {
            this.lokacijaBrojac ++;
            if(this.lokacijaBrojac % 3 == 0)
            {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                        response.data.forEach(el => {
                                this.restorani.push(el);
                        });
                        return this.restorani;
                    });
            }else if(this.lokacijaBrojac % 3 == 1)
            {
                this.multisort(this.restorani, ['lokacija.adresa.mesto', 'lokacija.adresa.mesto'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.restorani, ['lokacija.adresa.mesto', 'lokacija.adresa.mesto'], ['DESC', 'ASC']);
            }
        },
        sortirajProsecnaOcena: function(){
            this.prosecnaOcenaBrojac ++;
            if(this.prosecnaOcenaBrojac % 3 == 0)
            {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                        response.data.forEach(el => {
                                this.restorani.push(el);
                        });
                        return this.restorani;
                    });
            }else if(this.prosecnaOcenaBrojac % 3 == 1)
            {
                this.multisort(this.restorani, ['prosecnaOcena', 'prosecnaOcena'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.restorani, ['prosecnaOcena', 'prosecnaOcena'], ['DESC', 'ASC']);
            }
        },
        multisort: function (arr, columns, order_by) {
            if (typeof columns == 'undefined') {
                columns = []
                for (x = 0; x < arr[0].length; x++) {
                    columns.push(x);
                }
            }

            if (typeof order_by == 'undefined') {
                order_by = []
                for (x = 0; x < arr[0].length; x++) {
                    order_by.push('ASC');
                }
            }

            function multisort_recursive(a, b, columns, order_by, index) {
                var direction = order_by[index] == 'DESC' ? 1 : 0;

                var is_numeric = !isNaN(a[columns[index]] - b[columns[index]]);

                var x = is_numeric ? a[columns[index]] : a[columns[index]].toLowerCase();
                var y = is_numeric ? b[columns[index]] : b[columns[index]].toLowerCase();

                if (!is_numeric) {

                    let sum_x = 0;
                    let sum_y = 0;

                    x.split("").forEach(element => sum_x += element.charCodeAt())
                    y.split("").forEach(element => sum_y += element.charCodeAt())

                    x = sum_x;
                    y = sum_y;
                }

                if (x < y) {
                    return direction == 0 ? -1 : 1;
                }

                if (x == y) {
                    return columns.length - 1 > index ? multisort_recursive(a, b, columns, order_by, index + 1) : 0;
                }

                return direction == 0 ? 1 : -1;
            }

            return arr.sort(function (a, b) {
                return multisort_recursive(a, b, columns, order_by, 0);
            });
        },
        dobaviPutanjuSlike: function (restoran) {
            let imageDefault = "data:image/png;base64,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";

            // Default image
            if (restoran.putanjaDoSlike == "withoutPath")
                return imageDefault;

            let slika;

            slika = this.dobaviBase64odIdja(parseInt(restoran.putanjaDoSlike, 10));

            return slika;
        },
        dobaviBase64odIdja: function (idSlike) {
            let base64Slike;

            for (let el of this.slike) {
                if (el.id == idSlike) {
                    base64Slike = el.code64;
                    break;
                }
            }
            return base64Slike;
        },
        initForMap: function () {

            const mapSearch = new ol.Map({
                target: 'mapaPretraga',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    })
                ],
                view: new ol.View({
                    center: [2278434.939124534, 5591521.493117212],
                    zoom: 7
                })
            })

            mapSearch.on('click', function (evt) {
                var coord = ol.proj.toLonLat(evt.coordinate);
                reverseGeocodeSearch(coord);
            })

        },
        pregledMapeZaPretragu: function () {
            this.mapaZaPretraguVidljiva = !this.mapaZaPretraguVidljiva;
            if (this.mapaZaPretraguVidljiva) {
                this.$nextTick(function () {
                    this.initForMap();
                    let c = document.getElementById("mapaPretraga").childNodes;
                    c[0].style.borderRadius  = '10px';
                    c[0].style.border = '4px solid #04030f';
                })
            }
        },
    },
    mounted() {
        this.$nextTick(function () {
            this.initForMap();
        })

        axios.get('rest/slike/dobaviSlike').then(response => (this.slike = response.data));

        axios
            .get('rest/restoran/dobaviRestorane')
            .then(response => {
                this.restorani = [];
                console.log(response.data);
                response.data.forEach(el => this.restorani.push(el));
                return this.restorani;
            });
    },
    computed: {
        filtriraniRestorani: function () {
            return this.restorani.filter((restoran) => {
                return this.poklapaSeSaPretragom(restoran);
            });
        }
    },
});
 function reverseGeocodeSearch(coords) {
    fetch('http://nominatim.openstreetmap.org/reverse?format=json&lon=' + coords[0] + '&lat=' + coords[1])
        .then(function (response) {
            return response.json();
        }).then(function (json) {
 
            console.log(json.address);
            if (json.address.city) {
                let el = document.getElementById("gradZaPretragu");
                el.value = json.address.city;
                el.dispatchEvent(new Event('input'));
            } else if (json.address.city_district) {
                let el = document.getElementById("gradZaPretragu");
                el.value = json.address.city_district;
                el.dispatchEvent(new Event('input'));
            }
        });    
}
function slikaUUrl(element) {
    var file = element.files[0];
    var reader = new FileReader();
    reader.onloadend = function () {
        document.getElementById('slikaZaIzmenu')
            .setAttribute(
                'src', reader.result
            );
    }
    reader.readAsDataURL(file);
}