Vue.component("dostupne-porudzbine-dostavljac", {
    data() {
        return {
            porudzbine: [],
            pretraga: {
                restoran: 0,
				cena: '',
                datum : ''
            },
            podaciZaFiltriranje: {
                tipRestorana: "",
                statusPorudzbine: ""
            },
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false
        }
    },

     template: `
     <div id = "stilZaPorudzbine">
     <div>

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaFiltereVidljiv = !prostorZaFiltereVidljiv " class="btn"><i class="fa fa-filter" aria-hidden="true"></i> Filteri </button>
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <br><br>
     	
        <!-- Pretraga -->
        <div class="pretragaPorudzbina" v-if="prostorZaPretraguVidljiv" >
            <form method='post' >

                <input type="text" v-model="pretraga.restoran" v-bind:class="{filledInput: pretraga.restoran != 0 }" placeholder="Restoran" >
                <input type="text" v-model="pretraga.cena" v-bind:class="{filledInput: pretraga.cena != '' }" placeholder="Cena" >
                <input type="text" v-model="pretraga.datum" v-bind:class="{filledInput: pretraga.datum != '' }" placeholder="Datum" >           

            </form>
        </div>
        <!-- Kraj pretrage -->

        <!-- Filtriranje porudzbina -->
        <div class="filterZaPorudzbine" v-if="prostorZaFiltereVidljiv">
            <form method='post' 
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

                <select v-model="podaciZaFiltriranje.status" @change="onchangeStatusPorudzbine()">
                    <option value="">Bez filtera za tip</option>
                    <option>U PRIPREMI</option>
                    <option>CEKA DOSTAVLJACA</option>
                    <option>U TRANSPORTU</option>
                    <option>DOSTAVLJENA</option>
                </select>
            </form>
        </div>
        <!-- Kraj filtriranja porudzbina -->

        
        <div>
            <table class="styleForTable" style="width:80%">
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
                        <td> {{ porudzbina.id }} </td>
                        <td> {{ porudzbina.idRestorana }} </td>
                        <td> {{ porudzbina.vremePorudzbine }} </td>
                        <td> {{ porudzbina.imePrezimeKupca }}  </td> 
                        <td> {{ porudzbina.cena }}  </td>
                        <td> {{ porudzbina.status }}  </td>
                        <td> <button type="button" @click="transportujPorudzbinu(porudzbina)"><i class="fa fa-sort" aria-hidden="true"></i> Transportuj </button> </td>     
                    </tr>
                </tbody>                
            </table>
        </div>
     </div>


     </div>
     `,
     methods: {
        poklapaSeSaPretragom: function (porudzbina) {

            if (!porudzbina.restoran.match(this.pretraga.restoran))
                return false;

            if (!porudzbina.cena.match(this.pretraga.cena))
                return false;

            if (!porudzbina.datum.match(this.pretraga.datum))
                return false;

            return true;
        },
        onchangeTipRestorana: function () {
            if (this.podaciZaFiltriranje.tipRestorana == "") {
                axios.get('rest/Porudzbina/dobaviPorudzbineZaDostavu').then(response => (this.porudzbine = response.data));

            } else {
                let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.restoran == this.podaciZaFiltriranje.tipRestorana);
                this.porudzbine = filterPorudzbine;
            }
        },
        onchangeStatusPorudzbine: function(){
            if (this.podaciZaFiltriranje.statusPorudzbine == "") {
                axios.get('rest/Porudzbina/dobaviPorudzbineZaDostavu').then(response => (this.porudzbine = response.data));

            } else {
                let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.status == this.podaciZaFiltriranje.statusPorudzbine);
                this.porudzbine = filterPorudzbine;
            }
		},
        transportujPorudzbinu: function(porudzbina){
            axios
            .post('rest/Porudzbina/transportujPorudzbinu', porudzbina)
            .then(response => {
                this.porudzbine = [];
				response.data.forEach(el => this.porudzbine.push(el));

                return this.porudzbine;
            });
		},



     },
     mounted() {
             axios.get('rest/Porudzbina/dobaviPorudzbineZaDostavu').then(response => (this.porudzbine = response.data));
     },
     computed: {
        filtriranePorudzbine: function () {
            return this.porudzbine.filter((porudzbina) => {
                return this.poklapaSeSaPretragom(porudzbina);
            });
        }
     },
});

