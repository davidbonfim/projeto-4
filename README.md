# Sistema de Locadora de Veículos (Java)

Aplicação em Java orientada a objetos para cadastro e locação de veículos, com persistência em arquivos texto, interface de console (menu principal) e interface gráfica (Swing).

## Estrutura
- `Main.java`: entrada do modo console (menu principal).
- `src/MainGUI.java` + `src/LocadoraUI.java`: interface gráfica Swing.
- `src/SistemaLocadora.java`: lógica de negócio, CRUD e persistência.
- `src/Veiculo.java`, `src/Carro.java`, `src/Moto.java`: hierarquia de veículos (herança).
- `src/Locacao.java`: associação entre cliente e veículo.
- Arquivos de dados: `veiculos.txt`, `locacoes.txt`.

## Funcionalidades
- Cadastrar/listar/remover veículos (carro ou moto).
- Cadastrar/listar/remover locações (com datas de início/fim, validação de disponibilidade).
- Persistência automática em arquivos texto (CSV com `;`).
- Interface console e interface gráfica.

## Requisitos
- Java 17+ (ou 11+, desde que `java.time` esteja disponível).

## Como compilar e executar
No diretório do projeto:
```bash
# Console
javac -cp src Main.java
java -cp .:src Main   # no Windows use .;src

# Interface gráfica
javac src/*.java      # se precisar recompilar
java -cp src MainGUI
```

## Formato dos arquivos
- `veiculos.txt`: `TIPO;PLACA;MODELO;VALOR;EXTRA`
  - Ex: `CARRO;ABC-1234;Uno;100.0;4`
  - Ex: `MOTO;XYZ-9090;CB500;200.0;500`
- `locacoes.txt`: `ID;CLIENTE;PLACA;DIAS;DATA_INICIO`
  - Ex: `1;Joao;ABC-1234;3;2024-01-10`

## Observações
- Não é permitido remover veículo que tenha locações associadas (evita órfãos).
- A verificação de disponibilidade impede sobreposição de datas para a mesma placa.
