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

Vue.component("prijava",{
    data() {
        return {
            noviKorisnik: {},
            greske: []
        }
    },
    template:`
    <div class ="forica">

        <form id='login-form' @submit="proveriPrijavu" method='post'>

            <input type="text" v-model="noviKorisnik.korisnickoIme" placeholder="Korisničko ime" required>
            <input type="password" v-model="noviKorisnik.lozinka" placeholder="Lozinka" required>
            
            <br><br>
            <button type='submit' class="btn"><i class="fa fa-sign-in" aria-hidden="true"></i> Prijavi se </button>

        </form>      

    </div>
    
    `,
    methods: {
        proveriPrijavu: function(event){
            event.preventDefault();

            this.greske = [];
            
            if (!this.noviKorisnik.korisnickoIme) {
                this.greske.push('Korisničko ime je obavezno.');
            }

            if (!this.noviKorisnik.lozinka) {
                this.greske.push('Lozinka je obavezna.');
            }


            if (!this.greske.length) {
                axios
                .post('rest/korisnici/prijava',{"korisnickoIme":''+ this.noviKorisnik.korisnickoIme, "lozinka":''+this.noviKorisnik.lozinka})
                .then(response=>{
                    this.message = response.data;
                    console.log("\n\n ------- PODACI -------\n");
                    console.log(response.data);
                    toastr["success"]("Možete započeti poručivanje!", "Uspešna prijava!");
                    console.log("\n\n ----------------------\n\n");

                    location.href = response.data;
                  
                })
                .catch(err =>{ 
                    console.log("\n\n ------- ERROR -------\n");
                    console.log(err);
                    toastr["error"]("Korisničko ime i lozinka su netačni, ili je vaš nalog blokiran!", "Greška");
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
        axios.get('rest/korisnici/dobaviNovogKorisnika').then(response => (this.newUser = response.data));
    },
});