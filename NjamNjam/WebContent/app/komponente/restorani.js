Vue.component("restorani", {
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
            	<h1> {{ restoran.naziv }} </h1>
            	<h2 v-show="restoran.status == 'Ne radi'">Trenutno ne radi</h2>
                <img class="logoRestorana" v-bind:src="dobaviPutanjuSlike(restoran)">

                <table class="cardsRestoran">
	                <tr>
                        <td> Prosečna ocena: </td>
                        <td> {{ restoran.prosecnaOcena }} </td>
                    </tr>
                    <tr>
                        <td> Tip: </td>
                        <td> {{ restoran.tip }} </td>
                    </tr>

					<tr>
						<td> Lokacija: </td>
						<td> {{ restoran.lokacija && restoran.lokacija.adresa.mesto }}, {{ restoran.lokacija && restoran.lokacija.adresa.ulica }}, {{ restoran.lokacija && restoran.lokacija.adresa.broj }}</td>
					</tr>
                </table>

                <button type="button" v-if=" restoran.logickiObrisan == '0' " @click="pregledRestorana(restoran)" style="margin-bottom:10px" class="button">Pregled ponude</button> <br>
            	</div>
            </li>
        </ul>
        <!-- Kraj card za restorane -->

    </div>
    `,
    methods: {
    	pregledRestorana: function(restoran){
    		axios
                .post('rest/restoran/restoranZaPregled', {restoran})
         		.then(response => {router.push("/pregledPonude")});
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
            if (parseFloat(restoran.prosecnaOcena) < parseFloat(this.pretraga.prosecnaOcena))
                return false;

            return true;
        },
		onchangeTipRestorana: function () {
            if (this.podaciZaFiltriranjeRestorana.tip == "" || this.podaciZaFiltriranjeRestorana.tip == "Bez filtera za tip") {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                		this.restorani = response.data;
                		this.restorani.sort(dynamicSort("status"));
                		return this.restorani;
                    });

            } else {
                let filterRestorana = (this.restorani).filter(restoran => restoran.tip == this.podaciZaFiltriranjeRestorana.tip);
                this.restorani = filterRestorana;
            }
        },
        onchangeAktivnostRestorana: function () {
            if (this.podaciZaFiltriranjeRestorana.otvoreni == "" || this.podaciZaFiltriranjeRestorana.otvoreni == "Svi restorani") {
                axios
                    .get('rest/restoran/dobaviRestorane')
                    .then(response => {
                        this.restorani = [];
                		this.restorani = response.data;
                		this.restorani.sort(dynamicSort("status"));
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
                		this.restorani = response.data;
                		this.restorani.sort(dynamicSort("status"));
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
                		this.restorani = response.data;
                		this.restorani.sort(dynamicSort("status"));
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
                		this.restorani = response.data;
                		this.restorani.sort(dynamicSort("status"));
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
            let imageDefault = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAdVBMVEUAAAD///8cHBzz8/Po6Oj5+fkvLy/4+PgXFxd6enp2dnYqKip+fn4wMDCYmJj8/PympqZEREQKCgrd3d0lJSXV1dXm5uaHh4e4uLja2tqenp5bW1vOzs7ExMSvr6+Pj49sbGxBQUFNTU1eXl6Li4tvb29SUlLxh18RAAAFPUlEQVR4nO3da1fiMBAGYCLXXYRyEUVRRFz9/z9xZZG1STOhMNPkTc+8X+lJeM70nrTtmEC60/moEy+L/VM39HeuSyfw2zSi7pRZTOHvBMBO5yGeMEUFD7mNJewmAooTSWGqEooTSeE+nVCWSArvEgpFiaTwJqVQkggqFCSiCuWIsEIxIq5QiggsFCIiC2WI0EIRIrZQggguFCCiCMmTRDYRRdgbU79wiSjCYb+pKqIIu2ZIVvFXS4QBIquKQELTH1C/cqqIJAwQGVWEEpqigSpiCZtYUcGEDVQRTShfRTiheBXxhKY/oRa6iggoFF5REYWyVYQUilYRUyi5uwEVClYRVShXRVihWBVxhVJVBBYGDhq/WyKUWVGhhSJVxBZKVBFcGNjd1K0iupBfRXihKcibjPWqiC80Q96KmoGQWcUchIFtsQYxCyGLmIeQQ8xEaPpXb4u5CK8/9GcjDAy+hYn5CK89aGQkvPLQn5Pwut1NVsJAFennNPISBo6L/ZYIaSJZxNyE5La4aY2QOvQP2iMkDv03LRL6D/2tEprCc5MRXrjsXZJl9cFPeCE7KlQhflSoQvyoUIX4UaEK8aNCFeJHhSrEjwpViB8VqhA/KlQhflQYS3gzmNw11HJ64Wa26h0nTBS959lGuvnUwttVpYOXnWgPSYWjbeHtYij59smUwi3VxVdexXpJJ9wNA0Bjep9C/SQTvgR9h8xkOkokHN2fBRrzKNFTIuEkvIae0pN4pXYS4cC/C61G4p3FKYSjehU85D5PYZ1t8JSXHIXrC4DG3OYnfPe2Oux2u/6Vl9tffGGv2uR6d5waOvnwHCW562l0YeX1+MMH6/fXSofMk5voQnc25dZdYOHWccXrMLbww2ntw7OMW0byyfRaiS10Lgd33oWc18zxTlAjC0d2W9Q1kn1dxTuziSy0q0NvYfYO943TZWShfbSnN7CdtdwDuVyNRBZaJ2zPgQWtXe6a02VkofUQUuiEzHqWZ8npMq5wYbVEPoz1lc/ygj1Gl5GF43JDQ5klzyWu8K7+/7Yukhldxt4Oyw0Ft65FrsLSi1W6waPcpNxlweky+pn34JjJmQ/yWO+YyWhPUz/WBQbrtiKq0OqycoV1SUCF9sO7rE/ZYQrndpesG8OYQvtGAO/uPqTQuaHKurRAFM7dl7LymsMTVt4QwBxlAxO+zaoveWA2CSR8n649d4vNH2azMMJXYkCKPUwKIhz4qifTHYbwjerM7NhtYwjJd8hwN8IOiJCclyExcwhBuKe64p3MfAdBSAwKF+8irSMI/a8vWS1kWkcQegvIHr8/BVNYPMm1DiBcuD0sBY4RP8ETroU/iw0nZI0z+QInFG8eTijdugpVKJL2C8vXTqwBbW8QhJufscLwkNtVQRB+/YvRMY20DSFsMipUIX5UqEKpjPabucQzQNVgCOfP/5p+lj/egwh/hgwF78+cgiAsz3mWJwII7en7MreBSwEQ2gNr4hcX6YWfTvvSe5v0wqnTvshwTCnphe7cC+l9TXrhU+uF7tcKWdP0PEkvdB4UMkKDav+TXugMkLbxrr41Lb8v3jqCsDSbtGjpvbbx8rvtxwYahxB2Ovvt43K1nTfRNIiwwahQhfhRoQrxo0IV4keFKsSPClWIHxWqED8qVCF+VKhC/KhQhfhRoQrxo0IV4keFKsSPCqsRfpi88YwpCCncp/7LF8adg3xeKPm1qRhx56+eF0p8+yVm/G9OCQkzKyJZwoBQ/pGIBrOjGQGh+HT65lL9Hkg9obmfNvEMlnTmU/ItcIf8BW1BWM/iDfpOAAAAAElFTkSuQmCC";
            // Default image
            if (restoran.putanjaDoSlike == "nema")
                return imageDefault;

            let slika;

            slika = this.dobaviBase64odId(parseInt(restoran.putanjaDoSlike, 10));

            return slika;
        },
        dobaviBase64odId: function (idSlike) {
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
                this.restorani = response.data;
                this.restorani.sort(dynamicSort("status"));
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
function dynamicSort(property) {
    var sortOrder = 1;
    if(property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }
    return function (a,b) {
        /* next line works with strings and numbers, 
         * and you may want to customize it to your needs
         */
        var result = (a[property] > b[property]) ? -1 : (a[property] < b[property]) ? 1 : 0;
        return result * sortOrder;
    }
}