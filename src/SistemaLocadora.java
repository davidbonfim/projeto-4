import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaLocadora {
    private static final String ARQ_VEICULOS = "veiculos.txt";
    private static final String ARQ_LOCACOES = "locacoes.txt";

    private final List<Veiculo> listaVeiculos = new ArrayList<>();
    private final List<Locacao> listaLocacoes = new ArrayList<>();
    private int proximoId = 1;

    public List<Veiculo> getListaVeiculos() {
        return listaVeiculos;
    }

    public List<Locacao> getListaLocacoes() {
        return listaLocacoes;
    }

    public void cadastrarVeiculo(Veiculo veiculo) {
        listaVeiculos.add(veiculo);
    }

    public Veiculo buscarVeiculo(String placa) {
        for (Veiculo veiculo : listaVeiculos) {
            if (veiculo.getPlaca().equalsIgnoreCase(placa)) {
                return veiculo;
            }
        }
        return null;
    }

    public boolean removerVeiculo(String placa) {
        Veiculo veiculo = buscarVeiculo(placa);
        if (veiculo != null) {
            // Evita remover veículo que tem locações associadas
            for (Locacao l : listaLocacoes) {
                if (l.getVeiculo().getPlaca().equalsIgnoreCase(placa)) {
                    return false;
                }
            }
            return listaVeiculos.remove(veiculo);
        }
        return false;
    }

    public boolean removerLocacao(int id) {
        for (Locacao locacao : listaLocacoes) {
            if (locacao.getId() == id) {
                return listaLocacoes.remove(locacao);
            }
        }
        return false;
    }

    public boolean veiculoDisponivel(String placa, LocalDate dataInicio, int dias) {
        Veiculo veiculo = buscarVeiculo(placa);
        if (veiculo == null) {
            return false;
        }
        LocalDate dataFimNova = dataInicio.plusDays(dias - 1L);
        for (Locacao locacao : listaLocacoes) {
            if (!locacao.getVeiculo().getPlaca().equalsIgnoreCase(placa)) {
                continue;
            }
            LocalDate inicio = locacao.getDataInicio();
            LocalDate fim = inicio.plusDays(locacao.getDias() - 1L);
            boolean semSobreposicao = dataFimNova.isBefore(inicio) || dataInicio.isAfter(fim);
            if (!semSobreposicao) {
                return false;
            }
        }
        return true;
    }

    public Locacao cadastrarLocacao(String nomeCliente, String placa, int dias, LocalDate dataInicio) {
        Veiculo veiculo = buscarVeiculo(placa);
        if (veiculo == null) {
            return null;
        }
        if (dias <= 0) {
            return null;
        }
        if (!veiculoDisponivel(placa, dataInicio, dias)) {
            return null;
        }
        Locacao locacao = new Locacao(proximoId++, nomeCliente, veiculo, dias, dataInicio);
        listaLocacoes.add(locacao);
        return locacao;
    }

    public void salvarDados() {
        salvarVeiculos();
        salvarLocacoes();
    }

    public void carregarDados() {
        listaVeiculos.clear();
        listaLocacoes.clear();
        proximoId = 1;
        carregarVeiculos();
        carregarLocacoes();
    }

    private void salvarVeiculos() {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(ARQ_VEICULOS))) {
            for (Veiculo veiculo : listaVeiculos) {
                if (veiculo instanceof Carro) {
                    Carro carro = (Carro) veiculo;
                    writer.write(String.format("CARRO;%s;%s;%.2f;%d", carro.getPlaca(), carro.getModelo(),
                            carro.getValorDiaria(), carro.getNumeroPortas()));
                } else if (veiculo instanceof Moto) {
                    Moto moto = (Moto) veiculo;
                    writer.write(String.format("MOTO;%s;%s;%.2f;%d", moto.getPlaca(), moto.getModelo(),
                            moto.getValorDiaria(), moto.getCilindradas()));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar veiculos: " + e.getMessage());
        }
    }

    private void salvarLocacoes() {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(ARQ_LOCACOES))) {
            for (Locacao locacao : listaLocacoes) {
                writer.write(String.format("%d;%s;%s;%d;%s", locacao.getId(), locacao.getNomeCliente(),
                        locacao.getVeiculo().getPlaca(), locacao.getDias(), locacao.getDataInicio()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar locacoes: " + e.getMessage());
        }
    }

    private void carregarVeiculos() {
        Path path = Path.of(ARQ_VEICULOS);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length < 5) {
                    continue;
                }
                String tipo = partes[0];
                String placa = partes[1];
                String modelo = partes[2];
                double valorDiaria = Double.parseDouble(partes[3]);
                int extra = Integer.parseInt(partes[4]);
                if ("CARRO".equalsIgnoreCase(tipo)) {
                    listaVeiculos.add(new Carro(placa, modelo, valorDiaria, extra));
                } else if ("MOTO".equalsIgnoreCase(tipo)) {
                    listaVeiculos.add(new Moto(placa, modelo, valorDiaria, extra));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar veiculos: " + e.getMessage());
        }
    }

    private void carregarLocacoes() {
        Path path = Path.of(ARQ_LOCACOES);
        if (!Files.exists(path)) {
            return;
        }
        int maiorId = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length < 4) {
                    continue;
                }
                int id = Integer.parseInt(partes[0]);
                String cliente = partes[1];
                String placa = partes[2];
                int dias = Integer.parseInt(partes[3]);
                LocalDate dataInicio = LocalDate.now();
                if (partes.length >= 5) {
                    try {
                        dataInicio = LocalDate.parse(partes[4]);
                    } catch (Exception e) {
                        dataInicio = LocalDate.now();
                    }
                }
                Veiculo veiculo = buscarVeiculo(placa);
                if (veiculo != null) {
                    listaLocacoes.add(new Locacao(id, cliente, veiculo, dias, dataInicio));
                    maiorId = Math.max(maiorId, id);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar locacoes: " + e.getMessage());
        }
        proximoId = Math.max(proximoId, maiorId + 1);
    }
}
