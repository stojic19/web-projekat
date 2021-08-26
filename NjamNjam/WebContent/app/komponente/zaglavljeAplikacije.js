Vue.component("zaglavlje", {
	template: ` 
<div>
<nav>
<ul>
    <li><router-link to="/" exact>Početna strana</router-link></li>
    <li><router-link to="/restorani" exact>Restorani</router-link></li>      
    <li><router-link to="/prijava" exact>Prijava</router-link></li>
    <li><router-link to="/registracija" exact>Registracija</router-link></li>
</ul>
</nav>
</div>
`

});