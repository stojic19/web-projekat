Vue.component("kupac-porudzbine", {
    data() {
        return {
            porudzbine: [],
            pretraga: {
                imeRestorana: '',
				cenaOd: '',
				cenaDo:'',
                datumOd : '',
				datumDo:''
            },
            podaciZaFiltriranje: {
                tipRestorana: "",
                statusPorudzbine: ""
            },
            restoranBrojac : 0,
            cenaBrojac : 0,
            datumBrojac : 0,
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false,
			imaPorudzbine: false,
			komentarisanjeSakriveno: true,
			porudzbinaZaKomentar:{},
			komentar:{}
        }
    },

    template: `
     <div id = "stilZaPorudzbine">
     <div v-show="imaPorudzbine">

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaFiltereVidljiv = !prostorZaFiltereVidljiv " class="btn"><i class="fa fa-filter" aria-hidden="true"></i> Filteri </button>
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <br><br>
     	
        <!-- Pretraga -->
        <div class="pretragaPorudzbina" v-if="prostorZaPretraguVidljiv" >
            <form method='post' >

                <input type="text" v-model="pretraga.imeRestorana" v-bind:class="{filledInput: pretraga.imeRestorana != '' }" placeholder="Naziv restorana" >
                <input type="number" min="0" v-model="pretraga.cenaOd" v-bind:class="{filledInput: pretraga.cenaOd != '' }" placeholder="Cena od" >
				<input type="number" min="0" v-model="pretraga.cenaDo" v-bind:class="{filledInput: pretraga.cenaDo != '' }" placeholder="Cena do" >
                <input type="text" v-model="pretraga.datumOd" v-bind:class="{filledInput: pretraga.datumOd != '' }" placeholder="Datum od" >
				<input type="text" v-model="pretraga.datumDo" v-bind:class="{filledInput: pretraga.datumDo != '' }" placeholder="Datum do" >            

            </form>
        </div>
        <!-- Kraj pretrage -->

        <!-- Filtriranje porudzbina -->
        <div class="filterZaPorudzbine" v-if="prostorZaFiltereVidljiv">
            <form method='post'>
                <select v-model="podaciZaFiltriranje.tipRestorana" @change="onchangeTipRestorana()">
                    <option value="">Bez filtera za tip restorana</option>
                    <option>Brza hrana</option>
                    <option>Roštilj</option>
					<option>Burgeri</option>
					<option>Italijanski</option>
					<option>Picerija</option>
                    <option>Kineski</option>
                    <option>Azijska kuhinja</option>
                </select>

                <select v-model="podaciZaFiltriranje.statusPorudzbine" @change="onchangeStatusPorudzbine()">
                    <option value="">Bez filtera za status</option>
					<option>Nedostavljene</option>
					<option>OBRADA</option>
                    <option>U PRIPREMI</option>
                    <option>CEKA DOSTAVLJACA</option>
                    <option>U TRANSPORTU</option>
                    <option>DOSTAVLJENA</option>
                </select>
            </form>
        </div>
        <!-- Kraj filtriranja porudzbina -->

        <!-- Sortiranje porudzbina -->
        <div v-if="prostorZaSortiranjeVidljiv" class="porudzbineSortiranje">
            <form method='post'>

                <button type="button" @click="sortirajRestoran"><i class="fa fa-sort" aria-hidden="true"></i> Restoran </button>
                <button type="button" @click="sortirajCena"><i class="fa fa-sort" aria-hidden="true"></i> Cena</button>
                <button type="button" @click="sortirajDatum"><i class="fa fa-sort" aria-hidden="true"></i> Datum </button>

            </form>
        </div>
        <!-- Kraj sortiranja porudzbina -->
        
        <div>
            <table class="stilZaTabelu" style="width:80%">
                <thead>
                    <tr>
                        <th> ID </th> 
                        <th> Restoran </th>
                        <th> Vreme </th>
                        <th> Kupac </th>
                        <th> Cena </th>
                        <th> Status </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="porudzbina in filtriranePorudzbine">
                        <td> {{ porudzbina.ID }} </td>
                        <td> {{ porudzbina.imeRestorana }} </td>
                        <td> {{ porudzbina.vremePorudzbine }} </td>
                        <td> {{ porudzbina.imePrezimeKupca }}  </td> 
                        <td> {{ porudzbina.cena }}  </td>
                        <td> {{ porudzbina.status }}  </td>
                        <td v-show=" porudzbina.status == 'OBRADA'"> <button type="button" class="brisanjeStyle button" v-if=" porudzbina.status == 'OBRADA'" @click="otkaziPorudzbinu(porudzbina)"><i class="fa fa-trash" aria-hidden="true"></i> Otkaži </button></td> 
                        <td v-show=" porudzbina.status == 'DOSTAVLJENA' && porudzbina.poslatZahtev == '0' "> <button type="button" class="brisanjeStyle button" v-if=" porudzbina.status == 'DOSTAVLJENA' && porudzbina.poslatZahtev == '0' " @click="ostaviKomentar(porudzbina)"><i class="fa fa-sign-in" aria-hidden="true"></i> Ostavi komentar </button></td>     
                    </tr>
                </tbody>                
            </table>
        </div>
        
         <!-- Modalni dijalog za komenarisanje -->
        <div id = "dijalogZaKomentarisanje" v-bind:class="{bgModal: komentarisanjeSakriveno, bgModalShow: !komentarisanjeSakriveno}">
            <div class="modal-contents">
        
                <div class="close" @click="komentarisanjeSakriveno = !komentarisanjeSakriveno">+</div>

                <form method='post'>
                    
					<label for="tekst">Tekst:</label>
                    <input name="tekst" type="text" v-model="komentar.tekst"  placeholder="Tekst" required>

                    <label for="ocena">Ocena:</label>
                    <input name="ocena" min="1" max="5" type="number" v-model="komentar.ocena"  placeholder="Ocena" required>
                                  
                    <button type="button" @click="dodajKomentar" class="btn">Potvrdi</button>
                    <button type="button" @click="komentarisanjeSakriveno = !komentarisanjeSakriveno" class="btn">Odustani</button>

                </form>
            </div>
        </div> 
        <!-- Kraj modalnog dijaloga -->
        
     </div>
	<div v-show="!imaPorudzbine">
		<h2>Trenutno nema porudžbina za prikazivanje.</h2>
	</div>
     </div>
     `,
    methods: {
    	dodajKomentar: function(){
    		if (!this.komentar.tekst || !this.komentar.ocena) {
                toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                return;
            }
            if(isNaN(this.komentar.ocena))
            {
            	toastr["warning"]("Ocena sme biti samo broj!", "Proverite unos!");
                return;
            }
            if(this.komentar.ocena > 5)
            	this.komentar.ocena = 5;
            if(this.komentar.ocena < 1)
            	this.komentar.ocena = 1;
    		axios.post('rest/Porudzbina/dodajKomentar', {
						"idPorudzbine": this.porudzbinaZaKomentar.ID,
                        "idRestorana": this.porudzbinaZaKomentar.idRestorana,
                        "tekst": this.komentar.tekst,
                        "ocena": this.komentar.ocena,
					})
            		.then(response => {
                        toastr["success"]("Uspešno ostavljen komentar na porudžbinu " + this.porudzbinaZaKomentar.ID + "." , "Uspešno komentarisanje!");
                        this.porudzbine = response.data;
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešno komentarisanje!", "Greška");
                })
               this.komentar = {};
               this.komentarisanjeSakriveno = true;
    	},
    	ostaviKomentar: function(porudzbina){
			this.komentarisanjeSakriveno = false;
			this.porudzbinaZaKomentar = porudzbina;
		},
        poklapaSeSaPretragom: function (porudzbina) {
			if (!porudzbina.imeRestorana.match(this.pretraga.imeRestorana))
                return false;

            if (porudzbina.cena < this.pretraga.cenaOd)
                return false;

            if(this.pretraga.cenaDo != ''){
                if(porudzbina.cena > this.pretraga.cenaDo) 
                    return false;
			}

            var minParts = this.pretraga.datumOd.split('-');
            var maxParts = this.pretraga.datumDo.split('-');
            var datporParts = porudzbina.vremePorudzbine.split(' ');
            var datporDate = datporParts[0].split('/');
            var datmin = new Date(minParts[2], minParts[1] - 1, minParts[0]);
            var datmax = new Date(maxParts[2], maxParts[1] - 1, maxParts[0]);
            var datpor = new Date(datporDate[2], datporDate[1] - 1, datporDate[0]);
            if (datpor < datmin || datpor > datmax)
                return false;

            return true;
        },
        onchangeTipRestorana: function () {
            if (this.podaciZaFiltriranje.tipRestorana == "" || this.podaciZaFiltriranje.tipRestorana == "Bez filtera za tip restorana") {
                axios.get('rest/Porudzbina/dobaviPorudzbineKupca').then(response => (this.porudzbine = response.data));

            } else {
                let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.tipRestorana == this.podaciZaFiltriranje.tipRestorana);
                this.porudzbine = filterPorudzbine;
            }
        },
        onchangeStatusPorudzbine: function(){
            if (this.podaciZaFiltriranje.statusPorudzbine == "" || this.podaciZaFiltriranje.statusPorudzbine == "Bez filtera za status") {
                axios.get('rest/Porudzbina/dobaviPorudzbineKupca').then(response => (this.porudzbine = response.data));

            } else if( this.podaciZaFiltriranje.statusPorudzbine == "Nedostavljene"){
            	let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.status != "DOSTAVLJENA");
                this.porudzbine = filterPorudzbine;
            }
             else {
                let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.status == this.podaciZaFiltriranje.statusPorudzbine);
                this.porudzbine = filterPorudzbine;
            }
		},
        otkaziPorudzbinu: function(porudzbina){
            axios.post('rest/Porudzbina/otkaziPorudzbinu', {porudzbina})
            		.then(response => {
                        toastr["success"]("Uspešno otkazana porudžbina " + porudzbina.ID + "." , "Uspešno otkazivanje!");
                        this.porudzbine = response.data;
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešno otkazivanje!", "Greška");
                })
		},
        sortirajRestoran: function () {
            this.restoranBrojac ++;
            if(this.restoranBrojac % 3 == 0)
            {
                axios.get('rest/Porudzbina/dobaviPorudzbineKupca').then(response => (this.porudzbine = response.data));
            }else if(this.restoranBrojac % 3 == 1)
            {
                this.multisort(this.porudzbine, ['imeRestorana', 'imeRestorana'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.porudzbine, ['imeRestorana', 'imeRestorana'], ['DESC', 'ASC']);
            }  
        },
        sortirajCena: function () {
            this.cenaBrojac ++;
            if(this.cenaBrojac % 3 == 0)
            {
                axios.get('rest/Porudzbina/dobaviPorudzbineKupca').then(response => (this.porudzbine = response.data));
            }else if(this.cenaBrojac % 3 == 1)
            {
                this.multisort(this.porudzbine, ['cena', 'cena'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.porudzbine, ['cena', 'cena'], ['DESC', 'ASC']);
            }  
        },
        sortirajDatum: function () {
            this.datumBrojac ++;
            if(this.datumBrojac % 3 == 0)
            {
                axios.get('rest/Porudzbina/dobaviPorudzbineKupca').then(response => (this.porudzbine = response.data));
            }else if(this.datumBrojac % 3 == 1)
            {
                this.multisort(this.porudzbine, ['vremePorudzbine', 'vremePorudzbine'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.porudzbine, ['vremePorudzbine', 'vremePorudzbine'], ['DESC', 'ASC']);
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
             axios
            .get('rest/Porudzbina/dobaviPorudzbineKupca')
            .then(response => {
				this.porudzbine = response.data;
                if(response.data == "")
               	{
                		this.imaPorudzbine = false;
                }
                else
                {
                	this.imaPorudzbine= true;
                }
                return this.porudzbine,this.imaPorudzbine;
            });
     },
     computed: {
        filtriranePorudzbine: function () {
            return this.porudzbine.filter((porudzbina) => {
                return this.poklapaSeSaPretragom(porudzbina);
            });
        }
     },
});