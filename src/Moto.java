public class Moto extends Veiculo {
    private int cilindradas;

    public Moto(String placa, String modelo, double valorDiaria, int cilindradas) {
        super(placa, modelo, valorDiaria);
        this.cilindradas = cilindradas;
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    @Override
    public String toString() {
        return "Moto - " + super.toString() + ", Cilindradas: " + cilindradas;
    }
}
