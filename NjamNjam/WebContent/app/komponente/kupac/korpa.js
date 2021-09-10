toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": true,
    "progressBar": true,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}
Vue.component("korpa", {
    data() {
        return {
			slike: [],
			artikli:[],
			korpa:{},
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
			imaArtikle: false,
			cenaSaPopustom: 0,
			brojOsvojenihBodova: 0
        }
    },

    template: `
    <div id = "stilZaPregledRestorana">
        <h1>Korpa</h1>
        <!-- Pocetak prikaza artikala -->
		<div v-show="imaArtikle" style="margin: 10px auto">
		<br>
		<h1>Sadržaj</h1>
		<br>
		<div>
			<h2> Cena : {{korpa.cena}} din</h2>
			<h2> Popust: {{korpa.popust}}% </h2>
			<h2> Cena sa popustom : {{cenaSaPopustom}} din</h2>
			<h2> Broj osvojenih bodova : {{brojOsvojenihBodova}} </h2>
		</div>
		<br>
		<div>
		<button type="button" @click="ukloniSve()" class="brisanjeStyle btn" ><i class="fa fa-trash" aria-hidden="true"></i>  Ukloni sve </button>
		<button type="button" @click="poruci()" class="btn"><i class="fa fa-plus" aria-hidden="true"></i> Poruči </button>
		</div>
		<br>
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
        <!-- Kraj sortiranja artikala -->

		<!-- Card za artikal -->
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
				<div class="kolicina">
      				<input @change="azuriranaKolicina(artikal)" type="number" min="1" v-model="artikal.kolicinaZaKupovinu">
    			</div>
				<button type="button" @click="ukloniArtikal(artikal)" class="brisanjeStyle button" ><i class="fa fa-trash" aria-hidden="true"></i>  Ukloni </button> <br>
			  </div>
            </li>
        </ul>
        <!-- Kraj card za artikle -->
		</div>
		<div v-show="!imaArtikle">
		<br>
		<h2>U korpi trenutno nema artikala.</h2>
		<br>
		</div>
		<!-- Kraj prikaza artikala -->	
    </div>
    `,
    methods: {
    	poruci: function(){
    		axios
                    .get('rest/korpa/poruci')
                    .then(response => {
                        toastr["success"]("Porudžbine uspešno poslate. " , "Uspešno ažuriranje!");
                		if(response.data == "")
               			{
                			this.imaArtikle = false;
                		}
                		else
                		{
                			this.artikli = response.data;
                			this.imaArtikle = true;
                		}
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešno poručivanje!", "Greška");
                })
                this.dobaviKorpu();
    	},
    	ukloniSve: function(){
    		axios
                    .get('rest/korpa/ukloniSveIzKorpe')
                    .then(response => {
                        toastr["success"]("Uspešno uklonjeni svi artikli. " , "Uspešno ažuriranje!");
                		if(response.data == "")
               			{
                			this.imaArtikle = false;
                		}
                		else
                		{
                			this.artikli = response.data;
                			this.imaArtikle = true;
                		}
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešno ažuriranje!", "Greška");
                })
                this.dobaviKorpu();
    	},
    	ukloniArtikal: function (artikal){
    		axios
                    .post('rest/korpa/ukloniArtikalIzKorpe', 
                        artikal
                    )
                    .then(response => {
                        toastr["success"]("Uspešno uklonjen artikal " + artikal.naziv + "." , "Uspešno ažuriranje!");
                        this.artikli = response.data;
                		if(response.data == "")
               			{
                			this.imaArtikle = false;
                		}
                		else
                		{
                			this.imaArtikle = true;
                		}
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešno ažuriranje!", "Greška");
                })
                this.dobaviKorpu();
    	},
		azuriranaKolicina: function (artikal){
			axios
                    .post('rest/korpa/izmeniKolicinuArtikla', 
                        artikal
                    )
                    .then(response => {
                        toastr["success"](response.data , "Uspešno ažuriranje!");
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešno ažuriranje!", "Greška");
                })
                this.dobaviKorpu();
		},
		dobaviKorpu: function(){
		axios
            .get('rest/korpa/dobaviKorpu')
            .then(response => {
				this.korpa = response.data;
				this.cenaSaPopustom = ((100 - this.korpa.popust) / 100) * this.korpa.cena;
				this.brojOsvojenihBodova = this.korpa.cena /1000 * 133;
            });
		},
        dobaviPutanjuSlike: function (restoran) {
            let imageDefault = "data:image/png;base64,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";
            // Default image
            if (restoran.putanjaDoSlike == "nema")
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
            .get('rest/artikal/dobaviArtikleIzKorpe')
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
            
        axios
            .get('rest/korpa/dobaviKorpu')
            .then(response => {
				this.korpa = response.data;
				this.cenaSaPopustom = ((100 - this.korpa.popust) / 100) * this.korpa.cena;
				this.brojOsvojenihBodova = this.korpa.cena /1000 * 133;
                return this.korpa, this.cenaSaPopustom, this.brojOsvojenihBodova;
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