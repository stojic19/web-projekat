Vue.component("menadzer-dodavanje-artikla", {
    data() {
        return {
        	DTO:{
        		id: -1,
				logickiObrisan: 0,
				naziv: null,
				tip: "Jelo",
				cena: 0,
				idRestoranaKomPripada: null,
				kolicina: null,
				opis: null,
				putanjaDoSlike: ""
        	},
            kodZaSliku: '',
        }
    },
    template: `
    <div>
        <div class="dodavanjeRestorana">
            <h1 style="color:black;"> Dodavanje artikla </h1>
            <form method='post'>
                <table class="tabelaZaDodavanjeRestorana">
                    <tr>
                        <td> Naziv </td>
                        <td><input type="text" v-model="DTO.naziv" placeholder="Naziv"></td>
                    </tr>
					<tr>
						<td>Tip:</td>
						<td><select name="tip" v-model="DTO.tip" required>
                        	<option>Jelo</option>
                        	<option>Piće</option>
            			</select></td>
					</tr>
                    <tr>
                        <td> Cena: </td>
                        <td> <input type="number" v-model="DTO.cena" placeholder="Cena" required> </td>
                    </tr>
                    <tr>
                        <td> Količina: </td>
                        <td><input  type="text" v-model="DTO.kolicina" placeholder="Količina"></td>
                    </tr>
                    <tr>
                        <td> Opis </td>
                        <td><input type="text" v-model="DTO.opis" placeholder="Opis"> </td>
                    </tr>
                    <tr>
                        <td> Logo  </td>
                        <td> 
                            <input type="file" onchange="slikaUUrl(this)" placeholder="Izaberite sliku"/>
                        </td>
                    </tr>
					<tr>
                        <td><button type="button" @click="potvrdiDodavanje()" class="btn"><i class="fa fa-check" aria-hidden="true"></i>Potvrdi</button></td>
                    </tr>
                </table>          
            </form>
        </div>
        <br><br>
        <img id="slikaId" src="" alt="logo restorana" width="300" height="200" hidden> 
    </div>
    `,
    methods: {
		potvrdiDodavanje: function () {
            this.kodZaSliku = document.getElementById("slikaId").src;
            this.DTO.putanjaDoSlike = this.kodZaSliku;

            if (!this.DTO.naziv || !this.DTO.tip 
				|| !this.DTO.cena) {
                toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                return;
            }
            axios
                .post('rest/artikal/dodajArtikal', {
					artikal:this.DTO
					})
                .then(response => {
                    toastr["success"]("Uspešno dodavanje atikal!", "Uspešno dodavanje!");
                })
                .catch(err => {
                	console.log(err);
                    toastr["error"]("Već imamo artikal sa istim imenom, isprobajte drugo", "Greška");
                });
        },
    },
    mounted() {

		
    },
});
function slikaUUrl(element) {
    var file = element.files[0];
    var reader = new FileReader();
    reader.onloadend = function () {
        console.log('RESULT', reader.result);
        document.getElementById('slikaId')
            .setAttribute(
                'src', reader.result
            );
    }
    reader.readAsDataURL(file);
}