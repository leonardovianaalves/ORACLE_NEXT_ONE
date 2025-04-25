# ORACLE_NEXT_ONE
PROJETO DO CONVERSOR DE MOEDAS EM JAVA

#Como usar este código:
Obtenha uma chave API gratuita:
Acesse ExchangeRate-API e cadastre-se para obter uma chave gratuita
Substitua "seu-api-key-aqui" no código pela sua chave

#Dependências necessárias:
Este código usa a biblioteca Gson para processar JSON
Se estiver usando uma IDE online, verifique se ela suporta adicionar dependências
Em um projeto local, adicione ao seu pom.xml (Maven) ou build.gradle (Gradle) a dependência:

<!-- Para Maven -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.9</version>
</dependency>

#Funcionalidades:

Conversão entre 6 pares de moedas diferentes

Histórico de conversões com data/hora

Tratamento de erros e validações

Interface amigável via console

#Para exportar como JAR:
Na sua IDE, siga estes passos:
File → Project Structure → Artifacts
Adicione um novo JAR → From modules with dependencies
Selecione a classe principal (ConversorMoedas)

Build → Build Artifacts
