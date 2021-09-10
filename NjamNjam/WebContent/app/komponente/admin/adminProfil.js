/**
 * Settings for toastr.
 */
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


Vue.component("admin-profil", {
    data() {
        return {
            korisnik: {},
            izmeneProfila: {
                lozinka: '',
                ime: '',
                prezime: '',
                datum: '',
                pol:''
            },
            dijalogZaIzmenuSakriven: true
        }
    },
    template: `
    <div id = "stilZaProfil">
    	<div class="cardsRestoranPregledDiv">
        <h1> Pozdrav {{korisnik.korisnickoIme}} ! </h1>
        
        <table class="cardsRestoranPregled">
            <tr>
                <th>  </th>
                <th> Trenutno </th>

            </tr>

            <tr>
                <td> Lozinka </td>
                <td>  {{korisnik.lozinka}} </td>

            </tr>

            <tr>
                <td> Ime </td>
                <td> {{korisnik.ime}} </td>
            </tr>

            <tr>
                <td> Prezime </td>
                <td> {{korisnik.prezime}} </td>

            </tr>
    	    <tr>
                <td> Pol </td>
                <td> {{korisnik.pol }} </td>

            </tr>
            <tr>
                <td> Datum rođenja </td>
                <td> {{korisnik.datumRodjenja}} </td>

            </tr>
        </table>
        <br>
		<button type="button" @click="izmeniPodatke()" class="izmenaStyle btn" ><i class="fa fa-pencil" aria-hidden="true"></i>  Izmeni </button> <br>
        <br><br>
        <!-- Modalni dijalog za izmenu podataka -->
        <div id = "dijalogZaIzmenuKorisnika" v-bind:class="{bgModal: dijalogZaIzmenuSakriven, bgModalShow: !dijalogZaIzmenuSakriven}">
            <div class="modal-contents">
        
                <div class="close" @click="dijalogZaIzmenuSakriven = !dijalogZaIzmenuSakriven">+</div>

                <form method='post'>
                    
                    <label for="ime">Ime:</label>
                    <input name="ime" type="text" v-model="izmeneProfila.ime" placeholder="Ime" required>
					
					<label for="pol">Pol:</label>
					<select name="pol" v-model="izmeneProfila.pol" required>
                        <option>Muški</option>
                        <option>Ženski</option>
            		</select>

                    <label for="prezime">Prezime:</label>
                    <input name="prezime" type="text" v-model="izmeneProfila.prezime" placeholder="prezime">
                    
                    <label for="datum">Datum rođenja:</label>
                    <input name="datum" type="date" v-model="izmeneProfila.datumRodjenja" placeholder="Datum rođenja">
                    
                    <label for="lozinka">Lozinka:</label>
                    <input  name="lozinka" type="password" v-model="izmeneProfila.lozinka" placeholder="Lozinka">
                    
                    <label for="ponovljenaLozinka">Ponovljena lozinka:</label>
                    <input  name="ponovljenaLozinka" type="password" v-model="izmeneProfila.ponovljenaLozinka" placeholder="Ponovljena lozinka">
                    
                    <button type="button" @click="sacuvajPromene" class="btn">Potvrdi</button>
                    <button type="button" @click="dijalogZaIzmenuSakriven = !dijalogZaIzmenuSakriven" class="btn">Odustani</button>

                </form>

            </div>
        </div> <!-- Kraj modalnog dijaloga -->
        </div>
    </div>
    `,
    methods: {
    	izmeniPodatke: function() {
    		this.izmeneProfila.datumRodjenja = this.korisnik.datumRodjenja;
    		this.izmeneProfila.pol = this.korisnik.pol;
    		this.izmeneProfila.ime = this.korisnik.ime;
    		this.izmeneProfila.prezime = this.korisnik.prezime;
    		this.izmeneProfila.lozinka = this.korisnik.lozinka;
    		this.izmeneProfila.ponovljenaLozinka = this.korisnik.lozinka;
    		this.dijalogZaIzmenuSakriven = ! this.dijalogZaIzmenuSakriven;
    	}, 
        sacuvajPromene: function () {
        if (!this.izmeneProfila.lozinka || !this.izmeneProfila.ime || !this.izmeneProfila.prezime
                || !this.izmeneProfila.datumRodjenja || !this.izmeneProfila.pol)
            {
                	toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                	return;
            }
		if (this.izmeneProfila.lozinka != this.izmeneProfila.ponovljenaLozinka)
            {
                	toastr["warning"]("Lozinke se ne poklapaju!", "Proverite unos!");
                	return;
            }
            axios
                .post('rest/profil/sacuvajIzmeneKorisnika', {
                    "korisnickoIme": this.korisnik.korisnickoIme,
                    "lozinka": this.izmeneProfila.lozinka,
                    "ime": this.izmeneProfila.ime,
                    "prezime": this.izmeneProfila.prezime,
                    "uloga": this.korisnik.uloga,
                    "datumRodjenja":this.izmeneProfila.datumRodjenja,
                    "pol":this.izmeneProfila.pol
                })
                .then(response => {
                    toastr["success"]("Podaci upešno ažurirani!", "Uspešne izmene!");
                })
                .catch(err => {
                    toastr["error"]("Greška prilikom ažuriranja podataka.", "Greška");
                })
                this.dijalogZaIzmenuSakriven = ! this.dijalogZaIzmenuSakriven;
        }
    },
    mounted() {
        axios
            .get('rest/profil/profilKorisnika')
            .then(response => this.korisnik = response.data)
    },


});