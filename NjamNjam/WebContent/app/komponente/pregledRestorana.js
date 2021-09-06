Vue.component("pregled-restorana", {
    data() {
        return {
			slike: [],
            restoran : {},
			artikli:[],
			komentari:[],
			// Deo potreban za prikaz artikala
			pretraga: {
                naziv: '',
				cena: 0
            },
            podaciZaFiltriranjeArtikala: {
                tip: ""
            },
            nazivBrojac : 0,
            cenaBrojac : 0,
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false,
			imaArtikle: false
        }
    },

    template: `
    <div id = "stilZaPregledRestorana">
            	<div class="cardsRestoranPregledDiv">
            	<h1> {{ restoran.naziv }}</h1>
                <img class="logoRestorana" v-bind:src="dobaviPutanjuSlike(restoran)">

                <table class="cardsRestoranPregled">
                    <tr>
                        <td> Tip: </td>
                        <td> {{ restoran.tip }} </td>
                    </tr>
                    <tr>
						<td> Status: </td>
						<td> {{restoran.status}}	</td>
					</tr>
					<tr>
						<td> Ocena:	</td>
						<td> {{ restoran.prosecnaOcena }}	</td>
					</tr>
					<tr>
						<td> Lokacija: </td>
						<td> {{ restoran.lokacija && restoran.lokacija.adresa.mesto }}, {{ restoran.lokacija && restoran.lokacija.adresa.ulica }}, {{ restoran.lokacija && restoran.lokacija.adresa.broj }}</td>
					</tr>
					<tr>
						<td colspan="2">
							<div id="map" class="mapaLokacija" style="margin:5px auto">  </div> 
						</td>
					</tr>
                </table>
			</div>
        <!-- Kraj card za restoran -->



        <!-- Pocetak prikaza artikala -->
		<div v-show="imaArtikle" style="margin: 10px auto">
        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaFiltereVidljiv = !prostorZaFiltereVidljiv " class="btn"><i class="fa fa-filter" aria-hidden="true"></i> Filteri </button>
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <br><br>

        <!-- Pretraga -->
        <div class="pretragaArtikala" v-if="prostorZaPretraguVidljiv" >
            <form method='post' >

                <input type="text" v-model="pretraga.naziv" v-bind:class="{filledInput: pretraga.naziv != '' }" placeholder="Naziv" >
                <input type="number" v-model="pretraga.cena" v-bind:class="{filledInput: pretraga.cena != '' }" placeholder="Cena" >

            </form>
        </div>
        <!-- Kraj pretrage -->
        
		
        <!-- Filtriranje artikala -->
        <div class="filterZaArtikle" v-if="prostorZaFiltereVidljiv">
            <form method='post' 
                <select v-model="podaciZaFiltriranjeArtikala.tip" @change="onchangeTipArtikla()">
                    <option value="">Bez filtera za tip</option>
                    <option>Jelo</option>
                    <option>Piće</option>
                </select>
            </form>
        </div>
        <!-- Kraj filtriranja artikala -->

        
        <!-- Sortiranje artikala -->
        <div v-if="prostorZaSortiranjeVidljiv" class="sortiranjeArtikli">
            <form method='post'>

                <button type="button" @click="sortirajNaziv"><i class="fa fa-sort" aria-hidden="true"></i> Naziv </button>
                <button type="button" @click="sortirajCena"><i class="fa fa-sort" aria-hidden="true"></i> Cena </button>

            </form>
        </div>
        <!-- Kraj sortiranja restorana -->

		<!-- Card za restoran -->
        <ul>
            <li v-for="artikal in filtriraniArtikli">
            	<div class="cardsArtikliDiv">
                <img class="logoRestorana" v-bind:src="dobaviPutanjuSlike(artikal)">

                <table class="cardsArtikli">
                    <tr>
                        <td> Naziv : </td>
                        <td> {{ artikal.naziv }} </td>
                    </tr>
                    <tr>
                        <td> Tip: </td>
                        <td> {{ artikal.tip }} </td>
                    </tr>
					<tr>
						<td> Cena: </td>
						<td> {{ artikal.cena }} </td>
					</tr>
					<tr>
						<td> Količina: </td>
						<td> {{ artikal.kolicina }}</td>
					</tr>
					<tr>
						<td> Opis: </td>
						<td> {{ artikal.opis }} </td>
					</tr>
                </table>

                <button type="button" v-if=" artikal.logickiObrisan == '0' " @click="izmeniArtikal(artikal)" class="izmenaStyle button" ><i class="fa fa-pencil" aria-hidden="true"></i>  Izmeni </button> <br>
                <button type="button" v-if=" artikal.logickiObrisan == '0' " @click="obrisiArtikal(artikal)" class="brisanjeStyle button" ><i class="fa fa-trash" aria-hidden="true"></i>  Obriši </button> <br>
            	</div>
            </li>
        </ul>
        <!-- Kraj card za artikle -->
		</div>
		<div v-show="!imaArtikle">
		<h2>Restoran trenutno nema proizvode u ponudi.</h2>
		</div>
		<!-- Kraj prikaza artikala -->		
		<!-- Pocetak prikaza komentara -->

		<!-- Kraj prikaza komentara -->
    </div>
    `,
    methods: {
        initForMap: function (lon,lat) {
        	var place = [parseFloat(lon),parseFloat(lat)];
        	var center = ol.proj.fromLonLat(place);
        	var point = new ol.geom.Point(center);
            const map = new ol.Map({
                target: 'map',
                view: new ol.View({
                    center: center,
                    zoom: 16
                }),
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    }),
                    new ol.layer.Vector({
      					source: new ol.source.Vector({
        				features: [new ol.Feature(point)],
      				}),
      					style: new ol.style.Style({
        					image: new ol.style.Circle({
          						radius: 8,
          						fill: new ol.style.Fill({color: 'red'}),
        					}),
      					}),
    				}),
                ],
            })
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
		// METODE ZA ARTIKLE
		poklapaSeSaPretragom: function (artikal) {

            // Naziv
            if (!artikal.naziv.toLowerCase().match(this.pretraga.naziv.toLowerCase()))
                return false;
            
            //cena
            if (parseFloat(artikal.cena) < parseFloat(this.pretraga.cena))
                return false;
			
            return true;
        },
		onchangeTipArtikla: function () {
            if (this.podaciZaFiltriranjeArtikala.tip == "" || this.podaciZaFiltriranjeArtikala.tip == "Bez filtera za tip") {
                axios
                    .get('rest/artikal/dobaviArtikleMenadzera')
                    .then(response => {
                        this.artikli = [];
                        response.data.forEach(el => {
                                this.artikli.push(el);
                        });
                        return this.artikli;
                    });

            } else {
                let filterArtikala= (this.artikli).filter(artikal => artikal.tip == this.podaciZaFiltriranjeArtikala.tip);
                this.artikli = filterArtikala;
            }
        },
        sortirajNaziv: function () {
            this.nazivBrojac ++;
            if(this.nazivBrojac % 3 == 0)
            {
                axios
                    .get('rest/artikal/dobaviArtikleMenadzera')
                    .then(response => {
                        this.artikli = [];
                        response.data.forEach(el => {
                                this.artikli.push(el);
                        });
                        return this.artikli;
                    });
            }else if(this.nazivBrojac % 3 == 1)
            {
                this.multisort(this.artikli, ['naziv', 'naziv'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.artikli, ['naziv', 'naziv'], ['DESC', 'ASC']);
            }  
        },
        sortirajCena: function () {
            this.cenaBrojac ++;
            if(this.cenaBrojac % 3 == 0)
            {
                axios
                    .get('rest/artikal/dobaviArtikleMenadzera')
                    .then(response => {
                        this.artikli = [];
                        response.data.forEach(el => {
                                this.artikli.push(el);
                        });
                        return this.artikli;
                    });
            }else if(this.cenaBrojac % 3 == 1)
            {
                this.multisort(this.artikli, ['cena', 'cena'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.artikli, ['cena', 'cena'], ['DESC', 'ASC']);
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
    },
    mounted() {
        axios.get('rest/slike/dobaviSlike').then(response => (this.slike = response.data));

        axios
            .get('rest/restoran/dobaviRestoranZaPrikaz')
            .then(response => {
                this.restoran = response.data;
                this.$nextTick(function () {
            		this.initForMap(this.restoran.lokacija.geografskaDuzina,this.restoran.lokacija.geografskaSirina);
        			})
                return this.restoran;
            });
		axios
            .get('rest/artikal/dobaviArtikleRestorana')
            .then(response => {
                this.artikli = response.data;
                if(response.data == "")
               	{
                		this.imaArtikle = false;
                }
                else
                {
                	this.imaArtikle = true;
                }
                return this.artikli,this.imaArtikle;
            });
    },
    computed: {
		filtriraniArtikli: function () {
            return this.artikli.filter((artikal) => {
                return this.poklapaSeSaPretragom(artikal);
            });
        }
    },
});
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