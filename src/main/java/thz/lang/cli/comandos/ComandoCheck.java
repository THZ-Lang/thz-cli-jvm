package thz.lang.cli.comandos;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import thz.lang.ast.ProgramaAst;
import thz.lang.cli.CliHelper;
import thz.lang.cli.CliLogger;
import thz.lang.cli.CliErros;

public class ComandoCheck implements ComandoCli {

    @Override
    public List<String> nomes() {
        return List.of("check");
    }

    @Override
    public void executar(List<String> argumentos, boolean estrito) throws Exception {
        String arquivo = CliHelper.resolverArquivo(argumentos);
        if (arquivo == null || arquivo.isBlank() || !Files.exists(Path.of(arquivo))) {
            CliErros.erroArquivoNaoEncontrado(arquivo);
            return;
        }
        String fonte = Files.readString(Path.of(arquivo), StandardCharsets.UTF_8);

        if (arquivo.toLowerCase().endsWith(".cbl") || arquivo.toLowerCase().endsWith(".cob")) {
            try {
                var prog = thz.cobol.parser.CobolProcedureParser.parseProgram(fonte);
                CliLogger.info("[THZ CHECK] Programa COBOL validado com sucesso! PROGRAM-ID: " + prog.programId());
                return;
            } catch (Exception e) {
                CliLogger.erro("[THZ CHECK] Falha ao analisar programa COBOL: " + e.getMessage() + "\n");
                if (Boolean.getBoolean("thz.test.mode")) {
                    throw new IllegalStateException("Check COBOL falhou: " + e.getMessage());
                }
                System.exit(1);
                return;
            }
        }

        var analise = thz.lang.fachada.ThzCompilerFacade.analisar(fonte, estrito);
        if (analise.temErros()) {
            for (String bloco : analise.textoDiagnosticos())
                CliLogger.erro(bloco + "\n");
            CliErros.statusComandoCheck(analise.diagnosticos().size());
            if (Boolean.getBoolean("thz.test.mode")) {
                throw new IllegalStateException("Check falhou: " + analise.diagnosticos().size() + " erro(s).");
            }
            System.exit(1);
        }
        ProgramaAst ast = analise.ast();
        String versao = "";
        CliLogger.info("[THZ CHECK] Código validado com sucesso! AST íntegra para o programa: "
                + ast.nome() + versao + (estrito ? " [lint estrito aprovado]" : ""));
    }
}
