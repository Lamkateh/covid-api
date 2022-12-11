import requests
import psycopg2
import os

HOST = os.environ.get("HOST", "localhost")
USER = os.environ.get("USER", "postgres")
PASSWORD = os.environ.get("PASSWORD", "password")
DATABASE = os.environ.get("DATABASE", "covid-db")

# Open connection
conn = psycopg2.connect("host=%s dbname=%s user=%s password=%s" % (HOST, DATABASE, USER, PASSWORD))
cur = conn.cursor()


json_url = "https://www.data.gouv.fr/fr/datasets/r/d0566522-604d-4af6-be44-a26eefa01756"
r = requests.get(json_url).json()

for center in r['features']:
    center_name = center['properties']['c_nom']
    city = center['properties']['c_com_nom']
    zip_code = center['properties']['c_com_cp']
    number = center['properties']['c_adr_num'] if center['properties']['c_adr_num'] else ""
    street = center['properties']['c_adr_voie'] if center['properties']['c_adr_voie'] else ""
    if number != "" and street != "":
        address = (number + " " + street).strip()
    else:
        address = (number + street).strip()
    phone_number = center['properties']['c_rdv_tel']
    req = cur.execute('SELECT id FROM centers WHERE name = %s', (center_name,))
    exists = cur.fetchone()
    if exists is not None:
        cur.execute("UPDATE centers SET name = %s, address = %s, zip_code = %s, city = %s, phone = %s WHERE id = %s", (center_name, address, zip_code, city, phone_number, exists[0]))
    else:
        cur.execute("INSERT INTO centers (name, address, zip_code, city, phone) VALUES (%s, %s, %s, %s, %s)", (center_name, address, zip_code, city, phone_number))

print(len(r['features']))

conn.commit()