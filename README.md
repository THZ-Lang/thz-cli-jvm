# thz-cli — CLI Unificada, Dev Server, REPL e Ferramentas (Java 25)

Ponto de entrada oficial de linha de comando, servidor de desenvolvimento com *live reload*, REPL interativo, despachante de compilação e orquestrador de ferramentas da linguagem THZ-LANG. Consome o núcleo [`thz-core-jvm`](../thz-core-jvm) e gera o UberJAR executável utilizado para empacotamento (`jpackage`) e compilação nativa (GraalVM Native Image).

---

## 📋 Comandos Suportados (17 Comandos Oficiais)

| Comando | Descrição | Exemplo de Uso |
| :--- | :--- | :--- |
| **`check`** | Análise estática completa (léxico, sintático, semântico e tipos) | `thz check pedido.thz --estrito` |
| **`run`** | Executa programa interpretado ou função `main`/`Principal` | `thz run app.thz` |
| **`dev`** | Servidor de desenvolvimento com Live Reload automático ao salvar | `thz dev faturamento.thz` |
| **`audit`** | Matriz de governança viva e rastreabilidade (com suporte a `--git`) | `thz audit pedido.thz --git` |
| **`release`** / **`liberar`** | Protocolo Oficial de Liberação com homologação e assinatura SHA-256 | `thz release faturamento.thz --dados homolog.json` |
| **`agent`** | Inicia o Agente Autônomo de Codificação e RAG em terminal | `thz agent --modelo llama3` |
| **`init`** | Inicializa projeto criando manifesto canônico `thz.config.json` | `thz init meu_projeto` |
| **`compile`** | Dispara o pipeline de compilação AOT (LLVM IR / Clang) | `thz compile app.thz` |
| **`compile-all`** | Compilação em lote de todos os fontes do workspace | `thz compile-all` |
| **`fmt`** | Formatador canônico idempotente de código-fonte | `thz fmt --escrever pedido.thz` |
| **`doc`** | Geração de documentação técnica viva em Markdown e Mermaid | `thz doc pedido.thz --saida docs/` |
| **`ui`** | Renderização e compilação de telas declarativas (`.thzui`) em HTML5 | `thz ui tela.thzui --html` |
| **`ir`** | Emissão de representação intermediária `thz-ir/1` e LLVM IR (`--llvm`) | `thz ir app.thz --llvm` |
| **`ast`** | Exportação estruturada da Árvore de Sintaxe Abstrata (AST) em JSON | `thz ast app.thz` |
| **`livro`** / **`manual`** | Compilação de todos os manuais técnicos em PDF unificado | `thz livro --saida dist/MANUAL.pdf` |
| **`repl`** | Shell interativo multi-linha (.ajuda, .codigo, .limpar, .sair) | `thz repl` |
| **`gui`** | Lança a Desktop IDE nativa Swing FlatLaf | `thz gui` |

---

## 🛠️ Execução via Scripts e Gradle

A partir da raiz do repositório:
```bash
# Execução direta via Gradle
./gradlew cli --args="check exemplos/faturamento.thz"
./gradlew cli --args="run exemplos/gestao_pedidos_moderno.thz"
./gradlew cli --args="agent"

# Execução via scripts auxiliares multiplataforma
./scripts/run.sh run exemplos/faturamento.thz          # Linux / macOS
.\scripts\run.ps1 run exemplos\faturamento.thz        # Windows
```

---

## ⚡ Compilação Nativa AOT (GraalVM)

O `thz-cli-jvm` pode ser compilado diretamente em binário nativo autônomo com tempo de inicialização inferior a 5ms:

```powershell
powershell.exe -ExecutionPolicy Bypass -File JVM/thz-cli-jvm/scripts/build-native.ps1 -PularTestes
```

Gera o executável nativo autônomo `dist/bin/thz.exe`.

---

## 📦 Dependência do Core

```kotlin
implementation("thz.lang:thz-core:0.4.0")
```
Resolvido via Gradle Composite Build a partir de `../thz-core-jvm`.
