# stream


docker pull rabbitmq:3.11-management

docker run -it --rm --name rabbitmq -p 5552:5552 -p 15672:15672 -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS='-rabbitmq_stream advertised_host localhost' rabbitmq:3.11-management

docker exec rabbitmq rabbitmq-plugins enable rabbitmq_stream

http://localhost:15672/

guest
guest