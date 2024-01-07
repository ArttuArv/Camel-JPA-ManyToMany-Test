# jpa-manytomany-example

<style>
.blueBG {
    background-color: #23408F;
    color: white;
}
</style>

<table>
    <tr>
        <td class="blueBG">Integraatiokortti</td>
        <td><a href="https://dev.azure.com/university-of-oulu/UO/_wiki/wikis/UO.wiki/431/-integraatiokortti-pohja-#">Integraatiokortti-pohja</a></td>
    </tr>
    <tr>
        <td class="blueBG">Git</td>
        <td><a href="https://dev.azure.com/university-of-oulu/UO/_git/UO-ICT-IS-integration-archetypes">UO-ICT-IS-integration-archetypes</a></td>
    </tr>
    <tr>
        <!-- Lähinnä lisäkysymysten vuoksi, viimeisin kehittäjä ensiksi. -->
        <td class="blueBG">Kehittäjät</td>
        <td>Matti meikäläinen</td>
    </tr>
    <tr>
        <td class="blueBG">Ensimmäinen versio</td>
        <td>2023</td>
    </tr>
</table>

## Kuvaus

Integraatio kirjoittaa sekunnin välein asetuksessa määritetyn example.message arvon logille. 


## Asennus

Käännä ja asenna projekti lokaaliin maven repoon ajamalla seuraava komento

```bash
mvn clean install
```

Bundlen voi asentaa karafiin seuraavalla komennolla

```bash
bundle:install mvn:org.unioulu.jpa.manytomany.example/jpa-manytomany-example/1.0-SNAPSHOT
```

## Konfigurointi

Integraation oletus asetukset on määritetty `ExampleBlueprint.xml` tiedostoon joka löytyy projektin `resources/OSGI-INF/blueprint` kansiosta.
Nämä arvot voi yliajaa luomalla `org.unioulu.jpa.manytomany.example.ExampleContext.cfg` properties tyyppisten tiedoston ja lisäämällä sinne yliajettava arvo `tyyliin avain=arvo`. Projektiin liittyvät esimerkki asetustiedostot löytyvät `configs` kansiosta.

## Käyttö

Integraatio toimii asennuksen jälkeen automaattisesti ajastetusti.

## Tuki 

Integraation teknisestä toteutuksesta voi kysellä integraatiokortilla mainituilta kehittäjiltä ja arkkitehdeilta. 
Integraatiokortilta pitäisi löytyä myös muut yhteys- ja vastuuhenkilöt joilta voi kysellä lisätietoja liiketoiminnallisista vaatimuksista ja domainista.

<table>
    <tr>
        <td class="blueBG">Kohdejärjestelmän dokumentaatio</td>
        <td><a href="https://dev.azure.com/university-of-oulu/UO/_wiki/wikis/UO.wiki/431/-integraatiokortti-pohja-#">Integraatiokortti-pohja</a></td>
    </tr>
    <tr>
        <td class="blueBG">Lähdejärjestelmän dokumentaatio</td>
        <td><a href="https://dev.azure.com/university-of-oulu/UO/_git/UO-ICT-IS-integration-archetypes">UO-ICT-IS-integration-archetypes</a></td>
    </tr>
    <tr>
        <td class="blueBG">Muu dokumentaatio</td>
        <td><a href="https://dev.azure.com/university-of-oulu/UO/_git/UO-ICT-IS-integration-archetypes">UO-ICT-IS-integration-archetypes</a></td>
    </tr>
</table>

## Jatkokehitys

Integraation jatkokehityksen kannalta muut huomioitavat asiat, vinkit, yms.

# Camel-JPA-ManyToMany-Test
