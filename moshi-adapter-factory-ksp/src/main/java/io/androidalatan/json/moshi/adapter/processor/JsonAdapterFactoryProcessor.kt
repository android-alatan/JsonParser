package io.androidalatan.json.moshi.adapter.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.util.Locale

class JsonAdapterFactoryProcessor(
    private val codeGenerator: CodeGenerator,
    @Suppress("unused") private val logger: KSPLogger,
    private val moduleName: String
) : SymbolProcessor {

    private val targets = mutableListOf<KSClassDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols =
            resolver.getSymbolsWithAnnotation("com.squareup.moshi.JsonClass")

        symbols
            .filter { ksAnnotated ->
                ksAnnotated is KSClassDeclaration && ksAnnotated.validate()
            }
            .forEach { ksAnnotated ->
                ksAnnotated.accept(JsonClassAnnotationVisitor(targets), Unit)
            }

        return emptyList()
    }

    override fun finish() {

        val moduleName = moduleName.split("-")
            .fold(StringBuilder()) { builder, text ->
                builder.append(text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() })
            }

        val shortestPkgName = targets.fold("") { shortenPkgName, ksClassDeclaration ->
            val currentPkgName = ksClassDeclaration.packageName.asString()
            if (shortenPkgName.isEmpty()) {
                return@fold currentPkgName
            }

            if (shortenPkgName.length < currentPkgName.length) {
                shortenPkgName
            } else {
                currentPkgName
            }
        }

        if (shortestPkgName.isEmpty()) return

        val fileSpec = FileSpec.builder(shortestPkgName, "${moduleName}JsonAdapterFactory")
            .addImport("com.squareup.moshi", "JsonAdapter", "Moshi", "Types")
            .addImport("java.lang.reflect", "Type")
            .addImport("io.androidalatan.json.moshi.adapter", "JsonAdapterFactory")
            .addImport("io.androidalatan.json.moshi.adapter.internal", "TypeUtil")
            .let { builder ->
                targets.map { declaration -> declaration.packageName.asString() to declaration.simpleName.asString() }
                    .fold(builder) { fileSpec, (packageName, className) ->
                        fileSpec.addImport(packageName, className)
                    }
            }
            .addType(
                TypeSpec.classBuilder("${moduleName}JsonAdapterFactory")
                    .addSuperinterface(ClassName("io.androidalatan.json.moshi.adapter", "JsonAdapterFactory"))
                    .addFunction(
                        FunSpec.builder("create")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("type", Type::class)
                            .addParameter(
                                "annotations",
                                MutableSet::class.asClassName()
                                    .parameterizedBy(Annotation::class.asClassName())
                            )
                            .addParameter(
                                ParameterSpec.builder("moshi", ClassName("com.squareup.moshi", "Moshi"))
                                    .build()
                            )
                            .returns(
                                ClassName("com.squareup.moshi", "JsonAdapter").parameterizedBy(listOf(STAR))
                                    .copy(nullable = true)
                            )
                            .addCode(buildCodeBlock {
                                addStatement(
                                    "if (annotations.isNotEmpty()) return null"
                                )
                                addStatement("return when (TypeUtil.getRawType(type)) {")
                                indent()
                                kotlin.run {
                                    targets.map { target ->
                                        "${target.simpleName.asString()}::class.java -> ${target.simpleName.asString()}JsonAdapter(moshi)"
                                    }
                                        .forEach { state ->
                                            addStatement(state)
                                        }
                                }
                                addStatement("else -> null")
                                unindent()
                                addStatement("}")
                            })
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("acceptable")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("originType", Type::class.java)
                            .returns(Boolean::class.java)
                            .addCode(buildCodeBlock {
                                addStatement("val type = TypeUtil.getRawType(originType)")
                                    .run {
                                        targets.map { it.simpleName.asString() }
                                            .forEachIndexed { index, className ->
                                                if (index == 0) {
                                                    addStatement("return ${className}::class.java == type")
                                                    indent()
                                                } else if (index != 0) {
                                                    addStatement(" || ${className}::class.java == type")
                                                }
                                            }
                                    }
                                unindent()
                            })
                            .build()
                    )
                    .build()
            )
            .build()

        val outputStream = codeGenerator.createNewFile(
            Dependencies(
                true,
                *targets.map { it.containingFile!! }
                    .toTypedArray()
            ),
            fileSpec.packageName,
            fileSpec.name
        )
        OutputStreamWriter(outputStream, "UTF-8")
            .use { fileSpec.writeTo(it) }
    }

    class JsonClassAnnotationVisitor(private val targets: MutableList<KSClassDeclaration>) : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

            classDeclaration.annotations.firstOrNull { annotation ->
                annotation.shortName.asString() == "JsonClass"
            }?.arguments?.firstOrNull { argument ->
                argument.name?.asString() == "generateAdapter" && argument.value == true
            }
                ?.let {
                    targets.add(classDeclaration)
                }
        }
    }
}