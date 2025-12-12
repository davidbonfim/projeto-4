import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaLocadora sistema = new SistemaLocadora();
        sistema.carregarDados();

        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInt(scanner, "Escolha uma opcao: ");
            switch (opcao) {
                case 1:
                    cadastrarCarro(scanner, sistema);
                    break;
                case 2:
                    cadastrarMoto(scanner, sistema);
                    break;
                case 3:
                    realizarLocacao(scanner, sistema);
                    break;
                case 4:
                    removerVeiculo(scanner, sistema);
                    break;
                case 5:
                    removerLocacao(scanner, sistema);
                    break;
                case 6:
                    listarVeiculos(sistema);
                    break;
                case 7:
                    listarLocacoes(sistema);
                    break;
                case 8:
                    sistema.salvarDados();
                    System.out.println("Dados salvos. Saindo...");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 8);
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== Sistema de Locadora ===");
        System.out.println("1. Cadastrar Carro");
        System.out.println("2. Cadastrar Moto");
        System.out.println("3. Realizar Locacao");
        System.out.println("4. Remover Veiculo");
        System.out.println("5. Remover Locacao");
        System.out.println("6. Listar Veiculos");
        System.out.println("7. Listar Locacoes");
        System.out.println("8. Sair");
    }

    private static void cadastrarCarro(Scanner scanner, SistemaLocadora sistema) {
        System.out.println("--- Cadastro de Carro ---");
        String placa = lerLinha(scanner, "Placa: ");
        String modelo = lerLinha(scanner, "Modelo: ");
        double valor = lerDouble(scanner, "Valor da diaria: ");
        int portas = lerInt(scanner, "Numero de portas: ");
        sistema.cadastrarVeiculo(new Carro(placa, modelo, valor, portas));
        System.out.println("Carro cadastrado.");
    }

    private static void cadastrarMoto(Scanner scanner, SistemaLocadora sistema) {
        System.out.println("--- Cadastro de Moto ---");
        String placa = lerLinha(scanner, "Placa: ");
        String modelo = lerLinha(scanner, "Modelo: ");
        double valor = lerDouble(scanner, "Valor da diaria: ");
        int cilindradas = lerInt(scanner, "Cilindradas: ");
        sistema.cadastrarVeiculo(new Moto(placa, modelo, valor, cilindradas));
        System.out.println("Moto cadastrada.");
    }

    private static void realizarLocacao(Scanner scanner, SistemaLocadora sistema) {
        System.out.println("--- Realizar Locacao ---");
        String placa = lerLinha(scanner, "Placa do veiculo: ");
        String cliente = lerLinha(scanner, "Nome do cliente: ");
        int dias = lerInt(scanner, "Quantidade de dias: ");
        if (dias <= 0) {
            System.out.println("Dias deve ser maior que zero.");
            return;
        }
        String dataStr = lerLinha(scanner, "Data de inicio (yyyy-MM-dd): ");
        LocalDate dataInicio;
        try {
            dataInicio = LocalDate.parse(dataStr);
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Use o formato yyyy-MM-dd.");
            return;
        }

        if (sistema.buscarVeiculo(placa) == null) {
            System.out.println("Veiculo nao encontrado para a placa informada.");
            return;
        }
        if (!sistema.veiculoDisponivel(placa, dataInicio, dias)) {
            System.out.println("Veiculo indisponivel para o periodo informado.");
            return;
        }
        Locacao locacao = sistema.cadastrarLocacao(cliente, placa, dias, dataInicio);
        if (locacao != null) {
            System.out.println("Locacao registrada. Inicio: " + dataInicio + ". Total: " + locacao.calcularTotal());
        } else {
            System.out.println("Veiculo nao encontrado. Nao foi possivel registrar a locacao.");
        }
    }

    private static void removerVeiculo(Scanner scanner, SistemaLocadora sistema) {
        System.out.println("--- Remover Veiculo ---");
        String placa = lerLinha(scanner, "Placa: ");
        boolean removido = sistema.removerVeiculo(placa);
        if (removido) {
            System.out.println("Veiculo removido com sucesso.");
        } else {
            System.out.println("Nao foi possivel remover. Placa inexistente ou possui locacoes associadas.");
        }
    }

    private static void removerLocacao(Scanner scanner, SistemaLocadora sistema) {
        System.out.println("--- Remover Locacao ---");
        int id = lerInt(scanner, "ID da locacao: ");
        boolean removida = sistema.removerLocacao(id);
        if (removida) {
            System.out.println("Locacao removida.");
        } else {
            System.out.println("Locacao nao encontrada.");
        }
    }

    private static void listarVeiculos(SistemaLocadora sistema) {
        System.out.println("--- Lista de Veiculos ---");
        if (sistema.getListaVeiculos().isEmpty()) {
            System.out.println("Nenhum veiculo cadastrado.");
            return;
        }
        for (Veiculo veiculo : sistema.getListaVeiculos()) {
            System.out.println(veiculo);
        }
    }

    private static void listarLocacoes(SistemaLocadora sistema) {
        System.out.println("--- Lista de Locacoes ---");
        if (sistema.getListaLocacoes().isEmpty()) {
            System.out.println("Nenhuma locacao cadastrada.");
            return;
        }
        for (Locacao locacao : sistema.getListaLocacoes()) {
            System.out.println(locacao);
        }
    }

    private static int lerInt(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            }
            System.out.println("Valor invalido, tente novamente.");
            scanner.nextLine();
        }
    }

    private static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextDouble()) {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            }
            System.out.println("Valor invalido, tente novamente.");
            scanner.nextLine();
        }
    }

    private static String lerLinha(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
}
