import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversorMoedas {

    private static final String API_KEY = "seu-api-key-aqui"; // Substitua pela sua chave
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final List<String> historico = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirMenu();
    }

    private static void exibirMenu() {
        while (true) {
            System.out.println("\n****************************************");
            System.out.println("Bem-vindo ao Conversor de Moedas!");
            System.out.println("****************************************");
            System.out.println("1) Dólar (USD) → Real Brasileiro (BRL)");
            System.out.println("2) Real Brasileiro (BRL) → Dólar (USD)");
            System.out.println("3) Euro (EUR) → Real Brasileiro (BRL)");
            System.out.println("4) Real Brasileiro (BRL) → Euro (EUR)");
            System.out.println("5) Libra Esterlina (GBP) → Real Brasileiro (BRL)");
            System.out.println("6) Real Brasileiro (BRL) → Libra Esterlina (GBP)");
            System.out.println("7) Mostrar histórico de conversões");
            System.out.println("8) Sair");
            System.out.println("****************************************");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                if (opcao == 8) {
                    System.out.println("Obrigado por usar o Conversor de Moedas. Até mais!");
                    break;
                }

                processarOpcao(opcao);
            } catch (Exception e) {
                System.out.println("Opção inválida. Por favor, digite um número entre 1 e 8.");
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                converterMoeda("USD", "BRL", "Dólar", "Real Brasileiro");
                break;
            case 2:
                converterMoeda("BRL", "USD", "Real Brasileiro", "Dólar");
                break;
            case 3:
                converterMoeda("EUR", "BRL", "Euro", "Real Brasileiro");
                break;
            case 4:
                converterMoeda("BRL", "EUR", "Real Brasileiro", "Euro");
                break;
            case 5:
                converterMoeda("GBP", "BRL", "Libra Esterlina", "Real Brasileiro");
                break;
            case 6:
                converterMoeda("BRL", "GBP", "Real Brasileiro", "Libra Esterlina");
                break;
            case 7:
                mostrarHistorico();
                break;
            default:
                System.out.println("Opção inválida. Por favor, tente novamente.");
        }
    }

    private static void converterMoeda(String de, String para, String nomeDe, String nomePara) {
        System.out.print("\nDigite o valor em " + nomeDe + " a ser convertido para " + nomePara + ": ");
        
        try {
            double valor = scanner.nextDouble();
            scanner.nextLine(); // Limpar buffer

            if (valor <= 0) {
                System.out.println("O valor deve ser maior que zero.");
                return;
            }

            double taxa = obterTaxaConversao(de, para);
            if (taxa == -1) {
                System.out.println("Não foi possível obter a taxa de conversão no momento.");
                return;
            }

            double valorConvertido = valor * taxa;
            String resultado = df.format(valor) + " " + nomeDe + " = " + df.format(valorConvertido) + " " + nomePara;
            System.out.println("\nResultado: " + resultado);

            // Registrar no histórico
            String registro = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + 
                             " - " + resultado + " (Taxa: " + df.format(taxa) + ")";
            historico.add(registro);
        } catch (Exception e) {
            System.out.println("Valor inválido. Por favor, digite um número.");
            scanner.nextLine(); // Limpar buffer
        }
    }

    private static double obterTaxaConversao(String de, String para) {
        try {
            URL url = new URL(BASE_URL + de + "/" + para);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Processar a resposta JSON
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                
                if (jsonObject.get("result").getAsString().equals("success")) {
                    return jsonObject.get("conversion_rate").getAsDouble();
                } else {
                    System.out.println("Erro na API: " + jsonObject.get("error-type").getAsString());
                    return -1;
                }
            } else {
                System.out.println("Erro na conexão com a API. Código: " + responseCode);
                return -1;
            }
        } catch (IOException e) {
            System.out.println("Erro ao conectar com a API: " + e.getMessage());
            return -1;
        }
    }

    private static void mostrarHistorico() {
        if (historico.isEmpty()) {
            System.out.println("\nNenhuma conversão realizada ainda.");
            return;
        }

        System.out.println("\nHistórico de Conversões:");
        System.out.println("----------------------------------------");
        for (String registro : historico) {
            System.out.println(registro);
        }
        System.out.println("----------------------------------------");
    }
}