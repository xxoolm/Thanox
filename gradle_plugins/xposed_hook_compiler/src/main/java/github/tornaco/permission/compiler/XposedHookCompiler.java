package github.tornaco.permission.compiler;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.*;
import github.tornaco.permission.compiler.common.Logger;
import github.tornaco.permission.compiler.common.SettingsProvider;
import github.tornaco.xposed.annotation.XposedHook;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import java.util.*;

import static javax.lang.model.element.Modifier.*;

/**
 * Created by guohao4 on 2017/9/6.
 * Email: Tornaco@163.com
 */
@SupportedAnnotationTypes("github.tornaco.xposed.annotation.XposedHook")
@SupportedOptions("ModuleName")
public class XposedHookCompiler extends AbstractProcessor {

    private static final boolean DEBUG = true;
    private static final String ENV_KEY_MODULE_NAME = "ModuleName";

    private ErrorReporter mErrorReporter;
    private Types mTypes;

    private String registryModuleName;

    private final Set<XposedHookInfo> hooks = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mErrorReporter = new ErrorReporter(processingEnvironment);
        mTypes = processingEnvironment.getTypeUtils();

        Map<String, String> options = processingEnvironment.getOptions();
        for (String k : options.keySet()) {
            Logger.debug("processingEnvironment.getOptions() K: %s, V: %s", k, options.get(k));
        }
        registryModuleName = options.get(ENV_KEY_MODULE_NAME);
        Logger.debug("registryModuleName: %s", registryModuleName);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Collection<? extends Element> annotatedElements =
                roundEnvironment.getElementsAnnotatedWith(XposedHook.class);
        List<TypeElement> types = new ImmutableList.Builder<TypeElement>()
                .addAll(ElementFilter.typesIn(annotatedElements))
                .build();

        hooks.clear();
        for (TypeElement type : types) {
            processType(type);
        }

        if (!hooks.isEmpty()) {
            generateSourceFile();
            Logger.debug("process" + set + "@" + roundEnvironment);
        }

        return true;
    }

    private void generateSourceFile() {
        // class name
        String className = "XposedHookRegistry_" + registryModuleName.toUpperCase();
        String pkgName = "github.tornaco.xposed." + registryModuleName.toLowerCase();
        // Create source.
        String source = generateClass(pkgName, className);

        source = Reformatter.fixup(source);

        SourceFiles.writeSourceFile(processingEnv, pkgName + "." + className, source, null);
    }

    private String generateClass(String pkgName, String className) {
        TypeSpec.Builder subClass =
                TypeSpec
                        .classBuilder(className)
                        .addModifiers(FINAL, PUBLIC);

        ClassName IXposedHookZygoteInit = ClassName.get("de.robv.android.xposed", "IXposedHookZygoteInit");
        ClassName IXposedHookLoadPackage = ClassName.get("de.robv.android.xposed", "IXposedHookLoadPackage");
        // Sets.
        ClassName setClazz = ClassName.get("java.util", "Set");
        TypeName setOfIXposedHookZygoteInit = ParameterizedTypeName.get(setClazz, IXposedHookZygoteInit);
        TypeName setOfIXposedHookLoadPackage = ParameterizedTypeName.get(setClazz, IXposedHookLoadPackage);

        // Maps.
        ClassName mapClazz = ClassName.get("java.util", "Map");
        ClassName intClazz = ClassName.get("java.lang", "Integer");
        TypeName mapOfIXposedHookZygoteInit = ParameterizedTypeName.get(mapClazz, intClazz, setOfIXposedHookZygoteInit);
        TypeName mapOfIXposedHookLoadPackage = ParameterizedTypeName.get(mapClazz, intClazz, setOfIXposedHookLoadPackage);

        // All sdk.
        int[] allSdkVersion = XposedHook.SdkVersions.ALL;
        for (int sdkVersion : allSdkVersion) {
            subClass.addField(FieldSpec
                    .builder(setOfIXposedHookZygoteInit, "sIXposedHookZygoteInits" + sdkVersion, STATIC, PRIVATE, FINAL)
                    .initializer(CodeBlock.of("new $T<>()", HashSet.class))
                    .addJavadoc("IXposedHookZygoteInits of sdk version: " + sdkVersion)
                    .build());
            subClass.addField(FieldSpec
                    .builder(setOfIXposedHookLoadPackage, "sIXposedHookLoadPackages" + sdkVersion, STATIC, PRIVATE, FINAL)
                    .initializer(CodeBlock.of("new $T<>()", HashSet.class))
                    .addJavadoc("IXposedHookLoadPackages of sdk version: " + sdkVersion)
                    .build());
        }

        // Maps for sdks.
        subClass.addField(FieldSpec
                .builder(mapOfIXposedHookZygoteInit, "sIXposedHookZygoteInitsMap", STATIC, PRIVATE, FINAL)
                .initializer(CodeBlock.of("new $T<>()", HashMap.class))
                .addJavadoc("IXposedHookZygoteInits mapping")
                .build());
        subClass.addField(FieldSpec
                .builder(mapOfIXposedHookLoadPackage, "sIXposedHookLoadPackageMap", STATIC, PRIVATE, FINAL)
                .initializer(CodeBlock.of("new $T<>()", HashMap.class))
                .addJavadoc("IXposedHookLoadPackages mapping")
                .build());

        // initXposedHookInfos
        MethodSpec.Builder initXposedHookInfosMethodBuilder = MethodSpec.methodBuilder("initXposedHookInfos").addModifiers(PRIVATE, STATIC);
        for (XposedHookInfo info : hooks) {
            int[] currentHookVersions = info.getTargetSdkVersion();
            for (int currentHookVersion : currentHookVersions) {
                initXposedHookInfosMethodBuilder.addStatement(
                        "try {"
                                + "sIXposedHookZygoteInits"
                                + currentHookVersion
                                + ".add(new " +
                                info.getClassName() +
                                "());"
                                + "} catch(Throwable e) {throw new RuntimeException(e);}");

                initXposedHookInfosMethodBuilder.addStatement(
                        "try {"
                                + "sIXposedHookLoadPackages"
                                + currentHookVersion
                                + ".add(new " +
                                info.getClassName() +
                                "());"
                                + "} catch(Throwable e) {throw new RuntimeException(e);}");
            }
        }

        // Mapping.
        for (int sdkVersion : allSdkVersion) {
            initXposedHookInfosMethodBuilder.addComment("Adding to maps.");
            initXposedHookInfosMethodBuilder.addStatement("sIXposedHookZygoteInitsMap.put(" +
                    sdkVersion
                    + ", sIXposedHookZygoteInits" +
                    sdkVersion
                    + ")");
            initXposedHookInfosMethodBuilder.addStatement("sIXposedHookLoadPackageMap.put(" +
                    sdkVersion
                    + ", sIXposedHookLoadPackages" +
                    sdkVersion
                    + ")");
        }
        subClass.addMethod(initXposedHookInfosMethodBuilder.build());

        // Static
        subClass.addStaticBlock(CodeBlock.of(
                "initXposedHookInfos();\n"
        ));

        // Getter for IXposedHookZygoteInit
        subClass.addMethod(MethodSpec.methodBuilder("getXposedHookZygoteInitForSdk")
                .addJavadoc("Getter for IXposedHookZygoteInit")
                .returns(setOfIXposedHookZygoteInit)
                .addParameter(ParameterSpec.builder(TypeName.INT, "sdkVersion", FINAL).build())
                .addStatement("return sIXposedHookZygoteInitsMap.get(sdkVersion)")
                .addModifiers(PUBLIC, STATIC).build());

        // Getter for IXposedHookLoadPackage
        subClass.addMethod(MethodSpec.methodBuilder("getXposedHookLoadPackageForSdk")
                .addJavadoc("Getter for IXposedHookLoadPackage")
                .returns(setOfIXposedHookLoadPackage)
                .addParameter(ParameterSpec.builder(TypeName.INT, "sdkVersion", FINAL).build())
                .addStatement("return sIXposedHookLoadPackageMap.get(sdkVersion)")
                .addModifiers(PUBLIC, STATIC).build());


        JavaFile javaFile = JavaFile.builder(pkgName, subClass.build())
                .addFileComment(SettingsProvider.FILE_COMMENT)
                .skipJavaLangImports(true)
                .build();
        return javaFile.toString();
    }

    private void processType(TypeElement type) {
        XposedHook annotation = type.getAnnotation(XposedHook.class);
        if (annotation == null) {
            mErrorReporter.abortWithError("@XposedHook annotation is null on Type %s", type);
            return;
        }
        if (type.getKind() != ElementKind.CLASS) {
            mErrorReporter.abortWithError("@XposedHook" + " only applies to class", type);
        }

        NestingKind nestingKind = type.getNestingKind();
        if (nestingKind != NestingKind.TOP_LEVEL) {
            mErrorReporter.abortWithError("@XposedHook" + " only applies to top level class", type);
        }

        checkModifiersIfNested(type);

        XposedHookInfo xposedHookInfo = new XposedHookInfo(
                type.toString(),
                annotation.targetSdkVersion(),
                annotation.active());

        Logger.debug("XposedHook process: " + xposedHookInfo);
        if (xposedHookInfo.isActive()) {
            hooks.add(xposedHookInfo);
        }
    }

    private void checkModifiersIfNested(TypeElement type) {
        ElementKind enclosingKind = type.getEnclosingElement().getKind();
        if (enclosingKind.isClass() || enclosingKind.isInterface()) {
            if (type.getModifiers().contains(PRIVATE)) {
                mErrorReporter.abortWithError("@RuntimePermissions class must not be private", type);
            }
            if (!type.getModifiers().contains(STATIC)) {
                mErrorReporter.abortWithError("Nested @RuntimePermissions class must be static", type);
            }
        }
        // In principle type.getEnclosingElement() could be an ExecutableElement (for a class
        // declared inside a method), but since RoundEnvironment.getElementsAnnotatedWith doesn't
        // return such classes we won't see them here.
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

