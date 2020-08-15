# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 44 - Campus Taguspark


Diogo Barradas   81975   diogocavernas.cpoc@gmail.com

Pedro Lopes      81988   pedrocaxkampos@gmail.com

João Pedro       81990   jpbcorreia@hotmail.com



Repositório:
[tecnico-distsys/T_44-project](https://github.com/tecnico-distsys/T_44-project/)

-------------------------------------------------------------------------------

## Instruções de instalação 


### Ambiente

[0] Iniciar sistema operativo

Linux


[1] Iniciar servidores de apoio

JUDDI:
```
É usado o Servidor de nomes para Web Services que segue a norma UDDI 
->jUDDI 3.3.2 (configured for port 9090)

Tornar os scripts de lançamento executáveis:
chmod +x juddi-3.3.2_tomcat-7.0.64_9090/bin/*.sh

Para lançar o servidor, basta executar o seguinte comando na pasta 
juddi-3.3.2_tomcat-7.0.64_9090/bin:

./startup.sh

Para confirmar funcionamento, aceder à página de índice do jUDDI, que dá também acesso à 
interface de administração:
http://localhost:9090/juddiv3/

utilizador: uddiadmin
senha: da_password1
```


[2] Criar pasta temporária

```
cd Desktop
mkdir SD
cd SD
```


[3] Obter código fonte do projeto (versão entregue)

```
git clone -b

cd T_44-project
```

[4] Iniciar CERTIFICATE AUTHORITY SERVICE:
```
cd cas-ws
mvn clean
mvn generate-sources
mvn compile
mvn exec:java
```

[5] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

```
cd transporter-ws-cli
mvn clean install
```

```
cd cripto
mvn clean install
```

```
cd ws-handlers
mvn clean install
```

```
cd cas-ws-cli
mvn clean install
```
-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd Desktop/T_44-project/transporter-ws
mvn clean 
mvn generate-sources
mvn compile
mvn exec:java
```

[2] Construir **cliente** e executar testes

```
cd Desktop/T_44-projeto/transporter-ws-cli
mvn clean 
mvn generate-sources
mvn compile
mvn test
mvn verify
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd Desktop/T_44-projeto/broker-ws
mvn clean 
mvn generate-sources
mvn compile
mvn exec:java
```


[2] Construir **cliente** e executar testes

```
cd Desktop/T_44-projeto/broker-ws-cli
mvn clean 
mvn generate-sources
mvn compile
mvn test
mvn verify
```

...

-------------------------------------------------------------------------------
**FIM**
