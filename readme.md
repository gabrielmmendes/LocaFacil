# 🚗 Loca Fácil

> Sistema de Gestão de Locadora de Veículos  
> “Pouca burocracia, muita facilidade.”

---

## 📖 Descrição

As locadoras de veículos ainda dependem muito de controles manuais (planilhas, agendas de papel), o que causa:

- Duplicidade de reservas para a mesma data  
- Controle ineficiente da disponibilidade  
- Falta de histórico rápido de locações  
- Dificuldade em calcular multas e faturamento  
- Baixa rastreabilidade do processo  

**Loca Fácil** resolve isso com um sistema simples e funcional para:

- Cadastrar e gerenciar veículos e clientes  
- Criar e controlar contratos de locação (reserva, retirada, devolução)  
- Calcular valores diários, multas e extras  
- Emitir relatórios básicos de disponibilidade e faturamento :contentReference[oaicite:0]{index=0}

---

## 🚀 Escopo

1. **Gestão de Veículos**  
   - Cadastro, consulta, atualização e remoção (placa, modelo, categoria, cor, ano, tarifa, status) :contentReference[oaicite:1]{index=1}  
2. **Gestão de Clientes**  
   - Cadastro, consulta, atualização e remoção (nome, CPF/CNPJ, telefone, e-mail, CNH) :contentReference[oaicite:2]{index=2}  
3. **Contratos de Locação**  
   - Reserva: verifica disponibilidade e calcula valor parcial :contentReference[oaicite:3]{index=3}  
   - Check-out: registra retirada e altera status  
   - Check-in: registra devolução, calcula multas/extras e encerra contrato :contentReference[oaicite:4]{index=4}  
4. **Relatórios**  
   - Veículos disponíveis vs. alugados  
   - Locações ativas  
   - Histórico de cliente  
   - Faturamento em período :contentReference[oaicite:5]{index=5}  

---

## 📋 Requisitos Funcionais (R.F.x)

- **R.F.1** Cadastro de Veículos (R.F.1.1…R.F.1.4)  
- **R.F.2** Cadastro de Clientes (R.F.2.1…R.F.2.4)  
- **R.F.3** Criar Contrato de Locação (R.F.3.1…R.F.3.4)  
- **R.F.4** Registrar Retirada (Check-Out) (R.F.4.1…R.F.4.3)  
- **R.F.5** Registrar Devolução (Check-In) (R.F.5.1…R.F.5.3)  
- **R.F.6** Gerar Relatórios (R.F.6.1…R.F.6.5)  
- **R.F.7** Gerenciar Status de Veículos (R.F.7.1…R.F.7.2)  
- **R.F.8** Cancelar Contrato (R.F.8.1…R.F.8.2) :contentReference[oaicite:6]{index=6}

---

## 🎬 Casos de Uso (UCx)

Atores: **Atendente** e **Cliente**  
- **UC1** – Gerenciar Veículos  
- **UC2** – Gerenciar Clientes  
- **UC3** – Criar Reserva/Contrato  
- **UC4** – Registrar Retirada (Check-Out)  
- **UC5** – Registrar Devolução (Check-In)  
- **UC6** – Gerar Relatórios  
- **UC7** – Gerenciar Status de Veículos  
- **UC8** – Cancelar Contrato :contentReference[oaicite:7]{index=7}

---

## 📐 Modelo Conceitual

Principais classes:  
- **Cliente** (1)──(0..*) **ContratoLocacao**──(1) **Veículo**  
- **ContratoLocacao** (1)──(0..*) **MultaExtra**  
- Opcional: **Pagamento**, **CategoriaVeiculo**, **Endereço**

---

## 📦 Diagrama de Classes

[Diagrama de Classes](https://docs.google.com/document/d/14D2YnpXCNEougdbkzA2vOsfAeqe4p0DKQ0tbKV-V_p8/edit?usp=sharing)

> Veja `docs/diagrama-classes.png` para detalhes de atributos, métodos e relacionamentos :contentReference[oaicite:8]{index=8}

---

## 📦 Modelo entidade-relacionamento de forma gráfica (DER)

[🔗 Acessar DER no Lucidchart](https://lucid.app/lucidchart/6b240376-6d87-4e57-b3a1-d03a7d58316e/edit?viewport_loc=-1806%2C-830%2C4037%2C1978%2C0_0&invitationId=inv_8e834b23-3ac3-4963-9556-def1901aabca)

---

## 🛠️ Tecnologias

- **Java 21+**  
- **Maven** (ou **Gradle**)  
- **JUnit 4** para testes unitários  

---

## ▶️ Como Rodar

1. Clone este repositório  
   ```bash
   git clone https://github.com/SEU_USUARIO/loca-facil.git
   cd loca-facil
