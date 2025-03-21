import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */

public class Service {
    private String countryName;
    String apiKeyCity = "hUKP10ehg0Wym3U6vD+ndA==DcQ2EMsvbKWVWImu";
    private String appid = "27e4ffc1bbb33453a129db882817a834";
    private String urlWeather = "https://api.openweathermap.org/data/2.5/weather?lat=52.237049&lon=21.017532&units=metric&appid="+appid;
    private String urlCity = "http://api.openweathermap.org/geo/1.0/direct?q=warsaw&limit=5&appid="+appid;
    private String appidCur ="396eb8c50f491e2ad2aa82a8";
    private String urlCurrency = "https://v6.exchangerate-api.com/v6/"+appidCur+"/latest/USD";
    private String urlNBPa = "https://static.nbp.pl/dane/kursy/xml/a055z250320.xml";
    private String urlNBPb = "https://static.nbp.pl/dane/kursy/xml/b011z250319.xml";
    private String urlNBPc = "https://static.nbp.pl/dane/kursy/xml/c055z250320.xml";
    private String urlCountry;


    private Country country;
    private City city;
    private Weather weather;
    private Currency currency;
    private List<NBPEchangeRates> listNBP;

    public Service(String countryName) {
        this.countryName = countryName;
        urlCountry = "https://api.api-ninjas.com/v1/country?name=" +countryName;
        country = collectCountry(urlCountry);

        urlCurrency = "https://v6.exchangerate-api.com/v6/"+appidCur+"/latest/"+country.currency.code;
        currency = collectCurrency(urlCurrency);

        NBPEchangeRates nbpRatesA = collectExchangeRatesTable(urlNBPa);
        NBPEchangeRates nbpRatesB = collectExchangeRatesTable(urlNBPb);
        NBPEchangeRates nbpRatesC = collectExchangeRatesTable(urlNBPc);
        listNBP = new ArrayList<>();
        listNBP.add(nbpRatesA);
        listNBP.add(nbpRatesB);
        listNBP.add(nbpRatesC);

    }
    public Weather collectWeather(String cityName) {
        City city = collectCity("http://api.openweathermap.org/geo/1.0/direct?q="+cityName+"&limit=5&appid="+appid);
        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+city.lat+"&lon="+city.lon+"&units=metric&appid="+appid;
        try (
                InputStream urlConnection = new URL(url).openConnection().getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection))
        ) {
            String inputLine = in.readLine();
            Gson gson = new Gson();
            return gson.fromJson(inputLine, Weather.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public City collectCity(String url) {
        try (
                InputStream urlConnection = new URL(url).openConnection().getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection))
        ) {
            String inputLine = in.readLine();
            Gson gson = new Gson();
            City[] cities = gson.fromJson(inputLine, City[].class);
            return cities[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Currency collectCurrency(String url) {
        try (
                InputStream urlConnection = new URL(url).openConnection().getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection))
        ) {
            String inputLine = in.readLine();
            Gson gson = new Gson();

            String fullResponse="";
            while (inputLine != null) {
                fullResponse+=inputLine;
                inputLine = in.readLine();
            }
            return gson.fromJson(fullResponse, Currency.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public NBPEchangeRates collectExchangeRatesTable(String url) {
        try(
                InputStream urlConnection = new URL(url).openConnection().getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection))
            ) {
            String inputLine = in.readLine();

            String fullResponse="";
            while (inputLine != null) {
                fullResponse+=inputLine;
                inputLine = in.readLine();
            }

            JSONObject jsonObject = XML.toJSONObject(fullResponse);
            String jsonString = jsonObject.toString();
            Gson gson = new Gson();
            return gson.fromJson(jsonString, NBPEchangeRates.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Country collectCountry(String urlc) {
        try {
            URL url = new URL(urlc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("X-Api-Key", apiKeyCity);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            String fullResponse = "";
            while ((inputLine = in.readLine()) != null) fullResponse += inputLine;
            in.close();
            connection.disconnect();
            Gson gson = new Gson();
            Country[] countries = gson.fromJson(fullResponse, Country[].class);
            return countries[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWeather(String cityName) {
        City city = collectCity("http://api.openweathermap.org/geo/1.0/direct?q="+cityName+"&limit=5&appid="+appid);
        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+city.lat+"&lon="+city.lon+"&units=metric&appid="+appid;
        try(
                InputStream urlConnection = new URL(url).openConnection().getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection));
        ) {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Double getRateFor(String currencyName) {
        Object wartosc;
        try{
            Field pole = currency.conversion_rates.getClass().getDeclaredField(currencyName);
            pole.setAccessible(true);
            wartosc = pole.get(currency.conversion_rates);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (Double) wartosc;
    }
    public Double getNBPRate() {
        for(NBPEchangeRates rates : listNBP) {
            for (NBPEchangeRates.Pozycja pozycja : rates.tabela_kursow.pozycja) {
                if (pozycja.kod_waluty.equals(country.currency.code)) {
                    String kursSredni = pozycja.kurs_sredni.replace(",", ".");
                    return Double.valueOf(kursSredni);
                }
            }
        }
        return null;
    }
    public String getInfoWeather(String cityName){
        Weather weather = collectWeather(cityName);
        return String.valueOf(weather.main.temp);
    }
}

