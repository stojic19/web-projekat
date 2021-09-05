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


Vue.component("dostavljac-profil", {
    data() {
        return {
            korisnik: {},
            izmeneProfila: {
                password: '',
                name: '',
                surname: '',
            },
        }
    },
    template: `
    <div id = "styleForProfile">
        <h1> Pozdrav {{korisnik.korisnickoIme}} ! </h1>
        
        <table class="tableInProfil">
            <tr>
                <th>  </th>
                <th> Trenutno </th>
                <th> Novo </th>
            </tr>

            <tr>
                <td> Lozinka </td>
                <td>  {{korisnik.lozinka}} </td>
                <td> <input type="password" v-model="izmeneProfila.lozinka" placeholder="Lozinka"> </td>
            </tr>

            <tr>
                <td> Ime </td>
                <td> {{korisnik.ime}} </td>
                <td> <input type="text" v-model="izmeneProfila.ime" placeholder="Ime"> </td>
            </tr>

            <tr>
                <td> Prezime </td>
                <td> {{korisnik.prezime}} </td>
                <td> <input type="text" v-model="izmeneProfila.prezime" placeholder="Prezime"> </td>
            </tr>
    	    <tr>
                <td> Pol </td>
                <td> {{korisnik.pol }} </td>
                <td> <select type="text" v-model="korisnik.pol">
                        <option>Ženski</option>
                        <option>Muški</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td> Datum rođenja </td>
                <td> {{korisnik.datumRodjenja}} </td>
                <td> <input type="date" v-model="korisnik.datumRodjenja"> </td>
            </tr>
        </table>

        <br><br>
        <button @click="sacuvajPromene()" class="saveChanges" ><i class="fa fa-check" aria-hidden="true"></i> Sačuvaj izmene </button>

    </div>
    `,
    methods: {
        sacuvajPromene: function () {
            axios
                .post('rest/profil/sacuvajIzmeneKorisnika', {
                    "korisnickoIme": this.korisnik.korisnickoIme,
                    "lozinka": this.izmeneProfila.lozinka,
                    "ime": this.izmeneProfila.ime,
                    "prezime": this.izmeneProfila.prezime,
                    "uloga": this.korisnik.uloga,
                    "datumRodjenja":this.korisnik.datumRodjenja,
                    "pol":this.korisnik.pol
                })
                .then(response => {
                    toastr["success"]("Podaci upešno ažurirani!", "Uspešne izmene!");
                })
                .catch(err => {
                    toastr["error"]("Greška prilikom ažuriranja podataka.", "Greška");
                })
        }
    },
    mounted() {
        axios
            .get('rest/profil/profilKorisnika')
            .then(response => this.korisnik = response.data)
    },


});