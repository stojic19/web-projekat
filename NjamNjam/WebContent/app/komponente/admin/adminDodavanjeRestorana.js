Vue.component("admin-dodavanje-restorana", {
    data() {
        return {
        	DTO:{
        		ID: -1,
				logickiObrisan: 0,
				naziv: null,
				tip: "Burgeri",
                status: "Radi",
                broj: null,
                mesto: null,
                ulica: null,
                postanskiBroj: null,
                geografskaSirina: null,
                geografskaDuzina: null,
                putanjaDoSlike: "",
                idMenadzera: -1,
                prosecnaOcena: 0.0,
				korisnickoIme: null,
				lozinka: null,
				ime: null,
				prezime: null,
				pol: "Ženski",
				datumRodjenja: "",
				uloga: "MENADZER",
				tip: null,
				idRestorana: null,
        	},
            menadzeri: [],
            mapaVidljiva: true,
            kodZaSliku: '',
			izborZaMenadzera: true,
			dodajMenadzera: false,
			izaberiPostojeceg: false
        }
    },
    template: `
    <div>
        <div class="dodavanjeRestorana">
            <h1 style="color:black;"> Dodavanje restorana </h1>
            <form method='post'>

                <table class="tabelaZaDodavanjeRestorana">

                    <tr>
                        <td> Naziv </td>
                        <td><input type="text" v-model="DTO.naziv" placeholder="Naziv"></td>
                    </tr>
					<tr>
						<td>Tip:</td>
						<td><select name="tip" v-model="DTO.tip" required>
                        	<option>Brza hrana</option>
                        	<option>Roštilj</option>
					    	<option>Burgeri</option>
					    	<option>Italijanski</option>
					    	<option>Picerija</option>
                        	<option>Kineski</option>
                        	<option>Azijska kuhinja</option>
            			</select></td>
					</tr>
					<tr>
						<td>Status:</td>
						<td><select name="status" v-model="DTO.status" required>
                		<option>Radi</option>
                		<option>Ne radi</option>
            			</select></td>
					</tr>
                    <tr>
                        <td> Lokacija </td>
                        <td><button type="button" @click="pregledMape()"><i class="fa fa-map-marker" aria-hidden="true"></i> Izaberite na mapi </button></td>
                    </tr>
					<tr v-show="mapaVidljiva">
						<td colspan="2">
							<div id="map" class="map" v-if="pregledMape">  </div> 
						</td>
					</tr>
                    <tr>
                        <td> Mesto </td>
                        <td> <input type="text" id="mesto" placeholder="Mesto" required> </td>
                    </tr>
                    <tr>
                        <td> Ulica </td>
                        <td><input  type="text" id="ulica" placeholder="Ulica"></td>
                    </tr>
                    <tr>
                        <td> Broj </td>
                        <td><input type="text" id="broj" placeholder="Broj"> </td>
                    </tr>
			
                    <tr>
                        <td> Poštanski broj </td>
                        <td> <input type="text" id="postanskiBroj" placeHolder="Poštanski broj"></td>
                    </tr>
					<tr>
                        <td> Geografska širina </td>
                        <td><input  type="text" id="geoSirina" placeholder="Geografska širina"></td>
                    </tr>
                    <tr>
                        <td> Geografska dužina </td>
                        <td><input type="text" id="geoDuzina" placeholder="Geografska dužina"> </td>
                    </tr>
                    <tr>
                        <td> Logo  </td>
                        <td> 
                            <input type="file" onchange="slikaUUrlDodavanje(this)" placeholder="Izaberite sliku"/>
                        </td>
                    </tr>
					
					<tr style="align:center" v-show="izborZaMenadzera">
						<td colspan="2">
							<button v-show="!dodajMenadzera" type="button"  class="btn" @click="dodajOpcija()" >Dodaj menadžera</button>
							<button v-show="!izaberiPostojeceg" type="button"  class="btn" @click="izaberiOpcija()" >Izaberi postojećeg</button>
						</td>
					</tr>
					<tr v-show="izaberiPostojeceg">
                        <td> Slobodni menadžeri </td>
                        <td> 
                            <select v-model="DTO.idMenadzera">
                                <option v-for="menadzer in menadzeri" v-bind:value="menadzer.id">
                                    {{ menadzer.korisnickoIme }}, {{ menadzer.ime }}, {{ menadzer.prezime }} 
                                </option>
                            </select>
                        </td>
                    </tr>
					<tr v-show="izaberiPostojeceg">
                        <td><button type="button" @click="potvrdiDodavanjeBezNovog()" class="btn"><i class="fa fa-check" aria-hidden="true"></i>Potvrdi</button></td>
                    </tr>
                </table>
			
				
                <div v-show="dodajMenadzera" id = "dodavanjeMenadzera">
                    <input type="text" v-model="DTO.korisnickoIme" placeholder="Korisničko ime" required>
                    <input type="text" v-model="DTO.ime" placeholder="Ime" >
                    <input type="text" v-model="DTO.prezime" placeholder="Prezime">
					<select v-model="DTO.pol" required>
                		<option>Ženski</option>
                		<option>Muški</option>
            		</select>
            		<input type="date" v-model="DTO.datumRodjenja" required>
                    <input type="password" v-model="DTO.lozinka" placeholder="Lozinka" required>
                    <input type="password" placeholder="Ponovljena lozinka" required>

                    <button type="button" @click="potvrdiDodavanjeSaNovim()" class="btn"><i class="fa fa-check" aria-hidden="true"></i>Potvrdi</button>
                </div>             
            </form>
        </div>
        <br><br>
        <img id="slikaId" src="" alt="logo restorana" width="300" height="200" hidden> 
    </div>
    `,
    methods: {
		dodajOpcija: function(){
			this.dodajMenadzera = !this.dodajMenadzera;
			if(this.izaberiPostojeceg = false)
			{
				this.izaberiPostojeceg = true;
			}
		},
		izaberiOpcija: function(){
			this.izaberiPostojeceg = !this.izaberiPostojeceg;
			if(this.dodajMenadzera = false)
			{
				this.dodajMenadzera = true;
			}
		},
        pregledMape: function () {
            this.mapaVidljiva = !this.mapaVidljiva;
            /*if (this.mapaVidljiva) {
                this.$nextTick(function () {
                    this.initForMap();
                    let c = document.getElementById("map").childNodes;
                    c[0].style.borderRadius  = '10px';
                    c[0].style.border = '4px solid #04030f';
                })
            }*/
        },
        potvrdiDodavanjeBezNovog: function () {
			this.DTO.mesto = 	document.getElementById("mesto").value;
            this.DTO.ulica = 	document.getElementById("ulica").value;
            this.DTO.broj = 	document.getElementById("broj").value;
            this.DTO.postanskiBroj = document.getElementById("postanskiBroj").value;
            this.DTO.geografskaSirina = document.getElementById("geoSirina").value;
            this.DTO.geografskaDuzina = document.getElementById("geoDuzina").value;
            
            if(document.getElementById("slikaId").src != "http://localhost:8080/NjamNjam/admin.html")
            {
            	this.kodZaSliku = document.getElementById("slikaId").src;
            	this.DTO.putanjaDoSlike = this.kodZaSliku;
            }else{
            	this.DTO.putanjaDoSlike = "nema";
            }

            if (!this.DTO.naziv || !this.DTO.postanskiBroj 
				|| !this.DTO.mesto ||
                !this.DTO.ulica || !this.DTO.broj) {
                toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                return;
            }
            console.log(this.DTO);
            axios
                .post('rest/restoran/dodajNoviRestoran', this.DTO)
                .then(response => {
                    toastr["success"]("Uspešno dodavanje restorana!", "Uspešno dodavanje!");
                    router.push("/home");
                });
        },
		potvrdiDodavanjeSaNovim: function () {
			this.DTO.mesto = 	document.getElementById("mesto").value;
            this.DTO.ulica = 	document.getElementById("ulica").value;
            this.DTO.broj = 	document.getElementById("broj").value;
            this.DTO.postanskiBroj = document.getElementById("postanskiBroj").value;
            this.DTO.geografskaSirina = document.getElementById("geoSirina").value;
            this.DTO.geografskaDuzina = document.getElementById("geoDuzina").value;
			
			if(document.getElementById("slikaId").src != "http://localhost:8080/NjamNjam/admin.html")
            {
            	this.kodZaSliku = document.getElementById("slikaId").src;
            	this.DTO.putanjaDoSlike = this.kodZaSliku;
            }else{
            	this.DTO.putanjaDoSlike = "nema";
            }

            if (!this.DTO.naziv || !this.DTO.postanskiBroj 
				|| !this.DTO.mesto ||
                !this.DTO.ulica || !this.DTO.broj || 
				!this.DTO.korisnickoIme || !this.DTO.lozinka ||
			 	!this.DTO.ime || !this.DTO.prezime) {
                toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                return;
            }

            axios
                .post('rest/restoran/dodajNoviRestoranIMenadzera', this.DTO)
                .then(response => {
                    toastr["success"]("Uspešno dodavanje restorana i menadžera!", "Uspešno dodavanje!");
                    router.push("/home");
                })
                .catch(err => {
                	console.log(err);
                    toastr["error"]("Već imamo korisnika sa istim korisničkim imenom, isprobajte drugo", "Greška");
                });
        },
        initForMap: function () {
            const map = new ol.Map({
                target: 'map',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    })
                ],
                view: new ol.View({
                    center: [2278434.939124534, 5591521.493117212],
                    zoom: 7
                })
            })
            map.on('click', function (evt) {
                console.log(evt.coordinate);
                var coord = ol.proj.toLonLat(evt.coordinate);
                reverseGeocode(coord);
            })
        },
    },
    mounted() {
        this.$nextTick(function () {
            this.initForMap();
        })
		//axios.get('rest/korisnici/dobaviNovogKorisnika').then(response => (this.noviMenadzer = response.data));
		//axios.get('rest/restoran/dobaviNoviRestoranIMenadzera').then(response => (this.DTO = response.data));
        axios
            .get('rest/korisnici/dobaviSveKorisnike')
            .then(response => {
                this.menadzeri = [];
                response.data.forEach(el => {
					if(el.uloga == "MENADZER" && el.idRestorana == -1 && el.blokiran == 0 && el.logickiObrisan == 0)
                    this.menadzeri.push(el);
                });
                console.log(this.menadzeri.length);
		if(this.menadzeri.length == 0)
		{
			this.izborZaMenadzera = false;
			this.dodajMenadzera = true;
		}
                return this.menadzeri;
            });
		
    },
});

function reverseGeocode(coords) {
    fetch('http://nominatim.openstreetmap.org/reverse?format=json&lon=' + coords[0] + '&lat=' + coords[1])
        .then(function (response) {
            return response.json();
        }).then(function (json) {
			document.getElementById("geoSirina").value = coords[0];
            document.getElementById("geoDuzina").value = coords[1];
            console.log(json.address);
            if (json.address.city) {
                document.getElementById("mesto").value = json.address.city;
            } else if (json.address.city_district) {
                document.getElementById("mesto").value = json.address.city_district;
            }
            if (json.address.road) {
                document.getElementById("ulica").value = json.address.road;
            }
            if (json.address.house_number) {
                document.getElementById("broj").value = json.address.house_number;
            }
            if(json.address.postcode){
                document.getElementById("postanskiBroj").value = json.address.postcode;
            }

        });
}
function slikaUUrlDodavanje(element) {
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