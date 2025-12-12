public class Carro extends Veiculo {
    private int numeroPortas;

    public Carro(String placa, String modelo, double valorDiaria, int numeroPortas) {
        super(placa, modelo, valorDiaria);
        this.numeroPortas = numeroPortas;
    }

    public int getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(int numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    @Override
    public String toString() {
        return "Carro - " + super.toString() + ", Portas: " + numeroPortas;
    }
}
