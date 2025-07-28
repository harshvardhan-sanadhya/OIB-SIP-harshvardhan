import requests

Api_key="9c675eaa28c32ceb83599cad4d799360"
base_url="http://api.openweathermap.org/data/2.5/weather"

city=input("Enter the City name : ")

params={
    "q":city,
    "appid":Api_key,
    "units":"metric"
}
response = requests.get(base_url,params=params)
data=response.json()

if response.status_code==200:
    print(f"\n Weather Report for {data['name']} ")
    print(f"\n Temprature: {data['main']['temp']}:C")
    print(f"\n Feels like: {data['main']['feels_like']}")
    print(f"\n Humidity: {data['main']['humidity']}")
    print(f"\n Condition: {data['weather'][0]['description'].title()}")
else:
    print("\n Error! check the city name or try again later")