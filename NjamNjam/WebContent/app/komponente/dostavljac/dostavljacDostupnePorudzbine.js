Vue.component("dostupne-porudzbine-dostavljac", {
    data() {
        return {
            porudzbine: [],
            pretraga: {
                restoran: 0,
				cena: 0,
                datum : 0
            },
            imaPorudzbine : false
        }
    },

     template: `
     <div id = "stilZaPorudzbine">
     	<div v-show="imaPorudzbine">
     	
     	

        <ul>
            <li v-for="poruzbina in porudzbine">
            	<div class="cardsPorudzbineDiv">

                <table class="cardsPorudzbina">
                    <tr>
                        <td> ID : </td>
                        <td> {{ porudzbina.id }} </td>
                    </tr>
                    <tr>
                        <td> Restoran: </td>
                        <td> {{ porudzbina.idRestorana }} </td>
                    </tr>
					<tr>
						<td> Vreme: </td>
						<td> {{ porudzbina.vremePorudzbine }}</td>
					</tr>
					<tr>
						<td> Kupac: </td>
						<td> {{ porudzbina.imePrezimeKupca }} </td>
					</tr>
					<tr>
						<td> Cena: </td>
						<td> {{ porudzbina.cena }} </td>
					</tr>
                </table>

            	</div>
            </li>
        </ul>
        
        <div class="styleForTable">
            <table style="width:100%">
                <thead>
                    <tr>
                        <th> ID </th> 
                        <th> Restoran </th>
                        <th> Vreme </th>
                        <th> Kupac </th>
                        <th> Cena </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="porudzbina in porudzbine">
                        <td> {{ porudzbina.id }} </td>
                        <td> {{ porudzbina.idRestorana }} </td>
                        <td>  {{ porudzbina.vremePorudzbine }} </td>
                        <td> {{ porudzbina.imePrezimeKupca }}  </td> 
                        <td> {{ porudzbina.cena }}  </td>
                        <td> --ZA DUGME-- </td>     
                    </tr>
                </tbody>                
            </table>
        </div>
        

        </div>
        <div v-show="!imaPorudzbine">
		    <h2>Trenutno nema porudzbina.</h2>
	    </div>
     </div>
     `,
     methods: {},
     mounted() {
             axios
            .get('rest/Porudzbina/dobaviPorudzbineZaDostavu')
            .then(response => {
                this.porudzbine = [];
        		if(Object.prototype.toString.call(response.data) === '[object Array]')
        		{
					response.data.forEach(el => this.porudzbine.push(el));
					this.imaPorudzbine = true;
				}else{
        			this.imaPorudzbine = false;
        		}
                return this.porudzbine;
            });
     },
     computed: {},
});

