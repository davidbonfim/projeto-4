import java.time.LocalDate;

public class Locacao {
    private int id;
    private String nomeCliente;
    private Veiculo veiculo;
    private int dias;
    private LocalDate dataInicio;

    public Locacao(int id, String nomeCliente, Veiculo veiculo, int dias, LocalDate dataInicio) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.veiculo = veiculo;
        this.dias = dias;
        this.dataInicio = dataInicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataInicio.plusDays(dias - 1L);
    }

    public double calcularTotal() {
        return dias * veiculo.getValorDiaria();
    }

    @Override
    public String toString() {
        return "Locacao " + id + " - Cliente: " + nomeCliente + ", Placa: " + veiculo.getPlaca() + ", Dias: " + dias
                + ", Inicio: " + dataInicio + ", Fim: " + getDataFim() + ", Total: "
                + String.format("R$ %.2f", calcularTotal());
    }
}
