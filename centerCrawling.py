import requests
import psycopg2

HOST = "localhost"
USER = "postgres"
PASSWORD = "password"
DATABASE = "covid-db"

# Open connection
conn = psycopg2.connect("host=%s dbname=%s user=%s password=%s" % (HOST, DATABASE, USER, PASSWORD))
cur = conn.cursor()


json_url = "https://www.data.gouv.fr/fr/datasets/r/d0566522-604d-4af6-be44-a26eefa01756"
r = requests.get(json_url).json()

for center in r['features']:
    center_name = center['properties']['c_nom']
    city = center['properties']['c_com_nom']
    zip_code = center['properties']['c_com_cp']
    address = center['properties']['c_adr_num'] if center['properties']['c_adr_num'] != None else "" + ", " if center['properties']['c_adr_num'] != None else "" + center['properties']['c_adr_voie'] if center['properties']['c_adr_voie'] else ""
    phone_number = center['properties']['c_rdv_tel']
    #print("{}, {}, {} {}".format(center_name, address, zip_code, city))
    req = cur.execute('SELECT * FROM centers WHERE name = %s', (center_name,))
    exists = cur.fetchone()
    if exists is None:
        cur.execute("INSERT INTO centers (name, address, zip_code, city, phone) VALUES (%s, %s, %s, %s, %s)", (center_name, address, zip_code, city, phone_number))

print(len(r['features']))

conn.commit()