Vue.component("admin-komentari", {
    data() {
        return {
            komentariCekanje: [],
			komentariOstali:[],
			id: 0
        }
    },
    template: `
    <div id = "stilZaPregledRestorana" >

        <!-- Komentari koji cekaju na odobrenje -->
        <div v-show="komentariCekanje.length > 0">
            <table class="styleForTable">
                <thead>
                    <tr>
                        <th> Tekst </th>
                        <th> Ocena </th>
                        <th> Opcije </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="komentar in komentariCekanje">
                        <td> {{ komentar.tekst }} </td>
                        <td> {{ komentar.ocena }} </td>
						<td><button type="button" @click="prihvatiKomentar(komentar)"><i class="fa fa-check" aria-hidden="true"></i> Odobri </button></td>
						<td><button type="button" @click="odbijKomentar(komentar)" class="blockUser"><i class="fa fa-ban" aria-hidden="true"></i> Odbij </button></td> 
                    </tr>
                </tbody>                
            </table>
        </div>
		<!-- Ostali komentari -->
        <div>
            <table class="styleForTable">
                <thead>
                    <tr>
                        <th> Tekst </th>
                        <th> Ocena </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="komentar in komentariOstali">
                        <td> {{ komentar.tekst }} </td>
                        <td> {{ komentar.ocena }} </td>
						<td> {{ komentar.status }} </td>
                    </tr>
                </tbody>                
            </table>
        </div>
    </div>

    `,
	methods:{
		odbijKomentar: function(komentar){
			axios
                .post('rest/komentar/odbijKomentar', {komentar})
                .then(response => {
                    this.komentariCekanje = [];
            		this.komentariOstali = [];
                    response.data.forEach(el => {
                        if(el.status == "CEKANJE")
                    	this.komentariCekanje.push(el);
					else
						this.komentariOstali.push(el);
                    });
                    toastr["success"]("Uspešno ste odbili komentar!", "Uspeh!");
                    return this.komentariCekanje ,this.komentariOstali;
                });
		},
		prihvatiKomentar: function(komentar){
			axios
                .post('rest/komentar/prihvatiKomentar', {komentar})
                .then(response => {
                    this.komentariCekanje = [];
            		this.komentariOstali = [];
                    response.data.forEach(el => {
                        if(el.status == "CEKANJE")
                    	this.komentariCekanje.push(el);
					else
						this.komentariOstali.push(el);
                    });
                    toastr["success"]("Uspešno ste odobrili komentar!", "Uspeh!");
                    return this.komentariCekanje ,this.komentariOstali;
                });
		},
	},
    mounted() {
        axios
            .get('rest/komentar/dobaviKomentare')
            .then(response => {
                response.data.forEach(el => {
					if(el.status == "CEKANJE")
                    	this.komentariCekanje.push(el);
					else
						this.komentariOstali.push(el);
                });
                return this.komentariCekanje ,this.komentariOstali;
            });
    },
});