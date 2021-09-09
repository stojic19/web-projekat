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

Vue.component("registracija", {
    data() {
        return {
            noviKorisnik: {},
            greske: [],
            poruka: null,
            ponovljenaLozinka: ''
        }
    },
    template: `
    <div class ="pocetna">

        <form id='register-form' @submit="proveriRegistraciju" method='post'>

            <input type="text" v-model="noviKorisnik.korisnickoIme" placeholder="Korisničko ime" required>
            <input type="text" v-model="noviKorisnik.ime" placeholder="Ime" >
            <input type="text" v-model="noviKorisnik.prezime" placeholder="Prezime">
            
    	    <select v-model="noviKorisnik.pol" required>
                <option>Ženski</option>
                <option>Muški</option>
            </select>
            <input type="date" v-model="noviKorisnik.datum" required>
            <br><br>
            
  	
  			<input type="password" v-model="noviKorisnik.lozinka" placeholder="Lozinka" required>
            <input type="password" v-model="ponovljenaLozinka" placeholder="Ponovljena lozinka" required>
            
            <button type='submit' class="btn"><i class="fa fa-sign-in" aria-hidden="true"></i> Registruj se </button>
        </form>

    </div>
    
    `,
    methods: {
        proveriRegistraciju: function (event) {
            event.preventDefault();

            this.greske = [];

            if (!this.noviKorisnik.korisnickoIme) {
                this.greske.push('Korisničko ime je obavezno!');
            }

            if (!this.noviKorisnik.lozinka) {
                this.greske.push('Lozinka je obavezna!');
            }

            if (!this.noviKorisnik.ime) {
                this.greske.push('Ime je obavezno!');
            }

            if (!this.noviKorisnik.prezime) {
                this.greske.push('prezime je obavezno!');
            }

            if(this.rePassword != this.noviKorisnik.password){
                this.greske.push('Lozinka i ponovljena lozinka moraju biti iste!')
            }

            if (!this.greske.length) {
                axios
                    .post('rest/korisnici/registracija', {
                        "korisnickoIme": this.noviKorisnik.korisnickoIme,
                        "lozinka": this.noviKorisnik.lozinka,
                        "ime": this.noviKorisnik.ime,
                        "prezime": this.noviKorisnik.prezime,
                        "uloga": "KUPAC",
                        "pol" : this.noviKorisnik.pol,
                        "datumRodjenja": this.noviKorisnik.datum
                    })
                    .then(response => {
                        this.message = response.data;
                        console.log("\n\n ------- PODACI -------\n");
                        console.log(response.data);
                        toastr["success"]("Prijavite se!", "Uspešna registracija!");
                        console.log("\n\n ----------------------\n\n");

                        location.href = response.data; // we get from backend redirection to login with this
                    })
                    .catch(err => {
                        console.log("\n\n ------- GREŠKA -------\n");
                        console.log(err);
                        toastr["error"]("Već imamo korisnika sa istim korisničkim imenom, isprobajte drugo", "Greška");
                        console.log("\n\n ----------------------\n\n");
                    })
                return true;
            }

            this.errors.forEach(element => {
                console.log(element)
                toastr["error"](element, "Fail")
            });



        }

    },
    mounted() {
        axios.get('rest/korisnici/dobaviNovogKorisnika').then(response => (this.noviKorisnik = response.data));
    },

});