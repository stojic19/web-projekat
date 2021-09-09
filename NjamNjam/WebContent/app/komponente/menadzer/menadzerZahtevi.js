Vue.component("menadzer-zahtevi", {
    data() {
        return {
            porudzbine: [],
            pretraga: {
				cenaOd: '',
                cenaDo: '',
                datumOd : '',
                datumDo : ''
            },
            cenaBrojac : 0,
            datumBrojac : 0,
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false,
			imaPorudzbine: false
        }
    },

    template: `
     <div id = "stilZaPorudzbine">
     <div v-show="imaPorudzbine">

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <br><br>
     	
        <!-- Pretraga -->
        <div class="pretragaPorudzbina" v-if="prostorZaPretraguVidljiv" >
            <form method='post' >

                <input type="number" min="0" v-model="pretraga.cenaOd" v-bind:class="{filledInput: pretraga.cenaOd != '' }" placeholder="Cena od" >
				<input type="number" min="0" v-model="pretraga.cenaDo" v-bind:class="{filledInput: pretraga.cenaDo != '' }" placeholder="Cena do" >
                <input type="text" v-model="pretraga.datumOd" v-bind:class="{filledInput: pretraga.datumOd != '' }" placeholder="Datum od" >
				<input type="text" v-model="pretraga.datumDo" v-bind:class="{filledInput: pretraga.datumDo != '' }" placeholder="Datum do" >            

            </form>
        </div>
        <!-- Kraj pretrage -->

        <!-- Sortiranje porudzbina -->
        <div v-if="prostorZaSortiranjeVidljiv" class="porudzbineSortiranje">
            <form method='post'>

                <button type="button" @click="sortirajCena"><i class="fa fa-sort" aria-hidden="true"></i> Cena</button>
                <button type="button" @click="sortirajDatum"><i class="fa fa-sort" aria-hidden="true"></i> Datum </button>

            </form>
        </div>
        <!-- Kraj sortiranja porudzbina -->
        
        <div v-for="porudzbina in filtriranePorudzbine">
            <table class="styleForTable" style="width:80%">
                <thead>
                    <tr>
                        <th> ID </th> 
                        <th> Vreme </th>
                        <th> Kupac </th>
                        <th> Cena </th>
                    </tr>
                </thead>
                <tbody>
                    <tr >
                        <td> {{ porudzbina.ID }} </td>
                        <td> {{ porudzbina.vremePorudzbine }} </td>
                        <td> {{ porudzbina.imePrezimeKupca }}  </td> 
                        <td> {{ porudzbina.cena }}  </td>					   
                    </tr>
				
				<div v-show="porudzbina.dostavljaci.length > 0">
				<h2>Zahtevi za dostavu:</h2>
				<table class="styleForTable" style="width:80%">
                <thead>
                    <tr>
                        <th> Korisničko ime </th> 
                        <th> Ime </th>
                        <th> Prezime </th>
                        <th> Akcija </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="korisnik in porudzbina.dostavljaci">
                        <td> {{ korisnik.korisnickoIme }} </td>
                        <td> {{ korisnik.ime }} </td>
                        <td> {{ korisnik.prezime }}  </td> 
                        <td> <button type="button" class="btn" @click="izaberiDostavljaca(porudzbina, korisnik)"><i class="fa fa-sign-in" aria-hidden="true"></i> Izaberi </button></td> 					   
                    </tr>
					
                </tbody> 
				</table>
				</div>
				<div v-show="porudzbina.dostavljaci.length == 0">
				<h2>Trenutno nema zahteva za prikazivanje.</h2>
				</div>
                </tbody>                
            </table>
        </div>
     </div>
	<div v-show="!imaPorudzbine">
		<h2>Trenutno nema porudžbina za prikazivanje.</h2>
	</div>
     </div>
     `,
    methods: {
        poklapaSeSaPretragom: function (porudzbina) {
            if (porudzbina.cena < this.pretraga.cenaOd)
                return false;

            if(this.pretraga.cenaDo != ''){
                if(porudzbina.cena > this.pretraga.cenaDo) 
                    return false;
			}

            var minParts = this.pretraga.datumOd.split('-');
            var maxParts = this.pretraga.datumDo.split('-');
            var datmin = new Date(minParts[2], minParts[1], minParts[0]);
            var datmax = new Date(maxParts[2], maxParts[1], maxParts[0]);
            var datpor = new Date(porudzbina.vremePorudzbine);
            if (datpor < datmin || datpor > datmax)
                return false;

            return true;
        },
        izaberiDostavljaca: function(porudzbina, dostavljac){	
		porudzbina.dostavljaci = [];
		porudzbina.dostavljaci = [dostavljac];
            axios.post('rest/Porudzbina/transportujPorudzbinu', {porudzbina})
            		.then(response => {
                        toastr["success"]("Uspešno dodeljena porudžbina " + porudzbina.ID + "." , "Uspešno dodeljena!");
                        this.porudzbine = response.data;
                    })
                    .catch(err =>{ 
                    console.log(err);
                    toastr["error"]("Neuspešna dodela!", "Greška");
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
            .get('rest/Porudzbina/dobaviZahteve')
            .then(response => {
				this.porudzbine = response.data;
				console.log(response.data);
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