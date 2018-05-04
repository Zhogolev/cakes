# cakes
Test for dom.ru

## Run app.
For run up in the Docker:
0. prepare db.properties /src/main/resources
  0.1 change data.action -> create-drop
  0.2 change db.url 
  
1. mvn clean package 
2. copy *.jar  from /target to /docker
3. from /docker  docker build -t <your_image_name> .
4. docker run -p <port1>:<port2> <your_image_name>


## Application 

GET api/cakes ?cakeFilter  - getView </br>
GET api/cakes/{id} - getItem </br>
POST api/cakes - saveItem </br>
DELETE api/cakes/{id} - removeItem </br>

  
  cakeFilter = {</br>
      "text": "some text",</br>
      "limit": 2,</br>
      "page": 3,</br>
      "status" : [FRESH, SATLE]</br>
      } </br></br>
  
 
  post body {</br>
    "name": "testy pirogok",</br>
    "status" : FRESH</br>
  }</br>
