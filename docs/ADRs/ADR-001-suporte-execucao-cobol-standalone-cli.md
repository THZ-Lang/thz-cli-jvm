# ADR-001: Suporte a Execução de Programas COBOL Standalone na CLI (`thz run` / `thz check`)

- **Status:** Aceito
- **Data:** 2026-10-11
- **Decisores:** Time de Arquitetura THZ-LANG & Lucas Thomaz
- **Contexto:** Ecossistema THZ-LANG (v0.5.0), módulo `thz-cli-jvm` e integração com `thz-lang-cobol`

---

## 1. Contexto e Problema

Com a introdução da Variação 3 da estratégia de modernização de legado (THZ-COBOL Standalone puro em arquivos `.cbl` e `.cob`), a extensão do VS Code e os desenvolvedores passaram a despachar a execução direta desses arquivos via comando `thz run <arquivo.cbl>`.

No entanto, o motor de execução da CLI (`ComandoRun.java`) assumia que todo arquivo de entrada era código-fonte THZ (`.thz` ou `.thzui`). Isso ocasionava dois problemas críticos:
1. `ThzCompilerFacade.parseAst(fonte)` tentava analisar a sintaxe COBOL usando o compilador nativo THZ, retornando `null` em decorrência de erros de sintaxe (como a divisão `PROGRAM-ID.`).
2. O código invocava `ast.tipoModulo()` sem verificação de nulidade, gerando `java.lang.NullPointerException` fatal em tempo de execução.
3. Não havia integração de classpath com o módulo `thz-lang-cobol` no Composite Build da CLI.

---

## 2. Decisão Arquitetural

1. **Integração de Módulo:**
   - Adicionado `includeBuild("../thz-lang-cobol")` ao `settings.gradle.kts`.
   - Adicionada dependência `implementation("thz.lang:thz-lang-cobol:$thzVersion")` em `build.gradle.kts` e empacotada no UberJAR `shadowJar`.

2. **Roteamento Especializado por Extensão:**
   - `ComandoRun`: Quando o arquivo alvo possuir extensão `.cbl` ou `.cob`, delega a execução diretamente ao motor `thz.cobol.CobolBridge.executarCobolPuro(fonte)`, imprimindo as saídas capturadas do `DISPLAY`.
   - `ComandoCheck`: Valida a integridade sintática e parágrafos do programa COBOL através de `CobolProcedureParser.parseProgram(fonte)`.
   - `ComandoIr`: Permite emitir LLVM IR nativo AOT (`--llvm`) para programas COBOL diretamente pela CLI via `CobolBridge.emitirLlvmIr(fonte)`.

3. **Resiliência e Eliminação de NullPointerException:**
   - Adicionado tratamento para quando `ast == null` em fontes `.thz`: em vez de quebrar com NPE, executa o analisador e exibe os diagnósticos formatados de erro do compilador com código de saída apropriado.
   - Refatorada a checagem de comandos conhecidos em `ThzCli.java` para utilizar `COMANDOS.containsKey(...)` dinâmico, evitando falso roteamento de comandos oficiais para o despachador de arquivos.

---

## 3. Consequências

- **Execução Nativa Direta:** Comandos como `thz run CALC_JUROS.cbl` e botões de CodeLens do VS Code executam diretamente com resultado numérico e saída exibidos perfeitamente.
- **Rastreabilidade e Segurança:** Elimina falhas catastróficas não tratadas (NPE) na interface de linha de comando.
- **Zero Regressão:** Todos os testes unitários e de integração da CLI mantidos verdes com 100% de sucesso.
