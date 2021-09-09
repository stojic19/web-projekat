Vue.component("moje-porudzbine-dostavljac", {
    data() {
        return {
            porudzbine: [],
            pretraga: {
                imeRestorana: '',
				minCena: '',
                maxCena: '',
                minDatum : '',
                maxDatum : ''
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

                <input type="text" v-model="pretraga.imeRestorana" v-bind:class="{filledInput: pretraga.imeRestorana != '' }" placeholder="Restoran" >
                <input type="text" v-model="pretraga.minCena" v-bind:class="{filledInput: pretraga.minCena != '' }" placeholder="Minimalna cena" >
                <input type="text" v-model="pretraga.maxCena" v-bind:class="{filledInput: pretraga.maxCena != '' }" placeholder="Maksimalna cena" >
                <input type="text" v-model="pretraga.minDatum" v-bind:class="{filledInput: pretraga.minDatum != '' }" placeholder="Od (format: DD-MM-YYYY)" >
                <input type="text" v-model="pretraga.maxDatum" v-bind:class="{filledInput: pretraga.maxDatum != '' }" placeholder="Do (format: DD-MM-YYYY)" >         

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

        <!-- Sortiranje porudzbina -->
        <div v-if="prostorZaSortiranjeVidljiv" class="sortiranjePorudzbine">
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
                        <td> {{ porudzbina.id }} </td>
                        <td> {{ porudzbina.imeRestorana }} </td>
                        <td> {{ porudzbina.vremePorudzbine }} </td>
                        <td> {{ porudzbina.imePrezimeKupca }}  </td> 
                        <td> {{ porudzbina.cena }}  </td>
                        <td> {{ porudzbina.status }}  </td>
                        <td v-show=" porudzbina.status == 'U TRANSPORTU' "> <button type="button" class="btn" v-if=" porudzbina.status == 'U TRANSPORTU'" @click="dostaviPorudzbinu(porudzbina)"><i class="fa fa-sign-in" aria-hidden="true"></i> Dostavljena </button></td>    
                    </tr>
                </tbody>                
            </table>
        </div>
     </div>


     </div>
     `,
    methods: {
        poklapaSeSaPretragom: function (porudzbina) {

            if (!porudzbina.imeRestorana.match(this.pretraga.imeRestorana))
                return false;

            if (porudzbina.cena < this.pretraga.minCena)
                return false;

                        if(this.pretraga.maxCena != ''){
                if(porudzbina.cena > this.pretraga.maxCena) 
                    return false;
			}

            var minParts = this.pretraga.minDatum.split('-');
            var maxParts = this.pretraga.maxDatum.split('-');
            var datporParts = porudzbina.vremePorudzbine.split(' ');
            var datporDate = datporParts[0].split('/');
            var datmin = new Date(minParts[2], minParts[1] - 1, minParts[0]);
            var datmax = new Date(maxParts[2], maxParts[1] - 1, maxParts[0]);
            var datpor = new Date(datporDate[2], datporDate[1] - 1, datporDate[0]);
            console.log(datmin);
            console.log(datpor);
            if (datpor < datmin || datpor > datmax)
                return false;

            return true;
        },
        onchangeTipRestorana: function () {
            if (this.podaciZaFiltriranje.tipRestorana == "") {
                axios.get('rest/Porudzbina/dobaviNedostavljenePorudzbine').then(response => (this.porudzbine = response.data));

            } else {
                let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.tipRestorana == this.podaciZaFiltriranje.tipRestorana);
                this.porudzbine = filterPorudzbine;
            }
        },
        onchangeStatusPorudzbine: function(){
            if (this.podaciZaFiltriranje.statusPorudzbine == "") {
                axios.get('rest/Porudzbina/dobaviNedostavljenePorudzbine').then(response => (this.porudzbine = response.data));

            } else {
                let filterPorudzbine = (this.porudzbine).filter(porudzbina => porudzbina.status == this.podaciZaFiltriranje.statusPorudzbine);
                this.porudzbine = filterPorudzbine;
            }
		},
        dostaviPorudzbinu: function(porudzbina){
            axios.post('rest/Porudzbina/dostaviPorudzbinu', {porudzbina}).then(response => (this.porudzbine = response.data));
		},
        sortirajRestoran: function () {
            this.restoranBrojac ++;
            if(this.restoranBrojac % 3 == 0)
            {
                axios.get('rest/Porudzbina/dobaviNedostavljenePorudzbine').then(response => (this.porudzbine = response.data));
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
                axios.get('rest/Porudzbina/dobaviNedostavljenePorudzbine').then(response => (this.porudzbine = response.data));
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
                axios.get('rest/Porudzbina/dobaviNedostavljenePorudzbine').then(response => (this.porudzbine = response.data));
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
             axios.get('rest/Porudzbina/dobaviPorudzbineDostavljaca').then(response => (this.porudzbine = response.data));
     },
     computed: {
        filtriranePorudzbine: function () {
            return this.porudzbine.filter((porudzbina) => {
                return this.poklapaSeSaPretragom(porudzbina);
            });
        }
     },
});