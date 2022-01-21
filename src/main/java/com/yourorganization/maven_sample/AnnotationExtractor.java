package com.yourorganization.maven_sample;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;

public class AnnotationExtractor {
    public static void main(String[] args) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(AnnotationExtractor.class).resolve("src/main/java"));
        SourceRoot.Callback callback = (localPath, absolutePath, result) -> {
            result.ifSuccessful(cu -> {
                cu.accept(new VoidVisitorAdapter<Path>() {
                    @Override
                    public void visit(MarkerAnnotationExpr e, Path arg) {
                        innerVisit(e, arg);
                        super.visit(e, arg);
                    }

                    @Override
                    public void visit(NormalAnnotationExpr e, Path arg) {
                        innerVisit(e, arg);
                        super.visit(e, arg);
                    }

                    @Override
                    public void visit(SingleMemberAnnotationExpr e, Path arg) {
                        innerVisit(e, arg);
                        super.visit(e, arg);
                    }

                    private void innerVisit(AnnotationExpr e, Path arg) {
                        e.getRange().ifPresent(range -> {
                            System.out.printf("%s:%d:@%s\n", arg.toString(), range.begin.line, e.getName().asString());
                        });
                    }
                }, localPath);
            });
            return SourceRoot.Callback.Result.DONT_SAVE;
        };
        sourceRoot.parse("", callback);
    }
}
