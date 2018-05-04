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

GET api/cakes ?cakeFilter  - getView 
GET api/cakes/{id} - getItem 
POST api/cakes cakeDto - saveItem 
DELETE api/cakes/{id} - removeItem

  
  cakeFilter = {
      "text": "some text",
      "limit": 2,
      "page": 3,
      "status" : [FRESH, SATLE]
      } 
  
 
  
