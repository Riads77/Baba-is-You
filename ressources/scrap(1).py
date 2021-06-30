'''/*____________°\
  /.,------------,.\     EDITOR = ORHAN UYAR
 ///  .=^^^^^^^\__|\\    UNIVESITY = Gustave Eiffel
 \\\   `------.   .//    LINK = www.brotherkey.fr
  `\\`--...._  `;//'     PS = HAVE FUN
    `\\.-,___;.//'
      `\\-..-//'
         \*/
'''
import os
import requests
import wget
import shutil
from bs4 import BeautifulSoup

# Need to run this script
# pip install requests
# pip install bs4
# pip install wget

def download(file_location,image_name, image_url):
    resp = requests.get(image_url, stream=True)
    local_file = open(file_location+image_name, 'wb')
    resp.raw.decode_content = True
    shutil.copyfileobj(resp.raw, local_file)
    del resp

url = "https://babaiswiki.fandom.com/wiki/Category:Nouns"

response = requests.get(url)

if response.ok:
    soup = BeautifulSoup(response.text , "html.parser")
    table = soup.find('table', {'class': 'article-table'})
    lines = table.findAll('tr')
    number = 0
    for line in lines:
        cases = line.findAll('td')
        if (len(cases) != 0):
            case = cases[0].text
            case = case[:-1]
            if (os.path.exists(os.getcwd()+'/'+case) == False):
                os.mkdir(case)

            download((os.getcwd()+'/'+case+'/'), (cases[1].find('img'))['data-image-key'] , (cases[1].find('img'))['data-src'])
            number += 1
            print(str(number) +"/"+ str(len(lines)*2-2) + "\t -> Downloaded\t"+ case + " \t\t\t GIF IMAGE -> TEXT")
            download((os.getcwd()+'/'+case+'/'), (cases[2].find('img'))['data-image-key'] , (cases[2].find('img'))['data-src'])
            number += 1
            print(str(number) +"/"+ str(len(lines)*2-2) + "\t -> Downloaded\t"+ case + " \t\t\t GIF IMAGE -> OBJECT")
